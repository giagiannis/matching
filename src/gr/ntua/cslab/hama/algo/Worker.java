package gr.ntua.cslab.hama.algo;

import gr.ntua.cslab.hama.containers.Person;
import gr.ntua.cslab.hama.containers.PersonList;
import gr.ntua.cslab.hama.data.DataReader;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.bsp.BSP;
import org.apache.hama.bsp.BSPPeer;
import org.apache.hama.bsp.sync.SyncException;

/**
 * Class which is executed by each task. In this class the logic of the SMA algorithm is implemented.
 * @author Giannis Giannakopoulos
 *
 */
public abstract class Worker extends
		BSP<NullWritable, NullWritable, Text, Text, Message> {

	protected BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer;
	
	// configuration fields
	private static boolean 	DEBUG=false,
							DIAGNOSTICS=false;
	
	
	private PersonList men,women;
	
	private int datasetSize;
	private String[] peerNames;
	private int workers;

	protected int stepCounter=0;
	
	public Worker() {
		
	}
	
	@Override
	public void setup(
			BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer)
			throws IOException, SyncException, InterruptedException {
		super.setup(peer);
		this.peer=peer;
		DataReader reader=null;
		if(peer.getConfiguration().getBoolean("ring", false)){
			peer.write(new Text("Dataset size"), new Text(((Integer)(peer.getConfiguration().getInt("datasetSize", -1))).toString()));
			reader = new DataReader(peer.getConfiguration().getInt("datasetSize", -1), peer.getNumPeers(), peer.getPeerIndex());
			this.men = reader.getPeople();
			peer.write(new Text("men"), new Text(this.men.toString()));
			new DataReader(peer.getConfiguration().getInt("datasetSize", -1), peer.getNumPeers(), peer.getPeerIndex());
			this.women = reader.getPeople();
			peer.write(new Text("women"), new Text(this.women.toString()));
		} else {
			reader = new DataReader(peer.getConfiguration().get("men"), peer.getNumPeers(), peer.getPeerIndex());
			this.men = reader.getPeople();
			reader = new DataReader(peer.getConfiguration().get("women"), peer.getNumPeers(), peer.getPeerIndex());
			this.women = reader.getPeople();
			peer.write(new Text("men"), new Text(this.men.toString()));
			peer.write(new Text("women"), new Text(this.women.toString()));
		}
		this.datasetSize=reader.getDatasetSize();
		this.workers = peer.getNumPeers();
		this.peerNames = peer.getAllPeerNames();
		
	}
	
	@Override
	public void bsp(
			BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer)
			throws IOException, SyncException, InterruptedException {
		PersonList proposers, acceptors;
		
		boolean control=true;
		
		while(control){
			this.stepCounter++;
			this.peer.getCounter("ALGORITHM_COUNTERS", "STEPS").increment(1);
			
			if(this.menPropose()){
				proposers=this.men;
				acceptors=this.women;
			} else {
				proposers=this.women;
				acceptors=this.men;
			}
			
			long start=System.currentTimeMillis();
			this.proposalStep(proposers, peer);				//discrete superstep
			if(DIAGNOSTICS)peer.write(new Text("Proposal  step"), new Text(new Long(System.currentTimeMillis()-start).toString()));
			start=System.currentTimeMillis();
			this.proposalEvaluationStep(acceptors, peer);	//discrete superstep
			if(DIAGNOSTICS)peer.write(new Text("Prop Eval step"), new Text(new Long(System.currentTimeMillis()-start).toString()));
			start=System.currentTimeMillis();
			this.statusNotification(proposers, peer);		//discrete superstep
			if(DIAGNOSTICS)peer.write(new Text("Statu Not step"), new Text(new Long(System.currentTimeMillis()-start).toString()));
			start=System.currentTimeMillis();
			this.statusNotification(acceptors, peer);		//discrete superstep
			if(DIAGNOSTICS)peer.write(new Text("Statu Not step"), new Text(new Long(System.currentTimeMillis()-start).toString()));
			start=System.currentTimeMillis();
			control=this.singlePeopleExist(peer);
			if(DIAGNOSTICS)peer.write(new Text("Control step"), new Text(new Long(System.currentTimeMillis()-start).toString()));
			
			
			if(DEBUG)peer.write(new Text("proposer status"), new Text(proposers.toString()));
			if(DEBUG)peer.write(new Text("acceptor status"), new Text(acceptors.toString()));
		}
	}
	
	@Override
	public void cleanup(
			BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer)
			throws IOException {
		super.cleanup(peer);
	}
	
	private void proposalStep(PersonList proposers, BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer) throws IOException, SyncException, InterruptedException{
		if(DEBUG)peer.write(new Text("Making the proposals"), new Text());
		Iterator<Person> it = this.getIterator(proposers);
		while(it.hasNext()){
			Person p=it.next();
			int desiredId=p.getPreferences().getNext();
			Message m=new Message(Message.PROPOSAL_MESSAGE, p.getId(), desiredId);
			peer.send(this.getWorkerForId(desiredId), m);
			if(DEBUG) peer.write(new Text(""), new Text(p.getId()+" proposed to "+m.getDestination()));
		}
		peer.sync();
	}

	private void proposalEvaluationStep(PersonList acceptors, BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer) throws IOException, SyncException, InterruptedException{
		Iterator<Person> it;
		if(DEBUG)peer.write(new Text("Proposal evaluation ("+peer.getNumCurrentMessages()+" messages)"), new Text(""));
		while(peer.getNumCurrentMessages()>0){
			Message m = peer.getCurrentMessage();
			if(m.getType()==Message.PROPOSAL_MESSAGE)
				acceptors.get(m.getDestination()).addCandidate(m.getSource());
		}
		
		it = acceptors.getIterator();
		int count=0;
		while(it.hasNext()){
			Person p = it.next();
			int oldPartner=p.reviewProposals();
			if(oldPartner!=Person.NO_MARRIAGE) {
				peer.send(this.getWorkerForId(p.getPartnerId()), new Message(Message.ACCEPTANCE_MESSAGE, p.getId(), p.getPartnerId()));
				if(DEBUG) peer.write(new Text(""), new Text(p.getId()+" accepted "+p.getPartnerId()));
				count++;
			}
			if(oldPartner!=Person.NOT_EXIST && oldPartner!=Person.NO_MARRIAGE){
				peer.send(this.getWorkerForId(oldPartner), new Message(Message.DIVORCE_MESSAGE, p.getId(), oldPartner));
			}
		}
		if(DEBUG)peer.write(new Text("In the step "+count+" marriages were made"), new Text());
		peer.sync();
	}
	
	private void statusNotification(PersonList actors, BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer) throws IOException, SyncException, InterruptedException{
		if(DEBUG)peer.write(new Text("Divorce and marriage messages are been broadcasted"), new Text("--"+peer.getNumCurrentMessages()+" messages--"));
		while(peer.getNumCurrentMessages()>0){
			Message m = peer.getCurrentMessage();
			Person p=actors.get(m.getDestination());
			if(m.getType()==Message.DIVORCE_MESSAGE){
				if(p.getPartnerId()==m.getSource()){
					p.divorce();
					if(DEBUG) peer.write(new Text(""), new Text(p.getId()+" i was divorced by "+m.getSource()));
				}
				if(DEBUG)peer.write(new Text(p.getId()+" is now alone"), new Text());
			} if (m.getType()==Message.ACCEPTANCE_MESSAGE) {
				if(DEBUG) peer.write(new Text(""), new Text(p.getId()+" agreed to marry me "+m.getSource()));
				if(p.isMarried()){
					peer.send(this.getWorkerForId(p.getPartnerId()), new Message(Message.DIVORCE_MESSAGE,p.getId(),p.getPartnerId()));
					p.divorce();
				}
				p.marry(m.getSource());
			}
		}
		peer.sync();
	}
	
	private boolean singlePeopleExist(BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer) throws IOException, SyncException, InterruptedException{
		if(DEBUG) peer.write(new Text("Has more people"), new Text("Dubug"));
		//for(String p:peer.getAllPeerNames())
		peer.send(peer.getAllPeerNames()[0], new Message(Message.SINGLES_COUNT_MESSAGE, this.men.hasSinglePeople()));
		peer.sync();
		if(peer.getPeerName().equals(peer.getAllPeerNames()[0])) {
			Boolean result=false;
			while(peer.getNumCurrentMessages()>0){
				Message m = peer.getCurrentMessage();
				result=result || m.hasSinglePeople();
				if(result)
					break;
			}
			for(String s:peer.getAllPeerNames())
				peer.send(s, new Message(Message.SINGLES_COUNT_MESSAGE,result));
		} 
		peer.sync();
		Boolean result=false;
		while(peer.getNumCurrentMessages()>0){
			Message m=peer.getCurrentMessage();
			result=m.hasSinglePeople();
		}

//		peer.sync();
		if(DEBUG)peer.write(new Text("Has more people"), new Text(result.toString()));
		return result;
	}

	private String getWorkerForId(int id){
		int div=id/(this.datasetSize/workers), mod = id%(this.datasetSize/workers);
		int index=div;
		if(mod==0 && index>0)
			index-=1;
		if(index>=this.peerNames.length)
			index=this.peerNames.length-1;
		System.err.println(id+","+index);
		return this.peerNames[index];
	}

	/**
	 * Method implemented by each specific algorithm. This method returns the Iterator of the proposers 
	 * (single iterator, unhappy iterator, etc.).
	 * @param proposers
	 * @return
	 */
	protected abstract Iterator<Person> getIterator(PersonList proposers);
	
	/**
	 * Method used as a criterion to choose men or women as a proposer.
	 * @return
	 */
	protected abstract boolean menPropose();
}

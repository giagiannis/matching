package gr.ntua.cslab.hama.algo;

import gr.ntua.cslab.hama.containers.Person;
import gr.ntua.cslab.hama.containers.PersonList;
import gr.ntua.cslab.hama.data.DataReader;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.BSP;
import org.apache.hama.bsp.BSPJob;
import org.apache.hama.bsp.BSPPeer;
import org.apache.hama.bsp.NullInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.bsp.sync.SyncException;

public abstract class AbstractAlgorithm extends BSP<NullWritable, NullWritable, Text, Text, Message>{

	// configuration fields
	private static boolean 	DEBUG=false,
							DIAGNOSTICS=false;
	
	private static boolean	verbose=true;
	
	private int workers=2;
	private String dataMen, dataWomen;
	private int inputSize;

	private long executionTime;
	
	// per worker fields
	
	private PersonList men,women;
	
	private int datasetSize;
	private String[] peerNames;

	public AbstractAlgorithm() {
		
	}
	
	@Override
	public void setup(
			BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer)
			throws IOException, SyncException, InterruptedException {
		super.setup(peer);
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

		PersonList proposers=this.men, acceptors=this.women;
		boolean control=true;
		while(control){
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
			start=System.currentTimeMillis();
			
			if(DEBUG)peer.write(new Text("men status"), new Text(proposers.toString()));
			if(DEBUG)peer.write(new Text("women status"), new Text(acceptors.toString()));
		}
		
	}
	
	private void proposalStep(PersonList proposers, BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer) throws IOException, SyncException, InterruptedException{
		if(DEBUG)peer.write(new Text("Making the proposals"), new Text());
		Iterator<Person> it = proposers.getUnhappyIterator();
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
		// FIXME must be changed!!!
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
	
	@Override
	public void cleanup(
			BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer)
			throws IOException {
		super.cleanup(peer);
	}

	public String getWorkerForId(int id){
		int div=id/(this.datasetSize/workers), mod = id%(this.datasetSize/workers);
		int index=div;
		if(mod==0 && index>0)
			index-=1;
		if(index>=this.peerNames.length)
			index=this.peerNames.length-1;
		System.err.println(id+","+index);
		return this.peerNames[index];
	}

	
	// CONFIGURATION METHODS
	// used to configure the hama job
	
	public void setInput(String men, String women){
		this.dataMen=men;
		this.dataWomen=women;
	}
	
	public void setInput(int datasetSize){
		this.inputSize=datasetSize;
	}
	
	public void setNumberOfWorkers(int workers){
		this.workers=workers;
	}
	
	public void run() throws IOException, ClassNotFoundException, InterruptedException{
		BSPJob job = new BSPJob(new HamaConfiguration(),DemoApp.class);
		job.setJobName("SMA");
		if(this.inputSize>0){
			job.getConfiguration().setBoolean("ring", true);
			job.getConfiguration().setInt("datasetSize", this.inputSize);
		} else {
			job.getConfiguration().setBoolean("ring", false);
			job.getConfiguration().set("men", this.dataMen);
			job.getConfiguration().set("women", this.dataWomen);
		}
		job.setBspClass(DemoApp.class);
		job.setJarByClass(AbstractAlgorithm.class);
		
		job.setInputFormat(NullInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		job.setOutputPath(new Path("temp_hama_job"));
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setNumBspTask(this.workers);
		long startTime=System.currentTimeMillis();
		job.waitForCompletion(verbose);
		this.executionTime = System.currentTimeMillis()-startTime;
	}
	
	public long getExecutionTime(){
		return this.executionTime;
	}
	
	public static void setVerbose(boolean flag){
		AbstractAlgorithm.verbose=flag;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		AbstractAlgorithm demo = new DemoApp();
		
		try {
			demo.setInput(new Integer(args[0]));
			if(args.length>1){
				demo.setNumberOfWorkers(new Integer(args[1]));
			}
			System.err.println("Using ring preferences with size:\t"+new Integer(args[0]));
		} catch (NumberFormatException e){
			demo.setInput(args[0], args[1]);
			if(args.length>2){
				demo.setNumberOfWorkers(new Integer(args[2]));
			}
			System.err.println("Using in memory preferences:\t"+args[0]+", "+args[1]);
		} 
		AbstractAlgorithm.setVerbose(false);
		demo.run();
		System.out.println(demo.getExecutionTime()/1000.0);
	}

}

class DemoApp extends AbstractAlgorithm {
	public DemoApp() {
		super();
//		System.err.println("Hello world");
	}
}

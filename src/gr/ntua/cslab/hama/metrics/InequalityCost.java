package gr.ntua.cslab.hama.metrics;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.bsp.BSPPeer;
import org.apache.hama.bsp.sync.SyncException;

import gr.ntua.cslab.hama.algo.Message;
import gr.ntua.cslab.hama.containers.PersonList;

public class InequalityCost extends Metrics {

	public InequalityCost(
			BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer,
			PersonList men, PersonList women) {
		super(peer, men, women);
	}

	@Override
	public double get() throws IOException, SyncException, InterruptedException {
		int medianRank=InequalityCost.getMedianRankOfHappiness(this.men, this.women);		// sending the local rank
		this.peer.send(this.masterPeer, new Message(medianRank));
		this.peer.sync();
		if(this.peer.getPeerName().equals(this.masterPeer)){
			LinkedList<Double> medians = new LinkedList<Double>();
			while(this.peer.getNumCurrentMessages()>0){
				Message m =this.peer.getCurrentMessage();
				medians.add(m.getMetricValue());
			}
			Collections.sort(medians);
			double globalMedian = medians.get(medians.size()/2);
			for(String s:this.peer.getAllPeerNames()){
				this.peer.send(s, new Message(globalMedian));
			}
		}
		this.peer.sync();
		double median=0;
		while(this.peer.getNumCurrentMessages()>0){
			Message m =this.peer.getCurrentMessage();
			median=m.getMetricValue();
		}
		int diff = InequalityCost.countPeopleAboveRank(this.men, median) - InequalityCost.countPeopleAboveRank(this.women, median);
		peer.send(this.masterPeer, new Message(diff));
		this.peer.sync();
		if(this.peer.getPeerName().equals(this.masterPeer)){
			double sum=0.0;
			while(this.peer.getNumCurrentMessages()>0){
				Message m =this.peer.getCurrentMessage();
				sum+=m.getMetricValue();
			}
			for(String s:this.peer.getAllPeerNames()){
				this.peer.send(s, new Message(sum));
			}
		}
		this.peer.sync();
		double finalVal=0.0;
		while(this.peer.getNumCurrentMessages()>0){
			Message m =this.peer.getCurrentMessage();
			finalVal=m.getMetricValue();
		}
		
		return finalVal;
	}

}

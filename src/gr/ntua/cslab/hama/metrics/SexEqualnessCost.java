package gr.ntua.cslab.hama.metrics;

import gr.ntua.cslab.hama.algo.Message;
import gr.ntua.cslab.hama.containers.PersonList;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.bsp.BSPPeer;
import org.apache.hama.bsp.sync.SyncException;

public class SexEqualnessCost extends Metrics {

	public SexEqualnessCost(
			BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer,
			PersonList men, PersonList women) {
		super(peer, men, women);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double get() throws IOException, SyncException, InterruptedException {
		double average=0.0;
		average+=(EgalitarianCost.getRanksAverage(this.men)-EgalitarianCost.getRanksAverage(this.women))/2.0;
		this.peer.send(this.masterPeer, new Message(average));
		this.peer.sync();
		if(this.peer.getPeerName().equals(this.masterPeer)){
			double sum=0.0;
			int count=0;
			while(this.peer.getNumCurrentMessages()>0){
				Message m = this.peer.getCurrentMessage();
				sum+=m.getMetricValue();
				count++;
			}
			sum=sum/count;
			for(String s:this.peer.getAllPeerNames()){
				this.peer.send(s, new Message(sum));
			}
		}
		double val=0.0;
		this.peer.sync();
		while(this.peer.getNumCurrentMessages()>0){
			Message m = this.peer.getCurrentMessage();
			val = m.getMetricValue();
		}

		return val;	}

}

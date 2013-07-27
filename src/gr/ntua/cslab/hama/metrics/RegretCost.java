package gr.ntua.cslab.hama.metrics;

import java.io.IOException;

import gr.ntua.cslab.hama.algo.Message;
import gr.ntua.cslab.hama.containers.PersonList;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.bsp.BSPPeer;
import org.apache.hama.bsp.sync.SyncException;

public class RegretCost extends Metrics {

	public RegretCost(
			BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer,
			PersonList men, PersonList women) {
		super(peer, men, women);
	}

	@Override
	public  double get() throws IOException, SyncException, InterruptedException {
		double val1= RegretCost.getMaxRank(this.men), val2=RegretCost.getMaxRank(this.women);
		double max=(val1>val2?val1:val2);
		this.peer.send(this.masterPeer, new Message(max));
		this.peer.sync();
		if(this.peer.getPeerName().equals(this.masterPeer)){
			max=0;
			while(this.peer.getNumCurrentMessages()>0){
				Message m = this.peer.getCurrentMessage();
				max=(m.getMetricValue()>max?m.getMetricValue():max);
			}
			for(String s:this.peer.getAllPeerNames()){
				this.peer.send(s, new Message(max));
			}
		}
		this.peer.sync();
		while(this.peer.getNumCurrentMessages()>0){
			Message m = this.peer.getCurrentMessage();
			max = m.getMetricValue();
		}
		return max;
	}

}

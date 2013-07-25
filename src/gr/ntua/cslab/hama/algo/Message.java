package gr.ntua.cslab.hama.algo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * Class implementing the messages that will be exchanged between the peers.
 * @author Giannis Giannakopoulos
 *
 */
public class Message implements Writable {

	public static int 	PROPOSAL_MESSAGE=1,
						DIVORCE_MESSAGE=2,
						ACCEPTANCE_MESSAGE=3,
						SINGLES_COUNT_MESSAGE=4,
						UNHAPPY_COUNT_MESSAGE=5,
						METRICS_MESSAGE;
	private int type;
	private int source;
	private int destination;
	private double metricValue;
	
	public Message() {
		//default constructor
	}
	
	public Message(int type, int source, int destination){
		this.type=type;
		this.source=source;
		this.destination=destination;
	}
	
	public Message(int type, boolean condition){
		if(condition)
			this.source=1;
		else
			this.source=0;
	}
	
	public Message(double metricValue){
		this.type=METRICS_MESSAGE;
		this.metricValue = metricValue;	
	}
	
	public double getMetricValue(){
		return this.metricValue;
	}
	
	public boolean getCondition(){
		return (this.source==1);
	}
	
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.setType(in.readInt());
		this.setSource(in.readInt());
		this.setDestination(in.readInt());
		this.metricValue = in.readDouble();
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(this.type);
		out.writeInt(this.source);
		out.writeInt(this.destination);
		out.writeDouble(this.metricValue);
	}
	
	@Override
	public String toString() {
		return this.type+", "+this.source+"->"+this.destination;
	}

}

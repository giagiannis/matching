package gr.ntua.cslab.hama.metrics;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;


import gr.ntua.cslab.hama.algo.Message;
import gr.ntua.cslab.hama.containers.Person;
import gr.ntua.cslab.hama.containers.PersonList;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.bsp.BSPPeer;
import org.apache.hama.bsp.sync.SyncException;

public abstract class Metrics {

	protected PersonList men, women;
	protected BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer;
	protected String masterPeer;
	
	public Metrics(BSPPeer<NullWritable, NullWritable, Text, Text, Message> peer,PersonList men, PersonList women) {
		this.men=men;
		this.women=women;
		this.peer=peer;
		this.masterPeer = this.peer.getAllPeerNames()[0];
	}
	
	protected static double getRanksAverage(PersonList people){
		Iterator<Person> it = people.getMarriedIterator();
		int count=0;
		double sum=0.0;
		while(it.hasNext()){
			Person p = it.next();
			sum+=p.getCurrentPartnerRank();
			count++;
		}
		return sum/count;
	}
	
	protected static int getMaxRank(PersonList people){
		Iterator<Person> it =people.getMarriedIterator();
		int max=-1;
		while(it.hasNext()){
			Person p =it.next();
			max = (p.getCurrentPartnerRank()>max?p.getCurrentPartnerRank():max);
		}
		return max;
	}
	
	protected static int getMedianRankOfHappiness(PersonList men, PersonList women){
		LinkedList<Integer> ranks = new LinkedList<Integer>();
		Iterator<Person> it = men.getMarriedIterator();
		while(it.hasNext()){
			Person p = it.next();
			ranks.add(p.getCurrentPartnerRank());
		}
		
		it = women.getMarriedIterator();
		while(it.hasNext()){
			Person p = it.next();
			ranks.add(p.getCurrentPartnerRank());
		}
		
		Collections.sort(ranks);
		return ranks.get(ranks.size()/2);
	}
	
	protected static int countPeopleAboveRank(PersonList people, double threshold){
		Iterator<Person> it = people.getMarriedIterator();
		int count=0;
		while(it.hasNext()){
			Person p = it.next();
			if(p.getCurrentPartnerRank()>=threshold)
				count++;
		}
		return count;

	}
	
	public abstract double get() throws IOException, SyncException, InterruptedException;

}

package gr.ntua.cslab.metrics;

import java.util.Iterator;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;

/**
 * This abstract class will be inherited by all these classes that 
 * implement a metric measuring the stability and the quality of the stable matching
 * algorithms.
 * @author Giannis Giannakopoulos
 *
 */
public abstract class AbstractCost {

	protected PersonList men, women;
	
	public AbstractCost(PersonList men, PersonList women) {
		this.men=men;
		this.women=women;
	}
	
	/**
	 * Retuns the actual value of the metric (will be implemented by each metric separately)
	 * @return
	 */
	public abstract double get();
	
	/**
	 * Returns the {@link #get()} method result, expressed as a percentage, divided by the 
	 * size of the dataset. May be extended by some metrics in order to apply to specific cost functions. 
	 * @return
	 */
	public double getPercentage(){
		return this.get()/this.men.size();
	}
	
	protected static double getRanksSum(PersonList persons){
		Iterator<Person> it = persons.getIterator();
		double sum=0.0;
		int count=0;
		while(it.hasNext()){
			Person p=it.next();
			if(p.getCurrentPartnerRank()!=Integer.MAX_VALUE){
				sum+=p.getCurrentPartnerRank();
				count++;
			}
		}
		return sum/count;
	}
}

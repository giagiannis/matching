package gr.ntua.cslab.metrics;

import java.util.Iterator;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;

/**
 * Class used to count person happiness.
 * @author Giannis Giannakopoulos
 *
 */
public class HappinessMetrics {

	private PersonList person;
	
	/**
	 * Constructor of the class.
	 * @param person
	 */
	public HappinessMetrics(PersonList person) {
		this.person=person;
	}
	
	/**
	 * Returns the average rank of the person's preferences (lower is better)
	 * @return
	 */
	public double getAverageRank(){
		int sum=0;
		Iterator<Person> it = this.person.getIterator();
		while(it.hasNext()){
			Person person = it.next();
			sum+=person.getCurrentPartnerRank();
		}
		
		return ((double)sum/(double)this.person.size());
	}
	
	/**
	 * Returns the average rank of the person's preferences (lower is better)
	 * @return
	 */
	public double getAverageCoupleRank(){
		int sum=0;
		Iterator<Person> it = this.person.getIterator();
		while(it.hasNext()){
			Person person = it.next();
			sum+=(person.getCurrentPartnerRank()+person.getCurrentPartner().getCurrentPartnerRank());
		}
		
		return ((double)sum/((double)this.person.size()*2.0));
	}
	
	
}

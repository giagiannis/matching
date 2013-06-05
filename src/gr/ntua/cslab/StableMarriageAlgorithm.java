package gr.ntua.cslab;

import java.util.Iterator;

/**
 * This class implements the classic algorithm which solves the Stable Marriage Problem proposed by Shapley and Roth.
 * The algorithm is given two sets of preferences (corresponding to male and female preferences) and estimates a stable 
 * matching.
 * @author Giannis Giannakopoulos
 *
 */
public class StableMarriageAlgorithm {

	private PersonList men, women;
	
	public StableMarriageAlgorithm(PersonList men, PersonList women) {
		this.men=men;
		this.women=women;
	}
	
	public void step(){
		Iterator<Person> it = this.men.getSinglePersonIterator();
		while(it.hasNext()){
			Person man = it.next(), woman =this.women.get(man.getPreferences().getNextPreference()); 
			man.propose(woman);
		}
	}
}

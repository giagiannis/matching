package gr.ntua.cslab.algo;

import java.util.Iterator;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;
//import gr.ntua.cslab.data.DatasetReader;

/**
 * This class implements the classic algorithm which solves the Stable Marriage Problem proposed by Shapley and Roth.
 * The algorithm is given two sets of preferences (corresponding to male and female preferences) and estimates a stable 
 * matching.
 * @author Giannis Giannakopoulos
 *
 */
public class SMA extends AbstractStableMatchingAlgorithm{

	public SMA() {
		
	}
	
	public SMA(PersonList men, PersonList women) {
		super(men,women);
	}
	
	/**
	 * Returns true (in the classic algorithm men are always the proposers and women are the acceptors.
	 */
	@Override
	protected boolean menPropose() {
		return true;
	}
	
	@Override
	protected boolean terminationCondition() {
		return this.men.hasSinglePeople();
	}
	
	@Override
	protected Iterator<Person> getIterator(PersonList proposers) {
		return proposers.getSinglePersonIterator();
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		AbstractStableMatchingAlgorithm.runStaticWithRingPreferences(SMA.class, args);
	}
}

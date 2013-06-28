package gr.ntua.cslab.algo;

import java.util.Iterator;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;

/**
 * Naive ESMA: in this algorithm only single people are picked to make the next move.
 * @author Giannis Giannakopoulos
 *
 */
public class NESMA extends AbstractStableMatchingAlgorithm {

	public NESMA() {
		
	}

	public NESMA(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	protected boolean menPropose() {
		return (this.getStepCounter()%2==0);
	}
	
	@Override
	protected Iterator<Person> getIterator(PersonList proposers) {
		return proposers.getSinglePersonIterator();
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		AbstractStableMatchingAlgorithm.runStaticWithRingPreferences(NESMA.class, args);
	}
}

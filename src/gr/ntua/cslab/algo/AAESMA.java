package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.metrics.SexEqualnessCost;

/**
 * This class implements the ESMA algorithm, but it used a greedy method to determine which side will propose and which
 * side will accept. This makes the algorithm react in an affirmative way (AA) based on the results of the execution. 
 * @author Giannis Giannakopoulos
 *
 */
public class AAESMA extends AbstractStableMatchingAlgorithm {

	public AAESMA() {
		
	}
	
	public AAESMA(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	protected boolean menPropose() {
		SexEqualnessCost cost = new SexEqualnessCost(this.men, this.women);
		return (cost.get()>0);
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		AbstractStableMatchingAlgorithm.runStaticWithRingPreferences(AAESMA.class, args);
	}
}

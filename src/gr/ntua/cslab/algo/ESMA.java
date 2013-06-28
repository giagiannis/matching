package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;
//import gr.ntua.cslab.data.DatasetReader;

/**
 * Implementation of a female-friendly algorithm, based on Shapley-Roth algorithm.
 * @author Giannis Giannakopoulos
 *
 */
public class ESMA extends AbstractStableMatchingAlgorithm{
	
	public ESMA() {
		
	}
	
	public ESMA(PersonList men, PersonList women) {
		super(men,women);
	}
	
	/**
	 * This method returns true if the current step count is even, and false if it is odd.
	 */
	@Override
	protected boolean menPropose() {
		return(this.getStepCounter()%2==0);
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		AbstractStableMatchingAlgorithm.runStaticWithRingPreferences(ESMA.class, args);
	}
}

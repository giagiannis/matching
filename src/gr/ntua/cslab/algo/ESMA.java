package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.data.DatasetReader;

/**
 * Implementation of a female-friendly algorithm, based on Shapley-Roth algorithm.
 * @author Giannis Giannakopoulos
 *
 */
public class ESMA extends AbstractStableMatchingAlgorithm{
	
	public ESMA(PersonList men, PersonList women) {
		super(men,women);
	}
	
	/**
	 * This method returns true if the current step count is even, and false if it is odd.
	 */
	@Override
	protected boolean menPropose() {
		if(this.getStepCounter()%2==0)
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
		if(args.length<2){
			System.err.println("I need men and women preferences!");
			System.exit(1);
		}
		
		DatasetReader reader = new DatasetReader(args[0]);
		PersonList men = reader.getPeople();
		reader = new DatasetReader(args[1]);
		PersonList women = reader.getPeople();
		AbstractStableMatchingAlgorithm algo = new ESMA(men, women);
		algo.run();

		algo.performance();
	}
}

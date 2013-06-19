package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.data.DatasetReader;

/**
 * This class implements the classic algorithm which solves the Stable Marriage Problem proposed by Shapley and Roth.
 * The algorithm is given two sets of preferences (corresponding to male and female preferences) and estimates a stable 
 * matching.
 * @author Giannis Giannakopoulos
 *
 */
public class SMA extends AbstractStableMatchingAlgorithm{

	
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
	
	public static void main(String[] args) {
		if(args.length<2){
			System.err.println("I need men and women preferences!");
			System.exit(1);
		}
		
		DatasetReader reader = new DatasetReader(args[0]);
		PersonList men = reader.getPeople();
		reader = new DatasetReader(args[1]);
		PersonList women = reader.getPeople();
		AbstractStableMatchingAlgorithm algo = new SMA(men, women);
		algo.run();
		
		algo.performance();
	}
}

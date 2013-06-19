package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.data.DatasetReader;
import gr.ntua.cslab.metrics.SexEqualnessCost;

/**
 * This class implements the ESMA algorithm, but it used a greedy method to determine which side will propose and which
 * side will accept. This makes the algorithm react in an affirmative way (AA) based on the results of the execution. 
 * @author Giannis Giannakopoulos
 *
 */
public class AAESMA extends AbstractStableMatchingAlgorithm {

	public AAESMA(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	protected boolean menPropose() {
		SexEqualnessCost cost = new SexEqualnessCost(this.men, this.women);
		if(cost.get()>0)
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
		AbstractStableMatchingAlgorithm algo = new AAESMA(men, women);
		algo.run();

		algo.performance();
	}
}

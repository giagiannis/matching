package gr.ntua.cslab.algo;

import java.util.Random;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.data.DatasetReader;

/**
 * This class implements the Equitable Stable Marriage Algorithm using a randomized method to 
 * decide who will be the proposer and who the acceptor.
 * @author Giannis Giannakopoulos
 *
 */
public class RESMA extends AbstractStableMatchingAlgorithm {

	private Random rand;
	public RESMA(PersonList men, PersonList women) {
		super(men, women);
		this.rand = new Random();
		
	}

	/**
	 * Returns true/false values in a random manner (uniform).
	 */
	@Override
	protected boolean menPropose() {
		if(this.rand.nextBoolean())
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
		AbstractStableMatchingAlgorithm algo = new RESMA(men, women);
		algo.run();

		algo.performance();
	}
}

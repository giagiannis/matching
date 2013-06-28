package gr.ntua.cslab.algo;

import java.util.Random;

import gr.ntua.cslab.containers.PersonList;
//import gr.ntua.cslab.data.DatasetReader;

/**
 * This class implements the Equitable Stable Marriage Algorithm using a randomized method to 
 * decide who will be the proposer and who the acceptor.
 * @author Giannis Giannakopoulos
 *
 */
public class RESMA extends AbstractStableMatchingAlgorithm {

	private Random rand;
	
	public RESMA() {
		rand = new Random();
	}
	
	public RESMA(PersonList men, PersonList women) {
		super(men, women);
		this.rand = new Random();	
	}

	/**
	 * Returns true/false values in a random manner (uniform).
	 */
	@Override
	protected boolean menPropose() {
		return (this.rand.nextBoolean());
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		AbstractStableMatchingAlgorithm.runStaticWithRingPreferences(RESMA.class, args);
	}
}

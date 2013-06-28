package gr.ntua.cslab.algo;

import gr.ntua.cslab.metrics.GenderInequalityCost;

public class AAAESMAIneq extends AAAESMA{

	public AAAESMAIneq() {
		
	}
	
	
	@Override
	protected boolean menPropose() {
		GenderInequalityCost cost = new GenderInequalityCost(men, women);
		return (cost.get()<0);
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		AbstractStableMatchingAlgorithm.runStaticWithRingPreferences(AAAESMAIneq.class, args);
	}
}

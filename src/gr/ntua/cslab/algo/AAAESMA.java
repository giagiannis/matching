package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.metrics.SexEqualnessCost;

public class AAAESMA extends AbstractStableMatchingAlgorithm {

	public AAAESMA() {
		
	}

	public AAAESMA(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	protected boolean menPropose() {
		SexEqualnessCost cost = new SexEqualnessCost(men, women);
		return (cost.get()>0);
	}
	
	@Override
	public void step() {
		if(this.menPropose()){
			this.proposeStep(this.men);
			this.proposeStep(this.women);
		} else {
			this.proposeStep(this.women);
			this.proposeStep(this.men);
		}
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		AbstractStableMatchingAlgorithm.runStaticWithRingPreferences(AAAESMA.class, args);
	}

}

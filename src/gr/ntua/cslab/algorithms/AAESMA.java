package gr.ntua.cslab.algorithms;

import gr.ntua.cslab.metrics.SexEqualnessCost;

public class AAESMA extends AbstractSMA{

	public AAESMA() {

	}

	@Override
	public boolean terminationCondition() {
		return this.men.hasUnhappyPeople() || this.women.hasUnhappyPeople();
	}

	@Override
	protected boolean menPropose() {
		SexEqualnessCost c = new SexEqualnessCost(men, women);
		return (this.men.hasUnhappyPeople() && (c.get()>0 || !this.women.hasUnhappyPeople()));
	}
	
	public static void main(String[] args) {
		AbstractSMA.runAlgorithm(AAESMA.class, args);
	}

}

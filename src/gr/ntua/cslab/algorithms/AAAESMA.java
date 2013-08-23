package gr.ntua.cslab.algorithms;

import gr.ntua.cslab.metrics.SexEqualnessCost;

public class AAAESMA extends AbstractSMA{

	private boolean estimatedLastTime=false, lastCall;
	
	public AAAESMA() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean terminationCondition() {
		return this.men.hasUnhappyPeople() || this.women.hasUnhappyPeople();
	}

	@Override
	protected boolean menPropose() {
		if(!estimatedLastTime){
			SexEqualnessCost c = new SexEqualnessCost(men, women);
			lastCall=(this.men.hasUnhappyPeople() && (c.get()>0 || !this.women.hasUnhappyPeople()));
			estimatedLastTime=true;
		} else {
			estimatedLastTime=false;
			lastCall=!lastCall;
		}
		return lastCall;
	}
	
	public static void main(String[] args) {
		AbstractSMA.runAlgorithm(AAAESMA.class,args);
	}

}

package gr.ntua.cslab.algorithms;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.metrics.SexEqualnessCost;

public class AAESMAGrad extends AbstractSMA {

	private boolean lastCall=true;
	private double lastValue=0;
	public AAESMAGrad() {

	}

	public AAESMAGrad(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	public boolean terminationCondition() {
		return this.men.hasUnhappyPeople() || this.women.hasUnhappyPeople();
	}

	@Override
	protected boolean menPropose() {
		SexEqualnessCost cost = new SexEqualnessCost(men, women);
		double current=cost.get();
		double diff = current-lastValue;
		lastValue=current;
		boolean result=(diff*current<0?this.lastCall:!this.lastCall);
		result=(this.men.hasUnhappyPeople() && (result || !this.women.hasUnhappyPeople()));
		this.lastCall=result;
//		System.out.println(this.stepCounter+","+result+" ("+this.cycleDetected+")");
		return result;
	}

	public static void main(String[] args) {
		AbstractSMA.runAlgorithm(AAESMAGrad.class, args);
	}
}

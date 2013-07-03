package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.metrics.SexEqualnessCost;

public class AAESMADiff extends AAESMA {

	private double previousValue=0.0;
	private boolean lastResult;
	public AAESMADiff() {
		// TODO Auto-generated constructor stub
	}

	public AAESMADiff(PersonList men, PersonList women) {
		super(men, women);
	}
	
	@Override
	protected boolean menPropose() {
		
		SexEqualnessCost cost = new SexEqualnessCost(men, women);
		double current=cost.get(), diff=current-this.previousValue;
		this.previousValue=current;
		
		boolean result;
		
		if(current*diff<0){
			result=this.lastResult;
		} else {
			result=!this.lastResult;
		}
		
		this.lastResult=result;
		return result;
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		AbstractStableMatchingAlgorithm.runStaticWithRingPreferences(AAESMADiff.class, args);
	}
}

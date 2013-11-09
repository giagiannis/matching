package gr.ntua.cslab.algorithms;

import gr.ntua.cslab.containers.PersonList;

public class SMA extends AbstractSMA {

	public SMA() {
		// TODO Auto-generated constructor stub
	}

	public SMA(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	public boolean terminationCondition() {
		return this.men.hasSinglePeople();
	}

	@Override
	protected boolean menPropose() {
		return true;
	}

	public void step(){		
		boolean menDoPropose=true;
		if(menDoPropose){
			this.proposeStep(this.men);
		} else {
			this.proposeStep(this.women);
		}
		if(this.stepsDiagnostics!=0 && this.stepCounter%this.stepsDiagnostics==0){
			System.err.println(this.stepCounter+"\t"+(System.currentTimeMillis()-this.executionTime)+"\t"+this.diagnostics.step());
//			System.out.print(this.diagnostics.resultsIsStable()+"\n");
		}
	}

	
	public static void main(String[] args) {
		AbstractSMA.runAlgorithm(SMA.class, args);
	}
}

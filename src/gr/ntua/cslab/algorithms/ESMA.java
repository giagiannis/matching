package gr.ntua.cslab.algorithms;

import gr.ntua.cslab.containers.PersonList;

public class ESMA extends AbstractSMA {

	public ESMA() {

	}

	public ESMA(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	public boolean terminationCondition() {
		return this.men.hasUnhappyPeople() || this.women.hasUnhappyPeople();
	}

	@Override
	protected boolean menPropose() {
//		System.out.println((this.stepCounter%2==0 && this.men.hasUnhappyPeople()) || !this.women.hasUnhappyPeople());
		return (this.stepCounter%2==0);
	}

	public static void main(String[] args) {
		AbstractSMA.runAlgorithm(ESMA.class, args);
	}
}

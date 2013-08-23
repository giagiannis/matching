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

	public static void main(String[] args) {
		AbstractSMA.runAlgorithm(SMA.class, args);
	}
}

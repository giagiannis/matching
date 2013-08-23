package gr.ntua.cslab.algorithms;

import java.util.Random;

import gr.ntua.cslab.containers.PersonList;

public class RESMA extends AbstractSMA {

	private Random random;
	
	public RESMA() {
		super();
		this.random=new Random();
	}

	public RESMA(PersonList men, PersonList women) {
		super(men, women);
		this.random = new Random();
	}

	@Override
	public boolean terminationCondition() {
		return this.men.hasUnhappyPeople() || this.women.hasUnhappyPeople();
	}

	@Override
	protected boolean menPropose() {
		return this.random.nextBoolean();
	}
	
	public static void main(String[] args) {
		AbstractSMA.runAlgorithm(RESMA.class, args);
	}

}

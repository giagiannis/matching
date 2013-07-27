package gr.ntua.cslab.hama.algo;

import gr.ntua.cslab.hama.containers.Person;
import gr.ntua.cslab.hama.containers.PersonList;

import java.util.Iterator;

public class ESMA extends AbstractAlgorithm {

	public ESMA() {
		super();
	}

	@Override
	protected Iterator<Person> getIterator(PersonList proposers) {
		return proposers.getUnhappyIterator();
	}

	@Override
	protected boolean menPropose() {
		return (this.stepCounter%2==0);
	}

	@Override
	protected int getTerminationCondition() {
		return ESMA.NO_UNHAPPY_CONDITION;
	}
	
	public static void main(String[] args) throws Exception{
		AbstractAlgorithm.execute(args, ESMA.class);
	}

}

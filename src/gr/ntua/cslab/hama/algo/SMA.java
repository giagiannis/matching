package gr.ntua.cslab.hama.algo;

import gr.ntua.cslab.hama.containers.Person;
import gr.ntua.cslab.hama.containers.PersonList;

import java.util.Iterator;

public class SMA extends AbstractAlgorithm {
	
	public SMA() {
		super();		
	}

	@Override
	protected Iterator<Person> getIterator(PersonList persons) {
		return persons.getSingleIterator();
	}

	@Override
	protected boolean menPropose() {
		return true;
	}

	@Override
	protected int getTerminationCondition() {
		return SMA.NO_SINGLES_CONDITION;
	}
	
	public static void main(String[] args) throws Exception{
		AbstractAlgorithm.execute(args, SMA.class);
	}
}

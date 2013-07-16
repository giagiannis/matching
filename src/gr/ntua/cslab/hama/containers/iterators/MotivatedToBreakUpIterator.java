package gr.ntua.cslab.hama.containers.iterators;

import gr.ntua.cslab.hama.containers.Person;

import java.util.Iterator;

public class MotivatedToBreakUpIterator implements Iterator<Person> {

	private int i=0;
	private Person[] people;
	
	public MotivatedToBreakUpIterator(Person[] people) {
		this.people=people;
	}


	@Override
	public boolean hasNext() {
		while(this.i<this.people.length && !this.people[i].isMotivatedToBreakUp())
			this.i++;
		return this.i<this.people.length;
	}

	@Override
	public Person next() {
		return this.people[this.i++];
	}

	@Override
	public void remove() {
		// does nothing by default
	}

}

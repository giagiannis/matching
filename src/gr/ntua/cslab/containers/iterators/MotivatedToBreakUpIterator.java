package gr.ntua.cslab.containers.iterators;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;

import java.util.Iterator;

public class MotivatedToBreakUpIterator implements Iterator<Person> {

	private int id=0;
	private PersonList people;
	
	public MotivatedToBreakUpIterator(PersonList people) {
		this.people=people;
		this.id=1;
	}
	
	@Override
	public boolean hasNext() {
		while(this.id<=this.people.size() && !this.people.get(this.id).isMotivatedToBreakUp())
			this.id++;
		return this.id<=this.people.size();
	}

	@Override
	public Person next() {
		return this.people.get(this.id++);
	}

	@Override
	public void remove() {
		// does nothing by default
	}

}

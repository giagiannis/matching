package gr.ntua.cslab.containers.iterators;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;

import java.util.Iterator;

public class PersonIterator implements Iterator<Person>{

	private int id;
	private PersonList list;
	public PersonIterator(PersonList list) {
		this.id=1;
		this.list=list;
	}
	@Override
	public boolean hasNext() {
		return this.id<=this.list.size();
	}

	@Override
	public Person next() {
		return this.list.get(this.id++);
	}

	@Override
	public void remove() {
		// does nothing
		
	}
}
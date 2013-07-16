package gr.ntua.cslab.hama.containers.iterators;

import gr.ntua.cslab.hama.containers.Person;
import java.util.Iterator;

public class PersonIterator implements Iterator<Person>{

	private int i;
	private Person[] list;
	public PersonIterator(Person[] persons) {
		this.i=0;
		this.list=persons;
	}
	
	@Override
	public boolean hasNext() {
		return this.i<this.list.length;
	}

	@Override
	public Person next() {
		return this.list[this.i++];
	}

	@Override
	public void remove() {
		// does nothing
		
	}
}
package gr.ntua.cslab.hama.containers.iterators;

import java.util.Iterator;

import gr.ntua.cslab.hama.containers.Person;

public class MarriedIterator implements Iterator<Person>{

	private Person[] list;
	private int i;

	public MarriedIterator(Person[] persons) {
		this.list=persons;
		this.i=0;
	}
	
	@Override
	public boolean hasNext() {
		while( this.i<list.length && !list[this.i].isMarried())
			this.i++;
		return this.i<list.length;
	}

	@Override
	public Person next() {
		return this.list[this.i++];
	}

	@Override
	public void remove() {
		// does noting
		
	}
}

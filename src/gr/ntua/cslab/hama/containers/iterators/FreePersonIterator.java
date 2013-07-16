package gr.ntua.cslab.hama.containers.iterators;

import gr.ntua.cslab.hama.containers.Person;

import java.util.Iterator;

public class FreePersonIterator implements Iterator<Person>{

	private int i=0;
	private Person[] list;
	public FreePersonIterator(Person[] persons) {
		this.list=persons;
		this.i=0;
	}
	
	@Override
	public boolean hasNext() {
		while( this.i<list.length && list[this.i].isMarried())
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

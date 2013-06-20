package gr.ntua.cslab.containers.iterators;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;

import java.util.Iterator;

public class FreePersonIterator implements Iterator<Person>{

	private int id=0;
	private PersonList list;
	public FreePersonIterator(PersonList list) {
		this.list=list;
		this.id=1;
	}
	
	@Override
	public boolean hasNext() {
		while( this.id<=list.size() && list.get(this.id).isMarried())
			this.id++;
		return this.id<=list.size();
	}

	@Override
	public Person next() {
		return this.list.get(this.id++);
	}

	@Override
	public void remove() {
		// does noting
		
	}
	
}

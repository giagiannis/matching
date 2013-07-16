package gr.ntua.cslab.hama.containers;



import gr.ntua.cslab.hama.containers.iterators.FreePersonIterator;
import gr.ntua.cslab.hama.containers.iterators.MotivatedToBreakUpIterator;
import gr.ntua.cslab.hama.containers.iterators.PersonIterator;

import java.util.Iterator;

public class PersonList {

	private Person[] persons;
	private int offset;
	
	public PersonList(int capacity, int offset) {
		this.persons = new Person[capacity];
		this.offset=offset;
	}
	
	public void add(Person p){
		this.persons[p.getId()-this.offset]=p;
	}
	
	public int size(){
		return this.persons.length;
	}
	
	/**
	 * Get person by their id
	 * @param id
	 * @return
	 */
	public Person get(int id){
		if(id-offset<0 || id-offset>=this.persons.length)
			return null;
		return this.persons[id-offset];
	}
	
	public Iterator<Person> getIterator(){
		return new PersonIterator(this.persons);
	}
	
	public Iterator<Person> getUnhappyIterator(){
		return new MotivatedToBreakUpIterator(this.persons);
	}
	
	public Iterator<Person> getSingleIterator(){
		return new FreePersonIterator(this.persons);
	}
	
	public boolean hasSinglePeople(){
		Iterator<Person> it = this.getSingleIterator();
		return it.hasNext();
	}
	
	@Override
	public String toString() {
		String buffer="";
		for(int i=0;i<this.size();i++){
			buffer+=this.persons[i]+" ";
		}
		return buffer;
	}
}

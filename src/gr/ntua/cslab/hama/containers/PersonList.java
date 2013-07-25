package gr.ntua.cslab.hama.containers;



import gr.ntua.cslab.hama.containers.iterators.FreePersonIterator;
import gr.ntua.cslab.hama.containers.iterators.MarriedIterator;
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
	
	/**
	 * Returns an iterator about the personlist. All people are returned.
	 * @return
	 */
	public Iterator<Person> getIterator(){
		return new PersonIterator(this.persons);
	}
	
	/**
	 * Returns an iterator containing only the unhappy people. 
	 * @return
	 */
	public Iterator<Person> getUnhappyIterator(){
		return new MotivatedToBreakUpIterator(this.persons);
	}
	
	/**
	 * Returns an iterator containing only the single people.
	 * @return
	 */
	public Iterator<Person> getSingleIterator(){
		return new FreePersonIterator(this.persons);
	}
	
	public Iterator<Person> getMarriedIterator() {
		return new MarriedIterator(this.persons);
	}

	/**
	 * True if the {@link PersonList} contains single people, else false.
	 * @return
	 */
	public boolean hasSinglePeople(){
		Iterator<Person> it = this.getSingleIterator();
		return it.hasNext();
	}
	
	/**
	 * True if the {@link PersonList} contains unhappy people, else false.
	 * @return
	 */
	public boolean hasUnhappyPeople(){
		Iterator<Person> it = this.getUnhappyIterator();
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

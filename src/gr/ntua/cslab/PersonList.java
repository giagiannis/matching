package gr.ntua.cslab;

import java.util.Iterator;


/**
 * Class used to hold a list of people.
 * @author Giannis Giannakopoulos
 *
 */
public class PersonList {

	private Person[] people=null;
	
	/**
	 * Constructor of the class. Capacity of the list must be provided.
	 * @param numOfPeople
	 */
	public PersonList(int numOfPeople) {
		this.allocate(numOfPeople);
	}
	
	private void allocate(int numOfPeople){
		this.people = new Person[numOfPeople];
	}
	
	/**
	 * Returns the person with the specified id.
	 * @param id
	 * @return
	 */
	public Person get(int id){
		return this.people[id-1];
	}
	
	/**
	 * Adds a person to the list
	 * @param person
	 */
	public void add(Person person){
		this.people[person.getId()-1]=person;
	}
	
	/**
	 * Returns the size of the list
	 * @return
	 */
	public int size(){
		return this.people.length;
	}
	
	
	/**
	 * Returns the default iterator used to iterate through the list
	 * @return
	 */
	public Iterator<Person> getIterator() {
		return new PersonIterator(this);
	}
	
	/**
	 * Returns an iterator which returns only the not married people of the list
	 * @return
	 */
	public Iterator<Person> getFreePersonIterator() {
		return new FreePersonIterator(this);
	}
	
	@Override
	public String toString() {
		String buffer="[";
		for(int i=0;i<this.people.length;i++)
			buffer+=this.people[i].toString()+", ";
		buffer=buffer.substring(0, buffer.length()-2)+"]";
		return buffer;
	}
	
	public static void main(String[] args) {
		PersonList list= new DatasetReader(args[0]).getPeople();
		System.out.println("List size: "+list.size());
		Iterator<Person> p = list.getIterator();
		while(p.hasNext()){
			System.out.println(p.next());
		}
		list.get(1).propose(list.get(2));
		list.get(3).propose(list.get(2));
		System.out.println("after");
		p = list.getFreePersonIterator();
		while(p.hasNext()){
			System.out.println(p.next());
		}
		list.get(3).divorce();
		System.out.println("again");
		p = list.getFreePersonIterator();
		while(p.hasNext()){
			System.out.println(p.next());
		}
		
	}
}
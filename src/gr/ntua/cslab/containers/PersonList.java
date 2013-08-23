package gr.ntua.cslab.containers;

import gr.ntua.cslab.containers.iterators.FreePersonIterator;
import gr.ntua.cslab.containers.iterators.MotivatedToBreakUpIterator;
import gr.ntua.cslab.containers.iterators.PersonIterator;
import gr.ntua.cslab.data.DatasetReader;

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
		if(id<1){
			System.err.println("I've been asked to deliver "+id);
//			System.exit(1);
		}
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
	public Iterator<Person> getSinglePersonIterator() {
		return new FreePersonIterator(this);
	}
	
	public Iterator<Person> getMotivatedToBreakUpIterator(){
		return new MotivatedToBreakUpIterator(this);
	}
	
	/**
	 * Returns the number of single people on the list
	 * @return
	 */
	public int getNumberOfSingles(){
		int count=0;
		Iterator<Person> it = this.getSinglePersonIterator();
		while(it.hasNext()){
			it.next();
			count+=1;
		}
		return count;
	}
	
	/**
	 * Returns true if there exist unmarried people or false if everyone is married.
	 * @return
	 */
	public boolean hasSinglePeople(){
		return this.getNumberOfSingles()>0;
	}
	
	public boolean hasUnhappyPeople(){
		Iterator<Person> it = this.getMotivatedToBreakUpIterator();
		return it.hasNext();
	}
	
	public int countPeopleWithCycles(){
		Iterator<Person> it= this.getIterator();
		int count=0;
		while(it.hasNext()){
			Person p = it.next();
			if(p.hasCycle())
				count++;
		}
		return count;
	}
	
	/**
	 * Returns all the partners ranks (according to the people contained in PersonList) as an array.
	 * @return
	 */
	public int[] getRanksArray(){
		int[] sort = new int[this.size()];
		Iterator<Person> it = this.getIterator();
		int i=0;
		while(it.hasNext())
			sort[i++]=it.next().getCurrentPartnerRank();
		return sort;
	}
	
	/**
	 * Returns the number of satisfied people, given a satisfaction threshold. This threshold
	 * is expressed as a rank (refering to the partner): if the rank is below average 
	 * @param satisfactionThreshold
	 * @return
	 */
	public int getSatisfiedPeople(int satisfactionThreshold){
		Iterator<Person> it = this.getIterator();
		int count=0;
		while(it.hasNext()){
			if(it.next().getCurrentPartnerRank()<satisfactionThreshold)
				count+=1;
		}
		return count;
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
		System.out.println("Singles:\t"+list.getNumberOfSingles());
		p = list.getSinglePersonIterator();
		while(p.hasNext()){
			System.out.println(p.next());
		}
		list.get(3).divorce();
		System.out.println("again");
		p = list.getSinglePersonIterator();
		while(p.hasNext()){
			System.out.println(p.next());
		}
		
	}
}
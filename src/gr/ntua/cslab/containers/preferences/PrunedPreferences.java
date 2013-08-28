package gr.ntua.cslab.containers.preferences;

import java.util.TreeSet;

/**
 * Class used to keep the "next" preference. This class consists of a container which keeps ranks and not id's of next person.
 * @author giannis
 *
 */
public class PrunedPreferences {

	private TreeSet<Integer> prefereneces;
	private int size;
	
	public PrunedPreferences(int size) {
		this.prefereneces = new TreeSet<Integer>();
		this.prefereneces.add(1);
		this.size = size;
	}
	
	/**
	 * Adds an element to the list.
	 * @param rank
	 * @return
	 */
	public boolean addPreference(int rank){
		if(this.prefereneces.isEmpty()){
			this.prefereneces.add(rank);
			return true;
		}
		if(rank > this.prefereneces.last())
			return false;
		if(rank == this.prefereneces.last()-1)
			this.prefereneces.remove(this.prefereneces.last());
		this.prefereneces.add(rank);
		return true;
	}
	
	/**
	 * Returns the next preference. 
	 * @return
	 */
	public int getNext(){
		if(this.prefereneces.isEmpty())
			this.prefereneces.add(1);
		Integer head = this.prefereneces.first();
		this.prefereneces.remove(head);
		if(this.prefereneces.size()==0 && (head+1<=this.size)){
			this.prefereneces.add(head+1);
		}
		return head;
	}
	
	public int getNextPreference(){
		if(!this.prefereneces.isEmpty())
			return this.prefereneces.first();
		else
			return Integer.MAX_VALUE;
		
	}
	
	/**
	 * True if the preference list contains more values, else false.
	 * @return
	 */
	public boolean hasMorePreferences(){
		return !this.prefereneces.isEmpty();
	}
	
	public static void main(String[] args) {
		PrunedPreferences pref = new PrunedPreferences(10);
		for(int i=0;i<6;i++)System.out.println(pref.getNext());
		pref.addPreference(2);
		
		pref.addPreference(5);
		System.out.println(pref.getNext());
		System.out.println(pref.getNext());
		System.out.println(pref.getNext());
	}
	
}

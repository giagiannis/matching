package gr.ntua.cslab.containers.preferences;

import java.util.HashSet;
import java.util.Set;

public class RingPreferences implements Preferences {

	private int count=0;
	private int index;
	private int step;
	private int base=0;
	private int size;
	private int initialBase=0;
	
	public RingPreferences() {
		
	}

	/**
	 * Default constructor. Expected parameter: the size of 
	 * @param size
	 */
	public RingPreferences(int size, int step, int base){
		this.size=size;
		this.step=step;
		this.index=base-1;
		this.base=base-1;
		this.initialBase=base;
//		System.out.println("Base:"+this.base);
	}
	
	
	@Override
	public int getNext() {
		if(this.hasMore()){
			int retValue=this.index+1;
			this.index=nextIndex();
			this.count++;
			return retValue;
		} else 	
			return Preferences.NO_PREFERENCE;
	}
	
	
	private int nextIndex(){
		int nextIndex=this.index;
		nextIndex = (nextIndex+this.step)%this.size;
//		System.out.println("Next:"+nextIndex);
		if(nextIndex==this.base ){
//			System.out.println("Rebasing "+nextIndex);
			this.base+=1;
			nextIndex=this.base;
		}
		return nextIndex;
	}

	@Override
	public boolean hasMore() {
		if(this.count<this.size)
			return true;
		else
			return false;
	}
	
	@Override
	public int getRank(int id) {
		System.out.println("Div:\t"+id/this.step);
		System.out.println("Mod:\t"+id%this.step);
		return 0;
	}

	@Override
	public int getNextRank() {	
		return this.count+1;
	}

	public static void main(String[] args) {
		RingPreferences pref = new RingPreferences(new Integer(args[0]), new Integer(args[1]), new Integer(args[2]));
		Set<Integer> set = new HashSet<Integer>();
		for(int i=0;i<5;i++){
			System.out.println(pref.getNext());
		}
		System.out.println("Next rank:"+pref.getNextRank());
		System.out.println("8 rank:\t"+pref.getRank(8));
	}
}

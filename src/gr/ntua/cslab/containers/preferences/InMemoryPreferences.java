package gr.ntua.cslab.containers.preferences;

/**
 * Preferences object holds the preferences of a man or woman. A number of methods are provided to set and get
 * the preferences in ranking or preference id order. 
 * @author Giannis Giannakopoulos
 *
 */
public class InMemoryPreferences implements Preferences {
	
	private int[] preferencesOrderedByRank;
	private int[] preferencesOrderedById;
	private PrunedPreferences pruned;
	
	/**
	 * Empty constructor (does nothing).
	 */
	public InMemoryPreferences() {
		
	}
	
	/**
	 * Constructor used to initialize preference object.
	 * @param preferences the preferences (sorted by ranking or id)
	 * @param orderedByRank true if the ordering is based on rank or false if it is based on id
	 */
	public InMemoryPreferences(int[] preferences, boolean orderedByRank){
		if(orderedByRank)
			this.setPreferencesOrderedByRank(preferences);
		else
			this.setPreferencesOrderedById(preferences);
		this.pruned = new PrunedPreferences(preferences.length);
	}
	
	/**
	 * Sets the preferences assuming that the values are displayed in ranking order. This means that 
	 * the first value is the most desired preference, the second value is the second best, etc.
	 * @param preferences
	 */
	public final void setPreferencesOrderedByRank(int[] preferences){
		this.allocateTables(preferences.length);
		for(int i=0;i<preferences.length;i++){
			this.preferencesOrderedByRank[i]=preferences[i];
			this.preferencesOrderedById[preferences[i]-1]=i+1;
		}
	}
	
	/**
	 * Sets the preferences assuming that the values are provided in id order. This means that the first value
	 * of the preference array is the rank for the first partner (id=1), the second value is the rank for the second 
	 * partner (id=2), etc.
	 * @param preferences
	 */
	public final void setPreferencesOrderedById(int[] preferences){
		this.allocateTables(preferences.length);
		for(int i=0;i<preferences.length;i++){
			this.preferencesOrderedByRank[preferences[i]-1]=i+1;
			this.preferencesOrderedById[i]=preferences[i];
		}
	}
	
	private void allocateTables(int size){
		this.preferencesOrderedByRank=new int[size];
		this.preferencesOrderedById=new int[size];
	}
	
	/**
	 * Returns the next preference (ordered by rank)
	 * @return
	 */
	public int getNext(){
//		if(this.index>=this.preferencesOrderedByRank.length)
//			this.index=0;
//		return this.preferencesOrderedByRank[this.index++];
		return this.preferencesOrderedByRank[this.pruned.getNext()-1];
	}
	
	/**
	 * True if there are more preferences, false if there are not.
	 * @return
	 */
	public boolean hasMore(){
//		return this.index<this.preferencesOrderedByRank.length;
		return pruned.hasMorePreferences();
	}
	
	/**
	 * Returns the rank for the specified person -id-
	 * @param id
	 * @return
	 */
	public int getRank(int id){
		return this.preferencesOrderedById[id-1];
	}

	/**
	 * Returns the rank of the person's next preference. IMPORTANT: this method
	 * does not have the same effect as the {@link InMemoryPreferences#getNext()} method,
	 * as this method does not proceed to the next preference of the person.
	 * @return
	 */
	public int getNextRank(){
		return this.pruned.getNextPreference();
//		if(this.index>=this.preferencesOrderedByRank.length)
//			return this.preferencesOrderedById.length;
//		else
//			return this.index+1;
	}
	@Override
	public void addNext(int rank) {
		this.pruned.addPreference(rank);
//		if(rank<this.index && rank>0)
//			this.index=rank-1;
		
	}
	 
	@Override
	public String toString() {
		String buffer="";
		int i=1;
		for(int rank:this.preferencesOrderedByRank)
			buffer+="Pref "+(i++)+" is parnter "+rank+"\n";
		i=1;
		for(int rank:this.preferencesOrderedById)
			buffer+="Partner "+(i++)+" is ranked "+rank+"\n";
		return buffer.substring(0, buffer.length()-1);
	}
	
//	public static void main(String[] args) {
//		int[] foo = {1,5,4,2,3};
//		InMemoryPreferences pref = new InMemoryPreferences(foo, true);
//		while(pref.hasMore())
//			System.out.println(pref.getNext());
//		pref.setNext(3);
//		while(pref.hasMore())
//			System.out.println(pref.getNext());
//	}

	@Override
	public int getSize() {
		return this.preferencesOrderedById.length;
	}

	@Override
	public int getId(int rank) {
		return this.preferencesOrderedByRank[rank-1];
		
	}
}

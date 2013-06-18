package gr.ntua.cslab.containers;

/**
 * Preferences object holds the preferences of a man or woman. A number of methods are provided to set and get
 * the preferences in ranking or preference id order. 
 * @author Giannis Giannakopoulos
 *
 */
public class Preferences {

	private static final int DO_NOT_EXIST = -1;
	private int[] preferencesOrderedByRank;
	private int[] preferencesOrderedById;
	private int index=0;
	
	/**
	 * Empty constructor (does nothing).
	 */
	public Preferences() {
		
	}
	
	/**
	 * Constructor used to initialize preference object.
	 * @param preferences the preferences (sorted by ranking or id)
	 * @param orderedByRank true if the ordering is based on rank or false if it is based on id
	 */
	public Preferences(int[] preferences, boolean orderedByRank){
		if(orderedByRank)
			this.setPreferencesOrderedByRank(preferences);
		else
			this.setPreferencesOrderedById(preferences);
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
	public int getNextPreference(){
		if(this.index>=this.preferencesOrderedByRank.length)
			return Preferences.DO_NOT_EXIST;
		else
			return this.preferencesOrderedByRank[this.index++];
	}
	
	/**
	 * True if there are more preferences, false if there are not.
	 * @return
	 */
	public boolean hasPreferencesLeft(){
		return this.index<this.preferencesOrderedByRank.length;
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
	 * does not have the same effect as the {@link Preferences#getNextPreference()} method,
	 * as this method does not proceed to the next preference of the person.
	 * @return
	 */
	public int getNextPreferenceRank(){
		if(this.index>=this.preferencesOrderedByRank.length)
			return Preferences.DO_NOT_EXIST;
		else
			return this.index+1;
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
	
	public static void main(String[] args) {
		int[] foo = {1,5,4,2,3};
		Preferences pref = new Preferences(foo, true);
		while(pref.hasPreferencesLeft())
			System.out.println(pref.getRank(pref.getNextPreference()));
	}
}

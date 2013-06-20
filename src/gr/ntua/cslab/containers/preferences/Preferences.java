package gr.ntua.cslab.containers.preferences;

/**
 * It provides an abstraction layer between the preferences interface and the preferences implementation. 
 * @author Giannis Giannakopoulos
 *
 */
public interface Preferences {
	static final int NO_PREFERENCE = -1;
	
	/**
	 * Returns the next preference id of the specified person
	 * @return
	 */
	public int getNext();
	
	/**
	 * Returns the rank 
	 * @param id
	 * @return
	 */
	public int getRank(int id);
	
	/**
	 * True if more available preferences exist, false if they do not exist.
	 * @return
	 */
	public boolean hasMore();
	
	/**
	 * Returns the next rank of preferences. This method does not go to the next choice.
	 * @return
	 */
	public int getNextRank();

}
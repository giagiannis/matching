package gr.ntua.cslab.hama.containers.preferences;

public interface Preferences {

	/**
	 * Returns the next preference
	 * @return
	 */
	public int getNext();
	
	/**
	 * Returns true if there exist more elements to the preference list, false else.
	 * @return
	 */
	public boolean hasMore();
	
	/**
	 * Returns the next available rank (not proceeding to the next rank though).
	 * @return
	 */
	public int getNextRank();
	
	/**
	 * {@link Deprecated}
	 * Returns the rank for the specified id 
	 * @param id
	 * @return
	 */
	public int getRank(int id);
	
	/**
	 * Resets the preference list (starts from the beggining).
	 */
	public void reset();
}

package gr.ntua.cslab;

/**
 * Person class is modeling the actions that a person can do. 
 * @author Giannis Giannakopoulos
 *
 */
public class Person {

	private static Person STATE_BAKOURI=null;
	private int id;
	private Preferences preferences;
	private Person partner;
	
	/**
	 * Constructor expecting the person's preferences
	 * @param preferences
	 */
	public Person(int id, Preferences preferences) {
		this.preferences=preferences;
		this.id=id;
	}
	
	/**
	 * Returns the person's id
	 * @return
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * True if the person is married, else false
	 * @return
	 */
	public boolean isMarried(){
		return this.partner!=Person.STATE_BAKOURI;
	}
	
	/**
	 * Returns the current partner of the person
	 * @return
	 */
	public Person getCurrentPartner(){
		return this.partner;
	}
	
	/**
	 * Returns the preferences of the person
	 * @return
	 */
	public Preferences getPreferences(){
		return this.preferences;
	}
	
	/**
	 * Marries the object with the specified person (both members of the marriage become aware of the marriage)
	 * @param id
	 */
	public void marry(Person person){
		if(this.isMarried())
			this.divorce();
		this.partner=person;
		if(this.partner.getCurrentPartner()==null || this.partner.getCurrentPartner().getId()!=this.getId())
			this.partner.marry(this);
	}
	
	/**
	 * Divorces a couple (both members of the marriage are notified about the divorce)
	 */
	public void divorce(){
		if(!isMarried())
			return;
		Person previous=this.partner;
		this.partner=Person.STATE_BAKOURI;
		if(previous.getCurrentPartner()!=null && previous.getCurrentPartner().getId()==this.getId())
			previous.divorce();
	}
	
	/**
	 * Make a proposal to the specified person, the proposal may not lead to a marriage though: if a marriage is made then true is returned, else false
	 * @param p
	 * @return
	 */
	public boolean propose(Person p){
		if(!p.isMarried() || p.getPreferences().getRank(this.getId())<p.getCurrentPartnerRank()){
			this.marry(p);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the ranking of the object to its current partner
	 * @return
	 */
	public int getCurrentPartnerRank(){
		if(this.isMarried()){
			return this.getPreferences().getRank(this.partner.getId());
		} else {
			return Integer.MAX_VALUE;
		}
	}
	
	@Override
	public String toString() {
		String buffer="";
		buffer+=this.id;
//		if(this.isMarried())
//			buffer+=" married to id:\t"+this.getCurrentPartner().getId();
//		else
//			buffer+=" bakouri";
		return buffer;

	}
}

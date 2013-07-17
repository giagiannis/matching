package gr.ntua.cslab.hama.containers;

import gr.ntua.cslab.hama.containers.preferences.MemoryPreferences;

public class Person {

	public static int 	NOT_EXIST=Integer.MAX_VALUE,
						NO_MARRIAGE=-100;
	
	private int id;
	private MemoryPreferences preferences;
	private int partnerId;

	private int candidateId;
	
	/**
	 * Person constructor
	 * @param id
	 * @param preferences
	 */
	public Person(int id, MemoryPreferences preferences) {
		this.id=id;
		this.preferences=preferences;
		this.partnerId=NOT_EXIST;
		this.candidateId=NOT_EXIST;
	}
	
	/**
	 * Returns the id
	 * @return
	 */
	public int getId(){
		return this.id;
	}
	
	public int getPartnerId(){
		return this.partnerId;
	}
	/**
	 * Returns the preference rank
	 * @return
	 */
	public MemoryPreferences getPreferences(){
		return this.preferences;
	}
	
	/**
	 * Returns the rank of the current partner
	 * @return
	 */
	public int getCurrentPartnerRank(){
		if(this.isMarried())
			return this.preferences.getRank(partnerId);
		else
			return NOT_EXIST;
					
	}
	
	public boolean isMotivatedToBreakUp(){
		return (!isMarried()) || (this.getCurrentPartnerRank()>this.preferences.getNextRank());
	}
	
	/**
	 * True if the specified people is married
	 * @return
	 */
	public boolean isMarried(){
		return this.partnerId!=NOT_EXIST;
	}
	
	/**
	 * Marries the object with the partner specified by his/her id.
	 * @param partnerId
	 * @return
	 */
	public boolean marry(int partnerId){
		if(!this.isMarried() || this.getCurrentPartnerRank()>this.getPreferences().getRank(this.partnerId))
			this.partnerId=partnerId;
		else
			return false;
		return true;
	}
	
	/**
	 * Breaks up the specified object form their partner
	 */
	public void divorce(){
		this.partnerId=NOT_EXIST;
	}
	
	/**
	 * Adds a candidate partner.
	 * @param candidateId
	 */
	public void addCandidate(int candidateId){
		if(this.candidateId==NOT_EXIST || this.preferences.getRank(this.candidateId)>this.preferences.getRank(candidateId))
			this.candidateId=candidateId;
	}
	
	/**
	 * Method used to review the proposals made. If the candidate partner (if any) is preferred than the current
	 * partner, the methods returns true (indicating a new marriage), else it returns false.
	 * @return
	 */
	public int reviewProposals(){
		if(this.candidateId==NOT_EXIST)					// nobody proposed - no marriage
			return NO_MARRIAGE;
		else if(!this.isMarried()){						// somebody proposed and i am single: get married
			this.marry(this.candidateId);
			this.candidateId=NOT_EXIST;
			return NOT_EXIST;
		} else if(this.getCurrentPartnerRank()>this.preferences.getRank(this.candidateId)) {		//somebody proposed, i'm married and i prefer him: get married
			int old_partner=this.partnerId;
			this.divorce();
			this.marry(this.candidateId);
			this.candidateId=NOT_EXIST;
			return old_partner;
		} else																//somebody proposed, i'm married but i love my partner: no alteration
			return NO_MARRIAGE;
	}
	
	@Override
	public String toString() {
		if(this.isMarried()){
			return "<"+this.id+","+this.partnerId+">";
		} else {
			return "<"+this.id+">";
		}
	}

}

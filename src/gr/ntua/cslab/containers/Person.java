package gr.ntua.cslab.containers;

import gr.ntua.cslab.containers.preferences.Preferences;

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
	private Person candidatePartner;
	private int[] proposalsFrom, proposalsTo;
	private int k=Integer.MAX_VALUE;
	
	/**
	 * Constructor expecting the person's preferences
	 * @param preferences
	 */
	public Person(int id, Preferences preferences) {
		this.preferences=preferences;
		this.id=id;
		this.proposalsFrom = new int[preferences.getSize()];
		this.proposalsTo= new int[preferences.getSize()];
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
	
	public int getK(){
		return this.k;
	}
	
	public void setK(int k){
		this.k=k;
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
	
	/**
	 * Returns true if the current person prefers another person more to their current partner. 
	 * The method returns true if the the current person is not married.
	 * @return
	 */
	public boolean isMotivatedToBreakUp(){
		return ((!this.isMarried()) || (this.preferences.getNextRank()<this.getCurrentPartnerRank()));
	}
	
	
	/**
	 * This method works in a similar function like the {@link #propose(Person)} method. The person offers the 
	 * specified person p to get married, but p does not accept at once. P will review the offers at a later time
	 * and it will the determine who is going to get married to. There are some offers though that should be rejected
	 * at once (if the candidate partner is less preferred than the current, or if the are other candidates more preferred
	 * than this. In such cases, if the offer is accepted for review true is returned, else false. 
	 * @param p
	 */
	public boolean offer(Person p){
		this.proposalsTo[p.getId()-1]+=1;
		if(p.getCandidate()==null){
			p.setCandidate(this);
		} else if(p.getCandidateRank() > p.getPreferences().getRank(this.getId())){
			p.setCandidate(this);
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * With this method the persons review any offer it has accepted and decides whether they will get married or not.
	 */
	public boolean reviewOffers(){
		if(this.candidatePartner==null)
			return false;
		
		if(!this.isMarried() || this.getCurrentPartnerRank()>this.getCandidateRank()){		// the two couples are divorced and get married to each other
			this.proposalsFrom[this.candidatePartner.getId()-1]+=1;
			if(this.proposalsFrom[this.candidatePartner.getId()-1]<=this.k){
			this.marry(this.candidatePartner);
			this.preferences.setNext(this.getCurrentPartnerRank());
			this.candidatePartner = null;
			return true;
			}
			return false;
		} else {
			this.candidatePartner = null;
			return false;
		}
	}
	
	/**
	 * Sets the candidate partner
	 * @param p
	 */
	public void setCandidate(Person p){
		this.candidatePartner=p;
	}
	
	/**
	 * Returns the candidate partner
	 * @return
	 */
	public Person getCandidate(){
		return this.candidatePartner;
	}
	
	/**
	 * Returns the candidate rank
	 * @return
	 */
	public int getCandidateRank(){
		return this.getPreferences().getRank(this.candidatePartner.getId());
	}
	
	public boolean hasCycle(){
		for(int i=0;i<this.proposalsTo.length;i++){
			if(this.proposalsTo[i]>this.preferences.getRank(i+1)+1)
				return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String buffer="";
		buffer+=this.id;
		return buffer;

	}
	
	public String toStringProposals(){
		String buffer="";
		for(int i=0;i<this.proposalsTo.length;i++){
			if(this.proposalsTo[i]>0)
				buffer+="{"+(i+1)+"["+this.preferences.getRank(i+1)+"]"+","+this.proposalsTo[i]+"} ";
		}
		buffer+="|";
		for(int i=0;i<this.proposalsFrom.length;i++){
			if(this.proposalsFrom[i]>0)
				buffer+="{"+(i+1)+","+this.proposalsFrom[i]+"} ";
		}
		return buffer;
	}
	
	public String toStringProposalsCycle(){
		String buffer="";
		for(int i=0;i<this.proposalsTo.length;i++){
			if(this.proposalsTo[i]>this.preferences.getRank(i+1)+1)
				buffer+="{"+(i+1)+"["+this.preferences.getRank(i+1)+"]"+","+this.proposalsTo[i]+"} ";
		}
		return buffer;
	}
}

package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;

/**
 * Abstract class used as a base for each stable matching algorithm.
 * @author Giannis Giannakopoulos
 *
 */
public abstract class AbstractStableMatchingAlgorithm {

	protected PersonList men, women;
	private int stepCounter;
	/**
	 * Empty constructor
	 */
	public AbstractStableMatchingAlgorithm() {
		
	}
	
	/**
	 * Constructor which receives as arguments the men and women personlists
	 * @param men
	 * @param women
	 */
	public AbstractStableMatchingAlgorithm(PersonList men, PersonList women){
		this.setMen(men);
		this.setWomen(women);
	}
	
	/**
	 * Men setter
	 * @param men
	 */
	public void setMen(PersonList men){
		this.men=men;
	}
	
	/**
	 * Women setter
	 * @param women
	 */
	public void setWomen(PersonList women) {
		this.women=women;
	}
	
	/**
	 * Men getter
	 * @return
	 */
	public PersonList getMen(){
		return this.men;
	}
	
	/**
	 * Women getter
	 * @return
	 */
	public PersonList getWomen(){
		return this.women;
	}
	
	/**
	 * Abstract method implemented separately be each algorithm
	 */
	public abstract void step();
	
	/**
	 * Default running method: while there exist single people, marry them
	 */
	public void run() {
		this.stepCounter=0;
		while(this.men.hasSinglePeople()){
			this.step();
			this.stepCounter++;
		}
	}
	
	public int getStepCounter(){
		return this.stepCounter;
	}
}

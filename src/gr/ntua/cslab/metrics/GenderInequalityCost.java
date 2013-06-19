package gr.ntua.cslab.metrics;

import java.util.Arrays;

import gr.ntua.cslab.containers.PersonList;

/**
 * Class used to express gender inequality metric. 
 * @author Giannis Giannakopoulos
 *
 */
public class GenderInequalityCost extends AbstractCost{
	
	private int median=-1;
	
	/**
	 * Default constructor: accepts men and women personlists as arguments.
	 * @param men
	 * @param women
	 */
	public GenderInequalityCost(PersonList men, PersonList women) {
		super(men,women);
	}
	
	/**
	 * Returns the difference between the percentages of happy men (a happy man is a man whose partner is ranked by
	 * him with a higher rank than the average rank) minus the percentage of happy women
	 * @return
	 */
	@Override
	public double get(){
		return this.getMenHappinessCount()-this.getWomenHappinessCount();
	}
	
	/**
	 * Returns the percentage of happy men (how many men have ranked their wives with a rank lower than the 
	 * average rank of all people)
	 * @return
	 */
	public double getMenHappinessCount(){
		return this.men.getSatisfiedPeople(this.getGlobalRankMedian());
	}
	
	/**
	 * Returns the percentage of happy women (defined as happy men)<br/>
	 * See also:{@link #getMenHappinessCount()}
	 * @return
	 */
	public double getWomenHappinessCount(){
		return this.women.getSatisfiedPeople(this.getGlobalRankMedian());
	}
	
	private int getGlobalRankMedian(){
		if(this.median!=-1)
			return this.median;
		int[] ranks = new int[this.men.size()+this.women.size()];
		int i=0;
		for(int d:this.men.getRanksArray())
			ranks[i++]=d;
		for(int d:this.women.getRanksArray())
			ranks[i++]=d;
		Arrays.sort(ranks);
		this.median=ranks[ranks.length/2];
		return this.median;
	}
}

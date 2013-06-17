package gr.ntua.cslab.metrics;

import java.util.Arrays;

import gr.ntua.cslab.containers.PersonList;

/**
 * Class used to express gender inequality metric. 
 * @author Giannis Giannakopoulos
 *
 */
public class GenderInequalityMetric {

	private PersonList men, women;
	private int median=-1;
	
	/**
	 * Default constructor: accepts men and women personlists as arguments.
	 * @param men
	 * @param women
	 */
	public GenderInequalityMetric(PersonList men, PersonList women) {
		this.men=men;
		this.women=women;
	}
	
	/**
	 * Returns the difference between the percentages of happy men (a happy man is a man whose partner is ranked by
	 * him with a higher rank than the average rank) minus the percentage of happy women
	 * @return
	 */
	public double getInequalityMetric(){
		return this.getMenHappinessPercentage()-this.getWomenHappinessPercentage();
	}
	
	/**
	 * Returns the percentage of happy men (how many men have ranked their wives with a rank lower than the 
	 * average rank of all people)
	 * @return
	 */
	public double getMenHappinessPercentage(){
		return this.men.getSatisfiedPeople(this.getGlobalRankMedian())/(1.0*this.men.size());
	}
	
	/**
	 * Returns the percentage of happy women (defined as happy men)<br/>
	 * See also:{@link #getMenHappinessPercentage()}
	 * @return
	 */
	public double getWomenHappinessPercentage(){
		return this.women.getSatisfiedPeople(this.getGlobalRankMedian())/(1.0*this.women.size());
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

package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.metrics.GenderInequalityMetric;
import gr.ntua.cslab.metrics.HappinessMetrics;

/**
 * Abstract class used as a base for each stable matching algorithm.
 * @author Giannis Giannakopoulos
 *
 */
public abstract class AbstractStableMatchingAlgorithm {

	protected PersonList men, women;
	private int stepCounter;
	protected int numberOfMarriages=0;
	private long execDuration;
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
		long start=System.currentTimeMillis();
		this.stepCounter=0;
		while(this.men.hasSinglePeople() || this.women.hasSinglePeople()){
			this.step();
			this.stepCounter++;
		}
		this.execDuration=System.currentTimeMillis()-start;
	}
	
	/**
	 * Method used to print the algorithm's performance. It returns the number of steps, the number of marriages,
	 * the average rank according to men's preferences, the average rank according to women's preferences, the average
	 * couple rank and the execution duration (in millis).
	 */
	protected void performance(){
		// Happiness control
		System.out.print(this.stepCounter+"\t");
		System.out.print(this.numberOfMarriages+"\t");
		
		HappinessMetrics metr = new HappinessMetrics(this.men);
		System.out.format("%.4f\t",metr.getAverageRank());
		
		metr = new HappinessMetrics(this.women);
		System.out.format("%.4f\t",metr.getAverageRank());
		System.out.format("%.4f\t",metr.getAverageCoupleRank());
		
		GenderInequalityMetric ineqMetr = new GenderInequalityMetric(this.men, this.women);
		System.out.format("%.4f\t", ineqMetr.getMenHappinessPercentage());
		System.out.format("%.4f\t", ineqMetr.getWomenHappinessPercentage());
		System.out.format("%.4f\t", ineqMetr.getInequalityMetric());
		
		System.out.format("%d",this.execDuration);
	}
	
	public int getStepCounter(){
		return this.stepCounter;
	}
}

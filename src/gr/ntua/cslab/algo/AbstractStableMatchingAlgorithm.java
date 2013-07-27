package gr.ntua.cslab.algo;

import java.util.Iterator;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.data.DatasetReader;
import gr.ntua.cslab.metrics.AbstractCost;
import gr.ntua.cslab.metrics.EgalitarianCost;
import gr.ntua.cslab.metrics.GenderInequalityCost;
import gr.ntua.cslab.metrics.RegretCost;
import gr.ntua.cslab.metrics.SexEqualnessCost;

/**
 * Abstract class used as a base for each stable matching algorithm.
 * @author Giannis Giannakopoulos
 *
 */
public abstract class AbstractStableMatchingAlgorithm {

	protected PersonList men, women;
	protected int stepCounter;
	protected int numberOfMarriages=0;
	private long execDuration;
	
	private int numberOfStepsTopPrintMessage=0; 
	
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
	 * This parameter is about the frequency of the step-based messages that are printed. Default is 0 (no steps).
	 * @param steps
	 */
	public void setStepOfMessage(int steps){
		this.numberOfStepsTopPrintMessage=steps;
	}
	
	/**
	 * Abstract method implemented separately be each algorithm
	 */
	public void step() {
		if(this.menPropose()){
			this.proposeStep(this.men);
		} else {
			this.proposeStep(this.women);
		}
	}
	
	/**
	 * This method will be used to determine which side will be the proposer and which will be the acceptors.
	 * If the method returns true, then men will be the proposers, else women will propose. The method
	 * must be implemented separately be any subclass.
	 * @return
	 */
	protected abstract boolean menPropose();
	
	/**
	 * This method returns the iterator that will determine which people are going to be processed in the algorithm
	 * By default, motivated to break up people are returned. 
	 * @return
	 */
	protected Iterator<Person> getIterator(PersonList proposers){
		return proposers.getMotivatedToBreakUpIterator();
	}
	
	/**
	 * Default running method: while there exist single people, marry them
	 */
	public void run() {
		long start=System.currentTimeMillis();
		this.stepCounter=0;
		while(this.terminationCondition()){
			this.step();
		}
		
		this.execDuration=System.currentTimeMillis()-start;
	}
	
	protected boolean terminationCondition() {
//		return	this.men.hasSinglePeople(); 
		return this.men.hasUnhappyPeople() || this.women.hasUnhappyPeople();
	}
	
	/**
	 * Method used to print the algorithm's performance. It returns the number of steps, the number of marriages,
	 * the average rank according to men's preferences, the average rank according to women's preferences, the average
	 * couple rank and the execution duration (in millis).
	 */
	protected void performance(){
		// Happiness control
		System.out.format("%d\t",this.stepCounter);
		System.out.format("%d\t",this.numberOfMarriages);
		System.out.format("%d\t",this.execDuration);
		
		AbstractCost cost = new RegretCost(this.men, this.women);
		System.out.format("%.4f\t",cost.get());
		cost = new EgalitarianCost(this.men, this.women);
		System.out.format("%.4f\t",cost.get());
		cost = new SexEqualnessCost(this.men, this.women);
		System.out.format("%.4f\t",cost.get());
		cost = new GenderInequalityCost(this.men, this.women);
		System.out.format("%.4f",cost.getPercentage());
	}
	
	protected void stepDiagnostics(){
		System.err.format("%d\t",this.getStepCounter());
		AbstractCost cost = new RegretCost(this.men, this.women);
		System.err.format("%.4f\t",cost.get());
		cost = new EgalitarianCost(this.men, this.women);
		System.err.format("%.4f\t",cost.get());
		cost = new SexEqualnessCost(this.men, this.women);
		System.err.format("%.4f\t",cost.get());
		cost = new GenderInequalityCost(this.men, this.women);
		System.err.format("%.4f\t",cost.get());
		System.err.format("UM %d\t",this.countUnhappy(men));
		System.err.format("UW %d\t",this.countUnhappy(women));
		System.err.println("Stability "+this.quickStable());
	}
	
	private int countUnhappy(PersonList people){
		Iterator<Person> it=people.getIterator();
		int count=0;
		while(it.hasNext()){
			if(it.next().isMotivatedToBreakUp())
				count++;
		}
		return count;
	}
	
	/**
	 * Returns the current step.
	 * @return
	 */
	public int getStepCounter(){
		return this.stepCounter;
	}
	
	/**
	 * This method is executing a full loop of proposals. The proposers who are motivated to break
	 * up propose to the acceptors who then evaluate the proposals and accept or reject their offers.
	 * @param proposers
	 */
	protected void proposeStep(PersonList proposers){
		this.stepCounter++;
		PersonList acceptors;
		if(proposers==this.men)
			acceptors=this.women;
		else
			acceptors=this.men;
		Iterator<Person> it = this.getIterator(proposers);
		
		while(it.hasNext()){
			Person proposer = it.next();
			Person acceptor=acceptors.get(proposer.getPreferences().getNext());
			proposer.offer(acceptor);
		}
		it = acceptors.getIterator();
		while(it.hasNext())
			if(it.next().reviewOffers())
				this.numberOfMarriages+=1;
		if(numberOfStepsTopPrintMessage!=0 && this.getStepCounter()%this.numberOfStepsTopPrintMessage==0){
			this.stepDiagnostics();
		}
	}
	
	protected static void runStaticWithRingPreferences(Class<?> AlgorithmsClass, String[] args) throws InstantiationException, IllegalAccessException{
		AbstractStableMatchingAlgorithm algo=(AbstractStableMatchingAlgorithm) AlgorithmsClass.newInstance();
		int steps=0;
		if(args.length<1){
			System.err.println("I need at least one argument (for ring preferences, two for file arguments)");
			System.exit(1);
		} else {
			try {
				DatasetReader reader = new DatasetReader(new Integer(args[0]));
				algo.setMen(reader.getPeople(DatasetReader.MEN));
				algo.setWomen(reader.getPeople(DatasetReader.WOMEN));
				if(args.length>1){
					steps=new Integer(args[1]);
				}
			} catch (NumberFormatException e) {
				DatasetReader reader = new DatasetReader(args[0]);
				algo.setMen(reader.getPeople());
				reader = new DatasetReader(args[1]);
				algo.setWomen(reader.getPeople());
				if(args.length>2){
					steps=new Integer(args[2]);
				}
			}
		}
		algo.setStepOfMessage(steps);
		algo.run();
		algo.performance();
		algo.stepDiagnostics();
		System.out.println("\nUnstable Marriages:\t"+algo.resultIsStable());
	}
	
	protected int resultIsStable(){
		Iterator<Person> menIt = this.men.getIterator();
		int unstableMarriages=0;
		while(menIt.hasNext()){
			Person p = menIt.next();
			Iterator<Person> womenIt = this.women.getIterator();
			while(womenIt.hasNext()){
				Person current=womenIt.next();
				if(		current.getCurrentPartnerRank()>current.getPreferences().getRank(p.getId()) &&
						p.getCurrentPartnerRank() > p.getPreferences().getRank(current.getId())){
					System.out.println(current.getId()+" prefers "+p.getId());
					unstableMarriages++;
				}
			}
		}
		return unstableMarriages;
	}
	
	protected boolean quickStable(){
		Iterator<Person> menIt = this.men.getIterator();
		while(menIt.hasNext()){
			Person p = menIt.next();
			Iterator<Person> womenIt = this.women.getIterator();
			while(womenIt.hasNext()){
				Person current=womenIt.next();
				if(		current.getCurrentPartnerRank()>current.getPreferences().getRank(p.getId()) &&
						p.getCurrentPartnerRank() > p.getPreferences().getRank(current.getId())){
					return false;
				}
			}
		}
		return true;
	}
}

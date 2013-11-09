package gr.ntua.cslab.algorithms;

import java.util.Iterator;
import java.util.Random;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.data.DatasetReader;
import gr.ntua.cslab.diagnostics.Diagnostics;

public abstract class AbstractSMA {

	protected PersonList men,women;
	protected int stepCounter=0;
	protected Diagnostics diagnostics = null;
	protected int stepsDiagnostics;
	protected boolean cycleDetected=false;
	protected int randomPickSteps=-1;
	private Random rand = new Random();
	protected long executionTime;
	
	public AbstractSMA() {
		
	}
	
	public AbstractSMA(PersonList men, PersonList women){
		this.men=men;
		this.women=women;
	}
	
	/**
	 * @return the men
	 */
	public PersonList getMen() {
		return men;
	}

	/**
	 * @param men the men to set
	 */
	public void setMen(PersonList men) {
		this.men = men;
		if(this.randomPickSteps==-1)
			this.randomPickSteps=this.men.size();
	}

	/**
	 * @return the women
	 */
	public PersonList getWomen() {
		return women;
	}

	/**
	 * @param women the women to set
	 */
	public void setWomen(PersonList women) {
		this.women = women;
	}
	
	private void printDiagnostics(int stepsPrint) {
		this.stepsDiagnostics = stepsPrint;
	}

	public abstract boolean terminationCondition();

	protected abstract boolean menPropose();
	
	public void run(){
		this.diagnostics = new Diagnostics(this.men, this.women);
		this.executionTime = System.currentTimeMillis();
		while(this.terminationCondition()){
			this.step();
		}
		this.executionTime=System.currentTimeMillis()-this.executionTime;
		System.out.print(this.stepCounter+"\t"+this.executionTime+"\t"+this.diagnostics.step());
		System.out.print("\t"+this.diagnostics.resultsIsStable()+"\n");
//		System.err.print(this.stepCounter+"\t"+this.diagnostics.step()+"\n");
	}
	
	public void step(){
//		if(!this.cycleDetected)
//			this.cycleDetected = (this.men.countPeopleWithCycles()>0) || (this.women.countPeopleWithCycles()>0);
//		if(this.)		
		boolean menDoPropose;
		if(this.randomPickSteps==0 || this.stepCounter%this.randomPickSteps==0){
			menDoPropose = this.rand.nextBoolean();
			this.randomPickSteps = this.randomPickSteps*2/3;

		} else {
			menDoPropose = this.menPropose();
		}
		
		if(menDoPropose){
			this.proposeStep(this.men);
		} else {
			this.proposeStep(this.women);
		}
		if(this.stepsDiagnostics!=0 && this.stepCounter%this.stepsDiagnostics==0){
			System.err.println(this.stepCounter+"\t"+(System.currentTimeMillis()-this.executionTime)+"\t"+this.diagnostics.step());
//			System.out.print(this.diagnostics.resultsIsStable()+"\n");
		}
	}
	
	protected void proposeStep(PersonList proposers){
		this.stepCounter+=1;
		PersonList acceptors = (proposers==this.men?this.women:this.men);
		Iterator<Person> it = proposers.getMotivatedToBreakUpIterator();
		while(it.hasNext()){
			Person p =it.next();
			int next=p.getPreferences().getNext();
//			System.out.println(p+" wants "+next);
			p.offer(acceptors.get(next));
		}
		
		it = acceptors.getIterator();
		while(it.hasNext()){
			Person p =it.next();
			if(p.reviewOffers()){
//				System.out.println(p+" is married to "+p.getCurrentPartner());
			}
		}
	}	
	
	// STATIC METHODS USED BY main METHOD OF SUBCLASSES
	
	protected static void runAlgorithm(Class <?extends AbstractSMA> algorithmClass, String[] args){
		try {
			DatasetReader menData = new DatasetReader(args[0]), womenData= new DatasetReader(args[1]);
			int stepsPrint=0;
			if(args.length>=3){
				stepsPrint=new Integer(args[2]);
			}
			AbstractSMA algo=algorithmClass.newInstance();
			algo.setMen(menData.getPeople());
			algo.setWomen(womenData.getPeople());
			algo.printDiagnostics(stepsPrint);
			algo.run();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}

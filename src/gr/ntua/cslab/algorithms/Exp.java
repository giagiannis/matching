package gr.ntua.cslab.algorithms;

import java.util.Iterator;
import java.util.Random;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.metrics.SexEqualnessCost;

public class Exp extends AbstractSMA {

	public Exp() {
	}

	public Exp(PersonList men, PersonList women) {
		super(men, women);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean terminationCondition() {
		return this.men.hasUnhappyPeople() || this.women.hasUnhappyPeople();
	}

	@Override
	protected boolean menPropose() {
		SexEqualnessCost c = new SexEqualnessCost(men, women);
		return (this.men.hasUnhappyPeople() && (c.get()>0 || !this.women.hasUnhappyPeople()));
	}
	
	@Override
	public void step() {
		boolean menPropose=this.menPropose();
		if(!cycleDetected)
			cycleDetected=(this.men.countPeopleWithCycles()>0 || this.women.countPeopleWithCycles()>0);
		else{
			Random ran = new Random();
			menPropose=(this.stepCounter%10==0?ran.nextBoolean():menPropose);
		}
		this.proposeStep((menPropose?this.men:this.women));
		
		if(this.stepsDiagnostics!=0 && this.stepCounter%this.stepsDiagnostics==0){
			System.out.print(this.stepCounter+"\t"+this.diagnostics.step()+"\t");
			System.out.print(this.diagnostics.resultsIsStable()+"\n");
//			System.out.println("In cycle (m,w):"+this.men.countPeopleWithCycles()+","+this.women.countPeopleWithCycles());
//			System.out.println("Unhappy (m,w):"+this.men.hasUnhappyPeople()+","+this.women.hasUnhappyPeople());
//			System.out.print("\t");
//			Iterator<Person> it = this.men.getIterator();
//			while(it.hasNext()){
//				Person p =it.next();
//				if(p.hasCycle()){
//					System.out.print(p.toStrCycle()+"\t");
//				}
//			}
//			it = this.women.getIterator();
//			while(it.hasNext()){
//				Person p =it.next();
//				if(p.hasCycle()){
//					System.out.print(p.toStrCycle()+"\t");
//				}
//			}
//			System.out.println();
		}
		

	}
	
	@Override
	protected void proposeStep(PersonList proposers) {
		this.stepCounter+=1;
		PersonList acceptors = (proposers==this.men?this.women:this.men);
		Iterator<Person> it = proposers.getMotivatedToBreakUpIterator();
		while(it.hasNext()){
			Person p = it.next();
			int next=p.getPreferences().getNext();
			p.offer(acceptors.get(next));
		}
		
		it = acceptors.getIterator();
		while(it.hasNext()){
			it.next().reviewOffers();
		}
	}
	
	public static void main(String[] args) {
		AbstractSMA.runAlgorithm(Exp.class, args);
	}

}

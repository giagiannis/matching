package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.data.DatasetReader;

import java.util.Iterator;
import java.util.Random;

/**
 * Implementation of a female-friendly algorithm, based on Shapley-Roth algorithm.
 * @author Giannis Giannakopoulos
 *
 */
public class ESMA extends AbstractStableMatchingAlgorithm{

	private Random random;
//	private int menSteps;
	
	public ESMA(PersonList men, PersonList women) {
		super(men,women);
		this.random=new Random();
//		this.menSteps=0;
	}
	
	@Override
	public void step() {
		if(this.random.nextBoolean()){
			this.menProposeStep();
//			this.menSteps++;
		} else {
			this.womenProposeStep();
		}
	}
	
	public void menProposeStep(){
		Iterator<Person> it = this.men.getSinglePersonIterator();
		while(it.hasNext()){
			Person man = it.next(), woman =this.women.get(man.getPreferences().getNextPreference()); 
			if(man.propose(woman)){
				this.numberOfMarriages+=1;
			}
		}
	}
	
	public void womenProposeStep(){
		Iterator<Person> it = this.women.getSinglePersonIterator();
		while(it.hasNext()){
			Person woman = it.next(), man =this.men.get(woman.getPreferences().getNextPreference()); 
			if(woman.propose(man)){
				this.numberOfMarriages+=1;
			}
		}
	}
	
	public static void main(String[] args) {
		if(args.length<2){
			System.err.println("I need men and women preferences!");
			System.exit(1);
		}
		
		DatasetReader reader = new DatasetReader(args[0]);
		PersonList men = reader.getPeople();
		reader = new DatasetReader(args[1]);
		PersonList women = reader.getPeople();
		ESMA algo = new ESMA(men, women);
		algo.run();

		algo.performance();
//		System.err.println("\nMen steps:"+algo.menSteps);
//		System.err.println("Women steps:"+(algo.getStepCounter()-algo.menSteps));
	}

	
}
package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.data.DatasetReader;

import java.util.Iterator;

/**
 * Implementation of a female-friendly algorithm, based on Shapley-Roth algorithm.
 * @author Giannis Giannakopoulos
 *
 */
public class FemaleStableMarriageAlgorithm extends AbstractStableMatchingAlgorithm{

	public FemaleStableMarriageAlgorithm(PersonList men, PersonList women) {
		super(men,women);
	}
	
	@Override
	public void step() {
		if(this.getStepCounter()%2==0)
			this.menProposeStep();
		else
			this.womenProposeStep();
	}
	
	public void menProposeStep(){
		Iterator<Person> it = this.men.getSinglePersonIterator();
		while(it.hasNext()){
			Person man = it.next(), woman =this.women.get(man.getPreferences().getNextPreference()); 
			if(man.propose(woman)){
				System.out.println(man +" marries "+woman);
			}
		}
		System.out.println("Left men:\t"+men.getNumberOfSingles());
		System.out.println("Left women:\t"+women.getNumberOfSingles());
	}
	
	public void womenProposeStep(){
		Iterator<Person> it = this.women.getSinglePersonIterator();
		while(it.hasNext()){
			Person woman = it.next(), man =this.men.get(woman.getPreferences().getNextPreference()); 
			if(woman.propose(man)){
				System.out.println(man +" marries "+woman);
			}
		}
		System.out.println("Left men:\t"+men.getNumberOfSingles());
		System.out.println("Left women:\t"+women.getNumberOfSingles());
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
		AbstractStableMatchingAlgorithm algo = new FemaleStableMarriageAlgorithm(men, women);
		algo.run();
		System.out.println("Number of steps:\t"+algo.getStepCounter());
	}

	
}

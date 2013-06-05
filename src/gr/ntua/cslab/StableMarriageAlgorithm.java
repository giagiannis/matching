package gr.ntua.cslab;

import java.util.Iterator;

/**
 * This class implements the classic algorithm which solves the Stable Marriage Problem proposed by Shapley and Roth.
 * The algorithm is given two sets of preferences (corresponding to male and female preferences) and estimates a stable 
 * matching.
 * @author Giannis Giannakopoulos
 *
 */
public class StableMarriageAlgorithm extends StableMatchingAlgorithm{

	
	public StableMarriageAlgorithm(PersonList men, PersonList women) {
		super(men,women);
	}
	
	
	public void step(){
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
	
	
	public static void main(String[] args) {
		if(args.length<2){
			System.err.println("I need men and women preferences!");
			System.exit(1);
		}
		
		DatasetReader reader = new DatasetReader(args[0]);
		PersonList men = reader.getPeople();
		reader = new DatasetReader(args[1]);
		PersonList women = reader.getPeople();
		StableMarriageAlgorithm algo = new StableMarriageAlgorithm(men, women);
		algo.run();
	}
}

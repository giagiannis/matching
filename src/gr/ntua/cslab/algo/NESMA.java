package gr.ntua.cslab.algo;

import java.util.Iterator;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;

/**
 * Naive ESMA: in this algorithm only single people are picked to make the next move.
 * @author Giannis Giannakopoulos
 *
 */
public class NESMA extends AbstractStableMatchingAlgorithm {

	public NESMA() {
		
	}

	public NESMA(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	protected boolean menPropose() {
		return (this.getStepCounter()%2==0);
	}
	
	@Override
	protected void proposeStep(PersonList proposers) {
		this.stepCounter+=1;
		PersonList acceptors;
		if(proposers==this.men)
			acceptors=this.women;
		else
			acceptors=this.men;
		Iterator<Person> it = proposers.getSinglePersonIterator();
		while(it.hasNext()){
			Person proposer = it.next();
			Person acceptor=acceptors.get(proposer.getPreferences().getNext());
			proposer.offer(acceptor);
		}
		it = acceptors.getIterator();
		while(it.hasNext())
			if(it.next().reviewOffers())
				this.numberOfMarriages+=1;
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		AbstractStableMatchingAlgorithm.runStaticWithRingPreferences(NESMA.class, args);
	}
}

package gr.ntua.cslab.metrics;

import java.util.Iterator;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;

public class RegretCost extends AbstractCost {

	/**
	 * Default constructor calling the {@link AbstractCost} constructor.
	 * @param men
	 * @param women
	 */
	public RegretCost(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	public double get() {
		Iterator<Person> it = this.men.getIterator();
		int max=0;
		while(it.hasNext()){
			Person current=it.next();
			if(current.getCurrentPartner()!=null){
				int a=current.getCurrentPartnerRank(), b=current.getCurrentPartner().getCurrentPartnerRank();
				max=Math.max(max, Math.max(a, b));
			}
		}
		return max;
	}
}

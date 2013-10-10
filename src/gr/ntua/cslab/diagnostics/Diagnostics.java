package gr.ntua.cslab.diagnostics;

import java.util.Iterator;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.metrics.EgalitarianCost;
import gr.ntua.cslab.metrics.GenderInequalityCost;
import gr.ntua.cslab.metrics.RegretCost;
import gr.ntua.cslab.metrics.SexEqualnessCost;

/**
 * Class used to provide diagnostics upon the stable matching algorithms.
 * @author Giannis Giannakopoulos
 *
 */
public class Diagnostics {

	private PersonList men, women;

	public Diagnostics(PersonList men, PersonList women) {
		this.men=men;
		this.women=women;
	}
	
	public String step(){
		String buffer="";
		buffer+=String.format("%.0f\t", new RegretCost(men, women).get());
		buffer+=String.format("%.5f\t", new EgalitarianCost(men, women).get());
		buffer+=String.format("%.5f\t", Math.abs(new SexEqualnessCost(men, women).get()));
		buffer+=String.format("%.0f", new GenderInequalityCost(men, women).get());
		return buffer;
	}
	
	public boolean resultsIsStable(){
		Iterator<Person> menIterator =this.men.getIterator();
		while(menIterator.hasNext()){
			Iterator<Person> womenIterator =this.women.getIterator();
			Person man = menIterator.next();
			while(womenIterator.hasNext()){
				Person woman = womenIterator.next();
				if(	man.getPreferences().getRank(woman.getId())<man.getCurrentPartnerRank() &&
					woman.getPreferences().getRank(man.getId())<woman.getCurrentPartnerRank()){
					return false;
				}
			}
		}
		return true;
	}

}

package gr.ntua.cslab.algo;

import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.metrics.GenderInequalityCost;

public class AAESMAIneq extends AAESMA {

	public AAESMAIneq() {
		// TODO Auto-generated constructor stub
	}

	public AAESMAIneq(PersonList men, PersonList women) {
		super(men, women);
	}
	
	@Override
	protected boolean menPropose() {
		return(new GenderInequalityCost(men, women).get()<=0);
	}

}

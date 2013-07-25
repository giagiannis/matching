package gr.ntua.cslab.metrics;

import gr.ntua.cslab.containers.PersonList;

public class EgalitarianCost extends AbstractCost {

	/**
	 * Default constructor calling the {@link AbstractCost} constructor.
	 * @param men
	 * @param women
	 */
	public EgalitarianCost(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	public double get() {
		return (EgalitarianCost.getRanksSum(this.men)+EgalitarianCost.getRanksSum(this.women))/2.0;
	}
	
	@Override
	public double getPercentage() {
		return super.getPercentage()/2.0;
	}
}

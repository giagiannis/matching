package gr.ntua.cslab.metrics;

import gr.ntua.cslab.containers.PersonList;

public class SexEqualnessCost extends AbstractCost {

	/**
	 * Default constructor calling the {@link AbstractCost} constructor.
	 * @param men
	 * @param women
	 */
	public SexEqualnessCost(PersonList men, PersonList women) {
		super(men, women);
	}

	@Override
	public double get() {
		return SexEqualnessCost.getRanksSum(this.men)-SexEqualnessCost.getRanksSum(this.women);
	}

}

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

	/**
	 * Ranks of men-ranks of women. Positive value means that men are less happy than women, 
	 * while negative value means that women are less happy than men.
	 */
	@Override
	public double get() {
		return SexEqualnessCost.getRanksSum(this.men)-SexEqualnessCost.getRanksSum(this.women);
	}

}

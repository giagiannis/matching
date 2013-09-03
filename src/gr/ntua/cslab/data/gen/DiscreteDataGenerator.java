package gr.ntua.cslab.data.gen;

import java.util.Collections;
import java.util.LinkedList;

public class DiscreteDataGenerator extends Generator {

	private int hotRegion, coldRegion;
	
	public DiscreteDataGenerator() {
		// does noting
	}

	public DiscreteDataGenerator(int count) {
		super(count);
	}
	
	public void setHotRegion(int hotRegion){
		this.hotRegion=hotRegion;
		this.coldRegion = this.datasetSize - this.hotRegion;
	}
	
	public void setColdRegion(int coldRegion){
		this.coldRegion=coldRegion;
		this.hotRegion = this.datasetSize - this.coldRegion;
	}

	@Override
	protected LinkedList<Integer> line() {
		LinkedList<Integer> hot = new LinkedList<Integer>();
		for(int i=1;i<=this.hotRegion;i++)
			hot.add(i);
		Collections.shuffle(hot);
		LinkedList<Integer> cold = new LinkedList<Integer>();
		for(int i=hotRegion+1;i<=this.datasetSize;i++)
			cold.add(i);
		Collections.shuffle(cold);
		for(Integer d:cold)
			hot.add(d);
		return hot;
	}
	
	public static void main(String[] args) {
		DiscreteDataGenerator gen = new DiscreteDataGenerator();
		if(args.length<1){
			System.err.println("I need size of dataset");
			System.exit(1);
		}
		gen.setDatasetSize(new Integer(args[0]));
		if(args.length<2){
			System.err.println("No hot region set!");
			System.exit(1);
		} else {
			gen.setHotRegion(new Integer(args[1]));
		}
		gen.create();

	}

}

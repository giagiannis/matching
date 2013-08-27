package gr.ntua.cslab.data.gen;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Class used to create datasets. The class will create text files containing person's preferences
 * following a distribution (for the time being, uniform distribution is only supported, later on
 * other distributions may be added as well).
 * @author Giannis Giannakopoulos
 *
 */

public class RandomDataGenerator extends Generator{
	
	public RandomDataGenerator(){
		super();
	}
	
	public RandomDataGenerator(int count){
		super(count);
	}
	
	@Override
	protected LinkedList<Integer> line(){
		LinkedList<Integer> res = new LinkedList<Integer>();
		for(int i=1;i<=this.datasetSize;i++)
			res.add(i);
		Collections.shuffle(res);
		return res;
	}
	
	public static void main(String[] args) {
		Generator.runStatic(RandomDataGenerator.class, args);		
	}

}

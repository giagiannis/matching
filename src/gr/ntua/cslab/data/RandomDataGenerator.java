package gr.ntua.cslab.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	protected void line(){
		List<String> ordered = new ArrayList<String>();
		for(Integer i=1;i<this.datasetSize+1;i++)
			ordered.add(i.toString());
		Collections.shuffle(ordered);
		for(String d:ordered)
			this.out.print(d+" ");
		this.out.println();
	}
	
	public static void main(String[] args) {
		if(args.length<1){
			System.err.println("I need size of dataset");
			System.exit(1);
		}
		
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setDatasetSize(new Integer(args[0]));
		if(args.length>1)
			gen.setOutputFile(args[1]);
		gen.create();
		
	}

}

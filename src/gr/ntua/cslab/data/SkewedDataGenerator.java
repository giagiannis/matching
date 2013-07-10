package gr.ntua.cslab.data;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
//import java.util.Random;

public class SkewedDataGenerator extends Generator{

//	private Random rand;
	
	public SkewedDataGenerator() {
		super();
//		this.rand = new Random();
	}
	
	public SkewedDataGenerator(int count) {
		super(count);
//		this.rand = new Random();
		
	}
	
	
	@Override
	protected void line() {
		List<Entry<Integer, Integer>> scores = new ArrayList<Entry<Integer,Integer>>(this.datasetSize);
		for(int i=1;i<=this.datasetSize;i++)
			scores.add(new SimpleEntry<Integer, Integer>(i,this.datasetSize-i));
//		Collections.sor
		System.out.println(scores);
		Collections.sort(scores,new EntryComparator());
		System.out.println(scores);
	}

	public static void main(String[] args) {
		if(args.length<1){
			System.err.println("I need data number of people param");
			System.exit(1);
		}
		
		Generator gen = new SkewedDataGenerator(new Integer(args[0]));
		if(args.length>1)
			gen.setOutputFile(args[1]);
		gen.create();
		
	}
}

class EntryComparator implements Comparator<Entry<Integer,Integer>>{

	@Override
	public int compare(Entry<Integer,Integer> o1, Entry<Integer,Integer> o2) {
		if(o1.getValue()>o2.getValue())
			return 1;
		else if(o1.getValue()<o2.getValue())
			return -1;
		else
			return 0;
	}
	
}
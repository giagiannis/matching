package gr.ntua.cslab.data;

import java.io.FileNotFoundException;
import java.io.PrintStream;
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

public class Generator {
	private int datasetSize;
	private PrintStream out=System.out;
	
	public Generator(){
		
	}
	
	/**
	 * Set the output file that will be created. If no file is specified, the default output will be used
	 * (stdout).
	 * @param fileName
	 */
	public void setOutputFile(String fileName){
		try {
			this.out = new PrintStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * datasetSize getter
	 * @return
	 */
	public int getDatasetSize() {
		return datasetSize;
	}

	/**
	 * datasetSize setter
	 * @param datasetSize
	 */
	public void setDatasetSize(int datasetSize) {
		this.datasetSize = datasetSize;
	}
	
	private String line(){
		String buffer="";
		List<String> ordered = new ArrayList<String>();
		for(Integer i=1;i<this.datasetSize+1;i++)
			ordered.add(i.toString());
		Collections.shuffle(ordered);
		for(String d:ordered)
			this.out.print(d+" ");
		this.out.println();
		return buffer;
	}
	
	/**
	 * Creates the dataset.
	 */
	public void create(){
		this.out.println(this.datasetSize);
		for(int i=0;i<this.datasetSize;i++)
			this.line();
		this.out.close();
	}
	
	public static void main(String[] args) {
		if(args.length<1){
			System.err.println("I need size of dataset");
			System.exit(1);
		}
		
		Generator gen = new Generator();
		gen.setDatasetSize(new Integer(args[0]));
		if(args.length>1)
			gen.setOutputFile(args[1]);
		gen.create();
//		gen.myLine();
//		Generator gen = new Generator();
//		gen.setDatasetSize(new Integer(args[0]));
//		gen.lineWithSet();
		
	}

}

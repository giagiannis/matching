package gr.ntua.cslab.data;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Random;

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
		LinkedList<String> ordered = new LinkedList<String>();
		for(Integer i=1;i<this.datasetSize+1;i++)
			ordered.add(i.toString());
		Random rand = new Random();
		
		String buffer="";
		while(!ordered.isEmpty())
			buffer+=ordered.remove(rand.nextInt(ordered.size()))+" ";
		buffer=buffer.substring(0, buffer.length()-1);
		return buffer;
	}
	
	/**
	 * Creates the dataset.
	 */
	public void create(){
		this.out.println(this.datasetSize);
		for(int i=0;i<this.datasetSize;i++)
			this.out.println(this.line());
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
		
	}

}

package gr.ntua.cslab.data;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public abstract class Generator {

	protected int datasetSize;
	protected PrintStream out = System.out;
	
	public Generator() {
		// TODO Auto-generated constructor stub
	}
	
	public Generator(int count){
		this.setDatasetSize(count);	
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
	
	protected abstract void line();
	
	/**
	 * Creates the dataset.
	 */
	public void create(){
		this.out.println(this.datasetSize);
		for(int i=0;i<this.datasetSize;i++)
			this.line();
		this.out.close();
	}
}

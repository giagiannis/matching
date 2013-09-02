package gr.ntua.cslab.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Class used to aggregate algorithms output. The purpose of this class is to take as input
 * a number of output files (output as in algorithms diagnostics), aggregate them an create an average output
 * file.
 * @author Giannis Giannakopoulos
 *
 */
public class Aggregator {

//	private 
	private String[][] fileContents;
	private int numberOfLines;
	private PrintStream out;
	/**
	 * Default constructor. The filenames of the output files must be provided as argument.
	 * @param fileNames
	 */
	public Aggregator(String[] fileNames) {
		this.fileContents = new String[fileNames.length][];
		this.numberOfLines = Aggregator.estimateNumberOfLines(fileNames[0]);
		for(int i=0;i<this.fileContents.length;i++){
			this.readFile(fileNames[i], i);
		}
		this.out = System.out;
	}
	public void create(){
//		String output="";
		for(int i=0;i<this.numberOfLines;i++)
			this.out.print(this.getAverageOfLine(i));
	}
	
	public void setOutputFile(String filename){
		try {
			this.out = new PrintStream(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void readFile(String fileName, int fileNumber){
		try {
			this.fileContents[fileNumber]=new String[numberOfLines];
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String buffer;
			int i=0;
			while(reader.ready()){
				buffer=reader.readLine();
				if(buffer.charAt(0)!='#')
					this.fileContents[fileNumber][i++]=buffer;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int estimateNumberOfLines(String file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String buffer;
			int i=0;
			while(reader.ready()){
				buffer=reader.readLine();
				if(buffer.charAt(0)!='#')
					i++;
			}
			reader.close();
			return i;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private String getAverageOfLine(int line){
		String buffer="";
		int lim = this.fileContents[0][line].split("\t").length;
		for(int i=0;i<lim;i++){
			String[] temp = new String[this.fileContents.length];
			for(int j=0;j<temp.length;j++)
				temp[j]=this.fileContents[j][line].split("\t")[i];
			buffer+=this.getAverageOfColumn(temp)+"\t";
		}
		buffer+="\n";
		return buffer;
	}
	
	private String getAverageOfColumn(String[] values){
		Double sum=0.0;
		double min=Double.MAX_VALUE, max=Double.MIN_VALUE;
		for(int i=0;i<values.length;i++){
			try{
				int value=new Integer(values[i].trim());
				sum+=Math.abs(value);
				max=(value>max?value:max);
				min=(value<min?value:min);
			} catch (NumberFormatException e){
				double value=new Double (values[i].trim());
				sum+=Math.abs(value);
				max=(value>max?value:max);
				min=(value<min?value:min);
			}
		}
		sum/=values.length;
//		System.out.print("Max:\t"+max+"\t");
//		System.out.print("Sum:\t"+sum+"\t");
//		System.out.println("Min:\t"+min);
		return String.format("%.5f",sum);
	}
	
	public static void main(String[] args) {
		if(args.length<1){
			System.err.println("I need file names as input and output file as argument");
			System.exit(1);
		}
	
		Aggregator agg = new Aggregator(args);
		agg.create();
	}
}

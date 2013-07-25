package gr.ntua.cslab.hama.algo;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.BSPJob;
import org.apache.hama.bsp.NullInputFormat;
import org.apache.hama.bsp.TextOutputFormat;

public abstract class AbstractAlgorithm extends Worker{

	private static boolean	verbose=true;
	
	public static int 	NO_SINGLES_CONDITION=1,
						NO_UNHAPPY_CONDITION=2;
	
	private int workers=2;
	private String dataMen, dataWomen;
	private int inputSize;

	private long executionTime;
	

	public AbstractAlgorithm() {
		super();
	}
	
	public void setInput(String men, String women){
		this.dataMen=men;
		this.dataWomen=women;
	}
	
	public void setInput(int datasetSize){
		this.inputSize=datasetSize;
	}
	
	public void setNumberOfWorkers(int workers){
		this.workers=workers;
	}
	
	public void run() throws IOException, ClassNotFoundException, InterruptedException{
		BSPJob job = new BSPJob(new HamaConfiguration(),this.getAlgorithmClass());
		job.setJobName(this.getAlgorithmClass().getCanonicalName());
		if(this.inputSize>0){
			job.getConfiguration().setBoolean("ring", true);
			job.getConfiguration().setInt("datasetSize", this.inputSize);
		} else {
			job.getConfiguration().setBoolean("ring", false);
			job.getConfiguration().set("men", this.dataMen);
			job.getConfiguration().set("women", this.dataWomen);
		}
		job.setBspClass(this.getAlgorithmClass());
		job.setJarByClass(this.getAlgorithmClass());
		
		job.setInputFormat(NullInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		job.setOutputPath(new Path("temp_hama_job"));
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setNumBspTask(this.workers);
		long startTime=System.currentTimeMillis();
		job.waitForCompletion(verbose);
		this.executionTime = System.currentTimeMillis()-startTime;
	}
	
	public long getExecutionTime(){
		return this.executionTime;
	}
	
	public static void setVerbose(boolean flag){
		AbstractAlgorithm.verbose=flag;
	}
	
	public Class<? extends AbstractAlgorithm> getAlgorithmClass(){
		return this.getClass();
	}
		
	public static void execute(String args[], Class<? extends AbstractAlgorithm> myClass) throws Exception {
		AbstractAlgorithm algo = myClass.getConstructor().newInstance(); 
		try {
			algo.setInput(new Integer(args[0]));
			if(args.length>1){
				algo.setNumberOfWorkers(new Integer(args[1]));
			}
			System.err.println("Using ring preferences with size:\t"+new Integer(args[0]));
		} catch (NumberFormatException e){
			algo.setInput(args[0], args[1]);
			if(args.length>2){
				algo.setNumberOfWorkers(new Integer(args[2]));
			}
			System.err.println("Using in memory preferences:\t"+args[0]+", "+args[1]);
		}
		
		algo.run();
		
		System.out.println(algo.getExecutionTime()/1000.0);
	}
}

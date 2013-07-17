package gr.ntua.cslab.hama.data;

import gr.ntua.cslab.hama.containers.Person;
import gr.ntua.cslab.hama.containers.PersonList;
import gr.ntua.cslab.hama.containers.preferences.MemoryPreferences;
import gr.ntua.cslab.hama.containers.preferences.RingPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Class used to read input files from HDFS. This class provides methods to read a file, 
 * keep certain (sequential) parts of it in memory and returns PersonLists.
 * @author Giannis Giannakopoulos
 *
 */
public class DataReader {

	private BufferedReader reader;
	private int chunks;
	private int datasetSize;
	
	private boolean dataInMemory;
	
	private int chunkid;
	/**
	 * Constructor used to read a file.
	 * @param filename the name of the file to be read
	 * @param chunks how many chunks exist
	 * @param chunkid the chunk id (from 0 upto chunks-1)
	 */
	public DataReader(String filename, int chunks, int chunkid) {
		this.chunks=chunks;
		this.dataInMemory=true;
		try {
			FSDataInputStream in=FileSystem.get(new Configuration()).open(new Path(filename));
			this.reader = new BufferedReader(new InputStreamReader(in));
			this.datasetSize = new Integer(this.reader.readLine().trim());
			this.chunkid=chunkid;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Different constructor for data reader class used to load {@link RingPreferences}. 
	 * @param datasetSize
	 * @param chunks
	 * @param chunkid
	 */
	public DataReader(int datasetSize, int chunks, int chunkid){
		this.dataInMemory=false;
		this.datasetSize=datasetSize;
		this.chunkid=chunkid;
		this.chunks=chunks;
		
	}
	public PersonList getPeople(){
		PersonList list = new PersonList(this.getChunkSize(), this.getOffset());
		if(this.dataInMemory){
			try {
				int id=0;
				for(int i=1;i<this.getOffset();i++){
					id++;
					this.reader.readLine();
				}
				for(int i=0;i<this.getChunkSize();i++){
					id++;
					list.add(new Person(id, new MemoryPreferences(this.reader.readLine().split(" "), true)));

				}
				while(this.reader.ready() && (this.chunkid==this.chunks-1)){
					id++;
					list.add(new Person(id, new MemoryPreferences(this.reader.readLine().split(" "), true)));
				}
				this.reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			int id=this.getOffset();
			for(int i=0;i<this.getChunkSize();i++){
				list.add(new Person(id++, new RingPreferences(this.datasetSize,5,1)));
			}
		}
		
		return list;
	}
	
	public int getDatasetSize(){
		return this.datasetSize;
	}
	
	private int getChunkSize(){
		if(this.chunkid!=this.chunks-1)
			return this.datasetSize/this.chunks;
		else
			return this.datasetSize/this.chunks+this.datasetSize%this.chunks;
	}
	
	private int getOffset(){
		return this.chunkid*(this.datasetSize/this.chunks)+1;

	}
	
	public static void main(String[] args) {
		DataReader reader = new DataReader(args[0], new Integer(args[1]), new Integer(args[2]));
		System.out.println(reader.getPeople());
	}

}

package gr.ntua.cslab.data;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.containers.preferences.InMemoryPreferences;
import gr.ntua.cslab.containers.preferences.RingPreferences;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.util.Random;

/**
 * Dataset reader. The dataset must be a square matrix containing only the preferences. Ids are given automatically 
 * (starting from 1) one to each line.
 * @author Giannis Giannakopoulos
 *
 */
public class DatasetReader {

	public static int MEN=0, WOMEN=1;
	
	private BufferedReader reader;
	private boolean sortedByRank=true;
	private int size;

	private char type;
	
	/**
	 * Alternative constructor, used to create a preference list using ring preferences.
	 */
	public DatasetReader(int size) {
		this.size=size;
	}
	
	
	public DatasetReader(String filename){
		if(filename.contains("women"))
			this.type='w';
		else
			this.type='m';
		try {
			this.reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * If the flag is set, then the dataset is sorted by rank and the value is the id (default), else
	 * they are sorted by id and the value is the specified rank. 
	 * @param flag
	 */
	public void setSortedByRank(boolean flag){
		this.sortedByRank=flag;
	}
	
	private int[] getIntArray(String buffer){
		String[] splits = buffer.split(" ");
		int[] res = new int[splits.length];
		for(int i=0;i<res.length;i++)
			res[i]=new Integer(splits[i]);
		return res;
	}
	
	/**
	 * Returns the PersonList of people.
	 * @return
	 */
	public PersonList getPeople(){
		PersonList list = null;
		try {
			int numOfPeople=new Integer(this.reader.readLine().trim());
			list = new PersonList(numOfPeople);
			int id=1;
			while(this.reader.ready()){
				list.add(new Person(this.type,id++, new InMemoryPreferences(this.getIntArray(this.reader.readLine()), this.sortedByRank)));
			}
			this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public PersonList getPeople(int type){
		// the parameters of type creation will be modified on demand in the future
//		Random random = new Random();
		PersonList list=new PersonList(this.size);
		if(type==MEN){
			for(int i=1; i<this.size+1;i++)
				list.add(new Person(this.type, i, new RingPreferences(this.size, 5, i%(this.size/8)+1)));
		} else {
			for(int i=1; i<this.size+1;i++)
				list.add(new Person(this.type, i, new RingPreferences(this.size, 5, i%(this.size/8)+1)));
		}
		return list;
	}
}
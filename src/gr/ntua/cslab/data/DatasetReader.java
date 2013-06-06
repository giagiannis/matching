package gr.ntua.cslab.data;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;
import gr.ntua.cslab.containers.Preferences;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Dataset reader. The dataset must be a square matrix containing only the preferences. Ids are given automatically 
 * (starting from 1) one to each line.
 * @author Giannis Giannakopoulos
 *
 */
public class DatasetReader {

	private BufferedReader reader;
	private boolean sortedByRank=true;
	
	public DatasetReader(String filename){
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
				list.add(new Person(id++, new Preferences(this.getIntArray(this.reader.readLine()), this.sortedByRank)));
			}
			this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	
}
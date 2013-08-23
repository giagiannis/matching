package gr.ntua.cslab.data;

import java.util.LinkedList;

public class GaussDataGenerator extends Generator {

	public GaussDataGenerator() {
		// TODO Auto-generated constructor stub
	}

	public GaussDataGenerator(int count) {
		super(count);
	}

	@Override
	protected LinkedList<Integer> line() {
		return null;
	}
}

class ScoreLabel implements Comparable<ScoreLabel>{
	private int label;
	private int score;
	
	@Override
	public int compareTo(ScoreLabel o) {
		if(this.score < o.score)
			return -1;
		else if(this.score > o.score)
			return 1;
		else
			return 0;
	}
	
	
}

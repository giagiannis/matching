package gr.ntua.cslab.hama.containers.preferences;

/**
 * Ring preferences -not stored in memory but computed at runtime-.
 * @author Giannis Giannakopoulos
 *
 */
public class RingPreferences implements Preferences {

	private int count=0;
	private int index;
	private int step;
	private int base=0;
	private int size;
	private int offset=0;
	
	private int bigRound=0, smallRound=0;
	private int modulo;
	
	
	public RingPreferences() {
		
	}

	/**
	 * Default constructor. Expected parameter: the size of 
	 * @param size
	 */
	public RingPreferences(int size, int step, int offset){
		this.size=size;
		this.step=step;
		this.offset = offset;
		this.index=0;
		this.base=0;
		if(this.offset>this.size)
			this.offset=this.size;
		if(this.step>this.size)
			this.step=this.size;
		this.bigRound=this.size/this.step;
		this.smallRound=this.size/this.step;
		this.modulo = this.size%this.step;
		if(this.modulo!=0)
			this.bigRound+=1;
	}
	
	@Override
	public int getNext() {
		this.count++;
		int current=this.index;
		this.nextIndex();
		return this.getLabel(current);
	}

	@Override
	public boolean hasMore() {
		return (this.count<this.size);
	}

	@Override
	public int getNextRank() {
		return this.count+1;
	}

	@Override
	public int getRank(int id) {
		int index=this.getIndex(id);
		int mod = index%step;
		int div = index/step;
		int rank=0;
		if(mod==0){			//first round
			rank=0;
		} else if(mod>=this.modulo){			//round where the smaller
			rank=this.modulo*this.bigRound;
			rank+=(mod-this.modulo)*this.smallRound;
		} else {
			rank=mod*this.bigRound;
		}
		rank+=div+1;
		return rank;
	}

	@Override
	public void reset() {
		this.count=0;
		this.index=0;
	}
	
	private int getIndex(int label){
		int diff=label-this.offset;
		if(diff<0)
			return this.size+diff;
		else
			return diff;
	}

	private int nextIndex(){
		this.index=this.index+step;
		if(this.index>=this.size)
			this.rebase();
		return this.index;
	}
	
	private void rebase() {
		this.base+=1;
		this.index=this.base;
	}

	private int getLabel(int index){
		return (index+this.offset-1)%this.size+1;
	}
}

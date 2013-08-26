package gr.ntua.cslab.containers.preferences;

import java.util.HashMap;
import java.util.Map.Entry;

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
	
	private int getIndex(int label){
		int diff=label-this.offset;
		if(diff<0)
			return this.size+diff;
		else
			return diff;
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
	public int getNextRank() {	
		return this.count+1;
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

//	public static void main(String[] args) {
//		RingPreferences pref = new RingPreferences(new Integer(args[0]), new Integer(args[1]), new Integer(args[2]));
////		Set<Integer> set = new HashSet<Integer>();
////		HashMap<Integer, Integer> first=new HashMap<Integer, Integer>();
////		int count=1;
//		while(pref.hasMore())
////			first.put(count++, pref.getNext());
//			System.out.print(pref.getNext()+" ");
//		System.out.println();
//		pref.setNext(2);
//		while(pref.hasMore())
////			first.put(count++, pref.getNext());
//			System.out.print(pref.getNext()+" ");
//		System.out.println();
//		
//		HashMap<Integer, Integer> second=new HashMap<Integer, Integer>();
//		for(Integer i=1;i<=new Integer(args[0]);i++){
//			second.put(pref.getRank(i), i);
////			System.out.print(pref.getRank(i)+" ");pref.idAnalysis(i);
//		}
////		System.out.println(second);
////		System.out.println("===================================");
////		System.out.println("Valid:\t"+valid(first, second)+"\t\t"+new Integer(args[0])+"\t"+new Integer(args[1])+"\t"+new Integer(args[2]));
//	}
	
	public static boolean valid(HashMap<Integer, Integer> first, HashMap<Integer, Integer> second){
		if(first.size()!=second.size())
			return false;
		for(Entry<Integer, Integer> e:first.entrySet()){
			if(!e.getValue().equals(second.get(e.getKey()))){
//				System.out.println("Different "+e.getKey()+","+second.get(e.getKey()));
				return false;
			}
		}
		return true;
		
	}
	
	@Override
	public void addNext(int rank) {
		this.count=rank-1;
		this.index=rank-1;
	}

	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public int getId(int rank) {
		return 0;
	}
}

package gr.ntua.cslab.algorithms;

import java.util.Iterator;

import gr.ntua.cslab.containers.Person;
import gr.ntua.cslab.containers.PersonList;

public class Swing extends AbstractSMA {

	private int[] kMen=null, kWomen=null;
	
	public Swing() {
		// TODO Auto-generated constructor stub
	}

	public Swing(PersonList men, PersonList women) {
		super(men, women);
	}
	
	@Override
	public boolean terminationCondition() {
//		return this.stepCounter!=5;
		return this.men.hasSinglePeople();
	}

	@Override
	protected boolean menPropose() {
		return (this.stepCounter%2==0);
	}
	
	@Override
	protected void proposeStep(PersonList proposers) {
		if(this.kMen==null){
			this.kMen = new int[this.men.size()];
			for(int i=0;i<this.kMen.length;i++){
				this.kMen[i]=1;
			}
			this.kWomen = new int[this.men.size()];
			for(int i=0;i<this.kMen.length;i++){
				this.kWomen[i]=1;
			}
		}
		this.stepCounter+=1;
		PersonList acceptors = (proposers==this.men?this.women:this.men);
		
		Iterator<Person> it = proposers.getIterator();
		while(it.hasNext()){
			Person prop = it.next();
//			System.out.print(prop.toString()+":\t");
			int lim=0;
			if(proposers==this.men){
				lim = this.kMen[prop.getId()-1];
			} else {
				lim = this.kWomen[prop.getId()-1];
			}
			for(int i=1;i<=lim;i++){
				Person acc=acceptors.get(prop.getPreferences().getId(i));
//				System.out.print(acc.toString()+"["+i+"]");
				if(this.kWomen[acc.getId()-1]>=acc.getPreferences().getRank(prop.getId())){		//woman accepts proposer
					if(prop.isMarried()){
						Person d=prop.getCurrentPartner();
						if(acceptors==this.women){
							this.kWomen[d.getId()-1]+=1;
						} else {
							this.kMen[d.getId()-1]+=1;
						}
						prop.divorce();
					}
					if(acc.isMarried()){
						Person d=acc.getCurrentPartner();
						if(proposers==this.women){
							this.kWomen[d.getId()-1]+=1;
							
						} else {
							this.kMen[d.getId()-1]+=1;
						}
						acc.divorce();
					}
					prop.marry(acc);
					if(proposers==this.men){
						this.kMen[prop.getId()-1]=prop.getCurrentPartnerRank()-1;
						this.kWomen[acc.getId()-1]=acc.getCurrentPartnerRank()-1;
					} else {
						this.kWomen[prop.getId()-1]=prop.getCurrentPartnerRank()-1;
						this.kMen[acc.getId()-1]=acc.getCurrentPartnerRank()-1;
						
					}
				}
			}
			if(!prop.isMarried()){
				if(proposers==this.men){
					this.kMen[prop.getId()-1]+=(this.kMen[prop.getId()-1]>=this.men.size()?0:1);
				} else {
					this.kWomen[prop.getId()-1]+=(this.kWomen[prop.getId()-1]>=this.men.size()?0:1);
				}
			}
//			System.out.println();
		}
		
		
		
//		System.out.print("Singles:\t");
//		it = this.men.getIterator();
//		while(it.hasNext()){
//			Person p = it.next();
//			if(!p.isMarried()){
//				System.out.print(p+" ");
//			}
//		}
//		it = this.women.getIterator();
//		while(it.hasNext()){
//			Person p = it.next();
//			if(!p.isMarried()){
//				System.out.print(p+" ");
//			}
//		}
//		System.out.println();
	}
	 @Override
	public void step() {
		 this.proposeStep((this.menPropose()?this.men:this.women));
		 if(this.stepsDiagnostics!=0 && this.stepCounter%this.stepsDiagnostics==0){
			 System.err.println(this.stepCounter+"\t"+(System.currentTimeMillis()-this.executionTime)+"\t"+this.diagnostics.step());
			 //				System.out.print(this.diagnostics.resultsIsStable()+"\n");
		 }

			
	}
	
	
	public static void main(String[] args) {
		AbstractSMA.runAlgorithm(Swing.class, args);
	}

}

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.HashSet;
import java.util.List;


public class ProjectSelection {
    static double pm=0.1;
    static double pc=0.9;
    static double pd=0.2;
    static Chromosome bestPro;
    public static  Chromosome curBestPro=new Chromosome("0000");
    static double maxWorth=0;
    static ArrayList<Chromosome> curGroup=new ArrayList<Chromosome>();
    //static HashSet<String> isContains=new HashSet<String>();
    static int maxIteration=50;
    static int population=16;
    static int cataCountdown=10;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        //System.out.println("welcome");
        ArrayList<Chromosome> crs=new  ArrayList<Chromosome>(init());
        curGroup=(ArrayList<Chromosome>)crs.clone();
        //System.out.println(crs);
        int count=1;
        int countdown=1;
        Chromosome pre=new Chromosome("0000");
        System.out.println("the mutation probability is: "+pm);
        //System.out.println("the crossover probability is: "+pc);
        System.out.println("The evolution times is: "+maxIteration);
        System.out.println("----------------------------------------------");
        while(true){
        	//if(!group.isEmpty())
        	 if(count==maxIteration){
        		 if(jumpOutStillBest(curGroup))
        			 break;
        		 else
        			 count=0;
        	 }
        	 System.out.println("the currnet population is: "+curGroup);
        	 System.out.println("the size of currnet population is: "+curGroup.size());
        	 for(Chromosome c:curGroup){
         		System.out.print(c+": "+c.fitness+" ");
         	}
        	 System.out.println();
        	 ArrayList<Chromosome> newCrs=new  ArrayList<Chromosome>();
        	 selectTheBest(curGroup);
        	 RouletteWheelSelection(curGroup,newCrs);
        	 duplicateRemove(newCrs);
//        	if(Math.random()<=pc){
//        		
//        		
//        		
//        	}
        	doCrossover(newCrs);
    		
        	//doCrossover(newCrs);
        	if(Math.random()<=pm){
        		selectTheBest(newCrs);
        		doMutation(newCrs);
        	}
        	if(newCrs.size()<=1||Math.random()<=pd||countdown==cataCountdown){
        		newCrs=init();
        		countdown=1;
        		
        	}
        	//System.out.println(count+" the pre best is:+ "+pre);
        	//System.out.println(count+" the current best is:+ "+curBestPro);
        	if(curBestPro.toString().equals(pre.toString()))
        		count++;
        	else
        		count=1;
        	pre.clone(curBestPro);
        	Chromosome temp=new Chromosome();
        	temp.clone(curBestPro);
        	newCrs.add(temp);
        	
        	duplicateRemove(newCrs);
        	curGroup=( ArrayList<Chromosome>)newCrs.clone();
        	
        	System.out.println();
        	//System.out.println(curGroup);
        	//count++;
        	countdown++;
        	System.out.println("--------------------------------------");
        	
        	
        }
        List<Chromosome> lf=new ArrayList<Chromosome>(curGroup);
 	   Collections.sort(lf, new Comparator<Chromosome>(){
 		   @Override
	        public int compare( Chromosome o1, Chromosome o2) {
	        	return o1.fitness.compareTo(o2.fitness);
	      
	        }
 	   });
 	   for(int i=lf.size()-1;i>=0;i--){
 		  //System.out.println(curGroup.get(i).fitness);
 		   if(isVaild(lf.get(i).dna)){
 			  maxWorth=lf.get(i).fitness;
 			  
 			  bestPro=lf.get(i);
 			  break;
 		   
 	   }
 		   }
 	   System.out.println("the last population is: "+curGroup);
 	   
 	   System.out.println("the optimal solution is: "+bestPro);
 	   System.out.println("the optimal total return is: "+maxWorth+" millions");
        
	}
	public static void duplicateRemove(ArrayList<Chromosome> crs){
		   HashSet<String> set=new HashSet<String>();
		   for(int i=0;i<crs.size();i++){
			   if(set.contains(crs.get(i).toString()))
				   crs.remove(i);
			   else
				   set.add(crs.get(i).toString());
		   }
			 
	}
	public static ArrayList<Chromosome> init(){
		int num=2+(int)(Math.random()*8);
		int count=1;
		ArrayList<Chromosome> group=new ArrayList<Chromosome>();
		HashSet<String> set=new HashSet<String>();
		while(count<=num){
			
			Chromosome c=new Chromosome();
			if(!set.contains(c.toString())&&isVaild(c.dna)){
				group.add(c);
				set.add(c.toString());
				count++;
			}
			
			
		}
		return group;
		
	}
	public static boolean jumpOutStillBest(ArrayList<Chromosome> crs){
		ArrayList<Chromosome> newCrs=new  ArrayList<Chromosome>(init());
		List<Chromosome> lf=new ArrayList<Chromosome>(newCrs);
  	   Collections.sort(lf, new Comparator<Chromosome>(){
  		   @Override
 	        public int compare( Chromosome o1, Chromosome o2) {
 	        	return o1.fitness.compareTo(o2.fitness);
 	      
 	        }
  	   });
		if(newCrs.get(newCrs.size()-1).fitness>curBestPro.fitness){
			crs=(ArrayList<Chromosome>)newCrs.clone();
			return false;
		}
		else
			return true;
	}
    public static void selectTheBest(ArrayList<Chromosome> crs){
    	   
    	   List<Chromosome> lf=new ArrayList<Chromosome>(crs);
    	   Collections.sort(lf, new Comparator<Chromosome>(){
    		   @Override
   	        public int compare( Chromosome o1, Chromosome o2) {
   	        	return o1.fitness.compareTo(o2.fitness);
   	      
   	        }
    	   });
    	  //newCrs.add(lf.get(lf.size()-1));
    	   Chromosome temp=new Chromosome();
    	   int i=0;
    	   for(i=lf.size()-1;i>=0;i--){
    		   if(isVaild(lf.get(i).dna)){
    			   temp=lf.get(i);
    			   break;   
    		   }
    			   
    	   }
    	   
    	  if(isVaild(temp.dna)&&temp.fitness>curBestPro.fitness){
    		  curBestPro.clone(temp);;
    		 // System.out.println("the current best is change into:"+curBestPro+" from group: "+crs);
    	  }
    	     
    	  
    }
    public static void RouletteWheelSelection(ArrayList<Chromosome> crs,ArrayList<Chromosome> newCrs){
    	   int num=2+(int)(Math.random()*crs.size()-1),count=0;
    	   double[] p=getPro(crs);
    	   while(count<num){
    		   double r=Math.random();
    		   for(int i=0;i<p.length;i++){
    			   if(p[i]>=r&&!newCrs.contains(crs.get(i))){
    				   newCrs.add(crs.get(i));
    				   break;
    			   }
    				   
    		   }
    		   count++;
    	   }
    	   
    	   
    }
	
	public static double[] getPro(ArrayList<Chromosome> crs){
		   double sum=0.0;
		   for(Chromosome c:crs){
			   sum=sum+c.fitness;
		   }
		   double[] p=new double[crs.size()];
		   for(int i=0;i<crs.size();i++)
			   p[i]=crs.get(i).fitness/sum;
		   for(int i=1;i<crs.size();i++)
			   p[i]=p[i]+p[i-1];
		   return p;
	}
	public static boolean isVaild(int[] pr){
		   if(0.5*pr[0]+pr[1]+1.5*pr[2]+0.1*pr[3]>3.1)
			   return false;
		   else if(0.3*pr[0]+0.8*pr[1]+1.5*pr[2]+0.4*pr[3]>2.5)
			   return false;
		   else if(0.2*pr[0]+0.2*pr[1]+0.3*pr[2]+0.1*pr[3]>0.4)
			   return false;
		   else
			   return true;
	}
	
    public static void doCrossover(ArrayList<Chromosome> newCrs ){
    	          
    	         
    	          
    	          for(int i=0;i<newCrs.size()/2;i++){
    	        	  
    	        	  Chromosome temp=crossover(newCrs.get(i),newCrs.get(newCrs.size()-1-i));
    	        	  System.out.println("the selected chromosomes is: "+newCrs.get(i)+" and "+newCrs.get(newCrs.size()-1-i));
	        		  System.out.println("the generated child is: "+temp);
    	        	  if(isVaild(temp.dna)){
    	        		  newCrs.add(temp);
    	        		  
    	        	  }
    	        	   
    	          }
    	          
    	     
    }
	
	public static Chromosome crossover(Chromosome pr1, Chromosome pr2){
		   int pos=(int)(Math.random()*3);  // choose the position i random between 0-2, take 0-i from pr1 and take i+1-3 from pr2 
		   Chromosome newPr=new Chromosome();
		   for(int i=0;i<=pos;i++){
			   newPr.dna[i]=pr1.dna[i];
		   }
		   for(int i=pos+1;i<=3;i++){
			   newPr.dna[i]=pr2.dna[i];
		   }
		   newPr.setFitness();
		   return newPr;
		   
	}
	public static void doMutation(ArrayList<Chromosome> newCrs){
		   int mutationNum=1+(int)(Math.random()*newCrs.size());
		   ArrayList<Integer> pos=new ArrayList<Integer>();
	          int count=0;
	          while(count<mutationNum){
	        	  int p=(int)(Math.random()*(newCrs.size()));
	        	  if(!pos.contains(p)){
	        		  pos.add(p);
	        		  count++;
	        	  }
	          }	
	          for(int i=0;i<pos.size()-1;i=i+2){
//	        	  Chromosome temp=newCrs.get(pos.get(i));
//	        	  if(isVaild(temp.dna))
	        	  mutation(newCrs.get(pos.get(i)));
	          }
	}
	public static void mutation(Chromosome pr){
		   int mNum=1+(int)(Math.random()*4); // choose the number of mutation point
		   int count=0;
		   ArrayList<Integer> changePos=new ArrayList<Integer>();
		   while(count<mNum){
			   int pos=(int)(Math.random()*4);
			   if(!changePos.contains(pos)){
				   changePos.add(pos);
				   count++;
			   }
				   
		   }
		   int[] save=new int[4];
		   System.out.print("mutation occur! "+pr+" is change into: ");
		   for(int i=0;i<4;i++){
			   save[i]=pr.dna[i];
			   if(changePos.contains(i)){
				   if(pr.dna[i]==1)
					   pr.dna[i]=0;
				   else
					   pr.dna[i]=1;
			   }
			   
		   }
		   System.out.println(pr);
		   if(isVaild(pr.dna))
		     pr.setFitness();
		   else{
			   for(int i=0;i<4;i++){
				   pr.dna[i]=save[i];
			   }
		   }
			   
		   
		   
	}

}
class Chromosome{
	  int[] dna=new int[4];
	  Double fitness=0.0;
	  public Chromosome(){
		  for(int i=0;i<dna.length;i++){
			  dna[i]=(int)(Math.random()*2);
			  
		  }
		  fitness=fitnessFunction(dna);
	  }
	  public Chromosome(String value){
		     for(int i=0;i<4;i++){
		    	 dna[i]=Integer.parseInt(value.substring(i, i+1));
		     }
		     fitness=fitnessFunction(dna);
	  }
      public  double fitnessFunction(int[] pr){
				BigDecimal x1=BigDecimal.valueOf(0.2*pr[0]);
				BigDecimal x2=BigDecimal.valueOf(0.3*pr[1]);
				BigDecimal x3=BigDecimal.valueOf(0.5*pr[2]);
				BigDecimal x4=BigDecimal.valueOf(0.1*pr[3]);
				return (x1.add(x2).add(x3).add(x4).doubleValue());
			}
		 
      public void setFitness(){
    	  this.fitness=fitnessFunction(dna);
      }
	  public String toString(){
		  StringBuffer str=new StringBuffer();
		  for(int i=0;i<4;i++){
			  str.append(dna[i]);
		  }
		  return str.toString();
	  }
	 public void clone(Chromosome cr){
		    
		    for(int i=0;i<4;i++){
		    	this.dna[i]=cr.dna[i];
		    }
		    this.fitness=cr.fitness;
	 } 
}

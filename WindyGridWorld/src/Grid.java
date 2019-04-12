import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Grid {
       
	    int windy=0;
	    public HashMap<String,Double> q_Value;
	    int x;
	    int y;
	    public int maxX=0;
	    public int maxY=0;
	    Double val=Double.MIN_VALUE;
	    public boolean isGoal=false;
	    public boolean isActive=false;
	    public boolean hasStay;
	    public Grid(int windy, int x, int y,boolean hasStay ){
	    	this.windy=windy;
	    	q_Value=new HashMap<String,Double>();
	    	this.x=x;
	    	this.y=y;
	    	this.hasStay=hasStay;
	    	q_Value.put("left", 0.0);
	    	q_Value.put("right", 0.0);
	    	q_Value.put("up", 0.0);
	    	q_Value.put("down", 0.0);
	    	q_Value.put("left_up", 0.0);
	    	q_Value.put("right_up", 0.0);
	    	q_Value.put("left_down", 0.0);
	    	q_Value.put("right_down", 0.0);
	    	if(hasStay)
	    		q_Value.put("stay", 0.0);
	    	
	    }
	    
	    public void updateAction(){

	    	
	    	if(x==0){
	    		String[] str=new String[3];
	    		  str[0]="up";
	    		  str[1]="left_up";
	    		  str[2]="right_up";
	    		  for(int i=0;i<3;i++){
	    			  if(q_Value.containsKey(str[i]))
	    				  q_Value.remove(str[i]);
	    		  }
	    	  }
	    	  if(y==0){
	    		  String[] str=new String[3];
	    		  str[0]="left";
	    		  str[1]="left_up";
	    		  str[2]="left_down";
	    		  for(int i=0;i<3;i++){
	    			  if(q_Value.containsKey(str[i]))
	    				  q_Value.remove(str[i]);
	    		  }
	    	  }
	    	  if(x==maxX){
	    		  String[] str=new String[3];
	    		  str[0]="down";
	    		  str[1]="left_down";
	    		  str[2]="right_down";
	    		  for(int i=0;i<3;i++){
	    			  if(q_Value.containsKey(str[i]))
	    				  q_Value.remove(str[i]);
	    		  }
	    	  }
	    	  if(y==maxY){
                  String[] str=new String[3];
                  str[0]="right";
                  str[1]="right_up";
                  str[2]="right_down";
                  for(int i=0;i<3;i++){
	    			  if(q_Value.containsKey(str[i]))
	    				  q_Value.remove(str[i]);
	    		  }
	    	  }
	    	  
	    }
	    
	    public double getMax(){
	    	double max=Double.MIN_VALUE;
	    	for(Map.Entry<String, Double> entry:q_Value.entrySet()){
  			  if(entry.getValue()>max);
  			     max=entry.getValue();
  		  }
	    	return max;
	    }
	    
	    public String toString(){
	    	return String.format("w(%d,%d): %8.5f",x,y,getMax());
	    }
	    public String getMaxAction(){
	    	double max=Double.NEGATIVE_INFINITY;
	    	String action="";
	    	for(Map.Entry<String, Double> entry:q_Value.entrySet()){
  			  if(entry.getValue()>max){
  				 
  			     max=entry.getValue();
  			     action=entry.getKey();
  			  }
  			  
  		  }
	    	return action;
	    }
	    	   
}

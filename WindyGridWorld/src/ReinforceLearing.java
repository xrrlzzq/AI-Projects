import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class ReinforceLearing {
    static double learningRate=0.5;
    static double discountRate=0.2;
    static int reward=-1;
    static int goalReward=0;
    static String[] action;
    static int epsidoe=50000;
    static int[] windy={0,0,0,1,1,1,2,2,1,0};
    static int mY=9,mX=6;
    static boolean hasStay=false;
    static String[] actions={"up","down","right","left","left_up","right_up","left_down","right_down"};
    static String[] actions1={"up","down","right","left","left_up","right_up","left_down","right_down","stay"};
	public static void main(String[] args) {
		Grid[][] windyGrid=new Grid[7][10];
		
		
		System.out.println("do you want 9 actions?(y)");
		Scanner sc =new Scanner(System.in);
		if(sc.nextLine().equals("y"))
			hasStay=true;
		init(windyGrid);
		System.out.println("learning rate is: "+learningRate);
		System.out.println("discount rate is: "+discountRate);
		doProcess(windyGrid[3][0],windyGrid);
		printGrid(windyGrid);
		System.out.println();
		printPath(windyGrid);
		System.out.println();
		System.out.println("the strength of wind is:");
		printWindy();
		//System.out.print(windyGrid[3][2].q_Value);

	}
	public static void printWindy(){
		for(int i=0;i<windy.length;i++){
			System.out.print(String.format("[ %s ]", windy[i]));
		}
	}
	public static void printGrid(Grid[][] windyGrid){
		for(int i=0;i<windyGrid.length;i++){
			for(int j=0;j<windyGrid[i].length;j++){
				System.out.print(windyGrid[i][j]+" ");
			}
			System.out.println();
		}
		
	}
	public static void printPath(Grid[][] windyGrid){
		for(int i=0;i<windyGrid.length;i++){
			for(int j=0;j<windyGrid[i].length;j++){
				String index="",action=windyGrid[i][j].getMaxAction();
				switch(action){
		    	   case "up" : index="U";break;
		    	   case "right" : index="R";break;
		    	   case "left"  : index="L";break;
		    	   case "down"  : index="D";break;
		    	   case "left_up" : index="Q";break;
		    	   case "left_down" : index="Z";break;
		    	   case "right_up" : index="P";break;
		    	   case "right_down" :index="M";break;
		    	   case "stay" :index="S";break;
		    	   default : index="N";break;	   
		    	   }
				if(windyGrid[i][j].isGoal)
					index="G";
				System.out.print(String.format("[ %s ]", index));
			}
			System.out.println();
		}
	}
    public static void doProcess(Grid start,Grid[][] windyGrid){
    	int count=0;
    	while(count<epsidoe){
//    	for(int i=0;i<action.length;i++){
//  		  if(start.q_Value.containsKey(action[i])){
//  			 
//  			  Grid temp=move(windyGrid,action[i],start);
//  			  String index=String.format("%d%d", temp.x,temp.y);
//  			  
//  			  HashSet<String> newPath=new HashSet<String>();
//  			  newPath.add(index);
//  			  double old=start.q_Value.get(action[i]);
//  			  start.q_Value.replace(action[i],old+learningRate*(Q_Process(temp,windyGrid,newPath))-old );
//  			  
//  		  }
  			  
//  	  }
    	doEpisode(windyGrid, start);
    	count++;
    	}
  	  
    }
	public static void init(Grid[][] windyGrid){
		 
		for(int i=0;i<windyGrid.length;i++){
			for(int j=0;j<windyGrid[i].length;j++){
				int windy=0;
				if(j==3||j==4||j==5||j==8)
					windy=1;
				if(j==6||j==7)
					windy=2;
				
				Grid g=new Grid(windy,i,j,hasStay);
				if(i==3&&j==7)
					g.isGoal=true;
				g.maxX=windyGrid.length-1;
				g.maxY=windyGrid[i].length-1;
				g.updateAction();
				windyGrid[i][j]=g;
			}
			
		}
	}
	
	
    public static Grid move(Grid[][] windyGrid, String action, Grid g){
//    	   if(action.equals("up")){
//    		   return windyGrid[g.x-1][g.y];
//    	   }
//    	   else if(action.equals("right")){
//    		   return windyGrid[g.x][g.y+1];
//    	   }
    	   if(action.equals("stay"))
    		   return windyGrid[g.x][g.y];
    	   switch(action){
    	   case "up" : return windyGrid[Math.max(g.x-1-windy[g.y], 0)][g.y];
    	   case "right" : return windyGrid[Math.max(g.x-windy[g.y], 0)][Math.max(Math.min(g.y+1,mY),0)];
    	   case "left"  : return windyGrid[Math.max(g.x-windy[g.y], 0)][Math.max(0, g.y-1)];
    	   case "down"  : return windyGrid[Math.max(Math.min(g.x+1-windy[g.y], mX), 0)][g.y];
    	   case "left_up" : return windyGrid[Math.max(g.x-1-windy[g.y], 0)][Math.max(g.y-1, 0)];
    	   case "left_down" : return windyGrid[Math.max(Math.min(g.x+1-windy[g.y], mX), 0)][Math.max(g.y-1, 0)];
    	   case "right_up" : return windyGrid[Math.max(g.x-1-windy[g.y], 0)][Math.max(Math.min(g.y+1,mY),0)];
    	   case "right_down" : return windyGrid[Math.max(Math.min(g.x+1-windy[g.y], mX), 0)][Math.max(Math.min(g.y+1,mY),0)];
    	   default : return null;	   
    	   }
    }
   
   public static String chooseRandom(Grid s){
	      String[] a;
	      if(hasStay)
	    	  a=actions1;
	      else
	    	  a=actions;
	      int index =(int) (a.length*Math.random());
	      while(!s.q_Value.containsKey(a[index]))
	      index=(int) (a.length*Math.random());
	      
	      return a[index];
   }

   public static void doEpisode(Grid[][] windyGrid, Grid s){
	      Grid temp=s;
	      int count=0;
	      while(!temp.isGoal){
	    	  String next_Action=temp.getMaxAction();
//	    	  if(Math.random()<=0.1)
//	    		  next_Action=chooseRandom(temp);
	    	  //System.out.println(String.format("cureent node is w(%d,%d) , current action is: %s", temp.x,temp.y,next_Action));
	    	  Grid nextT=move(windyGrid,next_Action,temp);
	    	  //System.out.println(String.format("next node is: w(%d,%d)", nextT.x,nextT.y));
//	    	  if(nextT.x==temp.x&&nextT.y==temp.y)
//	    		  continue;
	    	  double old=temp.q_Value.get(next_Action);
	    	  int r=reward;
	    	  if(nextT.isGoal)
	    		 r=goalReward;
	    	  temp.q_Value.replace(next_Action,old+learningRate*(r+(discountRate)*nextT.getMax()-old));
//	    	  System.out.print(String.format("after update , the table of w(%d,%d) is: ", temp.x,temp.y));
//	    	  System.out.println(temp.q_Value);
	    	  temp=nextT;
//	    	  if(count==50000)
//	    		  break;
//	    	  count++;
	    	  
	    	  
	      }
   }
}


/*
A client that can be run from anywhere. If the server is running, the following command starts up the client:
java MyClient numThreads
numThreads is an optional number of threads you want the current machine to use, if not specified, program will use as many threads as there are cores on your machine.
*/

import java.net.*;
import java.io.*;
import java.util.*;

public class MyClient {

	public static boolean moreThreads;

  public static void main(String[] args) throws Exception {
    //automatically set number of threads equal to number of processors 
    int numberThreads = Runtime.getRuntime().availableProcessors();
    //change number of threads if user specifies a thread count
    if(args.length==1){
      numberThreads = Integer.parseInt(args[0]);
    }
    // start threads
    System.out.println("Starting "+numberThreads+" threads.");
    for (int i = 0; i < numberThreads; i++) {
      new RunIt().start();
    }
    moreThreads = true;
	while(moreThreads){
        //sleep for a minute
        Thread.currentThread().sleep(60000);
        //make sure number of running threads is what we wanted.
       //numberThreads+1 is because this "main" thread counts as a thread and we don't care if that is running, it's not taking up hardly any CPU time.
       while(Thread.activeCount() < numberThreads+1){
            //if a thread died, somehow, start a new thread
            System.out.println("started a new thread");
            new RunIt().start();
        }
    }
  }

    public static String determineCase(ArrayList<String> ordering)
    {
    	ArrayList<String> everyViewpoints = new ArrayList<String>();
		String[] allPoints = new String[]{ "ABCEG", "BDCFH", "BDEFH", "ACEFG", "BDFGH", "ABDEG", "ABDFG", "ACDFG", "ABDEH", "ABDFH", "ACDFH", "ABEFH", "ACEFH", "BCEFH", "ABDGH", "ACDGH", "ABEGH", "ACEGH", "BCEGH", "ADEGH", "BDEGH", "ADG", "BEH", "ACF", "BDG", "CEH", "ADF", "CFH", "ACDG", "BDEH", "ACEF", "BDFG", "CEGH", "ADFH", "ABEG", "BCFH" };
		
		for (String s : allPoints)
		{
			everyViewpoints.add(s);
		}
		ArrayList<String> usableViewpoints = new ArrayList<String>();
		usableViewpoints = availableViewpoints(ordering, everyViewpoints);
		if (usableViewpoints.size() == 0)
		{
			System.out.println("We placed everything. ");
			return ordering.toString();
		}
		int optimalViewpointIndex = -1;
		int minGaps = 1040;
		
		// determine which viewpoint is the point we are going to recurse
        ArrayList<Integer> optimalGaps = new ArrayList<Integer>();
        
		for (int i = 0; i < usableViewpoints.size(); ++i)
		{
			// insert usableViewpoints[i] into our current ordering in every gap
			// int feasibleGaps = 0;
            
            ArrayList<Integer> storedGaps = new ArrayList<Integer>();
            // storedGaps.clear();
			for (int j = 0; j < ordering.size(); ++j)
			{
				ArrayList<String> newOrdering = new ArrayList<String>();
				newOrdering.clear();
				newOrdering.addAll(ordering);
				newOrdering.add(j, usableViewpoints.get(i));
                
				if (isFeasibleOrdering(newOrdering))
				{
					storedGaps.add(j);
				}
			}
            
			if (storedGaps.size() < minGaps)
			{
				minGaps = storedGaps.size();
				optimalViewpointIndex = i;
                optimalGaps.clear();
                optimalGaps.addAll(storedGaps);
			}
            if (minGaps == 0)
		    {
			    // usableViewpoints[optimalViewpointIndex] can't go anywhere
			    return "";
		    }
		}
        for (int i = 0; i < optimalGaps.size(); ++i)
        {
            ArrayList<String> recurseOrdering = new ArrayList<String>();
            recurseOrdering.addAll(ordering);
            recurseOrdering.add(optimalGaps.get(i), usableViewpoints.get(optimalViewpointIndex));
            String result = determineCase(recurseOrdering);
            if (!result.equals(""))
            {
                return result;
            }
        }
        return "";
    }
    
    // helper function, return the available viewpoints you could still use
    public static ArrayList<String> availableViewpoints(ArrayList<String> usedPoints, ArrayList<String> candidatePoints)
    {
    	ArrayList<String> feasibleViewpoints = new ArrayList<String>();
    	feasibleViewpoints.addAll(candidatePoints);
    	feasibleViewpoints.removeAll(usedPoints);
    	return feasibleViewpoints;
    }

  public static void computePair(int i, int j, ArrayList<String> pair, ArrayList<String> points){

    //ArrayList<String> pair = new ArrayList<String>();
    if(i < j){
      pair.add(points.get(i));
      pair.add(points.get(j));
    }
    else{
      pair.add(points.get(j));
      pair.add(points.get(i));
    }

    //return pair;
  }

    // find red edges, basically re-run the findRedEdges
    // input: BDEH,ACEFH,A,...
    //commented out so I could test this
    //public static void findRedEdges(String caseAcceptedLine)
    //{
      //  ArrayList<String> points = 
    //}

  public static boolean isFeasibleOrdering(ArrayList<String> points) {
    // System.out.println(points);
    // Edge colors:
    // Green - See each other - unpierceable and close
    // Red - Do not see other - close - must be pierced
    // Purple - Do not see each other - cannot be pierced - too far
    // Orange - Do not see each other - don't care how we block.
    // Blue - Don't care if see each other - cannot be pierced.
    // Cyan - Don't care if see each other - don't care if pierced.
	// Yellow - Don't care if they see each other - cannot be too far away.

    HashMap<ArrayList<String>, String> edgeColor = new HashMap<ArrayList<String>, String>();
	
	

    // For every pair of points (u,v):
    // -If u is a guard and v is a viewpoint (or vice versa):
    // - If u sees v, set edgeColor(u,v) = green.
    // - Else set edgeColor(u,v) = orange.
    // -If u and v are both viewpoints or are both guards
    // - Initialize edgeColor(u,v) = cyan.
    for (int i = 0; i < points.size() - 1; i++) {
      for (int j = i + 1; j < points.size(); j++) {
        ArrayList<String> temp = new ArrayList<String>();
        temp.clear();
        temp.add(points.get(i));
        temp.add(points.get(j));
        // System.out.print(temp);

        if (points.get(i).length() == 1) {
          if (points.get(j).length() == 1) {
            edgeColor.put(temp, "cyan");
          } else {
            if (points.get(j).indexOf(points.get(i)) == -1) {
              edgeColor.put(temp, "orange");
            } else {
              edgeColor.put(temp, "green");
            }
          }
        } else {
          if (points.get(j).length() > 1) {
            edgeColor.put(temp, "cyan");
          } else {
            if (points.get(i).indexOf(points.get(j)) == -1) {
              edgeColor.put(temp, "orange");
            } else {
              edgeColor.put(temp, "green");
            }
          }
        }
      }
    }
    // Do until all edge colors have converged:
    // Boolean updatedAnEdge;
    // do{
    // updatedAnEdge = false;
    // For each cyan (resp. orange) edge:
    // Check for CW OC and CCW OC that use edges that are either green, purple, or
    // blue.
    // If have on both sides, flip edge color to blue (resp. purple) and set
    // updatedAnEdge to true.
    // }until(updatedAnEdge);

    boolean updatedAnEdge = false;
    do {
      ArrayList<String> pair = new ArrayList<String>();
      updatedAnEdge = false;
      for (int i = 0; i < points.size() - 3; i++) {
		  
		  int stoppingIndexForL = points.size();
		  if(i==0)
			  stoppingIndexForL = points.size()-2;
		  else if(i==1)
			  stoppingIndexForL = points.size()-1;
		  
        for (int l = i + 3; l != stoppingIndexForL; l++) {
          pair.clear();
          computePair(i, l, pair, points);
		  //System.out.println("Pair for " + i + " and " + l + ": " + pair);
          //pair.add(points.get(i));
          //pair.add(points.get(l));
          if ((edgeColor.get(pair).compareTo("orange") != 0) && (edgeColor.get(pair).compareTo("cyan") != 0) && (edgeColor.get(pair).compareTo("red") != 0) && (edgeColor.get(pair).compareTo("yellow") != 0)) {
            continue;
          }
          int stoppingIndexForM;
          if(i==0)
            stoppingIndexForM = points.size()-1;
          else
            stoppingIndexForM = i-1;

          //int stoppingIndexForM;
          //if(stoppingIndexForN == 0)
           // stoppingIndexForM = points.size();
         // else
          //  stoppingIndexForM = stoppingIndexForN;
          for (int j = i + 1; j < l - 1; j++) {
            pair.clear();
            computePair(j, l, pair, points);
			//System.out.println("Pair for " + j + " and " + l + ": " + pair);
            //pair.add(points.get(j));
            //pair.add(points.get(l));
            if ((edgeColor.get(pair).compareTo("purple") != 0) && (edgeColor.get(pair).compareTo("green") != 0)
                && (edgeColor.get(pair).compareTo("blue") != 0)) {
              continue;
            }
            for (int k = j + 1; k < l; k++) {
              pair.clear();
              computePair(i, k, pair, points);
			  //System.out.println("Pair for " + i + " and " + k + ": " + pair);
              //pair.add(points.get(i));
              //pair.add(points.get(k));
              if ((edgeColor.get(pair).compareTo("purple") != 0) && (edgeColor.get(pair).compareTo("green") != 0)
                  && (edgeColor.get(pair).compareTo("blue") != 0)) // unpierceable
              {
                continue;
              }
              for (int m = (l + 1)%points.size(); m != stoppingIndexForM; m = (m+1)%points.size()) {
                pair.clear();
                computePair(i, m, pair, points);
				//System.out.println("Pair for " + i + " and " + m + ": " + pair);
                //pair.add(points.get(i));
                //pair.add(points.get(m));
                if ((edgeColor.get(pair).compareTo("purple") != 0) && (edgeColor.get(pair).compareTo("green") != 0)
                    && (edgeColor.get(pair).compareTo("blue") != 0)) {
                  continue;
                }
                
                for (int n = (m + 1)%points.size(); n != i; n = (n+1)%points.size()) {
                  pair.clear();
                  computePair(n, l, pair, points);
				  //System.out.println("Pair for " + n + " and " + l + ": " + pair);
                  //pair.add(points.get(l));
                  //pair.add(points.get(n));
                  if ((edgeColor.get(pair).compareTo("purple") == 0) || (edgeColor.get(pair).compareTo("green") == 0)
                      || (edgeColor.get(pair).compareTo("blue") == 0)) {
                    pair.clear();
                    computePair(i, l, pair, points);
                    //pair.add(points.get(i));
                    //pair.add(points.get(l));

                    if (edgeColor.get(pair).compareTo("orange") == 0) {
                      edgeColor.replace(pair, "purple");
                      updatedAnEdge = true;
                    }
                    else if (edgeColor.get(pair).compareTo("cyan") == 0) {
                      edgeColor.replace(pair, "blue");
                      updatedAnEdge = true;
                    }
					else if(edgeColor.get(pair).compareTo("yellow") == 0) {
                      edgeColor.replace(pair, "green");
                      updatedAnEdge = true;
                    }
					else if(edgeColor.get(pair).compareTo("red") == 0) {
                      return false;
                    }
                  }
                }
              }
            }
          }
        }
      }//End encircleing 
	  
	  //Begin crossing lemma.
      for (int i = 0; i < points.size() - 3; i++) {
        for (int k = i + 2; k < points.size() - 1; k++) {
          pair.clear();
          pair.add(points.get(i));
          pair.add(points.get(k));
		  String ikColor = edgeColor.get(pair);
          if (!mustBeClose(ikColor) && !ikColor.equals("blue")) {
            continue;
          }
          for (int j = i + 1; j < k; j++) {
            for (int l = k + 1; l < points.size(); l++) {
              pair.clear();
              pair.add(points.get(j));
              pair.add(points.get(l));
			  String jlColor = edgeColor.get(pair);
			  if ((!mustBeClose(jlColor) && !jlColor.equals("blue")) || (ikColor.equals("blue") && jlColor.equals("blue"))) {
				continue;
			  }
			  
			  boolean bothClose = mustBeClose(ikColor) && mustBeClose(jlColor);
			  
			  
              if (bothClose) {
				  
				  
                pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(j));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(k));
                  pair.add(points.get(l));
                  if (edgeColor.get(pair).compareTo("purple") == 0) {
					  
                    return false;
                  }
				  else if (edgeColor.get(pair).compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).compareTo("cyan") == 0) {
                    edgeColor.replace(pair, "yellow");
                    updatedAnEdge = true;
                  }
                }


                pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(l));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(j));
                  pair.add(points.get(k));
                  if (edgeColor.get(pair).compareTo("purple") == 0) {
					  
                    return false;
                  }
				  else if (edgeColor.get(pair).compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).compareTo("cyan") == 0) {
                    edgeColor.replace(pair, "yellow");
                    updatedAnEdge = true;
                  }
                }

                /*pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(j));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(k));
                  pair.add(points.get(l));
                  if (edgeColor.get(pair).compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
                }*/

                pair.clear();
                pair.add(points.get(k));
                pair.add(points.get(l));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(i));
                  pair.add(points.get(j));
                  if (edgeColor.get(pair).compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).compareTo("cyan") == 0) {
                    edgeColor.replace(pair, "yellow");
                    updatedAnEdge = true;
                  }
                }

                /*pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(l));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(j));
                  pair.add(points.get(k));
                  if (edgeColor.get(pair).compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
                }*/

                pair.clear();
                pair.add(points.get(j));
                pair.add(points.get(k));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(i));
                  pair.add(points.get(l));
                  if (edgeColor.get(pair).compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).compareTo("cyan") == 0) {
                    edgeColor.replace(pair, "yellow");
                    updatedAnEdge = true;
                  }
                }

               /* pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(j));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(k));
                  pair.add(points.get(l));
                  if (edgeColor.get(pair).compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
                }*/

                /*pair.clear();
                pair.add(points.get(k));
                pair.add(points.get(l));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(i));
                  pair.add(points.get(j));
                  if (edgeColor.get(pair).compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
                }*/

                /*pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(l));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(j));
                  pair.add(points.get(k));
                  if (edgeColor.get(pair).compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
                }*/

                /*pair.clear();
                pair.add(points.get(j));
                pair.add(points.get(k));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(i));
                  pair.add(points.get(l));
                  if (edgeColor.get(pair).compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
                }*/
              }
			  else{
				  
				  //one edge is blue and one is close.
				  pair.clear();
					pair.add(points.get(i));
					pair.add(points.get(j));
					if (edgeColor.get(pair).compareTo("purple") == 0) {
					  pair.clear();
					  pair.add(points.get(k));
					  pair.add(points.get(l));
					  if (edgeColor.get(pair).compareTo("purple") == 0) {
						  
						//Set blue edge to purple.
						if(ikColor.equals("blue")){
							pair.clear();
							pair.add(points.get(i));
							pair.add(points.get(k));
							edgeColor.replace(pair,"purple");
							updatedAnEdge = true;
						}
						else if(jlColor.equals("blue")){
							pair.clear();
							pair.add(points.get(j));
							pair.add(points.get(l));
							edgeColor.replace(pair,"purple");
							updatedAnEdge = true;
						}
					  }
					}
					
					 pair.clear();
					pair.add(points.get(i));
					pair.add(points.get(l));
					if (edgeColor.get(pair).compareTo("purple") == 0) {
					  pair.clear();
					  pair.add(points.get(j));
					  pair.add(points.get(k));
					  if (edgeColor.get(pair).compareTo("purple") == 0) {
						  
						//Set blue edge to purple.
						if(ikColor.equals("blue")){
							pair.clear();
							pair.add(points.get(i));
							pair.add(points.get(k));
							edgeColor.replace(pair,"purple");
							updatedAnEdge = true;
						}
						else if(jlColor.equals("blue")){
							pair.clear();
							pair.add(points.get(j));
							pair.add(points.get(l));
							edgeColor.replace(pair,"purple");
							updatedAnEdge = true;
						}
					  }
					}
			  }
            }
          }
        }
      }//End Crossing Lemma.
	  
	  
	  //Begin find a blocker, bruh.
	  for(int i=0; i<points.size(); i++){
		  
		  for(int j=i+2; j<points.size(); j++){
			  
			  pair.clear();
			  pair.add(points.get(i));
			  pair.add(points.get(j));
			  if(!edgeColor.get(pair).equals("red"))
				  continue;
			  
			  //CCW side "below i,j".
			  int z;
			  for(z=j-1; z>i; z--){
				  pair.clear();
				  pair.add(points.get(i));
				  pair.add(points.get(z));
				  String color = edgeColor.get(pair);
				  if(color.equals("green") || color.equals("blue"))
					  break;
			  }
			  
			  int y;
			  for(y=i+1; y<j; y++){
				  pair.clear();
				  pair.add(points.get(y));
				  pair.add(points.get(j));
				  String color = edgeColor.get(pair);
				  if(color.equals("green") || color.equals("blue"))
					  break;
			  }
			  
			  boolean canBlockCCW = (z <= y);
			  
			  
			  //Check CW side
			  int a = i-1;
			  if(a<0) a = points.size()-1;
			  while(a!=j){
				  pair.clear();
				  computePair(a, j, pair, points);
				  String color = edgeColor.get(pair);
				  if(color.equals("green") || color.equals("blue"))
					  break;
				  
				  a--;
				  if(a<0) a = points.size()-1;
			  }
			  
			  
			  int b = j+1;
			  if(b==points.size()) b = 0;
			  while(b!=i){
				  pair.clear();
				  computePair(b, i, pair, points);
				  String color = edgeColor.get(pair);
				  if(color.equals("green") || color.equals("blue"))
					  break;
				  
				  b++;
				  if(b==points.size()) b = 0;
			  }
			  
			  
			  boolean canBlockCW;
			  if(a >= j){
				  //a wrapped around past 0.
				  if(b <= i || b >= a)
					  canBlockCW = true;
				  else
					  canBlockCW = false;
			  }
			  else{
				  if(b >= a && b <= i)
					  canBlockCW = true;
				  else
					  canBlockCW = false;
			  }
			  
			  if(!canBlockCCW && !canBlockCW){
				  /*System.out.println("Rejecting: " + points);
				  System.out.println("No blocker for " + points.get(i) + " and " + points.get(j) + " on either side.");
				  System.out.println("z: " + points.get(z));
				  System.out.println("y: " + points.get(y));
				  System.out.println("a: " + points.get(a));
				  System.out.println("b: " + points.get(b));
				  System.exit(1);*/
				  return false;
			  }
			  
			  if(canBlockCCW && canBlockCW)
				  continue;
			  
			  if(canBlockCCW){
				  
				  if(z == y){
					  
					  //if (i,z) or (z,j) were blue, it should now be green.
					  pair.clear();
					  pair.add(points.get(i));
					  pair.add(points.get(z));
					  String datColor = edgeColor.get(pair);
					  if(datColor.equals("blue")){
						edgeColor.replace(pair,"green");
						 updatedAnEdge = true;
					  }
					  
					  pair.clear();
					  pair.add(points.get(z));
					  pair.add(points.get(j));
					  datColor = edgeColor.get(pair);
					  if(datColor.equals("blue")){
						edgeColor.replace(pair,"green");
						 updatedAnEdge = true;
					  }
					  
					  for(int zhongxiu = i+1; zhongxiu < z; zhongxiu++){
						  
						  for(int erik = z+1; erik < j; erik++){
							  
							  pair.clear();
							  pair.add(points.get(zhongxiu));
							  pair.add(points.get(erik));
							  
							  String theirColor = edgeColor.get(pair);
							  
							  if(theirColor.equals("yellow")){
									edgeColor.replace(pair,"red");
									updatedAnEdge = true;
							  }
							  else if(theirColor.equals("cyan")){
								  
								  edgeColor.replace(pair,"orange");
									updatedAnEdge = true;
								  
							  }
							  else if(theirColor.equals("blue") || theirColor.equals("green")){
								  
								  //System.out.println("REJECTION BRUH");
								  return false;
								  
							  }
							  
						  }
					  }
					  
					  //For all points x on CW side s.t. (i,x) and (j,x) are green, red, or yellow (not too far away), then (z,x) cannot be too far away.
					  //For all points x on CW side s.t. (z,x) is purple, x must be purple to at least one of i and j.
					  
					  int x = j+1;
					  if(x == points.size()) x = 0;
					  while(x != i){
						  
							pair.clear();
							computePair(x, i, pair, points);
							String iColor = edgeColor.get(pair);
							
							pair.clear();
							computePair(x, j, pair, points);
							String jColor = edgeColor.get(pair);
							
							pair.clear();
						  computePair(x, z, pair, points);
						  String zColor = edgeColor.get(pair);
								
						  if(mustBeClose(iColor) && mustBeClose(jColor)){
							  
							  if(zColor.equals("purple")){
								  
									  /*System.out.println("Rejecting: " + points);
									  System.out.println("Need " + points.get(z) + " to block " + points.get(i) + " and " + points.get(j) + " but blocker needs to be too far from " + points.get(x));
									System.exit(1);*/
								  return false;
							  }
							  else if(zColor.equals("orange")){
								  edgeColor.replace(pair,"red");
								  updatedAnEdge = true;
							  }
							  else if(zColor.equals("cyan")){
								  edgeColor.replace(pair,"yellow");
								  updatedAnEdge = true;
							  }
							  else if(zColor.equals("blue")){
								  edgeColor.replace(pair,"green");
								  updatedAnEdge = true;
							  }
						  }
						  
						  if(zColor.equals("purple")){
							  
								if(mustBeClose(iColor))
								{
									pair.clear();
									computePair(x, j, pair, points);
									datColor = edgeColor.get(pair);
									if(!datColor.equals("purple")){
										edgeColor.replace(pair,"purple");
										updatedAnEdge = true;
									}
									
									for(int w = z+1; w<j; w++){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											return false;
										}
										else if(!wColor.equals("purple")){
											edgeColor.replace(pair,"purple");
											updatedAnEdge = true;
										}
										
									}
								}
								else if(mustBeClose(jColor))
								{
									pair.clear();
									computePair(x, i, pair, points);
									datColor = edgeColor.get(pair);
									if(!datColor.equals("purple")){
										edgeColor.replace(pair,"purple");
										updatedAnEdge = true;
									}
									
									for(int w=i+1; w<z; w++){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											return false;
										}
										else if(!wColor.equals("purple")){
											edgeColor.replace(pair,"purple");
											updatedAnEdge = true;
										}
										
									}
								}
					
							  
						  }
						  
						  x++;
						  if(x == points.size()) x = 0;
					  }
					  
				  }
				  else{
					  //TO DO LATER BRAH
					  
				  }
				  
			  }
			  else{
				  
				  //Blocking on CW side
				  if(a == b){
					  
					  //if (i,a) or (a,j) were blue, it should now be green.
					  pair.clear();
					  computePair(a, i, pair, points);
					 String datColor = edgeColor.get(pair);
					  if(datColor.equals("blue")){
						edgeColor.replace(pair,"green");
						 updatedAnEdge = true;
					  }
					  
					  pair.clear();
					  computePair(a, j, pair, points);
					  datColor = edgeColor.get(pair);
					  if(datColor.equals("blue")){
						edgeColor.replace(pair,"green");
						 updatedAnEdge = true;
					  }
					  
					  for(int zhongxiu = (j+1)%points.size(); zhongxiu != a; zhongxiu = (zhongxiu+1)%points.size()){
						  
						  for(int erik = (a+1)%points.size(); erik != i; erik = (erik+1)%points.size()){
							  
							  pair.clear();
							  computePair(zhongxiu, erik, pair, points);
							  
							  String theirColor = edgeColor.get(pair);
							  
							  if(theirColor.equals("yellow")){
									edgeColor.replace(pair,"red");
									updatedAnEdge = true;
							  }
							  else if(theirColor.equals("cyan")){
								  
								  edgeColor.replace(pair,"orange");
									updatedAnEdge = true;
								  
							  }
							  else if(theirColor.equals("blue") || theirColor.equals("green")){
								  
								  //System.out.println("REJECTION BRUH");
								  return false;
								  
							  }
							  
						  }
					  }
					  
					  //For all points x on CCW side s.t. (i,x) and (j,x) are green, red, or yellow (not too far away), then (a,x) cannot be too far away.
					  //For all points x on CCW side s.t. (a,x) is purple, x must be purple to at least one of i and j.
					  
					  int x = i+1;
					  while(x != j){
						  
							pair.clear();
							computePair(x, i, pair, points);
							String iColor = edgeColor.get(pair);
							
							pair.clear();
							computePair(x, j, pair, points);
							String jColor = edgeColor.get(pair);
							
							pair.clear();
						  computePair(x, a, pair, points);
						  String aColor = edgeColor.get(pair);
								
						  if(mustBeClose(iColor) && mustBeClose(jColor)){
							  
							  if(aColor.equals("purple")){
								 /* System.out.println("Rejecting: " + points);
								  System.out.println("Need " + points.get(z) + " to block " + points.get(i) + " and " + points.get(j) + " but blocker needs to be too far from " + points.get(x));
								  System.exit(1);*/
								  return false;
							  }
							  else if(aColor.equals("orange")){
								  edgeColor.replace(pair,"red");
								  updatedAnEdge = true;
							  }
							  else if(aColor.equals("cyan")){
								  edgeColor.replace(pair,"yellow");
								  updatedAnEdge = true;
							  }
							  else if(aColor.equals("blue")){
								  edgeColor.replace(pair,"green");
								  updatedAnEdge = true;
							  }
						  }
						  
						  if(aColor.equals("purple")){
							  
								if(mustBeClose(iColor))
								{
									pair.clear();
									computePair(x, j, pair, points);
									datColor = edgeColor.get(pair);
									if(!datColor.equals("purple")){
										edgeColor.replace(pair,"purple");
										updatedAnEdge = true;
									}
									
									int w = j+1;
									if(w== points.size()) w=0;
									while(w!=a){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											return false;
										}
										else if(!wColor.equals("purple")){
											edgeColor.replace(pair,"purple");
											updatedAnEdge = true;
										}
										
										w++;
										if(w == points.size()) w=0;
									}
								}
								else if(mustBeClose(jColor))
								{
									pair.clear();
									computePair(x, i, pair, points);
									datColor = edgeColor.get(pair);
									if(!datColor.equals("purple")){
										edgeColor.replace(pair,"purple");
										updatedAnEdge = true;
									}
									
									int w = a+1;
									if(w== points.size()) w=0;
									while(w!=i){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											return false;
										}
										else if(!wColor.equals("purple")){
											edgeColor.replace(pair,"purple");
											updatedAnEdge = true;
										}
										
										w++;
										if(w == points.size()) w=0;
									}
								}
					
							  
						  }
						  
						  x++;
						  //if(x == points.size()) x = 0;
					  }
			  }
			  else{
				  
				  //TO DO LATER BRAH
			  }
			  
			  
			}
		  }
		  
	  }
	  
    } while (updatedAnEdge);

	
	
    return true;
  }
  
  
  public static boolean mustBeClose(String color){
	  
	  return color.equals("green") || color.equals("yellow") || color.equals("red");
  }

  public static class RunIt extends Thread {

    public void run() {
      try {
        // Administrative stuff to connect with the server.
        BufferedReader in;
        PrintWriter out;

        // We are connecting on port 9450.
        String serverAddress = "52.203.213.88";
        Socket socket = new Socket(serverAddress, 9450);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
          String nextCase = in.readLine();
          if (nextCase.equals("STOP")) {
            System.out.println("something got wrecked, check server");
            return;
          }
          if (nextCase.equals("FINISHED")) {
            System.out.println("no additional cases left to work on for this thread");
			moreThreads = false;
            return;
          }
          int firstComma = nextCase.indexOf(',');
          String caseNumber = nextCase.substring(0, firstComma);
          String fileStuff = nextCase.substring(firstComma + 1);

          // nextCase == "1,ACEG,BDFH,ADEH,ABEF,BCFG,CDGH,A,B,C,D,E,F,G,H"
          // caseNumber == "1"
          // fileStuff == "ACEG,BDFH,ADEH,ABEF,BCFG,CDGH,A,B,C,D,E,F,G,H"
          /*
           * case1.txt: ACEG BDFH ADEH ABEF BCFG CDGH A B C D E F G H
           */
          String[] arr = fileStuff.split(",");
          ArrayList<String> ordering = new ArrayList<String>();
          for (String item : arr) {
            ordering.add(item);
          }
          System.out.println("working on case " + nextCase);

          String result = determineCase(ordering);
          if (!result.equals("")) {
            result = result.replace(" ", "");  //gtfo spaces
            result = result.substring(1, result.length()-1);  //gtfo braces
            System.out.println("Case accepted. \n" + result + "\n"); //ordering on own line
            out.println(result);
          } else {
            System.out.println("Case rejected (which is what we want).\n");
			System.out.flush();
            out.println("S");
          }
          // case was done successfully, tell server about it
          // DONE SOLVING THE CASE

          // assuming the case completed successfully, write back S
          // if the case failed, output anything but S

          System.out.println("solved case " + nextCase);
		  System.out.flush();
        }
      } catch (Exception e) {
        System.out.println("Got an exception: " + e);
		e.printStackTrace();
      }
    }
  }
}
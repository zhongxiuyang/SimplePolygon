
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
		//String[] allPoints = new String[]{ "ABCEG", "BDCFH", "BDEFH", "ACEFG", "BDFGH", "ABDEG", "ABDFG", "ACDFG", "ABDEH", "ABDFH", "ACDFH", "ABEFH", "ACEFH", "BCEFH", "ABDGH", "ACDGH", "ABEGH", "ACEGH", "BCEGH", "ADEGH", "BDEGH", "ADG", "BEH", "ACF", "BDG", "CEH", "ADF", "CFH", "ACDG", "BDEH", "ACEF", "BDFG", "CEGH", "ADFH", "ABEG", "BCFH" };
		//String[] allPoints = new String[]{"ABC", "ABD", "ACD", "BCD", "ABCD", "ABE", "ACE", "BCE", "ABCE", "ADE", "BDE", "ABDE", "CDE", "ACDE", "BCDE", "ABCDE", "ABF", "ACF", "BCF", "ABCF", "ADF", "BDF", "ABDF", "CDF", "ACDF", "BCDF", "ABCDF", "AEF", "BEF", "ABEF", "CEF", "ACEF", "BCEF", "ABCEF", "DEF", "ADEF", "BDEF", "ABDEF", "CDEF", "ACDEF", "BCDEF", "ABCDEF", "ABG", "ACG", "BCG", "ABCG", "ADG", "BDG", "ABDG", "CDG", "ACDG", "BCDG", "ABCDG", "AEG", "BEG", "ABEG", "CEG", "ACEG", "BCEG", "ABCEG", "DEG", "ADEG", "BDEG", "ABDEG", "CDEG", "ACDEG", "BCDEG", "ABCDEG", "AFG", "BFG", "ABFG", "CFG", "ACFG", "BCFG", "ABCFG", "DFG", "ADFG", "BDFG", "ABDFG", "CDFG", "ACDFG", "BCDFG", "ABCDFG", "EFG", "AEFG", "BEFG", "ABEFG", "CEFG", "ACEFG", "BCEFG", "ABCEFG", "DEFG", "ADEFG", "BDEFG", "ABDEFG", "CDEFG", "ACDEFG", "BCDEFG", "ABH", "ACH", "BCH", "ABCH", "ADH", "BDH", "ABDH", "CDH", "ACDH", "BCDH", "ABCDH", "AEH", "BEH", "ABEH", "CEH", "ACEH", "BCEH", "ABCEH", "DEH", "ADEH", "BDEH", "ABDEH", "CDEH", "ACDEH", "BCDEH", "ABCDEH", "AFH", "BFH", "ABFH", "CFH", "ACFH", "BCFH", "ABCFH", "DFH", "ADFH", "BDFH", "ABDFH", "CDFH", "ACDFH", "BCDFH", "ABCDFH", "EFH", "AEFH", "BEFH", "ABEFH", "CEFH", "ACEFH", "BCEFH", "ABCEFH", "DEFH", "ADEFH", "BDEFH", "ABDEFH", "CDEFH", "ACDEFH", "BCDEFH", "AGH", "BGH", "ABGH", "CGH", "ACGH", "BCGH", "ABCGH", "DGH", "ADGH", "BDGH", "ABDGH", "CDGH", "ACDGH", "BCDGH", "ABCDGH", "EGH", "AEGH", "BEGH", "ABEGH", "CEGH", "ACEGH", "BCEGH", "ABCEGH", "DEGH", "ADEGH", "BDEGH", "ABDEGH", "CDEGH", "ACDEGH", "BCDEGH", "FGH", "AFGH", "BFGH", "ABFGH", "CFGH", "ACFGH", "BCFGH", "ABCFGH", "DFGH", "ADFGH", "BDFGH", "ABDFGH", "CDFGH", "ACDFGH", "BCDFGH", "EFGH", "AEFGH", "BEFGH", "ABEFGH", "CEFGH", "ACEFGH", "BCEFGH", "DEFGH", "ADEFGH", "BDEFGH", "CDEFGH"}; 

    //10 guard starting points: ACEGI, BDFHJ, A
    //String[] allPoints = new String[]{"ACDFHI", "BDEGIJ", "ACEFHJ", "ABDFGI", "BCEGHJ", "ADFI", "BEGJ", "ACFH", "BDGI", "CEHJ"};
	
	String[] allPoints = {"ABDFHI", "ADEGHJ", "ABDEHI", "BCEFHI", "BCFGIJ", "BCEFHJ", "BCEGHJ", "BDEGHJ", "BDFGIJ", "ACDGHJ", "ACDFHJ", "ACDFHI", "ABDFGI", "ABDEGI", "ABDEGH", "ABEFHI", "CDFGIJ", "BCEFIJ", "BCEGIJ", "BDEGIJ", "ACDFGJ", "ACDFGI", "ACEGHJ", "ACEFHJ", "ACEFHI", "ABCEGI", "BCDFHJ", "ACDEGI", "BDEFHJ", "ACEFGI", "BDFGHJ", "ACEGHI", "BDEHIJ"};


		
		for (String s : allPoints)
		{
			everyViewpoints.add(s);
		}
		ArrayList<String> usableViewpoints = new ArrayList<String>();
		usableViewpoints = availableViewpoints(ordering, everyViewpoints);
		//int viewPointsToCheck = 40;
		if (usableViewpoints.size() <= 0)//allPoints.length - viewPointsToCheck)
		{
			System.out.println("We placed all the VPs.");//" + viewPointsToCheck + " VPs.");//everything. ");
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
            int numFeasibleGaps = 0;
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
          numFeasibleGaps++;

          if(numFeasibleGaps >+ minGaps){
            //This already cannot be the optimal point so skip it.
            break;
          }
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
	HashMap<ArrayList<String>, String> edgeColorCopy = new HashMap<ArrayList<String>, String>();
	HashMap<ArrayList<String>, String> coloringWeWouldHaveAccepted = new HashMap<ArrayList<String>, String>();
	boolean inFlippingPhase = false;
	ArrayList<String> flippedEdge = null;
	
	
	//-10 means they cannot be blocked
	//-1 means they can be blocked but no specific blocker.
	//>=0 means they can only be blocked by the point with the given index.
	HashMap<ArrayList<String>, Integer> canBlockBelow = new HashMap<ArrayList<String>, Integer> ();
	HashMap<ArrayList<String>, Integer> canBlockAbove = new HashMap<ArrayList<String>, Integer> ();
	

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
			canBlockBelow.put(temp,-1);
			canBlockAbove.put(temp,-1);
          } else {
            if (points.get(j).indexOf(points.get(i)) == -1) {
              edgeColor.put(temp, "orange");
			  canBlockBelow.put(temp,-1);
			  canBlockAbove.put(temp,-1);
            } else {
              edgeColor.put(temp, "green");
			  canBlockBelow.put(temp,-10);
			  canBlockAbove.put(temp,-10);
            }
          }
        } else {
          if (points.get(j).length() > 1) {
            edgeColor.put(temp, "cyan");
			canBlockBelow.put(temp,-1);
			canBlockAbove.put(temp,-1);
          } else {
            if (points.get(i).indexOf(points.get(j)) == -1) {
              edgeColor.put(temp, "orange");
			  canBlockBelow.put(temp,-1);
			  canBlockAbove.put(temp,-1);
            } else {
              edgeColor.put(temp, "green");
			  canBlockBelow.put(temp,-10);
			  canBlockAbove.put(temp,-10);
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
	  encircleing:
      for (int i = 0; i < points.size() - 3; i++) {
		  
		  int stoppingIndexForL = points.size();
		  if(i==0)
			  stoppingIndexForL = points.size()-1;
		  //else if(i==1)
		  //	  stoppingIndexForL = points.size()-1;
		  
        for (int l = i + 2; l != stoppingIndexForL; l++) {
          pair.clear();
          computePair(i, l, pair, points);
		  
		  if(canBlockAbove.get(pair) < -1 && canBlockBelow.get(pair) < -1)
			continue;
			
		  //int belowBlocker = -1;
		  if(canBlockBelow.get(pair) >= -1){
			  
				int j = i;
				String jlColor="";
				do{
				  j++;
				  if(j<l){
					pair.clear();
					computePair(j, l, pair, points);
					jlColor = edgeColor.get(pair);
				  }
				}while(j<l && !cannotBlock(jlColor));
				  
			  
				int k=l;
				String ikColor="";
				do{
					k--;
					if(k>=j){
						pair.clear();
						computePair(i, k, pair, points);
						ikColor = edgeColor.get(pair);
					}
					
				}while(k >= j && !cannotBlock(ikColor));
				
				
				if(k>j){
					pair.clear();
					computePair(i, l, pair, points);
					canBlockBelow.put(pair,-10);
				}
				else if(k==j){
					pair.clear();
					computePair(i, l, pair, points);
					canBlockBelow.put(pair,k);
				}			  
			  
		  }
		  
		  pair.clear();
		  computePair(i, l, pair, points);
		  if(canBlockAbove.get(pair) >= -1){
			  
				int m = l;
				String imColor="";
				do{
				  m = (m+1)%points.size();
				  if(m!=i){
					pair.clear();
					computePair(i, m, pair, points);
					imColor = edgeColor.get(pair);
				  }
				}while(m!=i && !cannotBlock(imColor));
				  
			  
				int n = i;
				int stoppingIndexForN = m-1;
				if(m==0) stoppingIndexForN = points.size()-1;
				
				String lnColor="";
				do{
					n--;
					if(n < 0) n = points.size()-1;
					if(n!=stoppingIndexForN){
						pair.clear();
						computePair(n, l, pair, points);
						lnColor = edgeColor.get(pair);
					}
					
				}while(n!=stoppingIndexForN && !cannotBlock(lnColor));
				
				
				if(n == m){
					pair.clear();
					computePair(i, l, pair, points);
					canBlockAbove.put(pair,n);
				}
				else if(n!=stoppingIndexForN){
					pair.clear();
					computePair(i, l, pair, points);
					canBlockAbove.put(pair,-10);
				}
						  
			  
		  }
		  
		  if(canBlockAbove.get(pair) < -1 && canBlockBelow.get(pair) < -1){
			  
			  //Last time we could block on one side, but now we cannot block on either side.
			 
				pair.clear();
				computePair(i, l, pair, points);
				//pair.add(points.get(i));
				//pair.add(points.get(l));

				if (edgeColor.get(pair).toLowerCase().compareTo("orange") == 0) {
				  edgeColor.replace(pair, "purple");
				  updatedAnEdge = true;
				}
				else if (edgeColor.get(pair).toLowerCase().compareTo("cyan") == 0) {
				  edgeColor.replace(pair, "blue");
				  updatedAnEdge = true;
				}
				else if(edgeColor.get(pair).toLowerCase().compareTo("yellow") == 0) {
				  edgeColor.replace(pair, "green");
				  updatedAnEdge = true;
				}
				else if(edgeColor.get(pair).toLowerCase().compareTo("red") == 0) {
					if(flippedEdge == null)
						return false;
					else if(edgeColor.get(flippedEdge).equals("RED")){
						edgeColorCopy.replace(flippedEdge,"green");
						edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
						updatedAnEdge = true;
						flippedEdge = null;
						break encircleing;
					}
					else if(edgeColor.get(flippedEdge).equals("GREEN")){
						edgeColorCopy.replace(flippedEdge,"red");
						edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
						updatedAnEdge = true;
						flippedEdge = null;
						break encircleing;
					}
					else{
						System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
					}
				}
                  
			  
		  }
		  
        }
      }//End encircleing 
	  
	  //Begin crossing lemma.
	  crossingLemma:
      for (int i = 0; i < points.size() - 3; i++) {
        for (int k = i + 2; k < points.size() - 1; k++) {
          pair.clear();
          pair.add(points.get(i));
          pair.add(points.get(k));
		  String ikColor = edgeColor.get(pair);
          if (!mustBeClose(ikColor) && !ikColor.equalsIgnoreCase("blue")) {
            continue;
          }
          for (int j = i + 1; j < k; j++) {
            for (int l = k + 1; l < points.size(); l++) {
              pair.clear();
              pair.add(points.get(j));
              pair.add(points.get(l));
			  String jlColor = edgeColor.get(pair);
			  if ((!mustBeClose(jlColor) && !jlColor.equalsIgnoreCase("blue")) || (ikColor.equalsIgnoreCase("blue") && jlColor.equalsIgnoreCase("blue"))) {
				continue;
			  }
			  
			  boolean bothClose = mustBeClose(ikColor) && mustBeClose(jlColor);
			  
			  
              if (bothClose) {
				  
				  
                pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(j));
                if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(k));
                  pair.add(points.get(l));
                  if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
					  
                    if(flippedEdge == null)
						return false;
					else if(edgeColor.get(flippedEdge).equals("RED")){
						edgeColorCopy.replace(flippedEdge,"green");
						edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
						updatedAnEdge = true;
						flippedEdge = null;
						break crossingLemma;
					}
					else if(edgeColor.get(flippedEdge).equals("GREEN")){
						edgeColorCopy.replace(flippedEdge,"red");
						edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
						updatedAnEdge = true;
						flippedEdge = null;
						break crossingLemma;
					}
					else{
						System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
					}
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("cyan") == 0) {
                    edgeColor.replace(pair, "yellow");
                    updatedAnEdge = true;
                  }
                }


                pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(l));
                if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(j));
                  pair.add(points.get(k));
                  if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
					  
                    if(flippedEdge == null)
						return false;
					else if(edgeColor.get(flippedEdge).equals("RED")){
						edgeColorCopy.replace(flippedEdge,"green");
						edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
						updatedAnEdge = true;
						flippedEdge = null;
						break crossingLemma;
					}
					else if(edgeColor.get(flippedEdge).equals("GREEN")){
						edgeColorCopy.replace(flippedEdge,"red");
						edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
						updatedAnEdge = true;
						flippedEdge = null;
						break crossingLemma;
					}
					else{
						System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
					}
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("cyan") == 0) {
                    edgeColor.replace(pair, "yellow");
                    updatedAnEdge = true;
                  }
                }

       

                pair.clear();
                pair.add(points.get(k));
                pair.add(points.get(l));
                if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(i));
                  pair.add(points.get(j));
                  if (edgeColor.get(pair).toLowerCase().compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("cyan") == 0) {
                    edgeColor.replace(pair, "yellow");
                    updatedAnEdge = true;
                  }
                }


                pair.clear();
                pair.add(points.get(j));
                pair.add(points.get(k));
                if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(i));
                  pair.add(points.get(l));
                  if (edgeColor.get(pair).toLowerCase().compareTo("orange") == 0) {
                    edgeColor.replace(pair, "red");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("blue") == 0) {
                    edgeColor.replace(pair, "green");
                    updatedAnEdge = true;
                  }
				  else if (edgeColor.get(pair).toLowerCase().compareTo("cyan") == 0) {
                    edgeColor.replace(pair, "yellow");
                    updatedAnEdge = true;
                  }
                }

              }
			  else{
				  
				  //one edge is blue and one is close.
				  pair.clear();
					pair.add(points.get(i));
					pair.add(points.get(j));
					if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
					  pair.clear();
					  pair.add(points.get(k));
					  pair.add(points.get(l));
					  if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
						  
						//Set blue edge to purple.
						if(ikColor.equalsIgnoreCase("blue")){
							pair.clear();
							pair.add(points.get(i));
							pair.add(points.get(k));
							edgeColor.replace(pair,"purple");
							updatedAnEdge = true;
						}
						else if(jlColor.equalsIgnoreCase("blue")){
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
					if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
					  pair.clear();
					  pair.add(points.get(j));
					  pair.add(points.get(k));
					  if (edgeColor.get(pair).toLowerCase().compareTo("purple") == 0) {
						  
						//Set blue edge to purple.
						if(ikColor.equalsIgnoreCase("blue")){
							pair.clear();
							pair.add(points.get(i));
							pair.add(points.get(k));
							edgeColor.replace(pair,"purple");
							updatedAnEdge = true;
						}
						else if(jlColor.equalsIgnoreCase("blue")){
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
	  
	  
	  //Begin Purple/Green Crossing.
	  purpleGreenCrossing:
	  for (int i = 0; i < points.size() - 3; i++) {
        for (int k = i + 2; k < points.size(); k++) {
          pair.clear();
          computePair(i, k, pair, points);
		  String ikColor = edgeColor.get(pair);
          if (!ikColor.equalsIgnoreCase("purple")) {
            continue;
          }
          for (int j = i + 1; j < k; j++) {
			  pair.clear();
			  computePair(i, j, pair, points);
			  String ijColor = edgeColor.get(pair);
			  
			  pair.clear();
			  computePair(j, k, pair, points);
			  String jkColor = edgeColor.get(pair);
			  
			  if (!ijColor.equalsIgnoreCase("purple") && !jkColor.equalsIgnoreCase("purple")) {
				  //If neither is purple, skip this j.
				continue;
			  }
			  
			  if(ijColor.equalsIgnoreCase("red") || jkColor.equalsIgnoreCase("red")){
				  //If you already know you have to block, then skip this j.
				  continue;
			  }
			  
			  
			  
            for (int l = (k + 1)%points.size(); l != i; l = (l+1)%points.size()) {
              pair.clear();
			  computePair(l, j, pair, points);
			  String jlColor = edgeColor.get(pair);
			  
			  if(!jlColor.equalsIgnoreCase("green"))
				continue;
				
				
				
			  pair.clear();
			  computePair(l, i, pair, points);
			  String ilColor = edgeColor.get(pair);
			  
			  pair.clear();
			  computePair(l, k, pair, points);
			  String klColor = edgeColor.get(pair);
			  
			  if (!ilColor.equalsIgnoreCase("purple") && !klColor.equalsIgnoreCase("purple")) {
				  //If neither is purple, skip this l.
				continue;
			  }
			  
			  if(ilColor.equalsIgnoreCase("red") || klColor.equalsIgnoreCase("red")){
				  //If you already know you have to block, then skip this l.
				  continue;
			  }
			  
			  
			  
			  if(ijColor.equalsIgnoreCase("purple") && klColor.equalsIgnoreCase("purple")){
				  
				  if(cannotBlock(ilColor) || cannotBlock(jkColor)){
					  //Only consider if we know at least one side cannot be blocked.
					  
				  
				  
				  //At least one of the other sides cannot be blocked so we
				  //might be able to change an edge color or reject.
				  
				  //System.out.println("i = " + points.get(i));
				//System.out.println("j = " + points.get(j));
				//System.out.println("k = " + points.get(k));
				//System.out.println("l = " + points.get(l));
				  
				  int pointInDiscI = -1;
				  //Is there a point p between j and k that is in i's disk?
				  for(int p = j+1; p<k; p++){
					  pair.clear();
					  computePair(i, p, pair, points);
					  String ipColor = edgeColor.get(pair);
					  
					  if(mustBeClose(ipColor)){
						  pointInDiscI = p;
						  break;
					  }
				  }
				  
				  if(pointInDiscI >= 0){
					  
					  for(int p = (l+1)%points.size(); p != i; p = (p+1)%points.size()){
						  
							pair.clear();
							computePair(k, p, pair, points);
							String kpColor = edgeColor.get(pair);
							
							if(mustBeClose(kpColor)){
								
								if(cannotBlock(ilColor) && cannotBlock(jkColor)){
									//System.out.println("REJECTION!!!");
									if(flippedEdge == null)
										return false;
									else if(edgeColor.get(flippedEdge).equals("RED")){
										edgeColorCopy.replace(flippedEdge,"green");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break purpleGreenCrossing;
									}
									else if(edgeColor.get(flippedEdge).equals("GREEN")){
										edgeColorCopy.replace(flippedEdge,"red");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break purpleGreenCrossing;
									}
									else{
										System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
									}
								}
								else if(cannotBlock(ilColor)){
									//System.out.println("Flipping j-k edge to red.");								
									pair.clear();
									computePair(k, j, pair, points);
									edgeColor.replace(pair,"red");
									updatedAnEdge = true;
								}
								else{
									//System.out.println("Flipping i-l edge to red.");
									pair.clear();
									computePair(i, l, pair, points);
									edgeColor.replace(pair,"red");
									updatedAnEdge = true;
								}
								
								break;
							}
						}
					  }
					  
				  }
				  
			  }
			  
			  if(ilColor.equalsIgnoreCase("purple") && jkColor.equalsIgnoreCase("purple")){
				  
				  if(!cannotBlock(ijColor) && !cannotBlock(klColor)){
					  //Both sides could potentially be blocked so don't mess with it.
					  continue;
				  }
				  
				  int pointInDiscI = -1;
				  //Is there a point p between j and k that is in i's disk?
				  for(int p = (k+1)%points.size(); p!=l; p = (p+1)%points.size()){
					  pair.clear();
					  computePair(i, p, pair, points);
					  String ipColor = edgeColor.get(pair);
					  
					  if(mustBeClose(ipColor)){
						  pointInDiscI = p;
						  break;
					  }
				  }
				  
				  if(pointInDiscI >= 0){
					  
					  for(int p = i+1; p < j; p++){
						  
							pair.clear();
							computePair(k, p, pair, points);
							String kpColor = edgeColor.get(pair);
							
							if(mustBeClose(kpColor)){
								
								if(cannotBlock(ijColor) && cannotBlock(klColor)){
								
									//System.out.println("REJECTION!!!");
									if(flippedEdge == null)
										return false;
									else if(edgeColor.get(flippedEdge).equals("RED")){
										edgeColorCopy.replace(flippedEdge,"green");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break purpleGreenCrossing;
									}
									else if(edgeColor.get(flippedEdge).equals("GREEN")){
										edgeColorCopy.replace(flippedEdge,"red");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break purpleGreenCrossing;
									}
									else{
										System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
									}
								}
								else if(cannotBlock(ijColor)){
									
									//System.out.println("Flipping k-l edge to red");
									pair.clear();
									computePair(k, l, pair, points);
									edgeColor.replace(pair,"red");
									updatedAnEdge = true;
								}
								else{
									//System.out.println("Flipping i-j edge to red");
									pair.clear();
									computePair(i, j, pair, points);
									edgeColor.replace(pair,"red");
									updatedAnEdge = true;
								}
								
							}
						  
					  }
					  
				  }
				  
			  }
			  
			}
		  }
		  
		}
		
	  }
	  //End green/purple crossing
	  
	  
	  //Begin find a blocker, bruh.
	  findABlocker:
	  for(int i=0; i<points.size(); i++){
		  
		  for(int j=i+2; j<points.size(); j++){
			  
			  pair.clear();
			  pair.add(points.get(i));
			  pair.add(points.get(j));
			  if(!edgeColor.get(pair).equalsIgnoreCase("red"))
				  continue;
			  
			  //CCW side "below i,j".
			  boolean edgeIsYellow = false;
			  int z;
			  boolean pointIsAGuard = points.get(i).length() == 1;
			  for(z=j-1; z>i; z--){
				  pair.clear();
				  pair.add(points.get(i));
				  pair.add(points.get(z));
				  String color = edgeColor.get(pair);
				  if(color.equalsIgnoreCase("green") || color.equalsIgnoreCase("blue") || (pointIsAGuard && color.equalsIgnoreCase("yellow"))){
					  if(color.equalsIgnoreCase("yellow")){
						  edgeIsYellow = true;
					  }
					  
					  break;
				  }
			  }
			  
			  if(edgeIsYellow)
				continue;
			  
			  int y;
			  pointIsAGuard = points.get(j).length() == 1;
			  for(y=i+1; y<j; y++){
				  pair.clear();
				  pair.add(points.get(y));
				  pair.add(points.get(j));
				  String color = edgeColor.get(pair);
				  if(color.equalsIgnoreCase("green") || color.equalsIgnoreCase("blue") || (pointIsAGuard && color.equalsIgnoreCase("yellow"))){
					  if(color.equalsIgnoreCase("yellow")){
						  edgeIsYellow = true;
					  }
					  break;
				  }
			  }
			  
			  if(edgeIsYellow)
				continue;
			  
			  boolean canBlockCCW = (z <= y);
			  
			  
			  
			  //Check CW side
			  int a = i-1;
			  pointIsAGuard = points.get(j).length() == 1;
			  if(a<0) a = points.size()-1;
			  while(a!=j){
				  pair.clear();
				  computePair(a, j, pair, points);
				  String color = edgeColor.get(pair);
				  if(color.equalsIgnoreCase("green") || color.equalsIgnoreCase("blue") || (pointIsAGuard && color.equalsIgnoreCase("yellow"))){
					  if(color.equalsIgnoreCase("yellow")){
						  edgeIsYellow = true;
					  }
					  break;
				  }
				  
				  a--;
				  if(a<0) a = points.size()-1;
			  }
			  
			  
			  if(edgeIsYellow) continue;
			  
			  int b = j+1;
			  pointIsAGuard = points.get(i).length() == 1;
			  if(b==points.size()) b = 0;
			  while(b!=i){
				  pair.clear();
				  computePair(b, i, pair, points);
				  String color = edgeColor.get(pair);
				  if(color.equalsIgnoreCase("green") || color.equalsIgnoreCase("blue") || (pointIsAGuard && color.equalsIgnoreCase("yellow"))){
					  if(color.equalsIgnoreCase("yellow")){
						  edgeIsYellow = true;
					  }
					  break;
				  }
				  
				  b++;
				  if(b==points.size()) b = 0;
			  }
			  
			  if(edgeIsYellow) continue;
			  
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
				  
				  if(flippedEdge == null)
						return false;
					else if(edgeColor.get(flippedEdge).equals("RED")){
						edgeColorCopy.replace(flippedEdge,"green");
						edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
						updatedAnEdge = true;
						flippedEdge = null;
						break findABlocker;
					}
					else if(edgeColor.get(flippedEdge).equals("GREEN")){
						edgeColorCopy.replace(flippedEdge,"red");
						edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
						updatedAnEdge = true;
						flippedEdge = null;
						break findABlocker;
					}
					else{
						System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
					}
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
					  if(datColor.equalsIgnoreCase("blue")){
						edgeColor.replace(pair,"green");
						 updatedAnEdge = true;
					  }
					  
					  pair.clear();
					  pair.add(points.get(z));
					  pair.add(points.get(j));
					  datColor = edgeColor.get(pair);
					  if(datColor.equalsIgnoreCase("blue")){
						edgeColor.replace(pair,"green");
						 updatedAnEdge = true;
					  }
					  
					  for(int zhongxiu = i+1; zhongxiu < z; zhongxiu++){
						  
						  for(int erik = z+1; erik < j; erik++){
							  
							  pair.clear();
							  pair.add(points.get(zhongxiu));
							  pair.add(points.get(erik));
							  
							  String theirColor = edgeColor.get(pair);
							  
							  if(theirColor.equalsIgnoreCase("yellow")){
									edgeColor.replace(pair,"red");
									updatedAnEdge = true;
							  }
							  else if(theirColor.equalsIgnoreCase("cyan")){
								  
								  edgeColor.replace(pair,"orange");
									updatedAnEdge = true;
								  
							  }
							  else if(theirColor.equalsIgnoreCase("blue") || theirColor.equalsIgnoreCase("green")){
								  
								  //System.out.println("REJECTION BRUH");
									if(flippedEdge == null)
										return false;
									else if(edgeColor.get(flippedEdge).equals("RED")){
										edgeColorCopy.replace(flippedEdge,"green");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break findABlocker;
									}
									else if(edgeColor.get(flippedEdge).equals("GREEN")){
										edgeColorCopy.replace(flippedEdge,"red");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break findABlocker;
									}
									else{
										System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
									}
								  
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
							  
							  if(zColor.equalsIgnoreCase("purple")){
								  
									  /*System.out.println("Rejecting: " + points);
									  System.out.println("Need " + points.get(z) + " to block " + points.get(i) + " and " + points.get(j) + " but blocker needs to be too far from " + points.get(x));
									System.exit(1);*/
									if(flippedEdge == null)
										return false;
									else if(edgeColor.get(flippedEdge).equals("RED")){
										edgeColorCopy.replace(flippedEdge,"green");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break findABlocker;
									}
									else if(edgeColor.get(flippedEdge).equals("GREEN")){
										edgeColorCopy.replace(flippedEdge,"red");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break findABlocker;
									}
									else{
										System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
									}
							  }
							  else if(zColor.equalsIgnoreCase("orange")){
								  edgeColor.replace(pair,"red");
								  updatedAnEdge = true;
							  }
							  else if(zColor.equalsIgnoreCase("cyan")){
								  edgeColor.replace(pair,"yellow");
								  updatedAnEdge = true;
							  }
							  else if(zColor.equalsIgnoreCase("blue")){
								  edgeColor.replace(pair,"green");
								  updatedAnEdge = true;
							  }
						  }
						  
						  if(zColor.equalsIgnoreCase("purple")){
							  
								if(mustBeClose(iColor))
								{
									pair.clear();
									computePair(x, j, pair, points);
									datColor = edgeColor.get(pair);
									if(!datColor.equalsIgnoreCase("purple")){
										edgeColor.replace(pair,"purple");
										updatedAnEdge = true;
									}
									
									for(int w = z+1; w<j; w++){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											if(flippedEdge == null)
												return false;
											else if(edgeColor.get(flippedEdge).equals("RED")){
												edgeColorCopy.replace(flippedEdge,"green");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else if(edgeColor.get(flippedEdge).equals("GREEN")){
												edgeColorCopy.replace(flippedEdge,"red");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else{
												System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
											}
										}
										else if(!wColor.equalsIgnoreCase("purple")){
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
									if(!datColor.equalsIgnoreCase("purple")){
										edgeColor.replace(pair,"purple");
										updatedAnEdge = true;
									}
									
									for(int w=i+1; w<z; w++){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											if(flippedEdge == null)
												return false;
											else if(edgeColor.get(flippedEdge).equals("RED")){
												edgeColorCopy.replace(flippedEdge,"green");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else if(edgeColor.get(flippedEdge).equals("GREEN")){
												edgeColorCopy.replace(flippedEdge,"red");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else{
												System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
											}
										}
										else if(!wColor.equalsIgnoreCase("purple")){
											edgeColor.replace(pair,"purple");
											updatedAnEdge = true;
										}
										
									}
								}
								else{
									
									//All three of i, j, and z are outside of x's disk.
									//System.out.println("We want " + points.get(z) + " to block " + points.get(i) + " and " + points.get(j) + ", and all three of them must be outside " + points.get(x) + "'s disk");
									int w = i+1;
									//if(w == points.size()) w=0;
									boolean thereIsPointBetweenIandZinXDisc = false;
									while(w!=z){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											thereIsPointBetweenIandZinXDisc = true;
											break;
										}
										
										
										w++;
										
									}
									
									w = z+1;
									boolean thereIsPointBetweenJandZinXDisc = false;
									while(w!=j){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											thereIsPointBetweenJandZinXDisc = true;
											break;
											
										}
										
										w++;
										
									}
									
									
									if(thereIsPointBetweenJandZinXDisc && thereIsPointBetweenIandZinXDisc){
										
										if(flippedEdge == null)//System.out.println("REJECTION BRAH");
											{ return false; }
											else if(edgeColor.get(flippedEdge).equals("RED")){
												edgeColorCopy.replace(flippedEdge,"green");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else if(edgeColor.get(flippedEdge).equals("GREEN")){
												edgeColorCopy.replace(flippedEdge,"red");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else{
												System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
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
					  
					  //Every point x \in [i,z) must be red to every point w \in (y,j].
					  for(int x = i; x<z; x++){
					  
						  for(int w = y+1; w<=j; w++){
							pair.clear();
							computePair(w, x, pair, points);
							String xwColor = edgeColor.get(pair);
							
							if(!xwColor.equalsIgnoreCase("red")){
								//System.out.println("Erik flip: " + points.get(x) + " and " + points.get(w));
								edgeColor.replace(pair,"red");
								updatedAnEdge = true;
							}
						  }
					  }
					  
				  }
				  
			  }
			  else{
				  
				  //Blocking on CW side
				  if(a == b){
					  
					  //if (i,a) or (a,j) were blue, it should now be green.
					  pair.clear();
					  computePair(a, i, pair, points);
					 String datColor = edgeColor.get(pair);
					  if(datColor.equalsIgnoreCase("blue")){
						edgeColor.replace(pair,"green");
						 updatedAnEdge = true;
					  }
					  
					  pair.clear();
					  computePair(a, j, pair, points);
					  datColor = edgeColor.get(pair);
					  if(datColor.equalsIgnoreCase("blue")){
						edgeColor.replace(pair,"green");
						 updatedAnEdge = true;
					  }
					  
					  for(int zhongxiu = (j+1)%points.size(); zhongxiu != a; zhongxiu = (zhongxiu+1)%points.size()){
						  
						  for(int erik = (a+1)%points.size(); erik != i; erik = (erik+1)%points.size()){
							  
							  pair.clear();
							  computePair(zhongxiu, erik, pair, points);
							  
							  String theirColor = edgeColor.get(pair);
							  
							  if(theirColor.equalsIgnoreCase("yellow")){
									edgeColor.replace(pair,"red");
									updatedAnEdge = true;
							  }
							  else if(theirColor.equalsIgnoreCase("cyan")){
								  
								  edgeColor.replace(pair,"orange");
									updatedAnEdge = true;
								  
							  }
							  else if(theirColor.equalsIgnoreCase("blue") || theirColor.equalsIgnoreCase("green")){
								  
								  //System.out.println("REJECTION BRUH");
									if(flippedEdge == null)
										return false;
									else if(edgeColor.get(flippedEdge).equals("RED")){
										edgeColorCopy.replace(flippedEdge,"green");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break findABlocker;
									}
									else if(edgeColor.get(flippedEdge).equals("GREEN")){
										edgeColorCopy.replace(flippedEdge,"red");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break findABlocker;
									}
									else{
										System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
									}
								  
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
							  
							  if(aColor.equalsIgnoreCase("purple")){
								 /* System.out.println("Rejecting: " + points);
								  System.out.println("Need " + points.get(z) + " to block " + points.get(i) + " and " + points.get(j) + " but blocker needs to be too far from " + points.get(x));
								  System.exit(1);*/
									if(flippedEdge == null)
										return false;
									else if(edgeColor.get(flippedEdge).equals("RED")){
										edgeColorCopy.replace(flippedEdge,"green");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break findABlocker;
									}
									else if(edgeColor.get(flippedEdge).equals("GREEN")){
										edgeColorCopy.replace(flippedEdge,"red");
										edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
										updatedAnEdge = true;
										flippedEdge = null;
										break findABlocker;
									}
									else{
										System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
									}
							  }
							  else if(aColor.equalsIgnoreCase("orange")){
								  edgeColor.replace(pair,"red");
								  updatedAnEdge = true;
							  }
							  else if(aColor.equalsIgnoreCase("cyan")){
								  edgeColor.replace(pair,"yellow");
								  updatedAnEdge = true;
							  }
							  else if(aColor.equalsIgnoreCase("blue")){
								  edgeColor.replace(pair,"green");
								  updatedAnEdge = true;
							  }
						  }
						  
						  if(aColor.equalsIgnoreCase("purple")){
							  
								if(mustBeClose(iColor))
								{
									pair.clear();
									computePair(x, j, pair, points);
									datColor = edgeColor.get(pair);
									if(!datColor.equalsIgnoreCase("purple")){
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
											if(flippedEdge == null)
												return false;
											else if(edgeColor.get(flippedEdge).equals("RED")){
												edgeColorCopy.replace(flippedEdge,"green");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else if(edgeColor.get(flippedEdge).equals("GREEN")){
												edgeColorCopy.replace(flippedEdge,"red");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else{
												System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
											}
										}
										else if(!wColor.equalsIgnoreCase("purple")){
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
									if(!datColor.equalsIgnoreCase("purple")){
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
											if(flippedEdge == null)
												return false;
											else if(edgeColor.get(flippedEdge).equals("RED")){
												edgeColorCopy.replace(flippedEdge,"green");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else if(edgeColor.get(flippedEdge).equals("GREEN")){
												edgeColorCopy.replace(flippedEdge,"red");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else{
												System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
											}
										}
										else if(!wColor.equalsIgnoreCase("purple")){
											edgeColor.replace(pair,"purple");
											updatedAnEdge = true;
										}
										
										w++;
										if(w == points.size()) w=0;
									}
								}
								else{
									
									//All three of i, j, and a are outside of x's disk.
									//System.out.println("We want " + points.get(a) + " to block " + points.get(i) + " and " + points.get(j) + ", and all three of them must be outside " + points.get(x) + "'s disk");
									
									int w = j+1;
									if(w == points.size()) w=0;
									boolean thereIsPointBetweenJandAinXDisc = false;
									while(w!=a){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											thereIsPointBetweenJandAinXDisc = true;
											break;
										}
										
										
										w++;
										if(w == points.size()) w=0;
										
									}
									
									w = a+1;
									if(w== points.size()) w=0;
									boolean thereIsPointBetweenIandAinXDisc = false;
									while(w!=i){
										
										pair.clear();
										computePair(x, w, pair, points);
										String wColor = edgeColor.get(pair);
										
										if(mustBeClose(wColor)){
											thereIsPointBetweenIandAinXDisc = true;
											break;
											
										}
										
										w++;
										if(w == points.size()) w=0;
									}
									
									
									if(thereIsPointBetweenJandAinXDisc && thereIsPointBetweenIandAinXDisc){
										
										if(flippedEdge == null)//System.out.println("REJECTION BRAH");
												{ return false; }
											else if(edgeColor.get(flippedEdge).equals("RED")){
												edgeColorCopy.replace(flippedEdge,"green");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else if(edgeColor.get(flippedEdge).equals("GREEN")){
												edgeColorCopy.replace(flippedEdge,"red");
												edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
												updatedAnEdge = true;
												flippedEdge = null;
												break findABlocker;
											}
											else{
												System.out.println("Uhhhhh.....wat.  " + edgeColor + " is " + edgeColor.get(flippedEdge));
											}
										
									}
									
									
									
									
								}
					
							  
						  }
						  
						  x++;
						  //if(x == points.size()) x = 0;
					  }
			  }
			  else{
				  
				  //TO DO LATER BRAH
				  //Every point x \in [j,a) must be red to every point w \in (b,i].
					  for(int x = j; x!=a; x = (x+1)%points.size()){
					  
						  for(int w = (b+1)%points.size(); w!=i+1; w = (w+1)%points.size()){
							pair.clear();
							computePair(w, x, pair, points);
							String xwColor = edgeColor.get(pair);
							
							if(!xwColor.equalsIgnoreCase("red")){
								//System.out.println("Erik flip: " + points.get(x) + " and " + points.get(w));
								edgeColor.replace(pair,"red");
								updatedAnEdge = true;
							}
						  }
					  }
			  }
			  
			  
			}
		  }
		  
	  }
	  
	  
	  if(!updatedAnEdge){
		  
		  if(!inFlippingPhase){
			  
			 edgeColorCopy = new HashMap<ArrayList<String>, String>(edgeColor);
				inFlippingPhase = true;
			  
		  }
		  else if(flippedEdge==null){
			  
			  for(Map.Entry<ArrayList<String>, String> entry: edgeColor.entrySet()) {
				  
				  ArrayList<String> datPair = entry.getKey();
				  String datColor = edgeColorCopy.get(datPair);
				  
				  if(datColor.equals("YELLOW")){
					  edgeColorCopy.replace(datPair,"yellow");
				  }
			  }
			  
			  edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
			  
		  }
		  
		  //yellow = we haven't tried flipping it yet.
		  //RED = it was a yellow edge and we temporarily flipped it to red.
		  //GREEN = it was a yellow edge and we temporarily flipped it to green.
		  //YELLOW = both red and green were acceptable.
		  ArrayList<ArrayList<String>> yellowEdgesConnectingGuards = new ArrayList<ArrayList<String>>();
		  for(Map.Entry<ArrayList<String>, String> entry: edgeColor.entrySet()) {

			ArrayList<String> datPair = entry.getKey();
			
			if(datPair.get(0).length() == 1 && datPair.get(1).length() == 1){
				String datColor = edgeColor.get(datPair);
				if(datColor.equals("yellow")) {
					edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
					edgeColor.replace(datPair, "RED");
					updatedAnEdge = true;
					flippedEdge = new ArrayList<String>(datPair);
					break;
					
				}
				else if(datColor.equals("RED")){
					edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
					edgeColor.replace(datPair, "GREEN");
					updatedAnEdge = true;
					flippedEdge = new ArrayList<String>(datPair);
					break;
				}
				else if(datColor.equals("GREEN")){
					edgeColorCopy.replace(datPair, "YELLOW");
					edgeColor = new HashMap<ArrayList<String>, String>(edgeColorCopy);
					
					flippedEdge = null;
				}
				
			}
		}
		  
		  
		  
	  }
	  
	  
	  
    } while (updatedAnEdge);
	
	

	
	
    return true;
  }
  
  
  public static boolean mustBeClose(String color){
	  
	  return color.equalsIgnoreCase("green") || color.equalsIgnoreCase("yellow") || color.equalsIgnoreCase("red");
  }
  
  public static boolean cannotBlock(String color){
	  
	  return color.equalsIgnoreCase("green") || color.equalsIgnoreCase("blue") || color.equalsIgnoreCase("purple");
  }

  public static class RunIt extends Thread {
        
        public void run() {
			/*String fileStuff = "A,B,C,D,E,F,G,H,I,J";
			String[] arr = fileStuff.split(",");
             ArrayList<String> ordering = new ArrayList<String>();
            for (String item : arr) {
                ordering.add(item);
            }
			
			long startTime = System.currentTimeMillis();
                        String result = determineCase(ordering);
                        long endTime = System.currentTimeMillis();
                        
                        long runningTime = (endTime - startTime)/1000;
                        
                        if(!result.equals("")){
                            result = result.replace(" ", "");  //gtfo spaces
                            result = result.substring(1, result.length()-1);  //gtfo braces
                            System.out.println("Case accepted in " + runningTime + " seconds. \n" + result + "\n"); //ordering on own line
                            //sendMe = caseNumber+",";
                            //sendMe += result;
                        } else{
                            System.out.println("Case rejected in " + runningTime + " seconds (which is what we want).\n");
                            System.out.flush();
                           // sendMe = "S"+caseNumber;
                        }*/
			
					
            try {
                // Administrative stuff to connect with the server.                
                // We are connecting on port 9451.
                String serverAddress = "52.202.254.64";
                Socket socket = new Socket(serverAddress, 9451);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("1");  //sending a 1 to denote, I want to start
                
                while(true){
                    String nextCase = in.readLine();
                    //close socket for now
                    in.close();
                    out.close();
                    socket.close();
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
                    
                     //   * case1.txt: ACEG BDFH ADEH ABEF BCFG CDGH A B C D E F G H
                    
                    System.out.println("working on case " + nextCase);
                    String[] arr = fileStuff.split(",");
                    ArrayList<String> ordering = new ArrayList<String>();
                    for (String item : arr) {
                        ordering.add(item);
                    }
                    
                    int acegiIndex = ordering.indexOf("ACEGI");
                    int aIndex = ordering.indexOf("A");
                    boolean rejected = false;
                    if(aIndex < acegiIndex){                        
                        System.out.println("Redundant ordering: ACEG is right of A.  Rejecting.");
                        System.out.flush();
                        rejected = true;
                    }
                    
                    int bdfhjIndex = ordering.indexOf("BDFHJ");
                    int iIndex = ordering.indexOf("I");
                    int jIndex = ordering.indexOf("J");
                    if(eIndex < bdfhIndex && bdfhIndex < fIndex){
                        
                        System.out.println("Redundant ordering: BDFHJ is between I and J.  Rejecting.");
                        System.out.flush();
                        rejected = true;
                    }
                    
                    
                    int gIndex = ordering.indexOf("G");
                    int hIndex = ordering.indexOf("H");
                    if(gIndex < bdfhjIndex && bdfhjIndex < hIndex){
                        
                        System.out.println("Redundant ordering: BDFHJ is between G and H.  Rejecting.");
                        System.out.flush();
                        rejected = true;
                    }
                    
                    String sendMe = "";
                    if(!rejected){                        
                        long startTime = System.currentTimeMillis();
                        String result = determineCase(ordering);
                        long endTime = System.currentTimeMillis();
                        
                        long runningTime = (endTime - startTime)/1000;
                        
                        if(!result.equals("")){
                            result = result.replace(" ", "");  //gtfo spaces
                            result = result.substring(1, result.length()-1);  //gtfo braces
                            System.out.println("Case accepted in " + runningTime + " seconds. \n" + result + "\n"); //ordering on own line
                            sendMe = caseNumber+",";
                            sendMe += result;
                        } else{
                            System.out.println("Case rejected in " + runningTime + " seconds (which is what we want).\n");
                            System.out.flush();
                            sendMe = "S"+caseNumber;
                        }
                    } else{
                        //reject case
                        sendMe = "S"+caseNumber;
                    }
                    
                    socket = new Socket(serverAddress, 9451);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(sendMe);
                    
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
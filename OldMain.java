import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class OldMain {
	
	public static int fileNumber = 1;
	public static ArrayList<String> allViewpoints = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		ArrayList<String> vps = new ArrayList<String>();
		
		int numOfGuards = 8;
		vps.add("ACEG");
		vps.add("BDFH");
        vps.add("ADEH");
        vps.add("ABEF");
//        vps.add("BCFG");
//        vps.add("CDGH");
        generateAllPoints(numOfGuards);
        generateCases(numOfGuards, vps);
		processEachCase();
	}
	
	// generate all viewpoints of size 5 and does not contain 3 consecutive, we are not call this function, therefore useless
	public static void generateAllPoints(int numOfGuards)
	{
		// should read from vplist.txt
		for (int i = 0; i < (1 << numOfGuards); i++)
		{
			String s = "";
			for (int j = 0; j < numOfGuards; j++)
			{
				if ((i & (1 << j)) > 0)
				{
					s = s + (char)(j + 'A');
				}
			}
			
			if (s.length() == 5)
			{
				if (!s.contains("ABC") && !s.contains("BCD") && !s.contains("CDE") && !s.contains("DEF") && !s.contains("EFG") && !s.contains("FGH"))
				{
					allViewpoints.add(s);
					System.out.println(s);
				}
			}
		}
    }
    
	// write all cases into file as case1.txt, case2.txt ...
	// need to use helper functions permute(), permuteHelper(), swap(), generateCombination(), combHelper()
    public static void generateCases(int numOfGuards, ArrayList<String> viewpoints)
	{
    	List<int[]> combinations = generateCombination(viewpoints.size() + numOfGuards, viewpoints.size());
    	ArrayList<Integer> newCombination = new ArrayList<Integer>();
    	for (int[] combination : combinations)
    	{
    		newCombination.clear();
    		for(int x : combination)
    		{
    			newCombination.add(x);
    		}
    		
    		try {
				permute(newCombination, viewpoints, numOfGuards);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    private static void permute(ArrayList<Integer> indices, ArrayList<String> viewpoints, int numOfGuards) throws IOException
    {
    	permuteHelper(indices, viewpoints, numOfGuards, 0, viewpoints.size() - 1);
    }
    
    private static void permuteHelper(ArrayList<Integer> indices, ArrayList<String> viewpoints, int numOfGuards, int l, int r)
    {
    	if (l == r)
    	{
    		try
    		{
    			String fileName = "case" + Integer.toString(fileNumber) + ".txt";
              	fileNumber++;
              	FileWriter myWriter = new FileWriter(fileName);
              	ArrayList<String> temp = new ArrayList<String>();
              	temp.clear();
              	int guard = 0;
              	for (int i = 0; i < viewpoints.size() + numOfGuards; i++)
              	{
              		temp.add("null");
              	}
              	for (int i = 0; i < indices.size(); i++)
              	{
              		temp.set(indices.get(i), viewpoints.get(i));
              	}
              	for (int i = 0; i < temp.size(); i++)
              	{
              		if (temp.get(i).compareTo("null") == 0)
              		{
              			temp.set(i, String.valueOf((char) (guard + 'A')));
              			guard++;
              		}
              	}
              	if (guard != numOfGuards)
              	{
              		System.out.println("Error: guard number invalid");
              	}
              	for (int i = 0; i < temp.size(); ++i)
              	{
              		myWriter.write(temp.get(i));
              		myWriter.write("\n");
              	}
        	    myWriter.close();
    		} catch (IOException e) {
    	        System.out.println("An error occurred.");
    	        e.printStackTrace();
    	    }
    	}	
    	else
    	{
    		for (int i = l; i <= r; i++)
    		{
    			indices = swap(indices, l, i);
    			permuteHelper(indices, viewpoints, numOfGuards, l + 1, r);
    			indices = swap(indices, l, i);
    		}
    	}
    }
    
    private static ArrayList<Integer> swap(ArrayList<Integer> indices, int i, int j)
    {
    	ArrayList<Integer> indicesCopy = new ArrayList<Integer>();
    	indicesCopy.addAll(indices);
    	int temp = indicesCopy.get(i);
    	indicesCopy.set(i, indicesCopy.get(j));
    	indicesCopy.set(j, temp);
    	return indicesCopy;
    }
    
    private static List<int[]> generateCombination(int n, int r)
    {
    	List<int[]> combinations = new ArrayList<>();
    	combHelper(combinations, new int[r], 0, n-1, 0);
    	return combinations;
    }
    
    private static void combHelper(List<int[]> combinations, int data[], int start, int end, int index)
    {
	    if (index == data.length) {
	        int[] combination = data.clone();
	        combinations.add(combination);
	    } else if (start <= end) {
	        data[index] = start;
	        combHelper(combinations, data, start + 1, end, index + 1);
	        combHelper(combinations, data, start + 1, end, index);
	    }
    }
    
    // useless function
    private static int permutation(int k)
    {
    	if (k < 0) { System.out.println("Error: invalid input. \n"); }
    	if (k == 1 || k == 0) { return 1; }
    	return k * permutation(k - 1);
    }
    
    // read caseX.txt and return entire file as an arraylist of string
    public static ArrayList<String> readFromFile(String filename)
	{
    	ArrayList<String> ordering = new ArrayList<String>();
    	try {
    	    BufferedReader lineReader = new BufferedReader(new FileReader(filename));
    	    String lineText = null;
    	    while ((lineText = lineReader.readLine()) != null) {
    	        ordering.add(lineText);
    	    }
    	    lineReader.close();
    	} catch (IOException ex) {
    	    System.err.println(ex);
    	}
    	return ordering;
	}
    
    // process each case from file and determine if this case feasible
    // first check if file exists, then call determineCase()
    public static void processEachCase()
    {
    	boolean isEndOfFiles = false;
    	int caseNumber = 1;
    	String fileName;
		do
		{
			fileName = "case" + Integer.toString(caseNumber) + ".txt";
			File f = new File(fileName); 
			if(f.exists() && f.isFile()) 
			{
				// System.out.println(fileName + " exists");
				ArrayList<String> ordering = new ArrayList<String>();
				ordering = readFromFile(fileName);
				// determine this case
				if (determineCase(ordering))
				{
					System.out.println(fileName + " is accepted.");
				}
				else
				{
					System.out.println(fileName + " is rejected.");
				}
			}
			else
			{
				// System.out.println(fileName + " does not exist"); 
				isEndOfFiles = true;
			}
			caseNumber++;
		} while(!isEndOfFiles);
    }
    
    // recursively determine if this case is feasible
    public static boolean determineCase(ArrayList<String> ordering)
    {
    	ArrayList<String> everyViewpoints = new ArrayList<String>();
		String[] allPoints = new String[]{ "ABDEG", "ABDFG", "ACDFG", "ABDEH", "ABDFH", "ACDFH", "ABEFH", "ACEFH", "BCEFH", "ABDGH", "ACDGH", "ABEGH", "ACEGH", "BCEGH", "ADEGH", "BDEGH" };
		for (String s : allPoints)
		{
			everyViewpoints.add(s);
		}
		ArrayList<String> usableViewpoints = new ArrayList<String>();
		usableViewpoints = availableViewpoints(ordering, everyViewpoints);
		if (usableViewpoints.size() == 0)
		{
			System.out.println("We placed everything. ");
			return true;
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
			for (int j = 0; j < ordering.size() + 1; ++j)
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
			    return false;
		    }
		}
        for (int i = 0; i < optimalGaps.size(); ++i)
        {
            ArrayList<String> recurseOrdering = new ArrayList<String>();
            recurseOrdering.addAll(ordering);
            recurseOrdering.add(optimalGaps.get(i), usableViewpoints.get(optimalViewpointIndex));
            if (determineCase(recurseOrdering))
            {
                return true;
            }
        }
        return false;
    }
    
    // helper function, return the available viewpoints you could still use
    public static ArrayList<String> availableViewpoints(ArrayList<String> usedPoints, ArrayList<String> candidatePoints)
    {
    	ArrayList<String> feasibleViewpoints = new ArrayList<String>();
    	feasibleViewpoints.addAll(candidatePoints);
    	feasibleViewpoints.removeAll(usedPoints);
    	return feasibleViewpoints;
    }
    
    // main stuff, given an ordering simply return true or false if feasible
    public static boolean isFeasibleOrdering(ArrayList<String> points)
	{
        // System.out.println(points);
	// 	  Edge colors:
    //    Green - See each other - unpierceable and close
    //    Red - Do not see other - close - must be pierced
    //    Purple - Do not see each other - cannot be pierced - too far
    //    Orange - Do not see each other - don't care how we block.
    //    Blue - Don't care if see each other - cannot be pierced.
    //    Cyan - Don't care if see each other - don't care if pierced.
    	
    	HashMap<ArrayList<String>, String> edgeColor = new HashMap<ArrayList<String>, String>();
    	
//        For every pair of points (u,v):
//        -If u is a guard and v is a viewpoint (or vice versa):
//             - If u sees v, set edgeColor(u,v) = green.
//             - Else set edgeColor(u,v) = orange.
//        -If u and v are both viewpoints or are both guards
//             - Initialize edgeColor(u,v) = cyan.
    	for (int i = 0; i < points.size() - 1; i++)
    	{
    		for (int j = i + 1; j < points.size(); j++)
    		{
    			ArrayList<String> temp = new ArrayList<String>();
    			temp.clear();
    			temp.add(points.get(i));
    			temp.add(points.get(j));
    			//System.out.print(temp);

    			if (points.get(i).length() == 1)
    			{
    				if(points.get(j).length() == 1)
    				{
    					edgeColor.put(temp, "cyan");
    				}
    				else
    				{
    					if (points.get(j).indexOf(points.get(i)) == -1)
    					{
    						edgeColor.put(temp, "orange");
    					}
    					else
    					{
    						edgeColor.put(temp, "green");
    					}
    				}
    			}
    			else
    			{
    				if(points.get(j).length() > 1)
    				{
    					edgeColor.put(temp, "cyan");
    				}
    				else
    				{
    					if (points.get(i).indexOf(points.get(j)) == -1)
    					{
    						edgeColor.put(temp, "orange");
    					}
    					else
    					{
    						edgeColor.put(temp, "green");
    					}
    				}
    			}
    		}
    	}
//        Do until all edge colors have converged:
//        Boolean updatedAnEdge;
//        do{
//          updatedAnEdge = false;
//          For each cyan (resp. orange) edge:
//            Check for CW OC and CCW OC that use edges that are either green, purple, or blue.
//            If have on both sides, flip edge color to blue (resp. purple) and set updatedAnEdge to true.
//        }until(updatedAnEdge);
        
    	boolean updatedAnEdge = false;
    	do {
    		ArrayList<String> pair = new ArrayList<String>();
    		updatedAnEdge = false;
    		for (int i = 0; i < points.size() - 5; i++)
        	{
    			for (int l = i + 3; l < points.size() - 2; l++)
				{
    				pair.clear();
	    			pair.add(points.get(i));
	    			pair.add(points.get(l));
	    			if ((edgeColor.get(pair).compareTo("orange") != 0) && (edgeColor.get(pair).compareTo("cyan") != 0))
	    			{
	    				continue;
	    			}
	    			for (int j = i + 1; j < l - 1; j++)
	    			{
	    				pair.clear();
		    			pair.add(points.get(j));
		    			pair.add(points.get(l));
		    			if ((edgeColor.get(pair).compareTo("purple") != 0) && (edgeColor.get(pair).compareTo("green") != 0) && (edgeColor.get(pair).compareTo("blue") != 0))
		    			{
		    				continue;
		    			}
	    				for (int k = j + 1; k < l; k++)
	    				{
	    					pair.clear();
			    			pair.add(points.get(i));
			    			pair.add(points.get(k));
							if ((edgeColor.get(pair).compareTo("purple") != 0) && (edgeColor.get(pair).compareTo("green") != 0) && (edgeColor.get(pair).compareTo("blue") != 0)) // unpierceable 
							{
								continue;
							}
        					for (int m = l + 1; m < points.size() -1; m++)
        					{
        						pair.clear();
    			    			pair.add(points.get(i));
    			    			pair.add(points.get(m));
    			    			if ((edgeColor.get(pair).compareTo("purple") != 0) && (edgeColor.get(pair).compareTo("green") != 0) && (edgeColor.get(pair).compareTo("blue") != 0))
    			    			{
    			    				continue;
    			    			}
        						for (int n = m + 1; n < points.size(); n++)
        						{
    			    				pair.clear();
        			    			pair.add(points.get(l));
        			    			pair.add(points.get(n));
        			    			if ((edgeColor.get(pair).compareTo("purple") == 0) || (edgeColor.get(pair).compareTo("green") == 0) || (edgeColor.get(pair).compareTo("blue") == 0))
        			    			{
        			    				pair.clear();
            			    			pair.add(points.get(i));
            			    			pair.add(points.get(l));
            			    			
            			    			if (edgeColor.get(pair).compareTo("orange") == 0) 
            			    			{
            			    				edgeColor.replace(pair, "purple");
            			    				updatedAnEdge = true;
            			    			}
            			    			if (edgeColor.get(pair).compareTo("cyan") == 0)  
            			    			{
            			    				edgeColor.replace(pair, "blue");
            			    				updatedAnEdge = true;
            			    			}
        							}
        						}
        					}
        				}
        			}
        		}
        	}
    		for (int i = 0; i < points.size() - 3; i++)
        	{
    			for (int k = i + 2; k < points.size() - 1; k++)
    			{
    				pair.clear();
        			pair.add(points.get(i));
	    			pair.add(points.get(k));
	    			if (edgeColor.get(pair).compareTo("green") != 0)
	    			{
	    				continue;
	    			}
    				for (int j = i + 1; j < k; j++)
    				{
        				for (int l = k + 1; l < points.size(); l++)
        				{
        					pair.clear();
                			pair.add(points.get(j));
        	    			pair.add(points.get(l));
        	    			if (edgeColor.get(pair).compareTo("green") == 0)
        	    			{
            					pair.clear();
                    			pair.add(points.get(i));
            	    			pair.add(points.get(j));
            	    			if (edgeColor.get(pair).compareTo("purple") == 0)
            	    			{
                					pair.clear();
                        			pair.add(points.get(k));
                	    			pair.add(points.get(l));
                	    			if (edgeColor.get(pair).compareTo("purple") == 0)
                	    			{
										// System.out.println("case A rejection (k, l): ");
										// System.out.println(pair);
                	    				return false;
                	    			}
            	    			}
            	    			
            	    			pair.clear();
                    			pair.add(points.get(i));
            	    			pair.add(points.get(l));
            	    			if (edgeColor.get(pair).compareTo("purple") == 0)
            	    			{
                					pair.clear();
                        			pair.add(points.get(j));
                	    			pair.add(points.get(k));
                	    			if (edgeColor.get(pair).compareTo("purple") == 0)
                	    			{
										// System.out.println("case B rejection (j, k): ");
										// System.out.println(pair);
                	    				return false;
                	    			}
            	    			}
            	    			
            	    			pair.clear();
                    			pair.add(points.get(i));
            	    			pair.add(points.get(j));
            	    			if (edgeColor.get(pair).compareTo("purple") == 0)
            	    			{
                					pair.clear();
                        			pair.add(points.get(k));
                	    			pair.add(points.get(l));
                	    			if (edgeColor.get(pair).compareTo("orange") == 0)
                	    			{
                	    				edgeColor.replace(pair, "red");
                	    				updatedAnEdge = true;
                	    			}
            	    			}
            	    			
            	    			pair.clear();
                    			pair.add(points.get(k));
            	    			pair.add(points.get(l));
            	    			if (edgeColor.get(pair).compareTo("purple") == 0)
            	    			{
                					pair.clear();
                        			pair.add(points.get(i));
                	    			pair.add(points.get(j));
                	    			if (edgeColor.get(pair).compareTo("orange") == 0)
                	    			{
                	    				edgeColor.replace(pair, "red");
                	    				updatedAnEdge = true;
                	    			}
            	    			}
                	    			
	        	    			pair.clear();
	                			pair.add(points.get(i));
	        	    			pair.add(points.get(l));
	        	    			if (edgeColor.get(pair).compareTo("purple") == 0)
	        	    			{
	            					pair.clear();
	                    			pair.add(points.get(j));
	            	    			pair.add(points.get(k));
	            	    			if (edgeColor.get(pair).compareTo("orange") == 0)
	            	    			{
	            	    				edgeColor.replace(pair, "red");
	            	    				updatedAnEdge = true;
	            	    			}
	        	    			}
	        	    			
	        	    			pair.clear();
	                			pair.add(points.get(j));
	        	    			pair.add(points.get(k));
	        	    			if (edgeColor.get(pair).compareTo("purple") == 0)
	        	    			{
	            					pair.clear();
	                    			pair.add(points.get(i));
	            	    			pair.add(points.get(l));
	            	    			if (edgeColor.get(pair).compareTo("orange") == 0)
	            	    			{
	            	    				edgeColor.replace(pair, "red");
	            	    				updatedAnEdge = true;
	            	    			}
	        	    			}
	        	    			
	        	    			pair.clear();
	                			pair.add(points.get(i));
	        	    			pair.add(points.get(j));
	        	    			if (edgeColor.get(pair).compareTo("purple") == 0)
	        	    			{
	            					pair.clear();
	                    			pair.add(points.get(k));
	            	    			pair.add(points.get(l));
	            	    			if (edgeColor.get(pair).compareTo("blue") == 0)
	            	    			{
	            	    				edgeColor.replace(pair, "green");
	            	    				updatedAnEdge = true;
	            	    			}
	        	    			}
	        	    			
	        	    			pair.clear();
	                			pair.add(points.get(k));
	        	    			pair.add(points.get(l));
	        	    			if (edgeColor.get(pair).compareTo("purple") == 0)
	        	    			{
	            					pair.clear();
	                    			pair.add(points.get(i));
	            	    			pair.add(points.get(j));
	            	    			if (edgeColor.get(pair).compareTo("blue") == 0)
	            	    			{
	            	    				edgeColor.replace(pair, "green");
	            	    				updatedAnEdge = true;
	            	    			}
	        	    			}
	        	    			
	        	    			pair.clear();
	                			pair.add(points.get(i));
	        	    			pair.add(points.get(l));
	        	    			if (edgeColor.get(pair).compareTo("purple") == 0)
	        	    			{
	            					pair.clear();
	                    			pair.add(points.get(j));
	            	    			pair.add(points.get(k));
	            	    			if (edgeColor.get(pair).compareTo("blue") == 0)
	            	    			{
	            	    				edgeColor.replace(pair, "green");
	            	    				updatedAnEdge = true;
	            	    			}
	        	    			}
	        	    			
	        	    			pair.clear();
	                			pair.add(points.get(j));
	        	    			pair.add(points.get(k));
	        	    			if (edgeColor.get(pair).compareTo("purple") == 0)
	        	    			{
	            					pair.clear();
	                    			pair.add(points.get(i));
	            	    			pair.add(points.get(l));
	            	    			if (edgeColor.get(pair).compareTo("blue") == 0)
	            	    			{
	            	    				edgeColor.replace(pair, "green");
	            	    				updatedAnEdge = true;
	            	    			}
            	    			}
        	    			}
        				}
        			}
        		}
        	}
    	} while(updatedAnEdge);

    	return true;
	}
}
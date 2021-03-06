
/*
A client that can be run from anywhere. If the server is running, the following command starts up the client:
java MyClient numThreads
numThreads is an optional number of threads you want the current machine to use, if not specified, program will use as many threads as there are cores on your machine.
*/

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MyClientLocal {

	public static boolean moreThreads;
	public static ConcurrentHashMap<String, HashMap<ArrayList<String>, String>> allTheMaps = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Stack<ArrayList<String>>> gapsThatWork = new ConcurrentHashMap<>();

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
  
  public static ArrayList<String> thereIsAPointWithNoGaps(ArrayList<String> ordering, ArrayList<String> usableViewpoints, HashMap<ArrayList<String>, String> edgeColor, String nextCase)
  {
	  String pointWith1Gap = "";
	  for(int i=0; i<usableViewpoints.size(); i++){
		  
		  String thisPoint = usableViewpoints.get(i);
		  ArrayList<String> gapsForThisPoint = gapsThatWork.get(thisPoint + "-" + nextCase).peek();
		  
		  if(gapsForThisPoint.size() == 0)
			  return null;
		  else if(gapsForThisPoint.size() == 1 && pointWith1Gap.equals("")){
			  pointWith1Gap = thisPoint;
		  }
		  
	  }
	  
	  if(!pointWith1Gap.equals("")){
		  String thisPoint = pointWith1Gap;
		  ArrayList<String> gapsForThisPoint = gapsThatWork.get(thisPoint + "-" + nextCase).peek();
		  System.out.println(thisPoint + " has only 1 gap.");
			String theGapString = gapsForThisPoint.get(0);
			String rightEndOfGap = theGapString.substring(theGapString.indexOf(':')+1);
			int theGap = 0;
			while(!rightEndOfGap.equals(ordering.get(theGap))){
				theGap++;
			}
			ordering.add(theGap, thisPoint);
			
			HashMap<ArrayList<String>, String> copyOfEdgeColors = new HashMap<ArrayList<String>, String>(edgeColor);
			
			for(int k=0; k<ordering.size(); k++)
			{
				if(k==theGap)
					continue;
				
				ArrayList<String> pair = new ArrayList<String>();
				computePair(k,theGap,pair,ordering);
				String otherPoint = ordering.get(k);
				
				if(otherPoint.length() > 1){
				
					//VP-VP edge
					copyOfEdgeColors.put(pair,"cyan");
				}
				else if(thisPoint.indexOf(otherPoint) == -1){
					
					//VP-Guard that don't see each other
					copyOfEdgeColors.put(pair,"orange");				
				}
				else{
					//VP-Guard that see each other
					copyOfEdgeColors.put(pair,"green");
				}
			}
			
			isFeasibleOrdering(ordering,copyOfEdgeColors,true,nextCase);
			return ordering;
			  
		  }
	  
	  
	  return ordering;
  }
  
  /*public static ArrayList<String> thereIsAPointWithNoGaps(ArrayList<String> ordering, ArrayList<String> usableViewpoints, HashMap<ArrayList<String>, String> edgeColor, String nextCase)
  {
	  for(int i=0; i<usableViewpoints.size(); i++){
		  
		  boolean foundAGap = false, foundASecondGap = false;
		  int theGap = -1;
		  String thisPoint = usableViewpoints.get(i);
		  
		  //System.out.println("Current point: " + thisPoint + ", current order length: " + ordering.size());
		  
		  for (int j = 0; j < ordering.size(); ++j)
			{
				
				HashMap<ArrayList<String>, String> copyOfEdgeColors = new HashMap<ArrayList<String>, String>(edgeColor);
				
				
				
				ArrayList<String> newOrdering = new ArrayList<String>();
				newOrdering.clear();
				newOrdering.addAll(ordering);
				newOrdering.add(j, usableViewpoints.get(i));
				
				for(int k=0; k<newOrdering.size(); k++)
				{
					if(k==j)
						continue;
					
					ArrayList<String> pair = new ArrayList<String>();
					computePair(k,j,pair,newOrdering);
					String otherPoint = newOrdering.get(k);
					
					if(otherPoint.length() > 1){
					
						//VP-VP edge
						copyOfEdgeColors.put(pair,"cyan");
					}
					else if(thisPoint.indexOf(otherPoint) == -1){
						
						//VP-Guard that don't see each other
						copyOfEdgeColors.put(pair,"orange");				
					}
					else{
						//VP-Guard that see each other
						copyOfEdgeColors.put(pair,"green");
					}
				}
				
                
				if (isFeasibleOrdering(newOrdering,copyOfEdgeColors,false,nextCase))
				{
					if(!foundAGap){
						foundAGap = true;
						theGap = j;
					}
					else {
						foundASecondGap = true;
						break;
					}
				}
				
				
			}
			
			if(!foundAGap)
			{
				//useableViewpoint[i] had no gap.
				System.out.println(thisPoint + " has no gaps.");
				return null;
				
			}
			else if(!foundASecondGap){
				System.out.println(thisPoint + " has only 1 gap.");
				ordering.add(theGap, thisPoint);
				
				HashMap<ArrayList<String>, String> copyOfEdgeColors = new HashMap<ArrayList<String>, String>(edgeColor);
				
				for(int k=0; k<ordering.size(); k++)
				{
					if(k==theGap)
						continue;
					
					ArrayList<String> pair = new ArrayList<String>();
					computePair(k,theGap,pair,ordering);
					String otherPoint = ordering.get(k);
					
					if(otherPoint.length() > 1){
					
						//VP-VP edge
						copyOfEdgeColors.put(pair,"cyan");
					}
					else if(thisPoint.indexOf(otherPoint) == -1){
						
						//VP-Guard that don't see each other
						copyOfEdgeColors.put(pair,"orange");				
					}
					else{
						//VP-Guard that see each other
						copyOfEdgeColors.put(pair,"green");
					}
				}
				
				isFeasibleOrdering(ordering,copyOfEdgeColors,true,nextCase);
				String key = "edgeColor" + ordering.size() +"-" + nextCase;
				edgeColor = allTheMaps.get(key);
			}
		  
	  }
	  
	  
	  return ordering;
	  
  }*/
  
  public static ArrayList<String> updateGapsForAllPoints(ArrayList<String> ordering, ArrayList<String> usableViewpoints, HashMap<ArrayList<String>, String> edgeColor, String nextCase, ArrayList<String> importantPoints){
	
		String key = "ABC-" + nextCase;
		if(!gapsThatWork.containsKey(key)){
			return initializeGapsThatWork(ordering, edgeColor, nextCase, importantPoints);			
		}
		/*for(int i=0; i<usableViewpoints.size(); i++){
			String thisPoint = usableViewpoints.get(i);
			System.out.println("Stack size for " + thisPoint + ": " + gapsThatWork.get(thisPoint+"-"+nextCase).size());
		}*/
		int startingStackSize = gapsThatWork.get(key).size();
		int cefhiSize = gapsThatWork.get("CEFHI-"+nextCase).size();
		System.out.println("Starting stack size: " + startingStackSize + ", cefhi: " + cefhiSize);
		int bigThreshold = 6;
		
		Boolean addedPointToOrder = true;
		while(addedPointToOrder){
			addedPointToOrder = false;
			
			for(int i=0; i<usableViewpoints.size(); i++){
				
				String thisPoint = usableViewpoints.get(i);
				Stack<ArrayList<String>> stackForThisPoint = gapsThatWork.get(thisPoint+"-"+nextCase);
				ArrayList<String> gapsThatWorkedBefore;
				if(stackForThisPoint.size() > startingStackSize)
					gapsThatWorkedBefore = stackForThisPoint.pop();
				else
					gapsThatWorkedBefore = stackForThisPoint.peek();
				
				
				
				
				ArrayList<String> updatedGaps = new ArrayList<String>();
				
				if(gapsThatWorkedBefore.get(0).equals("Big")){
					updatedGaps.add("Big");
					updatedGaps.add("Brah");
					gapsThatWork.get(thisPoint+"-"+nextCase).push(updatedGaps);
					continue;
				}
				
				int j = -1;
				int oldGapIndex = 0;
				String currentLeftPoint = "-infty";
				
				lookingForGapsLoop:
				while(j<ordering.size()-1 && oldGapIndex < gapsThatWorkedBefore.size()){
					
					String oldGap = gapsThatWorkedBefore.get(oldGapIndex);
					String oldGapLeftPoint = oldGap.substring(0,oldGap.indexOf(':'));
					String oldGapRightPoint = oldGap.substring(oldGap.indexOf(':')+1);
					
					//System.out.println("Checking " + oldGap + " from " + oldGapLeftPoint + " to " + oldGapRightPoint);
					
					while(!currentLeftPoint.equals(oldGapLeftPoint)){
						j++;
						currentLeftPoint = ordering.get(j);
					}
					
					String currentRightPoint;
					do{
						try{
							currentRightPoint = ordering.get(j+1);
						}
						catch(Exception e){
							System.out.println("CRASHING BRAH");
							System.out.println("Checking " + oldGap);
							for(int ii=0; ii<usableViewpoints.size(); ii++){
								String ithisPoint = usableViewpoints.get(ii);
								System.out.println("Stack size for " + ithisPoint + ": " + gapsThatWork.get(ithisPoint+"-"+nextCase).size());
							}
							System.exit(0);
						}
						
						currentRightPoint = ordering.get(j+1);
						
						HashMap<ArrayList<String>, String> copyOfEdgeColors = new HashMap<ArrayList<String>, String>(edgeColor);
					
						ArrayList<String> newOrdering = new ArrayList<String>();
						newOrdering.clear();
						newOrdering.addAll(ordering);
						newOrdering.add(j+1, usableViewpoints.get(i));
						
						for(int k=0; k<newOrdering.size(); k++)
						{
							if(k==j+1)
								continue;
							
							ArrayList<String> pair = new ArrayList<String>();
							computePair(k,j+1,pair,newOrdering);
							String otherPoint = newOrdering.get(k);
							
							if(otherPoint.length() > 1){
							
								//VP-VP edge
								copyOfEdgeColors.put(pair,"cyan");
							}
							else if(thisPoint.indexOf(otherPoint) == -1){
								
								//VP-Guard that don't see each other
								copyOfEdgeColors.put(pair,"orange");				
							}
							else{
								//VP-Guard that see each other
								copyOfEdgeColors.put(pair,"green");
							}
						}
						
						
						if (isFeasibleOrdering(newOrdering,copyOfEdgeColors,false,nextCase))
						{
							updatedGaps.add(currentLeftPoint+":"+currentRightPoint);
							
							if(updatedGaps.size() > bigThreshold && !importantPoints.contains(thisPoint))
								break lookingForGapsLoop;
						}
						
						currentLeftPoint = currentRightPoint;
						j++;
						
					}while(!currentRightPoint.equals(oldGapRightPoint));
					
					oldGapIndex++;
					
				}
				//We have the list of updated gaps for this point.
				
				if(updatedGaps.size() == 0){
					System.out.println("No gaps for " + thisPoint + ".  Rejecting.");
					for(int z =0; z<usableViewpoints.size(); z++){
						String prevPoint = usableViewpoints.get(z);
						String hashKey = prevPoint + "-" + nextCase;
						Stack<ArrayList<String>> theStack = gapsThatWork.get(hashKey);
						
						if(prevPoint.equals("CEFHI"))
							System.out.println("CEFHI stack size before pop: " + theStack.size() + ", starting size: " + startingStackSize);
						
						if(theStack.size() > startingStackSize)
							theStack.pop();
						
						if(prevPoint.equals("CEFHI"))
							System.out.println("CEFHI stack size after pop: " + theStack.size());
					}
					
					return null;  //null means we are rejecting.
				}
				
				if(updatedGaps.size() == 1){
					System.out.println(thisPoint + " has only one gap.  Adding it.");
					
					addedPointToOrder = true;
					String theGapString = updatedGaps.get(0);
					String rightEndOfGap = theGapString.substring(theGapString.indexOf(':')+1);
					int theGap = 0;
					while(!rightEndOfGap.equals(ordering.get(theGap))){
						theGap++;
					}
					ordering.add(theGap, thisPoint);
					
					HashMap<ArrayList<String>, String> copyOfEdgeColors = new HashMap<ArrayList<String>, String>(edgeColor);
					
					for(int k=0; k<ordering.size(); k++)
					{
						if(k==theGap)
							continue;
						
						ArrayList<String> pair = new ArrayList<String>();
						computePair(k,theGap,pair,ordering);
						String otherPoint = ordering.get(k);
						
						if(otherPoint.length() > 1){
						
							//VP-VP edge
							copyOfEdgeColors.put(pair,"cyan");
						}
						else if(thisPoint.indexOf(otherPoint) == -1){
							
							//VP-Guard that don't see each other
							copyOfEdgeColors.put(pair,"orange");				
						}
						else{
							//VP-Guard that see each other
							copyOfEdgeColors.put(pair,"green");
						}
					}
					
					isFeasibleOrdering(ordering,copyOfEdgeColors,true,nextCase);
					edgeColor = allTheMaps.get("edgeColor" + ordering.size() +"-" + nextCase);
					gapsThatWork.get(thisPoint+"-"+nextCase).push(updatedGaps);
				}
				else if(updatedGaps.size() > bigThreshold && !importantPoints.contains(thisPoint)){
					System.out.println("Here is a worthless freaking point: " + thisPoint);
					ArrayList<String> wat = new ArrayList<String>();
					wat.add("Big");
					wat.add("Bruh");
					gapsThatWork.get(thisPoint+"-"+nextCase).push(wat);
				}
				else{
					System.out.println("Updated gaps for " + thisPoint + ": " + updatedGaps);
					gapsThatWork.get(thisPoint+"-"+nextCase).push(updatedGaps);
				}
				
			}
			
			
		}
	  
	  return ordering;
  }
  
  public static void removeOldGaps(ArrayList<String> usableFromAllPoints, String nextCase){
	  
	  for(String thisPoint : usableFromAllPoints){
		  
		  gapsThatWork.get(thisPoint+"-"+nextCase).pop();
		  
	  }
  }

    public static String determineCase(ArrayList<String> ordering, String nextCase, String[] allPoints)
    {
		
		System.out.println("Placed " + ordering.size() + " points: " + ordering);
		
    	
		
		String[] everyFreakinPoint = {"HI", "GI", "GH", "GHI", "FI", "FH", "FHI", "FG", "FGI", "FGH", "FGHI", "EI", "EH", "EHI", "EG", "EGI", "EGH", "EGHI", "EF", "EFI", "EFH", "EFHI", "EFG", "EFGI", "EFGH", "EFGHI", "DI", "DH", "DHI", "DG", "DGI", "DGH", "DGHI", "DF", "DFI", "DFH", "DFHI", "DFG", "DFGI", "DFGH", "DFGHI", "DE", "DEI", "DEH", "DEHI", "DEG", "DEGI", "DEGH", "DEGHI", "DEF", "DEFI", "DEFH", "DEFHI", "DEFG", "DEFGI", "DEFGH", "DEFGHI", "CI", "CH", "CHI", "CG", "CGI", "CGH", "CGHI", "CF", "CFI", "CFH", "CFHI", "CFG", "CFGI", "CFGH", "CFGHI", "CE", "CEI", "CEH", "CEHI", "CEG", "CEGI", "CEGH", "CEGHI", "CEF", "CEFI", "CEFH", "CEFHI", "CEFG", "CEFGI", "CEFGH", "CEFGHI", "CD", "CDI", "CDH", "CDHI", "CDG", "CDGI", "CDGH", "CDGHI", "CDF", "CDFI", "CDFH", "CDFHI", "CDFG", "CDFGI", "CDFGH", "CDFGHI", "CDE", "CDEI", "CDEH", "CDEHI", "CDEG", "CDEGI", "CDEGH", "CDEGHI", "CDEF", "CDEFI", "CDEFH", "CDEFHI", "CDEFG", "CDEFGI", "CDEFGH", "CDEFGHI", "BI", "BH", "BHI", "BG", "BGI", "BGH", "BGHI", "BF", "BFI", "BFH", "BFHI", "BFG", "BFGI", "BFGH", "BFGHI", "BE", "BEI", "BEH", "BEHI", "BEG", "BEGI", "BEGH", "BEGHI", "BEF", "BEFI", "BEFH", "BEFHI", "BEFG", "BEFGI", "BEFGH", "BEFGHI", "BD", "BDI", "BDH", "BDHI", "BDG", "BDGI", "BDGH", "BDGHI", "BDF", "BDFI", "BDFH", "BDFHI", "BDFG", "BDFGI", "BDFGH", "BDFGHI", "BDE", "BDEI", "BDEH", "BDEHI", "BDEG", "BDEGI", "BDEGH", "BDEGHI", "BDEF", "BDEFI", "BDEFH", "BDEFHI", "BDEFG", "BDEFGI", "BDEFGH", "BDEFGHI", "BC", "BCI", "BCH", "BCHI", "BCG", "BCGI", "BCGH", "BCGHI", "BCF", "BCFI", "BCFH", "BCFHI", "BCFG", "BCFGI", "BCFGH", "BCFGHI", "BCE", "BCEI", "BCEH", "BCEHI", "BCEG", "BCEGI", "BCEGH", "BCEGHI", "BCEF", "BCEFI", "BCEFH", "BCEFHI", "BCEFG", "BCEFGI", "BCEFGH", "BCEFGHI", "BCD", "BCDI", "BCDH", "BCDHI", "BCDG", "BCDGI", "BCDGH", "BCDGHI", "BCDF", "BCDFI", "BCDFH", "BCDFHI", "BCDFG", "BCDFGI", "BCDFGH", "BCDFGHI", "BCDE", "BCDEI", "BCDEH", "BCDEHI", "BCDEG", "BCDEGI", "BCDEGH", "BCDEGHI", "BCDEF", "BCDEFI", "BCDEFH", "BCDEFHI", "BCDEFG", "BCDEFGI", "BCDEFGH", "BCDEFGHI", "AI", "AH", "AHI", "AG", "AGI", "AGH", "AGHI", "AF", "AFI", "AFH", "AFHI", "AFG", "AFGI", "AFGH", "AFGHI", "AE", "AEI", "AEH", "AEHI", "AEG", "AEGI", "AEGH", "AEGHI", "AEF", "AEFI", "AEFH", "AEFHI", "AEFG", "AEFGI", "AEFGH", "AEFGHI", "AD", "ADI", "ADH", "ADHI", "ADG", "ADGI", "ADGH", "ADGHI", "ADF", "ADFI", "ADFH", "ADFHI", "ADFG", "ADFGI", "ADFGH", "ADFGHI", "ADE", "ADEI", "ADEH", "ADEHI", "ADEG", "ADEGI", "ADEGH", "ADEGHI", "ADEF", "ADEFI", "ADEFH", "ADEFHI", "ADEFG", "ADEFGI", "ADEFGH", "ADEFGHI", "AC", "ACI", "ACH", "ACHI", "ACG", "ACGI", "ACGH", "ACGHI", "ACF", "ACFI", "ACFH", "ACFHI", "ACFG", "ACFGI", "ACFGH", "ACFGHI", "ACE", "ACEI", "ACEH", "ACEHI", "ACEG", "ACEGI", "ACEGH", "ACEGHI", "ACEF", "ACEFI", "ACEFH", "ACEFHI", "ACEFG", "ACEFGI", "ACEFGH", "ACEFGHI", "ACD", "ACDI", "ACDH", "ACDHI", "ACDG", "ACDGI", "ACDGH", "ACDGHI", "ACDF", "ACDFI", "ACDFH", "ACDFHI", "ACDFG", "ACDFGI", "ACDFGH", "ACDFGHI", "ACDE", "ACDEI", "ACDEH", "ACDEHI", "ACDEG", "ACDEGI", "ACDEGH", "ACDEGHI", "ACDEF", "ACDEFI", "ACDEFH", "ACDEFHI", "ACDEFG", "ACDEFGI", "ACDEFGH", "ACDEFGHI", "AB", "ABI", "ABH", "ABHI", "ABG", "ABGI", "ABGH", "ABGHI", "ABF", "ABFI", "ABFH", "ABFHI", "ABFG", "ABFGI", "ABFGH", "ABFGHI", "ABE", "ABEI", "ABEH", "ABEHI", "ABEG", "ABEGI", "ABEGH", "ABEGHI", "ABEF", "ABEFI", "ABEFH", "ABEFHI", "ABEFG", "ABEFGI", "ABEFGH", "ABEFGHI", "ABD", "ABDI", "ABDH", "ABDHI", "ABDG", "ABDGI", "ABDGH", "ABDGHI", "ABDF", "ABDFI", "ABDFH", "ABDFHI", "ABDFG", "ABDFGI", "ABDFGH", "ABDFGHI", "ABDE", "ABDEI", "ABDEH", "ABDEHI", "ABDEG", "ABDEGI", "ABDEGH", "ABDEGHI", "ABDEF", "ABDEFI", "ABDEFH", "ABDEFHI", "ABDEFG", "ABDEFGI", "ABDEFGH", "ABDEFGHI", "ABC", "ABCI", "ABCH", "ABCHI", "ABCG", "ABCGI", "ABCGH", "ABCGHI", "ABCF", "ABCFI", "ABCFH", "ABCFHI", "ABCFG", "ABCFGI", "ABCFGH", "ABCFGHI", "ABCE", "ABCEI", "ABCEH", "ABCEHI", "ABCEG", "ABCEGI", "ABCEGH", "ABCEGHI", "ABCEF", "ABCEFI", "ABCEFH", "ABCEFHI", "ABCEFG", "ABCEFGI", "ABCEFGH", "ABCEFGHI", "ABCD", "ABCDI", "ABCDH", "ABCDHI", "ABCDG", "ABCDGI", "ABCDGH", "ABCDGHI", "ABCDF", "ABCDFI", "ABCDFH", "ABCDFHI", "ABCDFG", "ABCDFGI", "ABCDFGH", "ABCDFGHI", "ABCDE", "ABCDEI", "ABCDEH", "ABCDEHI", "ABCDEG", "ABCDEGI", "ABCDEGH", "ABCDEGHI", "ABCDEF", "ABCDEFI", "ABCDEFH", "ABCDEFHI", "ABCDEFG", "ABCDEFGI", "ABCDEFGH", "ABCDEFGHI"};
		
		ArrayList<String> everyViewpoints = new ArrayList<String>();
		for (String s : everyFreakinPoint)
		{
			everyViewpoints.add(s);
		}
		ArrayList<String> usableViewpoints = new ArrayList<String>();
		usableViewpoints = availableViewpoints(ordering, everyViewpoints);
		
		
	
	//String[] allPoints = {"ABCDEGH","BCDEFHI","ACDEFGI","ABDEFGH","BCEFGHI","ACDFGHI","ABDEGHI","ABCEFHI", "ABCDFGI","ABDFH","BCEGI","ACDFH","BDEGI", "ACEFH","BDFGI","ACEGH","ABDEGH","BCEFHI","ACDFGI","ADG","BEH","CFI","ABEG","BCFH","CDGI","ADEH","BEFI","ACFG","BDGH","CEHI","ADFI","ABCDFH","BCDEGI","ACDEFH","BDEFGI","ACEFGH","BDFGHI","ACEGHI","ABDFHI","ABCEGI","ABCEH","BCDFI","ACDEG","BDEFH","CEFGI","ADFGH","BEGHI","ACFHI","ABDGI", "ABCFGH","BCDGHI","ACDEHI","ABDEFI","ABCEFG","BCDFGH","CDEGHI","ADEFHI","ABEFGI","CEGI","ADFH","BEGI","ACFH","BDGI","ACEH","BDFI","ADEG","BEFH","CFGI","ADGH","BEHI","ACFI","ABDG","BCEH","CDFI","ADFG","BEGH","CFHI","ADGI","ABEH","BCFI","ACDG","BDEH","CEFI" };
	
	//String[] allPoints = {"ABEH", "CFGI", "ACDG", "CDGI", "BEGH", "BCEGI", "ACDFH", "BDEGI", "BDFGHI", "BDGI", "ACFI", "ADFG", "BCDEFHI", "ABCEGI", "BCDEGI", "ABDEGH", "BCDGHI", "ACEFH", "CFI", "ADEH", "ACDFGHI", "BDFI", "CFHI", "BEFH", "ACDEFH", "BCFI", "ACDFGI", "BCEFHI", "ABCDFGI", "BEFI", "ABCFGH", "ADG", "ACEFGH", "ABDFHI", "ABDFH", "BCDFI", "ACDEG", "ACDEHI", "ACDEFGI", "BEHI", "ADGI", "BEGI", "ACFG", "ABDEGHI", "ABDEFGH", "ADGH", "CEGI", "BDEFGI", "BDEH", "BEH", "CEFGI", "ACEGH", "ABCEH", "ABDEFI", "ADEG", "CDEGHI", "ABEFGI", "CDFI", "BDFGI", "ABCEFG", "ABDGI", "ABEG", "ADFI", "ACEH", "ADFGH", "ABCDFH", "BCFH", "BCEFGHI", "CEFI", "BEGHI", "BCDFGH", "ADFH", "BDGH", "ACEGHI", "BCEH", "ACFH", "ABDG", "BDEFH", "CEHI", "ABCEFHI", "ABCDEGH", "ADEFHI", "ACFHI" };


		
		
		HashMap<ArrayList<String>, String> edgeColor = null;
		try
        {   
           /* // Reading the object from a file
            //FileInputStream file = new FileInputStream("edgeColor" + ordering.size() +"-" + nextCase + ".ser");
           // ObjectInputStream in = new ObjectInputStream(file);
              
            // Method for deserialization of object
            edgeColor = (HashMap<ArrayList<String>, String>)in.readObject();
              
            in.close();
            file.close();*/
			
			String key = "edgeColor" + ordering.size() +"-" + nextCase;
			edgeColor = allTheMaps.get(key);
			
        }          
        catch(Exception e)
        {
            e.printStackTrace();
        }
		
		ArrayList<String> importantPoints = new ArrayList<String>();
		//for(int i =0; i< allPoints.length; i++)
		//	importantPoints.add(allPoints[i]);
		
		//if(updateGapsForAllPoints(ordering, usableViewpoints, edgeColor, nextCase, importantPoints))
		//{
			//There was a point with no gaps, so we are rejcting.  We already removed the gaps for this subproblem.
		//	String key = "edgeColor" + currentOrderingSize +"-" + nextCase;
		//	allTheMaps.remove(key);
		//	return "";
		//}
		
		int currentOrderingSize = ordering.size();
		//ordering = thereIsAPointWithNoGaps(ordering, usableViewpoints, edgeColor, nextCase);
		ordering = updateGapsForAllPoints(ordering, usableViewpoints, edgeColor, nextCase, importantPoints);
		if(ordering == null){
			//File myObj = new File("edgeColor" + ordering.size() +"-" + nextCase + ".ser");
			//myObj.delete();
			String key = "edgeColor" + currentOrderingSize +"-" + nextCase;
			while(allTheMaps.containsKey(key)){
				allTheMaps.remove(key);
				currentOrderingSize++;
				key = "edgeColor" + currentOrderingSize +"-" + nextCase;
			}
			//removeOldGaps(usableViewpoints,nextCase);  <-- We updated the function to remove stuff for us.
			return "";
		}
		
		int newOrderingSize = ordering.size();
		if(currentOrderingSize != newOrderingSize){
			
			/*String result = determineCase(ordering, nextCase, allPoints);
			for(int sizeToRemove = currentOrderingSize; sizeToRemove <= newOrderingSize; sizeToRemove++){
				String key = "edgeColor" + sizeToRemove +"-" + nextCase;
				allTheMaps.remove(key);
			}
			removeOldGaps(usableViewpoints,nextCase);
			return result;*/
			edgeColor = allTheMaps.get("edgeColor" + newOrderingSize +"-" + nextCase);
		}
		
		ArrayList<String> usableFromAllPoints = usableViewpoints;
		
		/*everyViewpoints = new ArrayList<String>();
		usableViewpoints = new ArrayList<String>();
		
		for (String s : allPoints)
		{
			everyViewpoints.add(s);
		}
		usableViewpoints = availableViewpoints(ordering, everyViewpoints);
		//int viewPointsToCheck = 40;
		if (usableViewpoints.size() <= 0)//allPoints.length - viewPointsToCheck)
		{
			System.out.println(ordering);//" + viewPointsToCheck + " VPs.");//everything. ");
			removeOldGaps(usableFromAllPoints,nextCase);
			return ordering.toString();
		}*/
		
		
		//int optimalViewpointIndex = -1;
		//int minGaps = 1040;
		
		// determine which viewpoint is the point we are going to recurse
       // ArrayList<Integer> optimalGaps = new ArrayList<Integer>();
	   int minIndex = 0;
	   ArrayList<String> gaps = gapsThatWork.get(usableViewpoints.get(0)+"-"+nextCase).peek();
	   int min=999;
		if(gaps.get(0).equals("Big"))
			min = ordering.size();
		else
			min = gaps.size();
		
	   for(int i=1; i<usableViewpoints.size(); i++){
		   
		   gaps = gapsThatWork.get(usableViewpoints.get(i)+"-"+nextCase).peek();
		   int len=999;
			if(gaps.get(0).equals("Big"))
				len = ordering.size();
			else
				len = gaps.size();
			
		   if(len < min){
			   min = len;
			   minIndex = i;
		   }
	   }
	   
	   if (min == ordering.size())//allPoints.length - viewPointsToCheck)
		{
			System.out.println(ordering);//" + viewPointsToCheck + " VPs.");//everything. ");
			removeOldGaps(usableFromAllPoints,nextCase);
			return ordering.toString();
		}
        
		for (int i = 0; i < 1; i++)//usableViewpoints.size(); ++i)
		{
			// insert usableViewpoints[i] into our current ordering in every gap
			// int feasibleGaps = 0;
            
            //ArrayList<Integer> storedGaps = new ArrayList<Integer>();
            //int numFeasibleGaps = 0;
            // storedGaps.clear();
			String thisPoint = usableViewpoints.get(minIndex);
			ArrayList<String> gapsForThisPoint = gapsThatWork.get(thisPoint+"-"+nextCase).peek();
			int j = 0;
			for (int z = 0; z < gapsForThisPoint.size(); ++z)
			{
				String thisGap = gapsForThisPoint.get(z);
				String rightPointOfGap = thisGap.substring(thisGap.indexOf(':')+1);
				System.out.println("Processing gap: " + thisGap + ", looking for " + rightPointOfGap);
				while(!rightPointOfGap.equals(ordering.get(j))){
					j++;
				}
				
				HashMap<ArrayList<String>, String> copyOfEdgeColors = new HashMap<ArrayList<String>, String>(edgeColor);
				
				
				
				ArrayList<String> newOrdering = new ArrayList<String>();
				newOrdering.clear();
				newOrdering.addAll(ordering);
				newOrdering.add(j, thisPoint);
				
				for(int k=0; k<newOrdering.size(); k++)
				{
					if(k==j)
						continue;
					
					ArrayList<String> pair = new ArrayList<String>();
					computePair(k,j,pair,newOrdering);
					String otherPoint = newOrdering.get(k);
					
					if(otherPoint.length() > 1){
					
						//VP-VP edge
						copyOfEdgeColors.put(pair,"cyan");
					}
					else if(thisPoint.indexOf(otherPoint) == -1){
						
						//VP-Guard that don't see each other
						copyOfEdgeColors.put(pair,"orange");				
					}
					else{
						//VP-Guard that see each other
						copyOfEdgeColors.put(pair,"green");
					}
				}
				
                
				if (isFeasibleOrdering(newOrdering,copyOfEdgeColors,true,nextCase))
				{
					String result = determineCase(newOrdering, nextCase, allPoints);
					/*if (!result.equals(""))
					{
						//File myObj = new File("edgeColor" + ordering.size() +"-" + nextCase + ".ser");
						//myObj.delete();
						String key = "edgeColor" + ordering.size() +"-" + nextCase;
						allTheMaps.remove(key);
						return result;
					}*/
					/*storedGaps.add(j);
				  numFeasibleGaps++;

				  if(numFeasibleGaps >+ minGaps){
					//This already cannot be the optimal point so skip it.
					break;
				  }*/
				}
			}
            
			/*if (storedGaps.size() < minGaps)
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
		    }*/
		}
        /*for (int i = 0; i < optimalGaps.size(); ++i)
        {
            ArrayList<String> recurseOrdering = new ArrayList<String>();
            recurseOrdering.addAll(ordering);
            recurseOrdering.add(optimalGaps.get(i), usableViewpoints.get(optimalViewpointIndex));
            String result = determineCase(recurseOrdering);
            if (!result.equals(""))
            {
                return result;
            }
        }*/
		//File myObj = new File("edgeColor" + ordering.size() +"-" + nextCase + ".ser");
		//myObj.delete();
		for(int sizeToRemove = currentOrderingSize; sizeToRemove <= newOrderingSize; sizeToRemove++){
			String key = "edgeColor" + sizeToRemove +"-" + nextCase;
			allTheMaps.remove(key);
		}
		removeOldGaps(usableFromAllPoints,nextCase);
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


  
   public static boolean isFeasibleOrdering(ArrayList<String> points, HashMap<ArrayList<String>, String> edgeColor,  Boolean writeEdgeColor, String nextCase) {
    // System.out.println(points);
    // Edge colors:
    // Green - See each other - unpierceable and close
    // Red - Do not see other - close - must be pierced
    // Purple - Do not see each other - cannot be pierced - too far
    // Orange - Do not see each other - don't care how we block.
    // Blue - Don't care if see each other - cannot be pierced.
    // Cyan - Don't care if see each other - don't care if pierced.
	// Yellow - Don't care if they see each other - cannot be too far away.

    //HashMap<ArrayList<String>, String> edgeColor = new HashMap<ArrayList<String>, String>();
	HashMap<ArrayList<String>, String> edgeColorCopy = new HashMap<ArrayList<String>, String>();
	HashMap<ArrayList<String>, String> coloringWeWouldHaveAccepted = new HashMap<ArrayList<String>, String>();
	boolean inFlippingPhase = false;
	ArrayList<String> flippedEdge = null;
	
	
	//-10 means they cannot be blocked
	//-1 means they can be blocked but no specific blocker.
	//>=0 means they can only be blocked by the point with the given index.
	//HashMap<ArrayList<String>, Integer> canBlockBelow = new HashMap<ArrayList<String>, Integer> ();
	//HashMap<ArrayList<String>, Integer> canBlockAbove = new HashMap<ArrayList<String>, Integer> ();
	

	if(edgeColor == null)
	{
		edgeColor = new HashMap<ArrayList<String>, String>();
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
				//canBlockBelow.put(temp,-1);
				//canBlockAbove.put(temp,-1);
			  } else {
				if (points.get(j).indexOf(points.get(i)) == -1) {
				  edgeColor.put(temp, "orange");
				  //canBlockBelow.put(temp,-1);
				  //canBlockAbove.put(temp,-1);
				} else {
				  edgeColor.put(temp, "green");
				  //canBlockBelow.put(temp,-10);
				  //canBlockAbove.put(temp,-10);
				}
			  }
			} else {
			  if (points.get(j).length() > 1) {
				edgeColor.put(temp, "cyan");
				//canBlockBelow.put(temp,-1);
				//canBlockAbove.put(temp,-1);
			  } else {
				if (points.get(i).indexOf(points.get(j)) == -1) {
				  edgeColor.put(temp, "orange");
				  //canBlockBelow.put(temp,-1);
				  //canBlockAbove.put(temp,-1);
				} else {
				  edgeColor.put(temp, "green");
				  //canBlockBelow.put(temp,-10);
				  //canBlockAbove.put(temp,-10);
				}
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
		  String ilColor = edgeColor.get(pair);
		  
		  if(ilColor == null){
			  
			  System.out.println("Edge we were looking for: " + pair);
			  System.out.println("Current order: ");
			  System.out.println(points);
			  System.exit(0);
		  }
		  
		  //System.out.println(pair);
		  if(ilColor.equalsIgnoreCase("blue") || ilColor.equalsIgnoreCase("purple") || ilColor.equalsIgnoreCase("green"))
			  continue;
		  
		  //if(points.get(i).equals("ACDFH") && points.get(l).equals("G")) System.out.println("Checking ACDFH and G.");
		  
		  boolean canBlockAbove, canBlockBelow;
		  canBlockAbove = canBlockBelow = true;
		  //if(canBlockAbove.get(pair) < -1 && canBlockBelow.get(pair) < -1)
		//	continue;
			
		  //int belowBlocker = -1;
		  //if(canBlockBelow.get(pair) >= -1){
			  
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
				//if(points.get(i).equals("ACDFH") && points.get(l).equals("G")) System.out.println("Can't block below");
				canBlockBelow = false;
			}
			else{
				//if(points.get(i).equals("ACDFH") && points.get(l).equals("G")) System.out.println("Can block below");
				continue;
			}
						  
			  
		  //}
		  
		  //pair.clear();
		  //computePair(i, l, pair, points);
		  //if(canBlockAbove.get(pair) >= -1){
			  
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
			
			
			if(n!=m && n != stoppingIndexForN){
				//if(points.get(i).equals("ACDFH") && points.get(l).equals("G")) System.out.println("Can't block above");
				canBlockAbove = false;
			}
			
									  
			  
		  //}
		  
		  if(!canBlockAbove && !canBlockBelow){
			  
			 //if(points.get(i).equals("ACDFH") && points.get(l).equals("G")) System.out.println("Can't block either side");
			  //Last time we could block on one side, but now we cannot block on either side.
			 
				pair.clear();
				computePair(i, l, pair, points);
				//pair.add(points.get(i));
				//pair.add(points.get(l));

				if (ilColor.toLowerCase().compareTo("orange") == 0) {
				  edgeColor.replace(pair, "purple");
				  updatedAnEdge = true;
				}
				else if (ilColor.toLowerCase().compareTo("cyan") == 0) {
				  edgeColor.replace(pair, "blue");
				  updatedAnEdge = true;
				}
				else if(ilColor.toLowerCase().compareTo("yellow") == 0) {
				  edgeColor.replace(pair, "green");
				  updatedAnEdge = true;
				}
				else if(ilColor.toLowerCase().compareTo("red") == 0) {
					if(flippedEdge == null){
						//System.out.println("Rejecting at line 509");
						return false;
					}
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
			  
			  if(!jlColor.equalsIgnoreCase("green") && !jlColor.equalsIgnoreCase("blue"))
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
				  
				  int pointInDiscK = -1;
					  
				  for(int p = (l+1)%points.size(); p != i; p = (p+1)%points.size()){
					  
						pair.clear();
						computePair(k, p, pair, points);
						String kpColor = edgeColor.get(pair);
						
						if(mustBeClose(kpColor)){
							pointInDiscK = p;
							break;
						}
					}
					
					if(pointInDiscI >= 0 && pointInDiscK >= 0){
						
						if(cannotBlock(ilColor) && cannotBlock(jkColor)){
							
							if(jlColor.equalsIgnoreCase("green")){
								
								if(flippedEdge == null){
										//System.out.println("REJECTION!!!");
										return false;
										
								}
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
							else{
								
								
								pair.clear();
								computePair(j, l, pair, points);
								edgeColor.replace(pair,"purple");
								updatedAnEdge = true;
								
							}
						}
						else if(cannotBlock(ilColor)){
							
							pair.clear();
							computePair(k, j, pair, points);
							edgeColor.replace(pair,"red");
							updatedAnEdge = true;
						}
						else{
						
							pair.clear();
							computePair(i, l, pair, points);
							edgeColor.replace(pair,"red");
							updatedAnEdge = true;
							
						}
						
					}
					else if(pointInDiscI >= 0 && cannotBlock(jkColor) && cannotBlock(ilColor)){
						
						//Every point between l and i must bee too far from k.
						/*System.out.println("Every point between l and i must be too far from k.");
						System.out.println("i = " + points.get(i));
						System.out.println("j = " + points.get(j));
						System.out.println("k = " + points.get(k));
						System.out.println("l = " + points.get(l));	*/

						for(int p = (l+1)%points.size(); p != i; p = (p+1)%points.size()){
					  
							pair.clear();
							computePair(k, p, pair, points);
							String kpColor = edgeColor.get(pair);
							
							if(!kpColor.equalsIgnoreCase("purple")){
								edgeColor.replace(pair,"purple");
								updatedAnEdge = true;
								//System.out.println("Flipped " + points.get(k) + "-" + points.get(p) + " edge to purple.");
							}
						}						
						
					}
					else if(pointInDiscK >= 0 && cannotBlock(jkColor) && cannotBlock(ilColor)){
						/*System.out.println("Every point between j and k must be too far from i.");
						System.out.println("i = " + points.get(i));
						System.out.println("j = " + points.get(j));
						System.out.println("k = " + points.get(k));
						System.out.println("l = " + points.get(l));	*/
						
						for(int p = j+1; p<k; p++){
						  pair.clear();
						  computePair(i, p, pair, points);
						  String ipColor = edgeColor.get(pair);
						  
						  if(!ipColor.equalsIgnoreCase("purple")){
							  edgeColor.replace(pair,"purple");
							  updatedAnEdge = true;
							  //System.out.println("Flipped " + points.get(i) + "-" + points.get(p) + " edge to purple.");
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
				  
				  
					int pointInDiscK = -1;
				  for(int p = i+1; p < j; p++){
					  
						pair.clear();
						computePair(k, p, pair, points);
						String kpColor = edgeColor.get(pair);
						
						if(mustBeClose(kpColor)){
							pointInDiscK = p;
							
							break;
							
						}
					  
				  }
				  
				  if(pointInDiscI >= 0 && pointInDiscK >=0){
					  
					if(cannotBlock(ijColor) && cannotBlock(klColor)){
							
						if(jlColor.equalsIgnoreCase("green")){
								//System.out.println("REJECTION!!!");
								if(flippedEdge == null){
										//System.out.println("REJECTION!!!");
										return false;
										//return null;
								}
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
							else{
								
								pair.clear();
								computePair(j, l, pair, points);
								edgeColor.replace(pair,"purple");
								updatedAnEdge = true;
								
							}
					}
					else if(cannotBlock(ijColor)){
						
			
						pair.clear();
						computePair(k, l, pair, points);
						edgeColor.replace(pair,"red");
						updatedAnEdge = true;
					}
					else{
						
						
						pair.clear();
						computePair(i, j, pair, points);
						edgeColor.replace(pair,"red");
						updatedAnEdge = true;
					}
					
				  }
				  else if(cannotBlock(ijColor) && cannotBlock(klColor) && pointInDiscI >= 0){
					  
					  /*System.out.println("Every point between i and j must be too far from k.");
					  System.out.println("i = " + points.get(i));
					  System.out.println("j = " + points.get(j));
					  System.out.println("k = " + points.get(k));
					  System.out.println("l = " + points.get(l));*/
					  
					  for(int p = i+1; p < j; p++){
					  
						pair.clear();
						computePair(k, p, pair, points);
						String kpColor = edgeColor.get(pair);
						
						if(!kpColor.equalsIgnoreCase("purple")){
							edgeColor.replace(pair,"purple");
							updatedAnEdge = true;
							//System.out.println("Flipped " + points.get(k) + "-" + points.get(p) + " edge to purple.");
							
						}
					  
					}
					  
					  
				  }
				  else if(cannotBlock(ijColor) && cannotBlock(klColor) && pointInDiscK >= 0){
					  /*System.out.println("Every point between k and l must be too far from i.");
					  System.out.println("i = " + points.get(i));
					  System.out.println("j = " + points.get(j));
					  System.out.println("k = " + points.get(k));
					  System.out.println("l = " + points.get(l));*/
					  
					  for(int p = (k+1)%points.size(); p!=l; p = (p+1)%points.size()){
						  pair.clear();
						  computePair(i, p, pair, points);
						  String ipColor = edgeColor.get(pair);
						  
						  if(!ipColor.equalsIgnoreCase("purple")){
							  edgeColor.replace(pair,"purple");
							  //System.out.println("Flipped " + points.get(i) + "-" + points.get(p) + " edge to purple.");
							  updatedAnEdge = true;
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
	
	
	if(writeEdgeColor){
		
		//System.out.println("Writing the edge colors.");
		try{
			
			/*FileOutputStream file = new FileOutputStream("edgeColor" + points.size() +"-" + nextCase + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(file);
			  
			out.writeObject(edgeColor);
			  
			out.close();
			file.close();*/
			
			for(Map.Entry<ArrayList<String>, String> entry: edgeColor.entrySet()) {

				ArrayList<String> datPair = entry.getKey();
				
				if(datPair.get(0).length() == 1 && datPair.get(1).length() == 1){
					String datColor = edgeColor.get(datPair);
					if(datColor.equals("YELLOW")) {
						edgeColor.replace(datPair,"yellow");
					}
					
				}
			
			}
			
			String key = "edgeColor" + points.size() +"-" + nextCase;
			allTheMaps.put(key, edgeColor);
			
			//HashMap<ArrayList<String>, Integer> canBlockAbove
			//HashMap<ArrayList<String>, Integer> canBlockBelow
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

	
	
    return true;
  }
  
  public static boolean mustBeClose(String color){
	  
	  return color.equalsIgnoreCase("green") || color.equalsIgnoreCase("yellow") || color.equalsIgnoreCase("red");
  }
  
  public static boolean cannotBlock(String color){
	  
	  return color.equalsIgnoreCase("green") || color.equalsIgnoreCase("blue") || color.equalsIgnoreCase("purple");
  }
  
  public static String[] sortViewpoints(ArrayList<String> ordering, String[] allPoints, String nextCase){
	  
	  ArrayList<String> everyViewpoints = new ArrayList<String>();
	
	//String[] allPoints = {"ABCDEGH","BCDEFHI","ACDEFGI","ABDEFGH","BCEFGHI","ACDFGHI","ABDEGHI","ABCEFHI", "ABCDFGI","ABDFH","BCEGI","ACDFH","BDEGI", "ACEFH","BDFGI","ACEGH","ABDEGH","BCEFHI","ACDFGI","ADG","BEH","CFI","ABEG","BCFH","CDGI","ADEH","BEFI","ACFG","BDGH","CEHI","ADFI","ABCDFH","BCDEGI","ACDEFH","BDEFGI","ACEFGH","BDFGHI","ACEGHI","ABDFHI","ABCEGI","ABCEH","BCDFI","ACDEG","BDEFH","CEFGI","ADFGH","BEGHI","ACFHI","ABDGI", "ABCFGH","BCDGHI","ACDEHI","ABDEFI","ABCEFG","BCDFGH","CDEGHI","ADEFHI","ABEFGI","CEGI","ADFH","BEGI","ACFH","BDGI","ACEH","BDFI","ADEG","BEFH","CFGI","ADGH","BEHI","ACFI","ABDG","BCEH","CDFI","ADFG","BEGH","CFHI","ADGI","ABEH","BCFI","ACDG","BDEH","CEFI" };
	
	//String[] startingPoints = {"ACEG","BDFH","CEGI","ADFH","BEGI","ACFH","BDGI","ACEH","BDFI","ADEG","BEFH","CFGI","ADGH","BEHI","ACFI","ABDG","BCEH","CDFI","ADFG","BEGH","CFHI","ADGI","ABEH","BCFI","ACDG","BDEH","CEFI" };
	
	//String[] allPoints = {"ABEH", "CFGI", "ACDG", "CDGI", "BEGH", "BCEGI", "ACDFH", "BDEGI", "BDFGHI", "BDGI", "ACFI", "ADFG", "BCDEFHI", "ABCEGI", "BCDEGI", "ABDEGH", "BCDGHI", "ACEFH", "CFI", "ADEH", "ACDFGHI", "BDFI", "CFHI", "BEFH", "ACDEFH", "BCFI", "ACDFGI", "BCEFHI", "ABCDFGI", "BEFI", "ABCFGH", "ADG", "ACEFGH", "ABDFHI", "ABDFH", "BCDFI", "ACDEG", "ACDEHI", "ACDEFGI", "BEHI", "ADGI", "BEGI", "ACFG", "ABDEGHI", "ABDEFGH", "ADGH", "CEGI", "BDEFGI", "BDEH", "BEH", "CEFGI", "ACEGH", "ABCEH", "ABDEFI", "ADEG", "CDEGHI", "ABEFGI", "CDFI", "BDFGI", "ABCEFG", "ABDGI", "ABEG", "ADFI", "ACEH", "ADFGH", "ABCDFH", "BCFH", "BCEFGHI", "CEFI", "BEGHI", "BCDFGH", "ADFH", "BDGH", "ACEGHI", "BCEH", "ACFH", "ABDG", "BDEFH", "CEHI", "ABCEFHI", "ABCDEGH", "ADEFHI", "ACFHI" };

		
		for (String s : allPoints)
		{
			everyViewpoints.add(s);
		}
		ArrayList<String> usableViewpoints = new ArrayList<String>();
		usableViewpoints = availableViewpoints(ordering, everyViewpoints);
		
		
	  HashMap<ArrayList<String>, String> edgeColor = null;
		try
        {   
            // Reading the object from a file
            /*FileInputStream file = new FileInputStream("edgeColor" + ordering.size() +"-" + nextCase + ".ser");
            ObjectInputStream in = new ObjectInputStream(file);
              
            // Method for deserialization of object
            edgeColor = (HashMap<ArrayList<String>, String>)in.readObject();
              
            in.close();
            file.close();*/
			String key = "edgeColor" + ordering.size() +"-" + nextCase;
			edgeColor = allTheMaps.get(key);
			
        }          
        catch(Exception e)
        {
            e.printStackTrace();
        }
		
		//ArrayList<Integer[]> pointsAndGapsBrah = new ArrayList<Integer[]>();
		Integer[] gapsBrah = new Integer[usableViewpoints.size()];
		
		for (int i = 0; i < usableViewpoints.size(); ++i)
		{
			// insert usableViewpoints[i] into our current ordering in every gap
			int feasibleGaps = 0;
            
            //ArrayList<Integer> storedGaps = new ArrayList<Integer>();
            //int numFeasibleGaps = 0;
            // storedGaps.clear();
			String thisPoint = usableViewpoints.get(i);
			
			for (int j = 0; j < ordering.size(); ++j)
			{
				
				HashMap<ArrayList<String>, String> copyOfEdgeColors = new HashMap<ArrayList<String>, String>(edgeColor);
				
				
				
				ArrayList<String> newOrdering = new ArrayList<String>();
				newOrdering.clear();
				newOrdering.addAll(ordering);
				newOrdering.add(j, usableViewpoints.get(i));
				
				for(int k=0; k<newOrdering.size(); k++)
				{
					if(k==j)
						continue;
					
					ArrayList<String> pair = new ArrayList<String>();
					computePair(k,j,pair,newOrdering);
					String otherPoint = newOrdering.get(k);
					
					if(otherPoint.length() > 1){
					
						//VP-VP edge
						copyOfEdgeColors.put(pair,"cyan");
					}
					else if(thisPoint.indexOf(otherPoint) == -1){
						
						//VP-Guard that don't see each other
						copyOfEdgeColors.put(pair,"orange");				
					}
					else{
						//VP-Guard that see each other
						copyOfEdgeColors.put(pair,"green");
					}
				}
				
                
				if (isFeasibleOrdering(newOrdering,copyOfEdgeColors,true,nextCase))
				{
					feasibleGaps++;
				}
				
			}
			
			
			//Integer[] pointAndGaps = { i, feasibleGaps};
			gapsBrah[i] = feasibleGaps;
			
		}
		
		
		//String[] sortedOrder = new String[usableViewpoints.size()];
		for(int i=1; i<usableViewpoints.size(); i++){
			
			int numGaps = gapsBrah[i];
			String thisPoint = usableViewpoints.get(i);
			int j = i-1;
			while(j >= 0 && gapsBrah[j] > numGaps){
				
				gapsBrah[j+1] = gapsBrah[j];
				allPoints[j+1] = allPoints[j];
				
				j--;
			}
			
			gapsBrah[j+1] = numGaps;
			allPoints[j+1] = thisPoint;
			
		}
		
		/*for(int i=0; i<usableViewpoints.size(); i++){
			System.out.print(gapsBrah[i]);
			System.out.print(" ");
		}
		System.out.println();*/
		
		return allPoints;
	  
  }
  
  public static ArrayList<String> initializeGapsThatWork(ArrayList<String> ordering, HashMap<ArrayList<String>, String> edgeColor, String nextCase, ArrayList<String> importantPoints){
	  
	  String[] everyFreakinPoint = {"HI", "GI", "GH", "GHI", "FI", "FH", "FHI", "FG", "FGI", "FGH", "FGHI", "EI", "EH", "EHI", "EG", "EGI", "EGH", "EGHI", "EF", "EFI", "EFH", "EFHI", "EFG", "EFGI", "EFGH", "EFGHI", "DI", "DH", "DHI", "DG", "DGI", "DGH", "DGHI", "DF", "DFI", "DFH", "DFHI", "DFG", "DFGI", "DFGH", "DFGHI", "DE", "DEI", "DEH", "DEHI", "DEG", "DEGI", "DEGH", "DEGHI", "DEF", "DEFI", "DEFH", "DEFHI", "DEFG", "DEFGI", "DEFGH", "DEFGHI", "CI", "CH", "CHI", "CG", "CGI", "CGH", "CGHI", "CF", "CFI", "CFH", "CFHI", "CFG", "CFGI", "CFGH", "CFGHI", "CE", "CEI", "CEH", "CEHI", "CEG", "CEGI", "CEGH", "CEGHI", "CEF", "CEFI", "CEFH", "CEFHI", "CEFG", "CEFGI", "CEFGH", "CEFGHI", "CD", "CDI", "CDH", "CDHI", "CDG", "CDGI", "CDGH", "CDGHI", "CDF", "CDFI", "CDFH", "CDFHI", "CDFG", "CDFGI", "CDFGH", "CDFGHI", "CDE", "CDEI", "CDEH", "CDEHI", "CDEG", "CDEGI", "CDEGH", "CDEGHI", "CDEF", "CDEFI", "CDEFH", "CDEFHI", "CDEFG", "CDEFGI", "CDEFGH", "CDEFGHI", "BI", "BH", "BHI", "BG", "BGI", "BGH", "BGHI", "BF", "BFI", "BFH", "BFHI", "BFG", "BFGI", "BFGH", "BFGHI", "BE", "BEI", "BEH", "BEHI", "BEG", "BEGI", "BEGH", "BEGHI", "BEF", "BEFI", "BEFH", "BEFHI", "BEFG", "BEFGI", "BEFGH", "BEFGHI", "BD", "BDI", "BDH", "BDHI", "BDG", "BDGI", "BDGH", "BDGHI", "BDF", "BDFI", "BDFH", "BDFHI", "BDFG", "BDFGI", "BDFGH", "BDFGHI", "BDE", "BDEI", "BDEH", "BDEHI", "BDEG", "BDEGI", "BDEGH", "BDEGHI", "BDEF", "BDEFI", "BDEFH", "BDEFHI", "BDEFG", "BDEFGI", "BDEFGH", "BDEFGHI", "BC", "BCI", "BCH", "BCHI", "BCG", "BCGI", "BCGH", "BCGHI", "BCF", "BCFI", "BCFH", "BCFHI", "BCFG", "BCFGI", "BCFGH", "BCFGHI", "BCE", "BCEI", "BCEH", "BCEHI", "BCEG", "BCEGI", "BCEGH", "BCEGHI", "BCEF", "BCEFI", "BCEFH", "BCEFHI", "BCEFG", "BCEFGI", "BCEFGH", "BCEFGHI", "BCD", "BCDI", "BCDH", "BCDHI", "BCDG", "BCDGI", "BCDGH", "BCDGHI", "BCDF", "BCDFI", "BCDFH", "BCDFHI", "BCDFG", "BCDFGI", "BCDFGH", "BCDFGHI", "BCDE", "BCDEI", "BCDEH", "BCDEHI", "BCDEG", "BCDEGI", "BCDEGH", "BCDEGHI", "BCDEF", "BCDEFI", "BCDEFH", "BCDEFHI", "BCDEFG", "BCDEFGI", "BCDEFGH", "BCDEFGHI", "AI", "AH", "AHI", "AG", "AGI", "AGH", "AGHI", "AF", "AFI", "AFH", "AFHI", "AFG", "AFGI", "AFGH", "AFGHI", "AE", "AEI", "AEH", "AEHI", "AEG", "AEGI", "AEGH", "AEGHI", "AEF", "AEFI", "AEFH", "AEFHI", "AEFG", "AEFGI", "AEFGH", "AEFGHI", "AD", "ADI", "ADH", "ADHI", "ADG", "ADGI", "ADGH", "ADGHI", "ADF", "ADFI", "ADFH", "ADFHI", "ADFG", "ADFGI", "ADFGH", "ADFGHI", "ADE", "ADEI", "ADEH", "ADEHI", "ADEG", "ADEGI", "ADEGH", "ADEGHI", "ADEF", "ADEFI", "ADEFH", "ADEFHI", "ADEFG", "ADEFGI", "ADEFGH", "ADEFGHI", "AC", "ACI", "ACH", "ACHI", "ACG", "ACGI", "ACGH", "ACGHI", "ACF", "ACFI", "ACFH", "ACFHI", "ACFG", "ACFGI", "ACFGH", "ACFGHI", "ACE", "ACEI", "ACEH", "ACEHI", "ACEG", "ACEGI", "ACEGH", "ACEGHI", "ACEF", "ACEFI", "ACEFH", "ACEFHI", "ACEFG", "ACEFGI", "ACEFGH", "ACEFGHI", "ACD", "ACDI", "ACDH", "ACDHI", "ACDG", "ACDGI", "ACDGH", "ACDGHI", "ACDF", "ACDFI", "ACDFH", "ACDFHI", "ACDFG", "ACDFGI", "ACDFGH", "ACDFGHI", "ACDE", "ACDEI", "ACDEH", "ACDEHI", "ACDEG", "ACDEGI", "ACDEGH", "ACDEGHI", "ACDEF", "ACDEFI", "ACDEFH", "ACDEFHI", "ACDEFG", "ACDEFGI", "ACDEFGH", "ACDEFGHI", "AB", "ABI", "ABH", "ABHI", "ABG", "ABGI", "ABGH", "ABGHI", "ABF", "ABFI", "ABFH", "ABFHI", "ABFG", "ABFGI", "ABFGH", "ABFGHI", "ABE", "ABEI", "ABEH", "ABEHI", "ABEG", "ABEGI", "ABEGH", "ABEGHI", "ABEF", "ABEFI", "ABEFH", "ABEFHI", "ABEFG", "ABEFGI", "ABEFGH", "ABEFGHI", "ABD", "ABDI", "ABDH", "ABDHI", "ABDG", "ABDGI", "ABDGH", "ABDGHI", "ABDF", "ABDFI", "ABDFH", "ABDFHI", "ABDFG", "ABDFGI", "ABDFGH", "ABDFGHI", "ABDE", "ABDEI", "ABDEH", "ABDEHI", "ABDEG", "ABDEGI", "ABDEGH", "ABDEGHI", "ABDEF", "ABDEFI", "ABDEFH", "ABDEFHI", "ABDEFG", "ABDEFGI", "ABDEFGH", "ABDEFGHI", "ABC", "ABCI", "ABCH", "ABCHI", "ABCG", "ABCGI", "ABCGH", "ABCGHI", "ABCF", "ABCFI", "ABCFH", "ABCFHI", "ABCFG", "ABCFGI", "ABCFGH", "ABCFGHI", "ABCE", "ABCEI", "ABCEH", "ABCEHI", "ABCEG", "ABCEGI", "ABCEGH", "ABCEGHI", "ABCEF", "ABCEFI", "ABCEFH", "ABCEFHI", "ABCEFG", "ABCEFGI", "ABCEFGH", "ABCEFGHI", "ABCD", "ABCDI", "ABCDH", "ABCDHI", "ABCDG", "ABCDGI", "ABCDGH", "ABCDGHI", "ABCDF", "ABCDFI", "ABCDFH", "ABCDFHI", "ABCDFG", "ABCDFGI", "ABCDFGH", "ABCDFGHI", "ABCDE", "ABCDEI", "ABCDEH", "ABCDEHI", "ABCDEG", "ABCDEGI", "ABCDEGH", "ABCDEGHI", "ABCDEF", "ABCDEFI", "ABCDEFH", "ABCDEFHI", "ABCDEFG", "ABCDEFGI", "ABCDEFGH", "ABCDEFGHI"};
	  
	  int bigThreshold = 6;
	  
	  ArrayList<String> everyViewpoints = new ArrayList<String>();
	  for (String s : everyFreakinPoint)
	  {
		everyViewpoints.add(s);
	  }
	  ArrayList<String> usableViewpoints = new ArrayList<String>();
	  usableViewpoints = availableViewpoints(ordering, everyViewpoints);
	  
	  for(int i=0; i<usableViewpoints.size(); i++){
		  
		  String thisPoint = usableViewpoints.get(i);
		  ArrayList<String> gapsForThisPoint = new ArrayList<String>();
		  Stack<ArrayList<String>> stackForThisPoint = new Stack<ArrayList<String>>();
		  
		  String prevPoint = "-infty";
		  
		  //System.out.println("Current point: " + thisPoint + ", current order length: " + ordering.size());
		  
		  for (int j = 0; j < ordering.size(); ++j)
			{
				
				String currentPoint = ordering.get(j);
				
				HashMap<ArrayList<String>, String> copyOfEdgeColors = new HashMap<ArrayList<String>, String>(edgeColor);
				
				
				
				ArrayList<String> newOrdering = new ArrayList<String>();
				newOrdering.clear();
				newOrdering.addAll(ordering);
				newOrdering.add(j, usableViewpoints.get(i));
				
				for(int k=0; k<newOrdering.size(); k++)
				{
					if(k==j)
						continue;
					
					ArrayList<String> pair = new ArrayList<String>();
					computePair(k,j,pair,newOrdering);
					String otherPoint = newOrdering.get(k);
					
					if(otherPoint.length() > 1){
					
						//VP-VP edge
						copyOfEdgeColors.put(pair,"cyan");
					}
					else if(thisPoint.indexOf(otherPoint) == -1){
						
						//VP-Guard that don't see each other
						copyOfEdgeColors.put(pair,"orange");				
					}
					else{
						//VP-Guard that see each other
						copyOfEdgeColors.put(pair,"green");
					}
				}
				
                
				if (isFeasibleOrdering(newOrdering,copyOfEdgeColors,false,nextCase))
				{
					gapsForThisPoint.add(prevPoint + ":" + currentPoint);
					
					if(gapsForThisPoint.size() > bigThreshold && !importantPoints.contains(thisPoint))
						break;
					
				}
				
				prevPoint = currentPoint;
			}
			
			if(gapsForThisPoint.size() == 0){
				System.out.println("No gaps for " + thisPoint + ".  Rejecting.");
				for(int z =0; z<i; z++){
					String oldPoint = usableViewpoints.get(z);
					String hashKey = oldPoint + "-" + nextCase;
					gapsThatWork.get(hashKey).pop();
				}
				
				return null;  //null means we are rejecting.
			}
			
			if(gapsForThisPoint.size() > bigThreshold  && !importantPoints.contains(thisPoint)){
				gapsForThisPoint = new ArrayList<String>();
				gapsForThisPoint.add("Big");
				gapsForThisPoint.add("Brah");
			}
			
			
			System.out.println("Gaps for " + thisPoint + ": " + gapsForThisPoint);
			stackForThisPoint.push(gapsForThisPoint);
			String hashKey = thisPoint + "-" + nextCase;
			gapsThatWork.put(hashKey,stackForThisPoint);
			
	  }
	  
	  return ordering;
	  
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
                /*String serverAddress = "34.229.186.83";
                Socket socket = new Socket(serverAddress, 9452);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("1");  //sending a 1 to denote, I want to start
                */
                while(true){
                    /*String nextCase = in.readLine();
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
                    String caseNumber = nextCase.substring(0, firstComma);*/
					
					//String[] acegLocations = {"ACEG,A,B,C,D,E,F,G,H,I","A,ACEG,B,C,D,E,F,G,H,I","A,B,ACEG,C,D,E,F,G,H,I","A,B,C,ACEG,D,E,F,G,H,I","A,B,C,D,E,F,G,H,ACEG,I"};
					
                    String fileStuff;// = nextCase.substring(firstComma + 1);
					
					//fileStuff = "A,B,C,D,E,F,G,H,ACEG,I";
					//fileStuff = "[ACEG, A, B, C, D, E, F, G, H, I]";
					fileStuff = "[ADFH, BDFH, CEGI, BEGI, ACEG, A, B, BDGI, C, D, ACEH, E, ACFH, F, G, H, BDFI, I]";
					//fileStuff = "[CDFI, ADFH, BDFH, BEHI, BEFH, BDEH, ABEH, BCEH, BEGH, CEGI, BEGI, ACEG, ADEG, ADGI, A, B, BDGI, CEFI, C, CFGI, BCFI, D, ACEH, E, ACFH, ACFI, F, CFHI, ADGH, G, ACDG, ABDG, ADFG, H, BDFI, I]";
					//fileStuff = "ACFI,BCFI,CFHI,ACFH,ADFH,BDFH,ABEH,BEHI,BEFH,BDEH,BCEH,BEGH,BEGI,CEGI,ACEG,ACDG,ADGI,ADEG,A,ABDG,B,BDGI,CEFI,CFGI,C,CDFI,BDFI,ADFG,ADGH,D,ACEH,E,F,G,H,I";
					fileStuff = fileStuff.replace(" ", "");  //gtfo spaces
                    fileStuff = fileStuff.substring(1, fileStuff.length()-1);  //gtfo braces
					
					//BDFH,ACFH,ACEG,CEGI,A,B,BEGI,C,D,E,F,ACEH,G,H,ADFH,BDGI,BDFI,I";
					//for(String fileStuff : acegLocations)
					//{
                    
						// nextCase == "1,ACEG,BDFH,ADEH,ABEF,BCFG,CDGH,A,B,C,D,E,F,G,H"
						// caseNumber == "1"
						// fileStuff == "ACEG,BDFH,ADEH,ABEF,BCFG,CDGH,A,B,C,D,E,F,G,H"
						
						 //   * case1.txt: ACEG BDFH ADEH ABEF BCFG CDGH A B C D E F G H
						
						//System.out.println("working on case " + nextCase);
						String[] arr = fileStuff.split(",");
						ArrayList<String> ordering = new ArrayList<String>();
						for (String item : arr) {
							ordering.add(item);
						}
						
					   // int acegIndex = ordering.indexOf("ACEG");
						//int aIndex = ordering.indexOf("A");
						boolean rejected = false;
						/*if(aIndex < acegIndex){                        
							System.out.println("Redundant ordering: ACEG is right of A.  Rejecting.");
							System.out.flush();
							rejected = true;
						}
						
						int bdfhIndex = ordering.indexOf("BDFH");
						int eIndex = ordering.indexOf("E");
						int fIndex = ordering.indexOf("F");
						if(eIndex < bdfhIndex && bdfhIndex < fIndex){
							
							System.out.println("Redundant ordering: BDFH is between E and F.  Rejecting.");
							System.out.flush();
							rejected = true;
						}
						
						
						int gIndex = ordering.indexOf("G");
						int hIndex = ordering.indexOf("H");
						if(gIndex < bdfhIndex && bdfhIndex < hIndex){
							
							System.out.println("Redundant ordering: BDFH is between G and H.  Rejecting.");
							System.out.flush();
							rejected = true;
						}*/
											
						String erikLovesFiles = Long.toString(Thread.currentThread().getId());//caseNumber;
						if(!isFeasibleOrdering(ordering,null,true,erikLovesFiles))
							rejected = true;
						
						String sendMe = "";
						if(!rejected){                        
						
							double probToAccept;
							
							//String[] startingPoints = {"ABEH", "CFGI", "ACDG", "CDGI", "BEGH", "BCEGI", "ACDFH", "BDEGI", "BDFGHI", "BDGI", "ACFI", "ADFG", "BCDEFHI", "ABCEGI", "BCDEGI", "ABDEGH", "BCDGHI", "ACEFH", "CFI", "ADEH", "ACDFGHI", "BDFI", "CFHI", "BEFH", "ACDEFH", "BCFI", "ACDFGI", "BCEFHI", "ABCDFGI", "BEFI", "ABCFGH", "ADG", "ACEFGH", "ABDFHI", "ABDFH", "BCDFI", "ACDEG", "ACDEHI", "ACDEFGI", "BEHI", "ADGI", "BEGI", "ACFG", "ABDEGHI", "ABDEFGH", "ADGH", "CEGI", "BDEFGI", "BDEH", "BEH", "CEFGI", "ACEGH", "ABCEH", "ABDEFI", "ADEG", "CDEGHI", "ABEFGI", "CDFI", "BDFGI", "ABCEFG", "ABDGI", "ABEG", "ADFI", "ACEH", "ADFGH", "ABCDFH", "BCFH", "BCEFGHI", "CEFI", "BEGHI", "BCDFGH", "ADFH", "BDGH", "ACEGHI", "BCEH", "ACFH", "ABDG", "BDEFH", "CEHI", "ABCEFHI", "ABCDEGH", "ADEFHI", "ACFHI" }; probToAccept = 30/((double)startingPoints.length);
							//String[] startingPoints = {"ACEG","BDFH","CEGI","ADFH","BEGI","ACFH","BDGI","ACEH","BDFI","ADEG","BEFH","CFGI","ADGH","BEHI","ACFI","ABDG","BCEH","CDFI","ADFG","BEGH","CFHI","ADGI","ABEH","BCFI","ACDG","BDEH","CEFI" }; probToAccept = 33; //ZED = Zhongxiu ends debate.
							//String[] startingPoints = {"ACEG","BDFH","CEGI","ADFH","BDGI","ACEH","ADEG","BEFH","ACFI","ABDG","BCEH","CDFI","ADFG","CFHI","ADGI","ABEH","BCFI","BDEH" }; probToAccept = 44; //ZED = Zhongxiu ends debate.
							//String[] startingPoints = {"BDFH","CEGI","BDGI","ACEH","ADEG","BEFH","ABDG","BCEH","CDFI","CFHI","ADGI","ABEH","BCFI" }; probToAccept = 55; //ZED = Zhongxiu ends debate.
							//String[] startingPoints = {"BDFH","CEGI","ADFH","BEGI","ACFH","BDGI","ACEH","BDFI"}; probToAccept = 66; //ZED = Zhongxiu ends debate.
							//String[] startingPoints = {"ADEG","BEFH","CFGI","ADGH","BEHI","ACFI","ABDG","BCEH","CDFI","ADFG","BEGH","CFHI","ADGI","ABEH","BCFI","ACDG","BDEH","CEFI" }; probToAccept = 77; //ZED = Zhongxiu ends debate.
							String[] startingPoints = {"CDGI", "BCEGI", "ACDFH", "BDEGI", "BDFGHI", "BCDEFHI", "ABCEGI", "BCDEGI", "ABDEGH"};probToAccept = 88;// "BCDGHI", "ACEFH", "CFI", "ADEH", "ACDFGHI", "ACDEFH",  "ACDFGI", "BCEFHI", "ABCDFGI", "BEFI", "ABCFGH", "ADG", "ACEFGH", "ABDFHI", "ABDFH", "BCDFI", "ACDEG", "ACDEHI", "ACDEFGI", "ACFG", "ABDEGHI", "ABDEFGH", "BDEFGI", "BEH", "CEFGI", "ACEGH", "ABCEH", "ABDEFI", "CDEGHI", "ABEFGI", "BDFGI", "ABCEFG", "ABDGI", "ABEG", "ADFI", "ADFGH", "ABCDFH", "BCFH", "BCEFGHI", "BEGHI", "BCDFGH", "BDGH", "ACEGHI","BDEFH", "CEHI", "ABCEFHI", "ABCDEGH", "ADEFHI", "ACFHI" }; 
							//probToAccept = 88;
							//fileStuff = "CDFI,ADFH,BDFH,CFHI,BCFI,CEFI,ABEH,BEHI,BEFH,BEGH,BEGI,CEGI,ACEG,ACDG,ADGI,ADGH,ADEG,A,ABDG,B,C,BDGI,BCEH,BDEH,ACFH,D,ACEH,ACFI,E,F,CFGI,G,ADFG,H,BDFI,I";
							
							ArrayList<String> someStuff = new ArrayList<String>();
							 
							for(int i=0; i<startingPoints.length; i++){
								
								if(Math.random() < probToAccept)
									someStuff.add(startingPoints[i]);
								
							}
							
							String[] allPoints = new String[someStuff.size()];
							for(int i=0; i<someStuff.size(); i++){
								allPoints[i] = someStuff.get(i);
							}
							
							//allPoints = sortViewpoints(ordering,allPoints,erikLovesFiles);
							
							//initializeGapsThatWork(ordering, allTheMaps.get("edgeColor" + ordering.size() + "-"+erikLovesFiles), erikLovesFiles);
						
							long startTime = System.currentTimeMillis();
							String result = determineCase(ordering, erikLovesFiles, allPoints);
							long endTime = System.currentTimeMillis();
							
							long runningTime = (endTime - startTime)/1000;
							
							if(!result.equals("")){
								result = result.replace(" ", "");  //gtfo spaces
								result = result.substring(1, result.length()-1);  //gtfo braces
								System.out.println("Case accepted in " + runningTime + " seconds. \n" + result + "\n"); //ordering on own line
							   // sendMe = caseNumber+",";
								sendMe += result;
								System.exit(0);
							} else{
								
								System.out.println("Case rejected in " + runningTime + " seconds (which is what we want).\n");
								System.out.println(someStuff);
								System.out.flush();
								System.exit(0);
								//sendMe = "S"+caseNumber;
							}
						} else{
							//reject case
							System.out.println("Case rejected immediately - inital case is infeasible.");
							System.exit(0);
						   // sendMe = "S"+caseNumber;
						}
					
					//}
                    
                    /*socket = new Socket(serverAddress, 9452);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(sendMe);*/
                    
                    // case was done successfully, tell server about it
                    // DONE SOLVING THE CASE
                    
                    // assuming the case completed successfully, write back S
                    // if the case failed, output anything but S
                    
                    //System.out.println("solved case " + nextCase);
                    //System.out.flush();
                }
            } catch (Exception e) {
                System.out.println("Got an exception: " + e);
                e.printStackTrace();
            }
        }
    }
}
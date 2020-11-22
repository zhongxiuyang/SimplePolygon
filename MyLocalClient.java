
/*
A client that can be run from anywhere. If the server is running, the following command starts up the client:
java MyClient numThreads
numThreads is the number of threads you want the current machine to use.
*/

import java.net.*;
import java.io.*;
import java.util.*;

public class MyLocalClient {

  private static String serverAddress = "";
  public static ArrayList<String> allViewpoints = new ArrayList<String>();

  public static void main(String[] args) throws Exception {
    //serverAddress = "52.203.213.88";
    //int numberThreads = Integer.parseInt(args[0]);
    // start threads
    //for (int i = 0; i < numberThreads; i++) {
      new RunIt().start();
    //}
  }

  public static void generateAllPoints(int numOfGuards) {
    for (int i = 0; i < (1 << numOfGuards); i++) {
      String s = "";
      for (int j = 0; j < numOfGuards; j++) {
        if ((i & (1 << j)) > 0) {
          s = s + (char) (j + 'A');
        }
      }

      if (s.length() == 5) {
        if (!s.contains("ABC") && !s.contains("BCD") && !s.contains("CDE") && !s.contains("DEF") && !s.contains("EFG")
            && !s.contains("FGH")) {
          allViewpoints.add(s);
          // System.out.println(s);
        }
      }
    }
  }

  public static ArrayList<String> availableViewpoints(int numOfGuards, ArrayList<String> currentOrdering) {
    ArrayList<String> newViewpoints = new ArrayList<String>();
    newViewpoints.addAll(allViewpoints);
    newViewpoints.removeAll(currentOrdering);
    return newViewpoints;
  }

  public static boolean isFeasibleOrdering(ArrayList<String> points) {
    // System.out.println(points);
    // Edge colors:
    // Green - See each other - unpierceable and close
    // Red - Do not see other - close - must be pierced
    // Purple - Do not see each other - cannot be pierced - too far
    // Orange - Do not see each other - don't care how we block.
    // Blue - Don't care if see each other - cannot be pierced.
    // Cyan - Don't care if see each other - don't care if pierced.

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
      for (int i = 0; i < points.size() - 5; i++) {
        for (int l = i + 3; l < points.size() - 2; l++) {
          pair.clear();
          pair.add(points.get(i));
          pair.add(points.get(l));
          if ((edgeColor.get(pair).compareTo("orange") != 0) && (edgeColor.get(pair).compareTo("cyan") != 0)) {
            continue;
          }
          for (int j = i + 1; j < l - 1; j++) {
            pair.clear();
            pair.add(points.get(j));
            pair.add(points.get(l));
            if ((edgeColor.get(pair).compareTo("purple") != 0) && (edgeColor.get(pair).compareTo("green") != 0)
                && (edgeColor.get(pair).compareTo("blue") != 0)) {
              continue;
            }
            for (int k = j + 1; k < l; k++) {
              pair.clear();
              pair.add(points.get(i));
              pair.add(points.get(k));
              if ((edgeColor.get(pair).compareTo("purple") != 0) && (edgeColor.get(pair).compareTo("green") != 0)
                  && (edgeColor.get(pair).compareTo("blue") != 0)) // unpierceable
              {
                continue;
              }
              for (int m = l + 1; m < points.size() - 1; m++) {
                pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(m));
                if ((edgeColor.get(pair).compareTo("purple") != 0) && (edgeColor.get(pair).compareTo("green") != 0)
                    && (edgeColor.get(pair).compareTo("blue") != 0)) {
                  continue;
                }
                for (int n = m + 1; n < points.size(); n++) {
                  pair.clear();
                  pair.add(points.get(l));
                  pair.add(points.get(n));
                  if ((edgeColor.get(pair).compareTo("purple") == 0) || (edgeColor.get(pair).compareTo("green") == 0)
                      || (edgeColor.get(pair).compareTo("blue") == 0)) {
                    pair.clear();
                    pair.add(points.get(i));
                    pair.add(points.get(l));

                    if (edgeColor.get(pair).compareTo("orange") == 0) {
                      edgeColor.replace(pair, "purple");
                      updatedAnEdge = true;
                    }
                    if (edgeColor.get(pair).compareTo("cyan") == 0) {
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
      for (int i = 0; i < points.size() - 3; i++) {
        for (int k = i + 2; k < points.size() - 1; k++) {
          pair.clear();
          pair.add(points.get(i));
          pair.add(points.get(k));
          if (edgeColor.get(pair).compareTo("green") != 0) {
            continue;
          }
          for (int j = i + 1; j < k; j++) {
            for (int l = k + 1; l < points.size(); l++) {
              pair.clear();
              pair.add(points.get(j));
              pair.add(points.get(l));
              if (edgeColor.get(pair).compareTo("green") == 0) {
                pair.clear();
                pair.add(points.get(i));
                pair.add(points.get(j));
                if (edgeColor.get(pair).compareTo("purple") == 0) {
                  pair.clear();
                  pair.add(points.get(k));
                  pair.add(points.get(l));
                  if (edgeColor.get(pair).compareTo("purple") == 0) {
                    // System.out.println("case A rejection (k, l): ");
                    // System.out.println(pair);
                    return false;
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
                    // System.out.println("case B rejection (j, k): ");
                    // System.out.println(pair);
                    return false;
                  }
                }

                pair.clear();
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
                }

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
                }

                pair.clear();
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
                }

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
                }

                pair.clear();
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
                }

                pair.clear();
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
                }

                pair.clear();
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
                }

                pair.clear();
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
                }
              }
            }
          }
        }
      }
    } while (updatedAnEdge);

    return true;
  }

  public static class RunIt extends Thread {

    public void run() {
      try {
        // Administrative stuff to connect with the server.
        //BufferedReader in;
        //PrintWriter out;

        // We are connecting on port 9450.
        //Socket socket = new Socket(serverAddress, 9450);
        //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //out = new PrintWriter(socket.getOutputStream(), true);
        generateAllPoints(8);

        while (true) {
          String nextCase = "29,ACEG,ADEH,BDFH,CDGH,BCFG,ABEF,A,B,C,D,E,F,G,H";
          if (nextCase.equals("STOP")) {
            System.out.println("something got wrecked, check server");
            return;
          }
          if (nextCase.equals("FINISHED")) {
            System.out.println("no additional cases left to work on for this thread");
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
		  System.out.println("a");
          for (String item : arr) {
            ordering.add(item);
          }
		  System.out.println("b");
          System.out.println("working on case " + nextCase);

          boolean solvedThisCase = false;
          if (!isFeasibleOrdering(ordering)) {
            solvedThisCase = true;
			System.out.println("c");
          } else {
			  System.out.println("d");
            // keep adding one more viewpoint with smallest feasible gaps until no gaps will
            // accept your new viewpoint
            ArrayList<String> feasibleViewpoints = new ArrayList<String>();
            feasibleViewpoints = availableViewpoints(8, ordering);
            while (feasibleViewpoints.size() > 0) {
				System.out.println("e"+feasibleViewpoints.size());
              int feasibleGaps = 0;
              int minFeasibleGaps = ordering.size() + 2;
              int idealViewPointIndex = -1;
              ArrayList<String> newOrdering = new ArrayList<String>();
              for (int j = 0; j < feasibleViewpoints.size(); j++) // process each feasible viewpoints
              {
				  System.out.println("f"+feasibleViewpoints.size());
                // insert each unused viewpoint into current ordering
                // generate new ordering
                feasibleGaps = 0;
                for (int k = 0; k <= ordering.size(); k++) {
                  newOrdering.clear();
                  newOrdering.addAll(ordering);
                  newOrdering.add(k, feasibleViewpoints.get(j));
                  // check if feasible in every gap
                  if (isFeasibleOrdering(newOrdering)) {
                    feasibleGaps++;
                  }
                //   else
                //   {
                //     System.out.println("Rejected ordering: ");
                //     System.out.println(newOrdering);
                //   }
                }
                if (feasibleGaps == 0) {
                  // if every gap in infeasible, reject
                  feasibleViewpoints.clear();
                  solvedThisCase = true;
                  break;
                } else {
                  // otherwise store the feasible gaps and its index
                  if (feasibleGaps < minFeasibleGaps) {
                    minFeasibleGaps = feasibleGaps;
                    idealViewPointIndex = j;
                  }
                }
              }
				System.out.println("g");
              // generate new ordering for this case
              if (feasibleGaps > 0) {
                for (int p = 0; p <= ordering.size(); p++) {
				System.out.println("h");	
				  newOrdering.clear();
                  newOrdering.addAll(ordering);
                  try {
                    newOrdering.add(p, feasibleViewpoints.get(idealViewPointIndex));
                  } catch (Exception e) {
                    System.out.println(idealViewPointIndex);
                  }

                  feasibleViewpoints.remove(idealViewPointIndex);
                  if (isFeasibleOrdering(newOrdering)) {
                    ordering.clear();
                    ordering.addAll(newOrdering);
                    break;
                  }
                }
              }
			  System.out.println("i");
            }
          }
          if (solvedThisCase) {
            System.out.println("Case rejected.\n");
          //  out.println("S");
          } else {
            System.out.println("Case accepted.\n");
            //out.println(ordering);
          }
          // case was done successfully, tell server about it
          // DONE SOLVING THE CASE

          // assuming the case completed successfully, write back S
          // if the case failed, output anything but S

          System.out.println("solved case " + nextCase);
        }
      } catch (Exception e) {
        System.out.println("something went really wrong.");
      }
    }
  }
}
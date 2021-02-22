
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class AcceptedLine {
    public static void main(String[] args) throws Exception { 
      if(args.length == 0){
        System.out.println("Brah.  Gimmie dat string, brah.");
      } 
      else{
        findRedEdges(args[0]);  
      }
        //findRedEdges("ACDFH,ABEFH,ABDFH,BDFH,ABDGH,ABDFG,ACDGH,ACDFG,CDGH,ADEGH,A,ABDEG,BDEGH,ABDEH,ADEH,ABEGH,B,BCEGH,ACEGH,C,ACEG,BCEFH,ACEFH,ABEF,D,E,F,BCFG,G,H");
    }
	
    
    // find red edges, basically re-run the findRedEdges
    // input: BDEH,ACEFH,A,...
    public static void findRedEdges(String caseAcceptedLine)
    {
        String[] pointsArray = caseAcceptedLine.split(",");
        ArrayList<String> points = new ArrayList<String>();
        for (String i : pointsArray) {
            points.add(i);
        }
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
                    if(i==0) {
                        stoppingIndexForL = points.size()-2;
                    }
                    else if(i==1) {
                        stoppingIndexForL = points.size()-1;
                    }
                    for (int l = i + 3; l != stoppingIndexForL; l++) {
                        pair.clear();
                        computePair(i, l, pair, points);
                        //System.out.println("Pair for " + i + " and " + l + ": " + pair);
                        //pair.add(points.get(i));
                        //pair.add(points.get(l));
                        if ((edgeColor.get(pair).compareTo("orange") != 0) && (edgeColor.get(pair).compareTo("cyan") != 0) && (edgeColor.get(pair).compareTo("red") != 0)) {
                            continue;
                        }
                        int stoppingIndexForM;
                        if(i==0) {
                            stoppingIndexForM = points.size()-1;
                        }   
                        else {
                            stoppingIndexForM = i-1;
                        }
                            

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
                                            else if(edgeColor.get(pair).compareTo("red") == 0) {
                                                findRedEdgesHelper(edgeColor);
                                                return;
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
                                            findRedEdgesHelper(edgeColor);
                                            return;
                                        }
                                        else if (edgeColor.get(pair).compareTo("orange") == 0) {
                                            edgeColor.replace(pair, "red");
                                            updatedAnEdge = true;
                                        }
                                        else if (edgeColor.get(pair).compareTo("blue") == 0) {
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
                                        if (edgeColor.get(pair).compareTo("purple") == 0) {
                                            findRedEdgesHelper(edgeColor);
                                            return;
                                        }
                                        else if (edgeColor.get(pair).compareTo("orange") == 0) {
                                            edgeColor.replace(pair, "red");
                                            updatedAnEdge = true;
                                        }
                                        else if (edgeColor.get(pair).compareTo("blue") == 0) {
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
                                        if (edgeColor.get(pair).compareTo("orange") == 0) {
                                            edgeColor.replace(pair, "red");
                                            updatedAnEdge = true;
                                        }
                                        else if (edgeColor.get(pair).compareTo("blue") == 0) {
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
                                        if (edgeColor.get(pair).compareTo("orange") == 0) {
                                            edgeColor.replace(pair, "red");
                                            updatedAnEdge = true;
                                        }
                                        else if (edgeColor.get(pair).compareTo("blue") == 0) {
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
            findRedEdgesHelper(edgeColor);
            return;
    }
    public static void findRedEdgesHelper(HashMap<ArrayList<String>, String> edgeColor)
    {
        // System.out.print(edgeColor);
        for(Entry<ArrayList<String>, String> entry: edgeColor.entrySet()) {

            // if give value is equal to value from entry
            // print the corresponding key
            if(entry.getValue().compareTo("red") == 0) {
                System.out.println(entry.getKey());
            }
        }
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
}
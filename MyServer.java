/*
This is a simple server class that I created to run our program in a distributed manner. Not much here needs to change in the final version of our program. We really just need to change the number of cases that need to be run. Sends to the client a number starting at 0 and going up to (totalNumberOfCases-1). Assumes the client already has access to the case files.

Nothing really to change here. Whenever we run this, I'll create a server, give you the ip address and you'll run the clients on whatever machines you want. I will also take over my computer lab and run the client on those 30+ machines with 4+ cores each.
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.*;


public class MyServer{

    //The port that our server listens on.
    private static final int PORT = 9450;

    //all cases to be done
    private static ArrayList<Integer> cases = new ArrayList<>();
    private static HashSet<Integer> stillToBeCompleted = new HashSet<>();
    private static int nextCase = 0;
    //private static int failed = 0;

    //The main method that starts our program.
    public static void main(String args[]) throws Exception{
        System.out.println("The server is running!");
        for(int i=1; i<=2162160; i++){
            cases.add(i);
            stillToBeCompleted.add(i);
        }

        MyPrinter mp = new MyPrinter();
        mp.start();

        //A listener socket that listens for any incoming connections.
        ServerSocket listener = new ServerSocket(PORT);
        try{
            //The program runs in an infinite loop waiting for new
            //clients to attach to the server.
            while(true){
                new ServerListener(listener.accept()).start();
            }
        } finally{
            //When the server ends, the listener is shutdown.
            listener.close();
        }
    }

    private static class MyPrinter extends Thread{
        //USELESS NOW WITH FILE MOVING, DELETE THIS
        public void run(){
            while(true){
                try{
                    PrintWriter output = new PrintWriter(new FileWriter("remaining"));
                    TreeSet<Integer> ordered = new TreeSet<>();
                    if(stillToBeCompleted.size()==0){
                        //everything is done, quit
                        System.out.println("fast lane");
                        System.exit(0);
                    }
                    //maybe not necessary, I don't know
                    synchronized(stillToBeCompleted){
                        Iterator<Integer> iter = stillToBeCompleted.iterator();
                        while(iter.hasNext()){
                            ordered.add(iter.next());
                        }
                    }
                    while(!ordered.isEmpty()){
                        Integer printMe = ordered.first();
                        output.println(printMe);
                        ordered.remove(printMe);
                    }
                    output.close();
                    Thread.currentThread().sleep(300000);
                } catch(Exception e){
                    System.out.println("something went really bad");
                }
            }
        }
    }

    //A private class that "listens" for new clients to connect.
    //A new ServerListener object is created for each client.
    private static class ServerListener extends Thread{
        //administrative stuff to keep the connection working
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        //nothing interesting here, admin stuff.
        public ServerListener(Socket socket){
            this.socket = socket;
        }

        //The method that actually runs for each client.
        public void run(){
            Integer sendMe = -1;
            try{
                //The "in" variable is data that comes from the client.
                //The "out" variable is data the server sends to the client.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while(nextCase < cases.size()){
                    synchronized(cases){
                        sendMe = cases.get(nextCase++);
                    }
                    Scanner s = new Scanner(new File("cases/case"+sendMe+".txt"));
                    String output = ""+sendMe;
                    while(s.hasNext()){
                        output+=","+s.next();
                    }
                    s.close();
                    out.println(output);

                    //could add some kind of heartbeat or check here to see if still alive
                    //just add "stillToBeCompleted" back to cases to check at end if necessary
                    String response = in.readLine();
                    if(response.equals("S")){
                        //S means success, remove from list
                        synchronized(stillToBeCompleted){
                            stillToBeCompleted.remove(sendMe);
                        }
                        //move from cases folder to finished folder
                        Files.move(Paths.get("cases/case"+sendMe+".txt"), Paths.get("finished/case"+sendMe+".txt"));
                    } else{
                        //move from cases folder to accepted folder
                        Files.move(Paths.get("cases/case"+sendMe+".txt"), Paths.get("accepted/case"+sendMe+".txt"));

                        //it failed, trouble
                        PrintWriter broken = new PrintWriter(new FileWriter("accepted/ordering"+sendMe+".txt"));
                        broken.println(response);
                        broken.close();
                    }
                }
                out.println("FINISHED");
            } catch(Exception e){
                //Something really bad happened, not much we can do about it.
                System.out.println(e);
                System.out.println(sendMe +" added back to list");
                //add to end of list... who cares when it's solved
                synchronized(cases){
                    cases.add(sendMe);
                }
            } finally{
                //The client is quitting so remove the name from the list and
                //remove the writer from the list of clients to contact.
               try{
                    socket.close();
                } catch(IOException e){
                    //Something really bad happened, not much we can do about it.
                }
            }
        }
    }
}
/**
This can be run on a windows machine if you want to automatically run the newest version of the client on that machine. It will download and run the newest version whenever it gets updated on my website.
*/

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class AutomatedUpdater{

    public static void main(String[] args) throws Exception{
        int currentInt = 0;
        Process runningProcess = null;
        PrintIt printIt = null;       
        
        while(true){
            URL url = new URL("http://faculty.cs.uwosh.edu/faculty/krohn/version.html");
            Scanner sc = new Scanner(url.openStream());
            int newInt = sc.nextInt();
            //see if there is a new version
            if(newInt!=currentInt){
                //kill old client
                if(runningProcess!=null){
                    runningProcess.destroy();
                }
                //delete old client
                File deleteMe = new File("MyClient.java");
                if(deleteMe.delete()){
                    System.out.println("get wrecked old MyClient");
                } else{
                    System.out.println("I have no idea how this happened");
                }
                //get new client
                Scanner input = new Scanner(new URL("http://faculty.cs.uwosh.edu/faculty/krohn/MyClient.java").openStream());
                PrintWriter output = new PrintWriter(new FileWriter("MyClient.java"));
                while(input.hasNextLine()){
                    output.println(input.nextLine());
                }
                output.close();
                
                System.out.println("new version!");
                currentInt = newInt;
                //compile and run new client
                runningProcess = Runtime.getRuntime().exec("javac MyClient.java");
                runningProcess.waitFor();
                runningProcess = Runtime.getRuntime().exec("java MyClient");
                BufferedReader processOutput = new BufferedReader(new InputStreamReader(runningProcess.getInputStream()));
                printIt = new PrintIt(processOutput);
                printIt.start();
            }
            sc.close();
            Thread.currentThread().sleep(60000);
        }
    }
    
    //prints output from process that is running
    private static class PrintIt extends Thread{
        private BufferedReader output;
        
        public PrintIt(BufferedReader output){
            this.output = output;
        }
        
        public void run(){
            try{
                String line = output.readLine();
                while(line!=null){
                    System.out.println(line);
                    line = output.readLine();
                }
            }catch(Exception e){
                System.out.println("bad things happened");
            }
            System.out.println("this should never print");
        }
    }
    
}
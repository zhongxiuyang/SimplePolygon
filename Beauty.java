import java.util.*;
import java.io.*;
public class Beauty{
    public static void main(String args[]){
        String input = args[0];
        List<String> elephantList = Arrays.asList(input.split(","));
        int half_size = elephantList.size() / 2;
        for (int i = 0; i < half_size; i++)
        {
            System.out.printf("%s ", elephantList.get(i));
        }
        System.out.printf("\n\n\n");
        for (int i = elephantList.size() - 1; i >= half_size; i--)
        {
            System.out.printf("%s ", elephantList.get(i));
        }
        System.out.printf("\n");
    }
}
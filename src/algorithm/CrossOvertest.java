package algorithm;

import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CrossOvertest {

    public static void main(String[] args) {

        int[] parent1 = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] parent2 = {3, 4 ,1, 2, 8, 5, 7, 6};

        crossOver(parent1, parent2);
    }


    public static int[] crossOver(int[] e1, int[] e2){
        Random rand = new Random();
        ArrayList<Integer> randomIndices = new ArrayList<>();

        System.out.println("Parent 1: " + Arrays.toString(e1));
        System.out.println("Parent 2: " + Arrays.toString(e2));

        int[] newGenom = new int[e1.length];
        while(randomIndices.size() < rand.nextInt(e1.length)){
            int idx = rand.nextInt(e1.length);
            if(!randomIndices.contains(idx)){
                randomIndices.add(idx);
            }
        }
        System.out.println("Indeces to to choose parent 1: " + randomIndices);

        for(int i = 0; i<randomIndices.size(); i++){
            newGenom[randomIndices.get(i)] = e1[randomIndices.get(i)];
        }

        System.out.println(Arrays.toString(newGenom));

        for(int idx =  0; idx<e2.length; idx++){
            if(!contains(newGenom, e2[idx])){
                if(!randomIndices.contains(idx)){
                    newGenom[idx] = e2[idx];
                    randomIndices.add(idx);
                }else {
                    int count = 0;
                    while (randomIndices.contains(count)) {
                        count++;
                    }
                    System.out.println("placing: " + e2[idx] + " at index: " + count);
                    newGenom[count] = e2[idx];
                    randomIndices.add(count);
                }
            }
        }
        System.out.println(Arrays.toString(newGenom));
        return newGenom;
    }

    public static boolean contains(int[] arr, int num){
        for(int i : arr){
            if(i == num){
                return true;
            }
        }
        return false;
    }
}

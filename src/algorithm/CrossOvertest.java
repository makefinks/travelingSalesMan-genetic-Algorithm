package algorithm;

import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class CrossOvertest {
    public static void main(String[] args) {

        int[] parent1 = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] parent2 = {3, 4 ,1, 2, 8, 5, 7, 6,};

        //crossOver(parent1, parent2);
        //splitCrossOver(parent1, parent2);
        //System.out.println(Arrays.toString(splitCrossOver(parent1, parent2)));
        shuffleMutate(parent1, 4, 7);
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
        return Arrays.stream(arr).anyMatch(x -> x==num);
    }

    public static int[] splitCrossOver(int[] e1, int[] e2){

        Random rand = new Random();
        int[] newGenom = new int[e1.length];
        ArrayList<Integer> addedNums = new ArrayList<>();
        //split e1 at random index
        int splitIdx = rand.nextInt(e1.length);
        for(int i = 0; i<splitIdx; i++){
            newGenom[i] = e1[i];
            addedNums.add(e1[i]);
        }
        //fill rest with parent2 points if they are not already contained
        for(int i = splitIdx; i<newGenom.length; i++){
            int count = 0;
            while(addedNums.contains(e2[count])){
                count++;
            }
            newGenom[i] = e2[count];
            addedNums.add(e2[count]);
        }
        return newGenom;
    }

    public static int[] paulsCross(int[] e1, int[] e2) {
        Random rand = new Random();
        ArrayList<Integer> allePunkte = new ArrayList<>();
        for (int i : e1) {
            allePunkte.add(i);
        }
        ArrayList<ArrayList<Integer>> patterns = new ArrayList<>();
        boolean raus1 = false;
        boolean raus2 = false;
        for (int i = 0; i < e1.length && !raus1; i++) {
            raus2 = false;
            for (int j = 0; j < e2.length && !raus2; j++) {
                ArrayList<Integer> pattern = new ArrayList<>();
                if (e1[i] == e2[j]) {
                    // erste zahl ist startindex
                    pattern.add(i);
                }
                while (e1[i] == e2[j]) {
                    pattern.add(e1[i]);
                    i++;
                    j++;
                    if (!(i < e1.length)) {
                        i = 0;
                        raus1 = true;
                    }
                    if (!(j < e2.length)) {
                        j = 0;
                        raus2 = true;
                    }
                }
                patterns.add(pattern);
            }
        }

        patterns.removeAll(patterns.stream().filter((x) -> x.size() < 3).collect(Collectors.toList()));
        patterns.stream().max((x, y) -> Integer.compare(x.size(), y.size()));

        int[] newGenom = new int[e1.length];
        for(int i=0;i<e1.length;i++) {
            newGenom[i]=-1;
        }
        patterns.forEach((x)-> {
            for(int i=1;i<x.size();i++) {
                if(x.get(0)+i-1<newGenom.length) {
                    newGenom[x.get(0)+i-1]=x.get(i);
                    allePunkte.remove(x.get(i));
                }
            }
        });
        Collections.shuffle(allePunkte);
        for(int i=0;i<e1.length;i++) {
            if(newGenom[i]==-1) {
                newGenom[i]=allePunkte.remove(0);
            }
        }
        return newGenom;
    }

    public static int[] shuffleMutate(int[] element, int minChange, int maxChange){

        System.out.println(Arrays.toString(element));

        Random rand = new Random();
        int bound1;
        int bound2;

        do{
            bound1 = rand.nextInt(element.length);
            bound2 = rand.nextInt(element.length);
        }while(bound2-bound1 < minChange || bound2-bound1 > maxChange);

        //get snipped
        ArrayList<Integer> snippet = new ArrayList<>();
        for(int i = bound1; i<bound2; i++){
            snippet.add(element[i]);
        }
        //shuffle snipped
        Collections.shuffle(snippet);
        for(int i = bound1, x = 0; i<bound2; i++, x++){
            element[i] = snippet.get(x);
        }
        return element;
    }
}

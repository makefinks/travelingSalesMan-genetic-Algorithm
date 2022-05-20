package algorithm;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Studie {

    public static double[][] nodeDistances;
    public static ArrayList<Point> points;
    public static ArrayList<Point> pointBestRoute;
    public static ArrayList<Point> pointFirstRoute;
    public static ArrayList<Element> currentGen;
    public static int bestCaseGen;
    public static Element bestCase;
    public static Element firstCase;
    public static double worstFitness;
    public static ConcurrentHashMap<Integer, Double> fitnessHistory;
    public static ArrayList<Integer> bestCaseGens;
    public static Random random;
    static int gencount;
    static int fitCalcCount = 0;

    //***********************************************************
    //PARAMETERS FOR OPTIMIZING THE ALGORITHM
    //America best Setting:   genzero = 400; elite 300;
    static String input = "inputAlt";   //specifies the file to be used as input
    static int genZero = 400;   //specifies how many permutations are used in gen zero
    static int elite = 10;     //specifies how many parents are chosen after sorting
    static int max = 100000;     //specifies how many generations will be created
    static int mutations = 2;   //specifies how many Elements are switched while mutating
    static int mutationProbability = 100;        //the probability for mutation
    static int crossoverProbability = 100;       //the probability for crossover of two parents
    static int fitCap = 10000000;
    //***********************************************************

    //

    public static void main(String[] args) throws FileNotFoundException {

        random = new Random();
        bestCaseGens = new ArrayList<>();

        createNodes(1000, 10000, "input");

        /*
        Specify input file with format:

                [num] [x] [y]
                 1 123 512
                 2 123 412
                 3 623 123
                 ...
                 ..
                 .
         */

        //read input file and create List of Points
        Scanner sc = new Scanner(new File(input));
        points = new ArrayList<>();
        while (sc.hasNext()) {
            int müll = sc.nextInt();
            int x = sc.nextInt();
            int y = sc.nextInt();
            points.add(new Point(x, y));
        }

        //Calculate and display the distance Matrix for all points
        nodeDistances = new double[points.size()][points.size()];
        for (int i = 0; i < points.size(); i++) {
            for (int x = 0; x < points.size(); x++) {
                nodeDistances[i][x] = points.get(i).distance(points.get(x));
                //System.out.printf("%-15f", nodeDistances[i][x]);
            }
            //System.out.println();
        }

        //create Generation Zero and sort depending on Fitness
        currentGen = createGeneration();
        bestCase = currentGen.get(0);
        firstCase = currentGen.get(0);
        worstFitness = currentGen.get(0).calcFitness(nodeDistances, false);

        pointFirstRoute = new ArrayList<>();
        for (int i = 0; i < firstCase.points.length; i++) {
            pointFirstRoute.add(points.get(firstCase.points[i]));
        }
        pointFirstRoute.add(points.get(firstCase.points[0]));

        //JFrame
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(new Dimension(1200, 700));
        frame.setLocationRelativeTo(null);

        JPanel paintframe = new JPanel();
        frame.add(paintframe, BorderLayout.CENTER);

        JPanel rightGraphs = new JPanel();
        rightGraphs.setLayout(new GridLayout(2, 1));

        JPanel fitGraph = new JPanel();
        rightGraphs.add(fitGraph, 0);

        JPanel infoPanel = new JPanel();
        rightGraphs.add(infoPanel, 1);

        frame.add(rightGraphs, BorderLayout.EAST);

        fitnessHistory = new ConcurrentHashMap<>();

        for (gencount = 0; gencount < max; gencount++) {

            //VISUALS
            rightGraphs.remove(0);
            rightGraphs.add(new fitGraph(), 0);
            rightGraphs.remove(1);
            rightGraphs.add(new InfoPanel(), 1);
            frame.validate();


            currentGen.sort((x, y) -> Double.compare(x.fitness, y.fitness));
            //currentGen.stream().forEach(x -> System.out.println(x + " fitness: " + x.calcFitness(nodeDistances)));

            fitnessHistory.put(gencount, currentGen.get(0).calcFitness(nodeDistances, false));

            //check if the top element in the current Generation is the new bestcase
            if (currentGen.get(0).calcFitness(nodeDistances, false) < bestCase.calcFitness(nodeDistances, false)) {
                bestCase = currentGen.get(0);
                bestCaseGen = gencount;
                bestCaseGens.add(gencount);

                System.out.println("new bestcase:  gen-> " + gencount + " fit-> " + bestCase.calcFitness(nodeDistances, false));

                //MID-VISUALS:
                pointBestRoute = new ArrayList<>();
                for (int i = 0; i < bestCase.points.length; i++) {
                    Point p = points.get(bestCase.points[i]);
                    pointBestRoute.add(p);
                }
                pointBestRoute.add(points.get(bestCase.points[0]));

                //frame.remove(paintframe);
                paintFrame vis = new paintFrame(points, pointBestRoute, false);
                frame.add(vis, BorderLayout.CENTER);
                frame.validate();

            }

            //create new Generation with elite Parents
            ArrayList<Element> eliteSelection = (ArrayList<Element>) currentGen.stream().limit(elite).collect(Collectors.toList());
            ArrayList<Element> newGen = new ArrayList<>();
            //  currentGen.stream().limit(20).forEach(newGen::add);
            //start crossover of elite generation
            int mutationCount =  0;
            for (int i = 0; i < eliteSelection.size() - 1; i++) {
                //MARK: Crossover
                double crossProb = (double) crossoverProbability / (double) 100;
                if (Math.random() > 1 - crossProb) {
                    //Element crossed = crossOver(eliteSelection.get(i), eliteSelection.get(i + 1));
                    Element crossed = splitCrossOver(eliteSelection.get(i), eliteSelection.get(i + 1));
                    newGen.add(crossed);
                    //MARK: Mutation
                    double mutProb = (double) mutationProbability / (double) 100;
                    if (Math.random() > 1 - mutProb) {
                        newGen.add(mutate(crossed));
                        mutationCount++;
                    }
                }
            }
            //Stop the algorithm if the specified fitness calculation Limit is reached
                if(fitCalcCount > fitCap){
                    fitCalcCount = fitCap;
                    break;
                }
                currentGen = newGen;
        }
        //Summary
        System.out.println("-".repeat(50));
        System.out.printf("%-10s |%20f | %n", "Best Case", bestCase.calcFitness(nodeDistances, false));
        System.out.printf("%-10s |%20f | %n", "First Case", firstCase.calcFitness(nodeDistances, false));

        System.out.println("-".repeat(35));
        double difference = -(bestCase.calcFitness(nodeDistances, false) - firstCase.calcFitness(nodeDistances, false)) / firstCase.calcFitness(nodeDistances, false);
        System.out.printf("Verbesserung %18f %% %n", difference * 100);
        System.out.println("-".repeat(50));

        //Summary visualization:
        pointBestRoute = new ArrayList<>();
        for (int i = 0; i < bestCase.points.length; i++) {
            pointBestRoute.add(points.get(bestCase.points[i]));
        }
        pointBestRoute.add(points.get(bestCase.points[0]));

        frame.add(new paintFrame(points, pointBestRoute, true), BorderLayout.CENTER);
        frame.validate();
    }

    public static ArrayList<Element> createGeneration() {
        ArrayList<Element> elements = new ArrayList<Element>();
        while (elements.size() < genZero) {
            Element n = perm();
            if (!elements.contains(n)) {
                elements.add(n);
            }
        }
        return elements;
    }

    public static Element perm() {
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < nodeDistances.length; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums);
        return new Element(nums.stream().mapToInt(i -> i).toArray());
    }

    public static Element crossOver(Element e1, Element e2) {

/*      THIS METHOD WORKS BETTER THAN MERGING TWO PARENTS
        int[] newGenom = new int[e1.points.length];
        Random rand = new Random();

        ArrayList<Integer> rand1 = new ArrayList<Integer>();

        //How many elements are taken from parent1
        int amount = rand.nextInt(e1.points.length);
        //fill with valid indices
        while (rand1.size() < amount) {
            int next = rand.nextInt(e1.points.length);
            if (!rand1.contains(next)) {
                rand1.add(next);
            }
        }
        //start copying indices from parent1 to child
        for (int ix = 0; ix < rand1.size(); ix++) {
            newGenom[rand1.get(ix)] = e1.points[rand1.get(ix)];
        }

        //start copying indices from parent2 to child

        for (int ix = 0; ix < newGenom.length; ix++) {
            if (!rand1.contains(ix)) {
                newGenom[ix] = e1.points[ix];
            }
        }

        return new Element(newGenom);
 */


        //Creates random indices only containing an index once
        ArrayList<Integer> randomIndices = new ArrayList<>();
        int[] newGenom = new int[e1.points.length];
        int randAmount = 0;
        while(randAmount < e1.points.length/2){
            randAmount = random.nextInt(e1.points.length);
        }
        while(randomIndices.size() < randAmount){
            int idx = random.nextInt(e1.points.length);
            if(!randomIndices.contains(idx)){
                randomIndices.add(idx);
            }
        }
       // System.out.println("Indeces to to choose parent 1: " + randomIndices);

        //Put the numbers at the indices in parent1 at the same index in the new Genom
        for(int i = 0; i<randomIndices.size(); i++){
            newGenom[randomIndices.get(i)] = e1.points[randomIndices.get(i)];
        }

        //While iterating over parent 2
        for(int idx =  0; idx<e2.points.length; idx++){
            //if the number at idx of parent2 is already contained in newGenom do nothing
            if(!contains(newGenom, e2.points[idx])){
                //if idx is not contained in randomIndices put the number at index idx of parent2 at the same index in the new Genom
                if(!randomIndices.contains(idx)){
                    newGenom[idx] = e2.points[idx];
                    //Because we put something at idx in newGenom no other number can be put there anymore
                    //To ensure that add idx to randomIndices
                    randomIndices.add(idx);
                //if idx is contained in randomIndices iterate over newGenom and find an index that is not already used
                }else {
                    int count = 0;
                    while (randomIndices.contains(count)) {
                        count++;
                    }
                    //Put the number idx of parent2 at the newly located index count in new Genom
                    newGenom[count] = e2.points[idx];
                    //No other number can be placed at count anymore
                    //To ensure that add count to randomIndices
                    randomIndices.add(count);
                }
            }
        }
        //System.out.println(Arrays.toString(newGenom));
        return new Element(newGenom);
    }

    public static Element splitCrossOver(Element e1, Element e2){

        Random rand = new Random();
        int[] newGenom = new int[e1.points.length];
        ArrayList<Integer> addedNums = new ArrayList<>();
        //split e1 at random index
        int splitIdx = rand.nextInt(e1.points.length);
        for(int i = 0; i<splitIdx; i++){
            newGenom[i] = e1.points[i];
            addedNums.add(e1.points[i]);
        }
        //fill rest with parent2 points if they are not already contained
        for(int i = splitIdx; i<newGenom.length; i++){
            int count = 0;
            while(addedNums.contains(e2.points[count])){
                count++;
            }
            newGenom[i] = e2.points[count];
            addedNums.add(e2.points[count]);
        }
        return new Element(newGenom);
    }

    public static boolean contains(int[] arr, int num){
        for(int i : arr){
            if(i == num){
                return true;
            }
        }
        return false;
    }


    public static Element mutate(Element e) {

        int[] eArr = Arrays.copyOf(e.points, e.points.length);

        for(int i = 0; i<mutations; i++) {
            int src = random.nextInt(eArr.length);
            int target = 0;
            do {
                target = random.nextInt(eArr.length);
            } while (target == src);

            int temp = eArr[target];
            eArr[target] = eArr[src];
            eArr[src] = temp;
        }

        Element mutated = new Element(eArr);

        if(Math.random() > 2) {
            //eliminate longest distances
            Point p1 = null;
            Point p2 = null;
            double maxDist = 0;

            ArrayList<Point> list = new ArrayList<>();
            for (int i = 0; i < mutated.points.length; i++) {
                list.add(points.get(mutated.points[i]));
            }

            for (int i = 0; i < list.size() - 1; i++) {
                double distPoints = list.get(i).distance(list.get(i + 1));
                if (distPoints > maxDist) {
                    maxDist = distPoints;
                    p1 = list.get(i);
                    p2 = list.get(i + 1);
                }
            }

            if (random.nextBoolean()) {
                int index = random.nextInt(points.size());
                list.set(list.indexOf(p1), list.get(index));
                list.set(index, p1);
            } else {
                int index = random.nextInt(points.size());
                int index2 = random.nextInt(points.size());
                list.set(list.indexOf(p1), list.get(index));
                list.set(index, p1);
                list.set(list.indexOf(p2), list.get(index2));
                list.set(index2, p2);
            }

            for (int i = 0; i < mutated.points.length; i++) {
                mutated.points[i] = points.indexOf(list.get(i));
            }
        }
        return mutated;
    }

    public static void createNodes(int n, int bound, String path) {

        try (FileWriter fw = new FileWriter(path)) {
            ArrayList<Point> nodes = new ArrayList<>();
            Random rand = new Random();
            for (int i = 0; i < n; i++) {
                Point p = new Point(0, 0);
                do {
                    int x = rand.nextInt(bound);
                    int y = rand.nextInt(bound);
                    p = new Point(x, y);
                } while (nodes.contains(p));
                nodes.add(p);
            }
            nodes.forEach((x) -> {
                try {
                    fw.write(nodes.indexOf(x) + " " + x.x + " " + x.y + "\n");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}

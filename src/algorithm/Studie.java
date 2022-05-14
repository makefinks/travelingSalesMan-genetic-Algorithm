package algorithm;

import com.sun.jdi.connect.Connector;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.spi.BreakIteratorProvider;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;


public class Studie{

    public static double[][] nodeDistances;
    public static ArrayList<Point> points;
    public static ArrayList<Point> pointBestRoute;
    public static ArrayList<Point> pointFirstRoute;
    public static int bestCaseGen;

    public static Random random;
    Element bestRoute = null;
    static int currentGen = 400;
    static int elite = 200;
    static int max = 10_0000;

    static int gencount;

    public static void main(String[] args) throws FileNotFoundException {

        random = new Random();

        createNodes(50, 10000, "input");

        String input = "inputAlt";
        Scanner sc = new Scanner(new File(input));
        points = new ArrayList<>();
        while (sc.hasNext()) {
            int müll = sc.nextInt();
            int x = sc.nextInt();
            int y = sc.nextInt();
            points.add(new Point(x,y));
        }
        nodeDistances = new double[points.size()][points.size()];

        for (int i = 0; i < points.size(); i++) {
            for (int x = 0; x < points.size(); x++) {
                nodeDistances[i][x] = points.get(i).distance(points.get(x));
                System.out.printf("%-15f", nodeDistances[i][x]);
            }
            System.out.println();
        }

        Element bestCase;
        Element firstCase;

        //create Generation Zero and sort after fitness
        ArrayList<Element> currentGen = createGeneration();
        bestCase = currentGen.get(0);
        firstCase = currentGen.get(0);

        pointFirstRoute = new ArrayList<>();
        for (int i = 0; i < firstCase.points.length; i++) {
            pointFirstRoute.add(points.get(firstCase.points[i]));
        }
        pointFirstRoute.add(points.get(firstCase.points[0]));


        //Jframe
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(new Dimension(900, 700));

        JPanel paintframe = new JPanel();
        frame.add(paintframe, BorderLayout.CENTER);


        for (gencount = 0; gencount < max; gencount++) {

            //System.out.println("-".repeat(40) + "Gen"+gencount + "-".repeat(40));
            currentGen.sort((x, y) -> Double.compare(x.fitness, y.fitness));
            //currentGen.stream().forEach(x -> System.out.println(x + " fitness: " + x.calcFitness(nodeDistances)));

            if(currentGen.get(0).calcFitness(nodeDistances) < bestCase.calcFitness(nodeDistances)){
                bestCase = currentGen.get(0);
                bestCaseGen = gencount;
                System.out.println("new bestcase:  gen-> " + gencount + " fit-> " + bestCase.calcFitness(nodeDistances));


                    //visualization:
                    pointBestRoute = new ArrayList<>();
                    for (int i = 0; i < bestCase.points.length; i++) {
                        pointBestRoute.add(points.get(bestCase.points[i]));
                    }
                    pointBestRoute.add(points.get(bestCase.points[0]));


                    frame.remove(paintframe);
                    paintFrame vis = new paintFrame(points, pointBestRoute, false);
                    frame.add(vis, BorderLayout.CENTER);
                    frame.validate();


            }

            //create new Generation with elite Parents
            ArrayList<Element> eliteSelection = (ArrayList<Element>) currentGen.stream().limit(elite).collect(Collectors.toList());
            ArrayList<Element> newGen = new ArrayList<>();
            //start crossover of elite generation
            int mutationCount = 0;
            for(int i = 0; i<eliteSelection.size()-1; i++){
                Element crossed = crossOver(eliteSelection.get(i), eliteSelection.get(i+1));
                newGen.add(crossed);

                //mutated child

                if(Math.random() > 0.0){
                    newGen.add(mutate(crossed));
                    mutationCount++;
                }

            }
            //System.out.println("Mutations: " + mutationCount);
            currentGen = newGen;
        }

        //Summary
        System.out.println("-".repeat(50));
        System.out.printf("%-10s |%20f | %n","Best Case", bestCase.calcFitness(nodeDistances));
        System.out.printf("%-10s |%20f | %n","First Case", firstCase.calcFitness(nodeDistances));
        System.out.println("-".repeat(50));


        //visualization:
        pointBestRoute = new ArrayList<>();
        for(int i = 0; i<bestCase.points.length; i++){
            pointBestRoute.add(points.get(bestCase.points[i]));
        }
        pointBestRoute.add(points.get(bestCase.points[0]));

        mainframe visualizer = new mainframe();



    }

    public static ArrayList<Element> createGeneration() {
        ArrayList<Element> elements = new ArrayList<Element>();
        while (elements.size() < currentGen) {
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

        //System.out.println("Crossover");
        //System.out.println(e1);
        //System.out.println(e2);


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
		for(int ix = 0; ix<rand1.size(); ix++){
			newGenom[rand1.get(ix)] = e1.points[rand1.get(ix)];
		}
       // System.out.println("copying indeces: " + rand1.toString());
       // System.out.println(Arrays.toString(newGenom));

		//same process for parent2
		//ArrayList<Integer> rand2 = new ArrayList<Integer>();
		//int amount2 = e1.points.length-amount;

		//fill with valid indices
		/*
		while (rand2.size() < amount2) {

			int next = rand.nextInt(e1.points.length);
			if (!rand2.contains(next) && !rand1.contains(next)) {
				rand2.add(next);
			}
		}
		 */


		//start copying indices from parent2 to child
            for(int ix = 0; ix< newGenom.length; ix++){
                if(!rand1.contains(ix)){
                    newGenom[ix] = e1.points[ix];
                }

		}

       // System.out.println(Arrays.toString(newGenom));

        return new Element(newGenom);
    }

    public static Element mutate(Element e){

        int[] eArr = Arrays.copyOf(e.points, e.points.length);

        int src = random.nextInt(eArr.length);
        int target = 0;
        do {
            target = random.nextInt(eArr.length);
        }while (target == src);



        int temp = eArr[target];
        eArr[target] = eArr[src];
        eArr[src] = temp;

        return new Element(eArr);
    }

    public static void createNodes(int n,int bound, String path) {

        try(FileWriter fw = new FileWriter(path)) {
            ArrayList<Point> nodes = new ArrayList<>();
            Random rand = new Random();
            for(int i=0;i<n;i++) {
                Point p= new Point (0,0);
                do {
                    int x=rand.nextInt(bound);
                    int y=rand.nextInt(bound);
                    p=new Point(x,y);
                }while(nodes.contains(p));
                nodes.add(p);
            }
            nodes.forEach((x)-> {
                try {
                    fw.write(x.x+" "+x.y+"\n");
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

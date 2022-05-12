package algorithm;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;


public class Studie {

    public static double[][] nodeDistances;

    Element bestRoute = null;
    static int currentGen = 50;
    static int elite = 10;
    static int max = 10;

    public static void main(String[] args) throws FileNotFoundException {

        String input = "input";
        Scanner sc = new Scanner(new File(input));
        ArrayList<Point> points = new ArrayList<>();
        while (sc.hasNext()) {
            String line = sc.nextLine();
            //line.line.matches("=[0-9]*");
            String[] splitted = line.split(",|=");
            System.out.println(Arrays.toString(splitted));
            int x = Integer.parseInt(splitted[1]);
            int y = Integer.parseInt(splitted[3]);
            points.add(new Point(x, y));
        }
        nodeDistances = new double[points.size()][points.size()];

        for (int i = 0; i < points.size(); i++) {
            for (int x = 0; x < points.size(); x++) {
                nodeDistances[i][x] = points.get(i).distance(points.get(x));
                System.out.printf("%-15f", nodeDistances[i][x]);
            }
            System.out.println();
        }
        //create Generation Zero and sort after fitness
        ArrayList<Element> currentGen = createGeneration();
        for (int gencount = 0; gencount < max; gencount++) {
            currentGen.sort((x, y) -> Double.compare(x.fitness, y.fitness));
            currentGen.stream().forEach(x -> System.out.println(x + " fitness: " + x.calcFitness(nodeDistances)));

            //create new Generation with elite Parents
            ArrayList<Element> nextGen = new ArrayList<>();


        }


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
			newGenom[ix] = e1.points[rand1.get(ix)];
		}

		//same process for parent2
		ArrayList<Integer> rand2 = new ArrayList<Integer>();
		int amount2 = rand.nextInt(e1.points.length);
		//fill with valid indices
		while (rand1.size() < amount) {
			int next = rand.nextInt(e1.points.length);
			if (!rand1.contains(next)) {
				rand1.add(next);
			}
		}
		//start copying indices from parent2 to child
		for(int ix = 0; ix<rand1.size(); ix++){
			newGenom[ix] = e1.points[rand1.get(ix)];
		}




        return null;
    }

}

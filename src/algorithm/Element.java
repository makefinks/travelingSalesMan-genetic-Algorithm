package algorithm;
import java.util.Arrays;


public class Element {

	int[] points;
	double fitness;
	
	public Element(int[] points) {
		this.points = points;
		fitness = calcFitness(Studie.nodeDistances);
	} 	
	
	
	public double calcFitness(double[][] distances) {
		double fitness = 0;
		for(int i = 0; i<points.length-1; i++) {
			fitness += distances[points[i]][points[i+1]];
		}
		fitness += distances[points[points.length-1]][points[0]];
		return fitness;
	}

	public double getFitness(){
		return fitness;
	}

	@Override
	public boolean equals(Object obj) {


		Element other = (Element) obj;

		/*
		System.out.println(Arrays.toString(this.points));
		System.out.println(Arrays.toString(other.points));
		System.out.println(Arrays.equals(points, other.points));
		 */

		return Arrays.equals(points, other.points);
	}

	@Override
	public String toString() {
		return "Element [points=" + Arrays.toString(points) + "]";
	}
	
	
}

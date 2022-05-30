package algorithm;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Element {
	int[] points;
	ArrayList<Point> pointsL;
	double fitness;
	public Element(int[] points) {
		this.points = points;
		fitness = calcFitness(Studie.nodeDistances, true);
	}

	public double calcFitness(double[][] distances, boolean count) {
		double fitness = 0;
		for(int i = 0; i<points.length-1; i++) {
			fitness += distances[points[i]][points[i+1]];
		}
		fitness += distances[points[points.length-1]][points[0]];
		if(count){	Studie.fitCalcCount++;}
		return fitness;
	}
	public double getFitness(){
		return fitness;
	}
	@Override
	public boolean equals(Object obj) {
		Element other = (Element) obj;
		return Arrays.equals(points, other.points);
	}
	@Override
	public String toString() {
		return "Element [points=" + Arrays.toString(points) + "]";
	}
}

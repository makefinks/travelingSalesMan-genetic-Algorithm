package algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class mainframe {


        public mainframe() {

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame wind = new JFrame();
                    wind.setVisible(true);
                    wind.setSize(new Dimension(1000, 1000));
                    wind.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    wind.setLayout(new BorderLayout());

                    Random rand = new Random();

                    /*
                    ArrayList<Point> testpoints = new ArrayList<>();
                    for (int i = 0; i < 1000; i++) {
                        testpoints.add(new Point(rand.nextInt(1000), rand.nextInt(1000)));
                    }
                     */
                    paintFrame paintf = new paintFrame(Studie.points, Studie.pointBestRoute, true);
                    wind.add(paintf, BorderLayout.CENTER);
                }
            });

        }



    public static ArrayList<Point> createRoute(ArrayList<Point> points){
        Random rand = new Random();
        ArrayList<Point> route = new ArrayList<>();
        while(route.size() < 20){
            Point randP = points.get(rand.nextInt(points.size()));
            if(!route.contains(randP)){
                route.add(randP);
            }

        }
        route.add(route.get(0));
        return route;
    }

}

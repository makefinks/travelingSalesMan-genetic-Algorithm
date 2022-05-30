package algorithm;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class visualThread implements Runnable{


    @Override
    public void run() {


        ArrayList<Point> pointBestRoute = new ArrayList<>();
        for (int i = 0; i < Studie.bestCase.points.length; i++) {
            Point p = Studie.points.get(Studie.bestCase.points[i]);
            pointBestRoute.add(p);
        }
        pointBestRoute.add(Studie.points.get(Studie.bestCase.points[0]));
        //frame.remove(paintframe);
        paintFrame vis = new paintFrame(Studie.points, pointBestRoute, false);
        Studie.frame.add(vis, BorderLayout.CENTER);
        Studie.frame.validate();

    }
}

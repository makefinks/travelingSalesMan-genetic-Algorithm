package algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class fitGraph extends JPanel{

    public fitGraph() {
        setVisible(true);
        setPreferredSize(new Dimension(300, 100));
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        super.paintComponent(g);

        double panelHeight = getHeight();
        double panelWidth = getWidth();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle2D.Double background = new Rectangle2D.Double(0,0, panelWidth, panelHeight);
        g2d.setColor(Color.BLACK);
        g2d.fill(background);

        //Units based on Screen size

       double ex = panelWidth / Studie.max;
       double ey = panelHeight / Studie.worstFitness;
       g2d.setColor(Color.YELLOW);


        HashMap<Integer, Double> copy = (HashMap<Integer, Double>) Studie.fitnessHistory.clone();

       for(Integer gen : copy.keySet()){
           double x = ex*gen;
           double y = panelHeight-ey*Studie.fitnessHistory.get(gen);
           Ellipse2D.Double o = new Ellipse2D.Double(x, y, 2, 2);
           g2d.fill(o);

           if(gen == copy.size()-1){
               g2d.setFont(new Font("Arial", Font.PLAIN, 16));
               g2d.drawString(Double.toString(copy.get(gen)), (int)x, (int)y);
           }

       }










    }


}

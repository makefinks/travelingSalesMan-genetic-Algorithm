package algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class InfoPanel extends JPanel {

    public void JPanel(){

    }


    @Override
    public void paintComponent(Graphics g){

        double screenWidth = getWidth();
        double screenHeight = getHeight();

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        Rectangle2D.Double background = new Rectangle2D.Double(0,0, screenWidth, screenHeight);
        g2d.fill(background);

        Rectangle2D.Double frame = new Rectangle2D.Double(20, 10, screenWidth-30, screenHeight-30);
        g2d.setColor(Color.BLUE);
        g2d.draw(frame);

        g2d.setColor(Color.WHITE);
        g2d.drawString("current Gen:    " + Studie.gencount + "/" + Studie.max, 30, 30);
        g2d.drawString("best Gen: \t" + Studie.bestCaseGen, 30, 50);

        double improvTotal = -(Studie.bestCase.calcFitness(Studie.nodeDistances) - Studie.firstCase.calcFitness(Studie.nodeDistances)) / Studie.firstCase.calcFitness(Studie.nodeDistances);
        g2d.drawString("improvement total: " + Math.round(improvTotal*100) + "%", 30 , 70);


    }
}

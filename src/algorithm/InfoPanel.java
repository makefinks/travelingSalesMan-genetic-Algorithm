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
        String strCurrentGen = String.format("current Gen: %20d / %d", Studie.gencount, Studie.max);
        g2d.drawString(strCurrentGen, 30, 30);

        String strBestGen = String.format("best gen: %25d", Studie.bestCaseGen);
        g2d.drawString(strBestGen, 30, 50);

        double improvTotal = -(Studie.bestCase.calcFitness(Studie.nodeDistances, false) - Studie.firstCase.calcFitness(Studie.nodeDistances, false)) / Studie.firstCase.calcFitness(Studie.nodeDistances, false);
        g2d.drawString("improvement total: " + String.format("%10.2f%%", improvTotal*100), 30 , 70);

        try{
        double improvLast = ((Studie.fitnessHistory.get(Studie.bestCaseGens.get(Studie.bestCaseGens.size()-2))
                - Studie.bestCase.calcFitness(Studie.nodeDistances, false))
                / Studie.fitnessHistory.get(Studie.bestCaseGens.get(Studie.bestCaseGens.size()-2)));

        g2d.drawString("improvement last: " + String.format("%14.5f%%", improvLast * 100), 30, 90);
        }catch (Exception e){

        }

        g2d.drawString("fitCalcCount: " + Studie.fitCalcCount, 30, 110);
        g2d.drawString("gen size: " + Studie.currentGen.size(), 30, 130);
        g2d.drawString(String.format("First Case: %15f", Studie.firstCase.calcFitness(Studie.nodeDistances, false)), 60, 150);
        g2d.drawString(String.format("Best Case: %15f", Studie.bestCase.calcFitness(Studie.nodeDistances, false)), 60, 170);


    }
}

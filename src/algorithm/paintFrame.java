package algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class paintFrame extends JPanel {

    private ArrayList<Point> points;
    private ArrayList<Point> route;
    double panelHeight;
    double panelWidth;
    boolean drawFirst;



    paintFrame(ArrayList<Point> points, ArrayList<Point> route, boolean drawFirst){
        this.points = points;
        this.route = route;
        this.drawFirst = drawFirst;
        setVisible(true);
        setPreferredSize(new Dimension(100, 100));
    }

    @Override
    public void paintComponent(Graphics g) {

        panelHeight = getHeight();
        panelWidth = getWidth();

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle2D.Double background = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.BLACK);
        g2d.fill(background);

        g2d.setColor(Color.YELLOW);

        int largestx = 0;
        int largesty = 0;

        double ex = 0;
        double ey = 0;

        for (Point s : points) {
            if (s.x > largestx) {
                largestx = s.x;
            }
            if (s.y > largesty) {
                largesty = s.y;
            }
        }

        ex = panelWidth / largestx;
        ey = panelHeight / largesty;

        for (Point p : points) {
            double relativex = ex * p.x;
            double relativey = ey * p.y;

            Ellipse2D.Double node = new Ellipse2D.Double(relativex, relativey, 5, 5);
            Ellipse2D.Double startnode = new Ellipse2D.Double(relativex, relativey, 10, 10);

            if (p.equals(route.get(0))) {
                g2d.setColor(Color.RED);
                g2d.draw(startnode);
                g2d.fill(startnode);
            } else {
                g2d.setColor(Color.YELLOW);
                g2d.draw(node);
            }
            g2d.drawString(Integer.toString(points.indexOf(p)), (int) relativex, (int) relativey);
            //g2d.drawOval((int)relativex,(int)relativey, 5, 5);

        }
        g2d.drawString(Integer.toString(Studie.bestCaseGen), 0, 10);
        for (int i = 0; i < route.size() - 1; i++) {

            Point p1 = route.get(i);
            Point p2 = route.get(i + 1);

            double x1 = ex * p1.x + 2.5;
            double y1 = ey * p1.y + 2.5;
            double x2 = ex * p2.x + 2.5;
            double y2 = ey * p2.y + 2.5;

            g2d.setColor(Color.CYAN);
            Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
            g2d.draw(line);
            // g2d.drawLine((int)Math.round(x1+2.5), (int)Math.round(y1+2.5), (int)Math.round(x2+2.5), (int)Math.round(y2+2.5));
        }
        if(drawFirst){
            for (int i = 0; i < Studie.pointFirstRoute.size() - 1; i++) {

                Point p1 = Studie.pointFirstRoute.get(i);
                Point p2 = Studie.pointFirstRoute.get(i + 1);

                double x1 = ex * p1.x + 2.5;
                double y1 = ey * p1.y + 2.5;
                double x2 = ex * p2.x + 2.5;
                double y2 = ey * p2.y + 2.5;

                g2d.setColor(new Color
                        (255,0,0, 75));

                Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
                g2d.draw(line);
                g2d.setFont(new Font("Arial", Font.PLAIN, 2));
                g2d.drawString(Integer.toString(Studie.bestCaseGen), 0, 10);
                // g2d.drawLine((int)Math.round(x1+2.5), (int)Math.round(y1+2.5), (int)Math.round(x2+2.5), (int)Math.round(y2+2.5));
            }
        }
        //repaint();
        }
    }


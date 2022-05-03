/*
    SpaceRangers.java
    Herman Lin and Devin Zhu

    This class creates the universe that players interact with.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceRangers {

    private JFrame jf;
    private Universe jp;

    SpaceRangers() {
        jf = new JFrame("Space Rangers");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(800, 800);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
        
        jp = new Universe();
        jf.add(jp);

        new Thread() {
            public void run() {
                while(true) {
                    jp.repaint();
                }
            }
        }.start();

        jf.revalidate();
    }

    public static void main(String[] args) {
        SpaceRangers sr = new SpaceRangers();
    }
}

class Universe extends JPanel {

    Player player1;

    Universe() {
        super();
        setBackground(Color.BLACK);

        player1 = new Player();

        new Thread() {
            public void run() {
                while(true) {
                    try {
                        sleep(1000);
                        player1.computeCentroid(4);
                        player1.rotateShip(Math.toRadians(-90), 4);
                    } catch (InterruptedException e) {}
                }
            }
        }.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(player1.getColor());
        g.drawPolygon(player1.getShip());
    }
}
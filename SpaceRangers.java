/*
    SpaceRangers.java
    Herman Lin and Devin Zhu

    This class creates the universe that players interact with.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceRangers extends JFrame {

    private Universe jp;

    SpaceRangers() {
        super("Space Rangers");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        jp = new Universe();
        this.add(jp);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    jp.spaceship.computeCentroid();
                    jp.spaceship.rotateShip(Math.toRadians(5));
                }
            }
        });

        new Thread() {
            public void run() {
                while(true) {
                    jp.repaint();
                }
            }
        }.start();

        this.revalidate();
    }

    public static void main(String[] args) {
        SpaceRangers sr = new SpaceRangers();
    }
}

class Universe extends JPanel {

    Ship spaceship;

    Universe() {
        super();
        setBackground(Color.BLACK);

        spaceship = new Ship();


/*        new Thread() {
            public void run() {
                while(true) {
                    try {
                        sleep(1000);
                        player1.computeCentroid();
                        player1.rotateShip(Math.toRadians(-90));
                    } catch (InterruptedException e) {}
                }
            }
        }.start();
*/
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(spaceship.getColor());
        g.drawPolygon(spaceship.getShip());
    }
}
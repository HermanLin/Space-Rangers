import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The SpaceRangers class is the driver class for the entire game.
 * It creates the JFrame that holds all the components as well
 * as listens to the key inputs from the player.
 * 
 * @author Herman Lin
 * @author Devin Zhu
 */

public class SpaceRangers extends JFrame {

    private Universe universe;

    /**
     * Constructor for creating the JFrame and initializing the 
     * JPanel that is responsible for drawing components.
     */
    SpaceRangers() {
        super("Space Rangers");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        universe = new Universe();
        this.add(universe);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    universe.spaceship.computeCentroid();
                    universe.spaceship.rotateLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    universe.spaceship.computeCentroid();
                    universe.spaceship.rotateRight();
                }
            }
        });

        new Thread() {
            public void run() {
                while(true) { universe.repaint(); }
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

/*
        new Thread() {
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
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(spaceship.getColor());
        g2d.rotate(Math.toRadians(spaceship.getFacing()), 
                   spaceship.getCentroidX(), 
                   spaceship.getCentroidY());
        g2d.drawPolygon(spaceship.getShip());
    }
}
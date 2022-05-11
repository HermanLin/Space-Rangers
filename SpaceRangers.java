import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The SpaceRangers class is the driver class for the entire game.
 * It creates the JFrame that holds all the components as well
 * as listens to the key inputs from the player.
 * 
 * @author Herman Lin
 * @author Devin Zhu
 */

public class SpaceRangers extends JFrame {

    public static int SCREEN_WIDTH = 800;
    public static int SCREEN_HEIGHT = 800;
    private Universe universe;

    /**
     * Constructor for creating the JFrame and initializing the 
     * JPanel that is responsible for drawing components.
     */
    SpaceRangers() {
        super("Space Rangers");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        universe = new Universe();
        this.add(universe);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    //universe.spaceship.computeCentroid();
                    universe.spaceship.rotateLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    //universe.spaceship.computeCentroid();
                    universe.spaceship.rotateRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    universe.spaceship.increaseVelocity();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    universe.spaceship.decreaseVelocity();
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    universe.spaceship.computeTranslatedCentroid();
                    universe.ammunition.add(new Projectile(universe.spaceship.getColor(), 
                                                           universe.spaceship.getFacing(), 
                                                           universe.spaceship.getTranslatedCentroidX(), 
                                                           universe.spaceship.getTranslatedCentroidY()));
                }
            }
        });

        // Thread that continually updates the Universe
        new Thread() {
            public void run() {
                while(true) { 
                    try {
                        sleep(10); // delay for the whole universe, wow!
                    } catch (InterruptedException e) {}
                    universe.repaint(); 
                }
            }
        }.start();

        revalidate();
    }

    public static void main(String[] args) {
        SpaceRangers sr = new SpaceRangers();
    }
}

class Universe extends JPanel {

    Ship spaceship;
    ArrayList<Projectile> ammunition;
    ArrayList<Asteroid> asteroids;
    int numAsteroids = 5;

    Universe() {
        super();
        setBackground(Color.BLACK);
        spaceship = new Ship();

        ammunition = new ArrayList<Projectile>();
        asteroids = new ArrayList<Asteroid>(numAsteroids);

        for (int i = 0; i < numAsteroids; i++) {
            Asteroid newAsteroid = new Asteroid(asteroids);
            newAsteroid.randomLocation();
            asteroids.add(newAsteroid);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Identity transformation matrix for resetting g2d
        AffineTransform identity = new AffineTransform();

        // reset the graphics
        g2d.setTransform(identity);
        // set the color to the color of the spaceship
        g2d.setColor(spaceship.getColor());
        // move the spaceship
        spaceship.move();
        // move the graphics to the ship's relative location
        g2d.translate(spaceship.getPositionX(), 
                      spaceship.getPositionY());
        // update the ship's heading
        g2d.rotate(Math.toRadians(spaceship.getFacing()), 
                   spaceship.getCentroidX(), 
                   spaceship.getCentroidY());
        // draw the ship
        g2d.drawPolygon(spaceship.getShip());

        for (Projectile p : ammunition) {
            p.move(); // first move the projectile
            if (p.isAlive()) { // if the projectile is still alive/on-screen
                g2d.setTransform(identity); 
                g2d.translate(p.getPositionX(), p.getPositionY()); 
                g2d.drawPolygon(p);
            } 
        }

        for (Asteroid a : asteroids) {
            g2d.setTransform(identity);
            g2d.translate(a.getPositionX(),a.getPositionY());
            g2d.drawPolygon(a);
        }
    }
}
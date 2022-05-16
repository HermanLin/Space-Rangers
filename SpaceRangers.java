import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * The SpaceRangers class is the driver class for the entire game.
 * It creates the JFrame that holds all the components as well
 * as listens to the key inputs from the player.
 * 
 * @author Herman Lin
 * @author Devin Zhu
 */

public class SpaceRangers extends JFrame {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;
    public static final int DELAY = 10;
    private Universe universe;

    public static boolean keyHeld = false;
    public static int heldKeyCode;

    public static Client player;

    /**
     * Constructor for creating the JFrame and initializing the 
     * JPanel that is responsible for drawing components.
     */
    SpaceRangers(Client player, Color color) {
        super("Space Rangers");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.player = player;
        universe = new Universe(color);
        this.add(universe);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    keyHeld = true; heldKeyCode = e.getKeyCode();
                    universe.spaceship.rotateLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    keyHeld = true; heldKeyCode = e.getKeyCode();
                    universe.spaceship.rotateRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    keyHeld = true; heldKeyCode = e.getKeyCode();
                    universe.spaceship.increaseVelocity();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    keyHeld = true; heldKeyCode = e.getKeyCode();
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
            
            @Override
            public void keyReleased(KeyEvent e) { keyHeld = false; }
        });

        revalidate();
    }

    public static void main(String[] args) {
        new StartUp();
    }
}

class Universe extends JPanel {

    final int NUM_ASTEROIDS = 7;

    Ship spaceship;
    ArrayList<Projectile> ammunition;
    ArrayList<Asteroid> asteroids;

    // a queue that is continually updated with Data from other Players
    SynchronousQueue<Data> updates;
    // DataProcessor compresses/decompresses updates from the Server
    DataProcessor dp;
    String data = "";

    Universe(Color color) {
        super();
        setBackground(Color.BLACK);

        updates = new SynchronousQueue<Data>();
        dp = new DataProcessor();

        spaceship = new Ship(color);
        ammunition = new ArrayList<Projectile>();

        String update = "";
        // System.out.println("Checking data");
        if (SpaceRangers.player.sin.hasNextLine()) {
            // System.out.println("Receiving new Data");
            update = SpaceRangers.player.readFromServer();
        }
        // System.out.println("Finished Checking");
        if (update.isEmpty()) {
            // System.out.println("new data empty");
            asteroids = new ArrayList<Asteroid>(NUM_ASTEROIDS);

            for (int i = 0; i < NUM_ASTEROIDS; i++) {
                Asteroid newAsteroid = new Asteroid(asteroids);
                asteroids.add(newAsteroid);
            }
        } else {
            // grab the current asteroids state
            Data data = dp.decompressAsteroids(update);
            asteroids = data.asteroids;
        }

        // thread that continually reads from the Server
        new Thread() {
            public void run() {
                DataProcessor dp = new DataProcessor();
                String update = "";
                while(true) {
                    try {
                        sleep(SpaceRangers.DELAY);
                    } catch (InterruptedException e) {}
                    // read from the server
                    if (SpaceRangers.player.sin.hasNextLine()) {
                        update = SpaceRangers.player.readFromServer();
                    }
                    if (!update.isEmpty()) {
                        Data data = dp.decompressUpdate(update);
                        try {
                            updates.put(data);
                        } catch (Exception e) {}
                    }
                }
            }
        }.start();

        // thread that continually updates the Universe
        new Thread() {
            public void run() {
                DataProcessor dp = new DataProcessor();
                while(true) { 
                    try {
                        sleep(SpaceRangers.DELAY); // delay for the whole universe, wow!
                    } catch (InterruptedException e) {}
                    data = dp.compress(spaceship, ammunition, asteroids);
                    SpaceRangers.player.writeToServer(data);
                    repaint(); 
                }
            }
        }.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Identity transformation matrix for resetting g2d
        AffineTransform identity = new AffineTransform();

        // poll the updates queue for another player's data
        Data player2 = null;
        try {
            // System.out.println("Polling Queue");
            player2 = updates.poll(SpaceRangers.DELAY, TimeUnit.MILLISECONDS);
            // player2 = updates.poll();
        } catch (Exception e) {}

        // reset the graphics
        g2d.setTransform(identity);
        // set the color to the color of the spaceship
        g2d.setColor(spaceship.getColor());
        // check if the player is holding down a key for smoother movement
        if (SpaceRangers.keyHeld && SpaceRangers.heldKeyCode == KeyEvent.VK_LEFT) {
            spaceship.rotateLeft();
        } else if (SpaceRangers.keyHeld && SpaceRangers.heldKeyCode == KeyEvent.VK_RIGHT) {
            spaceship.rotateRight();
        } else if (SpaceRangers.keyHeld && SpaceRangers.heldKeyCode == KeyEvent.VK_UP) {
            spaceship.increaseVelocity();
        } else if (SpaceRangers.keyHeld && SpaceRangers.heldKeyCode == KeyEvent.VK_DOWN) {
            spaceship.decreaseVelocity();
        }
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

        // draw spaceship2 if present
        if (player2 != null) {
            g2d.setTransform(identity);
            g2d.setColor(player2.spaceship.getColor());
            g2d.translate(player2.spaceship.getPositionX(),
                          player2.spaceship.getPositionY());
            g2d.rotate(Math.toRadians(player2.spaceship.getFacing()),
                       player2.spaceship.getCentroidX(),
                       player2.spaceship.getCentroidY());
            g2d.drawPolygon(player2.spaceship.getShip());

            for (Projectile p : player2.projectiles) {
                p.move();
                if (p.isAlive()) {
                    g2d.setTransform(identity);
                    g2d.translate(p.getPositionX(), p.getPositionY());
                    g2d.drawPolygon(p);
                }
            }

            for (Asteroid a : asteroids) {
                if (a.isAlive()) {
                    // check if any projectile intersects with Asteroid a
                    for (Projectile p : player2.projectiles) {
                        if (a.collidesWith(p)) {
                            a.destroy();
                            p.destroy();
                        }
                    }
                }
            }
        } //else { System.out.println("No player2"); }

        g2d.setColor(Color.WHITE);
        // move and update asteroids within the universe
        for (Asteroid a : asteroids) {
            if (a.isAlive()) {
                // check if any projectile intersects with Asteroid a
                for (Projectile p : ammunition) {
                    if (a.collidesWith(p)) {
                        a.destroy();
                        p.destroy();
                    }
                }
                // move and draw the asteroid
                a.move();
                g2d.setTransform(identity);
                g2d.translate(a.getPositionX(),a.getPositionY());
                g2d.rotate(Math.toRadians(a.getDirection()),
                        a.getCentroidX(), a.getCentroidY());
                g2d.drawPolygon(a);
            }
        }
    }
}
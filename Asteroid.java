import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

/**
 * The Asteroid class contains data on a specific Asteroid within the
 * Universe. The class also holds an ArrayList containing all the 
 * Asteroids within the Universe, which allows for adding smaller
 * Asteroids to the Universe when a larger one is destroyed.
 * 
 * @author Herman Lin
 * @author Devin Zhu
 */


class Asteroid extends Polygon {

    // An ArrayList that contains all the asteroids within the
    // world. Asteroids maintain their own presence in the universe
    ArrayList<Asteroid> asteroids;
    // 4 different shapes of Asteroids where each consecutive
    // pair of indices represents the x/y coordinates of an
    // asteroid shape
    private static int[][] coordinates = {
        new int[] {0,1,3,3,1,-1,-3,-3,-1,3,-1}, 
        new int[] {2,3,1,-1,-3,-3,-1,0,0,2,3},
        new int[] {0,1,3,2,2,-1,-3,-2,-3,-1},
        new int[] {2,3,1,0,-2,-3,-1,0,1,3},
        new int[] {0,2,3,2,2,0,-2,-3,-2,0},
        new int[] {1,2,0,-1,-2,-3,-2,0,2,3},
        new int[] {0,2,3,2,0,-1,-2,-2,-3,-2,-1},
        new int[] {2,3,1,-2,-2,-3,-2,-1,0,2,3}
    };
    // Represents the number of points for each asteroid shape
    private static int[] nVertex = {11,10,10,11};
    // Represents how large the asteroid should be
    private double scale;
    // Center variables for the Asteroid, used in translation
    private double centerX;
    private double centerY;
    // Centroid variables for the Asteroid, used in rotation
    private double centroidX;
    private double centroidY;
    // Direction the Asteroid moves
    private double direction;
    // Velocities of the Asteroid
    private double velocityX;
    private double velocityY;
    // Color of the Asteroid
    private Color color = Color.WHITE;

    public Random random = new Random(System.currentTimeMillis());
    // Randomize the type of the default asteroid
    private int type = random.nextInt(4);

    /**
     * Constructor for creating a default large Asteroid
     * 
     * @param type the type of the Asteroid
     * @param scale the size of the Asteroid
     */
    Asteroid(int type, double scale) {
        super(coordinates[type * 2], 
              coordinates[type * 2 + 1], 
              nVertex[type]);
        this.scale = scale;
    }    

    /**
     * Constructor for creating asteroids of specific
     * qualities and location
     *
     * @param posX the x coordinate of the Asteroid
     * @param posY the y coordinate of the Asteroid
     * @param dir the direction the Asteroid moves in
     * @param velX the velocity the Asteroid moves in the X direction
     * @param velY the velocity the Asteroid moves in the Y direciton
     * @param type the type of the Asteroid
     * @param scale the size of the Asteroid
     * @param asteroids an ArrayList of Asteroids within the Universe
     */
    Asteroid(double posX, double posY, double dir, double velX, double velY, 
             int type, double scale, ArrayList<Asteroid> asteroids) {
        this(type, scale);
        centerX = posX; centerY = posY;
        direction = dir; 
        velocityX = velX; velocityY = velY;
        this.asteroids = asteroids;
    }
    
    // Getters and Setters
    public Asteroid getAsteroid() { return this; }
    public int getType() { return type; }
    public void setType(int newType) { type = newType; }
    public double getScale() { return scale; }
    public void setScale(int newScale) { type = newScale; }

    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
    public void setCenterX(double xcoord) { centerX = xcoord; }
    public void setCenterY(double ycoord) { centerY = ycoord; }

    public double getCentroidX() { return centroidX; }
    public double getCentroidY() { return centroidY; }
    public void setCentroidX(double xcoord) { centroidX = xcoord; }
    public void setCentroidY(double ycoord) { centroidY = ycoord; }

    public double getDirection() { return direction; }
    public void setDirection(double angle) { direction = angle; }

    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public void setVelocityX(double velX) { velocityX = velX; }
    public void setVelocityY(double velY) { velocityY = velY; }

    /**
    * Calculates the centroid of the asteroid shape that is used
    * when rotating the Asteroid.
    * 
    * @see https://en.wikipedia.org/wiki/Centroid#Of_a_polygon
    */
    public void computeCentroid() {
        centroidX = 0.0; centroidY = 0.0;
        double x0, x1, y0, y1, a, signedArea = 0.0;

        for (int i = 0; i < npoints; i++) {
            x0 = this.xpoints[i]; 
            y0 = this.ypoints[i];
            x1 = this.xpoints[(i+1) % this.npoints]; 
            y1 = this.ypoints[(i+1) % this.npoints];
            a = x0*y1 - x1*y0;
            signedArea += a;
            centroidX += (x0 + x1) * a;
            centroidY += (y0 + y1) * a;
        }

        signedArea *= 0.5;
        centroidX /= (6.0 * signedArea);
        centroidY /= (6.0 * signedArea);
    }
}
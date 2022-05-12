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
    public Random random = new Random();//(System.currentTimeMillis());

    // An ArrayList that contains all the asteroids within the
    // world. Asteroids maintain their own presence in the universe
    ArrayList<Asteroid> asteroids;
    // 4 different shapes of Asteroids where each consecutive
    // pair of indices represents the x/y coordinates of an
    // asteroid shape
    private static int[][] coordinates = {
        // new int[] {0,1,3,3,1,-1,-3,-3,-1,3,-1}, 
        // new int[] {2,3,1,-1,-3,-3,-1,0,0,2,3},
        new int[] {0,1,3,3,1,-1,-3,-3,-1,-1,0}, 
        new int[] {2,3,1,-1,-3,-3,-1,0,0,3,2},
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
    // Position variables for the Asteroid, used in translation
    private double positionx;
    private double positiony;
    // Centroid variables for the Asteroid, used in rotation
    private double centroidx;
    private double centroidy;
    // Direction the Asteroid moves
    private double direction;
    // Velocities of the Asteroid
    private double velocityx = 1.5;
    private double velocityy = 1.5;
    // Color of the Asteroid
    private Color color = Color.WHITE;

    
    // Randomize the type of the default asteroid
    private int type;

    /**
     * Constructor for creating a default large Asteroid
     */
    Asteroid(ArrayList<Asteroid> asteroids) {
        super();
        type = random.nextInt(4);
        this.npoints = nVertex[type];
        System.out.println("Type: " + type + ", nVertex: " + npoints);

        // this.xpoints = coordinates[type * 2];
        // this.ypoints = coordinates[type * 2 + 1]; 
        int[][] temp_array = {new int[npoints], new int[npoints]};        
        //temp_array[0] = coordinates[type * 2];
        //temp_array[1] = coordinates[type * 2 + 1]; 
        System.arraycopy(coordinates[type * 2], 0, temp_array[0], 0, nVertex[type]);
        System.arraycopy(coordinates[type * 2 + 1], 0, temp_array[1], 0, nVertex[type]);        
        scale = 10.0;

        for (int i = 0; i < npoints; i++) { 
            // this.xpoints[i] *= scale; 
            // this.ypoints[i] *= scale;
            temp_array[0][i] *= scale;
            temp_array[1][i] *= scale;
        }

        this.xpoints = temp_array[0];
        this.ypoints = temp_array[1];
        System.out.println(this);

        this.asteroids = asteroids;
        computeCentroid();
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
        super();
        type = random.nextInt(4);
        System.out.println("Type: " + type);
        this.xpoints = coordinates[type * 2];
        this.ypoints = coordinates[type * 2 + 1]; 
        this.npoints = nVertex[type];
        this.scale = scale;
        this.asteroids = asteroids;

        positionx = posX; positiony = posY;
        direction = dir; 
        velocityx = velX; velocityy = velY;

        computeCentroid();
    }
    
    // Getters and Setters
    public Asteroid getAsteroid() { return this; }
    public int getType() { return type; }
    public void setType(int newType) { type = newType; }
    public double getScale() { return scale; }
    public void setScale(int newScale) { type = newScale; }

    public double getPositionX() { return positionx; }
    public double getPositionY() { return positiony; }
    public void setPositionX(double xcoord) { positionx = xcoord; }
    public void setPositionY(double ycoord) { positiony = ycoord; }

    public double getCentroidX() { return centroidx; }
    public double getCentroidY() { return centroidy; }
    public void setCentroidX(double xcoord) { centroidx = xcoord; }
    public void setCentroidY(double ycoord) { centroidy = ycoord; }

    public double getDirection() { return direction; }
    public void setDirection(double angle) { direction = angle; }

    public double getVelocityX() { return velocityx; }
    public double getVelocityY() { return velocityy; }
    public void setVelocityX(double velX) { velocityx = velX; }
    public void setVelocityY(double velY) { velocityy = velY; }

    /**
    * Calculates the centroid of the asteroid shape that is used
    * when rotating the Asteroid.
    * 
    * @see https://en.wikipedia.org/wiki/Centroid#Of_a_polygon
    */
    public void computeCentroid() {
        centroidx = 0.0; centroidy = 0.0;
        double x0, x1, y0, y1, a, signedArea = 0.0;

        for (int i = 0; i < npoints; i++) {
            x0 = this.xpoints[i]; 
            y0 = this.ypoints[i];
            x1 = this.xpoints[(i+1) % this.npoints]; 
            y1 = this.ypoints[(i+1) % this.npoints];
            a = x0*y1 - x1*y0;
            signedArea += a;
            centroidx += (x0 + x1) * a;
            centroidy += (y0 + y1) * a;
        }

        signedArea *= 0.5;
        centroidx /= (6.0 * signedArea);
        centroidy /= (6.0 * signedArea);
    }

    /**
     * Randomize the starting location of the Asteroid
     */
    public void randomLocation() {
        // determine which side of the screen the Asteroid will spawn
        int side = random.nextInt(4);
        if (side == 0) { // top of screen
            positionx = random.nextDouble() * SpaceRangers.SCREEN_WIDTH;
            positiony = 0.0; 
        } else if (side == 1) { // bottom of screen
            positionx = random.nextDouble() * SpaceRangers.SCREEN_WIDTH;  
            positiony = SpaceRangers.SCREEN_HEIGHT;
        } else if (side == 2) { // left of screen
            positionx = 0.0; 
            positiony = random.nextDouble() * SpaceRangers.SCREEN_HEIGHT;
        } else if (side == 3) { // right of screen
            positionx = SpaceRangers.SCREEN_WIDTH;
            positiony = random.nextDouble() * SpaceRangers.SCREEN_HEIGHT;
        }
    }

    public void randomDirection() {
        direction = 360 * random.nextDouble();
    }

    /**
     * Represent the Asteroid through its x/y coordinates
     */
    public String toString() {
        String ret = "X: [ ";
        for (int x : this.xpoints) { ret += x + " "; }
        ret += "], Y: [ ";
        for (int y : this.ypoints) { ret += y + " "; }
        ret += "]";
        return ret;
    }

    public void move() {
        positionx += velocityx * Math.cos(Math.toRadians(direction + 90));
        positiony += velocityy * Math.sin(Math.toRadians(direction + 90));

        if (positionx < 0) { positionx = SpaceRangers.SCREEN_WIDTH; }
        else if (positionx > SpaceRangers.SCREEN_WIDTH) { positionx = 0; }
        if (positiony < 0) { positiony = SpaceRangers.SCREEN_HEIGHT; }
        else if (positiony > SpaceRangers.SCREEN_HEIGHT) { positiony = 0; }
    }
}
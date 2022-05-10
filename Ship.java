import java.awt.*;
import java.awt.geom.Point2D.Double;

/**
 * The Ship class holds data regarding where the player is
 * located within the Universe and performs any transformations
 * necessary to move/manipulate the player.
 *
 * @author Herman Lin 
 * @author Devin Zhu
 */

public class Ship extends Polygon {

    // Center variables for the Ship, used in translation
    private double centerx;
    private double centery;
    // Centroid variables for the Ship, used in rotation    
    private double centroidx;
    private double centroidy;
    // The direction the Ship is facing
    private double facing = 0; // starts facing down
    // Velocity of the ship, starts at 0 in both x/y direction
    private double velocityx = 0;
    private double velocityy = 0;
    // Describes the color of the ship
    private Color color;

    Ship() {
        // default location for a new ship
//        this(new int[] {400, 410, 400, 390}, 
//             new int[] {400, 375, 380, 375},
          this(new int[] {0, 10, 0, -10},
               new int[] {0, -25, -20, -25},   
               4, Color.WHITE);   
    }

    /**
     * @param xpoints x-coordinate points of the Ship
     * @param ypoints y-coordinate points of the Ship
     * @param npoints the number of xy-coordinate pairs
     * @param color the color of the Ship
     */
    Ship(int[] xpoints, int[] ypoints, int npoints, Color color) {
        super(xpoints, ypoints, npoints);
        this.color = color; 
        computeCentroid();

        // set default starting position
        centerx = 400.0;
        centery = 400.0; 
    }

    // Getters and Setters
    public Polygon getShip() { return this; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public double getCenterX() { return centerx; }
    public double getCenterY() { return centery; }
    public void setCenterX(double xcoord) { centerx = xcoord; }
    public void setCenterY(double ycoord) { centery = ycoord; }

    public double getCentroidX() { return centroidx; }
    public double getCentroidY() { return centroidy; }
    public void setCentroidX(double xcoord) { centroidx = xcoord; }
    public void setCentroidY(double ycoord) { centroidy = ycoord; }
    
    public double getFacing() { return facing; }
    public void setFacing(double dirAngle) { facing = dirAngle; }

    public double getVelocityX() { return velocityx; }
    public double getVelocityY() { return velocityy; }
    public void setVelocityX(double velx) { velocityx = velx; }
    public void setVelocityY(double vely) { velocityy = vely; }

    /**
    * Calculates the centroid of the Ship that is used
    * when rotating the ship.
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
     * Rotates the Ship by a certain angle by manipulating
     * the coordinates of the Ship.
     * 
     * @param rAngle rotation angle in radians 
     */
    public void rotateShip(double rAngle) {
        double x, y;
        for (int i = 0; i < npoints; i++) {
            x = this.xpoints[i] - centroidx;
            y = this.ypoints[i] - centroidy;
            this.xpoints[i] = (int) (centroidx + 
                Math.round(((x * Math.cos(rAngle)) - (y * Math.sin(rAngle)))));
            this.ypoints[i] = (int) (centroidy +
                Math.round(((x * Math.sin(rAngle)) + (y * Math.cos(rAngle)))));
        }
    }

    /**
     * Rotate the Ship by 5 degrees clockwise
     */
    public void rotateRight() {
        if (facing >= 355) { facing = 0; }
        else { facing += 5; }
//        System.out.println("Ship facing: " + facing);
    }

    /**
     * Rotate the Ship by 5 degrees counter-clockwise
     */
    public void rotateLeft() {
        if (facing <= 0) { facing = 355; }
        else { facing -= 5; }
//        System.out.println("Ship facing: " + facing);
    }

    public void move() {
        double deltaX = 5 * Math.cos(Math.toRadians(facing + 90));
        double deltaY = 5 * Math.sin(Math.toRadians(facing + 90));
        centerx += deltaX;
        centery += deltaY;
    }
}
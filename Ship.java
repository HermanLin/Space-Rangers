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
    private double facing = 180; // starts facing up
    private double moveFacing = 270; 
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
    public double getMoveFacing() { return moveFacing; }
    public void setMoveFacing(double dirAngle) { moveFacing = dirAngle; }

    public double getVelocityX() { return velocityx; }
    public double getVelocityY() { return velocityy; }
    public void setVelocityX(double velx) { velocityx = velx; }
    public void setVelocityY(double vely) { velocityy = vely; }

    /**
     * Get coordinates of the nose of the ship. This is used to get
     * the location from which projectiles will spawn from
     */
    public double getShipNoseX() { return this.xpoints[0]; }
    public double getShipNoseY() { return this.ypoints[0]; }

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
     * Rotate the Ship by 5 degrees clockwise
     */
    public void rotateRight() {
        if (facing >= 355) { facing = 0; }
        else { facing += 5; }
    }

    /**
     * Rotate the Ship by 5 degrees counter-clockwise
     */
    public void rotateLeft() {
        if (facing <= 0) { facing = 355; }
        else { facing -= 5; }
    }

    /**
     * Increase the ship's velocity based on the direction
     * it is facing. This is multiplied by a base speed of
     * 0.03 units.
     */
    public void increaseVelocity() {
        moveFacing = facing + 90;
        velocityx += 0.03 * Math.cos(Math.toRadians(moveFacing));
        velocityy += 0.03 * Math.sin(Math.toRadians(moveFacing));
    }
    /**
     * Decrease the ship's velocity based on the direction
     * it is facing. This is multiplied by a base speed of
     * 0.03 units.
     */
    public void decreaseVelocity() {
        moveFacing = facing + 90;
        velocityx -= 0.03 * Math.cos(Math.toRadians(moveFacing));
        velocityy -= 0.03 * Math.sin(Math.toRadians(moveFacing));
    }

    /**
     * Move the ship according to it's velocity values.
     */
    public void move() {
        // Cap the Ship's velocity at 3.0 units per unit of time
        if (velocityx > 3.0) { velocityx = 3.0; }
        else if (velocityx < -3.0) { velocityx = -3.0; }
        if (velocityy > 3.0) { velocityy = 3.0; }
        else if (velocityy < -3.0) { velocityy = -3.0; }

        // update the Ship's location
        centerx += velocityx;
        centery += velocityy;

        // if the Ship goes off screen, 
        // circle back to the other side
        if (centerx < 0) { centerx = 800; }
        else if (centerx > 800) { centerx = 0; }
        if (centery < 0) { centery = 800; }
        else if (centery > 800) { centery = 0; }
    }
}
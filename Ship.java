import java.awt.*;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;

/**
 * The Ship class holds data regarding where the player is
 * located within the Universe and performs any transformations
 * necessary to move/manipulate the player.
 *
 * @author Herman Lin 
 * @author Devin Zhu
 */

public class Ship extends Polygon {

    // Coordinate data for the Ship
    private static final int[] xcoords = {0, 10, 0, -10};
    private static final int[] ycoords = {0, -25, -20, -25};
    private static final int nVertex = 4;
    // Position variables for the Ship, used in translation
    private double positionx;
    private double positiony;
    // Centroid variables for the Ship, used in rotation and positioning   
    private double centroidx;
    private double centroidy;
    private double t_centroidx;
    private double t_centroidy;
    // The direction the Ship is facing
    private double facing = 180; // starts facing up
    private double moveFacing = 270; 
    // Velocity of the ship, starts at 0 in both x/y direction
    private double velocityx = 0;
    private double velocityy = 0;
    // Describes the color of the ship
    private Color color;

    /**
     * @param color the color of the ship
     */
    Ship(Color color) {
        super(xcoords, ycoords, nVertex);
        this.color = color;

        // set default starting position
        positionx = SpaceRangers.SCREEN_WIDTH/2;
        positiony = SpaceRangers.SCREEN_HEIGHT/2; 

        computeCentroid();
        computeTranslatedCentroid();
    }

    /**
     * @param color the color of the Ship
     * @param posx the x coordinate of the Ship
     * @param posy the y coordinate of the Ship
     */
    Ship(Color color, double posx, double posy) {
        super(xcoords, ycoords, nVertex);
        this.color = color; 
        positionx = posx;
        positiony = posy;

        computeCentroid();
        computeTranslatedCentroid();
    }

    // Getters and Setters
    public Polygon getShip() { return this; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public double getPositionX() { return positionx; }
    public double getPositionY() { return positiony; }
    public void setPositionX(double xcoord) { positionx = xcoord; }
    public void setPositionY(double ycoord) { positiony = ycoord; }

    public double getCentroidX() { return centroidx; }
    public double getCentroidY() { return centroidy; }
    public double getTranslatedCentroidX() { return t_centroidx; }
    public double getTranslatedCentroidY() { return t_centroidy; }
    public void setCentroidX(double xcoord) { centroidx = xcoord; }
    public void setCentroidY(double ycoord) { centroidy = ycoord; }
    public void setTranslatedCentroidX(double xcoord) { t_centroidx = xcoord; }
    public void setTranslatedCentroidY(double ycoord) { t_centroidy = ycoord; }
    
    public double getFacing() { return facing; }
    public void setFacing(double dirAngle) { facing = dirAngle; }
    public double getMoveFacing() { return moveFacing; }
    public void setMoveFacing(double dirAngle) { moveFacing = dirAngle; }

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
     * Calculate the centroid of the ship relative to the ship's
     * translated location
     */
    public void computeTranslatedCentroid() {
        t_centroidx = centroidx + positionx;
        t_centroidy = centroidy + positiony;
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
        velocityx += 0.01 * Math.cos(Math.toRadians(moveFacing));
        velocityy += 0.01 * Math.sin(Math.toRadians(moveFacing));
    }
    /**
     * Decrease the ship's velocity based on the direction
     * it is facing. This is multiplied by a base speed of
     * 0.03 units.
     */
    public void decreaseVelocity() {
        moveFacing = facing + 90;
        velocityx -= 0.01 * Math.cos(Math.toRadians(moveFacing));
        velocityy -= 0.01 * Math.sin(Math.toRadians(moveFacing));
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
        positionx += velocityx;
        positiony += velocityy;

        // if the Ship goes off screen, 
        // circle back to the other side
        if (positionx < 0) { positionx = SpaceRangers.SCREEN_WIDTH; }
        else if (positionx > SpaceRangers.SCREEN_WIDTH) { positionx = 0; }
        if (positiony < 0) { positiony = SpaceRangers.SCREEN_HEIGHT; }
        else if (positiony > SpaceRangers.SCREEN_HEIGHT) { positiony = 0; }
    }
}
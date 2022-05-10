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

    // Centroid variables for the Ship    
    private double centroidx;
    private double centroidy;
    // Describes the color of the ship
    private Color color;

    Ship() {
        // default location for a new ship
        this(new int[] {400, 410, 400, 390}, 
             new int[] {400, 375, 380, 375},
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
    }

    // Getters 
    public Polygon getShip() { return this; }
    public Color getColor() { return color; }
    public double getCentroidX() { return centroidx; }
    public double getCentroidY() { return centroidy; }

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
}
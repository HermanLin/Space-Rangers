import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Point2D.Double;

public class Ship extends Polygon{

    private Color color;
    private double centroidx;
    private double centroidy;
    private AffineTransform tx;

    Ship() {
        // default location for a new ship
        this(new int[] {400, 410, 400, 390}, 
             new int[] {400, 375, 380, 375},
             4);   
    }

    Ship(int[] xpoints, int[] ypoints, int npoints) {
        super(xpoints, ypoints, npoints);
        color = Color.WHITE;
        computeCentroid();
        tx = new AffineTransform();
    }

    public Polygon getShip() { return this; }
    public Color getColor() { return color; }

    /*
        computeCentroid
        - calculates the centroid of the polygon-ship
        - https://en.wikipedia.org/wiki/Centroid#Of_a_polygon
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
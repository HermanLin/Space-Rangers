import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Point2D.Double;

public class Player {

    private Color color;
    private Polygon ship;
    private Double centroid; 
    private AffineTransform tx;
    private int npoints;

    Player() {
        // default location for a new ship
        this(new int[] {400, 410, 400, 390}, 
             new int[] {400, 375, 380, 375});   
    }

    Player(int[] xpoints, int[] ypoints) {
        npoints = xpoints.length;
        ship = new Polygon(xpoints, ypoints, npoints);
        color = Color.WHITE;
        computeCentroid();
        tx = new AffineTransform();
    }

    public Polygon getShip() { return ship; }
    public Color getColor() { return color; }

    /*
        computeCentroid
        - calculates the centroid of the polygon-ship
        - https://en.wikipedia.org/wiki/Centroid#Of_a_polygon
    */
    public void computeCentroid() {
        Double centroid = new Double(0, 0);
        double x0, x1, y0, y1, a, signedArea = 0.0;

        for (int i = 0; i < npoints; i++) {
            x0 = ship.xpoints[i]; 
            y0 = ship.ypoints[i];
            x1 = ship.xpoints[(i+1) % npoints]; 
            y1 = ship.ypoints[(i+1) % npoints];
            a = x0*y1 - x1*y0;
            signedArea += a;
            centroid.x += (x0 + x1) * a;
            centroid.y += (y0 + y1) * a;
        }

        signedArea *= 0.5;
        centroid.x /= (6.0 * signedArea);
        centroid.y /= (6.0 * signedArea);

        this.centroid = centroid;
    }

    public void rotateShip(double rAngle) {
        double x, y;
        for (int i = 0; i < npoints; i++) {
            x = ship.xpoints[i] - centroid.x;
            y = ship.ypoints[i] - centroid.y;
            ship.xpoints[i] = (int) (centroid.x + 
                Math.round(((x * Math.cos(rAngle)) - (y * Math.sin(rAngle)))));
            ship.ypoints[i] = (int) (centroid.y +
                Math.round(((x * Math.sin(rAngle)) + (y * Math.cos(rAngle)))));
        }
    }
}
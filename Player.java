import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.geom.Point2D.Double;
import javax.swing.*;

public class Player implements KeyListener {

    private Color color;
    private Polygon ship;
    private Double centroid; 
    private AffineTransform tx;

    Player() {
        // default location for a new ship
        this(new int[] {400, 410, 400, 390}, 
             new int[] {400, 375, 380, 375});   
    }

    Player(int[] xpoints, int[] ypoints) {
        ship = new Polygon(xpoints, ypoints, 4);
        color = Color.WHITE;
        centroid = computeCentroid(ship, 4);
        tx = new AffineTransform();

        System.out.println(centroid);
    }

    public Polygon getShip() { return ship; }
    public Color getColor() { return color; }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {

        }
    }

    /*
        computeCentroid
        - calculates the centroid of the polygon-ship
        - https://en.wikipedia.org/wiki/Centroid#Of_a_polygon
    */
    private Double computeCentroid(Polygon ship, int vertexCount) {
        Double centroid = new Double(0, 0);
        double x0, x1, y0, y1, a, signedArea = 0.0;

        for (int i = 0; i < vertexCount; i++) {
            x0 = ship.xpoints[i]; 
            y0 = ship.ypoints[i];
            x1 = ship.xpoints[(i+1) % vertexCount]; 
            y1 = ship.ypoints[(i+1) % vertexCount];
            a = x0*y1 - x1*y0;
            signedArea += a;
            centroid.x += (x0 + x1) * a;
            centroid.y += (y0 + y1) * a;
        }

        signedArea *= 0.5;
        centroid.x /= (6.0 * signedArea);
        centroid.y /= (6.0 * signedArea);

        return centroid;
    }
}
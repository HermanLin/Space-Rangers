import java.awt.*;

/**
 * The Projectile class contains data on the player 
 * ship's weapon to destroy asteroids.
 * 
 * @author Herman Lin
 * @author Devin Zhu
 */

public class Projectile extends Polygon {

    // Data on the Projectile Polygon
    private static final int[] xcoords = {-10,-8,-8,-10};
    private static final int[] ycoords = {-10,-10,-8,-8};
    private static final int nVertex = 4;
    // The color of the projectile
    private Color color;
    // Position of the projectile
    private double positionx;
    private double positiony;
    // Velocity of the projectile (set speed of 5)
    private double velocity = 5;
    // Direction that the projectile moves in
    private double direction;
    // Boolean for determining when the projectile is offscreen/destroyed
    private boolean alive = true;

    /**
     * Constructor for creating a new Projectile
     * 
     * @param color the color of the projectile
     * @param direction the direction the projectile moves
     * @param posx the starting x-coord of the projectile
     * @param posy the starting y-coord of the projectile
     */
    Projectile(Color color, double direction, double posx, double posy) {
        super(xcoords, ycoords, nVertex);
        this.color = color;
        this.direction = direction;
        positionx = posx;
        positiony = posy;
    }
    
    // Getters 
    public Polygon getProjectile() { return this; }
    public double getPositionX() { return positionx; }
    public double getPositionY() { return positiony; }
    public double getDirection() { return direction; }
    public boolean isAlive() { return alive; }
    public void destroy() { alive = false; }

    public Rectangle getBounds() { 
        return new Rectangle((int)positionx, (int)positiony, 2, 2);
    }

    /**
     * Moves the projectile a specific velocity in a 
     * specific direction.
     */
    public void move() {
        positionx += velocity * Math.cos(Math.toRadians(direction + 90));
        positiony += velocity * Math.sin(Math.toRadians(direction + 90));

        // if the projectile goes off screen, make it unalive
        if (positionx < 0   || 
            positionx > SpaceRangers.SCREEN_WIDTH || 
            positiony < 0   || 
            positiony > SpaceRangers.SCREEN_HEIGHT) { alive = false; }
    }
}

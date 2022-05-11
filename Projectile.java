import java.awt.*;

/**
 * The Projectile class contains data on the player 
 * ship's weapon to destroy asteroids.
 * 
 * @author Herman Lin
 * @author Devin Zhu
 */

public class Projectile extends Polygon{
    //private double damage;
    private Color color;
    private static int npoints = 4;
    private static int[] xpoints = {-10,0,0,-10};
    private static int[] ypoints = {-10,-10,0,0};
    private double positionx;
    private double positiony;
    private double velocityx = 5;
    private double velocityy = 5;
    private double direction;
    private boolean alive = true;



    Projectile(Color color, double direction, double posx, double posy) {
        super(xpoints, ypoints, npoints);
        this.color = color;
        this.direction = direction;
        this.positionx = posx;
        this.positiony = posy;
    }
    
    public Polygon getProjectile() {return this;}
    public double getPositionX() {return positionx;}
    public double getPositionY() {return positiony;}

    // public void setPositionX(double xcord) {positionx = xcord;}
    // public void setPositionY(double ycord) {positiony = ycord;}

    public boolean isAlive() {return alive;}
    

    public void move() {
        positionx += velocityx * Math.cos(Math.toRadians(direction - 90));
        positiony += velocityy * Math.sin(Math.toRadians(direction - 90));
        if (positionx < 0) {alive = false;}
        else if (positionx > 800) {alive = false;} 
        if (positiony < 0) {alive = false;}
        else if (positiony > 800) {alive = false;}

    }
    
    
}

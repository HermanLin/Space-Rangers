import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Player {

    private Color color;
    private Polygon ship;
    private Point center; 

    Player() {
        int x[] = {400, 410, 400, 390};
        int y[] = {400, 375, 380, 375};
        ship = new Polygon(x, y, 4);
        color = Color.WHITE;
    }

    public Polygon getShip() { return ship; }
    public Color getColor() { return color; }

    
}
/*
    SpaceRangers.java
    Herman Lin and Devin Zhu

    This class creates the universe that players interact with.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceRangers {

    private JFrame jf;
    private JPanel jp;

    SpaceRangers() {
        jf = new JFrame("Space Rangers");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(800, 800);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
        
        jp = new Universe();
        jf.add(jp);
    }

    public static void main(String[] args) {
        SpaceRangers sr = new SpaceRangers();
    }
}

class Universe extends JPanel {

    Universe() {
        super();
        setBackground(Color.BLACK);
    }
}
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.regex.*;
import java.io.*;
import java.net.*;

public class StartUp extends JFrame {
    
    final String[] colors = {"White", "Red", "Yellow", "Green", "Cyan"};
    final String IP_ADDRESS =  // used for regex checking
        "[0-9]{1,4}\\.[0-9]{1,4}\\.[0-9]{1,4}\\.[0-9]{1,4}";
    Client player;
    String serverAddr;
    Color playerColor;

    JTextField addrInput;
    JComboBox<String> colorInput;
    JButton join;

    StartUp() {
        super("Space Rangers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        player = new Client();
        JTextField addrInput = new JTextField(20);
        colorInput = new JComboBox<String>(colors);

        JButton join = new JButton("Join the Space Rangers!");
        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverAddr = addrInput.getText().strip();
                playerColor = getColor();

                if (Pattern.matches(IP_ADDRESS, serverAddr)) {
                    Boolean connected = player.connectTo(serverAddr);
                    String msg = player.readFromServer();
                    int playerCount = Integer.parseInt(msg);
                    if (connected && playerCount < 2) { 
                        closeWindow();
                        new SpaceRangers(player, playerColor); 
                    } else {
                        try { player.socket.close(); }
                        catch (Exception ex) {}
                        JOptionPane.showMessageDialog(
                            null,
                            "Unable to connect to the server: " + serverAddr +
                            ". Server may be full",
                            "Space Rangers Error",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Please use a valid server address",
                        "Space Rangers Error",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JPanel prompt = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.RELATIVE;
        prompt.add(new Label("Server Address"), c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        prompt.add(addrInput, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        prompt.add(new Label("Ship Color"), c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        prompt.add(colorInput, c);
        c.gridwidth = GridBagConstraints.HORIZONTAL;
        prompt.add(join, c);

        this.add(prompt);
        this.pack();
    }

    private Color getColor() {
        String selected = (String)(colorInput.getSelectedItem());
        if (selected == "Cyan") { return Color.CYAN; }
        else if (selected == "Red") { return Color.RED; }
        else if (selected == "Yellow") { return Color.YELLOW; }
        else if (selected == "Green") { return Color.GREEN; }
        else { return Color.WHITE; }
    }

    private Boolean verifyServer(String serverAddr) {
        try {
            Socket socket = new Socket(serverAddr, Server.DEFAULT_PORT);
            player = new Client(socket);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void closeWindow() { this.dispose(); }

    // public static void main(String[] args) {
    //     StartUp su = new StartUp();
    // }
}
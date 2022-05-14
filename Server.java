import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.Color;

/**
 * The Server class accepts and closes connections from Players
 * whenever possible. It also receives and sends updates from a
 * Player to all OTHER Players.
 *
 * @author Herman Lin
 * @author Devin Zhu
 */

public class Server {
    
    public static final int DEFAULT_PORT = 5190;
    // dynamic array that allows for n players
    private static ArrayList<Connection> players;

    public static boolean isEmpty() { return players.isEmpty(); }
    public static Connection getFirst() { return players.get(0); }

    /**
     * Removes a player from the list of connections
     * 
     * @param player the player's Connection
     */
    public static void removePlayer(Connection player) {
        players.remove(player);
    }

    public static void writeToOtherPlayers(String data, Connection src) {
        for (Connection p : players) {
            if (p.equals(src)) { continue; }
            else {
                // try { p.sout.writeUTF(data); }
                try { p.sout.print(data); }
                catch (Exception e) {}
            }
        }
    }

    public static void main(String[] args) {
        players = new ArrayList<Connection>();

        try {
            ServerSocket ss = new ServerSocket(DEFAULT_PORT);

            while(true) {
                Socket playerSocket = ss.accept();
                Connection playerConn = new Connection(playerSocket);
                playerConn.start();
                players.add(playerConn);
            }
        } catch (Exception e) {
            System.out.println("Could not listen on port " + DEFAULT_PORT);
        }
    }
}

class Connection extends Thread {

    Socket socket;
    // DataInputStream sin;
    // DataOutputStream sout;
    Scanner sin;
    PrintStream sout;

    Connection(Socket newPlayerSocket) {
        try {
            // initalize the Player's socket connections
            socket = newPlayerSocket;
            // sin = new DataInputStream(socket.getInputStream());
            // sout = new DataOutputStream(socket.getOutputStream());
            sin = new Scanner(socket.getInputStream());
            sout = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {}
    }

    @Override
    public void run() {
        try {
            try {
                while(true) {
                    String data = sin.nextLine();
                    // String data = sin.readUTF();
                    // Server.writeToOtherPlayers(data, this);
                    System.out.println(data);
                    System.out.println("=====");
                }
            } catch (Exception e) {
            } finally {
                System.out.println("Closing");
                socket.close();
                Server.removePlayer(this);
            }
        } catch (Exception e) {}
    }
}
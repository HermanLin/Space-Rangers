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

    public static void writeToOtherPlayers(Data data, Connection src) {
        for (Connection p : players) {
            if (p.equals(src)) { continue; }
            else {
                try { p.objOut.writeObject(data); }
                catch (IOException e) {}
            }
        }
    }

    public static void main(String[] args) {
        players = new ArrayList<Connection>();

        try {
            ServerSocket ss = new ServerSocket(DEFAULT_PORT);

            while(true) {
                Socket playerSocket = ss.accept();
                Connection playerConn = new Connection(playerSocket, players);
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
    ObjectInputStream objIn;
    ObjectOutputStream objOut;
    Color playerColor;

    Connection(Socket newPlayerSocket,
               ArrayList<Connection> playerList) {
        try {
            System.out.println("Receiving");
            // initalize the Player's socket connections
            socket = newPlayerSocket;
            System.out.println("Connection Received");
            objIn = new ObjectInputStream(socket.getInputStream());
            System.out.println("Input Stream made");
            objOut = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Output Stream made");
            // if (!Server.isEmpty()) {
            //     Connection firstPlayer = Server.getFirst();
            //     // directly ask the first Player for Universe data

            //     // send update data to the new Player

            // }
        } catch (IOException e) {}
    }

    @Override
    public void run() {
        try {
            try {
                Data data = (Data)(objIn.readObject());
                Server.writeToOtherPlayers(data, this);
            } catch (Exception e) {
            } finally {
                socket.close();
                Server.removePlayer(this);
            }
        } catch (Exception e) {}
    }
}
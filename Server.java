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
    
    static final int DEFAULT_PORT = 5190;
    // dynamic array that allows for n players
    private static ArrayList<Connection> players;
    // array containing all the astroids in the game
    private static ArrayList<Asteroid> asteroids;

    /**
     * Removes a player from the list of connections
     * 
     * @param player the player's Connection
     */
    public static void removePlayer(Connection player) {
        players.remove(player);
    }

    /**
     * Process data received from a player so that it is ready
     * to be sent to all players. This method appends the list of
     * asteroids afterwards for the full data packet.
     *
     * The final datapacket is represented as follows:
     * [color, null, positionx, positiony, null, 
     *  position data of all projectiles from player,
     *  all asteroids]
     *
     * @param data data received from the player
     * @return data that has been processed
     */
    // public static Data processData(Data data) {
    //     // check for collisions with asteroids
    //     for (Asteroid a : data.asteroids) {

    //     }
        
    //     return {};
    // }

    public static void writeToOtherPlayers(Data data, Connection src) {
        for (Connection p : players) {
            if (p.equals(src)) { continue; }
            else {
                try { p.sout.write(data); }
                catch (IOException e) {}
            }
        }
    }

    public static void main(String[] args) {

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
            socket = newPlayerSocket;
            objIn = new ObjectInputStream(socket.getInputStream());
            objOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {}
    }

    @Override
    public void run() {
        try {
            try {
                Data data = objIn.readObject();
                Server.processData(data);
            } catch (Exception e) {
            } finally {
                playerSocket.close();
                Server.removePlayer(this);
            }
        } catch (Exception e) {}
    }
}
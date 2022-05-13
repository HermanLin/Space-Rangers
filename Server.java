import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.Color;

/**
 * This is the Server class that accepts connections from
 * players. It also contains server-wide data that is necessary
 * for gameplay.
 *
 * @author Herman Lin
 * @author Devin Zhu
 */

public class Server {
    
    static final int DEFAULT_PORT = 5190;
    // dynamic array that allows for n players
    private static ArrayList<Connection> players;
    // array containing all the astroids in the game
    private static Asteroid[] asteroids;

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
    public static byte[] processData(byte[] data) {
        // deepcopy of the data
        byte[] processed = new byte[data.length];
        System.arraycopy(data, 0, processed, 0, data.length);

        // check for collisions with asteroids 
        
        return {};
    }

    public static void writeToAll(byte[] data) {
        for (Connection p : players) {
            try { p.sout.write(data); }
            catch (IOException e) {}
        }
    }

    public static void main(String[] args) {

    }
}

class Connection extends Thread {

    Socket playerSocket;
    InputStream sin;
    OutputStream sout;
    Color playerColor;

    Connection(Socket newPlayerSocket,
               ArrayList<Connection> playerList) {
        try {
            playerSocket = newPlayerSocket;
            sin = playerSocket.getInputStream();
            sout = playerSocket.getOutputStream();
        } catch (IOException e) {}
    }

    @Override
    public void run() {
        try {
            try {
                int length = sin.available();
                byte[] buffer = new byte[length];
                sin.read(buffer);

                Server.processData(buffer);
            } catch (Exception e) {
            } finally {
                playerSocket.close();
                Server.removePlayer(this);
            }
        } catch (IOException e) {}
    }
}
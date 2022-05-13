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

    public static void removePlayer(Connection player) {
        players.remove(player);
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
            } catch (Exception e) {
            } finally {
                playerSocket.close();
                Server.removePlayer(this);
            }
        } catch (IOException e) {}
    }
}
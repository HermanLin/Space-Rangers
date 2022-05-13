import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The Player class serves as the client portion of the
 * Asteroids game. It connects to the Server in order to
 * play with other players
 *
 * @author Herman Lin
 * @author Devin Zhu
 */

public class Player {
    Socket sock;
    InputStream sin;
    OutputStream sout;    
    
    Player(Socket newSocket) {
        try {
            sock = newSocket;
            sin = sock.getInputStream();
            sout = sock.getOutputStream();
        } catch (IOException e) {

        }
    }

    //read data from server
    public void read() {

    }

    // [angle, null, positionx, positiony, null, posdata of projectiles]
    public void write() {

    }
}
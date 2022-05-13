import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The Client class serves as the client portion of the
 * Asteroids game. It connects to the Server in order to
 * play with other players
 *
 * @author Herman Lin
 * @author Devin Zhu
 */

public class Client {
    Socket sock;
    ObjectInputStream sin;
    ObjectOutputStream sout;
    
    Client(Socket newSocket) {
        try {
            sock = newSocket;
            sin = new ObjectInputStream(newSocket.getInputStream());
            sout = new ObjectOutputStream(newSocket.getOutputStream());
        } catch (IOException e) {}
    }

    //read data from server
    public Data readFromServer() {
        try {
            Data data = (Data) (sin.readObject());
            return data;

        } catch (Exception e) {}
        return null;
    }

    // write the ship object to the server
    public void writeToServer(Data data) {
        try {
            sout.writeObject(data);
        } catch (IOException e) {

        }

    }
}
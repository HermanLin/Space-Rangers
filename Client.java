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

    Socket socket;
    DataInputStream sin;
    DataOutputStream sout;
    
    Client() {}
    Client(Socket newSocket) {
        try {
            socket = newSocket;
            sin = new DataInputStream(newSocket.getInputStream());
            sout = new DataOutputStream(newSocket.getOutputStream());
        } catch (IOException e) {}
    }

    public boolean connectTo(String address) {
        try {
            socket = new Socket(address, Server.DEFAULT_PORT);
            sin = new DataInputStream(socket.getInputStream());
            sout = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (Exception e) { return false; }
    }

    //read data from server
    public String readFromServer() {
        try {
            String data = sin.readUTF();
            return data;

        } catch (Exception e) {}
        return "";
    }

    // write the ship object to the server
    public void writeToServer(String data) {
        try {
            sout.writeUTF(data);
        } catch (IOException e) {

        }

    }
}
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
            System.out.println("Attempting to connect");
            socket = new Socket(address, Server.DEFAULT_PORT);
            System.out.println("Connected");
            sin = new DataInputStream(socket.getInputStream());
            System.out.println("Input Stream made");
            sout = new DataOutputStream(socket.getOutputStream());
            System.out.println("Output Stream made");
            return true;
        } catch (Exception e) { return false; }
    }

    //read data from server
    public byte[] readFromServer() {
        try {
            int length = sin.available();
            byte[] data = new byte[length];
            sin.readFully(data);
            return data;

        } catch (Exception e) {}
        return null;
    }

    // write the ship object to the server
    public void writeToServer(byte[] data) {
        try {
            sout.write(data, 0, data.length);
        } catch (IOException e) {

        }

    }
}
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
    ObjectInputStream objIn;
    ObjectOutputStream objOut;
    
    Client() {}
    Client(Socket newSocket) {
        try {
            socket = newSocket;
            objIn = new ObjectInputStream(newSocket.getInputStream());
            objOut = new ObjectOutputStream(newSocket.getOutputStream());
        } catch (IOException e) {}
    }

    public boolean connectTo(String address) {
        try {
            System.out.println("Attempting to connect");
            socket = new Socket(address, Server.DEFAULT_PORT);
            System.out.println("Connected");
            objIn = new ObjectInputStream(socket.getInputStream());
            System.out.println("Input Stream made");
            objOut = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Output Stream made");
            return true;
        } catch (Exception e) { return false; }
    }

    //read data from server
    public Data readFromServer() {
        try {
            Data data = (Data) (objIn.readObject());
            return data;

        } catch (Exception e) {}
        return null;
    }

    // write the ship object to the server
    public void writeToServer(Data data) {
        try {
            objOut.writeObject(data);
        } catch (IOException e) {

        }

    }
}
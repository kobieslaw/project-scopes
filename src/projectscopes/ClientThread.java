package projectscopes;

import java.io.*;
import java.net.Socket;

/**
 *
 * @author Tomasz Najbar
 *
 * Creates a thread for client.
 */
public class ClientThread extends Thread {
    // Receive socket.
    private Socket socket = null;

    // Reader.
    private DataInputStream dataInputStream = null;

    // Communicator.
    private Communicator communicator = null;

    // Players.
    private Player[] players = null;

    // Id.
    private int id = -1;

    /**
     * Initializes client with socket and players position.
     *
     * @param id Client id.
     * @param socket Receive socket.
     * @param players Players in the game.
     */
    public ClientThread(int id, Socket socket, Player[] players) {
        this.id = id;
        this.socket = socket;
        this.players = players;
    }

    @Override
    public void run() {
        // Initialize reader and communicator.
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            communicator = new Communicator(dataInputStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Receive data from player.
        while (true) try {
            if (dataInputStream.available() > 0) {
                players[id].setDirection((int) communicator.receive());
                players[id].setUpdate(true);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}

package projectscopes;

import java.io.*;
import java.net.Socket;

/**
 * @author Tomasz Najbar
 *
 * Creates a thread for client.
 */
public class ClientThread implements Runnable {
    // Id.
    private int id = -1;

    // Receive socket.
    private Socket socket = null;

    // Players.
    private Player[] players = null;

    // Reader.
    private DataInputStream dataInputStream = null;

    /**
     * Initializes client with id, socket and players position.
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
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Receive data from player.
        while (true) try {
            if (dataInputStream.available() > 0) {
                //long startTime = System.nanoTime();

                players[id].setDirection(dataInputStream.readInt());
                players[id].setUpdate(true);

                //long endTime = System.nanoTime() - startTime;
                //System.out.println("CLIENT RECEIVE: " + endTime + " ns");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

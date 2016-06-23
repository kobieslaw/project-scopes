package projectscopes;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Tomasz Najbar
 *
 * Creates thread for Server.
 */
public class ServerThread extends Thread {
    // Port on which server will listen for client communication.
    int portNumber = -1;

    // Number of players that will participate in the game
    int noOfPlayers = -1;

    // Listener.
    ServerSocket serverSocket = null;

    // Output stream.
    DataOutputStream[] dataOutputStreams = null;

    // Players.
    private Player[] players = null;

    // Each Player current coordinates.
    private LineSegment[] lineSegments;

    // Current configuation.
    private Configurator configurator = null;

    // Indicates when new data should be send to players.
    private boolean upadate = false;

    // Scene for collision detection.
    private final Pane pane = new Pane();

    // Collision detector.
    private CollisionDetector collisionDetector = null;

    /**
     * Initializes server with port number and number of players.
     *
     * @param portNumber Port on which server will listen for client communication.
     * @param noOfPlayers Number of players that will participate in the game.
     */
    public ServerThread(int portNumber, int noOfPlayers, Configurator configurator) {
        this.portNumber = portNumber;
        this.noOfPlayers = noOfPlayers;

        dataOutputStreams = new DataOutputStream[noOfPlayers];

        players = new Player[noOfPlayers];
        // Initialize start positions.
        this.configurator = configurator;
        int playerSize = configurator.getPlayerSize();
        for (int i = 0; i < noOfPlayers; ++i) {
            double x = playerSize + (Math.random() * (configurator.getSceneWidth() - 2 * playerSize));
            double y = playerSize + (Math.random() * (configurator.getSceneHeight() - 2 * playerSize));
            players[i] = new Player(i, x, y, Math.random(), playerSize, configurator.getPlayerSpeed(), Color.BLACK);
        }

        lineSegments = new LineSegment[noOfPlayers];

        collisionDetector = new CollisionDetector(configurator.getPlayerSize());

        // Set pane size.
        pane.setLayoutX(configurator.getSceneWidth());
        pane.setLayoutY(configurator.getSceneHeight());
    }

    @Override
    public void run() {
        // Initialize server socket.
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Wait for all players.
        System.out.println("\nWaiting for players.");
        Socket[] sockets = new Socket[noOfPlayers];
        int connectedPlayers = 0;
        while (connectedPlayers < noOfPlayers) try {
            sockets[connectedPlayers] = serverSocket.accept();
            dataOutputStreams[connectedPlayers] = new DataOutputStream(sockets[connectedPlayers].getOutputStream());

            // Start new client thread.
            new ClientThread(connectedPlayers, sockets[connectedPlayers], players).start();

            ++connectedPlayers;

            if (connectedPlayers < noOfPlayers) {
                System.out.println("\nNew player connected. Waiting for " + (noOfPlayers - connectedPlayers) + " more players.");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        System.out.println("\nAll players successfully connected.\n");

        // Create and initialize communicators.
        Communicator[] communicators = new Communicator[noOfPlayers];
        for (int i = 0; i < communicators.length; ++i) {
            communicators[i] = new Communicator(dataOutputStreams[i]);
        }

        // Send client id and number of players to each client.
        int id = 0;
        for (Communicator communicator : communicators) try {
            communicator.send(id++);
            communicator.send(noOfPlayers);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Send current players location to clients.
        while (true) {
            // Check if all players have updated positions.
            boolean send = false;
            while (!send) {
                send = true;
                for (Player player : players) {
                    if (player != null && !player.getUpdate()) {
                        send = false;
                    }
                }
            }

            // Stop the game if only one player left.
            if (noOfPlayers < 2) {
                break;
            }

            // Calculate each player new position.
            for (Player player : players) {
                // Skip player if collided.
                if (player == null) {
                    continue;
                }

                player.move();

                double vectorX = Math.sin(player.getDirection());
                double vectorY = Math.cos(player.getDirection());
                lineSegments[player.getId()] = new LineSegment(player.getX() + vectorY * player.getSize(),
                                                               player.getY() - vectorX * player.getSize(),
                                                               player.getX() - vectorY * player.getSize(),
                                                               player.getY() + vectorX * player.getSize());

                player.setUpdate(false);

                if (collisionDetector.collision(player, pane)) {
                    players[player.getId()] = null;
                    --noOfPlayers;
                    continue;
                }

                pane.getChildren().add(new Line(player.getX() + vectorY * player.getSize(),
                        player.getY() - vectorX * player.getSize(),
                        player.getX() - vectorY * player.getSize(),
                        player.getY() + vectorX * player.getSize()));
            }

            // Send data to clients.
            for (Communicator communicator : communicators) try {
                communicator.send(lineSegments);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}

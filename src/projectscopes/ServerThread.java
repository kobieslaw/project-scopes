package projectscopes;

import javafx.scene.layout.Pane;

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
public class ServerThread implements Runnable {
    // Number of players that will participate in the game
    private int noOfPlayers = -1;

    // Listener.
    private ServerSocket serverSocket = null;

    // Output stream.
    private DataOutputStream[] dataOutputStreams = null;

    // Players.
    private Player[] players = null;

    // Each Player current coordinates.
    private LineSegment[] lineSegments = null;

    // Current configuration.
    private Configurator configurator = null;

    /**
     * Initializes server.
     *
     * @param configurator Game configuration.
     */
    public ServerThread(Configurator configurator) {
        this.configurator = configurator;

        noOfPlayers = configurator.getNoOfPlayers();

        dataOutputStreams = new DataOutputStream[noOfPlayers];

        players = new Player[noOfPlayers];
        // Calculate players start positions.
        int playerSize = configurator.getInitialPlayersSize();
        for (int i = 0; i < noOfPlayers; ++i) {
            double x = playerSize + (Math.random() * (configurator.getSceneWidth() - 2 * playerSize));
            double y = playerSize + (Math.random() * (configurator.getSceneHeight() - 2 * playerSize));
            players[i] = new Player(i, x, y, Math.random(), playerSize, configurator.getInitialPlayersSpeed());
        }

        lineSegments = new LineSegment[noOfPlayers];
        for (int i = 0; i < lineSegments.length; ++i) {
            lineSegments[i] = new LineSegment(0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void run() {
        // Initialize server socket.
        try {
            serverSocket = new ServerSocket(configurator.getConnectionPortNumber());
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
            new Thread(new ClientThread(connectedPlayers, sockets[connectedPlayers], players)).start();

            ++connectedPlayers;

            if (connectedPlayers < noOfPlayers) {
                System.out.println("\nNew player connected. Waiting for " + (noOfPlayers - connectedPlayers) + " more players.");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        System.out.println("\nAll players successfully connected.");

        // Send client id, number of players and configuration to each client.
        for (int i = 0; i < noOfPlayers; ++i) try {
            dataOutputStreams[i].writeInt(noOfPlayers);
            dataOutputStreams[i].write(Serializer.convertToBytes(configurator));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Scene for collision detection.
        final Pane pane = new Pane();
        pane.setLayoutX(configurator.getSceneWidth());
        pane.setLayoutY(configurator.getSceneHeight());

        // Collision detector.
        final CollisionDetector collisionDetector = new CollisionDetector(configurator.getInitialPlayersSize());

        // Send current players location to clients.
        while (true) {
            // Check if all players have updated positions.
            //long starTime = System.nanoTime();

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
            /*if (noOfPlayers < 2) {
                break;
            }*/

            // Calculate each player new position.
            for (Player player : players) {
                // Skip player if collided.
                if (player == null) {
                    continue;
                }

                player.move();

                double vectorX = Math.sin(player.getDirection()) * player.getSize();
                double vectorY = Math.cos(player.getDirection()) * player.getSize();

                lineSegments[player.getId()].setStartX(player.getX() + vectorY);
                lineSegments[player.getId()].setStartY(player.getY() - vectorX);
                lineSegments[player.getId()].setEndX(player.getX() - vectorY);
                lineSegments[player.getId()].setEndY(player.getY() + vectorX);

                player.setUpdate(false);

                // Check for collisions.
                if (collisionDetector.collision(player, pane)) {
                    players[player.getId()] = null;
                    --noOfPlayers;
                    continue;
                }

                pane.getChildren().add(new Line(player.getX() + vectorY,
                                                player.getY() - vectorX,
                                                player.getX() - vectorY,
                                                player.getY() + vectorX));
            }

            // Send data to clients.
            for (DataOutputStream dataOutputStream : dataOutputStreams) try {
                dataOutputStream.write(Serializer.convertToBytes(lineSegments));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //long endTime = System.nanoTime() - starTime;
            //System.out.println("SERVER SEND: " + endTime + " ns");
        }
    }
}

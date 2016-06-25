package projectscopes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author Tomasz Najbar
 *
 * Runs project-scopes.
 */
public class Main extends Application {
    // User Interface.
    private static UI ui = null;

    // Host IP address.
    private static String hostIpAddress = null;

    // Game configuration.
    private static Configurator configurator = null;

    // Input/Output streams.
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;

    // Game scene.
    private final Canvas canvas = new Canvas();
    private final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    private final Pane pane = new Pane(canvas);

    // Number of players in the game.
    private int noOfPlayers = -1;

    // Current player direction (left/right/straight).
    private int direction = 0;

    // Animation.
    private final Timeline timeline = new Timeline();

    // Buffer for incoming data.
    private byte[] bytes = null;

    // New players position received from server.
    private LineSegment[] lineSegments = null;

    // Indicates if player is still running.
    private boolean running = true;

    /**
     * Create content on the scene.
     */
    private Parent createContent() throws IOException {
        // Initialize receive buffer. Client will always receive the same amount of data from server,
        // so it is ok to calculate the buffer length here.
        lineSegments = new LineSegment[noOfPlayers];
        for (int i = 0; i < lineSegments.length; ++i) {
            lineSegments[i] = new LineSegment(0.0, 0.0, 0.0, 0.0);
        }

        bytes = new byte[Serializer.convertToBytes(lineSegments).length];

        // Draw each players new position.
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0 / configurator.getAnimationSpeed()), event -> {
            //long startTime = System.nanoTime();

            // Receive data from server.
            try {
                if (dataInputStream.available() > 0) {
                    dataInputStream.readFully(bytes);
                    lineSegments = (LineSegment[]) Serializer.convertToObject(bytes);
                    for (LineSegment lineSegment : lineSegments) {
                        graphicsContext.strokeLine(lineSegment.getStartX(),
                                                   lineSegment.getStartY(),
                                                   lineSegment.getEndX(),
                                                   lineSegment.getEndY());
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            // Send current direction to server.
            try {
                dataOutputStream.writeInt(direction);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            //long endTime = System.nanoTime() - startTime;
            //System.out.println("FRAME: " + endTime + "ns");
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);

        return pane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Connect with server.
        Socket socket = new Socket(hostIpAddress, configurator.getConnectionPortNumber());

        // Initialize Input/Output streams.
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        // Sleep for 1 second.
        TimeUnit.MILLISECONDS.sleep(50);

        // Get user movement keys and run client.
        String[] movementKeys = ui.getMovementKeys();

        // Create user controller.
        Controller controller = new Controller(movementKeys[0], movementKeys[1]);

        // Wait to receive number of players and configuration from server.
        noOfPlayers = dataInputStream.readInt();

        bytes = new byte[Serializer.convertToBytes(configurator).length];
        dataInputStream.readFully(bytes);
        configurator = (Configurator)Serializer.convertToObject(bytes);

        // Create scene.
        final Scene scene = new Scene(createContent());
        pane.setPrefSize(configurator.getSceneWidth(), configurator.getSceneHeight());
        pane.setCache(true);
        pane.setCacheShape(true);
        pane.setCacheHint(CacheHint.SPEED);

        canvas.setWidth(configurator.getSceneWidth());
        canvas.setHeight(configurator.getSceneHeight());

        // React on key press/release.
        scene.setOnKeyPressed(event -> direction = controller.getNewDirection(event.getCode().toString(), true));

        scene.setOnKeyReleased(event -> direction = controller.getNewDirection(event.getCode().toString(), false));

        // Set title and show scene.
        primaryStage.setTitle("ProjectScopes");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start animation.
        timeline.play();
    }

    public static void main(String[] args) throws IOException {
        // Setup default configuration.
        configurator = new Configurator();

        // Show menu.
        ui = new UI();
        ui.showModeMenu();

        if (ui.isServer()) {
            // Set number of players.
            configurator.setNoOfPlayers(ui.getNumberOfPlayers());

            // Setup new game configuration.
            if (!ui.defaultConfiguration(configurator)) {
                configurator.setSceneWidth(ui.getSceneWidth());
                configurator.setSceneHeight(ui.getSceneHeight());
                configurator.setInitialPlayersSize(ui.getInitialPlayersSize());
                configurator.setInitialPlayersSpeed(ui.getInitialPlayersSpeed());
                configurator.setAnimationSpeed(ui.getAnimationSpeed());
                configurator.setConnectionPortNumber(ui.getConnectionPortNumber());
            }

            // Run server.
            new Thread(new ServerThread(configurator)).start();
        } else {
            // Get IP address of the server.
            hostIpAddress = ui.getHostIpAddress();
        }

        // Run application.
        launch(args);
    }
}

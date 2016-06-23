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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    // Communicator with the server.
    private Communicator communicator = null;

    // Input/Output streams.
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;

    // Game scene.
    private final Canvas canvas = new Canvas();
    private final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    private final Pane pane = new Pane(canvas);

    // Client id.
    private int id = -1;

    // Number of players in the game.
    private int noOfPlayers = -1;

    // Current player direction (left/right/straight).
    private int direction = 0;

    // Animation.
    private final Timeline timeline = new Timeline();

    /**
     * Create content on the scene.
     */
    public Parent createContent() throws IOException {
        // Draw current players position.
        final KeyFrame keyFrame = new KeyFrame(configurator.getFps60(), event -> {
            //long startTime = System.nanoTime();

            // Receive data from server.
            try {
                if (dataInputStream.available() > 0) {
                    LineSegment[] lineSegments = (LineSegment[])communicator.receive();
                    for (LineSegment lineSegment : lineSegments) {
                        graphicsContext.strokeLine(lineSegment.getStartX(),
                                                   lineSegment.getStartY(),
                                                   lineSegment.getEndX(),
                                                   lineSegment.getEndY());
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }

            // Send current direction to server.
            try {
                communicator.send(direction);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            //long endTime = System.nanoTime() - startTime;
            //System.out.println("One frame takes " + endTime + "ns");
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        return pane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Connect with server.
        Socket socket = new Socket(hostIpAddress, configurator.getPort());

        // Initialize Input/Output streams.
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        // Initialize communicator.
        communicator = new Communicator(dataInputStream, dataOutputStream);

        // Sleep for 1 second.
        TimeUnit.MILLISECONDS.sleep(50);

        // Get user movement keys and run client.
        String[] movementKeys = ui.getMovementKeys();

        // Create user controller.
        Controller controller = new Controller(movementKeys[0], movementKeys[1]);

        // Wait to receive client id from server.
        id = (int)communicator.receive();

        // Wait to receive number of players from server.
        noOfPlayers = (int)communicator.receive();

        // Create scene.
        final Scene scene = new Scene(createContent());
        pane.setPrefSize(configurator.getSceneWidth(), configurator.getSceneHeight());
        pane.setCache(true);
        pane.setCacheShape(true);
        pane.setCacheHint(CacheHint.SPEED);

        canvas.setWidth(configurator.getSceneWidth());
        canvas.setHeight(configurator.getSceneHeight());

        // React on key press/release.
        scene.setOnKeyPressed(event -> {
            direction = controller.getNewDirection(event.getCode().toString(), true);
        });

        scene.setOnKeyReleased(event -> {
            direction = controller.getNewDirection(event.getCode().toString(), false);
        });

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
            // Run server.
            new ServerThread(configurator.getPort(), ui.getNumberOfPlayers(), configurator).start();
        } else {
            // Get IP address of the server.
            hostIpAddress = ui.getHostIpAddress();
        }

        // Run application.
        launch(args);
    }
}

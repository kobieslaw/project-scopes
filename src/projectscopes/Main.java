package projectscopes;

import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Tomasz Najbar
 */
public class Main extends Application {
    // Maximum number of Players.
    private final int MAX_PLAYERS = 8;

    // Configurator.
    private static Configurator configurator;

    // Current number of Players.
    private static int noOfPlayers = 0;

    // Current player.
    private int currentPlayer = 0;

    // Players.
    private static Player[] players;

    // Controllers.
    private static Controller[] controllers;

    // Window.
    private Pane root = new Pane();

    // Animation.
    private SequentialTransition sequentialTransition = new SequentialTransition();
    private Timeline[] timelines;

    // Create and update content.
    private Parent createContent() {
        // Calculate each Player move.
        currentPlayer = 0;
        for (Player player : players) {
            final KeyFrame keyFrame = new KeyFrame(Duration.seconds(player.getSpeed()), event -> {
                player.move();

                // Draw circle.
                final Circle circle = new Circle(player.getX(), player.getY(), player.getSize(), player.getColor());
                root.getChildren().add(circle);
            });

            timelines[currentPlayer].getKeyFrames().add(keyFrame);

            ++currentPlayer;
        }

        sequentialTransition.getChildren().addAll(timelines);
        sequentialTransition.setCycleCount(Timeline.INDEFINITE);

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Scene size.
        int sceneWidth = configurator.getSceneWidth();
        int sceneHeight = configurator.getSceneHeight();

        // Initialize timeline for each Player.
        timelines = new Timeline[noOfPlayers];
        for (int i = 0; i < timelines.length; ++i) {
            timelines[i] = new Timeline();
        }

        // Set window size.
        root.setPrefSize(sceneWidth, sceneHeight);
        // Create scene.
        final Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(event -> {
            for (int i = 0; i < noOfPlayers; ++i) {
                players[i].setDirection(controllers[i].getNewDirection(event.getCode(), true));
            }
        });

        scene.setOnKeyReleased(event -> {
            for (int i = 0; i < noOfPlayers; ++i) {
                players[i].setDirection(controllers[i].getNewDirection(event.getCode(), false));
            }
        });

        // Set title and show scene.
        primaryStage.setTitle("ProjectScopes");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Run animation.
        sequentialTransition.play();
    }

    public static void main(String[] args) {
        noOfPlayers = 2;
        configurator = new Configurator(noOfPlayers);

        // Initialize Players and Controllers.
        Color[] colors = new Color[noOfPlayers];
        colors[0] = Color.BLUE;
        colors[1] = Color.RED;

        players = new Player[noOfPlayers];
        int playerSize = configurator.getPlayerSize();
        for (int i = 0; i < noOfPlayers; ++i) {
            double x = playerSize + (Math.random() * (configurator.getSceneWidth() - 2 * playerSize));
            double y = playerSize + (Math.random() * (configurator.getSceneHeight() - 2 * playerSize));
            players[i] = new Player(x, y, Math.random(), playerSize, configurator.getPlayerSpeed(), colors[i]);
        }

        controllers = new Controller[noOfPlayers];
        controllers[0] = new Controller(KeyCode.LEFT, KeyCode.DOWN);
        controllers[1] = new Controller(KeyCode.A, KeyCode.S);

        launch(args);
    }
}

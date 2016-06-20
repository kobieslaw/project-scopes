package projectscopes;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author Tomasz Najbar
 */
public class Main extends Application {
    // Maximum number of Players. To be used in the future.
    //private final int MAX_PLAYERS = 8;

    // Configurator.
    private static Configurator configurator;

    // Current number of Players.
    private static int noOfPlayers = 0;

    // Players.
    private static Player[] players;

    // Controllers.
    private static Controller[] controllers;

    // Animation.
    private ParallelTransition parallelTransition = new ParallelTransition();
    private static Timeline[] timelines;

    // Window.
    private Pane root = new Pane();

    // Collision detector.
    private static CollisionDetector collisionDetector;

    // Says if Player is still in game.
    private boolean[] running;

    // Creates and updates content.
    private Parent createContent() {
        running = new boolean[noOfPlayers];
        for (int i = 0; i < noOfPlayers; ++i) {
            running[i] = true;
        }

        // Calculate each Player move.
        for (Player player : players) {
            final KeyFrame keyFrame = new KeyFrame(configurator.getFps60(), event -> {
                if (timelines[player.getId()] != null) {
                    running[player.getId()] = true;
                    // Calculate new Player position.
                    player.move();

                    double vectorX = Math.sin(player.getDirection());
                    double vectorY = Math.cos(player.getDirection());
                    final Line line = new Line(player.getX() + vectorY * player.getSize(), player.getY() - vectorX * player.getSize(),
                            player.getX() - vectorY * player.getSize(), player.getY() + vectorX * player.getSize());
                    line.setStroke(player.getColor());

                    if (timelines[player.getId()].getStatus() == Animation.Status.RUNNING) {
                        if (collisionDetector.collision(player, root)) {
                            timelines[player.getId()] = null;
                        }
                    }

                    root.getChildren().add(line);
                }
                else {
                    running[player.getId()] = false;
                }

                // Check how many Players are still playing. If one, stop animation.
                int playersInGame = noOfPlayers;
                for (int i = 0; i < noOfPlayers; ++i) {
                    if (!running[i]) {
                        --playersInGame;
                    }
                    if (playersInGame < 2) {
                        parallelTransition.stop();
                    }
                }
            });

            // Add each Player animation frame.
            timelines[player.getId()].getKeyFrames().add(keyFrame);
        }

        // Add all timelines to the animation and play it infinitely.
        parallelTransition.getChildren().addAll(timelines);
        parallelTransition.setCycleCount(Timeline.INDEFINITE);

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create scene.
        final Scene scene = new Scene(createContent());
        root.setPrefSize(configurator.getSceneWidth(), configurator.getSceneHeight());

        // React on key press/release.
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
        parallelTransition.play();
    }

    public static void main(String[] args) {
        // Set number of players.
        noOfPlayers = 2;

        // Initialize Configurator.
        configurator = new Configurator();

        // Initalize CollisionDetector.
        collisionDetector = new CollisionDetector(configurator.getPlayerSize());

        // Initialize Players, Timelines and Controllers.
        Color[] colors = new Color[noOfPlayers];
        colors[0] = Color.BLUE;
        colors[1] = Color.RED;
        /*colors[2] = Color.BLACK;
        colors[3] = Color.GREEN;
        colors[4] = Color.PINK;
        colors[5] = Color.GRAY;
        colors[6] = Color.ORANGE;
        colors[7] = Color.PURPLE;*/

        players = new Player[noOfPlayers];
        int playerSize = configurator.getPlayerSize();
        for (int i = 0; i < noOfPlayers; ++i) {
            double x = playerSize + (Math.random() * (configurator.getSceneWidth() - 2 * playerSize));
            double y = playerSize + (Math.random() * (configurator.getSceneHeight() - 2 * playerSize));
            players[i] = new Player(i, x, y, Math.random(), playerSize, configurator.getPlayerSpeed(), colors[i]);
        }

        timelines = new Timeline[noOfPlayers];
        for (int i = 0; i < timelines.length; ++i) {
            timelines[i] = new Timeline();
        }

        controllers = new Controller[noOfPlayers];
        controllers[0] = new Controller(KeyCode.LEFT, KeyCode.DOWN);
        controllers[1] = new Controller(KeyCode.A, KeyCode.S);
        /*controllers[2] = new Controller(KeyCode.O, KeyCode.P);
        controllers[3] = new Controller(KeyCode.R, KeyCode.T);
        controllers[4] = new Controller(KeyCode.Y, KeyCode.U);
        controllers[5] = new Controller(KeyCode.C, KeyCode.V);
        controllers[6] = new Controller(KeyCode.N, KeyCode.M);
        controllers[7] = new Controller(KeyCode.K, KeyCode.L);*/

        // Run the game.
        launch(args);
    }
}

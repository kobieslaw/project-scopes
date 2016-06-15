package projectscopes;

import javafx.animation.KeyFrame;
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
public class ProjectScopes extends Application {
    
    // Scene size.
    private final int sceneWidth = 800;
    private final int sceneHeight = 800;
    
    // Circle size.
    private final int circleSize = 4;
    
    // Initial circle position.
    private double x = circleSize + (Math.random() * (sceneWidth - 2 * circleSize));
    private double y = circleSize + (Math.random() * (sceneHeight - 2 * circleSize));
    
    // Initial direction.
    private double rand =  Math.random();
    private double dirX = Math.sin(rand);
    private double dirY = Math.cos(rand);
    
    boolean keyPressed = false;
    private KeyCode keyCode = null;
    

    // Animation.
    private final Timeline timeline = new Timeline();

    // Draw scene.
    public Parent createContent() {
        final Pane root = new Pane();
        root.setPrefSize(sceneWidth, sceneHeight);
        final KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.01), event -> {  
            x += dirX;
            y += dirY;
            
            if (keyPressed) {
                switch (keyCode) {
                    case O:
                        rand += 0.025;
                        dirX = Math.sin(rand);
                        dirY = Math.cos(rand);
                        break;
                    case P:
                        rand -= 0.025;
                        dirX = Math.sin(rand);
                        dirY = Math.cos(rand);
                        break;
                    default:
                        break;
                }
            }

            final Circle circle = new Circle(x, y, circleSize, Color.BLACK);
            root.getChildren().add(circle);
        });
        
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        
        return root;
    }
    
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createContent());
        
        scene.setOnKeyPressed(event -> {
            keyPressed = true;
            keyCode = event.getCode();
        });
        
        scene.setOnKeyReleased(event -> {
            keyPressed = false;
        });

        // Create scene.
        stage.setTitle("ProjectScopes");
        stage.setScene(scene);
        stage.show();
        
        // Start animation.
        timeline.play();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

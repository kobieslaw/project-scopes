package projectscopes;

import javafx.util.Duration;

/**
 *
 * @author Tomasz Najbar
 */
public class Configurator {
    // Scene size.
    private int sceneWidth = 600;
    private int sceneHeight = 600;

    // Initial Players size.
    private int playerSize = 5;

    // Initial Players speed.
    private double playerSpeed = 1;

    // Animation spped.
    private Duration fps60 = Duration.seconds(1.0 / 60.0);

    public int getSceneWidth() {
        return sceneWidth;
    }

    public int getSceneHeight() {
        return sceneHeight;
    }

    public int getPlayerSize() {
        return playerSize;
    }

    public double getPlayerSpeed() {
        return playerSpeed;
    }

    public Duration getFps60() { return fps60; }
}

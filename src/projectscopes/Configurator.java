package projectscopes;

/**
 *
 * @author Tomasz Najbar
 */
public class Configurator {
    // Scene size.
    private int sceneWidth = 600;
    private int sceneHeight = 600;

    // Initial Players size.
    private int playerSize = 3;

    // Initial Players speed.
    private double playerSpeed = 0.01;

    /**
     * Initializes configurator with initial Players speed.
     *
     * @param noOfPlayers Number of players.
     */
    public Configurator(int noOfPlayers) {
        // Calculate Players speed.
        playerSpeed /= noOfPlayers;
    }

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
}

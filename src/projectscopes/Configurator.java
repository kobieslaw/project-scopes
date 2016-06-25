package projectscopes;

import java.io.Serializable;

/**
 * @author Tomasz Najbar
 *
 * Contains game configuration parameters.
 */
public class Configurator implements Serializable {
    // Scene size.
    private int sceneWidth = 600;
    private int sceneHeight = 600;

    // Number of players.
    private int noOfPlayers = 0;

    // Initial Players size.
    private int initialPlayersSize = 3;

    // Initial Players speed.
    private double initialPlayersSpeed = 1.25;

    // Animation speed.
    private int fps = 60;

    // Port on which the program listens for server/client communication.
    private int portNumber = 9999;

    /**
     * Sets scene width.
     *
     * @param sceneWidth Scene width in pixels.
     */
    public void setSceneWidth(int sceneWidth) { this.sceneWidth = sceneWidth; }

    /**
     * Get scene width.
     *
     * @return Scene width in pixels
    */
    public int getSceneWidth() { return sceneWidth; }

    /**
     * Sets scene height.
     *
     * @param sceneHeight Scene height in pixels.
     */
    public void setSceneHeight(int sceneHeight) { this.sceneHeight = sceneHeight; }

    /**
     * Gets scene height.
     *
     * @return Scene height in pixels.
     */
    public int getSceneHeight() { return sceneHeight; }

    /**
     * Sets number of players in game.
     *
     * @param noOfPlayers Number of players that will participate in game.
     */
    public void setNoOfPlayers(int noOfPlayers) { this.noOfPlayers = noOfPlayers; }

    /**
     * Gets numbers of players in game.
     *
     * @return Number of players that participates in game.
     */
    public int getNoOfPlayers() { return  noOfPlayers; }

    /**
     * Sets initial players size.
     *
     * @param initialPlayersSize Initial player size in pixels.
     */
    public void setInitialPlayersSize(int initialPlayersSize) { this.initialPlayersSize = initialPlayersSize; }

    /**
     * Sets initial players size.
     *
     * @return Initial player size in pixels.
     */
    public int getInitialPlayersSize() {
        return initialPlayersSize;
    }

    /**
     * Sets initial players speed.
     *
     * @param initialPlayersSpeed Initial players size in game units.
     */
    public void setInitialPlayersSpeed(double initialPlayersSpeed) { this.initialPlayersSpeed = initialPlayersSpeed; }

    /**
     * Gets initial players speed.
     *
     * @return Initial players size in game units.
     */
    public double getInitialPlayersSpeed() {
        return initialPlayersSpeed;
    }

    /**
     * Sets animation speed.
     *
     * @param fps Animation speed in frames per second.
     */
    public void setAnimationSpeed(int fps) { this.fps = fps; }

    /**
     * Gets animation speed.
     *
     * @return Animation speed in frames per second.
     */
    public int getAnimationSpeed() { return fps; }

    /**
     * Sets number of port on which the program listens for server/client communication.
     *
     * @param portNumber Port number.
     */
    public void setConnectionPortNumber(int portNumber) { this.portNumber = portNumber; }

    /**
     * Gets number of port on which the program listens for server/client communication.
     *
     * @return Port number.
     */
    public int getConnectionPortNumber() { return portNumber; }
}

package projectscopes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Tomasz Najbar
 *
 * User interface.
 */
public class UI {
    // Indicates whether a user is connected as a Server (true) or Client (false).
    private boolean server = true;

    // Reads user input.
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Checks if user is connected as a Server.
     *
     * @return True if user is connected as a Server, False otherwise.
     */
    public boolean isServer() { return server; }

    /**
     * Shows menu with Server/Client choice.
     */
    public void showModeMenu() throws IOException {
        String mode;
        do {
            System.out.print("Choose mode (1. HOST, 2. JOIN): ");
            mode = bufferedReader.readLine();
        } while (!mode.equals("1") && !mode.equals("2"));
        System.out.print("\n");

        server = mode.equals("1");
    }

    /**
     * Gets number of players.
     *
     * @return A number that indicates how many players will participate in the game.
     * @throws IOException
     */
    public int getNumberOfPlayers() throws  IOException {
        System.out.print("Number of players: ");

        return Integer.parseInt(bufferedReader.readLine());
    }

    /**
     * Gets left and right user movement keys.
     *
     * @return String array with left and right movement keys.
     * @throws IOException
     */
    public String[] getMovementKeys() throws IOException {
        String[] movementKeys = new String[2];
        System.out.println("\nSet movement keys.");
        System.out.print("LEFT: ");
        movementKeys[0] = bufferedReader.readLine().toUpperCase();
        System.out.print("RIGHT: ");
        movementKeys[1] = bufferedReader.readLine().toUpperCase();

        return movementKeys;
    }

    /**
     * Gets IP address of a host.
     *
     * @return String representation of the server IP address.
     * @throws IOException
     */
    public String getHostIpAddress() throws IOException {
        System.out.print("Host IP address: ");

        return bufferedReader.readLine();
    }

    /**
     * Results with decision if configuration should be changed from default.
     *
     * @param configurator Default configuration parameters.
     * @return True if new configuration should be setup, False otherwise.
     * @throws IOException
     */
    public boolean defaultConfiguration(Configurator configurator) throws IOException {
        System.out.println("\nDEFAULT CONFIGURATION:");
        System.out.println("Scene width | Scene height | Initial players size | Initial players speed | Frames per second | Connection port number");
        System.out.printf("%7d", configurator.getSceneWidth());
        System.out.printf("%14d", configurator.getSceneHeight());
        System.out.printf("%19d", configurator.getInitialPlayersSize());
        System.out.printf("%25.2f", configurator.getInitialPlayersSpeed());
        System.out.printf("%20d", configurator.getAnimationSpeed());
        System.out.printf("%23d\n\n", configurator.getConnectionPortNumber());

        String userInput;
        do {
            System.out.print("Setup new configuration (Y/N)? ");
            userInput = bufferedReader.readLine().toLowerCase();
        } while (!userInput.equals("y") && !userInput.equals("yes") && !userInput.equals("n") && !userInput.equals("no"));

        return userInput.equals("n") || userInput.equals("no");
    }

    /**
     * Gets scene new width.
     *
     * @return Scene width in pixels.
     * @throws IOException
     */
    public int getSceneWidth() throws IOException {
        System.out.print("Scene width: ");

        return Integer.parseInt(bufferedReader.readLine());
    }

    /**
     * Gets scene new height.
     *
     * @return Scene height in pixels.
     * @throws IOException
     */
    public int getSceneHeight() throws IOException {
        System.out.print("Scene height: ");

        return Integer.parseInt(bufferedReader.readLine());
    }

    /**
     * Gets new initial players size.
     *
     * @return Initial players size in pixels.
     * @throws IOException
     */
    public int getInitialPlayersSize() throws IOException {
        System.out.print("Initial player size: ");

        return Integer.parseInt(bufferedReader.readLine());
    }

    /**
     * Gets new initial players speed.
     *
     * @return Initial players speed in game units.
     * @throws IOException
     */
    public double getInitialPlayersSpeed() throws IOException {
        System.out.print("Initial player speed: ");

        return Double.parseDouble(bufferedReader.readLine());
    }

    /**
     * Gets new animation speed.
     *
     * @return Animation speed in frames per second.
     * @throws IOException
     */
    public int getAnimationSpeed() throws IOException {
        System.out.print("Animation speed: ");

        return Integer.parseInt(bufferedReader.readLine());
    }

    /**
     * Gets connection port number.
     *
     * @return Port number.
     * @throws IOException
     */
    public int getConnectionPortNumber() throws IOException {
        System.out.print("Connection port number: ");

        return Integer.parseInt(bufferedReader.readLine());
    }
}

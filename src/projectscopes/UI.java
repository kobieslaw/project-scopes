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
    boolean server = true;

    // Reads user input.
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public boolean isServer() { return server; }

    /**
     * Shows menu with Server/Client choice.
     */
    public void showModeMenu() throws IOException {
        String mode = null;
        do {
            System.out.print("Choose mode (1. HOST, 2. JOIN): ");
            mode = bufferedReader.readLine();
        } while (!mode.equals("1") && !mode.equals("2"));
        System.out.print("\n");

        if (mode.equals("1")) {
            server = true;
        } else {
            server = false;
        }
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
}

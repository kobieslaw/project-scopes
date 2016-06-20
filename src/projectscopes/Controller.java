package projectscopes;

import javafx.scene.input.KeyCode;

/**
 *
 * @author Tomasz Najbar
 */
public class Controller {
    // Player turning keys.
    private KeyCode left;
    private KeyCode right;

    // Player new direction.
    private double direction = 0.0;

    // Checks if left or right is pressed.
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    /**
     * Initializes controller with Player turning keys.
     *
     * @param left Left turn.
     * @param right Right turn.
     */
    public Controller(KeyCode left, KeyCode right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Gets new player direction.
     *
     * @param keyCode Key pressed/released by Player.
     * @param keyPressed Determines whether key was pressed or released.
     * @return New Player direction.
     */
    public double getNewDirection(KeyCode keyCode, boolean keyPressed) {
        if (keyCode == left) {
            if (keyPressed) {
                leftPressed = true;
                direction = +0.025;
            } else {
                leftPressed = false;
                if (!rightPressed) {
                    direction = 0.0;
                }
            }
        } else if (keyCode == right) {
            if (keyPressed) {
                rightPressed = true;
                direction = -0.025;
            } else {
                rightPressed = false;
                if (!leftPressed) {
                    direction = 0.0;
                }
            }
        }

        return direction;
    }
}

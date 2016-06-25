package projectscopes;

/**
 * @author Tomasz Najbar
 *
 * Controlls player actions.
 */
public class Controller {
    // Player turning keys.
    private String left;
    private String right;

    // Player new direction.
    private int direction = 0;

    // Checks if left or right is pressed.
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    /**
     * Initializes controller with Player movement keys.
     *
     * @param left Left turn.
     * @param right Right turn.
     */
    public Controller(final String left, final String right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Gets new player direction (left/right/straight).
     *
     * @param keyCode Key pressed/released by Player.
     * @param keyPressed Indicates whether a key was pressed or released.
     * @return New Player direction.
     */
    public int getNewDirection(String keyCode, boolean keyPressed) {
        if (keyCode.equals(left)) {
            if (keyPressed) {
                leftPressed = true;
                direction = +1;
            } else {
                leftPressed = false;
                if (!rightPressed) {
                    direction = 0;
                }
            }
        } else if (keyCode.equals(right)) {
            if (keyPressed) {
                rightPressed = true;
                direction = -1;
            } else {
                rightPressed = false;
                if (!leftPressed) {
                    direction = 0;
                }
            }
        }

        return direction;
    }
}

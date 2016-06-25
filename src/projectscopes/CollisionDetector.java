package projectscopes;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomasz Najbar
 *
 * Checks for collision on scene.
 */
public class CollisionDetector {
    // Initial Player size. Used to calculate number of points to be verified in case of collision.
    private int initialPlayerSize = 0;

    // Points to be verified in case of collision.
    private List<int[]> points;

    /**
     * Checks if Player hit the boarder.
     *
     * @param w Scene width.
     * @param h Scene height.
     * @return True in case of collision, otherwise False.
     */
    private boolean boarderCollision(int w, int h) {
        for (int[] point : points) {
            if (point[0] < 0 || point[0] > w || point[1] < 0 || point[1] > h) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if Player collided with other Player or with itself.
     *
     * @param nodes All objects on the arena.
     * @return True in case of collision, otherwise False.
     */
    private boolean playerCollision(ObservableList<Node> nodes) {
        for (Node node : nodes) {
            for (int[] point : points) {
                if (node.contains(point[0], point[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Initializes CollisionDectector with initial Player size,
     * which will be used to calculate collision points.
     *
     * @param initialPlayerSize Size of the Player at the beginning of the game.
     */
    public CollisionDetector(int initialPlayerSize) {
        this.initialPlayerSize = initialPlayerSize;
    }

    /**
     * Checks if Player collided with other object.
     *
     * @param player Current player.
     * @param pane Current scene.
     * @return True in case of collision, otherwise False.
     */
    public boolean collision(Player player, Pane pane) {
        // Defines the distance between Player and colliding object.
        int colliderFactor = (int)player.getSpeed() + 1;

        // Create list of points to be verified.
        double vectorX = Math.sin(player.getDirection());
        double vectorY = Math.cos(player.getDirection());

        points = new ArrayList<>();
        points.add(new int[]{(int)(player.getX() + vectorX * colliderFactor), (int)(player.getY() + vectorY * colliderFactor)});
        for (int i = 0; i < player.getSize() / initialPlayerSize; ++i) {
            int xLeft = (int)(player.getX() + vectorX * colliderFactor + vectorY * initialPlayerSize * (i + 1));
            int yLeft = (int)(player.getY() + vectorY * colliderFactor - vectorX * initialPlayerSize * (i + 1));
            points.add(new int[]{xLeft, yLeft});

            int xRight = (int)(player.getX() + vectorX * colliderFactor - vectorY * initialPlayerSize * (i + 1));
            int yRight = (int)(player.getY() + vectorY * colliderFactor + vectorX * initialPlayerSize * (i + 1));
            points.add(new int[]{xRight, yRight});
        }

        return boarderCollision((int)pane.getLayoutX(), (int)pane.getLayoutY()) || playerCollision(pane.getChildrenUnmodifiable());
    }
}

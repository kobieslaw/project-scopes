package projectscopes;

/**
 * @author Tomasz Najbar
 *
 * Contains information about player.
 */
public class Player {
    // Id.
    private int id = 0;

    // Size.
    private int size = 0;

    // Speed.
    private double speed = 0.0;

    // Position.
    private double x = 0.0;
    private double y = 0.0;

    // Direction.
    private int direction = 0;
    private double factor = 0.0;
    private double vectorX = 0.0;
    private double vectorY = 0.0;

    // indicates if player should make a move.
    private boolean update = false;

    /**
     * Creates player instance.
     *
     * @param id Player identifier.
     * @param x Initial position on X axis.
     * @param y Initial position on Y axis.
     * @param factor Defines direction.
     * @param size Initial size.
     * @param speed Initial speed.
     */
    public Player(final int id, final double x, final double y, final double factor, final int size, final double speed) {
        this.id = id;

        this.x = x;
        this.y = y;

        // Calculate initial direction.
        this.factor = factor;
        vectorX = Math.sin(factor);
        vectorY = Math.cos(factor);

        this.size = size;
        this.speed = speed;
    }

    /**
     * Gets id of a player.
     *
     * @return Player id.
     */
    public int getId() { return id; }

    /**
     * Gets player position on X axis.
     *
     * @return Player position on X axis in pixel.
     */
    public double getX() {return x; }

    /**
     * Gets player position on Y axis.
     *
     * @return Player position on Y axis in pixels.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets player current size.
     *
     * @return Size of a player in pixels.
     */
    public int getSize() { return size; }

    /**
     * Gets player current speed.
     *
     * @return Player speed.
     */
    public double getSpeed() { return speed; }

    /**
     * Sets player new movement direction (-1 - left, 0 - straight, 1 - right).
     *
     * @param direction Direction in game units.
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Gets player current direction.
     *
     * @return Direction in game units.
     */
    public double getDirection() { return factor; }

    /**
     * Indicates if player should update its position (true - ready to move, false - do not move).
     *
     * @param update Boolean value that indicates if player should make a move.
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }

    /**
     * Gets info about player readiness to move.
     *
     * @return True if player should update its position, False otherwise.
     */
    public boolean getUpdate() {
        return update;
    }

    /**
     * Calculates player new position.
     */
    public void move() {
        if (direction != 0) {
            factor += direction * 0.035;
            vectorX = Math.sin(factor);
            vectorY = Math.cos(factor);
        }

        x += vectorX * speed;
        y += vectorY * speed;
    }
}

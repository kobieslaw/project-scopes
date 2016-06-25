package projectscopes;

import java.io.Serializable;

/**
 * @author Tomasz Najbar
 *
 * Contains start and end points coordinates of line segment.
 */
public class LineSegment implements Serializable {
    // Start and end points coordinates.
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    /**
     * Initializes LineSegment with start and end points coordinates.
     *
     * @param startX Start point coordinate on X axis.
     * @param startY Start point coordinate on Y axis.
     * @param endX End point coordinate on X axis.
     * @param endY End point coordinate on Y axis.
     */
    public LineSegment(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    /**
     * Sets X coordinate of start point.
     *
     * @param startX Coordinate on X axis.
     */
    public void setStartX(double startX) { this.startX = startX; }

    /**
     * Gets X coordinate of start point.
     *
     * @return Start point coordinate on X axis.
     */
    public double getStartX() { return startX; }

    /**
     * Sets Y coordinate of start point.
     *
     * @param startY Coordinate on Y axis.
     */
    public void setStartY(double startY) { this.startY = startY; }

    /**
     * Gets Y coordinate of start point.
     *
     * @return Start point coordinate on Y axis.
     */
    public double getStartY() { return startY; }

    /**
     * Sets X coordinate of end point.
     *
     * @param endX Coordinate on X axis.
     */
    public void setEndX(double endX) { this.endX = endX; }

    /**
     * Gets X coordinate of end point.
     *
     * @return End point coordinate on X axis.
     */
    public double getEndX() { return endX; }

    /**
     * Sets Y coordinate of end point.
     *
     * @param endY Coordinate on Y axis.
     */
    public void setEndY(double endY) { this.endY = endY; }

    /**
     * Gets Y coordinate of end point.
     *
     * @return End point coordinate on Y axis.
     */
    public double getEndY() { return endY; }
}

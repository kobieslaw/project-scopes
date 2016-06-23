package projectscopes;

import java.io.Serializable;

/**
 *
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

    public double getStartX() { return startX; }

    public double getStartY() { return startY; }

    public double getEndX() { return endX; }

    public double getEndY() { return endY; }
}

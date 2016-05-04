package flocking;

import java.awt.geom.Point2D;

/**
 * A more concise alternative to the CartesianCoordinate class used in the labs. Provides some helpful functionality
 * for operations between points, and extends Point2D so that it can be used in the Swing 2D drawing system
 * @author Y3761870
 */
public class Point extends Point2D.Double {
    Point(double x, double y) {
        super(x, y);
    }

    /**
     * Gives the angle from this point to a provided point
     * @param p Point to compare with
     * @return double Angle in radians from -180degrees to 180degrees
     */
    public double angleTowards(Point2D p) {
        return (Math.atan2(p.getY() - this.y, p.getX() - this.x));
    }

    /**
     * Add the location of this point to the location of a provided point
     * @param p Point to add to
     */
    void add(Point2D p) {
        this.x += p.getX();
        this.y += p.getY();
    }

    /**
     * Divide the location of this point by a given divisor (useful for averages)
     * @param divisor Double to divide by
     */
    void divide(double divisor) {
        this.x /= divisor;
        this.y /= divisor;
    }
}
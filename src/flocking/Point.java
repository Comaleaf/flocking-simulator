package flocking;

import java.awt.geom.Point2D;

/**
 * Created by Y3761870 on 28/04/2016.
 */
public class Point extends Point2D.Double {
    Point(double x, double y) {
        super(x, y);
    }

    double angleTowards(Point2D p) {
        return (Math.atan2(p.getY() - this.y, p.getX() - this.x));
    }

    void add(Point2D p) {
        this.x += p.getX();
        this.y += p.getY();
    }

    void subtract(Point2D p) {
        this.x -= p.getX();
        this.y -= p.getY();
    }

    void divide(double divisor) {
        this.x /= divisor;
        this.y /= divisor;
    }
}
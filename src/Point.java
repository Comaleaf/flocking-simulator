import java.awt.geom.Point2D;

/**
 * Created by lauren on 28/04/2016.
 */
public class Point extends Point2D.Double {
    Point(double x, double y) {
        super(x, y);
    }

    double angleTowards(Point2D p) {
        double theta = (Math.atan2(p.getY() - this.y, p.getX() - this.x));

        // atan2 ranges from -180 to 180 degrees - we want 0-360 degrees
        if (theta < 0)
            theta += Math.PI * 2;

        return theta;
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
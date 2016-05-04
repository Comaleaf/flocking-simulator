package flocking;

/**
 *
 */
public class Util {
    public final static double TWO_PI = Math.PI*2;

    public static double wrapAngle(double angle) {
        return angle - (TWO_PI * Math.floor((angle + Math.PI) / TWO_PI));
    }
}

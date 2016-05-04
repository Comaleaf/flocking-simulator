package flocking;

/**
 * Provides some utilities for working with angles
 */
public class Util {
    public final static double TWO_PI = Math.PI*2;

    /**
     * Normalises any angle into the -180 degrees to 180 degrees range
     * @param angle Angle in radians
     * @return Angle in radians between -180 degrees and 180 degrees
     */
    public static double wrapAngle(double angle) {
        return angle - (TWO_PI * Math.floor((angle + Math.PI) / TWO_PI));
    }
}

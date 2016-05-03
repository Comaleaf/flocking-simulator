package flocking;

/**
 * Created by Y3761870 on 30/04/2016.
 */
public class Util {
    public final static double TWO_PI = Math.PI*2;

    public final static double wrapAngle(double angle) {
        return angle - (TWO_PI * Math.floor((angle + Math.PI) / TWO_PI));
    }
}

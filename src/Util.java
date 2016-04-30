/**
 * Created by lauren on 30/04/2016.
 */
class Util {
    final static double TWO_PI = Math.PI*2;

    final static double wrapAngle(double angle) {
        return angle - (TWO_PI * Math.floor((angle + Math.PI) / TWO_PI));
    }
}

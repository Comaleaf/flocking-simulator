package flocking.behaviours;

import flocking.*;
import java.awt.MouseInfo;
import java.awt.Component;
import javax.swing.SwingUtilities;

/**
 * Created by Y3761870 on 29/04/2016.
 */
public class MouseLove implements Behaviour {

    private double factor = 0.0;
    private Component origin;

    public MouseLove(Component origin) {
        this.origin = origin;
    }

    public String toString() {
        return "Mouse Love";
    }

    public double targetAngle(Agent a) {
        java.awt.Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, this.origin);
        double angle = a.position.angleTowards(p);

        return Util.wrapAngle(a.angle - angle) * this.factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

}
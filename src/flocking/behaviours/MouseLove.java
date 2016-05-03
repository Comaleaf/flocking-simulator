package flocking.behaviours;

import flocking.*;
import java.awt.MouseInfo;
import java.awt.Component;
import javax.swing.SwingUtilities;

/**
 * MouseLove ("Go to Mouse") turns the agent towards the current mouse pointer location.
 * @author Y3761870
 */
public class MouseLove extends ScaledBehaviour {
    private Component origin;

    public MouseLove(Component origin) {
        // Need to set an origin because MouseInfo locations are OS-wide not relative to the frame
        this.origin = origin;
    }

    public String toString() {
        // Something mildly descriptive
        return "Go to Mouse";
    }

    @Override
    protected double unscaledAngle(Agent a) {
        // Make sure to use awt's Point class and not the package's
        java.awt.Point p = MouseInfo.getPointerInfo().getLocation();
        // Convert from OS-wide position to local position
        SwingUtilities.convertPointFromScreen(p, this.origin);
        // And give the angle from the agent relative to cursor position
        return a.angle - a.position.angleTowards(p);
    }
}
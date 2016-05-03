package flocking.behaviours;

import flocking.*;

/**
 * The ScaleBehaviour class implements the Behaviour interface whilst providing common functionality for
 * behaviours to use scaling factors.
 * @author Y3761870
 */
public abstract class ScaledBehaviour implements Behaviour {
    // The amount that the angle to turn to will be scaled by this
    private double factor = 0.0;

    // The user interface uses this method to give a label to each behaviour
    public String toString() {
        // In most cases it's sufficient to just use the class name
        return this.getClass().getSimpleName();
    }

    public double targetAngle(Agent a) {
        // Short-circuit all the execution if the result would just be zero anyway
        if (this.factor == 0.0)
            return 0.0;

        // Make sure to normalise the angle from -180 to 180 degrees before scaling, otherwise it might not
        // turn the closest path
        return Util.wrapAngle(this.unscaledAngle(a)) * this.factor;
    }

    // Subclasses should override this to give the particular angle pre-scaling - the targetAngle method when
    // invoked by the Agent will scale this automatically
    protected double unscaledAngle(Agent a) {
        return 0.0;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}

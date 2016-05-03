package flocking.behaviours;

import flocking.*;

/**
 * The ScaleBehaviour class implements the Behaviour interface whilst providing common functionality for
 * behaviours to use scaling factors.
 * @author Y3761870
 */
public abstract class ScaledBehaviour implements Behaviour {
    private double factor = 0.0;

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public double targetAngle(Agent a) {
        return Util.wrapAngle(this.unscaledAngle(a)) * this.factor;
    }

    protected double unscaledAngle(Agent a) {
        return 0.0;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}

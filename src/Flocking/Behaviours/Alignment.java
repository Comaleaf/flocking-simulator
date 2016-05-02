package flocking.behaviours;

import flocking.*;

/**
 * Created by Y3761870 on 30/04/2016.
 */
public class Alignment implements Behaviour {
    private double factor = 0.0;

    public String toString() {
        return "Alignment";
    }

    public double targetAngle(Agent a) {
        return Util.wrapAngle(a.angle - a.averageAngle()) * this.factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}

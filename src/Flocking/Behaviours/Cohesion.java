package flocking.behaviours;

import flocking.*;

/**
 * Created by Y3761870 on 29/04/2016.
 */
public class Cohesion implements Behaviour {

    private double factor = 0.0;

    public String toString() {
        return "Cohesion";
    }

    public double targetAngle(Agent a) {
        return Util.wrapAngle(a.angle - a.centre_of_mass_angle) * this.factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

}
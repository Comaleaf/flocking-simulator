package flocking.behaviours;

import flocking.*;

/**
 * Created by Y3761870 on 30/04/2016.
 */
public class Separation extends ScaledBehaviour {
    @Override
    public double unscaledAngle(Agent a) {
        return a.angle - a.centre_of_mass_angle + Math.PI;
    }
}

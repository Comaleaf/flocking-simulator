package flocking.behaviours;

import flocking.*;

/**
 * Separation turns away from the centre of mass of visible agents. The opposite of cohesion.
 * @author Y3761870
 */
public class Separation extends ScaledBehaviour {
    @Override
    public double unscaledAngle(Agent a) {
        return a.angle - a.centre_of_mass_angle + Math.PI;
    }
}

package flocking.behaviours;

import flocking.*;

/**
 * Cohesion turns the agent towards the centre of gravity of adjacent visible agents.
 * @author Y3761870
 */
public class Cohesion extends ScaledBehaviour {
    @Override
    protected double unscaledAngle(Agent a) {
        // The angle to the centre of mass of the agent, relative to its own angle
        return a.angle - a.centre_of_mass_angle;
    }
}
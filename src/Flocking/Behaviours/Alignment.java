package flocking.behaviours;

import flocking.*;

/**
 * Alignment behaviour class. Alignment turns towards the average angle of all adjacent agents.
 * @author Y3761870
 */
public class Alignment extends ScaledBehaviour {
    @Override
    protected double unscaledAngle(Agent a) {
        // Just take the average angle from the agent, relative to the agent's angle
        return a.angle - a.findAverageAngle();
    }
}
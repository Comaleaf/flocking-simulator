package flocking.behaviours;

import flocking.*;

/**
 * Alignment behaviour class. Alignment turns towards the average angle of all adjacent agents.
 * @author Y3761870
 */
public class Alignment extends ScaledBehaviour {
    protected double unscaledAngle(Agent a) {
       return a.angle - a.averageAngle();
    }
}
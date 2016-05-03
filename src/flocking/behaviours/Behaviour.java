package flocking.behaviours;

import flocking.Agent;

/**
 * @author Y3761870
 */
public interface Behaviour {
    /**
     * Finds the target angle for this behaviour, and scales it by the current scaling factor.
     * @param agent The agent to determine the target angle for
     * @return The angle to turn towards (scaled by the behaviour factor)
     */
    double targetAngle(Agent agent);

    /**
     * Sets the current behaviour scaling factor
     * @param factor The new behaviour scaling factor
     */
    void setFactor(double factor);
}
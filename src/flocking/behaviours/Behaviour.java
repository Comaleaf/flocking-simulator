package flocking.behaviours;

import flocking.Agent;

/**
 * Created by Y3761870 on 29/04/2016.
 */
public interface Behaviour {
    double targetAngle(Agent agent);
    void setFactor(double factor);
}
package flocking;

import flocking.behaviours.Behaviour;

import java.util.stream.Stream;
import java.util.*;

/**
 * An Agent (sometimes called a boid) each work separately as part of the simulation. They determine their own
 * position and updates using a list of behaviours given to them. The `Swarm` class manages these so consult
 * that for more information.
 * @author Y3761870
 */
public class Agent {
    // (Radians) A turn has to be greater than this angle for the agent to move
    private final static double turn_threshold_angle = 0.1;
    private final static double max_angular_vel = 0.004;
    private final static double angular_accel = 0.00005;

    // Storing the square of the view distance means visibility testing doesn't need to do a sqrt operation
    private double view_distance_sq = 22500.0;
    private double view_distance = 150.0;

    // All the current neighbours (maintained by the swarm using the Agent.canSee method
    List<Agent> neighbours;
    // List of the current behaviours that the agent uses to update its angle
    List<Behaviour> behaviours;

    // Current position/state of the angle in cartesian space
    public Point position = null;
    public double angle   = 0.0;
    double velocity       = 0.0;
    private double angular_vel    = 0.0;

    // These are updated every time the update() method is called
    public double centre_of_mass_angle = 0.0;
    Point         centre_of_mass       = null;

    Agent(Point position, double angle) {
        this.neighbours = new ArrayList<Agent>();
        this.position = position;
        this.angle = angle;
    }

    // This needs an accessor because the property is 'managed' by the class for performance reasons.
    // See the description for setAllViewDistance(double)

    /**
     * This needs an accessor because the property is 'managed' by the class for performance reasons. See
     * description for `setAllViewDistance` for more information.
     * @return Current view distance radius
     */
    double getViewDistance() {
        return this.view_distance;
    }

    /**
     * The reason this mutator manages the view instead of the property being exposed like in other cases is
     * so that the object can keep its view_distance_sq property consistent  with the view_distance property.
     * This is needed to avoid having to do sqrt() operations when calculating agent visiblity.
     * @param view_distance New radius of Agent's vision
     */
    void setViewDistance(double view_distance) {
        this.view_distance = view_distance;
        this.view_distance_sq = view_distance * view_distance;
    }

    /**
     * Determines whether an Agent can see another Agent.
      * @param a Agent to compare to
     * @return True if the agent given is within the viewing distance of this agent, false otherwise
     */
    boolean isWithinViewDistance(Agent a) {
        return (this.position.distanceSq(a.position) <= this.view_distance_sq);
    }

    /**
     * Finds the centre of mass of visible neighbours to this agent.
     * @return Point of centre of mass of visible neighbours to this agent
     */
    private Point findCentreOfMass() {
        // If there are no neighbours, there's no centre of mass, so return null
        if (neighbours.size() == 0)
            return null;

        // Otherwise it's just a straightforward average operation
        // This is the accumulator
        Point centre_of_mass = new Point(0.0, 0.0);

        // Sum all the neighbour locations
        for (Agent neighbour : this.neighbours)
            centre_of_mass.add(neighbour.position);

        // And then divide to take the average
        centre_of_mass.divide(neighbours.size());

        return centre_of_mass;
    }

    /**
     * Finds the average angle of all visible neighbour agents
     * @return double containing the average angle
     */
    public double findAverageAngle() {
        // If there are neighbours, give the average
        // Otherwise just give the current angle
        return this.neighbours.stream()
                .mapToDouble(a -> a.angle)
                .average()
                .orElse(this.angle);
    }

    /**
     * Gives the angle to the current centre of mass for visible neighbours of this agent
     * @return double containing angle in radians towards the centre of mass
     */
    private double findCentreOfMassAngle() {
        // If there's no centre of mass just return the angle that the agent is already at
        if (this.centre_of_mass == null)
            return this.angle;

        // Otherwise it's straightforward to determine the angle
        else
            return this.position.angleTowards(this.centre_of_mass);
    }

    /**
     * This should be invoked by the swarm to progress each Agent to the next tick or series of ticks.
     * The only 'gotcha' here is that the longer the time delta is, the more the interpolation will differ
     * from what a real-time simulation would give. It's best to keep it as short as possible
     * @param time_delta How far proportionally to update the agent since the last time
     */
    void update(long time_delta) {
        // Update the centre of mass properties
        this.centre_of_mass = findCentreOfMass();
        this.centre_of_mass_angle = findCentreOfMassAngle();

        // We need to find the adjustments from all the agent behaviours
        double adjustment = 0.0;

        // Just sum them - the behaviours should scale themselves
        for (Behaviour b : this.behaviours) {
            adjustment += b.targetAngle(this);
        }

        // Then wrap the results from -180degrees to 180degrees
        adjustment = Util.wrapAngle(adjustment);

        // If the desired angle is very close to the current one, don't bother turning more (otherwise the
        // agents will never stop turning). There is a class constant that determines the threshold.
        // An acceleration amount is used so that changes to angles are smooth and not instantenous.
        if (Math.abs(adjustment) < Agent.turn_threshold_angle) {
            // Slow down
            if (this.angular_vel > 0)
                // Make sure this doesn't overshoot
                this.angular_vel = Math.max(0, this.angular_vel - Agent.angular_accel * time_delta);
            else if (this.angular_vel < 0)
                this.angular_vel = Math.min(0, this.angular_vel + Agent.angular_accel * time_delta);
        }
        else
        // If it's bigger than the threshold, turn left
        if (adjustment > 0) {
            // Math.max (and Math.min below) are used to cap the turning velocity at a maximum so that the
            // Agent doesn't end up spinning at incredibly high speeds
            this.angular_vel = Math.max(-Agent.max_angular_vel, this.angular_vel - Agent.angular_accel * time_delta);
        }
        // If it's less, turn right
        else {
            this.angular_vel = Math.min(Agent.max_angular_vel, this.angular_vel + Agent.angular_accel * time_delta);
        }

        // Update position vector and angle
        this.angle = Util.wrapAngle(this.angle + this.angular_vel * time_delta);
        this.position.x += Math.cos(this.angle) * this.velocity * time_delta;
        this.position.y += Math.sin(this.angle) * this.velocity * time_delta;
    }
}
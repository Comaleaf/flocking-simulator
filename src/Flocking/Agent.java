package flocking;

import flocking.behaviours.Behaviour;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Y3761870 on 28/04/2016.
 */
public class Agent {
    // (Radians) A turn has to be greater than this angle for the agent to move
    final static double turn_threshold_angle = 0.1;

    // Storing the square of the view distance means visibility testing doesn't need to do a sqrt operation
    private double view_distance_sq = 22500.0;
    private double view_distance = 150.0;

    List<Agent> neighbours;
    List<Behaviour> behaviours;
    Point position;
    public double angle = 0.0;
    double velocity = 0.12;
    double angular_velocity = 0.0;

    public double centre_of_mass_angle = 0.0;
    public Point centre_of_mass = null;

    double angular_acceleration = 0.00005;

    Agent(Point position, double angle) {
        this.neighbours = new ArrayList<Agent>();
        this.position = position;
        this.angle = angle;
    }

    double getViewDistance() {
        return this.view_distance;
    }

    void setViewDistance(double view_distance) {
        this.view_distance = view_distance;
        this.view_distance_sq = view_distance * view_distance;
    }

    boolean canSee(Agent a) {
        return (this.position.distanceSq(a.position) <= this.view_distance_sq);
    }

    private Point centreOfMass() {
        if (neighbours.size() == 0)
            return null;

        Point centre_of_mass = new Point(0.0, 0.0);

        for (Agent neighbour : this.neighbours)
            centre_of_mass.add(neighbour.position);

        centre_of_mass.divide(neighbours.size());

        return centre_of_mass;
    }

    public double averageAngle() {
        if (neighbours.size() == 0)
            return this.angle;

        double total = 0.0;

        // Take the mean average of the angle of all neighbours
        for (Agent neighbour : this.neighbours)
            total += neighbour.angle;

        return total / neighbours.size();
    }

    private double angleToCentreOfMass() {
        if (this.centre_of_mass == null)
            return this.angle;

        else
            return this.position.angleTowards(this.centre_of_mass);
    }

    public void update(long t) {
        this.centre_of_mass = centreOfMass();
        this.centre_of_mass_angle = angleToCentreOfMass();

        double adjustment = 0.0;

        for (Behaviour b : this.behaviours) {
            adjustment += b.targetAngle(this);
        }

        adjustment = Util.wrapAngle(adjustment);

        // If the desired angle is very close to the current one, don't bother turning more
        if (Math.abs(adjustment) < Agent.turn_threshold_angle) {
            if (this.angular_velocity > 0)
                this.angular_velocity = Math.max(0, this.angular_velocity - this.angular_acceleration * t);
            else if (this.angular_velocity < 0)
                this.angular_velocity = Math.min(0, this.angular_velocity + this.angular_acceleration * t);
        }
        else
        // If it's bigger than the threshhold, turn left
        if (adjustment > 0) {
            this.angular_velocity = Math.max(-0.004, this.angular_velocity - this.angular_acceleration * t);
        }
        // If it's less, turn right
        else {
            this.angular_velocity = Math.min(0.004, this.angular_velocity + this.angular_acceleration * t);
        }

        // Update position vector and angle
        this.angle = Util.wrapAngle(this.angle + this.angular_velocity * t);
        this.position.x += Math.cos(this.angle) * this.velocity * t;
        this.position.y += Math.sin(this.angle) * this.velocity * t;
    }
}
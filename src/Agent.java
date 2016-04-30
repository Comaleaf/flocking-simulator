import com.sun.tools.javah.Mangle;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by lauren on 28/04/2016.
 */
class Agent {
    // (Radians) A turn has to be greater than this angle for the agent to move
    final static double turn_threshold_angle = 0.1;

    double view_distance_sq = 22500.0;
    double view_distance = 150.0;
    double alignment = 0.0;
    double separation = 0.0;
    double cohesion = 0.0;

    List<Agent> neighbours;
    Point position;
    double angle = 0.0;
    double velocity = 0.12;
    double angular_velocity = 0.0;

    double centre_of_mass_angle = 0.0;
    Point centre_of_mass = null;

    double angular_acceleration = 0.00005;

    Agent(Point position, double angle) {
        this.neighbours = new ArrayList<Agent>();
        this.position = position;
        this.angle = angle;
    }

    boolean canSee(Agent a) {
        return (this.position.distanceSq(a.position) <= this.view_distance_sq);
    }

    Point centreOfMass() {
        if (neighbours.size() == 0)
            return null;

        Point centre_of_mass = new Point(0.0, 0.0);

        for (Agent neighbour : this.neighbours)
            centre_of_mass.add(neighbour.position);

        centre_of_mass.divide(neighbours.size());

        return centre_of_mass;
    }

    double averageAngle() {
        if (neighbours.size() == 0)
            return this.angle;

        double total = 0.0;

        // Take the mean average of the angle of all neighbours
        for (Agent neighbour : this.neighbours)
            total += neighbour.angle;

        return total / neighbours.size();
    }

    double angleToCentreOfMass() {
        if (this.centre_of_mass == null)
            return this.angle;

        else
            return this.position.angleTowards(this.centre_of_mass);
    }

    void update(long t) {
        this.centre_of_mass = centreOfMass();
        this.centre_of_mass_angle = angleToCentreOfMass();

        double cohesion_delta   = Util.wrapAngle(this.angle - this.centre_of_mass_angle);
        double separation_delta = Util.wrapAngle(this.angle - this.centre_of_mass_angle + Math.PI);
        double alignment_delta  = Util.wrapAngle(this.angle - averageAngle());

        double adjustment = cohesion_delta*this.cohesion + separation_delta*this.separation + alignment_delta*this.alignment;

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

        this.angle += this.angular_velocity * t;

        this.angle = Util.wrapAngle(this.angle);

        this.position.x += Math.cos(this.angle) * this.velocity * t;
        this.position.y += Math.sin(this.angle) * this.velocity * t;
    }
}
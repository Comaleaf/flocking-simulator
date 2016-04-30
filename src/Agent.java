import java.util.List;
import java.util.ArrayList;

/**
 * Created by lauren on 28/04/2016.
 */
class Agent {
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

        double cohesion_delta   = wrapAngle(this.angle - this.centre_of_mass_angle);
        double separation_delta = wrapAngle(this.angle - this.centre_of_mass_angle + Math.PI);
        double alignment_delta  = wrapAngle(this.angle - averageAngle());

        double adjustment = cohesion_delta;// + separation_delta + alignment_delta;

//        double adjustment = this.angle - this.centre_of_mass_angle;

        if (adjustment < Math.PI) {
            // Turn left
            this.angular_velocity = Math.max(-0.004, this.angular_velocity - this.angular_acceleration * t);
        }
        else if (adjustment > Math.PI) {
            // Turn right
            this.angular_velocity = Math.min(0.004, this.angular_velocity + this.angular_acceleration * t);
        }
        else {
            if (this.angular_velocity > 0)
                this.angular_velocity = Math.max(0, this.angular_velocity - this.angular_acceleration * t);
            else if (this.angular_velocity < 0)
                this.angular_velocity = Math.min(0, this.angular_velocity + this.angular_acceleration * t);
        }

        this.angle += this.angular_velocity * t;

        this.angle = wrapAngle(this.angle);

        this.position.x += Math.cos(this.angle) * this.velocity * t;
        this.position.y += Math.sin(this.angle) * this.velocity * t;
    }

    static double wrapAngle(double theta) {
        // First wrap the angle
        theta = theta % (Math.PI * 2);
        // Then normalise angles below zero
        if (theta < 0) {
            return (Math.PI * 2) + theta;
        }
        // If it gets here it's just a normal angle, return it
        return theta;
    }
}
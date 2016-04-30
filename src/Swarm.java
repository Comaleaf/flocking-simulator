import java.util.*;

/**
 * Created by lauren on 29/04/2016.
 */
class Swarm extends ArrayList<Agent> {
    List<Strategy> strategies = new ArrayList<Strategy>();
    int target_population = 0;
    long time_grain = 10;
    double cohesion = 0.0;
    double separation = 0.0;
    double alignment = 0.0;

    Swarm() {
        super(2000);
    }

    void repopulate(int new_target) {
        this.target_population = new_target;
        repopulate();
    }

    void repopulate() {
        int difference = this.size() - target_population;

        if (difference > 0) {
            this.removeRange(this.size() - difference - 1, this.size() - 1);
        }
        else if (difference < 0) {
            Random rand = new Random();

            for (int i = 0; i < -difference; i++)
                this.add(new Agent(new Point(rand.nextDouble() * 800, rand.nextDouble() * 800), rand.nextDouble() * Math.PI * 2));
        }
    }

    void update(long time_delta) {
        repopulate();

        for (Agent agent : this) {
            agent.cohesion = this.cohesion;
            agent.separation = this.separation;
            agent.alignment = this.alignment;

            agent.neighbours.clear();

            for (Agent neighbour : this) {
                if (neighbour == agent)
                    continue;

                if (agent.canSee(neighbour))
                    agent.neighbours.add(neighbour);
            }

            agent.update(time_delta);
        }
    }
}

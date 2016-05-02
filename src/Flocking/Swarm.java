package flocking;

import flocking.behaviours.Behaviour;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;
import java.awt.Dimension;

/**
 * Created by Y3761870 on 29/04/2016.
 */
public class Swarm extends ArrayList<Agent> {
    public List<Behaviour> behaviours = new ArrayList<Behaviour>();
    public int target_population = 0;
    public long time_grain = 10;
    public Dimension spawn_bounds;

    public Swarm(Dimension initial_spawn_bounds) {
        super(2000);

        this.spawn_bounds = initial_spawn_bounds;
    }

    public void repopulate(int new_target) {
        this.target_population = new_target;
        repopulate();
    }

    public void repopulate() {
        int difference = this.size() - target_population;

        if (difference > 0) {
            this.removeRange(this.size() - difference - 1, this.size() - 1);
        }
        else if (difference < 0) {
            Random rand = new Random();
            Agent new_agent;

            for (int i = 0; i < -difference; i++) {
                new_agent = new Agent(
                        new Point(rand.nextDouble() * spawn_bounds.width, rand.nextDouble() * spawn_bounds.height),
                        rand.nextDouble() * Util.TWO_PI);
                new_agent.behaviours = this.behaviours;
                add(new_agent);
            }
        }
    }

    public void update(long time_delta) {
        repopulate();

        List<Agent> all_agents = this;

        // Computational complexity of updating the neighbours list is O(n^2) - this is pretty huge. With a large
        // number of agents it will take a long time to traverse the graph, regardless of optimisations to the local
        // loop. Parallelising essentially halves the time to completion with two cores (profiled).
        this.parallelStream().forEach(new Consumer<Agent>() {
            public void accept(Agent agent) {
                agent.neighbours = all_agents.stream().filter(a -> agent != a && agent.canSee(a)).collect(Collectors.toList());
                agent.update(time_delta);
            }
        });
    }
}

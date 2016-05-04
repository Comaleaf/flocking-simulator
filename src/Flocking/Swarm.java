package flocking;

import flocking.behaviours.Behaviour;

import java.util.*;
import java.util.stream.*;
import java.awt.geom.Rectangle2D;

/**
 *
 */
public class Swarm extends ArrayList<Agent> {
    // All the behaviours for each agent
    public List<Behaviour> behaviours = new ArrayList<>();
    // Change this when the swarm needs to add/remove agents - it will do it automatically
    // when it is updated
    public int target_population = 0;
    // Needed to ensure smooth animation when ticks have variable lengths in between
    private long last_updated;
    // Used to set the simulation time
    long time_grain = 10;
    // Determines whether offscreen agents are wrapped back on-screen or not
    boolean wrap_offscreen = false;

    public Scene scene;

    // Defaults
    private double velocity = 0.12;
    private double view_distance = 150.0;

    public Swarm() {
        super(2000);

        this.scene = new Scene(this);
        this.last_updated = System.nanoTime();
    }

    /**
     * Makes sure agents are always within the bounds given
     * @param a Agent to wrap
     * @param bounds Bounds to ensure agent is positioned within
     */
    private void wrapAgentToBounds(Agent a, Rectangle2D bounds) {
        while (a.position.x < bounds.getMinX()) a.position.x += bounds.getWidth();
        while (a.position.y < bounds.getMinY()) a.position.y += bounds.getHeight();
        while (a.position.x > bounds.getMaxX()) a.position.x -= bounds.getWidth();
        while (a.position.y > bounds.getMaxY()) a.position.y -= bounds.getHeight();
    }

    double getViewDistance() {
        return this.view_distance;
    }

    /**
     * Set's the velocity for all agents in the swarm
     * @param velocity New velocity
     */
    void setAllVelocity(double velocity) {
        this.velocity = velocity;
        this.stream().forEach(a -> a.velocity = velocity);
    }

    double getVelocity() {
        return this.velocity;
    }

    /**
     * Sets the view distance for all agents in the swarm
     * @param view_distance New view distance
     */
    void setAllViewDistance(double view_distance) {
        this.view_distance = view_distance;
        this.stream().forEach(a -> a.setViewDistance(view_distance));
    }

    /**
     * Adds/removes agents so that the target population is maintained. Adds them randomly dispersed over the view area
     */
    private void repopulate() {
        int difference = this.size() - target_population;

        if (difference > 0) {
            // There's too many so remove them
            this.removeRange(this.size() - difference - 1, this.size() - 1);
        }
        else if (difference < 0) {
            // There's not enough, so add them
            Random rand = new Random();
            Agent new_agent;

            Rectangle2D bounds = this.scene.getScaledVisibleRegion();

            for (int i = 0; i < -difference; i++) {
                new_agent = new Agent(
                        new Point(rand.nextDouble() * bounds.getWidth() + bounds.getX(), rand.nextDouble() * bounds.getHeight() + bounds.getY()),
                        rand.nextDouble() * Util.TWO_PI);
                new_agent.setViewDistance(this.view_distance);
                new_agent.velocity = this.velocity;
                new_agent.behaviours = this.behaviours;
                add(new_agent);
            }
        }
    }

    public void update(long framerate) {
        repopulate();

        // Need to determine how long since the last update so we can scale the movement accordingly..
        // this is to prevent visual inconsistencies in the speed of simulation when the processor is more taxed
        double time_factor = (double) (System.nanoTime() - this.last_updated) / framerate;
        final long time_delta = Math.round((double) this.time_grain * time_factor);
        this.last_updated = System.nanoTime();

        // Wrap the agents before determining neighbours otherwise the neighbours will get teleported when wrapped
        if (wrap_offscreen) {
            final Rectangle2D bounds = this.scene.getScaledVisibleRegion();
            parallelStream().forEach(a -> wrapAgentToBounds(a, bounds));
        }

        // Computational complexity of updating the neighbours list is O(n^2) - this is pretty huge. With a large
        // number of agents it will take a long time to traverse the graph, regardless of optimisations to the local
        // loop. Parallelising essentially halves the time to completion with two cores (profiled).
        parallelStream().forEach(agent -> {
            agent.neighbours = this.stream()
                    .filter(a -> agent != a && agent.isWithinViewDistance(a))
                    .collect(Collectors.toList());

            agent.update(time_delta);
        });
    }
}

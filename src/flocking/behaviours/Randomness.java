package flocking.behaviours;

import flocking.*;
import java.util.Random;

/**
 * Randomness just turns the agent by a random amount
 * @author Y3761870
 */
public class Randomness extends ScaledBehaviour {
    private Random rng = new Random();

    public String toString(){
        // Use a smaller string than 'Randomness' else it won't fit on the panel
        return "Random";
    }

    @Override
    protected double unscaledAngle(Agent a) {
        // Get a random double between 0-1 and scale it by TWO_PI (360 degrees)
        return a.angle - (rng.nextDouble() * Util.TWO_PI);
    }
}

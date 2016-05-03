package flocking.behaviours;

import flocking.*;
import java.util.Random;

/**
 * Created by Y3761870 on 30/04/2016.
 */
public class Randomness implements Behaviour {
    private double factor = 0.0;
    private Random rng = new Random();

    public String toString(){
        return "Random";
    }

    public double targetAngle(Agent a) {
        return Util.wrapAngle(a.angle - (rng.nextDouble() * Util.TWO_PI)) * this.factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}

/**
 * Created by lauren on 30/04/2016.
 */
public class Separation implements Behaviour {
    private double factor = 0.0;

    public String label () {
        return "Separation";
    }

    public double targetAngle(Agent a) {
        return a.angleToCentreOfMass() + Math.PI;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}

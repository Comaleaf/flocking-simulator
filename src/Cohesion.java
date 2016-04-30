/**
 * Created by lauren on 29/04/2016.
 */
class Cohesion implements Strategy {

    private double factor = 0.0;

    public String label () {
        return "Cohesion";
    }

    public double targetAngle(Agent a) {
        return a.angleToCentreOfMass();
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

}
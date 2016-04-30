/**
 * Created by lauren on 30/04/2016.
 */
class Alignment implements Behaviour {
    private double factor = 0.0;

    public String label () {
        return "Alignment";
    }

    public double targetAngle(Agent a) {
        return a.averageAngle();
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}

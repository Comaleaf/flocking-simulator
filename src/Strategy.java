/**
 * Created by lauren on 29/04/2016.
 */
interface Strategy {

    String label();
    double targetAngle(Agent agent);

    void setFactor(double factor);

}
package frc.team871.config;

/**
 * Class that acts as a container for PIDController parameters.
 */
public class PIDConfiguration {

    private double kp;
    private double ki;
    private double kd;
    private double inMin;
    private double inMax;
    private double outMin;
    private double outMax;
    private double tolerance;

    public PIDConfiguration(double kp, double ki, double kd, double inMin, double inMax, double outMin, double outMax, double tolerance) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.inMin = inMin;
        this.inMax = inMax;
        this.outMin = outMin;
        this.outMax = outMax;
        this.tolerance = tolerance;
    }

    public double getKp() {
        return kp;
    }

    public double getKi() {
        return ki;
    }

    public double getKd() {
        return kd;
    }

    public double getInMin() {
        return inMin;
    }

    public double getInMax() {
        return inMax;
    }

    public double getOutMin() {
        return outMin;
    }

    public double getOutMax() {
        return outMax;
    }

    public double getTolerance() {
        return tolerance;
    }
}

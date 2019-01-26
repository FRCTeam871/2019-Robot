package frc.team871.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;

public class Wrist {

    private SpeedController motor;
    //TODO: orientation sensor
    private PIDController pid;
    private AnalogInput pot;

    public Wrist(SpeedController motor, AnalogInput pot) {
        this.motor = motor;
        this.pot = pot;
    }

    public void enablePID(){
        pid.enable();
    }

    public void disablePID(){
        pid.disable();
    }

    public void setOrientation(double angle){
        pid.setSetpoint(angle);
    }

    public double getAngle(){
        return pot.getValue() / 1.1; //approx. conversion from ADC to degrees
    }
}

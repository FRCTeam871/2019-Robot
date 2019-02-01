package frc.team871.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;

public class ArmSegment {

    private  SpeedController rotateMotor;
    private AnalogPotentiometer pot;
    private double length;
    private PIDController pid;

    public ArmSegment(SpeedController rotateMotor, AnalogPotentiometer pot, double length){
        this.rotateMotor = rotateMotor;
        this.pot = pot;
        this.length = length;
        //TODO: Add Apropreate Values
        if(pot != null) {
            pid = new PIDController(0, 0, 0, pot, rotateMotor);
            pid.setInputRange(-60, 90);
            pid.setOutputRange(-1, 1);
        }
    }

    public void setAngle(double angle){
        if(pot != null) pid.setSetpoint(angle);
    }


    public double getAngle(){
        if(pot == null) return 0;
        return pot.get() / 1.1 ; //approx. conversion from ADC to degrees
    }

    public void enablePID(){
        if(pot != null) pid.enable();
    }

    public void disablePID(){
        if(pot != null) pid.disable();
    }

    public void rotate(double speed){
        rotateMotor.set(speed);
    }

    public double getLength(){
        return length;
    }
}

package frc.team871.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;

public class ArmSegment {

    private SpeedController rotateMotor;
    private AnalogPotentiometer pot;
    private double length;
    private PIDController pid;

    public ArmSegment(SpeedController rotateMotor, AnalogPotentiometer pot, double length){
        this.rotateMotor = rotateMotor;
        this.pot = pot;
        this.length = length;
        //TODO: Add Apropreate Values
        pid = new PIDController(0,0,0, pot, rotateMotor);
        pid.setInputRange(-60, 90);
        pid.setOutputRange(-1,1);
    }

    public void setAngle(double angle){
        pid.setSetpoint(angle);
    }


    public double getAngle(){
        return pot.get() / 1.1 ; //approx. conversion from ADC to degrees
    }

    public void enablePID(){
        pid.enable();
    }

    public void disablePID(){
        pid.disable();
    }

    public void rotate(double speed){
        rotateMotor.set(speed);
    }

    public double getLength(){
        return length;
    }
}

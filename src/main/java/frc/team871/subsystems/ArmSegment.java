package frc.team871.subsystems;

import com.team871.hid.IAxis;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.team871.config.PIDConfiguration;

public class ArmSegment {

    private SpeedController rotateMotor;
    private IAxis pot;
    private double length;
    private PIDController pid;

    public ArmSegment(SpeedController rotateMotor, IAxis pot, PIDConfiguration pidConfig, double length){
        this.rotateMotor = rotateMotor;
        this.pot = pot;
        this.length = length;

        pid = new PIDController(pidConfig.getKp(), pidConfig.getKi(), pidConfig.getKd(), pot, rotateMotor);
        pid.setInputRange(pidConfig.getInMin(), pidConfig.getInMax());
        pid.setOutputRange(pidConfig.getOutMin(), pidConfig.getOutMax());
        pid.setAbsoluteTolerance(pidConfig.getTolerance());

        pid.setName("ArmSegmentPID " + hashCode());
        LiveWindow.add(pid);

        pot.setName("ArmSegmentPID " + hashCode(), "Pot");
        LiveWindow.add(pot);

    }

    public void setAngle(double angle){
        pid.setSetpoint(angle);
    }


    public double getAngle(){
        return pot.getValue(); //approx. conversion from ADC to degrees
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

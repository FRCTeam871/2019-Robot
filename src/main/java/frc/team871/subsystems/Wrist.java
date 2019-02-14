package frc.team871.subsystems;

import com.team871.hid.HIDAxis;
import com.team871.hid.HIDButton;
import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import frc.team871.config.PIDConfiguration;

public class Wrist {

    private SpeedController motor;
    private PIDController pid;
    private IAxis pot;
    private double oldAxis;
    private boolean oldButton;

    public Wrist(SpeedController motor, IAxis pot, PIDConfiguration pidConfig) {
        this.motor = motor;
        this.pot = pot;

        pid = new PIDController(pidConfig.getKp(), pidConfig.getKi(), pidConfig.getKd(), pot, motor);
        pid.setInputRange(pidConfig.getInMin(), pidConfig.getInMax());
        pid.setOutputRange(pidConfig.getOutMin(), pidConfig.getOutMax());
        pid.setAbsoluteTolerance(pidConfig.getTolerance());
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
        return pot.getValue();
    }

    public void rotate(double speed){
        motor.set(speed);
    }

    /**
     * Handles both input values of an axis and button, making it
     * so that the most recently used input is used.
     * @param axis
     * @param button
     */
    public void handleInputs(IAxis axis, IButton button){
        if(oldAxis != axis.getValue()){
            setOrientation(axis.getValue());
        }else if(oldButton != button.getValue()){
            setOrientation(button.getValue() ? 90 : 0);
        }
        oldAxis = axis.getValue();
        oldButton = button.getValue();
    }
}

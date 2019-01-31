package frc.team871.subsystems;

import com.team871.hid.HIDAxis;
import com.team871.hid.HIDButton;
import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;

public class Wrist {

    private SpeedController motor;
    //TODO: orientation sensor
    private PIDController pid;
    private AnalogInput pot;
    private double oldAxis;
    private boolean oldButton;

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

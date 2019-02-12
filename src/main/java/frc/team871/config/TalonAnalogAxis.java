package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team871.hid.IAxis;

public class TalonAnalogAxis implements IAxis {

    TalonSRX talon;

    double inMin;
    double inMax;
    double outMin;
    double outMax;


    public TalonAnalogAxis(TalonSRX talon, double inMin, double inMax){
        this.talon = talon;
        this.inMin = inMin;
        this.inMax = inMax;
    }

    @Override
    public double getRaw() {
        return talon.getSelectedSensorPosition();
    }

    @Override
    public double getValue() {
        return (getRaw() - inMin) / (inMax - inMin) * (outMax - outMin) + outMin;
    }

    @Override
    public void setMapping(double outMin, double outMax) {
        this.outMin = outMin;
        this.outMax = outMax;
    }
}

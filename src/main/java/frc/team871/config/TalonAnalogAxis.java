package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team871.hid.IAxis;

/**
 * Class that maps an analog sensor on a TalonSRX SpeedController to an IAxis.
 */
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

    /**
     *
     * @return the mapping of getRaw() from the range [inMin, inMax] to [outMin, outMax]
     */
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

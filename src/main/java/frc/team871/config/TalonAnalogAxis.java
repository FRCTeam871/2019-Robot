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

    double outRange;
    double inRange;

    public TalonAnalogAxis(TalonSRX talon, double inMin, double inMax){
        this.talon = talon;
        this.inMin = inMin;
        this.inMax = inMax;

        inRange = Math.abs(inMax - inMin);
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



        if(outMin > outMax){
//            return (1.0 - ((getRaw() - inMin) / (inRange))) * (outRange) + outMin;
            return (((getRaw() - inMin) / (inRange))) * (outMin - outMax) + outMax;
        }else {
            return (getRaw() - inMin) / (inRange) * (outRange) + outMin;
        }
    }

    @Override
    public void setMapping(double outMin, double outMax) {
        this.outMin = outMin;
        this.outMax = outMax;

        outRange = Math.abs(outMax - outMin);
    }
}

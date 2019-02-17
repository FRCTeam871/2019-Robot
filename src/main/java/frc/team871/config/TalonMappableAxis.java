package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team871.hid.IAxis;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team871.control.MappableAxis;

/**
 * Class that maps an analog sensor on a TalonSRX SpeedController to an IAxis.
 */
public class TalonMappableAxis implements MappableAxis, Sendable {
    private final TalonSRX talon;

    private double inMin;
    private double inMax;
    private double outMin;
    private double outMax;

    private double outRange;
    private double inRange;

    private String name;
    private String subsystem;

    public TalonMappableAxis(TalonSRX talon, double inMin, double inMax, double outMin, double outMax) {
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
        if(outMin > outMax) {
            return (((getRaw() - inMin) / (inRange))) * (outMin - outMax) + outMax;
        }else {
            return (getRaw() - inMin) / (inRange) * (outRange) + outMin;
        }
    }

    @Override
    public void setOutputRange(double outMin, double outMax) {
        this.outMin = outMin;
        this.outMax = outMax;

        outRange = Math.abs(outMax - outMin);
    }

    @Override
    public void setInputRange(double min, double max) {
        this.inMax = max;
        this.inMin = min;

        inRange = Math.abs(inMax - inMin);
    }

    private void setInputRangeArr(double[] vals) {
        setInputRange(vals[0], vals[1]);
    }

    private void setOutRangeArr(double[] vals) {
        setOutputRange(vals[0], vals[1]);
    }

    private final double[] inRangeArr = new double[2];
    private double[] getInputRange() {
        inRangeArr[0] = inMin;
        inRangeArr[1] = inMax;
        return inRangeArr;
    }

    private final double[] outRangeArr = new double[2];
    private double[] getOutputRange() {
        outRangeArr[0] = outMin;
        outRangeArr[1] = outMax;
        return outRangeArr;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public String getSubsystem() {
        return subsystem;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Input Value", this::getRaw, null);
        builder.addDoubleProperty("Output Value", this::getValue, null);

        builder.addDoubleArrayProperty("Input Range", this::getInputRange, this::setInputRangeArr);
        builder.addDoubleArrayProperty("Output Range", this::getOutputRange, this::setOutRangeArr);
    }
}

package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team871.hid.ScaledOffsetAxis;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Class that maps an analog sensor on a TalonSRX SpeedController to an IAxis.
 */
public class TalonSmartAxis implements ScaledOffsetAxis, Sendable {
    private final TalonSRX talon;

    private double scalar;
    private double offset;

    private String name;
    private String subsystem;

    public TalonSmartAxis(WPI_TalonSRX talon, double scalar, double offset) {
        this.talon = talon;
        this.scalar = scalar;
        this.offset = offset;
    }

    @Override
    public double getRaw() {
        return talon.getSelectedSensorPosition();
    }

    /**
     * @return the mapping of getRaw() from the range [inMin, inMax] to [outMin, outMax]
     */
    @Override
    public double getValue() {
        return (getRaw() * scalar) + offset;
    }

    @Override
    public void setScale(double scale) {
        this.scalar = scale;
    }

    @Override
    public double getScale() {
        return scalar;
    }

    @Override
    public void setOffset(double offset) {
        this.offset = offset;
    }

    @Override
    public double getOffset() {
        return offset;
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


    /// These two are only for automatically setting the scale and offset (from Suffleboard, or smartdashboard)
    private enum MarkSide {
        Upper, Lower
    }

    private double lowerMarkRaw = Double.NaN;
    private double lowerMarkUser = Double.NaN;
    private double upperMarkRaw = Double.NaN;
    private double upperMarkUser = Double.NaN;

    private double getLowerMark() {
        return lowerMarkUser;
    }

    private double getUpperMark() {
        return upperMarkUser;
    }

    private void setLowerMark(double userValue) {
        lowerMarkRaw = getRaw();
        lowerMarkUser = userValue;

        maybeUpdateMapping(MarkSide.Lower);
    }

    private void setUpperMark(double userValue) {
        upperMarkRaw = getRaw();
        upperMarkUser = userValue;

        maybeUpdateMapping(MarkSide.Upper);
    }

    private void maybeUpdateMapping(MarkSide lastSetSide) {
        if(!Double.isNaN(lowerMarkRaw) && !Double.isNaN(lowerMarkUser) &&
           !Double.isNaN(upperMarkRaw) && !Double.isNaN(upperMarkUser)) {
            // Compute the new scale,  this is a simple ratio.
            final double newScale = (upperMarkUser - lowerMarkUser) / (upperMarkRaw - lowerMarkRaw);

            // Next figure out the offset.  Start by multiplying the raw by the scale, then take the difference between
            // the current position and the user position.
            final double scaledCurrent = getRaw() * newScale;
            final double newOffset = ((lastSetSide == MarkSide.Lower) ? lowerMarkRaw : upperMarkRaw) - scaledCurrent;

            setScale(newScale);
            setOffset(newOffset);

            // Reset the marks to NaN so we can repeat.
            lowerMarkRaw = Double.NaN;
            lowerMarkUser = Double.NaN;
            upperMarkRaw = Double.NaN;
            upperMarkUser = Double.NaN;
        }
    }


    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Raw Value", this::getRaw, null);
        builder.addDoubleProperty("Output Value", this::getValue, null);
        builder.addDoubleProperty("Scale", this::getScale, this::setScale);
        builder.addDoubleProperty("Offset", this::getOffset, this::setOffset);

        builder.addDoubleProperty("UpperMark", this::getUpperMark, this::setUpperMark);
        builder.addDoubleProperty("LowerMark", this::getLowerMark, this::setLowerMark);
    }
}

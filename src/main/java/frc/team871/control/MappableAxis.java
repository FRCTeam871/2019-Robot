package frc.team871.control;

import com.team871.hid.IAxis;

/**
 * An {@link IAxis} That supports mapping input ranges to output ranges.
 */
public interface MappableAxis extends IAxis {
    /**
     * Set the input range for the axis.
     *
     * @param min The minimum input of the axis
     * @param max The maximum output of the axis
     */
    void setInputRange(double min, double max);

    /**
     * Set the output range for the axis
     *
     * @param min The mininmum output value
     * @param max The maximum output value.
     */
    void setOutputRange(double min, double max);
}

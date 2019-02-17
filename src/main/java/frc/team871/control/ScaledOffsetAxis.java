package frc.team871.control;

import com.team871.hid.IAxis;

/**
 * An {@link IAxis} that supports applying a scale and transform to produce it's output
 */
public interface ScaledOffsetAxis extends IAxis {
    /**
     * Set the scale of the value.
     */
    void setScale(double scale);

    /**
     * Set the offset to apply to the scaled result.
     */
    void setOffset(double offset);

    double getScale();
    double getOffset();
}

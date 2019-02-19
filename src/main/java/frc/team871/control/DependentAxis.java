package frc.team871.control;

import com.team871.hid.IAxis;
import com.team871.hid.MappableAxis;

/**
 * This class is used to take a point unreachable by the arm and
 * transform it to a point that is in its range.
 */

public class DependentAxis implements MappableAxis {

    private IAxis baseAxis;
    private IAxis modifierAxis;

    private double scaling = 1;
    private double translation = 0;

    public DependentAxis(IAxis baseAxis, IAxis modifiedAxis){
        this.baseAxis = baseAxis;
        this.modifierAxis = modifiedAxis;
    }

    @Override
    public double getRaw() {
        return baseAxis.getRaw();
    }

    /**
     * Transforms the baseAxis to a value scaled to the width of the
     * unit circle at the height defined by the modifierAxis.
     *
     * @returns double that represents the result of the transformation.
     */
    @Override
    public double getValue() {
        //First "1" represents the maximum output value and the second "1" represents the maximum input value.
        return baseAxis.getRaw() * Math.sin(Math.acos(modifierAxis.getRaw()));
//        return ((baseAxis.getRaw() * ((Math.sqrt(1 + (modifierAxis.getRaw() * modifierAxis.getRaw())))/ 1) * scaling) + translation);
    }

    @Override
    public void setInputRange(double v, double v1) {

    }

    @Override
    public void setOutputRange(double min, double max) {
        scaling = (max - min) / (1 - (-1));
        translation = max - scaling;
    }
}

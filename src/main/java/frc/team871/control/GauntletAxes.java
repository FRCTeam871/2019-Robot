package frc.team871.control;

import com.team871.hid.AxisID;

public enum GauntletAxes implements AxisID{
    X(0),
    Y(1),
    UPPER_ARM(2),
    LOWER_ARM(3);

    private final int value;

    GauntletAxes(int num){
        this.value = num;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public int getId() {
        return this.value;
    }
}

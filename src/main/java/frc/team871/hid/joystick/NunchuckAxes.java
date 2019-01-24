package frc.team871.hid.joystick;

import com.team871.hid.AxisID;

public enum NunchuckAxes implements AxisID {
    X(0),
    Y(1);

    private int value;

    private NunchuckAxes(int num){
        this.value = num;
    }

    public String getName(){
        return this.toString();
    }
    public int getId(){
        return this.value;
    }
}

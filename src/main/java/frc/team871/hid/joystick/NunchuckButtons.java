package frc.team871.hid.joystick;

import com.team871.hid.ButtonID;

public enum NunchuckButtons implements ButtonID {
    C(0),
    Z(1);

    private int value;

    private NunchuckButtons(int num){
        this.value = num;
    }

    public String getName(){
        return this.toString();
    }

    public int getId(){
        return this.value;
    }
}

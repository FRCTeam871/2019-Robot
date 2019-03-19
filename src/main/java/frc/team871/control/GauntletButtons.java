package frc.team871.control;

import com.team871.hid.ButtonID;

public enum GauntletButtons implements ButtonID {
    C(1),
    Z(2);

    private final int val;

    private GauntletButtons(int num){
        this.val = num;
    }
    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public int getId() {
        return this.val;
    }
}

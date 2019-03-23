package frc.team871.control;

import com.team871.hid.ButtonID;
import com.team871.hid.HIDAxis;
import com.team871.hid.HIDButton;
import edu.wpi.first.wpilibj.Joystick;

public class AxisButtonInv extends HIDButton {
    HIDAxis axis;
    float threshold;

    public AxisButtonInv(final HIDAxis axis, float activateThreshold) {
        super(new ButtonID() {
            public String getName() {
                return axis.getId().getName() + "Button";
            }

            public int getId() {
                return 0;
            }
        }, (Joystick)null);
        this.threshold = activateThreshold;
    }

    public boolean getRaw() {
        return this.axis.getValue() < (double)this.threshold;
    }
}

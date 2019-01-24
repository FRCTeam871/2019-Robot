package frc.team871.control;

import com.team871.hid.ButtonTypes;
import com.team871.hid.GenericJoystick;
import com.team871.hid.HIDAxis;
import com.team871.hid.HIDButton;
import com.team871.hid.joystick.XBoxAxes;
import com.team871.hid.joystick.XBoxButtons;
import frc.team871.hid.joystick.NunchuckAxes;
import frc.team871.hid.joystick.NunchuckButtons;

import java.util.Arrays;

public enum NunchuckControlScheme implements IControlScheme{
    DEFAULT;

    private GenericJoystick<NunchuckButtons, NunchuckAxes> nunchuck;
    private GenericJoystick<XBoxButtons, XBoxAxes> xbox;

    NunchuckControlScheme(){
        nunchuck = new GenericJoystick<>(0, Arrays.asList(NunchuckButtons.values()), Arrays.asList(NunchuckAxes.values()));
        xbox = new GenericJoystick<>(1, Arrays.asList(XBoxButtons.values()), Arrays.asList(XBoxAxes.values()));
        nunchuck.getButton(NunchuckButtons.Z).setMode(ButtonTypes.TOGGLE);
        xbox.getButton(XBoxButtons.RBUMPER).setMode(ButtonTypes.TOGGLE);
        nunchuck.getAxis(NunchuckAxes.Y).setDeadband(0.2);
        xbox.getAxis(XBoxAxes.LTRIGGER).setDeadband(0.2);
        xbox.getAxis(XBoxAxes.RTRIGGER).setDeadband(0.2);
        xbox.getButton(XBoxButtons.LBUMPER).setMode(ButtonTypes.MOMENTARY);
        xbox.getButton(XBoxButtons.START).setMode(ButtonTypes.RISING);
        xbox.getButton(XBoxButtons.BACK).setMode(ButtonTypes.RISING);
        xbox.getAxis(XBoxAxes.LEFTX).setDeadband(0.2);
        xbox.getAxis(XBoxAxes.LEFTY).setDeadband(0.2);
        xbox.getAxis(XBoxAxes.RIGHTX).setDeadband(0.2);
    }

    @Override
    public HIDButton getVacuumToggleButton() {
        return nunchuck.getButton(NunchuckButtons.Z);
    }

    @Override
    public HIDButton getInverseKinimaticsToggleButton() {
        return xbox.getButton(XBoxButtons.RBUMPER);
    }

    @Override
    public HIDAxis getWristAxis() {
        return nunchuck.getAxis(NunchuckAxes.Y);
    }

    @Override
    public HIDAxis getUpperArmAxis() {
        return xbox.getAxis(XBoxAxes.LTRIGGER);
    }

    @Override
    public HIDAxis getLowerArmAxis() {
        return xbox.getAxis(XBoxAxes.RTRIGGER);
    }

    @Override
    public HIDButton getHeadingHoldButton() {
        return xbox.getButton(XBoxButtons.LBUMPER);
    }

    @Override
    public HIDButton getResetGyroButton() {
        return xbox.getButton(XBoxButtons.START);
    }

    @Override
    public HIDButton getRobotOrientationToggleButton() {
        return xbox.getButton(XBoxButtons.BACK);
    }

    @Override
    public HIDAxis getMecDriveXAxis() {
        return xbox.getAxis(XBoxAxes.LEFTX);
    }

    @Override
    public HIDAxis getMecDriveYAxis() {
        return xbox.getAxis(XBoxAxes.LEFTY);
    }

    @Override
    public HIDAxis getMecDriveRotationAxis() {
        return xbox.getAxis(XBoxAxes.RIGHTX);
    }
}

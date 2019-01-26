package frc.team871.control;


import com.team871.hid.ButtonTypes;
import com.team871.hid.ConstantAxis;
import com.team871.hid.ConstantButton;
import com.team871.hid.GenericJoystick;
import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import com.team871.hid.joystick.SaitekAxes;
import com.team871.hid.joystick.SaitekButtons;

import java.util.Arrays;

public enum SaitekControlScheme implements IControlScheme{
    DEFAULT;

    private GenericJoystick<SaitekButtons, SaitekAxes> saitekStickJoy;
    private ConstantAxis unusedAxis;
    private ConstantButton unusedButton;

    SaitekControlScheme() {
        saitekStickJoy = new GenericJoystick<>(0, Arrays.asList(SaitekButtons.values()), Arrays.asList(SaitekAxes.values()));
        saitekStickJoy.getButton(SaitekButtons.HAT_LEFT).setMode(ButtonTypes.TOGGLE);
        saitekStickJoy.getButton(SaitekButtons.I).setMode(ButtonTypes.RISING);
        saitekStickJoy.getButton(SaitekButtons.C).setMode(ButtonTypes.RISING);
        saitekStickJoy.getButton(SaitekButtons.B).setMode(ButtonTypes.MOMENTARY);
        saitekStickJoy.getButton(SaitekButtons.A).setMode(ButtonTypes.RISING);

        unusedAxis = new ConstantAxis(0);
        unusedButton = new ConstantButton(false);
    }

    @Override
    public IButton getVacuumToggleButton() {
        return saitekStickJoy.getButton(SaitekButtons.HAT_LEFT);
    }

    @Override
    public IButton getInverseKinimaticsToggleButton() {
        return unusedButton;
    }

    @Override
    public IButton getWristToggleButton() {
        return saitekStickJoy.getButton(SaitekButtons.I);
    }

    @Override
    public IAxis getWristAxis() {
        //This button will not be used in this configuration
        return unusedAxis;
    }

    @Override
    public IAxis getUpperArmAxis() {
        //This axis will not be used in this configuration
        return unusedAxis;
    }

    @Override
    public IAxis getLowerArmAxis() {
        //This axis will not be used in this configuration
        return unusedAxis;
    }

    @Override
    public IButton getHeadingHoldButton() {
        return saitekStickJoy.getButton(SaitekButtons.B);
    }

    @Override
    public IButton getResetGyroButton() {
        return saitekStickJoy.getButton(SaitekButtons.C);
    }

    @Override
    public IButton getRobotOrientationToggleButton() {
        return saitekStickJoy.getButton(SaitekButtons.A);
    }

    @Override
    public IAxis getMecDriveXAxis() {
        return saitekStickJoy.getAxis(SaitekAxes.X_AXIS);
    }

    @Override
    public IAxis getMecDriveYAxis() {
        return saitekStickJoy.getAxis(SaitekAxes.Y_AXIS);
    }

    @Override
    public IAxis getMecDriveRotationAxis() {
        return saitekStickJoy.getAxis(SaitekAxes.ROTATION);
    }

    @Override
    public IAxis getArmTargetXAxis() {
        return saitekStickJoy.getAxis(SaitekAxes.THROTTLE);
    }

    @Override
    public IAxis getArmTargetYAxis() {
        return saitekStickJoy.getAxis(SaitekAxes.I_AXIS);
    }
}

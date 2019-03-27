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
    private DependentAxis dependentAxis;

    SaitekControlScheme() {
        saitekStickJoy = new GenericJoystick<>(0, Arrays.asList(SaitekButtons.values()), Arrays.asList(SaitekAxes.values()));
        saitekStickJoy.getButton(SaitekButtons.HAT_LEFT).setMode(ButtonTypes.TOGGLE);
        saitekStickJoy.getButton(SaitekButtons.C).setMode(ButtonTypes.RISING);
        saitekStickJoy.getButton(SaitekButtons.B).setMode(ButtonTypes.MOMENTARY);
        saitekStickJoy.getButton(SaitekButtons.A).setMode(ButtonTypes.RISING);
        saitekStickJoy.getButton(SaitekButtons.HAT_DOWN).setMode(ButtonTypes.RISING);
        saitekStickJoy.getButton(SaitekButtons.HAT_UP).setMode(ButtonTypes.RISING);

        saitekStickJoy.getButton(SaitekButtons.FIRE).setMode(ButtonTypes.RISING);

        saitekStickJoy.getButton(SaitekButtons.E).setMode(ButtonTypes.TOGGLE);
        saitekStickJoy.getButton(SaitekButtons.I).setMode(ButtonTypes.TOGGLE);
        saitekStickJoy.getButton(SaitekButtons.D).setMode(ButtonTypes.RISING);
        saitekStickJoy.getButton(SaitekButtons.FIRE).setMode(ButtonTypes.RISING);
        saitekStickJoy.getButton(SaitekButtons.LOW_TRIGGER).setMode(ButtonTypes.RISING);

        unusedAxis = new ConstantAxis(0);
        unusedButton = new ConstantButton(false);

        dependentAxis = new DependentAxis(saitekStickJoy.getAxis(SaitekAxes.I_AXIS), saitekStickJoy.getAxis(SaitekAxes.THROTTLE));
    }

    @Override
    public IButton getOuterSuctionButton() {
        return saitekStickJoy.getButton(SaitekButtons.FIRE);
    }

    @Override
    public IButton getInnerSuctionButton() {
        return unusedButton;
    }

    @Override
    public IButton getInverseKinematicsToggleButton() {
        return unusedButton;
    }

    @Override
    public IButton getWristToggleButton() {
        return unusedButton;
    }

    @Override
    public IAxis getWristAxis() {
        //This button will not be used in this configuration
        return saitekStickJoy.getAxis(SaitekAxes.E_AXIS);
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
        return dependentAxis;
    }

    @Override
    public IAxis getArmTargetYAxis() {
        return saitekStickJoy.getAxis(SaitekAxes.THROTTLE);
    }

    @Override
    public IButton getAutoDockButton() {
        return unusedButton;
    }

    @Override
    public IAxis getArmSetpointAxis() {
        return saitekStickJoy.getAxis(SaitekAxes.THROTTLE);
    }

    @Override
    public IButton getArmSetpointUpButton() {
        return saitekStickJoy.getButton(SaitekButtons.HAT_UP);
    }

    @Override
    public IButton getArmSetpointDownButton() {
        return saitekStickJoy.getButton(SaitekButtons.HAT_DOWN);
    }

    @Override
    public IButton getClimbAdvanceButton() {
        return saitekStickJoy.getButton(SaitekButtons.D);
    }

    @Override
    public IButton getClimbUnAdvanceButton() {
        return saitekStickJoy.getButton(SaitekButtons.FIRE);
    }

    @Override
    public IButton getAutoClimbButton() {
        return saitekStickJoy.getButton(SaitekButtons.LOW_TRIGGER);
    }

    @Override
    public IButton getClimbFrontButton() {
        return saitekStickJoy.getButton(SaitekButtons.E);
    }

    @Override
    public IButton getClimbBackButton() {
        return saitekStickJoy.getButton(SaitekButtons.I);
    }

    @Override
    public IButton getEmergencyModeButton() {
        return unusedButton;
    }
}

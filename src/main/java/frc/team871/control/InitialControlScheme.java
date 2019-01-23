package frc.team871.control;

import com.team871.hid.ButtonTypes;
import com.team871.hid.GenericJoystick;
import com.team871.hid.HIDAxis;
import com.team871.hid.HIDButton;
import com.team871.hid.joystick.XBoxAxes;
import com.team871.hid.joystick.XBoxButtons;
import com.team871.navigation.Coordinate;

import java.util.Arrays;

public enum InitialControlScheme implements IControlScheme{
    DEFAULT;

    private GenericJoystick<XBoxButtons, XBoxAxes> systemsController, driveController;

    InitialControlScheme(){
        systemsController = new GenericJoystick<>(0, Arrays.asList(XBoxButtons.values()), Arrays.asList(XBoxAxes.values()));
        driveController = new GenericJoystick<>(1, Arrays.asList(XBoxButtons.values()), Arrays.asList(XBoxAxes.values()));
        systemsController.getButton(XBoxButtons.Y).setMode(ButtonTypes.TOGGLE);
        systemsController.getButton(XBoxButtons.BACK).setMode(ButtonTypes.TOGGLE);
        systemsController.getAxis(XBoxAxes.TRIGGER).setDeadband(0.2);
        //TODO set up the arm axis
        //driveController.getAxis();
        //driveController.getAxis();
        driveController.getButton(XBoxButtons.B).setMode(ButtonTypes.MOMENTARY);
        driveController.getButton(XBoxButtons.START).setMode(ButtonTypes.RISING);
        driveController.getButton(XBoxButtons.BACK).setMode(ButtonTypes.TOGGLE);
        driveController.getAxis(XBoxAxes.LEFTX).setDeadband(0.2);
        driveController.getAxis(XBoxAxes.LEFTY).setDeadband(0.2);
        driveController.getAxis(XBoxAxes.RIGHTX).setDeadband(0.2);
    }

    @Override
    public HIDButton getVacuumToggleButton() {
        return systemsController.getButton(XBoxButtons.Y);
    }

    @Override
    public HIDButton getInverseKinimaticsToggleButton() {
        return systemsController.getButton(XBoxButtons.BACK);
    }

    @Override
    public HIDAxis getWristAxis() {
        return systemsController.getAxis(XBoxAxes.TRIGGER);
    }

    @Override
    public HIDAxis getUpperArmAxis() {
        //TODO set up axis
        return null;
    }

    @Override
    public HIDAxis getLowerArmAxis() {
        //TODO set up axis
        return null;
    }

    @Override
    public HIDButton getHeadingHoldButton() {
        return driveController.getButton(XBoxButtons.B);
    }

    @Override
    public HIDButton getResetGyroButton() {
        return driveController.getButton(XBoxButtons.START);
    }

    @Override
    public HIDButton getRobotOrientationToggleButton() {
        return driveController.getButton(XBoxButtons.BACK);
    }

    @Override
    public HIDAxis getMecDriveXAxis() {
        return driveController.getAxis(XBoxAxes.LEFTX);
    }

    @Override
    public HIDAxis getMecDriveYAxis() {
        return driveController.getAxis(XBoxAxes.LEFTY);
    }

    @Override
    public HIDAxis getMecDriveRotationAxis() {
        return driveController.getAxis(XBoxAxes.RIGHTX);
    }
}

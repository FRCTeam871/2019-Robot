package frc.team871.control;

import com.team871.hid.ButtonTypes;
import com.team871.hid.ConstantAxis;
import com.team871.hid.GenericJoystick;
import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import com.team871.hid.joystick.XBoxAxes;
import com.team871.hid.joystick.XBoxButtons;
import java.util.Arrays;

public enum InitialControlSchemeHack implements IControlScheme{
    DEFAULT;

    private GenericJoystick<XBoxButtons, XBoxAxes> systemsController, driveController;
    private ConstantAxis unusedAxis;

    InitialControlSchemeHack(){
        systemsController = new GenericJoystick<>(0, Arrays.asList(XBoxButtons.values()), Arrays.asList(XBoxAxes.values()));
        driveController = new GenericJoystick<>(1, Arrays.asList(XBoxButtons.values()), Arrays.asList(XBoxAxes.values()));
        systemsController.getButton(XBoxButtons.Y).setMode(ButtonTypes.TOGGLE);
        systemsController.getButton(XBoxButtons.BACK).setMode(ButtonTypes.TOGGLE);
        systemsController.getButton(XBoxButtons.A).setMode(ButtonTypes.RISING);
        systemsController.getAxis(XBoxAxes.TRIGGER).setDeadband(0.2);
        systemsController.getAxis(XBoxAxes.LEFTY).setDeadband(0.2);
        systemsController.getAxis(XBoxAxes.RIGHTY).setDeadband(0.2);
        driveController.getButton(XBoxButtons.LBUMPER).setMode(ButtonTypes.MOMENTARY);
        driveController.getButton(XBoxButtons.START).setMode(ButtonTypes.RISING);
        driveController.getButton(XBoxButtons.BACK).setMode(ButtonTypes.TOGGLE);
        driveController.getAxis(XBoxAxes.LEFTX).setDeadband(0.2);
        driveController.getAxis(XBoxAxes.LEFTY).setDeadband(0.2);
        driveController.getAxis(XBoxAxes.RIGHTX).setDeadband(0.2);

        unusedAxis = new ConstantAxis(0);
    }

    @Override
    public IButton getVacuumToggleButton() {
        return systemsController.getButton(XBoxButtons.Y);
    }

    @Override
    public IButton getInverseKinematicsToggleButton() {
        return systemsController.getButton(XBoxButtons.BACK);
    }

    @Override
    public IButton getWristToggleButton() { return systemsController.getButton(XBoxButtons.A); }

    @Override
    public IAxis getWristAxis() {
        return systemsController.getAxis(XBoxAxes.LTRIGGER);
    }

    public IAxis getWristAxis2() {
        return systemsController.getAxis(XBoxAxes.RTRIGGER);
    }

    @Override
    public IAxis getUpperArmAxis() {
        return systemsController.getAxis(XBoxAxes.LEFTY);
    }

    @Override
    public IAxis getLowerArmAxis() {
        return systemsController.getAxis(XBoxAxes.RIGHTY);
    }

    @Override
    public IButton getHeadingHoldButton() {
        return driveController.getButton(XBoxButtons.LBUMPER);
    }

    @Override
    public IButton getResetGyroButton() {
        return driveController.getButton(XBoxButtons.START);
    }

    @Override
    public IButton getRobotOrientationToggleButton() {
        return driveController.getButton(XBoxButtons.BACK);
    }

    @Override
    public IAxis getMecDriveXAxis() {
        return driveController.getAxis(XBoxAxes.LEFTX);
    }

    @Override
    public IAxis getMecDriveYAxis() {
        return driveController.getAxis(XBoxAxes.LEFTY);
    }

    @Override
    public IAxis getMecDriveRotationAxis() {
        return driveController.getAxis(XBoxAxes.RIGHTX);
    }

    @Override
    public IAxis getArmTargetXAxis() {
        return unusedAxis;
    }

    @Override
    public IAxis getArmTargetYAxis() {
        return unusedAxis;
    }
}
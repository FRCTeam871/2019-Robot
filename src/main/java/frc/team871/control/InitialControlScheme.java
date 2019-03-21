package frc.team871.control;

import com.team871.hid.ButtonTypes;
import com.team871.hid.ConstantAxis;
import com.team871.hid.ConstantButton;
import com.team871.hid.GenericJoystick;
import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import com.team871.hid.joystick.XBoxAxes;
import com.team871.hid.joystick.XBoxButtons;

import java.util.Arrays;

public enum InitialControlScheme implements IControlScheme{
    DEFAULT;

    private GenericJoystick<XBoxButtons, XBoxAxes> systemsController, driveController;
    private ConstantAxis unusedAxis;
    private ConstantButton unusedButton;

    InitialControlScheme(){
        systemsController = new GenericJoystick<>(2, Arrays.asList(XBoxButtons.values()), Arrays.asList(XBoxAxes.values()));
        driveController = new GenericJoystick<>(3, Arrays.asList(XBoxButtons.values()), Arrays.asList(XBoxAxes.values()));
        systemsController.getButton(XBoxButtons.Y).setMode(ButtonTypes.TOGGLE);
        systemsController.getButton(XBoxButtons.BACK).setMode(ButtonTypes.RISING);
        systemsController.getButton(XBoxButtons.A).setMode(ButtonTypes.TOGGLE);
        systemsController.getButton(XBoxButtons.X).setMode(ButtonTypes.RISING);
        systemsController.getButton(XBoxButtons.B).setMode(ButtonTypes.RISING);
        systemsController.getButton(XBoxButtons.RBUMPER).setMode(ButtonTypes.RISING);
        systemsController.getButton(XBoxButtons.LBUMPER).setMode(ButtonTypes.RISING);
        systemsController.getAxis(XBoxAxes.TRIGGER).setDeadband(0.2);
        systemsController.getAxis(XBoxAxes.LEFTY).setDeadband(0.2);
        systemsController.getAxis(XBoxAxes.RIGHTY).setDeadband(0.2);
        systemsController.getButton(XBoxButtons.START).setMode(ButtonTypes.TOGGLE);
        systemsController.getAxis(XBoxAxes.RIGHTX).setDeadband(0.2);
        driveController.getButton(XBoxButtons.LBUMPER).setMode(ButtonTypes.MOMENTARY);
        driveController.getButton(XBoxButtons.START).setMode(ButtonTypes.RISING);
        driveController.getButton(XBoxButtons.BACK).setMode(ButtonTypes.TOGGLE);
        driveController.getAxis(XBoxAxes.LEFTX).setDeadband(0.2);
        driveController.getAxis(XBoxAxes.LEFTY).setDeadband(0.2);
        driveController.getAxis(XBoxAxes.RIGHTX).setDeadband(0.2);
        driveController.getButton(XBoxButtons.B).setMode(ButtonTypes.RISING);

        unusedAxis = new ConstantAxis(0);
    }

    @Override
    public IButton getOuterSuctionButton() {
        return systemsController.getButton(XBoxButtons.B);
    }

    @Override
    public IButton getInnerSuctionButton() {
        return systemsController.getButton(XBoxButtons.X);
    }

    @Override
    public IButton getInverseKinematicsToggleButton() {
        return systemsController.getButton(XBoxButtons.BACK);
    }

    @Override
    public IButton getWristToggleButton() { return systemsController.getButton(XBoxButtons.A); }

    @Override
    public IAxis getWristAxis() {
        return systemsController.getAxis(XBoxAxes.RIGHTX);
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
        return systemsController.getAxis(XBoxAxes.RIGHTX);
    }

    @Override
    public IAxis getArmTargetYAxis() {
//        return unusedAxis;
        return systemsController.getAxis(XBoxAxes.RIGHTY);
    }

    @Override
    public IAxis getArmSetpointAxis() {
        return unusedAxis;
    }

    @Override
    public IButton getArmSetpointUpButton() {
        return systemsController.getButton(XBoxButtons.RBUMPER);
    }

    @Override
    public IButton getArmSetpointDownButton() {
        return systemsController.getButton(XBoxButtons.LBUMPER);
    }

    @Override
    public IButton getAutoDockButton() {
        return driveController.getButton(XBoxButtons.B);
    }

}

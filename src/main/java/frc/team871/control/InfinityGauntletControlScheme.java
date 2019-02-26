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

public enum InfinityGauntletControlScheme implements IControlScheme{
    DEFAULT;

    private GenericJoystick<SaitekButtons, SaitekAxes> saitekDrive;
    private GenericJoystick<GauntletButtons, GauntletAxes> infinitySystem;
    private ConstantAxis unusedAxis;
    private ConstantButton unusedButton;

    InfinityGauntletControlScheme(){
        saitekDrive = new GenericJoystick<>(0, Arrays.asList(SaitekButtons.values()), Arrays.asList(SaitekAxes.values()));
        infinitySystem = new GenericJoystick<>(1, Arrays.asList(GauntletButtons.values()), Arrays.asList(GauntletAxes.values()));
        infinitySystem.getButton(GauntletButtons.Z).setMode(ButtonTypes.TOGGLE);
        infinitySystem.getAxis(GauntletAxes.LOWER_ARM).setOutputRange(1, -1);
        infinitySystem.getAxis(GauntletAxes.UPPER_ARM).setOutputRange(1, -1);
        infinitySystem.getAxis(GauntletAxes.Y).setDeadband(0.1);
        saitekDrive.getButton(SaitekButtons.B).setMode(ButtonTypes.MOMENTARY);
        saitekDrive.getButton(SaitekButtons.C).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.A).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.TRIGGER_1).setMode(ButtonTypes.TOGGLE);

        unusedAxis = new ConstantAxis(0);
        unusedButton = new ConstantButton(false);
    }

    @Override
    public IButton getVacuumToggleButton() {
        return saitekDrive.getButton(SaitekButtons.TRIGGER_1);
    }

    @Override
    public IButton getInverseKinematicsToggleButton() {
        //This button will not be used in this configuration
        return unusedButton;
    }

    @Override
    public IButton getWristToggleButton() {
        //This button will not be used in this configuration
        return unusedButton;
    }

    @Override
    public IAxis getWristAxis() {
        return infinitySystem.getAxis(GauntletAxes.Y);
    }

    @Override
    public IAxis getUpperArmAxis() {
        return infinitySystem.getAxis(GauntletAxes.UPPER_ARM);
    }

    @Override
    public IAxis getLowerArmAxis() {
        return infinitySystem.getAxis(GauntletAxes.LOWER_ARM);
    }

    @Override
    public IButton getHeadingHoldButton() {
        return saitekDrive.getButton(SaitekButtons.B);
    }

    @Override
    public IButton getResetGyroButton() {
        return saitekDrive.getButton(SaitekButtons.C);
    }

    @Override
    public IButton getRobotOrientationToggleButton() {
        return saitekDrive.getButton(SaitekButtons.A);
    }

    @Override
    public IAxis getMecDriveXAxis() {
        return saitekDrive.getAxis(SaitekAxes.X_AXIS);
    }

    @Override
    public IAxis getMecDriveYAxis() {
        return saitekDrive.getAxis(SaitekAxes.Y_AXIS);
    }

    @Override
    public IAxis getMecDriveRotationAxis() {
        return saitekDrive.getAxis(SaitekAxes.ROTATION);
    }

    @Override
    public IAxis getArmTargetXAxis() {
        //This button will not be used in this configuration
        return unusedAxis;
    }

    @Override
    public IAxis getArmTargetYAxis() {
        //This button will not be used in this configuration
        return unusedAxis;
    }

    @Override
    public IButton getAutoDockButton() {
        return unusedButton;
    }
}

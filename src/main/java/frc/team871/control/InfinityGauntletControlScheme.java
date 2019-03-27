package frc.team871.control;

import com.team871.hid.AxisButton;
import com.team871.hid.ButtonTypes;
import com.team871.hid.ConstantAxis;
import com.team871.hid.ConstantButton;
import com.team871.hid.GenericJoystick;
import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import com.team871.hid.joystick.SaitekAxes;
import com.team871.hid.joystick.SaitekButtons;
import com.team871.hid.joystick.XBoxAxes;
import com.team871.hid.joystick.XBoxButtons;
import java.util.Arrays;

public enum InfinityGauntletControlScheme implements IControlScheme{
    DEFAULT;

    private GenericJoystick<SaitekButtons, SaitekAxes> saitekDrive;
    private GenericJoystick<GauntletButtons, GauntletAxes> infinitySystem;
    private GenericJoystick<XBoxButtons, XBoxAxes> xboxOverride;
    private ConstantAxis unusedAxis;
    private ConstantButton unusedButton;
    private IButton emergency;

    boolean xboxDrive = false;

    InfinityGauntletControlScheme(){
        saitekDrive = new GenericJoystick<>(0, Arrays.asList(SaitekButtons.values()), Arrays.asList(SaitekAxes.values()));
        infinitySystem = new GenericJoystick<>(1, Arrays.asList(GauntletButtons.values()), Arrays.asList(GauntletAxes.values()));
        xboxOverride = new GenericJoystick<>(2, Arrays.asList(XBoxButtons.values()), Arrays.asList(XBoxAxes.values()));
        infinitySystem.getButton(GauntletButtons.Z).setMode(ButtonTypes.RISING);
        infinitySystem.getButton(GauntletButtons.C).setMode(ButtonTypes.RISING);
        infinitySystem.getAxis(GauntletAxes.LOWER_ARM).setOutputRange(80, -75 - 40);
        infinitySystem.getAxis(GauntletAxes.UPPER_ARM).setOutputRange(-0, 110);
        infinitySystem.getAxis(GauntletAxes.Y).setDeadband(0.1);
        saitekDrive.getButton(SaitekButtons.B).setMode(ButtonTypes.MOMENTARY);
        saitekDrive.getButton(SaitekButtons.C).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.A).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.TRIGGER_1).setMode(ButtonTypes.TOGGLE);
        saitekDrive.getButton(SaitekButtons.HAT_DOWN).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.HAT_UP).setMode(ButtonTypes.RISING);

        saitekDrive.getButton(SaitekButtons.SOUND_L_UP).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.SOUND_L_DOWN).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.SOUND_M_UP).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.SOUND_M_DOWN).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.SOUND_R_UP).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.SOUND_R_DOWN).setMode(ButtonTypes.RISING);

        saitekDrive.getButton(SaitekButtons.E).setMode(ButtonTypes.TOGGLE);
        saitekDrive.getButton(SaitekButtons.I).setMode(ButtonTypes.TOGGLE);
        saitekDrive.getButton(SaitekButtons.D).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.FIRE).setMode(ButtonTypes.RISING);
        saitekDrive.getButton(SaitekButtons.LOW_TRIGGER).setMode(ButtonTypes.MOMENTARY);

        emergency = new AxisButton(saitekDrive.getAxis(SaitekAxes.THROTTLE), 0.2f, true);
        emergency.setMode(ButtonTypes.MOMENTARY);

        unusedAxis = new ConstantAxis(0);
        unusedButton = new ConstantButton(false);

        xboxOverride.getButton(XBoxButtons.START).setMode(ButtonTypes.TOGGLE);

    }

    @Override
    public IButton getOuterSuctionButton() {
        return infinitySystem.getButton(GauntletButtons.C);
    }

    @Override
    public IButton getInnerSuctionButton() {
        return infinitySystem.getButton(GauntletButtons.Z);
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

        xboxDrive = xboxOverride.getButton(XBoxButtons.START).getValue();

        return xboxDrive ? xboxOverride.getAxis(XBoxAxes.LEFTX) : saitekDrive.getAxis(SaitekAxes.X_AXIS);
    }

    @Override
    public IAxis getMecDriveYAxis() {
        return xboxDrive ? xboxOverride.getAxis(XBoxAxes.LEFTY) :saitekDrive.getAxis(SaitekAxes.Y_AXIS);
    }

    @Override
    public IAxis getMecDriveRotationAxis() {
        return xboxDrive ? xboxOverride.getAxis(XBoxAxes.RIGHTX) :saitekDrive.getAxis(SaitekAxes.ROTATION);
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

    @Override
    public IAxis getArmSetpointAxis() {
        return saitekDrive.getAxis(SaitekAxes.THROTTLE);
    }

    @Override
    public IButton getArmSetpointUpButton() {
        return saitekDrive.getButton(SaitekButtons.HAT_UP);
    }

    @Override
    public IButton getArmSetpointDownButton() {
        return saitekDrive.getButton(SaitekButtons.HAT_DOWN);
    }

    @Override
    public IButton getEmergencyModeButton() {
        return emergency;
    }

    @Override
    public IButton getClimbAdvanceButton() {
        return saitekDrive.getButton(SaitekButtons.D);
    }

    @Override
    public IButton getClimbUnAdvanceButton() {
        return saitekDrive.getButton(SaitekButtons.FIRE);
    }

    @Override
    public IButton getAutoClimbButton() {
        return saitekDrive.getButton(SaitekButtons.LOW_TRIGGER);
    }

    @Override
    public IButton getClimbFrontButton() {
        return saitekDrive.getButton(SaitekButtons.E);
    }

    @Override
    public IButton getClimbBackButton() {
        return saitekDrive.getButton(SaitekButtons.I);
    }

    public GenericJoystick<SaitekButtons, SaitekAxes> getSaitek(){
        return saitekDrive;
    }

}

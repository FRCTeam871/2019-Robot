package frc.team871.control;

import com.team871.hid.IAxis;
import com.team871.hid.HIDButton;

public interface IControlScheme {

    HIDButton getVacuumToggleButton();

    HIDButton getInverseKinimaticsToggleButton();

    IAxis getWristAxis();

    IAxis getUpperArmAxis();

    IAxis getLowerArmAxis();

    HIDButton getHeadingHoldButton();

    HIDButton getResetGyroButton();

    HIDButton getRobotOrientationToggleButton();

    IAxis getMecDriveXAxis();

    IAxis getMecDriveYAxis();

    IAxis getMecDriveRotationAxis();

    IAxis getArmTargetXAxis();

    IAxis getArmTargetYAxis();


}

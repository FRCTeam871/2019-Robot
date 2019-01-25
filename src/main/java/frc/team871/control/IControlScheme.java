package frc.team871.control;

import com.team871.hid.HIDAxis;
import com.team871.hid.HIDButton;

public interface IControlScheme {

    HIDButton getVacuumToggleButton();

    HIDButton getInverseKinimaticsToggleButton();

    HIDAxis getWristAxis();

    HIDAxis getUpperArmAxis();

    HIDAxis getLowerArmAxis();

    HIDButton getHeadingHoldButton();

    HIDButton getResetGyroButton();

    HIDButton getRobotOrientationToggleButton();

    HIDAxis getMecDriveXAxis();

    HIDAxis getMecDriveYAxis();

    HIDAxis getMecDriveRotationAxis();
}

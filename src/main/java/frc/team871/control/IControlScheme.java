package frc.team871.control;

import com.team871.hid.IAxis;
import com.team871.hid.IButton;

public interface IControlScheme {

    IButton getOuterSuctionButton();

    IButton getInnerSuctionButton();

    IButton getInverseKinematicsToggleButton();

    IButton getWristToggleButton();

    IAxis getWristAxis();

    IAxis getUpperArmAxis();

    IAxis getLowerArmAxis();

    IButton getHeadingHoldButton();

    IButton getResetGyroButton();

    IButton getRobotOrientationToggleButton();

    IAxis getMecDriveXAxis();

    IAxis getMecDriveYAxis();

    IAxis getMecDriveRotationAxis();

    IAxis getArmTargetXAxis();

    IAxis getArmTargetYAxis();

    IButton getAutoDockButton();
    IAxis getArmSetpointAxis();

    IButton getArmSetpointUpButton();

    IButton getArmSetpointDownButton();

}

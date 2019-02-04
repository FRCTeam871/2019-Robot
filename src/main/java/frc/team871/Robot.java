/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team871;


import edu.wpi.first.wpilibj.TimedRobot;
import frc.team871.config.RowBoatConfigHack;
import frc.team871.control.InitialControlSchemeHack;
import frc.team871.subsystems.DriveTrain;
import frc.team871.config.IRowBoatConfig;
import frc.team871.config.RowBoatConfig;
import frc.team871.control.IControlScheme;
import frc.team871.control.InitialControlScheme;
import frc.team871.subsystems.Arm;
import frc.team871.subsystems.ArmSegment;
import frc.team871.subsystems.Vacuum;
import frc.team871.subsystems.Wrist;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private IControlScheme controlScheme;
    private IRowBoatConfig config;
    private DriveTrain driveTrain;
    private Vacuum vacuum;
    private Arm arm;
    private Wrist wrist;

    /**
      * This function is run when the robot is first started up and should be used
      * for any initialization code.
      */
    @Override
    public void robotInit() {
        this.controlScheme = InitialControlSchemeHack.DEFAULT;
        this.config = RowBoatConfigHack.DEFAULT;
//        this.vacuum = new Vacuum(config.getVacuumMotor(), config.getGrabSensor());
        this.driveTrain = new DriveTrain(config.getFrontLeftMotor(), config.getRearLeftMotor(), config.getFrontRightMotor(), config.getRearRightMotor(), config.getGyro());
        // TODO: Get actually lengths of the arm segments
        ArmSegment upperSegment = new ArmSegment(config.getUpperArmMotor(), config.getUpperArmPot(), 20.5);
        ArmSegment lowerSegment = new ArmSegment(config.getLowerArmMotor(), config.getLowerArmPot(),22.);
        this.wrist = new Wrist(config.getWristMotor(), config.getWristPotAxis());
        this.arm = new Arm(upperSegment, lowerSegment, wrist);
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {

        if(driveTrain.getDriveMode() == DriveTrain.DriveMode.ROBOT){
            System.out.println(controlScheme.getMecDriveXAxis() + " " + controlScheme.getMecDriveYAxis());
            System.out.println(controlScheme.getMecDriveXAxis().getValue() + " " + controlScheme.getMecDriveYAxis().getValue());
            driveTrain.driveRobotOriented(controlScheme.getMecDriveXAxis().getValue(), controlScheme.getMecDriveYAxis().getValue(), controlScheme.getMecDriveRotationAxis().getValue());
        } else {
            driveTrain.driveFieldOriented(controlScheme.getMecDriveXAxis().getValue(), controlScheme.getMecDriveYAxis().getValue(), controlScheme.getMecDriveRotationAxis().getValue());
        }
        if(controlScheme.getRobotOrientationToggleButton().getValue()){
            driveTrain.toggleFieldDriveMode();
        }
//        driveTrain.setHeadingHoldEnabled(controlScheme.getHeadingHoldButton().getValue());
        if(controlScheme.getResetGyroButton().getValue()) {
            driveTrain.resetGyro();
        }
//
//        if(controlScheme.getVacuumToggleButton().getValue()) {
//            vacuum.toggleState();
//        }

        arm.upperSegment.rotate(controlScheme.getUpperArmAxis().getValue() * -1.0);
        arm.lowerSegment.rotate(controlScheme.getLowerArmAxis().getValue() * 1.0);
        arm.wrist.setOrientation(controlScheme.getWristAxis().getValue() * 0.5);

//        if(arm.getCurrentArmMode() == Arm.ArmMode.INVERSE_KINEMATICS) {
//            arm.goToRelative(controlScheme.getArmTargetXAxis().getValue(), controlScheme.getArmTargetYAxis().getValue());
//        } else {
//            arm.setAngles(controlScheme.getUpperArmAxis().getValue(), controlScheme.getLowerArmAxis().getValue());
//        }
//        arm.handleInverseKinematicsMode(controlScheme.getInverseKinematicsToggleButton());

        //wrist.handleInputs(controlScheme.getWristAxis(), controlScheme.getWristToggleButton());
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team871;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
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

    Solenoid s1;
    Solenoid s2;

    /**
      * This function is run when the robot is first started up and should be used
      * for any initialization code.
      */
    @Override
    public void robotInit() {
        new Compressor(0).start();
        this.controlScheme = InitialControlSchemeHack.DEFAULT;
        this.config = RowBoatConfigHack.DEFAULT;
        this.vacuum = new Vacuum(config.getVacuumMotor(), config.getGrabSensor());
        this.driveTrain = new DriveTrain(config.getFrontLeftMotor(), config.getRearLeftMotor(), config.getFrontRightMotor(), config.getRearRightMotor(), config.getGyro());
        // TODO: Get actually lengths of the arm segments
        ArmSegment upperSegment = new ArmSegment(config.getUpperArmMotor(), ((RowBoatConfigHack)config).getUpperPot(), 20.5, 718-102-102, 616-102-102); //90 = 102 514
        ArmSegment lowerSegment = new ArmSegment(config.getLowerArmMotor(), ((RowBoatConfigHack)config).getLowerPot(),22., 342, 568);
        this.wrist = new Wrist(config.getWristMotor(), ((RowBoatConfigHack)config).getWristPot());
        this.arm = new Arm(upperSegment, lowerSegment, wrist);
//        wrist.enablePID();
//        lowerSegment.enablePID();
////        config.getUpperArmMotor().setInverted(true);
//        upperSegment.enablePID();

//        wrist.setOrientation(-90);
//        lowerSegment.setAngle(-35);
//        upperSegment.setAngle(-120);

        s1 = new Solenoid(0);
        s2 = new Solenoid(1);
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
    public void robotPeriodic() {

//        System.out.println("VAL = " + ((RowBoatConfigHack)config).getLowerPot().getRaw());
        System.out.println("ANG = " + arm.lowerSegment.getAngle() + " " + arm.upperSegment.getAngle() + " " + wrist.getAngle());
    }

    @Override
    public void teleopPeriodic() {
//
//        if(driveTrain.getDriveMode() == DriveTrain.DriveMode.ROBOT){
//            System.out.println(controlScheme.getMecDriveXAxis() + " " + controlScheme.getMecDriveYAxis());
//            System.out.println(controlScheme.getMecDriveXAxis().getValue() + " " + controlScheme.getMecDriveYAxis().getValue());
//            driveTrain.driveRobotOriented(-controlScheme.getMecDriveYAxis().getValue(), controlScheme.getMecDriveXAxis().getValue(), controlScheme.getMecDriveRotationAxis().getValue());
//        } else {
//            driveTrain.driveFieldOriented(controlScheme.getMecDriveXAxis().getValue(), controlScheme.getMecDriveYAxis().getValue(), controlScheme.getMecDriveRotationAxis().getValue());
//        }
//        if(controlScheme.getRobotOrientationToggleButton().getValue()){
//            driveTrain.toggleFieldDriveMode();
//        }
////        driveTrain.setHeadingHoldEnabled(controlScheme.getHeadingHoldButton().getValue());
//        if(controlScheme.getResetGyroButton().getValue()) {
//            driveTrain.resetGyro();
//        }

//        config.getFrontLeftMotor().set(-0.15);
//        config.getFrontRightMotor().set(-0.15);
//        config.getRearLeftMotor().set(-0.15);
//        config.getRearRightMotor().set(-0.15);

        vacuum.setState(controlScheme.getVacuumToggleButton().getValue() ? Vacuum.VacuumState.ENABLED : Vacuum.VacuumState.DISABLED);


        InitialControlSchemeHack cs = (InitialControlSchemeHack)controlScheme;
//        wrist.setOrientation((cs.getWristAxis().getValue() - cs.getWristAxis2().getValue()) * 90);
//        arm.lowerSegment.setAngle(cs.getLowerArmAxis().getValue() * -90);
//        arm.upperSegment.setAngle(cs.getUpperArmAxis().getValue() * -90);

//
//        if(controlScheme.getVacuumToggleButton().getValue()) {
//            vacuum.toggleState();
//        }

        arm.upperSegment.rotate(controlScheme.getUpperArmAxis().getValue() * -1.0);
        arm.lowerSegment.rotate(controlScheme.getLowerArmAxis().getValue() * 1.0);
        arm.wrist.setOrientation((cs.getWristAxis().getValue() - cs.getWristAxis2().getValue()));
//        arm.lowerSegment.setAngle((cs.getWristAxis().getValue() - cs.getWristAxis2().getValue()) * 45);

//        s1.set(controlScheme.getWristToggleButton().getValue());
//        s2.set(((InitialControlSchemeHack) controlScheme).getSol2().getValue());

//        if(arm.getCurrentArmMode() == Arm.ArmMode.INVERSE_KINEMATICS) {
//            arm.goToRelative(controlScheme.getArmTargetXAxis().getValue(), controlScheme.getArmTargetYAxis().getValue());
//        } else {
//            arm.setAngles(controlScheme.getUpperArmAxis().getValue(), controlScheme.getLowerArmAxis().getValue());
//        }
//        arm.handleInverseKinematicsMode(controlScheme.getInverseKinematicsToggleButton());

//        System.out.println("ANGLE = " + wrist.getAngle());
//        arm.lowerSegment.tickCalibration(((RowBoatConfigHack)config).getLowerPot(), controlScheme.getWristToggleButton());
//        arm.upperSegment.tickCalibration(((RowBoatConfigHack)config).getUpperPot(), controlScheme.getWristToggleButton());

        //System.out.println("ANGLE2 = " + ((RowBoatConfigHack)config).getLowerPot().getRaw());
//        System.out.println("pot = " + ((RowBoatConfigHack)config).getWristPot().getRaw());
//        wrist.handleInputs(controlScheme.getWristAxis(), controlScheme.getWristToggleButton());
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

}

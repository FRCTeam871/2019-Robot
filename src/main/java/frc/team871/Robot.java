/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team871;


import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.team871.control.InfinityGauntletControlScheme;
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
    private ArmSegment upperSegment;
    private ArmSegment lowerSegment;

    private boolean manualDriveMode = false;
    private boolean driveTrainEnabled = false;

    /**
      * This function is run when the robot is first started up and should be used
      * for any initialization code.
      */
    @Override
    public void robotInit() {
        this.controlScheme = InfinityGauntletControlScheme.DEFAULT;
        this.config = RowBoatConfig.DEFAULT;
        this.vacuum = new Vacuum(config.getVacuumMotor(), config.getGrabSensor(), config.getVacuumInnerValve(), config.getVacuumOuterValve()); //TODO: add solenoids to config
        this.driveTrain = new DriveTrain(config.getFrontLeftMotor(), config.getRearLeftMotor(), config.getFrontRightMotor(), config.getRearRightMotor(), config.getGyro());
        // TODO: Get actually lengths of the arm segments
        upperSegment = new ArmSegment(config.getUpperArmMotor(), config.getUpperArmPot(), config.getUpperArmPIDConfig(), 20.5);
        lowerSegment = new ArmSegment(config.getLowerArmMotor(), config.getLowerArmPot(), config.getLowerArmPIDConfig(),22.);
        this.wrist = new Wrist(config.getWristMotor(), config.getWristPotAxis(), config.getWristPIDConfig());
        this.arm = new Arm(upperSegment, lowerSegment, wrist);
    }

    @Override
    public void robotPeriodic() {
        //System.out.println();
//        System.out.println(lowerSegment.getAngle() + " " + upperSegment.getAngle() + " " + wrist.getAngle());
//        System.out.println(config.getUpperArmPot().getRaw() + " -> " + config.getUpperArmPot().getValue());
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {
        //set the default PID setpoints as the current position so it doesn't freak out instantly
        if(!manualDriveMode) {
            upperSegment.setAngle(upperSegment.getAngle());
            lowerSegment.setAngle(lowerSegment.getAngle());
//            wrist.setOrientation(wrist.getAngle());

            wrist.setOrientation(0);
            upperSegment.setAngle(0);
            lowerSegment.setAngle(0);


            upperSegment.enablePID();
            lowerSegment.enablePID();
            wrist.enablePID();
        }
    }

    @Override
    public void teleopPeriodic() {
        if(driveTrainEnabled) {
            if (driveTrain.getDriveMode() == DriveTrain.DriveMode.ROBOT) {
                driveTrain.driveRobotOriented(controlScheme.getMecDriveYAxis().getValue(), controlScheme.getMecDriveXAxis().getValue(), controlScheme.getMecDriveRotationAxis().getValue());
            } else {
                driveTrain.driveFieldOriented(controlScheme.getMecDriveYAxis().getValue(), controlScheme.getMecDriveXAxis().getValue(), controlScheme.getMecDriveRotationAxis().getValue());
            }
            if (controlScheme.getRobotOrientationToggleButton().getValue()) {
                driveTrain.toggleFieldDriveMode();
            }
            driveTrain.setHeadingHoldEnabled(controlScheme.getHeadingHoldButton().getValue());
            if (controlScheme.getResetGyroButton().getValue()) {
                driveTrain.resetGyro();
            }
        }

        vacuum.setState(controlScheme.getVacuumToggleButton());

        if(!manualDriveMode){
            arm.handleArmAxes(controlScheme.getUpperArmAxis(), controlScheme.getLowerArmAxis(), controlScheme.getArmTargetXAxis(), controlScheme.getMecDriveYAxis());
            arm.handleInverseKinematicsMode(controlScheme.getInverseKinematicsToggleButton());

            wrist.handleInputs(controlScheme.getWristAxis(), controlScheme.getWristToggleButton());

        }else{
            lowerSegment.rotate(controlScheme.getLowerArmAxis().getValue());
            upperSegment.rotate(controlScheme.getUpperArmAxis().getValue());
            wrist.rotate(controlScheme.getWristAxis().getValue());
        }

    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

}

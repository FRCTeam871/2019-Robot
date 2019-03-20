/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team871;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.team871.config.IRowBoatConfig;
import frc.team871.config.SecondRowBoatConfig;
import frc.team871.control.IControlScheme;
import frc.team871.control.InfinityGauntletControlScheme;
import frc.team871.control.InitialControlScheme;
import frc.team871.subsystems.Arm;
import frc.team871.subsystems.ArmSegment;
import frc.team871.subsystems.DriveTrain;
import frc.team871.subsystems.Vacuum;
import frc.team871.subsystems.Wrist;
import java.text.DecimalFormat;

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
    private boolean driveTrainEnabled = true;
    private boolean testBoard = false;
    private long lastPrint = System.currentTimeMillis();


    /**
      * This function is run when the robot is first started up and should be used
      * for any initialization code.
      */
    @Override
    public void robotInit() {
        this.config = SecondRowBoatConfig.DEFAULT;
        this.controlScheme = manualDriveMode ? InitialControlScheme.DEFAULT : InfinityGauntletControlScheme.DEFAULT;
        this.driveTrain = new DriveTrain(config.getFrontLeftMotor(), config.getRearLeftMotor(), config.getFrontRightMotor(), config.getRearRightMotor(), config.getGyro(), config.getHeadingPIDConfig(), config.getAutoDockXPIDConfig());

        if(!testBoard) {
            this.vacuum = new Vacuum(config.getVacuumMotor(), config.getGrabSensor(), config.getVacuumInnerValve(), config.getVacuumOuterValve()); //TODO: add solenoids to config

            upperSegment = new ArmSegment(config.getUpperArmMotor(), config.getUpperArmPot(), config.getUpperArmPIDConfig(), 20.5);
            lowerSegment = new ArmSegment(config.getLowerArmMotor(), config.getLowerArmPot(), config.getLowerArmPIDConfig(),22);
            this.wrist = new Wrist(config.getWristMotor(), config.getWristPotAxis(), config.getWristPIDConfig(), 10);
            this.arm = new Arm(upperSegment, lowerSegment, wrist);

            LiveWindow.add(arm);
        }
    }

    @Override
    public void robotPeriodic() {
        //TODO: network tables
        boolean printLineStatus = false;
        if(printLineStatus && System.currentTimeMillis() - lastPrint > 500) {
            lastPrint = System.currentTimeMillis();
            if (config.getTargetProvider().getLineSensor().doesTargetExist()) {
                DecimalFormat d = new DecimalFormat("0.0");
                System.out.println(d.format(config.getTargetProvider().getLineSensor().getCenterX()) + "\t" + d.format(config.getTargetProvider().getLineSensor().getLineAngle()));
            } else {
                System.out.println("No line");
            }
        }

        boolean printTargetStatus = false;
        if(printTargetStatus && System.currentTimeMillis() - lastPrint > 500) {
            lastPrint = System.currentTimeMillis();
            if (config.getTargetProvider().getTarget().doesTargetExist()) {
                DecimalFormat d = new DecimalFormat("0.0");
                System.out.println(d.format(config.getTargetProvider().getTarget().getCenterX()) + "\t" + d.format(config.getTargetProvider().getTarget().getCenterY()) + "\t" + d.format(config.getTargetProvider().getTarget().getLengthX()) + "\t" + d.format(config.getTargetProvider().getTarget().getLengthY()));
            } else {
                System.out.println("No target");
            }
        }

//        System.out.println(controlScheme.getArmTargetYAxis().getRaw() + " " + controlScheme.getArmTargetXAxis().getRaw() + " -> " + controlScheme.getArmTargetXAxis().getValue());
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {
        teleopPeriodic();
    }

    @Override
    public void teleopInit() {
        if(!testBoard) {
            //set the default PID setpoints as the current position so it doesn't freak out instantly
            if (!manualDriveMode) {
//                upperSegment.setAngle(upperSegment.getAngle());
//                lowerSegment.setAngle(lowerSegment.getAngle());
//                wrist.setOrientation(wrist.getAngle());

//                upperSegment.setAngle(0);
//                lowerSegment.setAngle(0);
                upperSegment.enablePID();
                lowerSegment.enablePID();
                wrist.enablePID();
            }
        }
    }

    @Override
    public void teleopPeriodic() {
        if(driveTrainEnabled) {
            driveTrain.handleInputs(controlScheme.getMecDriveXAxis(), controlScheme.getMecDriveYAxis(), controlScheme.getMecDriveRotationAxis(), controlScheme.getRobotOrientationToggleButton(), controlScheme.getHeadingHoldButton(), controlScheme.getResetGyroButton(), config.getTargetProvider(), controlScheme.getAutoDockButton());
        }

        if(testBoard) return;

        vacuum.handleInputs(controlScheme.getInnerSuctionButton(), controlScheme.getOuterSuctionButton());

        if(!manualDriveMode) {
            arm.handleArmAxes(controlScheme.getUpperArmAxis(), controlScheme.getLowerArmAxis(), controlScheme.getArmTargetXAxis(), controlScheme.getArmTargetYAxis());
            arm.handleInverseKinematicsMode(controlScheme.getInverseKinematicsToggleButton());
            wrist.setOrientation((-lowerSegment.getAngle() - upperSegment.getAngle()) + controlScheme.getWristAxis().getValue() * 90-20);
//            wrist.handleInputs(controlScheme.getWristAxis(), controlScheme.getWristToggleButton());
        } else {
            lowerSegment.rotate(controlScheme.getLowerArmAxis().getValue());
            upperSegment.rotate(controlScheme.getUpperArmAxis().getValue());
            wrist.rotate(controlScheme.getWristAxis().getValue());
        }
    }
}

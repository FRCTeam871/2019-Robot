/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team871;


import com.team871.navigation.Coordinate;
import com.team871.navigation.Navigation;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.team871.auto.DockingWaypointProvider;
import frc.team871.auto.GripPipeline;
import frc.team871.auto.ITargetProvider;
import frc.team871.auto.RobotUSBTargetProvider;
import frc.team871.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.team871.config.IRowBoatConfig;
import frc.team871.config.RowBoatConfig;
import frc.team871.control.IControlScheme;
import frc.team871.control.InfinityGauntletControlScheme;
import frc.team871.control.SaitekControlScheme;
import frc.team871.subsystems.Arm;
import frc.team871.subsystems.ArmSegment;
import frc.team871.subsystems.DriveTrain;
import frc.team871.subsystems.Vacuum;
import frc.team871.subsystems.Wrist;
import java.text.DecimalFormat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

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
    private Navigation nav;
    private DockingWaypointProvider waypointProvider;
    private ITargetProvider targetProvider;

    boolean testBoard = true;
    private ArmSegment upperSegment;
    private ArmSegment lowerSegment;

    private boolean manualDriveMode = false;
    private boolean driveTrainEnabled = true;

    /**
      * This function is run when the robot is first started up and should be used
      * for any initialization code.
      */
    @Override
    public void robotInit() {
        if(!testBoard) {
            this.controlScheme = InitialControlScheme.DEFAULT;
            this.config = RowBoatConfig.DEFAULT;
            this.vacuum = new Vacuum(config.getVacuumMotor(), config.getGrabSensor());
            this.driveTrain = new DriveTrain(config.getFrontLeftMotor(), config.getRearLeftMotor(), config.getFrontRightMotor(), config.getRearRightMotor(), config.getGyro());
            // TODO: Get actually lengths of the arm segments
            ArmSegment upperSegment = new ArmSegment(config.getUpperArmMotor(), config.getUpperArmPot(), 20.5);
            ArmSegment lowerSegment = new ArmSegment(config.getLowerArmMotor(), config.getLowerArmPot(), 22.);
            this.wrist = new Wrist(config.getWristMotor(), config.getWristPotAxis());
            this.arm = new Arm(upperSegment, lowerSegment, wrist);
            this.targetProvider = new RobotUSBTargetProvider(config.getLineCam());
        }




        this.controlScheme = SaitekControlScheme.DEFAULT;

        this.config = RowBoatConfig.DEFAULT;
        this.vacuum = new Vacuum(config.getVacuumMotor(), config.getGrabSensor(), config.getVacuumInnerValve(), config.getVacuumOuterValve()); //TODO: add solenoids to config
        this.driveTrain = new DriveTrain(config.getFrontLeftMotor(), config.getRearLeftMotor(), config.getFrontRightMotor(), config.getRearRightMotor(), config.getGyro());

        upperSegment = new ArmSegment(config.getUpperArmMotor(), config.getUpperArmPot(), config.getUpperArmPIDConfig(), 20.5);
        lowerSegment = new ArmSegment(config.getLowerArmMotor(), config.getLowerArmPot(), config.getLowerArmPIDConfig(),22);
        this.wrist = new Wrist(config.getWristMotor(), config.getWristPotAxis(), config.getWristPIDConfig(), 10);
        this.arm = new Arm(upperSegment, lowerSegment, wrist);


        LiveWindow.add(arm);

    }

    @Override
    public void robotPeriodic() {
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
        //set the default PID setpoints as the current position so it doesn't freak out instantly
        if(!manualDriveMode) {
            upperSegment.setAngle(upperSegment.getAngle());
            lowerSegment.setAngle(lowerSegment.getAngle());
            wrist.setOrientation(wrist.getAngle());

//            wrist.setOrientation(0);
//            upperSegment.setAngle(0);
//            lowerSegment.setAngle(0);


            upperSegment.enablePID();
            lowerSegment.enablePID();
            wrist.enablePID();
        }
    }

    @Override
    public void teleopPeriodic() {

        if(testBoard) return;

        driveTrain.handleInputs(controlScheme.getMecDriveXAxis(), controlScheme.getMecDriveYAxis(), controlScheme.getMecDriveRotationAxis(), controlScheme.getRobotOrientationToggleButton(), controlScheme.getHeadingHoldButton(), controlScheme.getResetGyroButton(), config.getTargetProvider(), controlScheme.getAutoDockButton());

        vacuum.setState(controlScheme.getVacuumToggleButton());
        vacuum.setTogglePrimarySucc(controlScheme.getVacuumPrimaryButton());

        if(!manualDriveMode){
            arm.handleArmAxes(controlScheme.getUpperArmAxis(), controlScheme.getLowerArmAxis(), controlScheme.getArmTargetXAxis(), controlScheme.getArmTargetYAxis());
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
//        teleopPeriodic();
    }
}

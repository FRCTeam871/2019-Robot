/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team871;


import com.team871.io.peripheral.EndPoint;
import com.team871.io.peripheral.SerialCommunicationInterface;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.team871.config.IRowBoatConfig;
import frc.team871.config.RowBoatConfig;
import frc.team871.config.SecondRowBoatConfig;
import frc.team871.control.IControlScheme;
import frc.team871.control.InfinityGauntletControlScheme;
import frc.team871.control.InitialControlScheme;
import frc.team871.control.SaitekControlScheme;
import frc.team871.subsystems.Arm;
import frc.team871.subsystems.ArmSegment;
import frc.team871.subsystems.DriveTrain;
import frc.team871.subsystems.Vacuum;
import frc.team871.subsystems.Wrist;
import frc.team871.subsystems.peripheral.LEDStripMode;
import frc.team871.subsystems.peripheral.LEDStripSettings;
import frc.team871.subsystems.peripheral.Teensy;

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
    private Teensy teensyWeensy;

    private boolean manualDriveMode = false;
    private boolean driveTrainEnabled = true;
    private boolean goHome = false;
    private boolean testBoard = false;
    private long lastPrint = System.currentTimeMillis();

    long t = System.currentTimeMillis();

    /**
      * This function is run when the robot is first started up and should be used
      * for any initialization code.
      */
    @Override
    public void robotInit() {
        this.config = RowBoatConfig.DEFAULT;
        this.controlScheme = manualDriveMode ? InitialControlScheme.DEFAULT : InfinityGauntletControlScheme.DEFAULT;
//        this.controlScheme = SaitekControlScheme.DEFAULT;
        this.driveTrain = new DriveTrain(config.getFrontLeftMotor(), config.getRearLeftMotor(), config.getFrontRightMotor(), config.getRearRightMotor(), config.getGyro(), config.getHeadingPIDConfig(), config.getAutoDockXPIDConfig());

        if(!testBoard) {
            this.vacuum = new Vacuum(config.getVacuumMotor(), config.getGrabSensor(), config.getVacuumInnerValve(), config.getVacuumOuterValve()); //TODO: add solenoids to config

            upperSegment = new ArmSegment(config.getUpperArmMotor(), config.getUpperArmPot(), config.getUpperArmPIDConfig(), 20.5);
            lowerSegment = new ArmSegment(config.getLowerArmMotor(), config.getLowerArmPot(), config.getLowerArmPIDConfig(),22);
            this.wrist = new Wrist(config.getWristMotor(), config.getWristPotAxis(), config.getWristPIDConfig(), 10);
            this.arm = new Arm(upperSegment, lowerSegment, wrist);


        this.teensyWeensy = new Teensy(new SerialCommunicationInterface(), EndPoint.NULL_ENDPOINT);
        teensyWeensy.writeLED(1, LEDStripMode.rainbowChase(5000, 100));
        teensyWeensy.writeLED(2, LEDStripMode.rainbowChase(5000, 100));
        teensyWeensy.writeLED(1, LEDStripSettings.brightness(100));
        teensyWeensy.writeLED(2, LEDStripSettings.brightness(100));

        teensyWeensy.writeLED(1, LEDStripMode.fade(0x000000, 0xff0000, 2000, 0));
        teensyWeensy.writeLED(2, LEDStripMode.fade(0x000000, 0xff0000, 2000, 0));

        LiveWindow.add(arm);

            if(this.controlScheme instanceof SaitekControlScheme){
                arm.setMode(Arm.ArmMode.SETPOINT);
            }
        }
    }

    @Override
    public void robotPeriodic() {
        teensyWeensy.update();
    }

    @Override
    public void autonomousInit() {
        vacuum.setSideOpen(Vacuum.VacuumSide.OUTER);
        teensyWeensy.writeLED(1, LEDStripMode.fade(0x000000, 0x00ff00, 500, 20));
        teensyWeensy.writeLED(2, LEDStripMode.fade(0x000000, 0x00ff00, 500, 20));
        teleopInit();
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
//                wrist.setOrientation(0);
                upperSegment.enablePID();
                lowerSegment.enablePID();
                wrist.enablePID();
            }

            if(manualDriveMode && goHome){
                upperSegment.setAngle(-122.0);
                lowerSegment.setAngle(74.7);
                wrist.setOrientation(-112.1);
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

        Vacuum.VacuumState prev = vacuum.getState();
        vacuum.handleInputs(controlScheme.getInnerSuctionButton(), controlScheme.getOuterSuctionButton());
        Vacuum.VacuumState now = vacuum.getState();

        if(now != prev){
            if(now == Vacuum.VacuumState.DISABLED) {
                teensyWeensy.writeLED(1, LEDStripMode.fade(0x000000, 0xff0000, 500, 20));
                teensyWeensy.writeLED(2, LEDStripMode.fade(0x000000, 0xff0000, 500, 20));
            }else{
                teensyWeensy.writeLED(1, LEDStripMode.fade(0x000000, 0x00ff00, 500, 20));
                teensyWeensy.writeLED(2, LEDStripMode.fade(0x000000, 0x00ff00, 500, 20));
            }
        }


        if(!manualDriveMode){
//            if(System.currentTimeMillis() - t > 2000) {
                arm.handleArmAxes(controlScheme.getUpperArmAxis(), controlScheme.getLowerArmAxis(), controlScheme.getArmTargetXAxis(), controlScheme.getArmTargetYAxis(), controlScheme.getArmSetpointAxis(), controlScheme.getArmSetpointUpButton(), controlScheme.getArmSetpointDownButton());
                arm.handleInverseKinematicsMode(controlScheme.getInverseKinematicsToggleButton());
                double delta = controlScheme.getWristAxis().getValue() * 120;
                if (controlScheme.getWristAxis().getValue() > 0.5) {
//                delta = 90;
                }
                wrist.setOrientation((-lowerSegment.getAngle() - upperSegment.getAngle()) + delta);
//            }
//            wrist.handleInputs(controlScheme.getWristAxis(), controlScheme.getWristToggleButton());
        } else if(manualDriveMode){
            if(!goHome) {
                lowerSegment.rotate(controlScheme.getLowerArmAxis().getValue());
                upperSegment.rotate(controlScheme.getUpperArmAxis().getValue());
                wrist.rotate(controlScheme.getWristAxis().getValue());
            }
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

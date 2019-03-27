/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team871;


import com.team871.io.peripheral.EndPoint;
import com.team871.io.peripheral.SerialCommunicationInterface;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.team871.config.IRowBoatConfig;
import frc.team871.config.SecondRowBoatConfig;
import frc.team871.control.IControlScheme;
import frc.team871.control.InfinityGauntletControlScheme;
import frc.team871.control.InitialControlScheme;
import frc.team871.control.SaitekControlScheme;
import frc.team871.subsystems.Arm;
import frc.team871.subsystems.ArmSegment;
import frc.team871.subsystems.Climb;
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
    private Climb climb;

    private ArmSegment upperSegment;
    private ArmSegment lowerSegment;
    private Teensy teensyWeensy;

    private boolean manualDriveMode = false;
    private boolean driveTrainEnabled = true;
    private boolean goHome = false;
    private boolean testBoard = false;
    private boolean leds = true;
    private boolean armEnabled = false;
    private long lastPrint = System.currentTimeMillis();

    long t = System.currentTimeMillis();
    boolean wasEmergency = false;

    long lastTeamCheck = System.currentTimeMillis();
    DriverStation.Alliance lastAlliance = DriverStation.Alliance.Invalid;

    boolean wasStrong = false;

    boolean unfold = true;
    long unfoldStart;

    /**
      * This function is run when the robot is first started up and should be used
      * for any initialization code.
      */
    @Override
    public void robotInit() {
        this.config = SecondRowBoatConfig.DEFAULT;
        this.controlScheme = manualDriveMode ? InitialControlScheme.DEFAULT : InfinityGauntletControlScheme.DEFAULT;
        this.controlScheme = InitialControlScheme.DEFAULT;
        this.driveTrain = new DriveTrain(config.getFrontLeftMotor(), config.getRearLeftMotor(), config.getFrontRightMotor(), config.getRearRightMotor(), config.getGyro(), config.getHeadingPIDConfig(), config.getAutoDockXPIDConfig());

        if(!testBoard) {
            this.vacuum = new Vacuum(config.getVacuumMotor(), config.getGrabSensor(), config.getVacuumInnerValve(), config.getVacuumOuterValve()); //TODO: add solenoids to config

            upperSegment = new ArmSegment(config.getUpperArmMotor(), config.getUpperArmPot(), config.getUpperArmPIDConfig(), 20.5);
            lowerSegment = new ArmSegment(config.getLowerArmMotor(), config.getLowerArmPot(), config.getLowerArmPIDConfig(),22);
            this.wrist = new Wrist(config.getWristMotor(), config.getWristPotAxis(), config.getWristPIDConfig(), 10);
            this.arm = new Arm(upperSegment, lowerSegment, wrist);


            this.teensyWeensy = new Teensy(new SerialCommunicationInterface(), EndPoint.NULL_ENDPOINT);
//            teensyWeensy.writeSound(Audio.setVolume(100));
//            teensyWeensy.writeSound(Audio.play("ut/spider.wav"));
            for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.rainbowChase(5000, 100));
            for(int i = 0; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripSettings.brightness(leds ? 100 : 0));

//            for(int i = 3; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripSettings.reverse(true));
            teensyWeensy.writeLED(0, LEDStripMode.chase((int)(500 / 24.0 * 2),500, 250,0x0000ff, 0xff0000));
//            teensyWeensy.writeLED(0, LEDStripMode.fade(0x0000ff, 0xff0000,500, (int)(500 / 24.0 * 2)));


            LiveWindow.add(arm);

            if(this.controlScheme instanceof SaitekControlScheme){
                arm.setMode(Arm.ArmMode.SETPOINT);
            }

            climb = new Climb(arm, driveTrain, config.getFrontClimbPistons(), config.getBackClimbPistons());
        }

        if (manualDriveMode || !driveTrainEnabled || goHome || !armEnabled || (controlScheme.getEmergencyModeButton().getRaw() && DriverStation.getInstance().isDSAttached())) {
            for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.chase(0, 500, 250, 0x000000, 0xff0000));
        }


    }

    @Override
    public void robotPeriodic() {
        teensyWeensy.update();

//        if(controlScheme instanceof InfinityGauntletControlScheme){
//            InfinityGauntletControlScheme c = (InfinityGauntletControlScheme) controlScheme;
//            GenericJoystick<SaitekButtons, SaitekAxes> saitek = c.getSaitek();
//            if(saitek.getButton(SaitekButtons.SOUND_L_UP).getValue()){
//                teensyWeensy.writeSound(Audio.play("sandfull.wav"));
//            }else if(saitek.getButton(SaitekButtons.SOUND_L_DOWN).getValue()){
//                teensyWeensy.writeSound(Audio.play("ut/megalo.wav"));
//            }else if(saitek.getButton(SaitekButtons.SOUND_M_UP).getValue()){
//                teensyWeensy.writeSound(Audio.play("ut/spider.wav"));
//            }else if(saitek.getButton(SaitekButtons.SOUND_M_DOWN).getValue()){
//                teensyWeensy.writeSound(Audio.play("ut/datingfi.wav"));
//            }else if(saitek.getButton(SaitekButtons.SOUND_R_UP).getValue()){
//                teensyWeensy.writeSound(Audio.play("ut/songthat.wav"));
//            }else if(saitek.getButton(SaitekButtons.SOUND_R_DOWN).getValue()){
//                teensyWeensy.writeSound(Audio.play("ut/asgorefu.wav"));
//            }
//        }

    }

    @Override
    public void autonomousInit() {
        vacuum.setSideOpen(Vacuum.VacuumSide.OUTER);
        for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fade(0x000000, 0x00ff00, 500, 20));
        teleopInit();
    }

    @Override
    public void autonomousPeriodic() {
        teleopPeriodic();
    }

    @Override
    public void teleopInit() {
        if(!testBoard) {
            for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fade(0x000000, 0xff0000, 500, 20));
            //set the default PID setpoints as the current position so it doesn't freak out instantly
            if (!manualDriveMode) {
//                upperSegment.setAngle(upperSegment.getAngle());
//                lowerSegment.setAngle(lowerSegment.getAngle());
//                wrist.setOrientation(wrist.getAngle());

//                upperSegment.setAngle(0);
//                lowerSegment.setAngle(0);
//                wrist.setOrientation(0);
                if(armEnabled) {

                    if(unfold){
                        unfoldStart = System.currentTimeMillis();
                        upperSegment.setAngle(0.0);
                        lowerSegment.setAngle(-70);
                        wrist.setOrientation(0);
                    }

                    upperSegment.enablePID();
                    lowerSegment.enablePID();
                    wrist.enablePID();
                }
            }

            if(manualDriveMode && goHome){
//                upperSegment.setAngle(0);
//                lowerSegment.setAngle(0);
//                wrist.setOrientation(0);
                upperSegment.setAngle(-111.0);
                lowerSegment.setAngle(74.7);
                wrist.setOrientation(-114.0);
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

        if(!manualDriveMode) {
            Vacuum.VacuumState prev = vacuum.getState();
            vacuum.handleInputs(controlScheme.getInnerSuctionButton(), controlScheme.getOuterSuctionButton(), controlScheme.getEmergencyModeButton());
            Vacuum.VacuumState now = vacuum.getState();

            boolean nowStrong = controlScheme.getInnerSuctionButton().getRaw();

//        System.out.println(controlScheme.getEmergencyModeButton().getRaw() + " " + controlScheme.getEmergencyModeButton().getValue());
            if (now != prev || (controlScheme.getEmergencyModeButton().getRaw() != wasEmergency) || nowStrong != wasStrong) {
                if (controlScheme.getEmergencyModeButton().getRaw()) {
                    if (now == Vacuum.VacuumState.DISABLED) {
                        for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.chase(40, 300, 150, 0x000000, 0xff0000));
                    } else {
                        for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.chase(40, 300, 150, 0x000000, 0x00ff00));
                    }
                } else {
                    if (controlScheme.getInnerSuctionButton().getRaw()) {
                        if (now == Vacuum.VacuumState.DISABLED) {
                            for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fade(0x000000, 0xff0000, 250, 40));
                        } else {
                            for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fade(0x000000, 0x00ff00, 250, 40));
                        }
                    } else {
                        if (now == Vacuum.VacuumState.DISABLED) {
                            for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fade(0x000000, 0xff0000, 500, 20));
                        } else {
                            for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fade(0x000000, 0x00ff00, 500, 20));
                        }
                    }
                }
            }
            wasStrong = nowStrong;
            wasEmergency = controlScheme.getEmergencyModeButton().getRaw();
        }

        if(!manualDriveMode){
//            if(System.currentTimeMillis() - t > 2000) {
            if(armEnabled) {
                if(unfold){
                    if(arm.isAtTarget() || System.currentTimeMillis() - unfoldStart > 5000){
                        unfold = false;
                        upperSegment.enablePID();
                    }
                }

                if(!unfold) arm.handleArmAxes(controlScheme.getUpperArmAxis(), controlScheme.getLowerArmAxis(), controlScheme.getArmTargetXAxis(), controlScheme.getArmTargetYAxis(), controlScheme.getArmSetpointAxis(), controlScheme.getArmSetpointUpButton(), controlScheme.getArmSetpointDownButton());
                arm.handleInverseKinematicsMode(controlScheme.getInverseKinematicsToggleButton());
                double raw = controlScheme.getWristAxis().getValue();

                double v = raw * raw * (raw < 0 ? -1 : 1);

                double delta = v * 120;
                if (controlScheme.getWristAxis().getValue() > 0.5) {
//                delta = 90;
                }
                wrist.setOrientation((-lowerSegment.getAngle() - upperSegment.getAngle()) + delta);
//            }
//            wrist.handleInputs(controlScheme.getWristAxis(), controlScheme.getWristToggleButton());
            }
        } else if(manualDriveMode){
            if(!goHome) {
                lowerSegment.rotate(controlScheme.getLowerArmAxis().getValue());
                upperSegment.rotate(controlScheme.getUpperArmAxis().getValue());
                wrist.rotate(controlScheme.getWristAxis().getValue());
            }
        }

        climb.update(controlScheme.getClimbAdvanceButton(), controlScheme.getClimbUnAdvanceButton(), controlScheme.getClimbFrontButton(), controlScheme.getClimbBackButton());

    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {
//        teleopPeriodic();
    }

    @Override
    public void disabledInit() {
        if(!manualDriveMode) {
            if (!DriverStation.getInstance().isFMSAttached() && !controlScheme.getEmergencyModeButton().getRaw()) {
//            teensyWeensy.writeLED(1, LEDStripMode.bounce(4, 0x0000ff, 0xff0000, 0x0000ff, 0xff0000));
//            teensyWeensy.writeLED(2, LEDStripMode.bounce(4, 0x0000ff, 0xff0000, 0x0000ff, 0xff0000));
                for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fire(Teensy.RAINBOW));
//                for(int i = 3; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.bounce(4, 0x0000ff, 0xff0000, 0x0000ff, 0xff0000));
            }
        }
    }

    @Override
    public void disabledPeriodic() {
        if(DriverStation.getInstance().isFMSAttached()) {
            if(System.currentTimeMillis() - lastTeamCheck > 1000) {
                lastTeamCheck = System.currentTimeMillis();
                DriverStation.Alliance nowAlliance = DriverStation.getInstance().getAlliance();

                if (nowAlliance != lastAlliance) {
                    if (nowAlliance == DriverStation.Alliance.Red) {
                        for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fade(0x000000, 0xff0000, 2000, 0));
                    } else if (nowAlliance == DriverStation.Alliance.Blue) {
                        for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fade(0x000000, 0x0000ff, 2000, 0));
                    } else {
                        for(int i = 1; i <= 2; i++) teensyWeensy.writeLED(i, LEDStripMode.fade(0x000000, 0x888888, 2000, 0));
                    }
                }
                lastAlliance = nowAlliance;
            }
        }else{
            if(System.currentTimeMillis() - lastTeamCheck > 5000) {
                lastTeamCheck = System.currentTimeMillis();
                switch((int)(Math.random() * 2)){
                    case 0:

                        break;
                    case 1:

                        break;
                }
            }
        }
    }
}

package frc.team871.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import com.team871.io.sensor.IDisplacementSensor;
import com.team871.navigation.Coordinate;
import com.team871.navigation.DistanceUnit;
import com.team871.navigation.Navigation;
import com.team871.navigation.Waypoint;
import com.team871.subsystem.IDriveTrain;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team871.SettablePIDSource;
import frc.team871.auto.DockingWaypointProvider;
import frc.team871.auto.ILineSensor;
import frc.team871.auto.ITarget;
import frc.team871.auto.ITargetProvider;
import frc.team871.config.PIDConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class DriveTrain extends MecanumDrive implements IDriveTrain, PIDOutput, Sendable {

    private double lastXInput;
    private double lastYInput;

    private AHRS gyro;
    private PIDController headingPID;
    private DriveMode currentDriveMode;
    private double pidRotation;
    private DockMode autoDoctorState = DockMode.PLAYER;

    private PIDController autoDockXController;
    private SettablePIDSource autoDockXSource;

    private Timer lostLineTimer;
    private boolean hadLine = false;

    public DriveTrain(SpeedController frontLeft, SpeedController rearLeft, SpeedController frontRight, SpeedController rearRight, AHRS gyro, PIDConfiguration headingPIDConfig, PIDConfiguration autodockXPIDConfig){
        super(frontLeft, rearLeft, frontRight, rearRight);
        this.gyro = gyro;
        this.currentDriveMode = DriveMode.ROBOT;
        headingPID = new PIDController(headingPIDConfig.getKp(), headingPIDConfig.getKi(), headingPIDConfig.getKd(), gyro, this); //values from last year's robor
        headingPID.setInputRange(headingPIDConfig.getInMin(), headingPIDConfig.getInMax());
        headingPID.setOutputRange(headingPIDConfig.getOutMin(), headingPIDConfig.getOutMax());
        headingPID.setContinuous();
        headingPID.setAbsoluteTolerance(headingPIDConfig.getTolerance());

        autoDockXSource = new SettablePIDSource(0);
        autoDockXController = new PIDController(autodockXPIDConfig.getKp(), autodockXPIDConfig.getKi(), autodockXPIDConfig.getKd(), autoDockXSource, o -> {}); //TODO: PID values

        gyro.setName("gyro");
        gyro.setSubsystem("drive");
        LiveWindow.add(gyro);

        headingPID.setName("Heading PID");
        LiveWindow.add(headingPID);
        autoDockXController.setName("Autodock PID");
        LiveWindow.add(autoDockXController);

        LiveWindow.add(this);

        lostLineTimer = new Timer();

    }

    public void handleInputs(IAxis xAxis, IAxis yAxis, IAxis rotationAxis, IButton fieldDriveModeButton, IButton headingHoldButton, IButton resetGyroButton, ITargetProvider targetProvider, IButton autoDockButton) {
        //System.out.println(autoDoctorState);
        if (autoDoctorState == DockMode.PLAYER) {
            if (getDriveMode() == DriveTrain.DriveMode.ROBOT) {
                driveRobotOriented(xAxis.getValue(), yAxis.getValue(), rotationAxis.getValue());
            } else {
                driveFieldOriented(xAxis.getValue(), yAxis.getValue(), rotationAxis.getValue());
            }
            if (fieldDriveModeButton.getValue()) {
                toggleFieldDriveMode();
            }
            setHeadingHoldEnabled(headingHoldButton.getValue());
            if (resetGyroButton.getValue()) {
                resetGyro();
            }
            if (autoDockButton.getValue()) {
                System.out.println("ENTER AUTODOCK");
                autoDoctorState = DockMode.AUTODOCK;
            }
        } else if (autoDoctorState == DockMode.AUTODOCK) {
            autoDock(targetProvider.getLineSensor(), targetProvider.getTarget());

            if (autoDockButton.getValue()) {
                autoDoctorState = DockMode.PLAYER;
                System.out.println("ENTER PLAYER 1");
            }
            if(hadLine && !targetProvider.getLineSensor().doesTargetExist()){
                lostLineTimer.start();
            }
            if ((!targetProvider.getLineSensor().doesTargetExist() && (!hadLine && lostLineTimer.get() > 1.0)) && !targetProvider.getTarget().doesTargetExist()) {
                autoDoctorState = DockMode.PLAYER;
                System.out.println("ENTER PLAYER 2");
                lostLineTimer.stop();
            }
            //TODO: get the number
            if (targetProvider.getTarget().doesTargetExist() && targetProvider.getTarget().getDistance() <= 20) {
                autoDoctorState = DockMode.PLAYER;
                System.out.println("ENTER PLAYER 3");
            }
            hadLine = targetProvider.getLineSensor().doesTargetExist();
        }
    }

    public void autoDock(ILineSensor line, ITarget target){
        if(line.doesTargetExist()) {
            autoDockXSource.setValue(line.getCenterX());
            autoDockXController.setEnabled(true);
            setHeadingHold(gyro.getYaw() + line.getLineAngle());
            setHeadingHoldEnabled(true);
            driveRobotOriented(autoDockXController.get(), Math.abs(line.getCenterX()) > 20 || Math.abs(line.getLineAngle()) > 5 ? 0 : -0.3, 0);
        }else{
            if(target.doesTargetExist()) {

                setHeadingHold(gyro.getYaw());
                autoDockXSource.setValue(target.getCenterX());
                autoDockXController.setEnabled(true);
                setHeadingHoldEnabled(true);
                System.out.println(autoDockXController.get());
                driveRobotOriented(autoDockXController.get(), 0, 0);
            }else if((!hadLine && lostLineTimer.get() <= 1.0)){
                driveRobotOriented(autoDockXController.get(), 0, 0);
            }else{
                autoDockXController.setEnabled(false);
                setHeadingHoldEnabled(false);
            }
        }
    }

    public void driveFieldOriented(double x, double y, double r) {
        lastXInput = x;
        lastYInput = y;
        super.driveCartesian(y, x, r + (headingPID.isEnabled() ? pidRotation : 0), gyro.getAngle());
    }


    @Override
    public void drivePolar(double magnitude, double angle, double r) {
        lastXInput = Math.cos(angle) * magnitude;
        lastYInput = Math.sin(angle) * magnitude;
        super.drivePolar(magnitude, angle, r + (headingPID.isEnabled() ? pidRotation : 0));
    }


    public void driveRobotOriented(double x, double y, double r) {
        lastXInput = x;
        lastYInput = y;
        super.driveCartesian(y, x, r + (headingPID.isEnabled() ? pidRotation : 0));
    }

    @Override
    public void setHeadingHoldEnabled(boolean value){
        headingPID.setEnabled(value);
    }

    @Override
    public boolean getHeadingHoldEnabled() {
        return headingPID.isEnabled();
    }

    @Override
    public void setHeadingHold(double heading) {
        headingPID.setSetpoint(heading);
        headingPID.enable();
    }

    @Override
    public void setHeadingTolerance(double tolerance) {
        headingPID.setAbsoluteTolerance(tolerance);
    }

    @Override
    public boolean isAtHeading() {
        return headingPID.onTarget();
    }

    @Override
    public void drive(double speed, double heading) {
        driveRobotOriented(speed * Math.cos(heading), speed * Math.sin(heading), 0);
    }

    @Override
    public void stop() {
        driveRobotOriented(0, 0, 0); //TODO: break mode
    }

    @Override
    public void pidWrite(double output) {
        pidRotation = output;
    }

    public void toggleFieldDriveMode(){
        currentDriveMode = (currentDriveMode == DriveMode.ROBOT)? DriveMode.FIELD : DriveMode.ROBOT;
    }

    public DriveMode getDriveMode(){
        return currentDriveMode;
    }

    public void resetGyro(){
        gyro.zeroYaw();
    }

    public enum DriveMode {
        ROBOT,
        FIELD
    }

    public enum DockMode {
        AUTODOCK,
        PLAYER
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);

        builder.addDoubleProperty("AutoDockXPIDOut", () -> autoDockXController.get(), (v) -> {});

    }
}

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
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.Vector2d;
import frc.team871.SettablePIDSource;
import frc.team871.auto.DockingWaypointProvider;
import frc.team871.auto.ILineSensor;
import frc.team871.auto.ITarget;
import frc.team871.auto.ITargetProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class DriveTrain extends MecanumDrive implements IDriveTrain, PIDOutput {

    private double lastXInput;
    private double lastYInput;

    private AHRS gyro;
    private PIDController headingPID;
    private DriveMode currentDriveMode;
    private double pidRotation;
    private DockMode autoDoctorState;

    private PIDController autoDockXController;
    private SettablePIDSource autoDockXSource;

    public DriveTrain(SpeedController frontLeft, SpeedController rearLeft, SpeedController frontRight, SpeedController rearRight, AHRS gyro){
        super(frontLeft, rearLeft, frontRight, rearRight);
        this.gyro = gyro;
        this.currentDriveMode = DriveMode.ROBOT;
        headingPID = new PIDController(0.03, 0, 0.03, gyro, this); //values from last year's robor
        headingPID.setInputRange(-180, 180);
        headingPID.setOutputRange(-0.5, 0.5);
        headingPID.setContinuous();
        headingPID.setAbsoluteTolerance(5);

        autoDockXSource = new SettablePIDSource(0);
        autoDockXController = new PIDController(0, 0, 0, autoDockXSource, o -> {}); //TODO: PID values

        gyro.setName("gyro");
        gyro.setSubsystem("drive");
        LiveWindow.add(gyro);

    }

    public void handleInputs(IAxis xAxis, IAxis yAxis, IAxis rotationAxis, IButton fieldDriveModeButton, IButton headingHoldButton, IButton resetGyroButton, ITargetProvider targetProvider, IButton autoDockButton) {
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
                autoDoctorState = DockMode.AUTODOCK;
            }
        } else if (autoDoctorState == DockMode.AUTODOCK) {
            autoDock(targetProvider.getLineSensor(), targetProvider.getTarget());

            if (autoDockButton.getValue()) {
                autoDoctorState = DockMode.PLAYER;
            }
            if (!targetProvider.getLineSensor().doesTargetExist() && !targetProvider.getTarget().doesTargetExist()) {
                autoDoctorState = DockMode.PLAYER;
            }
            //TODO: get the number
            if (targetProvider.getTarget().doesTargetExist() && targetProvider.getTarget().getDistance() <= 20) {
                autoDoctorState = DockMode.PLAYER;
            }
        }
    }

    public void autoDock(ILineSensor line, ITarget target){
        if(line.doesTargetExist()) {
            autoDockXSource.setValue(line.getCenterX());
            autoDockXController.setEnabled(true);
            setHeadingHold(gyro.getYaw() + line.getLineAngle());
            setHeadingHoldEnabled(true);
            driveRobotOriented(autoDockXController.get(), .3, 0);
        }else{
            if(target.doesTargetExist()) {
                setHeadingHold(gyro.getYaw());
                autoDockXSource.setValue(target.getCenterX());
                autoDockXController.setEnabled(true);
                setHeadingHoldEnabled(true);
                driveRobotOriented(autoDockXController.get(), .3, 0);
            }else{
                autoDockXController.setEnabled(false);
                setHeadingHoldEnabled(false);
            }
        }
    }

    public void driveFieldOriented(double x, double y, double r) {
        lastXInput = x;
        lastYInput = y;
        driveCartesian(y, x, r + (headingPID.isEnabled() ? pidRotation : 0), gyro.getAngle());
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
        driveCartesian(y, x, r + (headingPID.isEnabled() ? pidRotation : 0));
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
}

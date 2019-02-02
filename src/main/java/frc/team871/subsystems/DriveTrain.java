package frc.team871.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.team871.hid.IButton;
import com.team871.subsystem.IDriveTrain;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class DriveTrain extends MecanumDrive implements IDriveTrain, PIDOutput {

    private AHRS gyro;
    private PIDController headingPID;
    private DriveMode currentDriveMode;
    private double pidRotation;


    public enum DriveMode {
        ROBOT,
        FIELD
    }

    public DriveTrain(SpeedController frontLeft, SpeedController rearLeft, SpeedController frontRight, SpeedController rearRight, AHRS gyro){
        super(frontLeft, rearLeft, frontRight, rearRight);
        this.gyro = gyro;
        this.currentDriveMode = DriveMode.ROBOT;
        headingPID = new PIDController(0.03, 0, 0.03, gyro, this); //values from last year's robor
        headingPID.setInputRange(-180, 180);
        headingPID.setOutputRange(-0.5, 0.5);
        headingPID.setContinuous();
        headingPID.setAbsoluteTolerance(5);
    }


    public void driveFieldOriented(double x, double y, double r) {
        driveCartesian(y, x, r + (headingPID.isEnabled() ? pidRotation : 0), gyro.getAngle());
    }


    @Override
    public void drivePolar(double magnitude, double angle, double r) {
        super.drivePolar(magnitude, angle, r + (headingPID.isEnabled() ? pidRotation : 0));
    }


    public void driveRobotOriented(double x, double y, double r) {
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
}

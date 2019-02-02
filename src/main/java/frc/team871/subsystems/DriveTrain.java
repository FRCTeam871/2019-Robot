package frc.team871.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.team871.io.sensor.IDisplacementSensor;
import com.team871.navigation.Coordinate;
import com.team871.navigation.DistanceUnit;
import com.team871.subsystem.IDriveTrain;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DriveTrain extends MecanumDrive implements IDriveTrain, PIDOutput, IDisplacementSensor {
    private volatile Coordinate displacement;
    private Coordinate prevDisplacement;

    private final long integrationRate = 20; //in milliseconds
    private double lastXInput;
    private double lastYInput;

    private boolean enableIntegration = true;
    private Timer positionIntegrator;

    private AHRS gyro;
    private PIDController headingPID;
    private DriveMode currentDriveMode;
    private double pidRotation;

    private final List<VelocityHolder> velDataPoints = new ArrayList<>(); //TODO: consider using a map

    //TODO: copied from the previous year, optimize
    private class IntegrationTask extends TimerTask {
        private long tPrevious = 0;

        /**
         * Sets new x and y motor values to update the current location
         */
        private void updatePosition() {

//            SmartDashboard.putNumber("updStart", new Random().nextDouble());
//            SmartDashboard.putBoolean("enableIntegration", enableIntegration);
//            SmartDashboard.putNumber("tPrevious", tPrevious);
//            SmartDashboard.putNumber("prevX", lastXInput);
//            SmartDashboard.putNumber("prevY", lastYInput);
            // On the first iteration do nothing
            if(tPrevious == 0 || !enableIntegration) {
                tPrevious = System.currentTimeMillis();
                return;
            }

            if(displacement == null) {
                displacement = new Coordinate(0, 0);
            }

            final long curTime = System.currentTimeMillis();
            final double tDiff = (curTime - tPrevious) / 1000.0;
            final double xVel = calculateVelocity(lastXInput);
            final double yVel = calculateVelocity(lastYInput);

//            SmartDashboard.putNumber("xVel", xVel);
//            SmartDashboard.putNumber("yVel", yVel);

            final double xDistance = tDiff * xVel;
            final double yDistance = tDiff * yVel;

            Vector2d vec = new Vector2d(xDistance, yDistance);
            vec.rotate(gyro.getAngle());

//            SmartDashboard.putNumber("vecX", vec.x);
//            SmartDashboard.putNumber("vecY", vec.y);

            // This isn't correct, it assumes that each component can be the same. In reality,
            // It's the resultant velocity that matters...
            displacement.setX(displacement.getX() + vec.x);
            displacement.setY(displacement.getY() + vec.y);

            tPrevious = curTime;
//            SmartDashboard.putNumber("upd", tDiff);
        }

        /**
         * Interpolates the velocity in in/sec of the robot based off of experimental data.
         * This data shows the relationship between motor speeds and velocities of the robot.
         * @param speed The motor speed
         * @return The velocity in in/sec
         */
        private double calculateVelocity(double speed) {

            boolean reverse = speed < 0;
            speed = Math.abs(speed);

//            SmartDashboard.putBoolean("Reverse", reverse);

            VelocityHolder holderUpper = null;
            VelocityHolder holderLower = null;

            for(int i = 0; i < velDataPoints.size(); i++) {
                VelocityHolder vel = velDataPoints.get(i);
                if(speed <= vel.inputSpeed && holderUpper == null) {
                    holderUpper = vel;
                    if(speed == vel.inputSpeed) {
                        holderLower = holderUpper;
                    }else {
                        holderLower = velDataPoints.get(Math.max(0, Math.min(i - 1, velDataPoints.size()-1)));
                    }
                    break;
                }
            }

            if(holderLower == null || holderUpper == null) {
                throw new RuntimeException("YOU ARE BORKED");
                //Bad times, maybe should throw an exception
            }

            if(holderLower == holderUpper) {
                return holderLower.outputSpeed * (reverse ? -1 : 1);
            }

            final double relative = ((speed - holderLower.inputSpeed) / (holderUpper.inputSpeed - holderLower.inputSpeed));
            final double scale = holderUpper.outputSpeed - holderLower.outputSpeed;
            final double transformation = holderLower.outputSpeed;

            return ((relative * scale) + transformation) * (reverse ? -1 : 1);
        }

        @Override
        public void run() {
            updatePosition();
        }
    }

    private class VelocityHolder {
        public double inputSpeed;
        public double outputSpeed;

        VelocityHolder(double input, double output) {
            this.inputSpeed = input;
            this.outputSpeed = output;
        }
    }


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

        this.prevDisplacement = new Coordinate (0,0);

        //TODO these values will change for this year's row boat
        velDataPoints.add(new VelocityHolder(0,  0));
        velDataPoints.add(new VelocityHolder(.1, 0));
        velDataPoints.add(new VelocityHolder(.2, 15.8));
        velDataPoints.add(new VelocityHolder(.3, 28.2));
        velDataPoints.add(new VelocityHolder(.4, 40.95));
        velDataPoints.add(new VelocityHolder(.5, 53.95));
        velDataPoints.add(new VelocityHolder(.6, 64.7));
        velDataPoints.add(new VelocityHolder(.7, 72.33));
        velDataPoints.add(new VelocityHolder(.8, 82.34));
        velDataPoints.add(new VelocityHolder(.9, 92.167));
        velDataPoints.add(new VelocityHolder(1,  100));

        positionIntegrator = new Timer();
        positionIntegrator.schedule(new IntegrationTask(), 0, integrationRate);
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

    @Override
    public Coordinate getDisplacement(DistanceUnit distanceUnit) {
        return displacement;
    }

    @Override
    public Coordinate getVelocity(DistanceUnit distanceUnit) {
        return new Coordinate(displacement.getX() - prevDisplacement.getX(), displacement.getY() - prevDisplacement.getY());
    }

    @Override
    public void resetSensor() {
        enableIntegration = false;
        displacement = new Coordinate(0,0);
        enableIntegration = true;
    }
}

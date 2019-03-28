package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.team871.hid.IAxis;
import com.team871.io.actuator.CombinedSpeedController;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import frc.team871.auto.ITargetProvider;
import frc.team871.auto.detection.coprocessor.CoProcessorNetworkTargetProvider;
import frc.team871.config.network.DeepSpaceNetConfig;

import java.util.Arrays;
import java.util.HashMap;

public enum RowBoatConfig implements IRowBoatConfig {
    DEFAULT;

    DeepSpaceNetConfig netConfig;

    SpeedController frontLeftMotor;
    SpeedController rearLeftMotor;
    SpeedController frontRightMotor;
    SpeedController rearRightMotor;
    PIDConfiguration headingPIDConfig;
    PIDConfiguration autoDockXPIDConfig;
    SpeedController lowerArmMotor;
    SpeedController upperArmMotor;
    SpeedController wristMotor;
    SpeedController vacuumMotor;
    AHRS gyro;
    DigitalInput grabSensor;
    UsbCamera lineCam;

    IAxis wristPot;
    PIDConfiguration wristPIDConfig;
    IAxis lowerPot;
    PIDConfiguration lowerPIDConfig;
    IAxis upperPot;
    PIDConfiguration upperPIDConfig;

    Solenoid innerValve;
    Solenoid outerValve;

    ITargetProvider targetProvider;

    HashMap<DoubleSolenoid, DigitalInput> frontClimb;
    HashMap<DoubleSolenoid, DigitalInput> backClimb;

    DigitalInput frontClimbSense;
    DigitalInput backClimbSense;

    RowBoatConfig(){
        netConfig = new DeepSpaceNetConfig(false, NetworkTableInstance.getDefault());

        this.frontLeftMotor = new WPI_VictorSPX(3);
        this.rearLeftMotor = new WPI_VictorSPX(2);
        this.frontRightMotor = new WPI_VictorSPX(0);
        this.rearRightMotor = new WPI_VictorSPX(1);

        headingPIDConfig = new PIDConfiguration(0.01, 0, 0.03, -180, 180, -0.5, 0.5, 5);
        autoDockXPIDConfig = new PIDConfiguration(-0.01, 0, 0.001, 0, 0, 0, 0, 0);

        this.lowerArmMotor = new WPI_TalonSRX(7);
        WPI_TalonSRX t = new WPI_TalonSRX(5);
        this.upperArmMotor = new CombinedSpeedController(Arrays.asList(t, new WPI_TalonSRX(6)));
        this.wristMotor = new WPI_TalonSRX(4);
//        this.wristMotor.setInverted(true);
        this.vacuumMotor = new WPI_TalonSRX(8);

        this.gyro = new AHRS(I2C.Port.kMXP);

        //TODO: check values

        // an angle of 0 on all segments is when the entire arm is pointed directly forward, parallel to the base of the robot
        // positive angles are clockwise

        double wristMaxSpeed = 1.0;
        //wristPot = new TalonMappableAxis((TalonSRX)wristMotor, 301, 377, 90, -90);
        wristPot = new TalonSmartAxis((WPI_TalonSRX)wristMotor, 2.432, -832);
        wristPIDConfig = new PIDConfiguration(-0.05, 0, 0.02, -120, 120, -wristMaxSpeed, wristMaxSpeed, 4);

        double lowerMaxSpeed = 1.0;
        //lowerPot = new TalonMappableAxis((TalonSRX)lowerArmMotor, 342, 568, 0, -90);
        lowerPot = new TalonSmartAxis((WPI_TalonSRX)lowerArmMotor,  0.411, -221 + 8);
        lowerPIDConfig = new PIDConfiguration(0.04, 0, 0.04, -110, 80, -lowerMaxSpeed, lowerMaxSpeed, 4);

        double upperMaxSpeed = 0.5;
        //upperPot = new TalonMappableAxis(t, 322, 433, -90, 0);
        upperPot = new TalonSmartAxis((WPI_TalonSRX)t, 0.85, -347 * 0.85 + 5);
        upperPIDConfig = new PIDConfiguration(-0.025, 0, 0.03, -124, 80, -upperMaxSpeed, upperMaxSpeed, 4);

        // 347 453

        innerValve = new Solenoid(0);
        outerValve = new Solenoid(1);


//        this.lineCam = CameraServer.getInstance().startAutomaticCapture(0);
//        UsbCamera targetCam = CameraServer.getInstance().startAutomaticCapture(1);
//
//        targetCam.setExposureManual(20);
//
//        final int camWidth = 320/2;
//        final int camHeight = 240/2;

//        this.lineCam.setResolution(camWidth, camHeight);
//        this.lineCam.setPixelFormat(VideoMode.PixelFormat.kMJPEG);
//        targetCam.setResolution(camWidth, camHeight);
//        targetCam.setPixelFormat(VideoMode.PixelFormat.kMJPEG);
//        //this.lineCam.setExposureAuto();

        //All that commented out code above ^ replaced by this one line...
        targetProvider = new CoProcessorNetworkTargetProvider(getNetworkConfiguration().gripTable);


        frontClimb = new HashMap<>();
        frontClimb.put(new DoubleSolenoid(2, 3), new DigitalInput(0));
        backClimb = new HashMap<>();
        backClimb.put(new DoubleSolenoid(4, 5), new DigitalInput(1));

        frontClimbSense = new DigitalInput(0);
        backClimbSense = new DigitalInput(1);
    }

    @Override
    public DeepSpaceNetConfig getNetworkConfiguration() {
        return netConfig;
    }

    @Override
    public SpeedController getFrontLeftMotor() {
        return frontLeftMotor;
    }

    @Override
    public SpeedController getRearLeftMotor() {
        return rearLeftMotor;
    }

    @Override
    public SpeedController getFrontRightMotor() {
        return frontRightMotor;
    }

    @Override
    public SpeedController getRearRightMotor() {
        return rearRightMotor;
    }


    @Override
    public PIDConfiguration getHeadingPIDConfig() {
        return headingPIDConfig;
    }

    @Override
    public PIDConfiguration getAutoDockXPIDConfig() {
        return autoDockXPIDConfig;
    }

    @Override
    public SpeedController getLowerArmMotor() {
        return lowerArmMotor;
    }

    @Override
    public SpeedController getUpperArmMotor() {
        return upperArmMotor;
    }

    @Override
    public SpeedController getWristMotor() {
        return wristMotor;
    }

    @Override
    public SpeedController getVacuumMotor() {
        return vacuumMotor;
    }

    @Override
    public AHRS getGyro() {
        return gyro;
    }

    @Override
    public IAxis getLowerArmPot() {
        return lowerPot;
    }

    @Override
    public PIDConfiguration getLowerArmPIDConfig() {
        return lowerPIDConfig;
    }

    @Override
    public IAxis getUpperArmPot() {
        return upperPot;
    }

    @Override
    public PIDConfiguration getUpperArmPIDConfig() {
        return upperPIDConfig;
    }

    @Override
    public IAxis getWristPotAxis() {
        return wristPot;
    }

    @Override
    public PIDConfiguration getWristPIDConfig() {
        return wristPIDConfig;
    }

    @Override
    public DigitalInput getGrabSensor() {
        return grabSensor;
    }

    @Override
    public Solenoid getVacuumInnerValve() {
        return innerValve;
    }

    @Override
    public Solenoid getVacuumOuterValve() {
        return outerValve;
    }

    @Override
    public HashMap<DoubleSolenoid, DigitalInput> getFrontClimbPistons() {
        return frontClimb;
    }

    @Override
    public HashMap<DoubleSolenoid, DigitalInput> getBackClimbPistons() {
        return backClimb;
    }

    @Override
    public DigitalInput getFrontClimbSensor() {
        return frontClimbSense;
    }

    @Override
    public DigitalInput getBackClimbSensor() {
        return backClimbSense;
    }

    @Override
    public ITargetProvider getTargetProvider() {
        return targetProvider;
    }

    @Override
    public UsbCamera getLineCam() {
        return lineCam;
    }
}

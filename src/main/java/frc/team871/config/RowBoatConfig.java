package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.team871.hid.IAxis;
import com.team871.io.actuator.CombinedSpeedController;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import frc.team871.config.network.DeepSpaceNetConfig;

import java.util.Arrays;

public enum RowBoatConfig implements IRowBoatConfig {
    DEFAULT;

    DeepSpaceNetConfig deepSpaceNetConfig;

    SpeedController frontLeftMotor;
    SpeedController rearLeftMotor;
    SpeedController frontRightMotor;
    SpeedController rearRightMotor;
    SpeedController lowerArmMotor;
    SpeedController upperArmMotor;
    SpeedController wristMotor;
    SpeedController vacuumMotor;
    AHRS gyro;
    DigitalInput grabSensor;

    IAxis wristPot;
    PIDConfiguration wristPIDConfig;
    IAxis lowerPot;
    PIDConfiguration lowerPIDConfig;
    IAxis upperPot;
    PIDConfiguration upperPIDConfig;

    Solenoid innerValve;
    Solenoid outerValve;

    RowBoatConfig(){
        this.deepSpaceNetConfig = new DeepSpaceNetConfig(false, NetworkTableInstance.getDefault(), "0.0");

        this.frontLeftMotor = new WPI_VictorSPX(0);
        this.rearLeftMotor = new WPI_VictorSPX(1);
        this.frontRightMotor = new WPI_VictorSPX(2);
        this.rearRightMotor = new WPI_VictorSPX(3);

        this.lowerArmMotor = new WPI_TalonSRX(7);
        WPI_TalonSRX t = new WPI_TalonSRX(5);
        this.upperArmMotor = new CombinedSpeedController(Arrays.asList(t, new WPI_TalonSRX(6)));
        this.wristMotor = new WPI_TalonSRX(4);
//        this.wristMotor.setInverted(true);
        this.vacuumMotor = new WPI_TalonSRX(8);

        this.gyro = new AHRS(SerialPort.Port.kMXP);

        //TODO: check values

        // an angle of 0 on all segments is when the entire arm is pointed directly forward, parallel to the base of the robot
        // positive angles are clockwise

        double wristMaxSpeed = 1.0;
        wristPot = new TalonAnalogAxis((TalonSRX)wristMotor, 301, 377);
        wristPot.setMapping(90, -90);
        wristPIDConfig = new PIDConfiguration(-0.05, 0, 0.02, -90, 90, -wristMaxSpeed, wristMaxSpeed, 4);

        double lowerMaxSpeed = 1.0;
        lowerPot = new TalonAnalogAxis((TalonSRX)lowerArmMotor, 342, 568);
        lowerPot.setMapping(0, -90);
        lowerPIDConfig = new PIDConfiguration(0.04, 0, 0.04, -90, 90, -lowerMaxSpeed, lowerMaxSpeed, 4);

        double upperMaxSpeed = 0.5;
        upperPot = new TalonAnalogAxis((TalonSRX)t, 322, 433);
        upperPot.setMapping(-90, 0);
        upperPIDConfig = new PIDConfiguration(-0.05, 0, 0.02, -90, 90, -upperMaxSpeed, upperMaxSpeed, 4);

        innerValve = new Solenoid(0);
        outerValve = new Solenoid(1);
    }

    @Override
    public DeepSpaceNetConfig getNetConfig() {
        return this.deepSpaceNetConfig;
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
}

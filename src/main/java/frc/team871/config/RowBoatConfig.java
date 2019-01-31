package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.team871.io.actuator.CombinedSpeedController;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import java.util.Arrays;
import java.util.Collection;

public enum RowBoatConfig implements IRowBoatConfig{
    DEFAULT;

    SpeedController frontLeftMotor;
    SpeedController rearLeftMotor;
    SpeedController frontRightMotor;
    SpeedController rearRightMotor;
    SpeedController lowerArmMotor;
    SpeedController upperArmMotor;
    SpeedController wristMotor;
    SpeedController vacuumMotor;
    AHRS gyro;
    AnalogInput lowerArmPot;
    AnalogInput upperArmPot;
    AnalogInput wristPot;
    DigitalInput grabSensor;

    RowBoatConfig(){
         this.frontLeftMotor = new WPI_VictorSPX(0);
         this.rearLeftMotor = new WPI_VictorSPX(1);
         this.frontRightMotor = new WPI_VictorSPX(2);
         this.rearRightMotor = new WPI_VictorSPX(3);
         //TODO find motor type and port numbers
         this.lowerArmMotor = new WPI_TalonSRX(4);
         this.upperArmMotor = new CombinedSpeedController(Arrays.asList(new WPI_TalonSRX(5), new WPI_TalonSRX(6)));
         this.wristMotor = new WPI_TalonSRX(7);
         this.vacuumMotor = new WPI_TalonSRX(8);
         this.gyro = new AHRS(SerialPort.Port.kMXP);
//         this.lowerArmPot = new AnalogInput(-1);
//         this.upperArmPot = new AnalogInput(-1);
//         this.wristPot = new AnalogInput(-1);
//         this.grabSensor = new DigitalInput(-1);
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
    public AnalogInput getLowerArmPot() {
        return lowerArmPot;
    }

    @Override
    public AnalogInput getUpperArmPot() {
        return upperArmPot;
    }

    @Override
    public AnalogInput getWristPot() {
        return wristPot;
    }

    @Override
    public DigitalInput getGrabSensor() {
        return grabSensor;
    }
}

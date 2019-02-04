package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.team871.io.actuator.CombinedSpeedController;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import java.util.Arrays;

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
    AnalogInput lowerArmPotAxis;
    AnalogInput upperArmPotAxis;
    AnalogInput wristPotAxis;
    AnalogPotentiometer lowerArmPot;
    AnalogPotentiometer upperArmPot;
    AnalogPotentiometer wristPot;
    DigitalInput grabSensor;

    RowBoatConfig(){
         this.frontLeftMotor = new WPI_VictorSPX(0);
         this.rearLeftMotor = new WPI_VictorSPX(1);
         this.frontRightMotor = new WPI_VictorSPX(2);
         this.rearRightMotor = new WPI_VictorSPX(3);


         this.wristMotor = new WPI_TalonSRX(4);
         this.upperArmMotor = new CombinedSpeedController(Arrays.asList(new WPI_TalonSRX(5), new WPI_TalonSRX(6)));
         this.lowerArmMotor = new WPI_TalonSRX(7);
         this.vacuumMotor = new WPI_TalonSRX(8);

         this.gyro = new AHRS(SerialPort.Port.kMXP);

         //TODO find sensor channels
         this.lowerArmPotAxis = new AnalogInput(-1);
         this.lowerArmPot = new AnalogPotentiometer(lowerArmPotAxis, 1);
         this.upperArmPotAxis = new AnalogInput(-1);
         this.lowerArmPot = new AnalogPotentiometer(upperArmPotAxis, 1);
         this.wristPotAxis = new AnalogInput(-1);
         this.lowerArmPot = new AnalogPotentiometer(wristPotAxis, 1);
         this.grabSensor = new DigitalInput(-1);
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
    public AnalogInput getLowerArmAxisSensor() {
        return lowerArmPotAxis;
    }

    @Override
    public AnalogInput getUpperArmAxisSensor() {
        return upperArmPotAxis;
    }

    @Override
    public AnalogInput getWristAxisSensor() {
        return wristPotAxis;
    }

    @Override
    public AnalogPotentiometer getLowerArmPot() {
        return lowerArmPot;
    }

    @Override
    public AnalogPotentiometer getUpperArmPot() {
        return upperArmPot;
    }

    @Override
    public AnalogPotentiometer getWristPotAxis() {
        return wristPot;
    }

    @Override
    public DigitalInput getGrabSensor() {
        return grabSensor;
    }
}

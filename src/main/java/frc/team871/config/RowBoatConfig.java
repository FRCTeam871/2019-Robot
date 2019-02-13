package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.team871.hid.IAxis;
import com.team871.io.actuator.CombinedSpeedController;
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
    DigitalInput grabSensor;

    IAxis wristPot;
    IAxis lowerPot;
    IAxis upperPot;

    RowBoatConfig(){
        this.frontLeftMotor = new WPI_VictorSPX(0);
        this.rearLeftMotor = new WPI_VictorSPX(1);
        this.frontRightMotor = new WPI_VictorSPX(2);
        this.rearRightMotor = new WPI_VictorSPX(3);

        this.lowerArmMotor = new WPI_TalonSRX(7);
        this.upperArmMotor = new CombinedSpeedController(Arrays.asList(new WPI_TalonSRX(5), new WPI_TalonSRX(6)));
        this.wristMotor = new WPI_TalonSRX(4);
        this.vacuumMotor = new WPI_TalonSRX(8);

        this.gyro = new AHRS(SerialPort.Port.kMXP);

        //TODO: check values

        wristPot = new TalonAnalogAxis((TalonSRX)wristMotor, 301, 377);
        wristPot.setMapping(90, -90);

        lowerPot = new TalonAnalogAxis((TalonSRX)wristMotor, 779, 554);
        lowerPot.setMapping(0, -90);

        upperPot = new TalonAnalogAxis((TalonSRX)wristMotor, 301, 377);
        upperPot.setMapping(90, -90);


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
        return null;
    }

    @Override
    public IAxis getUpperArmPot() {
        return null;
    }

    @Override
    public IAxis getWristPotAxis() {
        return null;
    }

    @Override
    public DigitalInput getGrabSensor() {
        return grabSensor;
    }
}

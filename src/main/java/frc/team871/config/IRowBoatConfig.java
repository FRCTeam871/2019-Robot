package frc.team871.config;

import com.kauailabs.navx.frc.AHRS;
import com.team871.hid.IAxis;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;

public interface IRowBoatConfig {

    SpeedController getFrontLeftMotor();

    SpeedController getRearLeftMotor();

    SpeedController getFrontRightMotor();

    SpeedController getRearRightMotor();

    SpeedController getLowerArmMotor();

    SpeedController getUpperArmMotor();

    SpeedController getWristMotor();

    SpeedController getVacuumMotor();

    AHRS getGyro();

    IAxis getLowerArmPot();

    PIDConfiguration getLowerArmPIDConfig();

    IAxis getUpperArmPot();

    PIDConfiguration getUpperArmPIDConfig();

    IAxis getWristPotAxis();

    PIDConfiguration getWristPIDConfig();

    DigitalInput getGrabSensor();
}

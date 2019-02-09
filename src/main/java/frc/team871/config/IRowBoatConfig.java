package frc.team871.config;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import frc.team871.auto.ITargetProvider;

public interface IRowBoatConfig{

    SpeedController getFrontLeftMotor();

    SpeedController getRearLeftMotor();

    SpeedController getFrontRightMotor();

    SpeedController getRearRightMotor();

    SpeedController getLowerArmMotor();

    SpeedController getUpperArmMotor();

    SpeedController getWristMotor();

    SpeedController getVacuumMotor();

    AHRS getGyro();

    AnalogInput getLowerArmAxisSensor();

    AnalogInput getUpperArmAxisSensor();

    AnalogInput getWristAxisSensor();

    AnalogPotentiometer getLowerArmPot();

    AnalogPotentiometer getUpperArmPot();

    AnalogPotentiometer getWristPotAxis();

    DigitalInput getGrabSensor();

    ITargetProvider getTargetProvider();

    UsbCamera getLineCam();
}

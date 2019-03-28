package frc.team871.config;

import com.kauailabs.navx.frc.AHRS;
import com.team871.hid.IAxis;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import frc.team871.auto.ITargetProvider;
import frc.team871.config.network.DeepSpaceNetConfig;

import java.util.HashMap;

public interface IRowBoatConfig{

    DeepSpaceNetConfig getNetworkConfiguration();

    SpeedController getFrontLeftMotor();

    SpeedController getRearLeftMotor();

    SpeedController getFrontRightMotor();

    SpeedController getRearRightMotor();

    PIDConfiguration getHeadingPIDConfig();

    PIDConfiguration getAutoDockXPIDConfig();

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

    ITargetProvider getTargetProvider();

    UsbCamera getLineCam();

    Solenoid getVacuumInnerValve();

    Solenoid getVacuumOuterValve();

    HashMap<DoubleSolenoid, DigitalInput> getFrontClimbPistons();

    HashMap<DoubleSolenoid, DigitalInput> getBackClimbPistons();

    DigitalInput getFrontClimbSensor();

    DigitalInput getBackClimbSensor();
}

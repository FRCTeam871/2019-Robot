/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team871;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.team871.hid.GenericJoystick;
import com.team871.hid.joystick.XBoxAxes;
import com.team871.hid.joystick.XBoxButtons;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

import java.util.Arrays;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private MecanumDrive driveTrain;
    private GenericJoystick<XBoxButtons, XBoxAxes> controller;

    /**
      * This function is run when the robot is first started up and should be used
      * for any initialization code.
      */
    @Override
    public void robotInit() {

        SpeedController fl = new WPI_VictorSPX(2);
        SpeedController rl = new WPI_VictorSPX(3);
        SpeedController fr = new WPI_VictorSPX(4);
        SpeedController rr = new WPI_VictorSPX(5);

        driveTrain = new MecanumDrive(fl, rl, fr, rr);

        controller = new GenericJoystick<>(0, Arrays.asList(XBoxButtons.values()), Arrays.asList(XBoxAxes.values()));
        Arrays.asList(XBoxAxes.values()).stream().forEach(a -> controller.getAxis(a).setDeadband(0.1));

    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {
        driveTrain.driveCartesian(controller.getAxis(XBoxAxes.LEFTX).getValue(), controller.getAxis(XBoxAxes.LEFTY).getValue(), controller.getAxis(XBoxAxes.RIGHTX).getValue());
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

}

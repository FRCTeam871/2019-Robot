package frc.team871.subsystems;

import com.team871.hid.IAxis;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team871.config.PIDConfiguration;

public class ArmSegment implements Sendable {

    private SpeedController rotateMotor;
    private IAxis pot;
    private double length;
    private PIDController pid;

    private String sendableName;
    private String systemName;

    public ArmSegment(SpeedController rotateMotor, IAxis pot, PIDConfiguration pidConfig, double length){
        this.rotateMotor = rotateMotor;
        this.pot = pot;
        this.length = length;

        pid = new PIDController(pidConfig.getKp(), pidConfig.getKi(), pidConfig.getKd(), pot, rotateMotor);
        pid.setInputRange(pidConfig.getInMin(), pidConfig.getInMax());
        pid.setOutputRange(pidConfig.getOutMin(), pidConfig.getOutMax());
        pid.setAbsoluteTolerance(pidConfig.getTolerance());

        pid.setName("PID");
        LiveWindow.add(pid);

        pot.setName("Pot");
        LiveWindow.add(pot);
    }

    public void setAngle(double angle){
        pid.setSetpoint(angle);
    }


    public double getAngle(){
        return pot.getValue(); //approx. conversion from ADC to degrees
    }

    public void enablePID(){
        pid.enable();
    }

    public void disablePID(){
        pid.disable();
    }

    public void rotate(double speed){
        rotateMotor.set(speed);
    }

    public double getLength(){
        return length;
    }

    public boolean isAtTarget(){
        return Math.abs(pid.getError()) < 5;
    }

    @Override
    public String getName() {
        return sendableName;
    }

    @Override
    public void setName(String name) {
        this.sendableName = name;
        pot.setName("Pot");
        pid.setName("PID");
    }

    @Override
    public String getSubsystem() {
        return systemName;
    }

    @Override
    public void setSubsystem(String subsystem) {
        this.systemName = subsystem;
        pot.setSubsystem(subsystem);
        pid.setSubsystem(subsystem);
    }

    @Override
    public void initSendable(SendableBuilder builder) {

    }
}

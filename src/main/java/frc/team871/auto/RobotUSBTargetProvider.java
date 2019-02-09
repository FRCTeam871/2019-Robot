package frc.team871.auto;

import edu.wpi.cscore.UsbCamera;

public class RobotUSBTargetProvider implements ITargetProvider{

    private RobotUSBLineSensor ls;

    public RobotUSBTargetProvider(UsbCamera cam){
        ls =  new RobotUSBLineSensor(cam);
    }


    @Override
    public ILineSensor getLineSensor() {
        return ls;
    }

    @Override
    public ITarget getTarget() {
        return null;
    }
}

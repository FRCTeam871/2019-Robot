package frc.team871.auto;

import edu.wpi.cscore.UsbCamera;

public class RobotUSBTargetProvider implements ITargetProvider{

    private RobotUSBLineSensor ls;

    public RobotUSBTargetProvider(UsbCamera cam, int width, int height){
        ls =  new RobotUSBLineSensor(cam, width, height);
    }


    @Override
    public ILineSensor getLineSensor() {
        return ls;
    }

    @Override
    public ITarget getTarget() {
        return new ITarget() {
            @Override
            public double getCenterX() {
                return 0;
            }

            @Override
            public double getCenterY() {
                return 0;
            }

            @Override
            public double getDistance() {
                return 0;
            }

            @Override
            public double getLengthX() {
                return 0;
            }

            @Override
            public double getLengthY() {
                return 0;
            }

            @Override
            public boolean doesTargetExist() {
                return false;
            }
        };
    }
}

package frc.team871.subsystems;

import com.team871.io.sensor.DigitalSensor;
import edu.wpi.first.wpilibj.SpeedController;

public class Vacuum {
    private SpeedController pump;
    private DigitalSensor grabSensor;
    private VacuumState state;

    public enum VacuumState{
        ENABLED,
        DISABLED
    }

    public Vacuum(SpeedController pump, DigitalSensor grabSensor){
        this.pump = pump;
        this.grabSensor = grabSensor;
    }

    private void setState(VacuumState newState){
         switch(newState){
             case ENABLED:
                 pump.set(1.);
                 break;
             case DISABLED:
                 pump.set(0.);
                 break;
         }
         state = newState;
    }

    public VacuumState getState(){
        return state;
    }

    public boolean hasGamePiece(){
        return grabSensor.get();
    }
}

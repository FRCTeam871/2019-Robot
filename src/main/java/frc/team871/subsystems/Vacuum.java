package frc.team871.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;

public class Vacuum {
    private SpeedController pump;
    private DigitalInput grabSensor;
    private VacuumState state;

    public enum VacuumState{
        ENABLED,
        DISABLED
    }

    public Vacuum(SpeedController pump, DigitalInput grabSensor){
        this.pump = pump;
        this.grabSensor = grabSensor;
    }

    public void setState(VacuumState newState){
         switch(newState){
             case ENABLED:
                 pump.set(0.5);
                 break;
             case DISABLED:
                 pump.set(0.0);
                 break;
         }
         state = newState;
    }

    public void toggleState(){
        setState((state == VacuumState.ENABLED) ? VacuumState.DISABLED : VacuumState.ENABLED);
    }

    public VacuumState getState(){
        return state;
    }

    public boolean hasGamePiece(){
        return grabSensor.get();
    }
}

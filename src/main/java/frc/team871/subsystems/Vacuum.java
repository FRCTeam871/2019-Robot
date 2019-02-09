package frc.team871.subsystems;

import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;

public class Vacuum {
    private SpeedController pump;
    private DigitalInput grabSensor;
    private VacuumState state;
    private Solenoid togglePrimarySucc;

    private enum VacuumState{
        ENABLED,
        DISABLED
    }

    public Vacuum(SpeedController pump, DigitalInput grabSensor){
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

    public void setState(IButton vacuumToggleButton){
        setState((vacuumToggleButton.getValue())? VacuumState.ENABLED: VacuumState.DISABLED);
    }

    public boolean hasGamePiece(){
        return grabSensor.get();
    }

    public void setTogglePrimarySucc(IButton vacuumToggleButton){
        togglePrimarySucc.set(vacuumToggleButton.getValue());
    }
}

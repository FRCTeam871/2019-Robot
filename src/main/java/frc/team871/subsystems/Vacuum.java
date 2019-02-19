package frc.team871.subsystems;

import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class Vacuum implements Sendable {
    private SpeedController pump;
    private DigitalInput grabSensor;
    private VacuumState state;
    private VacuumSide side;

    Solenoid valve1;
    Solenoid valve2;

    String name;
    String subsystem;

    boolean last = true;

    public enum VacuumSide {
        NONE  (false, false),
        INNER (false, true),
        OUTER (true, false),
        BOTH  (true, true);

        boolean s1;
        boolean s2;

        VacuumSide(boolean s1, boolean s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
    }

    private enum VacuumState{
        ENABLED,
        DISABLED
    }

    public Vacuum(SpeedController pump, DigitalInput grabSensor, Solenoid valve1, Solenoid valve2){
        this.pump = pump;
        this.grabSensor = grabSensor;
        this.valve1 = valve1;
        this.valve2 = valve2;
    }

    private void setState(VacuumState newState){
        if(newState != state && newState == VacuumState.ENABLED){
            last = !last;
        }
         switch(newState){
             case ENABLED:
                 pump.set(0.25);
                 if(last){
                     setSideOpen(VacuumSide.INNER);
                 }else{
                     setSideOpen(VacuumSide.OUTER);
                 }

                 break;
             case DISABLED:
                 pump.set(0.);
                 setSideOpen(VacuumSide.BOTH);
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

    public VacuumSide getSide(){
        return side;
    }

    public void setSideOpen(VacuumSide side){
        this.side = side;
        valve1.set(side.s1);
        valve2.set(side.s2);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSubsystem() {
        return subsystem;
    }

    @Override
    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        //LiveWindow.addChild(this, grabSensor);
        builder.addStringProperty("State", state::toString, (m) -> {});
        builder.addStringProperty("ValveState", side::toString, (m) -> {});
    }


}

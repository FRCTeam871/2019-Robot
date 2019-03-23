package frc.team871.subsystems;

import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class Vacuum implements Sendable {
    private final SpeedController pump;
    private VacuumState state;
    private VacuumSide side;

    private final Solenoid valve1;
    private final Solenoid valve2;

    private String name;
    private String subsystem;

    boolean stronge = false;

    public enum VacuumSide {
        NONE(false, false),
        INNER(false, true),
        OUTER(true, false),
        BOTH(true, true);

        final boolean s1;
        final boolean s2;

        VacuumSide(boolean s1, boolean s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
    }

    public enum VacuumState {
        ENABLED,
        DISABLED
    }

    public Vacuum(SpeedController pump, DigitalInput grabSensor, Solenoid valve1, Solenoid valve2) {
        this.pump = pump;
        this.valve1 = valve1;
        this.valve2 = valve2;
    }

    public void handleInputs(IButton innerButton, IButton outerButton) {
        if(innerButton.getValue()) {
//            setSideOpen(side == VacuumSide.INNER ? VacuumSide.BOTH : VacuumSide.INNER);
        } else if(outerButton.getValue()) {
            setSideOpen(side == VacuumSide.OUTER ? VacuumSide.BOTH : VacuumSide.OUTER);
        }

        if(getState() == VacuumState.ENABLED){
            pump.set(stronge ? 0.6 : 0.35);
        }

        stronge = innerButton.getRaw();

    }

    private void setState(VacuumState newState) {
        switch (newState) {
            case ENABLED:
                pump.set(stronge ? 0.6 : 0.35);
                break;
            case DISABLED:
                pump.set(0.);
//                setSideOpen(VacuumSide.BOTH);
                break;
        }
        state = newState;
    }

    public void setSideOpen(VacuumSide side) {
        this.side = side;
        valve1.set(side.s1);
        valve2.set(side.s2);

        setState(side == VacuumSide.NONE || side == VacuumSide.BOTH ? VacuumState.DISABLED : VacuumState.ENABLED);
    }

    public VacuumState getState(){
        return state;
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
        builder.addStringProperty("State", state::toString, (m) -> {
        });
        builder.addStringProperty("ValveState", side::toString, (m) -> {
        });
    }
}

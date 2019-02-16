package frc.team871.subsystems;

import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;

public class Vacuum {
    private SpeedController pump;
    private DigitalInput grabSensor;
    private VacuumState state;

    Solenoid middleSolenoid;
    Solenoid sideSolenoid;
    Timer tim;

    public enum VacuumState {
        DRAIN,
        MIDDLE,
        SIDE,
        DISABLED;
    }

    public Vacuum(SpeedController pump, DigitalInput grabSensor, Solenoid middleSolenoid, Solenoid sideSolenoid){
        this.pump = pump;
        this.grabSensor = grabSensor;
        this.state = VacuumState.DRAIN;
        this.middleSolenoid = middleSolenoid;
        this.sideSolenoid = sideSolenoid;
    }

    public boolean hasGamePiece(){
        return grabSensor.get();
    }

    private void updateState(IButton middleButton, IButton sideButton){
        switch(state){
            case DRAIN:
                pump.set(0);
                if(middleButton.getValue){
                    middleSolenoid.set(true);
                    sideSolenoid.set(false);
                    state = VacuumState.MIDDLE;
                } else if (sideButton.getValue){
                    middleSolenoid.set(false);
                    sideSolenoid.set(true);
                    state = VacuumState.SIDE;
                } else if (tim.get() > 5000){
                    tim.stop();
                    state = VacuumState.DISABLED;
                    middleSolenoid.set(false);
                    sideSolenoid.set(false);

                }

            case MIDDLE:
                pump.set.3);
                if(middleButton.getValue || sideButton.getValue){
                    middleSolenoid.set(true);
                    sideSolenoid.set(true);
                    state = VacuumState.DRAIN;
                    tim.reset();
                    tim.start();
                }
                break;

            case SIDE:
                pump.set(.3);
                if(middleButton.getValue || sideButton.getValue){
                    middleSolenoid.set(true);
                    sideSolenoid.set(true);
                    state = VacuumState.DRAIN;
                    tim.reset();
                    tim.start();
                }
                break;

            case DISABLED:
                pump.set(0);
                if(middleButton.getValue){
                    middleSolenoid.set(true);
                    sideSolenoid.set(false);
                    state = VacuumState.MIDDLE;
                } else if (sideButton.getValue){
                    middleSolenoid.set(false);
                    sideSolenoid.set(true);
                    state = VacuumState.SIDE;
                }
        }
    }
}

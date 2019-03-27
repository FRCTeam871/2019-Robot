package frc.team871.subsystems;

import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import java.util.HashMap;

public class Climb {

    private Arm arm;
    private DriveTrain driveTrain;
    private ClimbState state = ClimbState.OFF;

    HashMap<DoubleSolenoid, DigitalInput> frontPistons;
    HashMap<DoubleSolenoid, DigitalInput> backPistons;

    DigitalInput frontSense;
    DigitalInput backSense;

    long lastStateChange;

    public Climb(Arm arm, DriveTrain driveTrain, HashMap<DoubleSolenoid, DigitalInput> frontPistons, HashMap<DoubleSolenoid, DigitalInput> backPistons, DigitalInput frontSense, DigitalInput backSense){
        this.arm = arm;
        this.driveTrain = driveTrain;
        this.frontPistons = frontPistons;
        this.backPistons = backPistons;
        this.backSense = backSense;
        this.frontSense = frontSense;
        setState(ClimbState.OFF);
    }

    public void update(IButton advanceButton, IButton unadvanceButton, IButton frontButton, IButton backButton, IButton autoClimbButton){
        boolean adv = advanceButton.getValue();
        boolean unadv = unadvanceButton.getValue();
        boolean autoClimb = autoClimbButton.getValue();
        System.out.println(state + " " + adv + " " + unadv + " " + frontButton.getValue() + " " + backButton.getValue());
        switch(state){
            case OFF:
                if(adv || autoClimb){
                    setState(ClimbState.HOME);
                }
                break;
            case HOME:
                if((arm.isAtTarget() && autoClimb) || adv){
                    setState(ClimbState.FRONT_EXT);
                }
                break;
            case MANUAL:
                doManual(frontButton, backButton);
                break;
            case FRONT_EXT:

                //the delay is so the sensor doesnt see the floor instantly
                if(adv || (autoClimb && frontSense.get() && System.currentTimeMillis() - lastStateChange > 1000)){
                    setState(ClimbState.BACK_EXT);
                }else if(unadv){
                    setState(ClimbState.HOME);
                }
                break;
            case BACK_EXT:

                //the delay is so the sensor doesnt see the floor instantly
                if(adv || (autoClimb && backSense.get() && System.currentTimeMillis() - lastStateChange > 1000)){
                    setState(ClimbState.RETRACT);
                }else if(unadv){
                    setState(ClimbState.FRONT_EXT);
                }
                break;
            case RETRACT:
                if(unadv){
                    setState(ClimbState.BACK_EXT);
                }
                break;
        }
    }

    private void doManual(IButton frontButton, IButton backButton) {
        DoubleSolenoid.Value front = frontButton.getValue() ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse;
        frontPistons.forEach((p, d) -> {
            DoubleSolenoid.Value front2 = front;
            if(front2 == DoubleSolenoid.Value.kForward && (d != null && d.get())){
                front2 = DoubleSolenoid.Value.kOff;
            }

            if(p.get() != front2){
                p.set(front2);
            }
        });

        DoubleSolenoid.Value back = backButton.getValue() ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse;
        backPistons.forEach((p, d) -> {
            DoubleSolenoid.Value back2 = back;
            if(back2 == DoubleSolenoid.Value.kForward && (d != null && d.get())){
                back2 = DoubleSolenoid.Value.kOff;
            }

            if(p.get() != back2){
                p.set(back2);
            }
        });
    }

    public void setState(ClimbState state){
        switch(state){
            case OFF:

                break;
            case HOME:
                arm.goHomeSafer();
                frontPistons.forEach((p, d) -> {
                    p.set(DoubleSolenoid.Value.kReverse);
                });
                backPistons.forEach((p, d) -> {
                    p.set(DoubleSolenoid.Value.kReverse);
                });
                break;
            case MANUAL:

                break;
            case FRONT_EXT:
                frontPistons.forEach((p, d) -> {
                    p.set(DoubleSolenoid.Value.kForward);
                });
                backPistons.forEach((p, d) -> {
                    p.set(DoubleSolenoid.Value.kReverse);
                });
                break;
            case BACK_EXT:
                frontPistons.forEach((p, d) -> {
                    p.set(DoubleSolenoid.Value.kReverse);
                });
                backPistons.forEach((p, d) -> {
                    p.set(DoubleSolenoid.Value.kForward);
                });
                break;
            case RETRACT:
                frontPistons.forEach((p, d) -> {
                    p.set(DoubleSolenoid.Value.kReverse);
                });
                backPistons.forEach((p, d) -> {
                    p.set(DoubleSolenoid.Value.kReverse);
                });
                break;
        }
        lastStateChange = System.currentTimeMillis();
        this.state = state;
    }

    public ClimbState getState(){
        return state;
    }

    public enum ClimbState {
        OFF,
        HOME,
        MANUAL,
        FRONT_EXT,
        BACK_EXT,
        RETRACT

    }

}

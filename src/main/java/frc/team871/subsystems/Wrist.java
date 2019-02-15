package frc.team871.subsystems;

import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;

public class Wrist {

    private SpeedController motor;
    //TODO: orientation sensor
    private PIDController pid;
    private IAxis pot;
    private double oldAxis;
    private boolean oldButton;
    private boolean calibrating = false;
    private double calMax = Double.NEGATIVE_INFINITY;
    private double calMin = Double.POSITIVE_INFINITY;
    private double calMed = Double.NaN;

    private double negative90 = 377;
    private double positive90 = 301;

    public Wrist(SpeedController motor, IAxis pot) {
        this.motor = motor;
        this.pot   = pot;
        pid = new PIDController(0.2, 0, 0, new PIDSource() {
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {

            }

            @Override
            public PIDSourceType getPIDSourceType() {
                return PIDSourceType.kDisplacement;
            }

            @Override
            public double pidGet() {
                return getAngle();
            }
        }, motor);
        pid.setAbsoluteTolerance(3);
        pid.setSetpoint(0);
    }

    public void enablePID(){
        if(pid != null) pid.enable();
    }

    public void disablePID(){
        if(pid != null) pid.disable();
    }

    public void setOrientation(double angle){
        //System.out.println(angle);
        if(pid != null && pid.isEnabled()) pid.setSetpoint(angle);
        else {
            //System.out.println("set");
            motor.set(angle);
        }
    }

    public double getAngle(){
//        if(pid == null) return 0;
        return ((pot.getRaw() - negative90) / (positive90 - negative90) * 2 - 1) * 90;
    }

    /**
     * Handles both input values of an axis and button, making it
     * so that the most recently used input is used.
     * @param axis
     * @param button
     */
    public void handleInputs(IAxis axis, IButton button){
        if(oldAxis != axis.getValue()){
            setOrientation(axis.getValue()*45);
        }else if(oldButton != button.getValue()){
            setOrientation(button.getValue() ? 90 : 0);
        }
        oldAxis = axis.getValue();
        oldButton = button.getValue();
    }

    public void tickCalibration(IAxis pot, IButton button){
        boolean myB = button.getValue();
        //System.out.println(myB + " " + calibrating);
        if(myB){
            if(calibrating){
                System.out.println("MIN = " + calMin + " | MAX = " + calMax);
            }else{
                System.out.println("CALIBRATION MODE ACTIVATED!!!");
            }
            calibrating = !calibrating;
        }
        if(calibrating){
            double v = pot.getRaw();

            if(v > calMax){
                calMax = v;
                System.out.println("NEW MAXIMUM = " + v);
            }else if(v < calMin){
                calMin = v;
                System.out.println("NEW MINIMUM = " + v);
            }
        }
//        double v = pot.getRaw();
//        calMed = calibrating? ((v > calMax)? calMax = v : (v < calMin)? calMin = v : 0 ): 0;
    }

}

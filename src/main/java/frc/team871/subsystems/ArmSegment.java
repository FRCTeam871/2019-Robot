package frc.team871.subsystems;

import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;

public class ArmSegment {

    private  SpeedController rotateMotor;
    private IAxis pot;
    private double length;
    private PIDController pid;

    private boolean calibrating = false;
    private double calMax = Double.NEGATIVE_INFINITY;
    private double calMin = Double.POSITIVE_INFINITY;
    private double calMed = Double.NaN;

    private double negative90;
    private double zero;

    public ArmSegment(SpeedController rotateMotor, IAxis pot, double length, double n90, double z0){
        this.negative90 = n90;
        this.zero = z0;
        this.rotateMotor = rotateMotor;
        this.pot = pot;
        this.length = length;
        //TODO: Add Apropreate Values
        if(pot != null) {
            pid = new PIDController(0.05, 0, 0.02, new PIDSource() {
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
            }, rotateMotor);
            pid.setOutputRange(-0.5, 0.5);
            pid.setAbsoluteTolerance(5);
            pid.setSetpoint(0);
        }
//        pid = null;
    }

    public void setAngle(double angle){
        if(pot != null) pid.setSetpoint(angle);
    }


    public double getAngle(){
        if(pot == null) return 0;
        return ((pot.getRaw() - negative90) / (zero - negative90) * 90) - 90; //approx. conversion from ADC to degrees
    }

    public void enablePID(){
        if(pot != null) pid.enable();
    }

    public void disablePID(){
        if(pot != null) pid.disable();
    }

    public void rotate(double speed){
        rotateMotor.set(speed);
    }

    public double getLength(){
        return length;
    }

    public void tickCalibration(IAxis pot, IButton button){
        boolean myB = button.getValue();
        //System.out.println(myB + " " + calibrating);
        if(myB){
            if(calibrating){
                System.out.println("MIN = " + calMin + " | MAX = " + calMax);
            }else{
                System.out.println("CALIBRATION MODE ACTIVATED!!! " + pot.getRaw());
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

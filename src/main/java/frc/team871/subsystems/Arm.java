package frc.team871.subsystems;

import com.team871.hid.IAxis;
import com.team871.hid.IButton;

public class Arm {

    private ArmSegment upperSegment;
    private ArmSegment lowerSegment;
    private Wrist wrist;
    private ArmMode currentArmMode = ArmMode.DIRECT;
    private double x;
    private double y;

    private enum ArmMode {
        DIRECT,
        INVERSE_KINEMATICS
    }

    public Arm(ArmSegment upperSegment, ArmSegment lowerSegment, Wrist wrist){
        this.upperSegment = upperSegment;
        this.lowerSegment = lowerSegment;
        this.wrist = wrist;
    }

    private double calcUpperAngle(){
        return Math.acos((((upperSegment.getLength() * upperSegment.getLength()) + (lowerSegment.getLength() * lowerSegment.getLength())) - ((x * x) + (y * y))) / (2 * upperSegment.getLength() * lowerSegment.getLength()));
    }

    private double calcLowerAngle(){
         return Math.atan2(y, x) - Math.acos((((lowerSegment.getLength() * lowerSegment.getLength()) + ((x * x) + (y * y))) - (upperSegment.getLength() * upperSegment.getLength()) ) / (2 * upperSegment.getLength() * Math.sqrt((x * x) + (y * y))));
    }

    public void setAngles(double upperAngle, double lowerAngle){
        System.out.println(upperAngle + " " + lowerAngle);
        upperSegment.setAngle(upperAngle);
        lowerSegment.setAngle(lowerAngle);
    }

    public void goTo(double x, double y){
        this.x = x;
        this.y = y;
        setAngles(calcUpperAngle(), calcLowerAngle());
    }

    public void goToRelative(double x, double y){
        goTo(getRadius() * x, getRadius() * y);
    }

    private double getRadius(){
        return lowerSegment.getLength() + upperSegment.getLength();
    }

    public void setWristOrientation(double angle){
        wrist.setOrientation(angle);
    }

    public void handleInverseKinematicsMode(IButton button) {

        if(button.getValue()){
            currentArmMode = currentArmMode == ArmMode.INVERSE_KINEMATICS ? ArmMode.DIRECT : ArmMode.INVERSE_KINEMATICS;
        }
        // currentArmMode = (currentArmMode == ArmMode.INVERSE_KINEMATICS)? ArmMode.DIRECT : ArmMode.INVERSE_KINEMATICS;
    }

    /**
     * Controls the arm by selecting which axes to use based on the current ArmMode.
     * @param upperAxis used in DIRECT control to set the angle of the upper arm segment
     * @param lowerAxis used in DIRECT control to set the angle of the lower arm segment
     * @param xAxis used in INVERSE_KINEMATICS control to set the x-position of the target
     * @param yAxis used in INVERSE_KINEMATICS control to set the y-position of the target
     */
    public void handleArmAxes(IAxis upperAxis, IAxis lowerAxis, IAxis xAxis, IAxis yAxis){
        if(currentArmMode == ArmMode.INVERSE_KINEMATICS) {
            goToRelative(xAxis.getValue(), yAxis.getValue());
        } else {
            setAngles(upperAxis.getValue() * 90, lowerAxis.getValue() * 90);
        }
    }
}

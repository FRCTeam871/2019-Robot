package frc.team871.subsystems;

import com.team871.hid.IAxis;
import com.team871.hid.IButton;

public class Arm {

    public ArmSegment upperSegment;
    public ArmSegment lowerSegment;
    public Wrist wrist;
    private ArmMode currentArmMode;
    private double x;
    private double y;

    public enum ArmMode {
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
        currentArmMode = (button.getValue())? ArmMode.DIRECT : ArmMode.INVERSE_KINEMATICS;
        // currentArmMode = (currentArmMode == ArmMode.INVERSE_KINEMATICS)? ArmMode.DIRECT : ArmMode.INVERSE_KINEMATICS;
    }

    public ArmMode getCurrentArmMode() {
        return currentArmMode;
    }

    public void handleArmAxes(IAxis upperAxis, IAxis lowerAxis, IAxis xAxis, IAxis yAxis){
        if(currentArmMode == ArmMode.INVERSE_KINEMATICS) {
            goToRelative(xAxis.getValue(),yAxis.getValue());
        } else {
            setAngles(upperAxis.getValue(), lowerAxis.getValue());
        } //arm.setAngles(controlScheme.getUpperArmAxis().getValue(), controlScheme.getLowerArmAxis().getValue());
    }
}

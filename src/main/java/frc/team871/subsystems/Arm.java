package frc.team871.subsystems;

import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class Arm implements Sendable {

    private ArmSegment upperSegment;
    private ArmSegment lowerSegment;
    private Wrist wrist;
    private ArmMode currentArmMode = ArmMode.INVERSE_KINEMATICS;
    private double x;
    private double y;
    private double lowerAngle;
    private double upperAngle;

    private final double lowerSq;
    private final double upperSq;

    private enum ArmMode {
        DIRECT,
        INVERSE_KINEMATICS
    }

    public Arm(ArmSegment upperSegment, ArmSegment lowerSegment, Wrist wrist){
        this.upperSegment = upperSegment;
        this.lowerSegment = lowerSegment;
        this.wrist = wrist;
        upperSegment.setName("UpperArm", "UpperArm");
        lowerSegment.setName("LowerArm","LowerArm");
        wrist.setName("Wrist","Wrist");
        LiveWindow.add(upperSegment);
        LiveWindow.add(lowerSegment);
        LiveWindow.add(wrist);

        lowerSq = lowerSegment.getLength() * lowerSegment.getLength();
        upperSq = upperSegment.getLength() * upperSegment.getLength();
    }

    private double calcUpperAngle(){
        double sqDst = (x * x) + (y * y);
        double ac = (upperSq + lowerSq - sqDst) / (2 * upperSegment.getLength() * lowerSegment.getLength());
        return -180 + Math.toDegrees(Math.acos(ac));
    }

    private double calcLowerAngle(){

        double targetX = x;
        double targetY = y;

        double sqDst = (x * x) + (y * y);

//        if(sqDst > lowerSq + upperSq){
//            double angle = Math.atan2(targetY, targetX);
//
//            targetX = (getRadius()) * Math.cos(angle);
//            targetY = (getRadius()) * Math.sin(angle);
//
//            sqDst = (targetX * targetX) + (targetY * targetY);
//        }

        double a = Math.atan2(y, x);

        double ac = (lowerSq + sqDst - upperSq) / (2 * upperSegment.getLength() * Math.sqrt(sqDst));
        if(ac > 1) ac = 1;

        return Math.toDegrees(a - Math.acos(ac));
    }

    public void setAngles(double upperAngle, double lowerAngle){
        this.upperAngle = upperAngle;
        this.lowerAngle = lowerAngle;
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
            goToRelative(xAxis.getValue() / 2.0 + 0.5, yAxis.getValue());
        } else {
            setAngles(upperAxis.getValue() * 90, lowerAxis.getValue() * 90);
        }
    }


    @Override
    public String getName() {
        return "Arm";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getSubsystem() {
        return "Arm";
    }

    @Override
    public void setSubsystem(String subsystem) {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addStringProperty("ArmMode", currentArmMode::toString, (m) -> {});
        builder.addDoubleProperty("LastX", () -> x, (v) -> {});
        builder.addDoubleProperty("LastY", () -> y, (v) -> {});
        builder.addDoubleProperty("LastLowerAngle", () -> lowerAngle, (v) -> {});
        builder.addDoubleProperty("LastUpperAngle", () -> upperAngle, (v) -> {});
    }

}

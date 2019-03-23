package frc.team871.subsystems;

import com.team871.hid.IAxis;
import com.team871.hid.IButton;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

import java.util.Arrays;
import java.util.stream.Stream;

public class Arm implements Sendable {

    private static final double MAX_EXTENT = 30 + 15 - 6; // 30 inch extents + 15 inches from center to edge of frame
    private ArmSegment upperSegment;
    private ArmSegment lowerSegment;
    private Wrist wrist;
    private ArmMode currentArmMode = ArmMode.DIRECT;
    private double x;
    private double y;
    private double lowerAngle;
    private double upperAngle;

    private final double lowerSq;
    private final double upperSq;

    // the height of the origin of the arm off of the floor
    public static final double FLOOR_OFS = 44; //TODO: actually measure this

    public static final double HEIGHT_ROCKET_HATCH_LOW = 19;
    public static final double HEIGHT_ROCKET_HATCH_MID = 47;
    public static final double HEIGHT_ROCKET_HATCH_HIGH = 75;
    public static final double HEIGHT_ROCKET_PORT_LOW = 27.5;
    public static final double HEIGHT_ROCKET_PORT_MID = 55.5;
    public static final double HEIGHT_ROCKET_PORT_HIGH = 83.5;
    public static final double HEIGHT_CARGO_SHIP = 19; // this height is unused because its the same as HEIGHT_ROCKET_HATCH_LOW

    private static final double[] SETPOINT_HEIGHTS = new double[] {HEIGHT_ROCKET_HATCH_LOW, HEIGHT_ROCKET_PORT_LOW, HEIGHT_ROCKET_HATCH_MID, HEIGHT_ROCKET_PORT_MID, HEIGHT_ROCKET_HATCH_HIGH, HEIGHT_ROCKET_PORT_HIGH};
    private static final double HEIGHT_MIN = Arrays.stream(SETPOINT_HEIGHTS).min().getAsDouble();
    private static final double HEIGHT_MAX = Arrays.stream(SETPOINT_HEIGHTS).max().getAsDouble();
    private static final boolean SETPOINT_AXIS_EVENLY_SPACED = false;
    private int currentSetpointIndex = 0;

    private boolean setpointUseAxis = true;
    private double lastSetpointAxis = 0;

    public enum ArmMode {
        DIRECT,
        INVERSE_KINEMATICS,
        SETPOINT
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

    private double calcUpperAngle() {
        double sqDst = (x * x) + (y * y);
        double ac = (upperSq + lowerSq - sqDst) / (2 * upperSegment.getLength() * lowerSegment.getLength());

        if(ac > 1){
            ac = 1;
        }

        return -(-180 + Math.toDegrees(Math.acos(ac)));
    }

    private double calcLowerAngle() {
        double sqDst = (x * x) + (y * y);
        double a = Math.atan2(y, x);

        double ac = (lowerSq + sqDst - upperSq) / (2 * lowerSegment.getLength() * Math.sqrt(sqDst));
        if(ac > 1) {
            ac = 1;
        }

        return Math.toDegrees(a - Math.acos(ac));
    }

    public void setMode(ArmMode mode){
        this.currentArmMode = mode;
    }

    public void setAngles(double upperAngle, double lowerAngle){
        this.upperAngle = upperAngle;
        this.lowerAngle = lowerAngle;
        upperSegment.setAngle(upperAngle);
        lowerSegment.setAngle(lowerAngle);
    }

    public void goTo(double x, double y){
        if(x + wrist.getLength() > MAX_EXTENT) {
            x = MAX_EXTENT - wrist.getLength();
        }

        if(y > 0 && x < 12) x = 12;

        this.x = x;
        this.y = y;

        setAngles(calcUpperAngle(), calcLowerAngle());
    }

    public void goToRelative(double x, double y){

//        double angle = Math.atan2(y, x);
//        double r = Math.sqrt(x*x + y*y);
//
//        r *= getRadius();
//
//        goTo(r * Math.cos(angle), r * Math.sin(angle));
        goTo(x * getRadius(), y * getRadius());
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
    }

    /**
     * Controls the arm by selecting which axes to use based on the current ArmMode.
     * @param upperAxis used in DIRECT control to set the angle of the upper arm segment
     * @param lowerAxis used in DIRECT control to set the angle of the lower arm segment
     * @param xAxis used in INVERSE_KINEMATICS control to set the x-position of the target
     * @param yAxis used in INVERSE_KINEMATICS control to set the y-position of the target
     */
    public void handleArmAxes(IAxis upperAxis, IAxis lowerAxis, IAxis xAxis, IAxis yAxis, IAxis setpointAxis, IButton setpointUpButton, IButton setpointDownButton){
        if(currentArmMode == ArmMode.INVERSE_KINEMATICS) {
            goToRelative((xAxis.getValue() + 1) / 2, yAxis.getValue());
        } else if(currentArmMode == ArmMode.SETPOINT) {

            double ax = (setpointAxis.getValue() + 1) / 2.0; // [-1,1] -> [0,1]
            boolean up = setpointUpButton.getValue();
            boolean down = setpointDownButton.getValue();
            // detect whether to use the axis or buttons
            if(Math.abs(ax - lastSetpointAxis) > 0.1) setpointUseAxis = true;
            if(up || down) setpointUseAxis = false;

            if(setpointUseAxis){
                if(SETPOINT_AXIS_EVENLY_SPACED){
                    currentSetpointIndex = (int)(ax * SETPOINT_HEIGHTS.length);
                }else{
                    double minDist = (HEIGHT_MAX - HEIGHT_MIN) * 2; // arbitrary large number
                    int newSetpoint = currentSetpointIndex;
                    for(int i = 0; i < SETPOINT_HEIGHTS.length; i++){
                        double axisHeight = ax * (HEIGHT_MAX - HEIGHT_MIN) + HEIGHT_MIN;
                        double dist = Math.abs(SETPOINT_HEIGHTS[i] - axisHeight);
                        if(dist < minDist){
                            minDist = dist;
                            newSetpoint = i;
                        }
                    }
                    currentSetpointIndex = newSetpoint;
                }
            }else{
                if(up){
                    currentSetpointIndex++;
                }else if(down){
                    currentSetpointIndex--;
                }
            }

            currentSetpointIndex = Math.max(0, Math.min(currentSetpointIndex, SETPOINT_HEIGHTS.length - 1));

            // see https://www.desmos.com/calculator/ziitcgyynj

            double rawY = SETPOINT_HEIGHTS[currentSetpointIndex] - FLOOR_OFS;

            double r = getRadius();

            // as far out as possible at that height
            // sin(acos(x)) also equals sqrt(1-x^2)
            double projectedX = Math.sin(Math.acos(rawY / r)) * r;
            double projectedY = -rawY;

            goTo(projectedX, projectedY);

            //System.out.println(currentSetpointIndex + " " + SETPOINT_HEIGHTS[currentSetpointIndex] + " " + rawY);

        } else {
            calcTarget(lowerAxis.getValue(), upperAxis.getValue());
        }
    }

    /**
     * Apply forward kinematics from the input angles to get the desired setpoint.
     *
     * @param lowerPotAngle The lower joint angle of the controller.
     * @param upperPotAngle The upper joint angle of the controller.
     */
    private void calcTarget(double lowerPotAngle, double upperPotAngle) {
        // In reality this is upperAngle - (90 - lowerAngle)
        final double compoundAngle = upperPotAngle + lowerPotAngle - 90;
        final double lowerRads = Math.toRadians(lowerPotAngle);
        final double compoundRads = Math.toRadians(compoundAngle);

        final double h1 = Math.sin(lowerRads) * lowerSegment.getLength();
        final double h2 = Math.cos(compoundRads) * upperSegment.getLength();

        final double l1 = Math.cos(lowerRads) * lowerSegment.getLength();
        final double l2 = -Math.sin(compoundRads) * upperSegment.getLength();

        goTo(l1 + l2, (h1 + h2));
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

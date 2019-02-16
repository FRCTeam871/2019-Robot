package frc.team871.config.network;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * @author T3Pfaffe on 1/30/2019.
 * @project DriverStation
 * Is a collection of network table variable keys specific to the Armstrong Robot
 */
public class DeepSpaceNetConfig extends AbstractNetConfig {


    public final String TABLE_KEY;

    public final String HEADING_KEY;
    public final String LOCATION_KEY;

    public final String UPPER_ARM_ANGLE_KEY;
    public final String LOWER_ARM_ANGLE_KEY;
    public final String WRIST_ANGLE_KEY;

    public final String UPPER_ARM_PID_KEY;
    public final String LOWER_ARM_PID_KEY;
    public final String WRIST_PID_KEY;

    public final String FOUND_TARGETS_LIST_KEY;

    public final String IS_GRABBING_KEY;
    public final String IS_VACUUM_ON_KEY;
    public final String IS_VACUUM_INNER_KEY;

    public DeepSpaceNetConfig(boolean isClient, NetworkTableInstance instance, String VERSION_VAL) {
        super(isClient, instance, VERSION_VAL);


        TABLE_KEY = "ROBOT";

        HEADING_KEY  = "headingAngle";
        LOCATION_KEY = "location";

        UPPER_ARM_ANGLE_KEY = "upperArmAngle";
        LOWER_ARM_ANGLE_KEY = "lowerArmAngle";
        WRIST_ANGLE_KEY     = "wristAngle";
        WRIST_PID_KEY       = "wristPID";
        LOWER_ARM_PID_KEY   = "lowerArmPID";
        UPPER_ARM_PID_KEY   = "upperArmPID";

        FOUND_TARGETS_LIST_KEY = "foundTargetsList";

        IS_GRABBING_KEY     = "isGrabbing";
        IS_VACUUM_ON_KEY    = "isVacuumOn";
        IS_VACUUM_INNER_KEY = "isVacuumInner";
    }

    public NetworkTable getTable(){
        return super.getInstance().getTable(TABLE_KEY);
    }

}

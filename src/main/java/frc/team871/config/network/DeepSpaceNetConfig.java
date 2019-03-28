package frc.team871.config.network;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.team871.config.network.tables.ArmNetTable;
import frc.team871.config.network.tables.GripNetTable;
import frc.team871.config.network.tables.RobotLocalizationNetTable;

/**
 * @author T3Pfaffe on 1/30/2019.
 * @project DriverStation
 * Is a collection of Keys  corresponding to data points over Network Tables.
 */
public class DeepSpaceNetConfig extends AbstractNetConfig {

    //Default table key
    public static final String TABLE_KEY = "ROBOT";

    public static final String TIME_ELAPSED_KEY = "TimeElapsed";

    //Default camera table
    private static final String CAMERAS_TABLE_KEY = "CameraPublisher";

    //Vacuum Keys
    public static final String IS_GRABBING_KEY = "isGrabbing";
    public static final String IS_VACUUM_ON_KEY = "isVacuumOn";
    public static final String IS_VACUUM_INNER_KEY = "isVacuumInner";
    //Networked PID tableKeys
    public static final String UPPER_ARM_PID_KEY = "upperArmPID";
    public static final String LOWER_ARM_PID_KEY = "lowerArmPID";
    public static final String WRIST_PID_KEY = "wristPID";


    //Robot location information
    public final RobotLocalizationNetTable robotLocalizationTable;

    //Networked Vision Processing tableKeys
    public final GripNetTable gripTable;

    //Arm position information
    public final ArmNetTable armTable;

    //Camera information
    public final NetworkTable camerasTable;

    public DeepSpaceNetConfig(boolean isClient, NetworkTableInstance instance) {
        super(isClient, instance);
        robotLocalizationTable = new RobotLocalizationNetTable(getDefaultTable());
        gripTable   = new GripNetTable(getDefaultTable());
        armTable    = new ArmNetTable(getDefaultTable());
        camerasTable = getInstance().getTable(CAMERAS_TABLE_KEY);
    }

    public DeepSpaceNetConfig(boolean isClient, NetworkTableInstance instance, String VERSION_VAL) {
        super(isClient, instance, VERSION_VAL);
        robotLocalizationTable = new RobotLocalizationNetTable(getDefaultTable());
        gripTable = new GripNetTable(getDefaultTable());
        armTable = new ArmNetTable(getDefaultTable());
        camerasTable = instance.getTable(CAMERAS_TABLE_KEY);
    }

    @Override
    public NetworkTable getDefaultTable(){
        return super.getInstance().getTable(TABLE_KEY);
    }

}

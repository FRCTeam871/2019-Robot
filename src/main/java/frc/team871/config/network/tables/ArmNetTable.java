package frc.team871.config.network.tables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * @author T3Pfaffe on 3/27/2019.
 * @project DriverStation
 */
public final class ArmNetTable extends NetworkEntriesModule {

    //Arm position information
    private static final String ARM_TABLE_KEY = "arm";
    private static final String UPPER_ARM_ANGLE_KEY = "upperArmAngle";
    private static final String LOWER_ARM_ANGLE_KEY = "lowerArmAngle";
    private static final String WRIST_ANGLE_KEY = "wristAngle";

    public ArmNetTable(NetworkTable parentTable) {
        super(parentTable, ARM_TABLE_KEY);
    }

    public final NetworkTableEntry getUpperArmAngleEntry(){
        return getTable().getEntry(UPPER_ARM_ANGLE_KEY);
    }

    public final NetworkTableEntry getLowerArmAngleEntry(){
        return getTable().getEntry(LOWER_ARM_ANGLE_KEY);
    }

    public final NetworkTableEntry getWristAngleEntry(){
        return getTable().getEntry(WRIST_ANGLE_KEY);
    }
}

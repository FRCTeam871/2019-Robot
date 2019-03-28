package frc.team871.config.network.tables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * @author T3Pfaffe on 3/27/2019.
 * @project DriverStation
 */
public final class TargetNetTable extends NetworkEntriesModule{

    //General Target Information subKeys
    private static final String HAS_TARGET_KEY = "hasTarget";
    private static final String DISTANCE_KEY   = "distance";
    private static final String ANGLE_KEY      = "angle";
    private static final String CENTER_X_KEY   = "centerX";
    private static final String CENTER_Y_KEY   = "centerY";
    private static final String LENGTH_X_KEY   = "lengthX";
    private static final String LENGTH_Y_KEY   = "lengthY";

    public TargetNetTable(NetworkTable parentTable, String targetName) {
        super(parentTable, targetName);
    }

    public NetworkTableEntry getHasTargetEntry(){
        return getTable().getEntry(HAS_TARGET_KEY);
    }

    public NetworkTableEntry getDistanceEntry(){
        return getTable().getEntry(DISTANCE_KEY);
    }

    public NetworkTableEntry getAngleEntry(){
        return getTable().getEntry(ANGLE_KEY);
    }

    public NetworkTableEntry getCenterXEntry(){
        return getTable().getEntry(CENTER_X_KEY);
    }

    public NetworkTableEntry getCenterYEntry(){
        return getTable().getEntry(CENTER_Y_KEY);
    }

    public NetworkTableEntry getLengthXEntry(){
        return getTable().getEntry(LENGTH_X_KEY);
    }

    public NetworkTableEntry getLengthYEntry(){
        return getTable().getEntry(LENGTH_Y_KEY);
    }

}

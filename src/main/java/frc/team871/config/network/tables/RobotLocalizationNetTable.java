package frc.team871.config.network.tables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * @author T3Pfaffe on 3/27/2019.
 * @project DriverStation
 */
public final class RobotLocalizationNetTable extends NetworkEntriesModule{

    private static final String LOCALIZATION_TABLE_KEY = "localization";
    private static final String HEADING_KEY    = "headingAngle";
    private static final String LOCATION_X_KEY = "locationX";
    private static final String LOCATION_Y_KEY = "locationY";

    public RobotLocalizationNetTable(NetworkTable parentTable) {
        super(parentTable, LOCALIZATION_TABLE_KEY);
    }

    public NetworkTableEntry getHeadingEntry(){
        return getTable().getEntry(HEADING_KEY);
    }

    public NetworkTableEntry getLocationXEntry(){
        return getTable().getEntry(LOCATION_X_KEY);
    }

    public NetworkTableEntry getLocationYEntry(){
        return getTable().getEntry(LOCATION_Y_KEY);
    }



}

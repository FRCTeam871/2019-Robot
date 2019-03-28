package frc.team871.config.network.tables;

import edu.wpi.first.networktables.NetworkTable;

/**
 * @author T3Pfaffe on 3/27/2019.
 * @project DriverStation
 */
public final class GripNetTable extends NetworkEntriesModule {

    public static final String SENSOR_DATA_TABLE_KEY     = "GRIP";
    public static final String DOCKING_TARGET_SENSOR_KEY = "dockingTargetSensor";
    public static final String LINE_SENSOR_KEY           = "lineSensor";

    private final TargetNetTable dockingTargetTable;
    private final TargetNetTable lineSensorTable;


    public GripNetTable(NetworkTable parentTable) {
        super(parentTable, SENSOR_DATA_TABLE_KEY);
        dockingTargetTable = new TargetNetTable(this.getTable(), DOCKING_TARGET_SENSOR_KEY);
        lineSensorTable    = new TargetNetTable(this.getTable(), LINE_SENSOR_KEY);
    }

    public TargetNetTable getDockingTargetTable(){
        return dockingTargetTable;
    }

    public TargetNetTable getLineSensorTable(){
        return lineSensorTable;
    }
}

package frc.team871.config.network.tables;

import edu.wpi.first.networktables.NetworkTable;

/**
 * @author T3Pfaffe on 3/27/2019.
 * @project DriverStation
 */
public class NetworkEntriesModule {

    private final NetworkTable parentTable;
    private final NetworkTable wrappedTable;

    public NetworkEntriesModule(NetworkTable parentTable, String SUB_TABLE_NAME){
        this.parentTable  = parentTable;
        this.wrappedTable = parentTable.getSubTable(SUB_TABLE_NAME);
    }

    public NetworkTable getTable(){
        return this.wrappedTable;
    }

    @Override
    public String toString(){
        return getTable().toString();
    }
}

package frc.team871.config.network.tables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * @author T3Pfaffe on 3/27/2019.
 * @project DriverStation
 */
public final class FmsNetTable extends NetworkEntriesModule{

    private static final String FMS_TABLE_KEY              = "FMSInfo";
    private static final String EVENT_NAME_KEY             = "EventName";
    private static final String GAMES_SPECIFIC_MESSAGE_KEY = "GameSpecificMessage";
    private static final String IS_RED_TEAM_KEY            = "IsRedAlliance";
    private static final String MATCH_NUMBER_KEY           = "MatchNumber";
    private static final String MATCH_TYPE_KEY             = "MatchType";
    private static final String REPLAY_NUMBER_KEY          = "ReplayNumber";
    private static final String STATION_NUMBER_KEY         = "StationNumber";


    public FmsNetTable(NetworkTable parentTable) {
        super(parentTable, FMS_TABLE_KEY);
    }

    public NetworkTableEntry getEventNameEntry(){
        return getTable().getEntry(EVENT_NAME_KEY);
    }

    public NetworkTableEntry getGameSpecificMessageEntry(){
        return getTable().getEntry(GAMES_SPECIFIC_MESSAGE_KEY);
    }

    public NetworkTableEntry getIsRedTeamEntry(){
        return getTable().getEntry(IS_RED_TEAM_KEY);
    }

    public NetworkTableEntry getMatchNumberEntry(){
        return getTable().getEntry(MATCH_NUMBER_KEY);
    }

    public NetworkTableEntry getMatchTypeEntry(){
        return getTable().getEntry(MATCH_TYPE_KEY);
    }

    public NetworkTableEntry getReplayNumberEntry(){
        return getTable().getEntry(REPLAY_NUMBER_KEY);
    }

    public NetworkTableEntry getStationNumberEntry(){
        return getTable().getEntry(STATION_NUMBER_KEY);
    }

}

package frc.team871.config.network;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.Arrays;

/**
 * @author TP-Laptop on 1/5/2019.
 * @project Robotics-Dashboard-Code
 *
 * Template that sets up an agreement of the data
 *  available on the NetworkTables.
 * Will *attempt* in insure that both the server
 *  and client are working on an agreed set of
 *  variables.
 */
public abstract class AbstractNetConfig {

  private final NetworkTableInstance networkTableInstance;
  public final String networkIdentity;

  //Server-client version check keys:
  public final String SERVER_VERSION_KEY = "SERVER_VERSION";
  public final String CLIENT_VERSION_KEY = "CLIENT_VERSION";

  //Generic match information keys:
  public final String FMS_TABLE_KEY             = "FMSInfo";
  public final String EVENT_NAME_KEY            = "EventName";
  public final String GAMES_SPECFIC_MESSAGE_KEY = "GameSpecificMessage";
  public final String IS_RED_TEAM_KEY           = "IsRedAlliance";
  public final String MATCH_NUMBER_KEY          = "MatchNumber";
  public final String MATCH_TYPE_KEY            = "MatchType";
  public final String REPLAY_NUMBER_KEY         = "ReplayNumber";
  public final String STATION_NUMBER_KEY        = "StationNumber";


  private Thread checkVersionThread;

  public AbstractNetConfig(boolean isClient, NetworkTableInstance instance){
    this(isClient, instance, "0.00");
  }

  public AbstractNetConfig(boolean isClient, NetworkTableInstance instance, String VERSION_VAL) {

    this.networkTableInstance = instance;

    if(isClient) {
      networkIdentity = "client";
      instance.setNetworkIdentity(networkIdentity);
    }
    else {
      networkIdentity = "server";
      instance.setNetworkIdentity(networkIdentity);
      instance.getEntry(SERVER_VERSION_KEY).setString(VERSION_VAL);
    }

    Runnable checkVersionTask = () ->{
      try {
        while(!instance.isConnected())
          Thread.sleep(50);
      } catch (InterruptedException e) {
        //nothing
      }

      System.out.println(" ");
      if (!instance.getEntry(SERVER_VERSION_KEY).getString("null").equals(VERSION_VAL)) {
        if(isClient) {
          System.out.println("WARNING!! NetworkTableKey versions are not the same. \nThis may make the robot and any clients incompatible.");
          System.out.println("Server(robot) Version: " + instance.getEntry(SERVER_VERSION_KEY).getString("[Not_Found]"));
          System.out.println("       Client Version: " + VERSION_VAL);
        }else
          System.out.println("ERROR!! NetworkTables were not set correctly, clients will most likely not work in conjunction with this server");
      }else{
        System.out.println("NetworkTable connection started with no found problems");
        System.out.println( networkIdentity + " broadcasting with version: " + VERSION_VAL);
        System.out.println( "Currently connected to: " + Arrays.toString(this.getInstance().getConnections()));
      }
      System.out.println(" \n");
      Thread.currentThread().interrupt();
    };
    //Runs until it can find the server.

    checkVersionThread = new Thread(checkVersionTask);
    checkVersionThread.setDaemon(true);
    checkVersionThread.start();
    //Fun fact #3064: you cant set Daemeon to true if you already started the thread.

  }



  public NetworkTableInstance getInstance(){
    return networkTableInstance;
  }

  public boolean isConnected(){
    return getInstance().isConnected();
  }

  public abstract NetworkTable getDefaultTable();

}
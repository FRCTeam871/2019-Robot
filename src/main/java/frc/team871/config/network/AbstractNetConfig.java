package frc.team871.config.network;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

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

    public final NetworkTableInstance networkTableInstance;
    public final String SERVER_VERSION_KEY = "SERVER_VERSION";
    public final String CLIENT_VERSION_KEY = "CLIENT_VERSION";
    public final String NET_IDENTITY;


    public AbstractNetConfig(boolean isClient, NetworkTableInstance instance, String VERSION_VAL) {
        this.networkTableInstance = instance;

        if(isClient) {
            NET_IDENTITY = "client";
            instance.setNetworkIdentity(NET_IDENTITY);
        }
        else {
            NET_IDENTITY = "server";
            instance.setNetworkIdentity(NET_IDENTITY);
            instance.getEntry(SERVER_VERSION_KEY).setString(VERSION_VAL);
        }



        if (!instance.getEntry(SERVER_VERSION_KEY).getString("null").equals(VERSION_VAL)) {
            if(isClient) {
                System.out.println("WARNING!! NetworkTableKey versions are not the same. \nThis may make the robot and any clients incompatible.");
                System.out.println("Server(robot) Version: " + instance.getEntry(SERVER_VERSION_KEY).getString("[Not_Found]"));
                System.out.println("       Client Version: " + CLIENT_VERSION_KEY);
            }else
                System.out.println("ERROR!! NetworkTables were not set correctly, clients will most likely not work in conjunction with this server");
        }else{
            System.out.println("NetworkTable connection started with no found problems");
            System.out.println( NET_IDENTITY + " broadcasting with version: " + VERSION_VAL);
        }

    }

    public NetworkTableInstance getInstance(){
        return networkTableInstance;
    }

    public abstract NetworkTable getTable();

}

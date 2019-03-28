package frc.team871.auto.detection.coprocessor;

import edu.wpi.first.networktables.EntryListenerFlags;
import frc.team871.auto.ITarget;
import frc.team871.config.network.tables.TargetNetTable;

public class CoProcessorNetworkDockingTargetSensor implements ITarget {

    private double angle;
    private double  centerX;
    private double  centerY;
    private double  distance;
    private double  lengthX;
    private double  lengthY;
    private boolean targetExists;

    public CoProcessorNetworkDockingTargetSensor (TargetNetTable lineSensorTable){
        angle = 0;
        centerX      = 0;
        distance     = 0;
        lengthX      = 0;
        lengthY      = 0;
        targetExists = false;

        //Change listeners here so a get<value> does not spam networkTables for values even if nothing has changed.
        lineSensorTable.getAngleEntry().addListener(event     -> angle = event.getEntry().getDouble(0), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        lineSensorTable.getCenterXEntry().addListener(event   -> centerX      = event.getEntry().getDouble(0), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        lineSensorTable.getCenterYEntry().addListener(event   -> centerY      = event.getEntry().getDouble(0), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        lineSensorTable.getDistanceEntry().addListener(event  -> distance     = event.getEntry().getDouble(0), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        lineSensorTable.getLengthXEntry().addListener(event   -> lengthX      = event.getEntry().getDouble(0), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        lineSensorTable.getLengthYEntry().addListener(event   -> lengthY      = event.getEntry().getDouble(0), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        lineSensorTable.getHasTargetEntry().addListener(event -> targetExists = event.getEntry().getBoolean(false), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);


    }

    //TODO: get rid of ILineSensor and just have angle included in ITarget

    /**
     * @return the angle of the last found target.
     */
    public double getAngle(){
        return angle;
    }

    @Override
    public double getCenterX() {
        return centerX;
    }

    @Override
    public double getCenterY() {
        return centerY;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public double getLengthX() {
        return lengthX;
    }

    @Override
    public double getLengthY() {
        return lengthY;
    }

    @Override
    public boolean doesTargetExist() {
        return targetExists;
    }
}

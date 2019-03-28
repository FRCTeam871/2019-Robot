package frc.team871.auto.detection.coprocessor;

import frc.team871.auto.ILineSensor;
import frc.team871.auto.ITarget;
import frc.team871.auto.ITargetProvider;
import frc.team871.config.network.tables.GripNetTable;

public class CoProcessorNetworkTargetProvider implements ITargetProvider {

    private ILineSensor lineTargetSense;
    private ITarget dockingTargetSense;

    public CoProcessorNetworkTargetProvider(GripNetTable gripNetTable){
        lineTargetSense = new CoProcessorNetworkLineSensor(gripNetTable.getLineSensorTable());
        dockingTargetSense = new CoProcessorNetworkDockingTargetSensor(gripNetTable.getDockingTargetTable());
    }

    @Override
    public ILineSensor getLineSensor() {
        return lineTargetSense;
    }

    @Override
    public ITarget getTarget() {
        return dockingTargetSense;
    }
}

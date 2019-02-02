package frc.team871.auto;

import com.team871.navigation.IWaypointProvider;
import com.team871.navigation.Waypoint;

public class DockingWaypointProvider<T extends Waypoint> implements IWaypointProvider<T> {

    private T wayPoint;
    private boolean hasProvidedWayPoint;

    public DockingWaypointProvider(T wayPoint) {
        this.wayPoint = wayPoint;
        this.hasProvidedWayPoint = false;
    }

    @Override
    public void reset() {
        hasProvidedWayPoint = false;
    }

    @Override
    public boolean hasNext() {
        return !hasProvidedWayPoint;
    }

    @Override
    public T next() {
        hasProvidedWayPoint = true;
        return wayPoint;
    }
}

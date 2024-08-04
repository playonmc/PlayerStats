package mc.play.stats.obj;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class EventDistanceInfo {
    private final Location startLocation;
    private final EntityType vehicleType; // This can be null for glide events
    private final long startTime;

    public EventDistanceInfo(Location startLocation, EntityType vehicleType, long startTime) {
        this.startLocation = startLocation;
        this.vehicleType = vehicleType;
        this.startTime = startTime;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public EntityType getVehicleType() {
        return vehicleType;
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "EventDistanceInfo[startLocation=" + startLocation + ", vehicleType=" + vehicleType + ", startTime=" + startTime + "]";
    }
}
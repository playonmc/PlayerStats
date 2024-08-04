package mc.play.stats.obj;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class EventDistanceInfo {
    private final Location startLocation;
    private final EntityType vehicleType;
    private final long startTime;
    private final ActivityType activityType;

    public enum ActivityType {
        GLIDE, RIDE, SWIM
    }

    public EventDistanceInfo(Location startLocation, EntityType vehicleType, long startTime, ActivityType activityType) {
        this.startLocation = startLocation;
        this.vehicleType = vehicleType;
        this.startTime = startTime;
        this.activityType = activityType;
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

    public ActivityType getActivityType() {
        return activityType;
    }

    @Override
    public String toString() {
        return "EventDistanceInfo[startLocation=" + startLocation + ", vehicleType=" + vehicleType + ", startTime=" + startTime + ", activityType=" + activityType + "]";
    }
}

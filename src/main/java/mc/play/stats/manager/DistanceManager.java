package mc.play.stats.manager;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.EventDistanceInfo;
import mc.play.stats.util.DistanceEventUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DistanceManager {
    private final Map<UUID, EventDistanceInfo> eventDistanceInfo;
    private final double MIN_DISTANCE_FLOWN = 15.0;
    private final double MIN_DISTANCE_RODE = 15.0;
    private final DistanceEventUtil distanceEventUtil;

    public DistanceManager(PlayerStatsPlugin plugin) {
        this.eventDistanceInfo = new HashMap<>();
        this.distanceEventUtil = new DistanceEventUtil(plugin);
        initializeTrackingForExistingVehicles();
    }

    public Map<UUID, EventDistanceInfo> getEventDistanceInfo() {
        return eventDistanceInfo;
    }

    public double getMinDistanceFlown() {
        return MIN_DISTANCE_FLOWN;
    }

    public double getMinDistanceRode() {
        return MIN_DISTANCE_RODE;
    }

    public DistanceEventUtil getDistanceEventUtil() {
        return distanceEventUtil;
    }

    public void finalizeAllDistanceEvents() {
        for (Map.Entry<UUID, EventDistanceInfo> entry : eventDistanceInfo.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                distanceEventUtil.processDistanceEvent(player, entry.getValue(), MIN_DISTANCE_FLOWN, MIN_DISTANCE_RODE);
            }
        }
        eventDistanceInfo.clear();
    }

    private void initializeTrackingForExistingVehicles() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isInsideVehicle()) {
                Vehicle vehicle = (Vehicle) player.getVehicle();
                if (vehicle != null) {
                    EntityType vehicleType = vehicle.getType();
                    if (vehicleType == EntityType.BOAT || vehicleType == EntityType.HORSE || vehicleType == EntityType.PIG || vehicleType == EntityType.DONKEY) {
                        eventDistanceInfo.put(player.getUniqueId(), new EventDistanceInfo(player.getLocation(), vehicleType, System.currentTimeMillis(), EventDistanceInfo.ActivityType.RIDE));
                    }
                }
            }
        }
    }
}
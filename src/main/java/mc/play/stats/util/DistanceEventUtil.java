package mc.play.stats.util;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import mc.play.stats.obj.EventDistanceInfo;
import org.bukkit.entity.Player;

public class DistanceEventUtil {

    private final PlayerStatsPlugin plugin;

    public DistanceEventUtil(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void processDistanceEvent(Player player, EventDistanceInfo eventInfo, double minDistanceFlown, double minDistanceRode) {
        if (eventInfo == null) return;

        double distance = eventInfo.getStartLocation().distance(player.getLocation());
        long timeElapsed = System.currentTimeMillis() - eventInfo.getStartTime();
        double roundedDistance = Math.round(distance * 10000.0) / 10000.0;
        double speed = Math.round((roundedDistance / (timeElapsed / 1000.0)) * 10000.0) / 10000.0;

        switch (eventInfo.getActivityType()) {
            case GLIDE:
                if (distance >= minDistanceFlown) {
                    Event glideEvent = new Event("player:glide")
                            .setMetadata("distance", roundedDistance)
                            .setMetadata("speed", speed);
                    plugin.triggerEvent(glideEvent, player);
                }
                break;
            case RIDE:
                if (distance >= minDistanceRode) {
                    Event rideEvent = new Event("player:ride")
                            .setMetadata("distance", roundedDistance)
                            .setMetadata("speed", speed)
                            .setMetadata("vehicle", eventInfo.getVehicleType().toString());
                    plugin.triggerEvent(rideEvent, player);
                }
                break;
            case SWIM:
                if (distance >= minDistanceRode) { // Assuming same minimum distance for swimming
                    Event swimEvent = new Event("player:swim")
                            .setMetadata("distance", roundedDistance)
                            .setMetadata("speed", speed);
                    plugin.triggerEvent(swimEvent, player);
                }
                break;
        }
    }
}

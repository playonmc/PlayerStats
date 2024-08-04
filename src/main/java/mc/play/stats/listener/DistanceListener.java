package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import mc.play.stats.obj.EventDistanceInfo;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import java.util.UUID;

public class DistanceListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public DistanceListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player player) {
            UUID playerId = player.getUniqueId();
            if (event.isGliding()) {
                plugin.getEventDistanceInfo().put(playerId, new EventDistanceInfo(player.getLocation(), null, System.currentTimeMillis()));
            } else {
                EventDistanceInfo eventInfo = plugin.getEventDistanceInfo().remove(playerId);
                if (eventInfo != null) {
                    double distance = eventInfo.getStartLocation().distance(player.getLocation());
                    long timeElapsed = System.currentTimeMillis() - eventInfo.getStartTime();
                    if (distance >= plugin.getMinDistanceFlown()) {
                        double roundedDistance = Math.round(distance * 10000.0) / 10000.0;
                        double speed = Math.round((roundedDistance / (timeElapsed / 1000.0)) * 10000.0) / 10000.0;
                        Event glideEvent = new Event("player:glide")
                                .setMetadata("distance", roundedDistance)
                                .setMetadata("speed", speed)
                                .setMetadata("world", player.getWorld().getName());
                        plugin.triggerEvent(glideEvent, player);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player player) {
            EntityType vehicleType = event.getVehicle().getType();
            if (vehicleType == EntityType.BOAT || vehicleType == EntityType.HORSE || vehicleType == EntityType.PIG || vehicleType == EntityType.DONKEY) {
                plugin.getEventDistanceInfo().put(player.getUniqueId(), new EventDistanceInfo(player.getLocation(), vehicleType, System.currentTimeMillis()));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player player) {
            UUID playerId = player.getUniqueId();
            EventDistanceInfo rideInfo = plugin.getEventDistanceInfo().remove(playerId);
            if (rideInfo != null) {
                double distance = rideInfo.getStartLocation().distance(player.getLocation());
                long timeElapsed = System.currentTimeMillis() - rideInfo.getStartTime();
                if (distance >= plugin.getMinDistanceRode()) {
                    double roundedDistance = Math.round(distance * 10000.0) / 10000.0;
                    double speed = Math.round((roundedDistance / (timeElapsed / 1000.0)) * 10000.0) / 10000.0;
                    Event rideEvent = new Event("player:ride")
                            .setMetadata("distance", roundedDistance)
                            .setMetadata("speed", speed)
                            .setMetadata("world", player.getWorld().getName())
                            .setMetadata("animal", rideInfo.getVehicleType().toString());
                    plugin.triggerEvent(rideEvent, player);
                }
            }
        }
    }
}

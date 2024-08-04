package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.EventDistanceInfo;
import mc.play.stats.util.DistanceEventUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
                plugin.getDistanceManager().getEventDistanceInfo().put(playerId, new EventDistanceInfo(player.getLocation(), null, System.currentTimeMillis(), EventDistanceInfo.ActivityType.GLIDE));
            } else {
                plugin.getDistanceManager().getDistanceEventUtil().processDistanceEvent(player, plugin.getDistanceManager().getEventDistanceInfo().remove(playerId), plugin.getDistanceManager().getMinDistanceFlown(), plugin.getDistanceManager().getMinDistanceRode());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player player) {
            EntityType vehicleType = event.getVehicle().getType();
            if (vehicleType == EntityType.BOAT || vehicleType == EntityType.HORSE || vehicleType == EntityType.PIG || vehicleType == EntityType.DONKEY) {
                plugin.getDistanceManager().getEventDistanceInfo().put(player.getUniqueId(), new EventDistanceInfo(player.getLocation(), vehicleType, System.currentTimeMillis(), EventDistanceInfo.ActivityType.RIDE));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player player) {
            plugin.getDistanceManager().getDistanceEventUtil().processDistanceEvent(player, plugin.getDistanceManager().getEventDistanceInfo().remove(player.getUniqueId()), plugin.getDistanceManager().getMinDistanceFlown(), plugin.getDistanceManager().getMinDistanceRode());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        EventDistanceInfo eventInfo = plugin.getDistanceManager().getEventDistanceInfo().get(playerId);

        if (player.isSwimming()) {
            if (eventInfo == null || eventInfo.getActivityType() != EventDistanceInfo.ActivityType.SWIM) {
                plugin.getLogger().info("Player " + player.getName() + " started swimming. Starting new swim distance tracking.");
                plugin.getDistanceManager().getEventDistanceInfo().put(playerId, new EventDistanceInfo(player.getLocation(), null, System.currentTimeMillis(), EventDistanceInfo.ActivityType.SWIM));
            }
        } else if (eventInfo != null && eventInfo.getActivityType() == EventDistanceInfo.ActivityType.SWIM) {
            plugin.getLogger().info("Player " + player.getName() + " stopped swimming. Processing swim distance event.");
            plugin.getDistanceManager().getDistanceEventUtil().processDistanceEvent(player, plugin.getDistanceManager().getEventDistanceInfo().remove(playerId), plugin.getDistanceManager().getMinDistanceFlown(), plugin.getDistanceManager().getMinDistanceRode());
        }
    }
}
package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class BedListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public BedListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        Event bedEnterEvent = new Event("player:bed_enter")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("location.x", playerLocation.getBlockX())
                .setMetadata("location.y", playerLocation.getBlockY())
                .setMetadata("location.z", playerLocation.getBlockZ())
                .setMetadata("location.world", player.getWorld().getName());

        plugin.addEvent(bedEnterEvent);
    }
}

package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalListeners implements Listener {
    private final PlayerStatsPlugin plugin;

    public PortalListeners(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortalEnter(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        Event portalEnterEvent = new Event("player:portal_enter")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("portalType", event.getCause().toString())
                .setMetadata("location", player.getLocation().toString())
                .setMetadata("world", player.getWorld().getName());
        plugin.addEvent(portalEnterEvent);
    }
}

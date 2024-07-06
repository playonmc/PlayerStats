package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public RespawnListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location respawnLocation = event.getRespawnLocation();
        World world = respawnLocation.getWorld();
        Event respawnEvent = new Event("player:respawn")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("location.x", respawnLocation.getBlockX())
                .setMetadata("location.y", respawnLocation.getBlockY())
                .setMetadata("location.z", respawnLocation.getBlockZ())
                .setMetadata("location.world", world == null ? "unknown" : world.getName());

        plugin.addEvent(respawnEvent);
    }
}

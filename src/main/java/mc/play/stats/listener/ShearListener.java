package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class ShearListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public ShearListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        Event shearEvent = new Event("player:shear")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("entityType", event.getEntity().getType().toString())
                .setMetadata("location", event.getEntity().getLocation().toString())
                .setMetadata("world", player.getWorld().getName());
        plugin.addEvent(shearEvent);
    }
}

package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static mc.play.stats.util.ItemStackSerializer.convertItemStackArrayToEvent;

public class PlayerDeathListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public PlayerDeathListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location playerLocation = player.getLocation();
        World playerWorld = player.getWorld();

        Event deathEvent = new Event("player:death")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("location.x", playerLocation.getBlockX())
                .setMetadata("location.y", playerLocation.getBlockY())
                .setMetadata("location.z", playerLocation.getBlockZ())
                .setMetadata("location.world", playerWorld.getName())
                .setMetadata("deathMessage", event.getDeathMessage())
                .setMetadata("causeOfDeath", player.getLastDamageCause() != null ? player.getLastDamageCause().getCause().toString() : "UNKNOWN");

        // Add inventory contents to event
        convertItemStackArrayToEvent(deathEvent, "inventoryContents", player.getInventory().getContents());

        if (player.getKiller() != null) {
            deathEvent.setMetadata("killerName", player.getKiller().getName())
                    .setMetadata("killerUuid", player.getKiller().getUniqueId().toString());
        }

        plugin.addEvent(deathEvent);
    }
}

package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemListeners implements Listener {
    private final PlayerStatsPlugin plugin;

    public ItemListeners(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            Location itemPickupLocation = event.getItem().getLocation();
            Event pickupEvent = new Event("item:pickup")
                    .setMetadata("playerName", player.getName())
                    .setMetadata("playerUuid", player.getUniqueId().toString())
                    .setMetadata("item", event.getItem().getItemStack().getType().toString())
                    .setMetadata("location.x", itemPickupLocation.getBlockX())
                    .setMetadata("location.y", itemPickupLocation.getBlockY())
                    .setMetadata("location.z", itemPickupLocation.getBlockZ())
                    .setMetadata("location.world", itemPickupLocation.getWorld().getName())
                    .setMetadata("remainingStack", event.getItem().getItemStack().getAmount());
            plugin.addEvent(pickupEvent);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Location itemDrop = event.getItemDrop().getLocation();
        Event dropEvent = new Event("item:drop")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("item", event.getItemDrop().getItemStack().getType().toString())
                .setMetadata("location.x", itemDrop.getBlockX())
                .setMetadata("location.y", itemDrop.getBlockY())
                .setMetadata("location.z", itemDrop.getBlockZ())
                .setMetadata("location.world", itemDrop.getWorld().getName())
                .setMetadata("world", player.getWorld().getName()) // Consider if this is necessary since world is already inside location
                .setMetadata("dropCause", "player")
                .setMetadata("itemCount", event.getItemDrop().getItemStack().getAmount());
        plugin.addEvent(dropEvent);
    }
}

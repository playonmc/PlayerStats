package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import static mc.play.stats.util.ItemStackSerializer.convertItemStackToEvent;

public class PlayerDamageListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public PlayerDamageListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker && event.getEntity() instanceof Player damaged) {
            ItemStack weapon = attacker.getInventory().getItemInMainHand();
            Location attackerLocation = attacker.getLocation();

            Event pvpEvent = new Event("pvp:damage")
                    .setMetadata("attackerName", attacker.getName())
                    .setMetadata("attackerUuid", attacker.getUniqueId().toString())
                    .setMetadata("playerName", damaged.getName())
                    .setMetadata("playerUuid", damaged.getUniqueId().toString())
                    .setMetadata("damage", event.getDamage())
                    .setMetadata("location.x", attackerLocation.getBlockX())
                    .setMetadata("location.y", attackerLocation.getBlockY())
                    .setMetadata("location.z", attackerLocation.getBlockZ())
                    .setMetadata("location.world", attacker.getWorld().getName());

            convertItemStackToEvent(pvpEvent, "weaponUsed", weapon);

            plugin.addEvent(pvpEvent);
        }
    }
}

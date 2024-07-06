package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import static mc.play.stats.util.ItemStackSerializer.convertItemStackArrayToEvent;
import static mc.play.stats.util.ItemStackSerializer.convertItemStackToEvent;

public class PlayerKillListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public PlayerKillListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        // Early return if there is no killer (i.e., the entity was not killed by a player)
        if (killer == null) {
            return;
        }

        String causeOfDeath = entity.getLastDamageCause() != null ? entity.getLastDamageCause().getCause().toString() : "UNKNOWN";
        Location entityLocation = entity.getLocation();
        World world = entityLocation.getWorld();
        ItemStack itemInUse = killer.getInventory().getItemInMainHand();

        Event customEvent;

        // Determine whether the killed entity is a player
        if (entity instanceof Player) {
            customEvent = new Event("player:kill")
                    .setMetadata("playerName", killer.getName())
                    .setMetadata("playerUuid", killer.getUniqueId().toString())
                    .setMetadata("victimName", entity.getName())
                    .setMetadata("victimUuid", entity.getUniqueId().toString())
                    .setMetadata("location.x", entityLocation.getBlockX())
                    .setMetadata("location.y", entityLocation.getBlockY())
                    .setMetadata("location.z", entityLocation.getBlockZ())
                    .setMetadata("location.world", world == null ? "UNKNOWN" : world.getName())
                    .setMetadata("causeOfDeath", causeOfDeath);

        } else {
            // Handling the death of non-player entities (e.g., mobs)
            customEvent = new Event("mob:kill")
                    .setMetadata("playerName", killer.getName())
                    .setMetadata("playerUuid", killer.getUniqueId().toString())
                    .setMetadata("mobType", entity.getType().name())
                    .setMetadata("location.x", entityLocation.getBlockX())
                    .setMetadata("location.y", entityLocation.getBlockY())
                    .setMetadata("location.z", entityLocation.getBlockZ())
                    .setMetadata("location.world", world == null ? "UNKNOWN" : world.getName())
                    .setMetadata("causeOfDeath", causeOfDeath);

        }

        if (itemInUse.getType() == Material.AIR) {
            customEvent.setMetadata("weaponUsed", "hand");
        } else {
            convertItemStackToEvent(customEvent, "weaponUsed", itemInUse);
        }

        plugin.addEvent(customEvent);
    }
}

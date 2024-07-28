package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import static mc.play.stats.util.ItemStackSerializer.convertItemStackToEvent;

public class BlockListeners implements Listener {
    private final PlayerStatsPlugin plugin;

    public BlockListeners(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        Player player = event.getPlayer();

        Event blockPlaceEvent = new Event("block:place")
                .setMetadata("blockType", block.getType().toString())
                .setMetadata("location.x", blockLocation.getBlockX())
                .setMetadata("location.y", blockLocation.getBlockY())
                .setMetadata("location.z", blockLocation.getBlockZ())
                .setMetadata("location.world", block.getWorld().getName());

        plugin.triggerEvent(blockPlaceEvent, player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        Player player = event.getPlayer();
        ItemStack itemInUse = player.getInventory().getItemInMainHand();

        Event blockBreakEvent = new Event("block:break")
                .setMetadata("blockType", block.getType().toString())
                .setMetadata("location.x", blockLocation.getBlockX())
                .setMetadata("location.y", blockLocation.getBlockY())
                .setMetadata("location.z", blockLocation.getBlockZ())
                .setMetadata("location.world", block.getWorld().getName())
                .setMetadata("expDrop", event.getExpToDrop());

        if (itemInUse.getType() == Material.AIR) {
            blockBreakEvent.setMetadata("brokenBy", "hand");
        } else {
            convertItemStackToEvent(blockBreakEvent, "brokenBy", itemInUse);
        }

        plugin.triggerEvent(blockBreakEvent, player);
    }
}

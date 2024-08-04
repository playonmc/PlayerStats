package mc.play.stats.listener;

import com.jeff_media.customblockdata.CustomBlockData;
import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

import static mc.play.stats.util.BlockExcludeUtil.SKIP_BLOCKS;

public class BlockListeners implements Listener {
    private final PlayerStatsPlugin plugin;

    public BlockListeners(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    private void handleBlockEvent(Player player, Block block, String action, BlockBreakEvent breakEvent) {
        if (SKIP_BLOCKS.contains(block.getType())) {
            return;
        }

        UUID playerUUID = player.getUniqueId();
        String blockKeyStr = "stats_%s_%s_%s";
        String blockKeyString = String.format(blockKeyStr, playerUUID, action, block.getType());
        NamespacedKey blockKey = new NamespacedKey(plugin, blockKeyString);
        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);

        boolean hasBeenHandled = customBlockData.getOrDefault(blockKey, PersistentDataType.BOOLEAN, false);

        if (hasBeenHandled) {
            return;
        }

        customBlockData.set(blockKey, PersistentDataType.BOOLEAN, true);

        if (action.equals("place")) {
            Event blockPlaceEvent = new Event("block:place")
                    .setMetadata("blockType", block.getType().toString())
                    .setMetadata("world", player.getWorld().getName());
            plugin.triggerEvent(blockPlaceEvent, player);
        } else if (action.equals("break") && breakEvent != null) {
            String blockType = player.getInventory().getItemInMainHand().getType() == Material.AIR ? "hand" : player.getInventory().getItemInMainHand().getType().name();
            Event blockBreakEvent = new Event("block:break")
                    .setMetadata("blockType", block.getType().toString())
                    .setMetadata("brokenBy", blockType.toUpperCase())
                    .setMetadata("world", player.getWorld().getName());

            int expDrop = breakEvent.getExpToDrop();
            if (expDrop > 0) {
                blockBreakEvent.setMetadata("expDrop", expDrop);
            }

            plugin.triggerEvent(blockBreakEvent, player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        handleBlockEvent(event.getPlayer(), event.getBlock(), "place", null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        handleBlockEvent(event.getPlayer(), event.getBlock(), "break", event);
    }
}

package mc.play.stats.listener;

import com.jeff_media.customblockdata.CustomBlockData;
import io.papermc.paper.persistence.PersistentDataContainerView;
import mc.play.stats.PlayerStatsPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockListeners implements Listener {
    private final PlayerStatsPlugin plugin;
    private final NamespacedKey blockPlaceKey;
    private final NamespacedKey blockBreakKey;

    public BlockListeners(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
        this.blockPlaceKey = new NamespacedKey(plugin, "stats_ignore_place");
        this.blockBreakKey = new NamespacedKey(plugin, "stats_ignore_break");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        ItemStack itemInHand = event.getItemInHand();
        Player player = event.getPlayer();

        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
        PersistentDataContainerView persistentDataContainer = itemInHand.getPersistentDataContainer();

        boolean hasBeenPlaced = customBlockData.has(blockPlaceKey, PersistentDataType.BOOLEAN) || persistentDataContainer.has(blockPlaceKey, PersistentDataType.BOOLEAN);

        if(! customBlockData.has(blockBreakKey, PersistentDataType.BOOLEAN) && persistentDataContainer.has(blockBreakKey, PersistentDataType.BOOLEAN)) {
            customBlockData.set(blockBreakKey, PersistentDataType.BOOLEAN, true);
        }

        /*
            On place, check if this block has already been placed before.
            If it has, we won't track the stats for it again.
         */
        if (hasBeenPlaced) {
            player.sendMessage("You have already placed this block before. Ignoring.");
            return;
        }

        // It hasn't been placed before, so we set the tracking data.
        customBlockData.set(blockPlaceKey, PersistentDataType.BOOLEAN, true);
        player.sendMessage("You have placed a block for the first time. Tracking.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);

        /*
            On break, check if this block has already been broken before.
            If it has, we won't track the stats for it again.
         */
        if (customBlockData.has(blockBreakKey, PersistentDataType.BOOLEAN)) {
            player.sendMessage("You have already broken this block before. Ignoring.");
            return;
        }

        // It hasn't been broken before, so we set the tracking data.
        customBlockData.set(blockBreakKey, PersistentDataType.BOOLEAN, true);
        player.sendMessage("You have broken a block for the first time. Tracking.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockItemDrop(BlockDropItemEvent event) {
        Block block = event.getBlock();

        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
        boolean hasBeenPlaced = customBlockData.has(blockPlaceKey, PersistentDataType.BOOLEAN);

        // add tracking data to the dropped item
        event.getItems().forEach(item -> {
            ItemStack itemStack = item.getItemStack();
            ItemMeta meta = itemStack.getItemMeta();

            // As this event only triggers when a block is broken, we can safely set the break tracking data.
            meta.getPersistentDataContainer().set(blockBreakKey, PersistentDataType.BOOLEAN, true);

            if(hasBeenPlaced) {
                // If the block has been placed before, we set the place tracking data.
                meta.getPersistentDataContainer().set(blockPlaceKey, PersistentDataType.BOOLEAN, true);
            }

            itemStack.setItemMeta(meta);
        });
    }
}
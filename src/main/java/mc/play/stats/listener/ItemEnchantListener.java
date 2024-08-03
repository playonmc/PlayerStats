package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class ItemEnchantListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public ItemEnchantListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemEnchant(EnchantItemEvent event) {
        Player player = event.getEnchanter();

        Event itemEnchantEvent = new Event("player:item_enchant")
                .setMetadata("item", event.getItem().getType().toString())
                .setMetadata("enchantments", event.getEnchantsToAdd().toString())
                .setMetadata("world", player.getWorld().getName());
        plugin.triggerEvent(itemEnchantEvent, player);
    }
}

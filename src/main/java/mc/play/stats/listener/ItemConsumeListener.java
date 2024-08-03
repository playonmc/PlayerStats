package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ItemConsumeListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public ItemConsumeListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        String effectGained = event.getItem().getItemMeta() != null && event.getItem().getItemMeta().hasDisplayName() ? event.getItem().getItemMeta().getDisplayName() : "none";

        Event consumeEvent = new Event("player:eat")
                .setMetadata("item", event.getItem().getType().toString())
                .setMetadata("effectGained", effectGained)
                .setMetadata("world", player.getWorld().getName());

        plugin.triggerEvent(consumeEvent, player);
    }
}

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

        Event consumeEvent = new Event("player:eat")
                .setMetadata("item", event.getItem().getType().toString());

        plugin.triggerEvent(consumeEvent, player);
    }
}

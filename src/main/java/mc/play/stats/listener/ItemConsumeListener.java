package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import static mc.play.stats.util.ItemStackSerializer.convertItemStackToEvent;

public class ItemConsumeListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public ItemConsumeListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        String effectGained = event.getItem().getItemMeta() != null && event.getItem().getItemMeta().hasDisplayName() ? event.getItem().getItemMeta().getDisplayName() : "none";

        Event consumeEvent = new Event("player:eat")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("item", event.getItem().getType().toString())
                .setMetadata("world", player.getWorld().getName())
                .setMetadata("effectGained", effectGained);

        if (event.getItem().getAmount() > 1) {
            convertItemStackToEvent(consumeEvent, "remainingItem", event.getItem());
        } else {
            consumeEvent.setMetadata("remainingItem", "NONE");
        }

        plugin.addEvent(consumeEvent);
    }
}

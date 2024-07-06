package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public AdvancementListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        Event advancementEvent = new Event("player:advancement")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("advancement", event.getAdvancement().getKey().toString());
        
        plugin.addEvent(advancementEvent);
    }
}

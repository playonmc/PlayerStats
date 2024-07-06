package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class LevelUpListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public LevelUpListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();
        Event levelChangeEvent = new Event("player:level_change")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("oldLevel", event.getOldLevel())
                .setMetadata("newLevel", event.getNewLevel());
        plugin.addEvent(levelChangeEvent);
    }
}
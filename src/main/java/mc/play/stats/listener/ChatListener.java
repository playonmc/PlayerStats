package mc.play.stats.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public ChatListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        Event chatEvent = new Event("player:chat")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString());

        plugin.triggerEvent(chatEvent, player);
    }
}

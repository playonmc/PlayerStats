package mc.play.stats.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    private final PlayerStatsPlugin plugin;
    private final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

    public ChatListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        String message = plainTextComponentSerializer.serialize(event.message());
        Event chatEvent = new Event("player:chat")
                .setMetadata("words", message.split(" ").length)
                .setMetadata("world", player.getWorld().getName());

        plugin.triggerEvent(chatEvent, player);
    }
}

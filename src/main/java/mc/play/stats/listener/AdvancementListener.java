package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {
    private final PlayerStatsPlugin plugin;
    private final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

    public AdvancementListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();

        if(advancement.getDisplay() == null) {
            return;
        }

        Event advancementEvent = new Event("player:advancement")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("advancement", plainTextComponentSerializer.serialize(advancement.getDisplay().title()));
        
        plugin.addEvent(advancementEvent);
    }
}

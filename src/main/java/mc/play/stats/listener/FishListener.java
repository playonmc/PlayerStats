package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public FishListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        String caughtEntity = "none";
        String caughtItem = "none";

        if (event.getCaught() != null) {
            caughtEntity = event.getCaught().getType().toString();
            if (event.getCaught() instanceof Item) {
                caughtItem = ((Item) event.getCaught()).getItemStack().getType().toString();
            }
        }

        Event fishEvent = new Event("player:fish")
                .setMetadata("playerName", player.getName())
                .setMetadata("playerUuid", player.getUniqueId().toString())
                .setMetadata("state", event.getState().toString())
                .setMetadata("caughtEntity", caughtEntity)
                .setMetadata("caughtItem", caughtItem);
        plugin.addEvent(fishEvent);
    }
}

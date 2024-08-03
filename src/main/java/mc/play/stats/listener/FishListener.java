package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public FishListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        String caughtEntity = "none";
        String caughtItem = "none";

        Entity caught = event.getCaught();
        if (caught != null) {
            caughtEntity = caught.getType().toString();

            if (caught instanceof Item item) {
                caughtItem = item.getType().toString();
            }
        }

        Event fishEvent = new Event("player:fish")
                .setMetadata("state", event.getState().toString())
                .setMetadata("caughtEntity", caughtEntity)
                .setMetadata("caughtItem", caughtItem);
        plugin.triggerEvent(fishEvent, player);
    }
}

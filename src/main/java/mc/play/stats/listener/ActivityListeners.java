package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ActivityListeners implements Listener {
    private final PlayerStatsPlugin plugin;

    public ActivityListeners(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean firstJoin = !player.hasPlayedBefore();

        Event joinEvent = new Event("player:join")
                .setMetadata("lastJoined", System.currentTimeMillis());

        if (firstJoin) {
            joinEvent.setMetadata("firstJoin", true);
        }

        plugin.triggerEvent(joinEvent, player);
        plugin.getPlayerStatisticHeartbeatManager().addPlayer(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        long playTime = System.currentTimeMillis() - player.getLastLogin();

        Event quitEvent = new Event("player:quit")
                .setMetadata("lastJoined", System.currentTimeMillis())
                .setMetadata("playTime", playTime);

        plugin.triggerEvent(quitEvent, player);
        plugin.getPlayerStatisticHeartbeatManager().removePlayer(player.getUniqueId());
    }
}

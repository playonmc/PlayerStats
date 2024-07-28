package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.InetSocketAddress;

public class ActivityListeners implements Listener {
    private final PlayerStatsPlugin plugin;

    public ActivityListeners(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean firstJoin = !player.hasPlayedBefore();
        InetSocketAddress address = event.getPlayer().getAddress();
        String ipAddress = address == null ? "EMPTY" : address.getAddress().getHostAddress();

        Event joinEvent = new Event("player:join")
                .setMetadata("lastJoined", System.currentTimeMillis());

        if (firstJoin) {
            joinEvent.setMetadata("firstJoin", true);
        }

        plugin.triggerEvent(joinEvent, player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        long playTime = System.currentTimeMillis() - player.getLastPlayed();

        Event quitEvent = new Event("player:quit")
                .setMetadata("lastJoined", System.currentTimeMillis())
                .setMetadata("quitReason", ChatColor.stripColor(event.getQuitMessage()))
                .setMetadata("playTime", playTime);

        plugin.triggerEvent(quitEvent, player);
    }
}

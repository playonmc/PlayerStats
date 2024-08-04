package mc.play.stats.manager;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerStatisticHeartbeatManager {
    private static final int HEARTBEAT_INTERVAL = 30 * 20; // 30 seconds, assuming 20 ticks per second
    private final PlayerStatsPlugin plugin;
    private final List<UUID> players;
    private ScheduledTask task;

    public PlayerStatisticHeartbeatManager(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.start();
    }

    public void addPlayer(UUID uuid) {
        players.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        sendStatistics(uuid);
        players.remove(uuid);
    }

    public void start() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            addPlayer(player.getUniqueId());
        }

        task = plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin,
                scheduledTask -> {
                    int playerCount = players.size();

                    if (playerCount == 0) {
                        return;
                    }

                    for (UUID uuid : players) {
                        sendStatistics(uuid);
                    }
                },
                1,
                HEARTBEAT_INTERVAL);
    }

    public void stop() {
        if (task == null) return;
        task.cancel();
    }

    public void sendStatistics(UUID uuid) {
        Player bukkitPlayer = plugin.getServer().getPlayer(uuid);
        if (bukkitPlayer == null) {
            plugin.getLogger().warning("Player " + uuid + " is not online, cannot send statistics.");
            return;
        }

        Event playerEvent = new Event("player:update")
                .setMetadata("glide_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.AVIATE_ONE_CM) / 100.0)
                .setMetadata("walk_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.WALK_ONE_CM) / 100.0)
                .setMetadata("sprint_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.SPRINT_ONE_CM) / 100.0)
                .setMetadata("swim_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.SWIM_ONE_CM) / 100.0)
                .setMetadata("fall_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.FALL_ONE_CM) / 100.0)
                .setMetadata("fly_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.FLY_ONE_CM) / 100.0)
                .setMetadata("climb_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.CLIMB_ONE_CM) / 100.0)
                .setMetadata("dive_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.SWIM_ONE_CM) / 100.0)
                .setMetadata("minecart_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.MINECART_ONE_CM) / 100.0)
                .setMetadata("boat_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.BOAT_ONE_CM) / 100.0)
                .setMetadata("pig_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.PIG_ONE_CM) / 100.0)
                .setMetadata("horse_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.HORSE_ONE_CM) / 100.0)
                .setMetadata("strider_distance", bukkitPlayer.getStatistic(org.bukkit.Statistic.STRIDER_ONE_CM) / 100.0)
                .setMetadata("trident_thrown", bukkitPlayer.getStatistic(org.bukkit.Statistic.USE_ITEM, org.bukkit.Material.TRIDENT))
                .setMetadata("dispenser_inspected", bukkitPlayer.getStatistic(org.bukkit.Statistic.DISPENSER_INSPECTED))
                .setMetadata("dropper_inspected", bukkitPlayer.getStatistic(org.bukkit.Statistic.DROPPER_INSPECTED))
                .setMetadata("hopper_inspected", bukkitPlayer.getStatistic(org.bukkit.Statistic.HOPPER_INSPECTED))
                .setMetadata("item_enchanted", bukkitPlayer.getStatistic(org.bukkit.Statistic.ITEM_ENCHANTED))
                .setMetadata("furnace_interaction", bukkitPlayer.getStatistic(org.bukkit.Statistic.FURNACE_INTERACTION))
                .setMetadata("crafting_table_interaction", bukkitPlayer.getStatistic(org.bukkit.Statistic.CRAFTING_TABLE_INTERACTION))
                .setMetadata("chest_opened", bukkitPlayer.getStatistic(org.bukkit.Statistic.CHEST_OPENED))
                .setMetadata("trapped_chest_triggered", bukkitPlayer.getStatistic(org.bukkit.Statistic.TRAPPED_CHEST_TRIGGERED))
                .setMetadata("enderchest_opened", bukkitPlayer.getStatistic(org.bukkit.Statistic.ENDERCHEST_OPENED))
                .setMetadata("shulker_box_opened", bukkitPlayer.getStatistic(org.bukkit.Statistic.SHULKER_BOX_OPENED))
                .setMetadata("barrel_opened", bukkitPlayer.getStatistic(Statistic.OPEN_BARREL));

        plugin.triggerEvent(playerEvent, bukkitPlayer);
    }
}
package mc.play.stats;

import com.google.common.collect.Lists;
import mc.play.stats.http.SDK;
import mc.play.stats.listener.*;
import mc.play.stats.obj.Event;
import mc.play.stats.obj.EventDistanceInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PlayerStatsPlugin extends JavaPlugin {
    private final List<Event> events;
    private SDK sdk;
    private BukkitTask task;

    // Distance-traveled configuration
    private final Map<UUID, EventDistanceInfo> eventDistanceInfo;
    private final double MIN_DISTANCE_FLOWN = 15.0;
    private final double MIN_DISTANCE_RODE = 15.0;

    public PlayerStatsPlugin() {
        this.events = new ArrayList<>();
        this.eventDistanceInfo = new HashMap<>();
    }

    @Override
    public void onEnable() {

        sdk = new SDK("TO-BE-CHANGED");

        task = getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            List<Event> runEvents = Lists.newArrayList(events.subList(0, Math.min(events.size(), 250)));
            if (runEvents.isEmpty()) return;

            getLogger().info("Sending events..");
            getLogger().info(SDK.getGson().toJson(runEvents));

            sdk.sendEvents(runEvents)
                    .thenAccept(aVoid -> {
                        events.removeAll(runEvents);
                        getLogger().info("Successfully sent events.");
                    })
                    .exceptionally(throwable -> {
                        getLogger().info("Failed to send join events: " + throwable.getMessage());
                        return null;
                    });
        }, 0, 20 * 10);

        Arrays.asList(
                new ActivityListeners(this),
                new AdvancementListener(this),
                new BedListener(this),
                new BlockListeners(this),
                new ChatListener(this),
                new CommandListener(this),
                new DistanceListener(this),
                new FishListener(this),
                new ItemConsumeListener(this),
                new ItemEnchantListener(this),
                new ItemListeners(this),
                new LevelUpListener(this),
                new PlayerDamageListener(this),
                new PlayerDeathListener(this),
                new PlayerKillListener(this),
                new PortalListeners(this),
                new RespawnListener(this),
                new ShearListener(this),
                new CraftListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onDisable() {
        task.cancel();
        endAllCurrentRides();
    }

    public void triggerEvent(Event event, Player player) {
        event.setMetadata("playerName", player.getName());
        event.setMetadata("playerUuid", player.getUniqueId().toString());

        addEvent(event);
    }

    public void addEvent(Event event) {
        getLogger().info("Triggered event: " + event.toString());
        //events.add(event);
    }

    public Map<UUID, EventDistanceInfo> getEventDistanceInfo() {
        return eventDistanceInfo;
    }

    public double getMinDistanceFlown() {
        return MIN_DISTANCE_FLOWN;
    }

    public double getMinDistanceRode() {
        return MIN_DISTANCE_RODE;
    }

    private void endAllCurrentRides() {
        for (Map.Entry<UUID, EventDistanceInfo> entry : eventDistanceInfo.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                EventDistanceInfo eventInfo = entry.getValue();
                double distance = eventInfo.getStartLocation().distance(player.getLocation());
                long timeElapsed = System.currentTimeMillis() - eventInfo.getStartTime();
                double roundedDistance = Math.round(distance * 10000.0) / 10000.0;
                double speed = Math.round((roundedDistance / (timeElapsed / 1000.0)) * 10000.0) / 10000.0;

                if (eventInfo.getVehicleType() == null) { // Glide event
                    if (distance >= MIN_DISTANCE_FLOWN) {
                        Event glideEvent = new Event("player:glide")
                                .setMetadata("distance", roundedDistance)
                                .setMetadata("speed", speed)
                                .setMetadata("world", player.getWorld().getName());
                        triggerEvent(glideEvent, player);
                    }
                } else { // Rideable event
                    if (distance >= MIN_DISTANCE_RODE) {
                        Event rideEvent = new Event("player:ride")
                                .setMetadata("distance", roundedDistance)
                                .setMetadata("speed", speed)
                                .setMetadata("world", player.getWorld().getName())
                                .setMetadata("animal", eventInfo.getVehicleType().toString());
                        triggerEvent(rideEvent, player);
                    }
                }
            }
        }

        eventDistanceInfo.clear();
    }
}

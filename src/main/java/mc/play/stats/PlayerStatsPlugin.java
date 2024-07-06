package mc.play.stats;

import com.google.common.collect.Lists;
import mc.play.stats.http.SDK;
import mc.play.stats.listener.*;
import mc.play.stats.obj.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerStatsPlugin extends JavaPlugin {
    private List<Event> events;
    private SDK sdk;

    @Override
    public void onEnable() {
        events = new ArrayList<>();
        sdk = new SDK("TO-BE-CHANGED");

        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
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
                new FishListener(this),
                new ItemConsumeListener(this),
                new ItemEnchantListener(this),
                new ItemListeners(this),
                new KickListener(this),
                new LevelUpListener(this),
                new PlayerDamageListener(this),
                new PlayerDeathListener(this),
                new PlayerKillListener(this),
                new PortalListeners(this),
                new RespawnListener(this),
                new ShearListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public void addEvent(Event event) {
        getLogger().info("Triggered event: " + event.toString());
        events.add(event);
    }
}

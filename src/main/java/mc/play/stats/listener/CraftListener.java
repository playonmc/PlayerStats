package mc.play.stats.listener;

import mc.play.stats.PlayerStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {
    private final PlayerStatsPlugin plugin;

    public CraftListener(PlayerStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCraft(CraftItemEvent event) {
        if(event.getAction() == InventoryAction.NOTHING) {
            return;
        }

        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack result = event.getRecipe().getResult();

        Event craftEvent = new Event("player:craft")
                .setMetadata("item", result.getType().name());

        plugin.triggerEvent(craftEvent, player);
    }
}

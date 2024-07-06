package mc.play.stats.util;

import mc.play.stats.obj.Event;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Utility class for operations related to ItemStacks.
 */
public class ItemStackSerializer {

    /**
     * Converts a Bukkit ItemStack to an Event representation.
     * Serializes item type, amount, durability, custom names, lore, and enchantments.
     * @param event The event to use.
     * @param key The key to start with.
     * @param itemStack The ItemStack to convert.
     */
    public static void convertItemStackToEvent(Event event, String key, ItemStack itemStack) {
        event.setMetadata(key + ".item.type", itemStack.getType().name());
        event.setMetadata(key + ".item.amount", itemStack.getAmount());

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (meta instanceof Damageable) {
                int damage = ((Damageable) meta).getDamage();
                event.setMetadata(key + ".item.durability", itemStack.getType().getMaxDurability() - damage);
                event.setMetadata(key + ".item.damage", damage);
            }
            if (meta.hasDisplayName()) {
                event.setMetadata(key + ".item.displayName", meta.getDisplayName());
            }
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                if (lore != null) {
                    for (int i = 0; i < lore.size(); i++) {
                        event.setMetadata(key + ".item.lore." + i, lore.get(i));
                    }
                }
            }
            if (!itemStack.getEnchantments().isEmpty()) {
                itemStack.getEnchantments().forEach((enchant, level) -> {
                    NamespacedKey enchantKey = enchant.getKey();
                    event.setMetadata(key + ".item.enchantments." + enchantKey, level);
                });
            }
        }
    }

    /**
     * Converts an array of Bukkit ItemStacks to an Event representation.
     * Serializes item type, amount, durability, custom names, lore, and enchantments.
     * @param event The event to use.
     * @param key The key to start with.
     * @param items The array of ItemStacks to convert.
     */
    public static void convertItemStackArrayToEvent(Event event, String key, ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                convertItemStackToEvent(event, key + "." + i, items[i]);
            }
        }
    }
}
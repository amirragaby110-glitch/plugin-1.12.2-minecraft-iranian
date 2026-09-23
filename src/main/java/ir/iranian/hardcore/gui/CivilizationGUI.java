package ir.iranian.hardcore.gui;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.civilization.CivilizationTier;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Persian Civilization & Empire Progression GUI.
 */
public class CivilizationGUI implements Listener {

    private final IranianHardcorePlugin plugin;

    public CivilizationGUI(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openGUI(Player player) {
        if (plugin.getCivilizationManager() == null) return;
        CivilizationTier current = plugin.getCivilizationManager().getPlayerTier(player);
        CivilizationTier next = current.getNextTier();

        Inventory inv = Bukkit.createInventory(null, 27, MessageUtils.color("&8[Tamaddon] &6Pishraft-e Shahanshahi"));

        // Current status (Slot 11)
        List<String> curLore = new ArrayList<>();
        curLore.add(MessageUtils.color("&7" + current.getDescription()));
        curLore.add(MessageUtils.color("&8------------------------"));
        curLore.add(MessageUtils.color("&eZarfiat-e Artesh: &a" + current.getMaxSoldiers() + " Sarbaz"));
        inv.setItem(11, createItem(Material.EMERALD_BLOCK, "&a&lMartabeh-ye Feli: &e" + current.getDisplayName(), curLore));

        // Next Tier / Upgrade Button (Slot 15)
        if (next != null) {
            long bal = (plugin.getCoinManager() != null) ? plugin.getCoinManager().getBalance(player) : 0;
            int soldiers = (plugin.getArmyManager() != null && plugin.getArmyManager().getArmy(player) != null) ? plugin.getArmyManager().getArmy(player).getSize() : 0;
            int bosses = plugin.getCivilizationManager().getBossKills(player);

            List<String> nextLore = new ArrayList<>();
            nextLore.add(MessageUtils.color("&7" + next.getDescription()));
            nextLore.add(MessageUtils.color("&8------------------------"));
            nextLore.add(MessageUtils.color("&eZarfiat-e Jadid: &a" + next.getMaxSoldiers() + " Sarbaz"));
            nextLore.add(MessageUtils.color("&7Sharayet-e Ertegha:"));
            nextLore.add(MessageUtils.color("  &7- Sekkeh: " + (bal >= next.getRequiredCoins() ? "&a" : "&c") + bal + " &7/ &e" + next.getRequiredCoins() + " Derik"));
            nextLore.add(MessageUtils.color("  &7- Sarbazan: " + (soldiers >= next.getRequiredSoldiers() ? "&a" : "&c") + soldiers + " &7/ &e" + next.getRequiredSoldiers()));
            nextLore.add(MessageUtils.color("  &7- Boss Kills: " + (bosses >= next.getRequiredBosses() ? "&a" : "&c") + bosses + " &7/ &e" + next.getRequiredBosses()));
            nextLore.add(MessageUtils.color("&8------------------------"));

            boolean canUpgrade = plugin.getCivilizationManager().canUpgrade(player);
            if (canUpgrade) {
                nextLore.add(MessageUtils.color("&a&l[Click] Baraye Ertegha be in Martabeh!"));
                inv.setItem(15, createItem(Material.GOLD_BLOCK, "&6&lErtegha be: &e" + next.getDisplayName(), nextLore));
            } else {
                nextLore.add(MessageUtils.color("&cSharayet-e ertegha kamel nist!"));
                inv.setItem(15, createItem(Material.COAL_BLOCK, "&7Ertegha be: &c" + next.getDisplayName(), nextLore));
            }
        } else {
            List<String> maxLore = new ArrayList<>();
            maxLore.add(MessageUtils.color("&6Shoma be balatarin martabeh (Shahanshahi-ye Bozorg) rasideh-id!"));
            inv.setItem(15, createItem(Material.NETHER_STAR, "&6&lOwj-e Ghodrat-e Shahanshahi", maxLore));
        }

        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains("Pishraft-e Shahanshahi")) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (event.getRawSlot() == 15) {
            if (plugin.getCivilizationManager() != null && plugin.getCivilizationManager().canUpgrade(player)) {
                plugin.getCivilizationManager().upgradeTier(player);
                openGUI(player);
            }
        }
    }

    private ItemStack createItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color(name));
            if (lore != null) meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}

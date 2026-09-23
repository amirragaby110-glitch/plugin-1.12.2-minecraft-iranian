package ir.iranian.hardcore.gui;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.calendar.PersianCalendar;
import ir.iranian.hardcore.calendar.PersianDate;
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
 * Persian Solar Calendar & Festivals GUI.
 */
public class CalendarGUI implements Listener {

    private final IranianHardcorePlugin plugin;

    public CalendarGUI(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openGUI(Player player) {
        PersianDate date = PersianCalendar.now();
        Inventory inv = Bukkit.createInventory(null, 27, MessageUtils.color("&8[Tarikh] &6Taghvim-e Khorshidi"));

        // Central Current Date Clock
        List<String> nowLore = new ArrayList<>();
        nowLore.add(MessageUtils.color("&7Sal-e Khorshidi: &e" + date.getYear()));
        nowLore.add(MessageUtils.color("&7Mah: &b" + date.getMonthName() + " (" + date.getMonth() + ")"));
        nowLore.add(MessageUtils.color("&7Rooz: &a" + date.getDay()));
        nowLore.add(MessageUtils.color("&7Saat: &f" + date.toTimeString()));
        nowLore.add(MessageUtils.color("&7Sal-e Kabiseh: " + (date.isLeapYear() ? "&aBale (366 Rooz)" : "&7Kheyr (365 Rooz)")));
        if (date.hasFestival()) {
            nowLore.add(MessageUtils.color("&6&lJashn-e Emrooz: &e" + date.getFestival()));
        }
        inv.setItem(13, createItem(Material.WATCH, "&6&l" + date.toFormalString(), nowLore));

        // Festivals
        inv.setItem(10, createItem(Material.DOUBLE_PLANT, "&a&l1 Farvardin: Nowruz", makeFestivalLore("Jashn-e sal-e no va farvardin-egan", "Speed & Regeneration")));
        inv.setItem(11, createItem(Material.ARROW, "&b&l10 Tir: Tirgan", makeFestivalLore("Tir-e Arash-e Kamangir va setayesh-e baran", "Arrow Damage Boost")));
        inv.setItem(12, createItem(Material.GOLD_BLOCK, "&e&l16 Mehr: Mehregan", makeFestivalLore("Jashn-e pirouzi-ye Kaveh va Fereydoon", "Strength Boost")));
        inv.setItem(14, createItem(Material.FIREWORK, "&d&l30 Azar: Shab-e Yalda", makeFestivalLore("Zayesh-e Khorshid va boland-tarin shab", "Night Vision & Saturation")));
        inv.setItem(15, createItem(Material.TORCH, "&c&l10 Bahman: Jashn-e Sadeh", makeFestivalLore("Jashn-e kashf-e atash tavasot-e Houshang Shah", "Fire Resistance")));
        inv.setItem(16, createItem(Material.RED_ROSE, "&4&l5 Esfand: Sepandarmazgan", makeFestivalLore("Rooz-e zamin, eshgh va paki-ye zan-e Irani", "Health Regen Boost")));

        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0f, 1.2f);
    }

    private List<String> makeFestivalLore(String desc, String buff) {
        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.color("&7" + desc));
        lore.add(MessageUtils.color("&eBarakat dar bazi: &a" + buff));
        return lore;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("Taghvim-e Khorshidi")) {
            event.setCancelled(true);
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

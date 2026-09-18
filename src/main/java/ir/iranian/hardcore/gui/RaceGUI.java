package ir.iranian.hardcore.gui;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.race.RaceType;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * منوی GUI انتخاب قوم ایرانی - نسخه 3.5 - هر بایوم یک قوم
 * 54 اسلات (6 ردیف) - 14 قوم ایرانی + اطلاعات
 * کاملا فارسی - با تاریخ و فرهنگ هر قوم
 */
public class RaceGUI implements Listener {

    private final IranianHardcorePlugin plugin;
    private final String guiTitle;

    public RaceGUI(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        this.guiTitle = MessageUtils.color(plugin.getConfigManager().getString("messages.race-gui-title", "&8&l🇮🇷 انتخاب قوم ایرانی - اقوام ایران"));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openRaceGUI(Player player) {
        // 54 اسلات برای 14 قوم
        Inventory inv = Bukkit.createInventory(null, 54, guiTitle);

        RaceType[] races = RaceType.values();
        // جایگاه‌های زیبا برای 14 قوم
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29};

        for (int i = 0; i < races.length && i < slots.length; i++) {
            RaceType race = races[i];
            ItemStack item = createRaceItem(race, player);
            inv.setItem(slots[i], item);
        }

        // آیتم اطلاعات در وسط
        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta meta = info.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&e&l📚 راهنمای اقوام ایرانی"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7ایران متشکل از اقوام مختلف با فرهنگ غنی است",
                "&7هر قوم قدرت خاص در بایوم و سرزمین خود دارد",
                "&7و در بایوم دشمن ضعیف می‌شود",
                "",
                "&6&lاقوام ایرانی:",
                "&7• فارس - پارس - تخت جمشید",
                "&7• آذری - آذربایجان - بابک خرمدین",
                "&7• کرد - زاگرس - عقاب",
                "&7• لر - لرستان - شیر",
                "&7• بلوچ - بلوچستان - کویر",
                "&7• عرب خوزستان - کارون",
                "&7• ترکمن - اسب ترکمن",
                "&7• گیلک - گیلان - باران",
                "&7• مازنی - مازندران - طبری",
                "&7• بختیاری - کوچ‌نشین",
                "&7• قشقایی - فرش قشقایی",
                "&7• بندری - خلیج فارس - دریانورد",
                "",
                "&eبرای انتخاب قوم ایرانی روی آن کلیک کنید",
                "&cتغییر قوم فقط هر 7 روز ممکن است!",
                "&6🇮🇷 زنده باد ایران - اقوام ایرانی"
        )));
        info.setItemMeta(meta);
        inv.setItem(4, info);

        // پرچم ایران وسط
        ItemStack flag = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta flagMeta = flag.getItemMeta();
        flagMeta.setDisplayName(MessageUtils.color("&c&l🇮🇷 پرچم ایران"));
        flagMeta.setLore(MessageUtils.color(Arrays.asList(
                "&7ایران - مهد تمدن 7000 ساله",
                "&7اقوام مختلف، یک ملت واحد",
                "",
                "&a&lشعار: &fزنده باد ایران!",
                "&b&lخلیج همیشه فارس!"
        )));
        flag.setItemMeta(flagMeta);
        inv.setItem(49, flag);

        // اطلاعات قوم فعلی
        RaceType current = plugin.getRaceManager().getRace(player);
        if (current != null) {
            ItemStack currentItem = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta currentMeta = currentItem.getItemMeta();
            currentMeta.setDisplayName(MessageUtils.color("&6&lقوم فعلی شما: " + current.getPersianName()));
            currentMeta.setLore(MessageUtils.color(Arrays.asList(
                    "&7شما عضو قوم &6" + current.getPersianName() + " &7هستید",
                    "&7بایوم فعلی: &f" + player.getLocation().getBlock().getBiome().name(),
                    "&7قدرت: " + (current.isHomeBiome(player.getLocation().getBlock().getBiome()) ? "&aکامل" : "&cضعیف")
            )));
            currentItem.setItemMeta(currentMeta);
            inv.setItem(53, currentItem);
        }

        // شیشه تزئینی ایرانی - سبز، سفید، قرمز
        ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta greenMeta = greenGlass.getItemMeta();
        greenMeta.setDisplayName(MessageUtils.color("&aسبز - طبیعت ایران"));
        greenGlass.setItemMeta(greenMeta);

        ItemStack whiteGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
        ItemMeta whiteMeta = whiteGlass.getItemMeta();
        whiteMeta.setDisplayName(MessageUtils.color("&fسفید - صلح ایران"));
        whiteGlass.setItemMeta(whiteMeta);

        ItemStack redGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta redMeta = redGlass.getItemMeta();
        redMeta.setDisplayName(MessageUtils.color("&cقرمز - خون شهدای ایران"));
        redGlass.setItemMeta(redMeta);

        for (int i = 0; i < 54; i++) {
            if (inv.getItem(i) == null) {
                if (i % 3 == 0) inv.setItem(i, greenGlass);
                else if (i % 3 == 1) inv.setItem(i, whiteGlass);
                else inv.setItem(i, redGlass);
            }
        }

        // بازنویسی جای قوم‌ها که شیشه نشود
        for (int i = 0; i < races.length && i < slots.length; i++) {
            RaceType race = races[i];
            ItemStack item = createRaceItem(race, player);
            inv.setItem(slots[i], item);
        }
        inv.setItem(4, info);
        inv.setItem(49, flag);
        if (current != null) {
            ItemStack currentItem = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta currentMeta = currentItem.getItemMeta();
            currentMeta.setDisplayName(MessageUtils.color("&6&lقوم فعلی شما: " + current.getPersianName()));
            currentItem.setItemMeta(currentMeta);
            inv.setItem(53, currentItem);
        }

        player.openInventory(inv);
        player.sendMessage(MessageUtils.withPrefix("&aمنوی انتخاب قوم ایرانی باز شد! &7یک قوم از اقوام اصیل ایران را انتخاب کنید"));
    }

    private ItemStack createRaceItem(RaceType race, Player player) {
        ItemStack item = new ItemStack(race.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&6&l" + race.getPersianName()));

        List<String> lore = new ArrayList<>();
        for (String line : race.getLore()) {
            lore.add(MessageUtils.color(line));
        }
        lore.add("");
        lore.add(MessageUtils.color("&7بایوم‌های خانه (قدرت کامل): &a" + race.getHomeBiomes().size()));
        lore.add(MessageUtils.color("&7بایوم‌های دشمن (ضعف): &c" + race.getHostileBiomes().size()));
        lore.add("");
        lore.add(MessageUtils.color("&e&l» برای انتخاب این قوم ایرانی کلیک کنید"));

        RaceType current = plugin.getRaceManager().getRace(player);
        if (current != null && current == race) {
            lore.add(MessageUtils.color("&a&l✔ قوم فعلی شما - ایرانی اصیل"));
        } else {
            lore.add(MessageUtils.color("&7&lکلیک کنید تا عضو این قوم شوید"));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().equals(guiTitle)) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        if (clicked.getType() == Material.STAINED_GLASS_PANE || clicked.getType() == Material.BOOK || clicked.getType() == Material.GOLD_BLOCK) {
            if (clicked.getType() == Material.BOOK || clicked.getType() == Material.GOLD_BLOCK) return;
            if (clicked.getType() == Material.STAINED_GLASS_PANE) return;
        }

        RaceType selected = null;
        for (RaceType race : RaceType.values()) {
            if (clicked.getType() == race.getIcon()) {
                if (clicked.getItemMeta() != null && clicked.getItemMeta().getDisplayName() != null) {
                    String display = clicked.getItemMeta().getDisplayName();
                    if (display.contains(race.getPersianName()) || display.contains(race.getEnglishName()) || display.contains(race.getId())) {
                        selected = race;
                        break;
                    }
                }
            }
        }

        if (selected == null) {
            int slot = event.getSlot();
            int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29};
            RaceType[] races = RaceType.values();
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] == slot && i < races.length) {
                    selected = races[i];
                    break;
                }
            }
        }

        if (selected == null) return;

        RaceType current = plugin.getRaceManager().getRace(player);
        if (current != null) {
            if (!plugin.getRaceManager().canChangeRace(player)) {
                long remaining = plugin.getRaceManager().getRemainingDaysForRaceChange(player);
                String msg = plugin.getConfigManager().getString("race.change-cooldown-message", "&cباید %days% روز دیگر صبر کنید.");
                msg = msg.replace("%days%", String.valueOf(remaining));
                player.sendMessage(MessageUtils.withPrefix(msg));
                player.closeInventory();
                return;
            }
            if (current == selected) {
                player.sendMessage(MessageUtils.withPrefix("&eشما عضو همین قوم ایرانی هستید! &6" + current.getPersianName()));
                player.closeInventory();
                return;
            }
        }

        plugin.getRaceManager().setRace(player, selected);
        player.closeInventory();
    }
}

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
 * منوی GUI برای انتخاب نژاد
 * 27 اسلات (3 ردیف) - 8 نژاد + اطلاعات
 */
public class RaceGUI implements Listener {

    private final IranianHardcorePlugin plugin;
    private final String guiTitle;

    public RaceGUI(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        this.guiTitle = MessageUtils.color(plugin.getConfigManager().getString("messages.race-gui-title", "&8&lانتخاب نژاد"));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * باز کردن GUI انتخاب نژاد
     */
    public void openRaceGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, guiTitle);

        // چیدن نژادها در GUI
        RaceType[] races = RaceType.values();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 22}; // جایگاه‌ها

        for (int i = 0; i < races.length && i < slots.length; i++) {
            RaceType race = races[i];
            ItemStack item = createRaceItem(race, player);
            inv.setItem(slots[i], item);
        }

        // آیتم اطلاعات در وسط پایین
        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta meta = info.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&e&lراهنما"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7هر نژاد قدرت خاص در بایوم خود دارد",
                "&7و در بایوم دشمن ضعیف می‌شود",
                "",
                "&eبرای انتخاب روی نژاد کلیک کنید",
                "&cتغییر نژاد فقط هر 7 روز ممکن است!"
        )));
        info.setItemMeta(meta);
        inv.setItem(4, info);

        // شیشه تزئینی
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        for (int i = 0; i < 27; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }

        player.openInventory(inv);
    }

    private ItemStack createRaceItem(RaceType race, Player player) {
        ItemStack item = new ItemStack(race.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&6&l" + race.getPersianName() + " &7(" + race.getEnglishName() + ")"));

        List<String> lore = new ArrayList<>();
        for (String line : race.getLore()) {
            lore.add(MessageUtils.color(line));
        }
        lore.add("");
        lore.add(MessageUtils.color("&7بایوم‌های خانه: &a" + race.getHomeBiomes().size()));
        lore.add(MessageUtils.color("&7بایوم‌های دشمن: &c" + race.getHostileBiomes().size()));
        lore.add("");
        lore.add(MessageUtils.color("&e» برای انتخاب کلیک کنید"));

        // اگر بازیکن همین نژاد را دارد، نشان بده
        RaceType current = plugin.getRaceManager().getRace(player);
        if (current != null && current == race) {
            lore.add(MessageUtils.color("&a&l✔ نژاد فعلی شما"));
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
        if (clicked.getType() == Material.STAINED_GLASS_PANE || clicked.getType() == Material.BOOK) return;

        // پیدا کردن نژاد از روی آیکون و نام
        RaceType selected = null;
        for (RaceType race : RaceType.values()) {
            if (clicked.getType() == race.getIcon()) {
                // برای اطمینان بیشتر، نام را هم چک کن
                if (clicked.getItemMeta() != null && clicked.getItemMeta().getDisplayName() != null) {
                    String display = clicked.getItemMeta().getDisplayName();
                    if (display.contains(race.getPersianName()) || display.contains(race.getEnglishName())) {
                        selected = race;
                        break;
                    }
                } else {
                    selected = race;
                    break;
                }
            }
        }

        // اگر با آیکون نشد، از اسلات پیدا کن
        if (selected == null) {
            int slot = event.getSlot();
            int[] slots = {10, 11, 12, 13, 14, 15, 16, 22};
            RaceType[] races = RaceType.values();
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] == slot && i < races.length) {
                    selected = races[i];
                    break;
                }
            }
        }

        if (selected == null) return;

        // چک کولدان
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
                player.sendMessage(MessageUtils.withPrefix("&eشما همین نژاد را دارید!"));
                player.closeInventory();
                return;
            }
        }

        // تنظیم نژاد
        plugin.getRaceManager().setRace(player, selected);
        player.closeInventory();

        // افکت انتخاب
        player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }
}

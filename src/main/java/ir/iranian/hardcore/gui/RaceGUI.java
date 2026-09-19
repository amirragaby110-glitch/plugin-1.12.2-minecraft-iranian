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
 * GUI Entekhab Ghome Irani - v4.0 Finglish
 * 54 slot (6 radif) - 14 ghom Irani + etelaat
 * Finglish - baraye khandane behtar dar client haye English
 */
public class RaceGUI implements Listener {

    private final IranianHardcorePlugin plugin;
    private final String guiTitle;

    public RaceGUI(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        boolean finglish = true;
        if (plugin.getLanguageManager() != null) {
            finglish = plugin.getLanguageManager().isFinglish();
        }
        String defaultTitle = finglish ? "&8&l🇮🇷 Entekhab Ghome Irani - Aghvam Iran" : "&8&l🇮🇷 انتخاب قوم ایرانی - اقوام ایران";
        this.guiTitle = MessageUtils.color(plugin.getConfigManager().getString("messages.race-gui-title", defaultTitle));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openRaceGUI(Player player) {
        boolean finglish = plugin.getLanguageManager() != null && plugin.getLanguageManager().isFinglish();
        Inventory inv = Bukkit.createInventory(null, 54, guiTitle);

        RaceType[] races = RaceType.values();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29};

        for (int i = 0; i < races.length && i < slots.length; i++) {
            RaceType race = races[i];
            ItemStack item = createRaceItem(race, player, finglish);
            inv.setItem(slots[i], item);
        }

        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta meta = info.getItemMeta();
        if (finglish) {
            meta.setDisplayName(MessageUtils.color("&e&l📚 Rahnama Aghvam Irani"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Iran moteshakel az aghvam mokhtalef ba farhang ghani ast",
                    "&7Har ghom ghodrat khas dar biome va sarzamin khod darad",
                    "&7Va dar biome doshman zaeef mishavad",
                    "",
                    "&6&lAghvam Irani:",
                    "&7• Fars - Pars - Takht Jamshid",
                    "&7• Azari - Azarbayjan - Babak Khorramdin",
                    "&7• Kord - Zagros - Oghab",
                    "&7• Lor - Lorestan - Shir",
                    "&7• Baloch - Balochestan - Kavir",
                    "&7• Arab Khoozestan - Karoon",
                    "&7• Torkaman - Asb Torkaman",
                    "&7• Gilak - Gilan - Baran",
                    "&7• Mazani - Mazandaran - Tabari",
                    "&7• Bakhtiari - Kooch-neshin",
                    "&7• Ghashghaei - Farsh Ghashghaei",
                    "&7• Bandari - Khalij Fars - Daryanavard",
                    "",
                    "&eBaraye entekhab ghome Irani rooye an click konid",
                    "&cTaghir ghom faghat har 7 rooz momken ast!",
                    "&6🇮🇷 Zende bad Iran - Aghvam Irani"
            )));
        } else {
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
        }
        info.setItemMeta(meta);
        inv.setItem(4, info);

        ItemStack flag = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta flagMeta = flag.getItemMeta();
        if (finglish) {
            flagMeta.setDisplayName(MessageUtils.color("&c&l🇮🇷 Parcham Iran"));
            flagMeta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Iran - Mahd tamadon 7000 sale",
                    "&7Aghvam mokhtalef, yek mellat vahed",
                    "",
                    "&a&lShoar: &fZende bad Iran!",
                    "&b&lKhalij hameshe Fars!"
            )));
        } else {
            flagMeta.setDisplayName(MessageUtils.color("&c&l🇮🇷 پرچم ایران"));
            flagMeta.setLore(MessageUtils.color(Arrays.asList(
                    "&7ایران - مهد تمدن 7000 ساله",
                    "&7اقوام مختلف، یک ملت واحد",
                    "",
                    "&a&lشعار: &fزنده باد ایران!",
                    "&b&lخلیج همیشه فارس!"
            )));
        }
        flag.setItemMeta(flagMeta);
        inv.setItem(49, flag);

        RaceType current = plugin.getRaceManager().getRace(player);
        if (current != null) {
            ItemStack currentItem = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta currentMeta = currentItem.getItemMeta();
            String name = finglish ? current.getFinglishName() : current.getPersianName();
            if (finglish) {
                currentMeta.setDisplayName(MessageUtils.color("&6&lGhom feli shoma: " + name));
                currentMeta.setLore(MessageUtils.color(Arrays.asList(
                        "&7Shoma ozv ghom &6" + name + " &7hastid",
                        "&7Biome feli: &f" + player.getLocation().getBlock().getBiome().name(),
                        "&7Ghodrat: " + (current.isHomeBiome(player.getLocation().getBlock().getBiome()) ? "&aKamel" : "&cZaeef")
                )));
            } else {
                currentMeta.setDisplayName(MessageUtils.color("&6&lقوم فعلی شما: " + name));
                currentMeta.setLore(MessageUtils.color(Arrays.asList(
                        "&7شما عضو قوم &6" + name + " &7هستید",
                        "&7بایوم فعلی: &f" + player.getLocation().getBlock().getBiome().name(),
                        "&7قدرت: " + (current.isHomeBiome(player.getLocation().getBlock().getBiome()) ? "&aکامل" : "&cضعیف")
                )));
            }
            currentItem.setItemMeta(currentMeta);
            inv.setItem(53, currentItem);
        }

        ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta greenMeta = greenGlass.getItemMeta();
        greenMeta.setDisplayName(MessageUtils.color(finglish ? "&aSabz - Tabiat Iran" : "&aسبز - طبیعت ایران"));
        greenGlass.setItemMeta(greenMeta);

        ItemStack whiteGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
        ItemMeta whiteMeta = whiteGlass.getItemMeta();
        whiteMeta.setDisplayName(MessageUtils.color(finglish ? "&fSefid - Solh Iran" : "&fسفید - صلح ایران"));
        whiteGlass.setItemMeta(whiteMeta);

        ItemStack redGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta redMeta = redGlass.getItemMeta();
        redMeta.setDisplayName(MessageUtils.color(finglish ? "&cGhermez - Khoon Shohaday Iran" : "&cقرمز - خون شهدای ایران"));
        redGlass.setItemMeta(redMeta);

        for (int i = 0; i < 54; i++) {
            if (inv.getItem(i) == null) {
                if (i % 3 == 0) inv.setItem(i, greenGlass);
                else if (i % 3 == 1) inv.setItem(i, whiteGlass);
                else inv.setItem(i, redGlass);
            }
        }

        for (int i = 0; i < races.length && i < slots.length; i++) {
            RaceType race = races[i];
            ItemStack item = createRaceItem(race, player, finglish);
            inv.setItem(slots[i], item);
        }
        inv.setItem(4, info);
        inv.setItem(49, flag);
        if (current != null) {
            ItemStack currentItem = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta currentMeta = currentItem.getItemMeta();
            String name = finglish ? current.getFinglishName() : current.getPersianName();
            currentMeta.setDisplayName(MessageUtils.color(finglish ? "&6&lGhom feli shoma: " + name : "&6&lقوم فعلی شما: " + name));
            currentItem.setItemMeta(currentMeta);
            inv.setItem(53, currentItem);
        }

        player.openInventory(inv);
        if (finglish) {
            player.sendMessage(MessageUtils.withPrefix("&aMenu entekhab ghom Irani baz shod! &7Yek ghom az aghvam asil Iran ra entekhab konid"));
        } else {
            player.sendMessage(MessageUtils.withPrefix("&aمنوی انتخاب قوم ایرانی باز شد! &7یک قوم از اقوام اصیل ایران را انتخاب کنید"));
        }
    }

    private ItemStack createRaceItem(RaceType race, Player player, boolean finglish) {
        ItemStack item = new ItemStack(race.getIcon());
        ItemMeta meta = item.getItemMeta();
        String displayName = finglish ? race.getFinglishName() : race.getPersianName();
        meta.setDisplayName(MessageUtils.color("&6&l" + displayName));

        List<String> lore = new ArrayList<>();
        List<String> raceLore = finglish ? race.getLoreFinglish() : race.getLorePersian();
        for (String line : raceLore) {
            lore.add(MessageUtils.color(line));
        }
        lore.add("");
        if (finglish) {
            lore.add(MessageUtils.color("&7Biome haye khane (ghodrat kamel): &a" + race.getHomeBiomes().size()));
            lore.add(MessageUtils.color("&7Biome haye doshman (zaaf): &c" + race.getHostileBiomes().size()));
            lore.add("");
            lore.add(MessageUtils.color("&e&l» Baraye entekhab in ghom Irani click konid"));
        } else {
            lore.add(MessageUtils.color("&7بایوم‌های خانه (قدرت کامل): &a" + race.getHomeBiomes().size()));
            lore.add(MessageUtils.color("&7بایوم‌های دشمن (ضعف): &c" + race.getHostileBiomes().size()));
            lore.add("");
            lore.add(MessageUtils.color("&e&l» برای انتخاب این قوم ایرانی کلیک کنید"));
        }

        RaceType current = plugin.getRaceManager().getRace(player);
        if (current != null && current == race) {
            lore.add(MessageUtils.color(finglish ? "&a&l✔ Ghom feli shoma - Irani asil" : "&a&l✔ قوم فعلی شما - ایرانی اصیل"));
        } else {
            lore.add(MessageUtils.color(finglish ? "&7&lClick konid ta ozv in ghom shavid" : "&7&lکلیک کنید تا عضو این قوم شوید"));
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
                    if (display.contains(race.getFinglishName()) || display.contains(race.getPersianName()) || display.contains(race.getEnglishName()) || display.contains(race.getId())) {
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
                boolean finglish = plugin.getLanguageManager() != null && plugin.getLanguageManager().isFinglish();
                String msgKey = finglish ? "general.prefix-finglish" : "race.change-cooldown-message";
                String msg = plugin.getConfigManager().getString("race.change-cooldown-message", "&cBayad %days% rooz digar sabr konid.");
                msg = msg.replace("%days%", String.valueOf(remaining));
                player.sendMessage(MessageUtils.withPrefix(msg));
                player.closeInventory();
                return;
            }
            if (current == selected) {
                boolean finglish = plugin.getLanguageManager() != null && plugin.getLanguageManager().isFinglish();
                String name = finglish ? current.getFinglishName() : current.getPersianName();
                if (finglish) {
                    player.sendMessage(MessageUtils.withPrefix("&eShoma ozv hamin ghom Irani hastid! &6" + name));
                } else {
                    player.sendMessage(MessageUtils.withPrefix("&eشما عضو همین قوم ایرانی هستید! &6" + name));
                }
                player.closeInventory();
                return;
            }
        }

        plugin.getRaceManager().setRace(player, selected);
        player.closeInventory();
    }
}

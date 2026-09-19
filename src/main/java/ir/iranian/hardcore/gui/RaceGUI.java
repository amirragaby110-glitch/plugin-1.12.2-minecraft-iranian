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
 * Race selection GUI - v5.0 Finglish
 * 100% Finglish compatible for Aternos & Spigot 1.12.2
 */
public class RaceGUI implements Listener {

    private final IranianHardcorePlugin plugin;
    public static final String GUI_TITLE = MessageUtils.color("&8&lEntekhab Ghome Irani - Aghvam Iran");

    // Slot positions for 14 Iranian tribes in a 54-slot inventory
    private static final int[] RACE_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    public RaceGUI(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openRaceGUI(Player player) {
        openRaceSelectionGUI(player);
    }

    public void openRaceSelectionGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, GUI_TITLE);

        // Fill background with black glass
        ItemStack filler = createItem(Material.STAINED_GLASS_PANE, (short) 15, " ");
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, filler);
        }

        // Add 14 Iranian tribes
        RaceType[] races = RaceType.values();
        RaceType currentRace = plugin.getRaceManager().getPlayerRace(player);

        for (int i = 0; i < races.length && i < RACE_SLOTS.length; i++) {
            RaceType race = races[i];
            int slot = RACE_SLOTS[i];
            ItemStack item = createRaceItem(race, race == currentRace);
            inv.setItem(slot, item);
        }

        // Info book at top center (slot 4)
        ItemStack infoBook = new ItemStack(Material.BOOK);
        ItemMeta meta = infoBook.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&e&lRahnaye Aghvame Irani"));
            List<String> lore = Arrays.asList(
                    "&7Iran moteshekel az aghvame mokhtalef ba farhange ghani ast",
                    "&7Har ghom ghodrate khas dar biome va sarzamine khod darad",
                    "&7Va dar biome doshman zaeef mishavad",
                    "",
                    "&6&lAghvame Irani:",
                    "&7* Pars - Takht Jamshid, Pasargad",
                    "&7* Azari - Azarbayjan, Babak Khorramdin",
                    "&7* Kurd - Zagros, Oghab",
                    "&7* Lor - Lorestan, Shir",
                    "&7* Baloch - Baloochestan, Kavir",
                    "&7* Arab - Khoozestan, Karoon",
                    "&7* Turkmen - Asbe Torkaman",
                    "&7* Gilak - Gilan, Baran",
                    "&7* Mazani - Mazandaran, Tabari",
                    "&7* Bakhtiari - Kooch neshin",
                    "&7* Qashqayi - Farshe Ghashghayi",
                    "&7* Bandari - Khalij Fars, Daryanavard",
                    "&7* Khorasani - Mashhad, Ferdowsi",
                    "&7* Sistani - Zabol, Rostam Dastan",
                    "",
                    "&eBaraye entekhabe ghome Irani rooye an click konid",
                    "&cTaghire ghom faghat har 7 rooz momken ast!",
                    "&6Zende bad Iran - Aghvame Irani"
            );
            List<String> coloredLore = new ArrayList<>();
            for (String line : lore) {
                coloredLore.add(MessageUtils.color(line));
            }
            meta.setLore(coloredLore);
            infoBook.setItemMeta(meta);
        }
        inv.setItem(4, infoBook);

        // Iranian flag colors at the top
        ItemStack green = createItem(Material.STAINED_GLASS_PANE, (short) 13, "&aSabz - Tabiat Iran");
        ItemStack white = createItem(Material.STAINED_GLASS_PANE, (short) 0, "&fSefid - Solh Iran");
        ItemStack red = createItem(Material.STAINED_GLASS_PANE, (short) 14, "&cGhermez - Khoon Shohadaye Iran");

        inv.setItem(0, green); inv.setItem(1, green); inv.setItem(2, green);
        inv.setItem(3, white);
        inv.setItem(5, white);
        inv.setItem(6, red); inv.setItem(7, red); inv.setItem(8, red);

        // Current race display (slot 49)
        if (currentRace != null) {
            ItemStack currentItem = new ItemStack(currentRace.getIcon());
            ItemMeta currentMeta = currentItem.getItemMeta();
            if (currentMeta != null) {
                currentMeta.setDisplayName(MessageUtils.color("&6&lGhome feli shoma: " + currentRace.getFinglishName()));
                List<String> curLore = Arrays.asList(
                        MessageUtils.color("&7Shoma ozve ghome &6" + currentRace.getFinglishName() + " &7hastid"),
                        MessageUtils.color("&7Biome feli: &f" + player.getLocation().getBlock().getBiome().name()),
                        MessageUtils.color("&7Ghodrat: " + (currentRace.isHomeBiome(player.getLocation().getBlock().getBiome()) ? "&aKamel" : "&cZaeef"))
                );
                currentMeta.setLore(curLore);
                currentItem.setItemMeta(currentMeta);
            }
            inv.setItem(49, currentItem);
        }

        player.openInventory(inv);
        player.sendMessage(MessageUtils.withPrefix("&aMenuye entekhabe ghome Irani baz shod! &7Yek ghom az aghvame asile Iran ra entekhab konid"));
    }

    private ItemStack createRaceItem(RaceType race, boolean isCurrent) {
        ItemStack item = new ItemStack(race.getIcon());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = race.getFinglishName();
            if (isCurrent) {
                meta.setDisplayName(MessageUtils.color("&a&l✔ " + name + " &e(Ghome shoma)"));
            } else {
                meta.setDisplayName(MessageUtils.color("&6&l" + name));
            }

            List<String> lore = new ArrayList<>();
            for (String line : race.getLoreFinglish()) {
                lore.add(MessageUtils.color(line));
            }
            lore.add("");
            lore.add(MessageUtils.color("&7Biomehaye khane (ghodrat kamel): &a" + race.getHomeBiomes().size()));
            lore.add(MessageUtils.color("&7Biomehaye doshman (zaaf): &c" + race.getHostileBiomes().size()));
            lore.add("");
            if (isCurrent) {
                lore.add(MessageUtils.color("&a&l✔ Ghome feli shoma - Irani asil"));
            } else {
                lore.add(MessageUtils.color("&7&lClick konid ta ozve in ghom shavid"));
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createItem(Material material, short data, String name) {
        ItemStack item = new ItemStack(material, 1, data);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color(name));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (event.getView() == null || !event.getView().getTitle().equals(GUI_TITLE)) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        if (clicked.getType() == Material.STAINED_GLASS_PANE || clicked.getType() == Material.BOOK) return;

        RaceType selected = null;
        for (RaceType race : RaceType.values()) {
            if (clicked.getType() == race.getIcon()) {
                if (clicked.getItemMeta() != null && clicked.getItemMeta().getDisplayName() != null) {
                    String display = clicked.getItemMeta().getDisplayName();
                    if (display.contains(race.getFinglishName()) || display.contains(race.getEnglishName()) || display.contains(race.getId())) {
                        selected = race;
                        break;
                    }
                }
            }
        }

        if (selected == null) {
            int slot = event.getSlot();
            selected = getRaceFromSlot(slot);
        }

        if (selected == null) return;

        RaceType current = plugin.getRaceManager().getPlayerRace(player);
        if (current != null) {
            if (!plugin.getRaceManager().canChangeRace(player)) {
                long remaining = plugin.getRaceManager().getRaceChangeCooldownLeftDays(player);
                String msg = plugin.getConfigManager().getString("race.change-cooldown-message", "&cBayad %days% rooz digar sabr konid.");
                msg = msg.replace("%days%", String.valueOf(remaining));
                player.sendMessage(MessageUtils.withPrefix(msg));
                player.closeInventory();
                return;
            }
            if (current == selected) {
                player.sendMessage(MessageUtils.withPrefix("&eShoma ozve hamin ghome Irani hastid! &6" + current.getFinglishName()));
                player.closeInventory();
                return;
            }
        }

        plugin.getRaceManager().setPlayerRace(player, selected);
        plugin.getRaceManager().recordRaceChange(player);
        player.closeInventory();
    }

    public RaceType getRaceFromSlot(int slot) {
        for (int i = 0; i < RACE_SLOTS.length; i++) {
            if (RACE_SLOTS[i] == slot) {
                RaceType[] races = RaceType.values();
                if (i < races.length) {
                    return races[i];
                }
            }
        }
        return null;
    }
}

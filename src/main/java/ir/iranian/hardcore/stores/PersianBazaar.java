package ir.iranian.hardcore.stores;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.items.PersianItems;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bazar Irani - Foroshgah ba GUI 100% Finglish
 * Bazikonan mitavanand ba sekke Derik (GOLD_NUGGET) itemhaye Irani bekharand
 */
public class PersianBazaar implements Listener {

    private final IranianHardcorePlugin plugin;
    private final String bazaarTitle = MessageUtils.color("&8&lBazar Irani - Hojrehaye Sonati");
    private final String weaponTitle = MessageUtils.color("&8&lBazar Salah - Rasteye Shamshir");
    private final String jewelTitle = MessageUtils.color("&8&lBazar Javaher - Rasteye Zargar");
    private final String carpetTitle = MessageUtils.color("&8&lBazar Farsh - Rasteye Farsh");
    private final String foodTitle = MessageUtils.color("&8&lBazar Attari va Chai - Rasteye Attar");

    private final Map<Material, Integer> prices = new HashMap<>();

    public PersianBazaar(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        initPrices();
    }

    private void initPrices() {
        prices.put(Material.DIAMOND_SWORD, 20);
        prices.put(Material.GOLD_HELMET, 30);
        prices.put(Material.BOW, 25);
        prices.put(Material.CARPET, 5);
        prices.put(Material.PRISMARINE_SHARD, 8);
        prices.put(Material.CLAY_BRICK, 3);
        prices.put(Material.FIREBALL, 15);
        prices.put(Material.GOLD_NUGGET, 1);
    }

    public void openMainMenu(Player player) {
        openMainBazaar(player);
    }

    public void openMainBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, bazaarTitle);

        inv.setItem(10, createCategoryItem(Material.DIAMOND_SWORD, "&c&lRasteye Salah Foroohan", Arrays.asList(
                "&7Shamshirhaye Irani: Alamut, Babak, Hakhamaneshi",
                "&7Kamane Arash Kamangir",
                "&eClick baraye vorood"
        )));

        inv.setItem(11, createCategoryItem(Material.GOLD_BLOCK, "&6&lRasteye Zargarha va Javaher", Arrays.asList(
                "&7Taje Kourosh, Morvaride Khalije Fars",
                "&7Sekke Derik Hakhamaneshi",
                "&eClick baraye vorood"
        )));

        inv.setItem(12, createCategoryItem(Material.CARPET, "&c&lRasteye Farsh Forooshan", Arrays.asList(
                "&7Farshe dastbafe Kermani va Yazdi",
                "&7Sanaye dasti Irani",
                "&eClick baraye vorood"
        )));

        inv.setItem(13, createCategoryItem(Material.POTION, "&6&lRasteye Attaran va Chai", Arrays.asList(
                "&7Chaye Lahijan, Adviehaye Irani",
                "&7Pare Simurgh, Atashe Moghadas",
                "&eClick baraye vorood"
        )));

        inv.setItem(14, createCategoryItem(Material.BOOK, "&e&lRasteye Ketab Forooshan", Arrays.asList(
                "&7Manshoore Kourosh, Lohe Elami",
                "&7Ketabhaye tarikhi Iran",
                "&eClick baraye vorood"
        )));

        // Coin info
        ItemStack coinInfo = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = coinInfo.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&e&lSekke Derik Hakhamaneshi - Vahede Pool"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Vahede poole Bazar Irani: Sekke Derik",
                    "&7Sekke talaye Daryoosh Bozorg",
                    "",
                    "&7Mojoodie shoma: &e" + countDarikCoins(player) + " sekke",
                    "",
                    "&7Sekke ra az dungeonha peyda konid!",
                    "&7Ya az roostayian bekharid"
            )));
            coinInfo.setItemMeta(meta);
        }
        inv.setItem(16, coinInfo);

        // Help
        ItemStack help = new ItemStack(Material.BOOK);
        ItemMeta helpMeta = help.getItemMeta();
        if (helpMeta != null) {
            helpMeta.setDisplayName(MessageUtils.color("&a&lRahnaye Bazar Irani"));
            helpMeta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Be bazare sonatie Iran khosh amadid!",
                    "&7Mesle Bazare Bozorge Tehran va Esfahan",
                    "",
                    "&eBaraye kharid click konid",
                    "&7Sekke Derik = GOLD_NUGGET",
                    "",
                    "&6Zende bad Iran!"
            )));
            help.setItemMeta(helpMeta);
        }
        inv.setItem(4, help);

        fillGlass(inv);
        player.openInventory(inv);
    }

    private void openWeaponBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, weaponTitle);

        inv.setItem(10, createShopItem(PersianItems.createShamshirAlamut(), 20));
        inv.setItem(11, createShopItem(PersianItems.createShamshirBabak(), 18));
        inv.setItem(12, createShopItem(PersianItems.createShamshirHakhamaneshi(), 30));
        inv.setItem(13, createShopItem(PersianItems.createKamanArash(), 25));

        inv.setItem(18, createBackButton());
        fillGlass(inv);
        player.openInventory(inv);
    }

    private void openJewelBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, jewelTitle);

        inv.setItem(10, createShopItem(PersianItems.createTajKourosh(), 50));
        inv.setItem(11, createShopItem(PersianItems.createMorvaridKhalij(), 15));
        inv.setItem(12, createShopItem(PersianItems.createSekkeHakhamaneshi(), 1));
        inv.setItem(13, createShopItem(PersianItems.createAtashMoghadas(), 20));

        inv.setItem(18, createBackButton());
        fillGlass(inv);
        player.openInventory(inv);
    }

    private void openCarpetBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, carpetTitle);

        inv.setItem(10, createShopItem(PersianItems.createFarshKermani(), 10));
        inv.setItem(11, createShopItem(PersianItems.createMorghAmin(), 12));

        inv.setItem(18, createBackButton());
        fillGlass(inv);
        player.openInventory(inv);
    }

    private void openFoodBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, foodTitle);

        inv.setItem(10, createShopItem(PersianItems.createChaiIrani(), 5));
        inv.setItem(11, createShopItem(PersianItems.createLohIlami(), 8));
        inv.setItem(12, createShopItem(PersianItems.createManshurKourosh(), 30));

        inv.setItem(18, createBackButton());
        fillGlass(inv);
        player.openInventory(inv);
    }

    private ItemStack createCategoryItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color(name));
            meta.setLore(MessageUtils.color(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createShopItem(ItemStack persianItem, int price) {
        ItemStack item = persianItem.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore() != null ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
            lore.add("");
            lore.add(MessageUtils.color("&e&lGheymat: &6" + price + " Sekke Derik"));
            lore.add(MessageUtils.color("&a&lClick baraye kharid!"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createBackButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&c&lBazgasht be Bazare Asli"));
            item.setItemMeta(meta);
        }
        return item;
    }

    private void fillGlass(Inventory inv) {
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta meta = glass.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
        }
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }
    }

    private int countDarikCoins(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.GOLD_NUGGET) {
                count += item.getAmount();
            }
        }
        return count;
    }

    private boolean hasEnoughCoins(Player player, int price) {
        return countDarikCoins(player) >= price;
    }

    private void removeCoins(Player player, int price) {
        int remaining = price;
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() == Material.GOLD_NUGGET) {
                int amount = item.getAmount();
                if (amount <= remaining) {
                    player.getInventory().setItem(i, null);
                    remaining -= amount;
                } else {
                    item.setAmount(amount - remaining);
                    remaining = 0;
                }
                if (remaining <= 0) break;
            }
        }
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getView() == null) return;
        String title = event.getView().getTitle();

        if (!title.equals(bazaarTitle) && !title.equals(weaponTitle) && !title.equals(jewelTitle)
                && !title.equals(carpetTitle) && !title.equals(foodTitle)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR || clicked.getType() == Material.STAINED_GLASS_PANE) {
            return;
        }

        if (title.equals(bazaarTitle)) {
            int slot = event.getSlot();
            switch (slot) {
                case 10:
                    openWeaponBazaar(player);
                    break;
                case 11:
                    openJewelBazaar(player);
                    break;
                case 12:
                    openCarpetBazaar(player);
                    break;
                case 13:
                    openFoodBazaar(player);
                    break;
                case 14:
                    openFoodBazaar(player);
                    break;
            }
        } else {
            if (clicked.getType() == Material.ARROW) {
                openMainBazaar(player);
                return;
            }

            int price = 10;
            if (clicked.getItemMeta() != null && clicked.getItemMeta().getLore() != null) {
                for (String line : clicked.getItemMeta().getLore()) {
                    if (line.contains("Gheymat:")) {
                        try {
                            String num = line.replaceAll("[^0-9]", "");
                            if (!num.isEmpty()) price = Integer.parseInt(num);
                        } catch (Exception ignored) {}
                    }
                }
            }

            if (!hasEnoughCoins(player, price)) {
                player.sendMessage(MessageUtils.withPrefix("&cSekke kafi nadarid! Niaz: " + price + " sekke, Darid: " + countDarikCoins(player)));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }

            removeCoins(player, price);

            ItemStack toGive = clicked.clone();
            ItemMeta meta = toGive.getItemMeta();
            if (meta != null && meta.getLore() != null) {
                List<String> lore = new ArrayList<>(meta.getLore());
                lore.removeIf(l -> l.contains("Gheymat:") || l.contains("Click baraye kharid"));
                meta.setLore(lore);
                toGive.setItemMeta(meta);
            }

            player.getInventory().addItem(toGive);
            player.sendMessage(MessageUtils.withPrefix("&aKharide movafagh! &7" + (clicked.getItemMeta() != null ? clicked.getItemMeta().getDisplayName() : "Item") + " &abe gheymate &6" + price + " Sekke Derik"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
        }
    }
}

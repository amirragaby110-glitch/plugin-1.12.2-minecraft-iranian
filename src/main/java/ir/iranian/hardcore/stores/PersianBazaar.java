package ir.iranian.hardcore.stores;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.items.PersianItems;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * بازار ایرانی - فروشگاه با GUI کاملا فارسی
 * بازیکنان می‌توانند با سکه دریک (GOLD_NUGGET) آیتم‌های ایرانی بخرند
 * مثل بازارهای سنتی ایران: بازار تهران، اصفهان، تبریز
 */
public class PersianBazaar implements Listener {

    private final IranianHardcorePlugin plugin;
    private final String bazaarTitle = MessageUtils.color("&8&l🏪 بازار ایرانی - حجره‌های سنتی");
    private final String weaponTitle = MessageUtils.color("&8&l⚔ بازار سلاح‌فروشان - راسته شمشیرگرها");
    private final String jewelTitle = MessageUtils.color("&8&l💎 بازار جواهر‌فروشان - راسته زرگرها");
    private final String carpetTitle = MessageUtils.color("&8&l🧶 بازار فرش‌فروشان - راسته فرش");
    private final String foodTitle = MessageUtils.color("&8&l☕ بازار عطاری و چای - راسته عطاران");

    // قیمت‌ها بر اساس سکه دریک (GOLD_NUGGET)
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

    /**
     * باز کردن بازار اصلی
     */
    public void openMainBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, bazaarTitle);

        // دسته‌بندی‌ها
        inv.setItem(10, createCategoryItem(Material.DIAMOND_SWORD, "&c&l⚔ راسته سلاح‌فروشان", Arrays.asList(
                "&7شمشیرهای ایرانی: الموت، بابک، هخامنشی",
                "&7کمان آرش کمانگیر",
                "&eکلیک برای ورود"
        )));

        inv.setItem(11, createCategoryItem(Material.GOLD_BLOCK, "&6&l💎 راسته زرگرها و جواهر‌فروشان", Arrays.asList(
                "&7تاج کوروش، مروارید خلیج فارس",
                "&7سکه دریک هخامنشی",
                "&eکلیک برای ورود"
        )));

        inv.setItem(12, createCategoryItem(Material.CARPET, "&c&l🧶 راسته فرش‌فروشان", Arrays.asList(
                "&7فرش دستباف کرمانی، یزدی",
                "&7صنایع دستی ایرانی",
                "&eکلیک برای ورود"
        )));

        inv.setItem(13, createCategoryItem(Material.POTION, "&6&l☕ راسته عطاران و چای‌فروشان", Arrays.asList(
                "&7چای لاهیجان، ادویه‌های ایرانی",
                "&7پر سیمرغ، آتش مقدس",
                "&eکلیک برای ورود"
        )));

        inv.setItem(14, createCategoryItem(Material.BOOK, "&e&l📜 راسته کتاب‌فروشان", Arrays.asList(
                "&7منشور کوروش، لوح ایلامی",
                "&7کتاب‌های تاریخی ایران",
                "&eکلیک برای ورود"
        )));

        // اطلاعات سکه
        ItemStack coinInfo = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = coinInfo.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&e&l🪙 سکه دریک هخامنشی - واحد پول"));
        meta.setLore(MessageUtils.color(Arrays.asList(
                "&7واحد پول بازار ایرانی: سکه دریک",
                "&7سکه طلای داریوش بزرگ",
                "",
                "&7موجودی شما: &e" + countDarikCoins(player) + " سکه",
                "",
                "&7سکه را از دانجن‌ها پیدا کنید!",
                "&7یا از روستاییان بخرید"
        )));
        coinInfo.setItemMeta(meta);
        inv.setItem(16, coinInfo);

        // راهنما
        ItemStack help = new ItemStack(Material.BOOK);
        ItemMeta helpMeta = help.getItemMeta();
        helpMeta.setDisplayName(MessageUtils.color("&a&lراهنمای بازار ایرانی"));
        helpMeta.setLore(MessageUtils.color(Arrays.asList(
                "&7به بازار سنتی ایران خوش آمدید!",
                "&7مثل بازار بزرگ تهران و اصفهان",
                "",
                "&eبرای خرید کلیک کنید",
                "&7سکه دریک = GOLD_NUGGET",
                "",
                "&6&l🇮🇷 زنده باد ایران!"
        )));
        help.setItemMeta(helpMeta);
        inv.setItem(4, help);

        fillGlass(inv);

        player.openInventory(inv);
    }

    private void openWeaponBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, weaponTitle);

        inv.setItem(10, createShopItem(PersianItems.createShamshirAlamut(), 20, "شمشیر الموت"));
        inv.setItem(11, createShopItem(PersianItems.createShamshirBabak(), 18, "شمشیر بابک"));
        inv.setItem(12, createShopItem(PersianItems.createShamshirHakhamaneshi(), 30, "آکیناکه هخامنشی"));
        inv.setItem(13, createShopItem(PersianItems.createKamanArash(), 25, "کمان آرش"));

        inv.setItem(18, createBackButton());
        fillGlass(inv);
        player.openInventory(inv);
    }

    private void openJewelBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, jewelTitle);

        inv.setItem(10, createShopItem(PersianItems.createTajKourosh(), 50, "تاج کوروش"));
        inv.setItem(11, createShopItem(PersianItems.createMorvaridKhalij(), 15, "مروارید خلیج فارس"));
        inv.setItem(12, createShopItem(PersianItems.createSekkeHakhamaneshi(), 1, "سکه دریک"));
        inv.setItem(13, createShopItem(PersianItems.createAtashMoghadas(), 20, "آتش مقدس"));

        inv.setItem(18, createBackButton());
        fillGlass(inv);
        player.openInventory(inv);
    }

    private void openCarpetBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, carpetTitle);

        inv.setItem(10, createShopItem(PersianItems.createFarshKermani(), 10, "فرش کرمانی"));
        inv.setItem(11, createShopItem(PersianItems.createMorghAmin(), 12, "پر سیمرغ"));

        inv.setItem(18, createBackButton());
        fillGlass(inv);
        player.openInventory(inv);
    }

    private void openFoodBazaar(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, foodTitle);

        inv.setItem(10, createShopItem(PersianItems.createChaiIrani(), 5, "چای لاهیجان"));
        inv.setItem(11, createShopItem(PersianItems.createLohIlami(), 8, "خشت ایلامی"));
        inv.setItem(12, createShopItem(PersianItems.createManshurKourosh(), 30, "منشور کوروش"));

        inv.setItem(18, createBackButton());
        fillGlass(inv);
        player.openInventory(inv);
    }

    private ItemStack createCategoryItem(Material mat, String name, java.util.List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color(name));
        meta.setLore(MessageUtils.color(lore));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createShopItem(ItemStack persianItem, int price, String simpleName) {
        ItemStack item = persianItem.clone();
        ItemMeta meta = item.getItemMeta();
        java.util.List<String> lore = meta.getLore() != null ? meta.getLore() : new java.util.ArrayList<>();
        lore.add("");
        lore.add(MessageUtils.color("&e&lقیمت: &6" + price + " سکه دریک"));
        lore.add(MessageUtils.color("&a&lکلیک برای خرید!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createBackButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&c&l↩ بازگشت به بازار اصلی"));
        item.setItemMeta(meta);
        return item;
    }

    private void fillGlass(Inventory inv) {
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);
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
            // منوی اصلی
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
                    // کتابفروشی - فعلا همان غذا
                    openFoodBazaar(player);
                    break;
            }
        } else {
            // منوهای خرید
            if (clicked.getType() == Material.ARROW) {
                openMainBazaar(player);
                return;
            }

            // خرید
            // قیمت را از Lore بخوان
            int price = 10; // پیش‌فرض
            if (clicked.getItemMeta() != null && clicked.getItemMeta().getLore() != null) {
                for (String line : clicked.getItemMeta().getLore()) {
                    if (line.contains("قیمت:")) {
                        try {
                            String num = line.replaceAll("[^0-9]", "");
                            if (!num.isEmpty()) price = Integer.parseInt(num);
                        } catch (Exception ignored) {}
                    }
                }
            }

            if (!hasEnoughCoins(player, price)) {
                player.sendMessage(MessageUtils.withPrefix("&cسکه کافی نداری! نیاز: " + price + " سکه، داری: " + countDarikCoins(player)));
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }

            // خرید موفق
            removeCoins(player, price);

            // آیتم را بدون قیمت بده
            ItemStack toGive = clicked.clone();
            ItemMeta meta = toGive.getItemMeta();
            if (meta != null && meta.getLore() != null) {
                java.util.List<String> lore = new java.util.ArrayList<>(meta.getLore());
                lore.removeIf(l -> l.contains("قیمت:") || l.contains("کلیک برای خرید"));
                meta.setLore(lore);
                toGive.setItemMeta(meta);
            }

            player.getInventory().addItem(toGive);
            player.sendMessage(MessageUtils.withPrefix("&aخرید موفق! &7" + (clicked.getItemMeta() != null ? clicked.getItemMeta().getDisplayName() : "آیتم") + " &aبه قیمت &6" + price + " سکه دریک"));
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
        }
    }
}

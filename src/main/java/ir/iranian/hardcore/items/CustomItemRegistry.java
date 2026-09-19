package ir.iranian.hardcore.items;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.foods.IranianFoodType;
import ir.iranian.hardcore.swords.PersianSwordType;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Procedural Registry for 20,000 Craftable Iranian Items (v5.0 Finglish)
 * Categories:
 * 1. Swords: 900 (1 - 900)
 * 2. Foods: 500 (901 - 1400)
 * 3. Tools: 1000 (1401 - 2400)
 * 4. Armors: 1000 (2401 - 3400)
 * 5. Jewels & Coins: 1000 (3401 - 4400)
 * 6. Carpets & Crafts: 1000 (4401 - 5400)
 * 7. Relics & Scrolls: 14600 (5401 - 20000)
 * Total: Exactly 20,000 registered procedural items!
 */
public class CustomItemRegistry implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public static final int TOTAL_ITEMS = 20000;

    // Prefixes for procedural tools, armors, and relics
    private static final String[] ERAS = {
            "Hakhamaneshi", "Ashkani", "Sasani", "Safavi", "Afshari",
            "Zand", "Qajari", "Samanid", "Deylami", "Buyid",
            "Ghaznavid", "Seljuk", "Khwarazmian", "Ilkhanate", "Timurid"
    };

    private static final String[] HEROES = {
            "Kourosh", "Dariush", "Rostam", "Sohrab", "Siavash",
            "Fereydoun", "Zal", "Arash", "Kaveh", "Giv",
            "Goudarz", "Tahmasp", "Shah Abbas", "Nader Shah", "Babak"
    };

    private static final String[] JEWEL_TYPES = {
            "Firoozeh Neyshaboor", "Yaghoot Badakhshan", "Zomorrod Panjshir",
            "Sekkeh Derik-e Tala", "Sekkeh Derik-e Noqreh", "Morvarid Khalij Fars",
            "Dourr-e Najaf", "Almas Kooh-e Noor", "Almas Darya-ye Noor", "Aghigh-e Zard"
    };

    private static final String[] CITIES = {
            "Isfahan", "Tabriz", "Shiraz", "Kashan", "Yazd",
            "Kerman", "Mashhad", "Hamedan", "Qazvin", "Neyshaboor",
            "Sanandaj", "Khorramabad", "Ahvaz", "Rasht", "Sari"
    };

    public CustomItemRegistry(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("CustomItemRegistry: 20,000 Iranian items registry initialized!");
    }

    public ItemStack getItemByNumber(int number) {
        if (number < 1) number = 1;
        if (number > TOTAL_ITEMS) number = TOTAL_ITEMS;

        // 1. Swords (1 - 900)
        if (number <= 900) {
            PersianSwordType.SwordData sword = PersianSwordType.getByNumber(number);
            return sword != null ? sword.createItemStack() : new ItemStack(Material.IRON_SWORD);
        }

        // 2. Foods (901 - 1400)
        if (number <= 1400) {
            int foodIndex = number - 900;
            IranianFoodType.FoodData food = IranianFoodType.getByNumber(foodIndex);
            return food != null ? food.createItemStack() : new ItemStack(Material.BREAD);
        }

        // 3. Tools (1401 - 2400)
        if (number <= 2400) {
            return createTool(number);
        }

        // 4. Armors (2401 - 3400)
        if (number <= 3400) {
            return createArmor(number);
        }

        // 5. Jewels & Coins (3401 - 4400)
        if (number <= 4400) {
            return createJewel(number);
        }

        // 6. Carpets & Crafts (4401 - 5400)
        if (number <= 5400) {
            return createCarpet(number);
        }

        // 7. Relics, Tablets & Scrolls (5401 - 20000)
        return createRelic(number);
    }

    public ItemStack getItemById(String id) {
        if (id == null) return null;
        id = id.toUpperCase().trim();

        try {
            int num = Integer.parseInt(id);
            return getItemByNumber(num);
        } catch (NumberFormatException ignored) {}

        if (id.startsWith("ITEM_")) {
            try {
                int num = Integer.parseInt(id.substring(5));
                return getItemByNumber(num);
            } catch (NumberFormatException ignored) {}
        }
        if (id.startsWith("SWORD_")) {
            PersianSwordType.SwordData sword = PersianSwordType.getById(id);
            if (sword != null) return sword.createItemStack();
        }
        if (id.startsWith("FOOD_")) {
            IranianFoodType.FoodData food = IranianFoodType.getById(id);
            if (food != null) return food.createItemStack();
        }

        return null;
    }

    private ItemStack createTool(int number) {
        int index = number - 1400;
        String era = ERAS[index % ERAS.length];
        String hero = HEROES[(index / ERAS.length) % HEROES.length];
        int toolType = index % 4; // 0=Pickaxe, 1=Axe, 2=Shovel, 3=Hoe

        Material mat;
        String toolName;
        if (toolType == 0) {
            mat = (index % 3 == 0) ? Material.DIAMOND_PICKAXE : Material.IRON_PICKAXE;
            toolName = "Kolang-e Madan";
        } else if (toolType == 1) {
            mat = (index % 3 == 0) ? Material.DIAMOND_AXE : Material.IRON_AXE;
            toolName = "Tabar-e Jangal";
        } else if (toolType == 2) {
            mat = Material.IRON_SPADE;
            toolName = "Beel-e Zera'at";
        } else {
            mat = Material.IRON_HOE;
            toolName = "Daskhaleh-ye Keshavarzi";
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6" + toolName + " - " + era + " (" + hero + ")"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Dowreh-ye Tarikhi: &e" + era));
            lore.add(MessageUtils.color("&7Yadegar-e: &a" + hero));
            lore.add(MessageUtils.color("&bAsil-e Irani - Karkard-e Ba-esteghemat"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Abzar-e Irani] &7ID: &eITEM_" + String.format("%05d", number) + " &7(#" + index + "/1000)"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.DURABILITY, 2 + (index % 2), true);
            if (toolType != 3) {
                meta.addEnchant(Enchantment.DIG_SPEED, 3 + (index % 3), true);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createArmor(int number) {
        int index = number - 2400;
        String hero = HEROES[index % HEROES.length];
        String era = ERAS[(index / HEROES.length) % ERAS.length];
        int pieceType = index % 4; // 0=Helmet, 1=Chestplate, 2=Leggings, 3=Boots

        Material mat;
        String pieceName;
        if (pieceType == 0) {
            mat = (index % 2 == 0) ? Material.DIAMOND_HELMET : Material.IRON_HELMET;
            pieceName = "Kolahkhod-e Pahlavani";
        } else if (pieceType == 1) {
            mat = (index % 2 == 0) ? Material.DIAMOND_CHESTPLATE : Material.IRON_CHESTPLATE;
            pieceName = "Jowshan-e Zarrin";
        } else if (pieceType == 2) {
            mat = (index % 2 == 0) ? Material.DIAMOND_LEGGINGS : Material.IRON_LEGGINGS;
            pieceName = "Zereh-ye Jangjooyan";
        } else {
            mat = (index % 2 == 0) ? Material.DIAMOND_BOOTS : Material.IRON_BOOTS;
            pieceName = "Mozeh-ye Savar-nezam";
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6" + pieceName + " - " + hero));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Pahlavan: &e" + hero));
            lore.add(MessageUtils.color("&7Dowreh: &a" + era));
            lore.add(MessageUtils.color("&bMohafazat-e Sangin dar barabar-e Doshman"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Zereh-ye Irani] &7ID: &eITEM_" + String.format("%05d", number) + " &7(#" + index + "/1000)"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2 + (index % 3), true);
            meta.addEnchant(Enchantment.DURABILITY, 3, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createJewel(int number) {
        int index = number - 3400;
        String type = JEWEL_TYPES[index % JEWEL_TYPES.length];
        String city = CITIES[(index / JEWEL_TYPES.length) % CITIES.length];

        Material mat = Material.GOLD_NUGGET;
        if (type.contains("Tala")) mat = Material.GOLD_INGOT;
        else if (type.contains("Firoozeh")) mat = Material.PRISMARINE_SHARD;
        else if (type.contains("Yaghoot") || type.contains("Aghigh")) mat = Material.REDSTONE;
        else if (type.contains("Zomorrod")) mat = Material.EMERALD;
        else if (type.contains("Almas")) mat = Material.DIAMOND;
        else if (type.contains("Morvarid")) mat = Material.NETHER_STAR;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&e" + type + " - " + city));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Javaher-e Nafis-e: &6" + city));
            lore.add(MessageUtils.color("&7Arzesh: &aGharar-dadeh baraye Tejarat dar Bazaar"));
            lore.add(MessageUtils.color("&eGowhar-e derakhshan-e sarzamin-e Pars"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Gowhar-e Irani] &7ID: &eITEM_" + String.format("%05d", number) + " &7(#" + index + "/1000)"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createCarpet(int number) {
        int index = number - 4400;
        String city = CITIES[index % CITIES.length];
        String era = ERAS[(index / CITIES.length) % ERAS.length];

        ItemStack item = new ItemStack(Material.CARPET, 1, (short)(index % 16));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&cFarsh-e Dastbaf-e " + city + " (" + era + ")"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Honar-e Asil-e: &e" + city));
            lore.add(MessageUtils.color("&7Tarh-e Sonnati: &aShah Abbasi va Lachak Toranj"));
            lore.add(MessageUtils.color("&dShohrat-e Jahani dar Jadeh-ye Abrisham"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Farsh-e Irani] &7ID: &eITEM_" + String.format("%05d", number) + " &7(#" + index + "/1000)"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createRelic(int number) {
        int index = number - 5400;
        String era = ERAS[index % ERAS.length];
        String city = CITIES[(index / ERAS.length) % CITIES.length];

        int relicType = index % 6;
        Material mat;
        String relicName;
        String desc;

        if (relicType == 0) {
            mat = Material.BOOK;
            relicName = "Tumar-e Shahnameh-ye Ferdowsi (" + index + ")";
            desc = "Dastan-haye hamaseh-ye Rostam va Sohrab";
        } else if (relicType == 1) {
            mat = Material.HARD_CLAY;
            relicName = "Khesht-e Khatt-e Mikhi (" + city + ")";
            desc = "Katibeh-ye bano-gozari-ye emperatori";
        } else if (relicType == 2) {
            mat = Material.FLOWER_POT;
            relicName = "Soofal-e Bastani-ye Meybod (" + index + ")";
            desc = "Honar-e soofalgari ba ghadamat-e 5000 saleh";
        } else if (relicType == 3) {
            mat = Material.MAP;
            relicName = "Naghsheh-ye Jadeh-ye Abrisham (" + city + ")";
            desc = "Masir-e kervan-haye bazargani dar Iran";
        } else if (relicType == 4) {
            mat = Material.YELLOW_FLOWER;
            relicName = "Zaferan-e Sorkh-e Ghaenat (" + index + ")";
            desc = "Tala-ye sorkh-e khorasan ba atre jadooyi";
        } else {
            mat = Material.PAPER;
            relicName = "Farman-e Manshoor-e Kourosh (" + index + ")";
            desc = "Nakhostin elamieh-ye hooghooghe bashar dar jahan";
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6" + relicName));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Dowreh: &e" + era));
            lore.add(MessageUtils.color("&7Shahr: &a" + city));
            lore.add(MessageUtils.color("&e" + desc));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Asar-e Bastani] &7ID: &eITEM_" + String.format("%05d", number) + " &7(#" + index + "/14600)"));
            lore.add(MessageUtils.color("&b7000 Saal Tamaddon-e Iran"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        if (matrix == null || matrix.length < 9) return;

        boolean hasClay = false;
        boolean hasPaperOrBook = false;
        boolean hasGoldOrIron = false;
        boolean hasWool = false;
        int count = 0;

        for (ItemStack item : matrix) {
            if (item == null || item.getType() == Material.AIR) continue;
            count++;
            Material t = item.getType();
            if (t == Material.CLAY_BALL || t == Material.CLAY || t == Material.HARD_CLAY) hasClay = true;
            if (t == Material.PAPER || t == Material.BOOK) hasPaperOrBook = true;
            if (t == Material.GOLD_INGOT || t == Material.IRON_INGOT || t == Material.GOLD_NUGGET) hasGoldOrIron = true;
            if (t == Material.WOOL) hasWool = true;
        }

        // Relic crafting: Clay + Paper -> Ancient Cuneiform Tablet / Relic
        if (hasClay && hasPaperOrBook && count == 2) {
            int randomRelic = 5401 + random.nextInt(14600);
            inv.setResult(getItemByNumber(randomRelic));
        }
        // Carpet crafting: Wool + Gold Ingot -> Persian Rug
        else if (hasWool && hasGoldOrIron && count == 2) {
            int randomCarpet = 4401 + random.nextInt(1000);
            inv.setResult(getItemByNumber(randomCarpet));
        }
        // Jewel crafting: Gold + Iron + Paper -> Ancient Coin / Jewel
        else if (hasGoldOrIron && hasPaperOrBook && count == 2) {
            int randomJewel = 3401 + random.nextInt(1000);
            inv.setResult(getItemByNumber(randomJewel));
        }
    }

    public boolean giveItem(Player player, String id) {
        ItemStack item = getItemById(id);
        if (item == null) return false;
        player.getInventory().addItem(item);
        player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aItem &e" + (item.hasItemMeta() ? item.getItemMeta().getDisplayName() : id) + " &abe shoma dadeh shod!"));
        return true;
    }

    public void giveRandomItem(Player player) {
        int randNum = 1 + random.nextInt(TOTAL_ITEMS);
        ItemStack item = getItemByNumber(randNum);
        player.getInventory().addItem(item);
        player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aItem-e tasadofi &eITEM_" + String.format("%05d", randNum) + " &a(#" + randNum + "/20000) dadeh shod!"));
    }
}

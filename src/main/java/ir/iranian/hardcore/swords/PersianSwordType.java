package ir.iranian.hardcore.swords;

import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * 900 Unique Craftable Iranian Swords (v5.0 Finglish)
 * 30 Base Historical Blades x 30 Legendary Prefixes = 900 Unique Swords!
 * Completely Finglish, Spigot 1.12.2 compatible, zero Persian Unicode.
 */
public class PersianSwordType {

    public static class BaseSword {
        private final int id;
        private final String code;
        private final String name;
        private final Material material;
        private final double damage;
        private final String description;

        public BaseSword(int id, String code, String name, Material material, double damage, String description) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.material = material;
            this.damage = damage;
            this.description = description;
        }

        public int getId() { return id; }
        public String getCode() { return code; }
        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public double getDamage() { return damage; }
        public String getDescription() { return description; }
    }

    public static class SwordPrefix {
        private final int id;
        private final String name;
        private final double bonusDamage;
        private final Enchantment enchantment;
        private final int enchantLevel;
        private final String lore;

        public SwordPrefix(int id, String name, double bonusDamage, Enchantment enchantment, int enchantLevel, String lore) {
            this.id = id;
            this.name = name;
            this.bonusDamage = bonusDamage;
            this.enchantment = enchantment;
            this.enchantLevel = enchantLevel;
            this.lore = lore;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public double getBonusDamage() { return bonusDamage; }
        public Enchantment getEnchantment() { return enchantment; }
        public int getEnchantLevel() { return enchantLevel; }
        public String getLore() { return lore; }
    }

    public static class SwordData {
        private final String id;
        private final int number;
        private final BaseSword base;
        private final SwordPrefix prefix;
        private final String displayName;
        private final double totalDamage;
        private final Material material;

        public SwordData(String id, int number, BaseSword base, SwordPrefix prefix) {
            this.id = id;
            this.number = number;
            this.base = base;
            this.prefix = prefix;
            this.displayName = "&6" + prefix.getName() + " " + base.getName();
            this.totalDamage = base.getDamage() + prefix.getBonusDamage();
            // Upgrade material if high tier prefix
            if (prefix.getBonusDamage() >= 2.5 && base.getMaterial() == Material.IRON_SWORD) {
                this.material = Material.DIAMOND_SWORD;
            } else {
                this.material = base.getMaterial();
            }
        }

        public String getId() { return id; }
        public int getNumber() { return number; }
        public BaseSword getBase() { return base; }
        public SwordPrefix getPrefix() { return prefix; }
        public String getDisplayName() { return displayName; }
        public double getTotalDamage() { return totalDamage; }
        public Material getMaterial() { return material; }

        public ItemStack createItemStack() {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(MessageUtils.color(displayName));
                List<String> lore = new ArrayList<>();
                lore.add(MessageUtils.color("&8&m------------------------"));
                lore.add(MessageUtils.color("&7No'e Silah: &e" + base.getName()));
                lore.add(MessageUtils.color("&7Pishvand: &a" + prefix.getName()));
                lore.add(MessageUtils.color("&7Ghodrat-e Zarbeh: &c+" + String.format("%.1f", totalDamage) + " Damage"));
                lore.add(MessageUtils.color("&8&o" + base.getDescription()));
                lore.add(MessageUtils.color("&d&o" + prefix.getLore()));
                lore.add(MessageUtils.color("&8&m------------------------"));
                lore.add(MessageUtils.color("&6[Shamshir-e Irani] &7ID: &e" + id + " &7(#" + number + "/900)"));
                lore.add(MessageUtils.color("&aAsil-e Irani &7- &bKhalij-e Hameshe Fars"));
                meta.setLore(lore);

                if (prefix.getEnchantment() != null && prefix.getEnchantLevel() > 0) {
                    meta.addEnchant(prefix.getEnchantment(), prefix.getEnchantLevel(), true);
                }
                if (totalDamage >= 11.0) {
                    meta.addEnchant(Enchantment.DURABILITY, 3, true);
                }
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                // Resource pack model override predicate (1.12.2)
                if (material == Material.DIAMOND_SWORD) {
                    short modelId = (short) ((number % 4) + 1);
                    item.setDurability(modelId);
                    meta.setUnbreakable(true);
                    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                }

                item.setItemMeta(meta);
            }
            return item;
        }
    }

    private static final List<BaseSword> BASES = new ArrayList<>();
    private static final List<SwordPrefix> PREFIXES = new ArrayList<>();
    private static final Map<String, SwordData> SWORDS_BY_ID = new HashMap<>();
    private static final List<SwordData> ALL_SWORDS = new ArrayList<>();
    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) return;

        // 30 Bases
        BASES.add(new BaseSword(1, "SHAMSHIR", "Shamshir", Material.IRON_SWORD, 6.0, "Shamshir-e khamideh-ye asil-e Irani"));
        BASES.add(new BaseSword(2, "ZOLFAGHAR", "Zolfaghar", Material.DIAMOND_SWORD, 9.0, "Shamshir-e do-shakheh-ye afsanehi"));
        BASES.add(new BaseSword(3, "GHADAREH", "Ghadareh", Material.IRON_SWORD, 7.0, "Tigh-e pahn va sangin-e pahlavanan"));
        BASES.add(new BaseSword(4, "KHANJAR", "Khanjar", Material.IRON_SWORD, 5.0, "Khanjar-e tiz-e kamoodeh-ye Irani"));
        BASES.add(new BaseSword(5, "SEIF", "Seif", Material.GOLD_SWORD, 6.5, "Shamshir-e zarrin-e shahi"));
        BASES.add(new BaseSword(6, "GORZ_GERANSAR", "Gorz-e Geransar", Material.IRON_SWORD, 8.5, "Gorz-e sangin ba sar-e gavmeshi"));
        BASES.add(new BaseSword(7, "TABARZIN", "Tabarzin", Material.IRON_SWORD, 8.0, "Tabar-e jangi-ye do-labeh"));
        BASES.add(new BaseSword(8, "NEYZEH", "Neyzeh", Material.IRON_SWORD, 7.5, "Neyzeh-ye fooladin-e sarbazan"));
        BASES.add(new BaseSword(9, "AKINAKEH", "Akinakeh", Material.GOLD_SWORD, 6.0, "Shamshir-e kootah-e Hakhamaneshi"));
        BASES.add(new BaseSword(10, "TIGH_PAHLAVANI", "Tigh-e Pahlavani", Material.DIAMOND_SWORD, 8.5, "Tigh-e shaja'at-e pahlavanan"));
        BASES.add(new BaseSword(11, "KARD_ZANJANI", "Kard-e Zanjani", Material.IRON_SWORD, 6.5, "Foolad-e ab-dideh-ye Zanjan"));
        BASES.add(new BaseSword(12, "SHAMSHIR_HAKHAMANESHI", "Shamshir-e Hakhamaneshi", Material.DIAMOND_SWORD, 9.0, "Shamshir-e emperatori-ye Pars"));
        BASES.add(new BaseSword(13, "SHAMSHIR_SASANI", "Shamshir-e Sasani", Material.DIAMOND_SWORD, 8.5, "Tigh-e savar-nezam-e javidan"));
        BASES.add(new BaseSword(14, "SHAMSHIR_SAFAVI", "Shamshir-e Safavi", Material.DIAMOND_SWORD, 8.0, "Shamshir-e foolad-e jowhardar"));
        BASES.add(new BaseSword(15, "SHAMSHIR_AFSHARI", "Shamshir-e Afshari", Material.DIAMOND_SWORD, 9.5, "Shamshir-e fatah-e Nader Shah"));
        BASES.add(new BaseSword(16, "SHAMSHIR_ASHKANI", "Shamshir-e Ashkani", Material.IRON_SWORD, 7.5, "Tigh-e savaran-e Part"));
        BASES.add(new BaseSword(17, "SHAMSHIR_DEYLAMI", "Shamshir-e Deylami", Material.IRON_SWORD, 8.0, "Shamshir-e do-dasteh-ye Alborz"));
        BASES.add(new BaseSword(18, "KHANJAR_SHAHI", "Khanjar-e Shahi", Material.GOLD_SWORD, 6.5, "Khanjar-e morassa be javaher"));
        BASES.add(new BaseSword(19, "GORZ_ROSTAM", "Gorz-e Rostam", Material.DIAMOND_SWORD, 10.0, "Gorz-e pahlavan-e Sistan"));
        BASES.add(new BaseSword(20, "TIGH_DAMAVAND", "Tigh-e Damavand", Material.DIAMOND_SWORD, 8.5, "Sang-e atashfeshani-ye Damavand"));
        BASES.add(new BaseSword(21, "SHAMSHIR_ALBORZ", "Shamshir-e Alborz", Material.IRON_SWORD, 7.5, "Foolad-e sakht-e kohestan"));
        BASES.add(new BaseSword(22, "KARD_KHORASANI", "Kard-e Khorasani", Material.IRON_SWORD, 6.5, "Kard-e tiz-e Toos va Neyshaboor"));
        BASES.add(new BaseSword(23, "SHAMSHIR_SHUSHTARI", "Shamshir-e Shushtari", Material.IRON_SWORD, 7.0, "Foolad-e chekosh-khordeh-ye Shushtar"));
        BASES.add(new BaseSword(24, "TIGH_JAVIDAN", "Tigh-e Javidan", Material.DIAMOND_SWORD, 9.0, "Silah-e sepah-e javidan"));
        BASES.add(new BaseSword(25, "SHAMSHIR_ARYAEE", "Shamshir-e Aryaee", Material.DIAMOND_SWORD, 8.5, "Shamshir-e kohan-e Aryaei"));
        BASES.add(new BaseSword(26, "NEYZEH_SEPAH", "Neyzeh-ye Sepah", Material.IRON_SWORD, 7.0, "Neyzeh-ye boland-e saf-shekan"));
        BASES.add(new BaseSword(27, "TABAR_KORDI", "Tabar-e Kordi", Material.IRON_SWORD, 8.0, "Tabar-e jangjooyan-e Zagros"));
        BASES.add(new BaseSword(28, "GHADAREH_LURI", "Ghadareh-ye Luri", Material.IRON_SWORD, 7.5, "Berenj va foolad-e Lorestan"));
        BASES.add(new BaseSword(29, "KHANJAR_BALOCHI", "Khanjar-e Balochi", Material.IRON_SWORD, 6.0, "Khanjar-e tond-e kavir"));
        BASES.add(new BaseSword(30, "SHAMSHIR_SIRAF", "Shamshir-e Siraf", Material.DIAMOND_SWORD, 8.0, "Tigh-e daryanavardane Khalij-e Fars"));

        // 30 Prefixes
        PREFIXES.add(new SwordPrefix(1, "Atashin", 2.0, Enchantment.FIRE_ASPECT, 2, "Sho'levar az atash-e Azargoshnasp"));
        PREFIXES.add(new SwordPrefix(2, "Yakhi", 1.5, Enchantment.DAMAGE_ALL, 1, "Sard mesle gholleh-ye Damavand"));
        PREFIXES.add(new SwordPrefix(3, "Zahri", 1.5, Enchantment.DAMAGE_ARTHROPODS, 2, "Zahr-e mar-haye kavir"));
        PREFIXES.add(new SwordPrefix(4, "Rostam", 3.0, Enchantment.KNOCKBACK, 2, "Nirooye bi-payan-e Pahlavan-e Zabol"));
        PREFIXES.add(new SwordPrefix(5, "Kourosh", 2.5, Enchantment.DURABILITY, 3, "Edalat va shokooh-e Manshoor-e Kourosh"));
        PREFIXES.add(new SwordPrefix(6, "Dariush", 2.5, Enchantment.DAMAGE_ALL, 2, "Ghodrat-e shahi az Parseh ta darya"));
        PREFIXES.add(new SwordPrefix(7, "Arash", 2.0, Enchantment.DAMAGE_ALL, 2, "Darbareh-ye partab va shekar"));
        PREFIXES.add(new SwordPrefix(8, "Shahanshahi", 3.0, Enchantment.DURABILITY, 3, "Zinat-e kakh-haye Hakhamaneshi"));
        PREFIXES.add(new SwordPrefix(9, "Alborz", 2.0, Enchantment.DURABILITY, 2, "Esteghemat-e kohestan-e boland"));
        PREFIXES.add(new SwordPrefix(10, "Zagros", 2.5, Enchantment.DAMAGE_ALL, 2, "Solbat-e sang-haye Zagros"));
        PREFIXES.add(new SwordPrefix(11, "Pahlavani", 2.0, Enchantment.KNOCKBACK, 1, "Rasm-e javonmardi va zoorkhaneh"));
        PREFIXES.add(new SwordPrefix(12, "Div-Kosh", 3.0, Enchantment.DAMAGE_UNDEAD, 3, "Khoshoonant alayhe divan va ahreemanan"));
        PREFIXES.add(new SwordPrefix(13, "Moqaddas", 2.0, Enchantment.DAMAGE_UNDEAD, 2, "Pak-kardeh ba atash-e moqaddas"));
        PREFIXES.add(new SwordPrefix(14, "Fooladin", 2.0, Enchantment.DURABILITY, 3, "Ab-dideh dar kanoon-e ahangari"));
        PREFIXES.add(new SwordPrefix(15, "Barq-Asa", 3.5, Enchantment.DAMAGE_ALL, 3, "Tondtar az rayeheh-ye sa'egheh"));
        PREFIXES.add(new SwordPrefix(16, "Khun-Riz", 2.5, Enchantment.DAMAGE_ALL, 2, "Zakhmdar-konandeh dar meydan-e nabard"));
        PREFIXES.add(new SwordPrefix(17, "Javdan", 3.0, Enchantment.DURABILITY, 3, "Hargez khasteh va shekasteh nemishavad"));
        PREFIXES.add(new SwordPrefix(18, "Damavand", 3.0, Enchantment.FIRE_ASPECT, 2, "Khoroosh-e atashfashan-e kohan"));
        PREFIXES.add(new SwordPrefix(19, "Toofani", 2.0, Enchantment.SWEEPING_EDGE, 2, "Mowj-e toofande-ye bad-haye 120 roozeh"));
        PREFIXES.add(new SwordPrefix(20, "Firoozeh-ei", 2.0, Enchantment.DURABILITY, 2, "Rang-e firoozeh-ye Neyshaboor"));
        PREFIXES.add(new SwordPrefix(21, "Yaghoot-Nishan", 2.5, Enchantment.DAMAGE_ALL, 2, "Yaghoot-e derakhshan-e Badakhshan"));
        PREFIXES.add(new SwordPrefix(22, "Zomorrod-Rang", 1.5, Enchantment.LOOT_BONUS_MOBS, 2, "Barakat va kheyrat-e zomorrod"));
        PREFIXES.add(new SwordPrefix(23, "Talaei", 1.5, Enchantment.DURABILITY, 1, "Sekkeh-ye Derik-e zar-afshan"));
        PREFIXES.add(new SwordPrefix(24, "Zartoshti", 2.5, Enchantment.FIRE_ASPECT, 1, "Roozgar-e Avesta va noor"));
        PREFIXES.add(new SwordPrefix(25, "Mithraei", 2.5, Enchantment.DAMAGE_ALL, 2, "Aein-e Mehr va partov-e khorshid"));
        PREFIXES.add(new SwordPrefix(26, "Sasanian", 2.5, Enchantment.KNOCKBACK, 2, "Heibat-e Artesh-e Sasani"));
        PREFIXES.add(new SwordPrefix(27, "Hakhamaneshi", 3.0, Enchantment.DAMAGE_ALL, 3, "Paye-gozar-e nakhostin emperatori"));
        PREFIXES.add(new SwordPrefix(28, "Safavid", 2.0, Enchantment.SWEEPING_EDGE, 2, "Honar-e ahangaran-e Isfahan"));
        PREFIXES.add(new SwordPrefix(29, "Kaveh", 3.0, Enchantment.KNOCKBACK, 2, "Derafsh-e Kaviani va khoroosh-e kargar"));
        PREFIXES.add(new SwordPrefix(30, "Simurgh", 3.0, Enchantment.DURABILITY, 3, "Par-e afsanehi bar gholleh-ye Qaf"));

        // Generate all 900 combinations
        int counter = 1;
        for (SwordPrefix prefix : PREFIXES) {
            for (BaseSword base : BASES) {
                String id = String.format("SWORD_%03d", counter);
                SwordData sword = new SwordData(id, counter, base, prefix);
                SWORDS_BY_ID.put(id, sword);
                ALL_SWORDS.add(sword);
                counter++;
            }
        }

        initialized = true;
    }

    public static SwordData getById(String id) {
        if (!initialized) init();
        return SWORDS_BY_ID.get(id.toUpperCase());
    }

    public static SwordData getByNumber(int number) {
        if (!initialized) init();
        if (number < 1 || number > ALL_SWORDS.size()) return null;
        return ALL_SWORDS.get(number - 1);
    }

    public static List<SwordData> getAllSwords() {
        if (!initialized) init();
        return Collections.unmodifiableList(ALL_SWORDS);
    }

    public static SwordData getRandomSword() {
        if (!initialized) init();
        int idx = new Random().nextInt(ALL_SWORDS.size());
        return ALL_SWORDS.get(idx);
    }

    public static int getTotalCount() {
        if (!initialized) init();
        return ALL_SWORDS.size();
    }
}

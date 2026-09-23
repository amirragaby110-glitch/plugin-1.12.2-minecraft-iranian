package ir.iranian.hardcore.army;

import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Historical Persian Military Soldier Classes (Achaemenid & Sassanid).
 */
public enum SoldierClass {

    PERSIAN_GUARD(
        "Javidan (Immortal Guard)",
        "Sarbaz-e Javidan-e Sepah-e Hakhamaneshi ba zereh-ye zarrin",
        45.0, 8.0, 0.30,
        Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.IRON_LEGGINGS, Material.GOLD_BOOTS,
        Material.DIAMOND_SWORD, Material.SHIELD
    ),
    SPEARMAN(
        "Neyzehdar-e Mad",
        "Piyadeh-nezam-e neyzeh-dar ba bord-e zarbeh-ye toolani",
        35.0, 6.0, 0.32,
        Material.IRON_HELMET, Material.LEATHER_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.IRON_SWORD, null
    ),
    ARCHER(
        "Kamandar-e Pars",
        "Kamandar-e tir-andaz ba kaman-e torkibi-ye Hakhamaneshi",
        30.0, 7.0, 0.33,
        Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
        Material.BOW, null
    ),
    SHIELD_BEARER(
        "Separdar-e Sangin",
        "Sarbaz-e hefazat-e khate moghadam ba separ-e bozorg",
        60.0, 5.0, 0.26,
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.IRON_SWORD, Material.SHIELD
    ),
    CAVALRY(
        "Savaran-e Asil",
        "Savar-nezam-e chabok-dast ba sor'at va hamleh-ye ghati'",
        50.0, 10.0, 0.38,
        Material.GOLD_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.IRON_SWORD, Material.SHIELD
    ),
    ROYAL_GUARD(
        "Gord-e Shahanshahi",
        "Nirooye vizheh va nohbeye darbar-e Hakhamaneshi",
        80.0, 14.0, 0.32,
        Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
        Material.DIAMOND_SWORD, Material.SHIELD
    ),
    COMMANDER(
        "Sepahbod-e Artesh",
        "Farmandeh-ye bozorg-e jabbhe ba ghodrat-e taghviat-e sarbazan",
        100.0, 16.0, 0.34,
        Material.GOLD_HELMET, Material.DIAMOND_CHESTPLATE, Material.GOLD_LEGGINGS, Material.DIAMOND_BOOTS,
        Material.DIAMOND_SWORD, Material.SHIELD
    );

    private final String displayName;
    private final String description;
    private final double baseHealth;
    private final double baseDamage;
    private final double baseSpeed;
    private final Material helmet;
    private final Material chestplate;
    private final Material leggings;
    private final Material boots;
    private final Material mainHand;
    private final Material offHand;

    SoldierClass(String displayName, String description, double baseHealth, double baseDamage, double baseSpeed,
                 Material helmet, Material chestplate, Material leggings, Material boots,
                 Material mainHand, Material offHand) {
        this.displayName = displayName;
        this.description = description;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
        this.baseSpeed = baseSpeed;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.mainHand = mainHand;
        this.offHand = offHand;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public double getBaseHealth() { return baseHealth; }
    public double getBaseDamage() { return baseDamage; }
    public double getBaseSpeed() { return baseSpeed; }
    public Material getHelmet() { return helmet; }
    public Material getChestplate() { return chestplate; }
    public Material getLeggings() { return leggings; }
    public Material getBoots() { return boots; }
    public Material getMainHand() { return mainHand; }
    public Material getOffHand() { return offHand; }
}

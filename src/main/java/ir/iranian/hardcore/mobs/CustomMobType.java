package ir.iranian.hardcore.mobs;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/**
 * 30 Custom Iranian Mobs & Bosses (v5.0 Finglish)
 * Based on Shahnameh, Iranian mythology and ancient Persian history.
 * Completely Finglish, zero Persian Unicode characters.
 */
public enum CustomMobType {

    DIV_SEPID("DIV_SEPID", "Div Sepid", "Div-e Sefid - Mazandaran Giant Demon", EntityType.ZOMBIE, Material.BONE, 50.0, 9.0,
            Arrays.asList("&f&lDiv Sepid - White Demon", "&7Ghavi tarin Div dar Shahnameh Ferdowsi", "&7Dar kooh-haye Alborz va Mazandaran"),
            Arrays.asList(new MobEffect(PotionEffectType.BLINDNESS, 100, 0, 0.4), new MobEffect(PotionEffectType.SLOW, 100, 1, 0.5)),
            Arrays.asList(Material.BONE, Material.ROTTEN_FLESH, Material.GOLD_INGOT, Material.DIAMOND)),

    DIV_SIAH("DIV_SIAH", "Div Siah", "Black Desert Demon", EntityType.HUSK, Material.COAL, 30.0, 7.0,
            Arrays.asList("&8&lDiv Siah - Black Demon", "&7Div-e sari va khatarnak dar kavir"),
            Arrays.asList(new MobEffect(PotionEffectType.POISON, 80, 0, 0.4), new MobEffect(PotionEffectType.SPEED, 200, 1, 1.0)),
            Arrays.asList(Material.COAL, Material.GOLD_NUGGET)),

    SIMURGH("SIMURGH", "Simurgh", "Celestial Mythical Bird", EntityType.CHICKEN, Material.FEATHER, 35.0, 4.0,
            Arrays.asList("&6&lSimurgh - Morgh-e Afsanehi", "&7Parandeh-ye dana va shafabakhsh bar faraz-e Alborz"),
            Arrays.asList(new MobEffect(PotionEffectType.REGENERATION, 200, 1, 0.6), new MobEffect(PotionEffectType.SPEED, 200, 1, 0.5)),
            Arrays.asList(Material.FEATHER, Material.GOLD_INGOT, Material.GOLDEN_APPLE)),

    ZAHHAK("ZAHHAK", "Zahhak Mar-Doosh", "Tyrant with shoulder snakes", EntityType.ZOMBIE_VILLAGER, Material.SKULL_ITEM, 65.0, 11.0,
            Arrays.asList("&4&lZahhak Mar-Doosh - Tyrant King", "&7Padeshah-e sitamgar ba do mar bar doosh"),
            Arrays.asList(new MobEffect(PotionEffectType.POISON, 120, 1, 0.6), new MobEffect(PotionEffectType.WITHER, 80, 0, 0.4)),
            Arrays.asList(Material.SKULL_ITEM, Material.GOLD_BLOCK, Material.DIAMOND)),

    ROSTAM_GHOST("ROSTAM_GHOST", "Rooh-e Rostam Dastan", "Ghost of champion Rostam", EntityType.SKELETON, Material.IRON_SWORD, 75.0, 13.0,
            Arrays.asList("&6&lRooh-e Rostam Dastan", "&7Bozorgtarin pahlavan-e tarikh-e Iran"),
            Arrays.asList(new MobEffect(PotionEffectType.INCREASE_DAMAGE, 1000, 1, 1.0)),
            Arrays.asList(Material.IRON_SWORD, Material.DIAMOND, Material.GOLD_INGOT)),

    AL("AL", "Al-e Mordab", "Swamp Hag Demon", EntityType.WITCH, Material.REDSTONE, 25.0, 6.0,
            Arrays.asList("&5&lAl - Div-e Mordab", "&7Jadoogar-e tariki dar batlagh-ha"),
            Arrays.asList(new MobEffect(PotionEffectType.POISON, 100, 0, 0.5)),
            Arrays.asList(Material.REDSTONE, Material.POTION)),

    KAVEH("KAVEH", "Kaveh Ahangar", "Blacksmith Hero", EntityType.IRON_GOLEM, Material.ANVIL, 85.0, 15.0,
            Arrays.asList("&6&lKaveh Ahangar - Pishvaye Ghayam", "&7Pahlavan-e moqadas va doost-e bazi-konan"),
            Arrays.asList(new MobEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000, 2, 1.0)),
            Arrays.asList(Material.IRON_INGOT, Material.GOLD_INGOT, Material.ANVIL)),

    AFRASIAB("AFRASIAB", "Afrasiab Toorani", "King of Turan - Boss", EntityType.WITHER_SKELETON, Material.DIAMOND_SWORD, 80.0, 12.0,
            Arrays.asList("&c&lAfrasiab - Padeshah-e Tooran", "&7Doshman-e dirineh-ye Iran-zamin"),
            Arrays.asList(new MobEffect(PotionEffectType.WITHER, 100, 1, 0.5)),
            Arrays.asList(Material.DIAMOND, Material.NETHER_STAR, Material.GOLD_BLOCK)),

    DARIUS_GHOST("DARIUS_GHOST", "Rooh-e Dariush Bozorg", "Ghost of Darius the Great", EntityType.ZOMBIE, Material.GOLD_BLOCK, 90.0, 12.0,
            Arrays.asList("&e&lRooh-e Dariush Bozorg", "&7Shahanshah-e Hakhamaneshi dar Parseh"),
            Arrays.asList(new MobEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000, 1, 1.0)),
            Arrays.asList(Material.GOLD_BLOCK, Material.DIAMOND, Material.EMERALD)),

    CYRUS_GHOST("CYRUS_GHOST", "Rooh-e Kourosh Bozorg", "Cyrus the Great - Ancient Emperor", EntityType.ZOMBIE, Material.GOLD_INGOT, 95.0, 13.0,
            Arrays.asList("&6&lRooh-e Kourosh Bozorg", "&7Bonyan-gozar-e nakhostin emperatori"),
            Arrays.asList(new MobEffect(PotionEffectType.REGENERATION, 1000, 1, 1.0)),
            Arrays.asList(Material.GOLD_BLOCK, Material.DIAMOND, Material.GOLDEN_APPLE)),

    ARASH_KAMANGIR("ARASH_KAMANGIR", "Arash Kamangir", "Heroic Archer of Damavand", EntityType.SKELETON, Material.BOW, 50.0, 8.0,
            Arrays.asList("&a&lArash Kamangir", "&7Pahlavani ke janesh ra dar kaman gozasht"),
            Arrays.asList(new MobEffect(PotionEffectType.SPEED, 1000, 1, 1.0)),
            Arrays.asList(Material.BOW, Material.ARROW, Material.DIAMOND)),

    FEREYDOUN("FEREYDOUN", "Shah Fereydoun", "King Fereydoun who defeated Zahhak", EntityType.ZOMBIE, Material.IRON_CHESTPLATE, 70.0, 10.0,
            Arrays.asList("&b&lShah Fereydoun", "&7Padeshah-e adel va shekast-dahandeh-ye Zahhak"),
            Arrays.asList(new MobEffect(PotionEffectType.INCREASE_DAMAGE, 1000, 1, 1.0)),
            Arrays.asList(Material.GOLD_INGOT, Material.DIAMOND)),

    ZAL_ZAR("ZAL_ZAR", "Zal-e Zar", "Father of Rostam", EntityType.SKELETON, Material.FEATHER, 55.0, 9.0,
            Arrays.asList("&f&lZal-e Zar - Parvardeh-ye Simurgh", "&7Pahlavan-e mooy-sefid dar Alborz"),
            Arrays.asList(new MobEffect(PotionEffectType.REGENERATION, 1000, 0, 1.0)),
            Arrays.asList(Material.FEATHER, Material.IRON_SWORD)),

    SOHRAB("SOHRAB", "Sohrab Pahlavan", "Young Lion Warrior", EntityType.ZOMBIE, Material.IRON_SWORD, 65.0, 11.0,
            Arrays.asList("&e&lSohrab-e Delir", "&7Pahlavan-e javan va delavar"),
            Arrays.asList(new MobEffect(PotionEffectType.SPEED, 1000, 1, 1.0)),
            Arrays.asList(Material.IRON_SWORD, Material.GOLD_INGOT)),

    ESFANDIAR("ESFANDIAR", "Esfandiar Royin-Tan", "Invulnerable Prince", EntityType.IRON_GOLEM, Material.DIAMOND_CHESTPLATE, 90.0, 14.0,
            Arrays.asList("&9&lEsfandiar Royin-Tan", "&7Pahlavan-e royin-tan ke tigh bar tanesh karsaz nabood"),
            Arrays.asList(new MobEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000, 3, 1.0)),
            Arrays.asList(Material.DIAMOND, Material.GOLD_BLOCK)),

    SARAB_KAVIR("SARAB_KAVIR", "Sarab-e Kavir", "Desert Mirage Wraith", EntityType.HUSK, Material.SAND, 35.0, 7.0,
            Arrays.asList("&e&lSarab-e Kavir", "&7Frib-dahandeh-ye mosaferan dar biaban"),
            Arrays.asList(new MobEffect(PotionEffectType.CONFUSION, 80, 0, 0.5)),
            Arrays.asList(Material.SAND, Material.GOLD_NUGGET)),

    GORG_KOHESTAN("GORG_KOHESTAN", "Gorg-e Kohestan", "Zagros Mountain Dire Wolf", EntityType.WOLF, Material.BONE, 30.0, 6.0,
            Arrays.asList("&7&lGorg-e Kohestan", "&7Gorg-e vahshi dar saf-haye Zagros"),
            Arrays.asList(new MobEffect(PotionEffectType.SPEED, 1000, 1, 1.0)),
            Arrays.asList(Material.BONE, Material.LEATHER)),

    BABR_MAZANDARAN("BABR_MAZANDARAN", "Babr-e Mazandaran", "Caspian Tiger", EntityType.OCELOT, Material.RAW_FISH, 45.0, 8.0,
            Arrays.asList("&6&lBabr-e Mazandaran", "&7Soltan-e jangalat-e Hirkani"),
            Arrays.asList(new MobEffect(PotionEffectType.SPEED, 1000, 2, 1.0)),
            Arrays.asList(Material.LEATHER, Material.RAW_FISH, Material.GOLD_INGOT)),

    YOOZPALANG_IRANI("YOOZPALANG_IRANI", "Yoozpalang-e Irani", "Asiatic Cheetah", EntityType.OCELOT, Material.RAW_BEEF, 35.0, 6.0,
            Arrays.asList("&e&lYoozpalang-e Irani", "&7Sari-tarin shekarchi-ye kavir"),
            Arrays.asList(new MobEffect(PotionEffectType.SPEED, 1000, 3, 1.0)),
            Arrays.asList(Material.LEATHER, Material.RAW_BEEF)),

    SHIR_IRANI("SHIR_IRANI", "Shir-e Irani", "Persian Lion", EntityType.WOLF, Material.PORK, 50.0, 9.0,
            Arrays.asList("&6&lShir-e Irani", "&7Namad-e shokooh va delavari dar Parseh"),
            Arrays.asList(new MobEffect(PotionEffectType.INCREASE_DAMAGE, 1000, 1, 1.0)),
            Arrays.asList(Material.LEATHER, Material.GOLD_INGOT)),

    KHAR_SE_PAH("KHAR_SE_PAH", "Khar-e Se-Pah", "Three-legged Mythical Wonder", EntityType.HORSE, Material.GOLD_BARDING, 40.0, 5.0,
            Arrays.asList("&b&lKhar-e Se-Pah", "&7Mowjood-e moqadas dar kanoon-e Avesta"),
            Arrays.asList(new MobEffect(PotionEffectType.SPEED, 1000, 2, 1.0)),
            Arrays.asList(Material.GOLD_INGOT, Material.SADDLE)),

    CHAMROSH("CHAMROSH", "Chamrosh", "Avian Seed Guardian", EntityType.CHICKEN, Material.WHEAT, 30.0, 4.0,
            Arrays.asList("&a&lChamrosh", "&7Parandeh-ye mohafez-e danedan"),
            Arrays.asList(new MobEffect(PotionEffectType.REGENERATION, 1000, 0, 1.0)),
            Arrays.asList(Material.WHEAT, Material.SEEDS)),

    HUMA("HUMA", "Huma", "Bird of Fortune", EntityType.PARROT, Material.FEATHER, 25.0, 3.0,
            Arrays.asList("&e&lHuma - Parandeh-ye Sa'adat", "&7Sayeh-ye an bar sar-e har kas oftad padeshah mishavad"),
            Arrays.asList(new MobEffect(PotionEffectType.LUCK, 1000, 2, 1.0)),
            Arrays.asList(Material.FEATHER, Material.DIAMOND)),

    MARDAZMA("MARDAZMA", "Mardazma", "Night Shapeshifter Demon", EntityType.ENDERMAN, Material.ENDER_PEARL, 50.0, 9.0,
            Arrays.asList("&8&lMardazma - Shayyad-e Shab", "&7Div-e tagheer-e chehreh dar tariki"),
            Arrays.asList(new MobEffect(PotionEffectType.BLINDNESS, 60, 0, 0.4)),
            Arrays.asList(Material.ENDER_PEARL, Material.DIAMOND)),

    BOOSHASP("BOOSHASP", "Booshasp", "Demon of Sloth and Sleep", EntityType.ZOMBIE, Material.BED, 35.0, 6.0,
            Arrays.asList("&7&lBooshasp - Div-e Khabi", "&7Div-e tanbali va khab-aloodegi"),
            Arrays.asList(new MobEffect(PotionEffectType.SLOW_DIGGING, 120, 1, 0.6)),
            Arrays.asList(Material.ROTTEN_FLESH, Material.POTION)),

    NASU("NASU", "Nasu", "Carrion Cave Demon", EntityType.CAVE_SPIDER, Material.SPIDER_EYE, 25.0, 5.0,
            Arrays.asList("&8&lNasu - Div-e Mordeh-Khar", "&7Dar zolmat-e ghar-ha zendegi mikone"),
            Arrays.asList(new MobEffect(PotionEffectType.POISON, 80, 1, 0.7)),
            Arrays.asList(Material.SPIDER_EYE, Material.STRING)),

    APAOSHA("APAOSHA", "Apaosha", "Demon of Drought - Nether Boss", EntityType.BLAZE, Material.MAGMA_CREAM, 75.0, 11.0,
            Arrays.asList("&4&lApaosha - Div-e Khoshksali", "&7Ahreeman-e tashnegi va atash"),
            Arrays.asList(new MobEffect(PotionEffectType.FIRE_RESISTANCE, 1000, 0, 1.0)),
            Arrays.asList(Material.BLAZE_ROD, Material.MAGMA_CREAM, Material.NETHER_STAR)),

    TISHTRYA("TISHTRYA", "Tishtrya", "Angel of Rain and Fertility", EntityType.VILLAGER, Material.WATER_BUCKET, 60.0, 6.0,
            Arrays.asList("&b&lTishtrya - Ized-e Baran", "&7Moqadas-e ab va baran dar barabar-e Apaosha"),
            Arrays.asList(new MobEffect(PotionEffectType.REGENERATION, 1000, 1, 1.0)),
            Arrays.asList(Material.PRISMARINE_SHARD, Material.DIAMOND)),

    SARBAZ_JAVIDAN("SARBAZ_JAVIDAN", "Sarbaz-e Javidan", "Immortal Persian Guard", EntityType.ZOMBIE, Material.GOLD_SWORD, 55.0, 9.0,
            Arrays.asList("&e&lSarbaz-e Javidan - Ten Thousand Immortals", "&7Elite soldier of the Achaemenid Empire"),
            Arrays.asList(new MobEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000, 1, 1.0)),
            Arrays.asList(Material.GOLD_SWORD, Material.GOLD_INGOT, Material.IRON_INGOT)),

    HASHASHIN_ALAMOAT("HASHASHIN_ALAMOAT", "Hashashin-e Alamoat", "Assassin of Alamut Castle", EntityType.SKELETON, Material.IRON_SWORD, 45.0, 10.0,
            Arrays.asList("&8&lHashashin-e Alamoat - Eagle Assassin", "&7Fadaie-ye Ghaleh-ye Alamoat dar Alborz"),
            Arrays.asList(new MobEffect(PotionEffectType.SPEED, 1000, 2, 1.0)),
            Arrays.asList(Material.IRON_SWORD, Material.EMERALD, Material.BOW));

    private final String id;
    private final String finglishName;
    private final String englishDesc;
    private final EntityType baseType;
    private final Material icon;
    private final double health;
    private final double damage;
    private final List<String> lore;
    private final List<MobEffect> effects;
    private final List<Material> drops;

    CustomMobType(String id, String finglishName, String englishDesc, EntityType baseType, Material icon, double health, double damage, List<String> lore, List<MobEffect> effects, List<Material> drops) {
        this.id = id;
        this.finglishName = finglishName;
        this.englishDesc = englishDesc;
        this.baseType = baseType;
        this.icon = icon;
        this.health = health;
        this.damage = damage;
        this.lore = lore;
        this.effects = effects;
        this.drops = drops;
    }

    public String getId() { return id; }
    public String getFinglishName() { return finglishName; }
    public String getEnglishDesc() { return englishDesc; }
    public EntityType getBaseType() { return baseType; }
    public Material getIcon() { return icon; }
    public double getHealth() { return health; }
    public double getDamage() { return damage; }
    public List<String> getLore() { return lore; }
    public List<MobEffect> getEffects() { return effects; }
    public List<Material> getDrops() { return drops; }

    public static class MobEffect {
        private final PotionEffectType type;
        private final int duration;
        private final int amplifier;
        private final double chance;

        public MobEffect(PotionEffectType type, int duration, int amplifier, double chance) {
            this.type = type;
            this.duration = duration;
            this.amplifier = amplifier;
            this.chance = chance;
        }

        public PotionEffectType getType() { return type; }
        public int getDuration() { return duration; }
        public int getAmplifier() { return amplifier; }
        public double getChance() { return chance; }
    }
}

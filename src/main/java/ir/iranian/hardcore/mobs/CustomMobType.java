package ir.iranian.hardcore.mobs;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/**
 * Custom Iranian Mobs - v4.0
 * Based on Persian mythology and history
 * Each mob has unique abilities and drops
 */
public enum CustomMobType {

    // ========== DIV (Demon) - Persian Mythology ==========
    DIV_SEPID(
            "DIV_SEPID",
            "Div Sepid",
            "دیو سپید",
            "Div-e Sefid - White Demon from Shahnameh",
            EntityType.ZOMBIE,
            Material.BONE,
            40.0, // Health
            8.0, // Damage
            Arrays.asList(
                    "&f&l☠ Div Sepid - White Demon",
                    "&7Div-e bozorg az Shahnameh Ferdowsi",
                    "&7Dar ghare Mazandaran zendegi mikonad",
                    "&7Ghavi tarin Div-e Iran",
                    "",
                    "&c&lGhodrat:",
                    "&7- Salamati: &f40 HP",
                    "&7- Zarbe: &f8 damage",
                    "&7- Asar: &fKori + Kendi",
                    "&7- Vizhegi: &fDar shab ghavi tar",
                    "",
                    "&e&lLoot: &fOstokhane Div, Tala, Ketab jadoo",
                    "&8&lMakan: &7Kohestan, Shab, Ghar"
            ),
            Arrays.asList(
                    new MobEffect(PotionEffectType.BLINDNESS, 100, 0, 0.3),
                    new MobEffect(PotionEffectType.SLOW, 100, 1, 0.5)
            ),
            Arrays.asList(
                    Material.BONE, Material.ROTTEN_FLESH, Material.GOLD_INGOT
            )
    ),

    DIV_SIAH(
            "DIV_SIAH",
            "Div Siah",
            "دیو سیاه",
            "Black Demon - Small but fast",
            EntityType.HUSK,
            Material.COAL,
            25.0,
            6.0,
            Arrays.asList(
                    "&8&l☠ Div Siah - Black Demon",
                    "&7Div-e kochak vali sari",
                    "&7Dar kavir zendegi mikonad",
                    "",
                    "&c&lGhodrat: Sari, Zahr",
                    "&e&lLoot: Zoghal, Tala"
            ),
            Arrays.asList(
                    new MobEffect(PotionEffectType.POISON, 80, 0, 0.4),
                    new MobEffect(PotionEffectType.SPEED, 200, 1, 1.0)
            ),
            Arrays.asList(Material.COAL, Material.GOLD_NUGGET)
    ),

    // ========== SIMURGH - Mythical Bird ==========
    SIMURGH(
            "SIMURGH",
            "Simurgh",
            "سیمرغ",
            "Simurgh - Legendary bird from Shahnameh, healer of Zal",
            EntityType.CHICKEN,
            Material.FEATHER,
            30.0,
            4.0,
            Arrays.asList(
                    "&6&l🦅 Simurgh - Morgh afsaneie",
                    "&7Simorgh, morgh bozorg va danaye Shahnameh",
                    "&7Pedare Zal ra dar Alborz bozorg kard",
                    "&7Neshane kherad va danesh",
                    "",
                    "&a&lGhodrat:",
                    "&7- Parvaz, Shafa dadan",
                    "&7- Dar rooz zaher mishavad",
                    "&7- Be bazi konan komak mikonad",
                    "",
                    "&e&lLoot: Par Simurgh, Tala, Sib talayi",
                    "&8&lMakan: &7Koh Alborz, Aseman"
            ),
            Arrays.asList(
                    new MobEffect(PotionEffectType.REGENERATION, 200, 1, 0.5),
                    new MobEffect(PotionEffectType.SPEED, 200, 1, 0.3)
            ),
            Arrays.asList(Material.FEATHER, Material.GOLD_INGOT, Material.GOLDEN_APPLE)
    ),

    // ========== ZAHHAK - Evil King with snakes ==========
    ZAHHAK(
            "ZAHHAK",
            "Zahhak",
            "ضحاک",
            "Zahhak - Evil king with two snakes on shoulders",
            EntityType.ZOMBIE_VILLAGER,
            Material.SKULL_ITEM,
            50.0,
            10.0,
            Arrays.asList(
                    "&4&l🐍 Zahhak - Padeshah zalem",
                    "&7Zahhak, padeshah ba do mar bar dosh",
                    "&7Har rooz maghz 2 javan ra mikhord",
                    "&7Ta Fereydoun o ra shekast dad",
                    "",
                    "&4&lGhodrat:",
                    "&7- Salamati: 50 HP",
                    "&7- Zarbe: 10 + Zahr mar",
                    "&7- 2 mar hamrah",
                    "&7- Atash",
                    "",
                    "&e&lLoot: Taj Zahhak, Shamshir mar, Tala faravan",
                    "&8&lMakan: &7Ghasr, Zendan"
            ),
            Arrays.asList(
                    new MobEffect(PotionEffectType.POISON, 120, 1, 0.6),
                    new MobEffect(PotionEffectType.WITHER, 80, 0, 0.3),
                    new MobEffect(PotionEffectType.FIRE_RESISTANCE, 1000, 0, 1.0)
            ),
            Arrays.asList(Material.SKULL_ITEM, Material.GOLD_BLOCK, Material.DIAMOND)
    ),

    // ========== ROSTAM - Hero (Friendly?) ==========
    ROSTAM_GHOST(
            "ROSTAM_GHOST",
            "Rostam Ghost",
            "روح رستم",
            "Ghost of Rostam - Greatest hero of Iran",
            EntityType.SKELETON,
            Material.IRON_SWORD,
            60.0,
            12.0,
            Arrays.asList(
                    "&6&l⚔ Rooh-e Rostam - Pahlavane Iran",
                    "&7Rostam Dastan, bozorgtarin pahlavane Iran",
                    "&7Pesare Zal va Simurgh",
                    "&7Koshande Div Sepid va Esfandiar",
                    "",
                    "&6&lGhodrat: Besiar ghavi",
                    "&e&lLoot: Shamshir Rostam, Zereh, Tala",
                    "&8&lMakan: &7Zabolestan, Koh"
            ),
            Arrays.asList(
                    new MobEffect(PotionEffectType.INCREASE_DAMAGE, 1000, 1, 1.0),
                    new MobEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000, 1, 1.0)
            ),
            Arrays.asList(Material.IRON_SWORD, Material.DIAMOND, Material.GOLD_INGOT)
    ),

    // ========== AL - Female demon that attacks pregnant women (myth) ==========
    AL(
            "AL",
            "Al",
            "آل",
            "Al - Female demon from Iranian folklore",
            EntityType.WITCH,
            Material.REDSTONE,
            20.0,
            5.0,
            Arrays.asList(
                    "&5&l👹 Al - Div-e zan",
                    "&7Al, Div-e zan dar afsanehaye Irani",
                    "&7Be zanane bardar hamle mikonad",
                    "",
                    "&c&lGhodrat: Jadoo, Zahr",
                    "&e&lLoot: Redstone, Potion"
            ),
            Arrays.asList(
                    new MobEffect(PotionEffectType.POISON, 100, 0, 0.5),
                    new MobEffect(PotionEffectType.WEAKNESS, 100, 0, 0.4)
            ),
            Arrays.asList(Material.REDSTONE, Material.POTION)
    ),

    // ========== KAVEH - Blacksmith hero (friendly) ==========
    KAVEH(
            "KAVEH",
            "Kaveh Ahangar",
            "کاوه آهنگر",
            "Kaveh the Blacksmith - Hero who fought Zahhak",
            EntityType.IRON_GOLEM,
            Material.ANVIL,
            80.0,
            15.0,
            Arrays.asList(
                    "&6&l🔨 Kaveh Ahangar - Ghahramane melli",
                    "&7Kaveh Ahangar, pishvaye ghiam bar Zahaak",
                    "&7Derafsh Kaviani ra sakht",
                    "&7Neshane adalat khahi",
                    "",
                    "&a&lGhahraman - Be shoma hamle nemikonad",
                    "&7Agar be Zahhak nazdik shavad, be o hamle mikonad",
                    "&e&lLoot: Derafsh Kaviani, Ahan, Tala"
            ),
            Arrays.asList(
                    new MobEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000, 2, 1.0),
                    new MobEffect(PotionEffectType.FIRE_RESISTANCE, 1000, 0, 1.0)
            ),
            Arrays.asList(Material.IRON_INGOT, Material.GOLD_INGOT, Material.ANVIL)
    );

    private final String id;
    private final String finglishName;
    private final String persianName;
    private final String englishDesc;
    private final EntityType baseType;
    private final Material icon;
    private final double health;
    private final double damage;
    private final List<String> lore;
    private final List<MobEffect> effects;
    private final List<Material> drops;

    CustomMobType(String id, String finglishName, String persianName, String englishDesc, EntityType baseType, Material icon, double health, double damage, List<String> lore, List<MobEffect> effects, List<Material> drops) {
        this.id = id;
        this.finglishName = finglishName;
        this.persianName = persianName;
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
    public String getPersianName() { return persianName; }
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

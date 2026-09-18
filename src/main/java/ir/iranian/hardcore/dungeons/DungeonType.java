package ir.iranian.hardcore.dungeons;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * انواع دانجن‌های ایرانی تاریخی
 * هر دانجن مخصوص یک بایوم و یک نژاد است
 * کاملا فارسی و بر اساس تاریخ واقعی ایران
 */
public enum DungeonType {

    ALAMUT_CASTLE(
            "ALAMUT_CASTLE",
            "قلعه الموت",
            "Alamut Castle",
            "قلعه افسانه‌ای حسن صباح در البرز",
            Material.COBBLESTONE,
            Arrays.asList(
                    Biome.EXTREME_HILLS,
                    Biome.EXTREME_HILLS_MOUNTAINS,
                    Biome.EXTREME_HILLS_PLUS,
                    Biome.SMALL_MOUNTAINS
            ),
            Arrays.asList(
                    "&8&l🏔 قلعه الموت - آشیانه عقاب",
                    "&7قلعه‌ای نفوذناپذیر بر فراز صخره‌های البرز",
                    "&7ساخته شده توسط حسن صباح، رهبر اسماعیلیان",
                    "&7در سال 1090 میلادی",
                    "",
                    "&c&lباس: &fحسن صباح - پیر کوهستان",
                    "&c&lسختی: &4★★★★★",
                    "&e&lلوت: &fشمشیر الموت، کتاب‌های اسماعیلی",
                    "",
                    "&8&lموقعیت تاریخی: &7الموت، قزوین - 2100 متر ارتفاع"
            ),
            "حسن صباح",
            5,
            25, 20, 25
    ),

    ANAHITA_TEMPLE(
            "ANAHITA_TEMPLE",
            "معبد آناهیتا",
            "Anahita Temple",
            "معبد الهه آب‌های ایران باستان",
            Material.SMOOTH_BRICK,
            Arrays.asList(
                    Biome.FOREST,
                    Biome.FOREST_HILLS,
                    Biome.BIRCH_FOREST,
                    Biome.ROOFED_FOREST,
                    Biome.JUNGLE,
                    Biome.FLOWER_FOREST
            ),
            Arrays.asList(
                    "&b&l🏛 معبد آناهیتا - الهه آب",
                    "&7بزرگترین معبد سنگی ایران، الهه آب و باروری",
                    "&7ساخته شده در دوره هخامنشی و اشکانی",
                    "&7در کنگاور، کرمانشاه",
                    "",
                    "&c&lباس: &fکاهن اعظم آناهیتا",
                    "&c&lسختی: &c★★★★☆",
                    "&e&lلوت: &fتندیس آناهیتا، مروارید مقدس",
                    "",
                    "&8&lموقعیت تاریخی: &7کنگاور، کرمانشاه - 32 ستون سنگی"
            ),
            "کاهن آناهیتا",
            4,
            30, 15, 30
    ),

    ARGE_BAM(
            "ARGE_BAM",
            "ارگ بم",
            "Arg-e Bam",
            "بزرگترین بنای خشتی جهان در کویر",
            Material.SANDSTONE,
            Arrays.asList(
                    Biome.DESERT,
                    Biome.DESERT_HILLS,
                    Biome.MESA,
                    Biome.MESA_PLATEAU
            ),
            Arrays.asList(
                    "&e&l🏜 ارگ بم - نگین کویر",
                    "&7بزرگترین سازه خشتی جهان، 180 هزار متر مربع",
                    "&7قدمت 2500 ساله، از دوره هخامنشی",
                    "&7ثبت شده در میراث جهانی یونسکو",
                    "",
                    "&c&lباس: &fحاکم بم - امیر کویر",
                    "&c&lسختی: &6★★★★★",
                    "&e&lلوت: &fفرش کرمانی، خشت طلایی بم",
                    "",
                    "&8&lموقعیت تاریخی: &7بم، کرمان - بزرگترین بنای خشتی جهان"
            ),
            "حاکم ارگ بم",
            5,
            35, 18, 35
    ),

    BANDAR_SIRAF(
            "BANDAR_SIRAF",
            "بندر سیراف",
            "Siraf Port",
            "بندر افسانه‌ای ایران در خلیج فارس",
            Material.WOOD,
            Arrays.asList(
                    Biome.OCEAN,
                    Biome.DEEP_OCEAN,
                    Biome.BEACH,
                    Biome.STONE_BEACH
            ),
            Arrays.asList(
                    "&b&l⚓ بندر سیراف - مروارید خلیج فارس",
                    "&7بزرگترین بندر ایران باستان در خلیج فارس",
                    "&7مرکز تجارت دریایی ساسانیان و عباسیان",
                    "&7با ثروتی افسانه‌ای از مروارید و ادویه",
                    "",
                    "&c&lباس: &fناخدای سیراف - دزد دریایی خلیج فارس",
                    "&c&lسختی: &b★★★★☆",
                    "&e&lلوت: &fمروارید خلیج فارس، نقشه گنج ساسانی",
                    "",
                    "&8&lموقعیت تاریخی: &7سیراف، بوشهر - خلیج همیشه فارس"
            ),
            "ناخدای سیراف",
            4,
            28, 12, 28
    ),

    GHALEH_BABAK(
            "GHALEH_BABAK",
            "قلعه بابک",
            "Babak Castle",
            "دژ تسخیرناپذیر بابک خرمدین",
            Material.SNOW_BLOCK,
            Arrays.asList(
                    Biome.ICE_PLAINS,
                    Biome.ICE_MOUNTAINS,
                    Biome.COLD_TAIGA,
                    Biome.COLD_TAIGA_HILLS,
                    Biome.ICE_PLAINS_SPIKES
            ),
            Arrays.asList(
                    "&f&l❄ قلعه بابک - دژ عقاب",
                    "&7قلعه‌ای در ارتفاع 2300 متری، خانه بابک خرمدین",
                    "&7قهرمان ملی که 22 سال در برابر اعراب جنگید",
                    "&7هرگز تسخیر نشد، فقط با خیانت سقوط کرد",
                    "",
                    "&c&lباس: &fبابک خرمدین - شیر آذربایجان",
                    "&c&lسختی: &f★★★★★",
                    "&e&lلوت: &fشمشیر بابک، پرچم سرخ خرمدینان",
                    "",
                    "&8&lموقعیت تاریخی: &7کلیبر، آذربایجان شرقی - 2300 متر ارتفاع"
            ),
            "بابک خرمدین",
            5,
            22, 20, 22
    ),

    CHOGHA_ZANBIL(
            "CHOGHA_ZANBIL",
            "زیگورات چغازنبیل",
            "Chogha Zanbil Ziggurat",
            "زیگورات 3250 ساله ایلامی",
            Material.BRICK,
            Arrays.asList(
                    Biome.SWAMPLAND,
                    Biome.SWAMPLAND_MOUNTAINS,
                    Biome.RIVER,
                    Biome.MUSHROOM_ISLAND
            ),
            Arrays.asList(
                    "&6&l🗿 زیگورات چغازنبیل - معبد ایلامی",
                    "&7بزرگترین زیگورات خارج از بین‌النهرین",
                    "&7ساخته شده توسط اونتاش-گال، شاه ایلام",
                    "&7در 1250 قبل از میلاد، 3250 سال قدمت",
                    "",
                    "&c&lباس: &fکاهن ایلامی - نگهبان اینشوشیناک",
                    "&c&lسختی: &2★★★★☆",
                    "&e&lلوت: &fلوح میخی ایلامی، خشت مقدس",
                    "",
                    "&8&lموقعیت تاریخی: &7شوش، خوزستان - میراث جهانی یونسکو"
            ),
            "کاهن چغازنبیل",
            4,
            28, 25, 28
    ),

    DAKHMEH_ZARTOSHTI(
            "DAKHMEH_ZARTOSHTI",
            "دخمه زرتشتی",
            "Tower of Silence",
            "برج خاموشان و آتشکده زرتشتی",
            Material.NETHER_BRICK,
            Collections.singletonList(
                    Biome.HELL
            ),
            Arrays.asList(
                    "&8&l🔥 دخمه زرتشتی - برج خاموشان",
                    "&7مکان مقدس زرتشتیان برای تدفین",
                    "&7و آتشکده آذرگشسب، یکی از سه آتش مقدس",
                    "&7که 700 سال روشن بود",
                    "",
                    "&c&lباس: &fموبد اعظم - نگهبان آتش",
                    "&c&lسختی: &4★★★★★",
                    "&e&lلوت: &fآتش مقدس، جام آتش",
                    "",
                    "&8&lموقعیت تاریخی: &7یزد، کرمان - آیین 3000 ساله زرتشت"
            ),
            "موبد زرتشتی",
            5,
            20, 15, 20
    ),

    TAKHT_JAMSHID_SKY(
            "TAKHT_JAMSHID_SKY",
            "تخت جمشید آسمانی",
            "Sky Persepolis",
            "کاخ آپادانای هخامنشی در آسمان",
            Material.GOLD_BLOCK,
            Collections.singletonList(
                    Biome.SKY
            ),
            Arrays.asList(
                    "&6&l👑 تخت جمشید آسمانی - پارسه",
                    "&7باشکوه‌ترین کاخ جهان باستان، پایتخت هخامنشیان",
                    "&7ساخته شده توسط داریوش، خشایارشا و اردشیر",
                    "&7با 100 ستون 20 متری در تالار آپادانا",
                    "",
                    "&c&lباس: &fداریوش بزرگ - شاه شاهان",
                    "&c&lسختی: &6★★★★★ &4&l(نهایی)",
                    "&e&lلوت: &fتاج کوروش، منشور کوروش، آکیناکه",
                    "",
                    "&8&lموقعیت تاریخی: &7مرودشت، فارس - 515 قبل از میلاد"
            ),
            "داریوش بزرگ",
            6,
            40, 30, 40
    );

    private final String id;
    private final String persianName;
    private final String englishName;
    private final String description;
    private final Material icon;
    private final List<Biome> biomes;
    private final List<String> lore;
    private final String bossName;
    private final int difficulty; // 1-6
    private final int sizeX, sizeY, sizeZ;

    DungeonType(String id, String persianName, String englishName, String description,
                Material icon, List<Biome> biomes, List<String> lore,
                String bossName, int difficulty, int sizeX, int sizeY, int sizeZ) {
        this.id = id;
        this.persianName = persianName;
        this.englishName = englishName;
        this.description = description;
        this.icon = icon;
        this.biomes = biomes;
        this.lore = lore;
        this.bossName = bossName;
        this.difficulty = difficulty;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public String getId() { return id; }
    public String getPersianName() { return persianName; }
    public String getEnglishName() { return englishName; }
    public String getDescription() { return description; }
    public Material getIcon() { return icon; }
    public List<Biome> getBiomes() { return biomes; }
    public List<String> getLore() { return lore; }
    public String getBossName() { return bossName; }
    public int getDifficulty() { return difficulty; }
    public int getSizeX() { return sizeX; }
    public int getSizeY() { return sizeY; }
    public int getSizeZ() { return sizeZ; }

    public boolean isBiomeValid(Biome biome) {
        return biomes.contains(biome);
    }

    public static DungeonType fromId(String id) {
        if (id == null) return null;
        for (DungeonType type : values()) {
            if (type.getId().equalsIgnoreCase(id)) return type;
        }
        return null;
    }

    public static DungeonType getForBiome(Biome biome) {
        for (DungeonType type : values()) {
            if (type.isBiomeValid(biome)) return type;
        }
        return null;
    }

    public String getDisplayName() {
        return "§6" + persianName + " §7(" + englishName + ")";
    }
}

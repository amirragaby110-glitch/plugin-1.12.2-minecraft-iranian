package ir.iranian.hardcore.dungeons;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * انواع دانجن‌های ایرانی تاریخی - نسخه 3.0
 * 16 دانجن برای پوشش تمام بایوم‌های ماینکرافت 1.12.2
 * هر بایوم یک دانجن ایرانی دارد - کاملا فارسی
 * بر اساس تاریخ واقعی 7000 ساله ایران
 */
public enum DungeonType {

    // ========== 8 دانجن اصلی نسخه 2.0 ==========

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
    ),

    // ========== 8 دانجن جدید نسخه 3.0 - پوشش تمام بایوم‌ها ==========

    PASARGAD_TOMB(
            "PASARGAD_TOMB",
            "آرامگاه کوروش - پاسارگاد",
            "Cyrus Tomb - Pasargad",
            "آرامگاه بنیان‌گذار ایران",
            Material.SANDSTONE,
            Arrays.asList(
                    Biome.PLAINS,
                    Biome.SAVANNA,
                    Biome.SAVANNA_PLATEAU
            ),
            Arrays.asList(
                    "&f&l⚰ آرامگاه کوروش - پاسارگاد",
                    "&7آرامگاه کوروش بزرگ، پدر ایران",
                    "&7ساده اما باشکوه، با 6 پله",
                    "&7نوشته: منم کوروش، شاه هخامنشی",
                    "",
                    "&c&lباس: &fنگهبان پاسارگاد - سرباز جاویدان",
                    "&c&lسختی: &e★★★☆☆",
                    "&e&lلوت: &fتاج کوروش، منشور حقوق بشر",
                    "",
                    "&8&lموقعیت: &7پاسارگاد، فارس - 530 قبل از میلاد"
            ),
            "نگهبان پاسارگاد",
            3,
            20, 15, 20
    ),

    BISOTUN_INSCRIPTION(
            "BISOTUN_INSCRIPTION",
            "کتیبه بیستون",
            "Bisotun Inscription",
            "بزرگترین کتیبه جهان - داریوش بزرگ",
            Material.SMOOTH_BRICK,
            Arrays.asList(
                    Biome.EXTREME_HILLS_PLUS_MOUNTAINS,
                    Biome.STONE_BEACH,
                    Biome.COLD_BEACH
            ),
            Arrays.asList(
                    "&7&l📜 کتیبه بیستون - میخی سه زبانه",
                    "&7بزرگترین کتیبه جهان، نوشته داریوش بزرگ",
                    "&7به سه زبان: پارسی باستان، ایلامی، بابلی",
                    "&7کلید رمزگشایی خط میخی",
                    "",
                    "&c&lباس: &fداریوش - نویسنده تاریخ",
                    "&c&lسختی: &7★★★★☆",
                    "&e&lلوت: &fلوح بیستون، خط میخی",
                    "",
                    "&8&lموقعیت: &7بیستون، کرمانشاه - 520 قبل از میلاد"
            ),
            "کاتب بیستون",
            4,
            30, 20, 10
    ),

    NAQSH_ROSTAM(
            "NAQSH_ROSTAM",
            "نقش رستم",
            "Naqsh-e Rostam",
            "آرامگاه 4 شاه هخامنشی در دل کوه",
            Material.SANDSTONE,
            Arrays.asList(
                    Biome.MESA_BRYCE,
                    Biome.MESA_PLATEAU_FOREST,
                    Biome.MESA_PLATEAU_MOUNTAINS,
                    Biome.DESERT_MOUNTAINS
            ),
            Arrays.asList(
                    "&6&l⛰ نقش رستم - آرامگاه شاهان",
                    "&7آرامگاه داریوش، خشایارشا، اردشیر، داریوش دوم",
                    "&7در دل کوه، با نقش‌های ساسانی",
                    "&7کعبه زرتشت در کنارش",
                    "",
                    "&c&lباس: &fخشایارشا - شاه جنگاور",
                    "&c&lسختی: &6★★★★★",
                    "&e&lلوت: &fتاج شاهان، شمشیر هخامنشی",
                    "",
                    "&8&lموقعیت: &7مرودشت، فارس - کنار تخت جمشید"
            ),
            "خشایارشا",
            5,
            35, 25, 15
    ),

    TAKHT_SOLEYMAN(
            "TAKHT_SOLEYMAN",
            "تخت سلیمان",
            "Takht-e Soleyman",
            "مقدس‌ترین مکان زرتشتیان - دریاچه جوشان",
            Material.ICE,
            Arrays.asList(
                    Biome.FROZEN_OCEAN,
                    Biome.FROZEN_RIVER,
                    Biome.COLD_TAIGA_MOUNTAINS
            ),
            Arrays.asList(
                    "&b&l🏔 تخت سلیمان - دریاچه مقدس",
                    "&7مقدس‌ترین مکان زرتشتیان، با دریاچه جوشان",
                    "&7آتشکده آذرگشسب در آن بود",
                    "&7زادگاه زرتشت پیامبر",
                    "",
                    "&c&lباس: &fموبد تخت سلیمان",
                    "&c&lسختی: &b★★★★★",
                    "&e&lلوت: &fآتش مقدس، آب مقدس",
                    "",
                    "&8&lموقعیت: &7تکاب، آذربایجان غربی - یونسکو"
            ),
            "نگهبان تخت سلیمان",
            5,
            30, 20, 30
    ),

    HEGMATANEH(
            "HEGMATANEH",
            "هگمتانه - پایتخت مادها",
            "Hegmataneh - Ecbatana",
            "پایتخت 700 ساله مادها و هخامنشیان",
            Material.SMOOTH_BRICK,
            Arrays.asList(
                    Biome.TAIGA,
                    Biome.TAIGA_HILLS,
                    Biome.MEGA_TAIGA,
                    Biome.MEGA_SPRUCE_TAIGA
            ),
            Arrays.asList(
                    "&2&l🏰 هگمتانه - پایتخت مادها",
                    "&7پایتخت مادها، هخامنشیان، اشکانیان",
                    "&7شهری با 7 دیوار رنگی، 700 سال پایتخت",
                    "&7دیاکو آن را ساخت",
                    "",
                    "&c&lباس: &fدیاکو - بنیان‌گذار ماد",
                    "&c&lسختی: &2★★★★☆",
                    "&e&lلوت: &fتاج مادی، شمشیر مادی",
                    "",
                    "&8&lموقعیت: &7همدان - 700 قبل از میلاد"
            ),
            "دیاکو مادی",
            4,
            32, 18, 32
    ),

    SUSA_PALACE(
            "SUSA_PALACE",
            "کاخ شوش - آپادانای شوش",
            "Susa Palace",
            "کاخ زمستانی داریوش بزرگ",
            Material.BRICK,
            Arrays.asList(
                    Biome.BIRCH_FOREST_HILLS,
                    Biome.BIRCH_FOREST_HILLS_MOUNTAINS,
                    Biome.FOREST_HILLS
            ),
            Arrays.asList(
                    "&e&l🏛 کاخ شوش - آپادانا",
                    "&7کاخ زمستانی داریوش، با ستون‌های باشکوه",
                    "&7گاوهای بالدار شوش در لوور پاریس",
                    "&7پایتخت ایلامیان و هخامنشیان",
                    "",
                    "&c&lباس: &fسردار شوش - نگهبان آپادانا",
                    "&c&lسختی: &e★★★★☆",
                    "&e&lلوت: &fگاو بالدار، لوح شوش",
                    "",
                    "&8&lموقعیت: &7شوش، خوزستان - 500 قبل از میلاد"
            ),
            "نگهبان شوش",
            4,
            28, 20, 28
    ),

    YAZD_JAMEH_MOSQUE(
            "YAZD_JAMEH_MOSQUE",
            "مسجد جامع یزد",
            "Yazd Jameh Mosque",
            "بلندترین مناره‌های جهان - شاهکار صفوی",
            Material.SANDSTONE,
            Arrays.asList(
                    Biome.DESERT_HILLS,
                    Biome.SAVANNA_MOUNTAINS,
                    Biome.MESA_PLATEAU_FOREST_MOUNTAINS
            ),
            Arrays.asList(
                    "&9&l🕌 مسجد جامع یزد - شاهکار کویر",
                    "&7مسجدی با بلندترین مناره‌های جهان، 52 متر",
                    "&7کاشی‌کاری فیروزه‌ای بی‌نظیر",
                    "&7ساخته شده در 100 سال، دوره صفوی",
                    "",
                    "&c&lباس: &fمعمار یزدی - استاد کاشی‌کار",
                    "&c&lسختی: &9★★★☆☆",
                    "&e&lلوت: &fکاشی فیروزه‌ای، فرش یزدی",
                    "",
                    "&8&lموقعیت: &7یزد - قلب کویر ایران"
            ),
            "معمار مسجد یزد",
            3,
            25, 30, 25
    ),

    AZADI_TOWER(
            "AZADI_TOWER",
            "برج آزادی",
            "Azadi Tower",
            "نماد تهران - دروازه تمدن بزرگ",
            Material.QUARTZ_BLOCK,
            Arrays.asList(
                    Biome.SAVANNA_PLATEAU_MOUNTAINS,
                    Biome.JUNGLE_HILLS,
                    Biome.JUNGLE_MOUNTAINS,
                    Biome.MUSHROOM_ISLAND,
                    Biome.MUSHROOM_SHORE
            ),
            Arrays.asList(
                    "&f&l🗽 برج آزادی - نماد ایران مدرن",
                    "&7برجی به یاد 2500 سال شاهنشاهی ایران",
                    "&7طراحی شده توسط حسین امانت",
                    "&7ترکیب معماری هخامنشی، ساسانی، اسلامی",
                    "",
                    "&c&lباس: &fنگهبان آزادی",
                    "&c&lسختی: &f★★★☆☆",
                    "&e&lلوت: &fپرچم ایران، تاج پهلوی",
                    "",
                    "&8&lموقعیت: &7تهران - 1971 میلادی - 45 متر"
            ),
            "نگهبان آزادی",
            3,
            20, 25, 20
    );

    private final String id;
    private final String persianName;
    private final String englishName;
    private final String description;
    private final Material icon;
    private final List<Biome> biomes;
    private final List<String> lore;
    private final String bossName;
    private final int difficulty;
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
        // اگر دقیق نبود، بر اساس نام بایوم حدس بزن
        String name = biome.name();
        if (name.contains("DESERT")) return ARGE_BAM;
        if (name.contains("FOREST") || name.contains("JUNGLE")) return ANAHITA_TEMPLE;
        if (name.contains("EXTREME") || name.contains("MOUNTAIN")) return ALAMUT_CASTLE;
        if (name.contains("ICE") || name.contains("COLD") || name.contains("FROZEN")) return GHALEH_BABAK;
        if (name.contains("SWAMP") || name.contains("RIVER")) return CHOGHA_ZANBIL;
        if (name.contains("OCEAN") || name.contains("BEACH")) return BANDAR_SIRAF;
        if (name.contains("MESA")) return NAQSH_ROSTAM;
        if (name.contains("TAIGA")) return HEGMATANEH;
        if (name.contains("PLAINS") || name.contains("SAVANNA")) return PASARGAD_TOMB;
        if (name.contains("HELL")) return DAKHMEH_ZARTOSHTI;
        if (name.contains("SKY")) return TAKHT_JAMSHID_SKY;
        return ARGE_BAM; // پیش‌فرض
    }

    public String getDisplayName() {
        return "§6" + persianName + " §7(" + englishName + ")";
    }

    /**
     * آیا تمام بایوم‌های 1.12 پوشش داده شده؟
     */
    public static boolean coversAllBiomes() {
        return true; // با getForBiome هوشمند، همه پوشش داده می‌شوند
    }
}

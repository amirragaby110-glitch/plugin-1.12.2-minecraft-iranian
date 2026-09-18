package ir.iranian.hardcore.race;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.List;

/**
 * نژادها = اقوام ایرانی - نسخه 3.5 - هر بایوم یک قوم ایران
 * کاملا فارسی - بر اساس اقوام واقعی ایران
 * هر قوم مخصوص یک بایوم و منطقه جغرافیایی ایران است
 * با قابلیت‌های مخصوص فرهنگی و تاریخی
 */
public enum RaceType {

    // ========== قوم پارس / فارس - مرکز ایران ==========
    FARS(
            "FARS",
            "قوم پارس - فارس",
            "Pars/Fars",
            Material.GOLD_BLOCK,
            Arrays.asList(
                    Biome.PLAINS,
                    Biome.MUTATED_PLAINS,
                    Biome.DESERT_HILLS,
                    Biome.SAVANNA_ROCK,
                    Biome.PLAINS // تخت جمشید، پاسارگاد
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.FROZEN_OCEAN,
                    Biome.MUTATED_ICE_FLATS
            ),
            Arrays.asList(
                    "&6&l👑 قوم پارس - بنیان‌گذاران ایران",
                    "&7پارس‌ها، بنیان‌گذاران شاهنشاهی هخامنشی",
                    "&7کوروش بزرگ، داریوش، خشایارشا از این قوم",
                    "&7مرکز: فارس، تخت جمشید، پاسارگاد",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &6معماری هخامنشی: &fعجله و سرعت ساخت",
                    "&7• &6فرماندهی شاهنشاهی: &fمقاومت و قدرت",
                    "&7• &6کشاورزی پارس: &fگرسنگی کمتر",
                    "&7• &6فرهنگ: &fشعر و ادب",
                    "",
                    "&c&lضعف: &fسرما - دشت‌های گرمسیری",
                    "&8&lتاریخ: &7 550 قبل از میلاد - هخامنشیان",
                    "&8&lجمعیت: &7 40% ایران - بزرگترین قوم"
            )
    ),

    // ========== قوم آذری - آذربایجان ==========
    AZARI(
            "AZARI",
            "قوم آذری - ترک آذربایجان",
            "Azari Turk",
            Material.SNOW_BLOCK,
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.ICE_MOUNTAINS,
                    Biome.TAIGA_COLD,
                    Biome.TAIGA_COLD_HILLS,
                    Biome.MUTATED_TAIGA_COLD,
                    Biome.FROZEN_RIVER
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.HELL,
                    Biome.MESA
            ),
            Arrays.asList(
                    "&b&l❄ قوم آذری - شیران آذربایجان",
                    "&7آذری‌ها، مردمان غیور شمال غرب ایران",
                    "&7بابک خرمدین، ستارخان، باقرخان از این قوم",
                    "&7مرکز: تبریز، اردبیل، ارومیه",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &bمقاومت سرما: &fمصونیت به کندی و سرما",
                    "&7• &fسرعت در برف: &fمثل اسب‌های قره‌باغ",
                    "&7• &cقدرت جنگاوری: &fشجاعت آذری",
                    "&7• &6فرهنگ: &fموسیقی عاشیقی",
                    "",
                    "&c&lضعف: &fگرمای کویر و جهنم",
                    "&8&lتاریخ: &7 مادها، بابک خرمدین، مشروطه",
                    "&8&lزبان: &7 ترکی آذری - شیرین"
            )
    ),

    // ========== قوم کرد - کردستان و زاگرس ==========
    KURD(
            "KURD",
            "قوم کرد - کردستان",
            "Kurd",
            Material.STONE,
            Arrays.asList(
                    Biome.EXTREME_HILLS,
                    Biome.MUTATED_EXTREME_HILLS,
                    Biome.SMALLER_EXTREME_HILLS,
                    Biome.MUTATED_EXTREME_HILLS_WITH_TREES
            ),
            Arrays.asList(
                    Biome.OCEAN,
                    Biome.DEEP_OCEAN,
                    Biome.SWAMPLAND
            ),
            Arrays.asList(
                    "&2&l🏔 قوم کرد - عقاب‌های زاگرس",
                    "&7کردها، مردمان کوهستان‌های زاگرس",
                    "&7شجاع، مهمان‌نواز، با فرهنگ غنی",
                    "&7مرکز: سنندج، کرمانشاه، ایلام، مهاباد",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &aکوهنوردی: &f70% کاهش سقوط از کوه",
                    "&7• &aپرش زاگرس: &fپرش بلند",
                    "&7• &cقدرت جنگاوری: &fقدرت در کوهستان",
                    "&7• &eفرهنگ: &fرقص هه‌لپرکی، لباس کردی",
                    "",
                    "&c&lضعف: &fدریا و مرداب - دوری از کوه",
                    "&8&lتاریخ: &7 مادها، هورامان، 3000 سال",
                    "&8&lزبان: &7 کردی - شیرین و حماسی"
            )
    ),

    // ========== قوم لر - لرستان ==========
    LOR(
            "LOR",
            "قوم لر - لرستان",
            "Lor",
            Material.WOOD,
            Arrays.asList(
                    Biome.FOREST_HILLS,
                    Biome.BIRCH_FOREST_HILLS,
                    Biome.EXTREME_HILLS_WITH_TREES,
                    Biome.FOREST
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.OCEAN,
                    Biome.BEACHES
            ),
            Arrays.asList(
                    "&6&l🌲 قوم لر - شیران لرستان",
                    "&7لرها، مردمان شجاع زاگرس میانی",
                    "&7با فرهنگ غنی، موسیقی لری، لباس لری",
                    "&7مرکز: خرم‌آباد، یاسوج، شهرکرد",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &6قدرت بدنی: &fقدرت و مقاومت",
                    "&7• &6دامداری: &fگرسنگی کمتر، گوشت بیشتر",
                    "&7• &aجنگل‌نوردی: &fسرعت در جنگل",
                    "&7• &eفرهنگ: &fکمانچه لری، رقص لری",
                    "",
                    "&c&lضعف: &fکویر و دریا",
                    "&8&lتاریخ: &7 کاسیت‌ها، 4000 سال",
                    "&8&lزبان: &7 لری - حماسی"
            )
    ),

    // ========== قوم بلوچ - بلوچستان ==========
    BALOCH(
            "BALOCH",
            "قوم بلوچ - بلوچستان",
            "Baloch",
            Material.SAND,
            Arrays.asList(
                    Biome.DESERT,
                    Biome.DESERT_HILLS,
                    Biome.MESA,
                    Biome.MUTATED_MESA,
                    Biome.MESA_CLEAR_ROCK,
                    Biome.MUTATED_DESERT
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.TAIGA_COLD,
                    Biome.FROZEN_OCEAN
            ),
            Arrays.asList(
                    "&e&l🏜 قوم بلوچ - فرزندان کویر",
                    "&7بلوچ‌ها، مردمان مقاوم جنوب شرق ایران",
                    "&7در کویر لوت و تفتان زندگی می‌کنند",
                    "&7مرکز: زاهدان، ایرانشهر، چابهار",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &6مقاومت گرما: &fمصونیت آتش در بیابان",
                    "&7• &eسرعت در شن: &fمثل باد کویر",
                    "&7• &aگرسنگی کمتر: &fمقاومت به تشنگی",
                    "&7• &bشب‌بینی: &fدید در شب کویر",
                    "",
                    "&c&lضعف: &fسرمای برف و یخ",
                    "&8&lتاریخ: &7 700 ساله، مقاومت",
                    "&8&lزبان: &7 بلوچی - شیرین"
            )
    ),

    // ========== قوم عرب خوزستان - خوزستان ==========
    ARAB_KHUZESTAN(
            "ARAB_KHUZESTAN",
            "قوم عرب - خوزستان",
            "Arab Khuzestan",
            Material.WATER_BUCKET,
            Arrays.asList(
                    Biome.SWAMPLAND,
                    Biome.MUTATED_SWAMPLAND,
                    Biome.RIVER,
                    Biome.MUSHROOM_ISLAND,
                    Biome.MUSHROOM_ISLAND_SHORE
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.ICE_MOUNTAINS
            ),
            Arrays.asList(
                    "&2&l🌴 قوم عرب خوزستان - نخلداران کارون",
                    "&7عرب‌های خوزستان، مردمان خونگرم جنوب",
                    "&7در کنار کارون و نخلستان‌ها",
                    "&7مرکز: اهواز، آبادان، خرمشهر",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &bشناگر ماهر: &fتنفس زیر آب طولانی",
                    "&7• &6مقاومت گرما: &fگرمای 50 درجه خوزستان",
                    "&7• &aکشاورزی نخل: &fگرسنگی کمتر",
                    "&7• &eفرهنگ: &fمضیف عربی، قهوه عربی",
                    "",
                    "&c&lضعف: &fسرمای کوهستان",
                    "&8&lتاریخ: &7 ایلامیان، 3000 سال",
                    "&8&lزبان: &7 عربی خوزستانی"
            )
    ),

    // ========== قوم ترکمن - ترکمن صحرا ==========
    TURKMEN(
            "TURKMEN",
            "قوم ترکمن - ترکمن صحرا",
            "Turkmen",
            Material.WOOL,
            Arrays.asList(
                    Biome.SAVANNA,
                    Biome.SAVANNA_ROCK,
                    Biome.MUTATED_SAVANNA,
                    Biome.MUTATED_SAVANNA_ROCK
            ),
            Arrays.asList(
                    Biome.JUNGLE,
                    Biome.SWAMPLAND,
                    Biome.ICE_FLATS
            ),
            Arrays.asList(
                    "&a&l🐎 قوم ترکمن - سوارکاران صحرا",
                    "&7ترکمن‌ها، بهترین سوارکاران ایران",
                    "&7اسب ترکمن، فرش ترکمن جهانی است",
                    "&7مرکز: گنبد کاووس، بندر ترکمن",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &aسوارکاری: &fسرعت بالا - مثل اسب ترکمن",
                    "&7• &aپرش: &fپرش بلند",
                    "&7• &6فرش‌بافی: &fشانس بیشتر",
                    "&7• &eفرهنگ: &fموسیقی بخشی، اسب",
                    "",
                    "&c&lضعف: &fجنگل و مرداب و سرما",
                    "&8&lتاریخ: &7 اوغوزها، 1000 ساله",
                    "&8&lزبان: &7 ترکمنی"
            )
    ),

    // ========== قوم گیلک - گیلان ==========
    GILAK(
            "GILAK",
            "قوم گیلک - گیلان",
            "Gilak",
            Material.LEAVES,
            Arrays.asList(
                    Biome.ROOFED_FOREST,
                    Biome.MUTATED_ROOFED_FOREST,
                    Biome.JUNGLE,
                    Biome.JUNGLE_HILLS,
                    Biome.MUTATED_FOREST,
                    Biome.REDWOOD_TAIGA_HILLS
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.MESA,
                    Biome.HELL
            ),
            Arrays.asList(
                    "&a&l🌧 قوم گیلک - فرزندان باران",
                    "&7گیلک‌ها، مردمان جنگل‌های هیرکانی",
                    "&7برنج، چای، ابریشم گیلان جهانی است",
                    "&7مرکز: رشت، انزلی، لاهیجان",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &bماهیگیری: &fشانس ماهیگیری بالا",
                    "&7• &aجنگل‌نوردی: &fسرعت و نامرئی در برگ",
                    "&7• &aکشاورزی برنج: &fگرسنگی کمتر",
                    "&7• &eفرهنگ: &fچای لاهیجان، رقص گیلکی",
                    "",
                    "&c&lضعف: &fکویر خشک",
                    "&8&lتاریخ: &7 کادوسیان، 3000 سال",
                    "&8&lزبان: &7 گیلکی - شیرین"
            )
    ),

    // ========== قوم مازنی - مازندران - طبری ==========
    MAZANI(
            "MAZANI",
            "قوم مازنی - طبری - مازندران",
            "Mazani Tabari",
            Material.WOOD,
            Arrays.asList(
                    Biome.BIRCH_FOREST,
                    Biome.BIRCH_FOREST_HILLS,
                    Biome.TAIGA,
                    Biome.TAIGA_HILLS,
                    Biome.REDWOOD_TAIGA,
                    Biome.MUTATED_BIRCH_FOREST_HILLS
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.SAVANNA,
                    Biome.HELL
            ),
            Arrays.asList(
                    "&2&l🌲 قوم مازنی - طبری - فرزندان تبرستان",
                    "&7مازنی‌ها، مردمان دریای خزر",
                    "&7از نسل طبرستان بزرگ",
                    "&7مرکز: ساری، بابل، آمل، نوشهر",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &aکشاورزی: &fسرعت کندن و کاشت",
                    "&7• &bدریای خزر: &fتنفس زیر آب و شنا",
                    "&7• &aجنگل هیرکانی: &fسرعت در جنگل",
                    "&7• &eفرهنگ: &fموسیقی تبری، کشتی لوچو",
                    "",
                    "&c&lضعف: &fکویر",
                    "&8&lتاریخ: &7 تپورها، 2500 سال - طبرستان",
                    "&8&lزبان: &7 مازندرانی - طبری"
            )
    ),

    // ========== قوم بختیاری - چهارمحال ==========
    BAKHTIARI(
            "BAKHTIARI",
            "قوم بختیاری - چهارمحال",
            "Bakhtiari",
            Material.IRON_INGOT,
            Arrays.asList(
                    Biome.EXTREME_HILLS_WITH_TREES,
                    Biome.MUTATED_EXTREME_HILLS_WITH_TREES,
                    Biome.REDWOOD_TAIGA_HILLS,
                    Biome.MUTATED_TAIGA,
                    Biome.MUTATED_REDWOOD_TAIGA_HILLS
            ),
            Arrays.asList(
                    Biome.OCEAN,
                    Biome.BEACHES,
                    Biome.DEEP_OCEAN
            ),
            Arrays.asList(
                    "&6&l🏔 قوم بختیاری - کوچ‌نشینان زاگرس",
                    "&7بختیاری‌ها، بزرگترین کوچ‌نشینان ایران",
                    "&7هر سال از خوزستان به چهارمحال کوچ می‌کنند",
                    "&7مرکز: شهرکرد، مسجدسلیمان، ایذه",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &aکوچ‌نشینی: &fمقاومت سقوط 70% + پرش 2",
                    "&7• &cقدرت: &fقدرت بدنی بالا",
                    "&7• &aسرعت کوهستان: &fسرعت در کوه",
                    "&7• &eفرهنگ: &fچوقا بختیاری، کوچ",
                    "",
                    "&c&lضعف: &fدریا - کوهستانی هستند",
                    "&8&lتاریخ: &7 1000 ساله، کوچ",
                    "&8&lزبان: &7 لری بختیاری"
            )
    ),

    // ========== قوم قشقایی - فارس کوچ‌نشین ==========
    QASHQAI(
            "QASHQAI",
            "قوم قشقایی - فارس",
            "Qashqai",
            Material.CARPET,
            Arrays.asList(
                    Biome.MESA_CLEAR_ROCK,
                    Biome.MESA_CLEAR_ROCK,
                    Biome.MESA_ROCK,
                    Biome.MUTATED_MESA_CLEAR_ROCK,
                    Biome.MUTATED_SAVANNA
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.FROZEN_OCEAN,
                    Biome.COLD_BEACH
            ),
            Arrays.asList(
                    "&e&l🐑 قوم قشقایی - کوچ‌نشینان فارس",
                    "&7قشقایی‌ها، کوچ‌نشینان ترک‌زبان فارس",
                    "&7فرش قشقایی جهانی است",
                    "&7مرکز: فیروزآباد، اقلید، سمیرم",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &aکوچ: &fسرعت 2 - کوچ سریع",
                    "&7• &aپرش: &fپرش بلند",
                    "&7• &6دامداری: &fگرسنگی کمتر",
                    "&7• &eفرهنگ: &fفرش قشقایی، موسیقی",
                    "",
                    "&c&lضعف: &fسرمای برف",
                    "&8&lتاریخ: &7 500 ساله، کوچ",
                    "&8&lزبان: &7 ترکی قشقایی"
            )
    ),

    // ========== قوم بندری - هرمزگان و بوشهر ==========
    BANDARI(
            "BANDARI",
            "قوم بندری - هرمزگان - خلیج فارس",
            "Bandari - Persian Gulf",
            Material.PRISMARINE,
            Arrays.asList(
                    Biome.OCEAN,
                    Biome.DEEP_OCEAN,
                    Biome.BEACHES,
                    Biome.COLD_BEACH,
                    Biome.STONE_BEACH,
                    Biome.FROZEN_OCEAN
            ),
            Arrays.asList(
                    Biome.EXTREME_HILLS,
                    Biome.ICE_FLATS,
                    Biome.ICE_MOUNTAINS
            ),
            Arrays.asList(
                    "&b&l⚓ قوم بندری - دریانوردان خلیج فارس",
                    "&7بندری‌ها، دریانوردان خلیج همیشه فارس",
                    "&7لنج‌سازی، صید مروارید، تجارت دریایی",
                    "&7مرکز: بندرعباس، بوشهر، چابهار، قشم",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &bدریانوردی: &fتنفس 3 برابر زیر آب",
                    "&7• &bشنای سریع: &fسرعت 2 در آب",
                    "&7• &aتجارت: &fمعامله بهتر با روستاییان",
                    "&7• &eفرهنگ: &fموسیقی بندری، لنج",
                    "",
                    "&c&lضعف: &fکوهستان و برف",
                    "&8&lتاریخ: &7 3000 سال دریانوردی - خلیج فارس",
                    "&8&lزبان: &7 بندری - با لهجه شیرین جنوبی",
                    "&8&lشعار: &7خلیج همیشه فارس!"
            )
    ),

    // ========== قوم خراسانی - خراسان ==========
    KHORASANI(
            "KHORASANI",
            "قوم خراسانی - خراسان",
            "Khorasani",
            Material.BOOK,
            Arrays.asList(
                    Biome.PLAINS,
                    Biome.MUTATED_PLAINS,
                    Biome.MUTATED_SAVANNA_ROCK
            ),
            Arrays.asList(
                    Biome.SWAMPLAND,
                    Biome.JUNGLE
            ),
            Arrays.asList(
                    "&5&l📚 قوم خراسانی - دیار فردوسی و عطار",
                    "&7خراسانی‌ها، مردمان دیار شعر و عرفان",
                    "&7فردوسی، عطار، خیام از این دیار",
                    "&7مرکز: مشهد، نیشابور، سبزوار، بیرجند",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &5فرهنگ و ادب: &fشانس بیشتر کتاب",
                    "&7• &aکشاورزی زعفران: &fگرسنگی کمتر",
                    "&7• &6مقاومت: &fمقاومت در برابر سختی",
                    "&7• &eفرهنگ: &fشاهنامه، عرفان",
                    "",
                    "&c&lضعف: &fمرداب و جنگل مرطوب",
                    "&8&lتاریخ: &7 5000 ساله - خراسان بزرگ",
                    "&8&lزبان: &7 فارسی خراسانی"
            )
    ),

    // ========== قوم سیستانی - سیستان ==========
    SISTANI(
            "SISTANI",
            "قوم سیستانی - سیستان - دیار رستم",
            "Sistani",
            Material.SANDSTONE,
            Arrays.asList(
                    Biome.MUTATED_DESERT,
                    Biome.MESA_ROCK,
                    Biome.DESERT_HILLS
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.JUNGLE,
                    Biome.SWAMPLAND
            ),
            Arrays.asList(
                    "&e&l🏹 قوم سیستانی - دیار رستم دستان",
                    "&7سیستانی‌ها، از نسل رستم دستان",
                    "&7دیار رستم و سهراب، شهر سوخته",
                    "&7مرکز: زابل، زاهدان - شهر سوخته 5000 ساله",
                    "",
                    "&a&lویژگی‌های نژادی:",
                    "&7• &cقدرت رستم: &fقدرت 2 - مثل رستم",
                    "&7• &6مقاومت کویر: &fمقاومت آتش و گرما",
                    "&7• &aتیراندازی: &fدقت تیر بالا",
                    "&7• &eفرهنگ: &fشاهنامه، رستم",
                    "",
                    "&c&lضعف: &fسرما و رطوبت",
                    "&8&lتاریخ: &7 5000 ساله - شهر سوخته",
                    "&8&lزبان: &7 فارسی سیستانی"
            )
    );

    private final String id;
    private final String persianName;
    private final String englishName;
    private final Material icon;
    private final List<Biome> homeBiomes;
    private final List<Biome> hostileBiomes;
    private final List<String> lore;

    RaceType(String id, String persianName, String englishName, Material icon,
             List<Biome> homeBiomes, List<Biome> hostileBiomes, List<String> lore) {
        this.id = id;
        this.persianName = persianName;
        this.englishName = englishName;
        this.icon = icon;
        this.homeBiomes = homeBiomes;
        this.hostileBiomes = hostileBiomes;
        this.lore = lore;
    }

    public String getId() { return id; }
    public String getPersianName() { return persianName; }
    public String getEnglishName() { return englishName; }
    public Material getIcon() { return icon; }
    public List<Biome> getHomeBiomes() { return homeBiomes; }
    public List<Biome> getHostileBiomes() { return hostileBiomes; }
    public List<String> getLore() { return lore; }

    public boolean isHomeBiome(Biome biome) {
        return homeBiomes.contains(biome);
    }

    public boolean isHostileBiome(Biome biome) {
        return hostileBiomes.contains(biome);
    }

    public static RaceType fromId(String id) {
        if (id == null) return null;
        for (RaceType type : values()) {
            if (type.getId().equalsIgnoreCase(id)) {
                return type;
            }
        }
        return null;
    }

    public static RaceType getForBiome(Biome biome) {
        for (RaceType type : values()) {
            if (type.isHomeBiome(biome)) return type;
        }
        // حدس هوشمند بر اساس نام بایوم
        String name = biome.name();
        if (name.contains("DESERT") || name.contains("MESA")) return BALOCH;
        if (name.contains("FOREST") || name.contains("JUNGLE")) return GILAK;
        if (name.contains("EXTREME") || name.contains("MOUNTAIN")) return KURD;
        if (name.contains("ICE") || name.contains("COLD") || name.contains("FROZEN")) return AZARI;
        if (name.contains("SWAMP") || name.contains("RIVER")) return ARAB_KHUZESTAN;
        if (name.contains("OCEAN") || name.contains("BEACH")) return BANDARI;
        if (name.contains("SAVANNA")) return TURKMEN;
        if (name.contains("TAIGA") || name.contains("BIRCH")) return MAZANI;
        if (name.contains("PLAINS")) return FARS;
        return FARS;
    }

    public String getDisplayName() {
        return "§6" + persianName + " §7(" + englishName + ")";
    }
}

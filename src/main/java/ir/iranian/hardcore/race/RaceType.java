package ir.iranian.hardcore.race;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * انواع نژادها با بایوم‌های مخصوص و ویژگی‌ها
 * هر نژاد در بایوم خانه قدرتمند و در بایوم دشمن ضعیف است
 */
public enum RaceType {

    MOUNTAINBORN(
            "MOUNTAINBORN",
            "کوهستان",
            "Mountainborn",
            Material.STONE,
            Arrays.asList(
                    Biome.EXTREME_HILLS,
                    Biome.EXTREME_HILLS_MOUNTAINS,
                    Biome.EXTREME_HILLS_PLUS,
                    Biome.EXTREME_HILLS_PLUS_MOUNTAINS,
                    Biome.MESA,
                    Biome.MESA_BRYCE,
                    Biome.MESA_PLATEAU,
                    Biome.MESA_PLATEAU_FOREST,
                    Biome.MESA_PLATEAU_FOREST_MOUNTAINS,
                    Biome.MESA_PLATEAU_MOUNTAINS,
                    Biome.SMALL_MOUNTAINS
            ),
            Arrays.asList(
                    Biome.OCEAN,
                    Biome.DEEP_OCEAN,
                    Biome.RIVER,
                    Biome.SWAMPLAND
            ),
            Arrays.asList(
                    "&7مقاوم به سقوط از ارتفاع",
                    "&7قدرت بیشتر در کوهستان",
                    "&a+ &f70% کاهش دمیج سقوط",
                    "&a+ &fمقاومت و قدرت در بایوم کوهستان",
                    "&c- &fکندی در آب"
            )
    ),

    FORESTBORN(
            "FORESTBORN",
            "جنگل",
            "Forestborn",
            Material.LEAVES,
            Arrays.asList(
                    Biome.FOREST,
                    Biome.FOREST_HILLS,
                    Biome.BIRCH_FOREST,
                    Biome.BIRCH_FOREST_HILLS,
                    Biome.BIRCH_FOREST_HILLS_MOUNTAINS,
                    Biome.ROOFED_FOREST,
                    Biome.ROOFED_FOREST_MOUNTAINS,
                    Biome.FLOWER_FOREST,
                    Biome.TAIGA,
                    Biome.TAIGA_HILLS,
                    Biome.TAIGA_MOUNTAINS,
                    Biome.COLD_TAIGA,
                    Biome.COLD_TAIGA_HILLS,
                    Biome.COLD_TAIGA_MOUNTAINS,
                    Biome.MEGA_TAIGA,
                    Biome.MEGA_TAIGA_HILLS,
                    Biome.MEGA_SPRUCE_TAIGA,
                    Biome.MEGA_SPRUCE_TAIGA_HILLS,
                    Biome.JUNGLE,
                    Biome.JUNGLE_HILLS,
                    Biome.JUNGLE_MOUNTAINS,
                    Biome.JUNGLE_EDGE,
                    Biome.JUNGLE_EDGE_MOUNTAINS
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.DESERT_HILLS,
                    Biome.DESERT_MOUNTAINS
            ),
            Arrays.asList(
                    "&7فرزند جنگل، سریع در میان برگ‌ها",
                    "&a+ &fسرعت و عجله در جنگل",
                    "&a+ &fنامرئی شدن نسبی در برگ‌ها",
                    "&c- &fضعف و کندی در بیابان"
            )
    ),

    DESERTBORN(
            "DESERTBORN",
            "بیابان",
            "Desertborn",
            Material.SAND,
            Arrays.asList(
                    Biome.DESERT,
                    Biome.DESERT_HILLS,
                    Biome.DESERT_MOUNTAINS,
                    Biome.SAVANNA,
                    Biome.SAVANNA_MOUNTAINS,
                    Biome.SAVANNA_PLATEAU,
                    Biome.SAVANNA_PLATEAU_MOUNTAINS,
                    Biome.MESA,
                    Biome.MESA_BRYCE
            ),
            Arrays.asList(
                    Biome.ICE_PLAINS,
                    Biome.ICE_MOUNTAINS,
                    Biome.ICE_PLAINS_SPIKES,
                    Biome.COLD_TAIGA,
                    Biome.FROZEN_OCEAN,
                    Biome.FROZEN_RIVER
            ),
            Arrays.asList(
                    "&7مقاوم به گرما و تشنگی",
                    "&a+ &fگرسنگی کمتر در بیابان",
                    "&a+ &fمقاومت به آتش در بایوم خود",
                    "&a+ &fسرعت بیشتر در شن",
                    "&c- &fضعف در سرما و برف"
            )
    ),

    OCEANBORN(
            "OCEANBORN",
            "اقیانوس",
            "Oceanborn",
            Material.WATER_BUCKET,
            Arrays.asList(
                    Biome.OCEAN,
                    Biome.DEEP_OCEAN,
                    Biome.BEACH,
                    Biome.COLD_BEACH,
                    Biome.STONE_BEACH,
                    Biome.RIVER,
                    Biome.FROZEN_OCEAN,
                    Biome.FROZEN_RIVER
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.SAVANNA,
                    Biome.MESA,
                    Biome.HELL
            ),
            Arrays.asList(
                    "&7فرزند دریا، شناگر ماهر",
                    "&a+ &fتنفس طولانی زیر آب (3 برابر)",
                    "&a+ &fشنای سریع و دید در شب زیر آب",
                    "&a+ &fقدرت در اقیانوس",
                    "&c- &fضعف، کندی و گرسنگی در خشکی"
            )
    ),

    FROSTBORN(
            "FROSTBORN",
            "برفی",
            "Frostborn",
            Material.SNOW_BALL,
            Arrays.asList(
                    Biome.ICE_PLAINS,
                    Biome.ICE_MOUNTAINS,
                    Biome.ICE_PLAINS_SPIKES,
                    Biome.COLD_TAIGA,
                    Biome.COLD_TAIGA_HILLS,
                    Biome.COLD_TAIGA_MOUNTAINS,
                    Biome.FROZEN_OCEAN,
                    Biome.FROZEN_RIVER,
                    Biome.COLD_BEACH
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.DESERT_HILLS,
                    Biome.SAVANNA,
                    Biome.HELL
            ),
            Arrays.asList(
                    "&7مقاوم به سرمای کشنده",
                    "&a+ &fمقاومت به کندی و سرما",
                    "&a+ &fقدرت و سرعت در برف",
                    "&a+ &fغذا کمتر کم می‌شود در سرما",
                    "&c- &fکندی و ضعف در بیابان و ندر"
            )
    ),

    SWAMPBORN(
            "SWAMPBORN",
            "مرداب",
            "Swampborn",
            Material.VINE,
            Arrays.asList(
                    Biome.SWAMPLAND,
                    Biome.SWAMPLAND_MOUNTAINS,
                    Biome.MUSHROOM_ISLAND,
                    Biome.MUSHROOM_SHORE
            ),
            Arrays.asList(
                    Biome.HELL,
                    Biome.DESERT
            ),
            Arrays.asList(
                    "&7مقاوم به سم و بیماری",
                    "&a+ &fمصونیت به مسمومیت در مرداب",
                    "&a+ &fتنفس زیر آب و دید در شب",
                    "&a+ &fقدرت در مرداب",
                    "&c- &fضعف شدید در برابر آتش"
            )
    ),

    NETHERBORN(
            "NETHERBORN",
            "نتر",
            "Netherborn",
            Material.LAVA_BUCKET,
            Collections.singletonList(
                    Biome.HELL
            ),
            Arrays.asList(
                    Biome.OCEAN,
                    Biome.DEEP_OCEAN,
                    Biome.RIVER,
                    Biome.SWAMPLAND,
                    Biome.ICE_PLAINS,
                    Biome.FROZEN_OCEAN
            ),
            Arrays.asList(
                    "&7زاده شده در آتش جهنم",
                    "&a+ &fمصونیت کامل به آتش و لاوا",
                    "&a+ &fقدرت و سرعت در ندر",
                    "&a+ &fمقاومت به Wither",
                    "&c- &fضعف شدید در آب - دمیج در آب"
            )
    ),

    ENDBORN(
            "ENDBORN",
            "پایان",
            "Endborn",
            Material.ENDER_PEARL,
            Collections.singletonList(
                    Biome.SKY
            ),
            Arrays.asList(
                    Biome.PLAINS,
                    Biome.DESERT,
                    Biome.FOREST
            ),
            Arrays.asList(
                    "&7فرزند خلاء و پایان",
                    "&a+ &fمقاوم به سقوط (مثل Enderman)",
                    "&a+ &fسرعت، پرش و دید در شب در اند",
                    "&a+ &fتلپورت کوتاه هنگام دمیج",
                    "&c- &fضعف در نور خورشید - کندی و ضعف در روز"
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

    public String getId() {
        return id;
    }

    public String getPersianName() {
        return persianName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public Material getIcon() {
        return icon;
    }

    public List<Biome> getHomeBiomes() {
        return homeBiomes;
    }

    public List<Biome> getHostileBiomes() {
        return hostileBiomes;
    }

    public List<String> getLore() {
        return lore;
    }

    public boolean isHomeBiome(Biome biome) {
        return homeBiomes.contains(biome);
    }

    public boolean isHostileBiome(Biome biome) {
        return hostileBiomes.contains(biome);
    }

    /**
     * پیدا کردن نژاد از روی ID
     */
    public static RaceType fromId(String id) {
        if (id == null) return null;
        for (RaceType type : values()) {
            if (type.getId().equalsIgnoreCase(id)) {
                return type;
            }
        }
        return null;
    }

    /**
     * آیا بایوم در هیچ نژادی خانه محسوب می‌شود؟
     */
    public static boolean isAnyHomeBiome(Biome biome) {
        for (RaceType type : values()) {
            if (type.isHomeBiome(biome)) return true;
        }
        return false;
    }

    public String getDisplayName() {
        return "§6" + persianName + " §7(" + englishName + ")";
    }
}

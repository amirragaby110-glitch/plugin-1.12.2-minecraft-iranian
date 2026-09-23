package ir.iranian.hardcore.race;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.List;

/**
 * Races = Iranian Tribes - v5.0 Finglish
 * 100% Finglish compatible for Aternos and Spigot 1.12.2
 * Based on real Iranian tribes
 * Each tribe has special abilities in home biome
 */
public enum RaceType {

    // ========== Fars - Center of Iran ==========
    FARS(
            "FARS",
            "Ghome Pars - Fars",
            "Ghome Pars - Fars",
            "Pars/Fars",
            Material.GOLD_BLOCK,
            Arrays.asList(
                    Biome.PLAINS,
                    Biome.MUTATED_PLAINS,
                    Biome.DESERT_HILLS,
                    Biome.SAVANNA_ROCK,
                    Biome.PLAINS
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.FROZEN_OCEAN,
                    Biome.MUTATED_ICE_FLATS
            ),
            Arrays.asList(
                    "&6&lGhome Pars - Bonyan gozarane Iran",
                    "&7Pars ha, bonyan gozarane Shahanshahi Hakhamaneshi",
                    "&7Kourosh Bozorg, Daryoosh, Khashayarsha az in ghom",
                    "&7Markaz: Fars, Takht Jamshid, Pasargad",
                    "",
                    "&a&lVizhegi haye Nezhadi:",
                    "&7• &6Memari Hakhamaneshi: &fAjale va sorat sakht",
                    "&7• &6Farmandehi Shahanshahi: &fMoghavemat va ghodrat",
                    "&7• &6Keshavarzi Pars: &fGorosnegi kamtar",
                    "&7• &6Farhang: &fSher va adab",
                    "",
                    "&c&lZaaf: &fSarma - Dasht haye garmsiri",
                    "&8&lTarikh: &7 550 ghabl az milad - Hakhamaneshian",
                    "&8&lJamiat: &7 40% Iran - Bozorgtarin ghom"
            ),
            Arrays.asList(
                    "&6&lGhome Pars - Bonyan gozarane Iran",
                    "&7Pars ha, bonyan gozarane Shahanshahi Hakhamaneshi",
                    "&7Kourosh Bozorg, Daryoosh, Khashayarsha az in ghom",
                    "&7Markaz: Fars, Takht Jamshid, Pasargad"
            )
    ),

    AZARI(
            "AZARI",
            "Ghome Azari - Tork Azarbayjan",
            "Ghome Azari - Tork Azarbayjan",
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
                    "&b&lGhome Azari - Shirane Azarbayjan",
                    "&7Azari ha, mardomane ghayoor shomal gharb Iran",
                    "&7Babak Khorramdin, Sattar Khan az in ghom",
                    "&7Markaz: Tabriz, Ardebil, Urmia",
                    "",
                    "&a&lVizhegi:",
                    "&7• &bMoghavemat sarma: &fMasooniat be sarma",
                    "&7• &fSorat dar barf: &fMesle asb haye gharabagh",
                    "&7• &cGhodrat jangavari: &fShojaat Azari",
                    "",
                    "&c&lZaaf: &fGarmaye kavir",
                    "&8&lTarikh: &7 Madha, Babak, Mashroote"
            ),
            Arrays.asList(
                    "&b&lGhome Azari - Shirane Azarbayjan",
                    "&7Azari ha, mardomane ghayoor shomal gharb Iran"
            )
    ),

    KURD(
            "KURD",
            "Ghome Kord - Kordestan",
            "Ghome Kord - Kordestan",
            "Kurd",
            Material.STONE,
            Arrays.asList(
                    Biome.EXTREME_HILLS,
                    Biome.MUTATED_EXTREME_HILLS,
                    Biome.SMALLER_EXTREME_HILLS,
                    Biome.MUTATED_EXTREME_HILLS_WITH_TREES
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.OCEAN
            ),
            Arrays.asList(
                    "&7&lGhome Kord - Oghab Zagros",
                    "&7Kord ha, mardomane kohestane Zagros",
                    "&7Markaz: Sanandaj, Kermanshah",
                    "",
                    "&a&lVizhegi:",
                    "&7• &7Kahesh soghoot 70%",
                    "&7• &aParesh Zagros",
                    "&7• &cGhodrat dar koh",
                    "",
                    "&c&lZaaf: &fBiaban va darya"
            ),
            Arrays.asList("&7&lGhome Kord - Oghab Zagros")
    ),

    LOR(
            "LOR",
            "Ghome Lor - Lorestan",
            "Ghome Lor - Lorestan",
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
                    Biome.OCEAN
            ),
            Arrays.asList(
                    "&2&lGhome Lor - Shirane Lorestan",
                    "&7Lor ha, mardomane jangale Zagros",
                    "&7Markaz: Khorramabad, Yasuj",
                    "&a&lVizhegi: Ghodrat badani, Sorat jangal"
            ),
            Arrays.asList("&2&lGhome Lor - Shirane Lorestan")
    ),

    BALOCH(
            "BALOCH",
            "Ghome Balooch - Baloochestan",
            "Ghome Balooch - Baloochestan",
            "Baloch",
            Material.SAND,
            Arrays.asList(
                    Biome.DESERT,
                    Biome.DESERT_HILLS,
                    Biome.MESA,
                    Biome.MESA_CLEAR_ROCK
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.TAIGA_COLD
            ),
            Arrays.asList(
                    "&e&lGhome Balooch - Moghavem Kavir",
                    "&7Balooch ha, mardomane kavir",
                    "&7Markaz: Zahedan, Chabahar",
                    "&a&lVizhegi: Moghavemat garma, Sorat dar shen"
            ),
            Arrays.asList("&e&lGhome Balooch - Moghavem Kavir")
    ),

    ARAB_KHUZESTAN(
            "ARAB_KHUZESTAN",
            "Ghome Arab - Khoozestan",
            "Ghome Arab - Khoozestan",
            "Arab Khoozestan",
            Material.WATER_BUCKET,
            Arrays.asList(
                    Biome.SWAMPLAND,
                    Biome.MUTATED_SWAMPLAND,
                    Biome.RIVER,
                    Biome.MUSHROOM_ISLAND
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.EXTREME_HILLS
            ),
            Arrays.asList(
                    "&b&lGhome Arab - Karoon",
                    "&7Arab haye Khoozestan, mardomane nakhl",
                    "&7Markaz: Ahvaz, Abadan",
                    "&a&lVizhegi: Nafas zir ab, Moghavemat garma"
            ),
            Arrays.asList("&b&lGhome Arab - Karoon")
    ),

    TURKMEN(
            "TURKMEN",
            "Ghome Torkaman",
            "Ghome Torkaman",
            "Turkmen",
            Material.WOOL,
            Arrays.asList(
                    Biome.SAVANNA,
                    Biome.SAVANNA_ROCK,
                    Biome.MUTATED_SAVANNA,
                    Biome.PLAINS
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.SWAMPLAND
            ),
            Arrays.asList(
                    "&6&lGhome Torkaman - Asb Torkaman",
                    "&7Torkaman ha, asb savarane Iran",
                    "&7Markaz: Gonbad Kavous",
                    "&a&lVizhegi: Sorat bala (asb), Paresh boland"
            ),
            Arrays.asList("&6&lGhome Torkaman - Asb Torkaman")
    ),

    GILAK(
            "GILAK",
            "Ghome Gilak - Gilan",
            "Ghome Gilak - Gilan",
            "Gilak",
            Material.LEAVES,
            Arrays.asList(
                    Biome.ROOFED_FOREST,
                    Biome.JUNGLE,
                    Biome.MUTATED_FOREST,
                    Biome.JUNGLE_HILLS
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.MESA
            ),
            Arrays.asList(
                    "&2&lGhome Gilak - Baran Gilan",
                    "&7Gilak ha, mardomane jangale Hirkani",
                    "&7Markaz: Rasht, Lahijan - Chay",
                    "&a&lVizhegi: Mahigiri, Namaree dar barg"
            ),
            Arrays.asList("&2&lGhome Gilak - Baran Gilan")
    ),

    MAZANI(
            "MAZANI",
            "Ghome Mazani - Tabari",
            "Ghome Mazani - Tabari",
            "Mazani Tabari",
            Material.WOOD,
            Arrays.asList(
                    Biome.BIRCH_FOREST,
                    Biome.TAIGA,
                    Biome.REDWOOD_TAIGA,
                    Biome.TAIGA_HILLS
            ),
            Arrays.asList(
                    Biome.DESERT,
                    Biome.HELL
            ),
            Arrays.asList(
                    "&2&lGhome Mazani - Tabarestan",
                    "&7Mazani ha, mardomane Tabarestan",
                    "&7Markaz: Sari, Babol - Darya Khazar",
                    "&a&lVizhegi: Keshavarzi, Nafas zir ab"
            ),
            Arrays.asList("&2&lGhome Mazani - Tabarestan")
    ),

    BAKHTIARI(
            "BAKHTIARI",
            "Ghome Bakhtiari",
            "Ghome Bakhtiari",
            "Bakhtiari",
            Material.IRON_INGOT,
            Arrays.asList(
                    Biome.EXTREME_HILLS,
                    Biome.REDWOOD_TAIGA_HILLS,
                    Biome.TAIGA_HILLS,
                    Biome.MUTATED_EXTREME_HILLS
            ),
            Arrays.asList(
                    Biome.OCEAN,
                    Biome.DESERT
            ),
            Arrays.asList(
                    "&7&lGhome Bakhtiari - Kooch neshin",
                    "&7Bakhtiari ha, kooch neshinane Zagros",
                    "&7Markaz: Shahrekord - Kooch",
                    "&a&lVizhegi: Moghavemat soghoot 70% + Paresh 2"
            ),
            Arrays.asList("&7&lGhome Bakhtiari - Kooch neshin")
    ),

    QASHQAYI(
            "QASHQAYI",
            "Ghome Ghashghayi",
            "Ghome Ghashghayi",
            "Qashqayi",
            Material.CARPET,
            Arrays.asList(
                    Biome.MESA_CLEAR_ROCK,
                    Biome.MUTATED_MESA_CLEAR_ROCK,
                    Biome.SAVANNA_ROCK,
                    Biome.MESA_ROCK
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.OCEAN
            ),
            Arrays.asList(
                    "&6&lGhome Ghashghayi - Farsh Ghashghayi",
                    "&7Ghashghayi ha, farsh bafane Iran",
                    "&7Markaz: Firoozabad - Farsh",
                    "&a&lVizhegi: Sorat 2, Damdari"
            ),
            Arrays.asList("&6&lGhome Ghashghayi - Farsh Ghashghayi")
    ),

    BANDARI(
            "BANDARI",
            "Ghome Bandari - Khalij Fars",
            "Ghome Bandari - Khalij Fars",
            "Bandari",
            Material.PRISMARINE,
            Arrays.asList(
                    Biome.OCEAN,
                    Biome.DEEP_OCEAN,
                    Biome.BEACHES,
                    Biome.STONE_BEACH
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.EXTREME_HILLS
            ),
            Arrays.asList(
                    "&b&lGhome Bandari - Khalij Fars",
                    "&7Bandari ha, daryanavardane Khalij Fars",
                    "&7Markaz: Bandar Abbas, Bushehr, Gheshm",
                    "&7Khalij HAMESHE Fars!",
                    "&a&lVizhegi: Nafas 3 barabar zir ab, Shenaye sari"
            ),
            Arrays.asList("&b&lGhome Bandari - Khalij Fars")
    ),

    KHORASANI(
            "KHORASANI",
            "Ghome Khorasani",
            "Ghome Khorasani",
            "Khorasani",
            Material.BOOK,
            Arrays.asList(
                    Biome.MUTATED_PLAINS,
                    Biome.PLAINS,
                    Biome.SAVANNA_ROCK
            ),
            Arrays.asList(
                    Biome.SWAMPLAND,
                    Biome.HELL
            ),
            Arrays.asList(
                    "&5&lGhome Khorasani - Diyar Ferdowsi",
                    "&7Khorasani ha, mardomane farhang va adab",
                    "&7Markaz: Mashhad, Neyshabur - Ferdowsi, Shahnameh",
                    "&a&lVizhegi: Shans ketab, Zafaran"
            ),
            Arrays.asList("&5&lGhome Khorasani - Diyar Ferdowsi")
    ),

    SISTANI(
            "SISTANI",
            "Ghome Sistani - Diyar Rostam",
            "Ghome Sistani - Diyar Rostam",
            "Sistani",
            Material.SANDSTONE,
            Arrays.asList(
                    Biome.MUTATED_DESERT,
                    Biome.DESERT,
                    Biome.MESA
            ),
            Arrays.asList(
                    Biome.ICE_FLATS,
                    Biome.TAIGA_COLD
            ),
            Arrays.asList(
                    "&e&lGhome Sistani - Diyar Rostam Dastan",
                    "&7Sistani ha, az nasle Rostam Dastan",
                    "&7Markaz: Zabol, Zahedan - Shahr Sookhte 5000 sale",
                    "&a&lVizhegi: Ghodrat Rostam 2, Moghavemat kavir"
            ),
            Arrays.asList("&e&lGhome Sistani - Diyar Rostam Dastan")
    );

    private final String id;
    private final String finglishName;
    private final String persianName;
    private final String englishName;
    private final Material icon;
    private final List<Biome> homeBiomes;
    private final List<Biome> hostileBiomes;
    private final List<String> loreFinglish;
    private final List<String> lorePersian;

    RaceType(String id, String finglishName, String persianName, String englishName, Material icon,
             List<Biome> homeBiomes, List<Biome> hostileBiomes, List<String> loreFinglish, List<String> lorePersian) {
        this.id = id;
        this.finglishName = finglishName;
        this.persianName = persianName;
        this.englishName = englishName;
        this.icon = icon;
        this.homeBiomes = homeBiomes;
        this.hostileBiomes = hostileBiomes;
        this.loreFinglish = loreFinglish;
        this.lorePersian = lorePersian;
    }

    public String getId() { return id; }
    public String getFinglishName() { return finglishName; }
    public String getPersianName() { return persianName; }
    public String getEnglishName() { return englishName; }
    public Material getIcon() { return icon; }
    public List<Biome> getHomeBiomes() { return homeBiomes; }
    public List<Biome> getHostileBiomes() { return hostileBiomes; }

    public List<String> getLoreFinglish() { return loreFinglish; }
    public List<String> getLorePersian() { return lorePersian; }

    public List<String> getLore(boolean isFinglish) {
        return loreFinglish;
    }

    public String getDisplayName(boolean isFinglish) {
        return finglishName;
    }

    // Backward compatibility
    public List<String> getLore() { return loreFinglish; }
    public String getDisplayName() { return "§6" + finglishName + " §7(" + englishName + ")"; }

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
}

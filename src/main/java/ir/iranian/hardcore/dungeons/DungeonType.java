package ir.iranian.hardcore.dungeons;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 30 Historical Iranian Dungeons (v5.0 Finglish)
 * Covers every biome in Minecraft 1.12.2 with authentic 7,000-year Iranian history.
 * Completely Finglish, zero Persian Unicode characters.
 */
public enum DungeonType {

    ALAMUT_CASTLE(
            "ALAMUT_CASTLE", "Ghale Alamoat", "Alamut Castle", "Ghale-ye afsanehi-ye Hassan Sabbah dar Alborz",
            Material.COBBLESTONE,
            Arrays.asList(Biome.EXTREME_HILLS, Biome.MUTATED_EXTREME_HILLS, Biome.EXTREME_HILLS_WITH_TREES, Biome.SMALLER_EXTREME_HILLS),
            Arrays.asList("&8&lGhale Alamoat - Ashiyaneh Oghab", "&7Ghale-ye nofooz-napazir bar faraz-e sakhreh-haye Alborz",
                    "&7Sakhteh-shodeh tavasot-e Hassan Sabbah dar saal-e 1090 Miladi", "&cBoss: &fHassan Sabbah - Pir-e Kohestan",
                    "&cSakhti: &4★★★★★", "&eLoot: &fShamshir-e Alamoat, Ketab-haye Ismaili"),
            "Hassan Sabbah", 5, 25, 20, 25),

    ANAHITA_TEMPLE(
            "ANAHITA_TEMPLE", "Maabade Anahita", "Anahita Temple", "Maabad-e bastani-ye Elaheh-ye Ab dar Kangavar",
            Material.SMOOTH_BRICK,
            Arrays.asList(Biome.FOREST, Biome.FOREST_HILLS, Biome.MUTATED_FOREST, Biome.ROOFED_FOREST),
            Arrays.asList("&b&lMaabade Anahita - Elaheh Ab", "&7Bozorgtarin benaye sangi bad az Takht Jamshid",
                    "&732 sotoon-e sangin dar keshvar-e Pars", "&cBoss: &fKahen-e Anahita",
                    "&cSakhti: &e★★★☆☆", "&eLoot: &fTandis-e Anahita, Morvarid"),
            "Kahen-e Anahita", 3, 20, 15, 20),

    ARGE_BAM(
            "ARGE_BAM", "Arge Bam", "Arg-e Bam Citadel", "Bozorgtarin benaye kheshti dar jahan - 2500 saleh",
            Material.SANDSTONE,
            Arrays.asList(Biome.DESERT, Biome.DESERT_HILLS, Biome.MUTATED_DESERT),
            Arrays.asList("&6&lArge Bam - Bozorgtarin Benaye Kheshti", "&7Desh-e bastani dar masir-e Jadeh-ye Abrisham",
                    "&7Sabt-e Yunesko ba 200,000 metr moraba", "&cBoss: &fHakem-e Bam",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fFarsh-e Kermani, Khesht-e Talaei"),
            "Hakem-e Bam", 4, 30, 18, 30),

    BANDAR_SIRAF(
            "BANDAR_SIRAF", "Bandar Siraf", "Siraf Port", "Bandar-e daryaei-ye Sasani dar Khalij-e Fars",
            Material.PRISMARINE,
            Arrays.asList(Biome.OCEAN, Biome.DEEP_OCEAN, Biome.BEACHES, Biome.STONE_BEACH),
            Arrays.asList("&9&lBandar Siraf - Morvarid Khalij Fars", "&7Bandar-e tejarati-ye kohan be Chin va Hend",
                    "&7Ghadamat-e Sasani dar sahel-e Boushehr", "&cBoss: &fNakhoda-ye Siraf",
                    "&cSakhti: &e★★★☆☆", "&eLoot: &fMorvarid-e Khalij-e Fars, Naghsheh Ganj"),
            "Nakhoda-ye Siraf", 3, 22, 16, 22),

    GHALEH_BABAK(
            "GHALEH_BABAK", "Ghale Babak", "Babak Castle", "Dejh-e Babak Khorramdin dar kooh-haye barfi",
            Material.STONE,
            Arrays.asList(Biome.ICE_FLATS, Biome.ICE_MOUNTAINS, Biome.MUTATED_ICE_FLATS, Biome.COLD_BEACH),
            Arrays.asList("&f&lGhale Babak - Dejh-e Khorramdinan", "&7Ghale-ye kohestani dar ertefa-e 2300 metri",
                    "&722 saal moghavemat dar barabar-e doshmanan", "&cBoss: &fBabak Khorramdin",
                    "&cSakhti: &4★★★★★", "&eLoot: &fShamshir-e Babak, Derafsh-e Sorkh"),
            "Babak Khorramdin", 5, 24, 18, 24),

    CHOGHA_ZANBIL(
            "CHOGHA_ZANBIL", "Ziggurat Chogha Zanbil", "Chogha Zanbil Ziggurat", "Ziggurat-e 3250 saleh-ye Elamite dar Khuzestan",
            Material.HARD_CLAY,
            Arrays.asList(Biome.SWAMPLAND, Biome.MUTATED_SWAMPLAND),
            Arrays.asList("&e&lZiggurat Chogha Zanbil", "&7Maabad-e pelakani dar 1250 ghable milad",
                    "&7Nakhostin asar-e sabt-e jahani-ye Iran", "&cBoss: &fKahen-e Elamite",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fLowh-e Elamite, Ajoor-e Khatt-e Mikhi"),
            "Kahen-e Elamite", 4, 28, 22, 28),

    DAKHMEH_ZARTOSHTI(
            "DAKHMEH_ZARTOSHTI", "Dakhme Zartoshti", "Zoroastrian Tower of Silence", "Borj-e khamooshan va atash-e javidan",
            Material.NETHER_BRICK,
            Arrays.asList(Biome.HELL),
            Arrays.asList("&4&lDakhme Zartoshti - Borj-e Khamooshan", "&7Borj-e sangi bar faraz-e kooh baraye aein-e bastan",
                    "&7Atashkadeh-ye atash-e moqadas", "&cBoss: &fMobad-e Zartoshti",
                    "&cSakhti: &4★★★★★", "&eLoot: &fAtash-e Moqaddas, Jaam-e Jam"),
            "Mobad-e Zartoshti", 5, 20, 20, 20),

    TAKHT_JAMSHID_SKY(
            "TAKHT_JAMSHID_SKY", "Takht Jamshid Sky", "Persepolis of the Sky", "Kakh-e Apadana dar aseman-e End",
            Material.QUARTZ_BLOCK,
            Arrays.asList(Biome.SKY),
            Arrays.asList("&d&lTakht Jamshid - Parseh", "&7Peytakht-e bashokooh-e Hakhamaneshi dar 515 ghable milad",
                    "&7Kakh-e Apadana ba sotoon-haye 20 metri", "&cBoss: &fDariush Bozorg",
                    "&cSakhti: &5★★★★★★", "&eLoot: &fTaj-e Kourosh, Manshoor-e Hakhamaneshi"),
            "Dariush Bozorg", 6, 32, 25, 32),

    PASARGAD_TOMB(
            "PASARGAD_TOMB", "Aramgah Kourosh", "Pasargadae Tomb", "Aramgah-e Kourosh-e Bozorg dar dasht",
            Material.SANDSTONE,
            Arrays.asList(Biome.PLAINS, Biome.MUTATED_PLAINS),
            Arrays.asList("&6&lAramgah Kourosh - Pasargad", "&7Bonyan-gozar-e nakhostin emperatori-ye jahan",
                    "&7Manshoor-e hoghoogh-e bashar", "&cBoss: &fRooh-e Kourosh Bozorg",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fManshoor-e Kourosh, Shamshir-e Parseh"),
            "Kourosh Bozorg", 4, 26, 18, 26),

    BISOTUN_INSCRIPTION(
            "BISOTUN_INSCRIPTION", "Katibe Bisotun", "Bisotun Inscription", "Bozorgtarin katibeh-ye khatt-e mikhi-ye jahan",
            Material.STONE,
            Arrays.asList(Biome.EXTREME_HILLS, Biome.SMALLER_EXTREME_HILLS),
            Arrays.asList("&8&lKatibe Bisotun - Kermanshah", "&7Katibeh-ye 520 ghable milad dar del-e kooh",
                    "&7Piroozi-ye Dariush bar 9 padeshah-e dorooghgo", "&cBoss: &fSepahbod-e Dariush",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fKhatt-e Mikhi, Sang-e Avesta"),
            "Sepahbod-e Dariush", 4, 22, 15, 22),

    NAQSH_ROSTAM(
            "NAQSH_ROSTAM", "Naghsh Rostam", "Naqsh-e Rostam Necropolis", "Aramgah-e 4 shah-e Hakhamaneshi dar del-e sakhreh",
            Material.RED_SANDSTONE,
            Arrays.asList(Biome.MESA, Biome.MESA_ROCK, Biome.MESA_CLEAR_ROCK, Biome.MUTATED_MESA),
            Arrays.asList("&c&lNaghsh Rostam - Ka'abeh Zartosht", "&7Aramgah-haye chalipa dar del-e kooh-e Hajiabad",
                    "&7Kourosh, Dariush, Khashayarsha, Ardeshir", "&cBoss: &fMohafez-e Chalipa",
                    "&cSakhti: &4★★★★★", "&eLoot: &fSang-e Hakhamaneshi, Khesht-e Zarrin"),
            "Mohafez-e Chalipa", 5, 28, 20, 28),

    TAKHT_SOLEYMAN(
            "TAKHT_SOLEYMAN", "Takht Soleyman", "Takht-e Soleyman", "Daryacheh-ye sorkh va atashkadeh-ye Azargoshnasp",
            Material.PACKED_ICE,
            Arrays.asList(Biome.TAIGA_COLD, Biome.MUTATED_TAIGA_COLD),
            Arrays.asList("&b&lTakht Soleyman - Azargoshnasp", "&7Atashkadeh-ye padeshahan va arteshdaran-e Sasani",
                    "&7Daryacheh-ye asrare-amiz dar del-e dahaneh-ye atashfashan", "&cBoss: &fMobad Azargoshnasp",
                    "&cSakhti: &4★★★★★", "&eLoot: &fSho'leh-ye Azargoshnasp, Yakh-e Moqaddas"),
            "Mobad Azargoshnasp", 5, 25, 20, 25),

    HEGMATANEH(
            "HEGMATANEH", "Hegmataneh", "Ecbatana Ancient Capital", "Peytakht-e Mad-ha ba 7 divar-e rangin dar Hamedan",
            Material.CLAY,
            Arrays.asList(Biome.TAIGA, Biome.TAIGA_HILLS),
            Arrays.asList("&2&lHegmataneh - Hamedan", "&7Nakhostin peytakht-e Iran-zamin dar 700 ghable milad",
                    "&77 divar-e rangin ba sotoon-haye zar-afshan", "&cBoss: &fDiyako Padeshah-e Mad",
                    "&cSakhti: &e★★★☆☆", "&eLoot: &fTala-ye Mad, Soofal-e Hamedan"),
            "Diyako Padeshah-e Mad", 3, 24, 16, 24),

    SUSA_PALACE(
            "SUSA_PALACE", "Kakh Shush", "Susa Palace Apadana", "Kakh-e zamestani-ye Hakhamaneshi dar Khuzestan",
            Material.SMOOTH_BRICK,
            Arrays.asList(Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS),
            Arrays.asList("&a&lKakh Shush - Apadana", "&7Shahr-e 6000 saleh va shokooh-e emperatori",
                    "&7Ajoor-haye lo'abdare shush", "&cBoss: &fSarbaz-e Javidan-e Arshad",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fAjoor-e Lo'abdare Shush, Zereh-ye Pars"),
            "Sarbaz-e Javidan-e Arshad", 4, 25, 17, 25),

    YAZD_JAMEH_MOSQUE(
            "YAZD_JAMEH_MOSQUE", "Masjed Jame Yazd", "Yazd Grand Mosque", "Bolandtarin menareh-ye jahan - 52 metr",
            Material.SANDSTONE,
            Arrays.asList(Biome.DESERT_HILLS, Biome.SAVANNA_ROCK),
            Arrays.asList("&e&lMasjed Jame Yazd - Kavir", "&7Menareh-haye 52 metri va kashi-kari-ye mo'araghir",
                    "&7Shahkar-e me'mari-ye khesht va kashi", "&cBoss: &fOstad-e Me'mar",
                    "&cSakhti: &e★★★☆☆", "&eLoot: &fKashi-ye Firoozeh, Sang-e Yazd"),
            "Ostad-e Me'mar", 3, 22, 28, 22),

    AZADI_TOWER(
            "AZADI_TOWER", "Borj Azadi", "Azadi Freedom Tower", "Namad-e Iran-e modern va me'mari-ye Sasanid",
            Material.QUARTZ_BLOCK,
            Arrays.asList(Biome.SAVANNA, Biome.MUTATED_SAVANNA),
            Arrays.asList("&f&lBorj Azadi - Tehran", "&7Me'mari-ye talfighi-ye Sasani va Islami ba sang-e marmar",
                    "&7Namad-e payetakht-e Iran", "&cBoss: &fMohafez-e Borj",
                    "&cSakhti: &e★★★☆☆", "&eLoot: &fSang-e Marmar-e Isfahan, Derafsh-e Iran"),
            "Mohafez-e Borj", 3, 20, 25, 20),

    DARVAZEH_MELLAL(
            "DARVAZEH_MELLAL", "Darvazeh Mellal", "Gate of All Nations", "Darvazeh-ye vashokooh-e Takht Jamshid",
            Material.SMOOTH_BRICK,
            Arrays.asList(Biome.PLAINS, Biome.DESERT),
            Arrays.asList("&6&lDarvazeh Mellal - Parseh", "&7Mojassameh-haye bozorg-e Lamassu ba sar-e ensan va bal-e oghab",
                    "&7Paziraei az tamam-e mellat-haye jahan", "&cBoss: &fLamassu - Gav-e Bal-dar",
                    "&cSakhti: &4★★★★★", "&eLoot: &fTash-e Lamassu, Tala-ye Hakhamaneshi"),
            "Lamassu", 5, 26, 18, 26),

    BAZAAR_TABRIZ(
            "BAZAAR_TABRIZ", "Bazaar Bozorg Tabriz", "Tabriz Grand Bazaar", "Bozorgtarin bazaar-e sar-pooshideh-ye jahan",
            Material.BRICK,
            Arrays.asList(Biome.EXTREME_HILLS_WITH_TREES, Biome.TAIGA),
            Arrays.asList("&c&lBazaar Bozorg Tabriz", "&75500 hejreh dar masir-e Jadeh-ye Abrisham",
                    "&7Markaz-e tejarat-e farsh va advee-ye shargh", "&cBoss: &fBazargan-e Bozorg",
                    "&cSakhti: &e★★★☆☆", "&eLoot: &fFarsh-e Tabriz, Sekkeh-ye Zarrin"),
            "Bazargan-e Bozorg", 3, 30, 16, 30),

    BAZAAR_KASHAN(
            "BAZAAR_KASHAN", "Bazaar Kashan", "Historic Kashan Bazaar", "Timcheh Aminodowleh ba taq-bandi-ye bi-nazir",
            Material.SANDSTONE,
            Arrays.asList(Biome.DESERT, Biome.SAVANNA),
            Arrays.asList("&e&lBazaar Sonnati Kashan", "&7Timcheh-ye Aminodowleh va me'mari-ye gozar-gah-haye kavir",
                    "&cBoss: &fTajer-e Kashan", "&cSakhti: &e★★★☆☆", "&eLoot: &fGolab-e Kashan, Farsh-e Abrisham"),
            "Tajer-e Kashan", 3, 25, 18, 25),

    MASOOLEH(
            "MASOOLEH", "Roosta-ye Masooleh", "Masooleh Stepped Village", "Roosta-ye pelekan-e Gilan dar del-e jangal",
            Material.WOOD,
            Arrays.asList(Biome.ROOFED_FOREST, Biome.FOREST),
            Arrays.asList("&2&lRoosta-ye Masooleh - Gilan", "&7Hayat-e khaneh-ye bala, posht-e bam-e khaneh-ye payin ast!",
                    "&7Hezar saal ghedmat dar jangal-haye Hirkani", "&cBoss: &fPir-e Masooleh",
                    "&cSakhti: &e★★★☆☆", "&eLoot: &fChai-ye Lahijan, Sanaye Dasti Gilan"),
            "Pir-e Masooleh", 3, 24, 20, 24),

    KANDOVAN(
            "KANDOVAN", "Roosta-ye Kandovan", "Kandovan Rock Dwellings", "Khaneh-haye kalleh-ghandi dar del-e sang-haye Sahand",
            Material.STONE,
            Arrays.asList(Biome.EXTREME_HILLS, Biome.ICE_FLATS),
            Arrays.asList("&7&lRoosta-ye Kandovan - Azarbayjan", "&7Sakhteh-shodeh tavasot-e atashfashan-e Sahand",
                    "&7Sard dar tabestan va garm dar zemestan", "&cBoss: &fMohafez-e Sahand",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fAsal-e Sahand, Sang-e Atashfashani"),
            "Mohafez-e Sahand", 4, 24, 18, 24),

    SHUSHTAR_WATER(
            "SHUSHTAR_WATER", "Sazeh-haye Abi Shushtar", "Shushtar Hydraulic System", "Shahkar-e mohandesi-ye ab dar dowreh-ye Sasani",
            Material.PRISMARINE,
            Arrays.asList(Biome.RIVER, Biome.SWAMPLAND),
            Arrays.asList("&b&lSazeh-haye Abi Shushtar", "&7Asiab-ha, abshar-ha va canal-haye 2000 saleh",
                    "&7Bozorgtarin majmoo'eh-ye san'ati-ye jahan-e bastan", "&cBoss: &fMohandes-e Sasani",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fCharkh-e Ab, Nil-e Karun"),
            "Mohandes-e Sasani", 4, 26, 16, 26),

    GONBAD_KAVOUS(
            "GONBAD_KAVOUS", "Gonbad Kavous", "Gonbad-e Qabus Brick Tower", "Bolandtarin borj-e tamam-ajoori-ye jahan - 72 metr",
            Material.BRICK,
            Arrays.asList(Biome.PLAINS, Biome.SAVANNA),
            Arrays.asList("&6&lGonbad Kavous - Golestan", "&7Shahkar-e hezar saleh-ye Ziyaran dar 1006 Miladi",
                    "&7Ertefa-e 72 metri bedoon-e heech asibi dar zelzeleh-ha", "&cBoss: &fQaboos Voshmgir",
                    "&cSakhti: &4★★★★★", "&eLoot: &fAjoor-e Gonbad, Shamshir-e Torkaman"),
            "Qaboos Voshmgir", 5, 20, 30, 20),

    SOLTANIYEH(
            "SOLTANIYEH", "Gonbad Soltaniyeh", "Soltaniyeh Turquoise Dome", "Bozorgtarin gonbad-e aajoori-ye jahan dar Zanjan",
            Material.SMOOTH_BRICK,
            Arrays.asList(Biome.EXTREME_HILLS, Biome.PLAINS),
            Arrays.asList("&3&lGonbad Soltaniyeh - Zanjan", "&7Gonbad-e do-poosheh-ye firoozeh-ei ba ertefa-e 50 metr",
                    "&7Elham-bakhsh-e Kelisaye Santa Maria dar Florence", "&cBoss: &fSoltan Mohammad Khodabandeh",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fFiroozeh-ye Soltaniyeh, Tigh-e Zanjani"),
            "Soltan Mohammad Khodabandeh", 4, 28, 24, 28),

    BAGH_FIN(
            "BAGH_FIN", "Bagh-e Fin", "Fin Garden & Royal Bath", "Bagh-e Irani-ye bashokooh va cheshmeh-ye Soleymaniyeh",
            Material.SANDSTONE,
            Arrays.asList(Biome.DESERT_HILLS, Biome.SAVANNA),
            Arrays.asList("&a&lBagh-e Fin Kashan", "&7Ghadimitireen bagh-e mojood dar Iran ba joo-haye firoozeh-ei",
                    "&7Gozargah-e Amir Kabir", "&cBoss: &fMohafez-e Cheshmeh",
                    "&cSakhti: &e★★★☆☆", "&eLoot: &fAb-e Cheshmeh Soleymaniyeh, Gol-e Mohammadi"),
            "Mohafez-e Cheshmeh", 3, 25, 15, 25),

    NASIR_MOSQUE(
            "NASIR_MOSQUE", "Masjed Nasir al-Mulk", "Pink Mosque of Shiraz", "Masjed-e shisheh-haye rangin va noor-e firoozeh",
            Material.STAINED_CLAY,
            Arrays.asList(Biome.PLAINS, Biome.FOREST),
            Arrays.asList("&d&lMasjed Nasir al-Mulk - Shiraz", "&7Raghse noor va rang az shisheh-haye haft-rang",
                    "&cBoss: &fMe'mar-e Shiraz", "&cSakhti: &e★★★☆☆", "&eLoot: &fShisheh-ye Haft Rang, Zaferan"),
            "Me'mar-e Shiraz", 3, 22, 16, 22),

    VANK_CATHEDRAL(
            "VANK_CATHEDRAL", "Kelisaye Vank", "Vank Cathedral", "Kelisaye zarrin-e Julfa dar Isfahan - dowreh Safavi",
            Material.BRICK,
            Arrays.asList(Biome.FOREST, Biome.PLAINS),
            Arrays.asList("&6&lKelisaye Vank - Isfahan", "&7Naghshi az Shah Abbas baraye Aramaneh-ye Julfa",
                    "&7Naghshi-haye talaei bar rooye divar-ha", "&cBoss: &fAsghof-e Julfa",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fEnghil-e Khatti, Tala-ye Julfa"),
            "Asghof-e Julfa", 4, 24, 18, 24),

    ALI_QAPU(
            "ALI_QAPU", "Kakh Ali Qapu", "Ali Qapu Palace", "Kakh-e shash-tabagheh dar Meidan-e Naghsh-e Jahan",
            Material.WOOD,
            Arrays.asList(Biome.PLAINS, Biome.SAVANNA),
            Arrays.asList("&6&lKakh Ali Qapu - Isfahan", "&7Talare mosighi ba gong-ha va tang-haye sotooni",
                    "&7Paziraei-ye Shah Abbas az safiran-e oroupaei", "&cBoss: &fShah Abbas Safavi",
                    "&cSakhti: &4★★★★★", "&eLoot: &fShamshir-e Safavi, Farsh-e Isfahan"),
            "Shah Abbas Safavi", 5, 25, 26, 25),

    CHEHEL_SOTUN(
            "CHEHEL_SOTUN", "Kakh Chehel Sotun", "Chehel Sotun 40 Columns", "Kakh-e 20 sotoone ke dar ab 40 sotoon dideh mishavad",
            Material.WOOD,
            Arrays.asList(Biome.FOREST, Biome.SWAMPLAND),
            Arrays.asList("&e&lChehel Sotun - Isfahan", "&7Divar-negareh-haye jange Chaldoran va Karnal",
                    "&7Estakhr-e bozorg dar moghabele kakh", "&cBoss: &fSepahsalar-e Safavi",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fNeyzeh-ye Safavi, Ayeneh-kari"),
            "Sepahsalar-e Safavi", 4, 28, 16, 28),

    SI_O_SE_POL(
            "SI_O_SE_POL", "Pol Si o Se Pol", "Si-o-se-pol Bridge", "Pol-e 33 dahaneh bar rooye Zayandeh Rood",
            Material.SMOOTH_BRICK,
            Arrays.asList(Biome.RIVER, Biome.PLAINS),
            Arrays.asList("&b&lPol Si o Se Pol - Isfahan", "&7Sakhteh-shodeh tavasot-e Allahverdi Khan dar 1602 Miladi",
                    "&7Tool-e 300 metri ba 33 cheshmeh", "&cBoss: &fAllahverdi Khan",
                    "&cSakhti: &c★★★★☆", "&eLoot: &fShamshir-e Zayandeh Rood, Sekkeh Derik"),
            "Allahverdi Khan", 4, 32, 14, 16);

    private final String id;
    private final String finglishName;
    private final String englishName;
    private final String description;
    private final Material primaryBlock;
    private final List<Biome> biomes;
    private final List<String> lore;
    private final String bossName;
    private final int difficulty;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;

    DungeonType(String id, String finglishName, String englishName, String description,
                Material primaryBlock, List<Biome> biomes, List<String> lore,
                String bossName, int difficulty, int sizeX, int sizeY, int sizeZ) {
        this.id = id;
        this.finglishName = finglishName;
        this.englishName = englishName;
        this.description = description;
        this.primaryBlock = primaryBlock;
        this.biomes = biomes;
        this.lore = lore;
        this.bossName = bossName;
        this.difficulty = difficulty;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public String getId() { return id; }
    public String getFinglishName() { return finglishName; }
    public String getPersianName() { return finglishName; }
    public String getEnglishName() { return englishName; }
    public String getDisplayName() { return finglishName; }
    public String getDescription() { return description; }
    public Material getPrimaryBlock() { return primaryBlock; }
    public List<Biome> getBiomes() { return Collections.unmodifiableList(biomes); }
    public List<String> getLore() { return Collections.unmodifiableList(lore); }
    public String getBossName() { return bossName; }
    public int getDifficulty() { return difficulty; }
    public int getSizeX() { return sizeX; }
    public int getSizeY() { return sizeY; }
    public int getSizeZ() { return sizeZ; }

    public static DungeonType getForBiome(Biome biome) {
        for (DungeonType type : values()) {
            if (type.biomes.contains(biome)) {
                return type;
            }
        }
        String name = biome.name();
        if (name.contains("DESERT")) return ARGE_BAM;
        if (name.contains("ICE") || name.contains("COLD")) return GHALEH_BABAK;
        if (name.contains("OCEAN") || name.contains("BEACH")) return BANDAR_SIRAF;
        if (name.contains("SWAMP")) return CHOGHA_ZANBIL;
        if (name.contains("MESA")) return NAQSH_ROSTAM;
        if (name.contains("HILLS") || name.contains("MOUNTAIN")) return ALAMUT_CASTLE;
        if (name.contains("HELL")) return DAKHMEH_ZARTOSHTI;
        if (name.contains("SKY")) return TAKHT_JAMSHID_SKY;
        if (name.contains("FOREST")) return ANAHITA_TEMPLE;
        return PASARGAD_TOMB;
    }
}

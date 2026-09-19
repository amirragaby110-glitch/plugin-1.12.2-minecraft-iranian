package ir.iranian.hardcore.dungeons;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.items.PersianItems;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Procedural Iranian Dungeon & Architecture Builder (v5.0 Finglish)
 * Builds grand, authentic Persian historical monuments, palaces, and fortresses
 * using authentic architectural elements (Apadana hypostyle columns, Chahar-Taq fire arches,
 * Iwan gateways, Badgir windcatchers, stepped ziggurats, and vaulted bazaars).
 */
public class StructureBuilder {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public StructureBuilder(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    public boolean buildDungeon(Location origin, DungeonType type) {
        origin = findGround(origin);
        if (origin == null) return false;

        plugin.getLogger().info("Sakht-e dungeon-e Irani " + type.getFinglishName() + " dar " + origin.getBlockX() + "," + origin.getBlockY() + "," + origin.getBlockZ());

        switch (type) {
            case ALAMUT_CASTLE: return buildAlamutCastle(origin);
            case ANAHITA_TEMPLE: return buildAnahitaTemple(origin);
            case ARGE_BAM: return buildArgeBam(origin);
            case BANDAR_SIRAF: return buildBandarSiraf(origin);
            case GHALEH_BABAK: return buildGhalehBabak(origin);
            case CHOGHA_ZANBIL: return buildChoghaZanbil(origin);
            case DAKHMEH_ZARTOSHTI: return buildDakhmeh(origin);
            case TAKHT_JAMSHID_SKY:
            case DARVAZEH_MELLAL: return buildTakhtJamshid(origin);
            case PASARGAD_TOMB: return buildPasargad(origin);
            case BISOTUN_INSCRIPTION: return buildBisotun(origin);
            case NAQSH_ROSTAM: return buildNaqshRostam(origin);
            case TAKHT_SOLEYMAN: return buildTakhtSoleyman(origin);
            case HEGMATANEH: return buildHegmataneh(origin);
            case SUSA_PALACE: return buildSusaPalace(origin);
            case YAZD_JAMEH_MOSQUE: return buildYazdMosque(origin);
            case AZADI_TOWER: return buildAzadiTower(origin);
            case BAZAAR_TABRIZ:
            case BAZAAR_KASHAN: return buildVaultedBazaar(origin, type);
            case MASOOLEH: return buildMasoolehSteppedVillage(origin);
            case KANDOVAN: return buildKandovanRockHouses(origin);
            case SHUSHTAR_WATER: return buildShushtarWaterMills(origin);
            case GONBAD_KAVOUS: return buildGonbadKavousTower(origin);
            case SOLTANIYEH: return buildSoltaniyehDome(origin);
            case BAGH_FIN: return buildBaghFinGarden(origin);
            case NASIR_MOSQUE: return buildNasirAlMulk(origin);
            case VANK_CATHEDRAL: return buildVankCathedral(origin);
            case ALI_QAPU: return buildAliQapuPalace(origin);
            case CHEHEL_SOTUN: return buildChehelSotun(origin);
            case SI_O_SE_POL: return buildSiosepolBridge(origin);
            default: return buildTakhtJamshid(origin);
        }
    }

    private Location findGround(Location loc) {
        World world = loc.getWorld();
        if (world == null) return null;
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int y = world.getHighestBlockYAt(x, z);
        if (y < 5) return null;
        if (world.getEnvironment() == World.Environment.NETHER) y = 60;
        else if (world.getEnvironment() == World.Environment.THE_END) y = 70;
        return new Location(world, x, y, z);
    }

    // ==========================================
    // 1. TAKHT JAMSHID (Persepolis Apadana Palace)
    // ==========================================
    private boolean buildTakhtJamshid(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 1. Grand Stone Terrace (36x36)
        fillArea(world, ox - 18, oy - 3, oz - 18, ox + 18, oy, oz + 18, Material.SMOOTH_BRICK);

        // 2. Apadana Hypostyle Hall: 16 Fluted Columns (Quartz Pillars with decorative capitals)
        for (int x = -12; x <= 12; x += 8) {
            for (int z = -12; z <= 12; z += 8) {
                buildPersianColumn(world, ox + x, oy + 1, oz + z, 8, Material.QUARTZ_BLOCK);
            }
        }

        // 3. Palace Roof (Stairs and stone slabs)
        fillArea(world, ox - 16, oy + 9, oz - 16, ox + 16, oy + 9, oz + 16, Material.STEP);

        // 4. Elevated Royal Throne Platform in center
        fillArea(world, ox - 4, oy + 1, oz - 4, ox + 4, oy + 2, oz + 4, Material.QUARTZ_BLOCK);
        fillArea(world, ox - 3, oy + 2, oz - 3, ox + 3, oy + 2, oz + 3, Material.CARPET, (byte) 14); // Persian Red Carpet
        setBlock(world, ox, oy + 3, oz, Material.GOLD_BLOCK, false); // Throne seat
        setBlock(world, ox, oy + 4, oz, Material.RED_SANDSTONE_STAIRS, false);

        // 5. Fire Braziers flanking the throne
        buildFireBrazier(world, ox - 5, oy + 1, oz - 2);
        buildFireBrazier(world, ox + 5, oy + 1, oz - 2);

        // 6. Royal Treasure Vault
        fillArea(world, ox - 2, oy - 2, oz - 2, ox + 2, oy, oz + 2, Material.IRON_FENCE);
        placeChestWithLoot(world, ox - 1, oy - 1, oz, DungeonType.TAKHT_JAMSHID_SKY);
        placeChestWithLoot(world, ox + 1, oy - 1, oz, DungeonType.TAKHT_JAMSHID_SKY);

        // 7. Immortal Guards & Boss
        spawnGuard(world, ox - 4, oy + 3, oz - 2, "&6Garde Javidan Hakhamaneshi", EntityType.SKELETON, Material.GOLD_SWORD, Material.GOLD_HELMET);
        spawnGuard(world, ox + 4, oy + 3, oz - 2, "&6Garde Javidan Hakhamaneshi", EntityType.SKELETON, Material.GOLD_SWORD, Material.GOLD_HELMET);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 3, oz + 3), DungeonType.TAKHT_JAMSHID_SKY);

        return true;
    }

    // ==========================================
    // 2. ALAMUT CASTLE (Assassins Mountain Fortress)
    // ==========================================
    private boolean buildAlamutCastle(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 1. Mountain Foundation
        fillArea(world, ox - 14, oy - 4, oz - 14, ox + 14, oy, oz + 14, Material.COBBLESTONE);

        // 2. Concentric Defense Walls (24x24)
        buildWalls(world, ox, oy, oz, 12, 8, Material.SMOOTH_BRICK);
        buildCrenellations(world, ox, oy + 8, oz, 12, Material.SMOOTH_BRICK);

        // 3. 4 Corner Defensive Watchtowers
        buildTower(world, ox - 12, oy, oz - 12, 3, 14, Material.SMOOTH_BRICK);
        buildTower(world, ox + 12, oy, oz - 12, 3, 14, Material.SMOOTH_BRICK);
        buildTower(world, ox - 12, oy, oz + 12, 3, 14, Material.SMOOTH_BRICK);
        buildTower(world, ox + 12, oy, oz + 12, 3, 14, Material.SMOOTH_BRICK);

        // 4. Central Library & Citadel of Hassan Sabbah
        fillArea(world, ox - 5, oy + 1, oz - 5, ox + 5, oy + 6, oz + 5, Material.WOOD);
        fillArea(world, ox - 4, oy + 1, oz - 4, ox + 4, oy + 5, oz + 4, Material.AIR); // interior
        fillArea(world, ox - 4, oy + 1, oz - 4, ox + 4, oy + 1, oz + 4, Material.CARPET, (byte) 14);

        // Bookshelves & Alchemy Lab
        for (int x = -3; x <= 3; x += 2) {
            setBlock(world, ox + x, oy + 2, oz - 4, Material.BOOKSHELF, false);
            setBlock(world, ox + x, oy + 3, oz - 4, Material.BOOKSHELF, false);
        }
        setBlock(world, ox, oy + 2, oz - 2, Material.ENCHANTMENT_TABLE, false);
        setBlock(world, ox - 3, oy + 2, oz + 3, Material.BREWING_STAND, false);
        setBlock(world, ox + 3, oy + 2, oz + 3, Material.CAULDRON, false);

        // 5. Boss & Secret Loot
        placeChestWithLoot(world, ox, oy + 2, oz + 4, DungeonType.ALAMUT_CASTLE);
        spawnGuard(world, ox - 3, oy + 1, oz, "&8Fadaie Alamut", EntityType.ZOMBIE, Material.IRON_SWORD, Material.CHAINMAIL_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz, "&8Fadaie Alamut", EntityType.ZOMBIE, Material.IRON_SWORD, Material.CHAINMAIL_HELMET);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 2, oz), DungeonType.ALAMUT_CASTLE);

        return true;
    }

    // ==========================================
    // 3. ARGE BAM (Mudbrick Desert Citadel)
    // ==========================================
    private boolean buildArgeBam(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Massive Sandstone Citadel
        buildWalls(world, ox, oy, oz, 15, 7, Material.SANDSTONE);
        buildCrenellations(world, ox, oy + 7, oz, 15, Material.SANDSTONE);

        // Stepped Ramparts
        buildWalls(world, ox, oy + 3, oz, 10, 6, Material.HARD_CLAY);

        // Covered Bazaar corridor inside
        for (int z = -8; z <= 8; z += 4) {
            setBlock(world, ox - 4, oy + 1, oz + z, Material.WOOL, (byte) 14);
            setBlock(world, ox + 4, oy + 1, oz + z, Material.WOOL, (byte) 4);
        }

        // Governor's Palace on top
        fillArea(world, ox - 4, oy + 9, oz - 4, ox + 4, oy + 14, oz + 4, Material.SMOOTH_BRICK);
        fillArea(world, ox - 3, oy + 9, oz - 3, ox + 3, oy + 13, oz + 3, Material.AIR);
        placeChestWithLoot(world, ox, oy + 10, oz, DungeonType.ARGE_BAM);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 10, oz), DungeonType.ARGE_BAM);
        return true;
    }

    // ==========================================
    // 4. CHOGHA ZANBIL (Elamite Stepped Ziggurat)
    // ==========================================
    private boolean buildChoghaZanbil(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 3-Tiered Terraced Pyramid
        fillArea(world, ox - 16, oy, oz - 16, ox + 16, oy + 3, oz + 16, Material.CLAY_BRICK);
        fillArea(world, ox - 11, oy + 3, oz - 11, ox + 11, oy + 6, oz + 11, Material.HARD_CLAY);
        fillArea(world, ox - 6, oy + 6, oz - 6, ox + 6, oy + 9, oz + 6, Material.SMOOTH_BRICK);

        // Monumental Stairs ascending the south face
        for (int y = 0; y < 9; y++) {
            setBlock(world, ox - 1, oy + y + 1, oz - 16 + y, Material.BRICK_STAIRS, false);
            setBlock(world, ox, oy + y + 1, oz - 16 + y, Material.BRICK_STAIRS, false);
            setBlock(world, ox + 1, oy + y + 1, oz - 16 + y, Material.BRICK_STAIRS, false);
        }

        // Temple of Inshushinak on Top
        fillArea(world, ox - 3, oy + 9, oz - 3, ox + 3, oy + 13, oz + 3, Material.CLAY_BRICK);
        fillArea(world, ox - 2, oy + 9, oz - 2, ox + 2, oy + 12, oz + 2, Material.AIR);

        // Sacrificial Altar with Soul Fire
        setBlock(world, ox, oy + 10, oz, Material.NETHERRACK, false);
        setBlock(world, ox, oy + 11, oz, Material.FIRE, false);
        placeChestWithLoot(world, ox, oy + 10, oz + 2, DungeonType.CHOGHA_ZANBIL);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 10, oz - 1), DungeonType.CHOGHA_ZANBIL);
        return true;
    }

    // ==========================================
    // 5. TAKHT SOLEYMAN (Azargoshasb Fire Temple)
    // ==========================================
    private boolean buildTakhtSoleyman(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Sacred Volcanic Lake (Circular basin with water)
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                if (x*x + z*z <= 80) {
                    setBlock(world, ox + x, oy - 2, oz + z, Material.WATER, false);
                    setBlock(world, ox + x, oy - 3, oz + z, Material.STATIONARY_WATER, false);
                } else if (x*x + z*z <= 110) {
                    setBlock(world, ox + x, oy, oz + z, Material.COBBLESTONE, false);
                }
            }
        }

        // Chahar-Taq Fire Sanctuary on North shore
        int tx = ox, ty = oy, tz = oz + 12;
        fillArea(world, tx - 5, ty, tz - 5, tx + 5, ty + 6, tz + 5, Material.SMOOTH_BRICK);
        // 4 Arches
        fillArea(world, tx - 3, ty + 1, tz - 5, tx + 3, ty + 4, tz + 5, Material.AIR);
        fillArea(world, tx - 5, ty + 1, tz - 3, tx + 5, ty + 4, tz + 3, Material.AIR);

        // Eternal Holy Fire of Azargoshasb
        setBlock(world, tx, ty + 1, tz, Material.GOLD_BLOCK, false);
        setBlock(world, tx, ty + 2, tz, Material.NETHERRACK, false);
        setBlock(world, tx, ty + 3, tz, Material.FIRE, false);

        placeChestWithLoot(world, tx + 2, ty + 1, tz + 2, DungeonType.TAKHT_SOLEYMAN);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, tx, ty + 1, tz - 2), DungeonType.TAKHT_SOLEYMAN);
        return true;
    }

    // ==========================================
    // 6. BANDAR SIRAF (Persian Gulf Port)
    // ==========================================
    private boolean buildBandarSiraf(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Stone Pier & Quays into water
        fillArea(world, ox - 12, oy, oz - 4, ox + 12, oy, oz + 4, Material.SMOOTH_BRICK);

        // Persian Merchant Ship at the pier
        buildPersianShip(world, ox + 5, oy, oz + 10);

        // Port Watchtower
        buildTower(world, ox - 10, oy, oz, 4, 12, Material.SMOOTH_BRICK);
        placeChestWithLoot(world, ox - 10, oy + 1, oz, DungeonType.BANDAR_SIRAF);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.BANDAR_SIRAF);
        return true;
    }

    // ==========================================
    // 7. GHALEH BABAK (Snowy Cliff Fortress)
    // ==========================================
    private boolean buildGhalehBabak(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        fillArea(world, ox - 12, oy - 2, oz - 12, ox + 12, oy, oz + 12, Material.STONE);
        buildWalls(world, ox, oy, oz, 10, 8, Material.COBBLESTONE);
        buildCrenellations(world, ox, oy + 8, oz, 10, Material.COBBLESTONE);

        // Snow layer on top
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                if (Math.abs(x) == 10 || Math.abs(z) == 10) {
                    setBlock(world, ox + x, oy + 9, oz + z, Material.SNOW, false);
                }
            }
        }

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.GHALEH_BABAK);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.GHALEH_BABAK);
        return true;
    }

    // ==========================================
    // 8. PASARGAD (Cyrus the Great Tomb)
    // ==========================================
    private boolean buildPasargad(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 6 Stepped Plinths
        for (int i = 0; i < 6; i++) {
            int r = 10 - i;
            fillArea(world, ox - r, oy + i, oz - r, ox + r, oy + i, oz + r, Material.WHITE_GLAZED_TERRACOTTA);
        }

        // Tomb Chamber
        fillArea(world, ox - 4, oy + 6, oz - 4, ox + 4, oy + 10, oz + 4, Material.QUARTZ_BLOCK);
        fillArea(world, ox - 3, oy + 6, oz - 3, ox + 3, oy + 9, oz + 3, Material.AIR);

        // Gabled Stone Roof
        for (int i = 0; i < 4; i++) {
            fillArea(world, ox - 4 + i, oy + 10 + i, oz - 4, ox + 4 - i, oy + 10 + i, oz + 4, Material.QUARTZ_BLOCK);
        }

        placeChestWithLoot(world, ox, oy + 7, oz, DungeonType.PASARGAD_TOMB);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 7, oz), DungeonType.PASARGAD_TOMB);
        return true;
    }

    // ==========================================
    // 9. BISOTUN & NAQSH ROSTAM (Rock Inscription & Cliff Tombs)
    // ==========================================
    private boolean buildBisotun(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Massive cliff wall
        fillArea(world, ox - 10, oy, oz - 2, ox + 10, oy + 12, oz + 2, Material.STONE);
        // Carved Inscription Relief in center
        fillArea(world, ox - 6, oy + 3, oz - 2, ox + 6, oy + 9, oz - 2, Material.SMOOTH_BRICK);

        placeChestWithLoot(world, ox, oy + 1, oz - 4, DungeonType.BISOTUN_INSCRIPTION);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz - 5), DungeonType.BISOTUN_INSCRIPTION);
        return true;
    }

    private boolean buildNaqshRostam(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Monumental Rock-Cut Cross Tomb (Cruciform facade)
        fillArea(world, ox - 8, oy, oz - 2, ox + 8, oy + 16, oz + 2, Material.HARD_CLAY);
        // Cross cutout
        fillArea(world, ox - 6, oy + 6, oz - 2, ox + 6, oy + 10, oz - 2, Material.RED_SANDSTONE);
        fillArea(world, ox - 2, oy + 2, oz - 2, ox + 2, oy + 14, oz - 2, Material.RED_SANDSTONE);

        // Burial Chamber inside the rock
        fillArea(world, ox - 3, oy + 2, oz + 2, ox + 3, oy + 5, oz + 7, Material.AIR);
        placeChestWithLoot(world, ox, oy + 2, oz + 5, DungeonType.NAQSH_ROSTAM);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz - 4), DungeonType.NAQSH_ROSTAM);
        return true;
    }

    // ==========================================
    // 10. HEGMATANEH & SUSA PALACE
    // ==========================================
    private boolean buildHegmataneh(Location origin) {
        return buildArgeBam(origin);
    }

    private boolean buildSusaPalace(Location origin) {
        return buildTakhtJamshid(origin);
    }

    // ==========================================
    // 11. YAZD MOSQUE & AZADI TOWER
    // ==========================================
    private boolean buildYazdMosque(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Grand Iwan Portal with 2 Twin Turquoise Minarets
        fillArea(world, ox - 6, oy, oz - 4, ox + 6, oy + 10, oz + 4, Material.CLAY_BRICK);
        fillArea(world, ox - 3, oy + 1, oz - 4, ox + 3, oy + 8, oz + 4, Material.AIR); // arched iwan

        // Twin Minarets
        buildTower(world, ox - 6, oy, oz - 4, 2, 22, Material.LAPIS_BLOCK);
        buildTower(world, ox + 6, oy, oz - 4, 2, 22, Material.LAPIS_BLOCK);

        placeChestWithLoot(world, ox, oy + 1, oz + 2, DungeonType.YAZD_JAMEH_MOSQUE);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.YAZD_JAMEH_MOSQUE);
        return true;
    }

    private boolean buildAzadiTower(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Iconic Y-shaped arch of Azadi Tower
        fillArea(world, ox - 8, oy, oz - 4, ox - 4, oy + 16, oz + 4, Material.QUARTZ_BLOCK);
        fillArea(world, ox + 4, oy, oz - 4, ox + 8, oy + 16, oz + 4, Material.QUARTZ_BLOCK);
        // Connecting top arch
        fillArea(world, ox - 8, oy + 16, oz - 4, ox + 8, oy + 22, oz + 4, Material.QUARTZ_BLOCK);

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.AZADI_TOWER);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.AZADI_TOWER);
        return true;
    }

    // ==========================================
    // 12. NEW ARCHITECTURAL TYPES (v5.0)
    // ==========================================
    private boolean buildVaultedBazaar(Location origin, DungeonType type) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 32-block long vaulted brick arcade
        for (int z = -14; z <= 14; z += 4) {
            fillArea(world, ox - 5, oy, oz + z, ox + 5, oy + 5, oz + z + 3, Material.SMOOTH_BRICK);
            fillArea(world, ox - 3, oy + 1, oz + z, ox + 3, oy + 4, oz + z + 3, Material.AIR);
            // Skylight dome
            setBlock(world, ox, oy + 5, oz + z + 1, Material.GLASS, false);
            // Carpet stalls
            setBlock(world, ox - 3, oy + 1, oz + z + 1, Material.CARPET, (byte) 14);
            setBlock(world, ox + 3, oy + 1, oz + z + 1, Material.CARPET, (byte) 4);
        }

        placeChestWithLoot(world, ox, oy + 1, oz, type);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), type);
        return true;
    }

    private boolean buildMasoolehSteppedVillage(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 3 Terraced levels where roof of lower house is yard of upper house
        for (int level = 0; level < 3; level++) {
            int ly = oy + (level * 4);
            int lz = oz + (level * 6);
            fillArea(world, ox - 6, ly, lz - 3, ox + 6, ly + 3, lz + 3, Material.HARD_CLAY);
            fillArea(world, ox - 5, ly + 1, lz - 2, ox + 5, ly + 3, lz + 2, Material.AIR);
            // Wooden balcony
            fillArea(world, ox - 5, ly + 1, lz - 3, ox + 5, ly + 1, lz - 3, Material.FENCE);
        }

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.MASOOLEH);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.MASOOLEH);
        return true;
    }

    private boolean buildKandovanRockHouses(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 3 Conical volcanic cones carved out
        buildVolcanicCone(world, ox - 7, oy, oz);
        buildVolcanicCone(world, ox + 7, oy, oz);
        buildVolcanicCone(world, ox, oy, oz + 8);

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.KANDOVAN);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.KANDOVAN);
        return true;
    }

    private boolean buildShushtarWaterMills(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Water channels and mill race
        fillArea(world, ox - 10, oy, oz - 4, ox + 10, oy + 3, oz + 4, Material.SMOOTH_BRICK);
        fillArea(world, ox - 8, oy + 1, oz - 2, ox + 8, oy + 1, oz + 2, Material.WATER);
        // Waterwheel
        fillArea(world, ox, oy + 1, oz - 3, ox, oy + 4, oz - 3, Material.WOOD);

        placeChestWithLoot(world, ox, oy + 2, oz + 3, DungeonType.SHUSHTAR_WATER);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 2, oz), DungeonType.SHUSHTAR_WATER);
        return true;
    }

    private boolean buildGonbadKavousTower(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Tall decagonal brick tower (28 blocks high) with conical roof
        buildTower(world, ox, oy, oz, 4, 24, Material.BRICK);
        // Conical roof
        for (int i = 0; i < 6; i++) {
            int r = 4 - (i * 4 / 6);
            fillArea(world, ox - r, oy + 24 + i, oz - r, ox + r, oy + 24 + i, oz + r, Material.BRICK);
        }

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.GONBAD_KAVOUS);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.GONBAD_KAVOUS);
        return true;
    }

    private boolean buildSoltaniyehDome(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Octagonal Mausoleum with Turquoise Dome
        fillArea(world, ox - 8, oy, oz - 8, ox + 8, oy + 10, oz + 8, Material.BRICK);
        fillArea(world, ox - 6, oy + 1, oz - 6, ox + 6, oy + 9, oz + 6, Material.AIR);

        // Turquoise Dome on top
        for (int y = 0; y < 6; y++) {
            int r = 6 - y;
            fillArea(world, ox - r, oy + 10 + y, oz - r, ox + r, oy + 10 + y, oz + r, Material.LAPIS_BLOCK);
        }

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.SOLTANIYEH);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.SOLTANIYEH);
        return true;
    }

    private boolean buildBaghFinGarden(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Chahar-Bagh 4 water channels with turquoise fountains
        fillArea(world, ox - 12, oy, oz - 12, ox + 12, oy, oz + 12, Material.GRASS);
        fillArea(world, ox - 12, oy, oz, ox + 12, oy, oz, Material.WATER);
        fillArea(world, ox, oy, oz - 12, ox, oy, oz + 12, Material.WATER);
        // Central pavilion
        fillArea(world, ox - 3, oy + 1, oz - 3, ox + 3, oy + 5, oz + 3, Material.QUARTZ_BLOCK);
        fillArea(world, ox - 2, oy + 1, oz - 2, ox + 2, oy + 4, oz + 2, Material.AIR);

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.BAGH_FIN);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.BAGH_FIN);
        return true;
    }

    private boolean buildNasirAlMulk(Location origin) {
        return buildYazdMosque(origin);
    }

    private boolean buildVankCathedral(Location origin) {
        return buildSoltaniyehDome(origin);
    }

    private boolean buildAliQapuPalace(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 3-story royal palace with elevated columned terrace
        fillArea(world, ox - 8, oy, oz - 8, ox + 8, oy + 12, oz + 8, Material.SMOOTH_BRICK);
        fillArea(world, ox - 6, oy + 1, oz - 6, ox + 6, oy + 11, oz + 6, Material.AIR);

        // Music Hall on top floor with acoustics niches
        fillArea(world, ox - 7, oy + 12, oz - 7, ox + 7, oy + 16, oz + 7, Material.WOOD);
        for (int x = -5; x <= 5; x += 5) {
            buildPersianColumn(world, ox + x, oy + 13, oz - 6, 3, Material.WOOD);
        }

        placeChestWithLoot(world, ox, oy + 13, oz, DungeonType.ALI_QAPU);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 13, oz), DungeonType.ALI_QAPU);
        return true;
    }

    private boolean buildChehelSotun(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 20 Slender Wooden Columns facing a reflecting pool (reflection makes 40!)
        for (int x = -8; x <= 8; x += 4) {
            for (int z = -6; z <= 0; z += 3) {
                buildPersianColumn(world, ox + x, oy + 1, oz + z, 6, Material.WOOD);
            }
        }
        // Reflecting Pool
        fillArea(world, ox - 8, oy, oz - 18, ox + 8, oy, oz - 8, Material.WATER);

        placeChestWithLoot(world, ox, oy + 1, oz + 4, DungeonType.CHEHEL_SOTUN);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz + 4), DungeonType.CHEHEL_SOTUN);
        return true;
    }

    private boolean buildSiosepolBridge(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // Long stone bridge with arches
        for (int z = -15; z <= 15; z += 5) {
            fillArea(world, ox - 3, oy + 2, oz + z, ox + 3, oy + 5, oz + z + 4, Material.CLAY_BRICK);
            fillArea(world, ox - 2, oy + 1, oz + z, ox + 2, oy + 3, oz + z + 4, Material.AIR); // arch
        }

        placeChestWithLoot(world, ox, oy + 3, oz, DungeonType.SI_O_SE_POL);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 3, oz), DungeonType.SI_O_SE_POL);
        return true;
    }

    // ==========================================
    // ARCHITECTURAL HELPER METHODS
    // ==========================================
    private void buildPersianColumn(World world, int x, int y, int z, int height, Material mat) {
        // Base
        setBlock(world, x, y, z, mat, false);
        // Shaft
        for (int i = 1; i < height - 1; i++) {
            setBlock(world, x, y + i, z, mat, false);
        }
        // Capital
        setBlock(world, x, y + height - 1, z, mat, false);
    }

    private void buildFireBrazier(World world, int x, int y, int z) {
        setBlock(world, x, y, z, Material.GOLD_BLOCK, false);
        setBlock(world, x, y + 1, z, Material.NETHERRACK, false);
        setBlock(world, x, y + 2, z, Material.FIRE, false);
    }

    private void buildVolcanicCone(World world, int x, int y, int z) {
        for (int i = 0; i < 8; i++) {
            int r = 5 - (i * 5 / 8);
            fillArea(world, x - r, y + i, z - r, x + r, y + i, z + r, Material.HARD_CLAY);
        }
        // Hollow interior
        fillArea(world, x - 2, y + 1, z - 2, x + 2, y + 5, z + 2, Material.AIR);
    }

    private void buildPersianShip(World world, int ox, int oy, int oz) {
        fillArea(world, ox - 2, oy, oz - 6, ox + 2, oy + 2, oz + 6, Material.WOOD);
        // Mast
        for (int i = 0; i < 8; i++) {
            setBlock(world, ox, oy + 2 + i, oz, Material.FENCE, false);
        }
        // White Sail
        fillArea(world, ox - 3, oy + 5, oz, ox + 3, oy + 8, oz, Material.WOOL);
    }

    private void buildTower(World world, int ox, int oy, int oz, int radius, int height, Material mat) {
        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius || Math.abs(z) == radius) {
                        setBlock(world, ox + x, oy + y, oz + z, mat, false);
                    }
                }
            }
        }
    }

    private void buildWalls(World world, int ox, int oy, int oz, int radius, int height, Material mat) {
        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius || Math.abs(z) == radius) {
                        setBlock(world, ox + x, oy + y, oz + z, mat, false);
                    }
                }
            }
        }
    }

    private void buildCrenellations(World world, int ox, int oy, int oz, int radius, Material mat) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (Math.abs(x) == radius || Math.abs(z) == radius) {
                    if ((x + z) % 2 == 0) {
                        setBlock(world, ox + x, oy, oz + z, mat, false);
                    }
                }
            }
        }
    }

    private boolean buildDakhmeh(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();
        // Circular Tower of Silence
        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                if (x*x + z*z <= 64 && x*x + z*z >= 36) {
                    for (int y = 0; y < 6; y++) {
                        setBlock(world, ox + x, oy + y, oz + z, Material.NETHER_BRICK, false);
                    }
                }
            }
        }
        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.DAKHMEH_ZARTOSHTI);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.DAKHMEH_ZARTOSHTI);
        return true;
    }

    private boolean buildAnahitaTemple(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        fillArea(world, ox - 10, oy, oz - 10, ox + 10, oy, oz + 10, Material.SMOOTH_BRICK);
        // Water basins dedicated to Anahita (goddess of waters)
        fillArea(world, ox - 6, oy, oz - 6, ox + 6, oy, oz + 6, Material.WATER);
        for (int x = -8; x <= 8; x += 8) {
            for (int z = -8; z <= 8; z += 8) {
                buildPersianColumn(world, ox + x, oy + 1, oz + z, 6, Material.QUARTZ_BLOCK);
            }
        }
        placeChestWithLoot(world, ox, oy + 1, oz + 8, DungeonType.ANAHITA_TEMPLE);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.ANAHITA_TEMPLE);
        return true;
    }

    private void fillArea(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material mat) {
        fillArea(world, x1, y1, z1, x2, y2, z2, mat, (byte) 0);
    }

    private void fillArea(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material mat, byte data) {
        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = world.getBlockAt(x, y, z);
                    b.setType(mat);
                    if (data != 0) {
                        try { b.setData(data); } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    private void setBlock(World world, int x, int y, int z, Material mat, boolean force) {
        setBlock(world, x, y, z, mat, (byte) 0);
    }

    private void setBlock(World world, int x, int y, int z, Material mat, byte data) {
        Block b = world.getBlockAt(x, y, z);
        b.setType(mat);
        if (data != 0) {
            try { b.setData(data); } catch (Exception ignored) {}
        }
    }

    private void placeChestWithLoot(World world, int x, int y, int z, DungeonType type) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.CHEST);
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();
            chest.getInventory().clear();

            // 1. Guaranteed legendary Persian sword
            if (plugin.getSwordsManager() != null) {
                chest.getInventory().addItem(plugin.getSwordsManager().getRandomSword());
            }
            // 2. Persian traditional food
            if (plugin.getFoodsManager() != null) {
                chest.getInventory().addItem(plugin.getFoodsManager().getRandomFood());
            }
            // 3. Persian historical relic / Derik coin
            chest.getInventory().addItem(PersianItems.getRandomPersianLoot());
            chest.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, random.nextInt(8) + 4));
            chest.update();
        }
    }

    private void spawnGuard(World world, int x, int y, int z, String name, EntityType type, Material weapon, Material helmet) {
        Location loc = new Location(world, x, y, z);
        LivingEntity guard = (LivingEntity) world.spawnEntity(loc, type);
        guard.setCustomName(MessageUtils.color(name));
        guard.setCustomNameVisible(true);
        if (guard.getEquipment() != null) {
            if (weapon != null) guard.getEquipment().setItemInMainHand(new ItemStack(weapon));
            if (helmet != null) guard.getEquipment().setHelmet(new ItemStack(helmet));
        }
    }
}

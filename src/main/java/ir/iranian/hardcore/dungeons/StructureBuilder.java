package ir.iranian.hardcore.dungeons;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.health.PlayerHealthManager;
import ir.iranian.hardcore.items.PersianBossItems;
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
 * Procedural Iranian Dungeon & Architecture Builder (v5.1 Solid Architecture Edition)
 * - Deep foundations to prevent any floating structures on uneven terrain
 * - Interior clearing to prevent tree/dirt clipping
 * - Grand open Persian Iwans (gateways) for easy entrance access
 * - Proper lighting (torches & fire braziers)
 * - Zero unicode corruption, 100% Finglish compliant
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

        // Scan down past leaves, logs, snow, plants to find real solid ground
        while (y > 5) {
            Block b = world.getBlockAt(x, y, z);
            Material m = b.getType();
            if (m != Material.AIR && m != Material.LEAVES && m != Material.LEAVES_2 
                    && m != Material.LOG && m != Material.LOG_2 && m != Material.SNOW
                    && m != Material.LONG_GRASS && m != Material.YELLOW_FLOWER && m != Material.RED_ROSE) {
                break;
            }
            y--;
        }
        if (y < 5) return null;
        return new Location(world, x, y, z);
    }

    // ==========================================
    // 1. TAKHT JAMSHID (Persepolis Apadana Palace)
    // ==========================================
    private boolean buildTakhtJamshid(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 1. Deep solid stone foundation (down 8 blocks)
        buildFoundation(world, ox - 18, oz - 18, ox + 18, oz + 18, oy, Material.SMOOTH_BRICK);

        // 2. Clear interior from oy+1 up to oy+15
        clearArea(world, ox - 18, oy + 1, oz - 18, ox + 18, oy + 15, oz + 18);

        // 3. Grand Apadana Hypostyle Hall: 16 Fluted Quartz Columns
        for (int x = -12; x <= 12; x += 8) {
            for (int z = -12; z <= 12; z += 8) {
                buildPersianColumn(world, ox + x, oy + 1, oz + z, 8, Material.QUARTZ_BLOCK);
            }
        }

        // 4. Grand Palace Roof with Stone Slabs
        fillArea(world, ox - 16, oy + 9, oz - 16, ox + 16, oy + 9, oz + 16, Material.STEP);

        // 5. Open Grand Entrance Gateway on the South side (Stairs + Gateway)
        for (int step = 0; step < 4; step++) {
            fillArea(world, ox - 3, oy - step, oz - 19 - step, ox + 3, oy - step, oz - 19 - step, Material.SMOOTH_BRICK);
        }
        // Gateway entrance torches
        setBlock(world, ox - 4, oy + 2, oz - 16, Material.TORCH, false);
        setBlock(world, ox + 4, oy + 2, oz - 16, Material.TORCH, false);

        // 6. Elevated Royal Throne Platform in center
        fillArea(world, ox - 4, oy + 1, oz - 4, ox + 4, oy + 1, oz + 4, Material.QUARTZ_BLOCK);
        setBlock(world, ox, oy + 2, oz, Material.GOLD_BLOCK, false);
        setBlock(world, ox, oy + 3, oz, Material.RED_SANDSTONE_STAIRS, false);

        // 7. Fire Braziers flanking the throne
        buildFireBrazier(world, ox - 5, oy + 1, oz);
        buildFireBrazier(world, ox + 5, oy + 1, oz);

        // 8. Royal Treasure Vault
        fillArea(world, ox - 2, oy - 2, oz - 2, ox + 2, oy, oz + 2, Material.IRON_FENCE);
        placeChestWithLoot(world, ox - 1, oy + 1, oz, DungeonType.TAKHT_JAMSHID_SKY);
        placeChestWithLoot(world, ox + 1, oy + 1, oz, DungeonType.TAKHT_JAMSHID_SKY);

        // 9. Torches for illumination
        setBlock(world, ox - 12, oy + 4, oz - 12, Material.TORCH, false);
        setBlock(world, ox + 12, oy + 4, oz - 12, Material.TORCH, false);
        setBlock(world, ox - 12, oy + 4, oz + 12, Material.TORCH, false);
        setBlock(world, ox + 12, oy + 4, oz + 12, Material.TORCH, false);

        // 10. Boss & Guards
        spawnGuard(world, ox - 3, oy + 1, oz - 2, "&6Garde Javidan", EntityType.SKELETON, Material.GOLD_SWORD, Material.GOLD_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz - 2, "&6Garde Javidan", EntityType.SKELETON, Material.GOLD_SWORD, Material.GOLD_HELMET);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 2, oz + 3), DungeonType.TAKHT_JAMSHID_SKY);

        return true;
    }

    // ==========================================
    // 2. ALAMUT CASTLE (Assassins Mountain Fortress)
    // ==========================================
    private boolean buildAlamutCastle(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 1. Deep foundation & clearing
        buildFoundation(world, ox - 14, oz - 14, ox + 14, oz + 14, oy, Material.COBBLESTONE);
        clearArea(world, ox - 14, oy + 1, oz - 14, ox + 14, oy + 16, oz + 14);

        // 2. Concentric Defense Walls (24x24)
        buildWalls(world, ox, oy, oz, 12, 8, Material.SMOOTH_BRICK);
        buildCrenellations(world, ox, oy + 8, oz, 12, Material.SMOOTH_BRICK);

        // 3. Wide Open Grand Gatehouse (South Wall: oz = oz - 12)
        fillArea(world, ox - 2, oy + 1, oz - 12, ox + 2, oy + 5, oz - 12, Material.AIR);
        setBlock(world, ox - 3, oy + 3, oz - 13, Material.TORCH, false);
        setBlock(world, ox + 3, oy + 3, oz - 13, Material.TORCH, false);

        // 4. 4 Corner Defensive Watchtowers with battlements
        buildTower(world, ox - 12, oy, oz - 12, 3, 14, Material.SMOOTH_BRICK);
        buildTower(world, ox + 12, oy, oz - 12, 3, 14, Material.SMOOTH_BRICK);
        buildTower(world, ox - 12, oy, oz + 12, 3, 14, Material.SMOOTH_BRICK);
        buildTower(world, ox + 12, oy, oz + 12, 3, 14, Material.SMOOTH_BRICK);

        // 5. Central Citadel & Library of Hassan Sabbah
        fillArea(world, ox - 5, oy + 1, oz - 5, ox + 5, oy + 6, oz + 5, Material.WOOD);
        fillArea(world, ox - 4, oy + 1, oz - 4, ox + 4, oy + 5, oz + 4, Material.AIR);
        // Citadel entrance door
        fillArea(world, ox - 1, oy + 1, oz - 5, ox + 1, oy + 3, oz - 5, Material.AIR);

        // Bookshelves & Alchemy Lab
        for (int x = -3; x <= 3; x += 2) {
            setBlock(world, ox + x, oy + 1, oz - 4, Material.BOOKSHELF, false);
            setBlock(world, ox + x, oy + 2, oz - 4, Material.BOOKSHELF, false);
        }
        setBlock(world, ox, oy + 1, oz - 2, Material.ENCHANTMENT_TABLE, false);
        setBlock(world, ox - 3, oy + 1, oz + 3, Material.BREWING_STAND, false);
        setBlock(world, ox + 3, oy + 1, oz + 3, Material.CAULDRON, false);

        // Torches inside citadel
        setBlock(world, ox - 3, oy + 3, oz, Material.TORCH, false);
        setBlock(world, ox + 3, oy + 3, oz, Material.TORCH, false);

        // 6. Boss & Secret Loot
        placeChestWithLoot(world, ox, oy + 1, oz + 4, DungeonType.ALAMUT_CASTLE);
        spawnGuard(world, ox - 3, oy + 1, oz, "&8Fadaie Alamut", EntityType.ZOMBIE, Material.IRON_SWORD, Material.CHAINMAIL_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz, "&8Fadaie Alamut", EntityType.ZOMBIE, Material.IRON_SWORD, Material.CHAINMAIL_HELMET);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.ALAMUT_CASTLE);

        return true;
    }

    // ==========================================
    // 3. ARGE BAM (Mudbrick Desert Citadel)
    // ==========================================
    private boolean buildArgeBam(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 15, oz - 15, ox + 15, oz + 15, oy, Material.SANDSTONE);
        clearArea(world, ox - 15, oy + 1, oz - 15, ox + 15, oy + 16, oz + 15);

        // Massive Sandstone Citadel Walls
        buildWalls(world, ox, oy, oz, 14, 7, Material.SANDSTONE);
        buildCrenellations(world, ox, oy + 7, oz, 14, Material.SANDSTONE);

        // Grand Arched Entrance Gateway (South)
        fillArea(world, ox - 2, oy + 1, oz - 14, ox + 2, oy + 4, oz - 14, Material.AIR);
        setBlock(world, ox - 3, oy + 3, oz - 15, Material.TORCH, false);
        setBlock(world, ox + 3, oy + 3, oz - 15, Material.TORCH, false);

        // Governor's Palace on elevated floor
        fillArea(world, ox - 5, oy + 1, oz - 5, ox + 5, oy + 7, oz + 5, Material.HARD_CLAY);
        fillArea(world, ox - 4, oy + 1, oz - 4, ox + 4, oy + 6, oz + 4, Material.AIR);
        fillArea(world, ox - 1, oy + 1, oz - 5, ox + 1, oy + 3, oz - 5, Material.AIR);

        placeChestWithLoot(world, ox, oy + 1, oz + 3, DungeonType.ARGE_BAM);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.ARGE_BAM);
        return true;
    }

    // ==========================================
    // 4. CHOGHA ZANBIL (Elamite Stepped Ziggurat)
    // ==========================================
    private boolean buildChoghaZanbil(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 16, oz - 16, ox + 16, oz + 16, oy, Material.CLAY_BRICK);
        clearArea(world, ox - 16, oy + 1, oz - 16, ox + 16, oy + 16, oz + 16);

        // 3-Tiered Terraced Pyramid
        fillArea(world, ox - 15, oy + 1, oz - 15, ox + 15, oy + 3, oz + 15, Material.CLAY_BRICK);
        fillArea(world, ox - 10, oy + 4, oz - 10, ox + 10, oy + 6, oz + 10, Material.HARD_CLAY);
        fillArea(world, ox - 6, oy + 7, oz - 6, ox + 6, oy + 9, oz + 6, Material.SMOOTH_BRICK);

        // Monumental Stairs ascending the south face
        for (int y = 0; y < 9; y++) {
            setBlock(world, ox - 1, oy + y + 1, oz - 15 + y, Material.BRICK_STAIRS, false);
            setBlock(world, ox, oy + y + 1, oz - 15 + y, Material.BRICK_STAIRS, false);
            setBlock(world, ox + 1, oy + y + 1, oz - 15 + y, Material.BRICK_STAIRS, false);
        }

        // Temple of Inshushinak on Top
        fillArea(world, ox - 3, oy + 10, oz - 3, ox + 3, oy + 14, oz + 3, Material.CLAY_BRICK);
        fillArea(world, ox - 2, oy + 10, oz - 2, ox + 2, oy + 13, oz + 2, Material.AIR);
        fillArea(world, ox - 1, oy + 10, oz - 3, ox + 1, oy + 12, oz - 3, Material.AIR); // doorway

        // Altar with Holy Fire
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

        buildFoundation(world, ox - 14, oz - 14, ox + 14, oz + 14, oy, Material.SMOOTH_BRICK);
        clearArea(world, ox - 14, oy + 1, oz - 14, ox + 14, oy + 12, oz + 14);

        // Sacred Spring Pool
        for (int x = -6; x <= 6; x++) {
            for (int z = -6; z <= 6; z++) {
                if (x*x + z*z <= 32) {
                    setBlock(world, ox + x, oy, oz + z, Material.WATER, false);
                }
            }
        }

        // Chahar-Taq Fire Sanctuary (4 Arches)
        int tx = ox, ty = oy, tz = oz + 8;
        fillArea(world, tx - 4, ty + 1, tz - 4, tx + 4, ty + 6, tz + 4, Material.SMOOTH_BRICK);
        fillArea(world, tx - 3, ty + 1, tz - 4, tx + 3, ty + 5, tz + 4, Material.AIR);
        fillArea(world, tx - 4, ty + 1, tz - 3, tx + 4, ty + 5, tz + 3, Material.AIR);

        // Eternal Holy Fire of Azargoshasb
        setBlock(world, tx, ty + 1, tz, Material.GOLD_BLOCK, false);
        setBlock(world, tx, ty + 2, tz, Material.NETHERRACK, false);
        setBlock(world, tx, ty + 3, tz, Material.FIRE, false);

        placeChestWithLoot(world, tx + 2, ty + 1, tz + 2, DungeonType.TAKHT_SOLEYMAN);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, tx, ty + 1, tz - 2), DungeonType.TAKHT_SOLEYMAN);
        return true;
    }

    // ==========================================
    // 6. BANDAR SIRAF (Persian Gulf Historic Port)
    // ==========================================
    private boolean buildBandarSiraf(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 14, oz - 10, ox + 14, oz + 10, oy, Material.SMOOTH_BRICK);
        clearArea(world, ox - 14, oy + 1, oz - 10, ox + 14, oy + 18, oz + 10);

        // Stone Harbor Quay & Promenade
        fillArea(world, ox - 13, oy + 1, oz - 9, ox + 13, oy + 1, oz + 9, Material.SMOOTH_BRICK);

        // High Cylindrical Lighthouse Tower (South-West)
        buildTower(world, ox - 9, oy + 1, oz - 5, 3, 14, Material.SMOOTH_BRICK);
        // Lighthouse top observation deck with Beacon Fire
        fillArea(world, ox - 10, oy + 15, oz - 6, ox - 8, oy + 15, oz - 4, Material.IRON_FENCE);
        setBlock(world, ox - 9, oy + 15, oz - 5, Material.NETHERRACK, false);
        setBlock(world, ox - 9, oy + 16, oz - 5, Material.FIRE, false);
        // Tower entrance
        fillArea(world, ox - 9, oy + 1, oz - 2, ox - 9, oy + 3, oz - 2, Material.AIR);

        // Grand Customs & Maritime Merchant Hall (12x10)
        fillArea(world, ox - 2, oy + 1, oz - 5, ox + 10, oy + 7, oz + 5, Material.SANDSTONE);
        fillArea(world, ox - 1, oy + 1, oz - 4, ox + 9, oy + 6, oz + 4, Material.AIR);
        // Vaulted roof with terracotta slabs
        fillArea(world, ox - 2, oy + 8, oz - 5, ox + 10, oy + 8, oz + 5, Material.STEP);

        // Hall Entrance
        fillArea(world, ox - 2, oy + 1, oz - 1, ox - 2, oy + 4, oz + 1, Material.AIR);

        // Torches & lanterns
        setBlock(world, ox - 1, oy + 3, oz - 3, Material.TORCH, false);
        setBlock(world, ox + 8, oy + 3, oz - 3, Material.TORCH, false);

        // Dual Maritime Treasure Chests
        placeChestWithLoot(world, ox + 4, oy + 1, oz + 3, DungeonType.BANDAR_SIRAF);
        placeChestWithLoot(world, ox + 6, oy + 1, oz + 3, DungeonType.BANDAR_SIRAF);

        spawnGuard(world, ox + 3, oy + 1, oz, "&6Negahban-e Siraf", EntityType.ZOMBIE, Material.IRON_SWORD, Material.CHAINMAIL_HELMET);
        spawnGuard(world, ox - 5, oy + 1, oz, "&6Negahban-e Siraf", EntityType.ZOMBIE, Material.IRON_SWORD, Material.CHAINMAIL_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox + 4, oy + 1, oz), DungeonType.BANDAR_SIRAF);
        return true;
    }

    // ==========================================
    // 7. GHALEH BABAK (Snowy Cliff Fortress)
    // ==========================================
    private boolean buildGhalehBabak(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 14, oz - 14, ox + 14, oz + 14, oy, Material.COBBLESTONE);
        clearArea(world, ox - 14, oy + 1, oz - 14, ox + 14, oy + 18, oz + 14);

        // High Fortress Defense Walls (22x22)
        buildWalls(world, ox, oy, oz, 11, 8, Material.COBBLESTONE);
        buildCrenellations(world, ox, oy + 8, oz, 11, Material.SMOOTH_BRICK);

        // 4 Round Defense Bastions on Corners
        buildTower(world, ox - 11, oy, oz - 11, 3, 12, Material.SMOOTH_BRICK);
        buildTower(world, ox + 11, oy, oz - 11, 3, 12, Material.SMOOTH_BRICK);
        buildTower(world, ox - 11, oy, oz + 11, 3, 12, Material.SMOOTH_BRICK);
        buildTower(world, ox + 11, oy, oz + 11, 3, 12, Material.SMOOTH_BRICK);

        // Arched Gate with Iron Portcullis
        fillArea(world, ox - 2, oy + 1, oz - 11, ox + 2, oy + 5, oz - 11, Material.AIR);
        fillArea(world, ox - 1, oy + 4, oz - 11, ox + 1, oy + 4, oz - 11, Material.IRON_FENCE);

        // Babak's Central Red War Room
        fillArea(world, ox - 5, oy + 1, oz - 5, ox + 5, oy + 6, oz + 5, Material.SMOOTH_BRICK);
        fillArea(world, ox - 4, oy + 1, oz - 4, ox + 4, oy + 5, oz + 4, Material.AIR);
        fillArea(world, ox - 1, oy + 1, oz - 5, ox + 1, oy + 3, oz - 5, Material.AIR); // door

        // Red carpets & Khorramdinan banners
        fillArea(world, ox - 3, oy + 1, oz - 3, ox + 3, oy + 1, oz + 3, Material.CARPET, (byte) 14);

        buildFireBrazier(world, ox - 3, oy + 1, oz + 3);
        buildFireBrazier(world, ox + 3, oy + 1, oz + 3);

        placeChestWithLoot(world, ox - 2, oy + 1, oz + 3, DungeonType.GHALEH_BABAK);
        placeChestWithLoot(world, ox + 2, oy + 1, oz + 3, DungeonType.GHALEH_BABAK);

        spawnGuard(world, ox - 3, oy + 1, oz - 1, "&cDelavar-e Babak", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz - 1, "&cDelavar-e Babak", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.GHALEH_BABAK);
        return true;
    }

    // ==========================================
    // 8. PASARGAD (Cyrus the Great Tomb)
    // ==========================================
    private boolean buildPasargad(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 14, oz - 14, ox + 14, oz + 14, oy, Material.QUARTZ_BLOCK);
        clearArea(world, ox - 14, oy + 1, oz - 14, ox + 14, oy + 18, oz + 14);

        // 6 Monumental Stepped Limestone Plinths (Decreasing dimensions)
        for (int i = 0; i < 6; i++) {
            int r = 11 - i;
            fillArea(world, ox - r, oy + i + 1, oz - r, ox + r, oy + i + 1, oz + r, Material.QUARTZ_BLOCK);
        }

        // Stepped stone plinth border detail with stone slabs
        fillArea(world, ox - 12, oy + 1, oz - 12, ox + 12, oy + 1, oz + 12, Material.STEP);

        // Limestone Burial Chamber on Top (8x8)
        fillArea(world, ox - 4, oy + 7, oz - 4, ox + 4, oy + 12, oz + 4, Material.QUARTZ_BLOCK);
        fillArea(world, ox - 3, oy + 7, oz - 3, ox + 3, oy + 11, oz + 3, Material.AIR);

        // Gabled Pitched Roof with Quartz Stairs
        for (int z = -4; z <= 4; z++) {
            setBlock(world, ox - 4, oy + 12, oz + z, Material.QUARTZ_STAIRS, (byte) 0); // ascending east
            setBlock(world, ox - 3, oy + 13, oz + z, Material.QUARTZ_STAIRS, (byte) 0);
            setBlock(world, ox - 2, oy + 14, oz + z, Material.QUARTZ_BLOCK, false); // ridge
            setBlock(world, ox - 1, oy + 14, oz + z, Material.QUARTZ_BLOCK, false);
            setBlock(world, ox, oy + 14, oz + z, Material.QUARTZ_BLOCK, false);
            setBlock(world, ox + 1, oy + 14, oz + z, Material.QUARTZ_BLOCK, false);
            setBlock(world, ox + 2, oy + 14, oz + z, Material.QUARTZ_BLOCK, false);
            setBlock(world, ox + 3, oy + 13, oz + z, Material.QUARTZ_STAIRS, (byte) 1);
            setBlock(world, ox + 4, oy + 12, oz + z, Material.QUARTZ_STAIRS, (byte) 1);
        }

        // Tomb Entrance Portal
        fillArea(world, ox - 1, oy + 7, oz - 4, ox + 1, oy + 9, oz - 4, Material.AIR);

        // Golden Sarcophagus of Cyrus the Great
        fillArea(world, ox - 1, oy + 7, oz + 1, ox + 1, oy + 7, oz + 2, Material.GOLD_BLOCK);
        setBlock(world, ox, oy + 8, oz + 1, Material.GOLD_BLOCK, false);

        // Flanking Eternal Braziers
        buildFireBrazier(world, ox - 2, oy + 7, oz);
        buildFireBrazier(world, ox + 2, oy + 7, oz);

        // 4 Ancient Ruined Columns in the royal precinct
        buildPersianColumn(world, ox - 12, oy + 1, oz - 12, 6, Material.QUARTZ_BLOCK);
        buildPersianColumn(world, ox + 12, oy + 1, oz - 12, 6, Material.QUARTZ_BLOCK);
        buildPersianColumn(world, ox - 12, oy + 1, oz + 12, 6, Material.QUARTZ_BLOCK);
        buildPersianColumn(world, ox + 12, oy + 1, oz + 12, 6, Material.QUARTZ_BLOCK);

        // Dual Royal Burial Loot Chests
        placeChestWithLoot(world, ox - 2, oy + 7, oz + 2, DungeonType.PASARGAD_TOMB);
        placeChestWithLoot(world, ox + 2, oy + 7, oz + 2, DungeonType.PASARGAD_TOMB);

        spawnGuard(world, ox - 2, oy + 7, oz - 2, "&6Garde Pasargad", EntityType.ZOMBIE, Material.GOLD_SWORD, Material.GOLD_HELMET);
        spawnGuard(world, ox + 2, oy + 7, oz - 2, "&6Garde Pasargad", EntityType.ZOMBIE, Material.GOLD_SWORD, Material.GOLD_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 7, oz), DungeonType.PASARGAD_TOMB);
        return true;
    }

    // ==========================================
    // 9. BISOTUN & NAQSH ROSTAM
    // ==========================================
    private boolean buildBisotun(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 12, oz - 8, ox + 12, oz + 8, oy, Material.STONE);
        clearArea(world, ox - 12, oy + 1, oz - 8, ox + 12, oy + 16, oz + 8);

        // Monumental Mountain Cliff Facade
        fillArea(world, ox - 12, oy + 1, oz - 2, ox + 12, oy + 15, oz + 4, Material.STONE);
        fillArea(world, ox - 8, oy + 4, oz - 2, ox + 8, oy + 12, oz - 1, Material.SMOOTH_BRICK);

        // Darius Cuneiform Relief Wall
        fillArea(world, ox - 5, oy + 5, oz - 1, ox + 5, oy + 10, oz - 1, Material.QUARTZ_BLOCK);
        setBlock(world, ox, oy + 8, oz - 1, Material.GOLD_BLOCK, false); // Royal Faravahar emblem

        // Stone Terrace & Staircase
        for (int s = 0; s < 4; s++) {
            fillArea(world, ox - 4, oy + 1 - s, oz - 7 + s, ox + 4, oy + 1 - s, oz - 7 + s, Material.SMOOTH_STAIRS);
        }

        // Fire Braziers flanking the terrace
        buildFireBrazier(world, ox - 6, oy + 1, oz - 5);
        buildFireBrazier(world, ox + 6, oy + 1, oz - 5);

        // Treasure Vault & Chests
        placeChestWithLoot(world, ox - 3, oy + 1, oz - 4, DungeonType.BISOTUN_INSCRIPTION);
        placeChestWithLoot(world, ox + 3, oy + 1, oz - 4, DungeonType.BISOTUN_INSCRIPTION);

        spawnGuard(world, ox - 4, oy + 1, oz - 3, "&6Garde Bisotun", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);
        spawnGuard(world, ox + 4, oy + 1, oz - 3, "&6Garde Bisotun", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz - 4), DungeonType.BISOTUN_INSCRIPTION);
        return true;
    }

    private boolean buildNaqshRostam(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 10, oz - 6, ox + 10, oz + 8, oy, Material.HARD_CLAY);
        clearArea(world, ox - 10, oy + 1, oz - 6, ox + 10, oy + 18, oz + 8);

        fillArea(world, ox - 10, oy + 1, oz - 2, ox + 10, oy + 18, oz + 4, Material.HARD_CLAY);
        // Cruciform rock-cut tomb facade (Cross shape)
        fillArea(world, ox - 8, oy + 7, oz - 2, ox + 8, oy + 11, oz - 2, Material.RED_SANDSTONE);
        fillArea(world, ox - 2, oy + 2, oz - 2, ox + 2, oy + 16, oz - 2, Material.RED_SANDSTONE);

        // Deep Burial Chamber carved into the mountain rock
        fillArea(world, ox - 4, oy + 2, oz + 2, ox + 4, oy + 6, oz + 8, Material.AIR);
        fillArea(world, ox - 1, oy + 2, oz - 2, ox + 1, oy + 4, oz + 2, Material.AIR); // portal

        // Sarcophagus of Darius the Great
        fillArea(world, ox - 2, oy + 2, oz + 5, ox + 2, oy + 2, oz + 6, Material.QUARTZ_BLOCK);
        setBlock(world, ox, oy + 3, oz + 5, Material.GOLD_BLOCK, false);
        setBlock(world, ox, oy + 3, oz + 6, Material.GOLD_BLOCK, false);

        // Dual Royal Burial Loot Chests
        placeChestWithLoot(world, ox - 3, oy + 2, oz + 6, DungeonType.NAQSH_ROSTAM);
        placeChestWithLoot(world, ox + 3, oy + 2, oz + 6, DungeonType.NAQSH_ROSTAM);

        spawnGuard(world, ox - 2, oy + 2, oz + 3, "&6Negahban-e Gur-e Bastani", EntityType.ZOMBIE, Material.DIAMOND_SWORD, Material.DIAMOND_HELMET);
        spawnGuard(world, ox + 2, oy + 2, oz + 3, "&6Negahban-e Gur-e Bastani", EntityType.ZOMBIE, Material.DIAMOND_SWORD, Material.DIAMOND_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz - 4), DungeonType.NAQSH_ROSTAM);
        return true;
    }

    private boolean buildHegmataneh(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 16, oz - 16, ox + 16, oz + 16, oy, Material.SMOOTH_BRICK);
        clearArea(world, ox - 16, oy + 1, oz - 16, ox + 16, oy + 16, oz + 16);

        // Concentric Mede Citadel Walls
        fillArea(world, ox - 14, oy + 1, oz - 14, ox + 14, oy + 5, oz + 14, Material.CLAY_BRICK);
        fillArea(world, ox - 12, oy + 1, oz - 12, ox + 12, oy + 6, oz + 12, Material.AIR);

        fillArea(world, ox - 10, oy + 1, oz - 10, ox + 10, oy + 8, oz + 10, Material.RED_SANDSTONE);
        fillArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 9, oz + 8, Material.AIR);

        // Central Gold-Crowned Palace
        fillArea(world, ox - 6, oy + 1, oz - 6, ox + 6, oy + 10, oz + 6, Material.QUARTZ_BLOCK);
        fillArea(world, ox - 4, oy + 1, oz - 4, ox + 4, oy + 9, oz + 4, Material.AIR);

        // Gate opening
        fillArea(world, ox - 1, oy + 1, oz - 14, ox + 1, oy + 4, oz - 14, Material.AIR);
        fillArea(world, ox - 1, oy + 1, oz - 10, ox + 1, oy + 4, oz - 10, Material.AIR);
        fillArea(world, ox - 1, oy + 1, oz - 6, ox + 1, oy + 4, oz - 6, Material.AIR);

        // Royal Mede Throne
        setBlock(world, ox, oy + 2, oz + 3, Material.GOLD_BLOCK, false);
        setBlock(world, ox, oy + 3, oz + 3, Material.PURPUR_STAIRS, false);

        placeChestWithLoot(world, ox - 2, oy + 2, oz + 3, DungeonType.HEGMATANEH);
        placeChestWithLoot(world, ox + 2, oy + 2, oz + 3, DungeonType.HEGMATANEH);

        spawnGuard(world, ox - 3, oy + 1, oz, "&6Sarbaz-e Maad", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz, "&6Sarbaz-e Maad", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.HEGMATANEH);
        return true;
    }

    private boolean buildSusaPalace(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 16, oz - 16, ox + 16, oz + 16, oy, Material.SMOOTH_BRICK);
        clearArea(world, ox - 16, oy + 1, oz - 16, ox + 16, oy + 16, oz + 16);

        // Grand Glazed Brick Frieze Hypostyle Hall
        for (int x = -10; x <= 10; x += 5) {
            for (int z = -10; z <= 10; z += 5) {
                buildPersianColumn(world, ox + x, oy + 1, oz + z, 8, Material.QUARTZ_BLOCK);
            }
        }

        // Roof with terracotta tile patterns
        fillArea(world, ox - 14, oy + 9, oz - 14, ox + 14, oy + 9, oz + 14, Material.STEP);

        // Raised Audience Dais
        fillArea(world, ox - 3, oy + 1, oz + 5, ox + 3, oy + 1, oz + 9, Material.GOLD_BLOCK);
        setBlock(world, ox, oy + 2, oz + 7, Material.DIAMOND_BLOCK, false);

        buildFireBrazier(world, ox - 4, oy + 1, oz + 7);
        buildFireBrazier(world, ox + 4, oy + 1, oz + 7);

        placeChestWithLoot(world, ox - 2, oy + 2, oz + 7, DungeonType.SUSA_PALACE);
        placeChestWithLoot(world, ox + 2, oy + 2, oz + 7, DungeonType.SUSA_PALACE);

        spawnGuard(world, ox - 3, oy + 1, oz, "&6Garde Shush", EntityType.ZOMBIE, Material.GOLD_SWORD, Material.GOLD_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz, "&6Garde Shush", EntityType.ZOMBIE, Material.GOLD_SWORD, Material.GOLD_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.SUSA_PALACE);
        return true;
    }

    private boolean buildYazdMosque(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 8, oz - 8, ox + 8, oz + 8, oy, Material.CLAY_BRICK);
        clearArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 22, oz + 8);

        // Grand Arched Portal
        fillArea(world, ox - 6, oy + 1, oz - 4, ox + 6, oy + 10, oz + 4, Material.CLAY_BRICK);
        fillArea(world, ox - 3, oy + 1, oz - 4, ox + 3, oy + 8, oz + 4, Material.AIR);

        // Twin Turquoise Minarets
        buildTower(world, ox - 6, oy + 1, oz - 4, 2, 20, Material.LAPIS_BLOCK);
        buildTower(world, ox + 6, oy + 1, oz - 4, 2, 20, Material.LAPIS_BLOCK);

        placeChestWithLoot(world, ox, oy + 1, oz + 2, DungeonType.YAZD_JAMEH_MOSQUE);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.YAZD_JAMEH_MOSQUE);
        return true;
    }

    private boolean buildAzadiTower(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 8, oz - 4, ox + 8, oz + 4, oy, Material.QUARTZ_BLOCK);
        clearArea(world, ox - 8, oy + 1, oz - 4, ox + 8, oy + 22, oz + 4);

        // Iconic Arch
        fillArea(world, ox - 8, oy + 1, oz - 4, ox - 4, oy + 16, oz + 4, Material.QUARTZ_BLOCK);
        fillArea(world, ox + 4, oy + 1, oz - 4, ox + 8, oy + 16, oz + 4, Material.QUARTZ_BLOCK);
        fillArea(world, ox - 8, oy + 16, oz - 4, ox + 8, oy + 22, oz + 4, Material.QUARTZ_BLOCK);

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.AZADI_TOWER);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.AZADI_TOWER);
        return true;
    }

    private boolean buildVaultedBazaar(Location origin, DungeonType type) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 6, oz - 14, ox + 6, oz + 14, oy, Material.SMOOTH_BRICK);
        clearArea(world, ox - 6, oy + 1, oz - 14, ox + 6, oy + 8, oz + 14);

        for (int z = -14; z <= 14; z += 4) {
            fillArea(world, ox - 5, oy + 1, oz + z, ox + 5, oy + 5, oz + z + 3, Material.SMOOTH_BRICK);
            fillArea(world, ox - 3, oy + 1, oz + z, ox + 3, oy + 4, oz + z + 3, Material.AIR);
            setBlock(world, ox, oy + 5, oz + z + 1, Material.GLASS, false);
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

        buildFoundation(world, ox - 7, oz - 4, ox + 7, oz + 14, oy, Material.HARD_CLAY);
        clearArea(world, ox - 7, oy + 1, oz - 4, ox + 7, oy + 16, oz + 14);

        for (int level = 0; level < 3; level++) {
            int ly = oy + 1 + (level * 4);
            int lz = oz + (level * 6);
            fillArea(world, ox - 6, ly, lz - 3, ox + 6, ly + 3, lz + 3, Material.HARD_CLAY);
            fillArea(world, ox - 5, ly + 1, lz - 2, ox + 5, ly + 3, lz + 2, Material.AIR);
            fillArea(world, ox - 5, ly + 1, lz - 3, ox + 5, ly + 1, lz - 3, Material.FENCE);
            // Door
            fillArea(world, ox - 1, ly + 1, lz - 3, ox + 1, ly + 2, lz - 3, Material.AIR);
        }

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.MASOOLEH);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.MASOOLEH);
        return true;
    }

    private boolean buildKandovanRockHouses(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 10, oz - 6, ox + 10, oz + 12, oy, Material.HARD_CLAY);
        clearArea(world, ox - 10, oy + 1, oz - 6, ox + 10, oy + 12, oz + 12);

        buildVolcanicCone(world, ox - 6, oy, oz);
        buildVolcanicCone(world, ox + 6, oy, oz);
        buildVolcanicCone(world, ox, oy, oz + 7);

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.KANDOVAN);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.KANDOVAN);
        return true;
    }

    private boolean buildShushtarWaterMills(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 12, oz - 6, ox + 12, oz + 6, oy, Material.SMOOTH_BRICK);
        clearArea(world, ox - 12, oy + 1, oz - 6, ox + 12, oy + 10, oz + 6);

        // Stone Canal Retaining Walls
        fillArea(world, ox - 12, oy + 1, oz - 5, ox + 12, oy + 3, oz + 5, Material.SMOOTH_BRICK);

        // Sunken Water Canal with curb
        fillArea(world, ox - 10, oy, oz - 2, ox + 10, oy, oz + 2, Material.WATER);

        // Sassanid Waterwheel (Wood & Fences)
        fillArea(world, ox - 1, oy + 1, oz - 2, ox + 1, oy + 5, oz - 2, Material.WOOD);
        setBlock(world, ox - 1, oy + 2, oz - 1, Material.FENCE, false);
        setBlock(world, ox + 1, oy + 2, oz - 1, Material.FENCE, false);
        setBlock(world, ox, oy + 4, oz - 1, Material.FENCE, false);

        // Stone Millhouse Chamber
        fillArea(world, ox + 4, oy + 1, oz - 4, ox + 10, oy + 5, oz + 4, Material.SMOOTH_BRICK);
        fillArea(world, ox + 5, oy + 1, oz - 3, ox + 9, oy + 4, oz + 3, Material.AIR);
        fillArea(world, ox + 4, oy + 1, oz, ox + 4, oy + 3, oz, Material.AIR); // door

        placeChestWithLoot(world, ox + 7, oy + 1, oz + 2, DungeonType.SHUSHTAR_WATER);
        placeChestWithLoot(world, ox + 7, oy + 1, oz - 2, DungeonType.SHUSHTAR_WATER);

        spawnGuard(world, ox - 4, oy + 1, oz, "&6Negahban-e Asyab", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.SHUSHTAR_WATER);
        return true;
    }

    private boolean buildGonbadKavousTower(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 5, oz - 5, ox + 5, oz + 5, oy, Material.BRICK);
        clearArea(world, ox - 5, oy + 1, oz - 5, ox + 5, oy + 28, oz + 5);

        buildTower(world, ox, oy + 1, oz, 4, 20, Material.BRICK);
        fillArea(world, ox - 1, oy + 1, oz - 4, ox + 1, oy + 3, oz - 4, Material.AIR); // door

        for (int i = 0; i < 6; i++) {
            int r = 4 - (i * 4 / 6);
            fillArea(world, ox - r, oy + 21 + i, oz - r, ox + r, oy + 21 + i, oz + r, Material.BRICK);
        }

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.GONBAD_KAVOUS);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.GONBAD_KAVOUS);
        return true;
    }

    private boolean buildSoltaniyehDome(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 8, oz - 8, ox + 8, oz + 8, oy, Material.BRICK);
        clearArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 18, oz + 8);

        fillArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 10, oz + 8, Material.BRICK);
        fillArea(world, ox - 6, oy + 1, oz - 6, ox + 6, oy + 9, oz + 6, Material.AIR);
        fillArea(world, ox - 2, oy + 1, oz - 8, ox + 2, oy + 4, oz - 8, Material.AIR); // entrance

        // Turquoise Dome
        for (int y = 0; y < 6; y++) {
            int r = 6 - y;
            fillArea(world, ox - r, oy + 11 + y, oz - r, ox + r, oy + 11 + y, oz + r, Material.LAPIS_BLOCK);
        }

        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.SOLTANIYEH);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.SOLTANIYEH);
        return true;
    }

    private boolean buildBaghFinGarden(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 14, oz - 14, ox + 14, oz + 14, oy, Material.GRASS);
        clearArea(world, ox - 14, oy + 1, oz - 14, ox + 14, oy + 12, oz + 14);

        // Grass garden terrace
        fillArea(world, ox - 13, oy + 1, oz - 13, ox + 13, oy + 1, oz + 13, Material.GRASS);

        // Sunken Blue Stone Water Canals (Cruciform Chahar Bagh)
        fillArea(world, ox - 12, oy, oz - 1, ox + 12, oy, oz + 1, Material.WATER);
        fillArea(world, ox - 1, oy, oz - 12, ox + 1, oy, oz + 12, Material.WATER);

        // Canal stone border slabs at oy+1
        for (int i = -12; i <= 12; i++) {
            setBlock(world, ox + i, oy + 1, oz - 2, Material.SMOOTH_BRICK, false);
            setBlock(world, ox + i, oy + 1, oz + 2, Material.SMOOTH_BRICK, false);
            setBlock(world, ox - 2, oy + 1, oz + i, Material.SMOOTH_BRICK, false);
            setBlock(world, ox + 2, oy + 1, oz + i, Material.SMOOTH_BRICK, false);
        }

        // Central Turquoise Pavilion (Koushk-e Safavi)
        fillArea(world, ox - 4, oy + 1, oz - 4, ox + 4, oy + 7, oz + 4, Material.QUARTZ_BLOCK);
        fillArea(world, ox - 3, oy + 1, oz - 3, ox + 3, oy + 6, oz + 3, Material.AIR);
        // 4 Open Arched Iwans
        fillArea(world, ox - 1, oy + 1, oz - 4, ox + 1, oy + 4, oz - 4, Material.AIR);
        fillArea(world, ox - 1, oy + 1, oz + 4, ox + 1, oy + 4, oz + 4, Material.AIR);
        fillArea(world, ox - 4, oy + 1, oz - 1, ox - 4, oy + 4, oz + 1, Material.AIR);
        fillArea(world, ox + 4, oy + 1, oz - 1, ox + 4, oy + 4, oz + 1, Material.AIR);

        // Central Turquoise Fountain Pool
        setBlock(world, ox, oy + 1, oz, Material.LAPIS_BLOCK, false);
        setBlock(world, ox, oy + 2, oz, Material.WATER, false);

        placeChestWithLoot(world, ox - 2, oy + 1, oz + 2, DungeonType.BAGH_FIN);
        placeChestWithLoot(world, ox + 2, oy + 1, oz + 2, DungeonType.BAGH_FIN);

        spawnGuard(world, ox - 2, oy + 1, oz - 2, "&6Garde Bagh-e Fin", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);
        spawnGuard(world, ox + 2, oy + 1, oz - 2, "&6Garde Bagh-e Fin", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.BAGH_FIN);
        return true;
    }

    private boolean buildNasirAlMulk(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 10, oz - 8, ox + 10, oz + 8, oy, Material.CLAY_BRICK);
        clearArea(world, ox - 10, oy + 1, oz - 8, ox + 10, oy + 14, oz + 8);

        // Pink & Glazed Terracotta Walls (18x14)
        fillArea(world, ox - 9, oy + 1, oz - 7, ox + 9, oy + 8, oz + 7, Material.HARD_CLAY);
        fillArea(world, ox - 7, oy + 1, oz - 5, ox + 7, oy + 7, oz + 5, Material.AIR);

        // Stained Glass Windows on the South wall creating rainbow sunlight beams
        for (int x = -6; x <= 6; x += 2) {
            world.getBlockAt(ox + x, oy + 2, oz - 7).setType(Material.STAINED_GLASS);
            world.getBlockAt(ox + x, oy + 2, oz - 7).setData((byte) (Math.abs(x) % 16));
            world.getBlockAt(ox + x, oy + 3, oz - 7).setType(Material.STAINED_GLASS);
            world.getBlockAt(ox + x, oy + 3, oz - 7).setData((byte) ((Math.abs(x) + 4) % 16));
            world.getBlockAt(ox + x, oy + 4, oz - 7).setType(Material.STAINED_GLASS);
            world.getBlockAt(ox + x, oy + 4, oz - 7).setData((byte) ((Math.abs(x) + 8) % 16));
        }

        // 6 Fluted Columns inside the prayer hall
        for (int x = -4; x <= 4; x += 4) {
            buildPersianColumn(world, ox + x, oy + 1, oz, 6, Material.QUARTZ_BLOCK);
        }

        // Persian Red Carpets on the floor
        fillArea(world, ox - 6, oy + 1, oz - 4, ox + 6, oy + 1, oz + 4, Material.CARPET, (byte) 14);

        // Entrance Portal
        fillArea(world, ox - 1, oy + 1, oz + 7, ox + 1, oy + 4, oz + 7, Material.AIR);

        placeChestWithLoot(world, ox - 3, oy + 1, oz + 4, DungeonType.NASIR_MOSQUE);
        placeChestWithLoot(world, ox + 3, oy + 1, oz + 4, DungeonType.NASIR_MOSQUE);

        spawnGuard(world, ox - 3, oy + 1, oz - 2, "&6Negahban-e Masjed", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz - 2, "&6Negahban-e Masjed", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.NASIR_MOSQUE);
        return true;
    }

    private boolean buildVankCathedral(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 10, oz - 10, ox + 10, oz + 10, oy, Material.BRICK);
        clearArea(world, ox - 10, oy + 1, oz - 10, ox + 10, oy + 22, oz + 10);

        // Grand Brick Nave
        fillArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 10, oz + 8, Material.BRICK);
        fillArea(world, ox - 6, oy + 1, oz - 6, ox + 6, oy + 9, oz + 6, Material.AIR);

        // High Central Vaulted Dome
        for (int y = 0; y < 6; y++) {
            int r = 5 - y;
            fillArea(world, ox - r, oy + 10 + y, oz - r, ox + r, oy + 10 + y, oz + r, Material.CLAY_BRICK);
        }

        // Arched Belfry (Bell Tower) with Iron Bell
        fillArea(world, ox - 8, oy + 1, oz - 12, ox - 4, oy + 18, oz - 8, Material.BRICK);
        fillArea(world, ox - 7, oy + 12, oz - 11, ox - 5, oy + 16, oz - 9, Material.AIR); // belfry chamber
        world.getBlockAt(ox - 6, oy + 14, oz - 10).setType(Material.ANVIL); // Cathedral Bell

        // Nave Altar with Gold Cross & Relic Vault
        setBlock(world, ox, oy + 2, oz + 5, Material.GOLD_BLOCK, false);
        setBlock(world, ox, oy + 3, oz + 5, Material.GLOWSTONE, false);

        // Entrance Portal
        fillArea(world, ox - 1, oy + 1, oz - 8, ox + 1, oy + 4, oz - 8, Material.AIR);

        placeChestWithLoot(world, ox - 2, oy + 2, oz + 5, DungeonType.VANK_CATHEDRAL);
        placeChestWithLoot(world, ox + 2, oy + 2, oz + 5, DungeonType.VANK_CATHEDRAL);

        spawnGuard(world, ox - 3, oy + 1, oz, "&6Negahban-e Kelisa", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz, "&6Negahban-e Kelisa", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.VANK_CATHEDRAL);
        return true;
    }

    private boolean buildAliQapuPalace(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 8, oz - 8, ox + 8, oz + 8, oy, Material.SMOOTH_BRICK);
        clearArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 18, oz + 8);

        fillArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 12, oz + 8, Material.SMOOTH_BRICK);
        fillArea(world, ox - 6, oy + 1, oz - 6, ox + 6, oy + 11, oz + 6, Material.AIR);
        fillArea(world, ox - 2, oy + 1, oz - 8, ox + 2, oy + 4, oz - 8, Material.AIR); // gate

        // Terrace with columns on top
        fillArea(world, ox - 7, oy + 12, oz - 7, ox + 7, oy + 12, oz + 7, Material.WOOD);
        for (int x = -5; x <= 5; x += 5) {
            buildPersianColumn(world, ox + x, oy + 13, oz - 6, 4, Material.WOOD);
        }

        placeChestWithLoot(world, ox, oy + 13, oz, DungeonType.ALI_QAPU);
        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 13, oz), DungeonType.ALI_QAPU);
        return true;
    }

    private boolean buildChehelSotun(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 10, oz - 20, ox + 10, oz + 8, oy, Material.SMOOTH_BRICK);
        clearArea(world, ox - 10, oy + 1, oz - 20, ox + 10, oy + 12, oz + 8);

        // Stone Terrace
        fillArea(world, ox - 9, oy + 1, oz - 19, ox + 9, oy + 1, oz + 7, Material.SMOOTH_BRICK);

        // Reflecting Pool with stone curb (16x10) - sunken so water never leaks
        fillArea(world, ox - 6, oy, oz - 18, ox + 6, oy, oz - 6, Material.WATER);
        for (int x = -7; x <= 7; x++) {
            setBlock(world, ox + x, oy + 1, oz - 19, Material.SMOOTH_BRICK, false);
            setBlock(world, ox + x, oy + 1, oz - 5, Material.SMOOTH_BRICK, false);
        }
        for (int z = -19; z <= -5; z++) {
            setBlock(world, ox - 7, oy + 1, oz + z, Material.SMOOTH_BRICK, false);
            setBlock(world, ox + 7, oy + 1, oz + z, Material.SMOOTH_BRICK, false);
        }

        // 20 Slender Wooden Columns facing the reflecting pool
        for (int x = -6; x <= 6; x += 4) {
            for (int z = -4; z <= 2; z += 2) {
                buildPersianColumn(world, ox + x, oy + 1, oz + z, 6, Material.WOOD);
            }
        }

        // Palace Hall (Behind columns)
        fillArea(world, ox - 7, oy + 1, oz + 3, ox + 7, oy + 7, oz + 7, Material.CLAY_BRICK);
        fillArea(world, ox - 5, oy + 1, oz + 4, ox + 5, oy + 6, oz + 6, Material.AIR);
        fillArea(world, ox - 2, oy + 1, oz + 3, ox + 2, oy + 5, oz + 3, Material.AIR); // grand open iwan

        // Persian carpets in the hall
        fillArea(world, ox - 4, oy + 1, oz + 4, ox + 4, oy + 1, oz + 6, Material.CARPET, (byte) 14);

        placeChestWithLoot(world, ox - 3, oy + 1, oz + 5, DungeonType.CHEHEL_SOTUN);
        placeChestWithLoot(world, ox + 3, oy + 1, oz + 5, DungeonType.CHEHEL_SOTUN);

        spawnGuard(world, ox - 3, oy + 1, oz - 1, "&6Garde Chehel Sotun", EntityType.ZOMBIE, Material.GOLD_SWORD, Material.GOLD_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz - 1, "&6Garde Chehel Sotun", EntityType.ZOMBIE, Material.GOLD_SWORD, Material.GOLD_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz + 5), DungeonType.CHEHEL_SOTUN);
        return true;
    }

    private boolean buildSiosepolBridge(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 4, oz - 18, ox + 4, oz + 18, oy, Material.CLAY_BRICK);
        clearArea(world, ox - 4, oy + 1, oz - 18, ox + 4, oy + 10, oz + 18);

        // Lower Arched Piers and Spans
        for (int z = -16; z <= 16; z += 4) {
            // Brick piers
            fillArea(world, ox - 3, oy + 1, oz + z, ox + 3, oy + 4, oz + z + 1, Material.CLAY_BRICK);
            // Open arch between piers
            fillArea(world, ox - 2, oy + 1, oz + z + 2, ox + 2, oy + 3, oz + z + 3, Material.AIR);
        }

        // Upper Promenade Deck (Roadway)
        fillArea(world, ox - 3, oy + 5, oz - 18, ox + 3, oy + 5, oz + 18, Material.SMOOTH_BRICK);

        // Stone Balustrades with Torches along the bridge
        for (int z = -18; z <= 18; z += 2) {
            setBlock(world, ox - 4, oy + 6, oz + z, Material.COBBLE_WALL, false);
            setBlock(world, ox + 4, oy + 6, oz + z, Material.COBBLE_WALL, false);
            if (z % 6 == 0) {
                setBlock(world, ox - 4, oy + 7, oz + z, Material.TORCH, false);
                setBlock(world, ox + 4, oy + 7, oz + z, Material.TORCH, false);
            }
        }

        // Bridgehead Gate Pavilions with Loot
        fillArea(world, ox - 3, oy + 6, oz - 18, ox + 3, oy + 9, oz - 16, Material.CLAY_BRICK);
        fillArea(world, ox - 1, oy + 6, oz - 18, ox + 1, oy + 8, oz - 16, Material.AIR); // arch

        fillArea(world, ox - 3, oy + 6, oz + 16, ox + 3, oy + 9, oz + 18, Material.CLAY_BRICK);
        fillArea(world, ox - 1, oy + 6, oz + 16, ox + 1, oy + 8, oz + 18, Material.AIR); // arch

        placeChestWithLoot(world, ox - 2, oy + 6, oz - 17, DungeonType.SI_O_SE_POL);
        placeChestWithLoot(world, ox + 2, oy + 6, oz + 17, DungeonType.SI_O_SE_POL);

        spawnGuard(world, ox, oy + 6, oz - 10, "&6Garde Pol", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);
        spawnGuard(world, ox, oy + 6, oz + 10, "&6Garde Pol", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 6, oz), DungeonType.SI_O_SE_POL);
        return true;
    }

    // ==========================================
    // ARCHITECTURAL HELPER METHODS
    // ==========================================
    private void buildFoundation(World world, int x1, int z1, int x2, int z2, int groundY, Material mat) {
        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = groundY; y >= groundY - 12; y--) {
                    Block b = world.getBlockAt(x, y, z);
                    Material m = b.getType();
                    if (m == Material.AIR || m == Material.WATER || m == Material.STATIONARY_WATER ||
                        m == Material.LEAVES || m == Material.LEAVES_2 || m == Material.LOG ||
                        m == Material.LOG_2 || m == Material.SNOW || m == Material.LONG_GRASS) {
                        b.setType(mat);
                    }
                }
            }
        }
    }

    private void clearArea(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    world.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }

    private void buildPersianColumn(World world, int x, int y, int z, int height, Material mat) {
        for (int i = 0; i < height; i++) {
            setBlock(world, x, y + i, z, mat, false);
        }
    }

    private void buildFireBrazier(World world, int x, int y, int z) {
        setBlock(world, x, y, z, Material.GOLD_BLOCK, false);
        setBlock(world, x, y + 1, z, Material.NETHERRACK, false);
        setBlock(world, x, y + 2, z, Material.FIRE, false);
    }

    private void buildVolcanicCone(World world, int x, int y, int z) {
        for (int i = 0; i < 8; i++) {
            int r = 5 - (i * 5 / 8);
            fillArea(world, x - r, y + i + 1, z - r, x + r, y + i + 1, z + r, Material.HARD_CLAY);
        }
        fillArea(world, x - 2, y + 1, z - 2, x + 2, y + 5, z + 2, Material.AIR);
        fillArea(world, x - 1, y + 1, z - 5, x + 1, y + 3, z - 2, Material.AIR); // door
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

        buildFoundation(world, ox - 11, oz - 11, ox + 11, oz + 11, oy, Material.NETHER_BRICK);
        clearArea(world, ox - 11, oy + 1, oz - 11, ox + 11, oy + 12, oz + 11);

        // Outer Circular Stone Rampart (Radius 10)
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                int distSq = x*x + z*z;
                if (distSq <= 100 && distSq >= 64) {
                    for (int y = 1; y <= 6; y++) {
                        setBlock(world, ox + x, oy + y, oz + z, Material.NETHER_BRICK, false);
                    }
                    if ((x + z) % 2 == 0) {
                        setBlock(world, ox + x, oy + 7, oz + z, Material.NETHER_BRICK, false); // crenellations
                    }
                } else if (distSq < 64) {
                    setBlock(world, ox + x, oy + 1, oz + z, Material.SMOOTH_BRICK, false); // stone paved floor
                }
            }
        }

        // Arched Gate Entrance (South)
        fillArea(world, ox - 2, oy + 1, oz - 10, ox + 2, oy + 4, oz - 8, Material.AIR);
        fillArea(world, ox - 1, oy + 3, oz - 10, ox + 1, oy + 3, oz - 10, Material.IRON_FENCE);

        // Central Ossuary Pit (Astodan)
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (x*x + z*z <= 9) {
                    setBlock(world, ox + x, oy + 1, oz + z, Material.BONE_BLOCK, false);
                }
            }
        }
        buildFireBrazier(world, ox - 3, oy + 1, oz);
        buildFireBrazier(world, ox + 3, oy + 1, oz);

        // Dual Ancient Relic Chests
        placeChestWithLoot(world, ox - 2, oy + 1, oz + 4, DungeonType.DAKHMEH_ZARTOSHTI);
        placeChestWithLoot(world, ox + 2, oy + 1, oz + 4, DungeonType.DAKHMEH_ZARTOSHTI);

        spawnGuard(world, ox - 3, oy + 1, oz - 2, "&8Moqan-e Dakhmeh", EntityType.ZOMBIE, Material.IRON_SWORD, Material.CHAINMAIL_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz - 2, "&8Moqan-e Dakhmeh", EntityType.ZOMBIE, Material.IRON_SWORD, Material.CHAINMAIL_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz), DungeonType.DAKHMEH_ZARTOSHTI);
        return true;
    }

    private boolean buildAnahitaTemple(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        buildFoundation(world, ox - 12, oz - 12, ox + 12, oz + 12, oy, Material.SMOOTH_BRICK);
        clearArea(world, ox - 12, oy + 1, oz - 12, ox + 12, oy + 14, oz + 12);

        // Stone Temple Platform
        fillArea(world, ox - 11, oy + 1, oz - 11, ox + 11, oy + 1, oz + 11, Material.SMOOTH_BRICK);

        // Sunken Sacred Water Reservoir with perimeter stone curb to prevent water leakage
        fillArea(world, ox - 6, oy, oz - 6, ox + 6, oy, oz + 6, Material.WATER);
        // Stone curb border at oy+1
        for (int x = -7; x <= 7; x++) {
            setBlock(world, ox + x, oy + 1, oz - 7, Material.SMOOTH_BRICK, false);
            setBlock(world, ox + x, oy + 1, oz + 7, Material.SMOOTH_BRICK, false);
            setBlock(world, ox - 7, oy + 1, oz + x, Material.SMOOTH_BRICK, false);
            setBlock(world, ox + 7, oy + 1, oz + x, Material.SMOOTH_BRICK, false);
        }

        // 8 Monumental Colonnade Columns (Kangavar Style)
        for (int x = -9; x <= 9; x += 6) {
            buildPersianColumn(world, ox + x, oy + 1, oz - 9, 8, Material.QUARTZ_BLOCK);
            buildPersianColumn(world, ox + x, oy + 1, oz + 9, 8, Material.QUARTZ_BLOCK);
        }

        // Central Altar of Water & Purity
        setBlock(world, ox, oy + 1, oz, Material.LAPIS_BLOCK, false);
        setBlock(world, ox, oy + 2, oz, Material.SEA_LANTERN, false);

        // Dual Ancient Relic Chests
        placeChestWithLoot(world, ox - 2, oy + 1, oz + 8, DungeonType.ANAHITA_TEMPLE);
        placeChestWithLoot(world, ox + 2, oy + 1, oz + 8, DungeonType.ANAHITA_TEMPLE);

        spawnGuard(world, ox - 3, oy + 1, oz, "&bNegahban-e Ab-haye Anahita", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);
        spawnGuard(world, ox + 3, oy + 1, oz, "&bNegahban-e Ab-haye Anahita", EntityType.ZOMBIE, Material.IRON_SWORD, Material.IRON_HELMET);

        plugin.getBossFightManager().spawnDungeonBoss(new Location(world, ox, oy + 1, oz - 3), DungeonType.ANAHITA_TEMPLE);
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

            // 1. Guaranteed Persian Sword
            if (plugin.getSwordsManager() != null) {
                chest.getInventory().addItem(plugin.getSwordsManager().getRandomSword());
            }
            // 2. Guaranteed Persian Food
            if (plugin.getFoodsManager() != null) {
                chest.getInventory().addItem(plugin.getFoodsManager().getRandomFood());
            }
            // 3. Historic Relic Loot & Sekkeh Derik
            chest.getInventory().addItem(PersianItems.getRandomPersianLoot());
            chest.getInventory().addItem(PersianItems.createSekkeHakhamaneshi());
            chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, random.nextInt(6) + 3));

            // 4. 40% Chance for Permanent Extra Heart Canister!
            if (random.nextDouble() < 0.40) {
                if (random.nextDouble() < 0.20) {
                    chest.getInventory().addItem(PlayerHealthManager.createElixirOfLife());
                } else if (random.nextBoolean()) {
                    chest.getInventory().addItem(PlayerHealthManager.createRedHeartCanister());
                } else {
                    chest.getInventory().addItem(PlayerHealthManager.createTurquoiseHeartCanister());
                }
            }

            // 5. 35% Chance for Dedicated Boss Slayer Weapons / Gear!
            if (random.nextDouble() < 0.35) {
                int r = random.nextInt(4);
                if (r == 0) chest.getInventory().addItem(PersianBossItems.createDivKoshSword());
                else if (r == 1) chest.getInventory().addItem(PersianBossItems.createBossPiercerBow());
                else if (r == 2) chest.getInventory().addItem(PersianBossItems.createNaphthaBomb(3));
                else chest.getInventory().addItem(PersianBossItems.createJamshidTalisman());
            }

            // 6. Gems
            chest.getInventory().addItem(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
            chest.getInventory().addItem(new ItemStack(Material.EMERALD, random.nextInt(4) + 2));

            chest.update();
        }
    }

    private void spawnGuard(World world, int x, int y, int z, String name, EntityType type, Material weapon, Material helmet) {
        if (type == EntityType.SKELETON && weapon != null && !weapon.name().contains("BOW")) {
            type = EntityType.ZOMBIE; // 1.12.2 skeleton melee AI bug fix
        }
        Location loc = new Location(world, x + 0.5, y, z + 0.5);
        LivingEntity guard = (LivingEntity) world.spawnEntity(loc, type);
        guard.setCustomName(MessageUtils.color(name));
        guard.setCustomNameVisible(true);
        guard.setRemoveWhenFarAway(false);
        if (guard.getEquipment() != null) {
            if (weapon != null) {
                guard.getEquipment().setItemInMainHand(new ItemStack(weapon));
                guard.getEquipment().setItemInMainHandDropChance(0.15f);
            }
            if (helmet != null) {
                guard.getEquipment().setHelmet(new ItemStack(helmet));
                guard.getEquipment().setHelmetDropChance(0.10f);
            }
        }
    }
}

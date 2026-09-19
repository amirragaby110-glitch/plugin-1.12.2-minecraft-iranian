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
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Iran Iran‌Iran Iran - Iran NMSIran Iran Iran Bukkit API
 * Iran Iran Iran Iran‌Iran Iran Iran Iran‌Iran
 */
public class StructureBuilder {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public StructureBuilder(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Iran Iran Iran Iran Iran - Iran 3.0 Iran 16 Iran
     */
    public boolean buildDungeon(Location origin, DungeonType type) {
        origin = findGround(origin);
        if (origin == null) return false;

        plugin.getLogger().info("Sakht-e dungeon-e Irani " + type.getPersianName() + " Iran " + origin.getBlockX() + "," + origin.getBlockY() + "," + origin.getBlockZ() + " - Iran: " + origin.getBlock().getBiome());

        switch (type) {
            case ALAMUT_CASTLE:
                return buildAlamutCastle(origin);
            case ANAHITA_TEMPLE:
                return buildAnahitaTemple(origin);
            case ARGE_BAM:
                return buildArgeBam(origin);
            case BANDAR_SIRAF:
                return buildBandarSiraf(origin);
            case GHALEH_BABAK:
                return buildGhalehBabak(origin);
            case CHOGHA_ZANBIL:
                return buildChoghaZanbil(origin);
            case DAKHMEH_ZARTOSHTI:
                return buildDakhmeh(origin);
            case TAKHT_JAMSHID_SKY:
                return buildTakhtJamshid(origin);
            // Iran‌Iran Iran 3.0
            case PASARGAD_TOMB:
                return buildPasargad(origin);
            case BISOTUN_INSCRIPTION:
                return buildBisotun(origin);
            case NAQSH_ROSTAM:
                return buildNaqshRostam(origin);
            case TAKHT_SOLEYMAN:
                return buildTakhtSoleyman(origin);
            case HEGMATANEH:
                return buildHegmataneh(origin);
            case SUSA_PALACE:
                return buildSusaPalace(origin);
            case YAZD_JAMEH_MOSQUE:
                return buildYazdMosque(origin);
            case AZADI_TOWER:
                return buildAzadiTower(origin);
            default:
                return buildGenericPersianDungeon(origin, type);
        }
    }

    private boolean buildGenericPersianDungeon(Location origin, DungeonType type) {
        // Iran Iran‌Iran Iran Iran Iran Iran Iran Iran Iran Iran Iran Iran Iran Iran Iran Iran
        plugin.getLogger().info("Sakht-e dungeon-e omoomi baraye " + type.getPersianName());
        return buildArgeBam(origin);
    }

    private Location findGround(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int y = world.getHighestBlockYAt(x, z);
        if (y < 5) return null;
        // Iran Iran Iran Iran Iran Iran‌Iran
        if (world.getEnvironment() == World.Environment.NETHER) {
            y = 60;
        } else if (world.getEnvironment() == World.Environment.THE_END) {
            y = 70;
        }
        return new Location(world, x, y, z);
    }

    // ==================== Iran Iran ====================
    private boolean buildAlamutCastle(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran Iran‌Iran
        fillArea(world, ox - 12, oy - 5, oz - 12, ox + 12, oy, oz + 12, Material.STONE);

        // DivarIran Iran 20x20
        buildWalls(world, ox, oy, oz, 10, 8, Material.COBBLESTONE);
        buildWalls(world, ox, oy, oz, 9, 8, Material.AIR); // Dakhel khali

        // Borj‌Iran Iran
        buildTower(world, ox - 10, oy, oz - 10, 6, 12, Material.SMOOTH_BRICK);
        buildTower(world, ox + 10, oy, oz - 10, 6, 12, Material.SMOOTH_BRICK);
        buildTower(world, ox - 10, oy, oz + 10, 6, 12, Material.SMOOTH_BRICK);
        buildTower(world, ox + 10, oy, oz + 10, 6, 12, Material.SMOOTH_BRICK);

        // Borj Iran (Iran Iran)
        buildTower(world, ox, oy, oz, 5, 18, Material.SMOOTH_BRICK);
        // Iran Iran Iran Iran Iran
        fillArea(world, ox - 2, oy + 15, oz - 2, ox + 2, oy + 17, oz + 2, Material.WOOD);
        setBlock(world, ox, oy + 16, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 16, oz, DungeonType.ALAMUT_CASTLE);

        // Darvazeh
        fillArea(world, ox - 1, oy + 1, oz - 10, ox + 1, oy + 3, oz - 10, Material.AIR);
        setBlock(world, ox, oy + 1, oz - 10, Material.IRON_FENCE, false);

        // Iran
        world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.ZOMBIE).setCustomName(MessageUtils.color("&8Iran Iran"));
        world.spawnEntity(new Location(world, ox, oy + 16, oz + 1), EntityType.WITCH).setCustomName(MessageUtils.color("&4&lIran Iran - Iran Iran"));

        // Iran Iran
        setBlock(world, ox, oy + 2, oz + 9, Material.SIGN_POST, false);

        return true;
    }

    // ==================== Iran Iran ====================
    private boolean buildAnahitaTemple(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran Iran 30x15
        fillArea(world, ox - 15, oy, oz - 7, ox + 15, oy, oz + 7, Material.SMOOTH_BRICK);
        fillArea(world, ox - 14, oy, oz - 6, ox + 14, oy, oz + 6, Material.WATER);

        // Sotoon‌Iran (32 Sotoon Iran Iran)
        for (int x = -14; x <= 14; x += 4) {
            for (int z = -6; z <= 6; z += 12) {
                buildColumn(world, ox + x, oy + 1, oz + z, 8, Material.SMOOTH_BRICK);
            }
        }
        for (int z = -4; z <= 4; z += 4) {
            for (int x = -14; x <= 14; x += 28) {
                buildColumn(world, ox + x, oy + 1, oz + z, 8, Material.SMOOTH_BRICK);
            }
        }

        // Saghf
        fillArea(world, ox - 15, oy + 9, oz - 7, ox + 15, oy + 9, oz + 7, Material.SMOOTH_BRICK);

        // Iran Iran Iran
        fillArea(world, ox - 3, oy + 1, oz - 2, ox + 3, oy + 1, oz + 2, Material.WATER);
        fillArea(world, ox - 1, oy + 1, oz - 1, ox + 1, oy + 1, oz + 1, Material.GOLD_BLOCK);

        // Iran Iran (Iran)
        buildColumn(world, ox, oy + 1, oz, 3, Material.QUARTZ_BLOCK);
        setBlock(world, ox, oy + 4, oz, Material.DIAMOND_BLOCK, false);

        // Iran
        setBlock(world, ox + 10, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox + 10, oy + 1, oz, DungeonType.ANAHITA_TEMPLE);

        // Iran
        world.spawnEntity(new Location(world, ox, oy + 1, oz + 3), EntityType.EVOKER).setCustomName(MessageUtils.color("&b&lIran Iran Iran"));

        return true;
    }

    // ==================== Iran Iran ====================
    private boolean buildArgeBam(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Divar Iran Iran 35x35
        buildWalls(world, ox, oy, oz, 17, 10, Material.SANDSTONE);
        // Iran Iran
        buildWalls(world, ox, oy, oz, 16, 9, Material.AIR);
        // Iran Iran Iran

        // Iran‌Iran Iran Iran
        for (int i = 0; i < 5; i++) {
            int hx = ox - 12 + random.nextInt(24);
            int hz = oz - 12 + random.nextInt(24);
            buildSmallHouse(world, hx, oy, hz, Material.SANDSTONE);
        }

        // Borj Iran Iran
        buildTower(world, ox - 17, oy, oz - 17, 5, 12, Material.SANDSTONE);
        buildTower(world, ox + 17, oy, oz - 17, 5, 12, Material.SANDSTONE);
        buildTower(world, ox - 17, oy, oz + 17, 5, 12, Material.SANDSTONE);
        buildTower(world, ox + 17, oy, oz + 17, 5, 12, Material.SANDSTONE);

        // Iran Iran Iran Iran
        buildWalls(world, ox, oy, oz, 6, 8, Material.SANDSTONE);
        fillArea(world, ox - 5, oy + 8, oz - 5, ox + 5, oy + 8, oz + 5, Material.SANDSTONE);
        setBlock(world, ox, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.ARGE_BAM);

        // BazaarIran Iran Iran
        setBlock(world, ox + 8, oy + 1, oz + 8, Material.CHEST, true);
        placeChestWithLoot(world, ox + 8, oy + 1, oz + 8, DungeonType.ARGE_BAM);

        world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.VILLAGER).setCustomName(MessageUtils.color("&6&lIran Iran Iran"));
        world.spawnEntity(new Location(world, ox + 2, oy + 1, oz), EntityType.IRON_GOLEM).setCustomName(MessageUtils.color("&eIran Iran"));

        return true;
    }

    // ==================== Iran Iran ====================
    private boolean buildBandarSiraf(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran Iran
        fillArea(world, ox - 10, oy, oz - 5, ox + 10, oy, oz + 5, Material.WOOD);
        fillArea(world, ox - 12, oy - 2, oz - 6, ox + 12, oy - 1, oz + 6, Material.WOOD);

        // Iran Iran
        fillArea(world, ox - 14, oy - 1, oz - 8, ox + 14, oy - 1, oz + 8, Material.WATER);

        // Iran
        buildSmallHouse(world, ox - 8, oy, oz, Material.WOOD);
        setBlock(world, ox - 8, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox - 8, oy + 1, oz, DungeonType.BANDAR_SIRAF);

        buildSmallHouse(world, ox + 8, oy, oz, Material.WOOD);
        setBlock(world, ox + 8, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox + 8, oy + 1, oz, DungeonType.BANDAR_SIRAF);

        // Iran Iran
        buildTower(world, ox, oy, oz + 12, 3, 15, Material.SANDSTONE);
        setBlock(world, ox, oy + 15, oz + 12, Material.GLOWSTONE, false);
        setBlock(world, ox, oy + 16, oz + 12, Material.FIRE, false);

        // Iran Iran Iran
        fillArea(world, ox + 15, oy - 3, oz, ox + 20, oy - 1, oz + 3, Material.WOOD);
        setBlock(world, ox + 18, oy - 2, oz + 1, Material.CHEST, true);
        placeChestWithLoot(world, ox + 18, oy - 2, oz + 1, DungeonType.BANDAR_SIRAF);

        world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.SKELETON).setCustomName(MessageUtils.color("&b&lIran Iran - Iran Iran"));

        return true;
    }

    // ==================== Iran Iran ====================
    private boolean buildGhalehBabak(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran Iran
        fillArea(world, ox - 11, oy - 3, oz - 11, ox + 11, oy, oz + 11, Material.STONE);
        fillArea(world, ox - 11, oy, oz - 11, ox + 11, oy, oz + 11, Material.SNOW_BLOCK);

        // Divar Iran
        buildWalls(world, ox, oy, oz, 10, 7, Material.COBBLESTONE);
        buildWalls(world, ox, oy, oz, 9, 7, Material.AIR);

        // Borj‌Iran
        buildTower(world, ox - 10, oy, oz - 10, 4, 10, Material.SMOOTH_BRICK);
        buildTower(world, ox + 10, oy, oz - 10, 4, 10, Material.SMOOTH_BRICK);

        // Iran Iran (Iran Iran Iran)
        fillArea(world, ox - 2, oy + 1, oz - 2, ox + 2, oy + 1, oz + 2, Material.NETHERRACK);
        setBlock(world, ox, oy + 2, oz, Material.FIRE, false);

        // Iran Iran Iran
        buildColumn(world, ox + 5, oy + 1, oz + 5, 8, Material.FENCE);
        setBlock(world, ox + 5, oy + 9, oz + 5, Material.WOOL, false, (byte) 14); // Ghermez

        // Iran Iran
        setBlock(world, ox, oy + 1, oz + 2, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 1, oz + 2, DungeonType.GHALEH_BABAK);

        world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.ZOMBIE).setCustomName(MessageUtils.color("&c&lIran Iran - Iran Iran"));
        ((org.bukkit.entity.Zombie) world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.ZOMBIE)).setBaby(false);

        return true;
    }

    // ==================== Iran Iran ====================
    private boolean buildChoghaZanbil(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran 5 Iran
        // Iran 1: 28x28 Iran 5
        fillArea(world, ox - 14, oy, oz - 14, ox + 14, oy + 4, oz + 14, Material.BRICK);
        // Iran 2: 22x22 Iran 5
        fillArea(world, ox - 11, oy + 5, oz - 11, ox + 11, oy + 9, oz + 11, Material.BRICK);
        // Iran 3: 16x16 Iran 5
        fillArea(world, ox - 8, oy + 10, oz - 8, ox + 8, oy + 14, oz + 8, Material.SANDSTONE);
        // Iran 4: 10x10 Iran 5
        fillArea(world, ox - 5, oy + 15, oz - 5, ox + 5, oy + 19, oz + 5, Material.SANDSTONE);
        // Iran 5: Iran 6x6
        fillArea(world, ox - 3, oy + 20, oz - 3, ox + 3, oy + 24, oz + 3, Material.GOLD_BLOCK);
        fillArea(world, ox - 2, oy + 21, oz - 2, ox + 2, oy + 23, oz + 2, Material.AIR);

        // Iran‌Iran
        for (int y = 0; y < 20; y++) {
            setBlock(world, ox + 14, oy + y, oz, Material.BRICK_STAIRS, false);
        }

        // Iran Iran Iran Iran
        setBlock(world, ox, oy + 21, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 21, oz, DungeonType.CHOGHA_ZANBIL);

        // Iran Iran (Iran)
        fillArea(world, ox - 16, oy - 1, oz - 16, ox + 16, oy - 1, oz + 16, Material.WATER);
        fillArea(world, ox - 14, oy, oz - 14, ox + 14, oy, oz + 14, Material.BRICK); // Iran Iran

        world.spawnEntity(new Location(world, ox, oy + 21, oz + 1), EntityType.SKELETON).setCustomName(MessageUtils.color("&6&lIran Iran - Iran Iran"));

        return true;
    }

    // ==================== Iran Iran (Iran) ====================
    private boolean buildDakhmeh(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Borj Iran‌Iran 20 Iran
        buildCircle(world, ox, oy, oz, 10, 6, Material.NETHER_BRICK);
        buildCircle(world, ox, oy, oz, 9, 6, Material.AIR);

        // Kaf Iran
        fillArea(world, ox - 8, oy, oz - 8, ox + 8, oy, oz + 8, Material.SOUL_SAND);
        // Iran‌Iran
        for (int i = 0; i < 10; i++) {
            int bx = ox - 7 + random.nextInt(14);
            int bz = oz - 7 + random.nextInt(14);
            setBlock(world, bx, oy + 1, bz, Material.BONE_BLOCK, false);
        }

        // Iran Iran
        fillArea(world, ox - 2, oy + 1, oz - 2, ox + 2, oy + 1, oz + 2, Material.NETHER_BRICK);
        setBlock(world, ox, oy + 2, oz, Material.FIRE, false);
        setBlock(world, ox, oy + 3, oz, Material.FIRE, false);

        // Divar Iran
        buildCircle(world, ox, oy + 1, oz, 3, 4, Material.NETHER_FENCE);

        // Iran
        setBlock(world, ox + 5, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox + 5, oy + 1, oz, DungeonType.DAKHMEH_ZARTOSHTI);

        world.spawnEntity(new Location(world, ox, oy + 1, oz + 2), EntityType.BLAZE).setCustomName(MessageUtils.color("&6&lIran Iran - Iran Iran"));

        return true;
    }

    // ==================== Iran Iran Iran (Iran) ====================
    private boolean buildTakhtJamshid(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran Iran 40x40
        fillArea(world, ox - 20, oy, oz - 20, ox + 20, oy + 2, oz + 20, Material.SANDSTONE);
        fillArea(world, ox - 19, oy + 3, oz - 19, ox + 19, oy + 3, oz + 19, Material.GOLD_BLOCK);

        // 100 SotoonIran Iran 16 Sotoon Iran‌Iran Iran Iran (4x4)
        for (int x = -15; x <= 15; x += 10) {
            for (int z = -15; z <= 15; z += 10) {
                buildColumn(world, ox + x, oy + 4, oz + z, 20, Material.QUARTZ_BLOCK);
                // IranSotoon Iran Iran Iran Iran (Iran)
                setBlock(world, ox + x, oy + 24, oz + z, Material.GOLD_BLOCK, false);
            }
        }

        // Saghf Iran
        fillArea(world, ox - 20, oy + 25, oz - 20, ox + 20, oy + 26, oz + 20, Material.SANDSTONE);

        // Iran Iran
        buildThrone(world, ox, oy + 4, oz + 15);

        // Darvazeh Iran
        buildGate(world, ox, oy + 4, oz - 20);

        // Iran‌Iran Iran Iran
        setBlock(world, ox - 10, oy + 4, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox - 10, oy + 4, oz, DungeonType.TAKHT_JAMSHID_SKY);

        setBlock(world, ox + 10, oy + 4, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox + 10, oy + 4, oz, DungeonType.TAKHT_JAMSHID_SKY);

        setBlock(world, ox, oy + 4, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 4, oz, DungeonType.TAKHT_JAMSHID_SKY);

        // Iran Iran: Iran Iran
        org.bukkit.entity.Enderman boss = (org.bukkit.entity.Enderman) world.spawnEntity(new Location(world, ox, oy + 4, oz + 15), EntityType.ENDERMAN);
        boss.setCustomName(MessageUtils.color("&6&l👑 Iran Iran - Iran Iran Iran"));
        boss.setCustomNameVisible(true);
        boss.setMaxHealth(200);
        boss.setHealth(200);

        return true;
    }

    // ==================== Iran‌Iran Iran Iran 3.0 ====================

    private boolean buildPasargad(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran Iran - 6 Iran
        for (int i = 0; i < 6; i++) {
            int size = 10 - i;
            fillArea(world, ox - size, oy + i, oz - size, ox + size, oy + i, oz + size, Material.SANDSTONE);
        }
        // Iran Iran
        fillArea(world, ox - 3, oy + 6, oz - 4, ox + 3, oy + 10, oz + 4, Material.SANDSTONE);
        fillArea(world, ox - 2, oy + 7, oz - 3, ox + 2, oy + 9, oz + 3, Material.AIR);
        setBlock(world, ox, oy + 7, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 7, oz, DungeonType.PASARGAD_TOMB);

        world.spawnEntity(new Location(world, ox, oy + 7, oz + 2), EntityType.SKELETON).setCustomName(MessageUtils.color("&f&lIran Iran - Iran Iran"));

        // Iran Iran - Iran Iran Iran
        fillArea(world, ox - 15, oy, oz - 15, ox + 15, oy, oz + 15, Material.GRASS);
        fillArea(world, ox - 1, oy, oz - 15, ox + 1, oy, oz + 15, Material.WATER);
        fillArea(world, ox - 15, oy, oz - 1, ox + 15, oy, oz + 1, Material.WATER);

        return true;
    }

    private boolean buildBisotun(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Divar Iran IranSotoon 30x20
        fillArea(world, ox - 15, oy, oz, ox + 15, oy + 20, oz, Material.SMOOTH_BRICK);
        // Iran
        fillArea(world, ox - 10, oy + 5, oz, ox + 10, oy + 15, oz, Material.SMOOTH_BRICK);
        // Iran Iran Iran Iran
        for (int x = -8; x <= 8; x += 2) {
            setBlock(world, ox + x, oy + 10, oz + 1, Material.GOLD_BLOCK, false);
        }

        setBlock(world, ox, oy + 6, oz + 1, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 6, oz + 1, DungeonType.BISOTUN_INSCRIPTION);

        world.spawnEntity(new Location(world, ox, oy + 6, oz + 2), EntityType.VILLAGER).setCustomName(MessageUtils.color("&7&lIran IranSotoon - Iran Iran"));

        return true;
    }

    private boolean buildNaqshRostam(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran Iran Iran
        fillArea(world, ox - 17, oy, oz, ox + 17, oy + 25, oz + 7, Material.SANDSTONE);
        // 4 Iran Iran
        for (int x = -12; x <= 12; x += 8) {
            fillArea(world, ox + x - 2, oy + 10, oz, ox + x + 2, oy + 15, oz, Material.AIR);
            fillArea(world, ox + x - 2, oy + 15, oz, ox + x + 2, oy + 16, oz, Material.SANDSTONE);
            setBlock(world, ox + x, oy + 11, oz + 1, Material.CHEST, true);
            placeChestWithLoot(world, ox + x, oy + 11, oz + 1, DungeonType.NAQSH_ROSTAM);
        }

        // Iran Iran
        buildWalls(world, ox, oy, oz + 12, 3, 8, Material.SANDSTONE);
        fillArea(world, ox - 3, oy + 8, oz + 9, ox + 3, oy + 8, oz + 15, Material.SANDSTONE);

        world.spawnEntity(new Location(world, ox, oy + 11, oz + 2), EntityType.SKELETON).setCustomName(MessageUtils.color("&6&lIran - Iran Iran"));

        return true;
    }

    private boolean buildTakhtSoleyman(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Divar Iran Iran 30x30
        buildWalls(world, ox, oy, oz, 15, 8, Material.SMOOTH_BRICK);
        // Iran Iran Iran
        fillArea(world, ox - 8, oy, oz - 8, ox + 8, oy, oz + 8, Material.WATER);
        fillArea(world, ox - 6, oy, oz - 6, ox + 6, oy, oz + 6, Material.ICE);
        // Iran
        buildWalls(world, ox, oy, oz, 4, 6, Material.NETHER_BRICK);
        setBlock(world, ox, oy + 2, oz, Material.FIRE, false);

        setBlock(world, ox + 10, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox + 10, oy + 1, oz, DungeonType.TAKHT_SOLEYMAN);

        world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.BLAZE).setCustomName(MessageUtils.color("&b&lIran Iran Iran"));

        return true;
    }

    private boolean buildHegmataneh(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran - 7 Divar Iran
        for (int r = 16; r >= 4; r -= 2) {
            Material mat = Material.WOOL;
            byte data = (byte) (r % 16);
            buildCircle(world, ox, oy + (16 - r), oz, r, 3, mat);
        }

        // Iran Iran
        buildWalls(world, ox, oy, oz, 4, 6, Material.SMOOTH_BRICK);
        setBlock(world, ox, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.HEGMATANEH);

        world.spawnEntity(new Location(world, ox, oy + 1, oz + 1), EntityType.ZOMBIE).setCustomName(MessageUtils.color("&2&lIran - Iran‌Iran Iran"));

        return true;
    }

    private boolean buildSusaPalace(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Iran Iran 28x28
        fillArea(world, ox - 14, oy, oz - 14, ox + 14, oy, oz + 14, Material.BRICK);
        // Sotoon‌Iran
        for (int x = -10; x <= 10; x += 5) {
            for (int z = -10; z <= 10; z += 5) {
                buildColumn(world, ox + x, oy + 1, oz + z, 10, Material.SMOOTH_BRICK);
                setBlock(world, ox + x, oy + 11, oz + z, Material.GOLD_BLOCK, false);
            }
        }
        // Saghf
        fillArea(world, ox - 14, oy + 12, oz - 14, ox + 14, oy + 12, oz + 14, Material.BRICK);

        // Iran Iran
        setBlock(world, ox - 14, oy + 1, oz, Material.SANDSTONE, false);
        setBlock(world, ox + 14, oy + 1, oz, Material.SANDSTONE, false);

        setBlock(world, ox, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.SUSA_PALACE);

        world.spawnEntity(new Location(world, ox, oy + 1, oz + 2), EntityType.IRON_GOLEM).setCustomName(MessageUtils.color("&e&lIran Iran"));

        return true;
    }

    private boolean buildYazdMosque(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Masjed Iran Iran 25x30
        buildWalls(world, ox, oy, oz, 12, 8, Material.SANDSTONE);
        fillArea(world, ox - 12, oy + 8, oz - 15, ox + 12, oy + 8, oz + 15, Material.SANDSTONE);

        // Iran Iran 52 Iran (Iran 20 Iran)
        buildTower(world, ox - 12, oy, oz - 15, 2, 20, Material.SANDSTONE);
        buildTower(world, ox + 12, oy, oz - 15, 2, 20, Material.SANDSTONE);

        // Iran Firoozeh-ei
        buildDome(world, ox, oy + 9, oz, 8, Material.WOOL, (byte) 9); // Firoozeh-ei

        // Iran
        fillArea(world, ox - 2, oy + 1, oz + 12, ox + 2, oy + 4, oz + 15, Material.GOLD_BLOCK);

        setBlock(world, ox, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.YAZD_JAMEH_MOSQUE);

        return true;
    }

    private boolean buildAzadiTower(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Borj Iran - 4 Iran
        buildColumn(world, ox - 6, oy, oz - 6, 15, Material.QUARTZ_BLOCK);
        buildColumn(world, ox + 6, oy, oz - 6, 15, Material.QUARTZ_BLOCK);
        buildColumn(world, ox - 6, oy, oz + 6, 15, Material.QUARTZ_BLOCK);
        buildColumn(world, ox + 6, oy, oz + 6, 15, Material.QUARTZ_BLOCK);

        // Iran
        fillArea(world, ox - 6, oy + 15, oz - 6, ox + 6, oy + 18, oz + 6, Material.QUARTZ_BLOCK);
        fillArea(world, ox - 3, oy + 15, oz - 3, ox + 3, oy + 18, oz + 3, Material.AIR);

        // Borj Iran
        buildWalls(world, ox, oy + 18, oz, 3, 10, Material.QUARTZ_BLOCK);

        // Iran Iran
        buildColumn(world, ox, oy + 28, oz, 5, Material.FENCE);
        fillArea(world, ox, oy + 33, oz, ox, oy + 35, oz, Material.WOOL, (byte) 14); // Ghermez
        fillArea(world, ox, oy + 32, oz, ox, oy + 32, oz, Material.WOOL); // Sefid
        fillArea(world, ox, oy + 31, oz, ox, oy + 31, oz, Material.WOOL, (byte) 5); // Sabz

        setBlock(world, ox, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.AZADI_TOWER);

        return true;
    }

    // Iran Iran Iran Iran
    private void buildDome(World world, int ox, int oy, int oz, int radius, Material mat, byte data) {
        for (int y = 0; y < radius; y++) {
            int r = radius - y;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    if (x * x + z * z <= r * r && x * x + z * z >= (r - 1) * (r - 1)) {
                        Block b = world.getBlockAt(ox + x, oy + y, oz + z);
                        b.setType(mat);
                        try { b.setData(data); } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    // ==================== Iran Iran Iran ====================

    private void fillArea(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material mat, byte data) {
        fillArea(world, x1, y1, z1, x2, y2, z2, mat);
        // For colored wool, set data via setBlock
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setData(data);
                }
            }
        }
    }

    private void fillArea(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material mat) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() != mat) {
                        block.setType(mat);
                    }
                }
            }
        }
    }

    private void setBlock(World world, int x, int y, int z, Material mat, boolean force) {
        setBlock(world, x, y, z, mat, force, (byte) 0);
    }

    private void setBlock(World world, int x, int y, int z, Material mat, boolean force, byte data) {
        Block block = world.getBlockAt(x, y, z);
        if (force || block.getType() == Material.AIR || block.getType().isSolid() == false) {
            block.setType(mat);
            if (data != 0) {
                try {
                    block.setData(data);
                } catch (Exception ignored) {}
            }
        } else if (mat == Material.CHEST || mat == Material.AIR || mat == Material.FIRE || mat == Material.GLOWSTONE) {
            block.setType(mat);
        }
    }

    private void buildWalls(World world, int ox, int oy, int oz, int radius, int height, Material mat) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius || Math.abs(z) == radius) {
                        world.getBlockAt(ox + x, oy + y, oz + z).setType(mat);
                    }
                }
            }
        }
    }

    private void buildTower(World world, int ox, int oy, int oz, int radius, int height, Material mat) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius || Math.abs(z) == radius || y == 0 || y == height - 1) {
                        if (x * x + z * z <= radius * radius + 2) {
                            world.getBlockAt(ox + x, oy + y, oz + z).setType(mat);
                        }
                    }
                }
            }
        }
        // Dakhel khali
        for (int x = -radius + 1; x < radius; x++) {
            for (int y = 1; y < height - 1; y++) {
                for (int z = -radius + 1; z < radius; z++) {
                    if (x * x + z * z < (radius - 1) * (radius - 1)) {
                        world.getBlockAt(ox + x, oy + y, oz + z).setType(Material.AIR);
                    }
                }
            }
        }
    }

    private void buildColumn(World world, int x, int y, int z, int height, Material mat) {
        for (int i = 0; i < height; i++) {
            world.getBlockAt(x, y + i, z).setType(mat);
        }
    }

    private void buildSmallHouse(World world, int ox, int oy, int oz, Material mat) {
        buildWalls(world, ox, oy, oz, 3, 4, mat);
        fillArea(world, ox - 3, oy + 4, oz - 3, ox + 3, oy + 4, oz + 3, mat);
        fillArea(world, ox - 2, oy + 1, oz - 2, ox + 2, oy + 3, oz + 2, Material.AIR);
        setBlock(world, ox, oy + 1, oz - 3, Material.WOODEN_DOOR, false);
    }

    private void buildCircle(World world, int ox, int oy, int oz, int radius, int height, Material mat) {
        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double dist = Math.sqrt(x * x + z * z);
                    if (dist >= radius - 0.5 && dist <= radius + 0.5) {
                        world.getBlockAt(ox + x, oy + y, oz + z).setType(mat);
                    }
                }
            }
        }
    }

    private void buildThrone(World world, int ox, int oy, int oz) {
        fillArea(world, ox - 2, oy, oz - 1, ox + 2, oy + 3, oz + 1, Material.GOLD_BLOCK);
        fillArea(world, ox - 1, oy + 1, oz, ox + 1, oy + 2, oz, Material.AIR);
        setBlock(world, ox, oy + 1, oz, Material.GOLD_BLOCK, false);
    }

    private void buildGate(World world, int ox, int oy, int oz) {
        fillArea(world, ox - 3, oy, oz, ox + 3, oy + 6, oz, Material.SANDSTONE);
        fillArea(world, ox - 1, oy + 1, oz, ox + 1, oy + 4, oz, Material.AIR);
        // Iran Iran
        setBlock(world, ox - 3, oy + 1, oz + 1, Material.SANDSTONE, false);
        setBlock(world, ox + 3, oy + 1, oz + 1, Material.SANDSTONE, false);
    }

    private void placeChestWithLoot(World world, int x, int y, int z, DungeonType type) {
        Block block = world.getBlockAt(x, y, z);
        if (block.getType() != Material.CHEST) {
            block.setType(Material.CHEST);
        }
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();
            chest.getInventory().clear();

            // Loot-e makhsoos Iran Iran - Iran 3.0 Iran 16 Iran
            switch (type) {
                case ALAMUT_CASTLE:
                    chest.getInventory().addItem(PersianItems.createShamshirAlamut());
                    chest.getInventory().addItem(new ItemStack(Material.BOOK, 5));
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 10));
                    break;
                case ANAHITA_TEMPLE:
                    chest.getInventory().addItem(PersianItems.createMorvaridKhalij());
                    chest.getInventory().addItem(new ItemStack(Material.DIAMOND, 3));
                    chest.getInventory().addItem(PersianItems.createMorghAmin());
                    break;
                case ARGE_BAM:
                    chest.getInventory().addItem(PersianItems.createFarshKermani());
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, 2));
                    chest.getInventory().addItem(PersianItems.createSekkeHakhamaneshi());
                    break;
                case BANDAR_SIRAF:
                    chest.getInventory().addItem(PersianItems.createMorvaridKhalij());
                    chest.getInventory().addItem(new ItemStack(Material.EMERALD, 10));
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 15));
                    break;
                case GHALEH_BABAK:
                    chest.getInventory().addItem(PersianItems.createShamshirBabak());
                    chest.getInventory().addItem(new ItemStack(Material.WOOL, 5, (short) 14));
                    chest.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                    break;
                case CHOGHA_ZANBIL:
                    chest.getInventory().addItem(PersianItems.createLohIlami());
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 8));
                    chest.getInventory().addItem(PersianItems.createSekkeHakhamaneshi());
                    break;
                case DAKHMEH_ZARTOSHTI:
                    chest.getInventory().addItem(PersianItems.createAtashMoghadas());
                    chest.getInventory().addItem(new ItemStack(Material.BONE, 20));
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, 1));
                    break;
                case TAKHT_JAMSHID_SKY:
                    chest.getInventory().addItem(PersianItems.createTajKourosh());
                    chest.getInventory().addItem(PersianItems.createManshurKourosh());
                    chest.getInventory().addItem(PersianItems.createShamshirHakhamaneshi());
                    chest.getInventory().addItem(PersianItems.createKamanArash());
                    chest.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, 3));
                    break;
                case PASARGAD_TOMB:
                    chest.getInventory().addItem(PersianItems.createTajKourosh());
                    chest.getInventory().addItem(PersianItems.createManshurKourosh());
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, 2));
                    break;
                case BISOTUN_INSCRIPTION:
                    chest.getInventory().addItem(PersianItems.createLohIlami());
                    chest.getInventory().addItem(new ItemStack(Material.BOOK, 10));
                    chest.getInventory().addItem(PersianItems.createSekkeHakhamaneshi());
                    break;
                case NAQSH_ROSTAM:
                    chest.getInventory().addItem(PersianItems.createTajKourosh());
                    chest.getInventory().addItem(PersianItems.createShamshirHakhamaneshi());
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 20));
                    break;
                case TAKHT_SOLEYMAN:
                    chest.getInventory().addItem(PersianItems.createAtashMoghadas());
                    chest.getInventory().addItem(PersianItems.createMorvaridKhalij());
                    chest.getInventory().addItem(new ItemStack(Material.DIAMOND, 5));
                    break;
                case HEGMATANEH:
                    chest.getInventory().addItem(PersianItems.createShamshirHakhamaneshi());
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 15));
                    chest.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 20));
                    break;
                case SUSA_PALACE:
                    chest.getInventory().addItem(PersianItems.createLohIlami());
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, 1));
                    chest.getInventory().addItem(PersianItems.createFarshKermani());
                    break;
                case YAZD_JAMEH_MOSQUE:
                    chest.getInventory().addItem(PersianItems.createFarshKermani());
                    chest.getInventory().addItem(new ItemStack(Material.EMERALD, 15));
                    chest.getInventory().addItem(new ItemStack(Material.CARPET, 10, (short) 9));
                    break;
                case AZADI_TOWER:
                    chest.getInventory().addItem(PersianItems.createSekkeHakhamaneshi());
                    chest.getInventory().addItem(new ItemStack(Material.WOOL, 3, (short) 14));
                    chest.getInventory().addItem(new ItemStack(Material.WOOL, 3, (short) 0));
                    chest.getInventory().addItem(new ItemStack(Material.WOOL, 3, (short) 5));
                    break;
                default:
                    chest.getInventory().addItem(PersianItems.getRandomPersianLoot());
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 5));
                    break;
            }

            // Loot-e tasadofi Iran - 3 Iran Iran
            for (int i = 0; i < 3; i++) {
                chest.getInventory().addItem(PersianItems.getRandomPersianLoot());
            }

            chest.update();
        }
    }
}

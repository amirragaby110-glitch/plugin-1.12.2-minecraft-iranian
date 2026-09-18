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
 * سازنده سازه‌های ایرانی - بدون NMS، فقط با Bukkit API
 * هر دانجن با بلوک‌گذاری مستقیم ساخته می‌شود
 */
public class StructureBuilder {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public StructureBuilder(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * ساخت دانجن بر اساس نوع
     */
    public boolean buildDungeon(Location origin, DungeonType type) {
        origin = findGround(origin);
        if (origin == null) return false;

        plugin.getLogger().info("ساخت دانجن " + type.getPersianName() + " در " + origin.getBlockX() + "," + origin.getBlockY() + "," + origin.getBlockZ());

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
            default:
                return false;
        }
    }

    private Location findGround(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int y = world.getHighestBlockYAt(x, z);
        if (y < 5) return null;
        // برای ندر و اند فرق می‌کند
        if (world.getEnvironment() == World.Environment.NETHER) {
            y = 60;
        } else if (world.getEnvironment() == World.Environment.THE_END) {
            y = 70;
        }
        return new Location(world, x, y, z);
    }

    // ==================== قلعه الموت ====================
    private boolean buildAlamutCastle(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // پایه صخره‌ای
        fillArea(world, ox - 12, oy - 5, oz - 12, ox + 12, oy, oz + 12, Material.STONE);

        // دیوارهای قلعه 20x20
        buildWalls(world, ox, oy, oz, 10, 8, Material.COBBLESTONE);
        buildWalls(world, ox, oy, oz, 9, 8, Material.AIR); // داخل خالی

        // برج‌های گوشه
        buildTower(world, ox - 10, oy, oz - 10, 6, 12, Material.STONE_BRICK);
        buildTower(world, ox + 10, oy, oz - 10, 6, 12, Material.STONE_BRICK);
        buildTower(world, ox - 10, oy, oz + 10, 6, 12, Material.STONE_BRICK);
        buildTower(world, ox + 10, oy, oz + 10, 6, 12, Material.STONE_BRICK);

        // برج مرکزی (آشیانه عقاب)
        buildTower(world, ox, oy, oz, 5, 18, Material.SMOOTH_BRICK);
        // اتاق حسن صباح در بالا
        fillArea(world, ox - 2, oy + 15, oz - 2, ox + 2, oy + 17, oz + 2, Material.WOOD);
        setBlock(world, ox, oy + 16, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 16, oz, DungeonType.ALAMUT_CASTLE);

        // دروازه
        fillArea(world, ox - 1, oy + 1, oz - 10, ox + 1, oy + 3, oz - 10, Material.AIR);
        setBlock(world, ox, oy + 1, oz - 10, Material.IRON_FENCE, false);

        // اسپاونرها
        world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.ZOMBIE).setCustomName(MessageUtils.color("&8سرباز اسماعیلی"));
        world.spawnEntity(new Location(world, ox, oy + 16, oz + 1), EntityType.WITCH).setCustomName(MessageUtils.color("&4&lحسن صباح - پیر کوهستان"));

        // تابلو فارسی
        setBlock(world, ox, oy + 2, oz + 9, Material.SIGN_POST, false);

        return true;
    }

    // ==================== معبد آناهیتا ====================
    private boolean buildAnahitaTemple(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // پایه معبد 30x15
        fillArea(world, ox - 15, oy, oz - 7, ox + 15, oy, oz + 7, Material.SMOOTH_BRICK);
        fillArea(world, ox - 14, oy, oz - 6, ox + 14, oy, oz + 6, Material.WATER);

        // ستون‌ها (32 ستون مثل کنگاور)
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

        // سقف
        fillArea(world, ox - 15, oy + 9, oz - 7, ox + 15, oy + 9, oz + 7, Material.SMOOTH_BRICK);

        // حوض مرکزی آناهیتا
        fillArea(world, ox - 3, oy + 1, oz - 2, ox + 3, oy + 1, oz + 2, Material.WATER);
        fillArea(world, ox - 1, oy + 1, oz - 1, ox + 1, oy + 1, oz + 1, Material.GOLD_BLOCK);

        // مجسمه آناهیتا (ساده)
        buildColumn(world, ox, oy + 1, oz, 3, Material.QUARTZ_BLOCK);
        setBlock(world, ox, oy + 4, oz, Material.DIAMOND_BLOCK, false);

        // چست
        setBlock(world, ox + 10, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox + 10, oy + 1, oz, DungeonType.ANAHITA_TEMPLE);

        // باس
        world.spawnEntity(new Location(world, ox, oy + 1, oz + 3), EntityType.EVOKER).setCustomName(MessageUtils.color("&b&lکاهن اعظم آناهیتا"));

        return true;
    }

    // ==================== ارگ بم ====================
    private boolean buildArgeBam(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // دیوار عظیم خشتی 35x35
        buildWalls(world, ox, oy, oz, 17, 10, Material.SANDSTONE);
        // لایه دوم
        buildWalls(world, ox, oy, oz, 16, 9, Material.AIR);
        // خندق؟ نه، کویر

        // خانه‌های داخل ارگ
        for (int i = 0; i < 5; i++) {
            int hx = ox - 12 + random.nextInt(24);
            int hz = oz - 12 + random.nextInt(24);
            buildSmallHouse(world, hx, oy, hz, Material.SANDSTONE);
        }

        // برج و بارو
        buildTower(world, ox - 17, oy, oz - 17, 5, 12, Material.SANDSTONE);
        buildTower(world, ox + 17, oy, oz - 17, 5, 12, Material.SANDSTONE);
        buildTower(world, ox - 17, oy, oz + 17, 5, 12, Material.SANDSTONE);
        buildTower(world, ox + 17, oy, oz + 17, 5, 12, Material.SANDSTONE);

        // قلعه حاکم در مرکز
        buildWalls(world, ox, oy, oz, 6, 8, Material.SANDSTONE);
        fillArea(world, ox - 5, oy + 8, oz - 5, ox + 5, oy + 8, oz + 5, Material.SANDSTONE);
        setBlock(world, ox, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 1, oz, DungeonType.ARGE_BAM);

        // بازارچه داخل ارگ
        setBlock(world, ox + 8, oy + 1, oz + 8, Material.CHEST, true);
        placeChestWithLoot(world, ox + 8, oy + 1, oz + 8, DungeonType.ARGE_BAM);

        world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.VILLAGER).setCustomName(MessageUtils.color("&6&lحاکم ارگ بم"));
        world.spawnEntity(new Location(world, ox + 2, oy + 1, oz), EntityType.IRON_GOLEM).setCustomName(MessageUtils.color("&eنگهبان بم"));

        return true;
    }

    // ==================== بندر سیراف ====================
    private boolean buildBandarSiraf(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // اسکله چوبی
        fillArea(world, ox - 10, oy, oz - 5, ox + 10, oy, oz + 5, Material.WOOD);
        fillArea(world, ox - 12, oy - 2, oz - 6, ox + 12, oy - 1, oz + 6, Material.WOOD);

        // آب اطراف
        fillArea(world, ox - 14, oy - 1, oz - 8, ox + 14, oy - 1, oz + 8, Material.WATER);

        // انبارها
        buildSmallHouse(world, ox - 8, oy, oz, Material.WOOD);
        setBlock(world, ox - 8, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox - 8, oy + 1, oz, DungeonType.BANDAR_SIRAF);

        buildSmallHouse(world, ox + 8, oy, oz, Material.WOOD);
        setBlock(world, ox + 8, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox + 8, oy + 1, oz, DungeonType.BANDAR_SIRAF);

        // فانوس دریایی
        buildTower(world, ox, oy, oz + 12, 3, 15, Material.SANDSTONE);
        setBlock(world, ox, oy + 15, oz + 12, Material.GLOWSTONE, false);
        setBlock(world, ox, oy + 16, oz + 12, Material.FIRE, false);

        // کشتی غرق شده
        fillArea(world, ox + 15, oy - 3, oz, ox + 20, oy - 1, oz + 3, Material.WOOD);
        setBlock(world, ox + 18, oy - 2, oz + 1, Material.CHEST, true);
        placeChestWithLoot(world, ox + 18, oy - 2, oz + 1, DungeonType.BANDAR_SIRAF);

        world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.SKELETON).setCustomName(MessageUtils.color("&b&lناخدای سیراف - دزد دریایی"));

        return true;
    }

    // ==================== قلعه بابک ====================
    private boolean buildGhalehBabak(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // پایه برفی
        fillArea(world, ox - 11, oy - 3, oz - 11, ox + 11, oy, oz + 11, Material.STONE);
        fillArea(world, ox - 11, oy, oz - 11, ox + 11, oy, oz + 11, Material.SNOW_BLOCK);

        // دیوار قلعه
        buildWalls(world, ox, oy, oz, 10, 7, Material.COBBLESTONE);
        buildWalls(world, ox, oy, oz, 9, 7, Material.AIR);

        // برج‌ها
        buildTower(world, ox - 10, oy, oz - 10, 4, 10, Material.SMOOTH_BRICK);
        buildTower(world, ox + 10, oy, oz - 10, 4, 10, Material.SMOOTH_BRICK);

        // آتشکده داخل (گرما در سرما)
        fillArea(world, ox - 2, oy + 1, oz - 2, ox + 2, oy + 1, oz + 2, Material.NETHERRACK);
        setBlock(world, ox, oy + 2, oz, Material.FIRE, false);

        // پرچم سرخ خرمدینان
        buildColumn(world, ox + 5, oy + 1, oz + 5, 8, Material.FENCE);
        setBlock(world, ox + 5, oy + 9, oz + 5, Material.WOOL, false, (byte) 14); // قرمز

        // چست بابک
        setBlock(world, ox, oy + 1, oz + 2, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 1, oz + 2, DungeonType.GHALEH_BABAK);

        world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.ZOMBIE).setCustomName(MessageUtils.color("&c&lبابک خرمدین - شیر آذربایجان"));
        ((org.bukkit.entity.Zombie) world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.ZOMBIE)).setBaby(false);

        return true;
    }

    // ==================== زیگورات چغازنبیل ====================
    private boolean buildChoghaZanbil(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // زیگورات 5 طبقه
        // طبقه 1: 28x28 ارتفاع 5
        fillArea(world, ox - 14, oy, oz - 14, ox + 14, oy + 4, oz + 14, Material.BRICK);
        // طبقه 2: 22x22 ارتفاع 5
        fillArea(world, ox - 11, oy + 5, oz - 11, ox + 11, oy + 9, oz + 11, Material.BRICK);
        // طبقه 3: 16x16 ارتفاع 5
        fillArea(world, ox - 8, oy + 10, oz - 8, ox + 8, oy + 14, oz + 8, Material.SANDSTONE);
        // طبقه 4: 10x10 ارتفاع 5
        fillArea(world, ox - 5, oy + 15, oz - 5, ox + 5, oy + 19, oz + 5, Material.SANDSTONE);
        // طبقه 5: معبد 6x6
        fillArea(world, ox - 3, oy + 20, oz - 3, ox + 3, oy + 24, oz + 3, Material.GOLD_BLOCK);
        fillArea(world, ox - 2, oy + 21, oz - 2, ox + 2, oy + 23, oz + 2, Material.AIR);

        // پله‌ها
        for (int y = 0; y < 20; y++) {
            setBlock(world, ox + 14, oy + y, oz, Material.BRICK_STAIRS, false);
        }

        // چست در معبد بالا
        setBlock(world, ox, oy + 21, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 21, oz, DungeonType.CHOGHA_ZANBIL);

        // آب اطراف (مرداب)
        fillArea(world, ox - 16, oy - 1, oz - 16, ox + 16, oy - 1, oz + 16, Material.WATER);
        fillArea(world, ox - 14, oy, oz - 14, ox + 14, oy, oz + 14, Material.BRICK); // حفظ زیگورات

        world.spawnEntity(new Location(world, ox, oy + 21, oz + 1), EntityType.SKELETON).setCustomName(MessageUtils.color("&6&lکاهن ایلامی - نگهبان اینشوشیناک"));

        return true;
    }

    // ==================== دخمه زرتشتی (ندر) ====================
    private boolean buildDakhmeh(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // برج دایره‌ای 20 قطر
        buildCircle(world, ox, oy, oz, 10, 6, Material.NETHER_BRICK);
        buildCircle(world, ox, oy, oz, 9, 6, Material.AIR);

        // کف استخوان
        fillArea(world, ox - 8, oy, oz - 8, ox + 8, oy, oz + 8, Material.SOUL_SAND);
        // استخوان‌ها
        for (int i = 0; i < 10; i++) {
            int bx = ox - 7 + random.nextInt(14);
            int bz = oz - 7 + random.nextInt(14);
            setBlock(world, bx, oy + 1, bz, Material.BONE_BLOCK, false);
        }

        // آتشکده مرکزی
        fillArea(world, ox - 2, oy + 1, oz - 2, ox + 2, oy + 1, oz + 2, Material.NETHER_BRICK);
        setBlock(world, ox, oy + 2, oz, Material.FIRE, false);
        setBlock(world, ox, oy + 3, oz, Material.FIRE, false);

        // دیوار آتش
        buildCircle(world, ox, oy + 1, oz, 3, 4, Material.NETHER_FENCE);

        // چست
        setBlock(world, ox + 5, oy + 1, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox + 5, oy + 1, oz, DungeonType.DAKHMEH_ZARTOSHTI);

        world.spawnEntity(new Location(world, ox, oy + 1, oz + 2), EntityType.BLAZE).setCustomName(MessageUtils.color("&6&lموبد اعظم - نگهبان آتش"));

        return true;
    }

    // ==================== تخت جمشید آسمانی (اند) ====================
    private boolean buildTakhtJamshid(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // پایه عظیم 40x40
        fillArea(world, ox - 20, oy, oz - 20, ox + 20, oy + 2, oz + 20, Material.SANDSTONE);
        fillArea(world, ox - 19, oy + 3, oz - 19, ox + 19, oy + 3, oz + 19, Material.GOLD_BLOCK);

        // 100 ستون؟ ما 16 ستون می‌سازیم برای سادگی (4x4)
        for (int x = -15; x <= 15; x += 10) {
            for (int z = -15; z <= 15; z += 10) {
                buildColumn(world, ox + x, oy + 4, oz + z, 20, Material.QUARTZ_BLOCK);
                // سرستون گاو دو سر هخامنشی (ساده)
                setBlock(world, ox + x, oy + 24, oz + z, Material.GOLD_BLOCK, false);
            }
        }

        // سقف آپادانا
        fillArea(world, ox - 20, oy + 25, oz - 20, ox + 20, oy + 26, oz + 20, Material.SANDSTONE);

        // تخت شاهی
        buildThrone(world, ox, oy + 4, oz + 15);

        // دروازه ملل
        buildGate(world, ox, oy + 4, oz - 20);

        // چست‌های گنج هخامنشی
        setBlock(world, ox - 10, oy + 4, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox - 10, oy + 4, oz, DungeonType.TAKHT_JAMSHID_SKY);

        setBlock(world, ox + 10, oy + 4, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox + 10, oy + 4, oz, DungeonType.TAKHT_JAMSHID_SKY);

        setBlock(world, ox, oy + 4, oz, Material.CHEST, true);
        placeChestWithLoot(world, ox, oy + 4, oz, DungeonType.TAKHT_JAMSHID_SKY);

        // باس نهایی: داریوش بزرگ
        org.bukkit.entity.Enderman boss = (org.bukkit.entity.Enderman) world.spawnEntity(new Location(world, ox, oy + 4, oz + 15), EntityType.ENDERMAN);
        boss.setCustomName(MessageUtils.color("&6&l👑 داریوش بزرگ - شاه شاهان هخامنشی"));
        boss.setCustomNameVisible(true);
        boss.setMaxHealth(200);
        boss.setHealth(200);

        return true;
    }

    // ==================== متدهای کمکی ساخت ====================

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
        // داخل خالی
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
        // گاو بالدار
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

            // لوت مخصوص هر دانجن
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
            }

            // لوت تصادفی اضافی
            for (int i = 0; i < 3; i++) {
                chest.getInventory().addItem(PersianItems.getRandomPersianLoot());
            }

            chest.update();
        }
    }
}

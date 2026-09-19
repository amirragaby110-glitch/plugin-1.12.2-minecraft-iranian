package ir.iranian.hardcore.structures;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.items.PersianItems;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Sazehaye Irani rooye map - v5.0 Finglish
 * - Bazar Irani
 * - Karvansaraye Shah Abbasi
 * - Chaykhane sonati
 * - Ab Anbare Yazdi
 */
public class PersianStructures {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public PersianStructures(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    public enum StructureType {
        BAZAAR("Bazar Irani", "Persian Bazaar", Material.EMERALD_BLOCK, "Bazare sonati ba hojrehaye Irani"),
        CARAVANSERAI("Karvansaraye Shah Abbasi", "Shah Abbasi Caravanserai", Material.SANDSTONE, "Esterahatgahe jaddaye Abrisham"),
        CHAIKHANEH("Chaykhane Sonati", "Traditional Teahouse", Material.WOOD, "Chaykhane ba samavar"),
        AB_ANBAR("Ab Anbare Yazdi", "Yazd Ab Anbar", Material.SMOOTH_BRICK, "Ab Anbare khonake kaviri ba badgir"),
        MOSQUE("Masjed Roostayi", "Rural Mosque", Material.LAPIS_BLOCK, "Masjede sonati"),
        VILLAGE("Roostaye Irani", "Iranian Village", Material.COBBLESTONE, "Roostaye sonatie Kandovan");

        private final String persianName;
        private final String englishName;
        private final Material icon;
        private final String desc;

        StructureType(String persianName, String englishName, Material icon, String desc) {
            this.persianName = persianName;
            this.englishName = englishName;
            this.icon = icon;
            this.desc = desc;
        }

        public String getPersianName() { return persianName; }
        public String getEnglishName() { return englishName; }
        public Material getIcon() { return icon; }
        public String getDesc() { return desc; }
    }

    public boolean forceBuildStructure(Location origin, StructureType type) {
        switch (type) {
            case BAZAAR: return buildBazaar(origin);
            case CARAVANSERAI: return buildCaravanserai(origin);
            case CHAIKHANEH: return buildChaikhaneh(origin);
            case AB_ANBAR: return buildAbAnbar(origin);
            case VILLAGE:
                if (plugin.getVillageManager() != null) {
                    plugin.getVillageManager().generateVillage(origin);
                    return true;
                }
                return buildBazaar(origin);
            case MOSQUE:
            default:
                return buildBazaar(origin);
        }
    }

    /**
     * Sakhtane Bazar Irani
     */
    public boolean buildBazaar(Location origin) {
        World world = origin.getWorld();
        if (world == null) return false;
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // Floor 15x15
        fill(world, ox - 7, oy, oz - 7, ox + 7, oy, oz + 7, Material.WOOD);
        fill(world, ox - 7, oy, oz - 7, ox + 7, oy, oz + 7, Material.CARPET, (byte) 14);

        // Hojreha
        buildHojreh(world, ox - 6, oy, oz - 6);
        buildHojreh(world, ox + 6, oy, oz - 6);
        buildHojreh(world, ox - 6, oy, oz + 6);
        buildHojreh(world, ox + 6, oy, oz + 6);

        // Roof
        fill(world, ox - 7, oy + 5, oz - 7, ox + 7, oy + 5, oz + 7, Material.WOOD);
        fill(world, ox - 6, oy + 6, oz - 6, ox + 6, oy + 6, oz + 6, Material.WOOD);

        // Villagers
        spawnPersianVillager(world, ox - 5, oy + 1, oz - 5, "Farshforoosh Kermani");
        spawnPersianVillager(world, ox + 5, oy + 1, oz - 5, "Chayforoosh Lahijani");
        spawnPersianVillager(world, ox - 5, oy + 1, oz + 5, "Javaherforoosh Esfahani");
        spawnPersianVillager(world, ox + 5, oy + 1, oz + 5, "Atigheforoosh Shirazi");

        // Chests
        setChest(world, ox, oy + 1, oz, StructureType.BAZAAR);

        IronGolem golem = (IronGolem) world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.IRON_GOLEM);
        golem.setCustomName(MessageUtils.color("&6Negahbane Bazar"));
        golem.setCustomNameVisible(true);

        return true;
    }

    /**
     * Karvansaraye Shah Abbasi
     */
    public boolean buildCaravanserai(Location origin) {
        World world = origin.getWorld();
        if (world == null) return false;
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        buildWalls(world, ox, oy, oz, 12, 6, Material.SANDSTONE);

        // Courtyard
        fill(world, ox - 8, oy, oz - 8, ox + 8, oy, oz + 8, Material.SANDSTONE);
        fill(world, ox - 6, oy, oz - 6, ox + 6, oy, oz + 6, Material.WATER);

        for (int x = -10; x <= 10; x += 5) {
            for (int z = -10; z <= 10; z += 10) {
                if (Math.abs(x) == 10 || Math.abs(z) == 10) {
                    buildSmallRoom(world, ox + x, oy, oz + z, Material.SANDSTONE);
                }
            }
        }

        fill(world, ox - 2, oy + 1, oz - 12, ox + 2, oy + 4, oz - 12, Material.AIR);
        fill(world, ox - 3, oy, oz - 13, ox + 3, oy + 5, oz - 13, Material.SANDSTONE);
        fill(world, ox - 2, oy + 1, oz - 13, ox + 2, oy + 4, oz - 13, Material.AIR);

        setChest(world, ox + 8, oy + 1, oz + 8, StructureType.CARAVANSERAI);
        setChest(world, ox - 8, oy + 1, oz - 8, StructureType.CARAVANSERAI);

        spawnPersianVillager(world, ox, oy + 1, oz + 3, "Karvansaradar Abbasi");

        return true;
    }

    /**
     * Chaykhane Sonati
     */
    public boolean buildChaikhaneh(Location origin) {
        World world = origin.getWorld();
        if (world == null) return false;
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        buildWalls(world, ox, oy, oz, 4, 4, Material.WOOD);
        fill(world, ox - 4, oy + 4, oz - 4, ox + 4, oy + 4, oz + 4, Material.WOOD);

        fill(world, ox - 3, oy, oz - 3, ox + 3, oy, oz + 3, Material.CARPET, (byte) 14);

        world.getBlockAt(ox, oy + 1, oz).setType(Material.FURNACE);
        world.getBlockAt(ox, oy + 2, oz).setType(Material.CAULDRON);

        world.getBlockAt(ox - 2, oy + 1, oz - 2).setType(Material.BED_BLOCK);
        world.getBlockAt(ox + 2, oy + 1, oz + 2).setType(Material.BED_BLOCK);

        setChest(world, ox + 1, oy + 1, oz, StructureType.CHAIKHANEH);
        spawnPersianVillager(world, ox, oy + 1, oz + 1, "Chaychie Tabrizi");

        return true;
    }

    /**
     * Ab Anbare Yazdi
     */
    public boolean buildAbAnbar(Location origin) {
        World world = origin.getWorld();
        if (world == null) return false;
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        fill(world, ox - 5, oy - 10, oz - 5, ox + 5, oy, oz + 5, Material.SMOOTH_BRICK);
        fill(world, ox - 4, oy - 9, oz - 4, ox + 4, oy - 1, oz + 4, Material.AIR);
        fill(world, ox - 4, oy - 9, oz - 4, ox + 4, oy - 5, oz + 4, Material.WATER);

        buildDome(world, ox, oy + 1, oz, 6, Material.SMOOTH_BRICK);
        buildWindcatcher(world, ox + 6, oy, oz);

        for (int y = 0; y < 10; y++) {
            world.getBlockAt(ox + 5, oy - y, oz).setType(Material.SMOOTH_BRICK);
            world.getBlockAt(ox + 4, oy - y, oz).setType(Material.AIR);
        }

        setChest(world, ox, oy - 4, oz + 3, StructureType.AB_ANBAR);
        return true;
    }

    private void buildHojreh(World world, int ox, int oy, int oz) {
        buildWalls(world, ox, oy, oz, 2, 4, Material.WOOD);
        fill(world, ox - 2, oy + 4, oz - 2, ox + 2, oy + 4, oz + 2, Material.WOOL, (byte) 14);
        world.getBlockAt(ox, oy + 1, oz).setType(Material.CHEST);
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

    private void fill(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material mat) {
        fill(world, x1, y1, z1, x2, y2, z2, mat, (byte) 0);
    }

    private void fill(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material mat, byte data) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
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

    private void buildSmallRoom(World world, int ox, int oy, int oz, Material mat) {
        buildWalls(world, ox, oy, oz, 1, 3, mat);
        fill(world, ox - 1, oy + 3, oz - 1, ox + 1, oy + 3, oz + 1, mat);
        world.getBlockAt(ox, oy + 1, oz).setType(Material.BED_BLOCK);
    }

    private void buildDome(World world, int ox, int oy, int oz, int radius, Material mat) {
        for (int y = 0; y < radius; y++) {
            int r = radius - y;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    if (x * x + z * z <= r * r && x * x + z * z >= (r - 1) * (r - 1)) {
                        world.getBlockAt(ox + x, oy + y, oz + z).setType(mat);
                    }
                }
            }
        }
    }

    private void buildWindcatcher(World world, int ox, int oy, int oz) {
        buildWalls(world, ox, oy, oz, 1, 10, Material.SMOOTH_BRICK);
        for (int y = 5; y < 9; y++) {
            world.getBlockAt(ox - 1, oy + y, oz).setType(Material.AIR);
            world.getBlockAt(ox + 1, oy + y, oz).setType(Material.AIR);
            world.getBlockAt(ox, oy + y, oz - 1).setType(Material.AIR);
            world.getBlockAt(ox, oy + y, oz + 1).setType(Material.AIR);
        }
    }

    private void spawnPersianVillager(World world, int x, int y, int z, String finglishName) {
        Location loc = new Location(world, x, y, z);
        Villager villager = (Villager) world.spawnEntity(loc, EntityType.VILLAGER);
        villager.setCustomName(MessageUtils.color("&e" + finglishName));
        villager.setCustomNameVisible(true);
        villager.setProfession(Villager.Profession.FARMER);
    }

    private void setChest(World world, int x, int y, int z, StructureType type) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.CHEST);
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();
            chest.getInventory().clear();
            switch (type) {
                case BAZAAR:
                    chest.getInventory().addItem(PersianItems.getRandomPersianLoot());
                    chest.getInventory().addItem(new ItemStack(Material.EMERALD, 5));
                    chest.getInventory().addItem(PersianItems.createFarshKermani());
                    break;
                case CARAVANSERAI:
                    chest.getInventory().addItem(new ItemStack(Material.BREAD, 10));
                    chest.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
                    chest.getInventory().addItem(new ItemStack(Material.SADDLE));
                    break;
                case CHAIKHANEH:
                    chest.getInventory().addItem(PersianItems.createChaiIrani());
                    chest.getInventory().addItem(new ItemStack(Material.COOKIE, 10));
                    break;
                case AB_ANBAR:
                    chest.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 3));
                    chest.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 5));
                    break;
                default:
                    chest.getInventory().addItem(PersianItems.getRandomPersianLoot());
                    break;
            }
            chest.update();
        }
    }

    public void tryGenerateInChunk(org.bukkit.Chunk chunk) {
        if (!plugin.getConfigManager().getBoolean("structures.enabled", true)) return;

        double chance = plugin.getConfigManager().getDouble("structures.spawn-chance", 0.015);
        if (random.nextDouble() > chance) return;

        Location center = new Location(chunk.getWorld(), chunk.getX() * 16 + 8, 0, chunk.getZ() * 16 + 8);
        center.setY(chunk.getWorld().getHighestBlockYAt(center));

        Biome biome = center.getBlock().getBiome();
        StructureType toBuild;

        if (biome.name().contains("DESERT") || biome.name().contains("MESA")) {
            toBuild = random.nextBoolean() ? StructureType.CARAVANSERAI : StructureType.AB_ANBAR;
        } else if (biome.name().contains("PLAINS") || biome.name().contains("SAVANNA")) {
            toBuild = StructureType.BAZAAR;
        } else if (biome.name().contains("FOREST") || biome.name().contains("TAIGA")) {
            toBuild = StructureType.CHAIKHANEH;
        } else {
            toBuild = StructureType.BAZAAR;
        }

        StructureType finalType = toBuild;
        org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, () -> {
            forceBuildStructure(center, finalType);
            plugin.getLogger().info("Sazeye Irani " + finalType.getPersianName() + " dar " + center.getBlockX() + "," + center.getBlockZ() + " sakhte shod");
        }, 20L);
    }
}

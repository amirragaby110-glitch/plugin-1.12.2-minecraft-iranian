package ir.iranian.hardcore.structures;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.health.PlayerHealthManager;
import ir.iranian.hardcore.items.PersianBossItems;
import ir.iranian.hardcore.items.PersianItems;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Procedural Exploration & Loot Structures Across the Map (v5.2 Finglish)
 * Features:
 * 1. Karvansara-ye Shah Abbasi (24x24 Silk Road Quadrangle Fortress)
 * 2. Ab-Anbar-e Yazdi (4-Badgir Subterranean Water Cistern)
 * 3. Chaikhaneh-ye Sonnati (Cozy Brick Teahouse & Garden)
 * 4. Atashkadeh-ye Sasanian (Chahar-Taq Eternal Fire Sanctuary)
 * 5. Bazar-e Sonnati (Vaulted Persian Merchant Bazaar)
 * 
 * Deep foundations, interior clearing, curb-contained water pools,
 * and high-value loot chests with Sekkeh Derik, Heart Canisters, and Persian relics.
 */
public class PersianStructures {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public PersianStructures(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    public enum StructureType {
        BAZAAR("Bazar-e Sonnati", "Persian Grand Bazaar", Material.EMERALD_BLOCK, "Bazar-e sonnati ba hojreh-haye tejarat"),
        CARAVANSERAI("Karvansara-ye Shah Abbasi", "Shah Abbasi Caravanserai", Material.SANDSTONE, "Dezh-e khedmat-resani-ye Jadeh-ye Abrisham"),
        CHAIKHANEH("Chaykhaneh-ye Sonnati", "Traditional Persian Teahouse", Material.WOOD, "Chaykhaneh ba samavar va farsh-e Kermani"),
        AB_ANBAR("Ab-Anbar-e Yazdi", "Yazd Water Cistern", Material.SMOOTH_BRICK, "Ab-anbar-e khonak-e kaviri ba 4 badgir"),
        ATASHKADEH("Atashkadeh-ye Sasanian", "Sassanid Fire Temple", Material.NETHERRACK, "Chahar-taq-e atash-e moghaddas"),
        VILLAGE("Roostaye Irani", "Iranian Mountain Village", Material.COBBLESTONE, "Roostaye pelekani-ye Masooleh");

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
        origin = findGround(origin);
        if (origin == null) return false;

        switch (type) {
            case BAZAAR: return buildBazaar(origin);
            case CARAVANSERAI: return buildCaravanserai(origin);
            case CHAIKHANEH: return buildChaikhaneh(origin);
            case AB_ANBAR: return buildAbAnbar(origin);
            case ATASHKADEH: return buildFireTemple(origin);
            case VILLAGE:
                if (plugin.getVillageManager() != null) {
                    plugin.getVillageManager().generateVillage(origin);
                    return true;
                }
                return buildCaravanserai(origin);
            default:
                return buildCaravanserai(origin);
        }
    }

    private Location findGround(Location loc) {
        World world = loc.getWorld();
        if (world == null) return null;
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int y = world.getHighestBlockYAt(x, z);

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
    // 1. KARVANSARA-YE SHAH ABBASI (Silk Road Caravanserai)
    // ==========================================
    public boolean buildCaravanserai(Location origin) {
        World world = origin.getWorld();
        if (world == null) return false;
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 1. Deep solid foundation (26x26)
        fill(world, ox - 13, oy - 6, oz - 13, ox + 13, oy, oz + 13, Material.SMOOTH_BRICK);

        // 2. Clear interior
        fill(world, ox - 13, oy + 1, oz - 13, ox + 13, oy + 14, oz + 13, Material.AIR);

        // 3. Perimeter fortified walls (Height 6)
        buildWallFrame(world, ox - 12, oz - 12, ox + 12, oz + 12, oy + 1, oy + 6, Material.SANDSTONE);

        // 4. Corner Bastion Towers (3x3, Height 9)
        int[] corners = {-12, 12};
        for (int cx : corners) {
            for (int cz : corners) {
                fill(world, ox + cx - 1, oy + 1, oz + cz - 1, ox + cx + 1, oy + 9, oz + cz + 1, Material.SMOOTH_BRICK);
                // Torch on tower top
                world.getBlockAt(ox + cx, oy + 10, oz + cz).setType(Material.TORCH);
                // Tower chest in top chamber
                setLootChest(world, ox + cx, oy + 8, oz + cz, StructureType.CARAVANSERAI);
            }
        }

        // 5. Central Cobblestone Courtyard Floor
        fill(world, ox - 10, oy, oz - 10, ox + 10, oy, oz + 10, Material.COBBLESTONE);

        // 6. Central Curb-Contained Octagonal Water Fountain (Safe, no flooding!)
        fill(world, ox - 2, oy + 1, oz - 2, ox + 2, oy + 1, oz + 2, Material.SANDSTONE);
        fill(world, ox - 1, oy + 1, oz - 1, ox + 1, oy + 1, oz + 1, Material.WATER);
        world.getBlockAt(ox, oy + 2, oz).setType(Material.COBBLE_WALL);
        world.getBlockAt(ox, oy + 3, oz).setType(Material.GLOWSTONE);

        // 7. Traveler Rooms on Sides (North, South, East, West)
        buildCaravanseraiRoom(world, ox - 7, oy + 1, oz - 10);
        buildCaravanseraiRoom(world, ox + 3, oy + 1, oz - 10);
        buildCaravanseraiRoom(world, ox - 7, oy + 1, oz + 7);
        buildCaravanseraiRoom(world, ox + 3, oy + 1, oz + 7);

        // 8. Grand Arched Pishtaq Gateway (South Entrance)
        fill(world, ox - 3, oy + 1, oz - 13, ox + 3, oy + 8, oz - 11, Material.SMOOTH_BRICK);
        fill(world, ox - 1, oy + 1, oz - 13, ox + 1, oy + 5, oz - 11, Material.AIR); // arched opening
        world.getBlockAt(ox - 2, oy + 4, oz - 14).setType(Material.TORCH);
        world.getBlockAt(ox + 2, oy + 4, oz - 14).setType(Material.TORCH);

        // 9. Loot Chests in courtyard stalls
        setLootChest(world, ox + 6, oy + 1, oz, StructureType.CARAVANSERAI);
        setLootChest(world, ox - 6, oy + 1, oz, StructureType.CARAVANSERAI);

        // 10. Caravanserai NPCs
        spawnPersianVillager(world, ox, oy + 1, oz + 4, "Karvansaradar Abbasi", Villager.Profession.LIBRARIAN);
        spawnPersianVillager(world, ox - 4, oy + 1, oz - 4, "Bazargan-e Abrisham", Villager.Profession.FARMER);

        IronGolem guard = (IronGolem) world.spawnEntity(new Location(world, ox, oy + 1, oz - 8), EntityType.IRON_GOLEM);
        guard.setCustomName(MessageUtils.color("&6&lNegahban-e Karvansara"));
        guard.setCustomNameVisible(true);

        return true;
    }

    private void buildCaravanseraiRoom(World world, int x, int y, int z) {
        fill(world, x, y, z, x + 4, y + 4, z + 3, Material.SANDSTONE);
        fill(world, x + 1, y, z + 1, x + 3, y + 3, z + 2, Material.AIR);
        fill(world, x + 1, y - 1, z + 1, x + 3, y - 1, z + 2, Material.WOOD);
        // Red wool cushion bed
        world.getBlockAt(x + 1, y, z + 2).setType(Material.WOOL);
        world.getBlockAt(x + 1, y, z + 2).setData((byte) 14); // Red
        // Door opening
        world.getBlockAt(x + 2, y, z).setType(Material.AIR);
        world.getBlockAt(x + 2, y + 1, z).setType(Material.AIR);
        world.getBlockAt(x + 2, y + 2, z + 1).setType(Material.TORCH);
    }

    // ==========================================
    // 2. AB-ANBAR-E YAZDI (Desert Water Cistern)
    // ==========================================
    public boolean buildAbAnbar(Location origin) {
        World world = origin.getWorld();
        if (world == null) return false;
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        // 1. Foundation & Deep Subterranean Reservoir (Depth 8 blocks)
        fill(world, ox - 8, oy - 9, oz - 8, ox + 8, oy, oz + 8, Material.SMOOTH_BRICK);
        fill(world, ox - 6, oy - 8, oz - 6, ox + 6, oy - 1, oz + 6, Material.AIR); // water vault

        // Water pool (Depth 4 blocks, covered by air)
        fill(world, ox - 5, oy - 8, oz - 5, ox + 5, oy - 4, oz + 5, Material.WATER);

        // Sunken ancient treasure chest at the bottom of the water reservoir!
        setLootChest(world, ox, oy - 8, oz, StructureType.AB_ANBAR);
        setLootChest(world, ox + 2, oy - 8, oz + 2, StructureType.AB_ANBAR);

        // 2. Surface Terracotta Vaulted Dome (Height 7)
        clearArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 18, oz + 8);
        buildDomeShell(world, ox, oy + 1, oz, 7, Material.HARD_CLAY);

        // 3. Four Traditional Badgirs (Windcatcher Towers - Height 16)
        int[] bOffsets = {-7, 7};
        for (int bx : bOffsets) {
            for (int bz : bOffsets) {
                fill(world, ox + bx - 1, oy + 1, oz + bz - 1, ox + bx + 1, oy + 16, oz + bz + 1, Material.CLAY_BRICK);
                // Vents at top
                for (int vy = 12; vy <= 15; vy++) {
                    world.getBlockAt(ox + bx, oy + vy, oz + bz - 1).setType(Material.AIR);
                    world.getBlockAt(ox + bx, oy + vy, oz + bz + 1).setType(Material.AIR);
                    world.getBlockAt(ox + bx - 1, oy + vy, oz + bz).setType(Material.AIR);
                    world.getBlockAt(ox + bx + 1, oy + vy, oz + bz).setType(Material.AIR);
                }
                world.getBlockAt(ox + bx, oy + 17, oz + bz).setType(Material.TORCH);
            }
        }

        // 4. Stepped Stairs descending into the deep cool reservoir
        for (int step = 0; step < 7; step++) {
            int sx = ox;
            int sy = oy - step;
            int sz = oz - 8 + step;
            world.getBlockAt(sx, sy, sz).setType(Material.SMOOTH_STAIRS);
            world.getBlockAt(sx, sy + 1, sz).setType(Material.AIR);
            world.getBlockAt(sx, sy + 2, sz).setType(Material.AIR);
            world.getBlockAt(sx, sy + 3, sz).setType(Material.AIR);
        }

        // Entrance Portal
        fill(world, ox - 2, oy + 1, oz - 9, ox + 2, oy + 4, oz - 7, Material.CLAY_BRICK);
        fill(world, ox - 1, oy + 1, oz - 9, ox + 1, oy + 3, oz - 7, Material.AIR);
        world.getBlockAt(ox - 2, oy + 2, oz - 10).setType(Material.TORCH);
        world.getBlockAt(ox + 2, oy + 2, oz - 10).setType(Material.TORCH);

        // Persian Mirab NPC
        spawnPersianVillager(world, ox, oy + 1, oz - 10, "Mirab-e Yazdi (Mas'ool-e Ab)", Villager.Profession.PRIEST);

        return true;
    }

    // ==========================================
    // 3. CHAIKHANEH-YE SONNATI (Traditional Teahouse)
    // ==========================================
    public boolean buildChaikhaneh(Location origin) {
        World world = origin.getWorld();
        if (world == null) return false;
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        fill(world, ox - 8, oy - 4, oz - 8, ox + 8, oy, oz + 8, Material.CLAY_BRICK);
        clearArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 10, oz + 8);

        // Brick Walls with large windows (12x10)
        buildWallFrame(world, ox - 6, oz - 5, ox + 6, oz + 5, oy + 1, oy + 5, Material.CLAY_BRICK);
        // Wood Roof
        fill(world, ox - 7, oy + 6, oz - 6, ox + 7, oy + 6, oz + 6, Material.WOOD);
        fill(world, ox - 6, oy + 7, oz - 5, ox + 6, oy + 7, oz + 5, Material.WOOD);

        // Interior Floor: Polished Wood with Persian Rugs
        fill(world, ox - 5, oy, oz - 4, ox + 5, oy, oz + 4, Material.WOOD);
        // Red Persian Carpets
        fill(world, ox - 4, oy + 1, oz - 3, ox + 4, oy + 1, oz + 3, Material.CARPET, (byte) 14);

        // Raised Seating Platforms (Takht-haye sonnati)
        fill(world, ox - 4, oy + 1, oz - 4, ox - 2, oy + 1, oz - 3, Material.WOOD);
        fill(world, ox + 2, oy + 1, oz - 4, ox + 4, oy + 1, oz - 3, Material.WOOD);
        fill(world, ox - 4, oy + 1, oz + 3, ox - 2, oy + 1, oz + 4, Material.WOOD);
        fill(world, ox + 2, oy + 1, oz + 3, ox + 4, oy + 1, oz + 4, Material.WOOD);

        // Counter with Brass Samovar (Brewing Stand)
        world.getBlockAt(ox, oy + 1, oz - 2).setType(Material.BREWING_STAND);
        world.getBlockAt(ox - 1, oy + 1, oz - 2).setType(Material.CAULDRON);
        world.getBlockAt(ox + 1, oy + 1, oz - 2).setType(Material.FURNACE);

        // Entrance Door opening
        world.getBlockAt(ox, oy + 1, oz + 5).setType(Material.AIR);
        world.getBlockAt(ox, oy + 2, oz + 5).setType(Material.AIR);

        // Teahouse Loot Chests with Chai Lahijan, Sangak, and Sweets
        setLootChest(world, ox - 5, oy + 1, oz, StructureType.CHAIKHANEH);
        setLootChest(world, ox + 5, oy + 1, oz, StructureType.CHAIKHANEH);

        // Teahouse Master NPC
        spawnPersianVillager(world, ox, oy + 1, oz - 1, "Haj Kazem - Chaychi", Villager.Profession.FARMER);

        return true;
    }

    // ==========================================
    // 4. ATASHKADEH-YE SASANIAN (Sassanid Fire Temple)
    // ==========================================
    public boolean buildFireTemple(Location origin) {
        World world = origin.getWorld();
        if (world == null) return false;
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        fill(world, ox - 8, oy - 6, oz - 8, ox + 8, oy, oz + 8, Material.SMOOTH_BRICK);
        clearArea(world, ox - 8, oy + 1, oz - 8, ox + 8, oy + 14, oz + 8);

        // 4 Grand Stone Corner Piers
        int[] pCoords = {-5, 5};
        for (int px : pCoords) {
            for (int pz : pCoords) {
                fill(world, ox + px - 1, oy + 1, oz + pz - 1, ox + px + 1, oy + 8, oz + pz + 1, Material.SMOOTH_BRICK);
            }
        }

        // Arched Chahar-Taq Dome Shell
        fill(world, ox - 6, oy + 8, oz - 6, ox + 6, oy + 9, oz + 6, Material.SMOOTH_BRICK);
        buildDomeShell(world, ox, oy + 9, oz, 5, Material.STONE);

        // Central Sacred Fire Altar (Azargoshasb)
        fill(world, ox - 1, oy + 1, oz - 1, ox + 1, oy + 1, oz + 1, Material.GOLD_BLOCK);
        world.getBlockAt(ox, oy + 2, oz).setType(Material.NETHERRACK);
        world.getBlockAt(ox, oy + 3, oz).setType(Material.FIRE);

        // Fire Brazier corners
        world.getBlockAt(ox - 1, oy + 2, oz - 1).setType(Material.IRON_FENCE);
        world.getBlockAt(ox + 1, oy + 2, oz - 1).setType(Material.IRON_FENCE);
        world.getBlockAt(ox - 1, oy + 2, oz + 1).setType(Material.IRON_FENCE);
        world.getBlockAt(ox + 1, oy + 2, oz + 1).setType(Material.IRON_FENCE);

        // 2 Holy Relic Loot Chests
        setLootChest(world, ox - 3, oy + 1, oz, StructureType.ATASHKADEH);
        setLootChest(world, ox + 3, oy + 1, oz, StructureType.ATASHKADEH);

        // Fire Priest NPC
        spawnPersianVillager(world, ox, oy + 1, oz - 4, "Mobed-e Zartoshti", Villager.Profession.PRIEST);

        return true;
    }

    // ==========================================
    // 5. BAZAAR-E SONNATI (Vaulted Grand Bazaar)
    // ==========================================
    public boolean buildBazaar(Location origin) {
        World world = origin.getWorld();
        if (world == null) return false;
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();

        fill(world, ox - 10, oy - 5, oz - 10, ox + 10, oy, oz + 10, Material.CLAY_BRICK);
        clearArea(world, ox - 10, oy + 1, oz - 10, ox + 10, oy + 12, oz + 10);

        // Vaulted Alleyway (X from -9 to +9, Z from -4 to +4)
        fill(world, ox - 9, oy, oz - 4, ox + 9, oy, oz + 4, Material.WOOD);
        fill(world, ox - 8, oy + 1, oz - 1, ox + 8, oy + 1, oz + 1, Material.CARPET, (byte) 14);

        // Arched Vaulted Brick Roof
        fill(world, ox - 9, oy + 6, oz - 4, ox + 9, oy + 6, oz + 4, Material.CLAY_BRICK);
        // Skylights (Hoorno) for natural atmospheric lighting
        for (int lx = -6; lx <= 6; lx += 4) {
            world.getBlockAt(ox + lx, oy + 6, oz).setType(Material.GLOWSTONE);
        }

        // 4 Merchant Stalls (Hojreh)
        buildMerchantStall(world, ox - 6, oy + 1, oz - 4, "Farshforoosh Kermani", Material.CARPET, (byte) 14);
        buildMerchantStall(world, ox + 3, oy + 1, oz - 4, "Javaherforoosh Neyshaboori", Material.EMERALD_BLOCK, (byte) 0);
        buildMerchantStall(world, ox - 6, oy + 1, oz + 2, "Ahangar-e Esfahani", Material.ANVIL, (byte) 0);
        buildMerchantStall(world, ox + 3, oy + 1, oz + 2, "Attar-e Shirazi", Material.BREWING_STAND, (byte) 0);

        // Bazaar Guard Golem
        IronGolem guard = (IronGolem) world.spawnEntity(new Location(world, ox, oy + 1, oz), EntityType.IRON_GOLEM);
        guard.setCustomName(MessageUtils.color("&6&lDarogheh-ye Bazaar"));
        guard.setCustomNameVisible(true);

        return true;
    }

    private void buildMerchantStall(World world, int x, int y, int z, String merchantName, Material counterMat, byte data) {
        fill(world, x, y, z, x + 3, y + 3, z + 2, Material.CLAY_BRICK);
        fill(world, x + 1, y, z + 1, x + 2, y + 2, z + 1, Material.AIR);
        world.getBlockAt(x + 1, y, z).setType(counterMat);
        if (data != 0) {
            try { world.getBlockAt(x + 1, y, z).setData(data); } catch (Exception ignored) {}
        }
        world.getBlockAt(x + 2, y, z).setType(Material.FENCE);
        world.getBlockAt(x + 2, y + 1, z).setType(Material.TORCH);

        setLootChest(world, x + 1, y, z + 1, StructureType.BAZAAR);
        spawnPersianVillager(world, x + 2, y, z + 1, merchantName, Villager.Profession.LIBRARIAN);
    }

    // ==========================================
    // Architectural Helpers
    // ==========================================

    private void buildWallFrame(World world, int x1, int z1, int x2, int z2, int y1, int y2, Material mat) {
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                world.getBlockAt(x, y, z1).setType(mat);
                world.getBlockAt(x, y, z2).setType(mat);
            }
            for (int z = z1; z <= z2; z++) {
                world.getBlockAt(x1, y, z).setType(mat);
                world.getBlockAt(x2, y, z).setType(mat);
            }
        }
    }

    private void buildDomeShell(World world, int cx, int cy, int cz, int radius, Material mat) {
        for (int y = 0; y <= radius; y++) {
            int r = radius - y;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    int distSq = x * x + z * z;
                    if (distSq <= r * r && distSq >= (r - 2) * (r - 2)) {
                        world.getBlockAt(cx + x, cy + y, cz + z).setType(mat);
                    }
                }
            }
        }
    }

    private void fill(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material mat) {
        fill(world, x1, y1, z1, x2, y2, z2, mat, (byte) 0);
    }

    private void fill(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material mat, byte data) {
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

    private void clearArea(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        fill(world, x1, y1, z1, x2, y2, z2, Material.AIR);
    }

    private void spawnPersianVillager(World world, int x, int y, int z, String name, Villager.Profession prof) {
        Location loc = new Location(world, x + 0.5, y, z + 0.5);
        Villager villager = (Villager) world.spawnEntity(loc, EntityType.VILLAGER);
        villager.setCustomName(MessageUtils.color("&e&l" + name));
        villager.setCustomNameVisible(true);
        villager.setProfession(prof);
    }

    /**
     * Fills exploration chests with rich Persian loot, Sekkeh Derik, Heart Canisters, and Boss-hunting gear!
     */
    public void setLootChest(World world, int x, int y, int z, StructureType type) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.CHEST);
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();
            chest.getInventory().clear();

            // Core Persian Treasures
            chest.getInventory().addItem(PersianItems.createSekkeHakhamaneshi());
            chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, random.nextInt(4) + 2));
            chest.getInventory().addItem(PersianItems.getRandomPersianLoot());

            // 35% chance for a Heart Canister / Health upgrade!
            if (random.nextDouble() < 0.35) {
                if (random.nextBoolean()) {
                    chest.getInventory().addItem(PlayerHealthManager.createRedHeartCanister());
                } else {
                    chest.getInventory().addItem(PlayerHealthManager.createTurquoiseHeartCanister());
                }
            }

            // 25% chance for Boss hunting tactical items!
            if (random.nextDouble() < 0.25) {
                int r = random.nextInt(3);
                if (r == 0) chest.getInventory().addItem(PersianBossItems.createNaphthaBomb(2));
                else if (r == 1) chest.getInventory().addItem(PersianBossItems.createJamshidTalisman());
                else chest.getInventory().addItem(PersianBossItems.createDivKoshSword());
            }

            // Structure specific additions
            if (type == StructureType.CARAVANSERAI) {
                chest.getInventory().addItem(new ItemStack(Material.SADDLE));
                chest.getInventory().addItem(new ItemStack(Material.IRON_BARDING));
                chest.getInventory().addItem(new ItemStack(Material.BREAD, 8));
            } else if (type == StructureType.AB_ANBAR) {
                chest.getInventory().addItem(PersianItems.createMorvaridKhalij());
                chest.getInventory().addItem(new ItemStack(Material.PRISMARINE_SHARD, 4));
                chest.getInventory().addItem(PlayerHealthManager.createElixirOfLife());
            } else if (type == StructureType.CHAIKHANEH) {
                chest.getInventory().addItem(PersianItems.createChaiIrani());
                chest.getInventory().addItem(new ItemStack(Material.COOKIE, 12));
                chest.getInventory().addItem(new ItemStack(Material.SUGAR, 8));
            } else if (type == StructureType.ATASHKADEH) {
                chest.getInventory().addItem(PersianItems.createAtashMoghadas());
                chest.getInventory().addItem(new ItemStack(Material.BLAZE_POWDER, 4));
                chest.getInventory().addItem(new ItemStack(Material.EMERALD, 5));
            } else if (type == StructureType.BAZAAR) {
                chest.getInventory().addItem(PersianItems.createFarshKermani());
                chest.getInventory().addItem(new ItemStack(Material.DIAMOND, 2));
                chest.getInventory().addItem(new ItemStack(Material.EMERALD, 6));
            }

            chest.update();
        }
    }

    public void tryGenerateInChunk(Chunk chunk) {
        if (!plugin.getConfigManager().getBoolean("structures.enabled", true)) return;

        double chance = plugin.getConfigManager().getDouble("structures.spawn-chance", 0.02);
        if (random.nextDouble() > chance) return;

        Location center = new Location(chunk.getWorld(), chunk.getX() * 16 + 8, 0, chunk.getZ() * 16 + 8);
        center = findGround(center);
        if (center == null) return;

        Biome biome = center.getBlock().getBiome();
        StructureType toBuild;

        if (biome.name().contains("DESERT") || biome.name().contains("MESA")) {
            toBuild = random.nextBoolean() ? StructureType.CARAVANSERAI : StructureType.AB_ANBAR;
        } else if (biome.name().contains("FOREST") || biome.name().contains("PLAINS")) {
            toBuild = random.nextBoolean() ? StructureType.BAZAAR : StructureType.CHAIKHANEH;
        } else if (biome.name().contains("MOUNTAIN") || biome.name().contains("EXTREME_HILLS")) {
            toBuild = StructureType.ATASHKADEH;
        } else {
            toBuild = StructureType.CARAVANSERAI;
        }

        Location finalCenter = center;
        StructureType finalType = toBuild;
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            forceBuildStructure(finalCenter, finalType);
            plugin.getLogger().info("Sazeh-ye Irani " + finalType.getPersianName() + " dar " 
                    + finalCenter.getBlockX() + "," + finalCenter.getBlockY() + "," + finalCenter.getBlockZ() + " sakhteh shod!");
        }, 30L);
    }
}

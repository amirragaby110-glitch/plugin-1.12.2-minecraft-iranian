package ir.iranian.hardcore.dungeons;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Dungeon Manager (v5.0 Finglish)
 * - 30 Historical Iranian Dungeons
 * - Manages generation, registry, and auto-chunk spawns
 * - Completely Finglish, zero Persian Unicode characters.
 */
public class DungeonManager {

    private final IranianHardcorePlugin plugin;
    private final StructureBuilder builder;
    private final Random random = new Random();

    private final Map<String, Location> generatedDungeons = new HashMap<>();
    private final Set<String> generatedRegions = new HashSet<>();

    public DungeonManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        this.builder = new StructureBuilder(plugin);
        loadGeneratedDungeons();
    }

    private void loadGeneratedDungeons() {
        FileConfiguration cfg = plugin.getConfigManager().getDungeonsConfig();
        if (cfg.isConfigurationSection("generated-dungeons")) {
            for (String key : cfg.getConfigurationSection("generated-dungeons").getKeys(false)) {
                String path = "generated-dungeons." + key;
                String worldName = cfg.getString(path + ".world");
                int x = cfg.getInt(path + ".x");
                int y = cfg.getInt(path + ".y");
                int z = cfg.getInt(path + ".z");
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    generatedDungeons.put(key, new Location(world, x, y, z));
                }
            }
        }
        plugin.getLogger().info("DungeonManager: Loaded " + generatedDungeons.size() + " Iranian dungeons.");
    }

    public boolean generateDungeon(Location loc, DungeonType type) {
        return generateDungeon(loc, type, false);
    }

    public boolean generateDungeon(Location loc, DungeonType type, boolean force) {
        if (loc == null || type == null) return false;

        // Check distance from existing dungeons unless forced
        if (!force) {
            for (Location existing : generatedDungeons.values()) {
                if (existing.getWorld() != null && existing.getWorld().equals(loc.getWorld())) {
                    if (existing.distanceSquared(loc) < 160000) { // 400 blocks squared
                        return false;
                    }
                }
            }
        }

        boolean success = builder.buildDungeon(loc, type);
        if (success) {
            String key = type.getId() + "_" + loc.getBlockX() + "_" + loc.getBlockZ() + "_" + System.currentTimeMillis();
            generatedDungeons.put(key, loc);

            FileConfiguration cfg = plugin.getConfigManager().getDungeonsConfig();
            String path = "generated-dungeons." + key;
            cfg.set(path + ".world", loc.getWorld().getName());
            cfg.set(path + ".x", loc.getBlockX());
            cfg.set(path + ".y", loc.getBlockY());
            cfg.set(path + ".z", loc.getBlockZ());
            cfg.set(path + ".type", type.getId());
            cfg.set(path + ".finglishName", type.getFinglishName());
            cfg.set(path + ".time", System.currentTimeMillis());
            plugin.getConfigManager().saveDungeonsConfig();

            plugin.getLogger().info("Dungeon Irani " + type.getFinglishName() + " dar " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + " sakhteh shod!");
        }
        return success;
    }

    public void tryGenerateInChunk(Chunk chunk) {
        if (!plugin.getConfigManager().getBoolean("dungeons.enabled", true)) return;
        if (chunk == null || chunk.getWorld() == null) return;

        int cx = chunk.getX();
        int cz = chunk.getZ();

        // 18x18 chunk regional exploration grid (~288x288 blocks)
        int rx = Math.floorDiv(cx, 18);
        int rz = Math.floorDiv(cz, 18);

        // Deterministic offset per region
        long seed = ((long) rx * 341873128712L + (long) rz * 132897987541L) ^ chunk.getWorld().getSeed();
        Random regRand = new Random(seed);
        int targetOffsetX = regRand.nextInt(14) + 2;
        int targetOffsetZ = regRand.nextInt(14) + 2;

        if (cx != (rx * 18 + targetOffsetX) || cz != (rz * 18 + targetOffsetZ)) {
            return;
        }

        String regionKey = chunk.getWorld().getName() + "_dungeon_r_" + rx + "_" + rz;
        if (generatedRegions.contains(regionKey)) return;
        generatedRegions.add(regionKey);

        Location center = new Location(chunk.getWorld(), cx * 16 + 8, 0, cz * 16 + 8);
        center.setY(chunk.getWorld().getHighestBlockYAt(center));

        Biome biome = center.getBlock().getBiome();
        DungeonType type = DungeonType.getForBiome(biome);

        if (type != null) {
            Location finalCenter = center;
            DungeonType finalType = type;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                generateDungeon(finalCenter, finalType);
            }, 20L);
        }
    }

    public void generateNearPlayer(Player player, DungeonType type) {
        Location loc = player.getLocation();
        Location target = loc.clone().add(loc.getDirection().multiply(35));
        target.setY(player.getWorld().getHighestBlockYAt(target));

        if (generateDungeon(target, type, true)) {
            player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aDungeon-e Irani &6" + type.getFinglishName() + " &adar nazdiki-ye shoma sakhteh shod!"));
            player.sendMessage(MessageUtils.color("&7Mokhtasat: &f" + target.getBlockX() + ", " + target.getBlockY() + ", " + target.getBlockZ()));

            // Safe entrance teleport facing the grand monument
            Location safeEntrance = target.clone().add(0, 2, -13);
            safeEntrance.setYaw(0f);
            safeEntrance.setPitch(0f);

            safeEntrance.getBlock().setType(Material.AIR);
            safeEntrance.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
            safeEntrance.clone().add(0, -1, 0).getBlock().setType(Material.SMOOTH_BRICK);

            player.teleport(safeEntrance);
        } else {
            player.sendMessage(MessageUtils.color("&8[&6Iran&8] &cSakht-e dungeon namovafagh bood! Shayad be dungeon-e digari kheyli nazdik ast."));
        }
    }

    public void listDungeons(Player player) {
        player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
        player.sendMessage(MessageUtils.color("&6&l30 Dungeon-e Tarikhi-ye Iran"));
        player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
        for (DungeonType type : DungeonType.values()) {
            player.sendMessage(MessageUtils.color("&7- &6" + type.getFinglishName() + " &7(" + type.getId() + ") &8- &f" + type.getDescription()));
            player.sendMessage(MessageUtils.color("  &7Biomes: &a" + type.getBiomes().size() + " &7| Sakhti: &c" + repeatStar(type.getDifficulty()) + " &7| Boss: &e" + type.getBossName()));
        }
        player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
        player.sendMessage(MessageUtils.color("&7Tedad-e sakhteh-shodeh: &a" + generatedDungeons.size()));
    }

    public Map<String, Location> getGeneratedDungeons() {
        return generatedDungeons;
    }

    public void clearAll() {
        generatedDungeons.clear();
        plugin.getConfigManager().getDungeonsConfig().set("generated-dungeons", null);
        plugin.getConfigManager().saveDungeonsConfig();
    }

    public void forceGenerateDungeon(Player player, DungeonType type) {
        generateNearPlayer(player, type);
    }

    public void clearGeneratedDungeons() {
        clearAll();
    }

    private String repeatStar(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("★");
        }
        return sb.toString();
    }
}

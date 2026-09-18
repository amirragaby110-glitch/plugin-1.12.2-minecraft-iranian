package ir.iranian.hardcore.dungeons;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * مدیریت دانجن‌های ایرانی
 * - ساخت دانجن
 * - ذخیره محل‌ها
 * - تولید تصادفی در چانک‌ها
 * - کاملا فارسی
 */
public class DungeonManager {

    private final IranianHardcorePlugin plugin;
    private final StructureBuilder builder;
    private final Random random = new Random();

    // کش دانجن‌های ساخته شده
    private final Map<String, Location> generatedDungeons = new HashMap<>();

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
                String type = cfg.getString(path + ".type");
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    generatedDungeons.put(key, new Location(world, x, y, z));
                }
            }
        }
        plugin.getLogger().info("تعداد دانجن‌های ایرانی لود شده: " + generatedDungeons.size());
    }

    /**
     * ساخت دانجن در لوکیشن مشخص
     */
    public boolean generateDungeon(Location loc, DungeonType type) {
        if (loc == null || type == null) return false;

        // چک فاصله از دانجن‌های دیگر (حداقل 500 بلوک)
        for (Location existing : generatedDungeons.values()) {
            if (existing.getWorld().equals(loc.getWorld())) {
                if (existing.distance(loc) < 500) {
                    return false; // خیلی نزدیک است
                }
            }
        }

        boolean success = builder.buildDungeon(loc, type);
        if (success) {
            String key = type.getId() + "_" + loc.getBlockX() + "_" + loc.getBlockZ() + "_" + System.currentTimeMillis();
            generatedDungeons.put(key, loc);

            // ذخیره در فایل
            FileConfiguration cfg = plugin.getConfigManager().getDungeonsConfig();
            String path = "generated-dungeons." + key;
            cfg.set(path + ".world", loc.getWorld().getName());
            cfg.set(path + ".x", loc.getBlockX());
            cfg.set(path + ".y", loc.getBlockY());
            cfg.set(path + ".z", loc.getBlockZ());
            cfg.set(path + ".type", type.getId());
            cfg.set(path + ".persianName", type.getPersianName());
            cfg.set(path + ".time", System.currentTimeMillis());
            plugin.getConfigManager().saveDungeonsConfig();

            plugin.getLogger().info("دانجن ایرانی " + type.getPersianName() + " در " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + " ساخته شد!");
        }
        return success;
    }

    /**
     * تولید تصادفی در چانک جدید - با شانس کم
     */
    public void tryGenerateInChunk(Chunk chunk) {
        if (!plugin.getConfigManager().getBoolean("dungeons.enabled", true)) return;

        double chance = plugin.getConfigManager().getDouble("dungeons.spawn-chance", 0.005); // 0.5%
        if (random.nextDouble() > chance) return;

        // پیدا کردن بایوم مناسب در چانک
        Location center = new Location(chunk.getWorld(), chunk.getX() * 16 + 8, 0, chunk.getZ() * 16 + 8);
        center.setY(chunk.getWorld().getHighestBlockYAt(center));

        Biome biome = center.getBlock().getBiome();
        DungeonType type = DungeonType.getForBiome(biome);

        if (type == null) {
            // اگر بایوم دقیق نبود، یکی تصادفی بر اساس محیط جهان
            if (chunk.getWorld().getEnvironment() == World.Environment.NETHER) {
                type = DungeonType.DAKHMEH_ZARTOSHTI;
            } else if (chunk.getWorld().getEnvironment() == World.Environment.THE_END) {
                type = DungeonType.TAKHT_JAMSHID_SKY;
            } else {
                // در اورورلد بر اساس بایوم نزدیک‌ترین را انتخاب کن
                if (biome.name().contains("DESERT") || biome.name().contains("MESA")) {
                    type = DungeonType.ARGE_BAM;
                } else if (biome.name().contains("FOREST") || biome.name().contains("JUNGLE")) {
                    type = DungeonType.ANAHITA_TEMPLE;
                } else if (biome.name().contains("EXTREME") || biome.name().contains("MOUNTAIN")) {
                    type = DungeonType.ALAMUT_CASTLE;
                } else if (biome.name().contains("ICE") || biome.name().contains("COLD")) {
                    type = DungeonType.GHALEH_BABAK;
                } else if (biome.name().contains("SWAMP") || biome.name().contains("RIVER")) {
                    type = DungeonType.CHOGHA_ZANBIL;
                } else if (biome.name().contains("OCEAN") || biome.name().contains("BEACH")) {
                    type = DungeonType.BANDAR_SIRAF;
                }
            }
        }

        if (type != null) {
            // تاخیر برای جلوگیری از لگ
            Location finalCenter = center;
            DungeonType finalType = type;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                generateDungeon(finalCenter, finalType);
            }, 20L);
        }
    }

    /**
     * ساخت دانجن نزدیک بازیکن (دستور ادمین)
     */
    public void generateNearPlayer(Player player, DungeonType type) {
        Location loc = player.getLocation();
        // 50 بلوک جلوتر
        Location target = loc.clone().add(loc.getDirection().multiply(50));
        target.setY(player.getWorld().getHighestBlockYAt(target));

        if (generateDungeon(target, type)) {
            player.sendMessage(MessageUtils.withPrefix("&aدانجن ایرانی &6" + type.getPersianName() + " &aدر نزدیکی شما ساخته شد!"));
            player.sendMessage(MessageUtils.withPrefix("&7مختصات: &f" + target.getBlockX() + ", " + target.getBlockY() + ", " + target.getBlockZ()));
            player.teleport(target.clone().add(0, 5, 0));
        } else {
            player.sendMessage(MessageUtils.withPrefix("&cساخت دانجن ناموفق بود! شاید خیلی نزدیک به دانجن دیگری است."));
        }
    }

    /**
     * لیست دانجن‌ها
     */
    public void listDungeons(Player player) {
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&6&l لیست دانجن‌های ایرانی تاریخی"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        for (DungeonType type : DungeonType.values()) {
            player.sendMessage(MessageUtils.color("&7- &6" + type.getPersianName() + " &7(" + type.getId() + ") &8- &f" + type.getDescription()));
            player.sendMessage(MessageUtils.color("  &7بایوم: &a" + type.getBiomes().size() + " بایوم &7| سختی: &c" + repeatStar(type.getDifficulty())));
        }
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&7تعداد ساخته شده: &a" + generatedDungeons.size()));
    }

    public Map<String, Location> getGeneratedDungeons() {
        return generatedDungeons;
    }

    public void clearAll() {
        generatedDungeons.clear();
        plugin.getConfigManager().getDungeonsConfig().set("generated-dungeons", null);
        plugin.getConfigManager().saveDungeonsConfig();
    }

    private String repeatStar(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("★");
        }
        return sb.toString();
    }
}

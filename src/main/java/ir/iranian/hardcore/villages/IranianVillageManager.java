package ir.iranian.hardcore.villages;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Random;

/**
 * تبدیل دهکده‌های وانیلا به روستاهای ایرانی
 * - نام‌های فارسی برای روستاییان
 * - معماری ایرانی: کاهگل، بادگیر، گنبد فیروزه‌ای
 * - کاملا فارسی
 */
public class IranianVillageManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    private final String[] persianNames = {
            "حاج کریم - کدخدای ده",
            "مشهدی حسن - کشاورز",
            "کربلایی تقی - نانوا",
            "اصغر آقا - آهنگر",
            "اکبر - چوپان",
            "فاطمه خانم - قالیباف",
            "زهرا - چای‌فروش",
            "احمد - بنای یزدی",
            "محمود - کاشی‌کار",
            "رضا - فرش‌فروش",
            "علی - عطار",
            "حسین - مسگر"
    };

    private final String[] villageNames = {
            "روستای کندوان - آذربایجان",
            "روستای میمند - کرمان",
            "روستای ابیانه - اصفهان",
            "روستای ماسوله - گیلان",
            "روستای پالنگان - کردستان",
            "دهکده چوبی نیشابور",
            "روستای قلعه‌نو - یزد",
            "روستای خور - اصفهان"
    };

    public IranianVillageManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;
        if (!plugin.getConfigManager().getBoolean("villages.enabled", true)) return;

        // فقط با شانس کم چک کن تا لگ نشود
        if (random.nextDouble() > 0.1) return;

        // آیا این چانک دهکده دارد؟ (بررسی وجود Villager)
        boolean hasVillage = false;
        for (org.bukkit.entity.Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof Villager) {
                hasVillage = true;
                break;
            }
        }

        // همچنین چک بلوک‌های دهکده (WOOD, COBBLESTONE زیاد)
        if (!hasVillage) {
            // ساده: اگر بایوم PLAINS یا DESERT باشد و چانک جدید، شانس تبدیل
            Biome biome = event.getWorld().getBlockAt(event.getChunk().getX() * 16, 64, event.getChunk().getZ() * 16).getBiome();
            if (biome == Biome.PLAINS || biome == Biome.DESERT || biome == Biome.SAVANNA) {
                if (random.nextDouble() < 0.02) {
                    hasVillage = true;
                }
            }
        }

        if (hasVillage) {
            // تبدیل روستاییان به ایرانی
            for (org.bukkit.entity.Entity entity : event.getChunk().getEntities()) {
                if (entity instanceof Villager) {
                    Villager villager = (Villager) entity;
                    String persianName = persianNames[random.nextInt(persianNames.length)];
                    villager.setCustomName(MessageUtils.color("&e" + persianName));
                    villager.setCustomNameVisible(true);
                    // حرفه تصادفی ایرانی
                    Villager.Profession[] professions = Villager.Profession.values();
                    villager.setProfession(professions[random.nextInt(professions.length)]);
                }
            }

            // پیام به بازیکنان نزدیک
            for (org.bukkit.entity.Player player : event.getWorld().getPlayers()) {
                if (player.getLocation().distance(new Location(event.getWorld(), event.getChunk().getX() * 16, 64, event.getChunk().getZ() * 16)) < 100) {
                    if (random.nextDouble() < 0.3) {
                        String villageName = villageNames[random.nextInt(villageNames.length)];
                        player.sendMessage(MessageUtils.withPrefix("&a&l🏘 روستای ایرانی کشف شد: &e" + villageName));
                        player.sendTitle(MessageUtils.color("&6" + villageName), MessageUtils.color("&7روستایی با معماری ایرانی - کاهگل و بادگیر"), 20, 60, 20);
                    }
                }
            }
        }
    }

    /**
     * ساخت روستای ایرانی کوچک در لوکیشن
     */
    public boolean buildIranianVillage(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        // 3 خانه کاهگلی
        buildKahgelHouse(world, ox - 8, oy, oz);
        buildKahgelHouse(world, ox + 8, oy, oz);
        buildKahgelHouse(world, ox, oy, oz + 8);

        // چاه آب وسط روستا
        world.getBlockAt(ox, oy, oz).setType(Material.WATER);
        world.getBlockAt(ox, oy - 1, oz).setType(Material.SANDSTONE);
        buildWalls(world, ox, oy, oz, 2, 1, Material.SANDSTONE);

        // مسجد کوچک روستا با گنبد فیروزه‌ای
        buildSmallMosque(world, ox, oy, oz - 10);

        // روستاییان
        for (int i = 0; i < 4; i++) {
            Location loc = new Location(world, ox + random.nextInt(16) - 8, oy + 1, oz + random.nextInt(16) - 8);
            Villager villager = (Villager) world.spawnEntity(loc, EntityType.VILLAGER);
            String name = persianNames[random.nextInt(persianNames.length)];
            villager.setCustomName(MessageUtils.color("&e" + name));
            villager.setCustomNameVisible(true);
        }

        return true;
    }

    private void buildKahgelHouse(World world, int ox, int oy, int oz) {
        // خانه کاهگلی 7x7
        for (int x = -3; x <= 3; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = -3; z <= 3; z++) {
                    if (Math.abs(x) == 3 || Math.abs(z) == 3 || y == 0 || y == 4) {
                        world.getBlockAt(ox + x, oy + y, oz + z).setType(Material.SANDSTONE);
                    }
                }
            }
        }
        // در
        world.getBlockAt(ox, oy + 1, oz - 3).setType(Material.AIR);
        world.getBlockAt(ox, oy + 2, oz - 3).setType(Material.AIR);
        // فرش داخل
        world.getBlockAt(ox, oy, oz).setType(Material.CARPET);
        try {
            world.getBlockAt(ox, oy, oz).setData((byte) 14);
        } catch (Exception ignored) {}
    }

    private void buildSmallMosque(World world, int ox, int oy, int oz) {
        // مسجد کوچک 9x9
        for (int x = -4; x <= 4; x++) {
            for (int y = 0; y < 6; y++) {
                for (int z = -4; z <= 4; z++) {
                    if (Math.abs(x) == 4 || Math.abs(z) == 4 || y == 0) {
                        world.getBlockAt(ox + x, oy + y, oz + z).setType(Material.SMOOTH_BRICK);
                    }
                }
            }
        }
        // گنبد فیروزه‌ای
        for (int y = 0; y < 4; y++) {
            int r = 4 - y;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    if (x * x + z * z <= r * r && x * x + z * z >= (r - 1) * (r - 1)) {
                        world.getBlockAt(ox + x, oy + 6 + y, oz + z).setType(Material.WOOL);
                        try {
                            world.getBlockAt(ox + x, oy + 6 + y, oz + z).setData((byte) 9); // فیروزه‌ای
                        } catch (Exception ignored) {}
                    }
                }
            }
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
}

package ir.iranian.hardcore.hardcore;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

/**
 * مدیریت مکانیک‌های هاردکور کلی
 */
public class HardcoreManager {

    private final IranianHardcorePlugin plugin;

    public HardcoreManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * اعمال تنظیمات اولیه هاردکور به تمام جهان‌ها
     */
    public void applyWorldSettings() {
        if (!plugin.getConfigManager().getBoolean("hardcore.health.disable-natural-regen", true)) return;

        for (World world : Bukkit.getWorlds()) {
            try {
                // در 1.12 از gamerule استفاده می‌کنیم
                world.setGameRuleValue("naturalRegeneration", "false");
                world.setGameRuleValue("doFireTick", "true");
                plugin.getLogger().info("تنظیمات هاردکور برای جهان " + world.getName() + " اعمال شد.");
            } catch (Exception e) {
                plugin.getLogger().warning("خطا در تنظیم gamerule برای " + world.getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * تنظیم جان بازیکن به 10 (5 قلب)
     */
    public void applyMaxHealth(Player player) {
        if (!plugin.getConfigManager().getBoolean("hardcore.health.enabled", true)) return;
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        double maxHealth = plugin.getConfigManager().getDouble("hardcore.health.max-health", 10.0);

        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
                    if (player.getHealth() > maxHealth) {
                        player.setHealth(maxHealth);
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("خطا در تنظیم جان بازیکن " + player.getName() + ": " + e.getMessage());
            }
        });
    }

    /**
     * آیا هاردکور فعال است؟
     */
    public boolean isEnabled() {
        return plugin.getConfigManager().getBoolean("hardcore.enabled", true);
    }

    /**
     * شروع تسک‌های دوره‌ای هاردکور
     */
    public void startTasks() {
        // تسک گرسنگی سریع‌تر
        if (plugin.getConfigManager().getBoolean("hardcore.health.faster-hunger", true)) {
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("iranianhardcore.bypass.hardcore")) continue;
                    if (player.getGameMode().name().equals("CREATIVE") || player.getGameMode().name().equals("SPECTATOR")) continue;

                    // افزایش exhaustion برای گرسنگی سریع‌تر
                    // هر 30 ثانیه کمی گرسنگی کم کن اگر در حال حرکت است
                    if (player.isSprinting() || player.getLocation().getBlock().getType().name().contains("WATER")) {
                        // شبیه‌سازی: اگر غذا بالای 6 بود، کمی کم کن
                        if (player.getFoodLevel() > 0 && Math.random() < 0.15) {
                            // در 1.12 setExhaustion وجود دارد
                            player.setExhaustion(player.getExhaustion() + 2.0f);
                        }
                    }
                }
            }, 100L, 100L); // هر 5 ثانیه
        }

        // تسک خطرناک‌تر کردن ندر و اند
        if (plugin.getConfigManager().getBoolean("hardcore.survival.nether.more-dangerous", true) ||
                plugin.getConfigManager().getBoolean("hardcore.survival.end.more-dangerous", true)) {

            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("iranianhardcore.bypass.hardcore")) continue;
                    handleEnvironmentDanger(player);
                }
            }, 60L, 60L);
        }
    }

    private void handleEnvironmentDanger(Player player) {
        World.Environment env = player.getWorld().getEnvironment();

        if (env == World.Environment.NETHER) {
            double chance = plugin.getConfigManager().getDouble("hardcore.survival.nether.chance", 0.05);
            if (Math.random() < chance) {
                // افکت‌های ندر
                // از کانفیگ بخوان
                // برای سادگی: با احتمال کم Wither بده
                if (Math.random() < 0.5) {
                    player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                            org.bukkit.potion.PotionEffectType.WITHER, 100, 0, false, true));
                }
                // گرمای ندر
                if (player.getFireTicks() == 0 && Math.random() < 0.1) {
                    player.setFireTicks(40);
                }
            }
        } else if (env == World.Environment.THE_END) {
            double chance = plugin.getConfigManager().getDouble("hardcore.survival.end.chance", 0.03);
            if (Math.random() < chance) {
                // افکت‌های اند
                if (Math.random() < 0.5) {
                    player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                            org.bukkit.potion.PotionEffectType.LEVITATION, 60, 0, false, true));
                } else {
                    player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                            org.bukkit.potion.PotionEffectType.WEAKNESS, 100, 0, false, true));
                }
            }
            // Void damage بیشتر در Event جداگانه هندل می‌شود
        }
    }
}

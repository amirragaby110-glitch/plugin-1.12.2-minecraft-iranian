package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * لیسنر برای محدودیت‌های بقا
 * - خواب فقط در شب و با احتیاط
 * - تخت هر 3 روز
 * - آب و گدازه سخت‌تر
 */
public class SurvivalListener implements Listener {

    private final IranianHardcorePlugin plugin;

    public SurvivalListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.survival.bed.enabled", true)) return;

        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.bedcooldown")) return;

        World world = player.getWorld();
        long time = world.getTime();

        // فقط در شب
        if (plugin.getConfigManager().getBoolean("hardcore.survival.bed.only-at-night", true)) {
            // شب: 12541 تا 23458 تقریباً
            if (time < 12541 || time > 23458) {
                // روز است - پیام بده
                player.sendMessage(MessageUtils.withPrefix(plugin.getConfigManager().getString("messages.bed-only-night", "&cفقط در شب می‌توانید بخوابید!")));
            }
        }

        // چک کولدان تخت: هر 3 روز
        int cooldownDays = plugin.getConfigManager().getInt("hardcore.survival.bed.cooldown-days", 3);
        long lastUse = plugin.getConfigManager().getBedCooldown(player.getUniqueId());
        long now = world.getFullTime();
        long cooldownTicks = cooldownDays * 24000L;

        if (lastUse != 0 && (now - lastUse) < cooldownTicks) {
            long remainingTicks = cooldownTicks - (now - lastUse);
            long remainingDays = (remainingTicks / 24000) + 1;

            String msg = plugin.getConfigManager().getString("hardcore.survival.bed.cooldown-message", "&cباید %days% روز دیگر صبر کنید.");
            msg = msg.replace("%days%", String.valueOf(remainingDays));
            player.sendMessage(MessageUtils.withPrefix(msg));

            // جلوگیری از خواب با خارج کردن از تخت؟ در 1.12 BedEnterEvent قابل کنسل نیست مستقیم
            // اما می‌توانیم با تاخیر بازیکن را از تخت خارج کنیم
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isSleeping()) {
                    // wakeup not in 1.12.2 - removed
                    // player.setSleepingIgnored(true);
                }
            }, 1L);

            event.setCancelled(true);
            return;
        }

        // چک امنیت محیط (هیولا نزدیک نباشد)
        // این توسط خود بازی چک می‌شود، اما ما پیام اضافه می‌دهیم
        double nearbyMonsters = world.getNearbyEntities(player.getLocation(), 8, 5, 8).stream()
                .filter(e -> e instanceof org.bukkit.entity.Monster).count();
        if (nearbyMonsters > 0) {
            player.sendMessage(MessageUtils.withPrefix(plugin.getConfigManager().getString("messages.bed-not-safe", "&cمحیط امن نیست!")));
        }
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.survival.bed.enabled", true)) return;

        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.bedcooldown")) {
            // حتی با بای‌پس هم هیل کامل نده
        }

        // خواب فقط کمی جان پر کند، نه کامل
        double healAmount = plugin.getConfigManager().getDouble("hardcore.survival.bed.heal-amount", 4.0);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            // فقط 2 قلب (4 جان) هیل بده، نه کامل
            double current = player.getHealth();
            double max = player.getMaxHealth();
            double newHealth = Math.min(max, current + healAmount);
            // چون بازیکن از تخت بلند شده، جانش ممکن است قبلاً پر شده باشد توسط وانیلا
            // پس ما کم می‌کنیم تا فقط healAmount اضافه شده باشد
            // ساده‌تر: اگر وانیلا فول هیل کرده، ما برمی‌گردانیم به current + healAmount
            // اما برای سادگی: ست می‌کنیم به newHealth اگر کمتر از مکس وانیلا بود
            // در واقع وانیلا بعد از خواب جان را فول می‌کند، پس ما دوباره کم می‌کنیم
            if (player.getHealth() > newHealth) {
                player.setHealth(newHealth);
                player.sendMessage(MessageUtils.withPrefix("&eخواب سبک بود... فقط &c" + (healAmount/2) + " قلب &eبهبود یافتی!"));
            } else {
                // اگر جانش کمتر بود، هیل بده
                player.setHealth(Math.min(max, player.getHealth() + healAmount));
            }

            // ثبت کولدان
            if (!player.hasPermission("iranianhardcore.bypass.bedcooldown")) {
                long now = player.getWorld().getFullTime();
                plugin.getConfigManager().setBedCooldown(player.getUniqueId(), now);
            }

            // کمی گرسنگی هم پر کن اما نه کامل
            int food = player.getFoodLevel();
            player.setFoodLevel(Math.min(20, food + 4));

        }, 5L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        // آب سخت‌تر
        if (plugin.getConfigManager().getBoolean("hardcore.survival.water-lava.harder-water", true)) {
            Material block = player.getLocation().getBlock().getType();
            Material eyeBlock = player.getEyeLocation().getBlock().getType();

            if (block == Material.WATER || block == Material.STATIONARY_WATER ||
                    eyeBlock == Material.WATER || eyeBlock == Material.STATIONARY_WATER) {

                // کندی در آب برای همه (مگر Oceanborn که بعداً باف می‌گیرد)
                // فقط اگر نژاد Oceanborn نباشد یا در بایوم خانه نباشد
                // اینجا فقط افکت کلی آب سخت‌تر
                if (player.getRemainingAir() < player.getMaximumAir()) {
                    // هوا سریع‌تر کم شود
                    if (Math.random() < 0.2) {
                        player.setRemainingAir(player.getRemainingAir() - 1);
                    }
                }

                // اگر مدت زیادی در آب باشد، خستگی
                if (Math.random() < 0.02) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0, false, false));
                }
            }
        }

        // لاوا سخت‌تر - قبلاً در MobHardcoreListener هندل شد، اما اینجا هم چک
        if (plugin.getConfigManager().getBoolean("hardcore.survival.water-lava.harder-lava", true)) {
            Material block = player.getLocation().getBlock().getType();
            if (block == Material.LAVA || block == Material.STATIONARY_LAVA) {
                if (Math.random() < 0.1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 0));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onWaterFlow(BlockFromToEvent event) {
        // آب و گدازه رفتار سخت‌تر - برای سادگی، جریان آب را کندتر کنیم؟
        // در 1.12 می‌توان جریان را لغو کرد تا سخت‌تر شود
        // اما برای جلوگیری از لگ، فقط در صورتی که کانفیگ فعال باشد و تصادفی

        if (!plugin.getConfigManager().getBoolean("hardcore.survival.water-lava.harder-water", true) &&
                !plugin.getConfigManager().getBoolean("hardcore.survival.water-lava.harder-lava", true)) return;

        Material type = event.getBlock().getType();
        if (type == Material.WATER || type == Material.STATIONARY_WATER) {
            // 10% شانس لغو جریان آب برای سخت‌تر شدن
            if (Math.random() < 0.1) {
                event.setCancelled(true);
            }
        }
    }
}

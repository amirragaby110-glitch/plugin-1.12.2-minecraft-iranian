package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.race.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * لیسنر برای اعمال قدرت‌های نژاد بر اساس بایوم
 * هر 2 ثانیه چک می‌کند و افکت مناسب می‌دهد
 */
public class RaceListener implements Listener {

    private final IranianHardcorePlugin plugin;

    public RaceListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        startBiomeCheckTask();
    }

    private void startBiomeCheckTask() {
        int interval = plugin.getConfigManager().getInt("race.biome-effects.check-interval-ticks", 40);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!plugin.getConfigManager().getBoolean("race.biome-effects.enabled", true)) return;
            if (!plugin.getConfigManager().getBoolean("race.enabled", true)) return;

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("iranianhardcore.bypass.hardcore")) continue;
                RaceType race = plugin.getRaceManager().getRace(player);
                if (race == null) continue;

                Biome currentBiome = player.getLocation().getBlock().getBiome();
                applyRaceEffects(player, race, currentBiome);
            }
        }, interval, interval);
    }

    private void applyRaceEffects(Player player, RaceType race, Biome biome) {
        boolean isHome = race.isHomeBiome(biome);
        boolean isHostile = race.isHostileBiome(biome);
        World.Environment env = player.getWorld().getEnvironment();

        // پاک کردن افکت‌های ضعیف قبلی؟ نه، فقط اضافه کن با مدت کوتاه

        switch (race) {
            case MOUNTAINBORN:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                }
                // کندی در آب
                if (isInWater(player)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0, false, false), true);
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0, false, false), true);
                }
                break;

            case FORESTBORN:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                    // اگر نزدیک برگ باشد، نامرئی نسبی
                    if (isNearLeaves(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, false, false), true);
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0, false, false), true);
                }
                break;

            case DESERTBORN:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, false, false), true);
                    // گرسنگی کمتر: اگر غذا کم است، کمی پر کن
                    if (player.getFoodLevel() < 20 && Math.random() < 0.1) {
                        player.setSaturation(Math.min(20, player.getSaturation() + 0.5f));
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                }
                break;

            case OCEANBORN:
                if (isHome || env == World.Environment.NORMAL && isInWater(player)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 0, false, false), true);
                    if (isInWater(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, false), true);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                        // نفس طولانی: هوا را پر نگه دار
                        if (player.getRemainingAir() < player.getMaximumAir() - 20) {
                            player.setRemainingAir(player.getRemainingAir() + 10);
                        }
                    }
                } else {
                    // خشکی = ضعف
                    if (!isInWater(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                        if (Math.random() < 0.05) {
                            player.setFoodLevel(Math.max(0, player.getFoodLevel() - 1));
                        }
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0, false, false), true);
                }
                break;

            case FROSTBORN:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, false, false), true); // مقاومت به سرما به صورت مقاومت به آتش شبیه‌سازی
                    // در برف گرسنگی کمتر
                    if (player.getFoodLevel() < 20 && Math.random() < 0.05) {
                        player.setSaturation(player.getSaturation() + 0.2f);
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                    // در بیابان آتش بگیر
                    if (env == World.Environment.NORMAL && biome.name().contains("DESERT") && Math.random() < 0.02) {
                        player.setFireTicks(40);
                    }
                }
                break;

            case SWAMPBORN:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    // مقاومت به مسمومیت در مرداب - در Event جداگانه
                }
                if (isHostile) {
                    // ضعف در برابر آتش
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                    if (player.getFireTicks() > 0) {
                        player.setFireTicks(player.getFireTicks() + 20);
                    }
                }
                break;

            case NETHERBORN:
                if (env == World.Environment.NETHER || isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                }
                if (isHostile || isInWater(player)) {
                    // ضعف شدید در آب
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 0, false, false), true);
                    if (isInWater(player)) {
                        player.damage(1.0);
                    }
                }
                break;

            case ENDBORN:
                if (env == World.Environment.THE_END || isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                }
                // ضعف در نور خورشید
                if (env == World.Environment.NORMAL) {
                    long time = player.getWorld().getTime();
                    boolean isDay = time < 12300 || time > 23850;
                    if (isDay && player.getWorld().getHighestBlockYAt(player.getLocation()) <= player.getLocation().getBlockY()) {
                        // در معرض آفتاب
                        if (player.getLocation().getBlock().getLightFromSky() > 10) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                            if (Math.random() < 0.02) {
                                player.setFireTicks(20);
                            }
                        }
                    }
                }
                break;
        }
    }

    private boolean isInWater(Player player) {
        Material type = player.getLocation().getBlock().getType();
        Material eye = player.getEyeLocation().getBlock().getType();
        return type == Material.WATER || type == Material.STATIONARY_WATER ||
                eye == Material.WATER || eye == Material.STATIONARY_WATER;
    }

    private boolean isNearLeaves(Player player) {
        int radius = 3;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -1; y <= 2; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Material m = player.getLocation().clone().add(x, y, z).getBlock().getType();
                    if (m == Material.LEAVES || m == Material.LEAVES_2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        Player player = (Player) event.getEntity();
        RaceType race = plugin.getRaceManager().getRace(player);
        if (race == null) return;

        if (race == RaceType.MOUNTAINBORN || race == RaceType.ENDBORN) {
            // 70% کاهش دمیج سقوط
            event.setDamage(event.getDamage() * 0.3);
        }

        // اگر در بایوم خانه کوهستان باشد، کاهش بیشتر
        if (race == RaceType.MOUNTAINBORN && race.isHomeBiome(player.getLocation().getBlock().getBiome())) {
            event.setDamage(event.getDamage() * 0.5);
            if (event.getDamage() < 2.0) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMoveForRace(PlayerMoveEvent event) {
        // افکت‌های فوری هنگام حرکت
        Player player = event.getPlayer();
        RaceType race = plugin.getRaceManager().getRace(player);
        if (race == null) return;

        // Oceanborn شنا سریع‌تر
        if (race == RaceType.OCEANBORN && isInWater(player)) {
            // افزایش سرعت شنا با Velocity
            if (player.isSprinting()) {
                if (Math.random() < 0.3) {
                    player.setVelocity(player.getLocation().getDirection().multiply(0.1));
                }
            }
        }

        // Netherborn در آب دمیج ببیند
        if (race == RaceType.NETHERBORN && isInWater(player)) {
            if (Math.random() < 0.1) {
                player.damage(0.5);
            }
        }
    }

    // برای Swampborn مقاومت به Poison
    @EventHandler
    public void onPoisonDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        RaceType race = plugin.getRaceManager().getRace(player);
        if (race == null) return;

        if (race == RaceType.SWAMPBORN) {
            Biome biome = player.getLocation().getBlock().getBiome();
            if (race.isHomeBiome(biome)) {
                // در مرداب به Poison مقاوم است
                if (player.hasPotionEffect(PotionEffectType.POISON)) {
                    player.removePotionEffect(PotionEffectType.POISON);
                    event.setCancelled(true);
                }
            }
        }
    }
}

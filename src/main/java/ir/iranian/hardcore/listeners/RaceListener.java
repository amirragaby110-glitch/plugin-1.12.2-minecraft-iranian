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
 * لیسنر اقوام ایرانی - نسخه 3.5 - هر بایوم یک قوم
 * هر قوم ایرانی قدرت مخصوص در بایوم خانه خود دارد
 * کاملا فارسی - بر اساس فرهنگ واقعی اقوام ایران
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

        switch (race) {
            // ========== قوم پارس - فارس ==========
            case FARS:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true); // معماری هخامنشی
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, false, false), true); // قدرت شاهنشاهی
                    if (player.getFoodLevel() < 20 && Math.random() < 0.1) {
                        player.setSaturation(Math.min(20, player.getSaturation() + 0.3f)); // کشاورزی فارس
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                }
                break;

            // ========== قوم آذری - آذربایجان ==========
            case AZARI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, false, false), true); // مقاومت سرما
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true); // سرعت در برف
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, false, false), true); // مقاومت به سرما
                    if (player.getFoodLevel() < 20 && Math.random() < 0.05) {
                        player.setSaturation(player.getSaturation() + 0.2f);
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    if (env == World.Environment.NORMAL && biome.name().contains("DESERT") && Math.random() < 0.02) {
                        player.setFireTicks(40);
                    }
                }
                break;

            // ========== قوم کرد - زاگرس ==========
            case KURD:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 0, false, false), true); // پرش زاگرس
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0, false, false), true); // قدرت جنگاوری
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                }
                if (isInWater(player)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0, false, false), true);
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0, false, false), true);
                }
                break;

            // ========== قوم لر - لرستان ==========
            case LOR:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0, false, false), true); // قدرت بدنی
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    if (isNearLeaves(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                }
                break;

            // ========== قوم بلوچ - بلوچستان ==========
            case BALOCH:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true); // سرعت در شن
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, false, false), true); // مقاومت گرما
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 0, false, false), true); // دید در شب کویر
                    if (player.getFoodLevel() < 20 && Math.random() < 0.1) {
                        player.setSaturation(Math.min(20, player.getSaturation() + 0.5f)); // مقاومت تشنگی
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                }
                break;

            // ========== قوم عرب خوزستان ==========
            case ARAB_KHUZESTAN:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, false, false), true); // گرمای خوزستان
                    if (isInWater(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                }
                break;

            // ========== قوم ترکمن ==========
            case TURKMEN:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, false), true); // سوارکاری - سرعت بالا
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 1, false, false), true); // پرش اسب
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0, false, false), true);
                }
                break;

            // ========== قوم گیلک ==========
            case GILAK:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0, false, false), true); // رطوبت گیلان
                    if (isNearLeaves(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, false, false), true); // جنگل هیرکانی
                    }
                    if (player.getFoodLevel() < 20 && Math.random() < 0.1) {
                        player.setSaturation(player.getSaturation() + 0.3f); // برنج گیلان
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0, false, false), true);
                }
                break;

            // ========== قوم مازنی ==========
            case MAZANI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true); // کشاورزی
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0, false, false), true); // دریای خزر
                    if (isInWater(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 0, false, false), true);
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0, false, false), true);
                }
                break;

            // ========== قوم بختیاری ==========
            case BAKHTIARI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 1, false, false), true); // کوچ‌نشینی
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0, false, false), true);
                }
                if (isInWater(player)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 0, false, false), true);
                }
                break;

            // ========== قوم قشقایی ==========
            case QASHQAYI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, false), true); // کوچ سریع
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 0, false, false), true);
                    if (player.getFoodLevel() < 20 && Math.random() < 0.1) {
                        player.setSaturation(player.getSaturation() + 0.4f); // دامداری
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                }
                break;

            // ========== قوم بندری ==========
            case BANDARI:
                if (isHome || (env == World.Environment.NORMAL && isInWater(player))) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0, false, false), true); // 3 برابر نفس
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 0, false, false), true);
                    if (isInWater(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, false), true); // شنای سریع
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                        if (player.getRemainingAir() < player.getMaximumAir() - 20) {
                            player.setRemainingAir(player.getRemainingAir() + 10); // نفس طولانی
                        }
                    }
                } else {
                    if (!isInWater(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0, false, false), true);
                }
                break;

            // ========== قوم خراسانی ==========
            case KHORASANI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 200, 0, false, false), true); // فرهنگ و ادب
                    if (player.getFoodLevel() < 20 && Math.random() < 0.08) {
                        player.setSaturation(player.getSaturation() + 0.3f); // زعفران
                    }
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                }
                break;

            // ========== قوم سیستانی ==========
            case SISTANI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1, false, false), true); // قدرت رستم
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, false, false), true); // کویر
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, false, false), true);
                }
                if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false, false), true);
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

        // اقوام کوهستانی مقاومت سقوط دارند
        if (race == RaceType.KURD || race == RaceType.BAKHTIARI || race == RaceType.LOR || race == RaceType.QASHQAYI) {
            event.setDamage(event.getDamage() * 0.3); // 70% کاهش
            if (race.isHomeBiome(player.getLocation().getBlock().getBiome())) {
                event.setDamage(event.getDamage() * 0.5);
                if (event.getDamage() < 2.0) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMoveForRace(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        RaceType race = plugin.getRaceManager().getRace(player);
        if (race == null) return;

        // بندری شنا سریع‌تر
        if (race == RaceType.BANDARI && isInWater(player)) {
            if (player.isSprinting()) {
                if (Math.random() < 0.3) {
                    player.setVelocity(player.getLocation().getDirection().multiply(0.12));
                }
            }
        }

        // آذری در آب گرم دمیج نبیند اما در آب سرد قدرتمند
        if (race == RaceType.AZARI && isInWater(player) && player.getLocation().getBlock().getBiome().name().contains("COLD")) {
            if (player.getRemainingAir() < player.getMaximumAir() - 10) {
                player.setRemainingAir(player.getRemainingAir() + 5);
            }
        }
    }

    @EventHandler
    public void onPoisonDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        RaceType race = plugin.getRaceManager().getRace(player);
        if (race == null) return;

        // عرب خوزستان و گیلک مقاومت به رطوبت و مسمومیت
        if (race == RaceType.ARAB_KHUZESTAN || race == RaceType.GILAK) {
            Biome biome = player.getLocation().getBlock().getBiome();
            if (race.isHomeBiome(biome)) {
                if (player.hasPotionEffect(PotionEffectType.POISON)) {
                    player.removePotionEffect(PotionEffectType.POISON);
                    event.setCancelled(true);
                }
            }
        }
    }
}

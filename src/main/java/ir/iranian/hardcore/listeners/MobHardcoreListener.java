package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * لیسنر برای سختی ماب‌ها
 * - جان 2 برابر
 * - دمیج 1.5 برابر
 * - اسپاون 2 برابر در شب
 * - زامبی در شکن
 * - کریپر قوی‌تر
 * - عنکبوت مسموم
 * - اسکلت دقیق‌تر
 */
public class MobHardcoreListener implements Listener {

    private final IranianHardcorePlugin plugin;

    public MobHardcoreListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.mobs.enabled", true)) return;

        LivingEntity entity = event.getEntity();

        // فقط ماب‌های دشمن
        if (!(entity instanceof Monster) && !(entity instanceof Slime) && !(entity instanceof Ghast) && !(entity instanceof Enderman)) {
            return;
        }

        // جان 2 برابر
        if (plugin.getConfigManager().getBoolean("hardcore.mobs.double-health", true)) {
            try {
                double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth * 2.0);
                entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            } catch (Exception ignored) {}
        }

        // دمیج 1.5 برابر
        if (plugin.getConfigManager().getBoolean("hardcore.mobs.damage-multiplier", true)) {
            try {
                double mult = plugin.getConfigManager().getDouble("hardcore.mobs.damage-multiplier", 1.5);
                if (entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                    double dmg = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
                    entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(dmg * mult);
                }
            } catch (Exception ignored) {}
        }

        // زامبی در شکن
        if (plugin.getConfigManager().getBoolean("hardcore.mobs.zombies-break-doors", true)) {
            if (entity instanceof Zombie) {
                ((Zombie) entity).setCanBreakDoors(true);
                // زامبی بچه و قوی‌تر
                ((Zombie) entity).setBaby(false);
            }
        }

        // اسپاون 2 برابر در شب
        if (plugin.getConfigManager().getBoolean("hardcore.mobs.double-night-spawn", true)) {
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                long time = entity.getWorld().getTime();
                // شب: 13000 تا 23000
                if (time > 13000 && time < 23000) {
                    if (Math.random() < 0.5) { // 50% شانس اسپاون اضافه
                        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                            try {
                                entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());
                            } catch (Exception ignored) {}
                        }, 5L);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.mobs.enabled", true)) return;

        // اسپایدر مسموم
        if (plugin.getConfigManager().getBoolean("hardcore.mobs.spiders-poison", true)) {
            if (event.getDamager() instanceof Spider || event.getDamager() instanceof CaveSpider) {
                if (event.getEntity() instanceof Player) {
                    Player player = (Player) event.getEntity();
                    if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

                    double chance = plugin.getConfigManager().getDouble("hardcore.mobs.spider-poison-chance", 0.4);
                    if (Math.random() < chance) {
                        int duration = (event.getDamager() instanceof CaveSpider) ? 200 : 100;
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, 0));
                    }
                }
            }
        }

        // دمیج ماب‌ها 1.5 برابر (برای مواقعی که Attribute کار نکرد)
        if (event.getDamager() instanceof Monster) {
            double mult = plugin.getConfigManager().getDouble("hardcore.mobs.damage-multiplier", 1.5);
            event.setDamage(event.getDamage() * mult);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.mobs.creeper-stronger-explosion", true)) return;
        if (!(event.getEntity() instanceof Creeper)) return;

        double mult = plugin.getConfigManager().getDouble("hardcore.mobs.creeper-explosion-multiplier", 1.8);
        // قدرت انفجار کریپر در 1.12 مستقیم قابل تنظیم نیست، اما می‌توانیم yield را بیشتر کنیم
        // yield = مقدار بلوک‌هایی که دراپ می‌شوند و قدرت تخریب
        float newYield = (float) (event.getYield() * mult);
        event.setYield(newYield);

        // همچنین می‌توانیم انفجار دوم ایجاد کنیم برای قوی‌تر شدن
        if (Math.random() < 0.3) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                event.getLocation().getWorld().createExplosion(event.getLocation(), (float) (2.0 * mult), false);
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSkeletonShoot(EntityShootBowEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.mobs.skeletons-more-accurate", true)) return;
        if (!(event.getEntity() instanceof Skeleton)) return;

        double mult = plugin.getConfigManager().getDouble("hardcore.mobs.skeleton-velocity-multiplier", 1.5);
        if (event.getProjectile() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getProjectile();
            // سرعت بیشتر = دقت بیشتر
            arrow.setVelocity(arrow.getVelocity().multiply(mult));
            // دمیج بیشتر
            arrow.setKnockbackStrength(arrow.getKnockbackStrength() + 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {
        // لاوا سخت‌تر
        if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;
                if (!plugin.getConfigManager().getBoolean("hardcore.survival.water-lava.harder-lava", true)) return;

                double mult = plugin.getConfigManager().getDouble("hardcore.survival.water-lava.lava-damage-multiplier", 1.5);
                event.setDamage(event.getDamage() * mult);

                // آتش بیشتر
                double fireMult = plugin.getConfigManager().getDouble("hardcore.survival.water-lava.lava-fire-ticks-multiplier", 2.0);
                player.setFireTicks((int) (player.getFireTicks() * fireMult) + 100);
            }
        }

        // Void damage بیشتر در اند
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (player.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
                    double mult = plugin.getConfigManager().getDouble("hardcore.survival.end.void-damage-multiplier", 2.0);
                    event.setDamage(event.getDamage() * mult);
                }
            }
        }
    }
}

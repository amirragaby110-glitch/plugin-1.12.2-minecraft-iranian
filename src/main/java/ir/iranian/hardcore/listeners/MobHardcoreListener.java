package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Listener baraye sakhtie mobha - v5.0 Finglish
 */
public class MobHardcoreListener implements Listener {

    private final IranianHardcorePlugin plugin;

    public MobHardcoreListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.mobs.enabled", true)) return;

        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Monster)) return;

        // Double health
        if (plugin.getConfigManager().getBoolean("hardcore.mobs.double-health", true)) {
            AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealth != null) {
                double newHealth = maxHealth.getBaseValue() * 2.0;
                maxHealth.setBaseValue(newHealth);
                entity.setHealth(newHealth);
            }
        }

        // Damage multiplier
        AttributeInstance attackDamage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamage != null) {
            double mult = plugin.getConfigManager().getDouble("hardcore.mobs.damage-multiplier", 1.5);
            attackDamage.setBaseValue(attackDamage.getBaseValue() * mult);
        }

        // Zombie baby chance
        if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;
            if (Math.random() < 0.15) {
                zombie.setBaby(true);
            }
        }

        // Double spawn at night
        if (plugin.getConfigManager().getBoolean("hardcore.mobs.double-night-spawn", true)) {
            long time = entity.getWorld().getTime();
            if (time >= 13000 && time <= 23000) {
                if (Math.random() < 0.5) {
                    if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                        entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;

        if (event.getDamager() instanceof Spider && event.getEntity() instanceof Player) {
            if (plugin.getConfigManager().getBoolean("hardcore.mobs.spiders-poison", true)) {
                double chance = plugin.getConfigManager().getDouble("hardcore.mobs.spider-poison-chance", 0.4);
                if (Math.random() < chance) {
                    Player player = (Player) event.getEntity();
                    if (!player.hasPermission("iranianhardcore.bypass.hardcore")) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
                    }
                }
            }
        }

        if (event.getDamager() instanceof Monster && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!player.hasPermission("iranianhardcore.bypass.hardcore")) {
                double mult = plugin.getConfigManager().getDouble("hardcore.mobs.damage-multiplier", 1.5);
                event.setDamage(event.getDamage() * mult);
            }
        }
    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.mobs.creeper-stronger-explosion", true)) return;

        if (event.getEntity() instanceof Creeper) {
            float mult = (float) plugin.getConfigManager().getDouble("hardcore.mobs.creeper-explosion-multiplier", 1.8);
            event.setYield(event.getYield() * mult);
            event.getLocation().getWorld().createExplosion(
                    event.getLocation().getX(),
                    event.getLocation().getY(),
                    event.getLocation().getZ(),
                    2.0f,
                    false,
                    true
            );
        }
    }

    @EventHandler
    public void onSkeletonShoot(CreatureSpawnEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.mobs.skeletons-more-accurate", true)) return;

        if (event.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            AttributeInstance speed = skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if (speed != null) {
                speed.setBaseValue(speed.getBaseValue() * 1.2);
            }
        }
    }

    @EventHandler
    public void onLavaDamage(EntityDamageEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.survival.water-lava.harder-lava", true)) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

            if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                double mult = plugin.getConfigManager().getDouble("hardcore.survival.water-lava.lava-damage-multiplier", 1.5);
                event.setDamage(event.getDamage() * mult);
                player.setFireTicks(player.getFireTicks() + 40);
            }

            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
                    double mult = plugin.getConfigManager().getDouble("hardcore.survival.end.void-damage-multiplier", 2.0);
                    event.setDamage(event.getDamage() * mult);
                }
            }
        }
    }
}

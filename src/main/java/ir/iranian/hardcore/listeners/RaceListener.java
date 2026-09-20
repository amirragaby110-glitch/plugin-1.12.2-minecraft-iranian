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
 * Listener Aghvame Irani - v5.1 Clean Combat Edition
 * Buffs in home biomes, gentle thematic challenges in hostile biomes.
 * ZERO WEAKNESS debuffs to ensure players can always damage mobs normally!
 */
public class RaceListener implements Listener {

    private final IranianHardcorePlugin plugin;

    public RaceListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        startBiomeCheckTask();
    }

    private void startBiomeCheckTask() {
        int interval = plugin.getConfigManager().getInt("race.biome-effects.check-interval-ticks", 60);

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
            case FARS:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0, false, false), true);
                    if (player.getFoodLevel() < 20 && Math.random() < 0.1) {
                        player.setSaturation(Math.min(20, player.getSaturation() + 0.3f));
                    }
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;

            case AZARI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120, 0, false, false), true);
                }
                break;

            case KURD:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;

            case LOR:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;

            case BALOCH:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;

            case ARAB_KHUZESTAN:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 240, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0, false, false), true);
                    if (isInWater(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false), true);
                    }
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;

            case TURKMEN:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 120, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;

            case GILAK:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 240, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120, 0, false, false), true);
                }
                break;

            case MAZANI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 240, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120, 0, false, false), true);
                }
                break;

            case BAKHTIARI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;

            case QASHQAYI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;

            case BANDARI:
                if (isInWater(player)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 240, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 120, 0, false, false), true);
                    if (player.getRemainingAir() < player.getMaximumAir() - 20) {
                        player.setRemainingAir(player.getRemainingAir() + 10);
                    }
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120, 0, false, false), true);
                }
                break;

            case KHORASANI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 240, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;

            case SISTANI:
                if (isHome) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0, false, false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false), true);
                } else if (isHostile) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, false, false), true);
                }
                break;
        }
    }

    private boolean isInWater(Player player) {
        Material type = player.getLocation().getBlock().getType();
        return type == Material.WATER || type == Material.STATIONARY_WATER;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        Player player = (Player) event.getEntity();
        RaceType race = plugin.getRaceManager().getRace(player);
        if (race == null) return;

        if (race == RaceType.KURD || race == RaceType.BAKHTIARI || race == RaceType.LOR || race == RaceType.QASHQAYI) {
            event.setDamage(event.getDamage() * 0.35);
            if (event.getDamage() < 1.5) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMoveForRace(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockY() == event.getTo().getBlockY() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        Player player = event.getPlayer();
        RaceType race = plugin.getRaceManager().getRace(player);
        if (race == null) return;

        if (race == RaceType.BANDARI && isInWater(player)) {
            if (player.isSprinting() && Math.random() < 0.25) {
                player.setVelocity(player.getLocation().getDirection().multiply(0.1));
            }
        }
    }

    @EventHandler
    public void onPoisonDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        RaceType race = plugin.getRaceManager().getRace(player);
        if (race == null) return;

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

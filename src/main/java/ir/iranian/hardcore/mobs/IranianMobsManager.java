package ir.iranian.hardcore.mobs;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.language.LanguageManager;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 * Iranian Mobs Manager - v4.0
 * Spawns custom Iranian mythical mobs
 * - Div Sepid, Div Siah, Simurgh, Zahhak, Rostam, Al, Kaveh
 * - Each with unique abilities
 */
public class IranianMobsManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public IranianMobsManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startMobSpawnTask();
    }

    private void startMobSpawnTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().getBoolean("mobs.custom.enabled", true)) return;
                for (World world : plugin.getServer().getWorlds()) {
                    if (world.getPlayers().isEmpty()) continue;
                    if (random.nextDouble() < 0.05) { // 5% chance every 2 minutes
                        trySpawnCustomMob(world);
                    }
                }
            }
        }.runTaskTimer(plugin, 2400L, 2400L); // Every 2 minutes
    }

    private void trySpawnCustomMob(World world) {
        if (world.getPlayers().isEmpty()) return;
        Player randomPlayer = world.getPlayers().get(random.nextInt(world.getPlayers().size()));
        Location loc = randomPlayer.getLocation().clone().add(
                random.nextInt(60) - 30,
                0,
                random.nextInt(60) - 30
        );
        loc.setY(world.getHighestBlockYAt(loc) + 1);

        // Check biome for appropriate mob
        CustomMobType type = getRandomMobForBiome(loc);
        if (type != null && random.nextDouble() < 0.3) {
            spawnCustomMob(loc, type);
        }
    }

    private CustomMobType getRandomMobForBiome(Location loc) {
        String biomeName = loc.getBlock().getBiome().name();
        if (biomeName.contains("DESERT") || biomeName.contains("MESA") || biomeName.contains("SAVANNA")) {
            // Desert mobs: Div Siah, Zahhak
            return random.nextBoolean() ? CustomMobType.DIV_SIAH : CustomMobType.ZAHHAK;
        } else if (biomeName.contains("ICE") || biomeName.contains("COLD") || biomeName.contains("TAIGA")) {
            // Cold mobs: Div Sepid
            return CustomMobType.DIV_SEPID;
        } else if (biomeName.contains("EXTREME_HILLS") || biomeName.contains("SMALLER")) {
            // Mountain mobs: Simurgh, Rostam
            return random.nextBoolean() ? CustomMobType.SIMURGH : CustomMobType.ROSTAM_GHOST;
        } else if (biomeName.contains("SWAMP") || biomeName.contains("JUNGLE")) {
            // Swamp mobs: Al
            return CustomMobType.AL;
        } else if (biomeName.contains("PLAINS") || biomeName.contains("FOREST")) {
            // Plains: Kaveh (friendly), Div
            if (random.nextDouble() < 0.1) {
                return CustomMobType.KAVEH;
            }
            return CustomMobType.DIV_SEPID;
        }
        // Default random
        CustomMobType[] values = CustomMobType.values();
        return values[random.nextInt(values.length)];
    }

    public LivingEntity spawnCustomMob(Location loc, CustomMobType type) {
        try {
            LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type.getBaseType());
            entity.setMetadata("iranian_mob", new FixedMetadataValue(plugin, type.getId()));
            entity.setMetadata("custom_mob_type", new FixedMetadataValue(plugin, type.name()));

            // Set custom name
            LanguageManager lang = plugin.getLanguageManager();
            String name = lang.getText(type.getPersianName(), type.getFinglishName());
            String displayName = "";
            switch (type) {
                case DIV_SEPID:
                    displayName = "&f&l☠ " + name + " &c&l" + (int)type.getHealth() + " HP";
                    break;
                case DIV_SIAH:
                    displayName = "&8&l☠ " + name;
                    break;
                case SIMURGH:
                    displayName = "&6&l🦅 " + name;
                    break;
                case ZAHHAK:
                    displayName = "&4&l🐍 " + name;
                    break;
                case ROSTAM_GHOST:
                    displayName = "&6&l⚔ " + name;
                    break;
                case AL:
                    displayName = "&5&l👹 " + name;
                    break;
                case KAVEH:
                    displayName = "&6&l🔨 " + name + " &a&l(Dost)";
                    break;
            }
            entity.setCustomName(MessageUtils.color(displayName));
            entity.setCustomNameVisible(true);

            // Set health
            if (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.getHealth());
                entity.setHealth(type.getHealth());
            }

            // Set damage
            if (entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(type.getDamage());
            }

            // Kaveh is friendly - no target
            if (type == CustomMobType.KAVEH) {
                // Make it not attack players
                if (entity instanceof org.bukkit.entity.Creature) {
                    ((org.bukkit.entity.Creature) entity).setTarget(null);
                }
            }

            // Broadcast spawn for rare mobs
            if (type == CustomMobType.ZAHHAK || type == CustomMobType.SIMURGH || type == CustomMobType.ROSTAM_GHOST) {
                String spawnMsg = lang.getText(
                        "&6&l" + type.getPersianName() + " &7در نزدیکی &f" + loc.getBlockX() + "," + loc.getBlockZ() + " &7ظاهر شد!",
                        "&6&l" + type.getFinglishName() + " &7dar nazdiki &f" + loc.getBlockX() + "," + loc.getBlockZ() + " &7zaher shod!"
                );
                for (Player p : loc.getWorld().getPlayers()) {
                    if (p.getLocation().distance(loc) < 100) {
                        p.sendMessage(MessageUtils.withPrefix(spawnMsg));
                    }
                }
            }

            plugin.getLogger().info("Spawned custom mob: " + type.getId() + " at " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
            return entity;
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to spawn custom mob " + type.getId() + ": " + e.getMessage());
            return null;
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!plugin.getConfigManager().getBoolean("mobs.custom.replace-natural", false)) return;
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        if (random.nextDouble() < 0.08) { // 8% chance to replace natural spawn with Iranian mob
            Location loc = event.getLocation();
            CustomMobType type = getRandomMobForBiome(loc);
            if (type != null) {
                event.setCancelled(true);
                spawnCustomMob(loc, type);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof LivingEntity)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();
        if (!damager.hasMetadata("iranian_mob")) return;

        String mobId = damager.getMetadata("iranian_mob").get(0).asString();
        try {
            CustomMobType type = CustomMobType.valueOf(mobId);
            // Apply mob's effects to player
            for (CustomMobType.MobEffect effect : type.getEffects()) {
                if (random.nextDouble() < effect.getChance()) {
                    Player player = (Player) event.getEntity();
                    player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier()));
                }
            }
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.hasMetadata("iranian_mob")) return;

        String mobId = entity.getMetadata("iranian_mob").get(0).asString();
        try {
            CustomMobType type = CustomMobType.valueOf(mobId);
            // Custom drops
            event.getDrops().clear();
            for (Material mat : type.getDrops()) {
                event.getDrops().add(new ItemStack(mat, random.nextInt(3) + 1));
            }
            // Extra gold for all Iranian mobs
            event.getDrops().add(new ItemStack(Material.GOLD_NUGGET, random.nextInt(5) + 2));
            if (random.nextDouble() < 0.2) {
                event.getDrops().add(new ItemStack(Material.GOLD_INGOT, 1));
            }

            // Special drops
            if (type == CustomMobType.ZAHHAK) {
                event.getDrops().add(new ItemStack(Material.DIAMOND, random.nextInt(2) + 1));
                event.setDroppedExp(50);
            } else if (type == CustomMobType.SIMURGH) {
                event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 1));
                event.setDroppedExp(30);
            } else if (type == CustomMobType.ROSTAM_GHOST) {
                event.getDrops().add(new ItemStack(Material.DIAMOND_SWORD, 1));
                event.setDroppedExp(40);
            }

            // Message
            if (entity.getKiller() != null) {
                Player killer = entity.getKiller();
                LanguageManager lang = plugin.getLanguageManager();
                String msg = lang.getText(
                        "&a&l" + type.getPersianName() + " &7را کشتید! &6زنده باد ایران!",
                        "&a&l" + type.getFinglishName() + " &7ra koshtid! &6Zende bad Iran!"
                );
                killer.sendMessage(MessageUtils.withPrefix(msg));
            }

        } catch (Exception ignored) {}
    }

    public void spawnMobCommand(Player player, String mobId) {
        try {
            CustomMobType type = CustomMobType.valueOf(mobId.toUpperCase());
            Location loc = player.getLocation().clone().add(player.getLocation().getDirection().multiply(3));
            LivingEntity entity = spawnCustomMob(loc, type);
            if (entity != null) {
                LanguageManager lang = plugin.getLanguageManager();
                String msg = lang.getText(
                        "&a" + type.getPersianName() + " ساخته شد!",
                        "&a" + type.getFinglishName() + " sakhte shod!"
                );
                player.sendMessage(MessageUtils.withPrefix(msg));
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(MessageUtils.withPrefix("&cMob not found! Available: DIV_SEPID, DIV_SIAH, SIMURGH, ZAHHAK, ROSTAM_GHOST, AL, KAVEH"));
        }
    }
}

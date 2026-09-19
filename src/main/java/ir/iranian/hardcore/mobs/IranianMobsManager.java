package ir.iranian.hardcore.mobs;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.swords.PersianSwordType;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Iranian Mobs & Speaking Mobs Manager (v5.0 Finglish)
 * - 30 Custom Mythical & Historical Iranian Mobs & Bosses
 * - ALL mobs in the game speak Finglish on attack, spawn, and death!
 * - Complete Finglish, zero Persian Unicode characters.
 */
public class IranianMobsManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();
    private final Map<UUID, Long> mobSpeechCooldown = new HashMap<>();

    // Dialogue pools for vanilla monsters speaking Finglish
    private final String[] zombieLines = {
            "Ghoosht-e tazeh! Migiramet!",
            "Maghz... Maghz-e shoma ro mikhoram!",
            "Man yek sarbaz-e ghadimi hastam ke az ghoor barkhasteham!",
            "Az dast-e man farar nemitooni bokoni!"
    };

    private final String[] skeletonLines = {
            "Tir-e kaman-e man hargez khata nemire!",
            "Oskelat-e bi-rahm be shoma hamleh mikone!",
            "Kaman-e Damavand dar dast-e man ast!",
            "Ghalb-e to ro ba tir neshaneh migiram!"
    };

    private final String[] creeperLines = {
            "Fssss... Ghorboon-e sar-e to beram... Boom!",
            "Fadaie-ye bi-baak dar hale enfejar!",
            "Man miyam jelo ta booooom konam!"
    };

    private final String[] spiderLines = {
            "Heeesss! Dar toor-e ankaboot-e marg gereftar shodi!",
            "Zahr-e man to ro az pa dar miare!"
    };

    private final String[] endermanLines = {
            "Cheshm dar cheshm-e man nandaz agar joonet ro doost dari!",
            "Az sayeh-haye tarik-e Parseh biroon amadam!"
    };

    private final String[] witchLines = {
            "Jooshandeh-ye margbar baratoon amadeh kardam!",
            "Jadoo-ye Al bar sar-e to forood miayad!"
    };

    public IranianMobsManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startMobSpawnTask();
        plugin.getLogger().info("IranianMobsManager: 30 Iranian mobs & speaking mobs system initialized!");
    }

    private void startMobSpawnTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().getBoolean("mobs.custom.enabled", true)) return;
                for (World world : plugin.getServer().getWorlds()) {
                    if (world.getPlayers().isEmpty()) continue;
                    if (random.nextDouble() < 0.08) {
                        trySpawnCustomMob(world);
                    }
                }
            }
        }.runTaskTimer(plugin, 1200L, 1200L); // Every 60 seconds
    }

    private void trySpawnCustomMob(World world) {
        if (world.getPlayers().isEmpty()) return;
        Player randomPlayer = world.getPlayers().get(random.nextInt(world.getPlayers().size()));
        Location loc = randomPlayer.getLocation().clone().add(
                random.nextInt(50) - 25,
                0,
                random.nextInt(50) - 25
        );
        loc.setY(world.getHighestBlockYAt(loc) + 1);

        CustomMobType type = getRandomMobForBiome(loc);
        if (type != null && random.nextDouble() < 0.40) {
            spawnCustomMob(loc, type);
        }
    }

    private CustomMobType getRandomMobForBiome(Location loc) {
        String biomeName = loc.getBlock().getBiome().name();
        if (biomeName.contains("DESERT") || biomeName.contains("MESA") || biomeName.contains("SAVANNA")) {
            CustomMobType[] desertMobs = {CustomMobType.DIV_SIAH, CustomMobType.ZAHHAK, CustomMobType.SARAB_KAVIR, CustomMobType.YOOZPALANG_IRANI};
            return desertMobs[random.nextInt(desertMobs.length)];
        } else if (biomeName.contains("ICE") || biomeName.contains("COLD") || biomeName.contains("TAIGA")) {
            CustomMobType[] coldMobs = {CustomMobType.DIV_SEPID, CustomMobType.GORG_KOHESTAN, CustomMobType.BABR_MAZANDARAN};
            return coldMobs[random.nextInt(coldMobs.length)];
        } else if (biomeName.contains("EXTREME_HILLS") || biomeName.contains("SMALLER")) {
            CustomMobType[] mtnMobs = {CustomMobType.SIMURGH, CustomMobType.ROSTAM_GHOST, CustomMobType.ARASH_KAMANGIR, CustomMobType.ZAL_ZAR};
            return mtnMobs[random.nextInt(mtnMobs.length)];
        } else if (biomeName.contains("SWAMP") || biomeName.contains("JUNGLE")) {
            CustomMobType[] swampMobs = {CustomMobType.AL, CustomMobType.NASU, CustomMobType.BOOSHASP};
            return swampMobs[random.nextInt(swampMobs.length)];
        } else if (biomeName.contains("HELL")) {
            return CustomMobType.APAOSHA;
        } else {
            // Plains & Forests
            CustomMobType[] plainsMobs = {CustomMobType.KAVEH, CustomMobType.FEREYDOUN, CustomMobType.SOHRAB, CustomMobType.SARBAZ_JAVIDAN, CustomMobType.SHIR_IRANI};
            return plainsMobs[random.nextInt(plainsMobs.length)];
        }
    }

    public LivingEntity spawnCustomMob(Location loc, CustomMobType type) {
        try {
            LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type.getBaseType());
            entity.setMetadata("iranian_mob", new FixedMetadataValue(plugin, type.getId()));

            String name = type.getFinglishName();
            String displayName = "&c&l[Mob Irani] &e" + name + " &c(" + (int)type.getHealth() + " HP)";
            if (type == CustomMobType.KAVEH) {
                displayName = "&6&l[Pahlavan] &e" + name + " &a(Dost)";
            } else if (type.getHealth() >= 75.0) {
                displayName = "&4&l[BOSS] &e" + name + " &c&l(" + (int)type.getHealth() + " HP)";
            }

            entity.setCustomName(MessageUtils.color(displayName));
            entity.setCustomNameVisible(true);

            if (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.getHealth());
                entity.setHealth(type.getHealth());
            }
            if (entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(type.getDamage());
            }

            // Kaveh is friendly to players
            if (type == CustomMobType.KAVEH && entity instanceof Creature) {
                ((Creature) entity).setTarget(null);
            }

            // Broadcast spawn for legendary mobs & bosses
            if (type.getHealth() >= 65.0) {
                String spawnMsg = MessageUtils.color("&4&l[Khatar] &6" + type.getFinglishName() + " &cdar mokhtasat-e &e" 
                        + loc.getBlockX() + ", " + loc.getBlockZ() + " &czaher shod!");
                for (Player p : loc.getWorld().getPlayers()) {
                    p.sendMessage(spawnMsg);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 0.7f, 0.8f);
                }
            }

            return entity;
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to spawn custom mob " + type.getId() + ": " + e.getMessage());
            return null;
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!plugin.getConfigManager().getBoolean("mobs.custom.replace-natural", true)) return;
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        if (random.nextDouble() < 0.08) {
            Location loc = event.getLocation();
            CustomMobType type = getRandomMobForBiome(loc);
            if (type != null) {
                event.setCancelled(true);
                spawnCustomMob(loc, type);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (!(event.getDamager() instanceof LivingEntity)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();

        long now = System.currentTimeMillis();
        long last = mobSpeechCooldown.getOrDefault(damager.getUniqueId(), 0L);

        // 1. Check if Damager is an Iranian Custom Mob
        if (damager.hasMetadata("iranian_mob")) {
            String mobId = damager.getMetadata("iranian_mob").get(0).asString();
            try {
                CustomMobType type = CustomMobType.valueOf(mobId);
                // Apply effects
                for (CustomMobType.MobEffect effect : type.getEffects()) {
                    if (random.nextDouble() < effect.getChance()) {
                        player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier()));
                    }
                }

                // Dialogue shouting
                if (now - last > 5000) {
                    mobSpeechCooldown.put(damager.getUniqueId(), now);
                    String shout = getCustomMobShout(type);
                    player.sendMessage(MessageUtils.color("&8[&c" + type.getFinglishName() + "&8] &f\"" + shout + "\""));
                }
            } catch (Exception ignored) {}
            return;
        }

        // 2. Vanilla mobs speaking Finglish
        if (now - last > 6000 && random.nextDouble() < 0.40) {
            mobSpeechCooldown.put(damager.getUniqueId(), now);
            String line = null;
            String mobTitle = "Doshman";

            if (damager instanceof Zombie) {
                line = zombieLines[random.nextInt(zombieLines.length)];
                mobTitle = "Zombie";
            } else if (damager instanceof Skeleton) {
                line = skeletonLines[random.nextInt(skeletonLines.length)];
                mobTitle = "Oskelat";
            } else if (damager instanceof Creeper) {
                line = creeperLines[random.nextInt(creeperLines.length)];
                mobTitle = "Creeper";
            } else if (damager instanceof Spider) {
                line = spiderLines[random.nextInt(spiderLines.length)];
                mobTitle = "Ankaboot";
            } else if (damager instanceof Enderman) {
                line = endermanLines[random.nextInt(endermanLines.length)];
                mobTitle = "Enderman";
            } else if (damager instanceof Witch) {
                line = witchLines[random.nextInt(witchLines.length)];
                mobTitle = "Jadoogar";
            }

            if (line != null) {
                player.sendMessage(MessageUtils.color("&8[&c" + mobTitle + "&8] &7\"" + line + "\""));
            }
        }
    }

    private String getCustomMobShout(CustomMobType type) {
        switch (type) {
            case DIV_SEPID:
                return "MAN DIV-E SEPID-E MAZANDARAN HASTAM! SHOMA RO ZIRE PA KHORD MIKONAM!";
            case ZAHHAK:
                return "MAR-HAYE MAN TESHNE-YE KHUN HASTAN! BIMIR!";
            case AFRASIAB:
                return "Lashkar-e Tooran bar Iran ghalabeh khahad kard!";
            case ROSTAM_GHOST:
                return "Derafsh-e Kaviani barfaraz ast! Ghodrat-e Zabolestan ro bebin!";
            case AL:
                return "Dar tariki-ye batlagh rooh-e to ro midozdam!";
            case ARASH_KAMANGIR:
                return "Tir-e kaman-e Damavand bar ghalb-e to khahad neshast!";
            case DARIUS_GHOST:
            case CYRUS_GHOST:
                return "In sarzamin-e kohan hargez tasleem nemishavad!";
            case APAOSHA:
                return "Tashnegi va atash jahan ra faragerefteh ast!";
            case HASHASHIN_ALAMOAT:
                return "Fadaie-ye Alamoat az sayeh-ha hamleh mikone!";
            default:
                return "Bimir ey doshman-e sarzamin-e Iran!";
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // 1. Custom Iranian Mob death
        if (entity.hasMetadata("iranian_mob")) {
            String mobId = entity.getMetadata("iranian_mob").get(0).asString();
            try {
                CustomMobType type = CustomMobType.valueOf(mobId);
                event.getDrops().clear();
                for (Material mat : type.getDrops()) {
                    event.getDrops().add(new ItemStack(mat, random.nextInt(3) + 1));
                }
                event.getDrops().add(new ItemStack(Material.GOLD_NUGGET, random.nextInt(6) + 3));

                // Boss rewards: drop a legendary sword from 900 swords!
                if (type.getHealth() >= 65.0) {
                    PersianSwordType.SwordData sword = PersianSwordType.getRandomSword();
                    if (sword != null) {
                        event.getDrops().add(sword.createItemStack());
                    }
                    event.getDrops().add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                    event.setDroppedExp(80);
                }

                if (entity.getKiller() != null) {
                    Player killer = entity.getKiller();
                    killer.sendMessage(MessageUtils.color("&8[&6Iran&8] &aMob-e &e" + type.getFinglishName() + " &ara koshtid! &6Zendeh bad Iran!"));
                    killer.playSound(killer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                }
            } catch (Exception ignored) {}
            return;
        }

        // 2. Vanilla mob death message
        if (entity.getKiller() != null && random.nextDouble() < 0.20) {
            String dyingLine = null;
            if (entity instanceof Zombie) {
                dyingLine = "Aaaagh... Shekast khordam... Vali baz khaham gasht...";
            } else if (entity instanceof Skeleton) {
                dyingLine = "Kaman-e man shekast... Vali tariki payan nadare...";
            } else if (entity instanceof Creeper) {
                dyingLine = "Kha... boooom...";
            }

            if (dyingLine != null) {
                entity.getKiller().sendMessage(MessageUtils.color("&8[&c" + entity.getType().name() + "&8] &7\"" + dyingLine + "\""));
            }
        }
    }

    public void spawnMobCommand(Player player, String mobId) {
        try {
            CustomMobType type = CustomMobType.valueOf(mobId.toUpperCase());
            Location loc = player.getLocation().clone().add(player.getLocation().getDirection().multiply(3));
            LivingEntity entity = spawnCustomMob(loc, type);
            if (entity != null) {
                player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aMob-e &e" + type.getFinglishName() + " &aspawn shod!"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(MessageUtils.color("&8[&6Iran&8] &cMob peyda nashod! Mesal: DIV_SEPID, ZAHHAK, ROSTAM_GHOST, AFRASIAB, DARIUS_GHOST, KAVEH, SIMURGH, AL"));
        }
    }
}

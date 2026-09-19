package ir.iranian.hardcore.mobs;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.swords.PersianSwordType;
import ir.iranian.hardcore.utils.MessageUtils;
import ir.iranian.hardcore.utils.SpeechUtils;
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
 * Iranian Mobs & Vocal Speech Manager (v5.0 Finglish)
 * - 30 Custom Mythical & Historical Iranian Mobs & Bosses
 * - Mobs speak vocally (vocal audio + floating speech bubble + actionbar) instead of chat spam!
 * - Complete Finglish, zero Persian Unicode characters.
 */
public class IranianMobsManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();
    private final Map<UUID, Long> mobSpeechCooldown = new HashMap<>();

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
        plugin.getLogger().info("IranianMobsManager: 30 Iranian mobs vocal speech system initialized!");
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
        }.runTaskTimer(plugin, 1200L, 1200L);
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

            if (type == CustomMobType.KAVEH && entity instanceof Creature) {
                ((Creature) entity).setTarget(null);
            }

            // Cinematic spawn for bosses (Title + Subtitle + Roar)
            if (type.getHealth() >= 65.0) {
                for (Player p : loc.getWorld().getPlayers()) {
                    SpeechUtils.sendTitle(p, "&4&l" + type.getFinglishName(), "&cDar sarzamin-e Iran padidar shod!", 10, 50, 10);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 0.8f, 0.6f);
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

        // 1. Iranian Custom Mob Vocal Speech
        if (damager.hasMetadata("iranian_mob")) {
            String mobId = damager.getMetadata("iranian_mob").get(0).asString();
            try {
                CustomMobType type = CustomMobType.valueOf(mobId);
                for (CustomMobType.MobEffect effect : type.getEffects()) {
                    if (random.nextDouble() < effect.getChance()) {
                        player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier()));
                    }
                }

                // Vocal speech cooldown 4s
                if (now - last > 4000) {
                    mobSpeechCooldown.put(damager.getUniqueId(), now);
                    String shout = getCustomMobShout(type);
                    Sound vocalSound = getCustomMobSound(type);
                    float pitch = getCustomMobPitch(type);
                    // Mob speaks vocally (Speech bubble + action bar + vocal sound)
                    SpeechUtils.speak(damager, type.getFinglishName(), shout, vocalSound, pitch);
                }
            } catch (Exception ignored) {}
            return;
        }

        // 2. Vanilla mobs speaking Finglish vocally
        if (now - last > 5000 && random.nextDouble() < 0.45) {
            mobSpeechCooldown.put(damager.getUniqueId(), now);
            String line = null;
            String mobTitle = "Doshman";
            Sound vocalSound = Sound.ENTITY_ZOMBIE_GROWL;
            float pitch = 1.0f;

            if (damager instanceof Zombie) {
                line = zombieLines[random.nextInt(zombieLines.length)];
                mobTitle = "Zombie";
                vocalSound = Sound.ENTITY_ZOMBIE_GROWL;
                pitch = 0.8f;
            } else if (damager instanceof Skeleton) {
                line = skeletonLines[random.nextInt(skeletonLines.length)];
                mobTitle = "Oskelat";
                vocalSound = Sound.ENTITY_SKELETON_AMBIENT;
                pitch = 1.1f;
            } else if (damager instanceof Creeper) {
                line = creeperLines[random.nextInt(creeperLines.length)];
                mobTitle = "Creeper";
                vocalSound = Sound.ENTITY_CREEPER_HURT;
                pitch = 0.7f;
            } else if (damager instanceof Spider) {
                line = spiderLines[random.nextInt(spiderLines.length)];
                mobTitle = "Ankaboot";
                vocalSound = Sound.ENTITY_SPIDER_AMBIENT;
                pitch = 0.85f;
            } else if (damager instanceof Enderman) {
                line = endermanLines[random.nextInt(endermanLines.length)];
                mobTitle = "Enderman";
                vocalSound = Sound.ENTITY_ENDERMEN_SCREAM;
                pitch = 0.9f;
            } else if (damager instanceof Witch) {
                line = witchLines[random.nextInt(witchLines.length)];
                mobTitle = "Jadoogar";
                vocalSound = Sound.ENTITY_WITCH_AMBIENT;
                pitch = 1.1f;
            }

            if (line != null) {
                SpeechUtils.speak(damager, mobTitle, line, vocalSound, pitch);
            }
        }
    }

    private Sound getCustomMobSound(CustomMobType type) {
        switch (type) {
            case DIV_SEPID: return Sound.ENTITY_POLAR_BEAR_WARNING;
            case DIV_SIAH: return Sound.ENTITY_WITHER_SPAWN;
            case ZAHHAK: return Sound.ENTITY_ENDERDRAGON_GROWL;
            case AFRASIAB: return Sound.ENTITY_VINDICATOR_CELEBRATE;
            case ROSTAM_GHOST: return Sound.BLOCK_ENCHANTMENT_TABLE_USE;
            case DARIUS_GHOST:
            case CYRUS_GHOST: return Sound.ENTITY_EVOCATION_ILLAGER_PREPARE_SUMMON;
            case KAVEH: return Sound.ENTITY_VILLAGER_YES;
            case SIMURGH: return Sound.ENTITY_PARROT_IMITATE_ENDER_DRAGON;
            case AL: return Sound.ENTITY_WITCH_AMBIENT;
            case HASHASHIN_ALAMOAT: return Sound.ENTITY_VINDICATOR_AMBIENT;
            default: return Sound.ENTITY_BLAZE_AMBIENT;
        }
    }

    private float getCustomMobPitch(CustomMobType type) {
        switch (type) {
            case DIV_SEPID:
            case DIV_SIAH: return 0.6f;
            case ZAHHAK: return 0.65f;
            case SIMURGH: return 1.4f;
            case KAVEH: return 0.95f;
            case ROSTAM_GHOST: return 0.85f;
            default: return 1.0f;
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

                // Boss rewards
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
                    SpeechUtils.sendActionBar(killer, "&aMob-e &e" + type.getFinglishName() + " &arakoshtid! &6Zendeh bad Iran!");
                    killer.playSound(killer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                }
            } catch (Exception ignored) {}
            return;
        }

        // 2. Vanilla mob death
        if (entity.getKiller() != null && random.nextDouble() < 0.25) {
            String dyingLine = null;
            if (entity instanceof Zombie) {
                dyingLine = "Aaaagh... Shekast khordam...";
            } else if (entity instanceof Skeleton) {
                dyingLine = "Kaman-e man shekast...";
            } else if (entity instanceof Creeper) {
                dyingLine = "Kha... boooom...";
            }

            if (dyingLine != null) {
                SpeechUtils.speak(entity, entity.getType().name(), dyingLine, Sound.ENTITY_ZOMBIE_DEATH, 0.9f);
            }
        }
    }

    public void spawnMobCommand(Player player, String mobId) {
        try {
            CustomMobType type = CustomMobType.valueOf(mobId.toUpperCase());
            Location loc = player.getLocation().clone().add(player.getLocation().getDirection().multiply(3));
            LivingEntity entity = spawnCustomMob(loc, type);
            if (entity != null) {
                SpeechUtils.sendActionBar(player, "&aMob-e &e" + type.getFinglishName() + " &aspawn shod!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(MessageUtils.color("&8[&6Iran&8] &cMob peyda nashod! Mesal: DIV_SEPID, ZAHHAK, ROSTAM_GHOST, AFRASIAB, DARIUS_GHOST, KAVEH, SIMURGH, AL"));
        }
    }
}

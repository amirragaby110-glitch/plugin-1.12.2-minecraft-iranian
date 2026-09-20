package ir.iranian.hardcore.dungeons;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.swords.PersianSwordType;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Dungeon Boss Fight Manager (v5.2 Finglish)
 * Features:
 * - Aggressive melee AI (no broken skeleton melee bug)
 * - Guaranteed high-impact boss attack damage (Phase 1: 10, Phase 2: 15, Phase 3: 20)
 * - Active target acquisition & player chasing task
 * - 3-Phase dynamic combat mechanics with minions, wither storms, and lightning
 * - Guaranteed 900-Sword and Sekkeh Derik loot
 */
public class BossFightManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Integer> bossPhase = new HashMap<>();
    private final Map<UUID, LivingEntity> activeBosses = new HashMap<>();
    private final Random random = new Random();

    public BossFightManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        startBossCombatAI();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("BossFightManager: 3-phase aggressive dungeon boss fights initialized!");
    }

    public LivingEntity spawnDungeonBoss(Location loc, DungeonType type) {
        World world = loc.getWorld();

        // 1.12.2 Safe Melee Types: Wither Skeleton, Husk, Zombie, Pig Zombie
        EntityType baseEntity = EntityType.ZOMBIE;
        if (type == DungeonType.ALAMUT_CASTLE) baseEntity = EntityType.WITHER_SKELETON; // Fast, natural melee
        else if (type == DungeonType.DAKHMEH_ZARTOSHTI) baseEntity = EntityType.WITHER_SKELETON;
        else if (type == DungeonType.TAKHT_JAMSHID_SKY) baseEntity = EntityType.PIG_ZOMBIE;
        else if (type == DungeonType.CHOGHA_ZANBIL) baseEntity = EntityType.HUSK;
        else if (type == DungeonType.GHALEH_BABAK) baseEntity = EntityType.ZOMBIE;

        LivingEntity boss = (LivingEntity) world.spawnEntity(loc, baseEntity);
        boss.setMetadata("dungeon_boss", new FixedMetadataValue(plugin, type.getId()));
        boss.setMetadata("boss_name", new FixedMetadataValue(plugin, type.getBossName()));
        boss.setRemoveWhenFarAway(false);

        double maxHealth = 130.0 + (type.getDifficulty() * 25.0); // 155 to 280 HP
        if (boss.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
            boss.setHealth(maxHealth);
        }
        if (boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
            boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(12.0 + type.getDifficulty() * 2.0);
        }
        if (boss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
            boss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.32);
        }
        if (boss.getAttribute(Attribute.GENERIC_FOLLOW_RANGE) != null) {
            boss.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40.0);
        }
        if (boss.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
            boss.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.7);
        }

        boss.setCustomName(MessageUtils.color("&4&l[BOSS] &6" + type.getBossName() + " &c(" + (int)maxHealth + " HP)"));
        boss.setCustomNameVisible(true);
        bossPhase.put(boss.getUniqueId(), 1);
        activeBosses.put(boss.getUniqueId(), boss);

        // Equip boss with diamond armor & weapon
        if (boss.getEquipment() != null) {
            boss.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
            boss.getEquipment().setItemInMainHandDropChance(0.0f);
            boss.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            boss.getEquipment().setHelmetDropChance(0.0f);
            boss.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            boss.getEquipment().setChestplateDropChance(0.0f);
        }

        // Spawn effects
        world.playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.8f);
        ir.iranian.hardcore.utils.SpeechUtils.playVoice(boss, Sound.ENTITY_ENDERDRAGON_GROWL, 0.7f);

        return boss;
    }

    /**
     * Periodic Combat AI Task:
     * Chases nearest players and guarantees active aggression so bosses never freeze or deal 0 damage.
     */
    private void startBossCombatAI() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<UUID, LivingEntity>> it = activeBosses.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<UUID, LivingEntity> entry = it.next();
                    LivingEntity boss = entry.getValue();

                    if (boss == null || boss.isDead() || !boss.isValid()) {
                        it.remove();
                        continue;
                    }

                    // Find nearest target player within 28 blocks
                    Player nearest = null;
                    double nearestDist = 28.0;

                    for (Player p : boss.getWorld().getPlayers()) {
                        if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) continue;
                        double dist = p.getLocation().distance(boss.getLocation());
                        if (dist < nearestDist) {
                            nearest = p;
                            nearestDist = dist;
                        }
                    }

                    if (nearest != null) {
                        if (boss instanceof Creature) {
                            ((Creature) boss).setTarget(nearest);
                        }

                        // Close range melee hit guarantee if within 2.2 blocks
                        if (nearestDist <= 2.2) {
                            int phase = bossPhase.getOrDefault(boss.getUniqueId(), 1);
                            double damage = 8.0 + (phase * 3.0);
                            nearest.damage(damage, boss);
                            nearest.getWorld().playSound(nearest.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 0.8f);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 15L); // Every 0.75 seconds
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBossDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity boss = (LivingEntity) event.getEntity();
        if (!boss.hasMetadata("dungeon_boss")) return;

        double currentHealth = boss.getHealth() - event.getFinalDamage();
        double maxHealth = boss.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null 
                ? boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() : 150.0;
        double healthRatio = currentHealth / maxHealth;
        String bossName = boss.hasMetadata("boss_name") ? boss.getMetadata("boss_name").get(0).asString() : "Boss";

        int currentPhase = bossPhase.getOrDefault(boss.getUniqueId(), 1);

        // Transition to Phase 2 (50% HP)
        if (healthRatio <= 0.50 && currentPhase == 1) {
            bossPhase.put(boss.getUniqueId(), 2);
            boss.setCustomName(MessageUtils.color("&4&l[BOSS - Phase 2] &6" + bossName + " &c&l(ENRAGED)"));

            // Enrage buffs
            boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000, 1));
            boss.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10000, 1));
            boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 1));

            // Summon 3 Minion Guards
            World world = boss.getWorld();
            world.strikeLightningEffect(boss.getLocation());
            world.playSound(boss.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.0f, 0.7f);

            for (int i = 0; i < 3; i++) {
                Location spawnLoc = boss.getLocation().clone().add(random.nextInt(6) - 3, 0, random.nextInt(6) - 3);
                Zombie guard = (Zombie) world.spawnEntity(spawnLoc, EntityType.ZOMBIE);
                guard.setCustomName(MessageUtils.color("&c&lSarbaz-e Fadaie-ye " + bossName));
                guard.setCustomNameVisible(true);
                if (guard.getEquipment() != null) {
                    guard.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
                    guard.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
                }
            }

            ir.iranian.hardcore.utils.SpeechUtils.playVoice(boss, Sound.ENTITY_ENDERDRAGON_GROWL, 0.8f);
        }
        // Transition to Phase 3 (20% HP)
        else if (healthRatio <= 0.20 && currentPhase == 2) {
            bossPhase.put(boss.getUniqueId(), 3);
            boss.setCustomName(MessageUtils.color("&4&l[BOSS - Phase 3] &6" + bossName + " &4&l(ULTIMATE)"));

            boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 1));
            boss.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10000, 2));
            boss.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));

            World world = boss.getWorld();
            world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 0.5f);
            world.spawnParticle(Particle.EXPLOSION_LARGE, boss.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);

            ir.iranian.hardcore.utils.SpeechUtils.playVoice(boss, Sound.ENTITY_WITHER_SPAWN, 0.6f);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBossAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();
        if (!damager.hasMetadata("dungeon_boss")) return;

        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            int phase = bossPhase.getOrDefault(damager.getUniqueId(), 1);

            // Guaranteed high-damage strike
            double minDamage = 9.0 + (phase * 3.5); // Phase 1: 12.5, Phase 2: 16, Phase 3: 19.5
            event.setDamage(Math.max(event.getDamage(), minDamage));
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 0.8f);

            // Phase 2 extra knockback
            if (phase == 2) {
                victim.setVelocity(damager.getLocation().getDirection().multiply(1.3).setY(0.4));
            }
            // Phase 3 fire attack + wither
            else if (phase == 3) {
                victim.setFireTicks(100);
                victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 1));
                victim.getWorld().strikeLightningEffect(victim.getLocation());
            }
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        LivingEntity boss = event.getEntity();
        if (!boss.hasMetadata("dungeon_boss")) return;

        String bossName = boss.hasMetadata("boss_name") ? boss.getMetadata("boss_name").get(0).asString() : "Boss";
        bossPhase.remove(boss.getUniqueId());
        activeBosses.remove(boss.getUniqueId());

        event.getDrops().clear();
        event.setDroppedExp(250);

        // Guaranteed Legendary Drops: 3 Swords from the 900 Swords!
        for (int i = 0; i < 3; i++) {
            PersianSwordType.SwordData sword = PersianSwordType.getRandomSword();
            if (sword != null) {
                event.getDrops().add(sword.createItemStack());
            }
        }

        // Gold & Sekkeh Derik
        event.getDrops().add(new ItemStack(Material.GOLD_BLOCK, random.nextInt(3) + 2));
        event.getDrops().add(new ItemStack(Material.DIAMOND, random.nextInt(4) + 2));
        event.getDrops().add(new ItemStack(Material.EMERALD, random.nextInt(5) + 3));

        World world = boss.getWorld();
        world.playSound(boss.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

        Player killer = boss.getKiller();
        String killerName = killer != null ? killer.getName() : "Pahlavanan";

        String broadcast = MessageUtils.color("&6&l[Piroozi-ye Bozorg] &aPahlavan &e" + killerName 
                + " &aboss-e tariki &6" + bossName + " &ara shekast dad! &6Zendeh bad Iran!");

        for (Player p : world.getPlayers()) {
            p.sendMessage(broadcast);
            p.sendTitle(MessageUtils.color("&6&lPIROOZI!"), MessageUtils.color("&a" + bossName + " shekast khord!"), 20, 70, 20);
        }
    }
}

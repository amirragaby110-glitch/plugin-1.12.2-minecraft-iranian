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

import java.util.*;

/**
 * Dungeon Boss Fight Manager (v5.0 Finglish)
 * Features:
 * - 3-Phase Dynamic Boss Fights
 * - Phase 1 (100% - 50% HP): Intro speech & standard combat
 * - Phase 2 (50% - 20% HP): Enrage mode, summons minion guards, lightning strikes
 * - Phase 3 (20% - 0% HP): Desperation mode, particle storm, high damage
 * - Victory fanfare, server announcement, guaranteed legendary 900-Sword drops & Derik coins
 */
public class BossFightManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Integer> bossPhase = new HashMap<>();
    private final Map<UUID, Long> speechCooldown = new HashMap<>();
    private final Random random = new Random();

    public BossFightManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("BossFightManager: 3-phase dungeon boss fights initialized!");
    }

    public LivingEntity spawnDungeonBoss(Location loc, DungeonType type) {
        World world = loc.getWorld();
        EntityType baseEntity = EntityType.ZOMBIE;
        if (type == DungeonType.ALAMUT_CASTLE) baseEntity = EntityType.SKELETON;
        else if (type == DungeonType.DAKHMEH_ZARTOSHTI) baseEntity = EntityType.WITHER_SKELETON;
        else if (type == DungeonType.TAKHT_JAMSHID_SKY) baseEntity = EntityType.ZOMBIE;
        else if (type == DungeonType.CHOGHA_ZANBIL) baseEntity = EntityType.HUSK;
        else if (type == DungeonType.GHALEH_BABAK) baseEntity = EntityType.ZOMBIE;

        LivingEntity boss = (LivingEntity) world.spawnEntity(loc, baseEntity);
        boss.setMetadata("dungeon_boss", new FixedMetadataValue(plugin, type.getId()));
        boss.setMetadata("boss_name", new FixedMetadataValue(plugin, type.getBossName()));

        double maxHealth = 120.0 + (type.getDifficulty() * 25.0); // 145 to 270 HP
        if (boss.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
            boss.setHealth(maxHealth);
        }
        if (boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
            boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(12.0 + type.getDifficulty() * 2.0);
        }

        boss.setCustomName(MessageUtils.color("&4&l[BOSS] &6" + type.getBossName() + " &c(" + (int)maxHealth + " HP)"));
        boss.setCustomNameVisible(true);
        bossPhase.put(boss.getUniqueId(), 1);

        // Equip boss
        if (boss.getEquipment() != null) {
            boss.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
            boss.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            boss.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        }

        // Spawn announcement & music
        world.playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.8f);
        ir.iranian.hardcore.utils.SpeechUtils.speak(boss, type.getBossName(), "Man " + type.getBossName() + " hastam! Hichkas zendeh az inja biroon nemiravad!", Sound.ENTITY_ENDERDRAGON_GROWL, 0.7f);

        for (Player p : world.getPlayers()) {
            if (p.getLocation().distance(loc) < 80) {
                p.sendTitle(MessageUtils.color("&4&l[BOSS FIGHT]"), MessageUtils.color("&6" + type.getBossName() + " (Phase 1)"), 10, 50, 20);
            }
        }

        return boss;
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

            ir.iranian.hardcore.utils.SpeechUtils.speak(boss, bossName, "Lashkar-e man be yari biyayid! Hame-ye doshmanan ro khord konid!", Sound.ENTITY_ENDERDRAGON_GROWL, 0.8f);

            for (Player p : world.getPlayers()) {
                if (p.getLocation().distance(boss.getLocation()) < 60) {
                    p.sendTitle(MessageUtils.color("&c&lPHASE 2: ENRAGED"), MessageUtils.color("&e" + bossName + " lashkar farakhond!"), 10, 40, 10);
                }
            }
        }
        // Transition to Phase 3 (20% HP)
        else if (healthRatio <= 0.20 && currentPhase == 2) {
            bossPhase.put(boss.getUniqueId(), 3);
            boss.setCustomName(MessageUtils.color("&4&l[BOSS - Phase 3] &6" + bossName + " &4&l(ULTIMATE)"));

            boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 2));
            boss.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10000, 2));
            boss.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));

            World world = boss.getWorld();
            world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 0.5f);
            world.spawnParticle(Particle.EXPLOSION_LARGE, boss.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);

            ir.iranian.hardcore.utils.SpeechUtils.speak(boss, bossName, "Marg bar shoma! Ta akharin ghatreh-ye khun migangam!", Sound.ENTITY_WITHER_SPAWN, 0.6f);

            for (Player p : world.getPlayers()) {
                if (p.getLocation().distance(boss.getLocation()) < 60) {
                    p.sendTitle(MessageUtils.color("&4&lPHASE 3: ULTIMATE"), MessageUtils.color("&c" + bossName + " dar akharin lahazat!"), 10, 40, 10);
                    // Push players back slightly
                    p.setVelocity(p.getLocation().toVector().subtract(boss.getLocation().toVector()).normalize().multiply(1.2).setY(0.4));
                }
            }
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

            // Phase 2 extra knockback
            if (phase == 2) {
                victim.setVelocity(damager.getLocation().getDirection().multiply(1.2).setY(0.35));
            }
            // Phase 3 fire attack
            else if (phase == 3) {
                victim.setFireTicks(80);
                victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
            }
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        LivingEntity boss = event.getEntity();
        if (!boss.hasMetadata("dungeon_boss")) return;

        String bossName = boss.hasMetadata("boss_name") ? boss.getMetadata("boss_name").get(0).asString() : "Boss";
        bossPhase.remove(boss.getUniqueId());

        event.getDrops().clear();
        event.setDroppedExp(150);

        // Guaranteed Legendary Drops: 2-3 Swords from the 900 Swords!
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

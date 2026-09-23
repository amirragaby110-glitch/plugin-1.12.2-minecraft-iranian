package ir.iranian.hardcore.mobs;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.items.PersianAuthenticItems;
import ir.iranian.hardcore.items.PersianBossItems;
import ir.iranian.hardcore.items.PersianItems;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Akvan Div (The Whirlwind Demon of Shahnameh)
 * Features:
 * - Wields desert whirlwinds and sand vortexes
 * - Hurls players into the air with Levitation & Gusts
 * - Drops "Sang-e Akvan" (Whirlwind Relic) and Sekkeh Derik
 */
public class AkvanDivBoss implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public AkvanDivBoss(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("AkvanDivBoss: Shahnameh Whirlwind Demon initialized!");
    }

    public static Zombie spawnAkvanDiv(Location loc) {
        Zombie boss = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        boss.setCustomName(MessageUtils.color("&4&lAkvan Div (Whirlwind Demon)"));
        boss.setCustomNameVisible(true);
        boss.setBaby(false);
        boss.setVillager(false);

        boss.setMaxHealth(250.0);
        boss.setHealth(250.0);

        boss.getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
        boss.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        boss.getEquipment().setItemInMainHand(PersianBossItems.createDivKoshSword());

        boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));

        // Periodic whirlwind attack task
        new BukkitRunnable() {
            @Override
            public void run() {
                if (boss.isDead() || !boss.isValid()) {
                    cancel();
                    return;
                }

                Location bLoc = boss.getLocation();
                // Whirlwind particle vortex
                bLoc.getWorld().playSound(bLoc, Sound.ENTITY_ENDERDRAGON_FLAP, 1.5f, 0.5f);
                bLoc.getWorld().spawnParticle(Particle.CLOUD, bLoc.clone().add(0, 1, 0), 40, 1.5, 2.0, 1.5, 0.1);

                for (Entity e : boss.getNearbyEntities(8.0, 4.0, 8.0)) {
                    if (e instanceof Player) {
                        Player p = (Player) e;
                        Vector v = new Vector(0, 0.8, 0);
                        p.setVelocity(v);
                        p.damage(6.0, boss);
                        MessageUtils.sendActionBar(p, "&c[Akvan Div] &eToofan-e Akvan shoma ra be hava partab kard!");
                    }
                }
            }
        }.runTaskTimer(IranianHardcorePlugin.getInstance(), 100L, 160L); // every 8 seconds

        return boss;
    }

    @EventHandler
    public void onAkvanDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getCustomName() == null || !entity.getCustomName().contains("Akvan Div")) return;

        // Guaranteed drops
        event.getDrops().clear();
        event.getDrops().add(PersianAuthenticItems.createSangAkvan());
        ItemStack derik = PersianItems.createSekkeHakhamaneshi();
        derik.setAmount(10);
        event.getDrops().add(derik);

        Location loc = entity.getLocation();
        loc.getWorld().playSound(loc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f, 1.0f);
        loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.add(0, 1, 0), 100, 1.0, 2.0, 1.0, 0.2);

        for (Player p : loc.getWorld().getPlayers()) {
            if (p.getLocation().distance(loc) <= 50) {
                p.sendTitle(MessageUtils.color("&6&lSHAHNAMEH"), MessageUtils.color("&aAkvan Div be dast-e Pahlavan-e Irani shekast khord!"), 10, 60, 20);
            }
        }
    }
}

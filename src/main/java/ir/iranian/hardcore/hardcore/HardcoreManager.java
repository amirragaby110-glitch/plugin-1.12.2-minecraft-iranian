package ir.iranian.hardcore.hardcore;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Modiriat mechanichaye hardcore koli - v5.0 Finglish
 */
public class HardcoreManager {

    private final IranianHardcorePlugin plugin;

    public HardcoreManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Aemall tanzimat avalie hardcore be tamam jahanha
     */
    public void applyWorldSettings() {
        if (!isHardcoreEnabled()) return;

        for (World world : Bukkit.getWorlds()) {
            try {
                // In 1.12 gamerules are set via string
                world.setGameRuleValue("naturalRegeneration", "false");
                plugin.getLogger().info("Tanzimat hardcore baraye jahan " + world.getName() + " aemall shod.");
            } catch (Exception e) {
                plugin.getLogger().warning("Khata dar tanzim gamerule baraye " + world.getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Tanzim jane bazikon be 10 (5 ghalb)
     */
    public void setHardcoreHealth(Player player) {
        if (!isHardcoreEnabled()) return;

        double maxHealth = plugin.getConfigManager().getDouble("hardcore.health.max-health", 10.0);
        try {
            AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attr != null) {
                attr.setBaseValue(maxHealth);
                if (player.getHealth() > maxHealth) {
                    player.setHealth(maxHealth);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Khata dar tanzim jane bazikon " + player.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Aya hardcore faal ast?
     */
    public boolean isHardcoreEnabled() {
        return plugin.getConfigManager().getBoolean("hardcore.enabled", true);
    }

    /**
     * Shoroo taskhaye doreie hardcore
     */
    public void startTasks() {
        if (!isHardcoreEnabled()) return;

        // Task goresnegi sari-tar
        boolean fasterHunger = plugin.getConfigManager().getBoolean("hardcore.health.faster-hunger", true);
        if (fasterHunger) {
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isSprinting() || player.isSneaking()) {
                        try {
                            float currentExhaustion = player.getExhaustion();
                            player.setExhaustion(currentExhaustion + 0.3f);
                        } catch (Exception ignored) {}
                    }
                }
            }, 100L, 100L); // Har 5 saniye
        }

        // Task khatarnaktar kardane Nether va The End
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                World.Environment env = player.getWorld().getEnvironment();

                if (env == World.Environment.NETHER) {
                    boolean netherDangerous = plugin.getConfigManager().getBoolean("hardcore.survival.nether.more-dangerous", true);
                    if (netherDangerous) {
                        double chance = plugin.getConfigManager().getDouble("hardcore.survival.nether.chance", 0.05);
                        if (Math.random() < chance) {
                            if (!player.hasPotionEffect(PotionEffectType.WITHER)) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 0, false, false));
                            }
                        }
                        if (Math.random() < 0.02) {
                            if (!player.hasPotionEffect(PotionEffectType.CONFUSION)) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0, false, false));
                            }
                        }
                    }
                } else if (env == World.Environment.THE_END) {
                    boolean endDangerous = plugin.getConfigManager().getBoolean("hardcore.survival.end.more-dangerous", true);
                    if (endDangerous) {
                        double chance = plugin.getConfigManager().getDouble("hardcore.survival.end.chance", 0.03);
                        if (Math.random() < chance) {
                            if (!player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, false, false));
                            }
                        }
                    }
                }
            }
        }, 200L, 200L); // Har 10 saniye
    }
}

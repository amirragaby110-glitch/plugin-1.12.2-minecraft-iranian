package ir.iranian.hardcore.pahlavani;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Persian Zoorkhaneh & Pahlavani Strength System (v5.4 Finglish)
 * Features:
 * - Sneaking 5 times consecutively inside a Zoorkhaneh pit (or on wood/wool arena)
 * - Rhythmic drumbeats of the Morshed (Sound.BLOCK_NOTE_BASEDRUM)
 * - Grants the legendary title "Pahlavan" + Strength II & Absorption II
 */
public class ZoorkhanehManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Integer> sneakCounts = new HashMap<>();
    private final Map<UUID, Long> lastSneakTime = new HashMap<>();
    private final Map<UUID, Long> pahlavanCooldown = new HashMap<>();

    public ZoorkhanehManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("ZoorkhanehManager: Pahlavani & Zoorkhaneh rituals initialized!");
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        Block standingOn = loc.clone().subtract(0, 1, 0).getBlock();

        // Check if standing in a Zoorkhaneh pit or arena
        boolean isArenaFloor = standingOn.getType() == Material.WOOD || standingOn.getType() == Material.CARPET || standingOn.getType() == Material.SMOOTH_BRICK;
        if (!isArenaFloor) return;

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        long last = lastSneakTime.getOrDefault(uuid, 0L);

        // Reset if more than 3 seconds between sneaks
        if (now - last > 3000) {
            sneakCounts.put(uuid, 1);
        } else {
            sneakCounts.put(uuid, sneakCounts.getOrDefault(uuid, 0) + 1);
        }
        lastSneakTime.put(uuid, now);

        int count = sneakCounts.get(uuid);

        if (count >= 1 && count < 5) {
            // Play Morshed drumbeat
            player.getWorld().playSound(loc, Sound.BLOCK_NOTE_BASEDRUM, 1.2f, 0.8f + (count * 0.15f));
            MessageUtils.sendActionBar(player, "&6[Zoorkhaneh] &eShena-ye Bastani: &b" + count + " / 5 &f(Zarb-e Morshed)");
        } else if (count == 5) {
            long lastBuff = pahlavanCooldown.getOrDefault(uuid, 0L);
            if (now - lastBuff < 120000) { // 2 minute cooldown between blessings
                MessageUtils.sendActionBar(player, "&a[Pahlavan] &fPahlavani dar voojood-e shoma jari ast!");
                return;
            }
            pahlavanCooldown.put(uuid, now);
            sneakCounts.remove(uuid);

            // Grand Pahlavani ceremony
            player.getWorld().playSound(loc, Sound.BLOCK_NOTE_BELL, 2.0f, 1.0f);
            player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.2f, 1.0f);
            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.add(0, 1, 0), 30, 0.7, 0.7, 0.7, 0.1);

            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 1)); // Strength II for 3 min
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 3600, 1)); // Absorption II for 3 min
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3600, 0)); // Resistance I

            MessageUtils.sendActionBar(player, "&6&l[Rasm-e Pahlavani] &aDast-Marizad Pahlavan! Barakat-e Goud va Zarb-e Morshed daryaft shod!");
        }
    }
}

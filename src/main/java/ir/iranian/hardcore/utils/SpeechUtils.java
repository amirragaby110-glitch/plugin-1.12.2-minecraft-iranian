package ir.iranian.hardcore.utils;

import ir.iranian.hardcore.IranianHardcorePlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Speech & Vocalization utility for Iranian Mobs & Villagers (v5.0 Finglish)
 * Plays vocal audio sequences, renders floating in-world speech bubbles above heads,
 * and displays RPG-style subtitles via ActionBar instead of spamming public chat.
 */
public class SpeechUtils {

    /**
     * Entity speaks: vocal sound plays, floating speech bubble hovers over head,
     * and nearby players receive action bar dialogue subtitle.
     */
    public static void speak(LivingEntity speaker, String speakerName, String text, Sound vocalSound, float pitch) {
        if (speaker == null || !speaker.isValid() || text == null || text.isEmpty()) return;

        World world = speaker.getWorld();
        Location loc = speaker.getLocation();

        // 1. Play vocal audio sound
        if (vocalSound != null) {
            world.playSound(loc, vocalSound, 1.2f, pitch);
        }

        // 2. Spawn temporary floating speech bubble above head
        showSpeechBubble(speaker, "&e" + speakerName + ": &f\"" + text + "\"", 50);

        // 3. Send ActionBar subtitle to all players within 18 blocks
        String actionBarMsg = MessageUtils.color("&6" + speakerName + "&7: &f\"" + text + "\"");
        double radiusSquared = 18.0 * 18.0;

        for (Player p : world.getPlayers()) {
            if (p.getLocation().distanceSquared(loc) <= radiusSquared) {
                sendActionBar(p, actionBarMsg);
            }
        }
    }

    /**
     * Sends action bar message to a player
     */
    public static void sendActionBar(Player player, String message) {
        if (player == null || !player.isOnline()) return;
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(MessageUtils.color(message)));
        } catch (Throwable ignored) {}
    }

    /**
     * Sends in-game title and subtitle
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (player == null || !player.isOnline()) return;
        try {
            player.sendTitle(MessageUtils.color(title), MessageUtils.color(subtitle), fadeIn, stay, fadeOut);
        } catch (Throwable ignored) {}
    }

    /**
     * Creates a temporary floating holographic speech bubble above the entity's head
     */
    public static void showSpeechBubble(LivingEntity entity, String text, int durationTicks) {
        if (entity == null || !entity.isValid()) return;

        Location bubbleLoc = entity.getLocation().clone().add(0, entity.getEyeHeight() + 0.45, 0);
        World world = bubbleLoc.getWorld();
        if (world == null) return;

        try {
            ArmorStand stand = (ArmorStand) world.spawnEntity(bubbleLoc, EntityType.ARMOR_STAND);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setMarker(true);
            stand.setSmall(true);
            stand.setCustomName(MessageUtils.color(text));
            stand.setCustomNameVisible(true);

            new BukkitRunnable() {
                int elapsed = 0;
                @Override
                public void run() {
                    elapsed += 5;
                    if (!stand.isValid() || elapsed >= durationTicks || !entity.isValid()) {
                        stand.remove();
                        cancel();
                        return;
                    }
                    // Follow entity head movement
                    stand.teleport(entity.getLocation().clone().add(0, entity.getEyeHeight() + 0.45, 0));
                }
            }.runTaskTimer(IranianHardcorePlugin.getInstance(), 5L, 5L);

        } catch (Throwable ignored) {}
    }
}

package ir.iranian.hardcore.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

/**
 * Vocal Speech & Audio Engine for Iranian Mobs & Bosses (v5.1 Sound-Only Edition)
 * Pure audio vocalizations with zero chat spam and ZERO entity/ArmorStand spawning,
 * ensuring 100% normal combat hit detection.
 */
public class SpeechUtils {

    /**
     * Entity vocalization: plays sound effect directly at entity position.
     * No text, no floating armor stands, no hit-blocking entities!
     */
    public static void playVoice(LivingEntity speaker, Sound vocalSound, float pitch) {
        if (speaker == null || !speaker.isValid()) return;
        World world = speaker.getWorld();
        if (world == null || vocalSound == null) return;
        world.playSound(speaker.getLocation(), vocalSound, 1.3f, pitch);
    }

    /**
     * Location-based vocal audio player
     */
    public static void playVoiceAt(Location loc, Sound vocalSound, float volume, float pitch) {
        if (loc == null || loc.getWorld() == null || vocalSound == null) return;
        loc.getWorld().playSound(loc, vocalSound, volume, pitch);
    }
}

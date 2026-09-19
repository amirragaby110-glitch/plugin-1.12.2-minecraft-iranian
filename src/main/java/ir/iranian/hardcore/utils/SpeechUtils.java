package ir.iranian.hardcore.utils;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Speech & Vocalization utility for Iranian Mobs & Villagers (v5.1 Finglish)
 * Plays vocal audio sequences, renders floating in-world speech bubbles above heads,
 * and displays RPG-style subtitles via ActionBar instead of spamming public chat.
 */
public class SpeechUtils {

    private static boolean bungeecordChecked = false;
    private static Method spigotMethod = null;
    private static Method sendMessageMethod = null;
    private static Method fromLegacyTextMethod = null;
    private static Object actionBarType = null;
    private static Class<?> textComponentClass = null;

    static {
        initReflection();
    }

    private static void initReflection() {
        try {
            Class<?> chatMessageTypeClass = Class.forName("net.md_5.bungee.api.ChatMessageType");
            for (Object constant : chatMessageTypeClass.getEnumConstants()) {
                if (constant.toString().equals("ACTION_BAR")) {
                    actionBarType = constant;
                    break;
                }
            }
            textComponentClass = Class.forName("net.md_5.bungee.api.chat.TextComponent");
            Class<?> baseComponentClass = Class.forName("net.md_5.bungee.api.chat.BaseComponent");
            Class<?> baseComponentArrayClass = Array.newInstance(baseComponentClass, 0).getClass();

            fromLegacyTextMethod = textComponentClass.getMethod("fromLegacyText", String.class);
            Class<?> spigotClass = Class.forName("org.bukkit.entity.Player$Spigot");
            sendMessageMethod = spigotClass.getMethod("sendMessage", chatMessageTypeClass, baseComponentArrayClass);
            spigotMethod = Player.class.getMethod("spigot");
            bungeecordChecked = true;
        } catch (Throwable ignored) {
            bungeecordChecked = false;
        }
    }

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
     * Sends action bar message to a player safely via reflection
     */
    public static void sendActionBar(Player player, String message) {
        if (player == null || !player.isOnline()) return;
        String colored = MessageUtils.color(message);

        if (bungeecordChecked && spigotMethod != null && sendMessageMethod != null && fromLegacyTextMethod != null && actionBarType != null) {
            try {
                Object spigot = spigotMethod.invoke(player);
                Object components = fromLegacyTextMethod.invoke(null, colored);
                sendMessageMethod.invoke(spigot, actionBarType, components);
                return;
            } catch (Throwable ignored) {}
        }

        // Fallback: send as brief subtitle if actionbar unavailable
        try {
            player.sendTitle("", colored, 0, 40, 10);
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
                    stand.teleport(entity.getLocation().clone().add(0, entity.getEyeHeight() + 0.45, 0));
                }
            }.runTaskTimer(IranianHardcorePlugin.getInstance(), 5L, 5L);

        } catch (Throwable ignored) {}
    }
}

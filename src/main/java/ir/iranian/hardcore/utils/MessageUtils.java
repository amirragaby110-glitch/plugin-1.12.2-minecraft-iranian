package ir.iranian.hardcore.utils;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Abzar payam-resani ba poshtibani Finglish va rang
 */
public class MessageUtils {

    private static String prefix = "&8[&6Iran&8] &r";

    public static void setPrefix(String p) {
        prefix = p;
    }

    public static String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> color(List<String> texts) {
        if (texts == null) return new ArrayList<>();
        List<String> colored = new ArrayList<>();
        for (String text : texts) {
            colored.add(color(text));
        }
        return colored;
    }

    public static String withPrefix(String text) {
        return color(prefix + text);
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public static void sendWithPrefix(CommandSender sender, String message) {
        sender.sendMessage(withPrefix(message));
    }

    public static void sendToPlayer(Player player, String path) {
        String msg = IranianHardcorePlugin.getInstance().getConfigManager().getString(path, path);
        sendWithPrefix(player, msg);
    }

    public static String getMessage(String path, String def) {
        String msg = IranianHardcorePlugin.getInstance().getConfigManager().getString(path, def);
        return color(msg);
    }

    public static String getPrefixedMessage(String path, String def) {
        return withPrefix(getMessage(path, def));
    }

    public static void broadcast(String message) {
        String colored = color(message);
        for (Player p : IranianHardcorePlugin.getInstance().getServer().getOnlinePlayers()) {
            p.sendMessage(colored);
        }
    }

    public static void broadcastWithPrefix(String message) {
        broadcast(prefix + message);
    }

    /**
     * Sends an Action Bar HUD message to the player directly above the hotbar.
     * Fully compatible with Spigot/Paper 1.12.2 and earlier/later versions.
     */
    public static void sendActionBar(Player player, String message) {
        if (player == null || !player.isOnline()) return;
        String colored = color(message);

        // 1. Spigot Bungee Chat API
        try {
            Class<?> chatMessageTypeClass = Class.forName("net.md_5.bungee.api.ChatMessageType");
            Class<?> textComponentClass = Class.forName("net.md_5.bungee.api.chat.TextComponent");
            Class<?> baseComponentClass = Class.forName("net.md_5.bungee.api.chat.BaseComponent");

            Object actionbarType = null;
            for (Object obj : chatMessageTypeClass.getEnumConstants()) {
                if (obj.toString().equals("ACTION_BAR")) {
                    actionbarType = obj;
                    break;
                }
            }

            Object components = textComponentClass.getMethod("fromLegacyText", String.class).invoke(null, colored);
            Object spigot = player.getClass().getMethod("spigot").invoke(player);

            // Try array parameter first (Spigot 1.12.2 varargs signature)
            boolean sent = false;
            for (java.lang.reflect.Method m : spigot.getClass().getMethods()) {
                if (m.getName().equals("sendMessage") && m.getParameterCount() == 2) {
                    Class<?>[] p = m.getParameterTypes();
                    if (p[0].equals(chatMessageTypeClass)) {
                        if (p[1].isArray()) {
                            m.invoke(spigot, actionbarType, components);
                            sent = true;
                            break;
                        } else if (p[1].isAssignableFrom(baseComponentClass)) {
                            Object[] arr = (Object[]) components;
                            if (arr.length > 0) {
                                m.invoke(spigot, actionbarType, arr[0]);
                                sent = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (sent) return;
        } catch (Throwable ignored) {}

        // 2. NMS PacketPlayOutChat (1.12.2)
        try {
            String serverVersion = player.getServer().getClass().getPackage().getName().split("\\.")[3];
            Class<?> chatCompClass = Class.forName("net.minecraft.server." + serverVersion + ".ChatComponentText");
            Class<?> iChatBaseClass = Class.forName("net.minecraft.server." + serverVersion + ".IChatBaseComponent");
            Class<?> packetClass = Class.forName("net.minecraft.server." + serverVersion + ".PacketPlayOutChat");
            Class<?> chatTypeClass = Class.forName("net.minecraft.server." + serverVersion + ".ChatMessageType");

            Object chatComp = chatCompClass.getConstructor(String.class).newInstance(colored);
            Object chatType = null;
            for (Object obj : chatTypeClass.getEnumConstants()) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatType = obj;
                    break;
                }
            }
            Object packet;
            if (chatType != null) {
                packet = packetClass.getConstructor(iChatBaseClass, chatTypeClass).newInstance(chatComp, chatType);
            } else {
                packet = packetClass.getConstructor(iChatBaseClass, byte.class).newInstance(chatComp, (byte) 2);
            }

            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + serverVersion + ".Packet"))
                    .invoke(connection, packet);
            return;
        } catch (Throwable ignored) {}

        // 3. Fallback subtitle
        try {
            player.sendTitle("", colored, 0, 30, 5);
        } catch (Throwable ignored) {}
    }
}

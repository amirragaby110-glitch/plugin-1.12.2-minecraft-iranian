package ir.iranian.hardcore.utils;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * ابزار پیام‌رسانی با پشتیبانی فارسی و رنگ
 */
public class MessageUtils {

    private static String prefix = "&8[&cسخت&8] &r";

    public static void setPrefix(String p) {
        prefix = p;
    }

    public static String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
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
}

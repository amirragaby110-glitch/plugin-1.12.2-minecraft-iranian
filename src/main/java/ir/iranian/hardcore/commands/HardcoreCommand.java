package ir.iranian.hardcore.commands;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.race.RaceType;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Dastorate admin hardcore - v5.0 Finglish
 */
public class HardcoreCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public HardcoreCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "reload":
                if (!sender.hasPermission("iranianhardcore.admin")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi nadarid!"));
                    return true;
                }
                plugin.getConfigManager().reload();
                sender.sendMessage(MessageUtils.getPrefixedMessage("messages.reloaded", "&aConfig reload shod!"));
                return true;

            case "info":
                sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                sender.sendMessage(MessageUtils.color("&6&lEtelaat Hardcore - Iranian Hardcore v5.0"));
                sender.sendMessage(MessageUtils.color("&7Version: &a" + plugin.getDescription().getVersion()));
                sender.sendMessage(MessageUtils.color("&7Hardcore faal: &a" + plugin.getConfigManager().getBoolean("hardcore.enabled", true)));
                sender.sendMessage(MessageUtils.color("&7Jane max: &c" + plugin.getConfigManager().getDouble("hardcore.health.max-health", 10.0)));
                sender.sendMessage(MessageUtils.color("&7Bazikonan online: &e" + Bukkit.getOnlinePlayers().size()));
                sender.sendMessage(MessageUtils.color("&7Aghvam faal: &a" + plugin.getConfigManager().getBoolean("race.enabled", true)));
                sender.sendMessage(MessageUtils.color("&7Shamshirha: &6900 Shamshir Irani"));
                sender.sendMessage(MessageUtils.color("&7Ghazaha: &e500 Ghaza Irani"));
                sender.sendMessage(MessageUtils.color("&7Itemha: &b20,000 Item Irani"));
                sender.sendMessage(MessageUtils.color("&7Dungeons: &a30 Dungeon Tarikhi"));
                sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                return true;

            case "setrace":
                if (!sender.hasPermission("iranianhardcore.admin")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi nadarid!"));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(MessageUtils.color("&cEstefadeh: /hardcore setrace <player> <race>"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(MessageUtils.withPrefix("&cBazikon yaft nashod!"));
                    return true;
                }
                RaceType race = RaceType.fromId(args[2].toUpperCase());
                if (race == null) {
                    sender.sendMessage(MessageUtils.withPrefix("&cGhome nameetabar!"));
                    return true;
                }
                plugin.getRaceManager().setPlayerRace(target, race);
                sender.sendMessage(MessageUtils.withPrefix("&aGhome " + target.getName() + " be " + race.getFinglishName() + " tanzim shod!"));
                return true;

            case "reset":
                if (!sender.hasPermission("iranianhardcore.admin")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi nadarid!"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(MessageUtils.color("&cEstefadeh: /hardcore reset <player>"));
                    return true;
                }
                Player resetTarget = Bukkit.getPlayer(args[1]);
                if (resetTarget == null) {
                    sender.sendMessage(MessageUtils.withPrefix("&cBazikon yaft nashod!"));
                    return true;
                }
                plugin.getRaceManager().removePlayerRace(resetTarget);
                plugin.getConfigManager().setBedLastUsed(resetTarget.getUniqueId().toString(), 0L);
                plugin.getConfigManager().setRaceChangeLastUsed(resetTarget.getUniqueId().toString(), 0L);
                sender.sendMessage(MessageUtils.withPrefix("&aTamam dadeh haye " + resetTarget.getName() + " reset shod!"));
                return true;

            default:
                sendHelp(sender);
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        sender.sendMessage(MessageUtils.color("&6&lDastorate Hardcore Irani v5.0:"));
        sender.sendMessage(MessageUtils.color("&e/hardcore reload &7- Reload config"));
        sender.sendMessage(MessageUtils.color("&e/hardcore info &7- Etelaate plugin"));
        sender.sendMessage(MessageUtils.color("&e/hardcore setrace <player> <race> &7- Tanzime ghom"));
        sender.sendMessage(MessageUtils.color("&e/hardcore reset <player> &7- Reset dadeha"));
        sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = Arrays.asList("reload", "info", "setrace", "reset");
            for (String s : subs) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(s);
                }
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("setrace") || args[0].equalsIgnoreCase("reset"))) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(p.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("setrace")) {
            for (RaceType type : RaceType.values()) {
                if (type.getId().toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add(type.getId());
                }
            }
        }
        return completions;
    }
}

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
 * دستورات ادمین هاردکور
 * /hardcore reload
 * /hardcore info
 * /hardcore setrace
 * /hardcore reset
 */
public class HardcoreCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public HardcoreCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("iranianhardcore.hardcore.info")) {
            sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "reload":
                if (!sender.hasPermission("iranianhardcore.hardcore.reload")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
                    return true;
                }
                plugin.getConfigManager().reload();
                plugin.getHardcoreManager().applyWorldSettings();
                sender.sendMessage(MessageUtils.getPrefixedMessage("messages.reloaded", "&aکانفیگ ریلود شد!"));
                break;

            case "info":
                sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
                sender.sendMessage(MessageUtils.color("&6&l اطلاعات هاردکور - Iranian Hardcore"));
                sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
                sender.sendMessage(MessageUtils.color("&7نسخه: &a" + plugin.getDescription().getVersion()));
                sender.sendMessage(MessageUtils.color("&7هاردکور فعال: &a" + plugin.getConfigManager().getBoolean("hardcore.enabled", true)));
                sender.sendMessage(MessageUtils.color("&7جان مکس: &c" + plugin.getConfigManager().getDouble("hardcore.health.max-health", 10.0)));
                sender.sendMessage(MessageUtils.color("&7بازیکنان آنلاین: &e" + Bukkit.getOnlinePlayers().size()));
                sender.sendMessage(MessageUtils.color("&7نژاد فعال: &a" + plugin.getConfigManager().getBoolean("race.enabled", true)));
                sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
                break;

            case "setrace":
                if (!sender.hasPermission("iranianhardcore.hardcore.setrace")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(MessageUtils.color("&cاستفاده: /hardcore setrace <player> <race>"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(MessageUtils.withPrefix("&cبازیکن یافت نشد!"));
                    return true;
                }
                RaceType race = RaceType.fromId(args[2]);
                if (race == null) {
                    sender.sendMessage(MessageUtils.withPrefix("&cنژاد نامعتبر!"));
                    return true;
                }
                plugin.getRaceManager().setRaceNoCooldown(target, race);
                sender.sendMessage(MessageUtils.withPrefix("&aنژاد " + target.getName() + " به " + race.getPersianName() + " تنظیم شد!"));
                break;

            case "reset":
                if (!sender.hasPermission("iranianhardcore.admin")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(MessageUtils.color("&cاستفاده: /hardcore reset <player>"));
                    return true;
                }
                Player resetTarget = Bukkit.getPlayer(args[1]);
                if (resetTarget == null) {
                    sender.sendMessage(MessageUtils.withPrefix("&cبازیکن یافت نشد!"));
                    return true;
                }
                plugin.getRaceManager().clearRace(resetTarget);
                // پاک کردن کولدان‌ها
                plugin.getConfigManager().setBedCooldown(resetTarget.getUniqueId(), 0);
                plugin.getConfigManager().setRaceChangeCooldown(resetTarget.getUniqueId(), 0);
                sender.sendMessage(MessageUtils.withPrefix("&aتمام داده‌های " + resetTarget.getName() + " ریست شد!"));
                break;

            case "help":
                sendHelp(sender);
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        sender.sendMessage(MessageUtils.color("&6&l دستورات هاردکور"));
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        sender.sendMessage(MessageUtils.color("&e/hardcore reload &7- ریلود کانفیگ"));
        sender.sendMessage(MessageUtils.color("&e/hardcore info &7- اطلاعات پلاگین"));
        sender.sendMessage(MessageUtils.color("&e/hardcore setrace <player> <race> &7- تنظیم نژاد"));
        sender.sendMessage(MessageUtils.color("&e/hardcore reset <player> &7- ریست داده‌ها"));
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = Arrays.asList("reload", "info", "setrace", "reset", "help");
            for (String s : subs) {
                if (s.startsWith(args[0].toLowerCase())) list.add(s);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setrace") || args[0].equalsIgnoreCase("reset")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) list.add(p.getName());
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setrace")) {
                for (RaceType race : RaceType.values()) {
                    if (race.getId().toLowerCase().startsWith(args[2].toLowerCase())) list.add(race.getId());
                }
            }
        }
        return list;
    }
}

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
 * دستورات نژاد
 * /race choose - باز کردن GUI
 * /race info - اطلاعات نژاد
 * /race change - تغییر نژاد (با کولدان)
 * /race help - راهنما
 * /race admin - دستورات ادمین
 */
public class RaceCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public RaceCommand(IranianHardcorePlugin plugin) {
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
            case "choose":
            case "select":
            case "gui":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cفقط بازیکنان می‌توانند نژاد انتخاب کنند!"));
                    return true;
                }
                Player player = (Player) sender;
                if (!player.hasPermission("iranianhardcore.race.choose")) {
                    player.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
                    return true;
                }
                plugin.getRaceGUI().openRaceGUI(player);
                break;

            case "info":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cفقط بازیکنان!"));
                    return true;
                }
                Player infoPlayer = (Player) sender;
                if (!infoPlayer.hasPermission("iranianhardcore.race.info")) {
                    infoPlayer.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
                    return true;
                }
                if (args.length >= 2) {
                    // اطلاعات نژاد دیگر یا اطلاعات یک نژاد خاص
                    RaceType type = RaceType.fromId(args[1]);
                    if (type != null) {
                        sendRaceDetail(infoPlayer, type);
                    } else {
                        // بازیکن دیگر
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            RaceType race = plugin.getRaceManager().getRace(target);
                            if (race == null) {
                                infoPlayer.sendMessage(MessageUtils.withPrefix("&cبازیکن " + target.getName() + " نژادی ندارد!"));
                            } else {
                                infoPlayer.sendMessage(MessageUtils.withPrefix("&7نژاد " + target.getName() + ": &6" + race.getPersianName()));
                            }
                        } else {
                            infoPlayer.sendMessage(MessageUtils.withPrefix("&cنژاد یا بازیکن یافت نشد!"));
                        }
                    }
                } else {
                    plugin.getRaceManager().sendRaceInfo(infoPlayer);
                }
                break;

            case "change":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cفقط بازیکنان!"));
                    return true;
                }
                Player changePlayer = (Player) sender;
                if (!changePlayer.hasPermission("iranianhardcore.race.change")) {
                    changePlayer.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
                    return true;
                }
                if (!plugin.getRaceManager().hasRace(changePlayer)) {
                    changePlayer.sendMessage(MessageUtils.withPrefix("&cشما هنوز نژادی ندارید! &e/race choose"));
                    return true;
                }
                if (!plugin.getRaceManager().canChangeRace(changePlayer)) {
                    long remaining = plugin.getRaceManager().getRemainingDaysForRaceChange(changePlayer);
                    String msg = plugin.getConfigManager().getString("race.change-cooldown-message", "&cباید %days% روز دیگر صبر کنید.");
                    msg = msg.replace("%days%", String.valueOf(remaining));
                    changePlayer.sendMessage(MessageUtils.withPrefix(msg));
                    return true;
                }
                plugin.getRaceGUI().openRaceGUI(changePlayer);
                break;

            case "list":
                sender.sendMessage(MessageUtils.color("&6&lلیست نژادها:"));
                for (RaceType race : RaceType.values()) {
                    sender.sendMessage(MessageUtils.color("&7- &6" + race.getPersianName() + " &7(" + race.getId() + ") &f: " + race.getEnglishName()));
                }
                break;

            case "help":
                sendHelp(sender);
                break;

            case "admin":
            case "set":
                if (!sender.hasPermission("iranianhardcore.race.admin")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(MessageUtils.color("&cاستفاده: /race admin set <player> <race>"));
                    sender.sendMessage(MessageUtils.color("&cمثال: /race admin set Amir MOUNTAINBORN"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("set")) {
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        sender.sendMessage(MessageUtils.withPrefix("&cبازیکن یافت نشد!"));
                        return true;
                    }
                    if (args.length < 4) {
                        sender.sendMessage(MessageUtils.color("&cنژاد را وارد کنید!"));
                        return true;
                    }
                    RaceType race = RaceType.fromId(args[3]);
                    if (race == null) {
                        sender.sendMessage(MessageUtils.withPrefix("&cنژاد نامعتبر! لیست: /race list"));
                        return true;
                    }
                    plugin.getRaceManager().setRaceNoCooldown(target, race);
                    sender.sendMessage(MessageUtils.withPrefix("&aنژاد " + target.getName() + " به " + race.getPersianName() + " تنظیم شد!"));
                } else if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("reset")) {
                    if (args.length < 3) {
                        sender.sendMessage(MessageUtils.color("&cاستفاده: /race admin clear <player>"));
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        sender.sendMessage(MessageUtils.withPrefix("&cبازیکن یافت نشد!"));
                        return true;
                    }
                    plugin.getRaceManager().clearRace(target);
                    sender.sendMessage(MessageUtils.withPrefix("&aنژاد " + target.getName() + " پاک شد!"));
                } else if (args[1].equalsIgnoreCase("reload")) {
                    plugin.getConfigManager().reload();
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.reloaded", "&aریلود شد!"));
                }
                break;

            default:
                // اگر مستقیم نام نژاد وارد کرد (برای انتخاب سریع)
                RaceType direct = RaceType.fromId(sub);
                if (direct != null && sender instanceof Player) {
                    Player p = (Player) sender;
                    if (!plugin.getRaceManager().canChangeRace(p) && plugin.getRaceManager().hasRace(p)) {
                        long remaining = plugin.getRaceManager().getRemainingDaysForRaceChange(p);
                        String msg = plugin.getConfigManager().getString("race.change-cooldown-message", "&cباید %days% روز دیگر صبر کنید.");
                        msg = msg.replace("%days%", String.valueOf(remaining));
                        p.sendMessage(MessageUtils.withPrefix(msg));
                        return true;
                    }
                    plugin.getRaceManager().setRace(p, direct);
                    return true;
                }
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        sender.sendMessage(MessageUtils.color("&6&l دستورات نژاد - Iranian Hardcore"));
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        sender.sendMessage(MessageUtils.color("&e/race choose &7- باز کردن منوی انتخاب نژاد"));
        sender.sendMessage(MessageUtils.color("&e/race info [race/player] &7- اطلاعات نژاد"));
        sender.sendMessage(MessageUtils.color("&e/race change &7- تغییر نژاد (هر 7 روز)"));
        sender.sendMessage(MessageUtils.color("&e/race list &7- لیست تمام نژادها"));
        sender.sendMessage(MessageUtils.color("&e/race help &7- راهنما"));
        if (sender.hasPermission("iranianhardcore.race.admin")) {
            sender.sendMessage(MessageUtils.color("&c/race admin set <player> <race> &7- تنظیم نژاد"));
            sender.sendMessage(MessageUtils.color("&c/race admin clear <player> &7- پاک کردن نژاد"));
            sender.sendMessage(MessageUtils.color("&c/race admin reload &7- ریلود کانفیگ"));
        }
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
    }

    private void sendRaceDetail(Player player, RaceType race) {
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&6&l اطلاعات نژاد: " + race.getPersianName()));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        for (String line : race.getLore()) {
            player.sendMessage(MessageUtils.color(line));
        }
        player.sendMessage(MessageUtils.color("&7بایوم‌های خانه: &a" + race.getHomeBiomes().size() + " عدد"));
        for (org.bukkit.block.Biome b : race.getHomeBiomes()) {
            player.sendMessage(MessageUtils.color("&8- &f" + b.name()));
        }
        player.sendMessage(MessageUtils.color("&7بایوم‌های دشمن: &c" + race.getHostileBiomes().size() + " عدد"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subs = Arrays.asList("choose", "info", "change", "list", "help", "admin");
            for (String s : subs) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(s);
                }
            }
            for (RaceType race : RaceType.values()) {
                if (race.getId().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(race.getId());
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                for (RaceType race : RaceType.values()) {
                    if (race.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(race.getId());
                    }
                }
            } else if (args[0].equalsIgnoreCase("admin")) {
                List<String> adminSubs = Arrays.asList("set", "clear", "reload");
                for (String s : adminSubs) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(s);
                    }
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("set")) {
                // بازیکنان آنلاین
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                        completions.add(p.getName());
                    }
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("set")) {
                for (RaceType race : RaceType.values()) {
                    if (race.getId().toLowerCase().startsWith(args[3].toLowerCase())) {
                        completions.add(race.getId());
                    }
                }
            }
        }

        return completions;
    }
}

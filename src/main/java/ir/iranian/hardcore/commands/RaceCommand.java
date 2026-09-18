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
 * دستورات قوم ایرانی - هر بایوم یک قوم - نسخه 3.5 کاملا فارسی
 * /race choose - انتخاب قوم ایرانی (GUI 54 اسلاته)
 * /race info - اطلاعات قوم
 * /race change - تغییر قوم هر 7 روز
 * /قوم - دستور فارسی
 * /نژاد - دستور فارسی
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
            case "انتخاب":
            case "قوم":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cفقط بازیکنان ایرانی می‌توانند قوم انتخاب کنند!"));
                    return true;
                }
                Player player = (Player) sender;
                if (!player.hasPermission("iranianhardcore.race.choose")) {
                    player.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
                    return true;
                }
                plugin.getRaceGUI().openRaceGUI(player);
                player.sendMessage(MessageUtils.withPrefix("&aمنوی اقوام ایرانی باز شد! &7یکی از اقوام اصیل ایران را انتخاب کنید"));
                break;

            case "info":
            case "اطلاعات":
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
                    RaceType type = RaceType.fromId(args[1]);
                    if (type != null) {
                        sendRaceDetail(infoPlayer, type);
                    } else {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            RaceType race = plugin.getRaceManager().getRace(target);
                            if (race == null) {
                                infoPlayer.sendMessage(MessageUtils.withPrefix("&cبازیکن " + target.getName() + " هنوز قوم ایرانی انتخاب نکرده!"));
                            } else {
                                infoPlayer.sendMessage(MessageUtils.withPrefix("&7قوم " + target.getName() + ": &6" + race.getPersianName() + " &7- بایوم: " + target.getLocation().getBlock().getBiome().name()));
                            }
                        } else {
                            infoPlayer.sendMessage(MessageUtils.withPrefix("&cقوم یا بازیکن یافت نشد! /race list"));
                        }
                    }
                } else {
                    plugin.getRaceManager().sendRaceInfo(infoPlayer);
                }
                break;

            case "change":
            case "تغییر":
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
                    changePlayer.sendMessage(MessageUtils.withPrefix("&cشما هنوز قوم ایرانی ندارید! &e/race choose &7یا &a/قوم"));
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
            case "لیست":
            case "اقوام":
                sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
                sender.sendMessage(MessageUtils.color("&6&l🇮🇷 لیست اقوام ایرانی - هر بایوم یک قوم"));
                sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
                for (RaceType race : RaceType.values()) {
                    sender.sendMessage(MessageUtils.color("&7- &6" + race.getPersianName() + " &7(" + race.getId() + ") &8- &f" + race.getEnglishName() + " &7| بایوم: " + race.getHomeBiomes().get(0).name()));
                }
                sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
                sender.sendMessage(MessageUtils.color("&7تعداد: &a" + RaceType.values().length + " قوم ایرانی"));
                break;

            case "help":
            case "راهنما":
                sendHelp(sender);
                break;

            case "admin":
            case "ادمین":
            case "set":
                if (!sender.hasPermission("iranianhardcore.race.admin")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(MessageUtils.color("&cاستفاده: /race admin set <بازیکن> <قوم>"));
                    sender.sendMessage(MessageUtils.color("&cمثال: /race admin set Amir FARS"));
                    sender.sendMessage(MessageUtils.color("&cمثال فارسی: /race admin set Amir کرد"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("set")) {
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        sender.sendMessage(MessageUtils.withPrefix("&cبازیکن یافت نشد!"));
                        return true;
                    }
                    if (args.length < 4) {
                        sender.sendMessage(MessageUtils.color("&cقوم را وارد کنید! /race list"));
                        return true;
                    }
                    RaceType race = RaceType.fromId(args[3]);
                    if (race == null) {
                        sender.sendMessage(MessageUtils.withPrefix("&cقوم نامعتبر! لیست: /race list"));
                        return true;
                    }
                    plugin.getRaceManager().setRaceNoCooldown(target, race);
                    sender.sendMessage(MessageUtils.withPrefix("&aقوم " + target.getName() + " به " + race.getPersianName() + " تنظیم شد! 🇮🇷"));
                } else if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("پاک")) {
                    if (args.length < 3) {
                        sender.sendMessage(MessageUtils.color("&cاستفاده: /race admin clear <بازیکن>"));
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        sender.sendMessage(MessageUtils.withPrefix("&cبازیکن یافت نشد!"));
                        return true;
                    }
                    plugin.getRaceManager().clearRace(target);
                    sender.sendMessage(MessageUtils.withPrefix("&aقوم " + target.getName() + " پاک شد!"));
                } else if (args[1].equalsIgnoreCase("reload")) {
                    plugin.getConfigManager().reload();
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.reloaded", "&aریلود شد!"));
                }
                break;

            default:
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
        sender.sendMessage(MessageUtils.color("&6&l🇮🇷 دستورات اقوام ایرانی - هر بایوم یک قوم"));
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        sender.sendMessage(MessageUtils.color("&e/race choose &7یا &e/قوم &7- باز کردن منوی انتخاب قوم ایرانی (14 قوم)"));
        sender.sendMessage(MessageUtils.color("&e/race info [قوم/بازیکن] &7- اطلاعات قوم ایرانی"));
        sender.sendMessage(MessageUtils.color("&e/race change &7- تغییر قوم (هر 7 روز)"));
        sender.sendMessage(MessageUtils.color("&e/race list &7یا &e/اقوام &7- لیست تمام اقوام ایرانی"));
        sender.sendMessage(MessageUtils.color("&e/race help &7- راهنما"));
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        sender.sendMessage(MessageUtils.color("&6&lاقوام ایرانی:"));
        sender.sendMessage(MessageUtils.color("&7فارس، آذری، کرد، لر، بلوچ، عرب، ترکمن، گیلک، مازنی، بختیاری، قشقایی، بندری، خراسانی، سیستانی"));
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        if (sender.hasPermission("iranianhardcore.race.admin")) {
            sender.sendMessage(MessageUtils.color("&c/race admin set <بازیکن> <قوم> &7- تنظیم قوم"));
            sender.sendMessage(MessageUtils.color("&c/race admin clear <بازیکن> &7- پاک کردن قوم"));
        }
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
    }

    private void sendRaceDetail(Player player, RaceType race) {
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&6&l🇮🇷 اطلاعات قوم ایرانی: " + race.getPersianName()));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        for (String line : race.getLore()) {
            player.sendMessage(MessageUtils.color(line));
        }
        player.sendMessage(MessageUtils.color("&7بایوم‌های خانه (قدرت کامل): &a" + race.getHomeBiomes().size() + " عدد"));
        for (org.bukkit.block.Biome b : race.getHomeBiomes()) {
            player.sendMessage(MessageUtils.color("&8- &a" + b.name()));
        }
        player.sendMessage(MessageUtils.color("&7بایوم‌های دشمن (ضعف): &c" + race.getHostileBiomes().size() + " عدد"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = Arrays.asList("choose", "info", "change", "list", "help", "admin", "انتخاب", "اطلاعات", "تغییر", "لیست", "اقوام", "قوم");
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
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("اطلاعات")) {
                for (RaceType race : RaceType.values()) {
                    if (race.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(race.getId());
                    }
                }
            } else if (args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("ادمین")) {
                List<String> adminSubs = Arrays.asList("set", "clear", "reload", "پاک");
                for (String s : adminSubs) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(s);
                    }
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("set")) {
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

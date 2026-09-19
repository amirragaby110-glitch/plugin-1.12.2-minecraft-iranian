package ir.iranian.hardcore.commands;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.dungeons.DungeonType;
import ir.iranian.hardcore.structures.PersianStructures;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * دستورات دانجن‌های ایرانی - کاملا فارسی
 * /dungeon list - لیست دانجن‌ها
 * /dungeon generate <type> - ساخت دانجن نزدیک
 * /dungeon teleport <id> - تلپورت به دانجن
 * /dungeon clear - پاک کردن همه دانجن‌ها
 * /dungeon bazaar - ساخت بازار ایرانی
 * /dungeon caravanserai - ساخت کاروانسرا
 */
public class DungeonCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public DungeonCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("iranianhardcore.dungeon")) {
            sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ندارید!"));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "list":
            case "لیست":
                if (sender instanceof Player) {
                    plugin.getDungeonManager().listDungeons((Player) sender);
                } else {
                    sender.sendMessage(MessageUtils.color("&6&l لیست دانجن‌های ایرانی:"));
                    for (DungeonType type : DungeonType.values()) {
                        sender.sendMessage(MessageUtils.color("&7- " + type.getPersianName() + " (" + type.getId() + ")"));
                    }
                }
                break;

            case "generate":
            case "ساخت":
            case "create":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cفقط بازیکنان!"));
                    return true;
                }
                Player player = (Player) sender;
                if (args.length < 2) {
                    player.sendMessage(MessageUtils.withPrefix("&cاستفاده: /dungeon generate <نوع>"));
                    player.sendMessage(MessageUtils.withPrefix("&7مثال: /dungeon generate ALAMUT_CASTLE"));
                    return true;
                }
                DungeonType type = DungeonType.fromId(args[1]);
                if (type == null) {
                    player.sendMessage(MessageUtils.withPrefix("&cدانجن یافت نشد! /dungeon list"));
                    return true;
                }
                plugin.getDungeonManager().generateNearPlayer(player, type);
                break;

            case "teleport":
            case "tp":
            case "تلپورت":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cفقط بازیکنان!"));
                    return true;
                }
                Player tpPlayer = (Player) sender;
                if (plugin.getDungeonManager().getGeneratedDungeons().isEmpty()) {
                    tpPlayer.sendMessage(MessageUtils.withPrefix("&cهنوز دانجنی ساخته نشده!"));
                    return true;
                }
                if (args.length < 2) {
                    // لیست دانجن‌های ساخته شده
                    tpPlayer.sendMessage(MessageUtils.color("&6&l دانجن‌های ساخته شده:"));
                    int i = 0;
                    for (Map.Entry<String, Location> entry : plugin.getDungeonManager().getGeneratedDungeons().entrySet()) {
                        Location loc = entry.getValue();
                        tpPlayer.sendMessage(MessageUtils.color("&7" + i + ": &e" + entry.getKey() + " &7در &f" + loc.getBlockX() + "," + loc.getBlockZ()));
                        i++;
                        if (i > 10) break;
                    }
                    return true;
                }
                try {
                    int index = Integer.parseInt(args[1]);
                    List<Location> locs = new ArrayList<>(plugin.getDungeonManager().getGeneratedDungeons().values());
                    if (index >= 0 && index < locs.size()) {
                        Location target = locs.get(index).clone().add(0, 5, 0);
                        tpPlayer.teleport(target);
                        tpPlayer.sendMessage(MessageUtils.withPrefix("&aبه دانجن تلپورت شدی!"));
                    } else {
                        tpPlayer.sendMessage(MessageUtils.withPrefix("&cایندکس نامعتبر!"));
                    }
                } catch (NumberFormatException e) {
                    tpPlayer.sendMessage(MessageUtils.withPrefix("&cعدد وارد کن!"));
                }
                break;

            case "clear":
            case "پاک":
                if (!sender.hasPermission("iranianhardcore.admin")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ادمین لازم است!"));
                    return true;
                }
                plugin.getDungeonManager().clearAll();
                sender.sendMessage(MessageUtils.withPrefix("&aتمام دانجن‌های ثبت شده پاک شد! (سازه‌ها باقی می‌مانند)"));
                break;

            case "bazaar":
            case "بازار":
                if (!(sender instanceof Player)) return true;
                Player bazaarPlayer = (Player) sender;
                Location bazaarLoc = bazaarPlayer.getLocation();
                bazaarLoc.setY(bazaarLoc.getWorld().getHighestBlockYAt(bazaarLoc));
                plugin.getPersianStructures().buildBazaar(bazaarLoc);
                bazaarPlayer.sendMessage(MessageUtils.withPrefix("&aبازار ایرانی ساخته شد! &e" + PersianStructures.StructureType.BAZAAR.getPersianName()));
                break;

            case "caravanserai":
            case "کاروانسرا":
                if (!(sender instanceof Player)) return true;
                Player caraPlayer = (Player) sender;
                Location caraLoc = caraPlayer.getLocation();
                caraLoc.setY(caraLoc.getWorld().getHighestBlockYAt(caraLoc));
                plugin.getPersianStructures().buildCaravanserai(caraLoc);
                caraPlayer.sendMessage(MessageUtils.withPrefix("&aکاروانسرای شاه عباسی ساخته شد!"));
                break;

            case "chaikhaneh":
            case "چایخانه":
                if (!(sender instanceof Player)) return true;
                Player chaiPlayer = (Player) sender;
                Location chaiLoc = chaiPlayer.getLocation();
                chaiLoc.setY(chaiLoc.getWorld().getHighestBlockYAt(chaiLoc));
                plugin.getPersianStructures().buildChaikhaneh(chaiLoc);
                chaiPlayer.sendMessage(MessageUtils.withPrefix("&aچایخانه سنتی ساخته شد!"));
                break;

            case "abanbar":
            case "آبانبار":
                if (!(sender instanceof Player)) return true;
                Player abPlayer = (Player) sender;
                Location abLoc = abPlayer.getLocation();
                abLoc.setY(abPlayer.getWorld().getHighestBlockYAt(abLoc));
                plugin.getPersianStructures().buildAbAnbar(abLoc);
                abPlayer.sendMessage(MessageUtils.withPrefix("&aآب‌انبار یزدی ساخته شد!"));
                break;

            case "help":
            case "راهنما":
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
        sender.sendMessage(MessageUtils.color("&6&l دستورات دانجن‌های ایرانی - کاملا فارسی"));
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        sender.sendMessage(MessageUtils.color("&e/dungeon list &7- لیست تمام دانجن‌های ایرانی"));
        sender.sendMessage(MessageUtils.color("&e/dungeon generate <نوع> &7- ساخت دانجن نزدیک شما"));
        sender.sendMessage(MessageUtils.color("&e/dungeon teleport [شماره] &7- تلپورت به دانجن"));
        sender.sendMessage(MessageUtils.color("&e/dungeon clear &7- پاک کردن لیست دانجن‌ها (ادمین)"));
        sender.sendMessage(MessageUtils.color("&e/dungeon bazaar &7- ساخت بازار ایرانی"));
        sender.sendMessage(MessageUtils.color("&e/dungeon caravanserai &7- ساخت کاروانسرای شاه عباسی"));
        sender.sendMessage(MessageUtils.color("&e/dungeon chaikhaneh &7- ساخت چایخانه سنتی"));
        sender.sendMessage(MessageUtils.color("&e/dungeon abanbar &7- ساخت آب‌انبار یزدی"));
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        sender.sendMessage(MessageUtils.color("&7انواع دانجن:"));
        for (DungeonType type : DungeonType.values()) {
            sender.sendMessage(MessageUtils.color("&8- &6" + type.getPersianName() + " &7(" + type.getId() + ")"));
        }
        sender.sendMessage(MessageUtils.color("&8&l&m-------------------"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = Arrays.asList("list", "generate", "teleport", "clear", "bazaar", "caravanserai", "chaikhaneh", "abanbar", "help");
            for (String s : subs) {
                if (s.startsWith(args[0].toLowerCase())) list.add(s);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("generate")) {
                for (DungeonType type : DungeonType.values()) {
                    if (type.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                        list.add(type.getId());
                    }
                }
            }
        }
        return list;
    }
}

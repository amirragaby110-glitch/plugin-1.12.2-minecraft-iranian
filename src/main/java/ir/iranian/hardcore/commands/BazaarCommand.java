package ir.iranian.hardcore.commands;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * دستور بازار ایرانی - کاملا فارسی - نسخه 3.0
 * /bazaar - باز کردن بازار ایرانی
 * /bazaar <type> - ساخت سازه بازار
 */
public class BazaarCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public BazaarCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.color("&cفقط بازیکنان ایرانی می‌توانند وارد بازار شوند!"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // باز کردن بازار اصلی
            plugin.getPersianBazaar().openMainBazaar(player);
            player.sendMessage(MessageUtils.withPrefix("&aبه بازار ایرانی خوش آمدید! &7مثل بازار بزرگ تهران"));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "open":
            case "باز":
            case "بازار":
                plugin.getPersianBazaar().openMainBazaar(player);
                break;

            case "build":
            case "ساخت":
                if (!player.hasPermission("iranianhardcore.admin")) {
                    player.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cدسترسی ادمین لازم است!"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(MessageUtils.withPrefix("&cاستفاده: /bazaar build <bazaar|caravanserai|chaikhaneh|abanbar|mosque|village>"));
                    return true;
                }
                String type = args[1].toLowerCase();
                switch (type) {
                    case "bazaar":
                    case "بازار":
                        plugin.getPersianStructures().buildBazaar(player.getLocation());
                        player.sendMessage(MessageUtils.withPrefix("&aبازار ایرانی ساخته شد!"));
                        break;
                    case "caravanserai":
                    case "کاروانسرا":
                        plugin.getPersianStructures().buildCaravanserai(player.getLocation());
                        player.sendMessage(MessageUtils.withPrefix("&aکاروانسرای شاه عباسی ساخته شد!"));
                        break;
                    case "chaikhaneh":
                    case "چایخانه":
                        plugin.getPersianStructures().buildChaikhaneh(player.getLocation());
                        player.sendMessage(MessageUtils.withPrefix("&aچایخانه سنتی ساخته شد!"));
                        break;
                    case "abanbar":
                    case "آبانبار":
                        plugin.getPersianStructures().buildAbAnbar(player.getLocation());
                        player.sendMessage(MessageUtils.withPrefix("&aآب‌انبار یزدی ساخته شد!"));
                        break;
                    case "village":
                    case "روستا":
                        plugin.getVillageManager().buildIranianVillage(player.getLocation());
                        player.sendMessage(MessageUtils.withPrefix("&aروستای ایرانی ساخته شد!"));
                        break;
                    default:
                        player.sendMessage(MessageUtils.withPrefix("&cنوع سازه نامعتبر!"));
                        break;
                }
                break;

            case "help":
            case "راهنما":
                sendHelp(player);
                break;

            default:
                plugin.getPersianBazaar().openMainBazaar(player);
                break;
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&6&l🏪 بازار ایرانی - راهنما - کاملا فارسی"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&e/bazaar &7- باز کردن بازار ایرانی"));
        player.sendMessage(MessageUtils.color("&e/bazaar build bazaar &7- ساخت بازار"));
        player.sendMessage(MessageUtils.color("&e/bazaar build caravanserai &7- کاروانسرا"));
        player.sendMessage(MessageUtils.color("&e/bazaar build chaikhaneh &7- چایخانه"));
        player.sendMessage(MessageUtils.color("&e/bazaar build abanbar &7- آب‌انبار"));
        player.sendMessage(MessageUtils.color("&e/bazaar build village &7- روستای ایرانی"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&7واحد پول: &eسکه دریک هخامنشی (GOLD_NUGGET)"));
        player.sendMessage(MessageUtils.color("&7سکه را از دانجن‌ها پیدا کنید!"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = Arrays.asList("open", "build", "help", "بازار", "ساخت", "راهنما");
            for (String s : subs) {
                if (s.startsWith(args[0].toLowerCase())) list.add(s);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("build") || args[0].equalsIgnoreCase("ساخت")) {
                List<String> types = Arrays.asList("bazaar", "caravanserai", "chaikhaneh", "abanbar", "village");
                for (String s : types) {
                    if (s.startsWith(args[1].toLowerCase())) list.add(s);
                }
            }
        }
        return list;
    }
}

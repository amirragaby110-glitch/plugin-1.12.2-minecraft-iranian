package ir.iranian.hardcore.commands;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.structures.PersianStructures;
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
 * Dastor Bazar Irani - v5.0 Finglish
 * /bazaar - Baz kardane Bazar Irani
 * /bazaar build <type> - Sakhtane saze bazar
 */
public class BazaarCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public BazaarCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.getConfigManager().getBoolean("bazaar.enabled", true)) {
            sender.sendMessage(MessageUtils.withPrefix("&cBazar Irani gheyre faal ast!"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.color("&cFaghat bazikonan mitavanand vared-e bazar shavand!"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Open main bazaar
            plugin.getPersianBazaar().openMainMenu(player);
            player.sendMessage(MessageUtils.withPrefix("&aBe Bazar Irani khosh amadid! &7Mesle Bazar Bozorge Tehran"));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "open":
            case "bazar":
                plugin.getPersianBazaar().openMainMenu(player);
                return true;

            case "build":
                if (!player.hasPermission("iranianhardcore.admin")) {
                    player.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi admin lazem ast!"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(MessageUtils.withPrefix("&cEstefadeh: /bazaar build <bazaar|caravanserai|chaikhaneh|abanbar|mosque|village>"));
                    return true;
                }
                String struct = args[1].toLowerCase();
                switch (struct) {
                    case "bazaar":
                        plugin.getPersianStructures().forceBuildStructure(player.getLocation(), PersianStructures.StructureType.BAZAAR);
                        player.sendMessage(MessageUtils.withPrefix("&aBazar Irani sakhte shod!"));
                        break;
                    case "caravanserai":
                        plugin.getPersianStructures().forceBuildStructure(player.getLocation(), PersianStructures.StructureType.CARAVANSERAI);
                        player.sendMessage(MessageUtils.withPrefix("&aKarvansaraye Shah Abbasi sakhte shod!"));
                        break;
                    case "chaikhaneh":
                        plugin.getPersianStructures().forceBuildStructure(player.getLocation(), PersianStructures.StructureType.CHAIKHANEH);
                        player.sendMessage(MessageUtils.withPrefix("&aChaykhane sonati sakhte shod!"));
                        break;
                    case "abanbar":
                        plugin.getPersianStructures().forceBuildStructure(player.getLocation(), PersianStructures.StructureType.AB_ANBAR);
                        player.sendMessage(MessageUtils.withPrefix("&aAb Anbare Yazdi sakhte shod!"));
                        break;
                    case "village":
                        plugin.getPersianStructures().forceBuildStructure(player.getLocation(), PersianStructures.StructureType.VILLAGE);
                        player.sendMessage(MessageUtils.withPrefix("&aRoostaye Irani sakhte shod!"));
                        break;
                    default:
                        player.sendMessage(MessageUtils.withPrefix("&cNoe saze nameetabar!"));
                        break;
                }
                return true;

            case "help":
            default:
                sendHelp(player);
                return true;
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        player.sendMessage(MessageUtils.color("&6&lBazar Irani - Rahnama - Finglish"));
        player.sendMessage(MessageUtils.color("&e/bazaar &7- Baz kardane Bazar Irani"));
        player.sendMessage(MessageUtils.color("&e/bazaar build bazaar &7- Sakhtane bazar"));
        player.sendMessage(MessageUtils.color("&e/bazaar build caravanserai &7- Karvansara"));
        player.sendMessage(MessageUtils.color("&e/bazaar build chaikhaneh &7- Chaykhane"));
        player.sendMessage(MessageUtils.color("&e/bazaar build abanbar &7- Ab Anbar"));
        player.sendMessage(MessageUtils.color("&e/bazaar build village &7- Roostaye Irani"));
        player.sendMessage(MessageUtils.color("&7Vahede pool: &eSekke Derik Hakhamaneshi (GOLD_NUGGET)"));
        player.sendMessage(MessageUtils.color("&7Sekke ra az dungeonha peyda konid!"));
        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = Arrays.asList("open", "build", "help");
            for (String s : subs) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(s);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("build")) {
                List<String> structs = Arrays.asList("bazaar", "caravanserai", "chaikhaneh", "abanbar", "mosque", "village");
                for (String s : structs) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(s);
                    }
                }
            }
        }
        return completions;
    }
}

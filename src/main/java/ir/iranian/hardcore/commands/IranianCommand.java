package ir.iranian.hardcore.commands;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.mobs.CustomMobType;
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
 * Iranian Command - v4.0
 * /iranian - Main command for new features
 * Finglish compatible
 */
public class IranianCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public IranianCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "thirst":
            case "teshnegi":
                double thirst = plugin.getThirstManager().getThirst(player);
                player.sendMessage(MessageUtils.withPrefix("&bTeshnegi shoma: &f" + String.format("%.0f", thirst) + "%"));
                if (thirst < 20) {
                    player.sendMessage(MessageUtils.withPrefix("&cKhatar! Bayad ab benoshid!"));
                }
                return true;

            case "temperature":
            case "dama":
                double temp = plugin.getTemperatureManager().getTemperature(player);
                player.sendMessage(MessageUtils.withPrefix("&bDama badan shoma: &f" + String.format("%.0f", temp)));
                player.sendMessage(MessageUtils.withPrefix("&7Biome: &f" + player.getLocation().getBlock().getBiome().name()));
                return true;

            case "drink":
            case "ab":
                if (plugin.getThirstManager().drinkWater(player, 25)) {
                    // Remove water bottle if in hand
                    if (player.getInventory().getItemInMainHand() != null &&
                            player.getInventory().getItemInMainHand().getType().name().contains("POTION")) {
                        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                    }
                }
                return true;

            case "spawnmob":
            case "mob":
                if (!player.hasPermission("iranian.admin")) {
                    player.sendMessage(MessageUtils.withPrefix("&cShoma dastresi admin nadarid!"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(MessageUtils.withPrefix("&cEstefade: /iranian spawnmob <DIV_SEPID|DIV_SIAH|SIMURGH|ZAHHAK|ROSTAM_GHOST|AL|KAVEH>"));
                    return true;
                }
                plugin.getMobsManager().spawnMobCommand(player, args[1]);
                return true;

            case "resourcepack":
            case "pack":
                plugin.getResourcePackManager().sendResourcePack(player);
                return true;

            case "resourcepackinfo":
            case "packinfo":
                plugin.getResourcePackManager().generateResourcePackInfo(player);
                return true;

            case "mashk":
                player.getInventory().addItem(plugin.getThirstManager().createMashkAb());
                player.sendMessage(MessageUtils.withPrefix("&aMashk Ab gereftid!"));
                return true;

            case "help":
            case "komak":
                sendHelp(player);
                return true;

            default:
                sendHelp(player);
                return true;
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&6&l🇮🇷 Iranian Hardcore v4.0 - Finglish"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&e/iranian thirst &7- Didan mizan teshnegi"));
        player.sendMessage(MessageUtils.color("&e/iranian temperature &7- Didan dama badan"));
        player.sendMessage(MessageUtils.color("&e/iranian drink &7- Noshidan ab (+25% teshnegi)"));
        player.sendMessage(MessageUtils.color("&e/iranian mashk &7- Gereftan Mashk Ab Irani"));
        player.sendMessage(MessageUtils.color("&e/iranian resourcepack &7- Daryaft resource pack Irani"));
        player.sendMessage(MessageUtils.color("&e/iranian spawnmob <type> &7- Sakht mob Irani (admin)"));
        player.sendMessage(MessageUtils.color("&7Types: DIV_SEPID, DIV_SIAH, SIMURGH, ZAHHAK, ROSTAM_GHOST, AL, KAVEH"));
        player.sendMessage(MessageUtils.color("&e/race choose &7- Entekhab ghome Irani"));
        player.sendMessage(MessageUtils.color("&e/bazaar &7- Bazar Irani"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&6Zende bad Iran! 🇮🇷"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(Arrays.asList("thirst", "temperature", "drink", "mashk", "resourcepack", "spawnmob", "help"));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("spawnmob")) {
            for (CustomMobType type : CustomMobType.values()) {
                completions.add(type.name());
            }
        }
        return completions;
    }
}

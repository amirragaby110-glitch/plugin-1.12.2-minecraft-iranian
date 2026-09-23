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
 * Dastorate ghome Irani - Har biome yek ghom - v5.0 Finglish
 * /race choose - Entekhab ghome Irani (GUI 54 slot)
 * /race info - Etelaat ghom
 * /race change - Taghir ghom har 7 rooz
 * /race list - List tamam aghvam
 */
public class RaceCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public RaceCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.getConfigManager().getBoolean("race.enabled", true)) {
            sender.sendMessage(MessageUtils.withPrefix("&cSystem aghvam gheyre faal ast!"));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "choose":
            case "gui":
            case "menu":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cFaghat bazikonan mitavanand ghom entekhab konand!"));
                    return true;
                }
                Player player = (Player) sender;
                if (!player.hasPermission("iranianhardcore.race.choose")) {
                    player.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi nadarid!"));
                    return true;
                }
                plugin.getRaceGUI().openRaceSelectionGUI(player);
                player.sendMessage(MessageUtils.withPrefix("&aMenuye aghvame Irani baz shod! &7Yeki az aghvame asile Iran ra entekhab konid"));
                return true;

            case "info":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cFaghat bazikonan!"));
                    return true;
                }
                Player infoPlayer = (Player) sender;
                if (!infoPlayer.hasPermission("iranianhardcore.race.info")) {
                    infoPlayer.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi nadarid!"));
                    return true;
                }
                if (args.length == 1) {
                    plugin.getRaceManager().sendRaceInfo(infoPlayer);
                } else {
                    String targetName = args[1];
                    RaceType targetRace = RaceType.fromId(targetName.toUpperCase());
                    if (targetRace != null) {
                        sendSpecificRaceInfo(infoPlayer, targetRace);
                    } else {
                        Player target = Bukkit.getPlayer(targetName);
                        if (target != null) {
                            RaceType race = plugin.getRaceManager().getPlayerRace(target);
                            if (race == null) {
                                infoPlayer.sendMessage(MessageUtils.withPrefix("&cBazikon " + target.getName() + " hanooz ghom entekhab nakarde!"));
                            } else {
                                infoPlayer.sendMessage(MessageUtils.withPrefix("&7Ghome " + target.getName() + ": &6" + race.getFinglishName() + " &7- Biome: " + target.getLocation().getBlock().getBiome().name()));
                            }
                        } else {
                            infoPlayer.sendMessage(MessageUtils.withPrefix("&cGhom ya bazikon yaft nashod! /race list"));
                        }
                    }
                }
                return true;

            case "change":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cFaghat bazikonan!"));
                    return true;
                }
                Player changePlayer = (Player) sender;
                if (!changePlayer.hasPermission("iranianhardcore.race.change")) {
                    changePlayer.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi nadarid!"));
                    return true;
                }
                if (plugin.getRaceManager().getPlayerRace(changePlayer) == null) {
                    changePlayer.sendMessage(MessageUtils.withPrefix("&cShoma hanooz ghome Irani nadarid! &e/race choose"));
                    return true;
                }
                if (!plugin.getRaceManager().canChangeRace(changePlayer)) {
                    long leftDays = plugin.getRaceManager().getRaceChangeCooldownLeftDays(changePlayer);
                    String msg = plugin.getConfigManager().getString("race.change-cooldown-message", "&cBayad %days% rooz digar sabr konid.");
                    changePlayer.sendMessage(MessageUtils.withPrefix(msg.replace("%days%", String.valueOf(leftDays))));
                    return true;
                }
                plugin.getRaceGUI().openRaceSelectionGUI(changePlayer);
                return true;

            case "list":
            case "all":
                sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                sender.sendMessage(MessageUtils.color("&6&lList Aghvame Irani - Har Biome Yek Ghom:"));
                for (RaceType race : RaceType.values()) {
                    sender.sendMessage(MessageUtils.color("&7- &6" + race.getFinglishName() + " &7(" + race.getId() + ") &8- &f" + race.getEnglishName() + " &7| Biome: " + race.getHomeBiomes().get(0).name()));
                }
                sender.sendMessage(MessageUtils.color("&7Tedad: &a" + RaceType.values().length + " ghome Irani"));
                sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                return true;

            case "help":
                sendHelp(sender);
                return true;

            case "admin":
                if (!sender.hasPermission("iranianhardcore.race.admin")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi nadarid!"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(MessageUtils.color("&cEstefadeh: /race admin set <bazikon> <ghom>"));
                    sender.sendMessage(MessageUtils.color("&cMesal: /race admin set Amir FARS"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("set")) {
                    if (args.length < 4) {
                        sender.sendMessage(MessageUtils.color("&cEstefadeh: /race admin set <bazikon> <ghom>"));
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        sender.sendMessage(MessageUtils.withPrefix("&cBazikon yaft nashod!"));
                        return true;
                    }
                    RaceType race = RaceType.fromId(args[3].toUpperCase());
                    if (race == null) {
                        sender.sendMessage(MessageUtils.withPrefix("&cGhome nameetabar! List: /race list"));
                        return true;
                    }
                    plugin.getRaceManager().setPlayerRaceByAdmin(target, race);
                    sender.sendMessage(MessageUtils.withPrefix("&aGhome " + target.getName() + " be " + race.getFinglishName() + " tanzim shod!"));
                } else if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("reset")) {
                    if (args.length < 3) {
                        sender.sendMessage(MessageUtils.color("&cEstefadeh: /race admin clear <bazikon>"));
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        sender.sendMessage(MessageUtils.withPrefix("&cBazikon yaft nashod!"));
                        return true;
                    }
                    plugin.getRaceManager().removePlayerRace(target);
                    sender.sendMessage(MessageUtils.withPrefix("&aGhome " + target.getName() + " pak shod!"));
                } else if (args[1].equalsIgnoreCase("reload")) {
                    plugin.getConfigManager().reload();
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.reloaded", "&aReload shod!"));
                }
                return true;

            default:
                RaceType directRace = RaceType.fromId(sub.toUpperCase());
                if (directRace != null && sender instanceof Player) {
                    Player p = (Player) sender;
                    if (!plugin.getRaceManager().canChangeRace(p)) {
                        long leftDays = plugin.getRaceManager().getRaceChangeCooldownLeftDays(p);
                        String msg = plugin.getConfigManager().getString("race.change-cooldown-message", "&cBayad %days% rooz digar sabr konid.");
                        p.sendMessage(MessageUtils.withPrefix(msg.replace("%days%", String.valueOf(leftDays))));
                        return true;
                    }
                    plugin.getRaceManager().setPlayerRace(p, directRace);
                    plugin.getRaceManager().recordRaceChange(p);
                    return true;
                }
                sendHelp(sender);
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        sender.sendMessage(MessageUtils.color("&6&lDastorate Aghvame Irani - Har Biome Yek Ghom:"));
        sender.sendMessage(MessageUtils.color("&e/race choose &7- Baz kardane menuye entekhabe ghome Irani (14 ghom)"));
        sender.sendMessage(MessageUtils.color("&e/race info [ghom/bazikon] &7- Etelaate ghome Irani"));
        sender.sendMessage(MessageUtils.color("&e/race change &7- Taghir ghom (har 7 rooz)"));
        sender.sendMessage(MessageUtils.color("&e/race list &7- List tamam aghvame Irani"));
        sender.sendMessage(MessageUtils.color("&e/race help &7- Rahnama"));
        sender.sendMessage(MessageUtils.color("&6Aghvame Irani:"));
        sender.sendMessage(MessageUtils.color("&7Pars, Azari, Kurd, Lor, Baloch, Arab, Turkmen, Gilak, Mazani, Bakhtiari, Qashqayi, Bandari, Khorasani, Sistani"));
        if (sender.hasPermission("iranianhardcore.race.admin")) {
            sender.sendMessage(MessageUtils.color("&c/race admin set <bazikon> <ghom> &7- Tanzime ghom"));
            sender.sendMessage(MessageUtils.color("&c/race admin clear <bazikon> &7- Pak kardane ghom"));
        }
        sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
    }

    private void sendSpecificRaceInfo(Player player, RaceType race) {
        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        player.sendMessage(MessageUtils.color("&6&lEtelaat Ghome Irani: " + race.getFinglishName()));
        for (String lore : race.getLoreFinglish()) {
            player.sendMessage(MessageUtils.color(lore));
        }
        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        player.sendMessage(MessageUtils.color("&7Biomehaye khane (ghodrat kamel): &a" + race.getHomeBiomes().size() + " adad"));
        for (org.bukkit.block.Biome b : race.getHomeBiomes()) {
            player.sendMessage(MessageUtils.color("  &a✔ &7" + b.name()));
        }
        player.sendMessage(MessageUtils.color("&7Biomehaye doshman (zaaf): &c" + race.getHostileBiomes().size() + " adad"));
        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
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
            for (RaceType type : RaceType.values()) {
                if (type.getId().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(type.getId());
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                for (RaceType type : RaceType.values()) {
                    if (type.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(type.getId());
                    }
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(p.getName());
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
            if (args[0].equalsIgnoreCase("admin") && (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("clear"))) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                        completions.add(p.getName());
                    }
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("set")) {
                for (RaceType type : RaceType.values()) {
                    if (type.getId().toLowerCase().startsWith(args[3].toLowerCase())) {
                        completions.add(type.getId());
                    }
                }
            }
        }

        return completions;
    }
}

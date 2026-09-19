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
 * Dastorate dungeonhaye Irani - v5.0 Finglish
 * /dungeon list - List 30 dungeon tarikhi
 * /dungeon generate <type> - Sakhtane dungeon nazdik
 * /dungeon teleport <id> - Teleport be dungeon
 * /dungeon clear - Pak kardane dadeha
 * /dungeon bazaar - Sakhtane bazar Irani
 * /dungeon caravanserai - Sakhtane karvansara
 */
public class DungeonCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public DungeonCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("iranianhardcore.dungeon")) {
            sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi nadarid!"));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "list":
                sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                sender.sendMessage(MessageUtils.color("&6&lList 30 Dungeon Tarikhi Iran:"));
                for (DungeonType type : DungeonType.values()) {
                    sender.sendMessage(MessageUtils.color("&7- &e" + type.name() + " &7: &a" + type.getFinglishName() + " &7(&b" + type.getBiomeName() + "&7) Boss: &c" + type.getBossName()));
                }
                sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                return true;

            case "generate":
            case "spawn":
            case "create":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cFaghat bazikonan!"));
                    return true;
                }
                Player player = (Player) sender;
                if (args.length < 2) {
                    player.sendMessage(MessageUtils.withPrefix("&cEstefadeh: /dungeon generate <type>"));
                    player.sendMessage(MessageUtils.withPrefix("&7Mesal: /dungeon generate ALAMUT_CASTLE"));
                    return true;
                }
                DungeonType type = DungeonType.fromName(args[1]);
                if (type == null) {
                    player.sendMessage(MessageUtils.withPrefix("&cDungeon yaft nashod! /dungeon list"));
                    return true;
                }
                plugin.getDungeonManager().forceGenerateDungeon(player, type);
                return true;

            case "teleport":
            case "tp":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cFaghat bazikonan!"));
                    return true;
                }
                Player tpPlayer = (Player) sender;
                Map<String, Location> dungeons = plugin.getDungeonManager().getGeneratedDungeons();
                if (dungeons.isEmpty()) {
                    tpPlayer.sendMessage(MessageUtils.withPrefix("&cHanooz dungeoni sakhte nashode!"));
                    return true;
                }
                if (args.length < 2) {
                    tpPlayer.sendMessage(MessageUtils.color("&6&lDungeonhaye sakhte shode:"));
                    int i = 0;
                    for (Map.Entry<String, Location> entry : dungeons.entrySet()) {
                        Location loc = entry.getValue();
                        tpPlayer.sendMessage(MessageUtils.color("&7" + i + ": &e" + entry.getKey() + " &7dar &f" + loc.getBlockX() + "," + loc.getBlockZ()));
                        i++;
                    }
                    tpPlayer.sendMessage(MessageUtils.withPrefix("&7Estefadeh: /dungeon teleport <shomare>"));
                    return true;
                }
                try {
                    int index = Integer.parseInt(args[1]);
                    List<Location> locs = new ArrayList<>(dungeons.values());
                    if (index >= 0 && index < locs.size()) {
                        tpPlayer.teleport(locs.get(index));
                        tpPlayer.sendMessage(MessageUtils.withPrefix("&aBe dungeon teleport shodid!"));
                    } else {
                        tpPlayer.sendMessage(MessageUtils.withPrefix("&cIndexe nameetabar!"));
                    }
                } catch (NumberFormatException e) {
                    tpPlayer.sendMessage(MessageUtils.withPrefix("&cLotfan adad vared konid!"));
                }
                return true;

            case "clear":
                if (!sender.hasPermission("iranianhardcore.admin")) {
                    sender.sendMessage(MessageUtils.getPrefixedMessage("messages.no-permission", "&cDastresi admin lazem ast!"));
                    return true;
                }
                plugin.getDungeonManager().clearGeneratedDungeons();
                sender.sendMessage(MessageUtils.withPrefix("&aTamam dungeonhaye sabt shode pak shod!"));
                return true;

            case "bazaar":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cFaghat bazikonan!"));
                    return true;
                }
                Player bazaarPlayer = (Player) sender;
                plugin.getPersianStructures().forceBuildStructure(bazaarPlayer.getLocation(), PersianStructures.StructureType.BAZAAR);
                bazaarPlayer.sendMessage(MessageUtils.withPrefix("&aBazar Irani sakhte shod!"));
                return true;

            case "caravanserai":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cFaghat bazikonan!"));
                    return true;
                }
                Player caraPlayer = (Player) sender;
                plugin.getPersianStructures().forceBuildStructure(caraPlayer.getLocation(), PersianStructures.StructureType.CARAVANSERAI);
                caraPlayer.sendMessage(MessageUtils.withPrefix("&aKarvansaraye Shah Abbasi sakhte shod!"));
                return true;

            case "chaikhaneh":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cFaghat bazikonan!"));
                    return true;
                }
                Player chaiPlayer = (Player) sender;
                plugin.getPersianStructures().forceBuildStructure(chaiPlayer.getLocation(), PersianStructures.StructureType.CHAIKHANEH);
                chaiPlayer.sendMessage(MessageUtils.withPrefix("&aChaykhane sonati sakhte shod!"));
                return true;

            case "abanbar":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.color("&cFaghat bazikonan!"));
                    return true;
                }
                Player abPlayer = (Player) sender;
                plugin.getPersianStructures().forceBuildStructure(abPlayer.getLocation(), PersianStructures.StructureType.AB_ANBAR);
                abPlayer.sendMessage(MessageUtils.withPrefix("&aAb Anbare Yazdi sakhte shod!"));
                return true;

            case "help":
            default:
                sendHelp(sender);
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        sender.sendMessage(MessageUtils.color("&6&lDastorate 30 Dungeon Tarikhi Iran:"));
        sender.sendMessage(MessageUtils.color("&e/dungeon list &7- List tamam 30 dungeon"));
        sender.sendMessage(MessageUtils.color("&e/dungeon generate <type> &7- Sakhtane dungeon nazdik shoma"));
        sender.sendMessage(MessageUtils.color("&e/dungeon teleport [shomare] &7- Teleport be dungeon"));
        sender.sendMessage(MessageUtils.color("&e/dungeon clear &7- Pak kardane list"));
        sender.sendMessage(MessageUtils.color("&e/dungeon bazaar &7- Sakhtane bazar"));
        sender.sendMessage(MessageUtils.color("&e/dungeon caravanserai &7- Sakhtane karvansara"));
        sender.sendMessage(MessageUtils.color("&e/dungeon chaikhaneh &7- Sakhtane chaykhane"));
        sender.sendMessage(MessageUtils.color("&e/dungeon abanbar &7- Sakhtane Ab Anbar"));
        sender.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = Arrays.asList("list", "generate", "teleport", "clear", "bazaar", "caravanserai", "chaikhaneh", "abanbar", "help");
            for (String s : subs) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(s);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("generate")) {
            for (DungeonType type : DungeonType.values()) {
                if (type.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(type.name());
                }
            }
        }
        return completions;
    }
}

package ir.iranian.hardcore.commands;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.army.Army;
import ir.iranian.hardcore.army.Formation;
import ir.iranian.hardcore.army.Soldier;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command Executor for Persian Military Army System (/army).
 */
public class ArmyCommand implements CommandExecutor {

    private final IranianHardcorePlugin plugin;

    public ArmyCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("In dastoor faghat baraye bazikonan ast!");
            return true;
        }

        Player player = (Player) sender;
        if (plugin.getArmyManager() == null) {
            player.sendMessage(MessageUtils.color("&cSystem-e artesh fa'al nist!"));
            return true;
        }

        Army army = plugin.getArmyManager().getArmy(player);

        if (args.length == 0 || args[0].equalsIgnoreCase("gui")) {
            if (plugin.getArmyGUI() != null) {
                plugin.getArmyGUI().openGUI(player);
            }
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "follow":
                army.setGlobalOrder(Soldier.Order.FOLLOW);
                MessageUtils.sendActionBar(player, "&a[Artesh] Farman-e Peyrovi (Follow) be tamam-e sarbazan dadeh shod!");
                break;
            case "stay":
                army.setGlobalOrder(Soldier.Order.STAY);
                MessageUtils.sendActionBar(player, "&e[Artesh] Farman-e Istan (Stay) be tamam-e sarbazan dadeh shod!");
                break;
            case "guard":
                army.setGlobalOrder(Soldier.Order.GUARD);
                MessageUtils.sendActionBar(player, "&b[Artesh] Farman-e Negahbani (Guard) dar in noghteh sader shod!");
                break;
            case "attack":
                army.setGlobalOrder(Soldier.Order.ATTACK);
                MessageUtils.sendActionBar(player, "&4[Artesh] Farman-e Hamleh (Attack) be tamam-e sarbazan dadeh shod!");
                break;
            case "formation":
                if (args.length > 1) {
                    try {
                        Formation f = Formation.valueOf(args[1].toUpperCase());
                        army.setFormation(f);
                        MessageUtils.sendActionBar(player, "&6[Artesh] Arayesh be &e" + f.getDisplayName() + " &6taghir yaft!");
                    } catch (Exception e) {
                        player.sendMessage(MessageUtils.color("&cArayesh-haye motabar: LINE, WALL, WEDGE, CIRCLE, DEFENSIVE"));
                    }
                } else {
                    player.sendMessage(MessageUtils.color("&7Arayesh-e feli: &e" + army.getFormation().getDisplayName()));
                }
                break;
            case "list":
                player.sendMessage(MessageUtils.color("&8[Artesh] &6List-e Sarbazan-e Shoma (" + army.getSize() + "):"));
                for (Soldier s : army.getSoldiers()) {
                    player.sendMessage(MessageUtils.color("  &7- &e" + s.getSoldierClass().getDisplayName() + " &a(Lv." + s.getLevel() + ") &7| Jan: &c" + String.format("%.1f", s.getHealth())));
                }
                break;
            default:
                player.sendMessage(MessageUtils.color("&6Dastoorat-e Artesh:"));
                player.sendMessage(MessageUtils.color("&e/army gui &7- Baz kardan-e manooye modiriat"));
                player.sendMessage(MessageUtils.color("&e/army follow &7- Peyrovi az farmandeh"));
                player.sendMessage(MessageUtils.color("&e/army stay &7- Tavaghof dar jaye khod"));
                player.sendMessage(MessageUtils.color("&e/army guard &7- Negahbani az mantagheh"));
                player.sendMessage(MessageUtils.color("&e/army formation <line|wall|wedge|circle|defensive>"));
                break;
        }
        return true;
    }
}

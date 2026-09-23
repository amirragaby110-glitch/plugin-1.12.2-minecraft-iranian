package ir.iranian.hardcore.commands;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.civilization.CivilizationTier;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command Executor for Persian Civilization Progression (/civilization or /civ).
 */
public class CivilizationCommand implements CommandExecutor {

    private final IranianHardcorePlugin plugin;

    public CivilizationCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("In dastoor faghat baraye bazikonan ast!");
            return true;
        }

        Player player = (Player) sender;
        if (plugin.getCivilizationManager() == null) {
            player.sendMessage(MessageUtils.color("&cSystem-e tamaddon fa'al nist!"));
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("gui")) {
            if (plugin.getCivilizationGUI() != null) {
                plugin.getCivilizationGUI().openGUI(player);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("upgrade")) {
            if (plugin.getCivilizationManager().canUpgrade(player)) {
                plugin.getCivilizationManager().upgradeTier(player);
            } else {
                player.sendMessage(MessageUtils.color("&cSharayet-e ertegha kamel nist! /civ gui ra baraye joz'iat baz konid."));
            }
            return true;
        }

        CivilizationTier tier = plugin.getCivilizationManager().getPlayerTier(player);
        player.sendMessage(MessageUtils.color("&8[Tamaddon] &6Martabeh-ye Feli: &e" + tier.getDisplayName()));
        player.sendMessage(MessageUtils.color("&8[Tamaddon] &7Zarfiat-e Artesh: &a" + tier.getMaxSoldiers() + " Sarbaz"));
        return true;
    }
}

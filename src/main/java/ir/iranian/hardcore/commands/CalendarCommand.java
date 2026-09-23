package ir.iranian.hardcore.commands;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.calendar.PersianCalendar;
import ir.iranian.hardcore.calendar.PersianDate;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command Executor for Persian Solar Hijri Calendar (/calendar).
 */
public class CalendarCommand implements CommandExecutor {

    private final IranianHardcorePlugin plugin;

    public CalendarCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PersianDate date = PersianCalendar.now();

        if (sender instanceof Player && (args.length == 0 || args[0].equalsIgnoreCase("gui"))) {
            if (plugin.getCalendarGUI() != null) {
                plugin.getCalendarGUI().openGUI((Player) sender);
                return true;
            }
        }

        sender.sendMessage(MessageUtils.color("&8§m----------------------------------------"));
        sender.sendMessage(MessageUtils.color("&6&l★ TAGHVIM-E KHURSHIDI-YE IRAN ★"));
        sender.sendMessage(MessageUtils.color("&eTarikh-e Emrooz: &a" + date.toFormalString()));
        sender.sendMessage(MessageUtils.color("&7Adadi: &b" + date.toNumericString() + " &7| Saat: &f" + date.toTimeString()));
        sender.sendMessage(MessageUtils.color("&7Sal-e Kabiseh: " + (date.isLeapYear() ? "&aBale" : "&7Kheyr")));
        if (date.hasFestival()) {
            sender.sendMessage(MessageUtils.color("&6&lJashn-e Bastani: &e" + date.getFestival()));
        }
        sender.sendMessage(MessageUtils.color("&8§m----------------------------------------"));
        return true;
    }
}

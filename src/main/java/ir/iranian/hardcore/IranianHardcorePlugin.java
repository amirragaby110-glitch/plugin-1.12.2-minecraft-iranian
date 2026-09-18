package ir.iranian.hardcore;

import ir.iranian.hardcore.commands.HardcoreCommand;
import ir.iranian.hardcore.commands.RaceCommand;
import ir.iranian.hardcore.config.ConfigManager;
import ir.iranian.hardcore.gui.RaceGUI;
import ir.iranian.hardcore.hardcore.HardcoreManager;
import ir.iranian.hardcore.listeners.MobHardcoreListener;
import ir.iranian.hardcore.listeners.PlayerHardcoreListener;
import ir.iranian.hardcore.listeners.RaceListener;
import ir.iranian.hardcore.listeners.SurvivalListener;
import ir.iranian.hardcore.race.RaceManager;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * کلاس اصلی پلاگین IranianHardcore
 * سخت‌ترین پلاگین هاردکور برای 1.12.2
 * الهام گرفته از RLCraft و Blood N Bones
 *
 * @author IranianTeam
 * @version 1.0.0
 */
public class IranianHardcorePlugin extends JavaPlugin {

    private static IranianHardcorePlugin instance;

    // Managers
    private ConfigManager configManager;
    private RaceManager raceManager;
    private HardcoreManager hardcoreManager;
    private RaceGUI raceGUI;

    @Override
    public void onEnable() {
        instance = this;

        // بنر شروع
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§6§l Iranian Hardcore §7- §cسخت‌ترین پلاگین هاردکور");
        Bukkit.getConsoleSender().sendMessage("§7 نسخه: §a" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§7 برای: §a1.12.2 Spigot/Paper");
        Bukkit.getConsoleSender().sendMessage("§7 نویسنده: §eIranianTeam");
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");

        // بارگذاری کانفیگ
        configManager = new ConfigManager(this);
        configManager.loadAll();

        // تنظیم prefix پیام‌ها
        String prefix = configManager.getString("general.prefix", "&8[&cسخت&8] &r");
        MessageUtils.setPrefix(prefix);

        // Managers
        raceManager = new RaceManager(this);
        hardcoreManager = new HardcoreManager(this);
        raceGUI = new RaceGUI(this);

        // اعمال تنظیمات جهان
        hardcoreManager.applyWorldSettings();

        // ثبت ایونت‌ها
        registerListeners();

        // ثبت دستورات
        registerCommands();

        // شروع تسک‌ها
        hardcoreManager.startTasks();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&aپلاگین با موفقیت فعال شد!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7تعداد نژادها: &6" + ir.iranian.hardcore.race.RaceType.values().length));
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&8[&cسخت&8] &cپلاگین غیرفعال شد!"));
        instance = null;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MobHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SurvivalListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RaceListener(this), this);
        // RaceGUI خودش رجیستر می‌شود
        getLogger().info("تمام Listener ها ثبت شدند.");
    }

    private void registerCommands() {
        // دستور race
        if (getCommand("race") != null) {
            RaceCommand raceCommand = new RaceCommand(this);
            getCommand("race").setExecutor(raceCommand);
            getCommand("race").setTabCompleter(raceCommand);
        } else {
            getLogger().warning("دستور race در plugin.yml یافت نشد!");
        }

        // دستور hardcore
        if (getCommand("hardcore") != null) {
            HardcoreCommand hcCommand = new HardcoreCommand(this);
            getCommand("hardcore").setExecutor(hcCommand);
            getCommand("hardcore").setTabCompleter(hcCommand);
        } else {
            getLogger().warning("دستور hardcore در plugin.yml یافت نشد!");
        }

        getLogger().info("دستورات ثبت شدند.");
    }

    // Getters
    public static IranianHardcorePlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public RaceManager getRaceManager() {
        return raceManager;
    }

    public HardcoreManager getHardcoreManager() {
        return hardcoreManager;
    }

    public RaceGUI getRaceGUI() {
        return raceGUI;
    }
}

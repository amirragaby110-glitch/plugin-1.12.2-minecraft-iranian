package ir.iranian.hardcore;

import ir.iranian.hardcore.commands.DungeonCommand;
import ir.iranian.hardcore.commands.HardcoreCommand;
import ir.iranian.hardcore.commands.RaceCommand;
import ir.iranian.hardcore.config.ConfigManager;
import ir.iranian.hardcore.dungeons.DungeonManager;
import ir.iranian.hardcore.gui.RaceGUI;
import ir.iranian.hardcore.hardcore.HardcoreManager;
import ir.iranian.hardcore.listeners.DungeonListener;
import ir.iranian.hardcore.listeners.MobHardcoreListener;
import ir.iranian.hardcore.listeners.PlayerHardcoreListener;
import ir.iranian.hardcore.listeners.RaceListener;
import ir.iranian.hardcore.listeners.SurvivalListener;
import ir.iranian.hardcore.race.RaceManager;
import ir.iranian.hardcore.structures.PersianStructures;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * کلاس اصلی پلاگین IranianHardcore - نسخه 2.0 کاملا فارسی
 * سخت‌ترین پلاگین هاردکور برای 1.12.2 با دانجن‌های تاریخی ایران
 * الهام گرفته از RLCraft و تاریخ 2500 ساله ایران
 *
 * ویژگی‌های جدید 2.0:
 * - 8 دانجن ایرانی تاریخی در هر بایوم
 * - سازه‌های ایرانی: بازار، کاروانسرا، چایخانه، آب‌انبار
 * - آیتم‌های ایرانی: شمشیرها، تاج کوروش، فرش، مروارید خلیج فارس
 * - کاملا فارسی
 *
 * @author IranianTeam
 * @version 2.0.0 - نسخه ایرانی
 */
public class IranianHardcorePlugin extends JavaPlugin {

    private static IranianHardcorePlugin instance;

    // Managers
    private ConfigManager configManager;
    private RaceManager raceManager;
    private HardcoreManager hardcoreManager;
    private DungeonManager dungeonManager;
    private PersianStructures persianStructures;
    private RaceGUI raceGUI;

    @Override
    public void onEnable() {
        instance = this;

        // بنر شروع فارسی
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§6§l پلاگین هاردکور ایرانی §7- §cنسخه 2.0");
        Bukkit.getConsoleSender().sendMessage("§7🇮🇷 §aتاریخ 2500 ساله ایران در ماینکرافت");
        Bukkit.getConsoleSender().sendMessage("§7 نسخه: §a" + getDescription().getVersion() + " §7- کاملا فارسی");
        Bukkit.getConsoleSender().sendMessage("§7 برای: §a1.12.2 Spigot/Paper");
        Bukkit.getConsoleSender().sendMessage("§7 نویسنده: §eتیم ایرانی - IranianTeam");
        Bukkit.getConsoleSender().sendMessage("§7 دانجن‌ها: §68 دانجن تاریخی + 4 سازه ایرانی");
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");

        // بارگذاری کانفیگ
        configManager = new ConfigManager(this);
        configManager.loadAll();

        // تنظیم prefix پیام‌ها
        String prefix = configManager.getString("general.prefix", "&8[&6ایران&8] &r");
        MessageUtils.setPrefix(prefix);

        // Managers - ترتیب مهم است
        raceManager = new RaceManager(this);
        hardcoreManager = new HardcoreManager(this);
        dungeonManager = new DungeonManager(this);
        persianStructures = new PersianStructures(this);
        raceGUI = new RaceGUI(this);

        // اعمال تنظیمات جهان
        hardcoreManager.applyWorldSettings();

        // ثبت ایونت‌ها
        registerListeners();

        // ثبت دستورات
        registerCommands();

        // شروع تسک‌ها
        hardcoreManager.startTasks();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&aپلاگین ایرانی با موفقیت فعال شد!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7تعداد نژادها: &6" + ir.iranian.hardcore.race.RaceType.values().length + " &7نژاد ایرانی"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7تعداد دانجن‌ها: &6" + ir.iranian.hardcore.dungeons.DungeonType.values().length + " &7دانجن تاریخی"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7سازه‌های ایرانی: &64 سازه (بازار، کاروانسرا، چایخانه، آب‌انبار)"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&a🇮🇷 زنده باد ایران! پاینده باد تاریخ ایران!"));
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&8[&6ایران&8] &cپلاگین ایرانی غیرفعال شد! خدانگهدار!"));
        instance = null;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MobHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SurvivalListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DungeonListener(this), this);
        // RaceGUI خودش رجیستر می‌شود
        getLogger().info("تمام Listener ها (شامل دانجن‌های ایرانی) ثبت شدند.");
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

        // دستور dungeon - جدید ایرانی
        if (getCommand("dungeon") != null) {
            DungeonCommand dungeonCommand = new DungeonCommand(this);
            getCommand("dungeon").setExecutor(dungeonCommand);
            getCommand("dungeon").setTabCompleter(dungeonCommand);
        } else {
            getLogger().warning("دستور dungeon در plugin.yml یافت نشد!");
        }

        getLogger().info("دستورات (شامل دانجن ایرانی) ثبت شدند.");
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

    public DungeonManager getDungeonManager() {
        return dungeonManager;
    }

    public PersianStructures getPersianStructures() {
        return persianStructures;
    }

    public RaceGUI getRaceGUI() {
        return raceGUI;
    }
}

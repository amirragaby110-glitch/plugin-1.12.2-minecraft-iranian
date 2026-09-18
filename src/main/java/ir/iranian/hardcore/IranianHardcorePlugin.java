package ir.iranian.hardcore;

import ir.iranian.hardcore.commands.BazaarCommand;
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
import ir.iranian.hardcore.stores.PersianBazaar;
import ir.iranian.hardcore.structures.PersianStructures;
import ir.iranian.hardcore.utils.MessageUtils;
import ir.iranian.hardcore.villages.IranianVillageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * کلاس اصلی پلاگین IranianHardcore - نسخه 3.0 کاملا فارسی - هر بایوم یک دانجن
 * سخت‌ترین پلاگین هاردکور برای 1.12.2 با تاریخ ایران
 *
 * ویژگی‌های 3.0 - هر بایوم یک دانجن ایرانی:
 * - 16 دانجن تاریخی ایران برای پوشش تمام 40+ بایوم 1.12.2
 * - سیستم بازار ایرانی با فروشگاه GUI کاملا فارسی
 * - روستاهای ایرانی با نام‌های فارسی و معماری کاهگلی
 * - 4+ سازه ایرانی: بازار، کاروانسرا، چایخانه، آب‌انبار، مسجد، روستا
 * - آیتم‌های ایرانی: 12+ آیتم با Lore تاریخی فارسی
 * - کاملا فارسی - از پیام‌ها تا GUI
 *
 * @author IranianTeam
 * @version 3.0.0 - هر بایوم یک دانجن ایرانی
 */
public class IranianHardcorePlugin extends JavaPlugin {

    private static IranianHardcorePlugin instance;

    // Managers - نسخه 3.0
    private ConfigManager configManager;
    private RaceManager raceManager;
    private HardcoreManager hardcoreManager;
    private DungeonManager dungeonManager;
    private PersianStructures persianStructures;
    private PersianBazaar persianBazaar;
    private IranianVillageManager villageManager;
    private RaceGUI raceGUI;

    @Override
    public void onEnable() {
        instance = this;

        // بنر شروع فارسی - نسخه 3.0
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§6§l🇮🇷 پلاگین هاردکور ایرانی §7- §cنسخه 3.0 - هر بایوم یک دانجن");
        Bukkit.getConsoleSender().sendMessage("§7✨ §aتاریخ 7000 ساله ایران در تمام بایوم‌های ماینکرافت");
        Bukkit.getConsoleSender().sendMessage("§7 نسخه: §a" + getDescription().getVersion() + " §7- کاملا فارسی");
        Bukkit.getConsoleSender().sendMessage("§7 برای: §a1.12.2 Spigot/Paper - بدون NMS");
        Bukkit.getConsoleSender().sendMessage("§7 نویسنده: §eتیم ایرانی - IranianTeam");
        Bukkit.getConsoleSender().sendMessage("§7 دانجن‌ها: §616 دانجن تاریخی برای 40+ بایوم");
        Bukkit.getConsoleSender().sendMessage("§7 سازه‌ها: §68 سازه ایرانی (بازار، کاروانسرا، چایخانه، آب‌انبار، مسجد، روستا)");
        Bukkit.getConsoleSender().sendMessage("§7 بازار: §aبازار ایرانی با سکه دریک - کاملا فارسی");
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");

        // بارگذاری کانفیگ فارسی کامل
        configManager = new ConfigManager(this);
        configManager.loadAll();

        // تنظیم prefix پیام‌ها - ایرانی
        String prefix = configManager.getString("general.prefix", "&8[&6ایران&8] &r");
        MessageUtils.setPrefix(prefix);

        // Managers - ترتیب مهم است
        raceManager = new RaceManager(this);
        hardcoreManager = new HardcoreManager(this);
        dungeonManager = new DungeonManager(this);
        persianStructures = new PersianStructures(this);
        persianBazaar = new PersianBazaar(this);
        villageManager = new IranianVillageManager(this);
        raceGUI = new RaceGUI(this);

        // اعمال تنظیمات جهان
        hardcoreManager.applyWorldSettings();

        // ثبت ایونت‌ها - شامل روستاهای ایرانی
        registerListeners();

        // ثبت دستورات - شامل بازار
        registerCommands();

        // شروع تسک‌ها
        hardcoreManager.startTasks();

        // پیام موفقیت فارسی
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&a✅ پلاگین ایرانی نسخه 3.0 با موفقیت فعال شد!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7تعداد نژادها: &6" + ir.iranian.hardcore.race.RaceType.values().length + " &7نژاد ایرانی"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7تعداد دانجن‌ها: &6" + ir.iranian.hardcore.dungeons.DungeonType.values().length + " &7دانجن تاریخی برای تمام بایوم‌ها"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7سازه‌های ایرانی: &68 سازه (بازار، کاروانسرا، چایخانه، آب‌انبار، مسجد، روستا، پاسارگاد...)"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7بازار ایرانی: &aفعال با سکه دریک هخامنشی - واحد پول ایرانی"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7روستاهای ایرانی: &aروستاییان با نام فارسی - کندوان، میمند، ابیانه"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&a🇮🇷 زنده باد ایران! پاینده باد تاریخ 7000 ساله ایران! خلیج همیشه فارس!"));
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&8[&6ایران&8] &cپلاگین ایرانی غیرفعال شد! خدانگهدار! به امید دیدار در تخت جمشید! 🇮🇷"));
        instance = null;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MobHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SurvivalListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DungeonListener(this), this);
        Bukkit.getPluginManager().registerEvents(villageManager, this);
        // PersianBazaar و RaceGUI خودشان رجیستر می‌شوند
        getLogger().info("تمام Listener ها (16 دانجن + بازار + روستاهای ایرانی) ثبت شدند.");
    }

    private void registerCommands() {
        // دستور race - نژاد ایرانی
        if (getCommand("race") != null) {
            RaceCommand raceCommand = new RaceCommand(this);
            getCommand("race").setExecutor(raceCommand);
            getCommand("race").setTabCompleter(raceCommand);
        }

        // دستور hardcore
        if (getCommand("hardcore") != null) {
            HardcoreCommand hcCommand = new HardcoreCommand(this);
            getCommand("hardcore").setExecutor(hcCommand);
            getCommand("hardcore").setTabCompleter(hcCommand);
        }

        // دستور dungeon - دانجن‌های ایرانی
        if (getCommand("dungeon") != null) {
            DungeonCommand dungeonCommand = new DungeonCommand(this);
            getCommand("dungeon").setExecutor(dungeonCommand);
            getCommand("dungeon").setTabCompleter(dungeonCommand);
        }

        // دستور bazaar - بازار ایرانی جدید 3.0
        if (getCommand("bazaar") != null) {
            BazaarCommand bazaarCommand = new BazaarCommand(this);
            getCommand("bazaar").setExecutor(bazaarCommand);
            getCommand("bazaar").setTabCompleter(bazaarCommand);
        }

        getLogger().info("دستورات (نژاد + هاردکور + 16 دانجن + بازار ایرانی) ثبت شدند.");
    }

    // Getters - نسخه 3.0
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

    public PersianBazaar getPersianBazaar() {
        return persianBazaar;
    }

    public IranianVillageManager getVillageManager() {
        return villageManager;
    }

    public RaceGUI getRaceGUI() {
        return raceGUI;
    }
}

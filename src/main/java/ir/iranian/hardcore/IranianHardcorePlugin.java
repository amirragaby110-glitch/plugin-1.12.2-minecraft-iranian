package ir.iranian.hardcore;

import ir.iranian.hardcore.climate.TemperatureManager;
import ir.iranian.hardcore.commands.BazaarCommand;
import ir.iranian.hardcore.commands.DungeonCommand;
import ir.iranian.hardcore.commands.HardcoreCommand;
import ir.iranian.hardcore.commands.RaceCommand;
import ir.iranian.hardcore.config.ConfigManager;
import ir.iranian.hardcore.dungeons.DungeonManager;
import ir.iranian.hardcore.gui.RaceGUI;
import ir.iranian.hardcore.hardcore.HardcoreManager;
import ir.iranian.hardcore.language.LanguageManager;
import ir.iranian.hardcore.listeners.DungeonListener;
import ir.iranian.hardcore.listeners.MobHardcoreListener;
import ir.iranian.hardcore.listeners.PlayerHardcoreListener;
import ir.iranian.hardcore.listeners.RaceListener;
import ir.iranian.hardcore.listeners.SurvivalListener;
import ir.iranian.hardcore.mobs.IranianMobsManager;
import ir.iranian.hardcore.race.RaceManager;
import ir.iranian.hardcore.resourcepack.ResourcePackManager;
import ir.iranian.hardcore.stores.PersianBazaar;
import ir.iranian.hardcore.structures.PersianStructures;
import ir.iranian.hardcore.thirst.ThirstManager;
import ir.iranian.hardcore.utils.MessageUtils;
import ir.iranian.hardcore.villages.IranianVillageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * IranianHardcore - v4.0 Finglish + Climate + Thirst + Custom Mobs + Resource Pack
 * - Finglish version for servers where Persian doesn't display
 * - Temperature system (cold/hot biomes)
 * - Thirst system (must drink water)
 * - Custom Iranian mobs (Div, Simurgh, Zahhak, etc) with resource pack
 * - Fully compatible with 1.12.2
 */
public class IranianHardcorePlugin extends JavaPlugin {

    private static IranianHardcorePlugin instance;

    // Managers - v4.0
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private RaceManager raceManager;
    private HardcoreManager hardcoreManager;
    private DungeonManager dungeonManager;
    private PersianStructures persianStructures;
    private PersianBazaar persianBazaar;
    private IranianVillageManager villageManager;
    private RaceGUI raceGUI;
    private TemperatureManager temperatureManager;
    private ThirstManager thirstManager;
    private IranianMobsManager mobsManager;
    private ResourcePackManager resourcePackManager;

    @Override
    public void onEnable() {
        instance = this;

        // Banner - v4.0 Finglish
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§6§l🇮🇷 Iranian Hardcore §7- §cVersion 4.0 - Finglish + Climate + Thirst + Mobs");
        Bukkit.getConsoleSender().sendMessage("§7✨ §a7000 years of Iranian history in Minecraft");
        Bukkit.getConsoleSender().sendMessage("§7 Version: §a" + getDescription().getVersion() + " §7- Finglish compatible");
        Bukkit.getConsoleSender().sendMessage("§7 For: §a1.12.2 Spigot/Paper - No NMS");
        Bukkit.getConsoleSender().sendMessage("§7 Author: §eIranianTeam");
        Bukkit.getConsoleSender().sendMessage("§7 Language: §aFinglish (Persian with English letters) + Persian");
        Bukkit.getConsoleSender().sendMessage("§7 Climate: §bTemperature system - Cold/Hot biomes");
        Bukkit.getConsoleSender().sendMessage("§7 Thirst: §bMust drink water - Mashk Ab");
        Bukkit.getConsoleSender().sendMessage("§7 Mobs: §c7 Custom Iranian mobs - Div, Simurgh, Zahhak, etc");
        Bukkit.getConsoleSender().sendMessage("§7 ResourcePack: §aCustom textures for Iranian mobs");
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");

        // Load config
        configManager = new ConfigManager(this);
        configManager.loadAll();

        // Language Manager - v4.0 Finglish (default for compatibility)
        languageManager = new LanguageManager(this);

        // Set prefix based on language
        String lang = languageManager.getCurrentLanguage();
        String prefix = configManager.getString("general.prefix", lang.equals("finglish") ? "&8[&6Iran&8] &r" : "&8[&6ایران&8] &r");
        if (lang.equals("finglish")) {
            prefix = configManager.getString("general.prefix-finglish", "&8[&6Iran&8] &r");
        }
        MessageUtils.setPrefix(prefix);

        // Managers - order matters
        raceManager = new RaceManager(this);
        hardcoreManager = new HardcoreManager(this);
        dungeonManager = new DungeonManager(this);
        persianStructures = new PersianStructures(this);
        persianBazaar = new PersianBazaar(this);
        villageManager = new IranianVillageManager(this);
        raceGUI = new RaceGUI(this);

        // New managers v4.0
        temperatureManager = new TemperatureManager(this);
        thirstManager = new ThirstManager(this);
        mobsManager = new IranianMobsManager(this);
        resourcePackManager = new ResourcePackManager(this);

        // Apply world settings
        hardcoreManager.applyWorldSettings();

        // Register listeners
        registerListeners();

        // Register commands
        registerCommands();

        // Start tasks
        hardcoreManager.startTasks();

        // Success message - Finglish
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&a✅ Iranian plugin v4.0 activated!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7Races: &6" + ir.iranian.hardcore.race.RaceType.values().length + " &7Iranian tribes"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7Dungeons: &6" + ir.iranian.hardcore.dungeons.DungeonType.values().length + " &7historical dungeons"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7Structures: &68 Iranian structures"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7Bazaar: &aActive with Derik coin"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7Climate: &bTemperature system active - Cold/Hot"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7Thirst: &bThirst system active - Must drink water"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7Custom Mobs: &c7 Iranian mobs - Div, Simurgh, Zahhak"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&7ResourcePack: &aCustom textures"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color(prefix + "&a🇮🇷 Zende bad Iran! 7000 years history! Khalij hameshe Fars!"));
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&8[&6Iran&8] &cIranian plugin disabled! Khoda negahdar! 🇮🇷"));
        instance = null;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MobHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SurvivalListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DungeonListener(this), this);
        Bukkit.getPluginManager().registerEvents(villageManager, this);
        // New managers register themselves
        getLogger().info("All listeners registered (16 dungeons + bazaar + villages + climate + thirst + mobs + resourcepack)");
    }

    private void registerCommands() {
        if (getCommand("race") != null) {
            RaceCommand raceCommand = new RaceCommand(this);
            getCommand("race").setExecutor(raceCommand);
            getCommand("race").setTabCompleter(raceCommand);
        }
        if (getCommand("hardcore") != null) {
            HardcoreCommand hcCommand = new HardcoreCommand(this);
            getCommand("hardcore").setExecutor(hcCommand);
            getCommand("hardcore").setTabCompleter(hcCommand);
        }
        if (getCommand("dungeon") != null) {
            DungeonCommand dungeonCommand = new DungeonCommand(this);
            getCommand("dungeon").setExecutor(dungeonCommand);
            getCommand("dungeon").setTabCompleter(dungeonCommand);
        }
        if (getCommand("bazaar") != null) {
            BazaarCommand bazaarCommand = new BazaarCommand(this);
            getCommand("bazaar").setExecutor(bazaarCommand);
            getCommand("bazaar").setTabCompleter(bazaarCommand);
        }
        // New command for Iranian features
        if (getCommand("iranian") != null) {
            ir.iranian.hardcore.commands.IranianCommand iranianCommand = new ir.iranian.hardcore.commands.IranianCommand(this);
            getCommand("iranian").setExecutor(iranianCommand);
            getCommand("iranian").setTabCompleter(iranianCommand);
        }
        getLogger().info("Commands registered (race + hardcore + dungeons + bazaar + iranian)");
    }

    // Getters - v4.0
    public static IranianHardcorePlugin getInstance() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
    public LanguageManager getLanguageManager() { return languageManager; }
    public RaceManager getRaceManager() { return raceManager; }
    public HardcoreManager getHardcoreManager() { return hardcoreManager; }
    public DungeonManager getDungeonManager() { return dungeonManager; }
    public PersianStructures getPersianStructures() { return persianStructures; }
    public PersianBazaar getPersianBazaar() { return persianBazaar; }
    public IranianVillageManager getVillageManager() { return villageManager; }
    public RaceGUI getRaceGUI() { return raceGUI; }
    public TemperatureManager getTemperatureManager() { return temperatureManager; }
    public ThirstManager getThirstManager() { return thirstManager; }
    public IranianMobsManager getMobsManager() { return mobsManager; }
    public ResourcePackManager getResourcePackManager() { return resourcePackManager; }
}

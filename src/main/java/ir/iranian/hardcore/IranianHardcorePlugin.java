package ir.iranian.hardcore;

import ir.iranian.hardcore.climate.TemperatureManager;
import ir.iranian.hardcore.commands.*;
import ir.iranian.hardcore.config.ConfigManager;
import ir.iranian.hardcore.dungeons.BossFightManager;
import ir.iranian.hardcore.dungeons.DungeonManager;
import ir.iranian.hardcore.foods.IranianFoodsManager;
import ir.iranian.hardcore.gui.RaceGUI;
import ir.iranian.hardcore.hardcore.HardcoreManager;
import ir.iranian.hardcore.health.PlayerHealthManager;
import ir.iranian.hardcore.items.CustomItemRegistry;
import ir.iranian.hardcore.items.PersianBossItems;
import ir.iranian.hardcore.language.LanguageManager;
import ir.iranian.hardcore.listeners.*;
import ir.iranian.hardcore.mobs.IranianMobsManager;
import ir.iranian.hardcore.race.RaceManager;
import ir.iranian.hardcore.resourcepack.ResourcePackManager;
import ir.iranian.hardcore.stores.PersianBazaar;
import ir.iranian.hardcore.structures.PersianStructures;
import ir.iranian.hardcore.swords.PersianSwordsManager;
import ir.iranian.hardcore.thirst.ThirstManager;
import ir.iranian.hardcore.utils.MessageUtils;
import ir.iranian.hardcore.villages.IranianVillageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * IranianHardcore - v5.0 Finglish Edition
 * - 900 Craftable Iranian Swords
 * - 500 Functional Iranian Foods
 * - 20,000 Procedural Iranian Items
 * - 30 Historical Dungeons & 3-Phase Boss Fights
 * - RLCraft Style Thirst System (Mashk Ab, Dirty Water, Purification)
 * - Climate, Weather & Seasons System
 * - 30 Iranian Mobs + Speaking Villagers + All Mobs Speaking Finglish
 * - Resource Pack v5.0
 * - 100% Finglish / English letters for Spigot 1.12.2 on Aternos
 */
public class IranianHardcorePlugin extends JavaPlugin {

    private static IranianHardcorePlugin instance;

    // Managers - v5.0
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private RaceManager raceManager;
    private HardcoreManager hardcoreManager;
    private DungeonManager dungeonManager;
    private BossFightManager bossFightManager;
    private PersianStructures persianStructures;
    private PersianBazaar persianBazaar;
    private IranianVillageManager villageManager;
    private RaceGUI raceGUI;
    private TemperatureManager temperatureManager;
    private ThirstManager thirstManager;
    private IranianMobsManager mobsManager;
    private ResourcePackManager resourcePackManager;
    private PersianSwordsManager swordsManager;
    private IranianFoodsManager foodsManager;
    private CustomItemRegistry customItemRegistry;
    private PlayerHealthManager healthManager;
    private PersianBossItems bossItems;
    private ir.iranian.hardcore.items.PersianAuthenticItems authenticItems;
    private ir.iranian.hardcore.pahlavani.ZoorkhanehManager zoorkhanehManager;
    private ir.iranian.hardcore.structures.AtashkadehBahram atashkadehBahram;
    private ir.iranian.hardcore.mobs.AkvanDivBoss akvanDivBoss;

    @Override
    public void onEnable() {
        instance = this;

        // Startup banner - v5.3 Finglish
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§6§l Iranian Hardcore §7- §cVersion 5.3 - Health & Boss Weapons Edition");
        Bukkit.getConsoleSender().sendMessage("§7 7000 years of Iranian history in Minecraft 1.12.2");
        Bukkit.getConsoleSender().sendMessage("§7 Health: §cCraftable Heart Canisters (Up to 30 Hearts)");
        Bukkit.getConsoleSender().sendMessage("§7 Boss Weapons: §eDiv-Kosh Blade, Boss Piercer Bow, Naphtha Bombs");
        Bukkit.getConsoleSender().sendMessage("§7 Structures: §6Shah Abbasi Caravanserai, Ab-Anbar, Chaikhaneh, Atashkadeh");
        Bukkit.getConsoleSender().sendMessage("§7 Swords: §6900 Craftable Iranian Swords");
        Bukkit.getConsoleSender().sendMessage("§7 Foods: §e500 Functional Iranian Foods");
        Bukkit.getConsoleSender().sendMessage("§7 Items: §b20,000 Procedural Iranian Items");
        Bukkit.getConsoleSender().sendMessage("§7 Dungeons: §a30 Historical Dungeons + 3-Phase Boss Fights");
        Bukkit.getConsoleSender().sendMessage("§7 Thirst: §bRLCraft Style Hydration + Mashk Ab");
        Bukkit.getConsoleSender().sendMessage("§7 Climate: §bTemperature, Weather & Persian Seasons");
        Bukkit.getConsoleSender().sendMessage("§7 Mobs: §c30 Iranian Mobs + Zero Text Vocal Sound Speech");
        Bukkit.getConsoleSender().sendMessage("§7 Encoding: §a100% Finglish (Safe for Aternos)");
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");

        // Load config
        configManager = new ConfigManager(this);
        configManager.loadAll();

        // Language Manager
        languageManager = new LanguageManager(this);
        MessageUtils.setPrefix(configManager.getString("general.prefix", "&8[&6Iran&8] &r"));

        // Core systems
        raceManager = new RaceManager(this);
        hardcoreManager = new HardcoreManager(this);
        dungeonManager = new DungeonManager(this);
        bossFightManager = new BossFightManager(this);
        persianStructures = new PersianStructures(this);
        persianBazaar = new PersianBazaar(this);
        villageManager = new IranianVillageManager(this);
        raceGUI = new RaceGUI(this);

        // v5.0 Features
        temperatureManager = new TemperatureManager(this);
        thirstManager = new ThirstManager(this);
        mobsManager = new IranianMobsManager(this);
        resourcePackManager = new ResourcePackManager(this);
        swordsManager = new PersianSwordsManager(this);
        foodsManager = new IranianFoodsManager(this);
        customItemRegistry = new CustomItemRegistry(this);
        healthManager = new PlayerHealthManager(this);
        bossItems = new PersianBossItems(this);
        authenticItems = new ir.iranian.hardcore.items.PersianAuthenticItems(this);
        zoorkhanehManager = new ir.iranian.hardcore.pahlavani.ZoorkhanehManager(this);
        atashkadehBahram = new ir.iranian.hardcore.structures.AtashkadehBahram(this);
        akvanDivBoss = new ir.iranian.hardcore.mobs.AkvanDivBoss(this);

        // Apply world settings & start tasks
        hardcoreManager.applyWorldSettings();
        registerListeners();
        registerCommands();
        hardcoreManager.startTasks();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&8[&6Iran&8] &a Iranian Hardcore v5.1 activated successfully!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&8[&6Iran&8] &aZendeh bad Iran! Khalij-e Hameshe Fars!"));
        Bukkit.getConsoleSender().sendMessage("§8§l§m----------------------------------------");
    }

    @Override
    public void onDisable() {
        if (healthManager != null) {
            healthManager.saveHealthData();
        }
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&8[&6Iran&8] &cIranian plugin disabled! Khoda negahdar! Zendeh bad Iran!"));
        instance = null;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MobHardcoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SurvivalListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DungeonListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RandomChestLootListener(this), this);
        Bukkit.getPluginManager().registerEvents(villageManager, this);
        getLogger().info("All listeners registered successfully!");
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
        if (getCommand("iranian") != null) {
            IranianCommand iranianCommand = new IranianCommand(this);
            getCommand("iranian").setExecutor(iranianCommand);
            getCommand("iranian").setTabCompleter(iranianCommand);
        }
        getLogger().info("Commands registered (race + hardcore + dungeon + bazaar + iranian)");
    }

    // Getters
    public static IranianHardcorePlugin getInstance() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
    public LanguageManager getLanguageManager() { return languageManager; }
    public RaceManager getRaceManager() { return raceManager; }
    public HardcoreManager getHardcoreManager() { return hardcoreManager; }
    public DungeonManager getDungeonManager() { return dungeonManager; }
    public BossFightManager getBossFightManager() { return bossFightManager; }
    public PersianStructures getPersianStructures() { return persianStructures; }
    public PersianBazaar getPersianBazaar() { return persianBazaar; }
    public IranianVillageManager getVillageManager() { return villageManager; }
    public RaceGUI getRaceGUI() { return raceGUI; }
    public TemperatureManager getTemperatureManager() { return temperatureManager; }
    public ThirstManager getThirstManager() { return thirstManager; }
    public IranianMobsManager getMobsManager() { return mobsManager; }
    public ResourcePackManager getResourcePackManager() { return resourcePackManager; }
    public PersianSwordsManager getSwordsManager() { return swordsManager; }
    public IranianFoodsManager getFoodsManager() { return foodsManager; }
    public CustomItemRegistry getCustomItemRegistry() { return customItemRegistry; }
    public PlayerHealthManager getHealthManager() { return healthManager; }
    public PersianBossItems getBossItems() { return bossItems; }
}

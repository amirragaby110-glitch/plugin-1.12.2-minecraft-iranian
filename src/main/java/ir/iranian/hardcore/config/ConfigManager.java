package ir.iranian.hardcore.config;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Modiriat tamam filehaye config plugin - v5.0 Finglish
 * - config.yml : Tanzimat asli
 * - races.yml : Zakhire ghome bazikonan
 * - data.yml : Cooldowns
 * - dungeons.yml : Dungeonhaye sakhte shode Irani
 * - structures.yml : Sazehaye Irani map
 */
public class ConfigManager {

    private final IranianHardcorePlugin plugin;

    private FileConfiguration config;
    private FileConfiguration racesConfig;
    private FileConfiguration dataConfig;
    private FileConfiguration dungeonsConfig;
    private FileConfiguration structuresConfig;

    private File racesFile;
    private File dataFile;
    private File dungeonsFile;
    private File structuresFile;

    // Cache
    private final Map<String, Object> cache = new HashMap<>();

    public ConfigManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Bargozaariye avalie tamam fileha
     */
    public void loadAll() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        // races.yml
        this.racesFile = new File(plugin.getDataFolder(), "races.yml");
        if (!racesFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                racesFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Khata dar sakhte races.yml: " + e.getMessage());
            }
        }
        this.racesConfig = YamlConfiguration.loadConfiguration(racesFile);

        // data.yml
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Khata dar sakhte data.yml: " + e.getMessage());
            }
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        // dungeons.yml
        this.dungeonsFile = new File(plugin.getDataFolder(), "dungeons.yml");
        if (!dungeonsFile.exists()) {
            try {
                dungeonsFile.createNewFile();
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dungeonsFile);
                cfg.set("info", "In file mahalle dungeonhaye Irani sakhte shode ra zakhire mikonad");
                cfg.save(dungeonsFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Khata dar sakhte dungeons.yml: " + e.getMessage());
            }
        }
        this.dungeonsConfig = YamlConfiguration.loadConfiguration(dungeonsFile);

        // structures.yml
        this.structuresFile = new File(plugin.getDataFolder(), "structures.yml");
        if (!structuresFile.exists()) {
            try {
                structuresFile.createNewFile();
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(structuresFile);
                cfg.set("info", "Mahalle sazehaye Irani mesle bazar va karvansara");
                cfg.save(structuresFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Khata dar sakhte structures.yml: " + e.getMessage());
            }
        }
        this.structuresConfig = YamlConfiguration.loadConfiguration(structuresFile);

        // Populate cache
        populateCache();

        plugin.getLogger().info("Tamam configha (shamele dungeonhaye Irani) bargozaari shod.");
    }

    private void populateCache() {
        cache.clear();
        cache.put("general.prefix", config.getString("general.prefix", "&8[&6Iran&8] &r"));
        cache.put("hardcore.enabled", config.getBoolean("hardcore.enabled", true));
        cache.put("hardcore.health.max-health", config.getDouble("hardcore.health.max-health", 10.0));
        cache.put("hardcore.health.disable-natural-regen", config.getBoolean("hardcore.health.disable-natural-regen", true));
        cache.put("hardcore.health.faster-hunger", config.getBoolean("hardcore.health.faster-hunger", true));
        cache.put("hardcore.health.raw-food-poison", config.getBoolean("hardcore.health.raw-food-poison", true));
        cache.put("hardcore.mobs.enabled", config.getBoolean("hardcore.mobs.enabled", true));
        cache.put("hardcore.mobs.double-health", config.getBoolean("hardcore.mobs.double-health", true));
        cache.put("hardcore.mobs.damage-multiplier", config.getDouble("hardcore.mobs.damage-multiplier", 1.5));
        cache.put("hardcore.survival.bed.enabled", config.getBoolean("hardcore.survival.bed.enabled", true));
        cache.put("hardcore.survival.bed.cooldown-days", config.getInt("hardcore.survival.bed.cooldown-days", 3));
        cache.put("race.enabled", config.getBoolean("race.enabled", true));
        cache.put("race.force-choose-on-first-join", config.getBoolean("race.force-choose-on-first-join", true));
        cache.put("race.change-cooldown-days", config.getInt("race.change-cooldown-days", 7));
        cache.put("dungeons.enabled", config.getBoolean("dungeons.enabled", true));
        cache.put("structures.enabled", config.getBoolean("structures.enabled", true));
    }

    public void reload() {
        cache.clear();
        loadAll();
    }

    // Getters with cache
    public boolean getBoolean(String path, boolean def) {
        if (cache.containsKey(path)) return (Boolean) cache.get(path);
        boolean val = config.getBoolean(path, def);
        cache.put(path, val);
        return val;
    }

    public int getInt(String path, int def) {
        if (cache.containsKey(path)) return (Integer) cache.get(path);
        int val = config.getInt(path, def);
        cache.put(path, val);
        return val;
    }

    public double getDouble(String path, double def) {
        if (cache.containsKey(path)) return (Double) cache.get(path);
        double val = config.getDouble(path, def);
        cache.put(path, val);
        return val;
    }

    public String getString(String path, String def) {
        if (cache.containsKey(path)) return (String) cache.get(path);
        String val = config.getString(path, def);
        cache.put(path, val);
        return val;
    }

    public FileConfiguration getConfig() { return config; }
    public FileConfiguration getRacesConfig() { return racesConfig; }
    public FileConfiguration getDataConfig() { return dataConfig; }
    public FileConfiguration getDungeonsConfig() { return dungeonsConfig; }
    public FileConfiguration getStructuresConfig() { return structuresConfig; }

    public void saveConfig() {
        plugin.saveConfig();
    }

    public void saveDungeons() {
        try {
            dungeonsConfig.save(dungeonsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Khata dar zakhire dungeons.yml: " + e.getMessage());
        }
    }

    public void saveDungeonsConfig() {
        saveDungeons();
    }

    public void saveStructures() {
        try {
            structuresConfig.save(structuresFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Khata dar zakhire structures.yml: " + e.getMessage());
        }
    }

    // Races persistence
    public String getPlayerRace(String uuid) {
        return racesConfig.getString("players." + uuid + ".race", null);
    }

    public void setPlayerRace(String uuid, String raceName) {
        racesConfig.set("players." + uuid + ".race", raceName);
        racesConfig.set("players." + uuid + ".chosen-at", System.currentTimeMillis());
        saveRaces();
    }

    public long getPlayerRaceChosenAt(String uuid) {
        return racesConfig.getLong("players." + uuid + ".chosen-at", 0L);
    }

    public void saveRaces() {
        try {
            racesConfig.save(racesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Khata dar zakhire races.yml: " + e.getMessage());
        }
    }

    // Bed cooldown persistence
    public long getBedLastUsed(String uuid) {
        return dataConfig.getLong("bed-cooldown." + uuid, 0L);
    }

    public void setBedLastUsed(String uuid, long timestamp) {
        dataConfig.set("bed-cooldown." + uuid, timestamp);
        saveData();
    }

    // Race change cooldown persistence
    public long getRaceChangeLastUsed(String uuid) {
        return dataConfig.getLong("race-change-cooldown." + uuid, 0L);
    }

    public void setRaceChangeLastUsed(String uuid, long timestamp) {
        dataConfig.set("race-change-cooldown." + uuid, timestamp);
        saveData();
    }

    public void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Khata dar zakhire data.yml: " + e.getMessage());
        }
    }
}

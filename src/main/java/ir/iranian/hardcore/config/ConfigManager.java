package ir.iranian.hardcore.config;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * مدیریت تمام فایل‌های کانفیگ پلاگین
 * - config.yml : تنظیمات اصلی
 * - races.yml : ذخیره نژاد بازیکنان
 * - data.yml : کولدان‌ها (تخت و تغییر نژاد)
 */
public class ConfigManager {

    private final IranianHardcorePlugin plugin;

    private FileConfiguration config;
    private FileConfiguration racesConfig;
    private FileConfiguration dataConfig;

    private File racesFile;
    private File dataFile;

    // کش برای سرعت بیشتر
    private final Map<UUID, String> raceCache = new HashMap<>();
    private final Map<UUID, Long> bedCooldownCache = new HashMap<>();
    private final Map<UUID, Long> raceChangeCooldownCache = new HashMap<>();

    public ConfigManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * بارگذاری اولیه تمام فایل‌ها
     */
    public void loadAll() {
        // config.yml
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();

        // races.yml
        racesFile = new File(plugin.getDataFolder(), "races.yml");
        if (!racesFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                racesFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("خطا در ساخت races.yml: " + e.getMessage());
            }
        }
        racesConfig = YamlConfiguration.loadConfiguration(racesFile);

        // data.yml
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("خطا در ساخت data.yml: " + e.getMessage());
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        // پر کردن کش
        loadCacheFromFiles();

        plugin.getLogger().info("تمام کانفیگ‌ها بارگذاری شد.");
    }

    private void loadCacheFromFiles() {
        raceCache.clear();
        bedCooldownCache.clear();
        raceChangeCooldownCache.clear();

        if (racesConfig.isConfigurationSection("players")) {
            for (String uuidStr : racesConfig.getConfigurationSection("players").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    String race = racesConfig.getString("players." + uuidStr);
                    if (race != null) {
                        raceCache.put(uuid, race);
                    }
                } catch (IllegalArgumentException ignored) {}
            }
        }

        if (dataConfig.isConfigurationSection("bedCooldown")) {
            for (String uuidStr : dataConfig.getConfigurationSection("bedCooldown").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    long time = dataConfig.getLong("bedCooldown." + uuidStr);
                    bedCooldownCache.put(uuid, time);
                } catch (IllegalArgumentException ignored) {}
            }
        }

        if (dataConfig.isConfigurationSection("raceChangeCooldown")) {
            for (String uuidStr : dataConfig.getConfigurationSection("raceChangeCooldown").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    long time = dataConfig.getLong("raceChangeCooldown." + uuidStr);
                    raceChangeCooldownCache.put(uuid, time);
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }

    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        racesConfig = YamlConfiguration.loadConfiguration(racesFile);
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        loadCacheFromFiles();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    // ==================== Race Storage ====================

    public String getPlayerRace(UUID uuid) {
        return raceCache.get(uuid);
    }

    public void setPlayerRace(UUID uuid, String raceId) {
        raceCache.put(uuid, raceId.toUpperCase());
        racesConfig.set("players." + uuid.toString(), raceId.toUpperCase());
        saveRacesFile();
    }

    public void removePlayerRace(UUID uuid) {
        raceCache.remove(uuid);
        racesConfig.set("players." + uuid.toString(), null);
        saveRacesFile();
    }

    public boolean hasRace(UUID uuid) {
        return raceCache.containsKey(uuid);
    }

    private void saveRacesFile() {
        try {
            racesConfig.save(racesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("خطا در ذخیره races.yml: " + e.getMessage());
        }
    }

    // ==================== Bed Cooldown ====================

    public long getBedCooldown(UUID uuid) {
        return bedCooldownCache.getOrDefault(uuid, 0L);
    }

    public void setBedCooldown(UUID uuid, long worldFullTime) {
        bedCooldownCache.put(uuid, worldFullTime);
        dataConfig.set("bedCooldown." + uuid.toString(), worldFullTime);
        saveDataFile();
    }

    // ==================== Race Change Cooldown ====================

    public long getRaceChangeCooldown(UUID uuid) {
        return raceChangeCooldownCache.getOrDefault(uuid, 0L);
    }

    public void setRaceChangeCooldown(UUID uuid, long worldFullTime) {
        raceChangeCooldownCache.put(uuid, worldFullTime);
        dataConfig.set("raceChangeCooldown." + uuid.toString(), worldFullTime);
        saveDataFile();
    }

    private void saveDataFile() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("خطا در ذخیره data.yml: " + e.getMessage());
        }
    }

    // ==================== Helper Config Getters ====================

    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    public String getString(String path, String def) {
        return config.getString(path, def);
    }
}

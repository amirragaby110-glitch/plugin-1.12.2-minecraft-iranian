package ir.iranian.hardcore.civilization;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Ancient Persian Civilization Progression Manager.
 */
public class CivilizationManager {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, CivilizationTier> playerTiers = new HashMap<>();
    private final Map<UUID, Integer> playerBossKills = new HashMap<>();
    private File civFile;
    private FileConfiguration civConfig;

    public CivilizationManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        loadData();
        plugin.getLogger().info("CivilizationManager: Persian Civilization progression initialized!");
    }

    public CivilizationTier getPlayerTier(Player player) {
        if (player == null) return CivilizationTier.NOMAD;
        return playerTiers.getOrDefault(player.getUniqueId(), CivilizationTier.NOMAD);
    }

    public int getMaxArmySize(Player player) {
        return getPlayerTier(player).getMaxSoldiers();
    }

    public int getBossKills(Player player) {
        if (player == null) return 0;
        return playerBossKills.getOrDefault(player.getUniqueId(), 0);
    }

    public void recordBossKill(Player player) {
        if (player == null) return;
        UUID uuid = player.getUniqueId();
        int kills = playerBossKills.getOrDefault(uuid, 0) + 1;
        playerBossKills.put(uuid, kills);
        saveData();
    }

    public boolean canUpgrade(Player player) {
        CivilizationTier current = getPlayerTier(player);
        CivilizationTier next = current.getNextTier();
        if (next == null) return false;

        long balance = 0;
        if (plugin.getCoinManager() != null) {
            balance = plugin.getCoinManager().getBalance(player);
        }

        int soldiers = 0;
        if (plugin.getArmyManager() != null && plugin.getArmyManager().getArmy(player) != null) {
            soldiers = plugin.getArmyManager().getArmy(player).getSize();
        }

        int bosses = getBossKills(player);

        return balance >= next.getRequiredCoins()
                && soldiers >= next.getRequiredSoldiers()
                && bosses >= next.getRequiredBosses();
    }

    public boolean upgradeTier(Player player) {
        if (!canUpgrade(player)) return false;
        CivilizationTier next = getPlayerTier(player).getNextTier();
        if (next == null) return false;

        // Deduct coins
        if (plugin.getCoinManager() != null && next.getRequiredCoins() > 0) {
            plugin.getCoinManager().deductBalance(player, next.getRequiredCoins());
        }

        playerTiers.put(player.getUniqueId(), next);
        saveData();

        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.5f, 1.0f);
        MessageUtils.sendTitle(player, "&6&lTAMADDON-E IRAN", "&aErtegha be: &e" + next.getDisplayName(), 10, 60, 20);
        player.sendMessage(MessageUtils.color("&8[Tamaddon] &aTabrik! Martabeh-ye tammadoni-ye shoma be &e" + next.getDisplayName() + " &aertegha yaft!"));
        player.sendMessage(MessageUtils.color("&8[Tamaddon] &7Zarfiat-e Artesh: &b" + next.getMaxSoldiers() + " Sarbaz"));
        return true;
    }

    private void loadData() {
        civFile = new File(plugin.getDataFolder(), "civilization.yml");
        if (!civFile.exists()) {
            civFile.getParentFile().mkdirs();
            try { civFile.createNewFile(); } catch (IOException ignored) {}
        }
        civConfig = YamlConfiguration.loadConfiguration(civFile);

        if (civConfig.contains("players")) {
            for (String key : civConfig.getConfigurationSection("players").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    String tierStr = civConfig.getString("players." + key + ".tier", "NOMAD");
                    int bosses = civConfig.getInt("players." + key + ".bosses", 0);
                    playerTiers.put(uuid, CivilizationTier.valueOf(tierStr));
                    playerBossKills.put(uuid, bosses);
                } catch (Exception ignored) {}
            }
        }
    }

    public void saveData() {
        if (civFile == null || civConfig == null) return;
        for (Map.Entry<UUID, CivilizationTier> entry : playerTiers.entrySet()) {
            String key = entry.getKey().toString();
            civConfig.set("players." + key + ".tier", entry.getValue().name());
            civConfig.set("players." + key + ".bosses", playerBossKills.getOrDefault(entry.getKey(), 0));
        }
        try { civConfig.save(civFile); } catch (IOException ignored) {}
    }
}

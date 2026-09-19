package ir.iranian.hardcore.language;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Language Manager - v5.0 Finglish
 * Uses 100% Finglish (Persian written in Latin alphabet) for full compatibility
 * with Spigot 1.12.2 and Aternos servers without character corruption.
 */
public class LanguageManager {

    private final IranianHardcorePlugin plugin;
    private String currentLanguage = "finglish";

    public LanguageManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        loadLanguage();
    }

    private void loadLanguage() {
        FileConfiguration config = plugin.getConfigManager().getConfig();
        currentLanguage = config.getString("general.language", "finglish").toLowerCase();
        if (!currentLanguage.equals("finglish") && !currentLanguage.equals("en")) {
            currentLanguage = "finglish";
        }
        plugin.getLogger().info("Language: " + currentLanguage + " (100% Finglish for Aternos compatibility)");
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public boolean isFinglish() {
        return true;
    }

    public boolean isPersian() {
        return true;
    }

    /**
     * Get text based on current language
     * @param persian Persian text
     * @param finglish Finglish text
     * @return appropriate text (always clean finglish)
     */
    public String getText(String persian, String finglish) {
        return finglish != null ? finglish : persian;
    }

    /**
     * Common translations - Finglish
     */
    public static class Translations {
        // General
        public static final String PREFIX_FA = "&8[&6Iran&8] &r";
        public static final String PREFIX_FINGLISH = "&8[&6Iran&8] &r";

        // Race selection
        public static final String RACE_CHOOSE_FA = "&eLotfan ghome Irani khod ra entekhab konid! &a/race choose &7- 14 ghom";
        public static final String RACE_CHOOSE_FINGLISH = "&eLotfan ghome Irani khod ra entekhab konid! &a/race choose &7- 14 ghom";

        public static final String RACE_SELECTED_FA = "&aGhome shoma be %race% taghir yaft! &6Zende bad Iran!";
        public static final String RACE_SELECTED_FINGLISH = "&aGhome shoma be %race% taghir yaft! &6Zende bad Iran!";

        // Thirst
        public static final String THIRST_LOW_FA = "&c&lTeshne hastid! &7Bayad ab benoshid!";
        public static final String THIRST_LOW_FINGLISH = "&c&lTeshne hastid! &7Bayad ab benoshid!";

        public static final String THIRST_CRITICAL_FA = "&4&lKhatar marg az teshnegi!";
        public static final String THIRST_CRITICAL_FINGLISH = "&4&lKhatar marg az teshnegi!";

        public static final String DRINK_WATER_FA = "&a&lAb noshidid! &7Teshnegi bartaraf shod";
        public static final String DRINK_WATER_FINGLISH = "&a&lAb noshidid! &7Teshnegi bartaraf shod";

        // Temperature
        public static final String COLD_FA = "&b&lSardetan ast! &7Nazdeke atash beravid ya lebas garme bepushid";
        public static final String COLD_FINGLISH = "&b&lSardetan ast! &7Nazdeke atash beravid ya lebas garme bepushid";

        public static final String HOT_FA = "&c&lGarmazade shodeid! &7Ab benoshid va dar saye bemanid";
        public static final String HOT_FINGLISH = "&c&lGarmazade shodeid! &7Ab benoshid va dar saye bemanid";

        public static final String FREEZING_FA = "&b&lDarid yakh mizanid!";
        public static final String FREEZING_FINGLISH = "&b&lDarid yakh mizanid!";

        public static final String HEATSTROKE_FA = "&c&lGarmazadegi shadid!";
        public static final String HEATSTROKE_FINGLISH = "&c&lGarmazadegi shadid!";

        // Mobs
        public static final String DIV_SPAWN_FA = "&4&lDiv zaher shod!";
        public static final String DIV_SPAWN_FINGLISH = "&4&lDiv zaher shod!";

        public static final String SIMURGH_SPAWN_FA = "&6&lSimorgh afsaneie zaher shod!";
        public static final String SIMURGH_SPAWN_FINGLISH = "&6&lSimorgh afsaneie zaher shod!";
    }
}

package ir.iranian.hardcore.language;

import ir.iranian.hardcore.IranianHardcorePlugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Language Manager - v4.0 Finglish
 * Supports Persian (fa) and Finglish (finglish) for servers where Persian characters don't display
 * Finglish = Persian written with English letters
 */
public class LanguageManager {

    private final IranianHardcorePlugin plugin;
    private String currentLanguage = "finglish"; // Default to finglish for compatibility

    public LanguageManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        loadLanguage();
    }

    private void loadLanguage() {
        FileConfiguration config = plugin.getConfigManager().getConfig();
        currentLanguage = config.getString("general.language", "finglish").toLowerCase();
        if (!currentLanguage.equals("fa") && !currentLanguage.equals("finglish") && !currentLanguage.equals("en")) {
            currentLanguage = "finglish";
        }
        plugin.getLogger().info("Language: " + currentLanguage + " (finglish = Persian with English letters for compatibility)");
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public boolean isFinglish() {
        return currentLanguage.equals("finglish");
    }

    public boolean isPersian() {
        return currentLanguage.equals("fa");
    }

    /**
     * Get text based on current language
     * @param persian Persian text
     * @param finglish Finglish text
     * @return appropriate text
     */
    public String getText(String persian, String finglish) {
        if (isFinglish()) {
            return finglish;
        }
        return persian;
    }

    /**
     * Common translations - Finglish
     */
    public static class Translations {
        // General
        public static final String PREFIX_FA = "&8[&6ایران&8] &r";
        public static final String PREFIX_FINGLISH = "&8[&6Iran&8] &r";

        // Race selection
        public static final String RACE_CHOOSE_FA = "&eلطفاً قوم ایرانی خود را انتخاب کنید! &a/race choose &7- 14 قوم";
        public static final String RACE_CHOOSE_FINGLISH = "&eLotfan ghome Irani khod ra entekhab konid! &a/race choose &7- 14 ghom";

        public static final String RACE_SELECTED_FA = "&aقوم شما به %race% تغییر یافت! &6زنده باد ایران!";
        public static final String RACE_SELECTED_FINGLISH = "&aGhome shoma be %race% taghir yaft! &6Zende bad Iran!";

        // Thirst
        public static final String THIRST_LOW_FA = "&c&lتشنه هستید! &7باید آب بنوشید!";
        public static final String THIRST_LOW_FINGLISH = "&c&lTeshne hastid! &7Bayad ab benoshid!";

        public static final String THIRST_CRITICAL_FA = "&4&lخطر مرگ از تشنگی!";
        public static final String THIRST_CRITICAL_FINGLISH = "&4&lKhatar marg az teshnegi!";

        public static final String DRINK_WATER_FA = "&a&lآب نوشیدید! &7تشنگی برطرف شد";
        public static final String DRINK_WATER_FINGLISH = "&a&lAb noshidid! &7Teshnegi bartaraf shod";

        // Temperature
        public static final String COLD_FA = "&b&lسردتان است! &7نزدیک آتش بروید یا لباس گرم بپوشید";
        public static final String COLD_FINGLISH = "&b&lSardetan ast! &7Nazdeke atash beravid ya lebas garme bepushid";

        public static final String HOT_FA = "&c&lگرمازده شده‌اید! &7آب بنوشید و در سایه بمانید";
        public static final String HOT_FINGLISH = "&c&lGarmazade shodeid! &7Ab benoshid va dar saye bemanid";

        public static final String FREEZING_FA = "&b&lدارید یخ می‌زنید!";
        public static final String FREEZING_FINGLISH = "&b&lDarid yakh mizanid!";

        public static final String HEATSTROKE_FA = "&c&lگرمازدگی شدید!";
        public static final String HEATSTROKE_FINGLISH = "&c&lGarmazadegi shadid!";

        // Mobs
        public static final String DIV_SPAWN_FA = "&4&lدیو ظاهر شد!";
        public static final String DIV_SPAWN_FINGLISH = "&4&lDiv zaher shod!";

        public static final String SIMURGH_SPAWN_FA = "&6&lسیمرغ افسانه‌ای ظاهر شد!";
        public static final String SIMURGH_SPAWN_FINGLISH = "&6&lSimorgh afsaneie zaher shod!";
    }
}

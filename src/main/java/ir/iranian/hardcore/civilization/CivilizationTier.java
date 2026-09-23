package ir.iranian.hardcore.civilization;

/**
 * Ancient Persian Civilization Progression Tiers (From Nomad to Achaemenid Empire).
 */
public enum CivilizationTier {

    NOMAD(
        "Mohajer (Nomad)",
        "Aghaz-e rah-e delavari dar sarzamin-e Iran",
        3, 0, 0, 0
    ),
    SETTLEMENT(
        "Abadi-ye Pars (Settlement)",
        "Bana-ye nakhostin abadi va ashiyaneh-ye yaran",
        6, 50, 1, 0
    ),
    VILLAGE(
        "Dehkadeh-ye Hakhamaneshi (Village)",
        "Gostaresh-e keshawarzi va khedmat-e Kamandaran",
        12, 200, 3, 1
    ),
    FORTIFIED_TOWN(
        "Shahr-e Mostahkam (Fortified Town)",
        "Sazeh-haye defa'i, borj-o-baroo va Savaran",
        20, 600, 8, 2
    ),
    SATRAPY(
        "Satrap-neshin (Imperial Satrapy)",
        "Farmandari-ye ostan-e bozorg va Sepah-e Javidan",
        35, 1500, 15, 4
    ),
    EMPIRE(
        "Shahanshahi-ye Hakhamaneshi (Persian Empire)",
        "Shokouh-e Takht-e Jamshid va Sepahbodan-e Khavar-e Mianeh",
        50, 4000, 25, 8
    );

    private final String displayName;
    private final String description;
    private final int maxSoldiers;
    private final int requiredCoins;
    private final int requiredSoldiers;
    private final int requiredBosses;

    CivilizationTier(String displayName, String description, int maxSoldiers,
                     int requiredCoins, int requiredSoldiers, int requiredBosses) {
        this.displayName = displayName;
        this.description = description;
        this.maxSoldiers = maxSoldiers;
        this.requiredCoins = requiredCoins;
        this.requiredSoldiers = requiredSoldiers;
        this.requiredBosses = requiredBosses;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int getMaxSoldiers() { return maxSoldiers; }
    public int getRequiredCoins() { return requiredCoins; }
    public int getRequiredSoldiers() { return requiredSoldiers; }
    public int getRequiredBosses() { return requiredBosses; }

    public CivilizationTier getNextTier() {
        int next = this.ordinal() + 1;
        if (next < values().length) {
            return values()[next];
        }
        return null;
    }
}

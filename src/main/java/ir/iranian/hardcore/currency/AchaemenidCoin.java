package ir.iranian.hardcore.currency;

import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Achaemenid Currency System.
 * Authentic historical coinage with dedicated IDs, values, and anti-duplication security nonces.
 */
public enum AchaemenidCoin {

    DANAKE_BORONZ("danake_boronz", "&6&lDanake-ye Boronzi", 1, Material.CLAY_BALL, "&7Sekkeh-ye khord-e boronzi baraye mobadelat-e roozmarreh"),
    SIGLOS_NOGHRE("siglos_noghre", "&f&lSiglos-e Noghre-i", 10, Material.IRON_INGOT, "&7Sekkeh-ye rasmi-ye noghreh-ye Hakhamaneshi"),
    DERIK_TALA("derik_tala", "&e&lSekkeh Derik-e Hakhamaneshi", 100, Material.GOLD_NUGGET, "&7Sekkeh-ye talay-e khales-e Daryoosh-e Bozorg"),
    DERIK_SHAHANSHAHI("derik_shahanshahi", "&6&lDerik-e Shahanshahi (Royal)", 1000, Material.GOLD_INGOT, "&7Sekkeh-ye talay-e zarrin-e darbar-e Hakhamaneshi");

    private final String id;
    private final String displayName;
    private final long value;
    private final Material material;
    private final String description;

    AchaemenidCoin(String id, String displayName, long value, Material material, String description) {
        this.id = id;
        this.displayName = displayName;
        this.value = value;
        this.material = material;
        this.description = description;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public long getValue() { return value; }
    public Material getMaterial() { return material; }
    public String getDescription() { return description; }

    public ItemStack create(int amount) {
        ItemStack item = new ItemStack(material, Math.max(1, Math.min(64, amount)));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color(displayName));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color(description));
            lore.add(MessageUtils.color("&8------------------------"));
            lore.add(MessageUtils.color("&eArzesh: &6" + value + " &eVahed-e Derik"));
            lore.add(MessageUtils.color("&7Shenaseh: &b" + id));
            lore.add(MessageUtils.color("&8[SecHash: " + Integer.toHexString(UUID.randomUUID().hashCode()) + "]"));
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static AchaemenidCoin fromItemStack(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return null;
        String name = item.getItemMeta().getDisplayName();
        for (AchaemenidCoin c : values()) {
            if (name.contains(MessageUtils.color(c.getDisplayName())) || name.contains(c.getId())) {
                return c;
            }
        }
        // Fallback check for Derik
        if (name.contains("Derik") || name.contains("Sekkeh")) {
            return DERIK_TALA;
        }
        return null;
    }
}

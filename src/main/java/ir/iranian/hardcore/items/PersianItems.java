package ir.iranian.hardcore.items;

import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Itemhaye Irani Tarikhi - v5.0 Finglish
 * Har dungeon loot makhsoose khod ra darad
 */
public class PersianItems {

    // ============ Shamshirha ============

    public static ItemStack createShamshirAlamut() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&8&lShamshir Alamut - Tighe Hassan Sabbah"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Shamshire afsaneie ghale Alamut",
                    "&7Motealegh be Hassan Sabbah, rahbar Esmaeelie",
                    "",
                    "&c&lVizhegiha:",
                    "&7* &cGhodrat: &f+10",
                    "&7* &aTiz dar koohestan",
                    "&7* &6Effect: Wither dar zarbe",
                    "",
                    "&8&lTarikh: &7Gharne 11 miladi - Alamut, Qazvin"
            )));
            meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
            meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
            meta.addEnchant(Enchantment.DURABILITY, 3, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createShamshirBabak() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&c&lShamshir Babak Khorramdin - Shir Azarbayjan"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Shamshire ghahremane melli Iran, Babak Khorramdin",
                    "&7Ke 22 sal dar barabare doshmanan moghavemat kard",
                    "",
                    "&c&lVizhegiha:",
                    "&7* &cGhodrat dar barf: &f+8",
                    "&7* &bMoghavemat be sarma",
                    "",
                    "&8&lTarikh: &7Gharne 9 miladi - Ghale Babak, Kaleybar"
            )));
            meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
            meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createShamshirHakhamaneshi() {
        ItemStack item = new ItemStack(Material.GOLD_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&lAkinake Hakhamaneshi - Shamshir Javidan"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Shamshire kootahe sarbazane Javidane Hakhamaneshi",
                    "&7Arteshe 10 hezar nafare garde shahanshahi",
                    "",
                    "&c&lVizhegiha:",
                    "&7* &6Ghodrate Elahi: &f+12",
                    "&7* &eShanse critical bala",
                    "",
                    "&8&lTarikh: &7550-330 ghabl az milad - Takht Jamshid"
            )));
            meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
            meta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
            meta.addEnchant(Enchantment.DURABILITY, 5, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    // ============ Tajha va Javaherat ============

    public static ItemStack createTajKourosh() {
        ItemStack item = new ItemStack(Material.GOLD_HELMET);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&lTaj Kourosh Bozorg - Shah Shahan"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Taje bonyan gozare Shahanshahi Hakhamaneshi",
                    "&7Kourosh Bozorg, azadkonandeye Babel",
                    "",
                    "&c&lVizhegiha:",
                    "&7* &aMoghavemat kamel",
                    "&7* &bGhodrate farmandehi",
                    "&7* &6Effect: Regeneration",
                    "",
                    "&8&lTarikh: &7600-530 ghabl az milad - Pasargad",
                    "&8&lShoar: &7Man Kourosh, Shah Jahan..."
            )));
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6, true);
            meta.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createManshurKourosh() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&e&lManshur Kourosh - Avalin Elamie Hoghoogh Bashar"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Manshoore Kourosh Bozorg, avalin elamie hoghooghe bashar jahan",
                    "&7Ke dar mooze British negahdari mishavad",
                    "",
                    "&f\"Manam Kourosh, Shahe Jahan, Shahe Bozorg...\"",
                    "&f\"Azadie adyan ra bargharar kardam...\"",
                    "",
                    "&a&lGhodrat: &fSolh va ashti",
                    "&8&lTarikh: &7539 ghabl az milad - Babel"
            )));
            meta.addEnchant(Enchantment.LUCK, 3, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    // ============ Farsh va Sanaye Dasti ============

    public static ItemStack createFarshKermani() {
        ItemStack item = new ItemStack(Material.CARPET, 1, (short) 14);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&c&lFarsh Dastbaf Kermani - Naghsh Shah Abbasi"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Farshe dastbafe Kerman ba naghshe Shah Abbasi",
                    "&7Bafte shode tavasote honarmandane Kermani",
                    "",
                    "&a&lVizhegi: &fGarma dar biaban va sarma",
                    "&8&lGhedmat: &7Honare 2500 sale Irani"
            )));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createMorghAmin() {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&b&lPare Simurgh - Parande Afsaneie Shahnameh"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Pare Simurgh, parandeye afsaneie Shahnameh Ferdowsi",
                    "&7Ke Zal ra dar Alborz bozorg kard",
                    "",
                    "&a&lGhodrat: &fParvaze kootah + Moghavemat soghoot",
                    "&8&lManba: &7Shahnameh Ferdowsi"
            )));
            meta.addEnchant(Enchantment.LUCK, 5, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    // ============ Ghaza va Nooshidani ============

    public static ItemStack createChaiIrani() {
        ItemStack item = new ItemStack(Material.POTION);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&lChai Irani Lahijan - Atre Behesht"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Chaye moattare Lahijan, behtarin chaye Iran",
                    "&7Kesht shode dar damanehaye Alborz",
                    "",
                    "&a&lEffect: &fSorat + Moghavemat + Garma",
                    "&8&lMakan: &7Lahijan, Gilan"
            )));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createMorvaridKhalij() {
        ItemStack item = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&b&lMorvarid Khalij Fars - Ashke Darya"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Morvaride nabe Khalije Hameshe Fars",
                    "&7Seyd shode tavasote ghavasane Boushehri",
                    "",
                    "&a&lGhodrat: &fTanaffos zir ab toolani",
                    "&8&lMakan: &7Bandar Siraf, Boushehr - Khalij Fars"
            )));
            meta.addEnchant(Enchantment.LUCK, 4, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    // ============ Lohha va Asare Bastani ============

    public static ItemStack createLohIlami() {
        ItemStack item = new ItemStack(Material.CLAY_BRICK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&lKhesht Elami - Khatte Mikhi"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Kheshte neveshte-dare Elami az Ziggurat Chogha Zanbil",
                    "&7Ba khatte mikhi Elami",
                    "",
                    "&f\"In maabad ra Untash-Napirisha baraye Inshushinak sakht\"",
                    "",
                    "&8&lGhedmat: &71250 ghabl az milad - Shush, Khuzestan"
            )));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createAtashMoghadas() {
        ItemStack item = new ItemStack(Material.FIREBALL);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&lAtashe Moghadas Zartoshti - Azargoshasb"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Atashe moghadase Zartoshtian, yeki az 3 atashe bozorg",
                    "&7Ke 700 sal dar Azargoshasb roshan bood",
                    "",
                    "&c&lGhodrat: &fMasooniat kamel be atash va lava",
                    "&8&lMakan: &7Atashkadeye Azargoshasb, Takht Soleyman"
            )));
            meta.addEnchant(Enchantment.FIRE_ASPECT, 3, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createSekkeHakhamaneshi() {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&e&lSekke Derik Hakhamaneshi - Avalin Sekke"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Sekke talaye Daryoosh Bozorg, avalin sekke beinolmelali jahan",
                    "&7Ba naghshe kamandare Hakhamaneshi",
                    "",
                    "&a&lArzesh: &fGhabele moamele dar bazar ba gheymate bala",
                    "&8&lTarikh: &7515 ghabl az milad - Takht Jamshid"
            )));
            item.setItemMeta(meta);
        }
        return item;
    }

    // ============ Salahhaye Vizhe ============

    public static ItemStack createKamanArash() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&a&lKamane Arash Kamangir - Marze Iran"));
            meta.setLore(MessageUtils.color(Arrays.asList(
                    "&7Kamane Arash Kamangir ke janesh ra baraye Iran dad",
                    "&7Va tirash ra az Alborz partab kard",
                    "",
                    "&c&lVizhegi: &fBorde 10 barabar + Ghodrate 8",
                    "&8&lAfsaneh: &7Arash marze Iran ra tayin kard"
            )));
            meta.addEnchant(Enchantment.ARROW_DAMAGE, 7, true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 3, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    // Random Iranian loot helper
    public static ItemStack getRandomPersianLoot() {
        int r = (int) (Math.random() * 10);
        switch (r) {
            case 0: return createSekkeHakhamaneshi();
            case 1: return createFarshKermani();
            case 2: return createMorvaridKhalij();
            case 3: return createChaiIrani();
            case 4: return createLohIlami();
            case 5: return createMorghAmin();
            case 6: return createAtashMoghadas();
            case 7: return new ItemStack(Material.GOLD_INGOT, 5);
            case 8: return new ItemStack(Material.DIAMOND, 2);
            default: return new ItemStack(Material.EMERALD, 3);
        }
    }
}

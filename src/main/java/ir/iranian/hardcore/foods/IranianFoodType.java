package ir.iranian.hardcore.foods;

import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * 500 Functional Iranian Foods & Drinks (v5.0 Finglish)
 * 50 Traditional Iranian Dishes x 10 Quality Tiers = 500 Craftable Foods!
 * Fully functional: restores hunger, restores thirst, adjusts body temperature, grants buffs.
 */
public class IranianFoodType {

    public static class BaseFood {
        private final int id;
        private final String code;
        private final String name;
        private final Material material;
        private final int hunger;
        private final double thirst;
        private final PotionEffectType effect;
        private final int effectDuration;
        private final int effectAmplifier;
        private final int tempChange;
        private final String description;

        public BaseFood(int id, String code, String name, Material material, int hunger, double thirst,
                        PotionEffectType effect, int effectDuration, int effectAmplifier, int tempChange, String description) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.material = material;
            this.hunger = hunger;
            this.thirst = thirst;
            this.effect = effect;
            this.effectDuration = effectDuration;
            this.effectAmplifier = effectAmplifier;
            this.tempChange = tempChange;
            this.description = description;
        }

        public int getId() { return id; }
        public String getCode() { return code; }
        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public int getHunger() { return hunger; }
        public double getThirst() { return thirst; }
        public PotionEffectType getEffect() { return effect; }
        public int getEffectDuration() { return effectDuration; }
        public int getEffectAmplifier() { return effectAmplifier; }
        public int getTempChange() { return tempChange; }
        public String getDescription() { return description; }
    }

    public static class FoodTier {
        private final int id;
        private final String name;
        private final int bonusHunger;
        private final double bonusThirst;
        private final PotionEffectType bonusEffect;
        private final String lore;

        public FoodTier(int id, String name, int bonusHunger, double bonusThirst, PotionEffectType bonusEffect, String lore) {
            this.id = id;
            this.name = name;
            this.bonusHunger = bonusHunger;
            this.bonusThirst = bonusThirst;
            this.bonusEffect = bonusEffect;
            this.lore = lore;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public int getBonusHunger() { return bonusHunger; }
        public double getBonusThirst() { return bonusThirst; }
        public PotionEffectType getBonusEffect() { return bonusEffect; }
        public String getLore() { return lore; }
    }

    public static class FoodData {
        private final String id;
        private final int number;
        private final BaseFood base;
        private final FoodTier tier;
        private final String displayName;
        private final int totalHunger;
        private final double totalThirst;
        private final int tempChange;

        public FoodData(String id, int number, BaseFood base, FoodTier tier) {
            this.id = id;
            this.number = number;
            this.base = base;
            this.tier = tier;
            this.displayName = "&e" + tier.getName() + " " + base.getName();
            this.totalHunger = Math.min(20, base.getHunger() + tier.getBonusHunger());
            this.totalThirst = Math.min(100.0, base.getThirst() + tier.getBonusThirst());
            this.tempChange = base.getTempChange();
        }

        public String getId() { return id; }
        public int getNumber() { return number; }
        public BaseFood getBase() { return base; }
        public FoodTier getTier() { return tier; }
        public String getDisplayName() { return displayName; }
        public int getTotalHunger() { return totalHunger; }
        public double getTotalThirst() { return totalThirst; }
        public int getTempChange() { return tempChange; }

        public ItemStack createItemStack() {
            ItemStack item = new ItemStack(base.getMaterial());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(MessageUtils.color(displayName));
                List<String> lore = new ArrayList<>();
                lore.add(MessageUtils.color("&8&m------------------------"));
                lore.add(MessageUtils.color("&7Ghaza-ye Asil-e Irani: &6" + base.getName()));
                lore.add(MessageUtils.color("&7Darajeh: &a" + tier.getName()));
                lore.add(MessageUtils.color("&a🍖 Ghorsat: &e+" + totalHunger + " Food"));
                lore.add(MessageUtils.color("&b💧 Teshnegi: &e+" + (int)totalThirst + "% Water"));
                if (tempChange < 0) {
                    lore.add(MessageUtils.color("&b❄ Dama: &b" + tempChange + "C (Khonak-konandeh)"));
                } else if (tempChange > 0) {
                    lore.add(MessageUtils.color("&c🔥 Dama: &c+" + tempChange + "C (Garm-konandeh)"));
                }
                lore.add(MessageUtils.color("&8&o" + base.getDescription()));
                lore.add(MessageUtils.color("&d&o" + tier.getLore()));
                lore.add(MessageUtils.color("&8&m------------------------"));
                lore.add(MessageUtils.color("&6[Ghaza-ye Irani] &7ID: &e" + id + " &7(#" + number + "/500)"));
                lore.add(MessageUtils.color("&aDast-pokht-e Irani &7- &bKhalij-e Hameshe Fars"));
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            return item;
        }
    }

    private static final List<BaseFood> BASES = new ArrayList<>();
    private static final List<FoodTier> TIERS = new ArrayList<>();
    private static final Map<String, FoodData> FOODS_BY_ID = new HashMap<>();
    private static final List<FoodData> ALL_FOODS = new ArrayList<>();
    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) return;

        // 50 Base Foods
        BASES.add(new BaseFood(1, "GHORMEH_SABZI", "Ghormeh Sabzi", Material.MUSHROOM_SOUP, 10, 35.0, PotionEffectType.REGENERATION, 160, 1, 0, "Khoresht-e sabzi-ye mo'atar ba loobiya va leemoo amani"));
        BASES.add(new BaseFood(2, "CHELO_KABAB_KOOBIDEH", "Chelo Kabab Koobideh", Material.COOKED_BEEF, 12, 25.0, PotionEffectType.INCREASE_DAMAGE, 200, 1, 0, "Kabab-e goosht-e goosfand ba berenj-e zaferani"));
        BASES.add(new BaseFood(3, "CHELO_KABAB_BARG", "Chelo Kabab Barg", Material.COOKED_BEEF, 14, 25.0, PotionEffectType.INCREASE_DAMAGE, 300, 2, 0, "Rasteh-ye goosfandi-ye laleh-goon ba polo"));
        BASES.add(new BaseFood(4, "JOOJEH_KABAB", "Joojeh Kabab", Material.COOKED_CHICKEN, 10, 25.0, PotionEffectType.SPEED, 240, 1, 0, "Joojeh-ye zaferani ba ab-leemoo"));
        BASES.add(new BaseFood(5, "FESENJAN", "Khoresht-e Fesenjan", Material.MUSHROOM_SOUP, 11, 30.0, PotionEffectType.DAMAGE_RESISTANCE, 240, 1, 0, "Khoresht-e gerdoo ba robb-e anar-e jangal"));
        BASES.add(new BaseFood(6, "TAHCHIN_MORGH", "Tahchin-e Morgh", Material.BREAD, 12, 20.0, PotionEffectType.ABSORPTION, 300, 1, 0, "Tahchin-e talaei-ye zaferani ba morgh va mast"));
        BASES.add(new BaseFood(7, "BAGHALI_POLO", "Baghali Polo ba Mahiche", Material.COOKED_BEEF, 14, 30.0, PotionEffectType.REGENERATION, 240, 2, 0, "Polo-ye shevid va baghali ba goosht-e mahiche"));
        BASES.add(new BaseFood(8, "DIZI_SANGI", "Dizi-ye Sangi", Material.RABBIT_STEW, 15, 45.0, PotionEffectType.INCREASE_DAMAGE, 300, 1, 5, "Abgoosht-e sonnati dar zarf-e sangi ba nokhod va goosht"));
        BASES.add(new BaseFood(9, "KASHK_BADEMJAN", "Kashk-e Bademjan", Material.BAKED_POTATO, 8, 25.0, PotionEffectType.FAST_DIGGING, 200, 1, 0, "Bademjan-e sorkh-shodeh ba kashk va na'na dagh"));
        BASES.add(new BaseFood(10, "MIRZA_GHASEMI", "Mirza Ghasemi", Material.BAKED_POTATO, 8, 25.0, PotionEffectType.NIGHT_VISION, 300, 0, 0, "Bademjan-e kebabi ba seer va gojeh-farangi"));
        BASES.add(new BaseFood(11, "ASH_RESHTEH", "Ash-e Reshteh", Material.MUSHROOM_SOUP, 12, 50.0, PotionEffectType.REGENERATION, 200, 1, 5, "Ash-e garm-e sabzi va reshteh ba piyaz-dagh va kashk"));
        BASES.add(new BaseFood(12, "KOOKOO_SABZI", "Kookoo Sabzi", Material.BREAD, 7, 20.0, PotionEffectType.JUMP, 200, 1, 0, "Kookoo-ye sabzijat-e taze ba zereshk va gerdoo"));
        BASES.add(new BaseFood(13, "NAN_SANGAK", "Nan-e Sangak", Material.BREAD, 7, 15.0, PotionEffectType.SATURATION, 100, 0, 0, "Nan-e sonnati-ye sang-paz-e kanoon-e ghadim"));
        BASES.add(new BaseFood(14, "NAN_BARBARI", "Nan-e Barbari", Material.BREAD, 6, 15.0, PotionEffectType.SATURATION, 80, 0, 0, "Nan-e pahn va zakhim ba konjed"));
        BASES.add(new BaseFood(15, "NAN_LAVASH", "Nan-e Lavash", Material.BREAD, 4, 10.0, PotionEffectType.SATURATION, 60, 0, 0, "Nan-e nazok va narm-e tanori"));
        BASES.add(new BaseFood(16, "NAN_TAFTOON", "Nan-e Taftoon", Material.BREAD, 5, 10.0, PotionEffectType.SATURATION, 70, 0, 0, "Nan-e gerd va khosh-tam"));
        BASES.add(new BaseFood(17, "KOTLET_TEHRANI", "Kotlet-e Tehrani", Material.COOKED_BEEF, 9, 15.0, PotionEffectType.SPEED, 180, 1, 0, "Kotlet-e goosht va sibzamini"));
        BASES.add(new BaseFood(18, "DOLMEH_BARG", "Dolmeh-ye Barge Mo", Material.APPLE, 8, 20.0, PotionEffectType.DAMAGE_RESISTANCE, 200, 1, 0, "Dolmeh-ye barge mo ba berenj va goosht"));
        BASES.add(new BaseFood(19, "ZERESHK_POLO", "Zereshk Polo ba Morgh", Material.COOKED_CHICKEN, 11, 25.0, PotionEffectType.HEALTH_BOOST, 300, 1, 0, "Zereshk-e sorkh va polo-ye zaferani ba morgh"));
        BASES.add(new BaseFood(20, "HALIM_BADEMJAN", "Halim Bademjan", Material.MUSHROOM_SOUP, 12, 35.0, PotionEffectType.REGENERATION, 200, 2, 0, "Halim-e kashk va bademjan ba goosht-e goosfand"));
        BASES.add(new BaseFood(21, "SHOLEZARD", "Sholezard Zaferani", Material.PUMPKIN_PIE, 8, 30.0, PotionEffectType.SPEED, 240, 2, 0, "Deser-e zaferan va golab ba darchen"));
        BASES.add(new BaseFood(22, "BASTANI_SONNATI", "Bastani Sonnati Zaferani", Material.COOKIE, 6, 40.0, PotionEffectType.FIRE_RESISTANCE, 240, 0, -15, "Bastani-ye zaferani ba pesteh va ser-shir"));
        BASES.add(new BaseFood(23, "FALOODEH_SHIRAZI", "Faloodeh Shirazi", Material.COOKIE, 5, 45.0, PotionEffectType.FIRE_RESISTANCE, 200, 0, -20, "Faloodeh-ye yakh-zadeh ba ab-leemoo-ye Shiraz"));
        BASES.add(new BaseFood(24, "GAZ_ISFAHAN", "Gaz-e Isfahan", Material.COOKIE, 6, 15.0, PotionEffectType.JUMP, 200, 2, 0, "Gaz-e angabin ba maghz-e pesteh"));
        BASES.add(new BaseFood(25, "SOHAN_QOM", "Sohan-e Qom", Material.COOKIE, 6, 15.0, PotionEffectType.INCREASE_DAMAGE, 180, 1, 0, "Sohan-e koreh-ei ba zaferan va javaneh-ye gandom"));
        BASES.add(new BaseFood(26, "BAKLAVA_TABRIZ", "Baklava-ye Tabriz", Material.COOKIE, 7, 15.0, PotionEffectType.SPEED, 200, 2, 0, "Shirini-ye la-be-la-ye pesteh va gerdoo"));
        BASES.add(new BaseFood(27, "DOOGH_SONNATI", "Doogh-e Sonnati ba Na'na", Material.POTION, 4, 60.0, PotionEffectType.DAMAGE_RESISTANCE, 200, 1, -15, "Doogh-e gazo-dar ba pooneh va gol-e sorkh"));
        BASES.add(new BaseFood(28, "SHARBAT_SEKANJABIN", "Sharbat-e Sekanjabin", Material.POTION, 3, 65.0, PotionEffectType.HEAL, 1, 0, -10, "Sharbat-e serkeh va angabin ba khiyar"));
        BASES.add(new BaseFood(29, "SHARBAT_KHAKSHIR", "Sharbat-e Khakshir", Material.POTION, 3, 70.0, PotionEffectType.FIRE_RESISTANCE, 300, 0, -25, "Khakshir-e khonak baraye daf'-e garmazadegi dar kavir"));
        BASES.add(new BaseFood(30, "SHARBAT_TOKHME_SHARBAT", "Sharbat-e Tokhm-e Sharbat", Material.POTION, 3, 75.0, PotionEffectType.REGENERATION, 200, 1, -15, "Tokhm-e sharbat ba ab-leemoo va golab"));
        BASES.add(new BaseFood(31, "CHAI_LAHIAJN", "Chai-ye Lahijan ba Nabat", Material.POTION, 2, 50.0, PotionEffectType.SPEED, 200, 1, 25, "Chai-ye dambakhsh-e Gilan ba nabat-e zaferani"));
        BASES.add(new BaseFood(32, "KABAB_TORSH", "Kabab Torsh-e Gilani", Material.COOKED_BEEF, 12, 25.0, PotionEffectType.INCREASE_DAMAGE, 240, 2, 0, "Goosht-e morinad ba anar va gerdoo"));
        BASES.add(new BaseFood(33, "MORASSA_POLO", "Morassa Polo", Material.COOKED_BEEF, 13, 25.0, PotionEffectType.LUCK, 300, 2, 0, "Polo-ye javaher-nishan ba khalal-e porteghal va pesteh"));
        BASES.add(new BaseFood(34, "ALBALOO_POLO", "Albaloo Polo ba Ghelgheli", Material.COOKED_BEEF, 11, 30.0, PotionEffectType.REGENERATION, 200, 1, 0, "Albaloo-ye shirin va torsh ba koofteh-ghelgheli"));
        BASES.add(new BaseFood(35, "GHEIMEH_NESAR", "Gheimeh Nesar-e Qazvin", Material.COOKED_BEEF, 13, 25.0, PotionEffectType.INCREASE_DAMAGE, 240, 1, 0, "Ghaza-ye majlesi-ye Qazvin ba khalal-e badam"));
        BASES.add(new BaseFood(36, "KHORESHT_GHEIMEH", "Khoresht-e Gheimeh", Material.MUSHROOM_SOUP, 10, 35.0, PotionEffectType.ABSORPTION, 240, 1, 0, "Gheimeh-ye sibzamini ba lapeh va leemoo"));
        BASES.add(new BaseFood(37, "KHORESHT_KARAFS", "Khoresht-e Karafs", Material.MUSHROOM_SOUP, 9, 40.0, PotionEffectType.SPEED, 240, 1, 0, "Karafs-e sorkh-shodeh ba na'na va ja'fari"));
        BASES.add(new BaseFood(38, "KALJOUSH", "Kaljoush-e Isfahani", Material.MUSHROOM_SOUP, 10, 45.0, PotionEffectType.DAMAGE_RESISTANCE, 240, 2, 0, "Kashk-e joush-amadeh ba gerdoo"));
        BASES.add(new BaseFood(39, "ESHKENEH", "Eshkeneh-ye Sonnati", Material.MUSHROOM_SOUP, 8, 40.0, PotionEffectType.DAMAGE_RESISTANCE, 180, 1, 10, "Soop-e sonnati-ye shanbalileh"));
        BASES.add(new BaseFood(40, "SHAMI_GILANI", "Shami-ye Gilani", Material.COOKED_BEEF, 9, 20.0, PotionEffectType.SPEED, 180, 1, 0, "Shami ba sabzi-ye chochagh"));
        BASES.add(new BaseFood(41, "KEBAB_CHENJEH", "Kebab Chenjeh", Material.COOKED_BEEF, 13, 20.0, PotionEffectType.INCREASE_DAMAGE, 260, 2, 0, "Tekkeh-haye goosht-e barreh"));
        BASES.add(new BaseFood(42, "MAHI_POLO", "Mahi Polo-ye Sabzi", Material.COOKED_FISH, 12, 25.0, PotionEffectType.WATER_BREATHING, 400, 0, 0, "Mahi-ye sefid-e Darya-ye Khazar ba sabzi-polo"));
        BASES.add(new BaseFood(43, "ABGOOSHT_BOZBASH", "Abgoosht-e Bozbash", Material.RABBIT_STEW, 13, 45.0, PotionEffectType.REGENERATION, 240, 1, 5, "Abgoosht ba sabzi va goosht"));
        BASES.add(new BaseFood(44, "YATIMCHEH", "Yatimcheh-ye Tabrizi", Material.BAKED_POTATO, 8, 35.0, PotionEffectType.SATURATION, 100, 0, 0, "Bademjan va kadoo-ye tabestani"));
        BASES.add(new BaseFood(45, "GHEIMEH_BADEMJAN", "Gheimeh Bademjan", Material.MUSHROOM_SOUP, 11, 35.0, PotionEffectType.ABSORPTION, 240, 1, 0, "Khoresht-e bademjan ba goosht"));
        BASES.add(new BaseFood(46, "HALVA_ZAFERANI", "Halva-ye Zaferani", Material.PUMPKIN_PIE, 7, 20.0, PotionEffectType.SPEED, 200, 1, 0, "Halva-ye ard-e gandom ba golab"));
        BASES.add(new BaseFood(47, "RANGINAK", "Ranginak-e Jonoob", Material.COOKIE, 8, 15.0, PotionEffectType.FIRE_RESISTANCE, 240, 0, 0, "Khorma va gerdoo-ye nakhlesatan"));
        BASES.add(new BaseFood(48, "NOON_PANIR_SABZI", "Noon va Panir va Sabzi", Material.BREAD, 7, 25.0, PotionEffectType.SATURATION, 120, 0, 0, "Sobhaneh-ye asil-e Irani ba panir-e Lighvan"));
        BASES.add(new BaseFood(49, "SAMANOO", "Samanoo-ye Nowruz", Material.PUMPKIN_PIE, 10, 30.0, PotionEffectType.REGENERATION, 300, 2, 0, "Shiresh-e javaneh-ye gandom-e haft-sin"));
        BASES.add(new BaseFood(50, "KACHI_SONNATI", "Kachi-ye Sonnati", Material.MUSHROOM_SOUP, 9, 25.0, PotionEffectType.HEAL, 1, 0, 10, "Kachi-ye roghan-heyvani ba adviyeh-jat"));

        // 10 Quality Tiers
        TIERS.add(new FoodTier(1, "Sonnati", 0, 0.0, null, "Dastpokht-e ghadimi va asil"));
        TIERS.add(new FoodTier(2, "Shahi", 2, 10.0, PotionEffectType.INCREASE_DAMAGE, "Layegh-e sofreh-ye padeshahan"));
        TIERS.add(new FoodTier(3, "Zaferani", 2, 10.0, PotionEffectType.SPEED, "Mo'atar ba behtarin zaferan-e Ghaenat"));
        TIERS.add(new FoodTier(4, "Bozorg", 4, 15.0, PotionEffectType.SATURATION, "Dis-e bozorg-e khanevadegi"));
        TIERS.add(new FoodTier(5, "Majlesi", 3, 15.0, PotionEffectType.REGENERATION, "Baraye jashn-ha va mehmankhaneh-ha"));
        TIERS.add(new FoodTier(6, "Khaneghi", 1, 5.0, PotionEffectType.ABSORPTION, "Ba eshgh va mehr-e madar"));
        TIERS.add(new FoodTier(7, "Darbari", 4, 20.0, PotionEffectType.DAMAGE_RESISTANCE, "Paziraei dar kakh-e Soltan"));
        TIERS.add(new FoodTier(8, "Tond va Atashin", 2, 5.0, PotionEffectType.FIRE_RESISTANCE, "Por-adviyeh va sho'levar"));
        TIERS.add(new FoodTier(9, "Khoshmazzeh", 2, 10.0, PotionEffectType.LUCK, "Tam-e bi-nazir va faramoosh-nashodani"));
        TIERS.add(new FoodTier(10, "Moqaddas", 5, 25.0, PotionEffectType.HEALTH_BOOST, "Tabarrok-yofteh dar aein-e kohan"));

        // Generate all 500 foods
        int counter = 1;
        for (FoodTier tier : TIERS) {
            for (BaseFood base : BASES) {
                String id = String.format("FOOD_%03d", counter);
                FoodData food = new FoodData(id, counter, base, tier);
                FOODS_BY_ID.put(id, food);
                ALL_FOODS.add(food);
                counter++;
            }
        }

        initialized = true;
    }

    public static FoodData getById(String id) {
        if (!initialized) init();
        return FOODS_BY_ID.get(id.toUpperCase());
    }

    public static FoodData getByNumber(int number) {
        if (!initialized) init();
        if (number < 1 || number > ALL_FOODS.size()) return null;
        return ALL_FOODS.get(number - 1);
    }

    public static List<FoodData> getAllFoods() {
        if (!initialized) init();
        return Collections.unmodifiableList(ALL_FOODS);
    }

    public static FoodData getRandomFood() {
        if (!initialized) init();
        int idx = new Random().nextInt(ALL_FOODS.size());
        return ALL_FOODS.get(idx);
    }

    public static int getTotalCount() {
        if (!initialized) init();
        return ALL_FOODS.size();
    }
}

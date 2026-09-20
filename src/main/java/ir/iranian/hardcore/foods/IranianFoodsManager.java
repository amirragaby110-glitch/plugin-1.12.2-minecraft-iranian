package ir.iranian.hardcore.foods;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.potion.PotionEffect;

import java.util.Random;

/**
 * Manager for 500 Functional Iranian Foods (v5.2 Finglish)
 * Supports distinct Shapeless Crafting Table recipes, dynamic cooking,
 * right-click consumption at full hunger, hunger/thirst/temp restoration, and buffs.
 */
public class IranianFoodsManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public IranianFoodsManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        IranianFoodType.init();
        registerDistinctRecipes();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("IranianFoodsManager: 500 Iranian foods registered successfully!");
    }

    private void registerDistinctRecipes() {
        try {
            // 1. Ghormeh Sabzi: Bowl + Cooked Beef + Wheat
            registerSingleFoodRecipe(1, "ghormeh_sabzi_craft", Material.BOWL, Material.COOKED_BEEF, Material.WHEAT);

            // 2. Dizi Sangak: Bowl + Bread + Cooked Mutton
            registerSingleFoodRecipe(2, "dizi_sangak_craft", Material.BOWL, Material.BREAD, Material.COOKED_MUTTON);

            // 3. Fesenjan: Bowl + Cooked Chicken + Apple
            registerSingleFoodRecipe(3, "fesenjan_craft", Material.BOWL, Material.COOKED_CHICKEN, Material.APPLE);

            // 4. Gheimeh: Bowl + Cooked Beef + Carrot
            registerSingleFoodRecipe(4, "gheimeh_craft", Material.BOWL, Material.COOKED_BEEF, Material.CARROT_ITEM);

            // 5. Zereshk Polo: Bread + Cooked Chicken + Seeds
            registerSingleFoodRecipe(5, "zereshk_polo_craft", Material.BREAD, Material.COOKED_CHICKEN, Material.SEEDS);

            // 6. Kabab Koobideh: Bread + Cooked Beef
            registerSingleFoodRecipe(6, "kabab_koobideh_craft", Material.BREAD, Material.COOKED_BEEF, Material.GRILLED_PORK);

            // 7. Falodeh Shirazi: Snowball + Sugar + Bowl
            registerSingleFoodRecipe(7, "falodeh_craft", Material.SNOW_BALL, Material.SUGAR, Material.BOWL);

            // 8. Chai Lahijan: Glass Bottle + Wheat + Sugar
            registerSingleFoodRecipe(8, "chai_lahijan_craft", Material.GLASS_BOTTLE, Material.WHEAT, Material.SUGAR);

            // 9. Halva Zaferani: Wheat + Sugar + Gold Nugget
            registerSingleFoodRecipe(9, "halva_craft", Material.WHEAT, Material.SUGAR, Material.GOLD_NUGGET);

            // 10. Doogh Abali: Milk Bucket + Glass Bottle
            registerSingleFoodRecipe(10, "doogh_abali_craft", Material.MILK_BUCKET, Material.GLASS_BOTTLE);

            // 11. Ash Reshteh: Bowl + Wheat + Potato
            registerSingleFoodRecipe(11, "ash_reshteh_craft", Material.BOWL, Material.WHEAT, Material.POTATO_ITEM);

            // 12. Sholezard: Bowl + Wheat + Sugar
            registerSingleFoodRecipe(12, "sholezard_craft", Material.BOWL, Material.WHEAT, Material.SUGAR);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not register all food recipes: " + e.getMessage());
        }
    }

    private void registerSingleFoodRecipe(int foodNumber, String keyName, Material... ingredients) {
        try {
            IranianFoodType.FoodData food = IranianFoodType.getByNumber(foodNumber);
            if (food == null) return;
            NamespacedKey key = new NamespacedKey(plugin, keyName);
            ShapelessRecipe recipe = new ShapelessRecipe(key, food.createItemStack());
            for (Material mat : ingredients) {
                recipe.addIngredient(mat);
            }
            Bukkit.addRecipe(recipe);
        } catch (Exception ignored) {}
    }

    /**
     * Dynamic Iranian Dish Crafting:
     * Base (Bowl, Bread, Bottle) + Protein (Beef, Chicken, Mutton, Fish) + Spice/Produce
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        if (matrix == null || matrix.length < 4) return;

        boolean hasBase = false;
        boolean hasMeat = false;
        boolean hasSpice = false;
        int count = 0;

        for (ItemStack item : matrix) {
            if (item == null || item.getType() == Material.AIR) continue;
            count++;
            Material t = item.getType();
            if (t == Material.BOWL || t == Material.BREAD || t == Material.GLASS_BOTTLE) {
                hasBase = true;
            } else if (t == Material.RAW_BEEF || t == Material.COOKED_BEEF || t == Material.RAW_CHICKEN 
                    || t == Material.COOKED_CHICKEN || t == Material.MUTTON || t == Material.COOKED_MUTTON 
                    || t == Material.RAW_FISH || t == Material.COOKED_FISH || t == Material.PORK || t == Material.GRILLED_PORK) {
                hasMeat = true;
            } else if (t == Material.WHEAT || t == Material.CARROT_ITEM || t == Material.POTATO_ITEM 
                    || t == Material.SUGAR || t == Material.APPLE || t == Material.MILK_BUCKET || t == Material.SNOW_BALL
                    || t == Material.SEEDS || t == Material.EGG || t == Material.BEETROOT) {
                hasSpice = true;
            }
        }

        if (hasBase && (hasMeat || hasSpice) && count >= 2 && count <= 4) {
            int randIndex = 1 + random.nextInt(IranianFoodType.getTotalCount());
            IranianFoodType.FoodData food = IranianFoodType.getByNumber(randIndex);
            if (food != null) {
                inv.setResult(food.createItemStack());
            }
        }
    }

    /**
     * Allows eating Iranian Foods even at full hunger (20/20) with Right-Click.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onRightClickFood(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String displayName = item.getItemMeta().getDisplayName();
        IranianFoodType.FoodData matched = null;

        for (IranianFoodType.FoodData food : IranianFoodType.getAllFoods()) {
            if (displayName.equals(MessageUtils.color(food.getDisplayName()))) {
                matched = food;
                break;
            }
        }

        if (matched != null) {
            event.setCancelled(true);

            // Deduct 1 food item from hand
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }

            consumeIranianFood(player, matched);
        }
    }

    /**
     * Fallback consumption listener when player eats normally with hunger < 20.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String name = item.getItemMeta().getDisplayName();
        Player player = event.getPlayer();

        for (IranianFoodType.FoodData food : IranianFoodType.getAllFoods()) {
            if (name.equals(MessageUtils.color(food.getDisplayName()))) {
                consumeIranianFood(player, food);
                return;
            }
        }
    }

    private void consumeIranianFood(Player player, IranianFoodType.FoodData food) {
        // Restore hunger and saturation
        player.setFoodLevel(Math.min(20, player.getFoodLevel() + food.getTotalHunger()));
        player.setSaturation(Math.min(20f, player.getSaturation() + (food.getTotalHunger() * 0.8f)));

        // Restore thirst
        if (plugin.getThirstManager() != null) {
            plugin.getThirstManager().addThirst(player, food.getTotalThirst());
        }

        // Apply Potion Effects
        if (food.getBase().getEffect() != null) {
            player.addPotionEffect(new PotionEffect(food.getBase().getEffect(), 
                    food.getBase().getEffectDuration() * 20, food.getBase().getEffectAmplifier()));
        }
        if (food.getTier().getBonusEffect() != null) {
            player.addPotionEffect(new PotionEffect(food.getTier().getBonusEffect(), 180 * 20, 0));
        }

        // Adjust Temperature
        if (plugin.getTemperatureManager() != null && food.getTempChange() != 0) {
            plugin.getTemperatureManager().adjustTemp(player, food.getTempChange());
        }

        // Burp and eating sounds
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.9f, 1.1f);
        player.sendMessage(MessageUtils.color("&8[&6Ghaza-ye Irani&8] &aBah bah! Che &e" + food.getBase().getName() + " &a- Noshe jan!"));
    }

    public boolean giveFood(Player player, String foodId) {
        IranianFoodType.FoodData food = IranianFoodType.getById(foodId);
        if (food == null) {
            try {
                int num = Integer.parseInt(foodId);
                food = IranianFoodType.getByNumber(num);
            } catch (NumberFormatException ignored) {}
        }
        if (food == null) return false;
        player.getInventory().addItem(food.createItemStack());
        player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aGhaza-ye &e" + food.getDisplayName() + " &a(#" + food.getNumber() + "/500) be shoma dadeh shod!"));
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.8f, 1.2f);
        return true;
    }

    public void giveRandomFood(Player player) {
        IranianFoodType.FoodData food = IranianFoodType.getRandomFood();
        if (food != null) {
            player.getInventory().addItem(food.createItemStack());
            player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aGhaza-ye tasadofi &e" + food.getDisplayName() + " &a(#" + food.getNumber() + "/500) dadeh shod!"));
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.8f, 1.2f);
        }
    }

    public IranianFoodType.FoodData getFood(String id) {
        return IranianFoodType.getById(id);
    }

    public ItemStack getRandomFood() {
        IranianFoodType.FoodData food = IranianFoodType.getRandomFood();
        return food != null ? food.createItemStack() : new ItemStack(Material.BREAD);
    }
}

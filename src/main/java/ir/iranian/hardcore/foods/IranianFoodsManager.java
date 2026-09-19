package ir.iranian.hardcore.foods;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Random;

/**
 * Manager for 500 Functional Iranian Foods (v5.0 Finglish)
 * Supports Crafting Table recipes, dynamic crafting, hunger/thirst/temp restoration, and buffs.
 */
public class IranianFoodsManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public IranianFoodsManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        IranianFoodType.init();
        registerBaseRecipes();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("IranianFoodsManager: 500 Iranian foods registered successfully!");
    }

    private void registerBaseRecipes() {
        try {
            // Register primary static shapeless recipes
            for (int i = 1; i <= Math.min(25, IranianFoodType.getTotalCount()); i++) {
                IranianFoodType.FoodData food = IranianFoodType.getByNumber(i);
                if (food == null) continue;

                NamespacedKey key = new NamespacedKey(plugin, "food_iran_" + food.getId().toLowerCase());
                ShapelessRecipe recipe = new ShapelessRecipe(key, food.createItemStack());
                recipe.addIngredient(Material.WHEAT);
                recipe.addIngredient(Material.BREAD);
                try {
                    Bukkit.addRecipe(recipe);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Could not register all food recipes: " + e.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        if (matrix == null || matrix.length < 9) return;

        boolean hasBowlOrBread = false;
        boolean hasMeat = false;
        boolean hasSpice = false;
        int count = 0;

        for (ItemStack item : matrix) {
            if (item == null || item.getType() == Material.AIR) continue;
            count++;
            Material t = item.getType();
            if (t == Material.BOWL || t == Material.BREAD || t == Material.GLASS_BOTTLE) {
                hasBowlOrBread = true;
            } else if (t == Material.RAW_BEEF || t == Material.COOKED_BEEF || t == Material.RAW_CHICKEN 
                    || t == Material.COOKED_CHICKEN || t == Material.MUTTON || t == Material.COOKED_MUTTON 
                    || t == Material.RAW_FISH || t == Material.COOKED_FISH) {
                hasMeat = true;
            } else if (t == Material.WHEAT || t == Material.CARROT_ITEM || t == Material.POTATO_ITEM 
                    || t == Material.SUGAR || t == Material.APPLE || t == Material.MILK_BUCKET) {
                hasSpice = true;
            }
        }

        // Crafting table combination: Bread/Bowl + Meat/Produce + Spice = Iranian Delicacy
        if (hasBowlOrBread && (hasMeat || hasSpice) && count >= 2 && count <= 4) {
            int randIndex = 1 + random.nextInt(IranianFoodType.getTotalCount());
            IranianFoodType.FoodData food = IranianFoodType.getByNumber(randIndex);
            if (food != null) {
                inv.setResult(food.createItemStack());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String name = item.getItemMeta().getDisplayName();
        Player player = event.getPlayer();

        // Check if consuming Iranian Food
        for (IranianFoodType.FoodData food : IranianFoodType.getAllFoods()) {
            if (name.equals(MessageUtils.color(food.getDisplayName()))) {
                // Restore hunger
                player.setFoodLevel(Math.min(20, player.getFoodLevel() + food.getTotalHunger()));
                player.setSaturation(Math.min(20f, player.getSaturation() + (food.getTotalHunger() * 0.8f)));

                // Restore thirst via ThirstManager
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

                // Play eating sounds and effects
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.8f, 1.1f);
                player.sendMessage(MessageUtils.color("&8[&6Ghaza-ye Irani&8] &aBah bah! Che &e" + food.getBase().getName() + " &a- Noshe jan!"));
                return;
            }
        }
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

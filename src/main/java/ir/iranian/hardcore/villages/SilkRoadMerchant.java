package ir.iranian.hardcore.villages;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.items.PersianAuthenticItems;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Silk Road Wandering Merchant (Bazargan-e Jadeh-ye Abrisham)
 * Features:
 * - Spawns at Caravanserais, Bazaars, and Roadside settlements
 * - Offers authentic Persian trades: Zaferan, Golab, Firoozeh, Pesteh, Sangak
 */
public class SilkRoadMerchant {

    public static Villager spawnMerchant(Location loc) {
        Villager merchant = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        merchant.setCustomName(MessageUtils.color("&6&lBazargan-e Jadeh-ye Abrisham"));
        merchant.setCustomNameVisible(true);
        merchant.setProfession(Villager.Profession.LIBRARIAN);
        merchant.setCareer(1);

        List<MerchantRecipe> recipes = new ArrayList<>();

        // 1. Zaferan-e Qaen (Trade 4 Gold Ingots for 2 Saffron)
        MerchantRecipe r1 = new MerchantRecipe(PersianAuthenticItems.createZaferanQaen(), 10);
        r1.addIngredient(new ItemStack(Material.GOLD_INGOT, 4));
        recipes.add(r1);

        // 2. Golab-e Ghamsar (Trade 2 Emeralds for 1 Rosewater)
        MerchantRecipe r2 = new MerchantRecipe(PersianAuthenticItems.createGolabGhamsar(), 8);
        r2.addIngredient(new ItemStack(Material.EMERALD, 2));
        recipes.add(r2);

        // 3. Pesteh-ye Rafsanjan (Trade 1 Gold Ingot for 6 Pistachios)
        ItemStack pistachios = PersianAuthenticItems.createPestehRafsanjan();
        pistachios.setAmount(6);
        MerchantRecipe r3 = new MerchantRecipe(pistachios, 12);
        r3.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));
        recipes.add(r3);

        // 4. Meel-e Bastani (Trade 16 Iron Ingots + 2 Gold Ingots for Meel)
        MerchantRecipe r4 = new MerchantRecipe(PersianAuthenticItems.createMeelBastani(), 3);
        r4.addIngredient(new ItemStack(Material.IRON_INGOT, 16));
        r4.addIngredient(new ItemStack(Material.GOLD_INGOT, 2));
        recipes.add(r4);

        // 5. Zang-e Zoorkhaneh (Trade 4 Gold Blocks for Sacred Bell)
        MerchantRecipe r5 = new MerchantRecipe(PersianAuthenticItems.createZangZoorkhaneh(), 2);
        r5.addIngredient(new ItemStack(Material.GOLD_BLOCK, 4));
        recipes.add(r5);

        merchant.setRecipes(recipes);
        return merchant;
    }
}

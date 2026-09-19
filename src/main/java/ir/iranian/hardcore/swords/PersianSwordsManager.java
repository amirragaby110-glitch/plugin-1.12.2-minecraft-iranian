package ir.iranian.hardcore.swords;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Manager for 900 Craftable Iranian Swords (v5.0 Finglish)
 * Supports Crafting Table recipes, dynamic elemental crafting, and combat perks.
 */
public class PersianSwordsManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    public PersianSwordsManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        PersianSwordType.init();
        registerBaseRecipes();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("PersianSwordsManager: 900 Iranian swords registered successfully!");
    }

    private void registerBaseRecipes() {
        try {
            // Register primary craftable recipes for base swords
            for (int i = 1; i <= Math.min(30, PersianSwordType.getTotalCount()); i++) {
                PersianSwordType.SwordData sword = PersianSwordType.getByNumber(i);
                if (sword == null) continue;

                NamespacedKey key = new NamespacedKey(plugin, "sword_iran_" + sword.getId().toLowerCase());
                ShapedRecipe recipe = new ShapedRecipe(key, sword.createItemStack());
                recipe.shape(" M ", " M ", " S ");
                recipe.setIngredient('S', Material.STICK);
                if (sword.getMaterial() == Material.DIAMOND_SWORD) {
                    recipe.setIngredient('M', Material.DIAMOND);
                } else if (sword.getMaterial() == Material.GOLD_SWORD) {
                    recipe.setIngredient('M', Material.GOLD_INGOT);
                } else {
                    recipe.setIngredient('M', Material.IRON_INGOT);
                }

                try {
                    Bukkit.addRecipe(recipe);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Could not register all static recipes: " + e.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        if (matrix == null || matrix.length < 9) return;

        ItemStack centerSword = null;
        Material catalyst = null;
        int itemsFound = 0;

        for (ItemStack item : matrix) {
            if (item == null || item.getType() == Material.AIR) continue;
            itemsFound++;
            if (item.getType().name().endsWith("_SWORD")) {
                centerSword = item;
            } else {
                catalyst = item.getType();
            }
        }

        // Crafting combination: 1 Sword + 1 Catalyst = Legendary Persian Sword
        if (itemsFound == 2 && centerSword != null && catalyst != null) {
            String prefixName = null;
            switch (catalyst) {
                case BLAZE_POWDER:
                case BLAZE_ROD:
                case MAGMA_CREAM:
                    prefixName = "Atashin";
                    break;
                case ICE:
                case PACKED_ICE:
                case SNOW_BALL:
                    prefixName = "Yakhi";
                    break;
                case SPIDER_EYE:
                case FERMENTED_SPIDER_EYE:
                    prefixName = "Zahri";
                    break;
                case GOLD_INGOT:
                case GOLD_BLOCK:
                    prefixName = "Shahanshahi";
                    break;
                case DIAMOND:
                case DIAMOND_BLOCK:
                    prefixName = "Rostam";
                    break;
                case EMERALD:
                    prefixName = "Kourosh";
                    break;
                case NETHER_STAR:
                    prefixName = "Barq-Asa";
                    break;
                case FEATHER:
                    prefixName = "Simurgh";
                    break;
                case ANVIL:
                case IRON_BLOCK:
                    prefixName = "Kaveh";
                    break;
                case REDSTONE:
                case GLOWSTONE_DUST:
                    prefixName = "Damavand";
                    break;
                default:
                    break;
            }

            if (prefixName != null) {
                int randNum = 1 + random.nextInt(900);
                PersianSwordType.SwordData chosen = PersianSwordType.getByNumber(randNum);
                if (chosen != null) {
                    inv.setResult(chosen.createItemStack());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta() || !weapon.getItemMeta().hasDisplayName()) return;

        String name = weapon.getItemMeta().getDisplayName();
        if (!name.contains("Shamshir") && !name.contains("Khanjar") && !name.contains("Ghadareh") 
                && !name.contains("Zolfaghar") && !name.contains("Tigh") && !name.contains("Gorz")
                && !name.contains("Tabar") && !name.contains("Neyzeh") && !name.contains("Kard")) return;

        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity victim = (LivingEntity) event.getEntity();

        // Elemental Effects
        if (name.contains("Atashin") || name.contains("Damavand")) {
            victim.setFireTicks(80);
            victim.getWorld().spawnParticle(Particle.FLAME, victim.getLocation().add(0, 1, 0), 10, 0.2, 0.2, 0.2, 0.05);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.8f, 1.2f);
        }
        if (name.contains("Yakhi")) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 2));
            victim.getWorld().spawnParticle(Particle.SNOW_SHOVEL, victim.getLocation().add(0, 1, 0), 12, 0.3, 0.3, 0.3, 0.05);
            victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.8f, 1.5f);
        }
        if (name.contains("Zahri")) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
            victim.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, victim.getLocation().add(0, 1, 0), 6, 0.2, 0.2, 0.2, 0.05);
        }
        if (name.contains("Barq-Asa")) {
            if (random.nextDouble() < 0.25) {
                victim.getWorld().strikeLightningEffect(victim.getLocation());
                event.setDamage(event.getDamage() + 4.0);
                player.sendMessage(MessageUtils.color("&e[Barq-Asa] &bSa'egheh-ye asil-e Irani bar doshman forood amad!"));
            }
        }
        if (name.contains("Rostam")) {
            victim.setVelocity(player.getLocation().getDirection().multiply(1.3).setY(0.4));
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 1.0f, 0.8f);
            if (random.nextDouble() < 0.20) {
                player.sendMessage(MessageUtils.color("&6[Ghodrat-e Rostam] &cZarbeh-ye sangin-e Pahlavan-e Zabol zadeh shod!"));
            }
        }
        if (name.contains("Simurgh")) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
        }
        if (name.contains("Khun-Riz")) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
        }
    }

    public boolean giveSword(Player player, String swordId) {
        PersianSwordType.SwordData sword = PersianSwordType.getById(swordId);
        if (sword == null) {
            try {
                int num = Integer.parseInt(swordId);
                sword = PersianSwordType.getByNumber(num);
            } catch (NumberFormatException ignored) {}
        }
        if (sword == null) return false;
        player.getInventory().addItem(sword.createItemStack());
        player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aShamshir &e" + sword.getDisplayName() + " &a(#" + sword.getNumber() + "/900) be shoma dadeh shod!"));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.2f);
        return true;
    }

    public void giveRandomSword(Player player) {
        PersianSwordType.SwordData sword = PersianSwordType.getRandomSword();
        if (sword != null) {
            player.getInventory().addItem(sword.createItemStack());
            player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aShamshir-e tasadofi &e" + sword.getDisplayName() + " &a(#" + sword.getNumber() + "/900) dadeh shod!"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.2f);
        }
    }

    public ItemStack getRandomSword() {
        PersianSwordType.SwordData sword = PersianSwordType.getRandomSword();
        return sword != null ? sword.createItemStack() : new ItemStack(Material.DIAMOND_SWORD);
    }

    public PersianSwordType.SwordData getSword(String id) {
        return PersianSwordType.getById(id);
    }
}

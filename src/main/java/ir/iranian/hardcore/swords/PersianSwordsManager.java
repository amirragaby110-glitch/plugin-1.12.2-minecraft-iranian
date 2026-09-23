package ir.iranian.hardcore.swords;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Manager for 900 Craftable Iranian Swords (v5.2 Finglish)
 * Supports unique diagonal Crafting Table recipes, dynamic elemental crafting,
 * right-click special abilities, and combat perks.
 */
public class PersianSwordsManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();
    private final Map<UUID, Long> abilityCooldown = new HashMap<>();

    public PersianSwordsManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        PersianSwordType.init();
        registerDistinctRecipes();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("PersianSwordsManager: 900 Iranian swords registered successfully!");
    }

    private void registerDistinctRecipes() {
        try {
            // 1. Shamshir Kourosh Bozorg (Gold + Diamond + Stick diagonal)
            PersianSwordType.SwordData s1 = PersianSwordType.getByNumber(1);
            if (s1 != null) {
                NamespacedKey k1 = new NamespacedKey(plugin, "sword_kourosh_craft");
                ShapedRecipe r1 = new ShapedRecipe(k1, s1.createItemStack());
                r1.shape("  G", " D ", "S  ");
                r1.setIngredient('G', Material.GOLD_INGOT);
                r1.setIngredient('D', Material.DIAMOND);
                r1.setIngredient('S', Material.STICK);
                Bukkit.addRecipe(r1);
            }

            // 2. Tigh Hassan Sabbah (Alamut) (Obsidian + Iron + Stick)
            PersianSwordType.SwordData s2 = PersianSwordType.getByNumber(2);
            if (s2 != null) {
                NamespacedKey k2 = new NamespacedKey(plugin, "sword_alamut_craft");
                ShapedRecipe r2 = new ShapedRecipe(k2, s2.createItemStack());
                r2.shape("  O", " I ", "S  ");
                r2.setIngredient('O', Material.OBSIDIAN);
                r2.setIngredient('I', Material.IRON_INGOT);
                r2.setIngredient('S', Material.STICK);
                Bukkit.addRecipe(r2);
            }

            // 3. Shamshir Babak Khorramdin (Redstone + Iron + Stick)
            PersianSwordType.SwordData s3 = PersianSwordType.getByNumber(3);
            if (s3 != null) {
                NamespacedKey k3 = new NamespacedKey(plugin, "sword_babak_craft");
                ShapedRecipe r3 = new ShapedRecipe(k3, s3.createItemStack());
                r3.shape("  R", " I ", "S  ");
                r3.setIngredient('R', Material.REDSTONE);
                r3.setIngredient('I', Material.IRON_INGOT);
                r3.setIngredient('S', Material.STICK);
                Bukkit.addRecipe(r3);
            }

            // 4. Zolfaghar (2 Diamonds + Gold Ingot + Stick)
            PersianSwordType.SwordData s4 = PersianSwordType.getByNumber(4);
            if (s4 != null) {
                NamespacedKey k4 = new NamespacedKey(plugin, "sword_zulfiqar_craft");
                ShapedRecipe r4 = new ShapedRecipe(k4, s4.createItemStack());
                r4.shape("D D", " G ", " S ");
                r4.setIngredient('D', Material.DIAMOND);
                r4.setIngredient('G', Material.GOLD_INGOT);
                r4.setIngredient('S', Material.STICK);
                Bukkit.addRecipe(r4);
            }

            // 5. Gorz-e Rostam Dastan (Iron Block + Iron Ingot + Stick)
            PersianSwordType.SwordData s5 = PersianSwordType.getByNumber(5);
            if (s5 != null) {
                NamespacedKey k5 = new NamespacedKey(plugin, "sword_rostam_craft");
                ShapedRecipe r5 = new ShapedRecipe(k5, s5.createItemStack());
                r5.shape(" B ", " I ", " S ");
                r5.setIngredient('B', Material.IRON_BLOCK);
                r5.setIngredient('I', Material.IRON_INGOT);
                r5.setIngredient('S', Material.STICK);
                Bukkit.addRecipe(r5);
            }

            // 6. Akinake Hakhamaneshi (Gold Ingot + Gold Ingot + Blaze Powder diagonal)
            PersianSwordType.SwordData s6 = PersianSwordType.getByNumber(6);
            if (s6 != null) {
                NamespacedKey k6 = new NamespacedKey(plugin, "sword_akinake_craft");
                ShapedRecipe r6 = new ShapedRecipe(k6, s6.createItemStack());
                r6.shape("  G", " G ", "S  ");
                r6.setIngredient('G', Material.GOLD_INGOT);
                r6.setIngredient('S', Material.BLAZE_ROD);
                Bukkit.addRecipe(r6);
            }

            // 7. Shamshir Atashin Damavand (Blaze Powder + Diamond + Stick)
            PersianSwordType.SwordData s7 = PersianSwordType.getByNumber(7);
            if (s7 != null) {
                NamespacedKey k7 = new NamespacedKey(plugin, "sword_damavand_craft");
                ShapedRecipe r7 = new ShapedRecipe(k7, s7.createItemStack());
                r7.shape("  B", " D ", "S  ");
                r7.setIngredient('B', Material.BLAZE_POWDER);
                r7.setIngredient('D', Material.DIAMOND);
                r7.setIngredient('S', Material.STICK);
                Bukkit.addRecipe(r7);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Could not register all static sword recipes: " + e.getMessage());
        }
    }

    /**
     * Dynamic Elemental Crafting in Crafting Table or Player 2x2 grid.
     * 1 Sword + 1 Catalyst = Upgraded Elemental Persian Sword!
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        if (matrix == null || matrix.length < 4) return;

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

        if (itemsFound == 2 && centerSword != null && catalyst != null) {
            String prefixName = null;
            switch (catalyst) {
                case BLAZE_POWDER:
                case BLAZE_ROD:
                case MAGMA_CREAM:
                case FIREBALL:
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
                case GOLD_NUGGET:
                    prefixName = "Shahanshahi";
                    break;
                case DIAMOND:
                case DIAMOND_BLOCK:
                    prefixName = "Rostam";
                    break;
                case EMERALD:
                case EMERALD_BLOCK:
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
                case IRON_INGOT:
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

    /**
     * Right-Click Special Abilities for Persian Swords (12-second cooldown).
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onSwordRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta() || !weapon.getItemMeta().hasDisplayName()) return;

        String name = weapon.getItemMeta().getDisplayName();
        if (!name.contains("Shamshir") && !name.contains("Tigh") && !name.contains("Gorz") 
                && !name.contains("Akinake") && !name.contains("Zolfaghar")) return;

        long now = System.currentTimeMillis();
        long last = abilityCooldown.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < 12000) {
            long remaining = (12000 - (now - last)) / 1000;
            player.sendMessage(MessageUtils.color("&8[&6Iran&8] &7Ghodrat-e vizheh &e" + remaining + " saniyeh &7digar amadeh mishavad."));
            return;
        }

        abilityCooldown.put(player.getUniqueId(), now);
        Location loc = player.getLocation();
        World world = player.getWorld();

        if (name.contains("Atashin") || name.contains("Damavand") || name.contains("Azargoshasb")) {
            world.spawnParticle(Particle.FLAME, loc.clone().add(0, 1, 0), 40, 1.5, 0.5, 1.5, 0.08);
            world.playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
            for (LivingEntity nearby : world.getEntitiesByClass(LivingEntity.class)) {
                if (nearby != player && nearby.getLocation().distance(loc) <= 6.0) {
                    nearby.setFireTicks(100);
                    nearby.damage(6.0, player);
                }
            }
            player.sendMessage(MessageUtils.color("&6&l[Atash-e Damavand] &eSholeh-haye koohestan doshmanan ra soozand!"));
        } else if (name.contains("Yakhi")) {
            world.spawnParticle(Particle.SNOW_SHOVEL, loc.clone().add(0, 1, 0), 50, 2.0, 0.5, 2.0, 0.05);
            world.playSound(loc, Sound.BLOCK_GLASS_BREAK, 1.0f, 1.2f);
            for (LivingEntity nearby : world.getEntitiesByClass(LivingEntity.class)) {
                if (nearby != player && nearby.getLocation().distance(loc) <= 6.0) {
                    nearby.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 2));
                    nearby.damage(4.0, player);
                }
            }
            player.sendMessage(MessageUtils.color("&b&l[Koolak-e Alborz] &bDoshmanan dar yakh va sarma monjamed shodand!"));
        } else if (name.contains("Barq-Asa") || name.contains("Kourosh")) {
            world.strikeLightningEffect(loc.clone().add(loc.getDirection().multiply(4)));
            world.playSound(loc, Sound.ENTITY_LIGHTNING_THUNDER, 1.0f, 1.2f);
            for (LivingEntity nearby : world.getEntitiesByClass(LivingEntity.class)) {
                if (nearby != player && nearby.getLocation().distance(loc) <= 7.0) {
                    nearby.damage(8.0, player);
                }
            }
            player.sendMessage(MessageUtils.color("&e&l[Barq-e Shahanshahi] &aSa'egheh-ye asil-e Pars doshmanan ra nabood kard!"));
        } else {
            // General Rostam Battle Roar
            world.spawnParticle(Particle.CRIT_MAGIC, loc.clone().add(0, 1, 0), 30, 1.0, 0.5, 1.0, 0.1);
            world.playSound(loc, Sound.ENTITY_IRONGOLEM_ATTACK, 1.0f, 0.8f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 0));
            player.sendMessage(MessageUtils.color("&6&l[Faryad-e Hamaseh] &cGhodrat va sor'at-e Pahlavan afzayesh yaft!"));
        }
    }

    /**
     * Elemental Combat Perks on hitting living targets.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta() || !weapon.getItemMeta().hasDisplayName()) return;

        String name = weapon.getItemMeta().getDisplayName();
        if (!name.contains("Shamshir") && !name.contains("Khanjar") && !name.contains("Ghadareh") 
                && !name.contains("Zolfaghar") && !name.contains("Tigh") && !name.contains("Gorz")
                && !name.contains("Tabar") && !name.contains("Neyzeh") && !name.contains("Kard")
                && !name.contains("Akinake")) return;

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
            if (random.nextDouble() < 0.30) {
                victim.getWorld().strikeLightningEffect(victim.getLocation());
                event.setDamage(event.getDamage() + 4.0);
                player.sendMessage(MessageUtils.color("&e[Barq-Asa] &bSa'egheh-ye asil-e Irani bar doshman forood amad!"));
            }
        }
        if (name.contains("Rostam")) {
            victim.setVelocity(player.getLocation().getDirection().multiply(1.3).setY(0.4));
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 1.0f, 0.8f);
        }
        if (name.contains("Simurgh")) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
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

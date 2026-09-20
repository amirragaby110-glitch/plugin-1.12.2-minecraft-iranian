package ir.iranian.hardcore.items;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Dedicated Boss-Hunting Weapons & Tactical Gear (v5.2 Finglish)
 * Features:
 * 1. Tigh-e Div-Kosh (+18 True Damage to Bosses + 25% Stun)
 * 2. Kaman-e Shekar-e Div (Boss Piercing Warbow - 2x Boss Damage)
 * 3. Bomb-e Naft-e Siah (Throwable Naphtha Firebomb - Melts Boss Armor)
 * 4. Separ-e Derafsh-e Kaviani (Aegis Shield - Reflects Boss Damage)
 * 5. Telesm-e Jam-e Jam (Talisman of Jamshid - Strips Boss Buffs & Exposes Weak Point)
 */
public class PersianBossItems implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Long> shieldCooldown = new HashMap<>();
    private final Map<UUID, Long> talismanCooldown = new HashMap<>();
    private final Random random = new Random();

    public PersianBossItems(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        registerBossItemRecipes();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("PersianBossItems: Dedicated boss-slayer weapons & tactical items initialized!");
    }

    private void registerBossItemRecipes() {
        try {
            // 1. Tigh-e Div-Kosh (Diamond Sword + Nether Star + 2 Sekkeh Derik)
            NamespacedKey swordKey = new NamespacedKey(plugin, "recipe_tigh_div_kosh");
            ShapedRecipe swordRecipe = new ShapedRecipe(swordKey, createDivKoshSword());
            swordRecipe.shape(" N ", " S ", " D ");
            swordRecipe.setIngredient('N', Material.NETHER_STAR);
            swordRecipe.setIngredient('S', Material.DIAMOND_SWORD);
            swordRecipe.setIngredient('D', Material.GOLD_NUGGET);
            Bukkit.addRecipe(swordRecipe);

            // 2. Kaman-e Shekar-e Div (Bow + 2 Diamonds + 2 Strings + 1 Emerald)
            NamespacedKey bowKey = new NamespacedKey(plugin, "recipe_kaman_div");
            ShapedRecipe bowRecipe = new ShapedRecipe(bowKey, createBossPiercerBow());
            bowRecipe.shape(" DS", "D B", " ES");
            bowRecipe.setIngredient('D', Material.DIAMOND);
            bowRecipe.setIngredient('S', Material.STRING);
            bowRecipe.setIngredient('B', Material.BOW);
            bowRecipe.setIngredient('E', Material.EMERALD);
            Bukkit.addRecipe(bowRecipe);

            // 3. Bomb-e Naft-e Siah (Potion Bottle + Gunpowder + Coal + Blaze Powder)
            NamespacedKey bombKey = new NamespacedKey(plugin, "recipe_bomb_naft");
            ShapedRecipe bombRecipe = new ShapedRecipe(bombKey, createNaphthaBomb(3));
            bombRecipe.shape(" B ", "GPG", " C ");
            bombRecipe.setIngredient('B', Material.BLAZE_POWDER);
            bombRecipe.setIngredient('G', Material.SULPHUR);
            bombRecipe.setIngredient('P', Material.GLASS_BOTTLE);
            bombRecipe.setIngredient('C', Material.COAL);
            Bukkit.addRecipe(bombRecipe);

            // 4. Separ-e Derafsh-e Kaviani (Shield + Gold Block + Red Carpet + Purple Carpet)
            NamespacedKey shieldKey = new NamespacedKey(plugin, "recipe_separ_kaviani");
            ShapedRecipe shieldRecipe = new ShapedRecipe(shieldKey, createDerafshShield());
            shieldRecipe.shape(" R ", "GSG", " P ");
            shieldRecipe.setIngredient('R', Material.CARPET); // Red
            shieldRecipe.setIngredient('G', Material.GOLD_INGOT);
            shieldRecipe.setIngredient('S', Material.SHIELD);
            shieldRecipe.setIngredient('P', Material.DIAMOND);
            Bukkit.addRecipe(shieldRecipe);

            // 5. Telesm-e Jam-e Jam (Clock + 4 Lapis + 2 Sekkeh Derik + Eye of Ender)
            NamespacedKey talismanKey = new NamespacedKey(plugin, "recipe_jam_e_jam");
            ShapedRecipe talismanRecipe = new ShapedRecipe(talismanKey, createJamshidTalisman());
            talismanRecipe.shape(" L ", "ECE", " D ");
            talismanRecipe.setIngredient('L', Material.LAPIS_BLOCK);
            talismanRecipe.setIngredient('E', Material.EYE_OF_ENDER);
            talismanRecipe.setIngredient('C', Material.WATCH);
            talismanRecipe.setIngredient('D', Material.GOLD_NUGGET);
            Bukkit.addRecipe(talismanRecipe);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not register all boss item recipes: " + e.getMessage());
        }
    }

    /**
     * Combat Perk: Tigh-e Div-Kosh bonus boss damage & stun.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onDivKoshAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta() || !weapon.getItemMeta().hasDisplayName()) return;

        String name = weapon.getItemMeta().getDisplayName();
        if (!name.contains("Tigh-e Div-Kosh")) return;

        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity victim = (LivingEntity) event.getEntity();

        // Check if victim is a dungeon boss
        boolean isBoss = victim.hasMetadata("dungeon_boss");

        if (isBoss) {
            // Massive +18 true damage to boss
            event.setDamage(event.getDamage() + 18.0);
            victim.getWorld().spawnParticle(Particle.CRIT_MAGIC, victim.getLocation().add(0, 1.5, 0), 20, 0.5, 0.5, 0.5, 0.2);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_LIGHTNING_IMPACT, 1.0f, 1.4f);

            // 25% Stun Chance
            if (random.nextDouble() < 0.25) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 4));
                victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 1));
                victim.getWorld().strikeLightningEffect(victim.getLocation());
                player.sendMessage(MessageUtils.color("&6&l[Div-Kosh] &c&lZARBEH-YE GHOOL-KOSH! &eBoss baraye 2 saniyeh gij shod!"));
            }
        }
    }

    /**
     * Combat Perk: Kaman-e Shekar-e Div arrow launch.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBowLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();
        if (!(arrow.getShooter() instanceof Player)) return;

        Player player = (Player) arrow.getShooter();
        ItemStack bow = player.getInventory().getItemInMainHand();
        if (bow == null || !bow.hasItemMeta() || !bow.getItemMeta().hasDisplayName()) return;

        if (bow.getItemMeta().getDisplayName().contains("Kaman-e Shekar-e Div")) {
            arrow.setMetadata("boss_piercer", new FixedMetadataValue(plugin, true));
            arrow.setCritical(true);
        }
    }

    /**
     * Combat Perk: Kaman-e Shekar-e Div arrow hits boss.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onArrowHitBoss(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getDamager();
        if (!arrow.hasMetadata("boss_piercer")) return;

        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity victim = (LivingEntity) event.getEntity();
            boolean isBoss = victim.hasMetadata("dungeon_boss");

            if (isBoss) {
                // Double damage on boss
                event.setDamage(event.getDamage() * 2.0 + 10.0);
                victim.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, victim.getLocation(), 2, 0.2, 0.2, 0.2, 0.05);
                victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 1.5f);
            }
        }
    }

    /**
     * Throwing Bomb-e Naft-e Siah (Right click snowball/grenade).
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onThrowNaphtha(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();

        // Do not intercept if player is trying to open a chest, furnace, door, workbench, etc.
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && !player.isSneaking()) {
            Material m = event.getClickedBlock().getType();
            if (m == Material.CHEST || m == Material.TRAPPED_CHEST || m == Material.ENDER_CHEST ||
                m == Material.FURNACE || m == Material.BURNING_FURNACE || m == Material.WORKBENCH ||
                m == Material.ANVIL || m == Material.ENCHANTMENT_TABLE || m == Material.BREWING_STAND ||
                m == Material.LEVER || m == Material.STONE_BUTTON || m == Material.WOOD_BUTTON ||
                m == Material.WOODEN_DOOR || m == Material.IRON_DOOR_BLOCK || m == Material.TRAP_DOOR ||
                m == Material.FENCE_GATE || m == Material.BED || m == Material.BED_BLOCK ||
                m == Material.DROPPER || m == Material.DISPENSER || m == Material.HOPPER) {
                return;
            }
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        if (item.getItemMeta().getDisplayName().contains("Bomb-e Naft-e Siah")) {
            event.setCancelled(true);

            // Deduct 1 bomb
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }

            // Launch explosive fireball / snowball
            Snowball bomb = player.launchProjectile(Snowball.class);
            bomb.setMetadata("naphtha_bomb", new FixedMetadataValue(plugin, player.getUniqueId().toString()));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.0f, 0.8f);
            player.sendMessage(MessageUtils.color("&8[&6Iran&8] &6Bomb-e naft-e siah partab shod!"));
        }
    }

    /**
     * Naphtha Bomb Impact Explosion.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onNaphthaHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball)) return;
        Snowball bomb = (Snowball) event.getEntity();
        if (!bomb.hasMetadata("naphtha_bomb")) return;

        Location loc = bomb.getLocation();
        World world = loc.getWorld();

        world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 3, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticle(Particle.FLAME, loc, 60, 2.0, 1.0, 2.0, 0.15);
        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 0.8f);

        String throwerId = bomb.getMetadata("naphtha_bomb").isEmpty() ? null : bomb.getMetadata("naphtha_bomb").get(0).asString();

        // AOE damage and armor melt
        for (LivingEntity nearby : world.getEntitiesByClass(LivingEntity.class)) {
            if (nearby.getLocation().distance(loc) <= 6.0) {
                if (throwerId != null && nearby.getUniqueId().toString().equals(throwerId)) continue;
                if (nearby instanceof Player) continue; // No friendly fire on players
                nearby.setFireTicks(200); // 10 seconds of fire
                nearby.damage(16.0);
                nearby.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 160, 1));
                nearby.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 160, 1));
            }
        }
    }

    /**
     * Combat Perk: Separ-e Derafsh-e Kaviani damage reflection.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onShieldReflect(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!player.isBlocking()) return;

        ItemStack shield = player.getInventory().getItemInMainHand();
        if (shield == null || shield.getType() != Material.SHIELD) {
            shield = player.getInventory().getItemInOffHand();
        }
        if (shield == null || !shield.hasItemMeta() || !shield.getItemMeta().hasDisplayName()) return;

        if (shield.getItemMeta().getDisplayName().contains("Derafsh-e Kaviani")) {
            if (event.getDamager() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) event.getDamager();
                double reflected = event.getDamage() * 0.5;
                attacker.damage(Math.max(4.0, reflected));
                attacker.getWorld().spawnParticle(Particle.CRIT_MAGIC, attacker.getLocation().add(0, 1, 0), 15, 0.3, 0.3, 0.3, 0.1);
                attacker.getWorld().playSound(attacker.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.2f, 1.5f);
                player.sendMessage(MessageUtils.color("&6&l[Derafsh-e Kaviani] &eZarbeh-ye doshman ba " + (int)reflected + " damage be khodash baztab shod!"));
            }
        }
    }

    /**
     * Telesm-e Jam-e Jam (Right Click near boss).
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onUseJamshidTalisman(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        if (item.getItemMeta().getDisplayName().contains("Jam-e Jam")) {
            event.setCancelled(true);

            long now = System.currentTimeMillis();
            long last = talismanCooldown.getOrDefault(player.getUniqueId(), 0L);
            if (now - last < 30000) {
                long rem = (30000 - (now - last)) / 1000;
                player.sendMessage(MessageUtils.color("&8[&6Jam-e Jam&8] &7Telesm &e" + rem + " saniyeh &7digar amadeh mishavad."));
                return;
            }

            // Find nearby boss
            LivingEntity targetBoss = null;
            for (LivingEntity e : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
                if (e.hasMetadata("dungeon_boss") && e.getLocation().distance(player.getLocation()) <= 18.0) {
                    targetBoss = e;
                    break;
                }
            }

            if (targetBoss == null) {
                player.sendMessage(MessageUtils.color("&8[&6Jam-e Jam&8] &cHich boss-i dar faseleh-ye 18 metri peyda nashod!"));
                return;
            }

            talismanCooldown.put(player.getUniqueId(), now);

            // Strip boss buffs
            targetBoss.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            targetBoss.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            targetBoss.removePotionEffect(PotionEffectType.SPEED);
            targetBoss.removePotionEffect(PotionEffectType.REGENERATION);

            // Weaken boss
            targetBoss.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));
            targetBoss.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 2));

            World world = player.getWorld();
            world.strikeLightningEffect(targetBoss.getLocation());
            world.spawnParticle(Particle.ENCHANTMENT_TABLE, targetBoss.getLocation().add(0, 2, 0), 80, 1.0, 1.0, 1.0, 0.5);
            world.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.0f, 1.5f);

            player.sendMessage(MessageUtils.color("&6&l[Jam-e Jam] &eMojezeye Jamshid! Hameye buff-haye boss batel shod va noghteh-za'f-ash ashkar gasht!"));
        }
    }

    // ==========================================
    // Boss Item Creators
    // ==========================================

    public static ItemStack createDivKoshSword() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        item.setDurability((short) 4);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&c&l🗡 Tigh-e Div-Kosh (Demon Slayer)"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Tigh-e afsanehi-ye forood-amadeh bar div-ha"));
            lore.add(MessageUtils.color("&c&l+18 True Damage &7be tamam-e Boss-ha"));
            lore.add(MessageUtils.color("&e25% Shans-e Stun &7(Gij kardan-e boss baraye 2s)"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Salah-e Mokhtas-e Boss] &aZendeh bad Iran!"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
            meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
            meta.addEnchant(Enchantment.DURABILITY, 5, true);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createBossPiercerBow() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&a&l🏹 Kaman-e Shekar-e Div (Boss Piercer)"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Kaman-e sangin ba tir-haye nofooz-konandeh"));
            lore.add(MessageUtils.color("&c200% Damage &7dar barabar-e Boss-haye Dungeon"));
            lore.add(MessageUtils.color("&bNofooz-e kamel dar zereh-ye doshman"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Salah-e Mokhtas-e Boss] &eShekarchi-ye Kohan"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.ARROW_DAMAGE, 6, true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createNaphthaBomb(int amount) {
        ItemStack item = new ItemStack(Material.MAGMA_CREAM, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&l💣 Bomb-e Naft-e Siah (Persian Naphtha)"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Atash-e nafti-ye bastani-ye Irani"));
            lore.add(MessageUtils.color("&cRight-Click baraye partab"));
            lore.add(MessageUtils.color("&eInflict 16 AOE Damage & Atash-e 10 saniyehi"));
            lore.add(MessageUtils.color("&4Zob kardan-e defa' va zereh-ye Boss"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createDerafshShield() {
        ItemStack item = new ItemStack(Material.SHIELD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&l🛡 Separ-e Derafsh-e Kaviani (Aegis)"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Separ-e moqaddas ba parcham-e pahlavani"));
            lore.add(MessageUtils.color("&eBaztab-e 50% Damage-e Boss be khodash"));
            lore.add(MessageUtils.color("&aMoghavemat-e kamel dar barabar-e zarabat"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.DURABILITY, 5, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createJamshidTalisman() {
        ItemStack item = new ItemStack(Material.WATCH);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&b&l🔮 Telesm-e Jam-e Jam (Boss Dispeller)"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Jam-e jahan-nama-ye Jamshid Shah"));
            lore.add(MessageUtils.color("&bRight-Click dar faseleh-ye 18 metri-ye Boss"));
            lore.add(MessageUtils.color("&cPak-kardan-e tamam-e buff-haye Boss"));
            lore.add(MessageUtils.color("&eAshkar-kardan-e noghteh-za'f baraye 10s"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}

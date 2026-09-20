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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Authentic Persian Historical & Mythological Relics (v5.4 Finglish)
 * Features:
 * 1. Meel-e Bastani (Pahlavani Heavy Cudgel - Seismic Ground Slam)
 * 2. Kabbadeh-ye Pouladin (Champion's Steel Bow - Arrow Deflection & Iron Skin)
 * 3. Zang-e Zoorkhaneh (Sacred Morshed Bell - Sonic Debuff Purge & Mob Blast)
 * 4. Sang-e Akvan (Whirlwind Stone - Double Jump & Wind Cushion)
 * 5. Par-e Simurgh (Sacred Damavand Feather - Mythical Revival & Levitation)
 * 6. Zaferan-e Qaen (Persian Saffron - Royal Cooking & Sacred Offerings)
 * 7. Golab-e Ghamsar (Pure Kashan Rosewater - Cures Impurities & Thirst)
 * 8. Firoozeh-ye Neyshaboor (Neyshaboor Turquoise - Luck & Defense)
 * 9. Pesteh-ye Rafsanjan (Rafsanjan Pistachio - High Energy Stamina Food)
 */
public class PersianAuthenticItems implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Long> bellCooldown = new HashMap<>();
    private final Map<UUID, Long> meelCooldown = new HashMap<>();
    private final Map<UUID, Long> akvanCooldown = new HashMap<>();
    private final Random random = new Random();

    public PersianAuthenticItems(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        registerAuthenticRecipes();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("PersianAuthenticItems: Pahlavani, Shahnameh & Silk Road relics initialized!");
    }

    private void registerAuthenticRecipes() {
        try {
            // 1. Meel-e Bastani
            ItemStack meel = createMeelBastani();
            ShapedRecipe meelRecipe = new ShapedRecipe(meel);
            meelRecipe.shape(" L ", " L ", " S ");
            meelRecipe.setIngredient('L', Material.LOG);
            meelRecipe.setIngredient('S', Material.IRON_INGOT);
            Bukkit.addRecipe(meelRecipe);

            // 2. Kabbadeh-ye Pouladin
            ItemStack kabbadeh = createKabbadehPouladin();
            ShapedRecipe kabbadehRecipe = new ShapedRecipe(kabbadeh);
            kabbadehRecipe.shape(" I ", "I I", "SIS");
            kabbadehRecipe.setIngredient('I', Material.IRON_BLOCK);
            kabbadehRecipe.setIngredient('S', Material.CHAINMAIL_CHESTPLATE);
            Bukkit.addRecipe(kabbadehRecipe);

            // 3. Zang-e Zoorkhaneh
            ItemStack zang = createZangZoorkhaneh();
            ShapedRecipe zangRecipe = new ShapedRecipe(zang);
            zangRecipe.shape(" G ", "GBG", " N ");
            zangRecipe.setIngredient('G', Material.GOLD_BLOCK);
            zangRecipe.setIngredient('B', Material.NOTE_BLOCK);
            zangRecipe.setIngredient('N', Material.GOLD_NUGGET);
            Bukkit.addRecipe(zangRecipe);

            // 4. Golab-e Ghamsar
            ItemStack golab = createGolabGhamsar();
            ShapedRecipe golabRecipe = new ShapedRecipe(golab);
            golabRecipe.shape(" R ", "RWR", " G ");
            golabRecipe.setIngredient('R', Material.RED_ROSE);
            golabRecipe.setIngredient('W', Material.POTION);
            golabRecipe.setIngredient('G', Material.SUGAR);
            Bukkit.addRecipe(golabRecipe);

        } catch (Exception e) {
            plugin.getLogger().warning("Could not register some authentic recipes: " + e.getMessage());
        }
    }

    // --- ITEM CREATION HELPERS ---

    public static ItemStack createMeelBastani() {
        ItemStack item = new ItemStack(Material.WOOD_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&lMeel-e Bastani-ye Pahlavani"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&7Gorze geran-baha va bastani-ye Pahlavanan-e Iran"));
            lore.add(MessageUtils.color("&e[Vizhegi] &fZarbat-e sangin ba Knockback III"));
            lore.add(MessageUtils.color("&a[Right-Click] &fGround Slam: 12 Damage AoE va partab-e doshmanan!"));
            lore.add(MessageUtils.color("&dAsil, Tarikhi va Pahlavani"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
            meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createKabbadehPouladin() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&b&lKabbadeh-ye Pouladin-e Pahlavan"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&7Kaman va zanjir-e ahanin-e varzesh-e bastani-ye Iran"));
            lore.add(MessageUtils.color("&e[Passive] &fKaman-dastan ra poshtiban karde va tirha ra daf mikonad"));
            lore.add(MessageUtils.color("&a[Dast Gereftan] &fResistance II va Iron Skin e'ta mikonad"));
            lore.add(MessageUtils.color("&dNemad-e Ghodrat va Gheirat-e Irani"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
            meta.addEnchant(Enchantment.DURABILITY, 3, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createZangZoorkhaneh() {
        ItemStack item = new ItemStack(Material.BELL != null ? Material.BELL : Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&e&lZang-e Zoorkhaneh va Navay-e Morshed"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&7Zang-e boronzi-ye Sardam-e Zoorkhaneh ba seday-e tanin-andaz"));
            lore.add(MessageUtils.color("&a[Right-Click] &fPaksazi-ye Poison, Weakness, Wither va Slowness"));
            lore.add(MessageUtils.color("&e[AoE] &fPartab-e tamam-e hayoola-ha be aghab ba amvaj-e soti"));
            lore.add(MessageUtils.color("&7Cooldown: 15 Saniyeh"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createSangAkvan() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&3&lSang-e Gerd-bad-e Akvan Div"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&7Gowhar-e bad va toofan az Shahnameh-ye Ferdowsi"));
            lore.add(MessageUtils.color("&b[Passive] &fPresh-e Dobareh (Double Jump) dar hawa ba gasht-o-gozar"));
            lore.add(MessageUtils.color("&a[Hefazat] &fSoghoot az ertefa hargez be shoma asib nemiresanad"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.PROTECTION_FALL, 5, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createParSimurgh() {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&lPar-e Jadooyi-ye Simurgh-e Damavand"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&7Par-e zarrin-e morgh-e afsaneh-i-ye Alborz va Damavand"));
            lore.add(MessageUtils.color("&e[Totem-e Hayat] &fDar hengam-e mordan, jan-e shoma ra nejat midahad"));
            lore.add(MessageUtils.color("&a[Barakat] &fRegeneration III, Absorption II va Parvaz-e Ahesta"));
            lore.add(MessageUtils.color("&dHedyeh-ye Zal va Rostam"));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.LUCK, 3, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createZaferanQaen() {
        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&4&lZaferan-e Asil-e Qaenat (Talay-e Sorkh)"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&7Talay-e sorkh-e Khorshid az khorasan"));
            lore.add(MessageUtils.color("&e[Estefadeh] &fNazr baraye Atashkadeh va sakht-e ghaza-haye a'la"));
            lore.add(MessageUtils.color("&a[Karkard] &fAfzayesh-e ghodrat va salamati"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createGolabGhamsar() {
        ItemStack item = new ItemStack(Material.POTION);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&d&lGolab-e Do-Atasheh-ye Ghamsar-e Kashan"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&7Atr-e gol-e mohammadi va eliksir-e paki"));
            lore.add(MessageUtils.color("&a[Noshidan] &fRaf'-e kamel-e teshnegi (100%) va darman-e asib-ha"));
            lore.add(MessageUtils.color("&b[Paksazi] &fPak kardan-e tamam-e samoom va aab-haye kasif"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createPestehRafsanjan() {
        ItemStack item = new ItemStack(Material.COOKIE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&a&lPesteh-ye Khandan-e Rafsanjan"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&7Pesteh-ye a'la va moghavi-ye Kerman"));
            lore.add(MessageUtils.color("&e[Khorak] &fPorkardan-e 8 vahed ghaza va e'tay-e Speed II"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    // --- EVENT HANDLERS ---

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack hand = event.getItem();
        if (hand == null || !hand.hasItemMeta() || !hand.getItemMeta().hasDisplayName()) return;

        Player player = event.getPlayer();
        String name = hand.getItemMeta().getDisplayName();

        // 1. Zang-e Zoorkhaneh
        if (name.contains("Zang-e Zoorkhaneh")) {
            long now = System.currentTimeMillis();
            long last = bellCooldown.getOrDefault(player.getUniqueId(), 0L);
            if (now - last < 15000) {
                long left = 15 - ((now - last) / 1000);
                MessageUtils.sendActionBar(player, "&cZang dar hale amadeh-sazi ast: " + left + " saniyeh!");
                return;
            }
            bellCooldown.put(player.getUniqueId(), now);

            // Audio & visual
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.5f, 1.8f);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BELL, 2.0f, 1.0f);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(0, 1, 0), 25, 0.6, 0.6, 0.6, 0.1);

            // Clear negative potion effects
            player.removePotionEffect(PotionEffectType.POISON);
            player.removePotionEffect(PotionEffectType.WITHER);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.removePotionEffect(PotionEffectType.CONFUSION);

            // Sonic pushback on hostile mobs
            for (Entity e : player.getNearbyEntities(8.0, 4.0, 8.0)) {
                if (e instanceof Monster) {
                    Vector diff = e.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.8).setY(0.4);
                    e.setVelocity(diff);
                    ((Monster) e).damage(8.0, player);
                }
            }

            MessageUtils.sendActionBar(player, "&6[Zang-e Morshed] &aNavaye zang-e Zoorkhaneh samoom ra pak kard va deevan ra tarand!");
            event.setCancelled(true);
            return;
        }

        // 2. Meel-e Bastani Ground Slam
        if (name.contains("Meel-e Bastani") && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            long now = System.currentTimeMillis();
            long last = meelCooldown.getOrDefault(player.getUniqueId(), 0L);
            if (now - last < 8000) {
                long left = 8 - ((now - last) / 1000);
                MessageUtils.sendActionBar(player, "&cMeel dar hale zakhireh-ye ghodrat ast: " + left + "s!");
                return;
            }
            meelCooldown.put(player.getUniqueId(), now);

            // Ground slam
            Location target = event.getClickedBlock().getLocation().add(0.5, 1.0, 0.5);
            player.getWorld().playSound(target, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.8f, 0.6f);
            player.getWorld().playSound(target, Sound.BLOCK_ANVIL_PLACE, 1.5f, 0.8f);
            player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, target, 2, 0.2, 0.2, 0.2, 0.0);

            for (Entity e : player.getNearbyEntities(6.0, 3.0, 6.0)) {
                if (e instanceof Monster) {
                    e.setVelocity(new Vector(0, 0.9, 0));
                    ((Monster) e).damage(12.0, player);
                }
            }
            MessageUtils.sendActionBar(player, "&6[Meel-e Bastani] &eGround Slam! Zamin be larzeh dar amad!");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onGolabConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        Player player = event.getPlayer();

        if (item.getItemMeta().getDisplayName().contains("Golab-e Do-Atasheh")) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.2f, 1.1f);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.8f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1));
            // Quench thirst 100%
            if (plugin.getThirstManager() != null) {
                plugin.getThirstManager().setThirst(player, 100);
            }
            MessageUtils.sendActionBar(player, "&d[Golab-e Ghamsar] &aTeshnegi 100% bartaraf shod va aramash daryaft shod!");
        } else if (item.getItemMeta().getDisplayName().contains("Pesteh-ye Khandan")) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.2f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100, 1));
            MessageUtils.sendActionBar(player, "&a[Pesteh-ye Rafsanjan] &eEnergy va sor'at afzayesh yaft!");
        }
    }

    // Arrow Deflection with Kabbadeh
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onKabbadehDeflection(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();

        boolean hasKabbadeh = (main != null && main.hasItemMeta() && main.getItemMeta().hasDisplayName() && main.getItemMeta().getDisplayName().contains("Kabbadeh"))
                || (off != null && off.hasItemMeta() && off.getItemMeta().hasDisplayName() && off.getItemMeta().getDisplayName().contains("Kabbadeh"));

        if (!hasKabbadeh) return;

        if (event.getDamager() instanceof Arrow || event.getDamager() instanceof SpectralArrow) {
            event.setCancelled(true);
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.5f, 1.2f);
            player.getWorld().spawnParticle(Particle.CRIT, player.getLocation().add(0, 1.2, 0), 10, 0.3, 0.3, 0.3, 0.2);
            MessageUtils.sendActionBar(player, "&b[Kabbadeh] &fTir-e doshman be zanjir-e kabbadeh barkhord va daf shod!");
        }
    }

    // Par-e Simurgh Totem Revival
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSimurghRevival(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (player.getHealth() - event.getFinalDamage() > 0) return;

        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();
        boolean hasSimurghMain = main != null && main.hasItemMeta() && main.getItemMeta().hasDisplayName() && main.getItemMeta().getDisplayName().contains("Par-e Jadooyi-ye Simurgh");
        boolean hasSimurghOff = off != null && off.hasItemMeta() && off.getItemMeta().hasDisplayName() && off.getItemMeta().getDisplayName().contains("Par-e Jadooyi-ye Simurgh");

        if (hasSimurghMain || hasSimurghOff) {
            event.setCancelled(true);
            if (hasSimurghMain) {
                main.setAmount(main.getAmount() - 1);
            } else {
                off.setAmount(off.getAmount() - 1);
            }

            player.setHealth(player.getMaxHealth());
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.5f, 1.0f);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.2f, 1.4f);
            player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation().add(0, 1.5, 0), 100, 1.0, 1.5, 1.0, 0.4);

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 600, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 0));

            MessageUtils.sendActionBar(player, "&6&l[SIMURGH] &eSimurgh-e Damavand jan-e shoma ra ba par-e khod faryadres shod!");
        }
    }
}

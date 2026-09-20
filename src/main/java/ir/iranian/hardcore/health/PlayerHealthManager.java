package ir.iranian.hardcore.health;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.items.PersianItems;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Health Upgrades & Extra Hearts System (v5.2 Finglish)
 * Features:
 * - Permanent craftable Heart Containers & Immortality Elixirs
 * - Ghalb-e Boloorin-e Sorkh (+2 HP / +1 Heart)
 * - Ghalb-e Firoozeh (+2 HP / +1 Heart)
 * - Eksir-e Javidan / Aab-e Hayat (+4 HP / +2 Hearts)
 * - Persistent storage in player_health.yml across relogs, deaths & restarts
 * - Up to 60.0 Max Health (30 full hearts)!
 */
public class PlayerHealthManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Double> playerMaxHealth = new HashMap<>();
    private File healthFile;
    private FileConfiguration healthConfig;

    public static final double DEFAULT_HEALTH = 20.0;
    public static final double MAX_HEALTH_CAP = 60.0; // 30 full hearts!

    public PlayerHealthManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        loadHealthData();
        registerHeartRecipes();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        for (Player p : Bukkit.getOnlinePlayers()) {
            applyMaxHealth(p);
        }
        plugin.getLogger().info("PlayerHealthManager: Extra Hearts & Health Upgrades initialized!");
    }

    private void loadHealthData() {
        healthFile = new File(plugin.getDataFolder(), "player_health.yml");
        if (!healthFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                healthFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create player_health.yml: " + e.getMessage());
            }
        }
        healthConfig = YamlConfiguration.loadConfiguration(healthFile);

        for (String key : healthConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                double hp = healthConfig.getDouble(key, DEFAULT_HEALTH);
                playerMaxHealth.put(uuid, Math.min(MAX_HEALTH_CAP, Math.max(DEFAULT_HEALTH, hp)));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public void saveHealthData() {
        if (healthConfig == null || healthFile == null) return;
        for (Map.Entry<UUID, Double> entry : playerMaxHealth.entrySet()) {
            healthConfig.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            healthConfig.save(healthFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save player_health.yml: " + e.getMessage());
        }
    }

    private void registerHeartRecipes() {
        try {
            // 1. Ghalb-e Boloorin-e Sorkh (+2 HP / +1 Heart)
            // Recipe: 4 Gold Ingots + 4 Sekkeh Derik + 1 Golden Apple
            NamespacedKey redHeartKey = new NamespacedKey(plugin, "recipe_ghalb_sorkh");
            ShapedRecipe redRecipe = new ShapedRecipe(redHeartKey, createRedHeartCanister());
            redRecipe.shape("GDG", "DAD", "GDG");
            redRecipe.setIngredient('G', Material.GOLD_INGOT);
            redRecipe.setIngredient('D', Material.GOLD_NUGGET);
            redRecipe.setIngredient('A', Material.GOLDEN_APPLE);
            Bukkit.addRecipe(redRecipe);

            // 2. Ghalb-e Firoozeh-ye Elahi (+2 HP / +1 Heart)
            // Recipe: 4 Emeralds + 4 Lapis Blocks + 1 Diamond
            NamespacedKey blueHeartKey = new NamespacedKey(plugin, "recipe_ghalb_firoozeh");
            ShapedRecipe blueRecipe = new ShapedRecipe(blueHeartKey, createTurquoiseHeartCanister());
            blueRecipe.shape("ELE", "LML", "ELE");
            blueRecipe.setIngredient('E', Material.EMERALD);
            blueRecipe.setIngredient('L', Material.LAPIS_BLOCK);
            blueRecipe.setIngredient('M', Material.DIAMOND_BLOCK);
            Bukkit.addRecipe(blueRecipe);

            // 3. Eksir-e Javidan / Aab-e Hayat (+4 HP / +2 Hearts)
            // Recipe: Milk Bucket + Nether Star + Golden Carrot + 2 Sekkeh Derik
            NamespacedKey elixirKey = new NamespacedKey(plugin, "recipe_eksir_javidan");
            ShapedRecipe elixirRecipe = new ShapedRecipe(elixirKey, createElixirOfLife());
            elixirRecipe.shape(" D ", "GNG", " B ");
            elixirRecipe.setIngredient('D', Material.GOLDEN_CARROT);
            elixirRecipe.setIngredient('G', Material.GOLD_NUGGET);
            elixirRecipe.setIngredient('N', Material.NETHER_STAR);
            elixirRecipe.setIngredient('B', Material.GLASS_BOTTLE);
            Bukkit.addRecipe(elixirRecipe);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not register all heart recipes: " + e.getMessage());
        }
    }

    public void applyMaxHealth(Player player) {
        if (player == null || !player.isOnline()) return;
        double targetHp = playerMaxHealth.getOrDefault(player.getUniqueId(), DEFAULT_HEALTH);
        targetHp = Math.min(MAX_HEALTH_CAP, Math.max(DEFAULT_HEALTH, targetHp));

        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(targetHp);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        applyMaxHealth(p);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        saveHealthData();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            applyMaxHealth(p);
            p.setHealth(playerMaxHealth.getOrDefault(p.getUniqueId(), DEFAULT_HEALTH));
        }, 2L);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        applyMaxHealth(event.getPlayer());
    }

    /**
     * Right-Click consumption of Heart Canisters / Elixirs.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onConsumeHeart(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();

        // Guard against intercepting block interactions
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

        boolean isOffHand = false;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            item = player.getInventory().getItemInOffHand();
            isOffHand = true;
        }
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String name = item.getItemMeta().getDisplayName();
        double hpGain = 0;
        String heartType = "";

        if (name.contains("Ghalb-e Boloorin-e Sorkh")) {
            hpGain = 2.0; // +1 heart
            heartType = "Ghalb-e Sorkh";
        } else if (name.contains("Ghalb-e Firoozeh")) {
            hpGain = 2.0; // +1 heart
            heartType = "Ghalb-e Firoozeh";
        } else if (name.contains("Eksir-e Javidan") || name.contains("Aab-e Hayat")) {
            hpGain = 4.0; // +2 hearts
            heartType = "Eksir-e Javidan";
        }

        if (hpGain > 0) {
            event.setCancelled(true);
            double currentMax = playerMaxHealth.getOrDefault(player.getUniqueId(), DEFAULT_HEALTH);

            if (currentMax >= MAX_HEALTH_CAP) {
                player.sendMessage(MessageUtils.color("&8[&6Iran&8] &cJan-e shoma dar hadd-e aksar ast (" + (int)(MAX_HEALTH_CAP/2) + " Ghalb / " + (int)MAX_HEALTH_CAP + " HP)!"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1.0f, 0.5f);
                return;
            }

            // Deduct 1 item
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                if (isOffHand) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                } else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
            }

            double newMax = Math.min(MAX_HEALTH_CAP, currentMax + hpGain);
            playerMaxHealth.put(player.getUniqueId(), newMax);
            saveHealthData();
            applyMaxHealth(player);

            // Heal player by the amount gained
            player.setHealth(Math.min(newMax, player.getHealth() + hpGain));

            // Effects & Sounds
            World world = player.getWorld();
            world.spawnParticle(Particle.HEART, player.getLocation().add(0, 1.5, 0), 25, 0.6, 0.6, 0.6, 0.1);
            world.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.2f);
            world.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

            int totalHearts = (int) (newMax / 2);
            int addedHearts = (int) (hpGain / 2);

            player.sendTitle(MessageUtils.color("&c&l+" + addedHearts + " GHALB-E JADID!"), 
                    MessageUtils.color("&aMajmoo'-e Jan: &e" + totalHearts + " Ghalb &7(" + (int)newMax + " HP)"), 10, 50, 20);

            player.sendMessage(MessageUtils.color("&8[&6" + heartType + "&8] &aGhalb-e jadid ba movafaghiat be shoma ezafe shod! Jan-e shoma: &e" 
                    + totalHearts + " Ghalb &7(" + (int)newMax + " HP)"));

            if (name.contains("Eksir")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 1));
            }
        }
    }

    public double getPlayerMaxHealth(Player player) {
        return playerMaxHealth.getOrDefault(player.getUniqueId(), DEFAULT_HEALTH);
    }

    public void setPlayerMaxHealth(Player player, double hp) {
        playerMaxHealth.put(player.getUniqueId(), Math.min(MAX_HEALTH_CAP, Math.max(DEFAULT_HEALTH, hp)));
        saveHealthData();
        applyMaxHealth(player);
    }

    // ==========================================
    // Heart Item Creators
    // ==========================================

    public static ItemStack createRedHeartCanister() {
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&c&l❤ Ghalb-e Boloorin-e Sorkh (+1 Ghalb)"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Ghalb-e boloorin-e ghaniafteh"));
            lore.add(MessageUtils.color("&a+1 Ghalb-e Daemi &7(+2 Max HP)"));
            lore.add(MessageUtils.color("&bRight-Click dar dast baraye masraf"));
            lore.add(MessageUtils.color("&7Haddaksar zarfiat: &e30 Ghalb (60 HP)"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Eksir-e Salamati] &aZendeh bad Iran!"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createTurquoiseHeartCanister() {
        ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&b&l💙 Ghalb-e Firoozeh-ye Elahi (+1 Ghalb)"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Ghalb-e tarash-khordeh az Firoozeh Neyshaboor"));
            lore.add(MessageUtils.color("&a+1 Ghalb-e Daemi &7(+2 Max HP)"));
            lore.add(MessageUtils.color("&bRight-Click dar dast baraye masraf"));
            lore.add(MessageUtils.color("&7Haddaksar zarfiat: &e30 Ghalb (60 HP)"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Javaher-e Nafis] &bFiroozeh-ye Asil"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createElixirOfLife() {
        ItemStack item = new ItemStack(Material.DRAGONS_BREATH);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color("&6&l✨ Eksir-e Javidan (Aab-e Hayat) (+2 Ghalb)"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&7Aab-e hayat az cheshmeh-ye afsanehi-ye Khezr"));
            lore.add(MessageUtils.color("&a+2 Ghalb-e Daemi &7(+4 Max HP)"));
            lore.add(MessageUtils.color("&e+ Regeneration & Absorption"));
            lore.add(MessageUtils.color("&bRight-Click dar dast baraye nooshidan"));
            lore.add(MessageUtils.color("&8&m------------------------"));
            lore.add(MessageUtils.color("&6[Afsaneh-ye Kohan] &aJavidanagi-ye Pahlavanan"));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}

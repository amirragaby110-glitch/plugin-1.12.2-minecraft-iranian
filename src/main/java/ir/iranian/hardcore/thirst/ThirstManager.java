package ir.iranian.hardcore.thirst;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Thirst, Hydration, Weather & Hotbar HUD System (v5.2 Finglish)
 * Features:
 * - Real-time continuous Hotbar ActionBar HUD showing Thirst, Temperature, Season, and Weather
 * - Sneak + Right-Click water to drink directly with bare hands (RLCraft style)
 * - Refillable Mashk-e Ab (10 sips) and Ghomghame-ye Fooladi (5 sips)
 * - Boiling water / charcoal filtration
 * - Severe dehydration penalties & death
 */
public class ThirstManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Double> playerThirst = new HashMap<>();
    private final Map<UUID, Long> lastDrinkTime = new HashMap<>();
    private final Map<UUID, Long> lastMessageTime = new HashMap<>();
    private final Random random = new Random();

    public ThirstManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        startThirstDrainTask();
        startHotbarHUDTask();
        registerRecipes();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("ThirstManager: RLCraft thirst & continuous Hotbar HUD initialized!");
    }

    private void registerRecipes() {
        try {
            // Mashk-e Ab: Leather + String + Glass Bottle (or Iron)
            NamespacedKey mashkKey = new NamespacedKey(plugin, "recipe_mashk_ab");
            ShapedRecipe mashkRecipe = new ShapedRecipe(mashkKey, createMashkAb(10));
            mashkRecipe.shape(" L ", "LSL", " B ");
            mashkRecipe.setIngredient('L', Material.LEATHER);
            mashkRecipe.setIngredient('S', Material.STRING);
            mashkRecipe.setIngredient('B', Material.GLASS_BOTTLE);
            Bukkit.addRecipe(mashkRecipe);

            // Ghomghame (Canteen): Iron Ingot + Glass Bottle
            NamespacedKey canteenKey = new NamespacedKey(plugin, "recipe_ghomghame");
            ShapedRecipe canteenRecipe = new ShapedRecipe(canteenKey, createCanteen(5));
            canteenRecipe.shape(" I ", "IBI", " I ");
            canteenRecipe.setIngredient('I', Material.IRON_INGOT);
            canteenRecipe.setIngredient('B', Material.GLASS_BOTTLE);
            Bukkit.addRecipe(canteenRecipe);

            // Furnace smelting: Dirty Water -> Clean Water
            FurnaceRecipe smeltRecipe = new FurnaceRecipe(createCleanWater(), Material.POTION);
            Bukkit.addRecipe(smeltRecipe);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not register thirst recipes: " + e.getMessage());
        }
    }

    /**
     * Drains player thirst based on activity, climate, and sprinting every 4 seconds.
     */
    private void startThirstDrainTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().getBoolean("thirst.enabled", true)) return;
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) continue;
                    updateThirst(player);
                }
            }
        }.runTaskTimer(plugin, 60L, 80L);
    }

    /**
     * Continuously refreshes the Action Bar HUD directly above the hotbar every 20 ticks (1 second).
     * Displays Thirst + Temperature + Season + Weather without flickering.
     */
    private void startHotbarHUDTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.SPECTATOR) continue;
                    sendHotbarHUD(player);
                }
            }
        }.runTaskTimer(plugin, 20L, 20L); // Exactly 1 second
    }

    private void updateThirst(Player player) {
        UUID uuid = player.getUniqueId();
        double thirst = playerThirst.getOrDefault(uuid, 100.0);

        // Base drain
        double drain = 0.4;

        if (player.isSprinting()) {
            drain += 0.5;
        }

        // Temperature effect
        if (plugin.getTemperatureManager() != null) {
            double temp = plugin.getTemperatureManager().getTemperature(player);
            if (temp > 25) {
                drain += (temp - 25) / 28.0;
            }
        }

        if (player.getFireTicks() > 0) {
            drain += 1.5;
        }

        if (plugin.getConfigManager().getBoolean("hardcore.enabled", true)) {
            drain *= 1.3;
        }

        thirst -= drain;
        thirst = Math.max(0, Math.min(100, thirst));
        playerThirst.put(uuid, thirst);

        applyThirstEffects(player, thirst);
    }

    private void applyThirstEffects(Player player, double thirst) {
        if (thirst <= 2) {
            player.damage(2.0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            sendThirstMessage(player, "critical");
        } else if (thirst < 20) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1));
            if (random.nextDouble() < 0.25) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 0));
                sendThirstMessage(player, "low");
            }
        } else if (thirst < 35) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
            if (random.nextDouble() < 0.15) {
                sendThirstMessage(player, "dry");
            }
        }
    }

    /**
     * Builds and transmits the unified hotbar HUD:
     * e.g. "💧 85% [||||||||  ]  |  🌡 +24°C (Motadel)  |  ☀ Bahar: Aftabi"
     */
    public void sendHotbarHUD(Player player) {
        if (player == null || !player.isOnline()) return;

        double thirst = getThirst(player);

        // 1. Thirst Section
        String thirstColor;
        if (thirst >= 70) thirstColor = "&b";
        else if (thirst >= 40) thirstColor = "&e";
        else if (thirst >= 20) thirstColor = "&6";
        else thirstColor = "&c&l";

        int filledBars = (int) Math.round(thirst / 10.0);
        StringBuilder barSb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (i < filledBars) barSb.append("|");
            else barSb.append(" ");
        }

        String thirstPart = thirstColor + "💧 " + String.format("%.0f", thirst) + "% &8[" + thirstColor + barSb.toString() + "&8]";

        // 2. Temperature Section
        String tempPart = "";
        if (plugin.getTemperatureManager() != null) {
            double temp = plugin.getTemperatureManager().getTemperature(player);
            String tempColor = "&a";
            String status = "Motadel";
            if (temp > 40) {
                tempColor = "&4&l";
                status = "Garmazadegi";
            } else if (temp > 25) {
                tempColor = "&6";
                status = "Garm";
            } else if (temp < -20) {
                tempColor = "&9&l";
                status = "Yakh-zadan";
            } else if (temp < 0) {
                tempColor = "&b";
                status = "Sard";
            }
            tempPart = " &8| " + tempColor + "🌡 " + (temp >= 0 ? "+" : "") + String.format("%.0f", temp) + "°C &7(" + status + ")";
        }

        // 3. Climate, Season & Weather Section
        String weatherPart = "";
        if (plugin.getTemperatureManager() != null) {
            World world = player.getWorld();
            String season = plugin.getTemperatureManager().getSeasonName(world).split(" ")[0]; // "Bahar", "Tabestan", etc.
            Biome biome = player.getLocation().getBlock().getBiome();
            String weatherDesc;

            if (world.isThundering()) {
                weatherDesc = "&e⚡ Toofani";
            } else if (world.hasStorm()) {
                if (plugin.getTemperatureManager().isColdBiome(biome)) {
                    weatherDesc = "&b🌨 Koolak";
                } else if (plugin.getTemperatureManager().isHotBiome(biome)) {
                    weatherDesc = "&6🌪 Toofan Shen";
                } else {
                    weatherDesc = "&9🌧 Barani";
                }
            } else {
                long time = world.getTime();
                boolean isNight = time > 13000 && time < 23000;
                weatherDesc = isNight ? "&7🌙 Mahtabi" : "&6☀ Aftabi";
            }

            weatherPart = " &8| " + weatherDesc + " &7(&f" + season + "&7)";
        }

        String fullMessage = thirstPart + tempPart + weatherPart;
        MessageUtils.sendActionBar(player, fullMessage);
    }

    private void sendThirstMessage(Player player, String type) {
        long now = System.currentTimeMillis();
        long last = lastMessageTime.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < 15000) return;
        lastMessageTime.put(player.getUniqueId(), now);

        if ("critical".equals(type)) {
            player.sendMessage(MessageUtils.color("&4&l[Khatar-e Marg] &cShoma az teshnegi dar hale halak shodan hastid! Sarian ab benooshid!"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 0.7f);
        } else if ("low".equals(type)) {
            player.sendMessage(MessageUtils.color("&c&l[Teshnegi] &eTeshnegi-ye shadid darid! Badanetoon za'eef shode ast."));
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 0.8f, 0.5f);
        } else if ("dry".equals(type)) {
            player.sendMessage(MessageUtils.color("&e[Teshnegi] &7Geloo-ye shoma khoshk shode ast. Ab benooshid."));
        }
    }

    private boolean isWaterBlock(Block b) {
        if (b == null) return false;
        Material m = b.getType();
        return m == Material.WATER || m == Material.STATIONARY_WATER;
    }

    /**
     * Sneak + Right click water block to drink directly or fill bottle (RLCraft style).
     * Handles both RIGHT_CLICK_BLOCK and RIGHT_CLICK_AIR (liquids often trigger air click with empty hands).
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDrinkSource(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        ItemStack inHand = player.getInventory().getItemInMainHand();
        boolean hasBottle = (inHand != null && inHand.getType() == Material.GLASS_BOTTLE);
        boolean hasEmptyHand = (inHand == null || inHand.getType() == Material.AIR);
        if (!hasBottle && !hasEmptyHand) return;

        // Detect target water block
        Block target = null;
        Block clicked = event.getClickedBlock();
        if (clicked != null) {
            if (isWaterBlock(clicked) || clicked.getType() == Material.CAULDRON) {
                target = clicked;
            } else if (isWaterBlock(clicked.getRelative(event.getBlockFace()))) {
                target = clicked.getRelative(event.getBlockFace());
            }
        }

        if (target == null) {
            try {
                List<Block> line = player.getLineOfSight((Set<Material>) null, 4);
                for (Block b : line) {
                    if (isWaterBlock(b) || b.getType() == Material.CAULDRON) {
                        target = b;
                        break;
                    }
                }
            } catch (Throwable ignored) {}
        }

        if (target == null && isWaterBlock(player.getLocation().getBlock())) {
            target = player.getLocation().getBlock();
        }

        if (target == null) return;
        event.setCancelled(true);
        boolean isCauldron = (target.getType() == Material.CAULDRON);

        // Fill empty glass bottle
        if (hasBottle) {
            if (inHand.getAmount() > 1) {
                inHand.setAmount(inHand.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }

            if (isCauldron) {
                player.getInventory().addItem(createCleanWater());
                player.sendMessage(MessageUtils.color("&8[&6Ab&8] &aShisheh-ye ab-e paak az dig por shod."));
            } else {
                player.getInventory().addItem(createDirtyWater());
                player.sendMessage(MessageUtils.color("&8[&6Ab&8] &eShisheh-ye ab-e kasif por shod. Ghabl az nooshidan an ra bejooshanid!"));
            }
            player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL, 1.0f, 1.0f);
            return;
        }

        // Bare hand drinking
        if (hasEmptyHand) {
            if (getThirst(player) >= 100.0) {
                player.sendMessage(MessageUtils.color("&8[&6Ab&8] &7Shoma teshne nistid! (Ab: 100%)"));
                return;
            }

            if (isCauldron) {
                drinkWater(player, 35.0);
                player.sendMessage(MessageUtils.color("&8[&6Ab&8] &aAb-e paak va khonak az dig nooshidid (+35% Ab)."));
            } else {
                drinkWater(player, 20.0);
                if (random.nextDouble() < 0.35) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 140, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 0));
                    player.sendMessage(MessageUtils.color("&c[Ab-e Roodkhaneh] &4Ab-e tasfieh-nashodeh nooshidid! Shekam-dard gereftid!"));
                    player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 0.8f, 0.6f);
                } else {
                    player.sendMessage(MessageUtils.color("&e[Ab-e Roodkhaneh] &7Ab-e roodkhaneh nooshidid (+20% Ab)."));
                }
            }
            sendHotbarHUD(player);
        }
    }

    /**
     * Refill or drink from Persian Mashk-e Ab or Steel Canteen.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onMashkInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String name = item.getItemMeta().getDisplayName();
        boolean isMashk = name.contains("Mashk");
        boolean isCanteen = name.contains("Ghomghame") || name.contains("Canteen");
        if (!isMashk && !isCanteen) return;

        event.setCancelled(true);

        // Check if pointing at water or cauldron to refill
        boolean refillTarget = false;
        Block clicked = event.getClickedBlock();
        if (clicked != null && (isWaterBlock(clicked) || clicked.getType() == Material.CAULDRON)) {
            refillTarget = true;
        } else if (clicked != null && isWaterBlock(clicked.getRelative(event.getBlockFace()))) {
            refillTarget = true;
        } else {
            try {
                List<Block> line = player.getLineOfSight((Set<Material>) null, 4);
                for (Block b : line) {
                    if (isWaterBlock(b) || b.getType() == Material.CAULDRON) {
                        refillTarget = true;
                        break;
                    }
                }
            } catch (Throwable ignored) {}
            if (!refillTarget && isWaterBlock(player.getLocation().getBlock())) {
                refillTarget = true;
            }
        }

        if (refillTarget) {
            if (isMashk) {
                player.getInventory().setItemInMainHand(createMashkAb(10));
                player.sendMessage(MessageUtils.color("&8[&6Mashk Ab&8] &aMashk-e shoma az ab por shod! (10/10 nooshesh)"));
            } else {
                player.getInventory().setItemInMainHand(createCanteen(5));
                player.sendMessage(MessageUtils.color("&8[&6Ghomghame&8] &aGhomghame-ye shoma por shod! (5/5 nooshesh)"));
            }
            player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL, 1.0f, 1.0f);
            return;
        }

        // Drink from Mashk or Canteen
        int remaining = getRemainingSips(item);
        if (remaining <= 0) {
            player.sendMessage(MessageUtils.color("&8[&6Mashk&8] &cIn zarf khali ast! Kenar-e ab beravid va Right-Click konid ta por shavad."));
            player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_EMPTY, 0.8f, 1.5f);
            return;
        }

        if (getThirst(player) >= 100.0) {
            player.sendMessage(MessageUtils.color("&8[&6Ab&8] &7Shoma teshne nistid! (Ab: 100%)"));
            return;
        }

        double thirstGain = isMashk ? 30.0 : 25.0;
        drinkWater(player, thirstGain);
        remaining--;

        if (isMashk) {
            player.getInventory().setItemInMainHand(createMashkAb(remaining));
        } else {
            player.getInventory().setItemInMainHand(createCanteen(remaining));
        }

        player.sendMessage(MessageUtils.color("&8[&6Nooshidan&8] &aYek gholop ab nooshidid (+ " + (int)thirstGain + "%). Baghymandeh: &e" + remaining + " nooshesh"));
        sendHotbarHUD(player);
    }

    /**
     * Drinking bottles of clean or dirty water.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.POTION) return;
        Player player = event.getPlayer();

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String name = item.getItemMeta().getDisplayName();
            if (name.contains("Ab-e Kasif")) {
                drinkWater(player, 15.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 140, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 0));
                player.sendMessage(MessageUtils.color("&c[Ab-e Kasif] &4Ab-e kasif nooshidid va bimari gereftid!"));
                sendHotbarHUD(player);
                return;
            } else if (name.contains("Ab-e Paak")) {
                drinkWater(player, 40.0);
                player.sendMessage(MessageUtils.color("&a[Ab-e Paak] &bAb-e paak va govarna nooshidid (+40% Ab)!"));
                sendHotbarHUD(player);
                return;
            }
        }

        // Vanilla water bottle drinking
        drinkWater(player, 25.0);
        sendHotbarHUD(player);
    }

    // Furnace or crafting purification (Dirty water + Charcoal -> Clean Water)
    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareCraftFilter(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        if (matrix == null || matrix.length < 4) return;

        boolean hasDirtyWater = false;
        boolean hasCharcoal = false;
        int count = 0;

        for (ItemStack it : matrix) {
            if (it == null || it.getType() == Material.AIR) continue;
            count++;
            if (it.getType() == Material.POTION && it.hasItemMeta() && it.getItemMeta().hasDisplayName()
                    && it.getItemMeta().getDisplayName().contains("Kasif")) {
                hasDirtyWater = true;
            } else if (it.getType() == Material.COAL) {
                hasCharcoal = true;
            }
        }

        if (count == 2 && hasDirtyWater && hasCharcoal) {
            inv.setResult(createCleanWater());
        }
    }

    public boolean drinkWater(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        double thirst = playerThirst.getOrDefault(uuid, 100.0);
        if (thirst >= 100.0) return false;

        thirst += amount;
        thirst = Math.max(0, Math.min(100.0, thirst));
        playerThirst.put(uuid, thirst);
        lastDrinkTime.put(uuid, System.currentTimeMillis());

        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 1.1f);
        sendHotbarHUD(player);
        return true;
    }

    private int getRemainingSips(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0;
        for (String line : item.getItemMeta().getLore()) {
            if (line.contains("Baghymandeh:")) {
                try {
                    String sub = line.substring(line.indexOf("Baghymandeh:") + 12).trim();
                    String num = sub.split("/")[0].replaceAll("[^0-9]", "");
                    return Integer.parseInt(num);
                } catch (Exception ignored) {}
            }
        }
        return 0;
    }

    public double getThirst(Player player) {
        return playerThirst.getOrDefault(player.getUniqueId(), 100.0);
    }

    public double getThirst(UUID uuid) {
        return playerThirst.getOrDefault(uuid, 100.0);
    }

    public void setThirst(Player player, double thirst) {
        playerThirst.put(player.getUniqueId(), Math.max(0, Math.min(100, thirst)));
        sendHotbarHUD(player);
    }

    public void setThirst(UUID uuid, double thirst) {
        playerThirst.put(uuid, Math.max(0, Math.min(100, thirst)));
    }

    public void addThirst(Player player, double amount) {
        double thirst = getThirst(player);
        thirst += amount;
        setThirst(player, thirst);
    }

    public void resetThirst(Player player) {
        playerThirst.put(player.getUniqueId(), 100.0);
        sendHotbarHUD(player);
    }

    public ItemStack createMashkAb(int sips) {
        ItemStack mashk = new ItemStack(Material.POTION);
        ItemMeta meta = mashk.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&b&l💧 Mashk-e Ab-e Sonnati"));
        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.color("&8&m------------------------"));
        lore.add(MessageUtils.color("&7Mashk-e poosti-ye asil-e Irani"));
        lore.add(MessageUtils.color("&7Baghymandeh: &e" + sips + "/10 &7nooshesh"));
        lore.add(MessageUtils.color("&bRight-Click dar dast baraye nooshidan"));
        lore.add(MessageUtils.color("&7Right-Click rooye ab baraye por-kardan"));
        lore.add(MessageUtils.color("&8&m------------------------"));
        lore.add(MessageUtils.color("&6[Kala-ye Irani] &aZendeh bad Iran!"));
        meta.setLore(lore);
        mashk.setItemMeta(meta);
        return mashk;
    }

    public ItemStack createMashkAb() {
        return createMashkAb(10);
    }

    public ItemStack createCanteen(int sips) {
        ItemStack canteen = new ItemStack(Material.POTION);
        ItemMeta meta = canteen.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&b&l💧 Ghomghame-ye Fooladi"));
        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.color("&8&m------------------------"));
        lore.add(MessageUtils.color("&7Ghomghame-ye mozkam baraye safar-haye toolani"));
        lore.add(MessageUtils.color("&7Baghymandeh: &e" + sips + "/5 &7nooshesh"));
        lore.add(MessageUtils.color("&bRight-Click baraye nooshidan"));
        lore.add(MessageUtils.color("&7Right-Click rooye ab baraye por-kardan"));
        lore.add(MessageUtils.color("&8&m------------------------"));
        meta.setLore(lore);
        canteen.setItemMeta(meta);
        return canteen;
    }

    public ItemStack createDirtyWater() {
        ItemStack bottle = new ItemStack(Material.POTION);
        ItemMeta meta = bottle.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&c&l💧 Ab-e Kasif (Dirty Water)"));
        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.color("&8&m------------------------"));
        lore.add(MessageUtils.color("&7Ab-e aloodheh-ye roodkhaneh va mordab"));
        lore.add(MessageUtils.color("&cKhatar-e bimari va enghel (Parasites)!"));
        lore.add(MessageUtils.color("&eDar kooreh bejooshanid ya ba zoghal saf konid."));
        lore.add(MessageUtils.color("&8&m------------------------"));
        meta.setLore(lore);
        bottle.setItemMeta(meta);
        return bottle;
    }

    public ItemStack createCleanWater() {
        ItemStack bottle = new ItemStack(Material.POTION);
        ItemMeta meta = bottle.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&b&l💧 Ab-e Paak va Jooshandeh"));
        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.color("&8&m------------------------"));
        lore.add(MessageUtils.color("&7Ab-e tasfieh-shodeh va govara"));
        lore.add(MessageUtils.color("&a+35% Teshnegi &7| &aDaf'-e teshnegi"));
        lore.add(MessageUtils.color("&8&m------------------------"));
        meta.setLore(lore);
        bottle.setItemMeta(meta);
        return bottle;
    }

    public ItemStack createWaterBottle() {
        return createCleanWater();
    }

    public ItemStack createCleanWaterBottle() {
        return createCleanWater();
    }

    public ItemStack createDirtyWaterBottle() {
        return createDirtyWater();
    }
}

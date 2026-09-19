package ir.iranian.hardcore.thirst;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
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
 * RLCraft Style Thirst & Hydration System (v5.0 Finglish)
 * Features:
 * - Water Unit / Meter (0 - 100%)
 * - Dirty Water vs Purified Boiled Water
 * - Sneak + Right-Click on water to drink directly
 * - Traditional Iranian Mashk-e Ab (10 sips, refillable)
 * - Ghomghame-ye Fooladi (Canteen - 5 sips)
 * - Severe dehydration penalties & death
 * - Action Bar HUD
 */
public class ThirstManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Double> playerThirst = new HashMap<>();
    private final Map<UUID, Long> lastDrinkTime = new HashMap<>();
    private final Map<UUID, Long> lastMessageTime = new HashMap<>();
    private final Random random = new Random();

    public ThirstManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        startThirstTask();
        registerRecipes();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("ThirstManager: RLCraft-style thirst system initialized!");
    }

    private void registerRecipes() {
        try {
            // Mashk-e Ab: Leather + String + Iron
            NamespacedKey mashkKey = new NamespacedKey(plugin, "recipe_mashk_ab");
            ShapedRecipe mashkRecipe = new ShapedRecipe(mashkKey, createMashkAb(10));
            mashkRecipe.shape(" L ", "LSL", " I ");
            mashkRecipe.setIngredient('L', Material.LEATHER);
            mashkRecipe.setIngredient('S', Material.STRING);
            mashkRecipe.setIngredient('I', Material.IRON_INGOT);
            Bukkit.addRecipe(mashkRecipe);

            // Ghomghame (Canteen): Glass Bottle + Iron
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
            plugin.getLogger().warning("Could not register all thirst recipes: " + e.getMessage());
        }
    }

    private void startThirstTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().getBoolean("thirst.enabled", true)) return;
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) continue;
                    updateThirst(player);
                }
            }
        }.runTaskTimer(plugin, 60L, 80L); // Every 4 seconds
    }

    private void updateThirst(Player player) {
        UUID uuid = player.getUniqueId();
        double thirst = playerThirst.getOrDefault(uuid, 100.0);

        // Base drain
        double drain = 0.4;

        // Sprinting adds drain
        if (player.isSprinting()) {
            drain += 0.5;
        }

        // Temperature effect on thirst
        if (plugin.getTemperatureManager() != null) {
            double temp = plugin.getTemperatureManager().getTemperature(player);
            if (temp > 25) {
                drain += (temp - 25) / 30.0; // Rapid thirst loss in hot biomes
            }
        }

        // If player is on fire or in lava
        if (player.getFireTicks() > 0) {
            drain += 1.5;
        }

        // Hardcore multiplier
        if (plugin.getConfigManager().getBoolean("hardcore.enabled", true)) {
            drain *= 1.4;
        }

        thirst -= drain;
        thirst = Math.max(0, Math.min(100, thirst));
        playerThirst.put(uuid, thirst);

        applyThirstEffects(player, thirst);
    }

    private void applyThirstEffects(Player player, double thirst) {
        // Severe RLCraft style dehydration
        if (thirst <= 2) {
            // Heart damage
            player.damage(2.0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 120, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            sendThirstMessage(player, "critical");
        } else if (thirst < 20) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0));
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

        updateActionBar(player, thirst);
    }

    private void updateActionBar(Player player, double thirst) {
        String color = "&a";
        if (thirst < 20) color = "&c&l";
        else if (thirst < 40) color = "&e";
        else if (thirst < 70) color = "&b";

        int bars = (int) (thirst / 10);
        StringBuilder thirstBar = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (i < bars) thirstBar.append("💧");
            else thirstBar.append("○");
        }

        String message = color + "Ab: " + String.format("%.0f", thirst) + "% " + thirstBar;

        if (plugin.getTemperatureManager() != null) {
            double temp = plugin.getTemperatureManager().getTemperature(player);
            String tempColor = "&a";
            if (temp < -20) tempColor = "&9❄";
            else if (temp < 0) tempColor = "&b❄";
            else if (temp > 40) tempColor = "&4🔥";
            else if (temp > 25) tempColor = "&c🔥";

            message += "  " + tempColor + " " + String.format("%.0f", temp) + "C";
        }

        sendActionBar(player, message);
    }

    private void sendActionBar(Player player, String message) {
        try {
            String colored = MessageUtils.color(message);
            Class<?> chatMessageTypeClass = Class.forName("net.md_5.bungee.api.ChatMessageType");
            Class<?> textComponentClass = Class.forName("net.md_5.bungee.api.chat.TextComponent");
            Class<?> baseComponentClass = Class.forName("net.md_5.bungee.api.chat.BaseComponent");
            Object chatMessageType = chatMessageTypeClass.getField("ACTION_BAR").get(null);
            Object textComponent = textComponentClass.getConstructor(String.class).newInstance(colored);
            Object spigot = player.getClass().getMethod("spigot").invoke(player);
            spigot.getClass().getMethod("sendMessage", chatMessageTypeClass, baseComponentClass)
                    .invoke(spigot, chatMessageType, textComponent);
        } catch (Exception ignored) {}
    }

    private void sendThirstMessage(Player player, String type) {
        long now = System.currentTimeMillis();
        long last = lastMessageTime.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < 15000) return; // 15s cooldown
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

    // Sneak + Right click water block to drink directly (RLCraft style)
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDrinkSource(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        ItemStack inHand = player.getInventory().getItemInMainHand();
        if (inHand != null && inHand.getType() != Material.AIR && inHand.getType() != Material.GLASS_BOTTLE) return;

        Block clicked = event.getClickedBlock();
        if (clicked == null) return;
        Block target = clicked.getRelative(event.getBlockFace());

        if (clicked.getType() == Material.WATER || clicked.getType() == Material.STATIONARY_WATER 
                || target.getType() == Material.WATER || target.getType() == Material.STATIONARY_WATER
                || clicked.getType() == Material.CAULDRON) {

            event.setCancelled(true);
            boolean isCauldron = (clicked.getType() == Material.CAULDRON);

            if (inHand != null && inHand.getType() == Material.GLASS_BOTTLE) {
                // Fill bottle
                inHand.setAmount(inHand.getAmount() - 1);
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

            // Bare hands drinking
            if (isCauldron) {
                drinkWater(player, 35.0);
                player.sendMessage(MessageUtils.color("&8[&6Ab&8] &aAb-e paak va khonak nooshidid (+35% Ab)."));
            } else {
                // Dirty water direct from lake/river
                drinkWater(player, 18.0);
                if (random.nextDouble() < 0.60) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 160, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                    player.sendMessage(MessageUtils.color("&c[Oongholaghoor] &4Ab-e kasif nooshidid! Shekametoon dard gereft va enghel (parasite) vared-e badan shod!"));
                    player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 0.8f, 0.6f);
                } else {
                    player.sendMessage(MessageUtils.color("&e[Ab-e Roodkhaneh] &7Ab-e sathi nooshidid (+18% Ab)."));
                }
            }
        }
    }

    // Refill or drink Mashk Ab
    @EventHandler(priority = EventPriority.HIGH)
    public void onMashkInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String name = item.getItemMeta().getDisplayName();
        if (!name.contains("Mashk Ab") && !name.contains("Ghomghame")) return;

        Block clicked = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clicked != null 
                && (clicked.getType() == Material.WATER || clicked.getType() == Material.STATIONARY_WATER || clicked.getType() == Material.CAULDRON)) {
            // Refill Mashk or Canteen
            event.setCancelled(true);
            if (name.contains("Mashk Ab")) {
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
        event.setCancelled(true);
        int remaining = getRemainingSips(item);
        if (remaining <= 0) {
            player.sendMessage(MessageUtils.color("&8[&6Mashk&8] &cIn zarf khali ast! Kenar-e ab beravid va Right-Click konid ta por shavad."));
            player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_EMPTY, 0.8f, 1.5f);
            return;
        }

        double thirstGain = name.contains("Mashk") ? 30.0 : 25.0;
        drinkWater(player, thirstGain);
        remaining--;

        if (name.contains("Mashk Ab")) {
            player.getInventory().setItemInMainHand(createMashkAb(remaining));
        } else {
            player.getInventory().setItemInMainHand(createCanteen(remaining));
        }

        player.sendMessage(MessageUtils.color("&8[&6Nooshidan&8] &aYek gholop ab nooshidid. Baghymandeh: &e" + remaining + " nooshesh"));
    }

    // Furnace or crafting purification
    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareCraftFilter(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        if (matrix == null) return;

        boolean hasDirtyWater = false;
        boolean hasCharcoal = false;
        int count = 0;

        for (ItemStack it : matrix) {
            if (it == null || it.getType() == Material.AIR) continue;
            count++;
            if (it.getType() == Material.POTION && it.hasItemMeta() && it.getItemMeta().hasDisplayName()
                    && it.getItemMeta().getDisplayName().contains("Ab-e Kasif")) {
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
        if (thirst >= 100) {
            player.sendMessage(MessageUtils.color("&8[&6Ab&8] &7Shoma teshne nistid!"));
            return false;
        }

        thirst += amount;
        thirst = Math.max(0, Math.min(100, thirst));
        playerThirst.put(uuid, thirst);
        lastDrinkTime.put(uuid, System.currentTimeMillis());

        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 1.1f);
        updateActionBar(player, thirst);
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
    }

    public void setThirst(UUID uuid, double thirst) {
        playerThirst.put(uuid, Math.max(0, Math.min(100, thirst)));
    }

    public void addThirst(Player player, double amount) {
        double thirst = getThirst(player);
        thirst += amount;
        setThirst(player, thirst);
        updateActionBar(player, getThirst(player));
    }

    public void resetThirst(Player player) {
        playerThirst.put(player.getUniqueId(), 100.0);
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

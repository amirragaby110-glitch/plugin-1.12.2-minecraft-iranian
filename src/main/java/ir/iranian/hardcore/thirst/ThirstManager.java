package ir.iranian.hardcore.thirst;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.language.LanguageManager;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Thirst System - v4.0
 * Player must drink water, otherwise gets debuffs and dies
 * - Thirst 0-100, 100 = full, 0 = dehydrated
 * - Decreases over time, faster when sprinting, jumping, in hot biomes
 * - Drink water bottle, or use Mashk (water skin) item
 */
public class ThirstManager {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Double> playerThirst = new HashMap<>();
    private final Map<UUID, Long> lastDrinkTime = new HashMap<>();
    private final Map<UUID, Long> lastMessageTime = new HashMap<>();

    public ThirstManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        startThirstTask();
    }

    private void startThirstTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().getBoolean("thirst.enabled", true)) return;
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.getGameMode().name().equals("CREATIVE") || player.getGameMode().name().equals("SPECTATOR")) continue;
                    updateThirst(player);
                }
            }
        }.runTaskTimer(plugin, 100L, 80L); // Every 4 seconds
    }

    private void updateThirst(Player player) {
        UUID uuid = player.getUniqueId();
        double thirst = playerThirst.getOrDefault(uuid, 100.0);

        // Base drain
        double drain = 0.3;

        // Sprinting drains more
        if (player.isSprinting()) {
            drain += 0.4;
        }

        // Hot biome drains more (handled by TemperatureManager too, but we add extra)
        if (plugin.getTemperatureManager() != null) {
            double temp = plugin.getTemperatureManager().getTemperature(player);
            if (temp > 30) {
                drain += temp / 100.0; // Up to +1 in extreme heat
            }
        }

        // Hardcore: faster drain
        if (plugin.getConfigManager().getBoolean("hardcore.enabled", true)) {
            drain *= 1.5;
        }

        thirst -= drain;
        thirst = Math.max(0, Math.min(100, thirst));
        playerThirst.put(uuid, thirst);

        applyThirstEffects(player, thirst);
    }

    private void applyThirstEffects(Player player, double thirst) {
        if (thirst <= 0) {
            // Dehydrated - damage
            player.damage(1.5);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
            sendThirstMessage(player, "critical");
        } else if (thirst < 20) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 0));
            sendThirstMessage(player, "low");
        } else if (thirst < 40) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
            if (Math.random() < 0.2) {
                sendThirstMessage(player, "low");
            }
        }

        // Update action bar with thirst
        updateActionBar(player, thirst);
    }

    private void updateActionBar(Player player, double thirst) {
        String bar = "";
        String color = "&a";
        if (thirst < 20) color = "&c";
        else if (thirst < 40) color = "&e";
        else if (thirst < 70) color = "&6";

        int bars = (int) (thirst / 10);
        StringBuilder thirstBar = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (i < bars) thirstBar.append("💧");
            else thirstBar.append("○");
        }

        bar = color + "Teshnegi: " + String.format("%.0f", thirst) + "% " + thirstBar.toString();

        // Also show temperature if available
        if (plugin.getTemperatureManager() != null) {
            double temp = plugin.getTemperatureManager().getTemperature(player);
            String tempColor = "&a";
            if (temp < -30) tempColor = "&b";
            else if (temp > 30) tempColor = "&c";
            bar += " " + tempColor + "Dama: " + String.format("%.0f", temp);
        }

        sendActionBar(player, bar);
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
        } catch (Exception e) {
            // Fallback
        }
    }

    private void sendThirstMessage(Player player, String type) {
        long now = System.currentTimeMillis();
        long last = lastMessageTime.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < 20000) return; // 20 sec cooldown
        lastMessageTime.put(player.getUniqueId(), now);

        LanguageManager lang = plugin.getLanguageManager();
        String msg = "";
        switch (type) {
            case "low":
                msg = lang.getText(LanguageManager.Translations.THIRST_LOW_FA, LanguageManager.Translations.THIRST_LOW_FINGLISH);
                break;
            case "critical":
                msg = lang.getText(LanguageManager.Translations.THIRST_CRITICAL_FA, LanguageManager.Translations.THIRST_CRITICAL_FINGLISH);
                break;
        }
        if (!msg.isEmpty()) {
            player.sendMessage(MessageUtils.withPrefix(msg));
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 0.5f);
        }
    }

    public boolean drinkWater(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        double thirst = playerThirst.getOrDefault(uuid, 100.0);
        if (thirst >= 100) {
            LanguageManager lang = plugin.getLanguageManager();
            String msg = lang.getText("&7شما تشنه نیستید!", "&7Shoma teshne nistid!");
            player.sendMessage(MessageUtils.withPrefix(msg));
            return false;
        }

        thirst += amount;
        thirst = Math.max(0, Math.min(100, thirst));
        playerThirst.put(uuid, thirst);
        lastDrinkTime.put(uuid, System.currentTimeMillis());

        LanguageManager lang = plugin.getLanguageManager();
        String msg = lang.getText(LanguageManager.Translations.DRINK_WATER_FA, LanguageManager.Translations.DRINK_WATER_FINGLISH);
        player.sendMessage(MessageUtils.withPrefix(msg + " &7(+" + (int)amount + "%)"));
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 1.2f);

        // Also cool down temperature a bit
        if (plugin.getTemperatureManager() != null) {
            double temp = plugin.getTemperatureManager().getTemperature(player);
            if (temp > 20) {
                plugin.getTemperatureManager().setTemperature(player, temp - 5);
            }
        }

        updateActionBar(player, thirst);
        return true;
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
    }

    public void resetThirst(Player player) {
        playerThirst.put(player.getUniqueId(), 100.0);
    }

    /**
     * Create Mashk Ab (Water Skin) - Iranian traditional water container
     */
    public ItemStack createMashkAb() {
        ItemStack mashk = new ItemStack(Material.POTION);
        org.bukkit.inventory.meta.ItemMeta meta = mashk.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&b&l💧 Mashk Ab - Mashk Irani"));
        meta.setLore(MessageUtils.color(java.util.Arrays.asList(
                "&7Mashk sonati Irani baraye negahdari ab",
                "&7Az poste boz sakhte shode",
                "",
                "&7Havie: &b10 bar ab",
                "&7Baraye noshidan Right-Click konid",
                "",
                "&e&lTarz estefade:",
                "&7- Right click dar dast",
                "&7- Ya ab ra az cheshme por konid",
                "",
                "&6&l🇮🇷 Sakhte Iran - Zende bad Iran!"
        )));
        mashk.setItemMeta(meta);
        return mashk;
    }

    public ItemStack createWaterBottle() {
        ItemStack bottle = new ItemStack(Material.POTION);
        org.bukkit.inventory.meta.ItemMeta meta = bottle.getItemMeta();
        meta.setDisplayName(MessageUtils.color("&b&l💧 Ab - Water Bottle"));
        meta.setLore(MessageUtils.color(java.util.Arrays.asList(
                "&7Ab tamiz baraye raf'e teshnegi",
                "&7+25% teshnegi",
                "",
                "&7Ba right click benoshid",
                "&6🇮🇷"
        )));
        bottle.setItemMeta(meta);
        return bottle;
    }
}

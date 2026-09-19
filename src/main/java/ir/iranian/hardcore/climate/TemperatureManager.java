package ir.iranian.hardcore.climate;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.language.LanguageManager;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Temperature & Climate System - v4.0
 * - Cold biomes: ICE_FLATS, TAIGA_COLD, etc cause freezing
 * - Hot biomes: DESERT, MESA, SAVANNA, HELL cause heatstroke
 * - Player needs to wear appropriate armor, stay near fire, drink water
 */
public class TemperatureManager {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Double> playerTemperature = new HashMap<>(); // -100 (freezing) to +100 (heatstroke), 0 = normal
    private final Map<UUID, Long> lastMessageTime = new HashMap<>();

    // Biomes classification for 1.12.2
    private final Set<Biome> coldBiomes = new HashSet<>(Arrays.asList(
            Biome.ICE_FLATS, Biome.ICE_MOUNTAINS, Biome.TAIGA_COLD, Biome.TAIGA_COLD_HILLS,
            Biome.FROZEN_OCEAN, Biome.FROZEN_RIVER, Biome.MUTATED_ICE_FLATS, Biome.MUTATED_TAIGA_COLD,
            Biome.COLD_BEACH
    ));

    private final Set<Biome> hotBiomes = new HashSet<>(Arrays.asList(
            Biome.DESERT, Biome.DESERT_HILLS, Biome.MESA, Biome.MESA_ROCK, Biome.MESA_CLEAR_ROCK,
            Biome.SAVANNA, Biome.SAVANNA_ROCK, Biome.MUTATED_DESERT, Biome.MUTATED_MESA,
            Biome.MUTATED_MESA_ROCK, Biome.MUTATED_MESA_CLEAR_ROCK, Biome.MUTATED_SAVANNA,
            Biome.MUTATED_SAVANNA_ROCK, Biome.HELL
    ));

    private final Set<Biome> moderateBiomes = new HashSet<>(Arrays.asList(
            Biome.PLAINS, Biome.FOREST, Biome.BIRCH_FOREST, Biome.ROOFED_FOREST,
            Biome.TAIGA, Biome.TAIGA_HILLS, Biome.EXTREME_HILLS, Biome.SWAMPLAND
    ));

    public TemperatureManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        startTemperatureTask();
    }

    private void startTemperatureTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().getBoolean("climate.enabled", true)) return;
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.getGameMode().name().equals("CREATIVE") || player.getGameMode().name().equals("SPECTATOR")) continue;
                    updateTemperature(player);
                }
            }
        }.runTaskTimer(plugin, 100L, 60L); // Every 3 seconds
    }

    private void updateTemperature(Player player) {
        UUID uuid = player.getUniqueId();
        Biome biome = player.getLocation().getBlock().getBiome();
        double temp = playerTemperature.getOrDefault(uuid, 0.0);

        // Base temperature change based on biome
        if (coldBiomes.contains(biome)) {
            temp -= getColdIntensity(biome);
        } else if (hotBiomes.contains(biome)) {
            temp += getHeatIntensity(biome);
        } else {
            // Moderate biomes - slowly return to normal
            if (temp > 0) temp -= 0.5;
            if (temp < 0) temp += 0.5;
        }

        // Time factor - night colder, day hotter in desert
        long time = player.getWorld().getTime();
        boolean isNight = time > 13000 && time < 23000;
        if (isNight && !hotBiomes.contains(biome)) {
            temp -= 0.3; // Night is colder
        }
        if (!isNight && hotBiomes.contains(biome)) {
            temp += 0.5; // Day in desert is hotter
        }

        // Armor factor
        temp += getArmorTemperatureEffect(player);

        // Fire factor - nearby fire/lava warms you in cold
        if (coldBiomes.contains(biome) && isNearHeatSource(player)) {
            temp += 2.0;
        }

        // Water factor - in water cools you in hot biome
        if (hotBiomes.contains(biome) && player.getLocation().getBlock().getType() == Material.WATER) {
            temp -= 1.5;
        }

        // Clamp
        temp = Math.max(-100, Math.min(100, temp));
        playerTemperature.put(uuid, temp);

        // Apply effects
        applyTemperatureEffects(player, temp, biome);
    }

    private double getColdIntensity(Biome biome) {
        switch (biome) {
            case ICE_FLATS:
            case MUTATED_ICE_FLATS:
            case FROZEN_OCEAN:
                return 1.5;
            case TAIGA_COLD:
            case TAIGA_COLD_HILLS:
            case MUTATED_TAIGA_COLD:
                return 1.0;
            case ICE_MOUNTAINS:
            case FROZEN_RIVER:
            case COLD_BEACH:
                return 0.8;
            default:
                return 0.5;
        }
    }

    private double getHeatIntensity(Biome biome) {
        switch (biome) {
            case DESERT:
            case MUTATED_DESERT:
            case HELL:
                return 1.5;
            case MESA:
            case MESA_ROCK:
            case MESA_CLEAR_ROCK:
            case MUTATED_MESA:
                return 1.2;
            case SAVANNA:
            case SAVANNA_ROCK:
            case MUTATED_SAVANNA:
                return 1.0;
            default:
                return 0.7;
        }
    }

    private double getArmorTemperatureEffect(Player player) {
        // Leather armor keeps you warm in cold, but hot in desert
        // Iron/chain cold in winter, etc
        int leatherCount = 0;
        int ironCount = 0;
        if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() == Material.LEATHER_HELMET) leatherCount++;
        if (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE) leatherCount++;
        if (player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) leatherCount++;
        if (player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == Material.LEATHER_BOOTS) leatherCount++;

        if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType().name().contains("IRON")) ironCount++;
        if (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType().name().contains("IRON")) ironCount++;
        if (player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType().name().contains("IRON")) ironCount++;
        if (player.getInventory().getBoots() != null && player.getInventory().getBoots().getType().name().contains("IRON")) ironCount++;

        // Leather warms in cold (+), but heats in hot (- for temp, meaning it makes you hotter)
        // Actually for our temp system: negative = cold, positive = hot
        // Leather should increase temp (warm) in cold, but also increase temp in hot (bad)
        // So leather always +0.2 temp, iron always -0.2 temp (cold metal)
        return leatherCount * 0.3 - ironCount * 0.1;
    }

    private boolean isNearHeatSource(Player player) {
        int radius = 5;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Material mat = player.getLocation().clone().add(x, y, z).getBlock().getType();
                    if (mat == Material.FIRE || mat == Material.LAVA || mat == Material.STATIONARY_LAVA ||
                            mat == Material.FURNACE || mat == Material.BURNING_FURNACE || mat == Material.TORCH) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void applyTemperatureEffects(Player player, double temp, Biome biome) {
        // Cold effects
        if (temp < -30) {
            if (temp < -80) {
                // Freezing - damage
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
                if (temp < -90 && Math.random() < 0.3) {
                    player.damage(1.0);
                    sendTemperatureMessage(player, "freezing");
                }
            } else if (temp < -50) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                if (Math.random() < 0.2) {
                    sendTemperatureMessage(player, "cold");
                }
            }
        }
        // Hot effects
        else if (temp > 30) {
            if (temp > 80) {
                // Heatstroke - damage
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
                if (temp > 90 && Math.random() < 0.3) {
                    player.damage(1.0);
                    sendTemperatureMessage(player, "heatstroke");
                }
                // Increase thirst in hot
                if (plugin.getThirstManager() != null) {
                    plugin.getThirstManager().addThirst(player, -0.5); // Extra thirst drain in extreme heat
                }
            } else if (temp > 50) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
                if (Math.random() < 0.15) {
                    sendTemperatureMessage(player, "hot");
                }
                if (plugin.getThirstManager() != null) {
                    plugin.getThirstManager().addThirst(player, -0.2);
                }
            }
        }
    }

    private void sendTemperatureMessage(Player player, String type) {
        long now = System.currentTimeMillis();
        long last = lastMessageTime.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < 15000) return; // 15 sec cooldown
        lastMessageTime.put(player.getUniqueId(), now);

        LanguageManager lang = plugin.getLanguageManager();
        String msg = "";
        switch (type) {
            case "cold":
                msg = lang.getText(LanguageManager.Translations.COLD_FA, LanguageManager.Translations.COLD_FINGLISH);
                break;
            case "freezing":
                msg = lang.getText(LanguageManager.Translations.FREEZING_FA, LanguageManager.Translations.FREEZING_FINGLISH);
                break;
            case "hot":
                msg = lang.getText(LanguageManager.Translations.HOT_FA, LanguageManager.Translations.HOT_FINGLISH);
                break;
            case "heatstroke":
                msg = lang.getText(LanguageManager.Translations.HEATSTROKE_FA, LanguageManager.Translations.HEATSTROKE_FINGLISH);
                break;
        }
        if (!msg.isEmpty()) {
            player.sendMessage(MessageUtils.withPrefix(msg));
        }

        // Action bar for temperature
        double temp = playerTemperature.getOrDefault(player.getUniqueId(), 0.0);
        String actionBar = "";
        if (temp < -30) {
            actionBar = "&b❄ Dama: " + String.format("%.0f", temp) + " | Sard! - Atash nazdik sho";
        } else if (temp > 30) {
            actionBar = "&c☀ Dama: " + String.format("%.0f", temp) + " | Garm! - Ab benosh";
        } else {
            actionBar = "&a☀ Dama: " + String.format("%.0f", temp) + " | Normal";
        }
        sendActionBar(player, actionBar);
    }

    private void sendActionBar(Player player, String message) {
        try {
            String colored = MessageUtils.color(message);
            // For 1.12.2, use title with action bar via NMS or via sending packet
            // Simple method: use sendActionBar via reflection or via title
            // We'll use the old method: player.sendActionBar is 1.16+, so for 1.12 we use title with 0, 40, 0 and action bar via packet
            // For simplicity, we'll send as message above hotbar using spigot method if available
            // Try via player.spigot().sendMessage(ChatMessageType.ACTION_BAR, ...)
            // We'll use reflection to avoid compile error
            Class<?> chatMessageTypeClass = Class.forName("net.md_5.bungee.api.ChatMessageType");
            Class<?> textComponentClass = Class.forName("net.md_5.bungee.api.chat.TextComponent");
            Object chatMessageType = chatMessageTypeClass.getField("ACTION_BAR").get(null);
            Object textComponent = textComponentClass.getConstructor(String.class).newInstance(colored);
            player.spigot().sendMessage((net.md_5.bungee.api.ChatMessageType) chatMessageType, (net.md_5.bungee.api.chat.BaseComponent) textComponent);
        } catch (Exception e) {
            // Fallback: send as normal message if action bar fails
            // player.sendMessage(MessageUtils.color(message));
        }
    }

    public double getTemperature(Player player) {
        return playerTemperature.getOrDefault(player.getUniqueId(), 0.0);
    }

    public void setTemperature(Player player, double temp) {
        playerTemperature.put(player.getUniqueId(), Math.max(-100, Math.min(100, temp)));
    }

    public void resetTemperature(Player player) {
        playerTemperature.put(player.getUniqueId(), 0.0);
    }
}

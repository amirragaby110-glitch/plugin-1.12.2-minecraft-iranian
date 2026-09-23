package ir.iranian.hardcore.climate;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Temperature, Weather & Seasons System (v5.0 Finglish)
 * - Cold biomes: ICE_FLATS, TAIGA_COLD, etc cause hypothermia
 * - Hot biomes: DESERT, MESA, SAVANNA, HELL cause heatstroke
 * - Persian Seasons: Bahar (Spring), Tabestan (Summer), Paeez (Autumn), Zemestan (Winter)
 * - Weather Hazards: Toofane Shen (Sandstorm), Koolak-e Barf (Blizzard)
 */
public class TemperatureManager {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Double> playerTemperature = new HashMap<>(); // -100 to +100
    private final Map<UUID, Long> lastMessageTime = new HashMap<>();
    private final Random random = new Random();

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

    public TemperatureManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        startTemperatureTask();
        plugin.getLogger().info("TemperatureManager: Climate, weather & seasons initialized!");
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
        }.runTaskTimer(plugin, 60L, 60L); // Every 3 seconds
    }

    private void updateTemperature(Player player) {
        UUID uuid = player.getUniqueId();
        Biome biome = player.getLocation().getBlock().getBiome();
        double temp = playerTemperature.getOrDefault(uuid, 0.0);

        // Biome baseline
        if (coldBiomes.contains(biome)) {
            temp -= getColdIntensity(biome);
        } else if (hotBiomes.contains(biome)) {
            temp += getHeatIntensity(biome);
        } else {
            // Return to equilibrium
            if (temp > 0) temp -= 0.6;
            if (temp < 0) temp += 0.6;
        }

        // Season factor
        int seasonMod = getSeasonTempModifier(player.getWorld());
        temp += seasonMod * 0.3;

        // Day/night cycle
        long time = player.getWorld().getTime();
        boolean isNight = time > 13000 && time < 23000;
        if (isNight) {
            temp -= 0.5;
        } else if (hotBiomes.contains(biome)) {
            temp += 0.6;
        }

        // Weather factor
        if (player.getWorld().hasStorm()) {
            temp -= 0.8;
            if (coldBiomes.contains(biome)) {
                // Blizzard
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0));
                if (random.nextDouble() < 0.10) {
                    player.sendMessage(MessageUtils.color("&9[Koolak-e Barf] &bBuran va koolak dar kohestan shoma ro sardtar mikone!"));
                }
            } else if (hotBiomes.contains(biome)) {
                // Sandstorm in desert during rain
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                if (random.nextDouble() < 0.10) {
                    player.sendMessage(MessageUtils.color("&6[Toofane Shen] &eToofan-e shen dar kavir dideh ro kam mikone!"));
                }
            }
        }

        // Armor thermal insulation
        temp += getArmorTemperatureEffect(player);

        // Fire sources nearby
        if (isNearHeatSource(player)) {
            temp += 2.5;
        }

        // In water in hot biome cools
        if (hotBiomes.contains(biome) && player.getLocation().getBlock().getType() == Material.WATER) {
            temp -= 2.0;
        }

        temp = Math.max(-100, Math.min(100, temp));
        playerTemperature.put(uuid, temp);

        applyTemperatureEffects(player, temp);
    }

    private double getColdIntensity(Biome biome) {
        switch (biome) {
            case ICE_FLATS:
            case MUTATED_ICE_FLATS:
            case FROZEN_OCEAN:
                return 1.6;
            case TAIGA_COLD:
            case TAIGA_COLD_HILLS:
            case MUTATED_TAIGA_COLD:
                return 1.1;
            case ICE_MOUNTAINS:
            case FROZEN_RIVER:
            case COLD_BEACH:
                return 0.9;
            default:
                return 0.6;
        }
    }

    private double getHeatIntensity(Biome biome) {
        switch (biome) {
            case DESERT:
            case MUTATED_DESERT:
            case HELL:
                return 1.6;
            case MESA:
            case MESA_ROCK:
            case MESA_CLEAR_ROCK:
            case MUTATED_MESA:
                return 1.3;
            case SAVANNA:
            case SAVANNA_ROCK:
            case MUTATED_SAVANNA:
                return 1.0;
            default:
                return 0.7;
        }
    }

    private double getArmorTemperatureEffect(Player player) {
        int leatherCount = 0;
        int ironCount = 0;
        for (org.bukkit.inventory.ItemStack item : player.getInventory().getArmorContents()) {
            if (item == null) continue;
            if (item.getType().name().contains("LEATHER")) leatherCount++;
            if (item.getType().name().contains("IRON")) ironCount++;
        }
        // Leather keeps warm (+), iron conducts cold (-)
        return (leatherCount * 0.4) - (ironCount * 0.2);
    }

    private boolean isNearHeatSource(Player player) {
        int radius = 4;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Material mat = player.getLocation().clone().add(x, y, z).getBlock().getType();
                    if (mat == Material.FIRE || mat == Material.LAVA || mat == Material.STATIONARY_LAVA 
                            || mat == Material.TORCH || mat == Material.BURNING_FURNACE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void applyTemperatureEffects(Player player, double temp) {
        // Blessed by Atash-e Bahram (Fire Resistance protects against extreme frost)
        if (temp < -40 && player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            return;
        }

        // Freezing
        if (temp < -40) {
            if (temp < -85) {
                player.damage(1.5);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120, 1));
                sendTemperatureMessage(player, "freezing");
            } else if (temp < -60) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                sendTemperatureMessage(player, "cold");
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0));
            }
        }
        // Heatstroke
        else if (temp > 40) {
            if (temp > 85) {
                player.damage(1.5);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 1));
                sendTemperatureMessage(player, "heatstroke");
                if (plugin.getThirstManager() != null) {
                    plugin.getThirstManager().addThirst(player, -0.6);
                }
            } else if (temp > 60) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                sendTemperatureMessage(player, "hot");
                if (plugin.getThirstManager() != null) {
                    plugin.getThirstManager().addThirst(player, -0.3);
                }
            }
        }
    }

    private void sendTemperatureMessage(Player player, String type) {
        long now = System.currentTimeMillis();
        long last = lastMessageTime.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < 15000) return;
        lastMessageTime.put(player.getUniqueId(), now);

        if ("freezing".equals(type)) {
            player.sendMessage(MessageUtils.color("&9&l[Yakh-zadan] &cSarma-ye koshandeh! Sarian nazdik-e atash beravid!"));
        } else if ("cold".equals(type)) {
            player.sendMessage(MessageUtils.color("&b[Sarma] &7Hava kheyli sard ast! Lebas-e garm (Leather) bepooshid."));
        } else if ("heatstroke".equals(type)) {
            player.sendMessage(MessageUtils.color("&4&l[Garmazadegi] &cKhatar-e garmazadegi-ye shadid! Ab benooshid ya be sayeh beravid!"));
        } else if ("hot".equals(type)) {
            player.sendMessage(MessageUtils.color("&e[Garma] &7Aftab-e soozan! Teshnegi-ye shoma saritar kam mishavad."));
        }
    }

    public String getSeasonName(World world) {
        long day = world.getFullTime() / 24000;
        long seasonCycle = (day / 30) % 4; // 30 in-game days per season
        if (seasonCycle == 0) return "Bahar (Spring)";
        if (seasonCycle == 1) return "Tabestan (Summer)";
        if (seasonCycle == 2) return "Paeez (Autumn)";
        return "Zemestan (Winter)";
    }

    public int getSeasonTempModifier(World world) {
        long day = world.getFullTime() / 24000;
        long seasonCycle = (day / 30) % 4;
        if (seasonCycle == 1) return 1; // Summer warmer
        if (seasonCycle == 3) return -1; // Winter colder
        return 0;
    }

    public String getWeatherDescription(World world) {
        if (world.isThundering()) return "Toofani va Sa'egheh";
        if (world.hasStorm()) return "Barani";
        return "Aftabi va Saf";
    }

    public double getTemperature(Player player) {
        return playerTemperature.getOrDefault(player.getUniqueId(), 0.0);
    }

    public boolean isColdBiome(Biome biome) {
        return coldBiomes.contains(biome);
    }

    public boolean isHotBiome(Biome biome) {
        return hotBiomes.contains(biome);
    }

    public void setTemperature(Player player, double temp) {
        playerTemperature.put(player.getUniqueId(), Math.max(-100, Math.min(100, temp)));
    }

    public void adjustTemp(Player player, double delta) {
        double current = getTemperature(player);
        setTemperature(player, current + delta);
    }

    public void resetTemperature(Player player) {
        playerTemperature.put(player.getUniqueId(), 0.0);
    }
}

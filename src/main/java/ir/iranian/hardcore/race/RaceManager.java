package ir.iranian.hardcore.race;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Modiriat aghvame Irani - Har biome yek ghom
 * v5.0 Finglish - Aghvame vaaghei Iran
 * Zakhire, bargozaari, aemall ghabeliat haye makhsoose har ghom
 */
public class RaceManager {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, RaceType> playerRaces = new HashMap<>();

    public RaceManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    public RaceType getPlayerRace(Player player) {
        return getPlayerRace(player.getUniqueId());
    }

    public RaceType getRace(Player player) {
        return getPlayerRace(player);
    }

    public void setRace(Player player, RaceType race) {
        setPlayerRace(player, race);
    }

    public long getRemainingDaysForRaceChange(Player player) {
        return getRaceChangeCooldownLeftDays(player);
    }

    public RaceType getPlayerRace(UUID uuid) {
        if (playerRaces.containsKey(uuid)) {
            return playerRaces.get(uuid);
        }
        String raceName = plugin.getConfigManager().getPlayerRace(uuid.toString());
        if (raceName != null) {
            RaceType type = RaceType.fromId(raceName);
            if (type != null) {
                playerRaces.put(uuid, type);
                return type;
            }
        }
        return null;
    }

    /**
     * Tanzim ghome Irani bazikon - Finglish
     */
    public void setPlayerRace(Player player, RaceType race) {
        setPlayerRace(player.getUniqueId(), race);
        applyRaceInitialStats(player, race);

        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        player.sendMessage(MessageUtils.withPrefix("&a&lGhome Irani shoma be &6&l" + race.getFinglishName() + " &a&ltaghir yaft!"));
        player.sendMessage(MessageUtils.color("&6&lZende bad ghome " + race.getFinglishName() + "!"));
        player.sendMessage(MessageUtils.color("&7Biome khane: &a" + race.getHomeBiomes().get(0).name()));
        player.sendMessage(MessageUtils.color("&7Ghodrat makhsoos: &eFaal shod dar biome khane"));
        player.sendMessage(MessageUtils.color("&7Zaaf: &cDar biomehaye sardsir/garmasir motafavet"));
        player.sendMessage(MessageUtils.withPrefix("&7Baraye etelaat bishtar: &e/race info"));
        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        player.sendTitle(MessageUtils.color("&6" + race.getFinglishName()), MessageUtils.color("&aBe ghome Irani khosh amadid!"), 20, 60, 20);
    }

    public void setPlayerRaceByAdmin(Player player, RaceType race) {
        setPlayerRace(player.getUniqueId(), race);
        applyRaceInitialStats(player, race);
        player.sendMessage(MessageUtils.withPrefix("&aGhome shoma tavasote admin be &6" + race.getFinglishName() + " &atanzim shod! &6Zende bad Iran!"));
    }

    public void setPlayerRace(UUID uuid, RaceType race) {
        playerRaces.put(uuid, race);
        plugin.getConfigManager().setPlayerRace(uuid.toString(), race.getId());
    }

    public void applyRaceInitialStats(Player player, RaceType race) {
        if (race == null) return;
        try {
            AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealth != null) {
                double baseHealth = plugin.getConfigManager().getDouble("hardcore.health.max-health", 20.0);
                if (race == RaceType.SISTANI || race == RaceType.LOR) {
                    baseHealth += 4.0;
                }
                maxHealth.setBaseValue(baseHealth);
            }
            AttributeInstance speed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if (speed != null) {
                double baseSpeed = 0.1;
                if (race == RaceType.TURKMEN || race == RaceType.QASHQAYI) {
                    baseSpeed = 0.115;
                }
                speed.setBaseValue(baseSpeed);
            }
        } catch (Exception ignored) {}
    }

    public void applyBiomeBuffs(Player player) {
        RaceType race = getPlayerRace(player);
        if (race == null) return;

        Biome currentBiome = player.getLocation().getBlock().getBiome();

        if (race.isHomeBiome(currentBiome)) {
            applyHomeBuffs(player, race);
        } else if (race.isHostileBiome(currentBiome)) {
            applyHostileDebuffs(player, race);
        }
    }

    private void applyHomeBuffs(Player player, RaceType race) {
        switch (race) {
            case FARS:
                giveEffect(player, PotionEffectType.FAST_DIGGING, 60, 0);
                giveEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 60, 0);
                break;
            case AZARI:
                giveEffect(player, PotionEffectType.SPEED, 60, 0);
                giveEffect(player, PotionEffectType.INCREASE_DAMAGE, 60, 0);
                break;
            case KURD:
                giveEffect(player, PotionEffectType.JUMP, 60, 1);
                giveEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 60, 0);
                break;
            case LOR:
                giveEffect(player, PotionEffectType.INCREASE_DAMAGE, 60, 0);
                giveEffect(player, PotionEffectType.FAST_DIGGING, 60, 0);
                break;
            case BALOCH:
                giveEffect(player, PotionEffectType.SPEED, 60, 0);
                giveEffect(player, PotionEffectType.FIRE_RESISTANCE, 60, 0);
                break;
            case ARAB_KHUZESTAN:
                giveEffect(player, PotionEffectType.WATER_BREATHING, 60, 0);
                giveEffect(player, PotionEffectType.FIRE_RESISTANCE, 60, 0);
                break;
            case TURKMEN:
                giveEffect(player, PotionEffectType.SPEED, 60, 1);
                giveEffect(player, PotionEffectType.JUMP, 60, 0);
                break;
            case GILAK:
                giveEffect(player, PotionEffectType.LUCK, 60, 1);
                giveEffect(player, PotionEffectType.REGENERATION, 60, 0);
                break;
            case MAZANI:
                giveEffect(player, PotionEffectType.SATURATION, 60, 0);
                giveEffect(player, PotionEffectType.WATER_BREATHING, 60, 0);
                break;
            case BAKHTIARI:
                giveEffect(player, PotionEffectType.JUMP, 60, 1);
                giveEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 60, 0);
                break;
            case QASHQAYI:
                giveEffect(player, PotionEffectType.SPEED, 60, 1);
                giveEffect(player, PotionEffectType.FAST_DIGGING, 60, 0);
                break;
            case BANDARI:
                giveEffect(player, PotionEffectType.WATER_BREATHING, 60, 0);
                giveEffect(player, PotionEffectType.SPEED, 60, 0);
                break;
            case KHORASANI:
                giveEffect(player, PotionEffectType.LUCK, 60, 1);
                giveEffect(player, PotionEffectType.NIGHT_VISION, 100, 0);
                break;
            case SISTANI:
                giveEffect(player, PotionEffectType.INCREASE_DAMAGE, 60, 1);
                giveEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 60, 0);
                break;
        }
    }

    private void applyHostileDebuffs(Player player, RaceType race) {
        giveEffect(player, PotionEffectType.SLOW_DIGGING, 60, 0);
        giveEffect(player, PotionEffectType.SLOW, 60, 0);
    }

    private void giveEffect(Player player, PotionEffectType type, int duration, int amplifier) {
        if (!player.hasPotionEffect(type)) {
            player.addPotionEffect(new PotionEffect(type, duration, amplifier, false, false));
        }
    }

    public boolean canChangeRace(Player player) {
        if (player.isOp() || player.hasPermission("iranianhardcore.admin") || player.hasPermission("iranianhardcore.bypass.race")) return true;
        long lastChanged = plugin.getConfigManager().getRaceChangeLastUsed(player.getUniqueId().toString());
        if (lastChanged == 0L) return true;
        int cooldownDays = plugin.getConfigManager().getInt("race.change-cooldown-days", 7);
        long cooldownMillis = (long) cooldownDays * 24 * 60 * 60 * 1000;
        return System.currentTimeMillis() - lastChanged >= cooldownMillis;
    }

    public long getRaceChangeCooldownLeftDays(Player player) {
        long lastChanged = plugin.getConfigManager().getRaceChangeLastUsed(player.getUniqueId().toString());
        if (lastChanged == 0L) return 0;
        int cooldownDays = plugin.getConfigManager().getInt("race.change-cooldown-days", 7);
        long cooldownMillis = (long) cooldownDays * 24 * 60 * 60 * 1000;
        long elapsed = System.currentTimeMillis() - lastChanged;
        long remaining = cooldownMillis - elapsed;
        if (remaining <= 0) return 0;
        return (remaining / (24 * 60 * 60 * 1000)) + 1;
    }

    public void recordRaceChange(Player player) {
        plugin.getConfigManager().setRaceChangeLastUsed(player.getUniqueId().toString(), System.currentTimeMillis());
    }

    public void removePlayerRace(Player player) {
        playerRaces.remove(player.getUniqueId());
        plugin.getConfigManager().setPlayerRace(player.getUniqueId().toString(), null);
        player.sendMessage(MessageUtils.withPrefix("&cGhome Irani shoma pak shod!"));
    }

    public void sendRaceInfo(Player player) {
        RaceType race = getPlayerRace(player);
        if (race == null) {
            player.sendMessage(MessageUtils.withPrefix("&cShoma hanooz ghome Irani entekhab nakardeid!"));
            player.sendMessage(MessageUtils.withPrefix("&eBaraye entekhab: &a/race choose &7ya &a/ghom"));
            player.sendMessage(MessageUtils.withPrefix("&7Aghvame Irani: Pars, Azari, Kurd, Lor, Baloch, Arab, Turkmen, Gilak, Mazani, Bakhtiari, Qashqayi, Bandari, Khorasani, Sistani"));
            return;
        }

        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        player.sendMessage(MessageUtils.color("&6&lGhome Irani shoma: &e&l" + race.getFinglishName()));
        player.sendMessage(MessageUtils.color("&7English: &f" + race.getEnglishName()));
        for (String lore : race.getLoreFinglish()) {
            player.sendMessage(MessageUtils.color(lore));
        }
        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
        player.sendMessage(MessageUtils.color("&7Biomehaye khane (ghodrat kamel): &a" + race.getHomeBiomes().size() + " biome"));
        for (Biome b : race.getHomeBiomes()) {
            player.sendMessage(MessageUtils.color("  &a✔ &7" + b.name()));
        }
        player.sendMessage(MessageUtils.color("&7Biomehaye doshman (zaaf): &c" + race.getHostileBiomes().size() + " biome"));
        player.sendMessage(MessageUtils.color("&7Biome feli shoma: &f" + player.getLocation().getBlock().getBiome().name()));

        Biome current = player.getLocation().getBlock().getBiome();
        if (race.isHomeBiome(current)) {
            player.sendMessage(MessageUtils.color("&a&l✔ Shoma dar sarzamine ghome khod hastid - Ghodrat kamel! Zende bad " + race.getFinglishName() + "!"));
        } else if (race.isHostileBiome(current)) {
            player.sendMessage(MessageUtils.color("&c&l✘ Shoma dar sarzamine doshmane ghome khod hastid - Zaeef shodeid!"));
        } else {
            player.sendMessage(MessageUtils.color("&e&l~ Shoma dar sarzamine bitaraf hastid."));
        }

        player.sendMessage(MessageUtils.color("&7Baraye taghir ghom: &e/race change &7(har 7 rooz)"));
        player.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
    }
}

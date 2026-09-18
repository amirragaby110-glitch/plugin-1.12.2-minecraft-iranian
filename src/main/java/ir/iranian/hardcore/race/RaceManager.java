package ir.iranian.hardcore.race;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * مدیریت اقوام ایرانی - هر بایوم یک قوم
 * نسخه 3.5 - کاملا فارسی - اقوام واقعی ایران
 * ذخیره، بارگذاری، اعمال قابلیت‌های مخصوص هر قوم
 */
public class RaceManager {

    private final IranianHardcorePlugin plugin;

    public RaceManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    public RaceType getRace(UUID uuid) {
        String id = plugin.getConfigManager().getPlayerRace(uuid);
        if (id == null) return null;
        return RaceType.fromId(id);
    }

    public RaceType getRace(Player player) {
        return getRace(player.getUniqueId());
    }

    public boolean hasRace(Player player) {
        return plugin.getConfigManager().hasRace(player.getUniqueId());
    }

    /**
     * تنظیم قوم ایرانی بازیکن - کاملا فارسی
     */
    public void setRace(Player player, RaceType race) {
        UUID uuid = player.getUniqueId();
        plugin.getConfigManager().setPlayerRace(uuid, race.getId());
        long now = player.getWorld().getFullTime();
        plugin.getConfigManager().setRaceChangeCooldown(uuid, now);

        applyImmediatePerks(player, race);

        player.sendMessage(MessageUtils.withPrefix("&a&lقوم ایرانی شما به &6&l" + race.getPersianName() + " &a&lتغییر یافت!"));
        player.sendMessage(MessageUtils.color("&6&l🇮🇷 زنده باد قوم " + race.getPersianName() + "!"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        for (String line : race.getLore()) {
            player.sendMessage(MessageUtils.color(line));
        }
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.withPrefix("&7برای اطلاعات بیشتر: &e/race info"));
        player.sendTitle(MessageUtils.color("&6" + race.getPersianName()), MessageUtils.color("&aبه قوم ایرانی خوش آمدید!"), 20, 60, 20);
        player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
    }

    public void setRaceNoCooldown(Player player, RaceType race) {
        plugin.getConfigManager().setPlayerRace(player.getUniqueId(), race.getId());
        applyImmediatePerks(player, race);
        player.sendMessage(MessageUtils.withPrefix("&aقوم شما توسط ادمین به &6" + race.getPersianName() + " &aتنظیم شد! &6🇮🇷"));
    }

    public boolean canChangeRace(Player player) {
        if (player.hasPermission("iranianhardcore.bypass.racecooldown")) return true;
        UUID uuid = player.getUniqueId();
        long lastChange = plugin.getConfigManager().getRaceChangeCooldown(uuid);
        if (lastChange == 0) return true;

        long now = player.getWorld().getFullTime();
        long cooldownTicks = plugin.getConfigManager().getConfig().getInt("race.change-cooldown-days", 7) * 24000L;
        long diff = now - lastChange;

        return diff >= cooldownTicks;
    }

    public long getRemainingDaysForRaceChange(Player player) {
        UUID uuid = player.getUniqueId();
        long lastChange = plugin.getConfigManager().getRaceChangeCooldown(uuid);
        if (lastChange == 0) return 0;
        long now = player.getWorld().getFullTime();
        long cooldownTicks = plugin.getConfigManager().getConfig().getInt("race.change-cooldown-days", 7) * 24000L;
        long diff = now - lastChange;
        if (diff >= cooldownTicks) return 0;
        long remainingTicks = cooldownTicks - diff;
        return (remainingTicks / 24000) + 1;
    }

    private void applyImmediatePerks(Player player, RaceType race) {
        switch (race) {
            case BANDARI:
            case ARAB_KHUZESTAN:
            case GILAK:
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 6000, 0, false, false), true);
                break;
            case BALOCH:
            case FARS:
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0, false, false), true);
                break;
            case AZARI:
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0, false, false), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 0, false, false), true);
                break;
            default:
                break;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != 10.0) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10.0);
                    if (player.getHealth() > 10.0) player.setHealth(10.0);
                }
            } catch (Exception ignored) {}
        }, 5L);
    }

    public void clearRace(Player player) {
        plugin.getConfigManager().removePlayerRace(player.getUniqueId());
        player.sendMessage(MessageUtils.withPrefix("&cقوم ایرانی شما پاک شد!"));
    }

    public void sendRaceInfo(Player player) {
        RaceType race = getRace(player);
        if (race == null) {
            player.sendMessage(MessageUtils.withPrefix("&cشما هنوز قوم ایرانی انتخاب نکرده‌اید!"));
            player.sendMessage(MessageUtils.withPrefix("&eبرای انتخاب: &a/race choose &7یا &a/قوم"));
            player.sendMessage(MessageUtils.withPrefix("&7اقوام ایرانی: فارس، آذری، کرد، لر، بلوچ، عرب، ترکمن، گیلک، مازنی، بختیاری، قشقایی، بندری، خراسانی، سیستانی"));
            return;
        }

        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&6&l🇮🇷 قوم ایرانی شما: &e&l" + race.getPersianName()));
        player.sendMessage(MessageUtils.color("&7انگلیسی: &f" + race.getEnglishName()));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        for (String line : race.getLore()) {
            player.sendMessage(MessageUtils.color(line));
        }
        player.sendMessage(MessageUtils.color("&7بایوم‌های خانه (قدرت کامل): &a" + race.getHomeBiomes().size() + " بایوم"));
        for (org.bukkit.block.Biome b : race.getHomeBiomes()) {
            player.sendMessage(MessageUtils.color("&8- &a" + b.name()));
        }
        player.sendMessage(MessageUtils.color("&7بایوم‌های دشمن (ضعف): &c" + race.getHostileBiomes().size() + " بایوم"));
        player.sendMessage(MessageUtils.color("&7بایوم فعلی شما: &f" + player.getLocation().getBlock().getBiome().name()));
        boolean isHome = race.isHomeBiome(player.getLocation().getBlock().getBiome());
        boolean isHostile = race.isHostileBiome(player.getLocation().getBlock().getBiome());
        if (isHome) {
            player.sendMessage(MessageUtils.color("&a&l✔ شما در سرزمین قوم خود هستید - قدرت کامل! زنده باد " + race.getPersianName() + "!"));
        } else if (isHostile) {
            player.sendMessage(MessageUtils.color("&c&l✘ شما در سرزمین دشمن قوم خود هستید - ضعیف شده‌اید!"));
        } else {
            player.sendMessage(MessageUtils.color("&e&l~ شما در سرزمین بی‌طرف هستید."));
        }
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&7برای تغییر قوم: &e/race change &7(هر 7 روز)"));
    }
}

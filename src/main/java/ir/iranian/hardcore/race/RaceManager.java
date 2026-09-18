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
 * مدیریت نژاد بازیکنان
 * ذخیره، بارگذاری، اعمال افکت‌ها
 */
public class RaceManager {

    private final IranianHardcorePlugin plugin;

    public RaceManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * گرفتن نژاد بازیکن
     */
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
     * تنظیم نژاد بازیکن
     */
    public void setRace(Player player, RaceType race) {
        UUID uuid = player.getUniqueId();
        plugin.getConfigManager().setPlayerRace(uuid, race.getId());
        // ثبت کولدان تغییر نژاد
        long now = player.getWorld().getFullTime();
        plugin.getConfigManager().setRaceChangeCooldown(uuid, now);

        applyImmediatePerks(player, race);

        player.sendMessage(MessageUtils.withPrefix("&aنژاد شما به &6" + race.getPersianName() + " &aتغییر یافت!"));
        player.sendMessage(MessageUtils.color("&7" + String.join("\n&7", race.getLore())));
    }

    /**
     * تنظیم نژاد بدون کولدان (برای ادمین)
     */
    public void setRaceNoCooldown(Player player, RaceType race) {
        plugin.getConfigManager().setPlayerRace(player.getUniqueId(), race.getId());
        applyImmediatePerks(player, race);
        player.sendMessage(MessageUtils.withPrefix("&aنژاد شما توسط ادمین به &6" + race.getPersianName() + " &aتنظیم شد!"));
    }

    /**
     * آیا بازیکن می‌تواند نژادش را تغییر دهد؟ (هر 7 روز)
     */
    public boolean canChangeRace(Player player) {
        if (player.hasPermission("iranianhardcore.bypass.racecooldown")) return true;
        UUID uuid = player.getUniqueId();
        long lastChange = plugin.getConfigManager().getRaceChangeCooldown(uuid);
        if (lastChange == 0) return true; // اولین بار

        long now = player.getWorld().getFullTime();
        long cooldownTicks = plugin.getConfigManager().getConfig().getInt("race.change-cooldown-days", 7) * 24000L;
        long diff = now - lastChange;

        return diff >= cooldownTicks;
    }

    /**
     * روزهای باقی‌مانده تا امکان تغییر نژاد
     */
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

    /**
     * افکت‌های فوری هنگام انتخاب نژاد
     */
    private void applyImmediatePerks(Player player, RaceType race) {
        // پاک کردن افکت‌های قبلی نژاد (اختیاری)
        // player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));

        // اعمال افکت‌های پایه بسته به نژاد
        switch (race) {
            case OCEANBORN:
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false), true);
                break;
            case NETHERBORN:
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), true);
                break;
            default:
                break;
        }

        // برای اطمینان جان بازیکن درست بماند (هاردکور 10)
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != 10.0) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10.0);
                    if (player.getHealth() > 10.0) player.setHealth(10.0);
                }
            } catch (Exception ignored) {}
        }, 5L);
    }

    /**
     * حذف نژاد
     */
    public void clearRace(Player player) {
        plugin.getConfigManager().removePlayerRace(player.getUniqueId());
    }

    /**
     * اطلاعات نژاد برای نمایش
     */
    public void sendRaceInfo(Player player) {
        RaceType race = getRace(player);
        if (race == null) {
            player.sendMessage(MessageUtils.withPrefix("&cشما هنوز نژادی انتخاب نکرده‌اید! &e/race choose"));
            return;
        }

        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&6&l نژاد شما: &e" + race.getPersianName() + " (" + race.getEnglishName() + ")"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        for (String line : race.getLore()) {
            player.sendMessage(MessageUtils.color(line));
        }
        player.sendMessage(MessageUtils.color("&7بایوم‌های خانه: &a" + race.getHomeBiomes().size() + " بایوم"));
        player.sendMessage(MessageUtils.color("&7بایوم‌های دشمن: &c" + race.getHostileBiomes().size() + " بایوم"));
        player.sendMessage(MessageUtils.color("&7بایوم فعلی: &f" + player.getLocation().getBlock().getBiome().name()));
        boolean isHome = race.isHomeBiome(player.getLocation().getBlock().getBiome());
        boolean isHostile = race.isHostileBiome(player.getLocation().getBlock().getBiome());
        if (isHome) {
            player.sendMessage(MessageUtils.color("&a✔ شما در بایوم خانه خود هستید - قدرت کامل!"));
        } else if (isHostile) {
            player.sendMessage(MessageUtils.color("&c✘ شما در بایوم دشمن هستید - ضعیف شده‌اید!"));
        } else {
            player.sendMessage(MessageUtils.color("&e~ شما در بایوم خنثی هستید."));
        }
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
    }
}

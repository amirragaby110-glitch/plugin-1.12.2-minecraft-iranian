package ir.iranian.hardcore.calendar;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Historical Persian Seasonal & Festival Event Controller.
 * Automatically detects Persian holidays (Nowruz, Mehregan, Yalda, Sadeh)
 * and applies festival blessings, notifications, and celebrations.
 */
public class PersianEventManager {

    private final IranianHardcorePlugin plugin;
    private PersianDate lastDate;

    public PersianEventManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        this.lastDate = PersianCalendar.now();
        startDailyCheckTask();
        plugin.getLogger().info("PersianEventManager: Solar calendar events initialized! Current Date: " + lastDate.toString());
    }

    private void startDailyCheckTask() {
        // Runs every 5 minutes to check date transition and festival buffs
        new BukkitRunnable() {
            @Override
            public void run() {
                PersianDate current = PersianCalendar.now();
                if (current.hasFestival() && (lastDate == null || !current.getFestival().equals(lastDate.getFestival()))) {
                    broadcastFestivalCelebration(current);
                }
                lastDate = current;
            }
        }.runTaskTimer(plugin, 100L, 6000L);
    }

    public void broadcastFestivalCelebration(PersianDate date) {
        String fest = date.getFestival();
        Bukkit.broadcastMessage(MessageUtils.color("&8§m----------------------------------------"));
        Bukkit.broadcastMessage(MessageUtils.color("&6&l★ JASHN-E BASTANI-YE IRAN ★"));
        Bukkit.broadcastMessage(MessageUtils.color("&eEmrooz: &a" + date.toFormalString() + " &7- &b" + fest));
        Bukkit.broadcastMessage(MessageUtils.color("&6Barakat-e Jashn bar tamam-e delavaran va bazikonan jari shod!"));
        Bukkit.broadcastMessage(MessageUtils.color("&8§m----------------------------------------"));

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.5f, 1.0f);
            if (fest.contains("Nowruz")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 1));
            } else if (fest.contains("Yalda")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 6000, 1));
            } else if (fest.contains("Mehregan")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 0));
            } else if (fest.contains("Sadeh")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0));
            }
        }
    }

    public PersianDate getCurrentDate() {
        return PersianCalendar.now();
    }
}

package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Listener baraye mahdoodiat-haye bagha - v5.0 Finglish
 */
public class SurvivalListener implements Listener {

    private final IranianHardcorePlugin plugin;

    public SurvivalListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.survival.bed.enabled", true)) return;

        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        // Bed only at night
        if (plugin.getConfigManager().getBoolean("hardcore.survival.bed.only-at-night", true)) {
            long time = player.getWorld().getTime();
            if (time < 12500 || time > 23500) {
                player.sendMessage(MessageUtils.withPrefix(plugin.getConfigManager().getString("messages.bed-only-night", "&cFaghat dar shab mitavanid bekhabid!")));
            }
        }

        // Bed cooldown: every 3 days
        long lastUsed = plugin.getConfigManager().getBedLastUsed(player.getUniqueId().toString());
        int cooldownDays = plugin.getConfigManager().getInt("hardcore.survival.bed.cooldown-days", 3);
        long cooldownMillis = (long) cooldownDays * 24 * 60 * 60 * 1000;

        if (lastUsed > 0 && System.currentTimeMillis() - lastUsed < cooldownMillis) {
            long remaining = cooldownMillis - (System.currentTimeMillis() - lastUsed);
            long daysLeft = (remaining / (24 * 60 * 60 * 1000)) + 1;
            String msg = plugin.getConfigManager().getString("hardcore.survival.bed.cooldown-message", "&cBayad %days% rooz digar sabr konid.");
            msg = msg.replace("%days%", String.valueOf(daysLeft));
            player.sendMessage(MessageUtils.withPrefix(msg));
        }
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.survival.bed.enabled", true)) return;

        Player player = event.getPlayer();
        double healAmount = plugin.getConfigManager().getDouble("hardcore.survival.bed.heal-amount", 4.0);

        try {
            double current = player.getHealth();
            double max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double target = Math.min(max, current + healAmount);
            player.setHealth(target);
            player.sendMessage(MessageUtils.withPrefix("&eKhab sabok bood... Faghat &c" + (healAmount / 2) + " ghalb &ebehbood yaftid!"));
        } catch (Exception ignored) {}

        plugin.getConfigManager().setBedLastUsed(player.getUniqueId().toString(), System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerMoveInWater(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockY() == event.getTo().getBlockY() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.survival.water-lava.harder-water", true)) return;

        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        Material block = player.getLocation().getBlock().getType();
        if (block == Material.WATER || block == Material.STATIONARY_WATER) {
            if (player.getRemainingAir() > 0) {
                if (Math.random() < 0.15) {
                    player.setRemainingAir(Math.max(0, player.getRemainingAir() - 2));
                }
            }
            if (Math.random() < 0.05) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 0));
            }
        }
    }

    @EventHandler
    public void onPlayerDrinkMashk(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() != Material.POTION) return;

        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String displayName = item.getItemMeta().getDisplayName();
        if (displayName.contains("Mashk") || displayName.contains("Canteen")) {
            if (plugin.getThirstManager() != null) {
                if (plugin.getThirstManager().getThirst(player) >= 100.0) {
                    player.sendMessage(MessageUtils.color("&aTeshne nistid!"));
                    return;
                }
                plugin.getThirstManager().addThirst(player, 35.0);
                player.sendMessage(MessageUtils.color("&bMashk ab ra nooshidid! Teshnegi bartaraf shod."));
            }
        }
    }

    @EventHandler
    public void onLiquidFlow(BlockFromToEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.survival.water-lava.harder-water", true)) return;

        Material mat = event.getBlock().getType();
        if (mat == Material.WATER || mat == Material.STATIONARY_WATER) {
            if (Math.random() < 0.10) {
                event.setCancelled(true);
            }
        }
    }
}

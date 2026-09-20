package ir.iranian.hardcore.structures;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Atashkadeh-ye Bahram (Zoroastrian Holy Eternal Fire Shrine)
 * Features:
 * - Offering wood, coal, or Saffron to the sacred flame
 * - Grants "Nour-e Khorshid / Barakat-e Bahram"
 * - 10 minutes of Night Vision, Fire Resistance, and absolute Cold Immunity
 */
public class AtashkadehBahram implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Long> blessingCooldown = new HashMap<>();

    public AtashkadehBahram(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("AtashkadehBahram: Sacred fire temple rituals initialized!");
    }

    @EventHandler
    public void onAltarInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null) return;

        // Check if clicking fire, netherrack, or magma in a sacred temple
        boolean isFireAltar = (block.getType() == Material.FIRE || block.getType() == Material.NETHERRACK || block.getType() == Material.MAGMA);
        if (!isFireAltar) return;

        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand == null || hand.getType() == Material.AIR) return;

        boolean isSacredOffering = hand.getType() == Material.LOG || hand.getType() == Material.COAL ||
                (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() && hand.getItemMeta().getDisplayName().contains("Zaferan"));

        if (!isSacredOffering) return;

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        long last = blessingCooldown.getOrDefault(uuid, 0L);

        if (now - last < 600000) { // 10 minutes
            long left = (600000 - (now - last)) / 1000;
            MessageUtils.sendActionBar(player, "&cAtash-e Bahram roshan ast. Barakat ta " + left + "s digar bar shoma jari ast!");
            return;
        }

        // Consume 1 item offering
        hand.setAmount(hand.getAmount() - 1);
        blessingCooldown.put(uuid, now);

        // Visual & audio effects
        Location loc = block.getLocation().add(0.5, 1.0, 0.5);
        player.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1.5f, 1.0f);
        player.getWorld().playSound(loc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.2f, 1.2f);
        player.getWorld().spawnParticle(Particle.FLAME, loc, 40, 0.4, 0.8, 0.4, 0.08);
        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 20, 0.5, 0.5, 0.5, 0.1);

        // Grant 10 minutes of Night Vision & Fire Resistance
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2400, 0));

        MessageUtils.sendActionBar(player, "&6&l[Atash-e Bahram] &aNazr pazirofteh shod! 'Nour-e Khorshid' (Hefazat dar barabar-e atash va sarma) اعطا shod!");
        event.setCancelled(true);
    }
}

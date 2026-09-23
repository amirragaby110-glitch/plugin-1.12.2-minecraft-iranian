package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/**
 * Listener baraye mechanichaye hardcore marboot be bazikon - v5.0 Finglish
 */
public class PlayerHardcoreListener implements Listener {

    private final IranianHardcorePlugin plugin;

    // Raw foods list
    private static final List<Material> RAW_FOODS = Arrays.asList(
            Material.RAW_BEEF,
            Material.RAW_CHICKEN,
            Material.PORK,
            Material.MUTTON,
            Material.RABBIT,
            Material.RAW_FISH,
            Material.ROTTEN_FLESH
    );

    // Stone blocks requiring proper tools
    private static final List<Material> STONE_BLOCKS = Arrays.asList(
            Material.STONE,
            Material.COBBLESTONE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.DIAMOND_ORE,
            Material.COAL_ORE,
            Material.REDSTONE_ORE,
            Material.GLOWING_REDSTONE_ORE,
            Material.LAPIS_ORE,
            Material.EMERALD_ORE,
            Material.OBSIDIAN,
            Material.NETHERRACK,
            Material.QUARTZ_ORE,
            Material.ENDER_STONE
    );

    public PlayerHardcoreListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;

        // Set hardcore health (5 hearts)
        plugin.getHardcoreManager().setHardcoreHealth(player);

        // First join race prompt
        if (plugin.getConfigManager().getBoolean("race.enabled", true)) {
            if (plugin.getRaceManager().getPlayerRace(player) == null) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (player.isOnline()) {
                        player.sendMessage(MessageUtils.withPrefix(plugin.getConfigManager().getString("race.messages.choose-first", "&eLotfan ghome Irani khod ra entekhab konid! /race choose")));
                        if (plugin.getConfigManager().getBoolean("race.open-gui-on-first-join", true)) {
                            plugin.getRaceGUI().openRaceSelectionGUI(player);
                        }
                    }
                }, 40L);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.health.faster-hunger", true)) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

            int oldFood = player.getFoodLevel();
            int newFood = event.getFoodLevel();

            if (newFood < oldFood) {
                double mult = plugin.getConfigManager().getDouble("hardcore.health.hunger-drain-multiplier", 2.0);
                int diff = oldFood - newFood;
                int extraDrain = (int) Math.round(diff * (mult - 1.0));
                event.setFoodLevel(Math.max(0, newFood - extraDrain));
            }
        }
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.health.disable-natural-regen", true)) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

            if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED ||
                    event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.health.raw-food-poison", true)) return;

        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        ItemStack item = event.getItem();
        if (RAW_FOODS.contains(item.getType())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 300, 1));
            player.sendMessage(MessageUtils.withPrefix("&cGhazaye kham shoma ra masmoom kard!"));
        }
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;

        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        double mult = plugin.getConfigManager().getDouble("hardcore.resources.tool-durability-multiplier", 2.0);
        int extraDamage = (int) Math.round(event.getDamage() * (mult - 1.0));
        event.setDamage(event.getDamage() + extraDamage);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;

        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        Material blockType = event.getBlock().getType();
        ItemStack inHand = player.getInventory().getItemInMainHand();

        // Leaves need shears
        if (plugin.getConfigManager().getBoolean("hardcore.resources.leaves-need-shears", true)) {
            if (blockType == Material.LEAVES || blockType == Material.LEAVES_2) {
                if (inHand.getType() != Material.SHEARS) {
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    return;
                }
            }
        }

        // Stone requires proper tool
        if (plugin.getConfigManager().getBoolean("hardcore.resources.require-correct-tool-for-stone", true)) {
            if (STONE_BLOCKS.contains(blockType)) {
                if (!isPickaxe(inHand.getType())) {
                    event.setCancelled(true);
                    player.sendMessage(MessageUtils.withPrefix("&cBaraye estekhraje in block be abzare monaseb niaz darid!"));
                }
            }
        }
    }

    private boolean isPickaxe(Material material) {
        return material == Material.WOOD_PICKAXE ||
                material == Material.STONE_PICKAXE ||
                material == Material.IRON_PICKAXE ||
                material == Material.GOLD_PICKAXE ||
                material == Material.DIAMOND_PICKAXE;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;

        Player player = event.getEntity();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        // Destroy inventory on death
        if (plugin.getConfigManager().getBoolean("hardcore.death.destroy-inventory", true)) {
            event.getDrops().clear();
        }

        if (plugin.getConfigManager().getBoolean("hardcore.death.clear-exp", true)) {
            event.setDroppedExp(0);
            event.setNewExp(0);
            event.setNewLevel(0);
        }

        player.sendMessage(MessageUtils.withPrefix(plugin.getConfigManager().getString("messages.inventory-destroyed", "&4Tamam itemhaye shoma nabood shod!")));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.enabled", true)) return;

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                plugin.getHardcoreManager().setHardcoreHealth(player);

                // Zero attack-blocking weakness debuff so players can always fight mobs cleanly
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 400, 0));

                player.sendMessage(MessageUtils.withPrefix("&7Shoma dobareh zendeh shodid..."));

                if (plugin.getConfigManager().getBoolean("race.enabled", true)) {
                    plugin.getRaceManager().applyRaceInitialStats(player, plugin.getRaceManager().getPlayerRace(player));
                }
            }
        }, 5L);
    }
}

package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
 * لیسنر برای مکانیک‌های هاردکور مربوط به بازیکن
 * - سلامت نصف
 * - گرسنگی سریع
 * - غذای خام مسموم
 * - ابزار 2 برابر خرابی
 * - برگ بدون قیچی هیچی نده
 * - مرگ با نابودی اینونتوری
 */
public class PlayerHardcoreListener implements Listener {

    private final IranianHardcorePlugin plugin;

    // لیست غذاهای خام که مسموم می‌کنند
    private final List<Material> rawFoods = Arrays.asList(
            Material.RAW_BEEF,
            Material.RAW_CHICKEN,
            Material.RAW_FISH,
            Material.PORK,
            Material.MUTTON,
            Material.RABBIT,
            Material.ROTTEN_FLESH
    );

    // بلوک‌های سنگی که نیاز به ابزار درست دارند
    private final List<Material> stoneBlocks = Arrays.asList(
            Material.STONE,
            Material.COBBLESTONE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.DIAMOND_ORE,
            Material.COAL_ORE,
            Material.EMERALD_ORE,
            Material.REDSTONE_ORE,
            Material.GLOWING_REDSTONE_ORE,
            Material.LAPIS_ORE,
            Material.OBSIDIAN,
            Material.NETHERRACK,
            Material.NETHER_BRICK,
            Material.SANDSTONE,
            Material.BRICK
    );

    public PlayerHardcoreListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // تنظیم جان نصف
        plugin.getHardcoreManager().applyMaxHealth(player);

        // اگر اولین بار است و باید نژاد انتخاب کند
        if (plugin.getConfigManager().getBoolean("race.force-choose-on-first-join", true)) {
            if (!plugin.getRaceManager().hasRace(player)) {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    player.sendMessage(MessageUtils.withPrefix(plugin.getConfigManager().getString("race.messages.choose-first", "&eلطفاً نژاد خود را انتخاب کنید! /race choose")));
                    if (plugin.getConfigManager().getBoolean("race.open-gui-on-first-join", true)) {
                        plugin.getRaceGUI().openRaceGUI(player);
                    }
                }, 40L);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (!plugin.getConfigManager().getBoolean("hardcore.health.faster-hunger", true)) return;
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        // اگر غذا در حال کم شدن است، سریع‌تر کم شود
        int current = player.getFoodLevel();
        int newLevel = event.getFoodLevel();

        if (newLevel < current) {
            double multiplier = plugin.getConfigManager().getDouble("hardcore.health.hunger-drain-multiplier", 2.0);
            // اختلاف را بیشتر کن
            int diff = current - newLevel;
            int extra = (int) Math.round(diff * (multiplier - 1));
            int finalLevel = Math.max(0, newLevel - extra);
            event.setFoodLevel(finalLevel);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRegainHealth(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!plugin.getConfigManager().getBoolean("hardcore.health.disable-natural-regen", true)) return;

        // غیرفعال کردن رژن طبیعی با غذا
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED ||
                event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.health.raw-food-poison", true)) return;
        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        if (rawFoods.contains(item.getType())) {
            // مسموم کردن
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 300, 1));
                player.sendMessage(MessageUtils.withPrefix("&cغذای خام شما را مسموم کرد!"));
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemDamage(PlayerItemDamageEvent event) {
        if (!plugin.getConfigManager().getBoolean("hardcore.resources.tool-durability-multiplier", true)) return;
        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        double mult = plugin.getConfigManager().getDouble("hardcore.resources.tool-durability-multiplier", 2.0);
        if (mult <= 1.0) return;

        // ابزار 2 برابر سریع‌تر خراب شود
        int originalDamage = event.getDamage();
        int newDamage = (int) Math.ceil(originalDamage * mult);
        event.setDamage(newDamage);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        Material blockType = event.getBlock().getType();
        ItemStack tool = player.getInventory().getItemInMainHand();

        // برگ‌ها بدون قیچی هیچی ندهند
        if (plugin.getConfigManager().getBoolean("hardcore.resources.leaves-need-shears", true)) {
            if (blockType == Material.LEAVES || blockType == Material.LEAVES_2) {
                if (tool == null || tool.getType() != Material.SHEARS) {
                    // لغو دراپ با ست کردن بلوک به هوا بدون دراپ
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    // هیچ دراپی نده
                    return;
                }
            }
        }

        // سنگ فقط با ابزار درست
        if (plugin.getConfigManager().getBoolean("hardcore.resources.require-correct-tool-for-stone", true)) {
            if (stoneBlocks.contains(blockType)) {
                if (tool == null || !isCorrectToolForStone(tool.getType())) {
                    event.setCancelled(true);
                    player.sendMessage(MessageUtils.withPrefix("&cبرای استخراج این بلوک به ابزار مناسب نیاز دارید!"));
                }
            }
        }
    }

    private boolean isCorrectToolForStone(Material tool) {
        // پیک‌اکس‌ها
        return tool == Material.WOOD_PICKAXE ||
                tool == Material.STONE_PICKAXE ||
                tool == Material.IRON_PICKAXE ||
                tool == Material.GOLD_PICKAXE ||
                tool == Material.DIAMOND_PICKAXE;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!plugin.getConfigManager().getBoolean("hardcore.death.destroy-inventory", true)) return;
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        // نابود کردن اینونتوری به جای دراپ
        event.getDrops().clear();
        event.setDroppedExp(0);
        if (plugin.getConfigManager().getBoolean("hardcore.death.clear-exp", true)) {
            event.setNewExp(0);
            event.setNewLevel(0);
            event.setKeepLevel(false);
        }

        player.sendMessage(MessageUtils.withPrefix(plugin.getConfigManager().getString("messages.inventory-destroyed", "&4تمام آیتم‌های شما نابود شد!")));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("iranianhardcore.bypass.hardcore")) return;

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            // اعمال جان نصف دوباره
            plugin.getHardcoreManager().applyMaxHealth(player);

            // دیباف ضعف هنگام اسپاون
            List<String> debuffs = plugin.getConfigManager().getConfig().getStringList("hardcore.death.respawn-debuffs");
            if (debuffs != null) {
                for (String s : debuffs) {
                    try {
                        String[] parts = s.split(":");
                        PotionEffectType type = PotionEffectType.getByName(parts[0]);
                        int duration = Integer.parseInt(parts[1]);
                        int amplifier = Integer.parseInt(parts[2]);
                        if (type != null) {
                            player.addPotionEffect(new PotionEffect(type, duration, amplifier, false, true));
                        }
                    } catch (Exception ignored) {}
                }
            }

            player.sendMessage(MessageUtils.withPrefix("&7شما با ضعف پس از مرگ اسپاون شدید..."));

            // اگر نژاد دارد، افکت‌های نژاد را دوباره اعمال کن
            if (plugin.getRaceManager().hasRace(player)) {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    // افکت‌های نژاد در RaceListener اعمال می‌شود
                }, 20L);
            }
        }, 5L);
    }
}

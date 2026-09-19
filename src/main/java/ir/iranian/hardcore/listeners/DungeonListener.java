package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * لیسنر دانجن‌های ایرانی - کاملا فارسی
 * - تولید دانجن در چانک جدید
 * - پیام ورود به دانجن
 * - لوت باس
 */
public class DungeonListener implements Listener {

    private final IranianHardcorePlugin plugin;

    public DungeonListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.isNewChunk()) {
            // تولید دانجن ایرانی با شانس کم
            plugin.getDungeonManager().tryGenerateInChunk(event.getChunk());
            // تولید سازه ایرانی
            plugin.getPersianStructures().tryGenerateInChunk(event.getChunk());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getCustomName() == null) return;
        String name = event.getEntity().getCustomName();

        // اگر باس ایرانی بود، پیام حماسی
        if (name.contains("حسن صباح") || name.contains("بابک") || name.contains("داریوش") ||
                name.contains("آناهیتا") || name.contains("سیراف") || name.contains("بم") ||
                name.contains("چغازنبیل") || name.contains("زرتشتی")) {

            // پیام به همه بازیکنان نزدیک
            for (Player p : event.getEntity().getWorld().getPlayers()) {
                if (p.getLocation().distance(event.getEntity().getLocation()) < 100) {
                    p.sendMessage(MessageUtils.withPrefix("&6&l⚔ باس ایرانی شکست خورد!"));
                    p.sendMessage(MessageUtils.withPrefix("&7" + name + " &fتوسط &a" + (event.getEntity().getKiller() != null ? event.getEntity().getKiller().getName() : "کسی") + " &fکشته شد!"));
                    p.getWorld().playSound(p.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
                }
            }

            // دراپ ویژه
            event.getDrops().clear();
            event.setDroppedExp(100);

            if (name.contains("حسن صباح")) {
                event.getDrops().add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_SWORD));
            } else if (name.contains("داریوش")) {
                event.getDrops().add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.GOLD_BLOCK, 5));
                event.getDrops().add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_BLOCK, 2));
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // اگر بازیکن وارد محدوده دانجن شد، پیام بده
        // برای سادگی، چک می‌کنیم آیا در نزدیکی دانجن است
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        for (java.util.Map.Entry<String, org.bukkit.Location> entry : plugin.getDungeonManager().getGeneratedDungeons().entrySet()) {
            org.bukkit.Location dungeonLoc = entry.getValue();
            if (!dungeonLoc.getWorld().equals(event.getPlayer().getWorld())) continue;

            double dist = event.getPlayer().getLocation().distance(dungeonLoc);
            if (dist < 30 && dist > 28) { // در حال نزدیک شدن
                String typeId = entry.getKey().split("_")[0];
                ir.iranian.hardcore.dungeons.DungeonType type = ir.iranian.hardcore.dungeons.DungeonType.fromId(typeId);
                if (type != null) {
                    Player p = event.getPlayer();
                    p.sendMessage(MessageUtils.withPrefix("&6&l🏛 نزدیک دانجن ایرانی شدی: &e" + type.getPersianName()));
                    p.sendMessage(MessageUtils.withPrefix("&7" + type.getDescription()));
                    p.sendTitle(MessageUtils.color("&6" + type.getPersianName()), MessageUtils.color("&7" + type.getDescription()), 20, 60, 20);
                    p.playSound(p.getLocation(), org.bukkit.Sound.BLOCK_PORTAL_AMBIENT, 1.0f, 0.5f);
                }
                break;
            }
        }
    }
}

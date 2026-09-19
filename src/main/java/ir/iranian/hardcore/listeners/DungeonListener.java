package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.dungeons.DungeonType;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Map;

/**
 * Listener baraye dungeonhaye Irani - v5.0 Finglish
 */
public class DungeonListener implements Listener {

    private final IranianHardcorePlugin plugin;

    public DungeonListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.isNewChunk()) {
            plugin.getDungeonManager().tryGenerateInChunk(event.getChunk());
            plugin.getPersianStructures().tryGenerateInChunk(event.getChunk());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getCustomName() == null) return;
        String name = event.getEntity().getCustomName();

        // Check if boss was an Iranian dungeon boss
        boolean isIranianBoss = name.contains("Hassan Sabbah") || name.contains("Babak") ||
                name.contains("Daryoosh") || name.contains("Anahita") || name.contains("Siraf") ||
                name.contains("Bam") || name.contains("Chogha") || name.contains("Zartosht") ||
                name.contains("Kourosh") || name.contains("Zahhak") || name.contains("Afrasiab");

        if (isIranianBoss) {
            String killerName = (event.getEntity().getKiller() != null) ? event.getEntity().getKiller().getName() : "Yek Delavar";
            for (Player p : event.getEntity().getWorld().getPlayers()) {
                if (p.getLocation().distance(event.getEntity().getLocation()) < 120) {
                    p.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                    p.sendMessage(MessageUtils.withPrefix("&6&lBosse Irani shekast khord!"));
                    p.sendMessage(MessageUtils.withPrefix("&e" + name + " &ftavasote &a" + killerName + " &fkoshte shod!"));
                    p.sendMessage(MessageUtils.color("&6Zende bad delavarane Iran zamin!"));
                    p.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                    p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                }
            }

            event.setDroppedExp(250);

            // Reward legendary Iranian sword
            if (plugin.getSwordsManager() != null) {
                event.getDrops().add(plugin.getSwordsManager().getRandomSword());
            }
            if (plugin.getFoodsManager() != null) {
                event.getDrops().add(plugin.getFoodsManager().getRandomFood());
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        for (Map.Entry<String, Location> entry : plugin.getDungeonManager().getGeneratedDungeons().entrySet()) {
            Location dungeonLoc = entry.getValue();
            if (dungeonLoc.getWorld() == null || !dungeonLoc.getWorld().equals(event.getPlayer().getWorld())) continue;

            double dist = event.getPlayer().getLocation().distance(dungeonLoc);
            if (dist < 32 && dist > 27) {
                String typeId = entry.getKey().split("_")[0];
                DungeonType type = DungeonType.fromId(typeId);
                if (type == null) {
                    type = DungeonType.fromName(entry.getKey());
                }
                if (type != null) {
                    Player p = event.getPlayer();
                    p.sendMessage(MessageUtils.withPrefix("&6&lNazdike Dungeon Irani shodid: &e" + type.getFinglishName()));
                    p.sendMessage(MessageUtils.withPrefix("&7" + type.getDescription()));
                    p.sendMessage(MessageUtils.color("&cBoss: " + type.getBossName()));
                    p.sendTitle(MessageUtils.color("&6" + type.getFinglishName()), MessageUtils.color("&eBoss: " + type.getBossName()), 20, 60, 20);
                    p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 1.0f, 0.5f);
                }
                break;
            }
        }
    }
}

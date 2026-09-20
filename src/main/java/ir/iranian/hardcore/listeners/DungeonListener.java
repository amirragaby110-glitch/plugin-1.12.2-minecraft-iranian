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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Listener baraye dungeonhaye Irani - v5.0 Finglish (Optimized for 20 TPS on Aternos)
 */
public class DungeonListener implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Set<String> notifiedDungeons = new HashSet<>();

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

        boolean isIranianBoss = name.contains("Hassan Sabbah") || name.contains("Babak") ||
                name.contains("Daryoosh") || name.contains("Anahita") || name.contains("Siraf") ||
                name.contains("Bam") || name.contains("Chogha") || name.contains("Zartosht") ||
                name.contains("Kourosh") || name.contains("Zahhak") || name.contains("Afrasiab");

        if (isIranianBoss) {
            String killerName = (event.getEntity().getKiller() != null) ? event.getEntity().getKiller().getName() : "Yek Delavar";
            for (Player p : event.getEntity().getWorld().getPlayers()) {
                if (p.getLocation().distanceSquared(event.getEntity().getLocation()) < 14400) { // 120 blocks
                    p.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                    p.sendMessage(MessageUtils.withPrefix("&6&lBosse Irani shekast khord!"));
                    p.sendMessage(MessageUtils.withPrefix("&e" + name + " &ftavasote &a" + killerName + " &fkoshte shod!"));
                    p.sendMessage(MessageUtils.color("&6Zende bad delavarane Iran zamin!"));
                    p.sendMessage(MessageUtils.color("&8&l[----------------------------------------]"));
                    p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                }
            }

            event.setDroppedExp(250);

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
        // Fast exit: only check when crossing chunk boundary to ensure 0% TPS overhead
        if (event.getFrom().getBlockX() >> 4 == event.getTo().getBlockX() >> 4 &&
            event.getFrom().getBlockZ() >> 4 == event.getTo().getBlockZ() >> 4) {
            return;
        }

        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        Location playerLoc = p.getLocation();

        for (Map.Entry<String, Location> entry : plugin.getDungeonManager().getGeneratedDungeons().entrySet()) {
            Location dungeonLoc = entry.getValue();
            if (dungeonLoc.getWorld() == null || !dungeonLoc.getWorld().equals(playerLoc.getWorld())) continue;

            String key = uuid.toString() + "_" + entry.getKey();
            if (notifiedDungeons.contains(key)) continue;

            double distSq = playerLoc.distanceSquared(dungeonLoc);
            if (distSq <= 1024) { // within 32 blocks
                notifiedDungeons.add(key);
                String typeId = entry.getKey().split("_")[0];
                DungeonType type = DungeonType.fromId(typeId);
                if (type == null) {
                    type = DungeonType.fromName(entry.getKey());
                }
                if (type != null) {
                    p.sendTitle(MessageUtils.color("&6" + type.getFinglishName()), MessageUtils.color("&eBoss: " + type.getBossName()), 15, 60, 15);
                    p.playSound(playerLoc, Sound.BLOCK_PORTAL_AMBIENT, 1.0f, 0.6f);
                }
                break;
            }
        }
    }
}

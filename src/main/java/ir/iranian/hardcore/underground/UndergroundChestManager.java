package ir.iranian.hardcore.underground;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.currency.AchaemenidCoin;
import ir.iranian.hardcore.items.PersianAuthenticItems;
import ir.iranian.hardcore.items.PersianBossItems;
import ir.iranian.hardcore.items.PersianItems;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Subterranean Exploration & Underground Persian Hidden Chest System.
 * Spawns deep buried treasure vaults (Y < 45) based on depth, distance, and rarity.
 * Keeps surface terrain 100% clean and pristine.
 */
public class UndergroundChestManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();
    private final Set<String> populatedChunks = new HashSet<>();

    public UndergroundChestManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("UndergroundChestManager: Subterranean exploration vaults initialized!");
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {
        Chunk chunk = event.getChunk();
        World world = chunk.getWorld();
        if (world.getEnvironment() != World.Environment.NORMAL) return;

        String key = world.getName() + "_" + chunk.getX() + "_" + chunk.getZ();
        if (populatedChunks.contains(key)) return;
        populatedChunks.add(key);

        // 35% chance per chunk to contain an underground Persian vault
        if (random.nextInt(100) > 35) return;

        int cx = chunk.getX() * 16 + random.nextInt(12) + 2;
        int cz = chunk.getZ() * 16 + random.nextInt(12) + 2;
        int cy = random.nextInt(35) + 10; // Y from 10 to 45

        Block block = world.getBlockAt(cx, cy, cz);
        Block floor = world.getBlockAt(cx, cy - 1, cz);

        // Must be in natural underground stone/cave
        if (floor.getType() == Material.STONE || floor.getType() == Material.COBBLESTONE) {
            spawnUndergroundVault(world, cx, cy, cz);
        }
    }

    private void spawnUndergroundVault(World world, int x, int y, int z) {
        // 3x3 small stone vault with carved reliefs
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                world.getBlockAt(x + dx, y - 1, z + dz).setType(Material.SMOOTH_BRICK);
                world.getBlockAt(x + dx, y, z + dz).setType(Material.AIR);
                world.getBlockAt(x + dx, y + 1, z + dz).setType(Material.AIR);
                world.getBlockAt(x + dx, y + 2, z + dz).setType(Material.SMOOTH_BRICK);
            }
        }

        // Central chest
        Block chestBlock = world.getBlockAt(x, y, z);
        chestBlock.setType(Material.CHEST);
        if (chestBlock.getState() instanceof Chest) {
            Chest chest = (Chest) chestBlock.getState();
            fillUndergroundLoot(chest, y);
        }

        // Atmospheric redstone/glowstone torch
        world.getBlockAt(x + 1, y, z).setType(Material.REDSTONE_TORCH_ON);
    }

    private void fillUndergroundLoot(Chest chest, int depth) {
        chest.getInventory().clear();

        // 1. Imperial Crypt Tier (Y: 5 to 19)
        if (depth < 20) {
            chest.getInventory().addItem(AchaemenidCoin.DERIK_SHAHANSHAHI.create(random.nextInt(2) + 1));
            chest.getInventory().addItem(AchaemenidCoin.DERIK_TALA.create(random.nextInt(6) + 3));
            chest.getInventory().addItem(PersianBossItems.createDivKoshSword());
            chest.getInventory().addItem(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
            if (random.nextBoolean()) {
                chest.getInventory().addItem(PersianAuthenticItems.createSangAkvan());
            }
        }
        // 2. Ancient Silver Tier (Y: 20 to 34)
        else if (depth < 35) {
            chest.getInventory().addItem(AchaemenidCoin.DERIK_TALA.create(random.nextInt(3) + 1));
            chest.getInventory().addItem(AchaemenidCoin.SIGLOS_NOGHRE.create(random.nextInt(8) + 4));
            chest.getInventory().addItem(PersianAuthenticItems.createZaferanQaen());
            chest.getInventory().addItem(PersianItems.createShamshirBabak());
            chest.getInventory().addItem(new ItemStack(Material.EMERALD, random.nextInt(4) + 2));
        }
        // 3. Deep Bronze Tier (Y: 35 to 45)
        else {
            chest.getInventory().addItem(AchaemenidCoin.SIGLOS_NOGHRE.create(random.nextInt(4) + 1));
            chest.getInventory().addItem(AchaemenidCoin.DANAKE_BORONZ.create(random.nextInt(16) + 8));
            chest.getInventory().addItem(PersianAuthenticItems.createPestehRafsanjan());
            chest.getInventory().addItem(new ItemStack(Material.IRON_INGOT, random.nextInt(5) + 2));
        }

        chest.update();
    }
}

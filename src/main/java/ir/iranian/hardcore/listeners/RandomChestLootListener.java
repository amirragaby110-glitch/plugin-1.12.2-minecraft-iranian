package ir.iranian.hardcore.listeners;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.health.PlayerHealthManager;
import ir.iranian.hardcore.items.PersianBossItems;
import ir.iranian.hardcore.items.PersianItems;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

/**
 * Random Natural Chest Loot Listener (v5.3 Finglish)
 * Features:
 * - Natural exploration loot injection into all world-generated chests
 *   (Dungeons, Mineshafts, Desert Temples, Jungle Temples, Nether Fortresses, Strongholds, Villages)
 * - Guarantees players can find Iranian Swords, Persian Food, Sekkeh Derik,
 *   Heart Canisters, and Boss-Slayer items naturally in random chests WITHOUT commands!
 * - Automatically ignores player-placed storage chests so player bases remain untouched.
 */
public class RandomChestLootListener implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();

    // In-memory set of visited chest coordinates to prevent duplicate injection
    private final Set<String> populatedChests = new HashSet<>();

    // Set of player-placed chest coordinates
    private final Set<String> playerPlacedChests = new HashSet<>();

    public RandomChestLootListener(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    private String getCoordKey(Location loc) {
        if (loc == null || loc.getWorld() == null) return "";
        return loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    }

    /**
     * Track chests placed by players so their storage is never modified.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block b = event.getBlockPlaced();
        if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
            String key = getCoordKey(b.getLocation());
            playerPlacedChests.add(key);
            b.setMetadata("player_placed", new FixedMetadataValue(plugin, true));
        }
    }

    /**
     * When any player opens a chest in the world, check if it's a natural/world chest
     * and inject exciting Persian loot!
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();

        Location chestLoc = null;
        if (holder instanceof Chest) {
            chestLoc = ((Chest) holder).getLocation();
        } else if (holder instanceof DoubleChest) {
            chestLoc = ((DoubleChest) holder).getLocation();
        }

        if (chestLoc == null) return;

        String key = getCoordKey(chestLoc);
        if (populatedChests.contains(key) || playerPlacedChests.contains(key)) return;

        Block block = chestLoc.getBlock();
        if (block.hasMetadata("player_placed") || block.hasMetadata("iranian_loot_injected") || containsPersianItem(inv)) {
            populatedChests.add(key);
            return;
        }

        // Mark as populated so it never runs twice on this chest
        populatedChests.add(key);
        block.setMetadata("iranian_loot_injected", new FixedMetadataValue(plugin, true));

        // Inject Persian exploration loot
        boolean addedLoot = false;

        // 1. 65% Chance for Persian Sword
        if (random.nextDouble() < 0.65 && plugin.getSwordsManager() != null) {
            inv.addItem(plugin.getSwordsManager().getRandomSword());
            addedLoot = true;
        }

        // 2. 60% Chance for Persian Food
        if (random.nextDouble() < 0.60 && plugin.getFoodsManager() != null) {
            inv.addItem(plugin.getFoodsManager().getRandomFood());
            addedLoot = true;
        }

        // 3. 75% Chance for Sekkeh Derik Coins
        if (random.nextDouble() < 0.75) {
            int coinCount = random.nextInt(6) + 2;
            for (int i = 0; i < coinCount; i++) {
                inv.addItem(PersianItems.createSekkeHakhamaneshi());
            }
            addedLoot = true;
        }

        // 4. 35% Chance for Permanent Heart Canister (Up to 30 Hearts!)
        if (random.nextDouble() < 0.35) {
            double roll = random.nextDouble();
            if (roll < 0.15) {
                inv.addItem(PlayerHealthManager.createElixirOfLife());
            } else if (roll < 0.60) {
                inv.addItem(PlayerHealthManager.createRedHeartCanister());
            } else {
                inv.addItem(PlayerHealthManager.createTurquoiseHeartCanister());
            }
            addedLoot = true;
        }

        // 5. 30% Chance for Dedicated Boss-Slayer Tactical Items!
        if (random.nextDouble() < 0.30) {
            int r = random.nextInt(5);
            if (r == 0) inv.addItem(PersianBossItems.createDivKoshSword());
            else if (r == 1) inv.addItem(PersianBossItems.createBossPiercerBow());
            else if (r == 2) inv.addItem(PersianBossItems.createNaphthaBomb(random.nextInt(3) + 2));
            else if (r == 3) inv.addItem(PersianBossItems.createDerafshShield());
            else inv.addItem(PersianBossItems.createJamshidTalisman());
            addedLoot = true;
        }

        // 6. 50% Chance for Historic Iranian Relics
        if (random.nextDouble() < 0.50) {
            inv.addItem(PersianItems.getRandomPersianLoot());
            addedLoot = true;
        }

        // Subtle audio-visual discovery feedback for the explorer
        if (addedLoot && event.getPlayer() instanceof Player) {
            Player p = (Player) event.getPlayer();
            p.playSound(chestLoc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.9f, 1.4f);
            MessageUtils.sendActionBar(p, "&6&l[Kashf-e Ghanimat] &eLoot-e Bastani-ye Irani dar in sandogh peyda shod!");
        }
    }

    private boolean containsPersianItem(Inventory inv) {
        if (inv == null || inv.getContents() == null) return false;
        for (ItemStack it : inv.getContents()) {
            if (it == null || !it.hasItemMeta() || !it.getItemMeta().hasDisplayName()) continue;
            String name = it.getItemMeta().getDisplayName();
            if (name.contains("Iran") || name.contains("Kourosh") || name.contains("Derik") ||
                name.contains("Ghalb") || name.contains("Eksir") || name.contains("Div-Kosh") ||
                name.contains("Ghormeh") || name.contains("Dizi") || name.contains("Chai") ||
                name.contains("Alamut") || name.contains("Zolfaghar") || name.contains("Babak")) {
                return true;
            }
        }
        return false;
    }
}

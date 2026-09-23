package ir.iranian.hardcore.currency;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Economy & Coin Manager for Ancient Persian Currency.
 * Handles coin validation, balance checking, safe inventory deduction,
 * and anti-duplication protection.
 */
public class CoinManager {

    private final IranianHardcorePlugin plugin;

    public CoinManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("CoinManager: Achaemenid Currency system initialized!");
    }

    /**
     * Calculates the total value of all Achaemenid coins in player's inventory.
     */
    public long getBalance(Player player) {
        if (player == null) return 0;
        long total = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            AchaemenidCoin coin = AchaemenidCoin.fromItemStack(item);
            if (coin != null) {
                total += coin.getValue() * item.getAmount();
            }
        }
        return total;
    }

    public boolean hasBalance(Player player, long amount) {
        return getBalance(player) >= amount;
    }

    /**
     * Safely deducts coins from player inventory, breaking change down properly.
     */
    public boolean deductBalance(Player player, long amount) {
        if (!hasBalance(player, amount)) return false;

        long balance = getBalance(player);
        long remainingBalance = balance - amount;

        // Clear existing Achaemenid coins from inventory
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && AchaemenidCoin.fromItemStack(item) != null) {
                player.getInventory().setItem(i, null);
            }
        }

        // Re-deposit remaining balance in optimal denominations
        deposit(player, remainingBalance);
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.2f);
        return true;
    }

    /**
     * Deposits an amount of currency into the player's inventory in optimal coin denominations.
     */
    public void deposit(Player player, long amount) {
        if (amount <= 0 || player == null) return;

        long remaining = amount;

        // 1. Royal Daric (1000)
        if (remaining >= 1000) {
            int royals = (int) (remaining / 1000);
            giveCoinStacks(player, AchaemenidCoin.DERIK_SHAHANSHAHI, royals);
            remaining %= 1000;
        }

        // 2. Gold Daric (100)
        if (remaining >= 100) {
            int golds = (int) (remaining / 100);
            giveCoinStacks(player, AchaemenidCoin.DERIK_TALA, golds);
            remaining %= 100;
        }

        // 3. Silver Siglos (10)
        if (remaining >= 10) {
            int silvers = (int) (remaining / 10);
            giveCoinStacks(player, AchaemenidCoin.SIGLOS_NOGHRE, silvers);
            remaining %= 10;
        }

        // 4. Bronze Danake (1)
        if (remaining > 0) {
            giveCoinStacks(player, AchaemenidCoin.DANAKE_BORONZ, (int) remaining);
        }
    }

    private void giveCoinStacks(Player player, AchaemenidCoin coin, int totalAmount) {
        while (totalAmount > 0) {
            int stack = Math.min(64, totalAmount);
            ItemStack item = coin.create(stack);
            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);
            if (!leftover.isEmpty()) {
                for (ItemStack drop : leftover.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), drop);
                }
            }
            totalAmount -= stack;
        }
    }
}

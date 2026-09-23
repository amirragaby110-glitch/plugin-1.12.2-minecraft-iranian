package ir.iranian.hardcore.gui;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.army.Army;
import ir.iranian.hardcore.army.Formation;
import ir.iranian.hardcore.army.Soldier;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Military Army Management GUI.
 */
public class ArmyGUI implements Listener {

    private final IranianHardcorePlugin plugin;

    public ArmyGUI(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openGUI(Player player) {
        if (plugin.getArmyManager() == null) return;
        Army army = plugin.getArmyManager().getArmy(player);
        int maxAllowed = (plugin.getCivilizationManager() != null) ? plugin.getCivilizationManager().getMaxArmySize(player) : 3;

        Inventory inv = Bukkit.createInventory(null, 54, MessageUtils.color("&8[Artesh] &6Modiriat-e Sepah"));

        // Border
        ItemStack border = createItem(Material.STAINED_GLASS_PANE, (byte) 15, " ", null);
        for (int i = 0; i < 9; i++) inv.setItem(i, border);
        for (int i = 45; i < 54; i++) inv.setItem(i, border);

        // Header info
        List<String> infoLore = new ArrayList<>();
        infoLore.add(MessageUtils.color("&7Sarbazan: &e" + army.getSize() + " &7/ &a" + maxAllowed));
        infoLore.add(MessageUtils.color("&7Arayesh: &b" + army.getFormation().getDisplayName()));
        infoLore.add(MessageUtils.color("&7Farman: &d" + army.getGlobalOrder().getLabel()));
        inv.setItem(4, createItem(Material.GOLD_BLOCK, (byte) 0, "&6&lArtesh-e Hakhamaneshi", infoLore));

        // Formation selection buttons
        inv.setItem(45, createItem(Material.SHIELD, (byte) 0, "&e&lArayesh-e Saf (Line)", null));
        inv.setItem(46, createItem(Material.IRON_DOOR, (byte) 0, "&e&lDivareh-ye Defa'i (Wall)", null));
        inv.setItem(47, createItem(Material.ARROW, (byte) 0, "&e&lPeykan-e Noofoozi (Wedge)", null));
        inv.setItem(48, createItem(Material.COMPASS, (byte) 0, "&e&lHalgheh (Circle)", null));

        // Global Order buttons
        inv.setItem(50, createItem(Material.LEASH, (byte) 0, "&a&lFarman: Follow (Peyrovi)", null));
        inv.setItem(51, createItem(Material.BARRIER, (byte) 0, "&c&lFarman: Stay (Istan)", null));
        inv.setItem(52, createItem(Material.IRON_SWORD, (byte) 0, "&4&lFarman: Attack (Hamleh)", null));
        inv.setItem(53, createItem(Material.GOLD_CHESTPLATE, (byte) 0, "&b&lFarman: Guard (Negahbani)", null));

        // Display soldiers
        int slot = 9;
        for (Soldier s : army.getSoldiers()) {
            if (slot >= 45) break;
            List<String> sLore = new ArrayList<>();
            sLore.add(MessageUtils.color("&7Kelas: &e" + s.getSoldierClass().getDisplayName()));
            sLore.add(MessageUtils.color("&7Level: &b" + s.getLevel() + " / 10"));
            sLore.add(MessageUtils.color("&7Jan: &c" + String.format("%.1f", s.getHealth()) + " / " + String.format("%.1f", s.getMaxHealth())));
            sLore.add(MessageUtils.color("&7Damage: &e" + String.format("%.1f", s.getAttackDamage())));
            sLore.add(MessageUtils.color("&7Farman: &d" + s.getOrder().getLabel()));
            sLore.add(MessageUtils.color("&8------------------------"));
            sLore.add(MessageUtils.color("&a[Click Chap] &fErtegha-ye Level (20 Derik)"));
            sLore.add(MessageUtils.color("&c[Click Rast] &fEkhraj va Tarakhiz"));

            inv.setItem(slot, createItem(s.getSoldierClass().getHelmet(), (byte) 0, "&6" + s.getSoldierClass().getDisplayName() + " &a(Lv." + s.getLevel() + ")", sLore));
            slot++;
        }

        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains("Modiriat-e Sepah")) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= 54) return;

        Army army = plugin.getArmyManager().getArmy(player);
        if (army == null) return;

        // Formations
        if (slot == 45) { army.setFormation(Formation.LINE); openGUI(player); }
        else if (slot == 46) { army.setFormation(Formation.WALL); openGUI(player); }
        else if (slot == 47) { army.setFormation(Formation.WEDGE); openGUI(player); }
        else if (slot == 48) { army.setFormation(Formation.CIRCLE); openGUI(player); }

        // Orders
        else if (slot == 50) { army.setGlobalOrder(Soldier.Order.FOLLOW); openGUI(player); }
        else if (slot == 51) { army.setGlobalOrder(Soldier.Order.STAY); openGUI(player); }
        else if (slot == 52) { army.setGlobalOrder(Soldier.Order.ATTACK); openGUI(player); }
        else if (slot == 53) { army.setGlobalOrder(Soldier.Order.GUARD); openGUI(player); }

        // Soldiers slots
        else if (slot >= 9 && slot < 9 + army.getSize()) {
            int idx = slot - 9;
            Soldier s = army.getSoldiers().get(idx);

            if (event.isLeftClick()) {
                // Upgrade
                if (plugin.getCoinManager() != null && plugin.getCoinManager().hasBalance(player, 20)) {
                    if (s.levelUp()) {
                        plugin.getCoinManager().deductBalance(player, 20);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
                        MessageUtils.sendActionBar(player, "&a[Artesh] Sarbaz be Level " + s.getLevel() + " ertegha yaft!");
                        openGUI(player);
                    } else {
                        MessageUtils.sendActionBar(player, "&cSarbaz dar hadaksar-e Level (10) ast!");
                    }
                } else {
                    MessageUtils.sendActionBar(player, "&cSekkeh-ye kafi baraye ertegha nadarid! (Niazmand: 20 Derik)");
                }
            } else if (event.isRightClick()) {
                // Dismiss
                army.removeSoldier(s);
                if (s.getEntity() != null) s.getEntity().remove();
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                MessageUtils.sendActionBar(player, "&e[Artesh] Sarbaz az sepah tarakhiz shod.");
                openGUI(player);
            }
        }
    }

    private ItemStack createItem(Material mat, byte data, String name, List<String> lore) {
        if (mat == null) mat = Material.STICK;
        ItemStack item = new ItemStack(mat, 1, (short) 0, data);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.color(name));
            if (lore != null) meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}

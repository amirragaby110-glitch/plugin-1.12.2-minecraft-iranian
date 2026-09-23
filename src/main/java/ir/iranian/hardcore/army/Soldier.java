package ir.iranian.hardcore.army;

import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Active Persian Military Soldier Instance.
 */
public class Soldier {

    public enum Order {
        FOLLOW("Peyrovi (Follow)"),
        STAY("Istan (Stay)"),
        GUARD("Negahbani (Guard)"),
        ATTACK("Hamleh (Attack)"),
        DEFEND("Defa' (Defend)");

        private final String label;
        Order(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private final UUID soldierId;
    private final UUID ownerId;
    private final SoldierClass soldierClass;
    private int level;
    private double health;
    private Order order;
    private Location guardLocation;
    private transient LivingEntity entity;

    public Soldier(UUID soldierId, UUID ownerId, SoldierClass soldierClass, int level, double health, Order order) {
        this.soldierId = soldierId;
        this.ownerId = ownerId;
        this.soldierClass = soldierClass;
        this.level = Math.max(1, Math.min(10, level));
        this.health = (health <= 0) ? getMaxHealth() : health;
        this.order = (order == null) ? Order.FOLLOW : order;
    }

    public UUID getSoldierId() { return soldierId; }
    public UUID getOwnerId() { return ownerId; }
    public SoldierClass getSoldierClass() { return soldierClass; }
    public int getLevel() { return level; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Location getGuardLocation() { return guardLocation; }
    public void setGuardLocation(Location guardLocation) { this.guardLocation = guardLocation; }

    public double getMaxHealth() {
        return soldierClass.getBaseHealth() * (1.0 + (level - 1) * 0.15);
    }

    public double getAttackDamage() {
        return soldierClass.getBaseDamage() * (1.0 + (level - 1) * 0.12);
    }

    public double getHealth() {
        if (entity != null && !entity.isDead()) {
            return entity.getHealth();
        }
        return health;
    }

    public void setHealth(double health) {
        this.health = Math.max(0, Math.min(getMaxHealth(), health));
        if (entity != null && !entity.isDead()) {
            entity.setHealth(this.health);
        }
    }

    public LivingEntity getEntity() { return entity; }

    public void bindEntity(LivingEntity entity) {
        this.entity = entity;
        if (entity == null) return;

        // Apply health attribute
        if (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        }
        entity.setHealth(Math.min(getMaxHealth(), health));

        // Speed
        if (entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
            entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(soldierClass.getBaseSpeed());
        }

        // Display Name
        entity.setCustomName(MessageUtils.color("&6[Artesh] &e" + soldierClass.getDisplayName() + " &a(Lv." + level + ")"));
        entity.setCustomNameVisible(true);

        // Equip Armors & Weapons
        EntityEquipment eq = entity.getEquipment();
        if (eq != null) {
            if (soldierClass.getHelmet() != null) eq.setHelmet(new ItemStack(soldierClass.getHelmet()));
            if (soldierClass.getChestplate() != null) eq.setChestplate(new ItemStack(soldierClass.getChestplate()));
            if (soldierClass.getLeggings() != null) eq.setLeggings(new ItemStack(soldierClass.getLeggings()));
            if (soldierClass.getBoots() != null) eq.setBoots(new ItemStack(soldierClass.getBoots()));
            if (soldierClass.getMainHand() != null) eq.setItemInMainHand(new ItemStack(soldierClass.getMainHand()));
            if (soldierClass.getOffHand() != null) eq.setItemInOffHand(new ItemStack(soldierClass.getOffHand()));

            eq.setHelmetDropChance(0.0f);
            eq.setChestplateDropChance(0.0f);
            eq.setLeggingsDropChance(0.0f);
            eq.setBootsDropChance(0.0f);
            eq.setItemInMainHandDropChance(0.0f);
            eq.setItemInOffHandDropChance(0.0f);
        }

        // Prevent despawn
        entity.setRemoveWhenFarAway(false);
    }

    public boolean levelUp() {
        if (level >= 10) return false;
        level++;
        this.health = getMaxHealth();
        if (entity != null && !entity.isDead()) {
            bindEntity(entity);
        }
        return true;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("soldierId", soldierId.toString());
        map.put("ownerId", ownerId.toString());
        map.put("class", soldierClass.name());
        map.put("level", level);
        map.put("health", getHealth());
        map.put("order", order.name());
        return map;
    }

    public static Soldier deserialize(Map<String, Object> map) {
        UUID sId = UUID.fromString((String) map.get("soldierId"));
        UUID oId = UUID.fromString((String) map.get("ownerId"));
        SoldierClass sClass = SoldierClass.valueOf((String) map.get("class"));
        int lvl = ((Number) map.get("level")).intValue();
        double hp = ((Number) map.get("health")).doubleValue();
        Order ord = Order.valueOf((String) map.get("order"));
        return new Soldier(sId, oId, sClass, lvl, hp, ord);
    }
}

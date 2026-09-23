package ir.iranian.hardcore.army;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.currency.AchaemenidCoin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Ancient Persian Army & Soldier Management Controller.
 * Handles Villager-to-Soldier conversion, tactical formations,
 * efficient scheduled combat AI, persistence, and anti-duplication protection.
 */
public class ArmyManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Map<UUID, Army> playerArmies = new HashMap<>();
    private final Map<UUID, Soldier> entitySoldierMap = new HashMap<>();
    private File armyFile;
    private FileConfiguration armyConfig;

    public ArmyManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        loadData();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startArmyAITask();
        plugin.getLogger().info("ArmyManager: Persian Military & Soldier System initialized!");
    }

    public Army getArmy(Player player) {
        if (player == null) return null;
        return getArmy(player.getUniqueId());
    }

    public Army getArmy(UUID ownerId) {
        return playerArmies.computeIfAbsent(ownerId, Army::new);
    }

    // ==========================================
    // 1. Villager Conversion to Soldier
    // ==========================================
    @EventHandler(priority = EventPriority.HIGH)
    public void onVillagerConvert(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) return;
        Entity clicked = event.getRightClicked();
        if (!(clicked instanceof Villager)) return;

        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        AchaemenidCoin coin = AchaemenidCoin.fromItemStack(hand);
        if (coin == null) return;

        Villager villager = (Villager) clicked;
        if (!villager.isValid() || villager.isDead() || villager.isBaby()) return;

        // Check if already a soldier
        if (entitySoldierMap.containsKey(villager.getUniqueId())) {
            MessageUtils.sendActionBar(player, "&cIn fard ghablan be khedmat-e artesh dar amadeh ast!");
            return;
        }

        Army army = getArmy(player);
        int maxAllowed = 3;
        if (plugin.getCivilizationManager() != null) {
            maxAllowed = plugin.getCivilizationManager().getMaxArmySize(player);
        }

        if (army.getSize() >= maxAllowed) {
            MessageUtils.sendActionBar(player, "&cZarfiat-e artesh por ast! (" + army.getSize() + "/" + maxAllowed + ") - Tamaddon ra ertegha dahid!");
            return;
        }

        // Consume 1 Coin
        if (hand.getAmount() > 1) {
            hand.setAmount(hand.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }

        // Cancel default villager trading UI
        event.setCancelled(true);

        Location spawnLoc = villager.getLocation();
        villager.remove(); // Safely remove original villager

        // Determine Soldier Class based on coin type or player preference
        SoldierClass sClass = SoldierClass.PERSIAN_GUARD;
        if (coin == AchaemenidCoin.DERIK_SHAHANSHAHI) {
            sClass = SoldierClass.ROYAL_GUARD;
        } else if (coin == AchaemenidCoin.SIGLOS_NOGHRE) {
            sClass = SoldierClass.ARCHER;
        } else if (coin == AchaemenidCoin.DANAKE_BORONZ) {
            sClass = SoldierClass.SPEARMAN;
        }

        Soldier soldier = new Soldier(UUID.randomUUID(), player.getUniqueId(), sClass, 1, 0, army.getGlobalOrder());
        army.addSoldier(soldier);
        spawnSoldierEntity(soldier, spawnLoc);

        // Visual & Audio fanfare
        player.getWorld().playSound(spawnLoc, Sound.ENTITY_PLAYER_LEVELUP, 1.2f, 1.0f);
        player.getWorld().playSound(spawnLoc, Sound.ITEM_ARMOR_EQUIP_IRON, 1.5f, 1.0f);
        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, spawnLoc.add(0, 1, 0), 25, 0.5, 0.8, 0.5, 0.1);

        player.sendMessage(MessageUtils.color("&8[Artesh] &aTabrik! Villager ba &e" + coin.getDisplayName() + " &abe khedmat-e artesh dar amad!"));
        player.sendMessage(MessageUtils.color("&8[Artesh] &7Kelas: &e" + sClass.getDisplayName() + " &7| Level: &b1 &7| Zarfiat: &a" + army.getSize() + "/" + maxAllowed));
        saveData();
    }

    public void spawnSoldierEntity(Soldier soldier, Location loc) {
        World world = loc.getWorld();
        if (world == null) return;

        // Spawn custom Zombie/Skeleton biped for 1.12.2 combat
        Zombie entity = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
        entity.setBaby(false);
        entity.setVillager(false);

        soldier.bindEntity(entity);
        entitySoldierMap.put(entity.getUniqueId(), soldier);
    }

    // Prevent Soldiers from burning in sunlight
    @EventHandler(priority = EventPriority.LOW)
    public void onCombust(EntityCombustEvent event) {
        if (entitySoldierMap.containsKey(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    // Soldier Death
    @EventHandler
    public void onSoldierDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Soldier soldier = entitySoldierMap.remove(entity.getUniqueId());
        if (soldier == null) return;

        event.getDrops().clear();
        event.setDroppedExp(0);

        Player owner = Bukkit.getPlayer(soldier.getOwnerId());
        if (owner != null && owner.isOnline()) {
            owner.playSound(owner.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1.0f, 1.0f);
            owner.sendMessage(MessageUtils.color("&c[Artesh] Sarbaz-e shoma (" + soldier.getSoldierClass().getDisplayName() + ") dar nabard jan bakht!"));
        }

        Army army = playerArmies.get(soldier.getOwnerId());
        if (army != null) {
            army.removeSoldier(soldier);
        }
        saveData();
    }

    // Friendly Fire Protection & Defend Owner
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCombat(EntityDamageByEntityEvent event) {
        Soldier damagedSoldier = entitySoldierMap.get(event.getEntity().getUniqueId());

        // Don't let owner hurt own soldier
        if (damagedSoldier != null && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (damager.getUniqueId().equals(damagedSoldier.getOwnerId())) {
                event.setCancelled(true);
                return;
            }
        }

        // When soldier attacks
        Soldier attackingSoldier = entitySoldierMap.get(event.getDamager().getUniqueId());
        if (attackingSoldier != null) {
            // Apply custom damage scaling
            event.setDamage(attackingSoldier.getAttackDamage());

            // Never attack owner
            if (event.getEntity().getUniqueId().equals(attackingSoldier.getOwnerId())) {
                event.setCancelled(true);
                return;
            }
        }

        // When owner is attacked, alert soldiers to retaliate
        if (event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity) {
            Player player = (Player) event.getEntity();
            Army army = playerArmies.get(player.getUniqueId());
            if (army != null) {
                LivingEntity attacker = (LivingEntity) event.getDamager();
                for (Soldier s : army.getSoldiers()) {
                    if (s.getOrder() == Soldier.Order.FOLLOW || s.getOrder() == Soldier.Order.DEFEND) {
                        if (s.getEntity() instanceof Creature) {
                            ((Creature) s.getEntity()).setTarget(attacker);
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // 2. High-Performance AI & Formation Task
    // ==========================================
    private void startArmyAITask() {
        // Runs once every 20 ticks (1.0 sec) - Low CPU footprint
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Army army = playerArmies.get(player.getUniqueId());
                    if (army == null || army.getSize() == 0) continue;

                    List<Soldier> soldiers = army.getSoldiers();
                    int total = soldiers.size();
                    Formation formation = army.getFormation();
                    Location playerLoc = player.getLocation();

                    for (int i = 0; i < total; i++) {
                        Soldier s = soldiers.get(i);
                        LivingEntity entity = s.getEntity();
                        if (entity == null || entity.isDead() || !entity.isValid()) continue;

                        if (s.getOrder() == Soldier.Order.FOLLOW) {
                            Location target = formation.calculatePosition(playerLoc, i, total);
                            double dist = entity.getLocation().distance(playerLoc);

                            // Teleport if too far
                            if (dist > 35.0) {
                                entity.teleport(target != null ? target : playerLoc);
                            } else if (dist > 3.5 && target != null) {
                                if (entity instanceof Creature) {
                                    Creature c = (Creature) entity;
                                    // Target nearby hostile mob if in combat
                                    if (c.getTarget() == null || c.getTarget().isDead()) {
                                        scanHostileTargets(c, 10.0);
                                        c.getEquipment(); // keep loaded
                                    }
                                    // Move to formation
                                    Vector v = target.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(0.3);
                                    v.setY(entity.getVelocity().getY());
                                    entity.setVelocity(v);
                                }
                            }
                        } else if (s.getOrder() == Soldier.Order.GUARD) {
                            if (entity instanceof Creature) {
                                Creature c = (Creature) entity;
                                if (c.getTarget() == null || c.getTarget().isDead()) {
                                    scanHostileTargets(c, 12.0);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 40L, 20L);
    }

    private void scanHostileTargets(Creature creature, double radius) {
        for (Entity e : creature.getNearbyEntities(radius, 4.0, radius)) {
            if (e instanceof Monster && !entitySoldierMap.containsKey(e.getUniqueId())) {
                creature.setTarget((LivingEntity) e);
                break;
            }
        }
    }

    // ==========================================
    // 3. Player Login / Logout Safety & Anti-Exploit
    // ==========================================
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Army army = playerArmies.get(player.getUniqueId());
        if (army != null) {
            // Despawn active entities to prevent chunk leaks & duplicate clones
            for (Soldier s : army.getSoldiers()) {
                if (s.getEntity() != null) {
                    s.setHealth(s.getEntity().getHealth());
                    entitySoldierMap.remove(s.getEntity().getUniqueId());
                    s.getEntity().remove();
                }
            }
        }
        saveData();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Army army = playerArmies.get(player.getUniqueId());
        if (army != null && army.getSize() > 0) {
            Location pLoc = player.getLocation();
            for (Soldier s : army.getSoldiers()) {
                spawnSoldierEntity(s, pLoc);
            }
            player.sendMessage(MessageUtils.color("&8[Artesh] &a" + army.getSize() + " sarbaz-e shoma amadeh-ye khedmat hastand!"));
        }
    }

    // ==========================================
    // 4. Persistence Data Storage
    // ==========================================
    private void loadData() {
        armyFile = new File(plugin.getDataFolder(), "armies.yml");
        if (!armyFile.exists()) {
            armyFile.getParentFile().mkdirs();
            try { armyFile.createNewFile(); } catch (IOException ignored) {}
        }
        armyConfig = YamlConfiguration.loadConfiguration(armyFile);

        if (armyConfig.contains("armies")) {
            for (String pId : armyConfig.getConfigurationSection("armies").getKeys(false)) {
                try {
                    UUID ownerId = UUID.fromString(pId);
                    Army army = new Army(ownerId);
                    String formStr = armyConfig.getString("armies." + pId + ".formation", "LINE");
                    army.setFormation(Formation.valueOf(formStr));

                    if (armyConfig.contains("armies." + pId + ".soldiers")) {
                        for (String sKey : armyConfig.getConfigurationSection("armies." + pId + ".soldiers").getKeys(false)) {
                            Map<String, Object> map = armyConfig.getConfigurationSection("armies." + pId + ".soldiers." + sKey).getValues(false);
                            Soldier soldier = Soldier.deserialize(map);
                            army.addSoldier(soldier);
                        }
                    }
                    playerArmies.put(ownerId, army);
                } catch (Exception ignored) {}
            }
        }
    }

    public void saveData() {
        if (armyFile == null || armyConfig == null) return;
        for (Map.Entry<UUID, Army> entry : playerArmies.entrySet()) {
            String pKey = entry.getKey().toString();
            Army army = entry.getValue();
            armyConfig.set("armies." + pKey + ".formation", army.getFormation().name());

            int idx = 0;
            armyConfig.set("armies." + pKey + ".soldiers", null); // clear previous
            for (Soldier s : army.getSoldiers()) {
                armyConfig.createSection("armies." + pKey + ".soldiers.s" + idx, s.serialize());
                idx++;
            }
        }
        try { armyConfig.save(armyFile); } catch (IOException ignored) {}
    }
}

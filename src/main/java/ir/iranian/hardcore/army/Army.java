package ir.iranian.hardcore.army;

import java.util.*;

/**
 * Player Military Army Representation.
 */
public class Army {

    private final UUID ownerId;
    private final List<Soldier> soldiers = new ArrayList<>();
    private Formation formation = Formation.LINE;
    private Soldier.Order globalOrder = Soldier.Order.FOLLOW;

    public Army(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getOwnerId() { return ownerId; }
    public List<Soldier> getSoldiers() { return Collections.unmodifiableList(soldiers); }
    public int getSize() { return soldiers.size(); }
    public Formation getFormation() { return formation; }
    public void setFormation(Formation formation) { this.formation = formation; }
    public Soldier.Order getGlobalOrder() { return globalOrder; }

    public void setGlobalOrder(Soldier.Order order) {
        this.globalOrder = order;
        for (Soldier s : soldiers) {
            s.setOrder(order);
        }
    }

    public void addSoldier(Soldier soldier) {
        if (!soldiers.contains(soldier)) {
            soldiers.add(soldier);
        }
    }

    public void removeSoldier(Soldier soldier) {
        soldiers.remove(soldier);
    }

    public Soldier getSoldier(UUID soldierId) {
        for (Soldier s : soldiers) {
            if (s.getSoldierId().equals(soldierId)) {
                return s;
            }
        }
        return null;
    }

    public Soldier getSoldierByEntity(org.bukkit.entity.Entity entity) {
        if (entity == null) return null;
        for (Soldier s : soldiers) {
            if (s.getEntity() != null && s.getEntity().getUniqueId().equals(entity.getUniqueId())) {
                return s;
            }
        }
        return null;
    }
}

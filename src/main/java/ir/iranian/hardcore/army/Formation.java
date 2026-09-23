package ir.iranian.hardcore.army;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Military Formation Calculator.
 * Efficient trigonometry to determine soldier target positions relative to the commander.
 */
public enum Formation {

    LINE("Khat-e Saf-keshi (Line)"),
    WALL("Divareh-ye Defa'i (Shield Wall)"),
    WEDGE("Peykan-e Noofoozi (V-Wedge)"),
    CIRCLE("Halgheh-ye Mohasereh (Circle)"),
    DEFENSIVE("Moraba'-e Jangari (Defensive Box)");

    private final String displayName;

    Formation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Location calculatePosition(Location commanderLoc, int index, int totalSoldiers) {
        if (commanderLoc == null) return null;
        Location loc = commanderLoc.clone();
        Vector dir = commanderLoc.getDirection().setY(0).normalize();
        Vector right = new Vector(-dir.getZ(), 0, dir.getX()).normalize();

        double offsetX = 0;
        double offsetZ = 0;

        switch (this) {
            case LINE: {
                // Line abreast, 2.5 blocks behind commander
                double spread = (index - (totalSoldiers - 1) / 2.0) * 1.8;
                Vector pos = dir.clone().multiply(-2.5).add(right.clone().multiply(spread));
                return loc.add(pos);
            }
            case WALL: {
                // Shield wall, 3.0 blocks in FRONT of commander
                double spread = (index - (totalSoldiers - 1) / 2.0) * 1.5;
                Vector pos = dir.clone().multiply(3.0).add(right.clone().multiply(spread));
                return loc.add(pos);
            }
            case WEDGE: {
                // V-shaped arrow pointing forward
                int side = (index % 2 == 0) ? 1 : -1;
                int rank = (index + 1) / 2;
                Vector pos = dir.clone().multiply(-rank * 1.5).add(right.clone().multiply(side * rank * 1.5));
                return loc.add(pos);
            }
            case CIRCLE: {
                // Perimeter circle around commander
                double angle = (2 * Math.PI / Math.max(1, totalSoldiers)) * index;
                double radius = Math.max(3.0, totalSoldiers * 0.45);
                double x = Math.cos(angle) * radius;
                double z = Math.sin(angle) * radius;
                return loc.add(x, 0, z);
            }
            case DEFENSIVE:
            default: {
                // Tight box formation (2 rows)
                int row = index % 2;
                int col = index / 2;
                double forward = (row == 0) ? -2.0 : -4.0;
                double side = (col - 1.5) * 1.8;
                Vector pos = dir.clone().multiply(forward).add(right.clone().multiply(side));
                return loc.add(pos);
            }
        }
    }
}

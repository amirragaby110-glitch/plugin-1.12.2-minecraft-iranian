package ir.iranian.hardcore.structures;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.currency.AchaemenidCoin;
import ir.iranian.hardcore.items.PersianAuthenticItems;
import ir.iranian.hardcore.items.PersianBossItems;
import ir.iranian.hardcore.items.PersianItems;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Monumental Ancient Persian Architectural Wonders & Exploration Structures (v5.4).
 * True-to-scale historical architecture:
 * 1. Takht-e Jamshid (Persepolis Apadana Palace - 40x40 Hypostyle Hall)
 * 2. Aramgah-e Kourosh-e Bozorg (Tomb of Cyrus the Great at Pasargadae - 6-tier plinth)
 * 3. Karvansara-ye Shah Abbasi (36x36 4-Iwan Fortress with Corner Towers & Courtyard Pool)
 * 4. Ab-Anbar-e Yazdi (Deep subterranean reservoir with 4 monumental Badgirs)
 * 5. Atashkadeh-ye Sasanian (Chahar-Taq dome sanctuary with eternal flame altar)
 * 6. Zoorkhaneh-ye Bastani (Grand octagonal brick dome, sunken pit, Sardam bell)
 * 7. Qanat-e Kavir (Subterranean aqueduct network connecting multiple wells)
 * 8. Bazar-e Sonnati (Vaulted brick bazaar with Timcheh dome and 6 merchant stalls)
 * 9. Chaykhaneh-ye Sonnati (Traditional Persian teahouse with takht and samovar)
 * 10. Ashiyaneh-ye Simurgh (Mountaintop sanctuary with sacred flame)
 */
public class PersianStructures {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();
    private final Set<String> generatedRegions = new HashSet<>();

    public PersianStructures(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    public enum StructureType {
        PERSEPOLIS("Takht-e Jamshid (Apadana)", "Persepolis Palace", Material.QUARTZ_BLOCK, "Kakh-e shokoohmand-e Daryoosh ba sotoon-haye azim"),
        CYRUS_TOMB("Aramgah-e Kourosh-e Bozorg", "Tomb of Cyrus the Great", Material.SANDSTONE, "Bana-ye pelekani-ye Pasargad ba otagh-e sangi"),
        CARAVANSERAI("Karvansara-ye Shah Abbasi", "Shah Abbasi Caravanserai", Material.SMOOTH_BRICK, "Dezh-e 4-Iwani-ye Jadeh-ye Abrisham ba 4 borj"),
        AB_ANBAR("Ab-Anbar-e Yazdi", "Yazd Water Cistern", Material.BRICK, "Ab-anbar-e amigh-e kaviri ba 4 badgir-e boland"),
        ATASHKADEH("Atashkadeh-ye Sasanian", "Sassanid Fire Temple", Material.NETHERRACK, "Chahar-taq-e gonbadi ba atash-e Bahram"),
        ZOORKHANEH("Zoorkhaneh-ye Bastani", "Ancient Persian Zoorkhaneh", Material.LOG, "Gonbad-e ajorin, goud-e hasht-zeli va sardam-e morshed"),
        QANAT("Qanat-e Kavir", "Subterranean Persian Aqueduct", Material.SMOOTH_BRICK, "Karez-e zirzamini ba aab-e khonak va govara"),
        BAZAAR("Bazar-e Sonnati", "Persian Grand Bazaar", Material.EMERALD_BLOCK, "Bazar-e ajorin ba timcheh va hojreh-haye tejarat"),
        CHAIKHANEH("Chaykhaneh-ye Sonnati", "Traditional Persian Teahouse", Material.WOOD, "Chaykhaneh ba samavar, takht va farsh-e Kermani"),
        SIMURGH_PEAK("Ashiyaneh-ye Simurgh", "Simurgh Mountain Peak", Material.GOLD_BLOCK, "Ashiyaneh-ye morgh-e afsaneh-i ba par-e jadooyi"),
        VILLAGE("Roosta-ye Irani", "Persian Village", Material.WOOD, "Roosta-ye sonnati ba mazrae va kadkhoda"),
        MOSQUE("Masjed-e Jame", "Grand Jameh Mosque", Material.LAPIS_BLOCK, "Masjed-e jame ba gonbad-e firooze-i va minaret");

        private final String persianName;
        private final String englishName;
        private final Material icon;
        private final String desc;

        StructureType(String persianName, String englishName, Material icon, String desc) {
            this.persianName = persianName;
            this.englishName = englishName;
            this.icon = icon;
            this.desc = desc;
        }

        public String getPersianName() { return persianName; }
        public String getEnglishName() { return englishName; }
        public Material getIcon() { return icon; }
        public String getDesc() { return desc; }
    }

    public boolean forceBuildStructure(Location origin, StructureType type) {
        origin = findGround(origin);
        if (origin == null) return false;

        switch (type) {
            case PERSEPOLIS: return buildPersepolis(origin);
            case CYRUS_TOMB: return buildCyrusTomb(origin);
            case CARAVANSERAI: return buildCaravanserai(origin);
            case AB_ANBAR: return buildAbAnbar(origin);
            case ATASHKADEH: return buildFireTemple(origin);
            case ZOORKHANEH: return buildZoorkhaneh(origin);
            case QANAT: return buildQanat(origin);
            case BAZAAR: return buildBazaar(origin);
            case CHAIKHANEH: return buildChaikhaneh(origin);
            case SIMURGH_PEAK: return buildSimurghPeak(origin);
            case VILLAGE: return buildBazaar(origin);
            case MOSQUE: return buildFireTemple(origin);
            default: return buildCaravanserai(origin);
        }
    }

    // ==========================================
    // 1. TAKHT-E JAMSHID (PERSEPOLIS APADANA PALACE)
    // ==========================================
    public boolean buildPersepolis(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        // Monumental Stone Platform (40x40, 4 blocks high)
        fill(w, ox - 20, oy - 4, oz - 20, ox + 20, oy, oz + 20, Material.SMOOTH_BRICK);
        clearArea(w, ox - 20, oy + 1, oz - 20, ox + 20, oy + 22, oz + 20);

        // Grand Dual Symmetrical Staircases at front (Z = +20)
        for (int step = 0; step < 4; step++) {
            fill(w, ox - 14 + step, oy - 3 + step, oz + 21 + step, ox - 8, oy - 3 + step, oz + 21 + step, Material.SMOOTH_BRICK);
            fill(w, ox + 8, oy - 3 + step, oz + 21 + step, ox + 14 - step, oy - 3 + step, oz + 21 + step, Material.SMOOTH_BRICK);
        }

        // 16 Monumental Fluted Columns (4x4 grid of columns, 14 blocks tall)
        int[] colCoords = {-12, -4, 4, 12};
        for (int cx : colCoords) {
            for (int cz : colCoords) {
                // Column base
                fill(w, ox + cx - 1, oy + 1, oz + cz - 1, ox + cx + 1, oy + 1, oz + cz + 1, Material.QUARTZ_BLOCK);
                // Shaft
                for (int y = 2; y <= 13; y++) {
                    w.getBlockAt(ox + cx, oy + y, oz + cz).setType(Material.QUARTZ_BLOCK);
                }
                // Double-Bull Capital
                fill(w, ox + cx - 1, oy + 14, oz + cz - 1, ox + cx + 1, oy + 14, oz + cz + 1, Material.QUARTZ_BLOCK);
                w.getBlockAt(ox + cx - 1, oy + 15, oz + cz).setType(Material.QUARTZ_BLOCK);
                w.getBlockAt(ox + cx + 1, oy + 15, oz + cz).setType(Material.QUARTZ_BLOCK);
            }
        }

        // Entablature and Wooden Cedar Beam Roof
        fill(w, ox - 15, oy + 16, oz - 15, ox + 15, oy + 16, oz + 15, Material.WOOD);
        for (int x = -15; x <= 15; x += 2) {
            fill(w, x + ox, oy + 17, oz - 15, x + ox, oy + 17, oz + 15, Material.LOG);
        }

        // Central Throne of Darius the Great (Takht-e Shahanshahi)
        fill(w, ox - 3, oy + 1, oz - 3, ox + 3, oy + 2, oz + 3, Material.GOLD_BLOCK);
        w.getBlockAt(ox, oy + 3, oz).setType(Material.GOLD_BLOCK);
        w.getBlockAt(ox, oy + 4, oz).setType(Material.CARPET);
        try { w.getBlockAt(ox, oy + 4, oz).setData((byte) 14); } catch (Exception ignored) {}

        // Red Carpet aisle from front to throne
        for (int z = -2; z <= 18; z++) {
            w.getBlockAt(ox - 1, oy + 1, oz + z).setType(Material.CARPET);
            w.getBlockAt(ox, oy + 1, oz + z).setType(Material.CARPET);
            w.getBlockAt(ox + 1, oy + 1, oz + z).setType(Material.CARPET);
            try {
                w.getBlockAt(ox - 1, oy + 1, oz + z).setData((byte) 14);
                w.getBlockAt(ox, oy + 1, oz + z).setData((byte) 14);
                w.getBlockAt(ox + 1, oy + 1, oz + z).setData((byte) 14);
            } catch (Exception ignored) {}
        }

        // Imperial Braziers flanking throne
        w.getBlockAt(ox - 4, oy + 1, oz).setType(Material.NETHERRACK);
        w.getBlockAt(ox - 4, oy + 2, oz).setType(Material.FIRE);
        w.getBlockAt(ox + 4, oy + 1, oz).setType(Material.NETHERRACK);
        w.getBlockAt(ox + 4, oy + 2, oz).setType(Material.FIRE);

        // Subterranean Apadana Treasure Crypt (Under the platform)
        fill(w, ox - 6, oy - 3, oz - 6, ox + 6, oy - 1, oz + 6, Material.AIR);
        fill(w, ox - 6, oy - 4, oz - 6, ox + 6, oy - 4, oz + 6, Material.SMOOTH_BRICK);

        // Secret entrance ladder down to vault
        w.getBlockAt(ox + 12, oy + 1, oz - 12).setType(Material.AIR);
        for (int y = -3; y <= 0; y++) {
            w.getBlockAt(ox + 12, oy + y, oz - 12).setType(Material.AIR);
            w.getBlockAt(ox + 12, oy + y, oz - 11).setType(Material.LADDER);
        }

        // Imperial Vault Treasure Chests
        setImperialChest(w, ox - 2, oy - 3, oz - 2);
        setImperialChest(w, ox + 2, oy - 3, oz - 2);

        // Immortals Royal Guards
        spawnGuard(w, ox - 2, oy + 1, oz - 1, "&6&lGord-e Javidan-e Kourosh");
        spawnGuard(w, ox + 2, oy + 1, oz - 1, "&6&lGord-e Javidan-e Daryoosh");

        return true;
    }

    // ==========================================
    // 2. ARAMGAH-E KOUROSH-E BOZORG (PASARGADAE)
    // ==========================================
    public boolean buildCyrusTomb(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        clearArea(w, ox - 14, oy + 1, oz - 14, ox + 14, oy + 20, oz + 14);

        // Exact 6 stepped plinth tiers rising towards the tomb chamber
        int[][] tiers = {
            {12, 10, 1}, // Tier 1: 24x20, 1 block tall
            {10, 8, 2},  // Tier 2: 20x16, 2 blocks tall
            {9, 7, 3},   // Tier 3
            {8, 6, 4},   // Tier 4
            {7, 5, 5},   // Tier 5
            {6, 4, 6}    // Tier 6: Base for chamber
        };

        for (int[] t : tiers) {
            int rx = t[0], rz = t[1], y = t[2];
            fill(w, ox - rx, oy + y, oz - rz, ox + rx, oy + y, oz + rz, Material.SANDSTONE);
        }

        // Tomb Chamber (Plinth level 7 to 11)
        int cy = oy + 7;
        fill(w, ox - 4, cy, oz - 3, ox + 4, cy + 4, oz + 3, Material.SANDSTONE);
        // Interior vault
        fill(w, ox - 2, cy + 1, oz - 2, ox + 2, cy + 3, oz + 2, Material.AIR);

        // Gabled Stone Roof (Saghf-e Shervani-ye Pasargad)
        fill(w, ox - 4, cy + 5, oz - 3, ox + 4, cy + 5, oz + 3, Material.SANDSTONE);
        fill(w, ox - 3, cy + 6, oz - 3, ox + 3, cy + 6, oz + 3, Material.SANDSTONE);
        fill(w, ox - 2, cy + 7, oz - 3, ox + 2, cy + 7, oz + 3, Material.SANDSTONE);
        fill(w, ox - 1, cy + 8, oz - 3, ox + 1, cy + 8, oz + 3, Material.SANDSTONE);

        // Chamber Entrance Door (Low opening facing North)
        w.getBlockAt(ox, cy + 1, oz + 3).setType(Material.AIR);
        w.getBlockAt(ox, cy + 2, oz + 3).setType(Material.AIR);

        // Inside Chamber: Sarcophagus of Cyrus the Great
        w.getBlockAt(ox, cy + 1, oz).setType(Material.GOLD_BLOCK);
        w.getBlockAt(ox, cy + 1, oz - 1).setType(Material.GOLD_BLOCK);

        // Chest containing Cyrus Cylinder (Manshur-e Kourosh) and Royal Darics
        Block chestBlock = w.getBlockAt(ox, cy + 2, oz);
        chestBlock.setType(Material.CHEST);
        if (chestBlock.getState() instanceof Chest) {
            Chest c = (Chest) chestBlock.getState();
            c.getInventory().addItem(PersianItems.createManshurKourosh());
            c.getInventory().addItem(PersianItems.createTajKourosh());
            c.getInventory().addItem(AchaemenidCoin.DERIK_SHAHANSHAHI.create(3));
            c.getInventory().addItem(AchaemenidCoin.DERIK_TALA.create(16));
            c.update();
        }

        // Perimeter Sacred Columns (Stylobate of Pasargadae)
        int[] px = {-12, 12};
        int[] pz = {-12, 0, 12};
        for (int x : px) {
            for (int z : pz) {
                for (int y = 1; y <= 6; y++) {
                    w.getBlockAt(ox + x, oy + y, oz + z).setType(Material.SANDSTONE);
                }
                w.getBlockAt(ox + x, oy + 7, oz + z).setType(Material.TORCH);
            }
        }

        return true;
    }

    // ==========================================
    // 3. KARVANSARA-YE SHAH ABBASI (FORTRESS)
    // ==========================================
    public boolean buildCaravanserai(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        // 36x36 Quadrangle Fortified Caravanserai
        fill(w, ox - 18, oy - 3, oz - 18, ox + 18, oy, oz + 18, Material.SMOOTH_BRICK);
        clearArea(w, ox - 18, oy + 1, oz - 18, ox + 18, oy + 14, oz + 18);

        // Outer Fortress Walls (6 blocks high with battlements)
        buildWallFrame(w, ox - 18, oz - 18, ox + 18, oz + 18, oy + 1, oy + 6, Material.SMOOTH_BRICK);
        // Battlements / Parapets on roof
        for (int x = -18; x <= 18; x += 2) {
            w.getBlockAt(ox + x, oy + 7, oz - 18).setType(Material.SMOOTH_BRICK);
            w.getBlockAt(ox + x, oy + 7, oz + 18).setType(Material.SMOOTH_BRICK);
        }
        for (int z = -18; z <= 18; z += 2) {
            w.getBlockAt(ox - 18, oy + 7, oz + z).setType(Material.SMOOTH_BRICK);
            w.getBlockAt(ox + 18, oy + 7, oz + z).setType(Material.SMOOTH_BRICK);
        }

        // 4 Monumental Octagonal Corner Towers (rising 10 blocks)
        int[][] towers = {{-18, -18}, {18, -18}, {-18, 18}, {18, 18}};
        for (int[] t : towers) {
            int tx = ox + t[0], tz = oz + t[1];
            fill(w, tx - 2, oy + 1, tz - 2, tx + 2, oy + 10, tz + 2, Material.SMOOTH_BRICK);
            fill(w, tx - 1, oy + 1, tz - 1, tx + 1, oy + 9, tz + 1, Material.AIR);
            w.getBlockAt(tx, oy + 11, tz).setType(Material.TORCH);
        }

        // Central Courtyard (20x20 open air cobblestone)
        fill(w, ox - 10, oy, oz - 10, ox + 10, oy, oz + 10, Material.COBBLESTONE);

        // Central Sunken Octagonal Pool (Hoz-e Firoozeh)
        fill(w, ox - 3, oy - 1, oz - 3, ox + 3, oy, oz + 3, Material.LAPIS_BLOCK);
        fill(w, ox - 2, oy, oz - 2, ox + 2, oy, oz + 2, Material.WATER);
        w.getBlockAt(ox, oy + 1, oz).setType(Material.COBBLE_WALL);
        w.getBlockAt(ox, oy + 2, oz).setType(Material.WATER); // gentle fountain

        // 8 Vaulted Guest Chambers (Hojreh-ha) along walls
        for (int i = -12; i <= 12; i += 8) {
            buildHojreh(w, ox + i, oy + 1, oz - 14);
            buildHojreh(w, ox + i, oy + 1, oz + 14);
        }

        // Monumental Entrance Portal (Iwan) facing South (Z = +18)
        fill(w, ox - 4, oy + 1, oz + 18, ox + 4, oy + 8, oz + 18, Material.SMOOTH_BRICK);
        fill(w, ox - 2, oy + 1, oz + 18, ox + 2, oy + 5, oz + 18, Material.AIR);
        w.getBlockAt(ox - 2, oy + 6, oz + 18).setType(Material.GLOWSTONE);
        w.getBlockAt(ox + 2, oy + 6, oz + 18).setType(Material.GLOWSTONE);

        // Spawn Silk Road Merchant & Caravanserai Guard Golem
        ir.iranian.hardcore.villages.SilkRoadMerchant.spawnMerchant(new Location(w, ox + 4, oy + 1, oz + 4));
        spawnGuard(w, ox, oy + 1, oz + 14, "&6&lDarogheh-ye Karvansara");

        return true;
    }

    private void buildHojreh(World w, int x, int y, int z) {
        fill(w, x - 2, y, z - 2, x + 2, y + 3, z + 2, Material.SMOOTH_BRICK);
        fill(w, x - 1, y, z - 1, x + 1, y + 2, z + 1, Material.AIR);
        // Door opening facing courtyard
        int faceZ = z < 0 ? z + 2 : z - 2;
        w.getBlockAt(x, y, faceZ).setType(Material.AIR);
        w.getBlockAt(x, y + 1, faceZ).setType(Material.AIR);
        // Interior furnishings (bed, chest, carpet)
        w.getBlockAt(x - 1, y, z).setType(Material.CARPET);
        try { w.getBlockAt(x - 1, y, z).setData((byte) 14); } catch (Exception ignored) {}
        w.getBlockAt(x + 1, y, z).setType(Material.CHEST);
        if (w.getBlockAt(x + 1, y, z).getState() instanceof Chest) {
            Chest c = (Chest) w.getBlockAt(x + 1, y, z).getState();
            c.getInventory().addItem(AchaemenidCoin.SIGLOS_NOGHRE.create(random.nextInt(6) + 2));
            c.getInventory().addItem(PersianAuthenticItems.createPestehRafsanjan());
            c.update();
        }
    }

    // ==========================================
    // 4. AB-ANBAR-E YAZDI (4-BADGIR CISTERN)
    // ==========================================
    public boolean buildAbAnbar(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        clearArea(w, ox - 14, oy + 1, oz - 14, ox + 14, oy + 26, oz + 14);

        // 1. Deep Subterranean Reservoir (14x14 diameter, 12 blocks deep)
        int depth = 12;
        fill(w, ox - 7, oy - depth, oz - 7, ox + 7, oy, oz + 7, Material.BRICK);
        fill(w, ox - 6, oy - depth + 1, oz - 6, ox + 6, oy, oz + 6, Material.AIR);
        // Fresh cool water volume (6 blocks deep of pure water)
        fill(w, ox - 6, oy - depth + 1, oz - 6, ox + 6, oy - depth + 6, oz + 6, Material.WATER);

        // 2. Brick Vaulted Dome Shell covering the reservoir
        buildDomeShell(w, ox, oy, oz, 8, Material.BRICK);

        // 3. Four Monumental Windcatchers (Badgirs) - 24 blocks tall!
        int[][] badgirPos = {{-7, -7}, {7, -7}, {-7, 7}, {7, 7}};
        for (int[] bp : badgirPos) {
            int bx = ox + bp[0], bz = oz + bp[1];
            // Hollow wind shaft down into cistern
            for (int y = -depth + 6; y <= 22; y++) {
                fill(w, bx - 1, oy + y, bz - 1, bx + 1, oy + y, bz + 1, Material.BRICK);
                w.getBlockAt(bx, oy + y, bz).setType(Material.AIR);
            }
            // Wind intake louvers at top
            w.getBlockAt(bx - 1, oy + 21, bz).setType(Material.AIR);
            w.getBlockAt(bx + 1, oy + 21, bz).setType(Material.AIR);
            w.getBlockAt(bx, oy + 21, bz - 1).setType(Material.AIR);
            w.getBlockAt(bx, oy + 21, bz + 1).setType(Material.AIR);
            w.getBlockAt(bx, oy + 23, bz).setType(Material.TORCH);
        }

        // 4. Monumental Arched Entrance Portal with 24-step staircase down
        for (int step = 0; step < 12; step++) {
            w.getBlockAt(ox, oy - step, oz + 8 + step).setType(Material.SMOOTH_STAIRS);
            w.getBlockAt(ox - 1, oy - step, oz + 8 + step).setType(Material.SMOOTH_BRICK);
            w.getBlockAt(ox + 1, oy - step, oz + 8 + step).setType(Material.SMOOTH_BRICK);
            for (int h = 1; h <= 3; h++) {
                w.getBlockAt(ox, oy - step + h, oz + 8 + step).setType(Material.AIR);
            }
        }

        // Loot Chest at foot of stairs
        Block chestBlock = w.getBlockAt(ox - 1, oy - depth + 7, oz + 5);
        chestBlock.setType(Material.CHEST);
        if (chestBlock.getState() instanceof Chest) {
            Chest c = (Chest) chestBlock.getState();
            c.getInventory().addItem(PersianAuthenticItems.createGolabGhamsar());
            c.getInventory().addItem(PersianItems.createFarshKermani());
            c.getInventory().addItem(AchaemenidCoin.DERIK_TALA.create(4));
            c.update();
        }

        return true;
    }

    // ==========================================
    // 5. ATASHKADEH-YE SASANIAN (CHAHAR-TAQ)
    // ==========================================
    public boolean buildFireTemple(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        clearArea(w, ox - 12, oy + 1, oz - 12, ox + 12, oy + 18, oz + 12);
        fill(w, ox - 10, oy - 2, oz - 10, ox + 10, oy, oz + 10, Material.SMOOTH_BRICK);

        // 4 Massive Corner Piers (Pillars)
        int[] p = {-6, 6};
        for (int px : p) {
            for (int pz : p) {
                fill(w, ox + px - 1, oy + 1, oz + pz - 1, ox + px + 1, oy + 8, oz + pz + 1, Material.SMOOTH_BRICK);
            }
        }

        // 4 Monumental Arches connecting the piers
        fill(w, ox - 5, oy + 7, oz - 6, ox + 5, oy + 8, oz - 6, Material.SMOOTH_BRICK);
        fill(w, ox - 5, oy + 7, oz + 6, ox + 5, oy + 8, oz + 6, Material.SMOOTH_BRICK);
        fill(w, ox - 6, oy + 7, oz - 5, ox - 6, oy + 8, oz + 5, Material.SMOOTH_BRICK);
        fill(w, ox + 6, oy + 7, oz - 5, ox + 6, oy + 8, oz + 5, Material.SMOOTH_BRICK);

        // Squinches & Central Hemispherical Masonry Dome
        fill(w, ox - 6, oy + 9, oz - 6, ox + 6, oy + 9, oz + 6, Material.SMOOTH_BRICK);
        buildDomeShell(w, ox, oy + 9, oz, 6, Material.BRICK);

        // Central Altar of the Sacred Eternal Fire (Atash-e Bahram)
        fill(w, ox - 2, oy + 1, oz - 2, ox + 2, oy + 1, oz + 2, Material.GOLD_BLOCK);
        w.getBlockAt(ox, oy + 2, oz).setType(Material.NETHERRACK);
        w.getBlockAt(ox, oy + 3, oz).setType(Material.FIRE);

        // Bronze ritual fire braziers
        w.getBlockAt(ox - 2, oy + 2, oz - 2).setType(Material.IRON_FENCE);
        w.getBlockAt(ox + 2, oy + 2, oz - 2).setType(Material.IRON_FENCE);
        w.getBlockAt(ox - 2, oy + 2, oz + 2).setType(Material.IRON_FENCE);
        w.getBlockAt(ox + 2, oy + 2, oz + 2).setType(Material.IRON_FENCE);

        // Relic Chest
        Block chestBlock = w.getBlockAt(ox - 4, oy + 1, oz);
        chestBlock.setType(Material.CHEST);
        if (chestBlock.getState() instanceof Chest) {
            Chest c = (Chest) chestBlock.getState();
            c.getInventory().addItem(PersianAuthenticItems.createZaferanQaen());
            c.getInventory().addItem(PersianItems.createAtashMoghadas());
            c.getInventory().addItem(AchaemenidCoin.DERIK_TALA.create(5));
            c.update();
        }

        // Mobed (Zoroastrian Priest NPC)
        spawnPersianVillager(w, ox + 3, oy + 1, oz - 3, "Mobed-e Zartoshti", Villager.Profession.PRIEST);

        return true;
    }

    // ==========================================
    // 6. ZOORKHANEH-YE BASTANI (GRAND ARENA)
    // ==========================================
    public boolean buildZoorkhaneh(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        clearArea(w, ox - 12, oy + 1, oz - 12, ox + 12, oy + 16, oz + 12);
        fill(w, ox - 10, oy - 3, oz - 10, ox + 10, oy, oz + 10, Material.WOOD);

        // Brick Hall Walls
        buildWallFrame(w, ox - 10, oz - 10, ox + 10, oz + 10, oy + 1, oy + 7, Material.BRICK);
        // Octagonal Brick Dome with central skylight (Hoorno)
        buildDomeShell(w, ox, oy + 7, oz, 8, Material.BRICK);
        w.getBlockAt(ox, oy + 15, oz).setType(Material.GLASS);

        // Central Sunken Goud-e Zoorkhaneh (8x8 octagonal pit, 2 blocks deep)
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                w.getBlockAt(ox + x, oy, oz + z).setType(Material.AIR);
                w.getBlockAt(ox + x, oy - 1, oz + z).setType(Material.AIR);
                w.getBlockAt(ox + x, oy - 2, oz + z).setType(Material.WOOD);
                if ((x + z) % 2 == 0) {
                    w.getBlockAt(ox + x, oy - 1, oz + z).setType(Material.CARPET);
                    try { w.getBlockAt(ox + x, oy - 1, oz + z).setData((byte) 14); } catch (Exception ignored) {}
                }
            }
        }

        // Elevated Sardam-e Morshed (Morshed's Podium & Bell)
        fill(w, ox + 6, oy + 1, oz - 2, ox + 8, oy + 3, oz + 2, Material.WOOD);
        w.getBlockAt(ox + 7, oy + 4, oz).setType(Material.NOTE_BLOCK); // Zarb-e Morshed
        w.getBlockAt(ox + 7, oy + 6, oz).setType(Material.GOLD_BLOCK); // Zang-e Morshed

        // Zoorkhaneh Weapon Chest
        Block chestBlock = w.getBlockAt(ox + 6, oy + 4, oz + 1);
        chestBlock.setType(Material.CHEST);
        if (chestBlock.getState() instanceof Chest) {
            Chest c = (Chest) chestBlock.getState();
            c.getInventory().addItem(PersianAuthenticItems.createMeelBastani());
            c.getInventory().addItem(PersianAuthenticItems.createKabbadehPouladin());
            c.getInventory().addItem(PersianAuthenticItems.createZangZoorkhaneh());
            c.getInventory().addItem(AchaemenidCoin.DERIK_TALA.create(8));
            c.update();
        }

        // Entrance
        w.getBlockAt(ox - 10, oy + 1, oz).setType(Material.AIR);
        w.getBlockAt(ox - 10, oy + 2, oz).setType(Material.AIR);

        // Pahlavan Guardian
        spawnGuard(w, ox, oy - 1, oz, "&6&lPahlavan-e Zoorkhaneh");

        return true;
    }

    // ==========================================
    // 7. QANAT-E KAVIR (AQUEDUCT SYSTEM)
    // ==========================================
    public boolean buildQanat(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        int depth = 14;

        // 3 Surface Wellheads connected 14 blocks underground
        int[] wellOffsets = {-15, 0, 15};
        for (int wz : wellOffsets) {
            // Well surface curb
            fill(w, ox - 1, oy + 1, oz + wz - 1, ox + 1, oy + 1, oz + wz + 1, Material.COBBLE_WALL);
            w.getBlockAt(ox, oy + 1, oz + wz).setType(Material.AIR);

            // Vertical Shaft down
            for (int y = -depth; y <= 0; y++) {
                fill(w, ox - 1, oy + y, oz + wz - 1, ox + 1, oy + y, oz + wz + 1, Material.SMOOTH_BRICK);
                w.getBlockAt(ox, oy + y, oz + wz).setType(Material.AIR);
                w.getBlockAt(ox, oy + y, oz + wz + 1).setType(Material.LADDER);
            }
        }

        // Continuous Subterranean Flowing Canal (40 blocks long)
        int by = oy - depth;
        for (int z = -20; z <= 20; z++) {
            for (int x = -2; x <= 2; x++) {
                w.getBlockAt(ox + x, by - 1, oz + z).setType(Material.SMOOTH_BRICK);
                for (int h = 0; h <= 3; h++) {
                    w.getBlockAt(ox + x, by + h, oz + z).setType(Material.AIR);
                }
                w.getBlockAt(ox + x, by + 4, oz + z).setType(Material.SMOOTH_BRICK);
            }
            w.getBlockAt(ox - 3, by, oz + z).setType(Material.SMOOTH_BRICK);
            w.getBlockAt(ox + 3, by, oz + z).setType(Material.SMOOTH_BRICK);
            // Pure flowing drinking water
            w.getBlockAt(ox, by, oz + z).setType(Material.WATER);
        }

        // Subterranean Loot Chest
        Block chestBlock = w.getBlockAt(ox + 2, by + 1, oz);
        chestBlock.setType(Material.CHEST);
        if (chestBlock.getState() instanceof Chest) {
            Chest c = (Chest) chestBlock.getState();
            c.getInventory().addItem(PersianAuthenticItems.createGolabGhamsar());
            c.getInventory().addItem(PersianAuthenticItems.createZaferanQaen());
            if (plugin.getThirstManager() != null) {
                c.getInventory().addItem(plugin.getThirstManager().createMashkAb(10));
            }
            c.getInventory().addItem(AchaemenidCoin.DERIK_TALA.create(6));
            c.update();
        }

        return true;
    }

    // ==========================================
    // 8. BAZAAR-E SONNATI (GRAND BAZAAR)
    // ==========================================
    public boolean buildBazaar(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        fill(w, ox - 12, oy - 3, oz - 12, ox + 12, oy, oz + 12, Material.BRICK);
        clearArea(w, ox - 12, oy + 1, oz - 12, ox + 12, oy + 12, oz + 12);

        // Vaulted Alleyway (Rasteh-ye Bazaar)
        fill(w, ox - 10, oy, oz - 3, ox + 10, oy, oz + 3, Material.WOOD);
        for (int x = -9; x <= 9; x++) {
            w.getBlockAt(ox + x, oy + 1, oz).setType(Material.CARPET);
            try { w.getBlockAt(ox + x, oy + 1, oz).setData((byte) 14); } catch (Exception ignored) {}
        }

        // Barrel-vaulted brick ceiling with skylights
        fill(w, ox - 10, oy + 7, oz - 3, ox + 10, oy + 7, oz + 3, Material.BRICK);
        for (int x = -8; x <= 8; x += 4) {
            w.getBlockAt(ox + x, oy + 7, oz).setType(Material.GLOWSTONE);
        }

        // 4 Stalls (Hojreh-ha)
        buildMerchantStall(w, ox - 6, oy + 1, oz - 5, "Farshforoosh-e Tabrizi", Material.CARPET, (byte) 14);
        buildMerchantStall(w, ox + 3, oy + 1, oz - 5, "Javaherforoosh-e Neyshaboori", Material.EMERALD_BLOCK, (byte) 0);
        buildMerchantStall(w, ox - 6, oy + 1, oz + 3, "Ahangar-e Esfahani", Material.ANVIL, (byte) 0);
        buildMerchantStall(w, ox + 3, oy + 1, oz + 3, "Attar-e Shirazi", Material.BREWING_STAND, (byte) 0);

        spawnGuard(w, ox, oy + 1, oz, "&6&lDarogheh-ye Bazaar");
        return true;
    }

    private void buildMerchantStall(World w, int x, int y, int z, String name, Material counterMat, byte data) {
        fill(w, x, y, z, x + 3, y + 3, z + 2, Material.BRICK);
        fill(w, x + 1, y, z + 1, x + 2, y + 2, z + 1, Material.AIR);
        w.getBlockAt(x + 1, y, z).setType(counterMat);
        if (data != 0) {
            try { w.getBlockAt(x + 1, y, z).setData(data); } catch (Exception ignored) {}
        }
        spawnPersianVillager(w, x + 1, y, z + 1, name, Villager.Profession.LIBRARIAN);
    }

    // ==========================================
    // 9. CHAYKHANEH-YE SONNATI
    // ==========================================
    public boolean buildChaikhaneh(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        clearArea(w, ox - 8, oy + 1, oz - 8, ox + 8, oy + 10, oz + 8);
        fill(w, ox - 7, oy, oz - 7, ox + 7, oy, oz + 7, Material.WOOD);
        buildWallFrame(w, ox - 7, oz - 7, ox + 7, oz + 7, oy + 1, oy + 5, Material.BRICK);

        // Raised wooden seating platforms (Takht)
        fill(w, ox - 5, oy + 1, oz - 5, ox - 2, oy + 1, oz - 3, Material.WOOD);
        fill(w, ox + 2, oy + 1, oz - 5, ox + 5, oy + 1, oz - 3, Material.WOOD);

        // Samovar counter
        w.getBlockAt(ox, oy + 1, oz - 2).setType(Material.BREWING_STAND);
        w.getBlockAt(ox - 1, oy + 1, oz - 2).setType(Material.CAULDRON);

        // Teahouse Master
        spawnPersianVillager(w, ox, oy + 1, oz - 1, "Chaychi-ye Sonnati", Villager.Profession.FARMER);
        return true;
    }

    // ==========================================
    // 10. SIMURGH PEAK
    // ==========================================
    public boolean buildSimurghPeak(Location o) {
        World w = o.getWorld();
        if (w == null) return false;
        int ox = o.getBlockX(), oy = o.getBlockY(), oz = o.getBlockZ();

        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                double dist = Math.hypot(x, z);
                if (dist <= 5.2) {
                    w.getBlockAt(ox + x, oy, oz + z).setType(Material.QUARTZ_BLOCK);
                    if (dist >= 4.0) {
                        w.getBlockAt(ox + x, oy + 1, oz + z).setType(Material.GOLD_BLOCK);
                    } else {
                        w.getBlockAt(ox + x, oy + 1, oz + z).setType(Material.HAY_BLOCK);
                    }
                    for (int y = 2; y <= 6; y++) {
                        w.getBlockAt(ox + x, oy + y, oz + z).setType(Material.AIR);
                    }
                }
            }
        }

        w.getBlockAt(ox, oy + 1, oz).setType(Material.LAPIS_BLOCK);
        w.getBlockAt(ox, oy + 2, oz).setType(Material.FIRE);

        Block chestBlock = w.getBlockAt(ox + 1, oy + 2, oz);
        chestBlock.setType(Material.CHEST);
        if (chestBlock.getState() instanceof Chest) {
            Chest c = (Chest) chestBlock.getState();
            c.getInventory().addItem(PersianAuthenticItems.createParSimurgh());
            c.getInventory().addItem(PersianBossItems.createJamshidTalisman());
            c.getInventory().addItem(AchaemenidCoin.DERIK_SHAHANSHAHI.create(2));
            c.getInventory().addItem(new ItemStack(Material.EMERALD, 12));
            c.update();
        }
        return true;
    }

    // ==========================================
    // Architectural Helpers
    // ==========================================
    private void setImperialChest(World w, int x, int y, int z) {
        Block b = w.getBlockAt(x, y, z);
        b.setType(Material.CHEST);
        if (b.getState() instanceof Chest) {
            Chest c = (Chest) b.getState();
            c.getInventory().addItem(PersianBossItems.createDivKoshSword());
            c.getInventory().addItem(PersianAuthenticItems.createMeelBastani());
            c.getInventory().addItem(AchaemenidCoin.DERIK_SHAHANSHAHI.create(5));
            c.getInventory().addItem(AchaemenidCoin.DERIK_TALA.create(24));
            c.getInventory().addItem(new ItemStack(Material.DIAMOND, 8));
            c.update();
        }
    }

    private void spawnGuard(World w, int x, int y, int z, String name) {
        Location loc = new Location(w, x + 0.5, y, z + 0.5);
        IronGolem golem = (IronGolem) w.spawnEntity(loc, EntityType.IRON_GOLEM);
        golem.setCustomName(MessageUtils.color(name));
        golem.setCustomNameVisible(true);
    }

    private void spawnPersianVillager(World w, int x, int y, int z, String name, Villager.Profession prof) {
        Location loc = new Location(w, x + 0.5, y, z + 0.5);
        Villager villager = (Villager) w.spawnEntity(loc, EntityType.VILLAGER);
        villager.setCustomName(MessageUtils.color("&e&l" + name));
        villager.setCustomNameVisible(true);
        villager.setProfession(prof);
    }

    private void buildWallFrame(World w, int x1, int z1, int x2, int z2, int y1, int y2, Material mat) {
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                w.getBlockAt(x, y, z1).setType(mat);
                w.getBlockAt(x, y, z2).setType(mat);
            }
            for (int z = z1; z <= z2; z++) {
                w.getBlockAt(x1, y, z).setType(mat);
                w.getBlockAt(x2, y, z).setType(mat);
            }
        }
    }

    private void buildDomeShell(World w, int cx, int cy, int cz, int radius, Material mat) {
        for (int y = 0; y <= radius; y++) {
            int r = radius - y;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    int distSq = x * x + z * z;
                    if (distSq <= r * r && distSq >= (r - 2) * (r - 2)) {
                        w.getBlockAt(cx + x, cy + y, cz + z).setType(mat);
                    }
                }
            }
        }
    }

    private void fill(World w, int x1, int y1, int z1, int x2, int y2, int z2, Material mat) {
        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    w.getBlockAt(x, y, z).setType(mat);
                }
            }
        }
    }

    private void clearArea(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
        fill(w, x1, y1, z1, x2, y2, z2, Material.AIR);
    }

    private Location findGround(Location loc) {
        World world = loc.getWorld();
        if (world == null) return null;
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int y = world.getHighestBlockYAt(x, z);

        while (y > 5) {
            Block b = world.getBlockAt(x, y, z);
            Material m = b.getType();
            if (m != Material.AIR && m != Material.LEAVES && m != Material.LEAVES_2 
                    && m != Material.LOG && m != Material.LOG_2 && m != Material.SNOW
                    && m != Material.LONG_GRASS && m != Material.YELLOW_FLOWER && m != Material.RED_ROSE) {
                break;
            }
            y--;
        }
        if (y < 5) return null;
        return new Location(world, x, y, z);
    }

    public void tryGenerateInChunk(Chunk chunk) {
        if (!plugin.getConfigManager().getBoolean("structures.enabled", true)) return;
        if (chunk == null || chunk.getWorld() == null) return;

        int cx = chunk.getX();
        int cz = chunk.getZ();

        int rx = Math.floorDiv(cx, 16);
        int rz = Math.floorDiv(cz, 16);

        long seed = ((long) rx * 987654321987L + (long) rz * 456789123456L) ^ chunk.getWorld().getSeed();
        Random regRand = new Random(seed);
        int targetOffsetX = (regRand.nextInt(12) + 2) % 16;
        int targetOffsetZ = (regRand.nextInt(12) + 2) % 16;

        if (cx != (rx * 16 + targetOffsetX) || cz != (rz * 16 + targetOffsetZ)) {
            return;
        }

        String regionKey = chunk.getWorld().getName() + "_grand_struct_" + rx + "_" + rz;
        if (generatedRegions.contains(regionKey)) return;
        generatedRegions.add(regionKey);

        Location center = new Location(chunk.getWorld(), cx * 16 + 8, 0, cz * 16 + 8);
        center = findGround(center);
        if (center == null) return;

        Biome biome = center.getBlock().getBiome();
        StructureType toBuild;

        if (biome.name().contains("DESERT") || biome.name().contains("MESA")) {
            int roll = regRand.nextInt(4);
            if (roll == 0) toBuild = StructureType.CYRUS_TOMB;
            else if (roll == 1) toBuild = StructureType.CARAVANSERAI;
            else if (roll == 2) toBuild = StructureType.AB_ANBAR;
            else toBuild = StructureType.QANAT;
        } else if (biome.name().contains("FOREST") || biome.name().contains("PLAINS")) {
            int roll = regRand.nextInt(3);
            if (roll == 0) toBuild = StructureType.PERSEPOLIS;
            else if (roll == 1) toBuild = StructureType.BAZAAR;
            else toBuild = StructureType.ZOORKHANEH;
        } else if (biome.name().contains("MOUNTAIN") || biome.name().contains("EXTREME_HILLS")) {
            toBuild = center.getBlockY() >= 105 ? StructureType.SIMURGH_PEAK : StructureType.ATASHKADEH;
        } else {
            toBuild = StructureType.CARAVANSERAI;
        }

        Location finalCenter = center;
        StructureType finalType = toBuild;
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            forceBuildStructure(finalCenter, finalType);
            plugin.getLogger().info("Monumental Persian Architecture " + finalType.getPersianName() + " generated at " 
                    + finalCenter.getBlockX() + "," + finalCenter.getBlockY() + "," + finalCenter.getBlockZ());
        }, 40L);
    }
}

package ir.iranian.hardcore.villages;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Iranian Village & Speaking Villagers Manager (v5.0 Finglish)
 * - Converts vanilla villages to historic Iranian villages (Abyaneh, Kandovan, Masooleh, Meymand)
 * - Villagers have Iranian names and speak interactive Finglish dialogues
 * - Pure Finglish / English letters for maximum compatibility with Aternos
 */
public class IranianVillageManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private final Random random = new Random();
    private final Map<UUID, Long> lastDialogueTime = new HashMap<>();

    private final String[] finglishNames = {
            "Haj Karim - Kadkhoda",
            "Mashhadi Hassan - Keshavarz",
            "Ostaa Taghi - Ahangar",
            "Akbar Agha - Ghasab",
            "Ziba Khanom - Ghalibaf",
            "Baba Ali - Attar",
            "Mirza Reza - Maktabdar",
            "Sohrab - Bazargan",
            "Daryoosh - Sarhang",
            "Ghasem - Chai-riz",
            "Mahmoud - Kashi-kar",
            "Reza - Farsh-foroush"
    };

    private final String[] villageNames = {
            "Roosta-ye Kandovan - Azarbayjan",
            "Roosta-ye Meymand - Kerman",
            "Roosta-ye Abyaneh - Isfahan",
            "Roosta-ye Masooleh - Gilan",
            "Roosta-ye Palangan - Kordestan",
            "Dehkadeh-ye Choobi - Neyshaboor",
            "Roosta-ye Ghaleh-No - Yazd",
            "Roosta-ye Khor - Isfahan"
    };

    private final String[] villagerDialogues = {
            "Salam baradar! Be roosta-ye ma khosh amadid!",
            "Chelo Kabab-e Barg va Ghormeh Sabzi-ye tazeh daram, meyl darid?",
            "In roozha Div-e Sepid va Zahhak dar kooh-haye Alborz dideh shodan, moragheb bashid!",
            "Shamshir-e asil-e Irani dar dokan darim, az foolad-e Damashgh va Isfahan!",
            "Khalij-e Hameshe Fars baraye Iran ast va khahad bood!",
            "Ye estekan Chai-ye Lahijan ba nabat benooshid ta khastegi-toon dar bere!",
            "Mashk-e ab-et ro por kardi javoon? Kavir-e Loot besiar bi-rahm ast!",
            "Ghafeleh-ye bazarganan az Jadeh-ye Abrisham be zoodi mirese!",
            "Zendeh bad Iran-e bozorg ba haft hezar saal tarikh-e por eftekhar!",
            "Kashk-e Bademjan va Dizi-ye sangi amadeh ast, befarmayed!",
            "Dast-e khali nayoomadi ke? Zarr o Seer darim baraye tejarat!",
            "Khesht va Kaahgel-e khaneh-ye ma 500 saal ghedmat dareh!",
            "Farsh-e dastbaf-e Kashan va Tabriz behtarin dar jahan ast!",
            "Khoda ghovvat pahlavan! Baraye azadi-ye Iran bejang!",
            "Dookan-e attari-ye man por az Zaferan va Avishan-e koohi ast."
    };

    public IranianVillageManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager)) return;
        Villager villager = (Villager) event.getRightClicked();
        Player player = event.getPlayer();

        // Dialogue cooldown: 3 seconds per player
        long now = System.currentTimeMillis();
        long last = lastDialogueTime.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < 3000) return;
        lastDialogueTime.put(player.getUniqueId(), now);

        // Ensure villager has an Iranian name
        if (villager.getCustomName() == null || villager.getCustomName().isEmpty()) {
            String name = finglishNames[random.nextInt(finglishNames.length)];
            villager.setCustomName(MessageUtils.color("&e" + name));
            villager.setCustomNameVisible(true);
        }

        String villagerName = villager.getCustomName();
        String line = villagerDialogues[random.nextInt(villagerDialogues.length)];

        float pitch = 0.9f + (random.nextFloat() * 0.3f);
        ir.iranian.hardcore.utils.SpeechUtils.playVoice(villager, Sound.ENTITY_VILLAGER_YES, pitch);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;
        if (!plugin.getConfigManager().getBoolean("villages.enabled", true)) return;

        if (random.nextDouble() > 0.1) return;

        boolean hasVillage = false;
        for (org.bukkit.entity.Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof Villager) {
                hasVillage = true;
                break;
            }
        }

        if (!hasVillage) {
            Biome biome = event.getWorld().getBlockAt(event.getChunk().getX() * 16, 64, event.getChunk().getZ() * 16).getBiome();
            if (biome == Biome.PLAINS || biome == Biome.DESERT || biome == Biome.SAVANNA) {
                if (random.nextDouble() < 0.02) {
                    hasVillage = true;
                }
            }
        }

        if (hasVillage) {
            for (org.bukkit.entity.Entity entity : event.getChunk().getEntities()) {
                if (entity instanceof Villager) {
                    Villager villager = (Villager) entity;
                    String finglishName = finglishNames[random.nextInt(finglishNames.length)];
                    villager.setCustomName(MessageUtils.color("&e" + finglishName));
                    villager.setCustomNameVisible(true);
                    Villager.Profession[] professions = Villager.Profession.values();
                    villager.setProfession(professions[random.nextInt(professions.length)]);
                }
            }

            for (Player player : event.getWorld().getPlayers()) {
                if (player.getLocation().distance(new Location(event.getWorld(), event.getChunk().getX() * 16, 64, event.getChunk().getZ() * 16)) < 100) {
                    if (random.nextDouble() < 0.3) {
                        String villageName = villageNames[random.nextInt(villageNames.length)];
                        player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aRoosta-ye Irani kashf shod: &e" + villageName));
                        player.sendTitle(MessageUtils.color("&6" + villageName), MessageUtils.color("&7Memari-ye Irani - Kaahgel va Badgir"), 20, 60, 20);
                    }
                }
            }
        }
    }

    public boolean buildIranianVillage(Location origin) {
        World world = origin.getWorld();
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        buildKahgelHouse(world, ox - 8, oy, oz);
        buildKahgelHouse(world, ox + 8, oy, oz);
        buildKahgelHouse(world, ox, oy, oz + 8);

        world.getBlockAt(ox, oy, oz).setType(Material.WATER);
        world.getBlockAt(ox, oy - 1, oz).setType(Material.SANDSTONE);
        buildWalls(world, ox, oy, oz, 2, 1, Material.SANDSTONE);

        buildSmallMosque(world, ox, oy, oz - 10);

        for (int i = 0; i < 4; i++) {
            Location loc = new Location(world, ox + random.nextInt(16) - 8, oy + 1, oz + random.nextInt(16) - 8);
            Villager villager = (Villager) world.spawnEntity(loc, EntityType.VILLAGER);
            String name = finglishNames[random.nextInt(finglishNames.length)];
            villager.setCustomName(MessageUtils.color("&e" + name));
            villager.setCustomNameVisible(true);
        }

        return true;
    }

    public void generateVillage(Location location) {
        buildIranianVillage(location);
    }

    public Villager spawnIranianVillager(Location location, String name) {
        World world = location.getWorld();
        if (world == null) return null;
        Villager villager = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
        if (name == null || name.isEmpty()) {
            name = finglishNames[random.nextInt(finglishNames.length)];
        }
        villager.setCustomName(MessageUtils.color("&e" + name));
        villager.setCustomNameVisible(true);
        return villager;
    }

    private void buildKahgelHouse(World world, int ox, int oy, int oz) {
        for (int x = -3; x <= 3; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = -3; z <= 3; z++) {
                    if (Math.abs(x) == 3 || Math.abs(z) == 3 || y == 0 || y == 4) {
                        world.getBlockAt(ox + x, oy + y, oz + z).setType(Material.SANDSTONE);
                    }
                }
            }
        }
        world.getBlockAt(ox, oy + 1, oz - 3).setType(Material.AIR);
        world.getBlockAt(ox, oy + 2, oz - 3).setType(Material.AIR);
        world.getBlockAt(ox, oy, oz).setType(Material.CARPET);
        try {
            world.getBlockAt(ox, oy, oz).setData((byte) 14);
        } catch (Exception ignored) {}
    }

    private void buildSmallMosque(World world, int ox, int oy, int oz) {
        for (int x = -4; x <= 4; x++) {
            for (int y = 0; y < 6; y++) {
                for (int z = -4; z <= 4; z++) {
                    if (Math.abs(x) == 4 || Math.abs(z) == 4 || y == 0) {
                        world.getBlockAt(ox + x, oy + y, oz + z).setType(Material.SMOOTH_BRICK);
                    }
                }
            }
        }
        for (int y = 0; y < 4; y++) {
            int r = 4 - y;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    if (x * x + z * z <= r * r && x * x + z * z >= (r - 1) * (r - 1)) {
                        world.getBlockAt(ox + x, oy + 6 + y, oz + z).setType(Material.WOOL);
                        try {
                            world.getBlockAt(ox + x, oy + 6 + y, oz + z).setData((byte) 9);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    private void buildWalls(World world, int ox, int oy, int oz, int radius, int height, Material mat) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius || Math.abs(z) == radius) {
                        world.getBlockAt(ox + x, oy + y, oz + z).setType(mat);
                    }
                }
            }
        }
    }
}

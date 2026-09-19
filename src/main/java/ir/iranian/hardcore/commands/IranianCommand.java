package ir.iranian.hardcore.commands;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.dungeons.DungeonType;
import ir.iranian.hardcore.foods.IranianFoodType;
import ir.iranian.hardcore.mobs.CustomMobType;
import ir.iranian.hardcore.swords.PersianSwordType;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Iranian Command Suite (v5.0 Finglish)
 * /iranian - Main command for all Iranian Hardcore features:
 * - RLCraft Thirst & Waterskins
 * - 900 Swords & Crafting
 * - 500 Iranian Foods
 * - 20,000 Items
 * - 30 Dungeons & Boss Fights
 * - 30 Iranian Mobs & Speaking Villagers
 * - Climate, Weather & Seasons
 */
public class IranianCommand implements CommandExecutor, TabCompleter {

    private final IranianHardcorePlugin plugin;

    public IranianCommand(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "thirst":
            case "teshnegi":
                double thirst = plugin.getThirstManager().getThirst(player);
                double temp = plugin.getTemperatureManager().getTemperature(player);
                player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
                player.sendMessage(MessageUtils.color("&b&l💧 Mizan-e Ab: &e" + String.format("%.0f", thirst) + "% / 100%"));
                player.sendMessage(MessageUtils.color("&6&l🌡 Dama-ye Badan: &e" + String.format("%.0f", temp) + "C"));
                player.sendMessage(MessageUtils.color("&7Biome: &a" + player.getLocation().getBlock().getBiome().name()));
                player.sendMessage(MessageUtils.color("&7Fasl: &e" + plugin.getTemperatureManager().getSeasonName(player.getWorld())));
                player.sendMessage(MessageUtils.color("&7Vaz'iat-e Hava: &b" + plugin.getTemperatureManager().getWeatherDescription(player.getWorld())));
                player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
                return true;

            case "drink":
            case "ab":
                if (args.length > 1 && args[1].equalsIgnoreCase("dirty")) {
                    plugin.getThirstManager().drinkWater(player, 15.0);
                    player.sendMessage(MessageUtils.color("&8[&6Ab&8] &cAb-e kasif nooshidid (+15% Ab)!"));
                } else {
                    plugin.getThirstManager().drinkWater(player, 35.0);
                    player.sendMessage(MessageUtils.color("&8[&6Ab&8] &aAb-e govara va paak nooshidid (+35% Ab)!"));
                }
                return true;

            case "mashk":
                player.getInventory().addItem(plugin.getThirstManager().createMashkAb());
                player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aMashk-e Ab-e Sonnati (10 nooshesh) dadeh shod!"));
                return true;

            case "canteen":
            case "ghomghame":
                player.getInventory().addItem(plugin.getThirstManager().createCanteen(5));
                player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aGhomghame-ye Fooladi (5 nooshesh) dadeh shod!"));
                return true;

            case "dirtywater":
                player.getInventory().addItem(plugin.getThirstManager().createDirtyWater());
                player.sendMessage(MessageUtils.color("&8[&6Iran&8] &cShisheh-ye Ab-e Kasif dadeh shod. Dar kooreh bejooshanid!"));
                return true;

            case "cleanwater":
                player.getInventory().addItem(plugin.getThirstManager().createCleanWater());
                player.sendMessage(MessageUtils.color("&8[&6Iran&8] &aShisheh-ye Ab-e Paak va Jooshandeh dadeh shod!"));
                return true;

            case "sword":
            case "shamshir":
                if (args.length < 2 || args[1].equalsIgnoreCase("random")) {
                    plugin.getSwordsManager().giveRandomSword(player);
                } else if (args[1].equalsIgnoreCase("list")) {
                    player.sendMessage(MessageUtils.color("&6900 Shamshir-e Irani vojood darad! Baraye daryaft: &e/iranian sword <SWORD_001 ta SWORD_900>"));
                } else {
                    if (!plugin.getSwordsManager().giveSword(player, args[1])) {
                        player.sendMessage(MessageUtils.color("&cShamshir peyda nashod! Mesal: SWORD_001 ta SWORD_900"));
                    }
                }
                return true;

            case "food":
            case "ghaza":
                if (args.length < 2 || args[1].equalsIgnoreCase("random")) {
                    plugin.getFoodsManager().giveRandomFood(player);
                } else if (args[1].equalsIgnoreCase("list")) {
                    player.sendMessage(MessageUtils.color("&6500 Ghaza-ye Irani vojood darad! Baraye daryaft: &e/iranian food <FOOD_001 ta FOOD_500>"));
                } else {
                    if (!plugin.getFoodsManager().giveFood(player, args[1])) {
                        player.sendMessage(MessageUtils.color("&cGhaza peyda nashod! Mesal: FOOD_001 ta FOOD_500"));
                    }
                }
                return true;

            case "item":
                if (args.length < 2 || args[1].equalsIgnoreCase("random")) {
                    plugin.getCustomItemRegistry().giveRandomItem(player);
                } else {
                    if (!plugin.getCustomItemRegistry().giveItem(player, args[1])) {
                        player.sendMessage(MessageUtils.color("&cItem peyda nashod! Mesal: ITEM_00001 ta ITEM_20000"));
                    }
                }
                return true;

            case "villager":
            case "roosta":
                player.sendMessage(MessageUtils.color("&8[&6Haj Karim - Kadkhoda&8] &f\"Salam baradar! Be sarzamin-e Iran khosh amadid! Zendeh bad Iran!\""));
                return true;

            case "spawnmob":
            case "mob":
                if (!player.hasPermission("iranian.admin")) {
                    player.sendMessage(MessageUtils.color("&cShoma dastresi admin nadarid!"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(MessageUtils.color("&cEstefade: /iranian spawnmob <MOB_TYPE>"));
                    return true;
                }
                plugin.getMobsManager().spawnMobCommand(player, args[1]);
                return true;

            case "dungeon":
                if (!player.hasPermission("iranian.admin")) {
                    player.sendMessage(MessageUtils.color("&cShoma dastresi admin nadarid!"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(MessageUtils.color("&8[&6Iran&8] &cEstefadeh: /iranian dungeon <name> (Mesal: ALAMUT_CASTLE)"));
                    return true;
                }
                DungeonType dt = DungeonType.fromName(args[1]);
                if (dt == null) {
                    player.sendMessage(MessageUtils.color("&8[&6Iran&8] &cDungeon yaft nashod! List: /dungeon list"));
                    return true;
                }
                plugin.getDungeonManager().forceGenerateDungeon(player, dt);
                return true;

            case "boss":
                if (!player.hasPermission("iranian.admin")) {
                    player.sendMessage(MessageUtils.color("&cShoma dastresi admin nadarid!"));
                    return true;
                }
                DungeonType dType = DungeonType.ALAMUT_CASTLE;
                if (args.length > 1) {
                    try {
                        dType = DungeonType.valueOf(args[1].toUpperCase());
                    } catch (Exception ignored) {}
                }
                plugin.getBossFightManager().spawnDungeonBoss(player.getLocation(), dType);
                player.sendMessage(MessageUtils.color("&4[BOSS] &6Boss fight baraye &e" + dType.getFinglishName() + " &ashoroo shod!"));
                return true;

            case "temperature":
            case "dama":
            case "weather":
            case "hava":
                double curTemp = plugin.getTemperatureManager().getTemperature(player);
                player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
                player.sendMessage(MessageUtils.color("&6&lAb va Hava-ye Iran"));
                player.sendMessage(MessageUtils.color("&7Dama: &e" + String.format("%.0f", curTemp) + "C"));
                player.sendMessage(MessageUtils.color("&7Fasl: &a" + plugin.getTemperatureManager().getSeasonName(player.getWorld())));
                player.sendMessage(MessageUtils.color("&7Vaz'iat: &b" + plugin.getTemperatureManager().getWeatherDescription(player.getWorld())));
                player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
                return true;

            case "resourcepack":
            case "pack":
                plugin.getResourcePackManager().sendResourcePack(player);
                return true;

            case "help":
            default:
                sendHelp(player);
                return true;
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
        player.sendMessage(MessageUtils.color("&6&l🇮🇷 Iranian Hardcore v5.0 - Finglish Edition"));
        player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
        player.sendMessage(MessageUtils.color("&e/iranian thirst &7- Didan mizan-e ab va dama (RLCraft style)"));
        player.sendMessage(MessageUtils.color("&e/iranian drink [dirty|clean] &7- Nooshidan-e ab"));
        player.sendMessage(MessageUtils.color("&e/iranian mashk &7- Daryaft Mashk-e Ab (10 nooshesh)"));
        player.sendMessage(MessageUtils.color("&e/iranian sword <random|id> &7- 900 Shamshir-e asil-e Irani"));
        player.sendMessage(MessageUtils.color("&e/iranian food <random|id> &7- 500 Ghaza-ye sonnati-ye Irani"));
        player.sendMessage(MessageUtils.color("&e/iranian item <random|id> &7- 20,000 Item-e tarikhi-ye Irani"));
        player.sendMessage(MessageUtils.color("&e/iranian spawnmob <type> &7- 30 Mob-e afsanehi-ye Irani (admin)"));
        player.sendMessage(MessageUtils.color("&e/iranian boss <dungeon> &7- Boss fight 3-marhalehi (admin)"));
        player.sendMessage(MessageUtils.color("&e/iranian weather &7- Fasl-ha va ab va hava"));
        player.sendMessage(MessageUtils.color("&e/iranian resourcepack &7- Daryaft Resource Pack"));
        player.sendMessage(MessageUtils.color("&e/race choose &7- Entekhab-e 14 Ghome Irani"));
        player.sendMessage(MessageUtils.color("&e/bazaar &7- Bazaar-e Bozorg-e Irani"));
        player.sendMessage(MessageUtils.color("&e/dungeon list &7- 30 Dungeon-e tarikhi-ye Iran"));
        player.sendMessage(MessageUtils.color("&8&m----------------------------------------"));
        player.sendMessage(MessageUtils.color("&6Zendeh bad Iran! Khalij-e Hameshe Fars! 🇮🇷"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(Arrays.asList("thirst", "drink", "mashk", "canteen", "dirtywater", "cleanwater",
                    "sword", "food", "item", "dungeon", "spawnmob", "boss", "temperature", "weather", "villager", "resourcepack", "help"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("dungeon") || args[0].equalsIgnoreCase("boss")) {
                for (DungeonType dt : DungeonType.values()) {
                    if (dt.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(dt.name());
                    }
                }
            } else if (args[0].equalsIgnoreCase("spawnmob")) {
                for (CustomMobType t : CustomMobType.values()) {
                    completions.add(t.name());
                }
            } else if (args[0].equalsIgnoreCase("boss")) {
                for (DungeonType d : DungeonType.values()) {
                    completions.add(d.name());
                }
            } else if (args[0].equalsIgnoreCase("sword")) {
                completions.addAll(Arrays.asList("random", "list", "SWORD_001", "SWORD_100", "SWORD_500", "SWORD_900"));
            } else if (args[0].equalsIgnoreCase("food")) {
                completions.addAll(Arrays.asList("random", "list", "FOOD_001", "FOOD_100", "FOOD_250", "FOOD_500"));
            } else if (args[0].equalsIgnoreCase("item")) {
                completions.addAll(Arrays.asList("random", "ITEM_00001", "ITEM_05000", "ITEM_10000", "ITEM_20000"));
            } else if (args[0].equalsIgnoreCase("drink")) {
                completions.addAll(Arrays.asList("clean", "dirty"));
            }
        }
        return completions;
    }
}

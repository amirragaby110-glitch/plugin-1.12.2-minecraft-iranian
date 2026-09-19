package ir.iranian.hardcore.resourcepack;

import ir.iranian.hardcore.IranianHardcorePlugin;
import ir.iranian.hardcore.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.io.File;

/**
 * Resource Pack Manager - v4.0
 * Provides custom textures for Iranian mobs and items
 * - Div, Simurgh, Zahhak textures
 * - Persian items textures
 * - Iranian flag, etc
 */
public class ResourcePackManager implements Listener {

    private final IranianHardcorePlugin plugin;
    private String resourcePackUrl = "";
    private String resourcePackHash = "";

    public ResourcePackManager(IranianHardcorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        loadConfig();
    }

    private void loadConfig() {
        resourcePackUrl = plugin.getConfigManager().getString("resourcepack.url", "");
        resourcePackHash = plugin.getConfigManager().getString("resourcepack.hash", "");

        // If no URL set, use default GitHub raw link for resource pack
        if (resourcePackUrl.isEmpty()) {
            // Default resource pack will be generated and hosted via GitHub
            resourcePackUrl = "https://github.com/amirragaby110-glitch/plugin-1.12.2-minecraft-iranian/raw/main/resourcepack/IranianHardcore-ResourcePack.zip";
        }

        plugin.getLogger().info("ResourcePack URL: " + (resourcePackUrl.isEmpty() ? "Disabled" : resourcePackUrl));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getConfigManager().getBoolean("resourcepack.enabled", true)) return;
        if (!plugin.getConfigManager().getBoolean("resourcepack.auto-send-on-join", false)) return;

        Player player = event.getPlayer();
        // Delay 3 seconds after join
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            sendResourcePack(player);
        }, 60L);
    }

    public void sendResourcePack(Player player) {
        if (resourcePackUrl.isEmpty()) {
            player.sendMessage(MessageUtils.withPrefix("&7Resource pack disabled - No URL set"));
            return;
        }

        try {
            if (resourcePackHash.isEmpty()) {
                player.setResourcePack(resourcePackUrl);
            } else {
                // For 1.12.2, setResourcePack with hash
                // Use reflection to call method with hash if available
                try {
                    player.getClass().getMethod("setResourcePack", String.class, String.class)
                            .invoke(player, resourcePackUrl, resourcePackHash);
                } catch (NoSuchMethodException e) {
                    player.setResourcePack(resourcePackUrl);
                }
            }
            player.sendMessage(MessageUtils.withPrefix("&a&lResource pack Irani ferestade shod! &7Lotfan Accept konid"));
            player.sendMessage(MessageUtils.withPrefix("&7Pack shamel texture Div, Simurgh, Zahhak, etc"));
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to send resource pack to " + player.getName() + ": " + e.getMessage());
        }
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        switch (event.getStatus()) {
            case ACCEPTED:
                player.sendMessage(MessageUtils.withPrefix("&aResource pack accept shod - Dar hal download..."));
                break;
            case SUCCESSFULLY_LOADED:
                player.sendMessage(MessageUtils.withPrefix("&a&lResource pack Irani ba movafaghiat load shod! &6Zende bad Iran! 🇮🇷"));
                player.sendMessage(MessageUtils.withPrefix("&7Div, Simurgh, Zahhak ba texture jadid!"));
                break;
            case DECLINED:
                player.sendMessage(MessageUtils.withPrefix("&cShoma resource pack ra rad kardid - Texture haye Irani namayesh dade nemishavad"));
                player.sendMessage(MessageUtils.withPrefix("&7Baraye didan dobare: /iranian resourcepack"));
                break;
            case FAILED_DOWNLOAD:
                player.sendMessage(MessageUtils.withPrefix("&cDownload resource pack moshkel dasht - Dobare emtehan konid: /iranian resourcepack"));
                break;
        }
    }

    public void setResourcePackUrl(String url) {
        this.resourcePackUrl = url;
        plugin.getConfigManager().getConfig().set("resourcepack.url", url);
        plugin.getConfigManager().saveConfig();
    }

    public String getResourcePackUrl() {
        return resourcePackUrl;
    }

    /**
     * Generate resource pack files info
     */
    public void generateResourcePackInfo(Player player) {
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&6&l🇮🇷 Iranian Hardcore Resource Pack"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
        player.sendMessage(MessageUtils.color("&7URL: &f" + resourcePackUrl));
        player.sendMessage(MessageUtils.color("&7Status: " + (resourcePackUrl.isEmpty() ? "&cDisabled" : "&aEnabled")));
        player.sendMessage(MessageUtils.color(""));
        player.sendMessage(MessageUtils.color("&e&lShamel:"));
        player.sendMessage(MessageUtils.color("&7- Div Sepid texture (zombie)"));
        player.sendMessage(MessageUtils.color("&7- Div Siah texture (husk)"));
        player.sendMessage(MessageUtils.color("&7- Simurgh texture (chicken)"));
        player.sendMessage(MessageUtils.color("&7- Zahhak texture (villager zombie)"));
        player.sendMessage(MessageUtils.color("&7- Rostam texture (skeleton)"));
        player.sendMessage(MessageUtils.color("&7- Al texture (witch)"));
        player.sendMessage(MessageUtils.color("&7- Kaveh texture (iron golem)"));
        player.sendMessage(MessageUtils.color("&7- Persian items (gold, sword, etc)"));
        player.sendMessage(MessageUtils.color("&7- Iranian flag"));
        player.sendMessage(MessageUtils.color(""));
        player.sendMessage(MessageUtils.color("&a/iranian resourcepack &7- Daryaft pack"));
        player.sendMessage(MessageUtils.color("&8&l&m-------------------"));
    }

    /**
     * Check if resource pack file exists locally
     */
    public boolean isLocalResourcePackExists() {
        File packFile = new File(plugin.getDataFolder().getParentFile().getParentFile(), "resourcepack/IranianHardcore-ResourcePack.zip");
        // Also check in plugin folder
        File localPack = new File("resourcepack/IranianHardcore-ResourcePack.zip");
        return packFile.exists() || localPack.exists();
    }
}

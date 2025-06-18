package whyI.resourceRush;

import org.bukkit.scheduler.BukkitRunnable;

public class CheckUpdater {
    private final String pluginResourceId = "1234";

    public CheckUpdater() {
        checkForUpdates();
    }

    private void checkForUpdates() {
        new BukkitRunnable() {
            public void run() {
                try {
                    String currentVersion = ResourceRush.getInstance().getDescription().getVersion();
                    String latestVersion = ResourceRush.getInstance().getConfig().getString("latest-version", currentVersion);
                    if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                        ResourceRush.getInstance().getLogger().info("====================================");
                        ResourceRush.getInstance().getLogger().info("A new version of the plugin is available: " + latestVersion);
                        ResourceRush.getInstance().getLogger().info("Your version: " + currentVersion);
                        ResourceRush.getInstance().getLogger().info("Download: https://www.spigotmc.org/resources/1234");
                        ResourceRush.getInstance().getLogger().info("====================================");
                    } else {
                        ResourceRush.getInstance().getLogger().info("You have the latest version of the plugin.");
                    }
                } catch (Exception e) {
                    ResourceRush.getInstance().getLogger().warning("Couldn't check for updates: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(ResourceRush.getInstance());
    }
}
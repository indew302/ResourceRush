package whyI.resourceRush;

import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckUpdater {
    private final int pluginResourceId = 126168;

    public CheckUpdater() {
        checkForUpdates();
    }

    private void checkForUpdates() {
        new BukkitRunnable() {
            public void run() {
                try {
                    URL url = new URL("https://api.spiget.org/v2/resources/" + pluginResourceId + "/versions/latest");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    JSONObject json = new JSONObject(response.toString());
                    String latestVersion = json.getString("name");

                    String currentVersion = ResourceRush.getInstance().getDescription().getVersion();

                    if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                        ResourceRush.getInstance().getLogger().info("====================================");
                        ResourceRush.getInstance().getLogger().info("A new version is available: " + latestVersion);
                        ResourceRush.getInstance().getLogger().info("Your version: " + currentVersion);
                        ResourceRush.getInstance().getLogger().info("Download: https://www.spigotmc.org/resources/" + pluginResourceId);
                        ResourceRush.getInstance().getLogger().info("====================================");
                    } else {
                        ResourceRush.getInstance().getLogger().info("You have the latest version.");
                    }
                } catch (Exception e) {
                    ResourceRush.getInstance().getLogger().warning("Update check failed: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(ResourceRush.getInstance());
    }
}
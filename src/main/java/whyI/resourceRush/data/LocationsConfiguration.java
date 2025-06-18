package whyI.resourceRush.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import whyI.resourceRush.ResourceRush;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LocationsConfiguration {

    private File configData = null;
    private FileConfiguration dataConfig = null;

    public LocationsConfiguration() {
        loadLocationsConfig();
        loadLocations();
    }

    public void loadLocationsConfig() {
        this.configData = new File(ResourceRush.getInstance().getDataFolder(), "locations.yml");
        if(!this.configData.exists()) {
            ResourceRush.getInstance().saveResource("locations.yml", false);
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configData);
    }

    private void loadLocations() {
        Location lobbyLocation = getConfig().getLocation("locations.lobby");
        ConfigurationSection section = getConfig().getConfigurationSection("locations");
        if (lobbyLocation == null) {
            Bukkit.getLogger().severe("[ResourceRush] Couldn't load the lobby location");
            return;
        }

        for (String locations : section.getKeys(false)) {
            ConfigurationSection locationSection = section.getConfigurationSection(locations);
            if (locationSection == null) {
                Bukkit.getLogger().warning("[ResourceRush] Not a single point of the game is set");
                continue;
            }

            if (locationSection.isLocation("locations")) {
                List<Map<?, ?>> rawList = locationSection.getMapList("locations");

                for (Map<?, ?> map : rawList) {
                    try {
                        String world = (String) map.get("world");
                        double x = ((Number) map.get("x")).doubleValue();
                        double y = ((Number) map.get("y")).doubleValue();
                        double z = ((Number) map.get("z")).doubleValue();
                        float yaw = ((Number) map.get("yaw")).floatValue();
                        float pitch = ((Number) map.get("pitch")).floatValue();

                        Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                        // ResourceRush.getInstance().getGameManager().setGameLocation((List<Location>) loc);
                    } catch (Exception e) {
                        Bukkit.getLogger().warning("[ResourceRush] Failed to load one of the game positions '" + locations + "'");
                        e.printStackTrace();
                    }
                }
            }

            // ResourceRush.getInstance().getGameManager().setLobbyLocation(lobbyLocation);
        }

    }

    public void saveConfig() {
        try {
            dataConfig.save(configData);
        } catch (IOException e) {
            Bukkit.getLogger().severe("The locations.yml file could not be saved");
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return dataConfig;
    }
}

package whyI.resourceRush.data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import whyI.resourceRush.ResourceRush;

import java.io.File;

public class MessageConfiguration {

    private File configData = null;
    private FileConfiguration dataConfig = null;

    public MessageConfiguration() {
        loadMessageConfig();
    }

    public void loadMessageConfig() {
        try {
            this.configData = new File(ResourceRush.getInstance().getDataFolder(), "messages.yml");
            if (!this.configData.exists()) {
                ResourceRush.getInstance().saveResource("messages.yml", false);
            }

            this.dataConfig = YamlConfiguration.loadConfiguration(this.configData);
        } catch (Exception e) {
            Bukkit.getLogger().severe("[ResourceRush] Failed to create/load the message configuration");
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return dataConfig;
    }
}

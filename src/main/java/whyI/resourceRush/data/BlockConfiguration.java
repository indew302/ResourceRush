package whyI.resourceRush.data;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.managers.game.GameManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BlockConfiguration {

    private File configData = null;
    private FileConfiguration dataConfig = null;

    public BlockConfiguration() {
        loadBlocksConfig();
        loadBlocks();
    }

    public void loadBlocksConfig() {
        try {
            this.configData = new File(ResourceRush.getInstance().getDataFolder(), "blocks.yml");
            if (!this.configData.exists()) {
                ResourceRush.getInstance().saveResource("blocks.yml", false);
            }

            this.dataConfig = YamlConfiguration.loadConfiguration(this.configData);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Couldn't create/upload the blocks.yml config");
            e.printStackTrace();
        }
    }

    private void loadBlocks() {
        List<String> materialList = getConfig().getStringList("materials.blocks");
        if(materialList.isEmpty()) {
            Bukkit.getLogger().warning("[ResourceRush] No blocks found in configuration.");
            return;
        }

        GameManager gameManager = ResourceRush.getInstance().getGameManager();
        if(gameManager == null) {
            Bukkit.getLogger().warning("[ResourceRush] GameManager can't initialization!");
            return;
        }

        try {
            for (int i = 0; i < materialList.size(); i++) {
                String materialName = materialList.get(i);

                if (materialName == null || materialName.trim().isEmpty()) {
                    Bukkit.getLogger().warning("[ResourceRush] Found null or empty material name in blocks.yml");
                    continue;
                }

                Material material = Material.getMaterial(materialName);
                if (material == null) {
                    Bukkit.getLogger().warning("[ResourceRush] Unknown material: " + materialName);
                    continue;
                }

                gameManager.getMaterialList().add(material);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("The plugin could not successfully load all the blocks");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(ResourceRush.getInstance());
        }

        Bukkit.getLogger().info("[ResourceRush] Blocks loading: " + gameManager.getMaterialList().size());
    }

    public void saveConfig() {
        try {
            dataConfig.save(configData);
        } catch (IOException e) {
            Bukkit.getLogger().severe("The blocks.yml file could not be saved");
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return dataConfig;
    }
}

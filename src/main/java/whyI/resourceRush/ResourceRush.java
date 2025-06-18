package whyI.resourceRush;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import whyI.resourceRush.commands.CollectCommand;
import whyI.resourceRush.commands.sub.*;
import whyI.resourceRush.data.BlockConfiguration;
import whyI.resourceRush.data.LocationsConfiguration;
import whyI.resourceRush.listeners.BlockBreak;
import whyI.resourceRush.listeners.DamagePlayer;
import whyI.resourceRush.listeners.PlayerQuit;
import whyI.resourceRush.managers.CommandManager;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.managers.score.ScoreManager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class ResourceRush extends JavaPlugin {

    private static ResourceRush instance;

    private CheckUpdater checkUpdater;

    private CommandManager commandManager;

    private BlockConfiguration blockConfiguration;

    // private LocationsConfiguration locationsConfiguration;

    private ScoreManager scoreManager;

    private GameManager gameManager;

    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();
        Logger log = Bukkit.getLogger();
        try {
            instance = this;

            saveDefaultConfig();

            this.checkUpdater = new CheckUpdater();

            this.gameManager = new GameManager();

            setGameManager(this.gameManager);

            this.scoreManager = new ScoreManager(gameManager);

            this.blockConfiguration = new BlockConfiguration();

            this.blockConfiguration.loadBlocksConfig();

            // this.locationsConfiguration = new LocationsConfiguration();

            // this.locationsConfiguration.loadLocationsConfig();

            try {
                this.commandManager = new CommandManager();

                this.commandManager.registerSubCommand(new CollectJoinSubCommand(gameManager));
                this.commandManager.registerSubCommand(new CollectLeaveSubCommand(gameManager));
                this.commandManager.registerSubCommand(new CollectStartSubCommand(gameManager));
                this.commandManager.registerSubCommand(new CollectStopSubCommand(gameManager));
                // this.commandManager.registerSubCommand(new CollectSetLobbySubCommand(locationsConfiguration, gameManager));
                // this.commandManager.registerSubCommand(new CollectLobbyTpSubCommand());
                this.commandManager.registerSubCommand(new CollectHelpSubCommand());

                getCommand("collect").setExecutor(new CollectCommand(commandManager));
            } catch (Exception e) {
                log.severe("Commands can't initialization");
                e.printStackTrace();
                pm.disablePlugin(this);
            }

            try {
                pm.registerEvents(new BlockBreak(gameManager), this);
                pm.registerEvents(new DamagePlayer(gameManager), this);
                pm.registerEvents(new PlayerQuit(gameManager), this);
            } catch (Exception e) {
                log.severe("Events can't initialization");
                e.printStackTrace();
                pm.disablePlugin(this);
            }

        } catch (Exception e) {
            log.severe("Plugin can't initialization");
            e.printStackTrace();
            pm.disablePlugin(this);
        }

    }

    public Map<String, Object> serializeLocation(Location loc) {
        Map<String, Object> map = new HashMap<>();
        map.put("world", loc.getWorld().getName());
        map.put("x", loc.getX());
        map.put("y", loc.getY());
        map.put("z", loc.getZ());
        map.put("yaw", loc.getYaw());
        map.put("pitch", loc.getPitch());
        return map;
    }

    public static ResourceRush getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void onDisable() {
        if(this.gameManager.isStarted()) {
            this.gameManager.getMaterialList().clear();
            this.gameManager.getPlayerPoints().clear();
            this.gameManager.getPlayerInventory().clear();

            this.gameManager.setStarted(false);
        }
        instance = null;
    }
}

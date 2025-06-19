package whyI.resourceRush;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import whyI.resourceRush.commands.CollectCommand;
import whyI.resourceRush.commands.sub.*;
import whyI.resourceRush.data.BlockConfiguration;
import whyI.resourceRush.data.MessageConfiguration;
import whyI.resourceRush.listeners.BlockBreak;
import whyI.resourceRush.listeners.DamagePlayer;
import whyI.resourceRush.listeners.PlayerQuit;
import whyI.resourceRush.managers.CommandManager;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.managers.score.ScoreManager;
import whyI.resourceRush.utility.MessageUtils;

import java.util.UUID;
import java.util.logging.Logger;

public final class ResourceRush extends JavaPlugin {

    private static ResourceRush instance;

    private CheckUpdater checkUpdater;

    private CommandManager commandManager;

    private BlockConfiguration blockConfiguration;

    private MessageConfiguration messageConfiguration;

    private MessageUtils messageUtils;

    private ScoreManager scoreManager;

    private GameManager gameManager;

    Logger log = Bukkit.getLogger();

    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();

        try {
            instance = this;

            saveDefaultConfig();

            this.checkUpdater = new CheckUpdater();

            this.gameManager = new GameManager();

            setGameManager(this.gameManager);

            this.scoreManager = new ScoreManager(this.gameManager);

            this.blockConfiguration = new BlockConfiguration();
            this.blockConfiguration.loadBlocksConfig();

            this.messageConfiguration = new MessageConfiguration();
            this.messageConfiguration.loadMessageConfig();

            this.messageUtils = new MessageUtils(messageConfiguration);

            try {
                this.commandManager = new CommandManager(messageUtils);

                this.commandManager.registerSubCommand(new CollectJoinSubCommand(this.gameManager));
                this.commandManager.registerSubCommand(new CollectLeaveSubCommand(this.gameManager));
                this.commandManager.registerSubCommand(new CollectStartSubCommand(this.gameManager));
                this.commandManager.registerSubCommand(new CollectStopSubCommand(this.gameManager));
                this.commandManager.registerSubCommand(new CollectReloadSubCommand(this.blockConfiguration));
                this.commandManager.registerSubCommand(new CollectHelpSubCommand());

                getCommand("collect").setExecutor(new CollectCommand(this.commandManager));
            } catch (Exception e) {
                log.severe("Commands can't initialization");
                e.printStackTrace();
                pm.disablePlugin(this);
            }

            try {
                pm.registerEvents(new BlockBreak(this.gameManager), this);
                pm.registerEvents(new DamagePlayer(this.gameManager), this);
                pm.registerEvents(new PlayerQuit(this.gameManager), this);
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

    public static ResourceRush getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public ScoreManager getScoreManager() {
        return this.scoreManager;
    }

    public MessageUtils getMessageUtils() {
        return messageUtils;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void onDisable() {
        try {
            if (this.gameManager.isStarted()) {
                this.gameManager.getMaterialList().clear();
                this.gameManager.getPlayerPoints().clear();
                for (UUID uuids : this.gameManager.getPlayerInventory().keySet()) {
                    Player player = Bukkit.getPlayer(uuids);
                    player.getInventory().clear();

                    ItemStack[] contents = this.gameManager.getPlayerInventory().get(uuids);
                    player.getInventory().setContents(contents);
                }

                this.gameManager.getPlayerInventory().clear();

                this.scoreManager.clearAllScoreboard();
                this.gameManager.setStarted(false);
            }
        } catch (Exception e) {
            log.severe("[ResourceRush] Couldn't stop the plugin correctly");
            e.printStackTrace();
        }

        instance = null;
    }
}
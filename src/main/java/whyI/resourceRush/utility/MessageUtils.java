package whyI.resourceRush.utility;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import whyI.resourceRush.data.MessageConfiguration;

public class MessageUtils {

    private MessageConfiguration messageConfiguration;

    public String _noPermission;
    public String _blocksbreakpoints;
    public String _alreadyingame;
    public String _gamenotrunning;
    public String _gamenotleave;
    public String _ifplayerincreative;

    public MessageUtils(MessageConfiguration messageConfiguration) {
        this.messageConfiguration = messageConfiguration;
        FileConfiguration config = this.messageConfiguration.getConfig();

        if(config == null) {
            Bukkit.getLogger().severe("[ResourceRush] Failed to initialize configuration retrieval, messages will not be loaded.");
            return;
        }

        this._noPermission = config.getString("noPermission");
        this._blocksbreakpoints = config.getString("blocksbreak-points");
        this._alreadyingame = config.getString("alreadyingame");
        this._gamenotrunning = config.getString("game-not-running");
        this._gamenotleave = config.getString("game-not-leave");
        this._ifplayerincreative = config.getString("if-player-in-creative");
    }
}

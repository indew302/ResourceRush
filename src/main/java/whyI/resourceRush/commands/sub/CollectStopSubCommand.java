package whyI.resourceRush.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;

public class CollectStopSubCommand implements SubCommand {
    private GameManager gameManager;

    public CollectStopSubCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public String getName() {
        return "stop";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("That command can usage only player.");
            return;
        }

        if (args.length > 1)
            return;

        FileConfiguration config = ResourceRush.getInstance().getConfig();
        String _noPerm = config.getString("noPermission");
        if(_noPerm == null) {
            player.sendMessage(Colorize.format("&4In the configuration, you need to specify the text noPermission"));
            return;
        }

        if (!player.hasPermission("resourcerush.admin") || !player.isOp()) {
            player.sendMessage(Colorize.format(_noPerm));
            return;
        }

        if (!this.gameManager.isStarted()) {
            player.sendMessage(Colorize.format("&4Error, the game is not currently running."));
            return;
        }

        this.gameManager.endGame();
        player.sendMessage(Colorize.format("You stopped game 'ResourceRush'"));
    }
}

package whyI.resourceRush.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;
import whyI.resourceRush.utility.MessageUtils;

public class CollectStartSubCommand implements SubCommand {
    private GameManager gameManager;

    public CollectStartSubCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public String getName() {
        return "start";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("That command can usage only player.");
            return;
        }

        if (args.length > 1)
            return;

        MessageUtils messageUtils = ResourceRush.getInstance().getMessageUtils();
        if(messageUtils._noPermission == null) {
            player.sendMessage(Colorize.format("&4In the configuration, you need to specify the text noPermission"));
            return;
        }

        if (!player.hasPermission("resourcerush.admin") || !player.isOp()) {
            player.sendMessage(Colorize.format(messageUtils._noPermission));
            return;
        }

        if (this.gameManager.isStarted()) {
            player.sendMessage(Colorize.format("&4Error, the game has already been triggered."));
            return;
        }

        this.gameManager.startGame();
        player.sendMessage(Colorize.format("You starting game 'ResourceRush'"));
    }
}

package whyI.resourceRush.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;

public class CollectStartSubCommand implements SubCommand {

    private GameManager gameManager;

    public CollectStartSubCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("That command can usage only player.");
            return;
        }

        if(args.length > 1) return;

        FileConfiguration config = ResourceRush.getInstance().getConfig();

        String _noPerm = config.getString("noPermission");

        if(!player.hasPermission("resourcerush.admin") || !player.isOp()) {
            player.sendMessage(Colorize.format(_noPerm));
            return;
        }

        if(gameManager.isStarted()) {
            player.sendMessage(Colorize.format("&4Error, the game has already been triggered."));
            return;
        }

        gameManager.startGame();
        player.sendMessage(Colorize.format("You starting game 'ResourceRush'"));
    }
}

package whyI.resourceRush.commands.sub;

import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;
import whyI.resourceRush.utility.MessageUtils;

public class CollectLeaveSubCommand implements SubCommand {
    private GameManager gameManager;

    public CollectLeaveSubCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public String getName() {
        return "leave";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("That command can usage only player.");
            return;
        }

        MessageUtils messageUtils = ResourceRush.getInstance().getMessageUtils();
        if(messageUtils._noPermission == null) {
            player.sendMessage(Colorize.format("&4In the configuration, you need to specify the text noPermission"));
            return;
        }

        if (!player.hasPermission("resourcerush.user") || !player.hasPermission("resourcerush.admin") || !player.isOp()) {
            player.sendMessage(Colorize.format(messageUtils._noPermission));
            return;
        }

        UUID uuid = player.getUniqueId();

        if (args.length > 1)
            return;

        if (!this.gameManager.isStarted()) {
            player.sendMessage(Colorize.format(messageUtils._alreadyingame));
            return;
        }

        if (!this.gameManager.getPlayerPoints().containsKey(uuid)) {
            player.sendMessage(Colorize.format(messageUtils._gamenotleave));
            return;
        }

        player.getInventory().clear();

        ItemStack[] contents = this.gameManager.getPlayerInventory().get(uuid);
        player.getInventory().setContents(contents);

        this.gameManager.getPlayerPoints().remove(uuid);
        this.gameManager.getPlayerInventory().remove(uuid);

        ResourceRush.getInstance().getScoreManager().clearScoreboard(player);
    }
}

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

public class CollectLeaveSubCommand implements SubCommand {
    private GameManager gameManager;

    public CollectLeaveSubCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public String getName() {
        return "leave";
    }

    public void execute(CommandSender sender, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player)sender;
        } else {
            sender.sendMessage("That command can usage only player.");
            return;
        }
        FileConfiguration config = ResourceRush.getInstance().getConfig();
        String _noPerm = config.getString("noPermission");
        if (!player.hasPermission("resourcerush.user") || !player.hasPermission("resourcerush.admin") || !player.isOp()) {
            player.sendMessage(Colorize.format(_noPerm));
            return;
        }
        UUID uuid = player.getUniqueId();
        if (args.length > 1)
            return;
        if (!this.gameManager.isStarted()) {
            player.sendMessage(Colorize.format("&4The game has not started yet."));
            return;
        }
        if (!this.gameManager.getPlayerPoints().containsKey(uuid)) {
            player.sendMessage(Colorize.format("&4You are not in the game!"));
            return;
        }
        player.getInventory().clear();
        ItemStack[] contents = (ItemStack[])this.gameManager.getPlayerInventory().get(uuid);
        player.getInventory().setContents(contents);
        this.gameManager.getPlayerPoints().remove(uuid);
        this.gameManager.getPlayerInventory().remove(uuid);
        ResourceRush.getInstance().getScoreManager().clearScoreboard(player);
    }
}

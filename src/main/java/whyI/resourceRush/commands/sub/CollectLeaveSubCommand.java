package whyI.resourceRush.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;

import java.util.UUID;

public class CollectLeaveSubCommand implements SubCommand {

    private GameManager gameManager;

    public CollectLeaveSubCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("That command can usage only player.");
            return;
        }

        FileConfiguration config = ResourceRush.getInstance().getConfig();

        String _noPerm = config.getString("noPermission");

        if(!player.hasPermission("resourcerush.user") || !player.hasPermission("resourcerush.admin") || !player.isOp()) {
            player.sendMessage(Colorize.format(_noPerm));
            return;
        }

        UUID uuid = player.getUniqueId();

        if(args.length > 1) return;

        if(!gameManager.isStarted()) {
            player.sendMessage(Colorize.format("&4The game has not started yet."));
            return;
        }

         if (!gameManager.getPlayerPoints().containsKey(uuid)) {
             player.sendMessage(Colorize.format("&4You are not in the game!"));
            return;
         }

        player.getInventory().clear();

        ItemStack[] contents = gameManager.getPlayerInventory().get(uuid);

        player.getInventory().setContents(contents);

        gameManager.getPlayerPoints().remove(uuid);
        gameManager.getPlayerInventory().remove(uuid);

        ResourceRush.getInstance().getScoreManager().clearScoreboard(player);
    }
}

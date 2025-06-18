package whyI.resourceRush.commands.sub;

import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;

public class CollectJoinSubCommand implements SubCommand {
    private GameManager gameManager;

    public CollectJoinSubCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public String getName() {
        return "join";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("That command can usage only player.");
            return;
        }

        FileConfiguration config = ResourceRush.getInstance().getConfig();
        String _noPerm = config.getString("noPermission");
        if(_noPerm == null) {
            player.sendMessage(Colorize.format("&4In the configuration, you need to specify the text noPermission"));
            return;
        }

        if (!player.hasPermission("resourcerush.user") || !player.hasPermission("resourcerush.admin") || !player.isOp()) {
            player.sendMessage(Colorize.format(_noPerm));
            return;
        }
        UUID uuid = player.getUniqueId();
        if (args.length > 1)
            return;
        if (this.gameManager.getPlayerPoints().containsKey(uuid)) {
            player.sendMessage(Colorize.format("&4You're already connected to the game."));
            return;
        }
        if (!this.gameManager.isStarted()) {
            player.sendMessage(Colorize.format("&4The game has not started yet."));
            return;
        }
        this.gameManager.getPlayerPoints().put(uuid, (Integer)this.gameManager.getPlayerPoints().getOrDefault(uuid, Integer.valueOf(0)));
        ItemStack[] contents = player.getInventory().getContents();
        this.gameManager.getPlayerInventory().put(uuid, contents);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMeta = pickaxe.getItemMeta();
        itemMeta.setDisplayName(Colorize.format("&fPickaxe &9Diamond"));
        pickaxe.setItemMeta(itemMeta);
        player.getInventory().clear();
        player.getInventory().setItem(4, pickaxe);
        ResourceRush.getInstance().getScoreManager().createScoreboard(player);
    }
}

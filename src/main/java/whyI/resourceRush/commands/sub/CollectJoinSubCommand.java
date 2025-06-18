package whyI.resourceRush.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

import java.util.UUID;

public class CollectJoinSubCommand implements SubCommand {

    private GameManager gameManager;

    public CollectJoinSubCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "join";
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

        if(gameManager.getPlayerPoints().containsKey(uuid)) {
            player.sendMessage(Colorize.format("&4You're already connected to the game."));
            return;
        }

        if(!gameManager.isStarted()) {
            player.sendMessage(Colorize.format("&4The game has not started yet."));
            return;
        }

        gameManager.getPlayerPoints().put(uuid, gameManager.getPlayerPoints().getOrDefault(uuid, 0));

        ItemStack[] contents = player.getInventory().getContents();
        gameManager.getPlayerInventory().put(uuid, contents);

        // player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20.0);
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

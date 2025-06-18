package whyI.resourceRush.commands.sub;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.utility.Colorize;

public class CollectLobbyTpSubCommand implements SubCommand {
    @Override
    public String getName() {
        return "tp";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            return;
        }

        if(args.length > 1) return;

        // Location lobby = ResourceRush.getInstance().getGameManager().getLobbyLocation();
        //if(lobby == null) {
        //    player.sendMessage(Colorize.format("&4The lobby point is not set"));
        //    return;
        //}

        // player.teleport(ResourceRush.getInstance().getGameManager().getLobbyLocation());
    }
}

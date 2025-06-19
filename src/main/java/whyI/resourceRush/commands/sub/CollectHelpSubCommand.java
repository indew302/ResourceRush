package whyI.resourceRush.commands.sub;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.utility.Colorize;
import whyI.resourceRush.utility.MessageUtils;

public class CollectHelpSubCommand implements SubCommand {
    public String getName() {
        return "help";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {

            if(args.length > 1) return;

            List<String> helpMsg = new ArrayList<>();

            helpMsg.add(Colorize.format("&m&7------------------------------------------"));
            helpMsg.add(Colorize.format("&7Usage: &r/collect reload - reloading configurations"));
            helpMsg.add(Colorize.format("&m&7------------------------------------------"));

            for (String a : helpMsg)
                sender.sendMessage(Colorize.format(a));

            return;
        }

        if (args.length > 1) return;

        MessageUtils messageUtils = ResourceRush.getInstance().getMessageUtils();
        if(messageUtils._noPermission == null) {
            player.sendMessage(Colorize.format("&4In the configuration, you need to specify the text noPermission"));
            return;
        }

        if (!player.hasPermission("resourcerush.user") || !player.hasPermission("resourcerush.admin") || !player.isOp()) {
            player.sendMessage(Colorize.format(messageUtils._noPermission));
            return;
        }

        List<String> helpMsg = new ArrayList<>();

        if (player.hasPermission("resourcerush.admin") || player.isOp()) {
            helpMsg.add(Colorize.format("&m&7------------------------------------------"));
            helpMsg.add(Colorize.format("&7Usage: &r/collect reload - reloading configurations"));
            helpMsg.add(Colorize.format("&7Usage: &r/collect start - starting game"));
            helpMsg.add(Colorize.format("&7Usage: &r/collect stop - stopped game"));
            helpMsg.add(Colorize.format("&7Usage: &r/collect join - connecting to the game"));
            helpMsg.add(Colorize.format("&7Usage: &r/collect leave - disconnecting from the game"));
            helpMsg.add(Colorize.format("&m&7------------------------------------------"));
        } else {
            helpMsg.add(Colorize.format("&m&7------------------------------------------"));
            helpMsg.add(Colorize.format("&7Usage: &r/collect join - connecting to the game"));
            helpMsg.add(Colorize.format("&7Usage: &r/collect leave - disconnecting from the game"));
            helpMsg.add(Colorize.format("&m&7------------------------------------------"));
        }

        for (String a : helpMsg)
            player.sendMessage(Colorize.format(a));

    }
}

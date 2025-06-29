package whyI.resourceRush.managers;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.utility.Colorize;
import whyI.resourceRush.utility.MessageUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private Map<String, SubCommand> subCommands = new HashMap<>();

    private MessageUtils messageUtils;

    public CommandManager(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    public void registerSubCommand(SubCommand subCommand) {
        this.subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    public boolean onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            if (args.length == 0) {
                sender.sendMessage(Colorize.format("&7Usage: &r/collect <subcommand>"));
                return true;
            }

            String name = args[0];
            SubCommand sub = this.subCommands.get(name);
            if (sub == null) {
                sender.sendMessage(Colorize.format("&4There is no such sub-command: &r" + name + "\n &eTry using the ('help') sub command"));
                return false;
            }

            sub.execute(sender, Arrays.copyOfRange(args, 1, args.length));
            return true;
        }

        if (!player.hasPermission("resourcerush.user") || !player.hasPermission("resourcerush.admin") || !player.isOp()) {
            player.sendMessage(Colorize.format(messageUtils._noPermission));
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(Colorize.format("&7Usage: &r/collect <subcommand>"));
            return true;
        }

        String name = args[0];
        SubCommand sub = this.subCommands.get(name);
        if (sub == null) {
            player.sendMessage(Colorize.format("&4There is no such sub-command: &r" + name + "\n &eTry using the ('help') sub command"));
            return false;
        }

        sub.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }
}
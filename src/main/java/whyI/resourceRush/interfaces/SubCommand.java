package whyI.resourceRush.interfaces;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    public String getName();

    void execute(CommandSender sender, String[] args);
}

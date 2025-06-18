package whyI.resourceRush.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import whyI.resourceRush.managers.CommandManager;

public class CollectCommand implements CommandExecutor {

    private CommandManager commandManager;

    public CollectCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return commandManager.onCommand(sender, args);
    }
}

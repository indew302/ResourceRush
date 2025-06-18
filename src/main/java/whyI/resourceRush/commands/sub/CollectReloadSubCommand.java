package whyI.resourceRush.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.data.BlockConfiguration;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.utility.Colorize;

public class CollectReloadSubCommand implements SubCommand {

    private BlockConfiguration blockConfiguration;

    public CollectReloadSubCommand(BlockConfiguration blockConfiguration) {
        this.blockConfiguration = blockConfiguration;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            if(args.length > 1) return;

            try {
                ResourceRush.getInstance().reloadConfig();
                this.blockConfiguration.loadBlocksConfig();

                sender.sendMessage(Colorize.format("&aConfigurations have been successfully reset."));
            } catch (Exception e) {
                Bukkit.getLogger().severe("[ResourceRush] The configurations could not be reset.");
                e.printStackTrace();
            }

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

        if(args.length > 1) return;

        try {
            ResourceRush.getInstance().reloadConfig();
            this.blockConfiguration.loadBlocksConfig();

            player.sendMessage(Colorize.format("&aConfigurations have been successfully reset."));
        } catch (Exception e) {
            Bukkit.getLogger().severe("[ResourceRush] The configurations could not be reset.");
            e.printStackTrace();
        }
    }
}

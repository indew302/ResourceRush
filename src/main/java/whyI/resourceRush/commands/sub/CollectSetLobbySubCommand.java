package whyI.resourceRush.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.data.LocationsConfiguration;
import whyI.resourceRush.interfaces.SubCommand;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;

public class CollectSetLobbySubCommand implements SubCommand {

    private LocationsConfiguration locationsConfiguration;

    private GameManager gameManager;

    public CollectSetLobbySubCommand(LocationsConfiguration locationsConfiguration, GameManager gameManager) {
        this.locationsConfiguration = locationsConfiguration;
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "setlobby";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("That command can usage only player.");
            return;
        }

        FileConfiguration config = this.locationsConfiguration.getConfig();

        if(args.length > 1) return;

        Location location = player.getLocation();

        try {
            config.set("locations.lobby", ResourceRush.getInstance().serializeLocation(location));

            this.locationsConfiguration.saveConfig();

            Location locationLobby = config.getLocation("locations.lobby");
            // this.gameManager.setLobbyLocation(location);

            player.sendMessage(Colorize.format("&aYou have successfully set up a lobby point"));
        } catch (Exception e) {
            player.sendMessage(Colorize.format("&4Please check the console"));
            Bukkit.getLogger().severe("[ResourceRush] Couldn't save lobby location: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

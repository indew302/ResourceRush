package whyI.resourceRush.listeners;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import whyI.resourceRush.managers.game.GameManager;

public class PlayerQuit implements Listener {
    private GameManager gameManager;

    public PlayerQuit(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!this.gameManager.isStarted())
            return;

        try {
            if (this.gameManager.getPlayerPoints().containsKey(uuid)) {
                player.getInventory().clear();

                ItemStack[] contents = this.gameManager.getPlayerInventory().get(uuid);
                player.getInventory().setContents(contents);

                this.gameManager.getPlayerPoints().remove(uuid);
                this.gameManager.getPlayerInventory().remove(uuid);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Failed to initialize the player's exit");
            e.printStackTrace();
        }
    }
}

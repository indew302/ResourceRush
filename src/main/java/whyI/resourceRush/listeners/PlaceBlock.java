package whyI.resourceRush.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import whyI.resourceRush.managers.game.GameManager;

import java.util.UUID;

public class PlaceBlock implements Listener {

    private GameManager gameManager;

    public PlaceBlock(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        UUID uuid = player.getUniqueId();

        if(!gameManager.isStarted()) return;

        if(!gameManager.getPlayerPoints().containsKey(uuid)) return;

        event.setCancelled(true);
    }
}

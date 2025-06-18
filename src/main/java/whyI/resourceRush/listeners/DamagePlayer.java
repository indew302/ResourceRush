package whyI.resourceRush.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import whyI.resourceRush.managers.game.GameManager;

import java.util.UUID;

public class DamagePlayer implements Listener {

    private GameManager gameManager;

    public DamagePlayer(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        UUID uuid = player.getUniqueId();

        if(!gameManager.isStarted()) return;
        if(!gameManager.getPlayerPoints().containsKey(uuid)) return;

        event.setCancelled(true);

    }
}

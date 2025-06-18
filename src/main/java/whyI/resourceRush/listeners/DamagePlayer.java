package whyI.resourceRush.listeners;

import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import whyI.resourceRush.managers.game.GameManager;

public class DamagePlayer implements Listener {
    private GameManager gameManager;

    public DamagePlayer(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Player player;
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            player = (Player)entity;
        } else {
            return;
        }
        UUID uuid = player.getUniqueId();
        if (!this.gameManager.isStarted())
            return;
        if (!this.gameManager.getPlayerPoints().containsKey(uuid))
            return;
        event.setCancelled(true);
    }
}

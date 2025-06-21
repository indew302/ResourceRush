package whyI.resourceRush.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;
import whyI.resourceRush.utility.MessageUtils;

public class BlockBreak implements Listener {

    private GameManager gameManager;

    public BlockBreak(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        FileConfiguration config = ResourceRush.getInstance().getConfig();
        MessageUtils messageUtils = ResourceRush.getInstance().getMessageUtils();
        Player player = event.getPlayer();

        Sound _blocksbreaksound = Sound.valueOf(config.getString("blocksbreak-sound"));
        Boolean _breaksound = Boolean.valueOf(config.getBoolean("break-sound"));
        if (_blocksbreaksound == null)
            return;

        UUID uuid = player.getUniqueId();
        Block block = event.getBlock();

        if (!this.gameManager.isStarted())
            return;

        if (!this.gameManager.getPlayerPoints().containsKey(uuid))
            return;

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            player.sendMessage(Colorize.format(messageUtils._ifplayerincreative));
            event.setCancelled(true);
            return;
        }

        try {
            for (int i = 0; i < gameManager.getMaterialList().size(); i++) {
                if (gameManager.getMaterialList() == null)
                    return;

                if(event.getBlock().getType() != gameManager.getMaterialList().get(i))
                    event.setCancelled(true);

                if (block.getType() == gameManager.getMaterialList().get(i)) {
                    gameManager.addPoints(player, Integer.valueOf(2));
                    new BukkitRunnable() {
                        int timeLeft = 1;
                        public void run() {
                            if(timeLeft <= 0) {
                                event.getBlock().setType(Material.AIR);
                                cancel();
                                return;
                            }

                            if(timeLeft > 0) {
                                event.getBlock().setType(Material.COBBLESTONE);
                            }

                            timeLeft--;
                        }
                    }.runTaskTimer(ResourceRush.getInstance(),  0L,  20L);

                    event.setDropItems(false);

                    final String v = messageUtils._blocksbreakpoints.replace("{blockType}", String.valueOf(block.getType()))
                            .replace("{points}", String.valueOf(gameManager.getPoints(player)));

                    if (_breaksound.booleanValue())
                        player.playSound(player.getLocation(), _blocksbreaksound, 1.0F, 2.0F);

                    player.sendMessage(Colorize.format(v));

                    ResourceRush.getInstance().getScoreManager().updateScoreboard(player);
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Couldn't get data from the block configuration");
            e.printStackTrace();
        }
    }
}
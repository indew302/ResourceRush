package whyI.resourceRush.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;

import java.util.UUID;

public class BlockBreak implements Listener {

    private GameManager gameManager;

    public BlockBreak(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        FileConfiguration config = ResourceRush.getInstance().getConfig();
        Player player = event.getPlayer();

        String _blocksbreak = config.getString("blocksbreak-points");
        Sound _blocksbreaksound = Sound.valueOf(config.getString("blocksbreak-sound"));
        Boolean _breaksound = config.getBoolean("break-sound");
        if(_blocksbreaksound == null) return;

        UUID uuid = player.getUniqueId();

        Block block = event.getBlock();
        if(!gameManager.isStarted()) return;

        if(!gameManager.getPlayerPoints().containsKey(uuid)) return;

        if(player.getGameMode().equals(GameMode.CREATIVE)) {
            player.sendMessage(Colorize.format("&4While you're in CREATIVE mode, you can't break blocks and earn points."));
            event.setCancelled(true);
            return;
        }

        for(int i = 0; i < gameManager.getMaterialList().size(); i++) {
            if(gameManager.getMaterialList() == null) return;

            if(block.getType() == gameManager.getMaterialList().get(i)) {
                gameManager.addPoints(player, 2);
                event.setDropItems(false);

                String _blockbreak = _blocksbreak.replace("{blockType}", String.valueOf(block.getType()))
                                .replace("{points}", String.valueOf(gameManager.getPoints(player)));

                if(_breaksound)
                    player.playSound(player.getLocation(), _blocksbreaksound, 1, 2);


                player.sendMessage(Colorize.format(_blockbreak));

                ResourceRush.getInstance().getScoreManager().updateScoreboard(player);
            }
        }

    }
}

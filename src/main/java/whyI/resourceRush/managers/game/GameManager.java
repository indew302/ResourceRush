package whyI.resourceRush.managers.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.utility.Colorize;

public class GameManager {

    private Map<UUID, Integer> playerPoints = new HashMap<>();

    private Map<UUID, ItemStack[]> playerInventory = (Map)new HashMap<>();

    private List<Material> materialList = new ArrayList<>();

    private BukkitTask bukkitTask;

    private boolean isStarted;

    private void broadcastStartGame() {
        FileConfiguration config = ResourceRush.getInstance().getConfig();
        for(Player players : Bukkit.getOnlinePlayers()) {
            List<String> broadcast = config.getStringList("broadcastStart");
            for(String broadcastStarting : broadcast) {
                players.sendMessage(Colorize.format(broadcastStarting));
            }
        }
    }

    public void startGame() {
        this.isStarted = true;

        for (Player players : Bukkit.getOnlinePlayers()) {
            UUID uuid = players.getUniqueId();

            if (this.playerPoints.containsKey(uuid))
                return;

        }

        FileConfiguration config = ResourceRush.getInstance().getConfig();

        broadcastStartGame();

        bukkitTask = new BukkitRunnable() {
            int timeLeft = config.getInt("game.expire-time") * 60;

            public void run() {
                if (this.timeLeft <= 0) {
                    cancel();
                    endGame();
                    return;
                }

                int minutes = this.timeLeft / 60;
                int secs = this.timeLeft % 60;

                for (UUID uuid : GameManager.this.playerPoints.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);

                    if (player == null || !player.isOnline())
                        continue;

                    if(this.timeLeft == 60)
                        player.sendMessage(Colorize.format("&e1 minute(s) left."));

                    if (this.timeLeft == 30)
                        player.sendMessage(Colorize.format("&e30 second(s) left."));

                    if (this.timeLeft <= 10)
                        player.sendMessage(Colorize.format("&e" + this.timeLeft + " second(s) left."));
                }

                this.timeLeft--;
            }

        }.runTaskTimer(ResourceRush.getInstance(), 0L, 20L);
    }

    public void endGame() {
        this.isStarted = false;
        for (Player players : Bukkit.getOnlinePlayers()) {
            UUID uuid = players.getUniqueId();

            if (!this.playerPoints.containsKey(uuid))
                return;

            UUID winner = Collections.max(playerPoints.entrySet(), Map.Entry.comparingByValue()).getKey();
            int winnerPoint = Collections.max(playerPoints.entrySet(), Map.Entry.comparingByValue()).getValue();

            OfflinePlayer winnerPlayer = Bukkit.getOfflinePlayer(winner);
            String winnerName = (winnerPlayer.getName() != null) ? winnerPlayer.getName() : "Unknown";

            List<String> winnerMessage = ResourceRush.getInstance().getConfig().getStringList("winner-message");

            try {
                for (UUID uuids : this.playerPoints.keySet()) {
                    Player player = Bukkit.getPlayer(uuids);

                    if (player == null)
                        return;

                    player.getInventory().clear();

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.0F);

                    for (String msg : winnerMessage) {
                        String a = msg.replace("{winnerName}", winnerName).replace("{winnerPoints}", String.valueOf(winnerPoint));
                        player.sendMessage(Colorize.format(a));
                    }

                    ItemStack[] contents = this.playerInventory.get(uuids);
                    player.getInventory().setContents(contents);

                    ResourceRush.getInstance().getScoreManager().clearScoreboard(player);
                }
            } catch (Exception e) {
                Bukkit.getLogger().severe("It was not possible to announce the winner, as well as to return the items " + e.getMessage());
                e.printStackTrace();
            }

            bukkitTask.cancel();

            removePlayersPoints();
            removePlayerInventoryContents();
        }
    }

    public void addPoints(Player player, Integer points) {
        try {
            UUID uuid = player.getUniqueId();

            if (!this.playerPoints.containsKey(uuid))
                return;

            int point = this.playerPoints.get(uuid);

            int v = point + points;

            this.playerPoints.replace(uuid, point, v);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Couldn't add points to player " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removePlayersPoints() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();

                if (!this.playerPoints.containsKey(uuid))
                    return;

                this.playerPoints.remove(uuid);
            }

        } catch (Exception e) {
            Bukkit.getLogger().severe("Couldn't clear HashMap of playerPoints " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removePlayerInventoryContents() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();

                if (!this.playerInventory.containsKey(uuid))
                    return;

                this.playerInventory.remove(uuid);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Couldn't clear HashMap of playerInventory " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getPoints(Player player) {
        UUID uuid = player.getUniqueId();
        if (this.playerPoints.containsKey(uuid)) {
            int points = this.playerPoints.get(uuid);
            return points;
        }
        return 0;
    }

    public Map<UUID, ItemStack[]> getPlayerInventory() {
        return this.playerInventory;
    }

    public Map<UUID, Integer> getPlayerPoints() {
        return this.playerPoints;
    }

    public List<Material> getMaterialList() {
        return this.materialList;
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    public void setStarted(boolean started) {
        this.isStarted = started;
    }
}
package whyI.resourceRush.managers.game;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.utility.Colorize;

import java.util.*;

public class GameManager {

    private Map<UUID, Integer> playerPoints = new HashMap<>();

    private Map<UUID, ItemStack[]> playerInventory = new HashMap<>();

    private List<Material> materialList = new ArrayList<>();

    // private List<Location> gameLocation = new ArrayList<>();

    // private Location lobbyLocation;

    private boolean isStarted;

    public void startGame() {
        isStarted =  true;

        for(Player players : Bukkit.getOnlinePlayers()) {
            UUID uuid = players.getUniqueId();
            if(playerPoints.containsKey(uuid)) return;

            new BukkitRunnable() {
                int timeLeft = 3 * 60; // 5 минут (можно взять из конфига)

                @Override
                public void run() {
                    if (timeLeft <= 0) {
                        cancel();
                        endGame(); // Завершение игры
                        return;
                    }

                    int minutes = timeLeft / 60;
                    int secs = timeLeft % 60;

                    for (UUID uuid : playerPoints.keySet()) {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null || !player.isOnline()) continue;

                        if (timeLeft == 30) {
                            player.sendMessage(Colorize.format("&e30 second(s) left."));
                        }

                        if (timeLeft <= 10) {
                            player.sendMessage(Colorize.format("&e" + timeLeft + " second(s) left."));
                        }

                    }

                    timeLeft--;
                }
            }.runTaskTimer(ResourceRush.getInstance(), 0L, 20L); // Каждую секунду


        }
    }

    public void endGame() {
        isStarted = false;

        for(Player players : Bukkit.getOnlinePlayers()) {
            UUID uuid = players.getUniqueId();

            if(!playerPoints.containsKey(uuid)) return;

            // int winner = Collections.max(playerPoints.values());

            UUID winner = Collections.max(playerPoints.entrySet(), Map.Entry.comparingByValue()).getKey();
            int winnerPoint = Collections.max(playerPoints.entrySet(), Map.Entry.comparingByValue()).getValue();

            OfflinePlayer winnerPlayer = Bukkit.getOfflinePlayer(winner);
            String winnerName = winnerPlayer.getName() != null ? winnerPlayer.getName() : "Unknown";

            List<String> winnerMessage = ResourceRush.getInstance().getConfig().getStringList("winner-message");

            try {
                for (UUID uuids : playerPoints.keySet()) {
                    Player player = Bukkit.getPlayer(uuids);

                    if (player == null) return;

                    player.getInventory().clear();

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0);

                    for (String msg : winnerMessage) {
                        String a = msg.replace("{winnerName}", winnerName)
                                .replace("{winnerPoints}", String.valueOf(winnerPoint));

                        player.sendMessage(Colorize.format(a));
                    }

                    ItemStack[] contents = playerInventory.get(uuids);

                    player.getInventory().setContents(contents);

                    ResourceRush.getInstance().getScoreManager().clearScoreboard(player);
                }
            } catch (Exception e) {
                Bukkit.getLogger().severe("It was not possible to announce the winner, as well as to return the items " + e.getMessage());
                e.printStackTrace();
            }

            removePlayersPoints();
            removePlayerInventoryContents();

        }
    }

    public void addPoints(Player player, Integer points) {
        try {
            UUID uuid = player.getUniqueId();

            if (!playerPoints.containsKey(uuid)) return;
            int point = playerPoints.get(uuid);

            int v = point + points;

            playerPoints.replace(uuid, point, v);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Couldn't add points to player " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removePlayersPoints() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();

                if (!playerPoints.containsKey(uuid)) return;

                playerPoints.remove(uuid);
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

                if (!playerInventory.containsKey(uuid)) return;

                playerInventory.remove(uuid);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Couldn't clear HashMap of playerInventory " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getPoints(Player player) {
        UUID uuid = player.getUniqueId();

        if(playerPoints.containsKey(uuid)) {
            int points = playerPoints.get(uuid);
            return points;
        }

        return 0;
    }

    public Map<UUID, ItemStack[]> getPlayerInventory() {
        return playerInventory;
    }

    public Map<UUID, Integer> getPlayerPoints() {
        return playerPoints;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

//    public List<Location> getGameLocation() {
//        return gameLocation;
//    }
//
//    public void setGameLocation(List<Location> gameLocation) {
//        this.gameLocation = gameLocation;
//    }
//
//    public Location getLobbyLocation() {
//        return lobbyLocation;
//    }
//
//    public void setLobbyLocation(Location lobbyLocation) {
//        this.lobbyLocation = lobbyLocation;
//    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }
}

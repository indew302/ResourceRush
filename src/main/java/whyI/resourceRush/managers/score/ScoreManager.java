package whyI.resourceRush.managers.score;

import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;

public class ScoreManager {
    private final GameManager gameManager;

    public ScoreManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Scoreboard createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return null;

        FileConfiguration config = ResourceRush.getInstance().getConfig();
        Scoreboard scoreboard = manager.getNewScoreboard();

        String objectiveName = "game_" + UUID.randomUUID().toString().substring(0, 8);
        Objective obj = scoreboard.registerNewObjective(objectiveName, "dummy",
                Colorize.format(config.getString("scoreboard.game.title")));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> lines = config.getStringList("scoreboard.game.lines");
        int points = gameManager.getPoints(player);
        int score = lines.size();

        Set<String> usedLines = new HashSet<>();
        for (String line : lines) {
            String formatted = Colorize.format(line
                    .replace("{player_name}", player.getName())
                    .replace("{points}", String.valueOf(points)));

            // Unique lines
            while (usedLines.contains(formatted)) {
                formatted += ChatColor.RESET;
            }

            usedLines.add(formatted);
            obj.getScore(formatted).setScore(score--);
        }

        player.setScoreboard(scoreboard);
        return scoreboard;
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (obj == null || !obj.getName().startsWith("game_")) {
            createScoreboard(player);
            return;
        }

        FileConfiguration config = ResourceRush.getInstance().getConfig();
        List<String> lines = config.getStringList("scoreboard.game.lines");

        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        int points = gameManager.getPoints(player);
        int score = lines.size();
        Set<String> usedLines = new HashSet<>();

        for (String line : lines) {
            String formatted = Colorize.format(line
                    .replace("{player_name}", player.getName())
                    .replace("{points}", String.valueOf(points)));

            while (usedLines.contains(formatted)) {
                formatted += ChatColor.RESET;
            }

            usedLines.add(formatted);
            obj.getScore(formatted).setScore(score--);
        }
    }

    public void clearScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            player.setScoreboard(manager.getMainScoreboard());
        }
    }

    public void clearAllScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if(manager != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setScoreboard(manager.getMainScoreboard());
            }
        }
    }
}

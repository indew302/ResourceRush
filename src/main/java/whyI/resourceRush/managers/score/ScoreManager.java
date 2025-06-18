package whyI.resourceRush.managers.score;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import whyI.resourceRush.ResourceRush;
import whyI.resourceRush.managers.game.GameManager;
import whyI.resourceRush.utility.Colorize;

import java.util.List;
import java.util.UUID;

public class ScoreManager {

    private GameManager gameManager;

    public ScoreManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Scoreboard createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return null;

        FileConfiguration config = ResourceRush.getInstance().getConfig();
        Scoreboard scoreboard = manager.getNewScoreboard();

        // Generate unique UUID id
        String objectiveName = "game_" + UUID.randomUUID().toString().substring(0, 8);

        Objective obj = scoreboard.registerNewObjective(objectiveName, "dummy",
                Colorize.format(config.getString("scoreboard.game.title")));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> lines = config.getStringList("scoreboard.game.lines");
        int points = gameManager.getPoints(player);
        int score = lines.size();
        for (String line : lines) {
            line = Colorize.format(line
                    .replace("{player_name}", player.getName())
                    .replace("{points}", String.valueOf(points)));
            obj.getScore(line).setScore(score--);
        }

        player.setScoreboard(scoreboard);
        return scoreboard;
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (obj == null || !obj.getName().startsWith("event")) {
            createScoreboard(player);
            return;
        }

        FileConfiguration config = ResourceRush.getInstance().getConfig();
        List<String> lines = config.getStringList("scoreboard.game.lines");

        // Clear old lines
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        int score = lines.size();
        int points = gameManager.getPoints(player);
        for (String line : lines) {
            String formatted = Colorize.format(line
                    .replace("{player_name}", player.getName())
                    .replace("{points}", String.valueOf(points)));
            obj.getScore(formatted).setScore(score--);
        }
    }

    public void clearScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

//    public void clearAllScoreboard() {
//        if(!gameManager.isStarted()) return;
//    }
}

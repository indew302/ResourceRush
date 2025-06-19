package whyI.resourceRush.utility;

import org.bukkit.ChatColor;

public class Colorize {

    public static String format(String a) {
        if(a == null) return "";
        return ChatColor.translateAlternateColorCodes('&', a);
    }
}

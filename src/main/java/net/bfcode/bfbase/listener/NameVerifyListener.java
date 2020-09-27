package net.bfcode.bfbase.listener;

import java.util.regex.Pattern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import net.bfcode.bfbase.BasePlugin;

public class NameVerifyListener implements Listener
{
    private static final Pattern NAME_PATTERN;
    private final BasePlugin plugin;
    
    public NameVerifyListener(final BasePlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLogin(final PlayerLoginEvent event) {
        final PlayerLoginEvent.Result result = event.getResult();
        final String playerName;
        if (result == PlayerLoginEvent.Result.ALLOWED && !NameVerifyListener.NAME_PATTERN.matcher(playerName = (event.getPlayer()).getName()).matches()) {
            this.plugin.getLogger().info("Name verification: " + playerName + " was kicked for having an invalid name (to disable, turn off the name-verification feature in the config of 'Base' plugin)");
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Invalid player name detected.");
        }
    }
    
    static {
        NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{1,16}$");
    }
}

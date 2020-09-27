package net.bfcode.bfbase.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.Listener;

public class PlayerLimitListener implements Listener
{
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLogin(final PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL && event.getPlayer().hasPermission("base.serverfull.bypass")) {
            event.allow();
        }
    }
}

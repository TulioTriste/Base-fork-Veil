package net.bfcode.bfbase.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Event;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerMoveEvent;

import net.bfcode.bfbase.event.PlayerMoveByBlockEvent;

import org.bukkit.event.Listener;

public class MoveByBlockEvent implements Listener
{
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }
        Bukkit.getPluginManager().callEvent((Event)new PlayerMoveByBlockEvent(e.getPlayer(), e.getTo(), e.getFrom()));
    }
}

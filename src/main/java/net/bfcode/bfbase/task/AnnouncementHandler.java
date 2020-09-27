package net.bfcode.bfbase.task;

import java.util.List;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.bfcode.bfbase.BasePlugin;

public class AnnouncementHandler extends BukkitRunnable
{
    private final BasePlugin plugin;
    
    public AnnouncementHandler(final BasePlugin plugin) {
        this.plugin = plugin;
    }
    
    public void run() {
        final List<String> announcements = this.plugin.getServerHandler().getAnnouncements();
        if (!announcements.isEmpty()) {
            final String next = announcements.get(0);
            Bukkit.broadcastMessage(next);
            Collections.rotate(announcements, -1);
        }
    }
}

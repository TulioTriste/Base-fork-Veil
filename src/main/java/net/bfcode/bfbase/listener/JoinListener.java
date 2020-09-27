package net.bfcode.bfbase.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.user.BaseUser;

public class JoinListener implements Listener
{
    private final BasePlugin plugin;
    
    public JoinListener(final BasePlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
        for (final Player player2 : Bukkit.getServer().getOnlinePlayers()) {
            if (!baseUser.getNotes().isEmpty()) {
                player2.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.BOLD + " has the following notes" + ChatColor.RED + '\u2193');
            }
            for (final String notes : baseUser.getNotes()) {
                player2.sendMessage(notes);
            }
        }
        baseUser.tryLoggingName(player);
        baseUser.tryLoggingAddress(player.getAddress().getAddress().getHostAddress());
    }
}

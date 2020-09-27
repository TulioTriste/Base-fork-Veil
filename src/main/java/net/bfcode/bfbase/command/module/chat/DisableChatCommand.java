package net.bfcode.bfbase.command.module.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.handlers.ChatHandler;
import net.md_5.bungee.api.ChatColor;

public class DisableChatCommand extends BaseCommand
{
    @SuppressWarnings("unused")
	private BasePlugin plugin;
	
    public DisableChatCommand(BasePlugin plugin) {
        super("disablechat", "Use to disable chat.");
        this.setAliases(new String[] { "disablechat" });
        this.setUsage("/(command)");
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("command.disablechat")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        if (ChatHandler.isMuted()) {
            ChatHandler.setChatToggled(false);
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Server chat has been " + ChatColor.GREEN + "enabled " + ChatColor.YELLOW + "by " +  ChatColor.LIGHT_PURPLE + sender.getName());
        }
        else {
            ChatHandler.setChatToggled(true);
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Server chat has been " + ChatColor.RED + "disabled " + ChatColor.YELLOW + "by " +  ChatColor.LIGHT_PURPLE + sender.getName());
        }
        return true;
    }
}

package net.bfcode.bfbase.command.module.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.CC;
import net.md_5.bungee.api.ChatColor;

public class ClearChatCommand extends BaseCommand {
	
	@SuppressWarnings("unused")
	private BasePlugin plugin;
	
    public ClearChatCommand(BasePlugin plugin) {
        super("clearchat", "Remove messages for chat.");
        this.setAliases(new String[] { "cc" });
        this.setUsage("/(command)");
        this.plugin = plugin;
    }
	
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("command.chatclear")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        if (sender.hasPermission(this.getPermission())) {
        	for (int i = 0; i < 100; ++i) {
            	Bukkit.broadcastMessage("");
        	}
    	}
        Bukkit.broadcastMessage(CC.translate("&eChat has been cleared by &d" + sender.getName()));
	return true;
	}
}

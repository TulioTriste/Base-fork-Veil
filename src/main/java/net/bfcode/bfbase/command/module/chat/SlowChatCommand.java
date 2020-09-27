package net.bfcode.bfbase.command.module.chat;

import org.bukkit.Bukkit;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.CC;
import net.bfcode.bfbase.util.handlers.ChatHandler;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SlowChatCommand extends BaseCommand
{
    private BasePlugin plugin;
	
    public SlowChatCommand(BasePlugin plugin) {
        super("slowchat", "time for write in chat for players.");
        this.setAliases(new String[] { "chatdelay" });
        this.setUsage("/(command) <message>");
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("command.slowchat")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        if(args.length == 0) {
        	sender.sendMessage(CC.translate("&cUsage: /" + label + " <number>"));
        	return true;
        }
        final Integer time = Ints.tryParse(args[0]);
        if (time == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[0] + "' is not a valid number.");
            return true;
        }
        if (time <= 0) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Chat delay has been set to default!");
            return true;
        }
        ChatHandler.setChatDelay(time);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Chat delay is now of " + time + " by " + sender.getName() + '.');
        return true;
    }
}

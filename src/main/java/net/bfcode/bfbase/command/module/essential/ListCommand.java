package net.bfcode.bfbase.command.module.essential;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfhcf.utils.CC;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.listener.VanishListener;

public class ListCommand extends BaseCommand {
	
    public ListCommand() {
        super("list", "Lists players online");
        this.setAliases(new String[] { "who" });
        this.setUsage("/(command)");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final ArrayList<String> list = new ArrayList<String>();
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (BaseCommand.canSee(sender, player)) {
                if (VanishListener.isVanished(player) == true) {
                    list.add(ChatColor.GRAY + player.getName());
                }
                else {
                    if (!player.hasPermission("command.list.own")) {
                        continue;
                    }
                    list.add(ChatColor.BLUE + player.getName());
                }
            }
        }
        for(String message : BasePlugin.getPlugin().getConfig().getStringList("messages.list-command")) {
        	sender.sendMessage(CC.translate(message
        			.replace("%online%", Bukkit.getServer().getOnlinePlayers().size() + "")));
        }
        return true;
    }
}

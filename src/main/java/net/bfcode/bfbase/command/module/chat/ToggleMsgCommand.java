package net.bfcode.bfbase.command.module.chat;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfhcf.utils.CC;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ToggleMsgCommand extends BaseCommand {
	
	public static ArrayList msg;
    
    public ToggleMsgCommand(final BasePlugin plugin) {
        super("togglemsg", "Toggle msg");
        this.setUsage("/(command)");
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can run this command.");
            return true;
        }
        final Player player = (Player)sender;
        if(!ToggleMsgCommand.msg.contains(player.getName())) {
        	ToggleMsgCommand.msg.add(player.getName());
        	player.sendMessage(CC.translate("&cHas toggleado los mensajes privados."));
        	return true;
        }
        else if(ToggleMsgCommand.msg.contains(player.getName())) {
        	ToggleMsgCommand.msg.remove(player.getName());
        	player.sendMessage(CC.translate("&aHas toggleado los mensajes privados."));
        	return true;
        }
		return true;
    }
    
    static {
        ToggleMsgCommand.msg = new ArrayList();
    }
}
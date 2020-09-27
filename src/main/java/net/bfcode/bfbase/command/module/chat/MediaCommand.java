package net.bfcode.bfbase.command.module.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfhcf.utils.CC;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;

public class MediaCommand extends BaseCommand {

	private BasePlugin plugin;
	
	public MediaCommand(BasePlugin plugin) {
		super("media", "Command of all media necessary ranks");
        this.setAliases(new String[] { "yt", "youtube", "famous", "partner" });
        this.plugin = plugin;
	}

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	Player player = (Player) sender;
    	
    	for(String message : plugin.getConfig().getStringList("messages.media-command")) {
    		player.sendMessage(CC.translate(message
    				.replace("%player%", player.getName())));
    	}
        return true;
    }

}

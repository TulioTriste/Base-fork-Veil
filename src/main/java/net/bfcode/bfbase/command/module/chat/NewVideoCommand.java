package net.bfcode.bfbase.command.module.chat;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.CC;
import net.bfcode.bfbase.util.Cooldowns;
import net.bfcode.bfbase.util.Utils;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class NewVideoCommand extends BaseCommand {
	
	private BasePlugin plugin;
	
	public NewVideoCommand(BasePlugin plugin) {
        super("newvideo", "use for warning record");
        this.setAliases(new String[] { "nuevovideo" });
        this.setUsage("/(command) <message>");
        this.plugin = plugin;
	}
	
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cYou must be player to execute this command."));
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission(Utils.PERMISSION + "newvideo")) {
            player.sendMessage(CC.translate(Utils.NO_PERMISSION));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /" + label + " <videoLink>"));
        }
        else 
            if (Cooldowns.isOnCooldown("newvideo_delay", player)) {
                player.sendMessage(CC.translate("&cPlease, wait &l" + Cooldowns.getCooldownInt("newvideo_delay", player) + " &cto use again."));
                return true;
            }
        	for(String message : plugin.getConfig().getStringList("messages.upload-video")) {
        		Bukkit.broadcastMessage(CC.translate(message
        				.replace("%player%", player.getName())
        				.replace("%link%", StringUtils.join((Object[])args, ' ', 0, args.length).replace("https://www.youtube.com/watch?v=", "youtu.be/")).replace("https://", "")));
        	}
            Cooldowns.addCooldown("newvideo_delay", player, 300);
            return true;
        }
}

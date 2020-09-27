package net.bfcode.bfbase.command.module.chat;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.Cooldowns;
import net.bfcode.bfbase.util.Utils;
import net.bfcode.bfbase.util.handlers.MessagesHandler;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.bfcode.bfhcf.utils.CC;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TwitchCommand extends BaseCommand
{
	private BasePlugin plugin;
	
    public TwitchCommand(BasePlugin plugin) {
        super("twitch", "use for warning your live");
        this.setAliases(new String[] { "rec" });
        this.setUsage("/(command) <message>");
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be player to execute this command.");
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission(Utils.PERMISSION + "twitch")) {
            player.sendMessage(ChatColor.RED + (Utils.NO_PERMISSION));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /" + label + " <twitchChannel>");
        }
        else {
            if (Cooldowns.isOnCooldown("twitch_delay", player)) {
                player.sendMessage(ChatColor.RED + "Please, wait " + ChatColor.BOLD + Cooldowns.getCooldownInt("twitch_delay", player) + ChatColor.RED + " to use again.");
                return true;
            }
            for(String message : plugin.getConfig().getStringList("messages.twitch")) {
            	Bukkit.broadcastMessage(CC.translate(message
            			.replace("%player%", player.getName())
            			.replace("%channel%",  StringUtils.join((Object[])args, ' ', 0, args.length))));
            }
            MessagesHandler.setPrivateMessages(player, true);
            Cooldowns.addCooldown("twitch_delay", player, 300);
        }
        return true;
    }
}

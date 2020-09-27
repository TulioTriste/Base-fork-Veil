package net.bfcode.bfbase.command.module.essential;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.Utils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class BroadcastCommand extends BaseCommand
{
    private BasePlugin plugin;
    
    public BroadcastCommand(BasePlugin plugin) {
        super("broadcast", "public your message");
        this.setAliases(new String[] { "bc", "messageall" });
        this.setUsage("/(command)");
        this.plugin = plugin;
    }
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission(Utils.PERMISSION + "broadcast")) {
            sender.sendMessage(ChatColor.RED + (Utils.NO_PERMISSION));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <-raw> <text...>");
        }
        else {
            if (args[0].startsWith("-raw")) {
                if (args.length >= 2) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringUtils.join((Object[])args, ' ', 1, args.length)));
                }
                else {
                    sender.sendMessage(ChatColor.RED + "&cUsage: /" + label + " <-raw> <text...>");
                }
                return true;
            }
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6Broadcast&7] &e" + StringUtils.join((Object[])args, ' ', 0, args.length)));
        }
        return true;
    } 
}

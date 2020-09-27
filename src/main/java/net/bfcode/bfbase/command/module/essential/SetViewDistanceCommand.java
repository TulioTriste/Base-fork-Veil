package net.bfcode.bfbase.command.module.essential;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.Ints;

public class SetViewDistanceCommand extends BaseCommand
{
    public SetViewDistanceCommand() {
        super("setviewdistance", "Starts global ban wave.");
        this.setAliases(new String[] { "renderdistance", "setrenderdistance", "svd", "srd" });
        this.setUsage("/(command) <vipsonly(true/false)> <distance>");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(this.getUsage());
            return false;
        }
        final boolean vipsonly = args[0].equalsIgnoreCase("true");
        final Integer distance = Ints.tryParse(args[1]);
        if (distance == null) {
            sender.sendMessage(ChatColor.RED + args[1] + " is not an integer u fatass!");
            return false;
        }
        if (distance <= 1) {
            sender.sendMessage(ChatColor.RED + "View distance may not be less or equals to 1.");
            return false;
        }
        if (vipsonly) {
            for (final Player target : Bukkit.getOnlinePlayers()) {
                if (target.hasPermission("command.setviewdistance.vipdistance")) {
                    target.spigot().setViewDistance((int)distance);
                }
            }
            sender.sendMessage(ChatColor.GREEN + "Set distance of VIPS (players with permission utils.vipdistance) to " + distance);
            return true;
        }
        for (final Player target : Bukkit.getOnlinePlayers()) {
            target.spigot().setViewDistance((int)distance);
        }
        return true;
    }
}

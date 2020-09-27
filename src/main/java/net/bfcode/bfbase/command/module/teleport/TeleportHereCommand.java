package net.bfcode.bfbase.command.module.teleport;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.BukkitUtils;

public class TeleportHereCommand extends BaseCommand
{
    public TeleportHereCommand() {
        super("teleporthere", "Teleport to a player to your position.");
        this.setAliases(new String[] { "tphere" });
        this.setUsage("/(command) <playerName>");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        if (BaseCommand.checkNull(sender, args[0])) {
            return true;
        }
        final Player player = (Player)sender;
        BukkitUtils.playerWithNameOrUUID(args[0]).teleport((Entity)player);
        Command.broadcastCommandMessage((CommandSender)player, ChatColor.translateAlternateColorCodes('&', "&eYou have teleported &a" + args[0] + " &eto you."));
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}

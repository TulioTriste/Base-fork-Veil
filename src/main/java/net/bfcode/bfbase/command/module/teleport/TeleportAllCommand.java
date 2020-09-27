package net.bfcode.bfbase.command.module.teleport;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.bfcode.bfbase.command.BaseCommand;

public class TeleportAllCommand extends BaseCommand
{
    public TeleportAllCommand() {
        super("teleportall", "Teleport all players to yourself.");
        this.setAliases(new String[] { "tpall" });
        this.setUsage("/(command)");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        final Player player = (Player)sender;
        for (final Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(player)) {
                if (!player.canSee(target)) {
                    continue;
                }
                target.teleport((Entity)player, PlayerTeleportEvent.TeleportCause.COMMAND);
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oAll players have been teleported to " + player.getName()));
            }
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "All players have been teleported to your location.");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}

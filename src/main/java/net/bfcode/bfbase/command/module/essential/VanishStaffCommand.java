package net.bfcode.bfbase.command.module.essential;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.bfcode.bfbase.command.BaseCommand;

public class VanishStaffCommand extends BaseCommand implements Listener
{
    private static ArrayList<Player> toggled;
    
    public VanishStaffCommand() {
        super("vanishstaff", "Show or hide staff.");
        this.setUsage("/(command)");
    }
    
    @SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this.");
        }
        final Player player = (Player)sender;
        if (!VanishStaffCommand.toggled.contains(player)) {
            VanishStaffCommand.toggled.add(player);
            for (final Player all : Bukkit.getOnlinePlayers()) {
                if (all.hasPermission("rank.staff")) {
                    player.hidePlayer(all);
                }
            }
            player.sendMessage(ChatColor.BLUE + "Hidding all staff.");
            return true;
        }
        if (VanishStaffCommand.toggled.contains(player)) {
            VanishStaffCommand.toggled.remove(player);
            for (final Player all : Bukkit.getOnlinePlayers()) {
                if (all.hasPermission("rank.staff")) {
                    player.showPlayer(all);
                }
            }
            player.sendMessage(ChatColor.BLUE + "Showing all staff.");
            return true;
        }
        return true;
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("rank.staff")) {
            VanishStaffCommand.toggled.forEach(Player -> Player.hidePlayer(event.getPlayer()));
        }
    }
    
    static {
        VanishStaffCommand.toggled = new ArrayList<Player>();
    }
}

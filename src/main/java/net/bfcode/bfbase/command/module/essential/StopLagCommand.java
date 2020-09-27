package net.bfcode.bfbase.command.module.essential;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;

public class StopLagCommand extends BaseCommand
{
    private final BasePlugin plugin;
    
    public StopLagCommand(final BasePlugin plugin) {
        super("stoplag", "Decrease the server lag.");
        this.plugin = plugin;
        this.setUsage("/(command)");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final boolean newMode = !this.plugin.getServerHandler().isDecreasedLagMode();
        this.plugin.getServerHandler().setDecreasedLagMode(newMode);
        final String newModeString = Boolean.toString(newMode);
        for (final World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", newModeString);
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Server is " + (newMode ? (ChatColor.RED + "not") : (ChatColor.GREEN + "now")) + ChatColor.YELLOW + " allowing intensive activity.");
        return true;
    }
}

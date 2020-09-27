package net.bfcode.bfbase.command.module.essential.toggle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;

public class ToggleDonorOnly extends BaseCommand
{
    BasePlugin plugin;
    
    public ToggleDonorOnly(final BasePlugin plugin) {
        super("toggledonoronly", "Turns the server into Donor only mode.");
        this.setUsage("/(command)");
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        this.plugin.getServerHandler().setDonorOnly(!this.plugin.getServerHandler().isDonorOnly());
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Server is " + (this.plugin.getServerHandler().isDonorOnly() ? (ChatColor.GREEN + "now") : (ChatColor.RED + "not")) + ChatColor.YELLOW + " in donor only mode.");
        return true;
    }
}

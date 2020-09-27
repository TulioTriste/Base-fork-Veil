package net.bfcode.bfbase.command.module.essential;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.bfhcf.utils.ConfigurationService;
import net.bfcode.bfbase.command.BaseCommand;
import net.md_5.bungee.api.ChatColor;

public class StoreCommand extends BaseCommand
{
    public StoreCommand() {
        super("store", "Stores");
        this.setAliases(new String[] { "buy" });
        this.setUsage("/(command)]");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "You can purchase ranks at " + ChatColor.LIGHT_PURPLE + ConfigurationService.STORE + ChatColor.YELLOW + ".");
        return true;
    }
}

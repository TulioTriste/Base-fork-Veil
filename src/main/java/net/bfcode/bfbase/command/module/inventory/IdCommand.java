package net.bfcode.bfbase.command.module.inventory;

import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.command.BaseCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class IdCommand extends BaseCommand
{
    public IdCommand() {
        super("id", "Checks the ID/name of an item.");
        this.setUsage("/(command) [itemName]");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this.");
            return true;
        }
        final Player p = (Player)sender;
        if (p.getInventory().getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
            p.sendMessage(ChatColor.YELLOW + "The ID of: " + p.getItemInHand().getType().toString().replace("_", "").toLowerCase() + " is " + p.getItemInHand().getTypeId());
            return true;
        }
        p.sendMessage(ChatColor.RED + "Put something in your hand.");
        return true;
    }
}

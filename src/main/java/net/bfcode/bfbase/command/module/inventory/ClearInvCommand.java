package net.bfcode.bfbase.command.module.inventory;

import java.util.Collections;
import java.util.List;
import org.bukkit.inventory.PlayerInventory;

import net.bfcode.bfbase.BaseConstants;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.BukkitUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClearInvCommand extends BaseCommand
{
    public ClearInvCommand() {
        super("ci", "Clears a players inventory.");
        this.setAliases(new String[] { "clear", "clearinventory" });
        this.setUsage("/(command) <playerName>");
    }
    
    @Override
    public boolean isPlayerOnlyCommand() {
        return true;
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player target;
        if (args.length > 0 && sender.hasPermission(command.getPermission() + ".others")) {
            target = BukkitUtils.playerWithNameOrUUID(args[0]);
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            target = (Player)sender;
        }
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        final PlayerInventory targetInventory = target.getInventory();
        targetInventory.clear();
        targetInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1) });
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Cleared inventory of player " + target.getName() + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}

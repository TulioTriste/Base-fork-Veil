package net.bfcode.bfbase.command.module.inventory;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.BaseConstants;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.staffmode.StaffPriority;
import net.bfcode.bfbase.util.BukkitUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CopyInvCommand extends BaseCommand
{
    public CopyInvCommand() {
        super("copyinv", "Copies a players inv");
        this.setAliases(new String[] { "copyinventory" });
        this.setUsage("/(command) <playerName>");
    }
    
    @Override
    public boolean onCommand(final CommandSender cs, final Command cmd, final String s, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(ChatColor.RED + "You cannot copy your inventory.");
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(ChatColor.RED + this.getUsage());
            return true;
        }
        final Player player = (Player)cs;
        if (args.length == 1) {
            final Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
            if (cs.equals(target)) {
                cs.sendMessage(ChatColor.RED + "You cannot copygit  the inventory of yourself.");
                return true;
            }
            if (target == null || !BaseCommand.canSee((CommandSender)player, target)) {
                player.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                return true;
            }
            final StaffPriority selfPriority = StaffPriority.of(player);
            if (StaffPriority.of(target).isMoreThan(selfPriority)) {
                cs.sendMessage(ChatColor.RED + "You do not have access to check the inventory of that player.");
                return true;
            }
            player.getInventory().setContents(target.getInventory().getContents());
            player.getInventory().setArmorContents(target.getInventory().getArmorContents());
            player.sendMessage(ChatColor.YELLOW + "You have copied the inventory of " + target.getName());
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return null;
    }
}

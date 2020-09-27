package net.bfcode.bfbase.command.module.essential;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.BaseConstants;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.BukkitUtils;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class PingCommand extends BaseCommand
{
    public PingCommand() {
        super("ping", "Checks the ping of a player.");
        this.setUsage("/(command) <playerName>");
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
        sender.sendMessage((target.equals(sender) ? (ChatColor.YELLOW + "Your ping is") : (ChatColor.YELLOW + "Ping of " + target.getName())) + ChatColor.GRAY + ": " + ChatColor.WHITE + this.getPing(target));
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1 && sender.hasPermission(command.getPermission() + ".others")) ? null : Collections.emptyList();
    }
    
    private int getPing(final Player p) {
        final CraftPlayer cp = (CraftPlayer)p;
        final EntityPlayer ep = cp.getHandle();
        return ep.ping;
    }
}

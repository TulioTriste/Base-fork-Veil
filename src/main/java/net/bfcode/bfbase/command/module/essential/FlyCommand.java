package net.bfcode.bfbase.command.module.essential;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.BaseConstants;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.BukkitUtils;

public class FlyCommand extends BaseCommand
{
    public FlyCommand() {
        super("fly", "Toggles flight mode for a player.");
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
        final boolean newFlight = !target.getAllowFlight();
        target.setAllowFlight(newFlight);
        if (newFlight) {
            target.setFlying(true);
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Flight mode of " + target.getName() + " set to " + newFlight + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}

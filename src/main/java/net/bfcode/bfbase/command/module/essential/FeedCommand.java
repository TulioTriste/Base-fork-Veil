package net.bfcode.bfbase.command.module.essential;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ImmutableSet;

import net.bfcode.bfbase.BaseConstants;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.BukkitUtils;

public class FeedCommand extends BaseCommand {    
	
    public FeedCommand() {
        super("feed", "Feeds a player.");
        this.setUsage("/(command) <playerName>");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player onlyTarget = null;
        Collection<Player> targets;
        if (args.length > 0 && sender.hasPermission(command.getPermission() + ".others")) {
            if (args[0].equalsIgnoreCase("all") && sender.hasPermission(command.getPermission() + ".all")) {
                targets = (Collection<Player>)ImmutableSet.copyOf(Bukkit.getOnlinePlayers());
            }
            else {
                if ((onlyTarget = BukkitUtils.playerWithNameOrUUID(args[0])) == null || !BaseCommand.canSee(sender, onlyTarget)) {
                    sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                    return true;
                }
                targets = (Collection<Player>)ImmutableSet.of(onlyTarget);
            }
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            targets = (Collection<Player>)ImmutableSet.of((onlyTarget = (Player)sender));
        }
        if (onlyTarget != null && onlyTarget.getFoodLevel() == 20) {
            sender.sendMessage(ChatColor.RED + onlyTarget.getName() + " already has full hunger.");
            return true;
        }
        for (final Player target : targets) {
            target.removePotionEffect(PotionEffectType.HUNGER);
            target.setFoodLevel(20);
        }
        sender.sendMessage(ChatColor.YELLOW + "Fed " + ((onlyTarget == null) ? "all online players" : onlyTarget.getName()) + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}

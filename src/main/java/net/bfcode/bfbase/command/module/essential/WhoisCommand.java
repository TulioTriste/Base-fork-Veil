package net.bfcode.bfbase.command.module.essential;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import net.bfcode.bfbase.BaseConstants;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.staffmode.StaffPriority;
import net.bfcode.bfbase.util.BukkitUtils;

public class WhoisCommand extends BaseCommand
{
    private final BasePlugin plugin;
    
    public WhoisCommand(final BasePlugin plugin) {
        super("whois", "Check information about a player.");
        this.plugin = plugin;
        this.setUsage("/(command) [player]");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1) {
            sender.sendMessage(this.getUsage());
            return true;
        }
        final Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        final Location location = target.getLocation();
        location.getWorld();
        this.plugin.getUserManager().getUser(target.getUniqueId());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + BukkitUtils.STRAIGHT_LINE_DEFAULT));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUsername: &f" + target.getName()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUUID: &f" + target.getUniqueId()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eOperator: &f" + (target.isOp() ? "True" : "False")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eStaff: &f" + (target.hasPermission("rank.staff") ? "True" : "False")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ePriority: &f" + StaffPriority.of(target).getPriorityLevel()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eIdle: &f" + (target.isOnline() ? (ChatColor.RED + "User is offline") : DurationFormatUtils.formatDurationWords(BukkitUtils.getIdleTime(target), true, true))));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + BukkitUtils.STRAIGHT_LINE_DEFAULT));
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
    
    static {
       ImmutableMap.of(4, "1.7.5", 5, "1.7.10", 47, "1.8");
    }
}

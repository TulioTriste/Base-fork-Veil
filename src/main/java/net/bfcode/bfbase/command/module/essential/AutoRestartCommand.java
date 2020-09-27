package net.bfcode.bfbase.command.module.essential;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.base.Strings;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.JavaUtils;
import net.bfcode.bfbase.util.command.CommandArgument;
import net.bfcode.bfbase.util.command.CommandWrapper;

public class AutoRestartCommand extends BaseCommand
{
    private final CommandWrapper handler;
    
    public AutoRestartCommand(final BasePlugin plugin) {
        super("autorestart", "Allows management of server restarts.");
        this.setUsage("/(command) <cancel|time|schedule>");
        final ArrayList<CommandArgument> arguments = new ArrayList<CommandArgument>(3);
        arguments.add(new AutoRestartCancelArgument(plugin));
        arguments.add(new AutoRestartScheduleArgument(plugin));
        arguments.add(new AutoRestartTimeArgument(plugin));
        arguments.sort(new CommandWrapper.ArgumentComparator());
        this.handler = new CommandWrapper(arguments);
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        return this.handler.onCommand(sender, command, label, args);
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return this.handler.onTabComplete(sender, command, label, args);
    }
    
    private static class AutoRestartTimeArgument extends CommandArgument
    {
        private final BasePlugin plugin;
        
        public AutoRestartTimeArgument(final BasePlugin plugin) {
            super("time", "Gets the remaining time until next restart.");
            this.plugin = plugin;
            this.aliases = new String[] { "remaining", "time" };
        }
        
        @Override
        public String getUsage(final String label) {
            return "/" + label + ' ' + this.getName();
        }
        
        @Override
        public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
            if (!this.plugin.getAutoRestartHandler().isPendingRestart()) {
                sender.sendMessage(ChatColor.RED + "There is not a restart task pending.");
                return true;
            }
            final String reason;
            sender.sendMessage(ChatColor.AQUA + "Automatic restart task occurring in " + DurationFormatUtils.formatDurationWords(this.plugin.getAutoRestartHandler().getRemainingMilliseconds(), true, true) + (Strings.nullToEmpty(reason = this.plugin.getAutoRestartHandler().getReason()).isEmpty() ? "" : (" for " + reason)) + '.');
            return true;
        }
    }
    
    private static class AutoRestartScheduleArgument extends CommandArgument
    {
        private final BasePlugin plugin;
        
        public AutoRestartScheduleArgument(final BasePlugin plugin) {
            super("schedule", "Schedule an automatic restart.");
            this.plugin = plugin;
            this.aliases = new String[] { "reschedule" };
        }
        
        @Override
        public String getUsage(final String label) {
            return "/" + label + ' ' + this.getName() + " <time> [reason]";
        }
        
        @Override
        public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + args[0].toLowerCase() + " <time> [reason]");
                return true;
            }
            final long millis = JavaUtils.parse(args[1]);
            if (millis == -1L) {
                sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m1s");
                return true;
            }
            final String reason = StringUtils.join((Object[])args, ' ', 2, args.length);
            this.plugin.getAutoRestartHandler().scheduleRestart(millis, reason);
            Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Scheduled a restart to be in " + DurationFormatUtils.formatDurationWords(millis, true, true) + (reason.isEmpty() ? "" : (" for " + reason)) + '.');
            return true;
        }
    }
    
    private static class AutoRestartCancelArgument extends CommandArgument
    {
        private final BasePlugin plugin;
        
        public AutoRestartCancelArgument(final BasePlugin plugin) {
            super("cancel", "Cancels the current automatic restart.");
            this.plugin = plugin;
        }
        
        @Override
        public String getUsage(final String label) {
            return "/" + label + ' ' + this.getName();
        }
        
        @Override
        public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
            if (!this.plugin.getAutoRestartHandler().isPendingRestart()) {
                sender.sendMessage(ChatColor.RED + "There is not a restart task pending.");
                return true;
            }
            this.plugin.getAutoRestartHandler().cancelRestart();
            sender.sendMessage(ChatColor.YELLOW + "Automatic restart task cancelled.");
            return true;
        }
    }
}

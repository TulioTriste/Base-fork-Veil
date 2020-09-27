package net.bfcode.bfbase.command.module.essential;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.bfbase.command.BaseCommand;

public class LagCommand extends BaseCommand
{
    public LagCommand() {
        super("lag", "View total server lag.");
        this.setUsage("/(command)");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final double tps = Bukkit.spigot().getTPS()[0];
        final double lag = Math.round((1.0 - tps / 20.0) * 100.0);
        final RuntimeMXBean serverStart = ManagementFactory.getRuntimeMXBean();
        final String serverUptime = DurationFormatUtils.formatDurationWords(serverStart.getUptime(), true, true);
        final DecimalFormat df = new DecimalFormat("#.#");
        final ChatColor colour = (tps >= 18.0) ? ChatColor.GREEN : ((tps >= 15.0) ? ChatColor.YELLOW : ChatColor.RED);
        final Double tpsF = Math.round(tps * 10000.0) / 10000.0;
        sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "TPS" + ChatColor.GRAY + ": " + colour + df.format(tpsF) + ChatColor.DARK_GRAY + " | " + ChatColor.GOLD.toString() + ChatColor.BOLD + "Lag" + ChatColor.GRAY + ": " + ChatColor.YELLOW + colour + Math.round(lag * 10000.0) / 10000.0 + '%' + ChatColor.DARK_GRAY + " | " + ChatColor.GOLD.toString() + ChatColor.BOLD + "Players" + ChatColor.GRAY + ": " + ChatColor.YELLOW + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ChatColor.DARK_GRAY + " | " + ChatColor.GOLD.toString() + ChatColor.BOLD + "Server Time" + ChatColor.GRAY + ": " + ChatColor.YELLOW + serverUptime);
        return true;
    }
}

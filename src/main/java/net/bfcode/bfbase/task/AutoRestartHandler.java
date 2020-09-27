package net.bfcode.bfbase.task;

import java.util.concurrent.TimeUnit;
import org.bukkit.plugin.Plugin;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.util.BukkitUtils;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.scheduler.BukkitTask;

public class AutoRestartHandler
{
    private static final int[] ALERT_SECONDS;
    private static final long TICKS_HOURS;
    private final BasePlugin plugin;
    private long current;
    private String reason;
    private BukkitTask task;
    
    public AutoRestartHandler(final BasePlugin plugin) {
        this.current = Long.MIN_VALUE;
        this.plugin = plugin;
        this.scheduleRestart(AutoRestartHandler.TICKS_HOURS);
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public void setReason(final String reason) {
        this.reason = reason;
    }
    
    public boolean isPendingRestart() {
        return this.task != null && this.current != Long.MIN_VALUE;
    }
    
    public void cancelRestart() {
        if (this.isPendingRestart()) {
            this.task.cancel();
            this.task = null;
            this.current = Long.MIN_VALUE;
        }
    }
    
    public long getRemainingMilliseconds() {
        return this.getRemainingTicks() * 50L;
    }
    
    public long getRemainingTicks() {
        return this.current - MinecraftServer.currentTick;
    }
    
    public void scheduleRestart(final long milliseconds) {
        this.scheduleRestart(milliseconds, null);
    }
    
    public void scheduleRestart(final long millis, final String reason) {
        this.cancelRestart();
        this.reason = reason;
        this.current = MinecraftServer.currentTick + 20 + millis / 50L;
        this.task = new BukkitRunnable() {
            public void run() {
                if (AutoRestartHandler.this.current == 0L) {
                    this.cancel();
                }
                final long remainingTicks;
                if ((remainingTicks = AutoRestartHandler.this.getRemainingTicks()) <= 0L) {
                    this.cancel();
                    Bukkit.getServer().savePlayers();
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "safestop");
                    return;
                }
                final long remainingMillis = remainingTicks * 50L;
                if (Ints.contains(AutoRestartHandler.ALERT_SECONDS, (int)(remainingMillis / 1000L))) {
                    Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------");
                    Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + " Server Restart");
                    Bukkit.broadcastMessage(" ");
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "   Time: " + ChatColor.WHITE + DurationFormatUtils.formatDurationWords(remainingMillis, true, true));
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "   Reason: " + ChatColor.WHITE + (reason.isEmpty() ? "Scheduled Restart" : reason.replace("[", "").replace("]", "")));
                    Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------");
                }
            }
        }.runTaskTimer(this.plugin, 20L, 20L);
    }
    
    static {
        ALERT_SECONDS = new int[] { 14400, 7200, 1800, 600, 300, 270, 240, 210, 180, 150, 120, 90, 60, 30, 15, 10, 5, 4, 3, 2, 1 };
        TICKS_HOURS = TimeUnit.HOURS.toMillis(24L);
    }
}

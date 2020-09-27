package net.bfcode.bfbase.util.bossbar;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.base.Preconditions;

import net.bfcode.bfbase.BasePlugin;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PlayerConnection;

public class BossBarManager
{
    private static final Map<UUID, BossBarEntry> bossBars;
    private static JavaPlugin plugin;
    
    public static void hook() {
        Preconditions.checkArgument(BossBarManager.plugin == null, "BossBarManager is already hooked");
        BossBarManager.plugin = BasePlugin.getPlugin();
    }
    
    public static void unhook() {
        Preconditions.checkArgument(BossBarManager.plugin != null, "BossBarManager is already unhooked");
        for (final UUID uuid : BossBarManager.bossBars.keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                continue;
            }
            hideBossBar(player);
        }
        BossBarManager.plugin = null;
    }
    
    public static Map<UUID, BossBarEntry> getBossBars() {
        return BossBarManager.bossBars;
    }
    
    public static boolean isShowingBossBar(final Player player) {
        return getShownBossBar(player) != null;
    }
    
    public static BossBar getShownBossBar(final Player player) {
        final UUID uuid = player.getUniqueId();
        final BossBarEntry bossBarEntry = BossBarManager.bossBars.get(uuid);
        return (bossBarEntry != null) ? bossBarEntry.getBossBar() : null;
    }
    
    public static BossBar hideBossBar(final Player player) {
        final BossBarEntry entry = BossBarManager.bossBars.get(player.getUniqueId());
        if (entry == null) {
            return null;
        }
        final BossBar bossBar = entry.getBossBar();
        final BukkitTask bukkitTask = entry.getCancelTask();
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)bossBar.destroyPacket);
        BossBarManager.bossBars.remove(player.getUniqueId());
        return bossBar;
    }
    
    public static void showBossBar(final BossBar bossBar, final Player player) {
        showBossBar(bossBar, player, 0L);
    }
    
    public static void showBossBar(final BossBar bossBar, final Player player, final long ticks) {
        final BossBar current = getShownBossBar(player);
        if (current != null) {
            if (current.equals(bossBar)) {
                return;
            }
            hideBossBar(player);
        }
        final PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket((Packet)bossBar.spawnPacket);
        BukkitTask bukkitTask;
        if (ticks <= 0L) {
            bukkitTask = null;
        }
        else {
            Preconditions.checkArgument(BossBarManager.plugin != null, "Cannot start destroy runnable as plugin wasn't hooked correctly.");
            bukkitTask = new BukkitRunnable() {
                public void run() {
                    BossBarManager.hideBossBar(player);
                }
            }.runTaskLater((Plugin)BossBarManager.plugin, ticks);
        }
        BossBarManager.bossBars.put(player.getUniqueId(), new BossBarEntry(bossBar, bukkitTask));
        connection.sendPacket((Packet)bossBar.spawnPacket);
    }
    
    static {
        bossBars = new HashMap<UUID, BossBarEntry>();
    }
}

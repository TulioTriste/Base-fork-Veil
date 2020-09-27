package net.bfcode.bfbase.util;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Preconditions;

import net.bfcode.bfbase.BasePlugin;
import net.md_5.bungee.api.ChatColor;

public class Utils {
    private static long MINUTE = TimeUnit.MINUTES.toMillis(1);
    private static long HOUR = TimeUnit.HOURS.toMillis(1);
    public static String PERMISSION;
    public static String NO_PERMISSION;
	public static String target_offline = CC.translate("&cThat player is currently offline.");

    public static boolean isOnline(final CommandSender sender, final Player player) {
        return player != null && (!(sender instanceof Player) || ((Player)sender).canSee(player));
    }
    
    public static void PLAYER_NOT_FOUND(final CommandSender sender, final String player) {
        sender.sendMessage(ChatColor.RED + "Player " + ChatColor.GRAY + "'" + player + ChatColor.RED + "' is currently offline.");
    }
    
    public static Location getHighestLocation(final Location origin) {
        return getHighestLocation(origin, null);
    }
    
    public static Location getHighestLocation(final Location origin, final Location def) {
        Preconditions.checkNotNull(origin, "The location cannot be null");
        final Location cloned = origin.clone();
        final World world = cloned.getWorld();
        final int x = cloned.getBlockX();
        int y = world.getMaxHeight();
        final int z = cloned.getBlockZ();
        while (y > origin.getBlockY()) {
            final Block block = world.getBlockAt(x, --y, z);
            if (!block.isEmpty()) {
                final Location next = block.getLocation();
                next.setPitch(origin.getPitch());
                next.setYaw(origin.getYaw());
                return next;
            }
        }
        return def;
    }
    
	public static Location destringifyLocation(String string) {
		String[] split = string.substring(1, string.length() - 2).split(",");
		World world = Bukkit.getWorld(split[0]);
		if (world == null) {
			return null;
		}
		double x = Double.parseDouble(split[1]);
		double y = Double.parseDouble(split[2]);
		double z = Double.parseDouble(split[3]);
		float yaw = Float.parseFloat(split[4]);
		float pitch = Float.parseFloat(split[5]);
		Location loc = new Location(world, x, y, z);
		loc.setYaw(yaw);
		loc.setPitch(pitch);
		return loc;
    }
    
    public static void setMaxPlayers(final int amount) throws ReflectiveOperationException {
        final String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        final Object playerlist = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".CraftServer").getDeclaredMethod("getHandle", (Class<?>[])null).invoke(Bukkit.getServer(), (Object[])null);
        final Field maxplayers = playerlist.getClass().getSuperclass().getDeclaredField("maxPlayers");
        maxplayers.setAccessible(true);
        maxplayers.set(playerlist, amount);
    }
    
    public static String formatLong(final long time) {
        final DecimalFormat format = new DecimalFormat("#0.0");
        return String.valueOf(format.format(time / 1000.0f));
    }
    
    public static String formatLongMin(final long time) {
        final long totalSecs = time / 1000L;
        return String.format("%02d:%02d", totalSecs / 60L, totalSecs % 60L);
    }
    
    public static String formatLongHours(final long time) {
        final long totalSecs = time / 1000L;
        return String.format("%02d:%02d:%02d", totalSecs / 3600L, totalSecs % 3600L / 60L, totalSecs % 60L);
    }
    
    public static String formatLocation(final Location loc) {
        return "X:" + loc.getBlockX() + ", Z:" + loc.getBlockZ();
    }
    
    public static String formatIntMin(final int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }
    
    @SuppressWarnings("deprecation")
	public static List<Entity> getNearby(final Location loc, final int distance) {
        final List<Entity> list = new ArrayList<Entity>();
        for (final Entity e : loc.getWorld().getEntities()) {
            if (e instanceof Player) {
                continue;
            }
            if (!e.getType().isAlive()) {
                continue;
            }
            if (loc.distance(e.getLocation()) > distance) {
                continue;
            }
            list.add(e);
        }
        for (final Player online : Bukkit.getOnlinePlayers()) {
            if (online.getWorld() == loc.getWorld() && loc.distance(online.getLocation()) <= distance) {
                list.add((Entity)online);
            }
        }
        return list;
    }
    
    public static int getPing(final Player player) {
        final CraftPlayer craft = (CraftPlayer)player;
        return craft.getHandle().ping;
    }

    public static void setUpDonators() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BasePlugin.getPlugin(), (Runnable)new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GOLD + "Online Donators: " + ChatColor.GRAY + Utils.getDonatorsOnline());
                Bukkit.broadcastMessage(ChatColor.YELLOW + "This rank can be purchased on our store, " + BasePlugin.getPlugin().getConfig().getString("Server.Store"));
            }
        }, 0L, 6000L);
    }
    
    @SuppressWarnings("deprecation")
	public static String getDonatorsOnline() {
        String message = "";
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            if ((player.hasPermission("base.player.bypass") && !player.isOp()) || !player.hasPermission("*")) {
                message += ChatColor.GRAY + player.getName() + ChatColor.YELLOW + ", " + ChatColor.GRAY;
            }
        }
        if (message.length() > 2) {
            message = message.substring(0, message.length() - 2);
        }
        if (message.length() == 0) {
            message = ChatColor.GREEN + "None";
        }
        return message;
    }
    
    static {
        Utils.PERMISSION = "command.";
        Utils.NO_PERMISSION = "You do not have permission to execute this command.";
    }
}

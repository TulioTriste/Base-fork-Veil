package net.bfcode.bfbase.command.module.teleport;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.bfcode.bfbase.BaseConstants;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.BukkitUtils;

public class TeleportCommand extends BaseCommand
{
    static final int MAX_COORD = 9000;
    static final int MIN_COORD_MINUS_ONE = -9000;
    static final int MIN_COORD = -9000;
    
    public TeleportCommand() {
        super("teleport", "Teleport to a player or position.");
        this.setUsage("/(command) (<playerName> [otherPlayerName]) | (x y z)");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1 || args.length > 4) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        Player targetA;
        if (args.length == 1 || args.length == 3) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Usage: " + this.getUsage(label));
                return true;
            }
            targetA = (Player)sender;
        }
        else {
            targetA = BukkitUtils.playerWithNameOrUUID(args[0]);
        }
        if (targetA == null || !BaseCommand.canSee(sender, targetA)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        if (args.length < 3) {
            final Player targetB = BukkitUtils.playerWithNameOrUUID(args[args.length - 1]);
            if (targetB == null || !BaseCommand.canSee(sender, targetB)) {
                sender.sendMessage(ChatColor.GOLD + "Player named or with UUID '" + ChatColor.WHITE + args[args.length - 1] + ChatColor.GOLD + "' not found.");
                return true;
            }
            if (targetA.equals(targetB)) {
                sender.sendMessage(ChatColor.RED + "The teleportee and teleported are the same player.");
                return true;
            }
            if (targetA.teleport((Entity)targetB, PlayerTeleportEvent.TeleportCause.COMMAND)) {
                Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Teleported " + targetA.getName() + " to " + targetB.getName() + '.');
            }
            else {
                sender.sendMessage(ChatColor.RED + "Failed to teleport you to " + targetB.getName() + '.');
            }
        }
        else if (targetA.getWorld() != null) {
            final Location targetALocation = targetA.getLocation();
            final double x = this.getCoordinate(sender, targetALocation.getX(), args[args.length - 3]);
            final double y = this.getCoordinate(sender, targetALocation.getY(), args[args.length - 2], 0, 0);
            final double z = this.getCoordinate(sender, targetALocation.getZ(), args[args.length - 1]);
            if (x == -3.0000001E7 || y == -3.0000001E7 || z == -3.0000001E7) {
                sender.sendMessage("Please provide a valid location.");
                return true;
            }
            targetALocation.setX(x);
            targetALocation.setY(y);
            targetALocation.setZ(z);
            if (targetA.teleport(targetALocation, PlayerTeleportEvent.TeleportCause.COMMAND)) {
                Command.broadcastCommandMessage(sender, ChatColor.translateAlternateColorCodes('&', "Teleported " + targetA.getName() + " to " + x + " " + y + " " + z));
            }
            else {
                sender.sendMessage(ChatColor.RED + "Failed to teleport you.");
            }
        }
        return true;
    }
    
    private double getCoordinate(final CommandSender sender, final double current, final String input) {
        return this.getCoordinate(sender, current, input, -30000000, 30000000);
    }
    
    private double getCoordinate(final CommandSender sender, final double current, String input, final int min, final int max) {
        final boolean relative = input.startsWith("~");
        double result = relative ? current : 0.0;
        if (!relative || input.length() > 1) {
            final boolean exact = input.contains(".");
            if (relative) {
                input = input.substring(1);
            }
            final double testResult = VanillaCommand.getDouble(sender, input);
            if (testResult == -3.0000001E7) {
                return -3.0000001E7;
            }
            result += testResult;
            if (!exact && !relative) {
                result += 0.5;
            }
        }
        if (min != 0 || max != 0) {
            if (result < min) {
                result = -3.0000001E7;
            }
            if (result > max) {
                result = -3.0000001E7;
            }
        }
        return result;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1 || args.length == 2) ? null : Collections.emptyList();
    }
}

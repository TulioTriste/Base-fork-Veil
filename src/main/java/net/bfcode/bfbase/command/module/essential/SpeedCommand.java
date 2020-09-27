package net.bfcode.bfbase.command.module.essential;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.BukkitUtils;
import net.minecraft.util.com.google.common.primitives.Floats;

public class SpeedCommand extends BaseCommand
{
    private static final ImmutableList<String> COMPLETIONS_FIRST;
    private static final ImmutableList<String> COMPLETIONS_SECOND;
    
    public SpeedCommand() {
        super("speed", "Sets the fly/walk speed of a player.");
        this.setUsage("/(command) <fly|walk> <speedMultiplier|reset> [playerName]");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player target;
        if (args.length > 2 && sender.hasPermission(command.getPermission() + ".others")) {
            target = BukkitUtils.playerWithNameOrUUID(args[2]);
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            target = (Player)sender;
        }
        Boolean flight;
        if (args[0].equalsIgnoreCase("fly")) {
            flight = true;
        }
        else {
            if (!args[0].equalsIgnoreCase("walk")) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            flight = false;
        }
        Float multiplier;
        if (args[1].equalsIgnoreCase("reset")) {
            multiplier = (flight ? 2.0f : 1.0f);
        }
        else {
            multiplier = Floats.tryParse(args[1]);
            if (multiplier == null) {
                sender.sendMessage(ChatColor.RED + "Invalid speed multiplier: '" + args[1] + "'.");
                return true;
            }
        }
        if (flight) {
            final float flySpeed = 0.1f * multiplier;
            try {
                target.setFlySpeed(flySpeed);
                Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Flight speed of " + target.getName() + " has been set to " + multiplier + '.');
                return true;
            }
            catch (IllegalArgumentException ex) {
                if (flySpeed < 0.1f) {
                    sender.sendMessage(ChatColor.RED + "Speed multiplier too low: " + multiplier);
                }
                else if (flySpeed > 0.1f) {
                    sender.sendMessage(ChatColor.RED + "Speed multiplier too high: " + multiplier);
                }
                return true;
            }
        }
        final float walkSpeed = 0.2f * multiplier;
        try {
            target.setWalkSpeed(walkSpeed);
            Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Walking speed of " + target.getName() + " has been set to " + multiplier + '.');
            return true;
        }
        catch (IllegalArgumentException ex) {
            if (walkSpeed < 0.2f) {
                sender.sendMessage(ChatColor.RED + "Speed multiplier too low: " + multiplier);
            }
            if (walkSpeed > 0.2f) {
                sender.sendMessage(ChatColor.RED + "Speed multiplier too high: " + multiplier);
            }
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        switch (args.length) {
            case 1: {
                return BukkitUtils.getCompletions(args, (List<String>)SpeedCommand.COMPLETIONS_FIRST);
            }
            case 2: {
                return BukkitUtils.getCompletions(args, (List<String>)SpeedCommand.COMPLETIONS_SECOND);
            }
            case 3: {
                return null;
            }
            default: {
                return Collections.emptyList();
            }
        }
    }
    
    static {
        COMPLETIONS_FIRST = ImmutableList.of("fly", "walk");
        COMPLETIONS_SECOND = ImmutableList.of("reset");
    }
}

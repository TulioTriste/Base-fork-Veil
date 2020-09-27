package net.bfcode.bfbase.command.module.essential;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;

public class StaffServerCommand extends BaseCommand
{
    public StaffServerCommand() {
        super("staffserver", "Teleports to a server.");
        this.setUsage("/(command) <server>");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You may not do that.");
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("Connect");
        dataOutput.writeUTF(args[0]);
        ((Player)sender).sendPluginMessage((Plugin)BasePlugin.getPlugin(), "BungeeCord", dataOutput.toByteArray());
        sender.sendMessage(ChatColor.YELLOW + "Sending you to " + ChatColor.GOLD + args[0]);
        return true;
    }
}

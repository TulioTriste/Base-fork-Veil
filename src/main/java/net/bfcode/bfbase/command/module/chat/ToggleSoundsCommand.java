package net.bfcode.bfbase.command.module.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.user.BaseUser;

public class ToggleSoundsCommand extends BaseCommand
{
    private final BasePlugin plugin;
    
    public ToggleSoundsCommand(final BasePlugin plugin) {
        super("togglesounds", "Toggles sounds.");
        this.setAliases(new String[] { "sounds" });
        this.setUsage("/(command)");
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        final Player player = (Player)sender;
        final BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        final boolean newToggled = !baseUser.isMessagingSounds();
        baseUser.setMessagingSounds(newToggled);
        sender.sendMessage(ChatColor.YELLOW + "You have " + (newToggled ? "enabled" : "disabled") + ChatColor.YELLOW + " sounds.");
        return true;
    }
}

package net.bfcode.bfbase.command.module.essential;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.BaseConstants;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.user.BaseUser;

public class NoteCommand extends BaseCommand
{
    public NoteCommand() {
        super("note", "add, removes, and checks notes for a user");
        this.setUsage("/(command) <add|remove|check> <playerName> [note]");
        this.setAliases(new String[] { "addnote, notes, checknote, removenote" });
    }
    
    @Override
    public boolean onCommand(final CommandSender cs, final Command cmd, final String s, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(ChatColor.RED + "Please use the server to execute this command.");
            return true;
        }
        final Player player = (Player)cs;
        if (args.length < 2) {
            player.sendMessage(this.getUsage(s));
            return true;
        }
        if (Bukkit.getPlayer(args[1]) == null && Bukkit.getOfflinePlayer(args[1]) == null) {
            cs.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        final BaseUser targetUser = BasePlugin.getPlugin().getUserManager().getUser(target.getUniqueId());
        final String note = StringUtils.join((Object[])args, ' ', 2, args.length);
        if (args[0].equalsIgnoreCase("add")) {
            final String upTime = DateFormatUtils.format(System.currentTimeMillis(), "dd/MM");
            final String time = DateFormatUtils.format(System.currentTimeMillis(), "hh:mm");
            targetUser.setNote(ChatColor.YELLOW + "Staff: " + ChatColor.GREEN + cs.getName() + ChatColor.GRAY + " [" + upTime + "-" + time + "] - " + ChatColor.YELLOW + note);
            player.sendMessage(ChatColor.GREEN + "You added a note to " + targetUser.getName());
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (!player.hasPermission(this.getPermission() + ".remove")) {
                player.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (targetUser.tryRemoveNote()) {
                Command.broadcastCommandMessage(cs, ChatColor.GREEN + "Removed note of " + target.getName() + ChatColor.YELLOW + '.');
                return true;
            }
            player.sendMessage(ChatColor.RED + "Note not found.");
            return true;
        }
        else {
            if (args[0].equalsIgnoreCase("check")) {
                for (final String notes : targetUser.getNotes()) {
                    player.sendMessage(notes);
                }
                return true;
            }
            player.sendMessage(this.getUsage(s));
            return true;
        }
    }
}

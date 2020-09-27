package net.bfcode.bfbase.kit.argument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.kit.Kit;
import net.bfcode.bfbase.user.BaseUser;
import net.bfcode.bfbase.util.BukkitUtils;
import net.bfcode.bfbase.util.command.CommandArgument;

public class KitListArgument extends CommandArgument
{
    private final BasePlugin plugin;
    
    public KitListArgument(final BasePlugin plugin) {
        super("list", "Lists all current kits");
        this.plugin = plugin;
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName();
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final List<Kit> kits = this.plugin.getKitManager().getKits();
        if (kits.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No kits have been defined.");
            return true;
        }
        final ArrayList<String> kitNames = new ArrayList<String>();
        for (final Kit kit : kits) {
            final String permission = kit.getPermissionNode();
            if (permission != null && !sender.hasPermission(permission)) {
                continue;
            }
            final BaseUser user = this.plugin.getUserManager().getUser(((Player)sender).getUniqueId());
            final ChatColor color = (user.getKitUses(kit) >= kit.getMaximumUses() || user.getRemainingKitCooldown(kit) >= kit.getMaximumUses() || this.plugin.getPlayTimeManager().getTotalPlayTime(((Player)sender).getUniqueId()) <= kit.getMinPlaytimeMillis()) ? ChatColor.RED : ChatColor.GREEN;
            kitNames.add(color + kit.getDisplayName());
        }
        final String kitList = StringUtils.join((Collection)kitNames, ChatColor.GRAY + ", ");
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Kit List" + ChatColor.GREEN + "[" + kitNames.size() + '/' + kits.size() + "]");
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + kitList + ChatColor.GRAY + ']');
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return Collections.emptyList();
    }
}

package net.bfcode.bfbase.kit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.kit.argument.KitApplyArgument;
import net.bfcode.bfbase.kit.argument.KitCreateArgument;
import net.bfcode.bfbase.kit.argument.KitDeleteArgument;
import net.bfcode.bfbase.kit.argument.KitDisableArgument;
import net.bfcode.bfbase.kit.argument.KitGuiArgument;
import net.bfcode.bfbase.kit.argument.KitHelpArgument;
import net.bfcode.bfbase.kit.argument.KitListArgument;
import net.bfcode.bfbase.kit.argument.KitPreviewArgument;
import net.bfcode.bfbase.kit.argument.KitRenameArgument;
import net.bfcode.bfbase.kit.argument.KitSetDelayArgument;
import net.bfcode.bfbase.kit.argument.KitSetDescriptionArgument;
import net.bfcode.bfbase.kit.argument.KitSetImageArgument;
import net.bfcode.bfbase.kit.argument.KitSetIndexArgument;
import net.bfcode.bfbase.kit.argument.KitSetItemsArgument;
import net.bfcode.bfbase.kit.argument.KitSetMaxUsesArgument;
import net.bfcode.bfbase.kit.argument.KitSetSlotArgument;
import net.bfcode.bfbase.kit.argument.KitSetminplaytimeArgument;
import net.bfcode.bfbase.util.BukkitUtils;
import net.bfcode.bfbase.util.command.ArgumentExecutor;
import net.bfcode.bfbase.util.command.CommandArgument;
import net.bfcode.bfhcf.HCFaction;

public class KitExecutor extends ArgumentExecutor
{
    private final BasePlugin plugin;
    
    public KitExecutor(final BasePlugin plugin) {
        super("kit");
        this.plugin = plugin;
        this.addArgument(new KitHelpArgument(plugin));
        this.addArgument(new KitApplyArgument(plugin));
        this.addArgument(new KitCreateArgument(plugin));
        this.addArgument(new KitDeleteArgument(plugin));
        this.addArgument(new KitSetDescriptionArgument(plugin));
        this.addArgument(new KitDisableArgument(plugin));
        this.addArgument(new KitGuiArgument(plugin));
        this.addArgument(new KitListArgument(plugin));
        this.addArgument(new KitPreviewArgument(plugin));
        this.addArgument(new KitRenameArgument(plugin));
        this.addArgument(new KitSetDelayArgument(plugin));
        this.addArgument(new KitSetImageArgument(plugin));
        this.addArgument(new KitSetIndexArgument(plugin));
        this.addArgument(new KitSetItemsArgument(plugin));
        this.addArgument(new KitSetMaxUsesArgument(plugin));
        this.addArgument(new KitSetminplaytimeArgument(plugin));
        this.addArgument(new KitSetSlotArgument(plugin));
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players may open kit GUI's.");
                return true;
            }
            List<Kit> kits = this.plugin.getKitManager().getKits();
            if (kits.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "No kits have been defined.");
                return true;
            }
            Player player = (Player)sender;
            player.openInventory(this.plugin.getKitManager().getGui(player));
            return true;
        }
        final CommandArgument argument = this.getArgument(args[0]);
        final String permission = ((argument == null) ? null : argument.getPermission());
        if (argument != null && (permission == null || sender.hasPermission(permission))) {
            argument.onCommand(sender, command, label, args);
            return true;
        }
        final Kit kit = this.plugin.getKitManager().getKit(args[0]);
        final String kitPermission;
        if (sender instanceof Player && kit != null && ((kitPermission = kit.getPermissionNode()) == null || sender.hasPermission(kitPermission))) {
            final Player player = (Player)sender;
            kit.applyTo(player, false, true);
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Kit or command " + args[0] + " not found.");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 1) {
            return super.onTabComplete(sender, command, label, args);
        }
        List<String> previous = super.onTabComplete(sender, command, label, args);
        final ArrayList<String> kitNames = new ArrayList<String>();
        for (final Kit kit : this.plugin.getKitManager().getKits()) {
            final String permission = kit.getPermissionNode();
            if (permission != null && !sender.hasPermission(permission)) {
                continue;
            }
            kitNames.add(kit.getName());
        }
        if (previous == null || previous.isEmpty()) {
            previous = kitNames;
        }
        else {
            previous = new ArrayList<String>(previous);
            previous.addAll(0, kitNames);
        }
        return BukkitUtils.getCompletions(args, previous);
    }
}

package net.bfcode.bfbase.kit.argument;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.kit.Kit;
import net.bfcode.bfbase.util.command.CommandArgument;
import net.bfcode.bfhcf.utils.CC;

public class KitSetSlotArgument extends CommandArgument {
	
	private BasePlugin plugin;
	
    public KitSetSlotArgument(final BasePlugin plugin) {
        super("setslot", "set a slot on HCF Inventory");
        this.plugin = plugin;
        this.aliases = new String[] { "setslots" };
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName();
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if(kit == null) {
            sender.sendMessage(ChatColor.RED + "Kit '" + args[1] + "' not found.");
            return true;
        }
        int slot = Integer.valueOf(args[2]);
        if(slot > 45 || slot < 1) {
        	sender.sendMessage(CC.translate("&cThe number that you gave it's not registered on the available numbers."));
        	return true;
        }
        kit.setSlot(slot);
        sender.sendMessage(CC.translate("&aSuccessfully set the slot to the kit &6" + args[1]));
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return Collections.emptyList();
    }
}

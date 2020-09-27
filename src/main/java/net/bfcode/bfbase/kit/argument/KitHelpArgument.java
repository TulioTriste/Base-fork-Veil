package net.bfcode.bfbase.kit.argument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.util.command.CommandArgument;
import net.bfcode.bfhcf.utils.CC;

public class KitHelpArgument extends CommandArgument {
    
    public KitHelpArgument(final BasePlugin plugin) {
        super("help", "Applies a kit to player");
        this.permission = "command.kit.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return "/" + label + ' ' + this.getName();
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	sender.sendMessage(CC.translate("&7&m-----------------------------------------------------"));
    	sender.sendMessage(CC.translate("&6&lKit Help"));
    	sender.sendMessage(CC.translate("&e/kit apply <kitName> <playerName> &7- &fApplies a kit to player"));
    	sender.sendMessage(CC.translate("&e/kit create <kitName> [kitDescription] &7- &fCreates a kit"));
    	sender.sendMessage(CC.translate("&e/kit delete <kitName> &7- &fDeletes a kit"));
    	sender.sendMessage(CC.translate("&e/kit setdescription <kitName> <none|description> &7- &fSets the description of a kit"));
    	sender.sendMessage(CC.translate("&e/kit disable <kitName> &7- &fDisable or enable a kit"));
    	sender.sendMessage(CC.translate("&e/kit gui &7- &fOpens the kit gui"));
    	sender.sendMessage(CC.translate("&e/kit list &7- &fLists all current kits"));
    	sender.sendMessage(CC.translate("&e/kit preview <kitName> &7- &fPreview the items you will get in a kit"));
    	sender.sendMessage(CC.translate("&e/kit rename <kitName> <newKitName> &7- &fRenames a kit"));
    	sender.sendMessage(CC.translate("&e/kit setdelay <kitName> <delay> &7- &fSets the delay time of a kit"));
    	sender.sendMessage(CC.translate("&e/kit setimage <kitName> &7- &fSets the image of kit in GUI to held item"));
    	sender.sendMessage(CC.translate("&e/kit setindex <kitName> <index[0 = minimum]> &7- &fSets the position of a kit for the GUI"));
    	sender.sendMessage(CC.translate("&e/kit setitems <kitName> &7- &fSets the items of a kit"));
    	sender.sendMessage(CC.translate("&e/kit setmaxuses <kitName> <amount|unlimited> &7- &fSets the maximum uses for a kit"));
    	sender.sendMessage(CC.translate("&e/kit setminplaytime <kitName> <time> &7- &fSets the minimum playtime to use a kit"));
    	sender.sendMessage(CC.translate("&e/kit setslot &7- &fset a slot on HCF Inventory"));
    	sender.sendMessage(CC.translate("&7&m-----------------------------------------------------"));
    	sender.sendMessage(CC.translate("&a/kit <kitName> &7- &fApplies a kit."));
        return true;
    }
}

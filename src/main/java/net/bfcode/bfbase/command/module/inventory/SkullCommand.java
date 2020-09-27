package net.bfcode.bfbase.command.module.inventory;

import java.util.Collections;
import java.util.List;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.bfcode.bfbase.command.BaseCommand;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class SkullCommand extends BaseCommand implements Listener
{
    public SkullCommand() {
        super("skull", "Gives you the skull from a player");
        this.setUsage("/(command) <player>");
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players bitch.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        final Player p = (Player)sender;
        final String target = args[0];
        ItemStack itemSkull = p.getInventory().getItemInHand();
        SkullMeta metaSkull = null;
        itemSkull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        metaSkull = (SkullMeta)itemSkull.getItemMeta();
        metaSkull.setDisplayName(ChatColor.YELLOW + "Skull of " + ChatColor.GOLD + target);
        metaSkull.setOwner(target);
        itemSkull.setItemMeta((ItemMeta)metaSkull);
        p.getInventory().addItem(new ItemStack[] { itemSkull });
        p.sendMessage(ChatColor.YELLOW + "You now have " + ChatColor.GOLD + target + ChatColor.YELLOW + " skull.");
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}

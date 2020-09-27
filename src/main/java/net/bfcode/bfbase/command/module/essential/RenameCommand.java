package net.bfcode.bfbase.command.module.essential;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfhcf.abilities.AntiTrapper;
import net.bfcode.bfhcf.abilities.BelchBomb;
import net.bfcode.bfhcf.abilities.Cocaine;
import net.bfcode.bfhcf.abilities.FreezeGun;
import net.bfcode.bfhcf.abilities.Grappling;
import net.bfcode.bfhcf.abilities.Murasame;
import net.bfcode.bfhcf.abilities.NinjaStar;
import net.bfcode.bfhcf.abilities.NoFall;
import net.bfcode.bfhcf.abilities.PocketBard;
import net.bfcode.bfhcf.abilities.RawPotion;
import net.bfcode.bfhcf.abilities.Refill;
import net.bfcode.bfhcf.abilities.Rocket;
import net.bfcode.bfhcf.abilities.RotateStick;
import net.bfcode.bfhcf.abilities.SnowBall;
import net.bfcode.bfhcf.abilities.SuperAxe;
import net.bfcode.bfhcf.abilities.Switcher;
import net.bfcode.bfhcf.abilities.ThunderAxe;
import net.bfcode.bfhcf.utils.CC;

public class RenameCommand extends BaseCommand {
	
    public RenameCommand() {
        super("rename", "Rename your held item.");
        this.setUsage("/(command) <newItemName>");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        final Player player = (Player)sender;
        final ItemStack stack = player.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You are not holding anything.");
            return true;
        }
        final ItemMeta meta = stack.getItemMeta();
        String oldName = meta.getDisplayName();
        if (oldName != null) {
            oldName = oldName.trim();
        }
        final String newName = (args[0].equalsIgnoreCase("none") || args[0].equalsIgnoreCase("null")) ? null : ChatColor.translateAlternateColorCodes('&', StringUtils.join((Object[])args, ' ', 0, args.length));
        if (oldName == null && newName == null) {
            sender.sendMessage(ChatColor.RED + "Your held item already has no name.");
            return true;
        }
        if (oldName != null && oldName.equals(newName)) {
            sender.sendMessage(ChatColor.RED + "Your held item is already named this.");
            return true;
        }
        if(stack.getType() == Material.TRIPWIRE_HOOK) {
        	return true;
        }
        if (oldName != null && meta.hasLore()) {
        	sender.sendMessage(CC.translate("&c¡Nope!"));
        	return true;
        }
        if (oldName != null && meta.hasLore() && (
        	oldName.equals(AntiTrapper.name) || 
        	oldName.equals(BelchBomb.name) || 
        	oldName.equals(Cocaine.name) || 
        	oldName.equals(FreezeGun.name) || 
        	oldName.equals(Grappling.name) || 
        	oldName.equals(Murasame.name) || 
        	oldName.equals(NinjaStar.name) || 
        	oldName.equals(NoFall.name) || 
        	oldName.equals(PocketBard.name) || 
        	oldName.equals(RawPotion.name) || 
        	oldName.equals(Refill.name) || 
        	oldName.equals(Rocket.name) || 
        	oldName.equals(RotateStick.name) || 
        	oldName.equals(SnowBall.name) || 
        	oldName.equals(SuperAxe.name) || 
        	oldName.equals(Switcher.name) || 
        	oldName.equals(ThunderAxe.name))) {
        	sender.sendMessage(CC.translate("&c¡Nope!"));
        	return true;
        }
        meta.setDisplayName(newName);
        stack.setItemMeta(meta);
        if (newName == null) {
            sender.sendMessage(ChatColor.YELLOW + "Removed name of held item from " + oldName + '.');
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + "Renamed item in hand from " + ((oldName == null) ? "no name" : oldName) + ChatColor.YELLOW + " to " + newName + ChatColor.YELLOW + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return Collections.emptyList();
    }
}

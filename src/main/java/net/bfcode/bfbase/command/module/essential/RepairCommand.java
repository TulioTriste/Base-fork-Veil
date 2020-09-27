package net.bfcode.bfbase.command.module.essential;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.bfcode.bfhcf.utils.CC;
import net.bfcode.bfbase.BaseConstants;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.Cooldowns;

public class RepairCommand extends BaseCommand {
	
    public RepairCommand() {
        super("repair", "Allows repairing of damaged tools for a player.");
        this.setUsage("/(command) <playerName> [all]");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "No console.");
            return true;
        }
        Player target = (Player)sender;
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        final HashSet<ItemStack> toRepair = new HashSet<ItemStack>();
        if (args.length >= 1 && args[0].equalsIgnoreCase("all") && target.hasPermission("command.repair.all")) {
        	if(Cooldowns.isOnCooldown("REPAIR_ALL", target) && !target.hasPermission("rank.staff") || !target.isOp()) {
        		target.sendMessage(CC.translate("&cPlease wait &l" + Cooldowns.getCooldownInt("REPAIR_ALL", target) + " &cseconds to use this command again."));
        		return true;
        	}
            PlayerInventory targetInventory = target.getInventory();
            toRepair.addAll(Arrays.asList(targetInventory.getContents()));
            toRepair.addAll(Arrays.asList(targetInventory.getArmorContents()));
            Cooldowns.addCooldown("REPAIR_ALL", target, 300);
        }
        else {
            toRepair.add(target.getItemInHand());
        }
        for (final ItemStack stack : toRepair) {
            if (stack != null) {
                if (stack.getType() == Material.AIR) {
                    return true;
                }
                if(stack.getType().equals(Material.GOLD_AXE) || stack.getType().equals(Material.FISHING_ROD) || stack.getType().equals(Material.GOLD_SWORD) || stack.getType().equals(Material.DIAMOND_HOE) || stack.getType().equals(Material.WOOD_AXE)) {
                	target.sendMessage(CC.translate("&cYou do not have permissions to repair this items."));
                	return true;
                }
                stack.setDurability((short)0);
            }
        }
        sender.sendMessage(ChatColor.YELLOW + "Repaired " + ((toRepair.size() > 1) ? "all" : "item in hand") + " of " + target.getName() + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
}

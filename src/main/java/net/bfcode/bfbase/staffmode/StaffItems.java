package net.bfcode.bfbase.staffmode;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.bfcode.bfbase.command.module.essential.StaffModeCommand;

public class StaffItems {

	@SuppressWarnings("deprecation")
	public static void modItems(Player p) {
		Inventory inv = p.getInventory();

		inv.clear();

		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemStack book = new ItemStack(Material.BOOK);
		ItemStack tp = new ItemStack(Material.WATCH);
		ItemStack vanish = new ItemStack(351, 1, (short) 10);
		ItemStack freeze = new ItemStack(Material.PACKED_ICE);
		ItemStack online = new ItemStack(Material.GHAST_TEAR);

		ItemMeta compassMeta = compass.getItemMeta();
		ItemMeta bookMeta = book.getItemMeta();
		ItemMeta eggMeta = tp.getItemMeta();
		ItemMeta vanishMeta = vanish.getItemMeta();
		ItemMeta freezeMeta = freeze.getItemMeta();
		ItemMeta onlineMeta = online.getItemMeta();
		

		compassMeta.setDisplayName("§bTeleporter");
		bookMeta.setDisplayName("§bInventory");
		eggMeta.setDisplayName("§bRandom TP");
		vanishMeta.setDisplayName("§3Vanish: §aEnabled");
		freezeMeta.setDisplayName("§bFreeze");
		onlineMeta.setDisplayName("§bTop");

		compass.setItemMeta(compassMeta);
		book.setItemMeta(bookMeta);
		tp.setItemMeta(eggMeta);
		vanish.setItemMeta(vanishMeta);
		freeze.setItemMeta(freezeMeta);
		online.setItemMeta(onlineMeta);
		
		if(p.isOp()) {
			ItemStack worldedit = new ItemStack(Material.WOOD_AXE);
			ItemMeta worldeditMeta = worldedit.getItemMeta();
			worldeditMeta.setDisplayName("§bWorldEdit");
			worldedit.setItemMeta(worldeditMeta);
			inv.setItem(6, worldedit);
		}

		inv.setItem(0, compass);
		inv.setItem(1, book);
		inv.setItem(4, freeze);
		inv.setItem(7, tp);
		inv.setItem(8, vanish);
		inv.setItem(2, online);
	}

	public static void saveInventory(Player p) {
		StaffModeCommand.armorContents.put(p.getName(), p.getInventory().getArmorContents());
		StaffModeCommand.inventoryContents.put(p.getName(), p.getInventory().getContents());
	}

	public static void loadInventory(Player p) {
		p.getInventory().clear();

		p.getInventory().setContents((ItemStack[]) StaffModeCommand.inventoryContents.get(p.getName()));
		p.getInventory().setArmorContents((ItemStack[]) StaffModeCommand.armorContents.get(p.getName()));

		StaffModeCommand.inventoryContents.remove(p.getName());
		StaffModeCommand.armorContents.remove(p.getName());
	}

}







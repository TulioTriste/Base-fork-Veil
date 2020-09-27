package net.bfcode.bfbase.staffmode;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import net.bfcode.bfhcf.utils.CC;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.util.TimeUtils;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;

public class StaffInventory {
	  private final static BasePlugin plugin = BasePlugin.getPlugin();

	public static String translate(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}

	public static List<String> translateFromArray(List<String> text) {
		List<String> messages = new ArrayList<String>();
		for (String string : text) {
			messages.add(translate(string));
		}
		return messages;
	}

	public static void inspector(Player player, Player target) {
		Inventory inventory = Bukkit.getServer().createInventory(null, 45, ChatColor.translateAlternateColorCodes('&', "&bInspecting: " + target.getName()));
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				PlayerInventory playerInventory = target.getInventory();

				ItemStack cookedBeef = new ItemStack(Material.COOKED_BEEF, target.getFoodLevel());
				ItemMeta cookedBeefMeta = cookedBeef.getItemMeta();
				cookedBeefMeta.setDisplayName(translate("&bHunger"));
				cookedBeef.setItemMeta(cookedBeefMeta);
						
				ItemStack brewingStand = new ItemStack(Material.BREWING_STAND_ITEM, target.getPlayer().getActivePotionEffects().size());
				ItemMeta brewingStandMeta = brewingStand.getItemMeta();
				brewingStandMeta.setDisplayName(translate("&bActive Effects"));
				ArrayList<String> brewingStandLore = new ArrayList<String>();
				for (PotionEffect potionEffect : target.getPlayer().getActivePotionEffects()) {
					String effectName = potionEffect.getType().getName();
					int effectLevel = potionEffect.getAmplifier();
					effectLevel++;
					brewingStandLore.add(translate("&b" + WordUtils.capitalizeFully(effectName).replace("_", " ") + " " + effectLevel + "&7: &c" + TimeUtils.IntegerCountdown.setFormat(Integer.valueOf(potionEffect.getDuration() / 20))));
				}
				brewingStandMeta.setLore(brewingStandLore);
				brewingStand.setItemMeta(brewingStandMeta);

			    ItemStack compass = new ItemStack(Material.COMPASS, 1);
				ItemMeta compassMeta = compass.getItemMeta();
				compassMeta.setDisplayName(translate("&bPlayer Location"));
				compassMeta.setLore(translateFromArray(Arrays.asList(new String[] { "&bWorld&7: &b" + player.getWorld().getName(),"&bCoords", "  &bX&7: &c" + target.getLocation().getBlockX(), "  &bY&7: &c" + target.getLocation().getBlockY(), "  &bZ&7: &c" + target.getLocation().getBlockZ() })));
				compass.setItemMeta(compassMeta);

				inventory.setContents(playerInventory.getContents());
				inventory.setItem(36, playerInventory.getHelmet());
				inventory.setItem(37, playerInventory.getChestplate());
				inventory.setItem(38, playerInventory.getLeggings());
				inventory.setItem(39, playerInventory.getBoots());
				inventory.setItem(40, playerInventory.getItemInHand());
				inventory.setItem(42, cookedBeef);
				inventory.setItem(43, brewingStand);
				inventory.setItem(44, compass);				

			}
		}, 0, 20);
		player.openInventory(inventory);
	}
	
//	public static void selector(Player player) {
//		Inventory inventory = Bukkit.getServer().createInventory(null, 9, CC.translate("&2&lSelector"));
//		
//		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
//			public void run() {
//				
//				if(BasePlugin.getPlugin().getConfig().getBoolean("servers-inventory.slot-1.activated")) {
//					ItemStack item = new ItemStack(Material.ANVIL, 1);
//					ItemMeta itemMeta = item.getItemMeta();
//					itemMeta.setDisplayName(CC.translate(BasePlugin.getPlugin().getConfig().getString("servers-inventory.slot-1.server")));
//					item.setItemMeta(itemMeta);
//					
//					inventory.setItem(0, item);
//				}
//				
//				if(BasePlugin.getPlugin().getConfig().getBoolean("servers-inventory.slot-2.activated")) {
//					ItemStack item = new ItemStack(Material.ANVIL, 1);
//					ItemMeta itemMeta = item.getItemMeta();
//					itemMeta.setDisplayName(CC.translate(BasePlugin.getPlugin().getConfig().getString("servers-inventory.slot-2.server")));
//					item.setItemMeta(itemMeta);
//					
//					inventory.setItem(1, item);
//				}
//				
//				if(BasePlugin.getPlugin().getConfig().getBoolean("servers-inventory.slot-3.activated")) {
//					ItemStack item = new ItemStack(Material.ANVIL, 1);
//					ItemMeta itemMeta = item.getItemMeta();
//					itemMeta.setDisplayName(CC.translate(BasePlugin.getPlugin().getConfig().getString("servers-inventory.slot-3.server")));
//					item.setItemMeta(itemMeta);
//					
//					inventory.setItem(2, item);
//				}
//				
//				if(BasePlugin.getPlugin().getConfig().getBoolean("servers-inventory.slot-4.activated")) {
//					ItemStack item = new ItemStack(Material.ANVIL, 1);
//					ItemMeta itemMeta = item.getItemMeta();
//					itemMeta.setDisplayName(CC.translate(BasePlugin.getPlugin().getConfig().getString("servers-inventory.slot-4.server")));
//					item.setItemMeta(itemMeta);
//					
//					inventory.setItem(3, item);
//				}
//				
//				if(BasePlugin.getPlugin().getConfig().getBoolean("servers-inventory.slot-5.activated")) {
//					ItemStack item = new ItemStack(Material.ANVIL, 1);
//					ItemMeta itemMeta = item.getItemMeta();
//					itemMeta.setDisplayName(CC.translate(BasePlugin.getPlugin().getConfig().getString("servers-inventory.slot-5.server")));
//					item.setItemMeta(itemMeta);
//					
//					inventory.setItem(4, item);
//				}
//				
//				if(BasePlugin.getPlugin().getConfig().getBoolean("servers-inventory.slot-6.activated")) {
//					ItemStack item = new ItemStack(Material.ANVIL, 1);
//					ItemMeta itemMeta = item.getItemMeta();
//					itemMeta.setDisplayName(CC.translate(BasePlugin.getPlugin().getConfig().getString("servers-inventory.slot-6.server")));
//					item.setItemMeta(itemMeta);
//					
//					inventory.setItem(5, item);
//				}
//				
//				if(BasePlugin.getPlugin().getConfig().getBoolean("servers-inventory.slot-7.activated")) {
//					ItemStack item = new ItemStack(Material.ANVIL, 1);
//					ItemMeta itemMeta = item.getItemMeta();
//					itemMeta.setDisplayName(CC.translate(BasePlugin.getPlugin().getConfig().getString("servers-inventory.slot-7.server")));
//					item.setItemMeta(itemMeta);
//					
//					inventory.setItem(6, item);
//				}
//				
//				if(BasePlugin.getPlugin().getConfig().getBoolean("servers-inventory.slot-8.activated")) {
//					ItemStack item = new ItemStack(Material.ANVIL, 1);
//					ItemMeta itemMeta = item.getItemMeta();
//					itemMeta.setDisplayName(CC.translate(BasePlugin.getPlugin().getConfig().getString("servers-inventory.slot-8.server")));
//					item.setItemMeta(itemMeta);
//					
//					inventory.setItem(7, item);
//				}
//				
//				if(BasePlugin.getPlugin().getConfig().getBoolean("servers-inventory.slot-9.activated")) {
//					ItemStack item = new ItemStack(Material.ANVIL, 1);
//					ItemMeta itemMeta = item.getItemMeta();
//					itemMeta.setDisplayName(CC.translate(BasePlugin.getPlugin().getConfig().getString("servers-inventory.slot-9.server")));
//					item.setItemMeta(itemMeta);
//					
//					inventory.setItem(8, item);
//				}
//				}
//			}, 0, 20);
//		player.openInventory(inventory);
//	}
}

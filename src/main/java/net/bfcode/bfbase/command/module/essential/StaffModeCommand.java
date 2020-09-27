package net.bfcode.bfbase.command.module.essential;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.listener.VanishListener;
import net.bfcode.bfbase.staffmode.StaffItems;
import net.bfcode.bfhcf.HCFaction;
import net.bfcode.bfhcf.scoreboard.PlayerBoard;

public class StaffModeCommand extends BaseCommand implements Listener {
	
	public static ArrayList<Player> modMode = new ArrayList<Player>();
	public static ArrayList<UUID> Staff = new ArrayList<UUID>();
	public static ArrayList<Player> teleportList = new ArrayList<Player>();
	public static HashMap<String, ItemStack[]> armorContents = new HashMap<>();
	public static HashMap<String, ItemStack[]> inventoryContents = new HashMap<>();
	private static BasePlugin plugin;
	
	public String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public StaffModeCommand(final BasePlugin plugin) {
        super("staffmode", "for the staffmode");
        this.setUsage("/(command)");
        this.plugin = plugin;
	}

	static StaffModeCommand instance = new StaffModeCommand(plugin);

	public static StaffModeCommand getInstance() {
		return instance;
	}

	public static boolean isMod(Player p) {
		return Staff.contains(p.getUniqueId());
	}

	public static boolean enterMod(final Player p) {
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		helmet.addEnchantment(Enchantment.DURABILITY, 3);
		chestplate.addEnchantment(Enchantment.DURABILITY, 3);
		leggings.addEnchantment(Enchantment.DURABILITY, 3);
		boots.addEnchantment(Enchantment.DURABILITY, 3);
		modMode.add(p);
		Staff.add(p.getUniqueId());
		StaffItems.saveInventory(p);
		VanishListener.getInstance().setVanish(p, true);
		for(Player players : Bukkit.getOnlinePlayers()) {
			HCFaction.getPlugin().getScoreboardHandler().getPlayerBoard(players.getUniqueId()).init(p);
		}
		p.getInventory().clear();
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(chestplate);
		p.getInventory().setLeggings(leggings);
		p.getInventory().setBoots(boots);
		p.setExp(0.0F);
		p.setAllowFlight(true);
		p.setGameMode(GameMode.CREATIVE);
		StaffItems.modItems(p);
		p.sendMessage("§bStaffMode has been §aenabled.");
		return true;
	}

	public static boolean leaveMod(final Player p) {
		modMode.remove(p);
		Staff.remove(p.getUniqueId());
		p.getInventory().clear();
		StaffItems.loadInventory(p);
		VanishListener.getInstance().setVanish(p, false);
		p.sendMessage("§bStaffMode has been §cdisabled.");
		if(!p.hasPermission("command.gamemode") || !p.isOp()) {
			p.setGameMode(GameMode.SURVIVAL);
			p.setAllowFlight(true);
		} else {
			p.setGameMode(GameMode.CREATIVE);
			p.setAllowFlight(true);
		}
		return true;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!sender.hasPermission("command.staffmode")) {
				sender.sendMessage(ChatColor.RED + "You lack the sufficient permissions to execute this command.");
				return true;
			}
			if (args.length < 1) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "User command only");
					return true;
				}
				if (modMode.contains(sender)) {
					leaveMod((Player) sender);
					return true;
				}
				enterMod((Player) sender);
				return true;
			}
			if (!sender.hasPermission("command.staffmode.others")) {
				sender.sendMessage(ChatColor.RED + "No.");
				return true;
			}
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				sender.sendMessage("§cPlayer not found.");
				return true;
			}
			if (modMode.contains(t)) {
				leaveMod(t);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have &cdisabled &7" + t.getName() + "'s &b&lStaff Mode"));
				return true;
			}
			enterMod(t);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have &aenabled &7" + t.getName() + "'s &b&lStaff Mode"));
			return true;
	}
	
	public static void onDisableMod() {
		    List<Player> player = new ArrayList<>();
		    for(Player players : Bukkit.getOnlinePlayers()) {
		    	player.add(players);
		    }
		    int j = player.size();
		    for (int i = 0; i < j; i++) {
		      Player p = player.get(i);
		      if (Staff.contains(p.getUniqueId())) {
		        leaveMod(p);
		        p.sendMessage(ChatColor.RED.toString() + "You have been taken out of staff mode because of a reload.");
		        teleportList.remove(p);
		      }
		    }
		  }

}

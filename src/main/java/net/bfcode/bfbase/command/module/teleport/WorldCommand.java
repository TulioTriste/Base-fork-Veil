package net.bfcode.bfbase.command.module.teleport;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.bfcode.bfbase.command.BaseCommand;

public class WorldCommand extends BaseCommand implements Listener
{
    public Inventory inv;
    
    public WorldCommand() {
        super("world", "Change current world.");
        this.setAliases(new String[] { "changeworld", "switchworld" });
        this.setUsage("/(command) <worldName>");
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        this.inv = Bukkit.createInventory((InventoryHolder)null, 9, "Worlds");
        
        final ItemStack overworld = new ItemStack(Material.GRASS, 1, (short)3);
        final ItemMeta overworldm = overworld.getItemMeta();
        overworldm.setLore(Arrays.asList(ChatColor.GRAY + " Click to teleport to the " + ChatColor.YELLOW + "Overworld"));
        overworldm.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Overworld");
        overworld.setItemMeta(overworldm);
        this.inv.setItem(1, overworld);
        
        final ItemStack nether = new ItemStack(Material.NETHERRACK, 1, (short)3);
        final ItemMeta netherm = nether.getItemMeta();
        netherm.setLore(Arrays.asList(ChatColor.GRAY + " Click to teleport to the " + ChatColor.YELLOW + "Nether"));
        netherm.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Nether");
        nether.setItemMeta(netherm);
        this.inv.setItem(3, nether);
        
        final ItemStack end = new ItemStack(Material.ENDER_STONE, 1, (short)3);
        final ItemMeta endm = end.getItemMeta();
        endm.setLore(Arrays.asList(ChatColor.GRAY + " Click to teleport to the " + ChatColor.YELLOW + "End"));
        endm.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "End");
        end.setItemMeta(endm);
        this.inv.setItem(5, end);
        
        final ItemStack event = new ItemStack(Material.BOW, 1, (short)0);
        final ItemMeta eventm = event.getItemMeta();
        eventm.setLore(Arrays.asList(ChatColor.GRAY + " Click to teleport to the " + ChatColor.YELLOW + "Event"));
        eventm.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Event");
        event.setItemMeta(eventm);
        this.inv.setItem(7, event);
        
        ((Player)sender).openInventory(this.inv);
        return true;
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final ItemStack clicked = event.getCurrentItem();
        final Inventory inventory = event.getInventory();
        if (inventory.getName().equals("Worlds")) {
            if (clicked.getType() == Material.GRASS) {
                event.setCancelled(true);
                player.closeInventory();
                final Location location = Bukkit.getWorld("world").getSpawnLocation();
                player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
            }
            if (clicked.getType() == Material.NETHERRACK) {
                event.setCancelled(true);
                player.closeInventory();
                final Location location = Bukkit.getWorld("world_nether").getSpawnLocation();
                player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
            }
            if (clicked.getType() == Material.ENDER_STONE) {
                event.setCancelled(true);
                player.closeInventory();
                final Location location = Bukkit.getWorld("world_the_end").getSpawnLocation();
                player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
            }
            if (clicked.getType() == Material.BOW) {
                event.setCancelled(true);
                player.closeInventory();
                final Location location = Bukkit.getWorld("events").getSpawnLocation();
                player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
            }
            event.setCancelled(true);
        }
    }
}

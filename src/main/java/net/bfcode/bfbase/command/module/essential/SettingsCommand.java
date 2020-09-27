package net.bfcode.bfbase.command.module.essential;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.user.BaseUser;
import net.bfcode.bfbase.util.ItemBuilder;

public class SettingsCommand extends BaseCommand implements Listener, InventoryHolder
{
    private final BasePlugin plugin;
    
    public SettingsCommand(final BasePlugin plugin) {
        super("settings", "Configure the looking on the server.");
        this.setUsage("/(command) <message>");
        this.setAliases(new String[] { "options", "config" });
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You may not execute this command.");
            return true;
        }
        final Player player = (Player)sender;
        player.sendMessage(ChatColor.YELLOW + "Opening your menu...");
        final ItemStack[] contents = this.getContents(player);
        final Inventory inv = Bukkit.createInventory((InventoryHolder)this, contents.length, ChatColor.translateAlternateColorCodes('&', "&6&lSettings"));
        inv.setContents(contents);
        player.openInventory(inv);
        return true;
    }
    
    private ItemStack[] getContents(final Player player) {
        final BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        final ItemStack[] itemStacks = { null, new ItemBuilder(Material.INK_SACK, 1, (byte)(baseUser.isMessagesVisible() ? 10 : 8)).displayName((baseUser.isMessagesVisible() ? ChatColor.GREEN : ChatColor.RED) + "Private messages").lore(this.c("&7Toggle whether you receive messages", "", "&7Click to toggle")).build(), null, null, new ItemBuilder(Material.INK_SACK, 1, (byte)(baseUser.isMessagingSounds() ? 10 : 8)).displayName((baseUser.isMessagingSounds() ? ChatColor.GREEN : ChatColor.RED) + "Sounds").lore(this.c("&7Toggle whether you want notifications sounds.", "", "&7Click to toggle")).build(), null, null, null, null };
        return itemStacks;
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getView() != null && event.getWhoClicked() instanceof Player && event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof SettingsCommand) {
            final Player player = (Player)event.getWhoClicked();
            final BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
            event.setCancelled(true);
            if (baseUser != null) {
                final int slot = event.getSlot();
                switch (slot) {
                    case 1: {
                        player.performCommand("togglepm");
                        break;
                    }
                    case 4: {
                        player.performCommand("togglesounds");
                        break;
                    }
                    default: {
                        return;
                    }
                }
                final Inventory inventory = event.getClickedInventory();
                inventory.setContents(this.getContents(player));
            }
        }
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
    
    private String[] c(final String... strs) {
        for (int i = 0; i < strs.length; ++i) {
            strs[i] = ChatColor.translateAlternateColorCodes('&', strs[i]);
        }
        return strs;
    }
    
    public Inventory getInventory() {
        return null;
    }
}

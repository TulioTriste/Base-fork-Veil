package net.bfcode.bfbase.command.module.essential;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.drops.Drop;
import net.md_5.bungee.api.ChatColor;

public class DropsCommand extends BaseCommand implements Listener {
	
    private Inventory inv;
    private BasePlugin plugin;
    
    public DropsCommand(BasePlugin plugin) {
        super("drops", "Toggle mob drops.");
        new HashMap<UUID, String>();
        this.plugin = plugin;
        this.setUsage("/(command)");
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage(this.getUsage());
            return true;
        }
        Player player = (Player)sender;
        if (this.plugin.getDropsManager().getDrop(player.getUniqueId().toString()) == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "New user registered into Drops database.");
            Drop drop = new Drop(player.getUniqueId().toString(), true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
            this.plugin.getDropsManager().createDrop(drop);
        }
        this.inv = Bukkit.createInventory((InventoryHolder)player, 54, "Mob Drops Manager");
        this.DropsGui(player);
        return true;
    }
    
    private void DropsGui(Player player) {
        Drop drop = this.plugin.getDropsManager().getDrop(player.getUniqueId().toString());
        ItemStack beef = new ItemStack(Material.RAW_BEEF, 1);
        this.inv.setItem(0, beef);
        ItemStack status_beef = new ItemStack(Material.WOOL, 1, (short)(drop.getBeef().equals(false) ? 14 : 5));
        ItemMeta meta_status_beef = status_beef.getItemMeta();
        meta_status_beef.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Raw Beef: " + (drop.getBeef().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_beef.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_beef.setItemMeta(meta_status_beef);
        this.inv.setItem(1, status_beef);
        ItemStack pork = new ItemStack(Material.PORK, 1);
        this.inv.setItem(9, pork);
        ItemStack status_pork = new ItemStack(Material.WOOL, 1, (short)(drop.getPork().equals(false) ? 14 : 5));
        ItemMeta meta_status_pork = status_pork.getItemMeta();
        meta_status_pork.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Raw Porkchop: " + (drop.getPork().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_pork.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_pork.setItemMeta(meta_status_pork);
        this.inv.setItem(10, status_pork);
        ItemStack chicken = new ItemStack(Material.RAW_CHICKEN, 1);
        this.inv.setItem(18, chicken);
        ItemStack status_chicken = new ItemStack(Material.WOOL, 1, (short)(drop.getChicken().equals(false) ? 14 : 5));
        ItemMeta meta_status_chicken = status_chicken.getItemMeta();
        meta_status_chicken.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Raw Chicken: " + (drop.getChicken().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_chicken.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_chicken.setItemMeta(meta_status_chicken);
        this.inv.setItem(19, status_chicken);
        ItemStack feather = new ItemStack(Material.FEATHER, 1);
        this.inv.setItem(27, feather);
        ItemStack status_feather = new ItemStack(Material.WOOL, 1, (short)(drop.getFeather().equals(false) ? 14 : 5));
        ItemMeta meta_status_feather = status_feather.getItemMeta();
        meta_status_feather.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Feather: " + (drop.getFeather().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_feather.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_feather.setItemMeta(meta_status_feather);
        this.inv.setItem(28, status_feather);
        ItemStack wool = new ItemStack(Material.WOOL, 1);
        this.inv.setItem(36, wool);
        ItemStack status_wool = new ItemStack(Material.WOOL, 1, (short)(drop.getWool().equals(false) ? 14 : 5));
        ItemMeta meta_status_wool = status_wool.getItemMeta();
        meta_status_wool.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Wool: " + (drop.getWool().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_wool.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_wool.setItemMeta(meta_status_wool);
        this.inv.setItem(37, status_wool);
        ItemStack leather = new ItemStack(Material.LEATHER, 1);
        this.inv.setItem(45, leather);
        ItemStack status_leather = new ItemStack(Material.WOOL, 1, (short)(drop.getLeather().equals(false) ? 14 : 5));
        ItemMeta meta_status_leather = status_leather.getItemMeta();
        meta_status_leather.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Leather: " + (drop.getLeather().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_leather.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_leather.setItemMeta(meta_status_leather);
        this.inv.setItem(46, status_leather);
        ItemStack magmacream = new ItemStack(Material.MAGMA_CREAM, 1);
        this.inv.setItem(21, magmacream);
        ItemStack status_magmacream = new ItemStack(Material.WOOL, 1, (short)(drop.getMagmacream().equals(false) ? 14 : 5));
        ItemMeta meta_status_magmacream = status_magmacream.getItemMeta();
        meta_status_magmacream.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Magma Cream: " + (drop.getMagmacream().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_magmacream.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_magmacream.setItemMeta(meta_status_magmacream);
        this.inv.setItem(30, status_magmacream);
        ItemStack ghasttear = new ItemStack(Material.GHAST_TEAR, 1);
        this.inv.setItem(22, ghasttear);
        ItemStack status_ghasttear = new ItemStack(Material.WOOL, 1, (short)(drop.getGhasttear().equals(false) ? 14 : 5));
        ItemMeta meta_status_ghasttear = status_ghasttear.getItemMeta();
        meta_status_ghasttear.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Ghast Tear: " + (drop.getGhasttear().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_ghasttear.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_ghasttear.setItemMeta(meta_status_ghasttear);
        this.inv.setItem(31, status_ghasttear);
        ItemStack nugget = new ItemStack(Material.GOLD_NUGGET, 1);
        this.inv.setItem(23, nugget);
        ItemStack status_nugget = new ItemStack(Material.WOOL, 1, (short)(drop.getNugget().equals(false) ? 14 : 5));
        ItemMeta meta_status_nugget = status_nugget.getItemMeta();
        meta_status_nugget.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Gold Nugget: " + (drop.getNugget().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_nugget.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_nugget.setItemMeta(meta_status_nugget);
        this.inv.setItem(32, status_nugget);
        ItemStack blazerod = new ItemStack(Material.BLAZE_ROD, 1);
        this.inv.setItem(48, blazerod);
        ItemStack status_blazerod = new ItemStack(Material.WOOL, 1, (short)(drop.getBlazerod().equals(false) ? 14 : 5));
        ItemMeta meta_status_blazerod = status_blazerod.getItemMeta();
        meta_status_blazerod.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Blaze Rod: " + (drop.getBlazerod().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_blazerod.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_blazerod.setItemMeta(meta_status_blazerod);
        this.inv.setItem(39, status_blazerod);
        ItemStack gunpowder = new ItemStack(Material.SULPHUR, 1);
        this.inv.setItem(49, gunpowder);
        ItemStack status_gunpowder = new ItemStack(Material.WOOL, 1, (short)(drop.getGunpowder().equals(false) ? 14 : 5));
        ItemMeta meta_status_gunpowder = status_gunpowder.getItemMeta();
        meta_status_gunpowder.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Gunpowder: " + (drop.getGunpowder().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_gunpowder.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_gunpowder.setItemMeta(meta_status_gunpowder);
        this.inv.setItem(40, status_gunpowder);
        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 1);
        this.inv.setItem(50, enderpearl);
        ItemStack status_enderpearl = new ItemStack(Material.WOOL, 1, (short)(drop.getEnderpearl().equals(false) ? 14 : 5));
        ItemMeta meta_status_enderpearl = status_enderpearl.getItemMeta();
        meta_status_enderpearl.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Ender Pearl: " + (drop.getEnderpearl().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_enderpearl.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_enderpearl.setItemMeta(meta_status_enderpearl);
        this.inv.setItem(41, status_enderpearl);
        ItemStack flesh = new ItemStack(Material.ROTTEN_FLESH, 1);
        this.inv.setItem(8, flesh);
        ItemStack status_flesh = new ItemStack(Material.WOOL, 1, (short)(drop.getFlesh().equals(false) ? 14 : 5));
        ItemMeta meta_status_flesh = status_flesh.getItemMeta();
        meta_status_flesh.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Rotten Flesh: " + (drop.getFlesh().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_flesh.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_flesh.setItemMeta(meta_status_flesh);
        this.inv.setItem(7, status_flesh);
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        this.inv.setItem(17, arrow);
        ItemStack status_arrow = new ItemStack(Material.WOOL, 1, (short)(drop.getArrow().equals(false) ? 14 : 5));
        ItemMeta meta_status_arrow = status_arrow.getItemMeta();
        meta_status_arrow.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Arrow: " + (drop.getArrow().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_arrow.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_arrow.setItemMeta(meta_status_arrow);
        this.inv.setItem(16, status_arrow);
        ItemStack bone = new ItemStack(Material.BONE, 1);
        this.inv.setItem(26, bone);
        ItemStack status_bone = new ItemStack(Material.WOOL, 1, (short)(drop.getBone().equals(false) ? 14 : 5));
        ItemMeta meta_status_bone = status_bone.getItemMeta();
        meta_status_bone.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Bone: " + (drop.getBone().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_bone.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_bone.setItemMeta(meta_status_bone);
        this.inv.setItem(25, status_bone);
        ItemStack string = new ItemStack(Material.STRING, 1);
        this.inv.setItem(35, string);
        ItemStack status_string = new ItemStack(Material.WOOL, 1, (short)(drop.getStringItem().equals(false) ? 14 : 5));
        ItemMeta meta_status_string = status_string.getItemMeta();
        meta_status_string.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "String: " + (drop.getStringItem().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_string.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_string.setItemMeta(meta_status_string);
        this.inv.setItem(34, status_string);
        ItemStack spidereye = new ItemStack(Material.SPIDER_EYE, 1);
        this.inv.setItem(44, spidereye);
        ItemStack status_spidereye = new ItemStack(Material.WOOL, 1, (short)(drop.getSpidereye().equals(false) ? 14 : 5));
        ItemMeta meta_status_spidereye = status_spidereye.getItemMeta();
        meta_status_spidereye.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Spider Eye: " + (drop.getSpidereye().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_spidereye.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_spidereye.setItemMeta(meta_status_spidereye);
        this.inv.setItem(43, status_spidereye);
        ItemStack slimeball = new ItemStack(Material.SLIME_BALL, 1);
        this.inv.setItem(53, slimeball);
        ItemStack status_slimeball = new ItemStack(Material.WOOL, 1, (short)(drop.getSlimeball().equals(false) ? 14 : 5));
        ItemMeta meta_status_slimeball = status_slimeball.getItemMeta();
        meta_status_slimeball.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Slimeball: " + (drop.getSlimeball().equals(false) ? (ChatColor.RED + "Disabled.") : (ChatColor.GREEN + "Enabled.")));
        meta_status_slimeball.setLore((List)Arrays.asList(ChatColor.YELLOW + "Click to change the status of this drop."));
        status_slimeball.setItemMeta(meta_status_slimeball);
        this.inv.setItem(52, status_slimeball);
        ItemStack enable_all = new ItemStack(Material.INK_SACK, 1, (short)10);
        ItemMeta meta_enable_all = enable_all.getItemMeta();
        meta_enable_all.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Enable all.");
        enable_all.setItemMeta(meta_enable_all);
        this.inv.setItem(3, enable_all);
        ItemStack disable_all = new ItemStack(Material.INK_SACK, 1, (short)1);
        ItemMeta meta_disable_all = disable_all.getItemMeta();
        meta_disable_all.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Disable all.");
        disable_all.setItemMeta(meta_disable_all);
        this.inv.setItem(5, disable_all);
        ItemStack book = new ItemStack(Material.BOOK, 1);
        ItemMeta meta_book = book.getItemMeta();
        meta_book.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "Read before using.");
        meta_book.setLore((List)Arrays.asList(ChatColor.YELLOW + "Use this GUI to disable specific mob drops by clicking on the wools.", ChatColor.YELLOW + "While you have one disabled, you wont be able to pickup it."));
        meta_book.addEnchant(Enchantment.DURABILITY, 1, true);
        book.setItemMeta(meta_book);
        this.inv.setItem(4, book);
        player.openInventory(this.inv);
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals("Mob Drops Manager")) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        Drop drop = this.plugin.getDropsManager().getDrop(player.getUniqueId().toString());
        event.setCancelled(true);
        switch (event.getRawSlot()) {
            case 1: {
                if (drop.getBeef().equals(false)) {
                    drop.setBeef(true);
                }
                else {
                    drop.setBeef(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 10: {
                if (drop.getPork().equals(false)) {
                    drop.setPork(true);
                }
                else {
                    drop.setPork(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 19: {
                if (drop.getChicken().equals(false)) {
                    drop.setChicken(true);
                }
                else {
                    drop.setChicken(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 28: {
                if (drop.getFeather().equals(false)) {
                    drop.setFeather(true);
                }
                else {
                    drop.setFeather(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 37: {
                if (drop.getWool().equals(false)) {
                    drop.setWool(true);
                }
                else {
                    drop.setWool(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 46: {
                if (drop.getLeather().equals(false)) {
                    drop.setLeather(true);
                }
                else {
                    drop.setLeather(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 30: {
                if (drop.getMagmacream().equals(false)) {
                    drop.setMagmacream(true);
                }
                else {
                    drop.setMagmacream(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 31: {
                if (drop.getGhasttear().equals(false)) {
                    drop.setGhasttear(true);
                }
                else {
                    drop.setGhasttear(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 32: {
                if (drop.getNugget().equals(false)) {
                    drop.setNugget(true);
                }
                else {
                    drop.setNugget(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 39: {
                if (drop.getBlazerod().equals(false)) {
                    drop.setBlazerod(true);
                }
                else {
                    drop.setBlazerod(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 40: {
                if (drop.getGunpowder().equals(false)) {
                    drop.setGunpowder(true);
                }
                else {
                    drop.setGunpowder(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 41: {
                if (drop.getEnderpearl().equals(false)) {
                    drop.setEnderpearl(true);
                }
                else {
                    drop.setEnderpearl(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 7: {
                if (drop.getFlesh().equals(false)) {
                    drop.setFlesh(true);
                }
                else {
                    drop.setFlesh(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 16: {
                if (drop.getArrow().equals(false)) {
                    drop.setArrow(true);
                }
                else {
                    drop.setArrow(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 25: {
                if (drop.getBone().equals(false)) {
                    drop.setBone(true);
                }
                else {
                    drop.setBone(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 34: {
                if (drop.getStringItem().equals(false)) {
                    drop.setStringItem(true);
                }
                else {
                    drop.setStringItem(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 43: {
                if (drop.getSpidereye().equals(false)) {
                    drop.setSpidereye(true);
                }
                else {
                    drop.setSpidereye(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 52: {
                if (drop.getSlimeball().equals(false)) {
                    drop.setSlimeball(true);
                }
                else {
                    drop.setSlimeball(false);
                }
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have changed the status of one drop.");
                break;
            }
            case 3: {
                drop.setBeef(true);
                drop.setPork(true);
                drop.setChicken(true);
                drop.setFeather(true);
                drop.setWool(true);
                drop.setLeather(true);
                drop.setMagmacream(true);
                drop.setGhasttear(true);
                drop.setNugget(true);
                drop.setBlazerod(true);
                drop.setGunpowder(true);
                drop.setEnderpearl(true);
                drop.setFlesh(true);
                drop.setArrow(true);
                drop.setBone(true);
                drop.setStringItem(true);
                drop.setSpidereye(true);
                drop.setSlimeball(true);
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have enabled all drops.");
                break;
            }
            case 5: {
                drop.setBeef(false);
                drop.setPork(false);
                drop.setChicken(false);
                drop.setFeather(false);
                drop.setWool(false);
                drop.setLeather(false);
                drop.setMagmacream(false);
                drop.setGhasttear(false);
                drop.setNugget(false);
                drop.setBlazerod(false);
                drop.setGunpowder(false);
                drop.setEnderpearl(false);
                drop.setFlesh(false);
                drop.setArrow(false);
                drop.setBone(false);
                drop.setStringItem(false);
                drop.setSpidereye(false);
                drop.setSlimeball(false);
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "You have disabled all drops.");
                break;
            }
        }
    }
    
    @EventHandler
    public void onItemPickupSetCancelled(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Drop drop = this.plugin.getDropsManager().getDrop(player.getUniqueId().toString());
        if (drop != null) {
            if (event.getItem().getItemStack().getType() == Material.RAW_BEEF && drop.getBeef().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.PORK && drop.getPork().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.RAW_CHICKEN && drop.getChicken().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.FEATHER && drop.getFeather().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.WOOL && drop.getWool().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.LEATHER && drop.getLeather().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.MAGMA_CREAM && drop.getMagmacream().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.GHAST_TEAR && drop.getGhasttear().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.GOLD_NUGGET && drop.getNugget().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.BLAZE_ROD && drop.getBlazerod().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.SULPHUR && drop.getGunpowder().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.ENDER_PEARL && drop.getEnderpearl().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.ROTTEN_FLESH && drop.getFlesh().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.ARROW && drop.getArrow().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.BONE && drop.getBone().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.STRING && drop.getStringItem().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.SPIDER_EYE && drop.getSpidereye().equals(false)) {
                event.setCancelled(true);
            }
            if (event.getItem().getItemStack().getType() == Material.SLIME_BALL && drop.getSlimeball().equals(false)) {
                event.setCancelled(true);
            }
        }
    }
}

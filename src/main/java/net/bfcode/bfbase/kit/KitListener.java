package net.bfcode.bfbase.kit;

import java.util.ArrayList;
import java.util.UUID;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.block.BlockState;
import org.bukkit.block.Block;

import java.util.Arrays;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.meta.ItemMeta;

import net.bfcode.bfhcf.HCFaction;
import net.bfcode.bfhcf.faction.type.Faction;
import net.bfcode.bfhcf.faction.type.SpawnFaction;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.module.essential.StaffModeCommand;
import net.bfcode.bfbase.kit.event.KitApplyEvent;
import net.bfcode.bfbase.user.BaseUser;
import net.bfcode.bfbase.util.ParticleEffect;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.ChatColor;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import java.util.List;
import org.bukkit.event.Listener;

public class KitListener implements Listener
{
    public static List<Inventory> previewInventory;
    private final BasePlugin plugin;
    
    public KitListener(final BasePlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (KitListener.previewInventory.contains(e.getInventory())) {
            KitListener.previewInventory.remove(e.getInventory());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (KitListener.previewInventory.contains(event.getInventory())) {
            event.setCancelled(true);
            return;
        }
        final Inventory inventory = event.getInventory();
        if (inventory == null) {
            return;
        }
        final String title = inventory.getTitle();
        final HumanEntity humanEntity = event.getWhoClicked();
        if (title.contains("Kit Selector") && humanEntity instanceof Player) {
            event.setCancelled(true);
            if (!Objects.equals(event.getView().getTopInventory(), event.getClickedInventory())) {
                return;
            }
            final ItemStack stack = event.getCurrentItem();
            if (stack == null || !stack.hasItemMeta()) {
                return;
            }
            final ItemMeta meta = stack.getItemMeta();
            if (!meta.hasDisplayName()) {
                return;
            }
            final Player player = (Player)humanEntity;
            final String name = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
            final Kit kit = this.plugin.getKitManager().getKit(name);
            if (kit == null) {
                return;
            }
            kit.applyTo(player, false, true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onKitSign(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Block block = event.getClickedBlock();
            final BlockState state = block.getState();
            if (!(state instanceof Sign)) {
                return;
            }
        	final Player player = event.getPlayer();
            final Sign sign = (Sign)state;
            final String[] lines = sign.getLines();
            Faction factionAt = HCFaction.getPlugin().getFactionManager().getFactionAt(player.getLocation());
            if(factionAt instanceof SpawnFaction) {
            	if (lines.length >= 2 && lines[1].contains("Kit")) {
                	final Kit kit = this.plugin.getKitManager().getKit((lines.length >= 3) ? lines[2] : null);
                	if (kit == null) {
                    	return;
                	}
                	event.setCancelled(true);
                	final String[] fakeLines = Arrays.copyOf(sign.getLines(), 4);
                	final boolean applied = kit.applyTo(player, false, false);
                	if (applied) {
                    	fakeLines[0] = ChatColor.GREEN + "Successfully";
                    	fakeLines[1] = ChatColor.GREEN + "equipped kit";
                    	fakeLines[2] = kit.getDisplayName();
                    	fakeLines[3] = "";
                	}
                	else {
                    	fakeLines[0] = ChatColor.RED + "Failed to";
                    	fakeLines[1] = ChatColor.RED + "equip kit";
                    	fakeLines[2] = kit.getDisplayName();
                    	fakeLines[3] = ChatColor.RED + "Check chat";
                	}
                	if (this.plugin.getSignHandler().showLines(player, sign, fakeLines, 100L, false) && applied) {
                    	ParticleEffect.EXPLOSION_NORMAL.display(player, sign.getLocation().clone().add(0.5, 0.5, 0.5), 0.01f, 10);
                	}
            	}
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onKitApply(final KitApplyEvent event) {
        if (event.isForce()) {
            return;
        }
        final Player player = event.getPlayer();
        final Kit kit = event.getKit();
        if (!player.isOp() && !kit.isEnabled()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "The " + kit.getDisplayName() + " kit is currently disabled.");
            return;
        }
        final String kitPermission = kit.getPermissionNode();
        if (kitPermission != null && !player.hasPermission(kitPermission)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You do not have permission to use this kit.");
            return;
        }
        final UUID uuid = player.getUniqueId();
        final long minPlaytimeMillis = kit.getMinPlaytimeMillis();
        if (minPlaytimeMillis > 0L && this.plugin.getPlayTimeManager().getTotalPlayTime(uuid) < minPlaytimeMillis) {
            player.sendMessage(ChatColor.RED + "You need at least " + kit.getMinPlaytimeWords() + " minimum playtime to use kit " + kit.getDisplayName() + '.');
            event.setCancelled(true);
            return;
        }
        final BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
        final long remaining = baseUser.getRemainingKitCooldown(kit);
        if (remaining > 0L && !player.isOp()) {
            player.sendMessage(ChatColor.RED + "You cannot use the " + kit.getDisplayName() + " kit for " + DurationFormatUtils.formatDurationWords(remaining, true, true) + '.');
            event.setCancelled(true);
            return;
        }
        final int curUses = baseUser.getKitUses(kit);
        final int maxUses;
        if (curUses >= (maxUses = kit.getMaximumUses()) && maxUses != Integer.MAX_VALUE) {
            player.sendMessage(ChatColor.RED + "You have already used this kit " + curUses + '/' + maxUses + " times.");
            event.setCancelled(true);
        }
        if (StaffModeCommand.isMod(player)) {
            player.sendMessage(ChatColor.RED + "You cannot apply kits while in staff mode.");
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onKitApplyMonitor(final KitApplyEvent event) {
        if (!event.isForce()) {
            final Kit kit = event.getKit();
            final BaseUser baseUser = this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId());
            baseUser.incrementKitUses(kit);
            baseUser.updateKitCooldown(kit);
        }
    }
    
    static {
        KitListener.previewInventory = new ArrayList<Inventory>();
    }
}

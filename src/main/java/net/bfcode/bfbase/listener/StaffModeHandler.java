package net.bfcode.bfbase.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.bfcode.bfhcf.HCFaction;
import net.bfcode.bfbase.command.module.essential.StaffModeCommand;
import net.bfcode.bfbase.staffmode.StaffInventory;
import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.PacketPlayOutBlockAction;

public class StaffModeHandler implements Listener {
	
	private final Map<UUID, Location> fakeChestLocationMap = new HashMap<UUID, Location>();
	
	  public static void handleFakeChest(Player player, Chest chest, boolean open)
	  {
	    Inventory chestInventory = chest.getInventory();
	    if ((chestInventory instanceof DoubleChestInventory)) {
	      chest = (Chest)((DoubleChestInventory)chestInventory).getHolder().getLeftSide();
	    }
	    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutBlockAction(chest.getX(), chest.getY(), chest.getZ(), Blocks.CHEST, 1, open ? 1 : 0));
	    player.playSound(chest.getLocation(), open ? Sound.CHEST_OPEN : Sound.CHEST_CLOSE, 1.0F, 1.0F);
	  }
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (StaffModeCommand.modMode.contains(event.getPlayer())) {
			StaffModeCommand.leaveMod(event.getPlayer());
		}
	}

	@EventHandler
	public void onGamemodeChange(PlayerMoveEvent event) {
		if (event.getPlayer().hasPermission("command.staffmode")) {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				if (StaffModeCommand.modMode.contains(event.getPlayer())) {
					event.getPlayer().setGameMode(GameMode.CREATIVE);
					event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not switch gamemodes whilst in Staff Mode."));
				}
			}
		}
	}
	
	
	@EventHandler
	public void oJoin(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("command.staffmode")) {
			StaffModeCommand.enterMod(event.getPlayer());
			if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				event.getPlayer().setGameMode(GameMode.CREATIVE);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.isCancelled())
			return;
		Entity entity = e.getEntity();

		if ((entity instanceof Player)) {
			Player p = (Player) entity;
			if ((StaffModeCommand.modMode.contains(p)))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if ((!(e.getEntity() instanceof Player))) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (StaffModeCommand.modMode.contains(p)) {
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if (StaffModeCommand.modMode.contains(event.getPlayer().getName())) {
			event.getPlayer().getInventory().clear();
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
			StaffModeCommand.modMode.remove(event.getPlayer().getName());
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if ((StaffModeCommand.modMode.contains(p))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBreak(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if ((StaffModeCommand.modMode.contains(p))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if ((StaffModeCommand.modMode.contains(p)) && (p.getGameMode().equals(GameMode.CREATIVE))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPick(PlayerPickupItemEvent e) {
		if (StaffModeCommand.modMode.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onRecord(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((StaffModeCommand.modMode.contains(p)) && (p.getItemInHand().getType() == Material.WATCH)
				&& (e.getAction().toString().contains("RIGHT"))) {
			List<Player> asd = new ArrayList<>();
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				asd.add(player);
			}
			Random test = new Random();
			Player random = asd.get(test.nextInt(asd.size()));
			if (Bukkit.getServer().getOnlinePlayers().size() == 1) {
				p.sendMessage(ChatColor.RED + "There are not enough players to use this.");
			}
			if (Bukkit.getServer().getOnlinePlayers().size() > 1) {
				if (p != random) {
					p.teleport(random);
					p.sendMessage(ChatColor.YELLOW + "You were teleported randomly to " + ChatColor.GOLD
							+ random.getName() + ChatColor.YELLOW + ".");
				}
				if (p == random) {
					p.sendMessage(ChatColor.RED + "Oops, it just randomly picked up you, please try again.");
				}
			}
		}
	}

    @EventHandler
    public void onVanish(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        else if (StaffModeCommand.isMod(p) && p.getItemInHand().getItemMeta().getDisplayName().contains("§3Vanish: §aEnabled") && e.getAction().toString().contains("RIGHT")) {
            VanishListener.getInstance().setVanish(p, false);
            p.sendMessage(String.valueOf(ChatColor.YELLOW.toString()) + "You have toggled your vanish " + ChatColor.RED + "off" + ChatColor.YELLOW + ".");
            ItemStack carpet = new ItemStack(351, 1, (short)8);
            ItemMeta carpetMeta = carpet.getItemMeta();
            carpetMeta.setDisplayName("§3Vanish: §cDisable");
            carpet.setItemMeta(carpetMeta);
            p.getInventory().setItemInHand(carpet);
            e.setCancelled(true);
        }
        else if (StaffModeCommand.isMod(p) && p.getItemInHand().getItemMeta().getDisplayName().contains("§3Vanish: §cDisable") && e.getAction().toString().contains("RIGHT")) {
            VanishListener.getInstance().setVanish(p, true);
            p.sendMessage(String.valueOf(ChatColor.YELLOW.toString()) + "You have toggled your vanish " + ChatColor.GREEN + "on" + ChatColor.YELLOW + ".");
            ItemStack carpet = new ItemStack(351, 1, (short)10);
            ItemMeta carpetMeta = carpet.getItemMeta();
            carpetMeta.setDisplayName("§3Vanish: §aEnabled");
            carpet.setItemMeta(carpetMeta);
            p.getInventory().setItemInHand(carpet);
            e.setCancelled(true);
        }
    }
	
	@EventHandler
	public void onClickItemInventory(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(StaffModeCommand.isMod(player)) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getItemInHand().getType() == Material.GHAST_TEAR || event.getAction() == Action.RIGHT_CLICK_AIR && player.getItemInHand().getType() == Material.GHAST_TEAR) {
				Bukkit.dispatchCommand(player, "top");
			}
//			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getItemInHand().getType() == Material.PAPER || event.getAction() == Action.RIGHT_CLICK_AIR && player.getItemInHand().getType() == Material.PAPER) {
//				StaffInventory.selector(player);
//			}
		}
	}

	@EventHandler
	public void rightClick2(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof Player)) {
			return;
		}
		Player staff = e.getPlayer();
		Player p = (Player) e.getRightClicked();
		if ((StaffModeCommand.modMode.contains(staff)) && (staff.getGameMode() == GameMode.CREATIVE)
				&& ((p instanceof Player)) && ((staff instanceof Player))
				&& (staff.getItemInHand().getType() == Material.BOOK)) {
			StaffInventory.inspector(staff, p);
		}
	}
	
	  @EventHandler
	  public void freezeClick(PlayerInteractEntityEvent e)
	  {
	    if (!(e.getRightClicked() instanceof Player)) {
	      return;
	    }
	    Player staff = e.getPlayer();
	    Player p = (Player)e.getRightClicked();
	    if ((StaffModeCommand.modMode.contains(staff)) && (staff.getGameMode() == GameMode.CREATIVE) && 
	      ((p instanceof Player)) && ((staff instanceof Player)) && 
	      (staff.getItemInHand().getType() == Material.PACKED_ICE)) {
	      staff.chat("/freeze " + p.getName());
	    }
	  }
	  

	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onTag(EntityDamageByEntityEvent e) {
		if ((!(e.getEntity() instanceof Player)) || (!(e.getDamager() instanceof Player))) {
			return;
		}
		Player staff = (Player) e.getDamager();
		if (StaffModeCommand.modMode.contains(staff.getName())) {
			e.setCancelled(true);
		}
	}

	  @SuppressWarnings("incomplete-switch")
	@EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	  public void onPlayerInteract(PlayerInteractEvent event)
	  {
	    Player player = event.getPlayer();
	    UUID uuid = player.getUniqueId();
	    switch (event.getAction())
	    {
	    case LEFT_CLICK_BLOCK: 
	      if (StaffModeCommand.isMod(player)) {
	        event.setCancelled(true);
	      }
	      break;
	    case RIGHT_CLICK_BLOCK: 
	      Block block = event.getClickedBlock();
	      BlockState state = block.getState();
	      if (state instanceof Chest && StaffModeCommand.isMod(player))
	      {
	        Chest chest = (Chest)state;
	        Location chestLocation = chest.getLocation();
	        InventoryType type = chest.getInventory().getType();
	        if ((type == InventoryType.CHEST) && (this.fakeChestLocationMap.putIfAbsent(uuid, chestLocation) == null))
	        {
	          ItemStack[] contents = chest.getInventory().getContents();
	          Inventory fakeInventory = Bukkit.createInventory(null, contents.length, ChatColor.YELLOW + "[Silent] " + type.getDefaultTitle());
	          fakeInventory.setContents(contents);
	          event.setCancelled(true);
	          player.openInventory(fakeInventory);
	          handleFakeChest(player, chest, true);
	          this.fakeChestLocationMap.put(uuid, chestLocation);
	        }
	      }
	      break;
	    }
	  }
	  
	  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	  public void onInventoryClose(InventoryCloseEvent event)
	  {
	    Player player = (Player)event.getPlayer();
	    Location chestLocation = (Location)this.fakeChestLocationMap.remove(player.getUniqueId());
	    BlockState blockState;
	    if ((chestLocation != null) && (((blockState = chestLocation.getBlock().getState()) instanceof Chest))) {
	      handleFakeChest(player, (Chest)blockState, false);
	    }
	  }
	  
	  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	  public void onInventoryClick(InventoryClickEvent event)
	  {
	    HumanEntity humanEntity = event.getWhoClicked();
	    Player player;
	    ItemStack stack;
	    if (((humanEntity instanceof Player)) && (this.fakeChestLocationMap.containsKey((player = (Player)humanEntity).getUniqueId())) && ((stack = event.getCurrentItem()) != null) && (stack.getType() != Material.AIR) && (!player.hasPermission("vanish.chestinteract")))
	    {
	      event.setCancelled(true);
	      player.sendMessage(ChatColor.RED + "You cannot interact with this.");
	    }
	  }
	}
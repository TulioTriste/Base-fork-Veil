package net.bfcode.bfbase.kit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.ChatPaginator;

import com.google.common.collect.Lists;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.drops.CaseInsensitiveMap;
import net.bfcode.bfbase.kit.event.KitRenameEvent;
import net.bfcode.bfbase.util.Config;
import net.bfcode.bfbase.util.GenericUtils;
import net.bfcode.bfhcf.utils.ConfigurationService;

public class FlatFileKitManager implements KitManager, Listener {
	
    private final Map<String, Kit> kitNameMap;
    private final Map<UUID, Kit> kitUUIDMap;
    private final BasePlugin plugin;
    private Config config;
    private List<Kit> kits;
    
    public FlatFileKitManager(final BasePlugin plugin) {
        this.kitNameMap = new CaseInsensitiveMap<Kit>();
        this.kitUUIDMap = new HashMap<UUID, Kit>();
        this.kits = new ArrayList<Kit>();
        this.plugin = plugin;
        this.reloadKitData();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onKitRename(final KitRenameEvent event) {
        this.kitNameMap.remove(event.getOldName());
        this.kitNameMap.put(event.getNewName(), event.getKit());
    }
    
    @Override
    public List<Kit> getKits() {
        return this.kits;
    }
    
    @Override
    public Kit getKit(final UUID uuid) {
        return this.kitUUIDMap.get(uuid);
    }
    
    @Override
    public Kit getKit(final String id) {
        return this.kitNameMap.get(id);
    }
    
    @Override
    public boolean containsKit(final Kit kit) {
        return this.kits.contains(kit);
    }
    
    @Override
    public void createKit(final Kit kit) {
        if (this.kits.add(kit)) {
            this.kitNameMap.put(kit.getName(), kit);
            this.kitUUIDMap.put(kit.getUniqueID(), kit);
        }
    }
    
    @Override
    public void removeKit(final Kit kit) {
        if (this.kits.remove(kit)) {
            this.kitNameMap.remove(kit.getName());
            this.kitUUIDMap.remove(kit.getUniqueID());
        }
    }
    
    @Override
    public Inventory getGui(final Player player) {
        final UUID uuid = player.getUniqueId();
        final Inventory inventory = Bukkit.createInventory(player, (ConfigurationService.KIT_MAP ? (this.kits.size() + 9 - 1) / 9 * 9 : 45), ChatColor.BLUE + "Kit Selector");
        for (final Kit kit : this.kits) {
            final ItemStack stack = kit.getImage();
            final String description = kit.getDescription();
            final String kitPermission = kit.getPermissionNode();
            ArrayList lore;
            if (kitPermission == null || player.hasPermission(kitPermission)) {
                lore = new ArrayList();
                if (kit.isEnabled()) {
                    if (kit.getDelayMillis() > 0L) {
                        lore.add(ChatColor.YELLOW + kit.getDelayWords() + " cooldown");
                    }
                }
                else {
                    lore.add(ChatColor.RED + "Disabled");
                }
                final int maxUses;
                if ((maxUses = kit.getMaximumUses()) != Integer.MAX_VALUE) {
                    lore.add(ChatColor.YELLOW + "Used " + this.plugin.getUserManager().getUser(uuid).getKitUses(kit) + '/' + maxUses + " times.");
                }
                if (description != null) {
                    lore.add(" ");
                    for (final String part : ChatPaginator.wordWrap(description, 24)) {
                        lore.add(ChatColor.WHITE + part);
                    }
                }
            }
            else {
                lore = Lists.newArrayList((Object[])new String[] { ChatColor.RED + "You do not own this kit." });
            }
            final ItemStack cloned = stack.clone();
            final ItemMeta meta = cloned.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + kit.getName());
            meta.setLore((List)lore);
            cloned.setItemMeta(meta);
            if(ConfigurationService.KIT_MAP) {
                inventory.addItem(new ItemStack[] { cloned });
            } else {
            	inventory.setItem(kit.getSlot() - 1, cloned);
            }
        }
        return inventory;
    }
    
    @Override
    public void reloadKitData() {
        this.config = new Config(this.plugin, "kits");
        final Object object = this.config.get("kits");
        if (object instanceof List) {
            this.kits = GenericUtils.createList(object, Kit.class);
            for (final Kit kit : this.kits) {
                this.kitNameMap.put(kit.getName(), kit);
                this.kitUUIDMap.put(kit.getUniqueID(), kit);
            }
        }
    }
    
    @Override
    public void saveKitData() {
        this.config.set("kits", this.kits);
        this.config.save();
    }
}

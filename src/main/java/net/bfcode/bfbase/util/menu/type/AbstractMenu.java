package net.bfcode.bfbase.util.menu.type;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.bfcode.bfbase.util.menu.ArrayIterator;
import net.bfcode.bfbase.util.menu.Menu;
import net.bfcode.bfbase.util.menu.slot.DefaultSlot;
import net.bfcode.bfbase.util.menu.slot.Slot;

public abstract class AbstractMenu implements Menu
{
    private final Inventory inventory;
    private Menu parent;
    private Slot[] slots;
    private CloseHandler handler;
    
    protected AbstractMenu(String title, final int slots, final Menu parent) {
        if (title == null) {
            title = InventoryType.CHEST.getDefaultTitle();
        }
        this.inventory = Bukkit.createInventory((InventoryHolder)this, slots, title);
        this.parent = parent;
        this.generateSlots();
    }
    
    protected AbstractMenu(String title, final InventoryType type, final Menu parent) {
        Objects.requireNonNull(type, "type cannot be null");
        if (title == null) {
            title = type.getDefaultTitle();
        }
        this.inventory = Bukkit.createInventory((InventoryHolder)this, type, title);
        this.parent = parent;
        this.generateSlots();
    }
    
    protected void generateSlots() {
        this.slots = new Slot[this.inventory.getSize()];
        for (int i = 0; i < this.slots.length; ++i) {
            this.slots[i] = new DefaultSlot(this.inventory, i);
        }
    }
    
    @Override
    public Optional<Menu> getParent() {
        return Optional.ofNullable(this.parent);
    }
    
    @Override
    public void open(final Player viewer) {
        viewer.openInventory(this.getInventory());
    }
    
    @Override
    public void close(final Player viewer) {
        final Inventory inv = this.getInventory();
        if (!inv.getViewers().contains(viewer)) {
            throw new IllegalStateException("menu not open for player");
        }
        viewer.closeInventory();
    }
    
    @Override
    public Slot getSlot(final int index) {
        return this.slots[index];
    }
    
    public Iterator<Slot> iterator() {
        return new ArrayIterator<Slot>(this.slots);
    }
    
    @Override
    public void clear() {
        for (final Slot slot : this.slots) {
            slot.setItem(null);
        }
    }
    
    @Override
    public void clear(final int index) {
        final Slot slot = this.getSlot(index);
        slot.setItem(null);
    }
    
    public Inventory getInventory() {
        return this.inventory;
    }
    
    @Override
    public Optional<CloseHandler> getCloseHandler() {
        return Optional.ofNullable(this.handler);
    }
    
    @Override
    public void setCloseHandler(final CloseHandler handler) {
        this.handler = handler;
    }
    
    public abstract static class Builder implements Menu.Builder
    {
        private String title;
        private Menu parent;
        
        @Override
        public Menu.Builder title(final String title) {
            this.title = title;
            return this;
        }
        
        @Override
        public Menu.Builder parent(final Menu parent) {
            this.parent = parent;
            return this;
        }
        
        public String getTitle() {
            return this.title;
        }
        
        public Menu getParent() {
            return this.parent;
        }
    }
}

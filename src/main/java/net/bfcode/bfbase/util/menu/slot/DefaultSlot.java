package net.bfcode.bfbase.util.menu.slot;

import java.util.Objects;
import java.util.Optional;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DefaultSlot implements Slot
{
    private final Inventory handle;
    private final int index;
    private ClickOptions options;
    private ClickHandler handler;
    
    public DefaultSlot(final Inventory handle, final int index) {
        this(handle, index, ClickOptions.DENY_ALL);
    }
    
    public DefaultSlot(final Inventory handle, final int index, final ClickOptions options) {
        Objects.requireNonNull(handle);
        this.handle = handle;
        this.index = index;
        this.setClickOptions(options);
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public ClickOptions getClickOptions() {
        return this.options;
    }
    
    @Override
    public void setClickOptions(final ClickOptions options) {
        Objects.requireNonNull(options);
        this.options = options;
    }
    
    @Override
    public ItemStack getItem() {
        return this.handle.getItem(this.index);
    }
    
    @Override
    public void setItem(final ItemStack item) {
        this.handle.setItem(this.index, item);
    }
    
    @Override
    public Optional<ClickHandler> getClickHandler() {
        return Optional.ofNullable(this.handler);
    }
    
    @Override
    public void setClickHandler(final ClickHandler handler) {
        this.handler = handler;
    }
}

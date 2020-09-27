package net.bfcode.bfbase.util.menu;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import net.bfcode.bfbase.util.menu.slot.Slot;

public interface Menu extends InventoryHolder, Iterable<Slot>
{
    Optional<Menu> getParent();
    
    void open(final Player p0);
    
    void close(final Player p0) throws IllegalStateException;
    
    Slot getSlot(final int p0);
    
    void clear();
    
    void clear(final int p0);
    
    Dimension getDimensions();
    
    Optional<CloseHandler> getCloseHandler();
    
    void setCloseHandler(final CloseHandler p0);
    
    public static class Dimension
    {
        private final int rows;
        private final int columns;
        
        public Dimension(final int rows, final int columns) {
            this.rows = rows;
            this.columns = columns;
        }
        
        public int getRows() {
            return this.rows;
        }
        
        public int getColumns() {
            return this.columns;
        }
    }
    
    public interface Builder
    {
        Builder title(final String p0);
        
        Builder parent(final Menu p0);
        
        Menu build();
    }
    
    @FunctionalInterface
    public interface CloseHandler
    {
        void close(final Player p0, final Menu p1);
    }
}

package net.bfcode.bfbase.util.menu.type;

import org.bukkit.event.inventory.InventoryType;

import net.bfcode.bfbase.util.menu.Menu;

public class BoxMenu extends AbstractMenu
{
    protected BoxMenu(final String title, final InventoryType type, final Menu menu) {
        super(title, type, menu);
    }
    
    public static Builder builder(final InventoryType type) {
        switch (type) {
            case WORKBENCH:
            case DISPENSER:
            case DROPPER: {
                return new Builder(type);
            }
            default: {
                throw new IllegalArgumentException("box menu must have a 3x3 inventory type");
            }
        }
    }
    
    @Override
    public Menu.Dimension getDimensions() {
        return new Menu.Dimension(3, 3);
    }
    
    public static class Builder extends AbstractMenu.Builder
    {
        private InventoryType type;
        
        Builder(final InventoryType type) {
            this.type = type;
        }
        
        @Override
        public BoxMenu build() {
            return new BoxMenu(this.getTitle(), this.type, this.getParent());
        }
    }
}

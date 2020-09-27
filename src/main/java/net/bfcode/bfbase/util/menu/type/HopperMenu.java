package net.bfcode.bfbase.util.menu.type;

import org.bukkit.event.inventory.InventoryType;

import net.bfcode.bfbase.util.menu.Menu;

public class HopperMenu extends AbstractMenu
{
    protected HopperMenu(final String title, final Menu menu) {
        super(title, InventoryType.HOPPER, menu);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    @Override
    public Menu.Dimension getDimensions() {
        return new Menu.Dimension(1, 5);
    }
    
    public static class Builder extends AbstractMenu.Builder
    {
        @Override
        public HopperMenu build() {
            return new HopperMenu(this.getTitle(), this.getParent());
        }
    }
}

package net.bfcode.bfbase.util.menu.type;

import net.bfcode.bfbase.util.menu.Menu;

public class ChestMenu extends AbstractMenu
{
    protected ChestMenu(final String title, final int slots, final Menu menu) {
        super(title, slots, menu);
    }
    
    public static Builder builder(final int rows) {
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("invalid row count");
        }
        return new Builder(rows * 9);
    }
    
    @Override
    public Menu.Dimension getDimensions() {
        return new Menu.Dimension(this.getInventory().getSize() / 9, 9);
    }
    
    public static class Builder extends AbstractMenu.Builder
    {
        private int size;
        
        Builder(final int size) {
            this.size = size;
        }
        
        @Override
        public ChestMenu build() {
            return new ChestMenu(this.getTitle(), this.size, this.getParent());
        }
    }
}

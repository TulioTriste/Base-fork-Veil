package net.bfcode.bfbase.util.menu;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.bfcode.bfbase.util.menu.mask.Mask;
import net.bfcode.bfbase.util.menu.mask.Mask2D;
import net.bfcode.bfbase.util.menu.slot.Slot;
import net.bfcode.bfbase.util.menu.type.ChestMenu;

public class MenuUtils
{
    public static Menu createMenu(final String title, final int rows) {
        return ChestMenu.builder(rows).title(title).build();
    }
    
    public static void addClickHandler(final Slot slot) {
        slot.setClickHandler((player, info) -> player.sendMessage("You clicked the slot at index " + info.getClickedSlot().getIndex()));
    }
    
    public static void addMaterialBorder(final Inventory inventory, final Menu menu, final Material material, final int amount) {
        final ItemStack glass = new ItemStack(material, amount);
        final Mask mask = Mask2D.builder(menu).apply("111111111").nextRow().apply("100000001").nextRow().apply("100000001").nextRow().apply("111111111").build();
        for (final int slot : mask) {
            menu.getSlot(slot).setItem(glass);
        }
    }
    
    public static void addBorder(final Inventory inventory, final Menu menu, final Material material) {
        final ItemStack glass = new ItemStack(material);
        for (int i = 0; i < 9; ++i) {
            menu.getSlot(i).setItem(glass);
        }
        menu.getSlot(17).setItem(glass);
        menu.getSlot(18).setItem(glass);
        for (int i = 26; i < 36; ++i) {
            menu.getSlot(i).setItem(glass);
        }
    }
}
